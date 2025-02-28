package com.overridetech.funpay_monitor.client.api;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    @Value(value = "${binance.currency-rate-url}")
    private String binanceCurrencyPriceUrl;

    private final RestClient restClient = RestClient.builder().build();

    @Cacheable(cacheNames = "currency-rate")
    public CurrencyRateDto getCurrencyRates() {
        Map responseBody = restClient.get()
                .uri(binanceCurrencyPriceUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                            throw new IllegalArgumentException("Failed to fetch currency rate.\n" + errorBody);
                        })
                .body(Map.class);

        return computeCurrencyRates(responseBody);
    }

    private CurrencyRateDto computeCurrencyRates(Map responseBody) {
        CurrencyRateDto currencyRateDto = new CurrencyRateDto();
        BigDecimal usdRub = new BigDecimal((String) Objects.requireNonNull(responseBody).get("price"))
                .round(new MathContext(5, RoundingMode.HALF_UP));
        BigDecimal rubUsd = BigDecimal.ONE.divide(usdRub, 5, RoundingMode.HALF_UP);

        currencyRateDto.setUsdrub(usdRub);
        currencyRateDto.setRubusd(rubUsd);
        return currencyRateDto;
    }

}

package com.overridetech.funpay_monitor.client.api;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    private static final String BINANCE_SYMBOL_PRICE_BASE_URL = "https://api.binance.com";
    private final RestClient restClient = RestClient.builder().baseUrl(BINANCE_SYMBOL_PRICE_BASE_URL).build();


    @Cacheable(cacheNames = "currency-rate", key = "#symbol")
    public CurrencyRateDto getCurrencyRates(String symbol) {
        return restClient.get()
                .uri("/api/v3/ticker/price?symbol=" + symbol)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            String errorBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                            throw new IllegalArgumentException("Failed to fetch currency rate for symbol:" + symbol + "\n" + errorBody);
                        })
                .body(CurrencyRateDto.class);
    }

}

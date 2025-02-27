package com.overridetech.funpay_monitor.client.api;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BinanceClient {

    private static final String BINANCE_SYMBOL_PRICE_BASE_URL = "https://api.binance.com";
    private final RestClient restClient = RestClient.builder().baseUrl(BINANCE_SYMBOL_PRICE_BASE_URL).build();

    public CurrencyRateDto getCurrencyRates(String symbol) {
        try {
            return restClient.get()
                    .uri("/api/v3/ticker/price?symbol=" + symbol)
                    .header("Accept", "application/json")
                    .retrieve()
                    .body(CurrencyRateDto.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to fetch currency rate for symbol:" + symbol, e);
        }
    }

}

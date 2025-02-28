package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.api.BinanceClient;
import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final BinanceClient binanceClient;

    @SneakyThrows
    public CurrencyRateDto getCurrencyRatesFor() {
        return binanceClient.getCurrencyRates();
    }

}

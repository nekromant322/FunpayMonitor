package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.api.BinanceClient;
import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import com.overridetech.funpay_monitor.selenium.FunpayParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final BinanceClient binanceClient;
    private final FunpayParser funpayParser;

    @SneakyThrows
    public CurrencyRateDto getCurrencyRates() {
        return binanceClient.getCurrencyRates();
    }

    public CurrencyRateDto getCurrencyRatesWithSelenium() {
        return funpayParser.getExcangeRate();
    }
}

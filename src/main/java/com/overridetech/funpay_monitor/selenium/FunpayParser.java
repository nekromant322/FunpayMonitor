package com.overridetech.funpay_monitor.selenium;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import com.overridetech.funpay_monitor.selenium.page.FunpayPOM;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FunpayParser {

    private final FunpayPOM funpayPOM;


    @Cacheable(cacheNames = "currency-rate-funpay")
    public CurrencyRateDto getExcangeRate() {
        try {
            Double priceInRub = funpayPOM.getBasePage().getPrice("₽");
            Double priceInUsd = funpayPOM.openNavBarCurrencyWidget().switchCurrencyToUSD().getPrice("$");
            Double usdRub = priceInRub / priceInUsd;
            Double rubUsd = priceInUsd / priceInRub;

            CurrencyRateDto currencyRateDto = new CurrencyRateDto();
            currencyRateDto.setUsdrub(usdRub);
            currencyRateDto.setRubusd(rubUsd);
            return currencyRateDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            funpayPOM.closeWebDriver();
        }
    }

}

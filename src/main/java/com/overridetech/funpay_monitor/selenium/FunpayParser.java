package com.overridetech.funpay_monitor.selenium;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import com.overridetech.funpay_monitor.selenium.page.FunpayPOM;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class FunpayParser {

    private final FunpayPOM funpayPOM;


    @Cacheable(cacheNames = "currency-rate-funpay")
    public CurrencyRateDto getExcangeRate() {
        try {
            BigDecimal priceInRub = funpayPOM.getBasePage().getPrice("₽");
            BigDecimal priceInUsd = funpayPOM.openNavBarCurrencyWidget().switchCurrencyToUSD().getPrice("$");
            BigDecimal usdRub = priceInRub.divide(priceInUsd, 2, RoundingMode.HALF_UP);
            BigDecimal rubUsd = priceInUsd.divide(priceInRub, 2, RoundingMode.HALF_UP);

            CurrencyRateDto currencyRateDto = new CurrencyRateDto();
            currencyRateDto.setUsdrub(usdRub);
            currencyRateDto.setRubusd(rubUsd);
            return currencyRateDto;
        } finally {
            funpayPOM.closeWebDriver();
        }
    }

}

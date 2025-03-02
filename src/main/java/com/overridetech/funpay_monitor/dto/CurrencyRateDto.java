package com.overridetech.funpay_monitor.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class CurrencyRateDto {
    private BigDecimal usdrub;
    private BigDecimal rubusd;
}

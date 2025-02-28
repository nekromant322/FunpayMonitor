package com.overridetech.funpay_monitor.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class CurrencyRateDto {
    private String symbol;
    private BigDecimal price;
}

package com.overridetech.funpay_monitor.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CurrencyRateDto {
    private String symbol;
    private String price;
}

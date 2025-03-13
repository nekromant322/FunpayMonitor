package com.overridetech.funpay_monitor.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto  {
    String name;
    String urlForScrap;
    String urlForExport;
    String ServerKeyWord;
    String TcSideKeyWord;
}
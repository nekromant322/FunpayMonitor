package com.overridetech.funpay_monitor.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseOffer implements Offer {
    private String ref;
    private String category;
    private String item;
    private String price;
    private String stock;
    private String seller;
    private Boolean isOnline;
    private String server;
    private String rating;
    private String reviews;
    private String experience;

    private LocalDateTime time;
}

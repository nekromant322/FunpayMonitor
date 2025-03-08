package com.overridetech.funpay_monitor.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterArgDto {

    @NotEmpty
    private String category;
    private Long minStock;
    private Long maxStock;
    private Double minPrice;
    private Double maxPrice;
    private Boolean checkForOnline;

}

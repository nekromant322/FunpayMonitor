package com.overridetech.funpay_monitor.mapper;

import com.overridetech.funpay_monitor.dto.FilterArgDto;
import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;

public class FilterArgMapper {

    public static FilterArgDto mapEntityToDto(BaseFilterArgTable entity) {
        return new FilterArgDto(
                entity.getCategory(),
                entity.getMinStock(),
                entity.getMaxStock(),
                entity.getMinPrice(),
                entity.getMaxPrice(),
                entity.getCheckForOnline()
        );
    }
}

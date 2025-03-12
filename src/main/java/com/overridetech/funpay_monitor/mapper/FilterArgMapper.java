package com.overridetech.funpay_monitor.mapper;

import com.overridetech.funpay_monitor.dto.FilterArgDto;
import com.overridetech.funpay_monitor.entity.BaseFilterArgTable;
import org.springframework.stereotype.Component;

@Component
public class FilterArgMapper {

    public FilterArgDto mapEntityToDto(BaseFilterArgTable entity) {
        return new FilterArgDto(
                entity.getCategory(),
                entity.getMinStock(),
                entity.getMaxStock(),
                entity.getMinPrice(),
                entity.getMaxPrice(),
                entity.getCheckForOnline()
        );
    }

    public void updateEntity(BaseFilterArgTable table, FilterArgDto filterArgDto) {
        table.setCategory(filterArgDto.getCategory());
        table.setMinStock(filterArgDto.getMinStock());
        table.setMaxStock(filterArgDto.getMaxStock());
        table.setMinPrice(filterArgDto.getMinPrice());
        table.setMaxPrice(filterArgDto.getMaxPrice());
        table.setCheckForOnline(filterArgDto.getCheckForOnline());
    }
}

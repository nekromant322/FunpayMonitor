package com.overridetech.funpay_monitor.mapper;

import com.overridetech.funpay_monitor.dto.CategoryDto;
import com.overridetech.funpay_monitor.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto mapEntityToDto(Category entity) {
        return new CategoryDto(
                entity.getName(),
                entity.getUrlForScrap(),
                entity.getUrlForExport(),
                entity.getServerKeyWord(),
                entity.getTcSideKeyWord()
        );
    }

    public Category mapDtoToEntity(CategoryDto dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity.setUrlForScrap(dto.getUrlForScrap());
        entity.setUrlForExport(dto.getUrlForExport());
        entity.setServerKeyWord(dto.getServerKeyWord());
        entity.setTcSideKeyWord(dto.getTcSideKeyWord());
        return entity;
    }
}

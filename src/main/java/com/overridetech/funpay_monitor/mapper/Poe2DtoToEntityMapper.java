package com.overridetech.funpay_monitor.mapper;

import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.entity.Poe2Offer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Poe2DtoToEntityMapper {
    public Poe2Offer mapDtoToEntity(FunPayPoe2Offer dto) {
        Poe2Offer entity = new Poe2Offer();

        entity.setItem(dto.getItem());
        entity.setRating(dto.getRating());
        entity.setOnline(dto.getIsOnline());
        entity.setRef(dto.getRef());
        entity.setSeller(dto.getSeller());
        entity.setTime(dto.getTime());
        entity.setExperience(dto.getExperience());
        entity.setLeague(dto.getServer());

        try {
            entity.setPrice(new BigDecimal(dto.getPrice()));
            entity.setStock(Long.valueOf(dto.getStock()));
        } catch (Exception e) {
            entity.setPrice(BigDecimal.ZERO);
            entity.setStock(0L);
        }

        return entity;
    }
}

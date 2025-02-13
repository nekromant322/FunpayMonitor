package com.overridetech.funpay_monitor.mapper;

import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.entity.Poe2DivineOffer;

import java.math.BigDecimal;

public class Poe2DtoToEntityMapper {
    public static Poe2DivineOffer mapDtoToEntity(FunPayPoe2Offer dto) {
        Poe2DivineOffer entity = new Poe2DivineOffer();

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

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
            entity.setPrice(Double.parseDouble(dto.getPrice()));
            entity.setStock(Long.valueOf(dto.getStock()));
        } catch (Exception e) {
            entity.setPrice(Double.NaN);
            entity.setStock(0L);
        }

        return entity;
    }

    public static FunPayPoe2Offer mapEntityToDto(Poe2DivineOffer entity) {
        FunPayPoe2Offer dto = new FunPayPoe2Offer();

        dto.setRef(entity.getRef());
        dto.setSeller(entity.getSeller());
        dto.setIsOnline(entity.getOnline());
        dto.setServer(entity.getLeague());
        dto.setRating(entity.getRating());
        dto.setExperience(entity.getExperience());
        dto.setTime(entity.getTime());
        dto.setPrice(entity.getPrice().toString());
        dto.setStock(String.valueOf(entity.getStock()));

        return dto;
    }

}

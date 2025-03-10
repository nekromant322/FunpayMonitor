package com.overridetech.funpay_monitor.service;

import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.entity.Poe2DivineOffer;
import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetManipulation;
import com.overridetech.funpay_monitor.mapper.Poe2DtoToEntityMapper;
import com.overridetech.funpay_monitor.repository.Poe2DivineOfferRepository;
import com.overridetech.funpay_monitor.util.filter.OutliersFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleSheetService {

    private final GoogleSheetManipulation googleSheet;
    private final Poe2DivineOfferRepository poe2DivineOfferRepository;
    private final OutliersFilter<FunPayPoe2Offer> filter;

    //todo  добавить в таблицу спаршенных данных, колонку категория,
    //      создать таблицу -  название категория, id googletable
    // связать их как 1 к 1
    public void refreshAvgPrice(String tableId, Duration duration) {
        LocalDateTime after = LocalDateTime.now().minus(duration);
        List<Poe2DivineOffer> offers = poe2DivineOfferRepository.findByTimeAfter(after);
        String range = "Sheet1!A2:B";
        List<List<Object>> dataSet = List.of(List.of(getAvgPrice(offers), LocalDateTime.now().toString()));
        googleSheet.append(tableId, dataSet, range);
    }


    private Double getAvgPrice(List<Poe2DivineOffer> offers) {
        List<FunPayPoe2Offer> dtos = offers.stream()
                .map(Poe2DtoToEntityMapper::mapEntityToDto)
                .toList();
        dtos = filter.trimDataSet(dtos, 0.1, 0.5, 20d);
        return dtos.
                stream().
                collect(Collectors.averagingDouble(n -> Double.parseDouble(n.getPrice())));


    }

    public Permission grantPermission(String mail, String tableId) {
        return googleSheet.grantPermission(mail, tableId);
    }

    public String createGoogleSheet(String tableName) {
        return googleSheet.createGoogleSheet(tableName);
    }
}

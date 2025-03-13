package com.overridetech.funpay_monitor.service;

import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.dto.BaseOfferDto;
import com.overridetech.funpay_monitor.entity.Offer;
import com.overridetech.funpay_monitor.integration.googlesheet.Exportable;
import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetManipulation;
import com.overridetech.funpay_monitor.mapper.OfferDtoToEntityMapper;
import com.overridetech.funpay_monitor.repository.OfferRepository;
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
    private final OfferRepository poe2DivineOfferRepository;
    private final OutliersFilter<BaseOfferDto> filter;


    public void refreshAvgPrice(String tableId, Duration duration) {
        LocalDateTime after = LocalDateTime.now().minus(duration);
        List<Offer> offers = poe2DivineOfferRepository.findByTimeAfter(after);
        String range = "Sheet1!A2:B";
        List<List<Object>> dataSet = List.of(List.of(getAvgPrice(offers), LocalDateTime.now().toString()));
        googleSheet.append(tableId, dataSet, range);
    }

    public void refreshDto(String tableId, Exportable dto) {
        googleSheet.refreshData(tableId, List.of(dto));
    }


    private Double getAvgPrice(List<Offer> offers) {
        List<BaseOfferDto> dtos = offers.stream()
                .map(OfferDtoToEntityMapper::mapEntityToDto)
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

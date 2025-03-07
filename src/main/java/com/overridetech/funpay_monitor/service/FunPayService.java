package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.FunPayClient;
import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.entity.Poe2DivineOffer;
import com.overridetech.funpay_monitor.mapper.Poe2DtoToEntityMapper;
import com.overridetech.funpay_monitor.parser.FunPayPoe2Parser;
import com.overridetech.funpay_monitor.repository.Poe2DivineOfferRepository;
import com.overridetech.funpay_monitor.util.filter.OutliersFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunPayService {

    private final FunPayClient client;
    private final Poe2DivineOfferRepository repository;
    private final OutliersFilter<FunPayPoe2Offer> outliersFilter;

    public void scrapDivineOffers() {
        try {
            LocalDateTime time = LocalDateTime.now();
            var htmls = client.getHtmls();

            List<FunPayPoe2Offer> dtos = htmls.stream()
                    .map(FunPayPoe2Parser::parseHtmlToFunPayPoe2Offer)
                    .filter(Objects::nonNull)
                    .filter(dto -> dto.getItem().toLowerCase().contains("божеств"))
                    .peek(dto -> dto.setTime(time))
                    .toList();

            dtos = outliersFilter.trimDataSet(dtos, 0.1, 0.5, 20d);

            var offers = dtos.stream()
                    .filter(Objects::nonNull)
                    .map(Poe2DtoToEntityMapper::mapDtoToEntity)
                    .toList();

            repository.saveAll(offers);

        } catch (Exception e) {
            log.error("что-то обосралось при скрапе: {}", e.getMessage());
        }
    }

    //todo  добавить в таблицу спаршенных данных, колонку категория,
    //      создать таблицу -  название категория, id googletable
    // связать их как 1 к 1
    public List<List<Object>> refreshAvgPrice(Duration duration) {
        LocalDateTime after = LocalDateTime.now().minus(duration);
        List<Poe2DivineOffer> offers = repository.findByTimeAfter(after);
        return List.of(List.of(getAvgPrice(offers), LocalDateTime.now().toString()));
    }


    private Double getAvgPrice(List<Poe2DivineOffer> offers) {
        List<FunPayPoe2Offer> dtos = offers.stream()
                .map(Poe2DtoToEntityMapper::mapEntityToDto)
                .toList();
        dtos = outliersFilter.trimDataSet(dtos, 0.1, 0.5, 20d);
        return dtos.stream()
                .collect(Collectors.averagingDouble(n -> Double.parseDouble(n.getPrice())));
    }


}

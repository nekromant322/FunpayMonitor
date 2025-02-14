package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.FunPayClient;
import com.overridetech.funpay_monitor.mapper.Poe2DtoToEntityMapper;
import com.overridetech.funpay_monitor.parser.FunPayPoe2Parser;
import com.overridetech.funpay_monitor.repository.Poe2DivineOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunPayService {

    private final FunPayClient client;
    private final Poe2DivineOfferRepository repository;

    public void scrapDivineOffers() {
        try {
            LocalDateTime time = LocalDateTime.now();
            var htmls = client.getHtmls();

            var dtos = htmls.stream()
                    .map(FunPayPoe2Parser::parseHtmlToFunPayPoe2Offer)
                    .filter(Objects::nonNull)
                    .filter(dto -> dto.getItem().toLowerCase().contains("божеств"))
                    .peek(dto -> dto.setTime(time))
                    .toList();

            var offers = dtos.stream()
                    .filter(Objects::nonNull)
                    .map(Poe2DtoToEntityMapper::mapDtoToEntity)
                    .toList();

            repository.saveAll(offers);

        } catch (Exception e) {
            log.error("что-то обосралось при скрапе: {}", e.getMessage());
        }
    }


}

package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.FunPayClient;
import com.overridetech.funpay_monitor.dto.BaseOffer;
import com.overridetech.funpay_monitor.entity.Category;
import com.overridetech.funpay_monitor.mapper.Poe2DtoToEntityMapper;
import com.overridetech.funpay_monitor.parser.FunPayPoe2Parser;
import com.overridetech.funpay_monitor.repository.CategoryRepository;
import com.overridetech.funpay_monitor.repository.Poe2DivineOfferRepository;
import com.overridetech.funpay_monitor.util.filter.OutliersFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunPayService {

    private final FunPayClient client;
    private final Poe2DivineOfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final OutliersFilter<BaseOffer> outliersFilter;
    private final FunPayPoe2Parser funPayPoe2Parser;


    public void scrapOffers() {

        Category category = categoryRepository.findByName("POE 2 divine").orElseThrow();
        try {
            LocalDateTime time = LocalDateTime.now();
            var htmls = client.getHtmls(category.getUrlForScrap());

            List<BaseOffer> dtos = htmls.stream()
                    .map(html -> funPayPoe2Parser.parseHtmlToFunPayPoe2Offer(html, category.getName()))
                    .filter(Objects::nonNull)
                    .filter(dto -> dto.getItem().toLowerCase().contains("божеств"))
                    .peek(dto -> dto.setTime(time))
                    .toList();

            dtos = outliersFilter.trimDataSet(dtos, 0.1, 0.5, 20d);

            var offers = dtos.stream()
                    .filter(Objects::nonNull)
                    .map(Poe2DtoToEntityMapper::mapDtoToEntity)
                    .toList();

            offerRepository.saveAll(offers);

        } catch (Exception e) {
            log.error("что-то обосралось при скрапе: {}", e.getMessage());
        }
    }


}

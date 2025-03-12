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
import java.util.ArrayList;
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


    /** Парсит данные с фанпея и заносит их в бд, отсекая анномальные значения
     *
     * @param categoriesName принимает список названий категории по которым нужно собрать данные с фанпея
     *                       Если не передать параметры, то спарсит по всем категориям
     */
    public void scrapOffers(String... categoriesName) {
        List<Category> categories = categoryRepository.findByNameIn(List.of(categoriesName));
        if (categories.isEmpty()) {
            categories = categoryRepository.findAll();
        }
        List<BaseOffer> dtos = parseDto(categories);
        dtos = outliersFilter.trimDataSet(dtos, 0.1, 0.5, 20d);

        var offers = dtos.stream()
                .filter(Objects::nonNull)
                .map(Poe2DtoToEntityMapper::mapDtoToEntity)
                .toList();

        offerRepository.saveAll(offers);
    }


    private List<BaseOffer> parseDto(List<Category> categories) {
        List<BaseOffer> dtos = new ArrayList<>();
        for (Category category : categories) {
            try {
                LocalDateTime time = LocalDateTime.now();
                var htmls = client.getHtmls(category.getUrlForScrap());

                dtos.addAll(htmls.stream()
                        .map(html -> funPayPoe2Parser.parseHtmlToFunPayPoe2Offer(html, category.getName()))
                        .filter(Objects::nonNull)
                        .filter(dto -> dto.getItem().toLowerCase().contains(category.getTcSideKeyWord())
                                && dto.getServer().toLowerCase().contains(category.getServerKeyWord()))
                        .peek(dto -> dto.setTime(time))
                        .toList());
            } catch (Exception e) {
                log.error("При скрапе категории {} что-то обосралось при скрапе: {}", category.getName(), e.getMessage());
            }
        }
        return dtos;
    }
}

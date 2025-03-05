package com.overridetech.funpay_monitor.service;

import com.overridetech.funpay_monitor.client.FunPayClient;
import com.overridetech.funpay_monitor.dto.DataFilter;
import com.overridetech.funpay_monitor.dto.FunPayPoe2Offer;
import com.overridetech.funpay_monitor.entity.Poe2Offer;
import com.overridetech.funpay_monitor.mapper.Poe2DtoToEntityMapper;
import com.overridetech.funpay_monitor.parser.FunPayPoe2Parser;
import com.overridetech.funpay_monitor.repository.Poe2OfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunPayService {

    private final FunPayClient client;
    private final Poe2OfferRepository offerRepository;
    private final FunPayPoe2Parser parser;
    private final Poe2DtoToEntityMapper poe2DtoToEntityMapper;


    public void scrapOffers() {
        try {
            LocalDateTime time = LocalDateTime.now();
            var htmls = client.getHtmls();

            List<FunPayPoe2Offer> dtos = htmls.stream()
                    .map(parser::parseHtmlToFunPayPoe2Offer)
                    .filter(Objects::nonNull)
                    .peek(dto -> dto.setTime(time))
                    .toList();

            List<Poe2Offer> offers = dtos.stream()
                    .filter(Objects::nonNull)
                    .map(poe2DtoToEntityMapper::mapDtoToEntity)
                    .toList();

            savePoe2Offers(offers);

        } catch (Exception e) {
            log.error("что-то обосралось при скрапе: {}", e.getMessage());
        }
    }

    public void savePoe2Offers(List<Poe2Offer> offers) {
        offerRepository.saveAll(offers);
    }

    @Transactional
    public Map<LocalDateTime, BigDecimal> getResultWithFilter(DataFilter dataFilter) {
        if (dataFilter.getStartTime() != null
                && dataFilter.getEndTime() != null
                && dataFilter.getHigh() != null
                && dataFilter.getLow() != null
                && Strings.isNotBlank(dataFilter.getItem())
                && Strings.isNotBlank(dataFilter.getLeague())) {

            BigDecimal maxPrice = getMaxPriceByFilter(dataFilter);

            BigDecimal minPrice = getMinPriceByFilter(dataFilter);

            List<Poe2Offer> filteredOffers = getFilteredOffersWithPriceInRange(dataFilter,
                    minPrice, maxPrice);


            return getFilteredTimeSeriesWithPriceInRange(filteredOffers);

        }
        return new HashMap<>();
    }

    private List<Poe2Offer> getFilteredOffersWithPriceInRange(DataFilter dataFilter, BigDecimal minPrice, BigDecimal maxPrice) {
        return offerRepository.findByItemAndLeagueAndOnlineStatusAndTimeAfterAndTimeInRangeAndPriceInRange(
                dataFilter.getItem(),
                dataFilter.getLeague(),
                dataFilter.getOnline(),
                dataFilter.getStartTime(),
                dataFilter.getEndTime(),
                minPrice,
                maxPrice);
    }

    private Map<LocalDateTime, BigDecimal> getFilteredTimeSeriesWithPriceInRange(List<Poe2Offer> offers) {

        Map<LocalDateTime, List<Poe2Offer>> map = offers.stream()
                .collect(Collectors.groupingBy(Poe2Offer::getTime));

        Map<LocalDateTime, BigDecimal> unsortedResult = map.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                k -> calculateAveragePriceInTimeSeries(k.getValue())
        ));

        return new TreeMap<>(unsortedResult);
    }

    private BigDecimal calculateAveragePriceInTimeSeries(List<Poe2Offer> list) {
        BigDecimal sum = list.stream()
                .filter(Objects::nonNull)
                .map(Poe2Offer::getPrice)
                .filter(Objects::nonNull)
                .filter(p -> !BigDecimal.ZERO.equals(p))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(list.size()), RoundingMode.HALF_DOWN);
    }

    private BigDecimal getMinPriceByFilter(DataFilter dataFilter) {
        return offerRepository.findMinPriceByItemAndLeagueAndOnlineStatusInTimeRange(
                dataFilter.getItem(),
                dataFilter.getLeague(),
                dataFilter.getOnline(),
                dataFilter.getStartTime(),
                dataFilter.getEndTime()
        ).orElse(BigDecimal.ZERO).multiply(BigDecimal.ONE.subtract(dataFilter.getLow()));
    }

    private BigDecimal getMaxPriceByFilter(DataFilter dataFilter) {
        return offerRepository.findMaxPriceByItemAndLeagueAndOnlineStatusInTimeRange(
                dataFilter.getItem(),
                dataFilter.getLeague(),
                dataFilter.getOnline(),
                dataFilter.getStartTime(),
                dataFilter.getEndTime()
        ).orElse(BigDecimal.ZERO).multiply(BigDecimal.ONE.subtract(dataFilter.getHigh()));
    }

}

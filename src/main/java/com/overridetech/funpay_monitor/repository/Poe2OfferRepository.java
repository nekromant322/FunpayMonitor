package com.overridetech.funpay_monitor.repository;

import com.overridetech.funpay_monitor.entity.Poe2Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface Poe2OfferRepository extends JpaRepository<Poe2Offer, UUID> {

    @Query("SELECT o FROM Poe2Offer o WHERE o.item = :item " +
            "AND o.league = :league AND o.online = COALESCE(:online, o.online) " +
            "AND o.time BETWEEN :startDate AND :endDate " +
            "AND o.price >= :startPrice AND o.price <= :endPrice")
    List<Poe2Offer> findByItemAndLeagueAndOnlineStatusAndTimeAfterAndTimeInRangeAndPriceInRange(
            @Param("item") String item,
            @Param("league") String league,
            @Param("online") Boolean online,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("startPrice") BigDecimal startPrice,
            @Param("endPrice") BigDecimal endPrice
    );

    @Query("SELECT MAX(o.price) FROM Poe2Offer o WHERE o.item = :item " +
            "AND o.league = :league AND o.online = COALESCE(:online, o.online) " +
            "AND o.time BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> findMaxPriceByItemAndLeagueAndOnlineStatusInTimeRange(
            @Param("item") String item,
            @Param("league") String league,
            @Param("online") Boolean online,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT MIN(o.price) FROM Poe2Offer o WHERE o.item = :item " +
            "AND o.league = :league AND o.online = COALESCE(:online, o.online) " +
            "AND o.time BETWEEN :startDate AND :endDate")
    Optional<BigDecimal> findMinPriceByItemAndLeagueAndOnlineStatusInTimeRange(
            @Param("item") String item,
            @Param("league") String league,
            @Param("online") Boolean online,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}

package com.overridetech.funpay_monitor.repository;

import com.overridetech.funpay_monitor.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface Poe2DivineOfferRepository extends JpaRepository<Offer, UUID> {

    @Query("select p from Offer p where p.time > ?1")
    List<Offer> findByTimeAfter(LocalDateTime time);
}

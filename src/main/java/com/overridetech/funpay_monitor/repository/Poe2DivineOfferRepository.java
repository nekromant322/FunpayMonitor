package com.overridetech.funpay_monitor.repository;

import com.overridetech.funpay_monitor.entity.Poe2DivineOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface Poe2DivineOfferRepository extends JpaRepository<Poe2DivineOffer, UUID> {

    @Query("select p from Poe2DivineOffer p where p.time > ?1")
    List<Poe2DivineOffer> findByTimeAfter(LocalDateTime time);
}

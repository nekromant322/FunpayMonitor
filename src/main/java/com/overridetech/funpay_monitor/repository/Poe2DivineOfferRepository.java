package com.overridetech.funpay_monitor.repository;

import com.overridetech.funpay_monitor.entity.Poe2DivineOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Poe2DivineOfferRepository extends JpaRepository<Poe2DivineOffer, UUID> {
}

package com.overridetech.funpay_monitor.entity;

import com.overridetech.funpay_monitor.integration.googlesheet.Exportable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Poe2DivineOffer implements Exportable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private Double price;

    private Long stock;

    private String seller;

    private String ref;

    private String league;

    private Boolean online;

    private LocalDateTime time;

    private String rating;

    private String experience;

    @Override
    public List<Object> prepareToExport() {
        return List.of(uuid, price, stock, seller, ref, league, online, time, rating, experience);
    }
}

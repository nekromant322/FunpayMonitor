package com.overridetech.funpay_monitor.schedulers;


import com.overridetech.funpay_monitor.service.FunPayService;
import com.overridetech.funpay_monitor.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PoeSheduler {
    private final FunPayService funPayService;
    private final GoogleSheetService googleSheetService;

    @Value("${google-sheet.poe-divine-id}")
    private String id;

    @Scheduled(fixedRateString = "${app.scrapRate}")
    public void scarpDivineOffers() {

        funPayService.scrapOffers("WOW retail", "WOW classic Пламегор Альянс");
    }

    //todo    получать список всех id из бд и рефрешить каждую
    @Scheduled(cron = "0 0 * * * ?")
    public void refreshAvgPrice() {
        googleSheetService.refreshAvgPrice(id, Duration.ofHours(1));
    }


}

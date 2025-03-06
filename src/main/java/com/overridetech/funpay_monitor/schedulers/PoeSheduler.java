package com.overridetech.funpay_monitor.schedulers;


import com.overridetech.funpay_monitor.config.property.GoogleServiceProperties;
import com.overridetech.funpay_monitor.service.FunPayService;
import com.overridetech.funpay_monitor.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PoeSheduler {
    private final FunPayService funPayService;
    private final GoogleSheetService googleSheetService;
    private final GoogleServiceProperties googleServiceProperties;

    @Scheduled(fixedRateString = "${app.scrapRate}")
    public void scarpDivineOffers() {
       funPayService.scrapDivineOffers();
    }

//    получать список всех id из бд и рефрешить каждую
    @Scheduled(cron = "0 0 * * * ?")
    public void refreshAvgPrice(){
        String id = googleServiceProperties.getPoeDivineId();
        googleSheetService.refreshAvgPrice(id, Duration.ofHours(1));
    }


}

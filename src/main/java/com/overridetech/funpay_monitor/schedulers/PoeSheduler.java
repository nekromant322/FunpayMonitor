package com.overridetech.funpay_monitor.schedulers;


import com.overridetech.funpay_monitor.service.FunPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PoeSheduler {
    private final FunPayService funPayService;

    @Scheduled(fixedRateString = "${app.scrapRate}")
    public void scarpDivineOffers() {
        funPayService.scrapOffers();
    }

}

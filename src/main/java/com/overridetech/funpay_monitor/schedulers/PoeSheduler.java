package com.overridetech.funpay_monitor.schedulers;


import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetExport;
import com.overridetech.funpay_monitor.service.FunPayService;
import com.overridetech.funpay_monitor.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PoeSheduler {
    private final FunPayService funPayService;
    private final GoogleSheetService googleSheetService;

    @Value("${google-sheet.poe-divine-id}")
    private String id;

    @Scheduled(fixedRateString = "${app.scrapRate}")
    public void scarpDivineOffers() {
        funPayService.scrapDivineOffers();
    }

    //todo    получать список всех id из бд и рефрешить каждую
    @Scheduled(cron = "0 0 * * * ?")
    @GoogleSheetExport(spreadsheetId = "109VJFvRRgmaQfu_4C8y8G4Ro-ejKVkXLGX3dmFEGgoc", sheetName = "Sheet1")
    public List<List<Object>> refreshAvgPrice() {
        return funPayService.refreshAvgPrice(Duration.ofHours(1));
    }


}

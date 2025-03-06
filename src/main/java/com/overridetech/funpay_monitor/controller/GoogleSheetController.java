package com.overridetech.funpay_monitor.controller;


import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.config.property.GoogleServiceProperties;
import com.overridetech.funpay_monitor.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/google-sheet")
public class GoogleSheetController {

    private final GoogleSheetService googleSheetService;
    private final GoogleServiceProperties googleServiceProperties;

    @GetMapping("/table")
    public String googleSheet(@RequestParam String tableName)  {
        return googleSheetService.createGoogleSheet(tableName);

    }

    @GetMapping("/grantPermission")
    public Permission grantPermission(
            @RequestParam String mail,
            @RequestParam String tableId)  {
        return googleSheetService.grantPermission(mail, tableId);
    }

    @GetMapping("/refresh")
    public String refresh() throws IOException {
        String id = googleServiceProperties.getPoeDivineId();
        googleSheetService.refreshAvgPrice(id, Duration.ofHours(1));
        return "refreshed";
    }

}

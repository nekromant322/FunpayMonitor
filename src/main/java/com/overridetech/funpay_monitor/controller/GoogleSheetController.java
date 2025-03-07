package com.overridetech.funpay_monitor.controller;


import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.service.GoogleSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/google-sheet")
public class GoogleSheetController {

    private final GoogleSheetService googleSheetService;

    @GetMapping("/table")
    public String googleSheet(@RequestParam String tableName) {
        return googleSheetService.createGoogleSheet(tableName);

    }

    @GetMapping("/grantPermission")
    public Permission grantPermission(
            @RequestParam String mail,
            @RequestParam String tableId) {
        return googleSheetService.grantPermission(mail, tableId);
    }

    @GetMapping("/refresh")
    public String refresh(@Value("${google-sheet.poe-divine-id}") String id) {
        googleSheetService.refreshAvgPrice(id, Duration.ofHours(1));
        return "refreshed";
    }

}

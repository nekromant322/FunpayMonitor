package com.overridetech.funpay_monitor.controller;


import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetManipulation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/google-sheet")
public class GoogleSheetController {

    private final GoogleSheetManipulation googleSheetManipulation;

    @GetMapping("/google-sheet")
    public String googleSheet( @RequestParam String tableName) throws IOException {
        return googleSheetManipulation.createGoogleSheet(tableName);

    }

    @GetMapping("/grantPermission")
    public Permission grantPermission(
            @RequestParam String mail,
            @RequestParam String tableId) throws IOException {
        return googleSheetManipulation.grantPermission(mail, tableId);
    }


}

package com.overridetech.funpay_monitor.service;

import com.google.api.services.drive.model.Permission;
import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetManipulation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleSheetService {

    private final GoogleSheetManipulation googleSheet;



    public Permission grantPermission(String mail, String tableId) {
        return googleSheet.grantPermission(mail, tableId);
    }

    public String createGoogleSheet(String tableName) {
        return googleSheet.createGoogleSheet(tableName);
    }
}

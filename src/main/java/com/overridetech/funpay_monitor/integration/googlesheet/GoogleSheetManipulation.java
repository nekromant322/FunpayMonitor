package com.overridetech.funpay_monitor.integration.googlesheet;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleSheetManipulation {

    private final Sheets sheets;
    private final Drive drive;

    public String createGoogleSheet(String tableName) throws IOException {

        Spreadsheet spreadsheet = new Spreadsheet().setProperties(
                new SpreadsheetProperties().setTitle(tableName));
        Spreadsheet result = sheets.spreadsheets().
                create(spreadsheet).
                setFields("spreadsheetId").
                execute();
        log.warn("spreadSheet was created  with id: {}", result.getSpreadsheetId());

        return result.getSpreadsheetId();
    }


    public Permission grantPermission(String mail, String tableId) throws IOException {
        Permission permission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(mail);

        return drive.permissions().create(tableId, permission).execute();

    }
}

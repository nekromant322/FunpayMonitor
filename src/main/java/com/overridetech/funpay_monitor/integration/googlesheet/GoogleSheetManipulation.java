package com.overridetech.funpay_monitor.integration.googlesheet;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleSheetManipulation {

    private final Sheets sheets;
    private final Drive drive;

    private final static String START_CELL = "A2";
    private final static String RANGE_TO_CLEAR_WITHOUT_HEADERS = "Sheet1!A2:Z";


    public List<List<Object>> refreshData(String tableId, List<Exportable> dataSet) {
        List<List<Object>> values = dataSet.stream()
                .map(Exportable::prepareToExport)
                .collect(Collectors.toList());

        int numRows = values.size();
        int numCols = values.isEmpty() ? 0 : values.get(0).size();
        String range = calculateRange(numRows, numCols);

        clearSheet(tableId, RANGE_TO_CLEAR_WITHOUT_HEADERS);
        write(tableId, values, range);
        return get(tableId, range);
    }


    @SneakyThrows
    private List<List<Object>> get(String tableId, String range) {
        return sheets.spreadsheets().values()
                .get(tableId, range)
                .execute()
                .getValues();

    }

    @SneakyThrows
    private void write(String tableId, List<List<Object>> values, String range) {

        ValueRange body = new ValueRange().setValues(values);

        sheets.spreadsheets().values()
                .update(tableId, range, body)
                .setValueInputOption("RAW")
                .execute();
    }

    @SneakyThrows
    public void append(String tableId, List<List<Object>> values, String range) {

        ValueRange body = new ValueRange().setValues(values);

        sheets.spreadsheets().values()
                .append(tableId, range, body)
                .setValueInputOption("RAW")
                .setInsertDataOption("INSERT_ROWS")
                .execute();

    }

    @SneakyThrows
    private void clearSheet(String tableId, String range) {
        sheets.spreadsheets().values()
                .clear(tableId, range, new ClearValuesRequest())
                .execute();
    }


    private static String calculateRange(int numRows, int numCols) {
        char lastColumn = (char) (START_CELL.charAt(0) + numCols - 1);
        int lastRow = Character.getNumericValue(START_CELL.charAt(1)) + numRows - 1;
        return "Sheet1!" + START_CELL + ":" + lastColumn + lastRow;
    }

    @SneakyThrows
    public String createGoogleSheet(String tableName) {

        Spreadsheet spreadsheet = new Spreadsheet().setProperties(
                new SpreadsheetProperties().setTitle(tableName));
        Spreadsheet result = sheets.spreadsheets().
                create(spreadsheet).
                setFields("spreadsheetId").
                execute();
        log.warn("spreadSheet was created  with id: {}", result.getSpreadsheetId());

        return result.getSpreadsheetId();
    }

    @SneakyThrows
    public Permission grantPermission(String mail, String tableId) {
        Permission permission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(mail);

        return drive.permissions().create(tableId, permission).execute();

    }
}

package com.overridetech.funpay_monitor.integration.googlesheet;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GoogleSheetExport {
    String spreadsheetId();

    String sheetName();

    SheetAction action() default SheetAction.APPEND;
}

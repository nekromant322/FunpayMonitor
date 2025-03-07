package com.overridetech.funpay_monitor.integration;


import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetExport;
import com.overridetech.funpay_monitor.integration.googlesheet.GoogleSheetManipulation;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.openqa.selenium.InvalidArgumentException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aspect
@Component
@RequiredArgsConstructor
public class GoogleSheetExportAspect {

    private final GoogleSheetManipulation sheets;


    @Around(value = "@annotation(googleSheetExport)", argNames = "joinPoint,googleSheetExport")
    public void aroundScheduledMethod(ProceedingJoinPoint joinPoint, GoogleSheetExport googleSheetExport) throws Throwable {
        Object result = joinPoint.proceed();

        if (result instanceof List<?> list && !list.isEmpty()) {
            List<List<Object>> values = normalizeToListOfLists(list);
            switch (googleSheetExport.action()) {
                case REWRITE ->
                        sheets.refreshObject(googleSheetExport.spreadsheetId(), googleSheetExport.sheetName(), values);

                case APPEND ->
                        sheets.appendObject(googleSheetExport.spreadsheetId(), googleSheetExport.sheetName(), values);

            }

        } else {
            throw new InvalidArgumentException("На данный момент работает только со списками");
        }


    }

    private List<List<Object>> convertToValues(List<?> list) {
        Field[] fields = list.getFirst().getClass().getDeclaredFields();

        List<List<Object>> values = list.stream()
                .map(obj -> Stream.of(fields)
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                return field.get(obj);
                            } catch (IllegalAccessException e) {
                                return "Ошибка";
                            }
                        })
                        .collect(Collectors.toList())
                )
                .toList();
        return values;
    }

    private List<List<Object>> normalizeToListOfLists(List<?> list) {
        if (list.getFirst() instanceof List<?>) {
            return (List<List<Object>>) list;
        }
        return convertToValues(list);
    }

}

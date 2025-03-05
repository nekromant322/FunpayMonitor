package com.overridetech.funpay_monitor.controller;

import com.overridetech.funpay_monitor.dto.DataFilter;
import com.overridetech.funpay_monitor.service.FunPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {
    private final FunPayService funPayService;


    @PostMapping("/raw")
    public ResponseEntity<Map<LocalDateTime, BigDecimal>> getFilteredResult(@RequestBody DataFilter filter) {
        Map<LocalDateTime, BigDecimal> result = funPayService.getResultWithFilter(filter);
        return ResponseEntity.ok(result);
    }


}

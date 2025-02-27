package com.overridetech.funpay_monitor.controller;

import com.overridetech.funpay_monitor.dto.CurrencyRateDto;
import com.overridetech.funpay_monitor.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/currency")
public class CurrencyController {

    private final CurrencyService currencyService;


    /* Пример запроса
     * http://localhost:8080/api/v1/currency/rate?symbol=USDTRUB
     * Пример ответа
     * {
     *   "symbol": "USDTRUB",
     *   "price": "91.10000000"
     * }
     */
    @GetMapping("/rate")
    public CurrencyRateDto getCurrencyRateFor(@RequestParam String symbol) {
        return currencyService.getCurrencyRatesFor(symbol);
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleValidationExceptions(IllegalArgumentException ex) {
        log.error(ex.getMessage());
        String errors = ex.getMessage();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}

package com.overridetech.funpay_monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FunpayMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FunpayMonitorApplication.class, args);
    }

}

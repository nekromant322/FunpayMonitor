package com.overridetech.funpay_monitor.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "cache.exchange-rate.expire")
public class CacheConfigProperties {

    private int afterWrite;

    private TimeUnit timeUnit;
}

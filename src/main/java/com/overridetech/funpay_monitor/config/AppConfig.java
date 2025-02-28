package com.overridetech.funpay_monitor.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.overridetech.funpay_monitor.config.property.CacheConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class AppConfig {


    private final CacheConfigProperties cacheConfigProperties;

    @Bean
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder().expireAfterWrite(
                cacheConfigProperties.getAfterWrite(),
                cacheConfigProperties.getTimeUnit());
    }

    @Bean
    public CacheManager cacheManager(Caffeine caffeine) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}

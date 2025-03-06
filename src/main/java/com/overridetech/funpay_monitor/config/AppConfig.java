package com.overridetech.funpay_monitor.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.overridetech.funpay_monitor.config.property.CacheConfigProperties;
import com.overridetech.funpay_monitor.config.property.GoogleServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class AppConfig {


    private final CacheConfigProperties cacheConfigProperties;
    private final GoogleServiceProperties googleServiceProperties;

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

    @Bean
    public Sheets getSheetsService(@Qualifier("spreadSheetScopeHttpRequestInitializer") HttpRequestInitializer httpRequestInitializer) throws IOException, GeneralSecurityException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new Sheets.Builder(httpTransport,
                JacksonFactory.getDefaultInstance(),
                httpRequestInitializer).
                setApplicationName("Google sheets adapter").
                build();
    }


    @Bean
    public Drive getDriveService(@Qualifier("driveScopeHttpRequestInitializer") HttpRequestInitializer httpRequestInitializer) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        return new Drive.Builder(httpTransport, JacksonFactory.getDefaultInstance(), httpRequestInitializer)
                .setApplicationName("grant permissions driver")
                .build();
    }

    @Bean("spreadSheetScopeHttpRequestInitializer")
    public HttpRequestInitializer ssHttpRequestInitializer() throws IOException {
        final List<String> SCOPES =
                Collections.singletonList(SheetsScopes.SPREADSHEETS);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(googleServiceProperties.getAuthPath()));
        return new HttpCredentialsAdapter(credentials.createScoped(SCOPES));
    }

    @Bean("driveScopeHttpRequestInitializer")
    public HttpRequestInitializer driveHttpRequestInitializer() throws IOException {
        final List<String> SCOPES =
                Collections.singletonList(SheetsScopes.DRIVE);
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(googleServiceProperties.getAuthPath()));
        return new HttpCredentialsAdapter(credentials.createScoped(SCOPES));
    }

}

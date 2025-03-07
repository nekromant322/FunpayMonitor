package com.overridetech.funpay_monitor.config.google;

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
import com.overridetech.funpay_monitor.config.property.GoogleServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class GoogleApiConfig {

    private final GoogleServiceProperties googleServiceProperties;


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
    public HttpRequestInitializer ssHttpRequestInitializer(GoogleCredentials credentials) {
        final List<String> SCOPES =
                Collections.singletonList(SheetsScopes.SPREADSHEETS);


        return new HttpCredentialsAdapter(credentials.createScoped(SCOPES));
    }

    @Bean("driveScopeHttpRequestInitializer")
    public HttpRequestInitializer driveHttpRequestInitializer(GoogleCredentials credentials) {
        final List<String> SCOPES =
                Collections.singletonList(SheetsScopes.DRIVE);


        return new HttpCredentialsAdapter(credentials.createScoped(SCOPES));
    }


    @Bean
    @Profile("prod")
    public GoogleCredentials getProdGoogleCredentials() throws IOException {
        InputStream credentialsInputStream = new ByteArrayInputStream(googleServiceProperties.getCredentialsJson().getBytes());
        return GoogleCredentials.fromStream(credentialsInputStream);

    }

    @Bean
    @Profile("dev")
    public GoogleCredentials getDevGoogleCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream("google_sheet_auth.json"));

    }
}

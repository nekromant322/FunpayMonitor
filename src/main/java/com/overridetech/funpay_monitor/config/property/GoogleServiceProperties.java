package com.overridetech.funpay_monitor.config.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Getter
@Setter
@Configuration
@Component
@ConfigurationProperties(prefix = "google")
public class GoogleServiceProperties {
    private String type = "service_account";

    @JsonProperty("project_id")
    private String projectId = "funpay-monitor";

    @JsonProperty("private_key_id")
    private String privateKeyId;

    @JsonProperty("private_key")
    private String privateKey;

    @JsonProperty("client_email")
    private String clientEmail = "sheets-service-account@funpay-monitor.iam.gserviceaccount.com";

    @JsonProperty("client_id")
    private String clientId = "102173051762174719008";

    @JsonProperty("auth_uri")
    private String authUri = "https://accounts.google.com/o/oauth2/auth";

    @JsonProperty("token_uri")
    private String tokenUri = "https://oauth2.googleapis.com/token";

    @JsonProperty("auth_provider_x509_cert_url")
    private String authProviderX509CertUrl = "https://www.googleapis.com/oauth2/v1/certs";

    @JsonProperty("client_x509_cert_url")
    private String clientX509CertUrl = "https://www.googleapis.com/robot/v1/metadata/x509/sheets-service-account%40funpay-monitor.iam.gserviceaccount.com";

    @JsonProperty("universe_domain")
    private String universeDomain = "googleapis.com";

    public String getCredentialsJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}

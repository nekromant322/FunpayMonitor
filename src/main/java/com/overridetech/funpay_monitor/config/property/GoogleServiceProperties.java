package com.overridetech.funpay_monitor.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleServiceProperties {

    private String clientId = "102173051762174719008";

    private String clientEmail = "sheets-service-account@funpay-monitor.iam.gserviceaccount.com";

    private String privateKey;

    private String privateKeyId;

}

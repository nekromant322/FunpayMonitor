package com.overridetech.funpay_monitor.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "google")
public class GoogleServiceProperties {

    private String authPath;
    private String poeDivineId;

}

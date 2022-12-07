package com.paysafe.buckpal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "buckpal")
public class BuckPalConfigurationProperties {

    private long transferThreshold = Long.MAX_VALUE;

}

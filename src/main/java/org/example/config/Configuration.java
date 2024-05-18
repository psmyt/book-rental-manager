package org.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.Duration;

@SpringBootConfiguration
@EnableConfigurationProperties
@ConfigurationProperties
@Data
public class Configuration  {
    @Value("${application.reservationTtl}")
    Duration reservationTtl;
}

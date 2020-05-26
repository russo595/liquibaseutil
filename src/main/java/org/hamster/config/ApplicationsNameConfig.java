package org.hamster.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "example")
@Data
public class ApplicationsNameConfig {
    /**
     * Ключ - название БД сервиса
     * Значение - схема где хранятся view
     */
    private Map<String, String> services;
}

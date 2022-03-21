package org.hamster.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Ключ - название БД сервиса
 * Значение - схема где хранятся view
 */
@Configuration
@ConfigurationProperties(prefix = "example")
data class ApplicationsNameConfig(val services: Map<String, String>)
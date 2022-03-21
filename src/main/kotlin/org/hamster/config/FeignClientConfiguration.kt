package org.hamster.config

import feign.Logger
import feign.RequestInterceptor
import feign.auth.BasicAuthRequestInterceptor
import feign.codec.ErrorDecoder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType

@Configuration
class FeignClientConfiguration {
    @Value("\${NEXUS_USER}")
    private lateinit var nexusUser: String

    @Value("\${NEXUS_PASSWORD}")
    private lateinit var nexusPassword: String

    @Bean
    fun loggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    @Bean
    fun basicAuthRequestInterceptor(): BasicAuthRequestInterceptor {
        return BasicAuthRequestInterceptor(nexusUser, nexusPassword)
    }

    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            template.header(
                "Accept",
                MediaType.APPLICATION_XHTML_XML.type,
                MediaType.APPLICATION_XML.type
            )
        }
    }

    @Bean
    fun errorDecoder(): ErrorDecoder {
        return ErrorDecoder.Default()
    }
}
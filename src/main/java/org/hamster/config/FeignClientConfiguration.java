package org.hamster.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class FeignClientConfiguration {

    @Value("${NEXUS_USER}")
    private String nexusUser;
    @Value("${NEXUS_PASSWORD}")
    private String nexusPassword;

    @Bean
    public Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(nexusUser, nexusPassword);
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Accept", MediaType.APPLICATION_XHTML_XML.getType(), MediaType.APPLICATION_XML.getType());
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ErrorDecoder.Default();
    }
}

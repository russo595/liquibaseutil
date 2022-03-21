package org.hamster.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude.Value
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RefreshScope
class DbDiffUtilConfig {
    @Bean("customObjectMapper")
    fun objectMapper(): ObjectMapper {
        val objectMapper = JsonMapper.builder().apply {
            addModule(JavaTimeModule())
            addModule(KotlinModule.Builder().build())
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            defaultPropertyInclusion(Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL))
            visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        }.build()

        return objectMapper
    }
}
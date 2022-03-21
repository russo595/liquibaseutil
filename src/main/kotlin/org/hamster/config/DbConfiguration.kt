package org.hamster.config

import org.apache.commons.dbcp2.BasicDataSource
import org.apache.maven.maven_metadata._1_0.ObjectFactory
import org.apache.maven.maven_metadata._1_0.Versioning
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Configuration
import java.text.MessageFormat
import javax.sql.DataSource

@Configuration
class DbConfiguration {
    @Value("\${url_prom}")
    private lateinit var urlProm: String

    @Value("\${url_test_stand}")
    private lateinit var urlTestStand: String

    @Value("\${driver}")
    private lateinit var driver: String

    @Value("\${testStandUser}")
    private lateinit var testStandUser: String

    @Value("\${testStandPassword}")
    private lateinit var testStandPassword: String

    @Value("\${promUser}")
    private lateinit var promUser: String

    @Value("\${promPassword}")
    private val promPassword: String? = null
    fun promDataSource(serviceName: String?): DataSource {
        val url = MessageFormat.format(urlProm, serviceName)
        return DataSourceBuilder.create()
            .url(url)
            .type(BasicDataSource::class.java)
            .driverClassName(driver)
            .username(promUser)
            .password(promPassword)
            .build()
    }

    fun testStandDataSource(serviceName: String?): DataSource {
        val url = MessageFormat.format(urlTestStand, serviceName)
        return DataSourceBuilder.create()
            .url(url)
            .type(BasicDataSource::class.java)
            .driverClassName(driver)
            .username(testStandUser)
            .password(testStandPassword)
            .build()
    }
}
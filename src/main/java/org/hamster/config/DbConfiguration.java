package org.hamster.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.text.MessageFormat;

@Configuration
public class DbConfiguration {

    @Value("${url_prom}")
    private String urlProm;
    @Value("${url_test_stand}")
    private String urlTestStand;
    @Value("${driver}")
    private String driver;
    @Value("${testStandUser}")
    private String testStandUser;
    @Value("${testStandPassword}")
    private String testStandPassword;
    @Value("${promUser}")
    private String promUser;
    @Value("${promPassword}")
    private String promPassword;

    public DataSource promDataSource(String serviceName) {
        String url = MessageFormat.format(urlProm, serviceName);
        return DataSourceBuilder.create()
                .url(url)
                .type(BasicDataSource.class)
                .driverClassName(driver)
                .username(promUser)
                .password(promPassword)
                .build();
    }

    public DataSource testStandDataSource(String serviceName) {
        String url = MessageFormat.format(urlTestStand, serviceName);
        return DataSourceBuilder.create()
                .url(url)
                .type(BasicDataSource.class)
                .driverClassName(driver)
                .username(testStandUser)
                .password(testStandPassword)
                .build();
    }
}

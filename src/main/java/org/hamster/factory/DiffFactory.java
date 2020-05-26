package org.hamster.factory;

import org.hamster.service.AbstractDiff;
import org.hamster.service.ChangeLogDiff;
import org.hamster.service.ReportDiff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.hamster.config.DbConfiguration;

@Component
public class DiffFactory {

    @Autowired
    private DbConfiguration dbConfiguration;

    @Bean
    public AbstractDiff reportDiff() {
        return new ReportDiff(dbConfiguration);
    }

    @Bean
    public AbstractDiff changeLogDiff() {
        return new ChangeLogDiff(dbConfiguration);
    }
}

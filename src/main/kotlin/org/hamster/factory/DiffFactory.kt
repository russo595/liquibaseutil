package org.hamster.factory

import org.hamster.config.DbConfiguration
import org.hamster.service.AbstractDiff
import org.hamster.service.ChangeLogDiff
import org.hamster.service.ReportDiff
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DiffFactory {
    @Autowired
    private lateinit var dbConfiguration: DbConfiguration

    @Bean
    fun reportDiff(): AbstractDiff = ReportDiff(dbConfiguration)

    @Bean
    fun changeLogDiff(): AbstractDiff = ChangeLogDiff(dbConfiguration)
}
package org.hamster.service

import liquibase.diff.DiffResult
import liquibase.diff.output.report.DiffToReport
import org.hamster.config.DbConfiguration
import org.hamster.enumeration.Extension
import java.io.PrintStream

class ReportDiff(dbConfiguration: DbConfiguration?) : AbstractDiff(dbConfiguration!!) {
    override fun diff(diffResult: DiffResult, out: PrintStream) {
        val diffToReport = DiffToReport(diffResult, out)
        diffToReport.print()
    }

    override val extension: String
        get() = Extension.TXT.extension
}
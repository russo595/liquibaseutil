package org.hamster.service

import liquibase.diff.DiffResult
import liquibase.diff.output.DiffOutputControl
import liquibase.diff.output.changelog.DiffToChangeLog
import org.hamster.config.DbConfiguration
import org.hamster.enumeration.Extension
import java.io.PrintStream

class ChangeLogDiff(dbConfiguration: DbConfiguration) : AbstractDiff(dbConfiguration) {
    override fun diff(diffResult: DiffResult, out: PrintStream) {
        val diffToChangeLog = DiffToChangeLog(diffResult, DiffOutputControl())
        diffToChangeLog.print(out)
    }

    override val extension: String
        get() = Extension.XML.extension
}
package org.hamster.service

import liquibase.database.Database
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.diff.DiffGeneratorFactory
import liquibase.diff.DiffResult
import liquibase.diff.compare.CompareControl
import liquibase.structure.DatabaseObject
import liquibase.structure.core.Column
import liquibase.structure.core.View
import org.hamster.config.DbConfiguration
import java.io.ByteArrayOutputStream
import java.io.PrintStream

abstract class AbstractDiff(val dbConfiguration: DbConfiguration) {

    abstract fun diff(diffResult: DiffResult, out: PrintStream)

    abstract val extension: String

    fun generateDiff(fileName: String?, schema: String?): ByteArray {
        var databaseProm: Database? = null
        var databaseTestStand: Database? = null
        try {
            ByteArrayOutputStream().use { baos ->
                PrintStream(baos).use { out ->
                    databaseProm = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                        JdbcConnection(
                            dbConfiguration.promDataSource(fileName).connection
                        )
                    ).apply {
                        defaultSchemaName = schema
                    }
                    databaseTestStand = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                        JdbcConnection(
                            dbConfiguration.testStandDataSource(fileName).connection
                        )
                    ).apply {
                        defaultSchemaName = schema
                    }
                    val compareTypes: Set<Class<out DatabaseObject>> = setOf(Column::class.java, View::class.java)
                    val compareControl = CompareControl(compareTypes)
                    val diffResult =
                        DiffGeneratorFactory.getInstance().compare(databaseTestStand, databaseProm, compareControl)
                    diff(diffResult, out)
                    baos.flush()
                    return baos.toByteArray()
                }
            }
        } finally {
            if (databaseProm != null) {
                databaseProm!!.close()
            }
            if (databaseTestStand != null) {
                databaseTestStand!!.close()
            }
        }
    }
}
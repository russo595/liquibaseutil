package org.hamster.service;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.View;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamster.config.DbConfiguration;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDiff {

    private final DbConfiguration dbConfiguration;

    public abstract void diff(DiffResult diffResult, PrintStream out) throws DatabaseException, IOException, ParserConfigurationException;

    public abstract String getExtension();

    public byte[] generateDiff(String fileName, String schema) throws SQLException, LiquibaseException, IOException, ParserConfigurationException {
        Database databaseProm = null;
        Database databaseTestStand = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintStream out = new PrintStream(baos)) {
            databaseProm = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dbConfiguration.promDataSource(fileName).getConnection()));
            databaseProm.setDefaultSchemaName(schema);
            databaseTestStand = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(dbConfiguration.testStandDataSource(fileName).getConnection()));
            databaseTestStand.setDefaultSchemaName(schema);
            Set<Class<? extends DatabaseObject>> compareTypes = Stream.of(Column.class, View.class)
                    .collect(Collectors.toSet());
            CompareControl compareControl = new CompareControl(compareTypes);
            DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(databaseTestStand, databaseProm, compareControl);
            diff(diffResult, out);
            baos.flush();
            return baos.toByteArray();
        } finally {
            if (databaseProm != null) {
                databaseProm.close();
            }
            if (databaseTestStand != null) {
                databaseTestStand.close();
            }
        }
    }
}

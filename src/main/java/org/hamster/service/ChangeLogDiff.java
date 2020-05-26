package org.hamster.service;

import liquibase.diff.DiffResult;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import org.hamster.enumeration.Extension;
import org.hamster.config.DbConfiguration;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;

public class ChangeLogDiff extends AbstractDiff {

    public ChangeLogDiff(DbConfiguration dbConfiguration) {
        super(dbConfiguration);
    }

    @Override
    public void diff(DiffResult diffResult, PrintStream out) throws IOException, DatabaseException, ParserConfigurationException {
        DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, new DiffOutputControl());
        diffToChangeLog.print(out);
    }

    @Override
    public String getExtension() {
        return Extension.XML.getExtension();
    }
}

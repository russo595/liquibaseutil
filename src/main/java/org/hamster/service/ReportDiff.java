package org.hamster.service;

import liquibase.diff.DiffResult;
import liquibase.diff.output.report.DiffToReport;
import liquibase.exception.DatabaseException;
import org.hamster.enumeration.Extension;
import org.hamster.config.DbConfiguration;

import java.io.PrintStream;

public class ReportDiff extends AbstractDiff {

    public ReportDiff(DbConfiguration dbConfiguration) {
        super(dbConfiguration);
    }

    @Override
    public void diff(DiffResult diffResult, PrintStream out) throws DatabaseException {
        DiffToReport diffToReport = new DiffToReport(diffResult, out);
        diffToReport.print();
    }

    @Override
    public String getExtension() {
        return Extension.TXT.getExtension();
    }
}

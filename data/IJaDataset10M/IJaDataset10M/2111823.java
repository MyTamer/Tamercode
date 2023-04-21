package com.simpledata.bc.reports.common;

import com.simpledata.bc.reports.base.Subreport;
import com.simpledata.bc.reports.base.SubreportTreeItem;

/** 
public abstract class SpecializedSubreport {

    /** Internal storage of all report data. */
    protected Subreport m_report;

    /**
    public void addData(SpecializedDataRow row) {
        m_report.addRow(row.toObjectArray());
    }

    /**
    public SubreportTreeItem getReport() {
        return (SubreportTreeItem) m_report;
    }
}
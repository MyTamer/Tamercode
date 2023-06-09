package org.adempiere.impexp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;
import org.compiere.util.DisplayType;
import org.compiere.util.Msg;
import org.compiere.util.Util;

/**
 * Export excel from ArrayList of data
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 *
 */
public class ArrayExcelExporter extends AbstractExcelExporter {

    private Properties m_ctx = null;

    private ArrayList<ArrayList<Object>> m_data = null;

    public ArrayExcelExporter(Properties ctx, ArrayList<ArrayList<Object>> data) {
        super();
        m_ctx = ctx;
        m_data = data;
    }

    @Override
    public Properties getCtx() {
        return m_ctx;
    }

    @Override
    protected int getColumnCount() {
        return m_data.get(0).size();
    }

    @Override
    protected int getDisplayType(int row, int col) {
        ArrayList<Object> dataRow = m_data.get(row + 1);
        Object value = dataRow.get(col);
        if (value == null) ; else if (value instanceof Timestamp) {
            return DisplayType.Date;
        } else if (value instanceof Number) {
            if (value instanceof Integer) {
                return DisplayType.Integer;
            } else {
                return DisplayType.Number;
            }
        } else if (value instanceof Boolean) {
            return DisplayType.YesNo;
        } else {
            return DisplayType.String;
        }
        return -1;
    }

    @Override
    protected String getHeaderName(int col) {
        Object o = m_data.get(0).get(col);
        String name = o != null ? o.toString() : null;
        String nameTrl = Msg.translate(getLanguage(), name);
        if (Util.isEmpty(nameTrl)) nameTrl = name;
        return nameTrl;
    }

    @Override
    protected int getRowCount() {
        return m_data.size() - 1;
    }

    @Override
    protected Object getValueAt(int row, int col) {
        ArrayList<Object> dataRow = m_data.get(row + 1);
        Object value = dataRow.get(col);
        return value;
    }

    @Override
    protected boolean isColumnPrinted(int col) {
        return true;
    }

    @Override
    protected boolean isFunctionRow() {
        return false;
    }

    @Override
    protected boolean isPageBreak(int row, int col) {
        return false;
    }

    @Override
    protected void setCurrentRow(int row) {
    }
}

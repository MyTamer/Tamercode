package org.opennms.api.reporting.parameter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.opennms.api.reporting.ReportFormat;
import org.opennms.api.reporting.ReportMode;

/**
 * <p>ReportParameters class.</p>
 *
 * @author ranger
 * @version $Id: $
 */
public class ReportParameters implements Serializable {

    private static final long serialVersionUID = -3848794546173077375L;

    protected String m_reportId;

    protected ReportFormat m_format;

    protected String m_displayName;

    protected List<ReportDateParm> m_dateParms;

    protected List<ReportStringParm> m_stringParms;

    protected List<ReportIntParm> m_intParms;

    protected List<ReportFloatParm> m_floatParms;

    protected List<ReportDoubleParm> m_doubleParms;

    /**
     * <p>Constructor for ReportParameters.</p>
     */
    public ReportParameters() {
        super();
    }

    /**
     * <p>getDateParms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ReportDateParm> getDateParms() {
        return m_dateParms;
    }

    /**
     * <p>setDateParms</p>
     *
     * @param dateParms a {@link java.util.List} object.
     */
    public void setDateParms(List<ReportDateParm> dateParms) {
        m_dateParms = dateParms;
    }

    /**
     * <p>getStringParms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ReportStringParm> getStringParms() {
        return m_stringParms;
    }

    /**
     * <p>setStringParms</p>
     *
     * @param strings a {@link java.util.List} object.
     */
    public void setStringParms(List<ReportStringParm> strings) {
        m_stringParms = strings;
    }

    /**
     * <p>getIntParms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ReportIntParm> getIntParms() {
        return m_intParms;
    }

    /**
     * <p>setIntParms</p>
     *
     * @param ints a {@link java.util.List} object.
     */
    public void setIntParms(List<ReportIntParm> ints) {
        m_intParms = ints;
    }

    /**
     * <p>getFloatParms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ReportFloatParm> getFloatParms() {
        return m_floatParms;
    }

    /**
     * <p>setFloatParms</p>
     *
     * @param ints a {@link java.util.List} object.
     */
    public void setFloatParms(List<ReportFloatParm> floats) {
        m_floatParms = floats;
    }

    /**
     * <p>getDoubleParms</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ReportDoubleParm> getDoubleParms() {
        return m_doubleParms;
    }

    /**
     * <p>setDoubleParms</p>
     *
     * @param ints a {@link java.util.List} object.
     */
    public void setDoubleParms(List<ReportDoubleParm> doubleParms) {
        m_doubleParms = doubleParms;
    }

    /**
     * <p>setReportId</p>
     *
     * @param reportId a {@link java.lang.String} object.
     */
    public void setReportId(String reportId) {
        m_reportId = reportId;
    }

    /**
     * <p>getReportId</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getReportId() {
        return m_reportId;
    }

    /**
     * <p>setDisplayName</p>
     *
     * @param displayName a {@link java.lang.String} object.
     */
    public void setDisplayName(String displayName) {
        m_displayName = displayName;
    }

    /**
     * <p>getDisplayName</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDisplayName() {
        return m_displayName;
    }

    /**
     * <p>setFormat</p>
     *
     * @param format a {@link org.opennms.api.reporting.ReportFormat} object.
     */
    public void setFormat(ReportFormat format) {
        m_format = format;
    }

    /**
     * <p>getFormat</p>
     *
     * @return a {@link org.opennms.api.reporting.ReportFormat} object.
     */
    public ReportFormat getFormat() {
        return m_format;
    }

    /**
     * <p>getReportParms</p>
     *
     * * @param format a {@link org.opennms.api.reporting.ReportMode} object.
     * 
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<String, Object> getReportParms(ReportMode mode) {
        HashMap<String, Object> parmMap = new HashMap<String, Object>();
        if (m_stringParms != null) {
            Iterator<ReportStringParm> stringIter = m_stringParms.iterator();
            while (stringIter.hasNext()) {
                ReportStringParm parm = stringIter.next();
                parmMap.put(parm.getName(), parm.getValue());
            }
        }
        if (m_dateParms != null) {
            Iterator<ReportDateParm> dateIter = m_dateParms.iterator();
            while (dateIter.hasNext()) {
                ReportDateParm parm = dateIter.next();
                parmMap.put(parm.getName(), parm.getValue(mode));
            }
        }
        if (m_intParms != null) {
            Iterator<ReportIntParm> intIter = m_intParms.iterator();
            while (intIter.hasNext()) {
                ReportIntParm parm = intIter.next();
                parmMap.put(parm.getName(), parm.getValue());
            }
        }
        if (m_floatParms != null) {
            Iterator<ReportFloatParm> floatIter = m_floatParms.iterator();
            while (floatIter.hasNext()) {
                ReportFloatParm parm = floatIter.next();
                parmMap.put(parm.getName(), parm.getValue());
            }
        }
        if (m_doubleParms != null) {
            Iterator<ReportDoubleParm> doubleIter = m_doubleParms.iterator();
            while (doubleIter.hasNext()) {
                ReportDoubleParm parm = doubleIter.next();
                parmMap.put(parm.getName(), parm.getValue());
            }
        }
        return parmMap;
    }

    /**
     * <p>getReportParms</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<String, Object> getReportParms() {
        return getReportParms(ReportMode.IMMEDIATE);
    }
}

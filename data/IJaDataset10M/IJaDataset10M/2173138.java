package sun.management.snmp.jvminstr;

import java.io.Serializable;
import java.lang.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import com.sun.jmx.snmp.SnmpString;
import com.sun.jmx.snmp.SnmpStatusException;
import com.sun.jmx.snmp.agent.SnmpMib;
import com.sun.jmx.snmp.SnmpDefinitions;
import sun.management.snmp.jvmmib.JvmThreadingMBean;
import sun.management.snmp.jvmmib.EnumJvmThreadCpuTimeMonitoring;
import sun.management.snmp.jvmmib.EnumJvmThreadContentionMonitoring;
import sun.management.snmp.util.MibLogger;

/**
 * The class is used for implementing the "JvmThreading" group.
 */
public class JvmThreadingImpl implements JvmThreadingMBean {

    /**
     * Variable for storing the value of "JvmThreadCpuTimeMonitoring".
     *
     * "The state of the Thread CPU Time Monitoring feature.
     * This feature can be:
     *
     * unsupported: The JVM does not support Thread CPU Time Monitoring.
     * enabled    : The JVM supports Thread CPU Time Monitoring, and it
     * is enabled.
     * disabled   : The JVM supports Thread CPU Time Monitoring, and it
     * is disabled.
     *
     * Only enabled(3) and disabled(4) may be supplied as values to a
     * SET request. unsupported(1) can only be set internally by the
     * agent.
     *
     * See java.lang.management.ThreadMXBean.isThreadCpuTimeSupported(),
     * java.lang.management.ThreadMXBean.isThreadCpuTimeEnabled(),
     * java.lang.management.ThreadMXBean.setThreadCpuTimeEnabled()
     * "
     *
     */
    static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringUnsupported = new EnumJvmThreadCpuTimeMonitoring("unsupported");

    static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringEnabled = new EnumJvmThreadCpuTimeMonitoring("enabled");

    static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringDisabled = new EnumJvmThreadCpuTimeMonitoring("disabled");

    /**
     * Variable for storing the value of "JvmThreadContentionMonitoring".
     *
     * "The state of the Thread Contention Monitoring feature.
     * This feature can be:
     *
     * unsupported: The JVM does not support Thread Contention Monitoring.
     * enabled    : The JVM supports Thread Contention Monitoring, and it
     * is enabled.
     * disabled   : The JVM supports Thread Contention Monitoring, and it
     * is disabled.
     *
     * Only enabled(3) and disabled(4) may be supplied as values to a
     * SET request. unsupported(1) can only be set internally by the
     * agent.
     *
     * See java.lang.management.ThreadMXBean.isThreadContentionMonitoringSupported(),
     * java.lang.management.ThreadMXBean.isThreadContentionMonitoringEnabled(),
     * java.lang.management.ThreadMXBean.setThreadContentionMonitoringEnabled()
     * "
     *
     */
    static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringUnsupported = new EnumJvmThreadContentionMonitoring("unsupported");

    static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringEnabled = new EnumJvmThreadContentionMonitoring("enabled");

    static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringDisabled = new EnumJvmThreadContentionMonitoring("disabled");

    /**
     * Constructor for the "JvmThreading" group.
     * If the group contains a table, the entries created through an SNMP SET
     * will not be registered in Java DMK.
     */
    public JvmThreadingImpl(SnmpMib myMib) {
        log.debug("JvmThreadingImpl", "Constructor");
    }

    /**
     * Constructor for the "JvmThreading" group.
     * If the group contains a table, the entries created through an SNMP SET
     * will be AUTOMATICALLY REGISTERED in Java DMK.
     */
    public JvmThreadingImpl(SnmpMib myMib, MBeanServer server) {
        log.debug("JvmThreadingImpl", "Constructor with server");
    }

    /**
     * ThreadMXBean accessor. It is acquired from the
     * java.lang.management.ManagementFactory
     * @return The local ThreadMXBean.
     */
    static ThreadMXBean getThreadMXBean() {
        return ManagementFactory.getThreadMXBean();
    }

    /**
     * Getter for the "JvmThreadCpuTimeMonitoring" variable.
     */
    public EnumJvmThreadCpuTimeMonitoring getJvmThreadCpuTimeMonitoring() throws SnmpStatusException {
        ThreadMXBean mbean = getThreadMXBean();
        if (!mbean.isThreadCpuTimeSupported()) {
            log.debug("getJvmThreadCpuTimeMonitoring", "Unsupported ThreadCpuTimeMonitoring");
            return JvmThreadCpuTimeMonitoringUnsupported;
        }
        try {
            if (mbean.isThreadCpuTimeEnabled()) {
                log.debug("getJvmThreadCpuTimeMonitoring", "Enabled ThreadCpuTimeMonitoring");
                return JvmThreadCpuTimeMonitoringEnabled;
            } else {
                log.debug("getJvmThreadCpuTimeMonitoring", "Disabled ThreadCpuTimeMonitoring");
                return JvmThreadCpuTimeMonitoringDisabled;
            }
        } catch (UnsupportedOperationException e) {
            log.debug("getJvmThreadCpuTimeMonitoring", "Newly unsupported ThreadCpuTimeMonitoring");
            return JvmThreadCpuTimeMonitoringUnsupported;
        }
    }

    /**
     * Setter for the "JvmThreadCpuTimeMonitoring" variable.
     */
    public void setJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring x) throws SnmpStatusException {
        ThreadMXBean mbean = getThreadMXBean();
        if (JvmThreadCpuTimeMonitoringEnabled.intValue() == x.intValue()) mbean.setThreadCpuTimeEnabled(true); else mbean.setThreadCpuTimeEnabled(false);
    }

    /**
     * Checker for the "JvmThreadCpuTimeMonitoring" variable.
     */
    public void checkJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring x) throws SnmpStatusException {
        if (JvmThreadCpuTimeMonitoringUnsupported.intValue() == x.intValue()) {
            log.debug("checkJvmThreadCpuTimeMonitoring", "Try to set to illegal unsupported value");
            throw new SnmpStatusException(SnmpDefinitions.snmpRspWrongValue);
        }
        if ((JvmThreadCpuTimeMonitoringEnabled.intValue() == x.intValue()) || (JvmThreadCpuTimeMonitoringDisabled.intValue() == x.intValue())) {
            ThreadMXBean mbean = getThreadMXBean();
            if (mbean.isThreadCpuTimeSupported()) return;
            log.debug("checkJvmThreadCpuTimeMonitoring", "Unsupported operation, can't set state");
            throw new SnmpStatusException(SnmpDefinitions.snmpRspInconsistentValue);
        }
        log.debug("checkJvmThreadCpuTimeMonitoring", "unknown enum value ");
        throw new SnmpStatusException(SnmpDefinitions.snmpRspWrongValue);
    }

    /**
     * Getter for the "JvmThreadContentionMonitoring" variable.
     */
    public EnumJvmThreadContentionMonitoring getJvmThreadContentionMonitoring() throws SnmpStatusException {
        ThreadMXBean mbean = getThreadMXBean();
        if (!mbean.isThreadContentionMonitoringSupported()) {
            log.debug("getJvmThreadContentionMonitoring", "Unsupported ThreadContentionMonitoring");
            return JvmThreadContentionMonitoringUnsupported;
        }
        if (mbean.isThreadContentionMonitoringEnabled()) {
            log.debug("getJvmThreadContentionMonitoring", "Enabled ThreadContentionMonitoring");
            return JvmThreadContentionMonitoringEnabled;
        } else {
            log.debug("getJvmThreadContentionMonitoring", "Disabled ThreadContentionMonitoring");
            return JvmThreadContentionMonitoringDisabled;
        }
    }

    /**
     * Setter for the "JvmThreadContentionMonitoring" variable.
     */
    public void setJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring x) throws SnmpStatusException {
        ThreadMXBean mbean = getThreadMXBean();
        if (JvmThreadContentionMonitoringEnabled.intValue() == x.intValue()) mbean.setThreadContentionMonitoringEnabled(true); else mbean.setThreadContentionMonitoringEnabled(false);
    }

    /**
     * Checker for the "JvmThreadContentionMonitoring" variable.
     */
    public void checkJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring x) throws SnmpStatusException {
        if (JvmThreadContentionMonitoringUnsupported.intValue() == x.intValue()) {
            log.debug("checkJvmThreadContentionMonitoring", "Try to set to illegal unsupported value");
            throw new SnmpStatusException(SnmpDefinitions.snmpRspWrongValue);
        }
        if ((JvmThreadContentionMonitoringEnabled.intValue() == x.intValue()) || (JvmThreadContentionMonitoringDisabled.intValue() == x.intValue())) {
            ThreadMXBean mbean = getThreadMXBean();
            if (mbean.isThreadContentionMonitoringSupported()) return;
            log.debug("checkJvmThreadContentionMonitoring", "Unsupported operation, can't set state");
            throw new SnmpStatusException(SnmpDefinitions.snmpRspInconsistentValue);
        }
        log.debug("checkJvmThreadContentionMonitoring", "Try to set to unknown value");
        throw new SnmpStatusException(SnmpDefinitions.snmpRspWrongValue);
    }

    /**
     * Getter for the "JvmThreadTotalStartedCount" variable.
     */
    public Long getJvmThreadTotalStartedCount() throws SnmpStatusException {
        return new Long(getThreadMXBean().getTotalStartedThreadCount());
    }

    /**
     * Getter for the "JvmThreadPeakCount" variable.
     */
    public Long getJvmThreadPeakCount() throws SnmpStatusException {
        return new Long(getThreadMXBean().getPeakThreadCount());
    }

    /**
     * Getter for the "JvmThreadDaemonCount" variable.
     */
    public Long getJvmThreadDaemonCount() throws SnmpStatusException {
        return new Long(getThreadMXBean().getDaemonThreadCount());
    }

    /**
     * Getter for the "JvmThreadCount" variable.
     */
    public Long getJvmThreadCount() throws SnmpStatusException {
        return new Long(getThreadMXBean().getThreadCount());
    }

    /**
     * Getter for the "JvmThreadPeakCountReset" variable.
     */
    public synchronized Long getJvmThreadPeakCountReset() throws SnmpStatusException {
        return new Long(jvmThreadPeakCountReset);
    }

    /**
     * Setter for the "JvmThreadPeakCountReset" variable.
     */
    public synchronized void setJvmThreadPeakCountReset(Long x) throws SnmpStatusException {
        final long l = x.longValue();
        if (l > jvmThreadPeakCountReset) {
            final long stamp = System.currentTimeMillis();
            getThreadMXBean().resetPeakThreadCount();
            jvmThreadPeakCountReset = stamp;
            log.debug("setJvmThreadPeakCountReset", "jvmThreadPeakCountReset=" + stamp);
        }
    }

    /**
     * Checker for the "JvmThreadPeakCountReset" variable.
     */
    public void checkJvmThreadPeakCountReset(Long x) throws SnmpStatusException {
    }

    private long jvmThreadPeakCountReset = 0;

    static final MibLogger log = new MibLogger(JvmThreadingImpl.class);
}

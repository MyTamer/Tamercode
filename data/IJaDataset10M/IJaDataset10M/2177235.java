package org.opennms.netmgt.collectd;

import java.io.File;
import java.util.List;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.config.DataCollectionConfigFactory;
import org.opennms.netmgt.config.MibObject;
import org.opennms.netmgt.rrd.RrdException;
import org.opennms.netmgt.rrd.RrdUtils;
import org.opennms.netmgt.snmp.SnmpValue;

/**
 * This class encapsulates an RRDTool data source. Data source information
 * parsed from the DataCollection.xml file is stored in RRDDataSource objects.
 * 
 * For additional information on RRD and RRDTool see:
 * http://ee-staff.ethz.ch/~oetiker/webtools/rrdtool/
 * 
 * @author <A HREF="mailto:mike@opennms.org">Mike </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * 
 * @version 1.1.1.1
 * 
 */
public class JMXDataSource {

    private static final int MAX_DS_NAME_LENGTH = 19;

    public static final String RRD_ERROR = "RRD_ERROR";

    /**
     * Defines the list of supported (MIB) object types whic may be mapped to
     * one of the supported RRD data source types. Currently the only two
     * supported RRD data source types are: COUNTER & GAUGE. A simple string
     * comparison is performed against this list of supported types to determine
     * if an object can be represented by an RRD data source. NOTE: String
     * comparison uses String.startsWith() method so "counter32" & "counter64"
     * values will match "counter" entry. Comparison is case sensitive.
     */
    private static final String[] supportedObjectTypes = new String[] { "counter", "gauge", "timeticks", "integer", "octetstring" };

    /**
     * Index of data type in supportedObjectTypes string array.
     */
    private static final int COUNTER_INDEX = 0;

    private static final int GAUGE_INDEX = 1;

    private static final int TIMETICKS_INDEX = 2;

    private static final int INTEGER_INDEX = 3;

    private static final int OCTETSTRING_INDEX = 4;

    /**
     * RRDTool defined Data Source Types NOTE: "DERIVE" and "ABSOLUTE" not
     * currently supported.
     */
    private static final String DST_GAUGE = "GAUGE";

    private static final String DST_COUNTER = "COUNTER";

    /**
     * Data Source Type. This must be one of the available RRDTool data source
     * type values: GAUGE, COUNTER, DERIVE, or ABSOLUTE
     */
    private String m_type;

    /**
     * Data Source Heartbeat. This is the maximum number of seconds that may
     * pass between updates of this data source before the value of the data
     * source is assumed to be 'Unknown'.
     */
    private int m_heartbeat;

    /**
     * Minimum Expected Range. Together with m_max defines the expected range of
     * the data supplied by this data source. May be set to "U" for 'Unknown'.
     */
    private String m_min;

    /**
     * Maximum Expected Range. Together with m_min defines the expected range of
     * the data supplied by this data source. May be set to "U" for Unknown.
     */
    private String m_max;

    private String m_oid;

    private String m_instance;

    private String m_name;

    private String m_collectionName;

    /**
          * @param objectType MIB object type being inquired about
          * @return true if RRDDataSource can  handle the given type, false if it can't
          */
    public static boolean handlesType(String objectType) {
        return (JMXDataSource.mapType(objectType) != null);
    }

    /**
     * Static method which takes a MIB object type (counter, counter32,
     * octetstring, etc...) and returns the appropriate RRD data type. If the
     * object type cannot be mapped to an RRD type, null is returned. RRD only
     * supports integer data so MIB objects of type 'octetstring' are not
     * supported.
     * 
     * @param objectType -
     *            MIB object type to be mapped.
     * 
     * @return RRD type string or NULL object type is not supported.
     */
    public static String mapType(String objectType) {
        String rrdType = null;
        objectType = objectType.toLowerCase();
        int index;
        for (index = 0; index < supportedObjectTypes.length; index++) {
            if (objectType.startsWith(supportedObjectTypes[index])) break;
        }
        switch(index) {
            case COUNTER_INDEX:
                rrdType = DST_COUNTER;
                break;
            case OCTETSTRING_INDEX:
            case TIMETICKS_INDEX:
            case INTEGER_INDEX:
            case GAUGE_INDEX:
                rrdType = DST_GAUGE;
                break;
            default:
                rrdType = null;
                break;
        }
        return rrdType;
    }

    /**
     * Constructor
     */
    public JMXDataSource() {
        super();
        m_type = null;
        m_heartbeat = 600;
        m_min = "U";
        m_max = "U";
    }

    public JMXDataSource(MibObject obj, String collectionName) {
        m_collectionName = collectionName;
        Category log = ThreadCategory.getInstance(getClass());
        this.setHeartbeat(2 * DataCollectionConfigFactory.getInstance().getStep(collectionName));
        if (this.getName().length() > MAX_DS_NAME_LENGTH) {
            if (log.isEnabledFor(Level.WARN)) log.warn("buildDataSourceList: Mib object name/alias '" + obj.getAlias() + "' exceeds 19 char maximum for RRD data source names, truncating.");
            char[] temp = this.getName().toCharArray();
            this.setName(String.copyValueOf(temp, 0, MAX_DS_NAME_LENGTH));
        }
        this.setType(JMXDataSource.mapType(obj.getType()));
        this.m_min = "U";
        this.m_max = "U";
        if (log.isDebugEnabled()) {
            log.debug("buildDataSourceList: ds_name: " + this.getName() + " ds_oid: " + this.getOid() + "." + this.getInstance() + " ds_max: " + this.getMax() + " ds_min: " + this.getMin());
        }
    }

    /**
     * This method is used to assign the object's identifier.
     * 
     * @param oid -
     *            object identifier in dotted decimal notation (e.g.,
     *            ".1.3.6.1.2.1.1.1")
     */
    public void setOid(String oid) {
        m_oid = oid;
    }

    /**
     * This method is used to assign the object's instance id.
     * 
     * @param instance -
     *            instance identifier (to be appended to oid)
     */
    public void setInstance(String instance) {
        m_instance = instance;
    }

    /**
     * This method is used to assign the data source name.
     * 
     * @param alias -
     *            object alias (e.g., "sysDescription")
     */
    public void setName(String name) {
        m_name = name;
    }

    /**
     * Returns the object's identifier.
     * 
     * @return The object's identifier string.
     */
    public String getOid() {
        return m_oid;
    }

    /**
     * Returns the object's instance id.
     * 
     * @return The object's instance id string.
     */
    public String getInstance() {
        return m_instance;
    }

    /**
     * Returns the object's name.
     * 
     * @return The object's name.
     */
    public String getName() {
        return m_name;
    }

    /**
     * Class copy constructor. Constructs a new object that is an identical to
     * the passed object, however no data is shared between the two objects. Any
     * changes to one will not affect the other.
     * 
     * @param second
     *            The object to make a duplicate of.
     * 
     */
    public JMXDataSource(JMXDataSource second) {
        m_oid = second.m_oid;
        m_instance = second.m_instance;
        m_name = second.m_name;
        m_type = second.m_type;
        m_heartbeat = second.m_heartbeat;
        m_min = second.m_min;
        m_max = second.m_max;
    }

    /**
     * This method is used to assign the object's expected data type.
     * 
     * @param type -
     *            object's data type
     */
    public void setType(String type) {
        m_type = type;
    }

    public void setHeartbeat(int heartbeat) {
        m_heartbeat = heartbeat;
    }

    public void setMin(String minimum) {
        m_min = minimum;
    }

    public void setMax(String maximum) {
        m_max = maximum;
    }

    /**
     * Returns the object's data type.
     * 
     * @return The object's data type
     */
    public String getType() {
        return m_type;
    }

    public int getHeartbeat() {
        return m_heartbeat;
    }

    public String getMin() {
        return m_min;
    }

    public String getMax() {
        return m_max;
    }

    /**
     * Used to get a duplicate of self. The duplicate is identical to self but
     * shares no common data.
     * 
     * @return A newly created copy of self.
     */
    public Object clone() {
        return new JMXDataSource(this);
    }

    /**
     * This method is responsible for returning a String object which represents
     * the content of this RRDDataSource object. Primarily used for debugging
     * purposes.
     * 
     * @return String which represents the content of this RRDDataSource object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\n   oid:       ").append(m_oid);
        buffer.append("\n   name: 	 ").append(m_name);
        buffer.append("\n   type:      ").append(m_type);
        buffer.append("\n   heartbeat: ").append(m_heartbeat);
        buffer.append("\n   min:       ").append(m_min);
        buffer.append("\n   max:       ").append(m_max);
        return buffer.toString();
    }

    public boolean performUpdate(String owner, File repository, SnmpValue value) {
        String val = getStorableValue(value);
        String collectionName = m_collectionName;
        int step = DataCollectionConfigFactory.getInstance().getStep(collectionName);
        List<String> rraList = DataCollectionConfigFactory.getInstance().getRRAList(collectionName);
        boolean result = false;
        try {
            RrdUtils.createRRD(owner, repository.getAbsolutePath(), getName(), step, getType(), getHeartbeat(), getMin(), getMax(), rraList);
            RrdUtils.updateRRD(owner, repository.getAbsolutePath(), getName(), val);
        } catch (RrdException e) {
            result = true;
        }
        return result;
    }

    public String getStorableValue(SnmpValue snmpVal) {
        return (snmpVal == null ? null : Long.toString(snmpVal.toLong()));
    }
}

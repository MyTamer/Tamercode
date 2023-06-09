package org.apache.ddlutils.dynabean;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.ddlutils.model.Table;

/**
 * SqlDynaClass is a DynaClass which is associated with a persistent 
 * Table in a Database.
 * 
 * @version $Revision: 463757 $
 */
public class SqlDynaClass extends BasicDynaClass {

    /** Unique ID for serializaion purposes. */
    private static final long serialVersionUID = -5768155698352911245L;

    /** The table for which this dyna class is defined. */
    private Table _table;

    /** The primary key dyna properties. */
    private SqlDynaProperty[] _primaryKeyProperties;

    /** The non-primary key dyna properties. */
    private SqlDynaProperty[] _nonPrimaryKeyProperties;

    /**
     * Factory method for creating and initializing a new dyna class instance
     * for the given table.
     * 
     * @param table The table
     * @return The dyna class for the table
     */
    public static SqlDynaClass newInstance(Table table) {
        List properties = new ArrayList();
        for (int idx = 0; idx < table.getColumnCount(); idx++) {
            properties.add(new SqlDynaProperty(table.getColumn(idx)));
        }
        SqlDynaProperty[] array = new SqlDynaProperty[properties.size()];
        properties.toArray(array);
        return new SqlDynaClass(table, array);
    }

    /**
     * Creates a new dyna class application for the given table that has the given properties.
     * 
     * @param table      The table
     * @param properties The dyna properties
     */
    public SqlDynaClass(Table table, SqlDynaProperty[] properties) {
        super(table.getName(), SqlDynaBean.class, properties);
        _table = table;
    }

    /**
     * Returns the table for which this dyna class is defined.
     * 
     * @return The table
     */
    public Table getTable() {
        return _table;
    }

    /**
     * Returns the table name for which this dyna class is defined.
     * 
     * @return The table name
     */
    public String getTableName() {
        return getTable().getName();
    }

    /**
     * Returns the properties of this dyna class.
     * 
     * @return The properties
     */
    public SqlDynaProperty[] getSqlDynaProperties() {
        return (SqlDynaProperty[]) getDynaProperties();
    }

    /**
     * Returns the properties for the primary keys of the corresponding table.
     * 
     * @return The properties
     */
    public SqlDynaProperty[] getPrimaryKeyProperties() {
        if (_primaryKeyProperties == null) {
            initPrimaryKeys();
        }
        return _primaryKeyProperties;
    }

    /**
     * Returns the properties for the non-primary keys of the corresponding table.
     * 
     * @return The properties
     */
    public SqlDynaProperty[] getNonPrimaryKeyProperties() {
        if (_nonPrimaryKeyProperties == null) {
            initPrimaryKeys();
        }
        return _nonPrimaryKeyProperties;
    }

    /**
     * Initializes the primary key and non primary key property arrays.
     */
    protected void initPrimaryKeys() {
        List pkProps = new ArrayList();
        List nonPkProps = new ArrayList();
        DynaProperty[] properties = getDynaProperties();
        for (int idx = 0; idx < properties.length; idx++) {
            if (properties[idx] instanceof SqlDynaProperty) {
                SqlDynaProperty sqlProperty = (SqlDynaProperty) properties[idx];
                if (sqlProperty.isPrimaryKey()) {
                    pkProps.add(sqlProperty);
                } else {
                    nonPkProps.add(sqlProperty);
                }
            }
        }
        _primaryKeyProperties = (SqlDynaProperty[]) pkProps.toArray(new SqlDynaProperty[pkProps.size()]);
        _nonPrimaryKeyProperties = (SqlDynaProperty[]) nonPkProps.toArray(new SqlDynaProperty[nonPkProps.size()]);
    }
}

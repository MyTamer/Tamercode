package org.openXpertya.model;

import java.util.*;
import java.sql.*;
import java.math.*;
import org.openXpertya.util.*;

/** Modelo Generado por AD_WF_ActivityResult
 *  @author Comunidad de Desarrollo openXpertya*         *Basado en Codigo Original Modificado, Revisado y Optimizado de:*         * Jorg Janke 
 *  @version  - 2008-01-03 10:26:25.906 */
public class X_AD_WF_ActivityResult extends PO {

    /** Constructor estándar */
    public X_AD_WF_ActivityResult(Properties ctx, int AD_WF_ActivityResult_ID, String trxName) {
        super(ctx, AD_WF_ActivityResult_ID, trxName);
    }

    /** Load Constructor */
    public X_AD_WF_ActivityResult(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AD_Table_ID=650 */
    public static final int Table_ID = 650;

    /** TableName=AD_WF_ActivityResult */
    public static final String Table_Name = "AD_WF_ActivityResult";

    protected static KeyNamePair Model = new KeyNamePair(650, "AD_WF_ActivityResult");

    protected static BigDecimal AccessLevel = new BigDecimal(7);

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID);
        return poi;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_AD_WF_ActivityResult[").append(getID()).append("]");
        return sb.toString();
    }

    /** Set Workflow Activity Result.
Result of the Workflow Process Activity */
    public void setAD_WF_ActivityResult_ID(int AD_WF_ActivityResult_ID) {
        set_ValueNoCheck("AD_WF_ActivityResult_ID", new Integer(AD_WF_ActivityResult_ID));
    }

    /** Get Workflow Activity Result.
Result of the Workflow Process Activity */
    public int getAD_WF_ActivityResult_ID() {
        Integer ii = (Integer) get_Value("AD_WF_ActivityResult_ID");
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Workflow Activity.
Workflow Activity */
    public void setAD_WF_Activity_ID(int AD_WF_Activity_ID) {
        set_ValueNoCheck("AD_WF_Activity_ID", new Integer(AD_WF_Activity_ID));
    }

    /** Get Workflow Activity.
Workflow Activity */
    public int getAD_WF_Activity_ID() {
        Integer ii = (Integer) get_Value("AD_WF_Activity_ID");
        if (ii == null) return 0;
        return ii.intValue();
    }

    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(getID(), String.valueOf(getAD_WF_Activity_ID()));
    }

    /** Set Attribute Name.
Name of the Attribute */
    public void setAttributeName(String AttributeName) {
        if (AttributeName == null) throw new IllegalArgumentException("AttributeName is mandatory");
        if (AttributeName.length() > 60) {
            log.warning("Length > 60 - truncated");
            AttributeName = AttributeName.substring(0, 59);
        }
        set_Value("AttributeName", AttributeName);
    }

    /** Get Attribute Name.
Name of the Attribute */
    public String getAttributeName() {
        return (String) get_Value("AttributeName");
    }

    /** Set Attribute Value.
Value of the Attribute */
    public void setAttributeValue(String AttributeValue) {
        if (AttributeValue != null && AttributeValue.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            AttributeValue = AttributeValue.substring(0, 1999);
        }
        set_Value("AttributeValue", AttributeValue);
    }

    /** Get Attribute Value.
Value of the Attribute */
    public String getAttributeValue() {
        return (String) get_Value("AttributeValue");
    }

    /** Set Description.
Optional short description of the record */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 254);
        }
        set_Value("Description", Description);
    }

    /** Get Description.
Optional short description of the record */
    public String getDescription() {
        return (String) get_Value("Description");
    }

    /** Set Comment/Help.
Comment or Hint */
    public void setHelp(String Help) {
        if (Help != null && Help.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            Help = Help.substring(0, 1999);
        }
        set_Value("Help", Help);
    }

    /** Get Comment/Help.
Comment or Hint */
    public String getHelp() {
        return (String) get_Value("Help");
    }
}

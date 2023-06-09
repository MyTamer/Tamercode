package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for AD_PrintColor
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_AD_PrintColor extends PO implements I_AD_PrintColor, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_AD_PrintColor(Properties ctx, int AD_PrintColor_ID, String trxName) {
        super(ctx, AD_PrintColor_ID, trxName);
    }

    /** Load Constructor */
    public X_AD_PrintColor(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 6 - System - Client 
      */
    protected int get_AccessLevel() {
        return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO(Properties ctx) {
        POInfo poi = POInfo.getPOInfo(ctx, Table_ID, get_TrxName());
        return poi;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("X_AD_PrintColor[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Print Color.
		@param AD_PrintColor_ID 
		Color used for printing and display
	  */
    public void setAD_PrintColor_ID(int AD_PrintColor_ID) {
        if (AD_PrintColor_ID < 1) throw new IllegalArgumentException("AD_PrintColor_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_AD_PrintColor_ID, Integer.valueOf(AD_PrintColor_ID));
    }

    /** Get Print Color.
		@return Color used for printing and display
	  */
    public int getAD_PrintColor_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_AD_PrintColor_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Validation code.
		@param Code 
		Validation Code
	  */
    public void setCode(String Code) {
        if (Code == null) throw new IllegalArgumentException("Code is mandatory.");
        if (Code.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            Code = Code.substring(0, 2000);
        }
        set_Value(COLUMNNAME_Code, Code);
    }

    /** Get Validation code.
		@return Validation Code
	  */
    public String getCode() {
        return (String) get_Value(COLUMNNAME_Code);
    }

    /** Set Default.
		@param IsDefault 
		Default value
	  */
    public void setIsDefault(boolean IsDefault) {
        set_Value(COLUMNNAME_IsDefault, Boolean.valueOf(IsDefault));
    }

    /** Get Default.
		@return Default value
	  */
    public boolean isDefault() {
        Object oo = get_Value(COLUMNNAME_IsDefault);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
    public void setName(String Name) {
        if (Name == null) throw new IllegalArgumentException("Name is mandatory.");
        if (Name.length() > 60) {
            log.warning("Length > 60 - truncated");
            Name = Name.substring(0, 60);
        }
        set_Value(COLUMNNAME_Name, Name);
    }

    /** Get Name.
		@return Alphanumeric identifier of the entity
	  */
    public String getName() {
        return (String) get_Value(COLUMNNAME_Name);
    }

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), getName());
    }
}

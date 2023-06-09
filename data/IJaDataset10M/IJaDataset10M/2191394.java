package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for R_StatusCategory
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_R_StatusCategory extends PO implements I_R_StatusCategory, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_R_StatusCategory(Properties ctx, int R_StatusCategory_ID, String trxName) {
        super(ctx, R_StatusCategory_ID, trxName);
    }

    /** Load Constructor */
    public X_R_StatusCategory(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_R_StatusCategory[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** Set Description.
		@param Description 
		Optional short description of the record
	  */
    public void setDescription(String Description) {
        if (Description != null && Description.length() > 255) {
            log.warning("Length > 255 - truncated");
            Description = Description.substring(0, 255);
        }
        set_Value(COLUMNNAME_Description, Description);
    }

    /** Get Description.
		@return Optional short description of the record
	  */
    public String getDescription() {
        return (String) get_Value(COLUMNNAME_Description);
    }

    /** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
    public void setHelp(String Help) {
        if (Help != null && Help.length() > 2000) {
            log.warning("Length > 2000 - truncated");
            Help = Help.substring(0, 2000);
        }
        set_Value(COLUMNNAME_Help, Help);
    }

    /** Get Comment/Help.
		@return Comment or Hint
	  */
    public String getHelp() {
        return (String) get_Value(COLUMNNAME_Help);
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
        if (Name.length() > 120) {
            log.warning("Length > 120 - truncated");
            Name = Name.substring(0, 120);
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

    /** Set Status Category.
		@param R_StatusCategory_ID 
		Request Status Category
	  */
    public void setR_StatusCategory_ID(int R_StatusCategory_ID) {
        if (R_StatusCategory_ID < 1) throw new IllegalArgumentException("R_StatusCategory_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_R_StatusCategory_ID, Integer.valueOf(R_StatusCategory_ID));
    }

    /** Get Status Category.
		@return Request Status Category
	  */
    public int getR_StatusCategory_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_R_StatusCategory_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }
}

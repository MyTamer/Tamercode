package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_Product_Category
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_M_Product_Category extends PO implements I_M_Product_Category, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_M_Product_Category(Properties ctx, int M_Product_Category_ID, String trxName) {
        super(ctx, M_Product_Category_ID, trxName);
    }

    /** Load Constructor */
    public X_M_Product_Category(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
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
        StringBuffer sb = new StringBuffer("X_M_Product_Category[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_AD_PrintColor getAD_PrintColor() throws Exception {
        Class<?> clazz = MTable.getClass(I_AD_PrintColor.Table_Name);
        I_AD_PrintColor result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_AD_PrintColor) constructor.newInstance(new Object[] { getCtx(), new Integer(getAD_PrintColor_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Print Color.
		@param AD_PrintColor_ID 
		Color used for printing and display
	  */
    public void setAD_PrintColor_ID(int AD_PrintColor_ID) {
        if (AD_PrintColor_ID <= 0) set_Value(COLUMNNAME_AD_PrintColor_ID, null); else set_Value(COLUMNNAME_AD_PrintColor_ID, Integer.valueOf(AD_PrintColor_ID));
    }

    /** Get Print Color.
		@return Color used for printing and display
	  */
    public int getAD_PrintColor_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_AD_PrintColor_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_A_Asset_Group getA_Asset_Group() throws Exception {
        Class<?> clazz = MTable.getClass(I_A_Asset_Group.Table_Name);
        I_A_Asset_Group result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_A_Asset_Group) constructor.newInstance(new Object[] { getCtx(), new Integer(getA_Asset_Group_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Asset Group.
		@param A_Asset_Group_ID 
		Group of Assets
	  */
    public void setA_Asset_Group_ID(int A_Asset_Group_ID) {
        if (A_Asset_Group_ID <= 0) set_Value(COLUMNNAME_A_Asset_Group_ID, null); else set_Value(COLUMNNAME_A_Asset_Group_ID, Integer.valueOf(A_Asset_Group_ID));
    }

    /** Get Asset Group.
		@return Group of Assets
	  */
    public int getA_Asset_Group_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_A_Asset_Group_ID);
        if (ii == null) return 0;
        return ii.intValue();
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

    /** Set Self-Service.
		@param IsSelfService 
		This is a Self-Service entry or this entry can be changed via Self-Service
	  */
    public void setIsSelfService(boolean IsSelfService) {
        set_Value(COLUMNNAME_IsSelfService, Boolean.valueOf(IsSelfService));
    }

    /** Get Self-Service.
		@return This is a Self-Service entry or this entry can be changed via Self-Service
	  */
    public boolean isSelfService() {
        Object oo = get_Value(COLUMNNAME_IsSelfService);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** MMPolicy AD_Reference_ID=335 */
    public static final int MMPOLICY_AD_Reference_ID = 335;

    /** LiFo = L */
    public static final String MMPOLICY_LiFo = "L";

    /** FiFo = F */
    public static final String MMPOLICY_FiFo = "F";

    /** Set Material Policy.
		@param MMPolicy 
		Material Movement Policy
	  */
    public void setMMPolicy(String MMPolicy) {
        if (MMPolicy == null) throw new IllegalArgumentException("MMPolicy is mandatory");
        if (MMPolicy.equals("L") || MMPolicy.equals("F")) ; else throw new IllegalArgumentException("MMPolicy Invalid value - " + MMPolicy + " - Reference_ID=335 - L - F");
        if (MMPolicy.length() > 1) {
            log.warning("Length > 1 - truncated");
            MMPolicy = MMPolicy.substring(0, 1);
        }
        set_Value(COLUMNNAME_MMPolicy, MMPolicy);
    }

    /** Get Material Policy.
		@return Material Movement Policy
	  */
    public String getMMPolicy() {
        return (String) get_Value(COLUMNNAME_MMPolicy);
    }

    /** Set Product Category.
		@param M_Product_Category_ID 
		Category of a Product
	  */
    public void setM_Product_Category_ID(int M_Product_Category_ID) {
        if (M_Product_Category_ID < 1) throw new IllegalArgumentException("M_Product_Category_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_Product_Category_ID, Integer.valueOf(M_Product_Category_ID));
    }

    /** Get Product Category.
		@return Category of a Product
	  */
    public int getM_Product_Category_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_Product_Category_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** M_Product_Category_Parent_ID AD_Reference_ID=163 */
    public static final int M_PRODUCT_CATEGORY_PARENT_ID_AD_Reference_ID = 163;

    /** Set Parent Product Category.
		@param M_Product_Category_Parent_ID Parent Product Category	  */
    public void setM_Product_Category_Parent_ID(int M_Product_Category_Parent_ID) {
        if (M_Product_Category_Parent_ID <= 0) set_Value(COLUMNNAME_M_Product_Category_Parent_ID, null); else set_Value(COLUMNNAME_M_Product_Category_Parent_ID, Integer.valueOf(M_Product_Category_Parent_ID));
    }

    /** Get Parent Product Category.
		@return Parent Product Category	  */
    public int getM_Product_Category_Parent_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_Product_Category_Parent_ID);
        if (ii == null) return 0;
        return ii.intValue();
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

    /** Set Planned Margin %.
		@param PlannedMargin 
		Project's planned margin as a percentage
	  */
    public void setPlannedMargin(BigDecimal PlannedMargin) {
        if (PlannedMargin == null) throw new IllegalArgumentException("PlannedMargin is mandatory.");
        set_Value(COLUMNNAME_PlannedMargin, PlannedMargin);
    }

    /** Get Planned Margin %.
		@return Project's planned margin as a percentage
	  */
    public BigDecimal getPlannedMargin() {
        BigDecimal bd = (BigDecimal) get_Value(COLUMNNAME_PlannedMargin);
        if (bd == null) return Env.ZERO;
        return bd;
    }

    /** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
    public void setValue(String Value) {
        if (Value == null) throw new IllegalArgumentException("Value is mandatory.");
        if (Value.length() > 40) {
            log.warning("Length > 40 - truncated");
            Value = Value.substring(0, 40);
        }
        set_Value(COLUMNNAME_Value, Value);
    }

    /** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
    public String getValue() {
        return (String) get_Value(COLUMNNAME_Value);
    }
}

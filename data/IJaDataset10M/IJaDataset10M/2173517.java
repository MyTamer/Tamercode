package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for M_Freight
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_M_Freight extends PO implements I_M_Freight, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_M_Freight(Properties ctx, int M_Freight_ID, String trxName) {
        super(ctx, M_Freight_ID, trxName);
    }

    /** Load Constructor */
    public X_M_Freight(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_M_Freight[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_C_Country getC_Country() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_Country.Table_Name);
        I_C_Country result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_Country) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_Country_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Country.
		@param C_Country_ID 
		Country 
	  */
    public void setC_Country_ID(int C_Country_ID) {
        if (C_Country_ID <= 0) set_Value(COLUMNNAME_C_Country_ID, null); else set_Value(COLUMNNAME_C_Country_ID, Integer.valueOf(C_Country_ID));
    }

    /** Get Country.
		@return Country 
	  */
    public int getC_Country_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_Country_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_C_Currency getC_Currency() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_Currency.Table_Name);
        I_C_Currency result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_Currency) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_Currency_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
    public void setC_Currency_ID(int C_Currency_ID) {
        if (C_Currency_ID < 1) throw new IllegalArgumentException("C_Currency_ID is mandatory.");
        set_Value(COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
    }

    /** Get Currency.
		@return The Currency for this record
	  */
    public int getC_Currency_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_Currency_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_C_Region getC_Region() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_Region.Table_Name);
        I_C_Region result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_Region) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_Region_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Region.
		@param C_Region_ID 
		Identifies a geographical Region
	  */
    public void setC_Region_ID(int C_Region_ID) {
        if (C_Region_ID <= 0) set_Value(COLUMNNAME_C_Region_ID, null); else set_Value(COLUMNNAME_C_Region_ID, Integer.valueOf(C_Region_ID));
    }

    /** Get Region.
		@return Identifies a geographical Region
	  */
    public int getC_Region_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_Region_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Freight Amount.
		@param FreightAmt 
		Freight Amount 
	  */
    public void setFreightAmt(BigDecimal FreightAmt) {
        if (FreightAmt == null) throw new IllegalArgumentException("FreightAmt is mandatory.");
        set_Value(COLUMNNAME_FreightAmt, FreightAmt);
    }

    /** Get Freight Amount.
		@return Freight Amount 
	  */
    public BigDecimal getFreightAmt() {
        BigDecimal bd = (BigDecimal) get_Value(COLUMNNAME_FreightAmt);
        if (bd == null) return Env.ZERO;
        return bd;
    }

    public I_M_FreightCategory getM_FreightCategory() throws Exception {
        Class<?> clazz = MTable.getClass(I_M_FreightCategory.Table_Name);
        I_M_FreightCategory result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_M_FreightCategory) constructor.newInstance(new Object[] { getCtx(), new Integer(getM_FreightCategory_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Freight Category.
		@param M_FreightCategory_ID 
		Category of the Freight
	  */
    public void setM_FreightCategory_ID(int M_FreightCategory_ID) {
        if (M_FreightCategory_ID < 1) throw new IllegalArgumentException("M_FreightCategory_ID is mandatory.");
        set_Value(COLUMNNAME_M_FreightCategory_ID, Integer.valueOf(M_FreightCategory_ID));
    }

    /** Get Freight Category.
		@return Category of the Freight
	  */
    public int getM_FreightCategory_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_FreightCategory_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Freight.
		@param M_Freight_ID 
		Freight Rate
	  */
    public void setM_Freight_ID(int M_Freight_ID) {
        if (M_Freight_ID < 1) throw new IllegalArgumentException("M_Freight_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_Freight_ID, Integer.valueOf(M_Freight_ID));
    }

    /** Get Freight.
		@return Freight Rate
	  */
    public int getM_Freight_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_Freight_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_M_Shipper getM_Shipper() throws Exception {
        Class<?> clazz = MTable.getClass(I_M_Shipper.Table_Name);
        I_M_Shipper result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_M_Shipper) constructor.newInstance(new Object[] { getCtx(), new Integer(getM_Shipper_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Shipper.
		@param M_Shipper_ID 
		Method or manner of product delivery
	  */
    public void setM_Shipper_ID(int M_Shipper_ID) {
        if (M_Shipper_ID < 1) throw new IllegalArgumentException("M_Shipper_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_Shipper_ID, Integer.valueOf(M_Shipper_ID));
    }

    /** Get Shipper.
		@return Method or manner of product delivery
	  */
    public int getM_Shipper_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_Shipper_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), String.valueOf(getM_Shipper_ID()));
    }

    /** To_Country_ID AD_Reference_ID=156 */
    public static final int TO_COUNTRY_ID_AD_Reference_ID = 156;

    /** Set To.
		@param To_Country_ID 
		Receiving Country
	  */
    public void setTo_Country_ID(int To_Country_ID) {
        if (To_Country_ID <= 0) set_Value(COLUMNNAME_To_Country_ID, null); else set_Value(COLUMNNAME_To_Country_ID, Integer.valueOf(To_Country_ID));
    }

    /** Get To.
		@return Receiving Country
	  */
    public int getTo_Country_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_To_Country_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** To_Region_ID AD_Reference_ID=157 */
    public static final int TO_REGION_ID_AD_Reference_ID = 157;

    /** Set To.
		@param To_Region_ID 
		Receiving Region
	  */
    public void setTo_Region_ID(int To_Region_ID) {
        if (To_Region_ID <= 0) set_Value(COLUMNNAME_To_Region_ID, null); else set_Value(COLUMNNAME_To_Region_ID, Integer.valueOf(To_Region_ID));
    }

    /** Get To.
		@return Receiving Region
	  */
    public int getTo_Region_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_To_Region_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Valid from.
		@param ValidFrom 
		Valid from including this date (first day)
	  */
    public void setValidFrom(Timestamp ValidFrom) {
        if (ValidFrom == null) throw new IllegalArgumentException("ValidFrom is mandatory.");
        set_Value(COLUMNNAME_ValidFrom, ValidFrom);
    }

    /** Get Valid from.
		@return Valid from including this date (first day)
	  */
    public Timestamp getValidFrom() {
        return (Timestamp) get_Value(COLUMNNAME_ValidFrom);
    }
}

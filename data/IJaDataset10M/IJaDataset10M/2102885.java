package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_Tax
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_C_Tax extends PO implements I_C_Tax, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_Tax(Properties ctx, int C_Tax_ID, String trxName) {
        super(ctx, C_Tax_ID, trxName);
    }

    /** Load Constructor */
    public X_C_Tax(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 2 - Client 
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
        StringBuffer sb = new StringBuffer("X_C_Tax[").append(get_ID()).append("]");
        return sb.toString();
    }

    /** C_Country_ID AD_Reference_ID=156 */
    public static final int C_COUNTRY_ID_AD_Reference_ID = 156;

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

    /** C_Region_ID AD_Reference_ID=157 */
    public static final int C_REGION_ID_AD_Reference_ID = 157;

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

    public I_C_TaxCategory getC_TaxCategory() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_TaxCategory.Table_Name);
        I_C_TaxCategory result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_TaxCategory) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_TaxCategory_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Tax Category.
		@param C_TaxCategory_ID 
		Tax Category
	  */
    public void setC_TaxCategory_ID(int C_TaxCategory_ID) {
        if (C_TaxCategory_ID < 1) throw new IllegalArgumentException("C_TaxCategory_ID is mandatory.");
        set_Value(COLUMNNAME_C_TaxCategory_ID, Integer.valueOf(C_TaxCategory_ID));
    }

    /** Get Tax Category.
		@return Tax Category
	  */
    public int getC_TaxCategory_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_TaxCategory_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Tax.
		@param C_Tax_ID 
		Tax identifier
	  */
    public void setC_Tax_ID(int C_Tax_ID) {
        if (C_Tax_ID < 1) throw new IllegalArgumentException("C_Tax_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_C_Tax_ID, Integer.valueOf(C_Tax_ID));
    }

    /** Get Tax.
		@return Tax identifier
	  */
    public int getC_Tax_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_Tax_ID);
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

    /** Set Document Level.
		@param IsDocumentLevel 
		Tax is calculated on document level (rather than line by line)
	  */
    public void setIsDocumentLevel(boolean IsDocumentLevel) {
        set_Value(COLUMNNAME_IsDocumentLevel, Boolean.valueOf(IsDocumentLevel));
    }

    /** Get Document Level.
		@return Tax is calculated on document level (rather than line by line)
	  */
    public boolean isDocumentLevel() {
        Object oo = get_Value(COLUMNNAME_IsDocumentLevel);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Sales Tax.
		@param IsSalesTax 
		This is a sales tax (i.e. not a value added tax)
	  */
    public void setIsSalesTax(boolean IsSalesTax) {
        set_Value(COLUMNNAME_IsSalesTax, Boolean.valueOf(IsSalesTax));
    }

    /** Get Sales Tax.
		@return This is a sales tax (i.e. not a value added tax)
	  */
    public boolean isSalesTax() {
        Object oo = get_Value(COLUMNNAME_IsSalesTax);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Summary Level.
		@param IsSummary 
		This is a summary entity
	  */
    public void setIsSummary(boolean IsSummary) {
        set_Value(COLUMNNAME_IsSummary, Boolean.valueOf(IsSummary));
    }

    /** Get Summary Level.
		@return This is a summary entity
	  */
    public boolean isSummary() {
        Object oo = get_Value(COLUMNNAME_IsSummary);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** Set Tax exempt.
		@param IsTaxExempt 
		Business partner is exempt from tax
	  */
    public void setIsTaxExempt(boolean IsTaxExempt) {
        set_Value(COLUMNNAME_IsTaxExempt, Boolean.valueOf(IsTaxExempt));
    }

    /** Get Tax exempt.
		@return Business partner is exempt from tax
	  */
    public boolean isTaxExempt() {
        Object oo = get_Value(COLUMNNAME_IsTaxExempt);
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

    /** Parent_Tax_ID AD_Reference_ID=158 */
    public static final int PARENT_TAX_ID_AD_Reference_ID = 158;

    /** Set Parent Tax.
		@param Parent_Tax_ID 
		Parent Tax indicates a tax that is made up of multiple taxes
	  */
    public void setParent_Tax_ID(int Parent_Tax_ID) {
        if (Parent_Tax_ID <= 0) set_Value(COLUMNNAME_Parent_Tax_ID, null); else set_Value(COLUMNNAME_Parent_Tax_ID, Integer.valueOf(Parent_Tax_ID));
    }

    /** Get Parent Tax.
		@return Parent Tax indicates a tax that is made up of multiple taxes
	  */
    public int getParent_Tax_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_Parent_Tax_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Rate.
		@param Rate 
		Rate or Tax or Exchange
	  */
    public void setRate(BigDecimal Rate) {
        if (Rate == null) throw new IllegalArgumentException("Rate is mandatory.");
        set_Value(COLUMNNAME_Rate, Rate);
    }

    /** Get Rate.
		@return Rate or Tax or Exchange
	  */
    public BigDecimal getRate() {
        BigDecimal bd = (BigDecimal) get_Value(COLUMNNAME_Rate);
        if (bd == null) return Env.ZERO;
        return bd;
    }

    /** Set Requires Tax Certificate.
		@param RequiresTaxCertificate 
		This tax rate requires the Business Partner to be tax exempt
	  */
    public void setRequiresTaxCertificate(boolean RequiresTaxCertificate) {
        set_Value(COLUMNNAME_RequiresTaxCertificate, Boolean.valueOf(RequiresTaxCertificate));
    }

    /** Get Requires Tax Certificate.
		@return This tax rate requires the Business Partner to be tax exempt
	  */
    public boolean isRequiresTaxCertificate() {
        Object oo = get_Value(COLUMNNAME_RequiresTaxCertificate);
        if (oo != null) {
            if (oo instanceof Boolean) return ((Boolean) oo).booleanValue();
            return "Y".equals(oo);
        }
        return false;
    }

    /** SOPOType AD_Reference_ID=287 */
    public static final int SOPOTYPE_AD_Reference_ID = 287;

    /** Both = B */
    public static final String SOPOTYPE_Both = "B";

    /** Sales Tax = S */
    public static final String SOPOTYPE_SalesTax = "S";

    /** Purchase Tax = P */
    public static final String SOPOTYPE_PurchaseTax = "P";

    /** Set SO/PO Type.
		@param SOPOType 
		Sales Tax applies to sales situations, Purchase Tax to purchase situations
	  */
    public void setSOPOType(String SOPOType) {
        if (SOPOType == null) throw new IllegalArgumentException("SOPOType is mandatory");
        if (SOPOType.equals("B") || SOPOType.equals("S") || SOPOType.equals("P")) ; else throw new IllegalArgumentException("SOPOType Invalid value - " + SOPOType + " - Reference_ID=287 - B - S - P");
        if (SOPOType.length() > 1) {
            log.warning("Length > 1 - truncated");
            SOPOType = SOPOType.substring(0, 1);
        }
        set_Value(COLUMNNAME_SOPOType, SOPOType);
    }

    /** Get SO/PO Type.
		@return Sales Tax applies to sales situations, Purchase Tax to purchase situations
	  */
    public String getSOPOType() {
        return (String) get_Value(COLUMNNAME_SOPOType);
    }

    /** Set Tax Indicator.
		@param TaxIndicator 
		Short form for Tax to be printed on documents
	  */
    public void setTaxIndicator(String TaxIndicator) {
        if (TaxIndicator != null && TaxIndicator.length() > 10) {
            log.warning("Length > 10 - truncated");
            TaxIndicator = TaxIndicator.substring(0, 10);
        }
        set_Value(COLUMNNAME_TaxIndicator, TaxIndicator);
    }

    /** Get Tax Indicator.
		@return Short form for Tax to be printed on documents
	  */
    public String getTaxIndicator() {
        return (String) get_Value(COLUMNNAME_TaxIndicator);
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

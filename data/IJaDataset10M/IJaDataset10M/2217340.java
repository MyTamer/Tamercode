package org.compiere.model;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;

/** Generated Model for C_Tax_Acct
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_C_Tax_Acct extends PO implements I_C_Tax_Acct, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_Tax_Acct(Properties ctx, int C_Tax_Acct_ID, String trxName) {
        super(ctx, C_Tax_Acct_ID, trxName);
    }

    /** Load Constructor */
    public X_C_Tax_Acct(Properties ctx, ResultSet rs, String trxName) {
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
        StringBuffer sb = new StringBuffer("X_C_Tax_Acct[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_C_AcctSchema getC_AcctSchema() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_AcctSchema.Table_Name);
        I_C_AcctSchema result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_AcctSchema) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_AcctSchema_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Accounting Schema.
		@param C_AcctSchema_ID 
		Rules for accounting
	  */
    public void setC_AcctSchema_ID(int C_AcctSchema_ID) {
        if (C_AcctSchema_ID < 1) throw new IllegalArgumentException("C_AcctSchema_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_C_AcctSchema_ID, Integer.valueOf(C_AcctSchema_ID));
    }

    /** Get Accounting Schema.
		@return Rules for accounting
	  */
    public int getC_AcctSchema_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_AcctSchema_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    public I_C_Tax getC_Tax() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_Tax.Table_Name);
        I_C_Tax result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_Tax) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_Tax_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
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

    /** Set Tax Credit.
		@param T_Credit_Acct 
		Account for Tax you can reclaim
	  */
    public void setT_Credit_Acct(int T_Credit_Acct) {
        set_Value(COLUMNNAME_T_Credit_Acct, Integer.valueOf(T_Credit_Acct));
    }

    /** Get Tax Credit.
		@return Account for Tax you can reclaim
	  */
    public int getT_Credit_Acct() {
        Integer ii = (Integer) get_Value(COLUMNNAME_T_Credit_Acct);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Tax Due.
		@param T_Due_Acct 
		Account for Tax you have to pay
	  */
    public void setT_Due_Acct(int T_Due_Acct) {
        set_Value(COLUMNNAME_T_Due_Acct, Integer.valueOf(T_Due_Acct));
    }

    /** Get Tax Due.
		@return Account for Tax you have to pay
	  */
    public int getT_Due_Acct() {
        Integer ii = (Integer) get_Value(COLUMNNAME_T_Due_Acct);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Tax Expense.
		@param T_Expense_Acct 
		Account for paid tax you cannot reclaim
	  */
    public void setT_Expense_Acct(int T_Expense_Acct) {
        set_Value(COLUMNNAME_T_Expense_Acct, Integer.valueOf(T_Expense_Acct));
    }

    /** Get Tax Expense.
		@return Account for paid tax you cannot reclaim
	  */
    public int getT_Expense_Acct() {
        Integer ii = (Integer) get_Value(COLUMNNAME_T_Expense_Acct);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Tax Liability.
		@param T_Liability_Acct 
		Account for Tax declaration liability
	  */
    public void setT_Liability_Acct(int T_Liability_Acct) {
        set_Value(COLUMNNAME_T_Liability_Acct, Integer.valueOf(T_Liability_Acct));
    }

    /** Get Tax Liability.
		@return Account for Tax declaration liability
	  */
    public int getT_Liability_Acct() {
        Integer ii = (Integer) get_Value(COLUMNNAME_T_Liability_Acct);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Tax Receivables.
		@param T_Receivables_Acct 
		Account for Tax credit after tax declaration
	  */
    public void setT_Receivables_Acct(int T_Receivables_Acct) {
        set_Value(COLUMNNAME_T_Receivables_Acct, Integer.valueOf(T_Receivables_Acct));
    }

    /** Get Tax Receivables.
		@return Account for Tax credit after tax declaration
	  */
    public int getT_Receivables_Acct() {
        Integer ii = (Integer) get_Value(COLUMNNAME_T_Receivables_Acct);
        if (ii == null) return 0;
        return ii.intValue();
    }
}

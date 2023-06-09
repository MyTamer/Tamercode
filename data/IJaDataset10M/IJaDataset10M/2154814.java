package org.compiere.model;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_ProjectIssueMA
 *  @author Adempiere (generated) 
 *  @version Release 3.3.1t - $Id$ */
public class X_C_ProjectIssueMA extends PO implements I_C_ProjectIssueMA, I_Persistent {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /** Standard Constructor */
    public X_C_ProjectIssueMA(Properties ctx, int C_ProjectIssueMA_ID, String trxName) {
        super(ctx, C_ProjectIssueMA_ID, trxName);
    }

    /** Load Constructor */
    public X_C_ProjectIssueMA(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
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
        StringBuffer sb = new StringBuffer("X_C_ProjectIssueMA[").append(get_ID()).append("]");
        return sb.toString();
    }

    public I_C_ProjectIssue getC_ProjectIssue() throws Exception {
        Class<?> clazz = MTable.getClass(I_C_ProjectIssue.Table_Name);
        I_C_ProjectIssue result = null;
        try {
            Constructor<?> constructor = null;
            constructor = clazz.getDeclaredConstructor(new Class[] { Properties.class, int.class, String.class });
            result = (I_C_ProjectIssue) constructor.newInstance(new Object[] { getCtx(), new Integer(getC_ProjectIssue_ID()), get_TrxName() });
        } catch (Exception e) {
            log.log(Level.SEVERE, "(id) - Table=" + Table_Name + ",Class=" + clazz, e);
            log.saveError("Error", "Table=" + Table_Name + ",Class=" + clazz);
            throw e;
        }
        return result;
    }

    /** Set Project Issue.
		@param C_ProjectIssue_ID 
		Project Issues (Material, Labor)
	  */
    public void setC_ProjectIssue_ID(int C_ProjectIssue_ID) {
        if (C_ProjectIssue_ID < 1) throw new IllegalArgumentException("C_ProjectIssue_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_C_ProjectIssue_ID, Integer.valueOf(C_ProjectIssue_ID));
    }

    /** Get Project Issue.
		@return Project Issues (Material, Labor)
	  */
    public int getC_ProjectIssue_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_C_ProjectIssue_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() {
        return new KeyNamePair(get_ID(), String.valueOf(getC_ProjectIssue_ID()));
    }

    /** Set Attribute Set Instance.
		@param M_AttributeSetInstance_ID 
		Product Attribute Set Instance
	  */
    public void setM_AttributeSetInstance_ID(int M_AttributeSetInstance_ID) {
        if (M_AttributeSetInstance_ID < 0) throw new IllegalArgumentException("M_AttributeSetInstance_ID is mandatory.");
        set_ValueNoCheck(COLUMNNAME_M_AttributeSetInstance_ID, Integer.valueOf(M_AttributeSetInstance_ID));
    }

    /** Get Attribute Set Instance.
		@return Product Attribute Set Instance
	  */
    public int getM_AttributeSetInstance_ID() {
        Integer ii = (Integer) get_Value(COLUMNNAME_M_AttributeSetInstance_ID);
        if (ii == null) return 0;
        return ii.intValue();
    }

    /** Set Movement Quantity.
		@param MovementQty 
		Quantity of a product moved.
	  */
    public void setMovementQty(BigDecimal MovementQty) {
        if (MovementQty == null) throw new IllegalArgumentException("MovementQty is mandatory.");
        set_Value(COLUMNNAME_MovementQty, MovementQty);
    }

    /** Get Movement Quantity.
		@return Quantity of a product moved.
	  */
    public BigDecimal getMovementQty() {
        BigDecimal bd = (BigDecimal) get_Value(COLUMNNAME_MovementQty);
        if (bd == null) return Env.ZERO;
        return bd;
    }
}

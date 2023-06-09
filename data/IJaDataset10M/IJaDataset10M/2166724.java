package org.compiere.process;

import java.math.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Org Ownership Process
 *	
 *  @author Jorg Janke
 *  @version $Id: OrgOwnership.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class OrgOwnership extends SvrProcess {

    /**	Organization Parameter		*/
    private int p_AD_Org_ID = -1;

    private int p_M_Warehouse_ID = -1;

    private int p_M_Product_Category_ID = -1;

    private int p_M_Product_ID = -1;

    private int p_C_BP_Group_ID = -1;

    private int p_C_BPartner_ID = -1;

    /**
	 *  Prepare - e.g., get Parameters.
	 */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else if (name.equals("AD_Org_ID")) p_AD_Org_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("M_Warehouse_ID")) p_M_Warehouse_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("M_Product_Category_ID")) p_M_Product_Category_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("M_Product_ID")) p_M_Product_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("C_BP_Group_ID")) p_C_BP_Group_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("C_BPartner_ID")) p_C_BPartner_ID = ((BigDecimal) para[i].getParameter()).intValue(); else log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
        }
    }

    /**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
    protected String doIt() throws Exception {
        log.info("doIt - AD_Org_ID=" + p_AD_Org_ID);
        if (p_AD_Org_ID < 0) throw new IllegalArgumentException("OrgOwnership - invalid AD_Org_ID=" + p_AD_Org_ID);
        generalOwnership();
        if (p_M_Warehouse_ID > 0) return warehouseOwnership();
        if (p_M_Product_ID > 0 || p_M_Product_Category_ID > 0) return productOwnership();
        if (p_C_BPartner_ID > 0 || p_C_BP_Group_ID > 0) return bPartnerOwnership();
        return "* Not supported * **";
    }

    /**
	 * 	Set Warehouse Ownership
	 *	@return ""
	 */
    private String warehouseOwnership() {
        log.info("warehouseOwnership - M_Warehouse_ID=" + p_M_Warehouse_ID);
        if (p_AD_Org_ID == 0) throw new IllegalArgumentException("Warehouse - Org cannot be * (0)");
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE M_Warehouse " + "SET AD_Org_ID=").append(p_AD_Org_ID).append(" WHERE M_Warehouse_ID=").append(p_M_Warehouse_ID).append(" AND AD_Client_ID=").append(getAD_Client_ID()).append(" AND AD_Org_ID<>").append(p_AD_Org_ID);
        int no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "M_Warehouse_ID"));
        sql = new StringBuffer();
        sql.append("UPDATE M_Warehouse_Acct " + "SET AD_Org_ID=").append(p_AD_Org_ID).append(" WHERE M_Warehouse_ID=").append(p_M_Warehouse_ID).append(" AND AD_Client_ID=").append(getAD_Client_ID()).append(" AND AD_Org_ID<>").append(p_AD_Org_ID);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
        sql = new StringBuffer();
        sql.append("UPDATE M_Locator " + "SET AD_Org_ID=").append(p_AD_Org_ID).append(" WHERE M_Warehouse_ID=").append(p_M_Warehouse_ID).append(" AND AD_Client_ID=").append(getAD_Client_ID()).append(" AND AD_Org_ID<>").append(p_AD_Org_ID);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "M_Locator_ID"));
        sql = new StringBuffer();
        sql.append("UPDATE M_Storage s " + "SET AD_Org_ID=").append(p_AD_Org_ID).append(" WHERE EXISTS " + "(SELECT * FROM M_Locator l WHERE l.M_Locator_ID=s.M_Locator_ID" + " AND l.M_Warehouse_ID=").append(p_M_Warehouse_ID).append(") AND AD_Client_ID=").append(getAD_Client_ID()).append(" AND AD_Org_ID<>").append(p_AD_Org_ID);
        no = DB.executeUpdate(sql.toString(), get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "Storage"));
        return "";
    }

    /**
	 * 	Product Ownership
	 *	@return ""
	 */
    private String productOwnership() {
        log.info("productOwnership - M_Product_Category_ID=" + p_M_Product_Category_ID + ", M_Product_ID=" + p_M_Product_ID);
        String set = " SET AD_Org_ID=" + p_AD_Org_ID;
        if (p_M_Product_Category_ID > 0) set += " WHERE EXISTS (SELECT * FROM M_Product p" + " WHERE p.M_Product_ID=x.M_Product_ID AND p.M_Product_Category_ID=" + p_M_Product_Category_ID + ")"; else set += " WHERE M_Product_ID=" + p_M_Product_ID;
        set += " AND AD_Client_ID=" + getAD_Client_ID() + " AND AD_Org_ID<>" + p_AD_Org_ID;
        log.fine("productOwnership - " + set);
        String sql = "UPDATE M_Product x " + set;
        int no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "M_Product_ID"));
        sql = "UPDATE M_Product_Acct x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
        sql = "UPDATE M_Product_BOM x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "M_Product_BOM_ID"));
        sql = "UPDATE M_Product_PO x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "PO"));
        sql = "UPDATE M_Product_Trl x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "AD_Language"));
        return "";
    }

    /**
	 * 	Business Partner Ownership
	 *	@return ""
	 */
    private String bPartnerOwnership() {
        log.info("bPartnerOwnership - C_BP_Group_ID=" + p_C_BP_Group_ID + ", C_BPartner_ID=" + p_C_BPartner_ID);
        String set = " SET AD_Org_ID=" + p_AD_Org_ID;
        if (p_C_BP_Group_ID > 0) set += " WHERE EXISTS (SELECT * FROM C_BPartner bp WHERE bp.C_BPartner_ID=x.C_BPartner_ID AND bp.C_BP_Group_ID=" + p_C_BP_Group_ID + ")"; else set += " WHERE C_BPartner_ID=" + p_C_BPartner_ID;
        set += " AND AD_Client_ID=" + getAD_Client_ID() + " AND AD_Org_ID<>" + p_AD_Org_ID;
        log.fine("bPartnerOwnership - " + set);
        String sql = "UPDATE C_BPartner x " + set;
        int no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_BPartner_ID"));
        sql = "UPDATE C_BP_Customer_Acct x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
        sql = "UPDATE C_BP_Employee_Acct x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
        sql = "UPDATE C_BP_Vendor_Acct x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_AcctSchema_ID"));
        sql = "UPDATE C_BPartner_Location x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_BPartner_Location_ID"));
        sql = "UPDATE AD_User x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "AD_User_ID"));
        sql = "UPDATE C_BP_BankAccount x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        addLog(0, null, new BigDecimal(no), Msg.translate(getCtx(), "C_BP_BankAccount_ID"));
        return "";
    }

    /**
	 * 	Set General Ownership (i.e. Org to 0).
	 * 	In general for items with two parents
	 */
    private void generalOwnership() {
        String set = "SET AD_Org_ID=0 WHERE AD_Client_ID=" + getAD_Client_ID() + " AND AD_Org_ID<>0";
        String sql = "UPDATE R_ContactInterest " + set;
        int no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - R_ContactInterest=" + no);
        sql = "UPDATE AD_User_Roles " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - AD_User_Roles=" + no);
        sql = "UPDATE C_BPartner_Product " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - C_BPartner_Product=" + no);
        sql = "UPDATE C_BP_Withholding x " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - C_BP_Withholding=" + no);
        sql = "UPDATE M_Product_Costing " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - M_Product_Costing=" + no);
        sql = "UPDATE M_Replenish " + set;
        no = DB.executeUpdate(sql, get_TrxName());
        if (no != 0) log.fine("generalOwnership - M_Replenish=" + no);
    }
}

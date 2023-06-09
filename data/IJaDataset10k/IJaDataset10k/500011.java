package org.compiere.model;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.util.*;

/**
 *	Aging Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MAging.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MAging extends X_T_Aging {

    /**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param T_Aging_ID id
	 *	@param trxName transaction
	 */
    public MAging(Properties ctx, int T_Aging_ID, String trxName) {
        super(ctx, T_Aging_ID, trxName);
        if (T_Aging_ID == 0) {
            setDueAmt(Env.ZERO);
            setDue0(Env.ZERO);
            setDue0_7(Env.ZERO);
            setDue0_30(Env.ZERO);
            setDue1_7(Env.ZERO);
            setDue31_60(Env.ZERO);
            setDue31_Plus(Env.ZERO);
            setDue61_90(Env.ZERO);
            setDue61_Plus(Env.ZERO);
            setDue8_30(Env.ZERO);
            setDue91_Plus(Env.ZERO);
            setPastDueAmt(Env.ZERO);
            setPastDue1_7(Env.ZERO);
            setPastDue1_30(Env.ZERO);
            setPastDue31_60(Env.ZERO);
            setPastDue31_Plus(Env.ZERO);
            setPastDue61_90(Env.ZERO);
            setPastDue61_Plus(Env.ZERO);
            setPastDue8_30(Env.ZERO);
            setPastDue91_Plus(Env.ZERO);
            setOpenAmt(Env.ZERO);
            setInvoicedAmt(Env.ZERO);
            setIsListInvoices(false);
            setIsSOTrx(false);
        }
    }

    /**
	 * 	Full Constructor
	 *	@param ctx context
	 *	@param AD_PInstance_ID instance
	 *	@param StatementDate statement date
	 *	@param C_BPartner_ID bpartner
	 *	@param C_Currency_ID currency
	 *	@param C_Invoice_ID invoice
	 *	@param C_InvoicePaySchedule_ID invoice schedule
	 *	@param C_BP_Group_ID group
	 *	@param DueDate due date
	 *	@param IsSOTrx SO Trx
	 *	@param trxName transaction
	 */
    public MAging(Properties ctx, int AD_PInstance_ID, Timestamp StatementDate, int C_BPartner_ID, int C_Currency_ID, int C_Invoice_ID, int C_InvoicePaySchedule_ID, int C_BP_Group_ID, Timestamp DueDate, boolean IsSOTrx, String trxName) {
        this(ctx, 0, trxName);
        setAD_PInstance_ID(AD_PInstance_ID);
        setStatementDate(StatementDate);
        setC_BPartner_ID(C_BPartner_ID);
        setC_Currency_ID(C_Currency_ID);
        setC_BP_Group_ID(C_BP_Group_ID);
        setIsSOTrx(IsSOTrx);
        set_ValueNoCheck("C_Invoice_ID", new Integer(C_Invoice_ID));
        set_Value("C_InvoicePaySchedule_ID", new Integer(C_InvoicePaySchedule_ID));
        setIsListInvoices(C_Invoice_ID != 0);
        setDueDate(DueDate);
    }

    /**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
    public MAging(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /** Number of items 		*/
    private int m_noItems = 0;

    /** Sum of Due Days			*/
    private int m_daysDueSum = 0;

    /**
	 * 	Add Amount to Buckets
	 *	@param DueDate due date 
	 *	@param daysDue positive due - negative not due
	 *	@param invoicedAmt invoiced amount
	 *	@param openAmt open amount
	 */
    public void add(Timestamp DueDate, int daysDue, BigDecimal invoicedAmt, BigDecimal openAmt) {
        if (invoicedAmt == null) invoicedAmt = Env.ZERO;
        setInvoicedAmt(getInvoicedAmt().add(invoicedAmt));
        if (openAmt == null) openAmt = Env.ZERO;
        setOpenAmt(getOpenAmt().add(openAmt));
        m_noItems++;
        m_daysDueSum += daysDue;
        setDaysDue(m_daysDueSum / m_noItems);
        if (getDueDate().after(DueDate)) setDueDate(DueDate);
        BigDecimal amt = openAmt;
        if (daysDue <= 0) {
            setDueAmt(getDueAmt().add(amt));
            if (daysDue == 0) setDue0(getDue0().add(amt));
            if (daysDue >= -7) setDue0_7(getDue0_7().add(amt));
            if (daysDue >= -30) setDue0_30(getDue0_30().add(amt));
            if (daysDue <= -1 && daysDue >= -7) setDue1_7(getDue1_7().add(amt));
            if (daysDue <= -8 && daysDue >= -30) setDue8_30(getDue8_30().add(amt));
            if (daysDue <= -31 && daysDue >= -60) setDue31_60(getDue31_60().add(amt));
            if (daysDue <= -31) setDue31_Plus(getDue31_Plus().add(amt));
            if (daysDue <= -61 && daysDue >= -90) setDue61_90(getDue61_90().add(amt));
            if (daysDue <= -61) setDue61_Plus(getDue61_Plus().add(amt));
            if (daysDue <= -91) setDue91_Plus(getDue91_Plus().add(amt));
        } else {
            setPastDueAmt(getPastDueAmt().add(amt));
            if (daysDue <= 7) setPastDue1_7(getPastDue1_7().add(amt));
            if (daysDue <= 30) setPastDue1_30(getPastDue1_30().add(amt));
            if (daysDue >= 8 && daysDue <= 30) setPastDue8_30(getPastDue8_30().add(amt));
            if (daysDue >= 31 && daysDue <= 60) setPastDue31_60(getPastDue31_60().add(amt));
            if (daysDue >= 31) setPastDue31_Plus(getPastDue31_Plus().add(amt));
            if (daysDue >= 61 && daysDue <= 90) setPastDue61_90(getPastDue61_90().add(amt));
            if (daysDue >= 61) setPastDue61_Plus(getPastDue61_Plus().add(amt));
            if (daysDue >= 91) setPastDue91_Plus(getPastDue91_Plus().add(amt));
        }
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer("MAging[");
        sb.append("AD_PInstance_ID=").append(getAD_PInstance_ID()).append(",C_BPartner_ID=").append(getC_BPartner_ID()).append(",C_Currency_ID=").append(getC_Currency_ID()).append(",C_Invoice_ID=").append(getC_Invoice_ID());
        sb.append("]");
        return sb.toString();
    }
}

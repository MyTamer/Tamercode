package org.compiere.process;

import java.math.*;
import java.sql.*;
import java.util.logging.*;
import org.compiere.model.*;

/**
 *	Copy Order and optionally close
 *	
 *  @author Jorg Janke
 *  @version $Id: CopyOrder.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class CopyOrder extends SvrProcess {

    /** Order to Copy				*/
    private int p_C_Order_ID = 0;

    /** Document Type of new Order	*/
    private int p_C_DocType_ID = 0;

    /** New Doc Date				*/
    private Timestamp p_DateDoc = null;

    /** Close/Process Old Order		*/
    private boolean p_IsCloseDocument = false;

    /**
	 *  Prepare - e.g., get Parameters.
	 */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) ; else if (name.equals("C_Order_ID")) p_C_Order_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("C_DocType_ID")) p_C_DocType_ID = ((BigDecimal) para[i].getParameter()).intValue(); else if (name.equals("DateDoc")) p_DateDoc = (Timestamp) para[i].getParameter(); else if (name.equals("IsCloseDocument")) p_IsCloseDocument = "Y".equals(para[i].getParameter()); else log.log(Level.SEVERE, "Unknown Parameter: " + name);
        }
    }

    /**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
    protected String doIt() throws Exception {
        log.info("C_Order_ID=" + p_C_Order_ID + ", C_DocType_ID=" + p_C_DocType_ID + ", CloseDocument=" + p_IsCloseDocument);
        if (p_C_Order_ID == 0) throw new IllegalArgumentException("No Order");
        MDocType dt = MDocType.get(getCtx(), p_C_DocType_ID);
        if (dt.get_ID() == 0) throw new IllegalArgumentException("No DocType");
        if (p_DateDoc == null) p_DateDoc = new Timestamp(System.currentTimeMillis());
        MOrder from = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
        MOrder newOrder = MOrder.copyFrom(from, p_DateDoc, dt.getC_DocType_ID(), dt.isSOTrx(), false, true, get_TrxName());
        newOrder.setC_DocTypeTarget_ID(p_C_DocType_ID);
        boolean OK = newOrder.save();
        if (!OK) throw new IllegalStateException("Could not create new Order");
        if (p_IsCloseDocument) {
            MOrder original = new MOrder(getCtx(), p_C_Order_ID, get_TrxName());
            original.setDocAction(MOrder.DOCACTION_Complete);
            original.processIt(MOrder.DOCACTION_Complete);
            original.save();
            original.setDocAction(MOrder.DOCACTION_Close);
            original.processIt(MOrder.DOCACTION_Close);
            original.save();
        }
        return dt.getName() + ": " + newOrder.getDocumentNo();
    }
}

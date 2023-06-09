package org.compiere.www;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.*;
import javax.servlet.http.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	HTML Zoom Window
 *	
 *  @author Rob Klein
 *  @version $Id: WZoom.java $
 */
public class WZoom extends HttpServlet {

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(WAttachment.class);

    /**
	 * Initialize global variables
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (!WebEnv.initWeb(config)) throw new ServletException("WZoom.init");
    }

    public static final String P_Record_ID = "AD_Record_ID";

    public static final String P_Table_ID = "AD_Table_ID";

    private static int s_WindowNo = 3;

    /**
	 * 	Process the HTTP Get request.
	 * 	Initial display and streaming 
	 *  @param request request
	 *  @param response response
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
        HttpSession session = request.getSession(false);
        WWindowStatus ws = WWindowStatus.get(request);
        WebDoc doc = null;
        if (session == null || ws == null) {
            doc = WebDoc.createPopup("No Context");
            doc.addPopupClose(ws.ctx);
        } else {
            String error = null;
            int AD_Record_ID = WebUtil.getParameterAsInt(request, P_Record_ID);
            int AD_Table_ID = WebUtil.getParameterAsInt(request, P_Table_ID);
            if (AD_Record_ID == 0 || AD_Table_ID == 0) {
                doc = WebDoc.createPopup("Invalid Record ID or Table ID");
                doc.addPopupClose(ws.ctx);
            } else {
                doc = createPage(ws.ctx, request, AD_Record_ID, AD_Table_ID);
            }
        }
        WebUtil.createResponse(request, response, this, null, doc, false);
    }

    /**
	 *  Process the HTTP Post request.
	 *  Update Attachment
	 *  @param request request
	 *  @parem response response
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession(false);
        WWindowStatus ws = WWindowStatus.get(request);
        WebDoc doc = null;
        doc = WebDoc.create("Help - Post Not Implemented");
        WebUtil.createResponse(request, response, this, null, doc, false);
    }

    /**
	 * 	Create Attachment Page
	 * 	@param ctx context
	 *	@param AD_Attachment_ID id for existing attachment
	 *	@param AD_Table_ID table for new attachment
	 *	@param Record_ID record for new attachment
	 *	@param error optional error message
	 *	@return WebDoc
	 */
    public static WebDoc createPage(Properties ctx, HttpServletRequest request, int AD_Record_ID, int AD_Table_ID) {
        WebDoc doc = null;
        String TableName = null;
        int AD_Window_ID = 0;
        int PO_Window_ID = 0;
        String sql = "SELECT TableName, AD_Window_ID, PO_Window_ID FROM AD_Table WHERE AD_Table_ID=?";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Table_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TableName = rs.getString(1);
                AD_Window_ID = rs.getInt(2);
                PO_Window_ID = rs.getInt(3);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
        }
        if (TableName == null || AD_Window_ID == 0) {
            doc = WebDoc.createPopup("No Context");
            doc.addPopupClose(ctx);
            return doc;
        }
        boolean isSOTrx = true;
        if (PO_Window_ID != 0) {
            String whereClause = TableName + "_ID=" + AD_Record_ID;
            isSOTrx = DB.isSOTrx(TableName, whereClause);
            if (!isSOTrx) AD_Window_ID = PO_Window_ID;
        }
        WWindowStatus ws = WWindowStatus.get(request);
        HttpSession sess = request.getSession();
        WebSessionCtx wsc = WebSessionCtx.get(request);
        if (ws != null) {
            int WindowNo = ws.mWindow.getWindowNo();
            log.fine("Disposing - WindowNo=" + WindowNo + ", ID=" + ws.mWindow.getAD_Window_ID());
            ws.mWindow.dispose();
            Env.clearWinContext(wsc.ctx, WindowNo);
        }
        GridWindowVO mWindowVO = GridWindowVO.create(ctx, s_WindowNo++, AD_Window_ID, 0);
        if (mWindowVO == null) {
            String msg = Msg.translate(ctx, "AD_Window_ID") + " " + Msg.getMsg(ctx, "NotFound") + ", ID=" + AD_Window_ID + "/" + 0;
            doc = WebDoc.createPopup(msg);
            doc.addPopupClose(ctx);
            return doc;
        }
        ws = new WWindowStatus(mWindowVO);
        sess.setAttribute(WWindowStatus.NAME, ws);
        ws.mWindow.initTab(ws.curTab.getTabNo());
        ws.curTab.setQuery(MQuery.getEqualQuery(TableName + "_ID", AD_Record_ID));
        ws.curTab.query(false);
        return doc;
    }
}

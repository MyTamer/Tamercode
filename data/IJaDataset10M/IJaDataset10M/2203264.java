package org.compiere.model;

import java.awt.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import org.compiere.util.*;

/**
 *  Window Workbench Model
 *
 *  @author Jorg Janke
 *  @version $Id: GridWorkbench.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 */
public class GridWorkbench implements Serializable {

    /**
	 *  Workbench Model Constructor
	 *  @param ctx context
	 */
    public GridWorkbench(Properties ctx) {
        m_ctx = ctx;
    }

    /**
	 *  No Workbench - Just Frame for Window
	 *  @param ctx context
	 *  @param AD_Window_ID window
	 */
    public GridWorkbench(Properties ctx, int AD_Window_ID) {
        m_ctx = ctx;
        m_windows.add(new WBWindow(TYPE_WINDOW, AD_Window_ID));
    }

    /** Properties      */
    private Properties m_ctx;

    /** List of windows */
    private ArrayList<WBWindow> m_windows = new ArrayList<WBWindow>();

    private int AD_Workbench_ID = 0;

    private String Name = "";

    private String Description = "";

    private String Help = "";

    private int AD_Column_ID = 0;

    private int AD_Image_ID = 0;

    private int AD_Color_ID = 0;

    private int PA_Goal_ID = 0;

    private String ColumnName = "";

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(GridWorkbench.class);

    /**
	 *  Init Workbench
	 *  @param ad_Workbench_ID workbench
	 *  @return true if initialized
	 */
    public boolean initWorkbench(int ad_Workbench_ID) {
        AD_Workbench_ID = ad_Workbench_ID;
        String sql = null;
        if (Env.isBaseLanguage(m_ctx, "AD_Workbench")) sql = "SELECT w.Name,w.Description,w.Help," + " w.AD_Column_ID,w.AD_Image_ID,w.AD_Color_ID,w.PA_Goal_ID," + " c.ColumnName " + "FROM AD_Workbench w, AD_Column c " + "WHERE w.AD_Workbench_ID=?" + " AND w.IsActive='Y'" + " AND w.AD_Column_ID=c.AD_Column_ID"; else sql = "SELECT t.Name,t.Description,t.Help," + " w.AD_Column_ID,w.AD_Image_ID,w.AD_Color_ID,w.PA_Goal_ID," + " c.ColumnName " + "FROM AD_Workbench w, AD_Workbench_Trl t, AD_Column c " + "WHERE w.AD_Workbench_ID=?" + " AND w.IsActive='Y'" + " AND w.AD_Workbench_ID=t.AD_Workbench_ID" + " AND t.AD_Language='" + Env.getAD_Language(m_ctx) + "'" + " AND w.AD_Column_ID=c.AD_Column_ID";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Workbench_ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Name = rs.getString(1);
                Description = rs.getString(2);
                if (Description == null) Description = "";
                Help = rs.getString(3);
                if (Help == null) Help = "";
                AD_Column_ID = rs.getInt(4);
                AD_Image_ID = rs.getInt(5);
                AD_Color_ID = rs.getInt(6);
                PA_Goal_ID = rs.getInt(7);
                ColumnName = rs.getString(8);
            } else AD_Workbench_ID = 0;
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
        }
        if (AD_Workbench_ID == 0) return false;
        return initWorkbenchWindows();
    }

    /**
	 *  String Representation
	 *  @return info
	 */
    public String toString() {
        return "MWorkbench ID=" + AD_Workbench_ID + " " + Name + ", windows=" + m_windows.size() + ", LinkColumn=" + ColumnName;
    }

    /**
	 *  Dispose
	 */
    public void dispose() {
        for (int i = 0; i < m_windows.size(); i++) {
            dispose(i);
        }
        m_windows.clear();
        m_windows = null;
    }

    /**
	 *  Get Workbench Query.
	 *  @return ColumnName=@#ColumnName@
	 */
    public MQuery getQuery() {
        return MQuery.getEqualQuery(ColumnName, "@#" + ColumnName + "@");
    }

    /**
	 * 	Get Workbench
	 * 	@return workbensch id
	 */
    public int getAD_Workbench_ID() {
        return AD_Workbench_ID;
    }

    /**
	 * 	Get Name
	 *	@return name
	 */
    public String getName() {
        return Name;
    }

    /**
	 * 	Get Description
	 *	@return description
	 */
    public String getDescription() {
        return Description;
    }

    /**
	 * 	Get Help
	 *	@return help
	 */
    public String getHelp() {
        return Help;
    }

    /**
	 * 	Get Link AD_Column_ID
	 *	@return column
	 */
    public int getAD_Column_ID() {
        return AD_Column_ID;
    }

    /**
	 * 	Get AD_Image_ID
	 *	@return image
	 */
    public int getAD_Image_ID() {
        return AD_Image_ID;
    }

    /**
	 * 	Get AD_Color_ID
	 *	@return color
	 */
    public int getAD_Color_ID() {
        return AD_Color_ID;
    }

    /**
	 * 	Get PA_Goal_ID
	 *	@return goal
	 */
    public int getPA_Goal_ID() {
        return PA_Goal_ID;
    }

    /** Window          */
    public static final int TYPE_WINDOW = 1;

    /** Form            */
    public static final int TYPE_FORM = 2;

    /** Process         */
    public static final int TYPE_PROCESS = 3;

    /** Task            */
    public static final int TYPE_TASK = 4;

    /**
	 *  Init Workbench Windows
	 *  @return true if init ok
	 */
    private boolean initWorkbenchWindows() {
        String sql = "SELECT AD_Window_ID, AD_Form_ID, AD_Process_ID, AD_Task_ID " + "FROM AD_WorkbenchWindow " + "WHERE AD_Workbench_ID=? AND IsActive='Y'" + "ORDER BY SeqNo";
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql, null);
            pstmt.setInt(1, AD_Workbench_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int AD_Window_ID = rs.getInt(1);
                int AD_Form_ID = rs.getInt(2);
                int AD_Process_ID = rs.getInt(3);
                int AD_Task_ID = rs.getInt(4);
                if (AD_Window_ID > 0) m_windows.add(new WBWindow(TYPE_WINDOW, AD_Window_ID)); else if (AD_Form_ID > 0) m_windows.add(new WBWindow(TYPE_FORM, AD_Form_ID)); else if (AD_Process_ID > 0) m_windows.add(new WBWindow(TYPE_PROCESS, AD_Process_ID)); else if (AD_Task_ID > 0) m_windows.add(new WBWindow(TYPE_TASK, AD_Task_ID));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            log.log(Level.SEVERE, sql, e);
            return false;
        }
        return true;
    }

    /**
	 *  Get Window Count
	 *  @return window count
	 */
    public int getWindowCount() {
        return m_windows.size();
    }

    /**
	 *  Get Window Type of Window
	 *  @param index index in workbench
	 *  @return -1 if not valid
	 */
    public int getWindowType(int index) {
        if (index < 0 || index > m_windows.size()) return -1;
        WBWindow win = (WBWindow) m_windows.get(index);
        return win.Type;
    }

    /**
	 *  Get ID for Window
	 *  @param index index in workbench
	 *  @return -1 if not valid
	 */
    public int getWindowID(int index) {
        if (index < 0 || index > m_windows.size()) return -1;
        WBWindow win = (WBWindow) m_windows.get(index);
        return win.ID;
    }

    /**************************************************************************
	 *  Set Window Model of Window
	 *  @param index index in workbench
	 *  @param mw model window
	 */
    public void setMWindow(int index, GridWindow mw) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.Type != TYPE_WINDOW) throw new IllegalArgumentException("Not a MWindow: " + index);
        win.mWindow = mw;
    }

    /**
	 *  Get Window Model of Window
	 *  @param index index in workbench
	 *  @return model window
	 */
    public GridWindow getMWindow(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.Type != TYPE_WINDOW) throw new IllegalArgumentException("Not a MWindow: " + index);
        return win.mWindow;
    }

    /**
	 *  Get Name of Window
	 *  @param index index in workbench
	 *  @return Window Name or null if not set
	 */
    public String getName(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null && win.Type == TYPE_WINDOW) return win.mWindow.getName();
        return null;
    }

    /**
	 *  Get Description of Window
	 *  @param index index in workbench
	 *  @return Window Description or null if not set
	 */
    public String getDescription(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null && win.Type == TYPE_WINDOW) return win.mWindow.getDescription();
        return null;
    }

    /**
	 *  Get Help of Window
	 *  @param index index in workbench
	 *  @return Window Help or null if not set
	 */
    public String getHelp(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null && win.Type == TYPE_WINDOW) return win.mWindow.getHelp();
        return null;
    }

    /**
	 *  Get Icon of Window
	 *  @param index index in workbench
	 *  @return Window Icon or null if not set
	 */
    public Icon getIcon(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null && win.Type == TYPE_WINDOW) return win.mWindow.getIcon();
        return null;
    }

    /**
	 *  Get Image Icon of Window
	 *  @param index index in workbench
	 *  @return Window Icon or null if not set
	 */
    public Image getImage(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null && win.Type == TYPE_WINDOW) return win.mWindow.getImage();
        return null;
    }

    /**
	 *  Get AD_Color_ID of Window
	 *  @param index index in workbench
	 *  @return Window Color or Workbench color if not set
	 */
    public int getAD_Color_ID(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        int retValue = -1;
        if (retValue == -1) return getAD_Color_ID();
        return retValue;
    }

    /**
	 *  Set WindowNo of Window
	 *  @param index index in workbench
	 *  @param windowNo window no
	 */
    public void setWindowNo(int index, int windowNo) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        win.WindowNo = windowNo;
    }

    /**
	 *  Get WindowNo of Window
	 *  @param index index in workbench
	 *  @return WindowNo of Window if previously set, otherwise -1;
	 */
    public int getWindowNo(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        return win.WindowNo;
    }

    /**
	 *  Dispose of Window
	 *  @param index index in workbench
	 */
    public void dispose(int index) {
        if (index < 0 || index > m_windows.size()) throw new IllegalArgumentException("Index invalid: " + index);
        WBWindow win = (WBWindow) m_windows.get(index);
        if (win.mWindow != null) win.mWindow.dispose();
        win.mWindow = null;
    }

    /**
	 * 	Get Window Size
	 *	@return window size or null if not set
	 */
    public Dimension getWindowSize() {
        return null;
    }

    /**************************************************************************
	 *  Window Type
	 */
    class WBWindow {

        /**
		 * 	WBWindow
		 *	@param type
		 *	@param id
		 */
        public WBWindow(int type, int id) {
            Type = type;
            ID = id;
        }

        /** Type			*/
        public int Type = 0;

        /** ID				*/
        public int ID = 0;

        /** Window No		*/
        public int WindowNo = -1;

        /** Window Midel	*/
        public GridWindow mWindow = null;
    }
}

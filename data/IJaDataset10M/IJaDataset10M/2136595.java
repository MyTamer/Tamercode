package org.openXpertya.model;

import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Evaluatee;
import org.openXpertya.util.Evaluator;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

/**
 *  Model Tab Value Object
 *
 *  @author Comunidad de Desarrollo openXpertya
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         * Jorg Janke
 *  @version  $Id: MTabVO.java,v 1.25 2005/05/21 04:47:54 jjanke Exp $
 */
public class MTabVO implements Evaluatee, Serializable {

    /** Descripción de Campo */
    static final long serialVersionUID = 9160212869277319305L;

    /** Descripción de Campo */
    public String Name = "";

    /** Descripción de Campo */
    public boolean IsView = false;

    /** Descripción de Campo */
    public boolean IsSingleRow = false;

    /** Descripción de Campo */
    public boolean IsSecurityEnabled = false;

    /** Descripción de Campo */
    public boolean IsReadOnly = false;

    /** Descripción de Campo */
    public boolean IsInsertRecord = true;

    /** Descripción de Campo */
    public boolean IsHighVolume = false;

    /** Descripción de Campo */
    public boolean IsDeleteable = false;

    /** Descripción de Campo */
    public String Help = "";

    /** Descripción de Campo */
    public boolean HasTree = false;

    /** Descripción de Campo */
    public String Description = "";

    /** Descripción de Campo */
    public int AD_Process_ID = 0;

    /** Primary Parent Column */
    public int AD_Column_ID = 0;

    /** Descripción de Campo */
    public int TabLevel = 0;

    /** Descripción de Campo */
    public String ReplicationType = "L";

    /** Descripción de Campo */
    public boolean IsSortTab = false;

    /** Descripción de Campo */
    public int Included_Tab_ID = 0;

    /** Descripción de Campo */
    public int AD_Image_ID = 0;

    /** Descripción de Campo */
    public int AD_ColumnSortYesNo_ID = 0;

    /** Descripción de Campo */
    public int AD_ColumnSortOrder_ID = 0;

    /** Descripción de Campo */
    public boolean onlyCurrentRows = true;

    /** Descripción de Campo */
    public int onlyCurrentDays = 0;

    /** Fields contain MFieldVO entities */
    public ArrayList Fields = null;

    /** Descripción de Campo */
    public int AD_Tab_ID;

    /** Descripción de Campo */
    public int AD_Table_ID;

    /** AD Window - replicated */
    public int AD_Window_ID;

    /** Descripción de Campo */
    public String AccessLevel;

    /** Descripción de Campo */
    public String CommitWarning;

    /** Descripción de Campo */
    public String DisplayLogic;

    /** Descripción de Campo */
    public String OrderByClause;

    /** Descripción de Campo */
    public String ReadOnlyLogic;

    /** Tab No (not AD_Tab_ID) 0.. */
    public int TabNo;

    /** Descripción de Campo */
    public String TableName;

    /** Descripción de Campo */
    public String WhereClause;

    /** Window No - replicated */
    public int WindowNo;

    /** Context - replicated */
    public Properties ctx;

    /** Mostrar mensajes processmsg en dialog */
    public boolean showDialogProcessMsg;

    /**
     *  Private constructor - must use Factory
     */
    private MTabVO() {
    }

    /**
     *      Create MTab VO
     *
     *  @param wVO value object
     *  @param TabNo tab no
     *      @param rs ResultSet from AD_Tab_v
     *      @param isRO true if window is r/o
     *  @param onlyCurrentRows if true query is limited to not processed records
     *  @return TabVO
     */
    public static MTabVO create(MWindowVO wVO, int TabNo, ResultSet rs, boolean isRO, boolean onlyCurrentRows) {
        CLogger.get().config("#" + TabNo);
        MTabVO vo = new MTabVO();
        vo.ctx = wVO.ctx;
        vo.WindowNo = wVO.WindowNo;
        vo.AD_Window_ID = wVO.AD_Window_ID;
        vo.TabNo = TabNo;
        if (!loadTabDetails(vo, rs)) {
            return null;
        }
        if (isRO) {
            CLogger.get().fine("Tab is ReadOnly");
            vo.IsReadOnly = true;
        }
        vo.onlyCurrentRows = onlyCurrentRows;
        if (vo.IsSortTab) {
            vo.Fields = new ArrayList();
        } else {
            createFields(vo);
            if ((vo.Fields == null) || (vo.Fields.size() == 0)) {
                CLogger.get().log(Level.SEVERE, "No Fields");
                return null;
            }
        }
        return vo;
    }

    /**
     *  Create Tab Fields
     *  @param mTabVO tab value object
     *  @return true if fields were created
     */
    private static boolean createFields(MTabVO mTabVO) {
        mTabVO.Fields = new ArrayList();
        String sql = MFieldVO.getSQL(mTabVO.ctx);
        try {
            PreparedStatement pstmt = DB.prepareStatement(sql);
            pstmt.setInt(1, mTabVO.AD_Tab_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                MFieldVO voF = MFieldVO.create(mTabVO.ctx, mTabVO.WindowNo, mTabVO.TabNo, mTabVO.AD_Window_ID, mTabVO.IsReadOnly, rs);
                if (voF != null) {
                    mTabVO.Fields.add(voF);
                }
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            CLogger.get().log(Level.SEVERE, "createFields", e);
            return false;
        }
        return mTabVO.Fields.size() != 0;
    }

    /**
     *      Get Variable Value (Evaluatee)
     *      @param variableName name
     *      @return value
     */
    public String get_ValueAsString(String variableName) {
        return Env.getContext(ctx, WindowNo, variableName, false);
    }

    /**
     *      Load Tab Details from rs into vo
     *      @param vo Tab value object
     *      @param rs ResultSet from AD_Tab_v/t
     *      @return true if read ok
     */
    private static boolean loadTabDetails(MTabVO vo, ResultSet rs) {
        MRole role = MRole.getDefault(vo.ctx, false);
        boolean showTrl = "Y".equals(Env.getContext(vo.ctx, "#ShowTrl"));
        boolean showAcct = "Y".equals(Env.getContext(vo.ctx, "#ShowAcct"));
        boolean showAdvanced = "Y".equals(Env.getContext(vo.ctx, "#ShowAdvanced"));
        try {
            vo.AD_Tab_ID = rs.getInt("AD_Tab_ID");
            Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AD_Tab_ID", String.valueOf(vo.AD_Tab_ID));
            vo.Name = rs.getString("Name");
            Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "Name", vo.Name);
            Boolean roleTabAccess = role.getTabAccess(vo.AD_Window_ID, vo.AD_Tab_ID);
            String roleTabWhere = role.getTabWhere(vo.AD_Window_ID, vo.AD_Tab_ID);
            if (rs.getString("IsTranslationTab").equals("Y")) {
                vo.TableName = rs.getString("TableName");
                if (!Env.isBaseTranslation(vo.TableName) && !Env.isMultiLingualDocument(vo.ctx)) {
                    showTrl = false;
                }
                if (!showTrl) {
                    CLogger.get().fine("TrlTab Not displayed - AD_Tab_ID=" + vo.AD_Tab_ID + "=" + vo.Name + ", Table=" + vo.TableName + ", BaseTrl=" + Env.isBaseTranslation(vo.TableName) + ", MultiLingual=" + Env.isMultiLingualDocument(vo.ctx));
                    return false;
                }
            }
            if (!showAdvanced && rs.getString("IsAdvancedTab").equals("Y")) {
                CLogger.get().fine("AdvancedTab Not displayed - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                return false;
            }
            if (!showAcct && rs.getString("IsInfoTab").equals("Y")) {
                CLogger.get().fine("AcctTab Not displayed - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                return false;
            }
            if (roleTabAccess == null) {
                CLogger.get().fine("No Role Access - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                return false;
            }
            vo.DisplayLogic = rs.getString("DisplayLogic");
            if ((vo.DisplayLogic != null) && (vo.DisplayLogic.length() > 0)) {
                if ((Env.parseContext(vo.ctx, 0, vo.DisplayLogic, false, false).length() > 0) && Evaluator.evaluateLogic(vo, vo.DisplayLogic)) {
                    CLogger.get().fine("Tab Not displayed (" + vo.DisplayLogic + ") AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                    return false;
                }
            }
            vo.AccessLevel = rs.getString("AccessLevel");
            if (!role.canView(vo.ctx, vo.AccessLevel)) {
                CLogger.get().fine("No Role Access - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                return false;
            }
            Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AccessLevel", vo.AccessLevel);
            vo.AD_Table_ID = rs.getInt("AD_Table_ID");
            Env.setContext(vo.ctx, vo.WindowNo, vo.TabNo, "AD_Table_ID", String.valueOf(vo.AD_Table_ID));
            if (!role.isTableAccess(vo.AD_Table_ID, true)) {
                CLogger.get().fine("No Table Access - AD_Tab_ID=" + vo.AD_Tab_ID + " " + vo.Name);
                return false;
            }
            vo.IsReadOnly = rs.getString("IsReadOnly").equals("Y") || !roleTabAccess;
            vo.ReadOnlyLogic = rs.getString("ReadOnlyLogic");
            if (rs.getString("IsInsertRecord").equals("N")) {
                vo.IsInsertRecord = false;
            }
            vo.Description = rs.getString("Description");
            if (vo.Description == null) {
                vo.Description = "";
            }
            vo.Help = rs.getString("Help");
            if (vo.Help == null) {
                vo.Help = "";
            }
            if (rs.getString("IsSingleRow").equals("Y")) {
                vo.IsSingleRow = true;
            }
            if (rs.getString("HasTree").equals("Y")) {
                vo.HasTree = true;
            }
            vo.AD_Table_ID = rs.getInt("AD_Table_ID");
            vo.TableName = rs.getString("TableName");
            if (rs.getString("IsView").equals("Y")) {
                vo.IsView = true;
            }
            vo.AD_Column_ID = rs.getInt("AD_Column_ID");
            if (rs.getString("IsSecurityEnabled").equals("Y")) {
                vo.IsSecurityEnabled = true;
            }
            if (rs.getString("IsDeleteable").equals("Y")) {
                vo.IsDeleteable = true;
            }
            if (rs.getString("IsHighVolume").equals("Y")) {
                vo.IsHighVolume = true;
            }
            vo.CommitWarning = rs.getString("CommitWarning");
            if (vo.CommitWarning == null) {
                vo.CommitWarning = "";
            }
            vo.WhereClause = rs.getString("WhereClause");
            if (vo.WhereClause == null) {
                vo.WhereClause = "";
            }
            if (roleTabWhere != null && roleTabWhere.length() > 0) {
                vo.WhereClause += " AND " + roleTabWhere;
            }
            vo.OrderByClause = rs.getString("OrderByClause");
            if (vo.OrderByClause == null) {
                vo.OrderByClause = "";
            }
            vo.AD_Process_ID = rs.getInt("AD_Process_ID");
            if (rs.wasNull()) {
                vo.AD_Process_ID = 0;
            }
            vo.AD_Image_ID = rs.getInt("AD_Image_ID");
            if (rs.wasNull()) {
                vo.AD_Image_ID = 0;
            }
            vo.Included_Tab_ID = rs.getInt("Included_Tab_ID");
            if (rs.wasNull()) {
                vo.Included_Tab_ID = 0;
            }
            vo.TabLevel = rs.getInt("TabLevel");
            if (rs.wasNull()) {
                vo.TabLevel = 0;
            }
            vo.IsSortTab = rs.getString("IsSortTab").equals("Y");
            if (vo.IsSortTab) {
                vo.AD_ColumnSortOrder_ID = rs.getInt("AD_ColumnSortOrder_ID");
                vo.AD_ColumnSortYesNo_ID = rs.getInt("AD_ColumnSortYesNo_ID");
            }
            vo.showDialogProcessMsg = rs.getString("isprocessmsgshowdialog") != null ? rs.getString("isprocessmsgshowdialog").equals("Y") : false;
            try {
                int index = rs.findColumn("ReplicationType");
                vo.ReplicationType = rs.getString(index);
                if ("R".equals(vo.ReplicationType)) {
                    vo.IsReadOnly = true;
                }
            } catch (Exception e) {
            }
        } catch (SQLException ex) {
            CLogger.get().log(Level.SEVERE, "loadTabDetails", ex);
            return false;
        }
        return true;
    }

    /**
     *  Return the SQL statement used for the MTabVO.create
     *  @param ctx context
     *  @return SQL SELECT String
     */
    protected static String getSQL(Properties ctx) {
        String sql = "SELECT * FROM AD_Tab_v WHERE AD_Window_ID=?" + " ORDER BY SeqNo";
        if (!Env.isBaseLanguage(ctx, "AD_Window")) {
            sql = "SELECT * FROM AD_Tab_vt WHERE AD_Window_ID=?" + " AND AD_Language='" + Env.getAD_Language(ctx) + "'" + " ORDER BY SeqNo";
        }
        return sql;
    }

    /**
     *  Set Context including contained elements
     *  @param newCtx new context
     */
    public void setCtx(Properties newCtx) {
        ctx = newCtx;
        for (int i = 0; i < Fields.size(); i++) {
            MFieldVO field = (MFieldVO) Fields.get(i);
            field.setCtx(newCtx);
        }
    }
}

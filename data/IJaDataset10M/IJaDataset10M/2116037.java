package org.openXpertya.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import org.openXpertya.util.CCache;
import org.openXpertya.util.CLogger;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Util;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class MPreference extends X_AD_Preference {

    private static CCache s_CustomPreferences = new CCache("CustomAdPreference", 10);

    public static String GetCustomPreferenceValue(String Key) {
        String Value = null;
        synchronized (s_CustomPreferences) {
            if (s_CustomPreferences.containsKey(Key)) {
                Value = (String) s_CustomPreferences.get(Key);
            } else {
                Value = (String) DB.getSQLObject(null, "SELECT Value FROM " + Table_Name + " WHERE attribute = ? ", new Object[] { Key });
                if (Value == null) Value = "";
                s_CustomPreferences.put(Key, Value);
            }
        }
        if (Value == null) Value = "";
        return Value;
    }

    public static String GetCustomPreferenceValue(String Key, Integer clientID) {
        return GetCustomPreferenceValue(Key, clientID, null, null, false);
    }

    public static String GetCustomPreferenceValue(String Key, Integer clientID, Integer orgID, Integer userID, boolean ignoreCache) {
        String Value = null;
        synchronized (s_CustomPreferences) {
            if (!ignoreCache && s_CustomPreferences.containsKey(Key)) {
                Value = (String) s_CustomPreferences.get(Key);
            } else {
                StringBuffer sql = new StringBuffer("SELECT Value " + "FROM " + Table_Name + " " + "WHERE attribute = ? AND IsActive = 'Y' ");
                if (clientID != null) {
                    sql.append(" AND AD_Client_ID = ").append(clientID);
                }
                if (!Util.isEmpty(orgID, false)) {
                    sql.append(" AND AD_Org_ID = ").append(orgID);
                }
                if (!Util.isEmpty(userID, true)) {
                    sql.append(" AND AD_User_ID = ").append(userID);
                }
                Value = (String) DB.getSQLObject(null, sql.toString(), new Object[] { Key });
                if (Value == null) Value = "";
                s_CustomPreferences.put(Key, Value);
            }
        }
        return Value;
    }

    public static void SetCustomPreferenceValue(String Key, String Value) {
        s_CustomPreferences.clear();
        PreparedStatement ps = null;
        int n = 0;
        try {
            ps = DB.prepareStatement("UPDATE " + Table_Name + " SET Value = ?, Updated = NOW() WHERE attribute = ? ", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
            ps.setString(1, Value);
            ps.setString(2, Key);
            n = ps.executeUpdate();
            if (n < 1) {
                ps.close();
                ps = DB.prepareStatement("INSERT INTO " + Table_Name + " (ad_preference_id,isactive,ad_client_id,ad_org_id,createdby,updatedby,value,attribute,updated) VALUES ((select coalesce(max(ad_preference_id)+1,1) from " + Table_Name + " where ad_preference_id < 100000),'Y',0,0,0,0,?,?,NOW()) ", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE, null);
                ps.setString(1, Value);
                ps.setString(2, Key);
                n = ps.executeUpdate();
            }
        } catch (SQLException e) {
            CLogger.get().log(Level.SEVERE, "SetCustomPreferenceValue", e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
            }
        }
    }

    public static boolean GetCustomPreferenceValueBool(String Key) {
        return "Y".equals(GetCustomPreferenceValue(Key, null));
    }

    /** Descripción de Campos */
    public static String NULL = "null";

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param AD_Preference_ID
     * @param trxName
     */
    public MPreference(Properties ctx, int AD_Preference_ID, String trxName) {
        super(ctx, AD_Preference_ID, trxName);
        if (AD_Preference_ID == 0) {
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param rs
     * @param trxName
     */
    public MPreference(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param ctx
     * @param Attribute
     * @param Value
     * @param trxName
     */
    public MPreference(Properties ctx, String Attribute, String Value, String trxName) {
        this(ctx, 0, trxName);
        setAttribute(Attribute);
        setValue(Value);
    }

    public static MPreference getUserPreference(Properties ctx, String attribute, String trxName) {
        RecordFinder finder = new RecordFinder();
        Map filter = new TreeMap();
        filter.put("AD_User_ID", Env.getContextAsInt(ctx, "#AD_User_ID"));
        filter.put("Attribute", attribute);
        ResultSet rs = finder.find(ctx, filter, Table_Name);
        if (rs != null) {
            return new MPreference(ctx, rs, trxName);
        }
        return null;
    }

    public static String GetCustomPreferenceValue(String Key, Integer clientID, boolean ignoreChache) {
        s_CustomPreferences.remove(Key);
        return GetCustomPreferenceValue(Key, clientID);
    }
}

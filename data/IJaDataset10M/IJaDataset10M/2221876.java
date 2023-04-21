package spidr.dbload;

import java.io.*;
import java.util.*;
import java.sql.*;
import spidr.dbaccess.*;
import wdc.dbaccess.*;
import wdc.settings.*;
import wdc.utils.*;
import spidr.datamodel.*;

/**
public class SWRMetadata {

    public static final String DATA_TABLE = "SWR";

    public static final String INVENTORY_TABLE = "swr_inventory";

    /** Creates a vector to be inserted in metadata.
    public static Vector obtainInventory(Connection con) throws Exception {
        Vector iuList = new Vector();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            Vector tables = DBAccess.getAllTables(stmt);
            if (tables == null) return iuList;
            Iterator iter = tables.iterator();
            while (iter.hasNext()) {
                String table = (String) iter.next();
                if (table.length() != ("minXXXX").length() || !table.startsWith("min")) iter.remove();
            }
            for (int num = 0; num < tables.size(); num++) {
                String tableName = (String) tables.get(num);
                String sqlStr = "SELECT \"SWR\", stn, element, YEAR(obsdate)*100+MONTH(obsdate) AS yrMon, count(*)" + " FROM " + tableName + " GROUP BY stn, element, yrMon";
                ResultSet rs = stmt.executeQuery(sqlStr);
                while (rs.next()) {
                    iuList.add(new InventoryUnit(rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5)));
                }
                rs.close();
            }
        } catch (Exception e) {
            throw new Exception("obtainInventory@SWRMetadata: Problem to obtain metadata: " + e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
            ;
        }
        return iuList;
    }

    /**
    public static void updateInventory(Connection con, Vector iuList) throws Exception {
        updateInventory(con, iuList, true);
    }

    /**
    public static void updateInventory(Connection con, Vector iuList, boolean isCompleteUpdate) throws Exception {
        UpdateMetadata.updateLocalInventory(con, DATA_TABLE, INVENTORY_TABLE, iuList, isCompleteUpdate);
    }

    /** Creates a vector to be inserted in metadata.
    public static Vector obtainStationDescription(Connection con) throws Exception {
        Vector list = new Vector();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            final String[] QUERY_LIST = { "SELECT code, \"SWR\", name, lat, IF(lon>180,lon-360,lon) FROM stations" };
            for (int nQuery = 0; nQuery < QUERY_LIST.length; nQuery++) {
                ResultSet rs = stmt.executeQuery(QUERY_LIST[nQuery]);
                while (rs.next()) {
                    list.add(new Station(rs.getString(1), rs.getString(2), rs.getString(3), rs.getFloat(4), rs.getFloat(5)));
                }
                rs.close();
            }
            return list;
        } catch (Exception e) {
            throw new Exception("obtainStationDescription@SWRMetadata: Problem to obtain metadata: " + e);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception e) {
            }
            ;
        }
    }

    /** Selects period of elements. Only inventory is used.
    public static Vector selectElemPeriods(Connection con) throws Exception {
        return UpdateMetadata.selectStnElemPeriods(con, DATA_TABLE, INVENTORY_TABLE, "elem");
    }

    /** Selects period of stations. Only inventory is used.
    public static Vector selectStnPeriods(Connection con) throws Exception {
        return UpdateMetadata.selectStnElemPeriods(con, DATA_TABLE, INVENTORY_TABLE, "stn");
    }

    /**
    public static Vector getInventory(Connection con) throws Exception {
        return getInventory(con, null, null, null);
    }

    /**
    public static Vector getInventory(Connection con, DateInterval dateInterval) throws Exception {
        return getInventory(con, null, null, dateInterval);
    }

    /**
    public static Vector getInventory(Connection con, String stn, String elem, DateInterval dateInterval) throws Exception {
        return UpdateMetadata.getLocalInventory(con, DATA_TABLE, stn, elem, dateInterval, INVENTORY_TABLE);
    }

    /**
    public static void main(String args[]) {
        System.out.println("Activate settings");
        try {
            if (args.length == 0) {
                System.out.println("No parameters: base conf-file required.");
                return;
            }
            Settings.getInstance().load(args[0]);
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
        Connection con = null;
        try {
            con = ConnectionPool.getConnection(DATA_TABLE);
            System.out.println("Start");
            long startTime = (new java.util.Date()).getTime();
            System.out.println("Obtain inventory ...");
            Vector list = obtainInventory(con);
            System.out.println("Total: " + list.size());
            System.out.println("Time: " + ((new java.util.Date()).getTime() - startTime) / 1000f + " sec");
            ConnectionPool.releaseConnection(con);
            con = ConnectionPool.getConnection(DATA_TABLE, "Inventory");
            System.out.println("Update inventory ...");
            updateInventory(con, list, true);
            System.out.println("Time: " + ((new java.util.Date()).getTime() - startTime) / 1000f + " sec");
            System.out.println("Select station periods ...");
            list = selectStnPeriods(con);
            System.out.println("Total: " + list.size());
            System.out.println("Time: " + ((new java.util.Date()).getTime() - startTime) / 1000f + " sec");
            System.out.println("Select element periods ...");
            list = selectElemPeriods(con);
            System.out.println("Total: " + list.size());
            System.out.println("Time: " + ((new java.util.Date()).getTime() - startTime) / 1000f + " sec");
            System.out.println("Stop");
        } catch (Exception e) {
            System.out.println("Error:" + e);
        } finally {
            ConnectionPool.releaseConnection(con);
        }
    }
}
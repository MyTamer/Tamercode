package dbaccess.cosm;

import java.sql.*;
import java.util.*;
import java.lang.*;
import dbaccess.util.*;

/**
public class Inventory {

    Statement stmt;

    DBConnect connection;

    boolean found;

    boolean Debug;

    int Verbose;

    boolean ErrorCheckOnly;

    int Ndeleted;

    int Ninserted;

    private static String TABLE = "inventory";

    /** Station ID. */
    String stn;

    /** Year of the observation. */
    int year;

    /** Month of the observation. */
    int month;

    /** Monitor. */
    String monitor;

    /** Source (G=General format; 4=4096 format). */
    String source;

    /** Number Corrected for the day. */
    int numCorrected;

    /** Number UnCorrected for the day. */
    int numUncorrected;

    /** Number Pressure for the day. */
    int numPressure;

    /** Average Corrected for the day. */
    int avgCorrected;

    /** Average UnCorrected for the day. */
    int avgUncorrected;

    /** Average Pressure for the day. */
    int avgPressure;

    /** Minimum Corrected for the day. */
    int minCorrected;

    /** Minimum UnCorrected for the day. */
    int minUncorrected;

    /** Minimum Pressure for the day. */
    int minPressure;

    /** Maximum Corrected for the day. */
    int maxCorrected;

    /** Maximum UnCorrected for the day. */
    int maxUncorrected;

    /** Maximum Pressure for the day. */
    int maxPressure;

    /**
    public Inventory() {
        found = false;
        Verbose = 0;
        Debug = false;
        ErrorCheckOnly = false;
        Ndeleted = 0;
        Ninserted = 0;
        year = 0;
        month = 0;
        stn = null;
        source = null;
    }

    /**
    public Inventory(DBConnect c) {
        connection = c;
        stmt = c.getStatement();
        found = false;
        Verbose = 0;
        Debug = false;
        ErrorCheckOnly = false;
        Ndeleted = 0;
        Ninserted = 0;
        year = 0;
        month = 0;
        stn = null;
        source = null;
    }

    /**
    public String getStn() {
        return stn;
    }

    /**
    public int getYear() {
        return year;
    }

    /**
    public int getMonth() {
        return month;
    }

    /**
    public String getMonitor() {
        return monitor;
    }

    /**
    public String getSource() {
        return source;
    }

    /**
    public int getNumCorrected() {
        return numCorrected;
    }

    /**
    public int getNumUncorrected() {
        return numUncorrected;
    }

    /**
    public int getNumPressure() {
        return numPressure;
    }

    /**
    public int getAvgCorrected() {
        return avgCorrected;
    }

    /**
    public int getAvgUncorrected() {
        return avgUncorrected;
    }

    /**
    public int getAvgPressure() {
        return avgPressure;
    }

    /**
    public int getMinCorrected() {
        return minCorrected;
    }

    /**
    public int getMinUncorrected() {
        return minUncorrected;
    }

    /**
    public int getMinPressure() {
        return minPressure;
    }

    /**
    public int getMaxCorrected() {
        return maxCorrected;
    }

    /**
    public int getMaxUncorrected() {
        return maxUncorrected;
    }

    /**
    public int getMaxPressure() {
        return maxPressure;
    }

    /**
    public int getNdeleted() {
        return Ndeleted;
    }

    /**
    public int getNinserted() {
        return Ninserted;
    }

    /**
    public void setVerbose(int level) {
        Verbose = level;
        if (level > 2) Debug = true;
    }

    /**
    public void setErrorCheckOnly(boolean check) {
        ErrorCheckOnly = check;
    }

    /**
    public void setConnection(DBConnect c) {
        connection = c;
        stmt = c.getStatement();
    }

    /**
    public void setStn(String station) {
        stn = station;
    }

    /**
    public void setYear(int yr) {
        year = yr;
    }

    /**
    public void setMonth(int mo) {
        month = mo;
    }

    /**
    public void setMonitor(String mon) {
        monitor = mon;
    }

    /**
    public void setSource(String src) {
        source = src;
    }

    /**
    public void reset() {
        found = false;
        Ndeleted = 0;
        Ninserted = 0;
        year = 0;
        month = 0;
        stn = null;
        source = null;
    }

    /**
    public boolean get(String station, int yr, int mo) {
        setStn(station);
        setYear(yr);
        setMonth(mo);
        return get();
    }

    /**
    public boolean get() {
        found = false;
        String query = "SELECT * FROM " + TABLE + " WHERE ";
        query += "stn='" + stn + "'";
        query += " and yr=" + year;
        query += " and mo=" + month;
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                found = true;
                extractData(rs);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return found;
    }

    /**
    public Vector getYears() {
        Vector years = new Vector(50);
        String query = "SELECT DISTINCT yr FROM " + TABLE;
        query += " ORDER BY yr desc";
        try {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                found = true;
                years.addElement(rs.getString("yr"));
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        years.trimToSize();
        return years;
    }

    /**
    public void extractData(ResultSet rs) {
        try {
            stn = rs.getString("stn");
        } catch (Exception e) {
        }
        try {
            year = rs.getInt("yr");
        } catch (Exception e) {
        }
        try {
            month = rs.getInt("mo");
        } catch (Exception e) {
        }
        try {
            source = rs.getString("source");
        } catch (Exception e) {
        }
        try {
            numCorrected = rs.getInt("numCorrected");
            numUncorrected = rs.getInt("numUncorrected");
            numPressure = rs.getInt("numPressure");
            avgCorrected = rs.getInt("avgCorrected");
            avgUncorrected = rs.getInt("avgUncorrected");
            avgPressure = rs.getInt("avgPressure");
            minCorrected = rs.getInt("minCorrected");
            minUncorrected = rs.getInt("minUncorrected");
            minPressure = rs.getInt("minPressure");
            maxCorrected = rs.getInt("maxCorrected");
            maxUncorrected = rs.getInt("maxUncorrected");
            maxPressure = rs.getInt("maxPressure");
            found = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
    public boolean isFound() {
        return found;
    }

    /**
    public boolean delete(String station, int yr, int mo, String src) {
        setStn(station);
        setYear(yr);
        setMonth(mo);
        setSource(src);
        return delete();
    }

    /**
    public boolean delete() {
        if (Debug) System.out.println("Delete inventory for station=" + stn + " year=" + year + " month=" + month + " source=" + source + "...");
        Ndeleted = 0;
        try {
            String sql = "DELETE FROM inventory";
            if (stn != null || source != null || year > 0 || month > 0) sql += " WHERE ";
            if (stn != null) {
                sql += "stn='" + stn + "'";
            }
            if (year > 0) {
                if (stn != null) sql += " AND ";
                sql += "yr=" + year;
            }
            if (month > 0) {
                if (stn != null || year > 0) sql += " AND ";
                sql += "mo=" + month;
            }
            if (source != null) {
                if (stn != null || year > 0 || month > 0) sql += " AND ";
                sql += "source='" + source + "'";
            }
            if (Debug) System.out.println(sql);
            Ndeleted += stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
    public boolean insert() {
        if (Debug) System.out.println("Insert inventory for station=" + stn + " year=" + year + " month=" + month + " source=" + source + "...");
        PreparedStatement stmtInsert = null;
        try {
            String sql = "INSERT INTO inventory VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmtInsert = connection.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println("***ERROR: Unable to create a prepared statement.");
            return false;
        }
        CosmDataList cl = null;
        CosmDataList4 cl4 = null;
        if (source.equals("G")) {
            cl = new CosmDataList(connection);
            cl.setStn(stn);
            cl.setDateRange(year, month);
            cl.getStnYrMo();
            cl.calculateStats();
        } else {
            cl4 = new CosmDataList4(connection);
            cl4.setStn(stn);
            cl4.setDateRange(year, month);
            cl4.getStnYrMo();
            cl4.calculateStats();
        }
        try {
            stmtInsert.setString(1, stn);
            stmtInsert.setInt(2, year);
            stmtInsert.setInt(3, month);
            stmtInsert.setString(4, source);
            if (source.equals("G")) {
                stmtInsert.setInt(5, cl.getNumCorrected());
                stmtInsert.setInt(6, cl.getNumUncorrected());
                stmtInsert.setInt(7, cl.getNumPressure());
                stmtInsert.setInt(8, cl.getAvgCorrected());
                stmtInsert.setInt(9, cl.getAvgUncorrected());
                stmtInsert.setInt(10, cl.getAvgPressure());
                stmtInsert.setInt(11, cl.getMinCorrected());
                stmtInsert.setInt(12, cl.getMinUncorrected());
                stmtInsert.setInt(13, cl.getMinPressure());
                stmtInsert.setInt(14, cl.getMaxCorrected());
                stmtInsert.setInt(15, cl.getMaxUncorrected());
                stmtInsert.setInt(16, cl.getMaxPressure());
            } else {
                stmtInsert.setInt(5, cl4.getNumCorrected());
                stmtInsert.setInt(6, 0);
                stmtInsert.setInt(7, 0);
                stmtInsert.setInt(8, cl4.getAvgCorrected());
                stmtInsert.setInt(9, 0);
                stmtInsert.setInt(10, 0);
                stmtInsert.setInt(11, cl4.getMinCorrected());
                stmtInsert.setInt(12, 0);
                stmtInsert.setInt(13, 0);
                stmtInsert.setInt(14, cl4.getMaxCorrected());
                stmtInsert.setInt(15, 0);
                stmtInsert.setInt(16, 0);
            }
            if (!ErrorCheckOnly) Ninserted += stmtInsert.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
    public boolean updateInventory() {
        if (!delete()) return false;
        String query = "SELECT DISTINCT ";
        if (source.equals("G")) {
            query += "datagf.stn,";
        } else if (source.equals("4")) {
            query += "data4096.stn,";
        }
        query += "year(obsdate) as yr,month(obsdate) as mo";
        if (source.equals("G")) {
            query += " FROM datagf,hdrgf";
            query += " WHERE datagf.stn=hdrgf.stn";
            query += " AND yr=hdrgf.yr";
            if (stn != null) query += " AND datagf.stn='" + stn + "'";
        } else if (source.equals("4")) {
            query += " FROM data4096,hdr4096";
            query += " WHERE data4096.stn=hdr4096.stn";
            query += " AND yr=hdr4096.yr AND mo=hdr4096.mo";
            if (stn != null) query += " AND data4096.stn='" + stn + "'";
        }
        if (year > 0) query += " AND year(obsdate)=" + year;
        if (month > 0) query += " AND month(obsdate)=" + month;
        if (Debug) System.out.println(query);
        System.out.flush();
        DBConnect con = new DBConnect(connection);
        Statement stmt2 = con.getStatement();
        try {
            ResultSet rs = stmt2.executeQuery(query);
            while (rs.next()) {
                try {
                    stn = rs.getString("stn");
                } catch (Exception e) {
                }
                try {
                    year = rs.getInt("yr");
                } catch (Exception e) {
                }
                try {
                    month = rs.getInt("mo");
                } catch (Exception e) {
                }
                insert();
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println(" *** " + e.getSQLState());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
    public boolean update() {
        if (!delete()) return false;
        return insert();
    }

    /**
    public void print() {
        if (!found) {
            System.out.println("\n*** Cosmic rays inventory info not found");
            return;
        }
        Format d7 = new Format("%7d");
        System.out.println("\n    *** Cosmic rays inventory info ***");
        System.out.println("Station=" + stn + "  Year: " + year + "  Month: " + month + "  Source: " + source);
        System.out.println("        Corrected  Uncorrected    Pressure");
        System.out.println("Number:  " + d7.form(numCorrected) + "     " + d7.form(numUncorrected) + "     " + d7.form(numPressure));
        System.out.println("Average: " + d7.form(avgCorrected) + "     " + d7.form(avgUncorrected) + "     " + d7.form(avgPressure));
        System.out.println("Minimum: " + d7.form(minCorrected) + "     " + d7.form(minUncorrected) + "     " + d7.form(minPressure));
        System.out.println("Maximum: " + d7.form(maxCorrected) + "     " + d7.form(maxUncorrected) + "     " + d7.form(maxPressure));
    }

    /**
    public void printhdg() {
        System.out.print("                                ");
        System.out.print("Corrected           ");
        System.out.print("Uncorrected         ");
        System.out.println("Pressure");
        System.out.println("Stn    Yr   Mo Monitor  Source");
        System.out.print("  Num  Avg  Min  Max");
        System.out.print("  Num  Avg  Min  Max");
        System.out.print("  Num  Avg  Min  Max");
        System.out.println("  Num  Avg  Min  Max");
    }

    /**
    public void printline() {
        if (!found) {
            return;
        }
        Format d7 = new Format("%7d");
        System.out.print(stn + " " + year + " " + month + " " + source);
        if (numCorrected > 0) System.out.print(d7.form(numCorrected) + d7.form(avgCorrected) + d7.form(minCorrected) + d7.form(maxCorrected)); else System.out.print("                    ");
        if (numUncorrected > 0) System.out.print(d7.form(numUncorrected) + d7.form(avgUncorrected) + d7.form(minUncorrected) + d7.form(maxUncorrected));
        System.out.print("                    ");
        if (numPressure > 0) System.out.print(d7.form(numPressure) + d7.form(avgPressure) + d7.form(minPressure) + d7.form(maxPressure));
    }
}
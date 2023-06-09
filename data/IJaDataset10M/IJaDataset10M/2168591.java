package org.apache.derbyDemo.vtis.example;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import org.apache.derbyDemo.vtis.core.*;

/**
 * <p>
 * This VTI makes a table out of the output of the subversion log ("svn log") command.
 * </p>
 */
public class SubversionLogVTI extends FlatFileVTI {

    private static final String[] COLUMN_NAMES = { "XID", "committer", "commit_time", "line_count", "description" };

    private static final int XID = 0;

    private static final int COMMITTER = XID + 1;

    private static final int COMMIT_TIME = COMMITTER + 1;

    private static final int LINE_COUNT = COMMIT_TIME + 1;

    private static final int DESCRIPTION = LINE_COUNT + 1;

    private static final String RECORD_HEADER = "------------------------------------------------------------------------";

    private SimpleDateFormat _dateFormatter;

    /**
     * <p>
     * Build a SubversionLogVTI given the name of the output file created by the
     * "svn log" command.
     * </p>
     */
    public SubversionLogVTI(String logFileName) {
        super(COLUMN_NAMES, logFileName);
    }

    /**
     * <p>
     * This is the method which is registered as a table function.
     * </p>
     */
    public static ResultSet subversionLogVTI(String logFileName) throws SQLException {
        return new SubversionLogVTI(logFileName);
    }

    /**
     * <p>
     * Parse the next chunk of text, using readLine(), and return the next row.
     * Returns null if the file is exhausted.
     * </p>
     */
    protected String[] parseRow() throws SQLException {
        String[] newRow = new String[COLUMN_NAMES.length];
        String headerLine = readNextLine();
        if (headerLine == null) {
            return null;
        }
        if (!isRecordHeader(headerLine)) {
            throw new SQLException("Badly formatted record header: " + headerLine);
        }
        String mainline = readNextLine();
        if (mainline == null) {
            return null;
        }
        int oldIdx[] = new int[1];
        oldIdx[0] = 0;
        for (int i = 0; i < DESCRIPTION; i++) {
            newRow[i] = readField(mainline, oldIdx);
        }
        int descriptionLineCount = 0;
        try {
            String lineCountField = newRow[LINE_COUNT];
            if (lineCountField != null) {
                lineCountField = lineCountField.trim();
                String numberString = lineCountField.substring(0, lineCountField.indexOf(' '));
                descriptionLineCount = Integer.parseInt(numberString);
            }
        } catch (Throwable t) {
            throw wrap("Error parsing description line count at line " + getLineNumber() + ": " + mainline, t);
        }
        descriptionLineCount++;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < descriptionLineCount; i++) {
            String nextLine = readNextLine();
            buffer.append(nextLine);
        }
        newRow[DESCRIPTION] = buffer.toString();
        return newRow;
    }

    /**
     * <p>
     * Subversion formats timestamps thusly: "2007-09-16 11:17:37 -0700 (Sun, 16 Sep 2007)"
     * </p>
     */
    public java.sql.Timestamp getTimestamp(int columnIndex) throws SQLException {
        String columnValue = getString(columnIndex).trim();
        try {
            SimpleDateFormat dateFormatter = getDateFormatter();
            java.util.Date rawDate = dateFormatter.parse(columnValue);
            long time = rawDate.getTime();
            return new java.sql.Timestamp(time);
        } catch (Throwable t) {
            throw new SQLException(t.getMessage());
        }
    }

    /**
     * <p>
     * Read the next field in the main line of the record. Fields are delimited
     * by | or line-end.
     * </p>
     */
    private String readField(String mainline, int[] oldIdx) throws SQLException {
        String result = null;
        int fieldStart = oldIdx[0];
        int fieldEnd = mainline.indexOf("|", fieldStart);
        if (fieldEnd < 0) {
            fieldEnd = mainline.length();
        }
        if (fieldStart > fieldEnd) {
            return null;
        }
        try {
            result = mainline.substring(fieldStart, fieldEnd);
            if (result != null) {
                result = result.trim();
            }
        } catch (Throwable t) {
            throw wrap("Bad record at line " + getLineNumber() + ". Field start = " + fieldStart + ", fieldEnd = " + fieldEnd + ": " + mainline, t);
        }
        oldIdx[0] = fieldEnd + 1;
        return result;
    }

    /**
     * <p>
     * Returns true if a line is a record header.
     * </p>
     */
    private boolean isRecordHeader(String line) {
        if (line.startsWith(RECORD_HEADER)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>
     * Read a line, possibly just using the last line that was pushed back.
     * </p>
     */
    private String readNextLine() throws SQLException {
        String retval;
        retval = readLine();
        return retval;
    }

    /**
     * <p>
     * Subversion formats timestamps thusly: "2007-09-16 11:17:37 -0700 (Sun, 16 Sep 2007)"
     * </p>
     */
    private SimpleDateFormat getDateFormatter() {
        if (_dateFormatter == null) {
            _dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z (EEE, dd MMM yyyy)");
        }
        return _dateFormatter;
    }
}

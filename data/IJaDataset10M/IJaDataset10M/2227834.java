package java.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

/**
 * This interface provides access to the data set returned by a SQL
 * statement.  An instance of this interface is returned by the various
 * execution methods in the <code>Statement</code>.
 *
 * <p> This class models a cursor, which can be stepped through one row at a
 * time.  Methods are provided for accessing columns by column name or by
 * index.</p>
 *
 * <p> Note that a result set is invalidated if the statement that returned
 * it is closed.</p>
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 */
public interface ResultSet {

    /**
   * The rows will be processed in order from first to last.
   */
    int FETCH_FORWARD = 1000;

    /**
   * The rows will be processed in order from last to first.
   */
    int FETCH_REVERSE = 1001;

    /**
   * The rows will be processed in an unknown order
   */
    int FETCH_UNKNOWN = 1002;

    /**
   * This type of result set may only step forward through the rows returned.
   */
    int TYPE_FORWARD_ONLY = 1003;

    /**
   * This type of result set is scrollable and is not sensitive to changes
   * made by other statements.
   */
    int TYPE_SCROLL_INSENSITIVE = 1004;

    /**
   * This type of result set is scrollable and is also sensitive to changes
   * made by other statements.
   */
    int TYPE_SCROLL_SENSITIVE = 1005;

    /**
   * The concurrency mode of for the result set may not be modified.
   */
    int CONCUR_READ_ONLY = 1007;

    /**
   * The concurrency mode of for the result set may be modified.
   */
    int CONCUR_UPDATABLE = 1008;

    int HOLD_CURSORS_OVER_COMMIT = 1;

    int CLOSE_CURSORS_AT_COMMIT = 2;

    /**
   * This method advances to the next row in the result set.  Any streams
   * open on the current row are closed automatically.
   *
   * @return <code>true</code> if the next row exists, <code>false</code>
   *         otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean next() throws SQLException;

    /**
   * This method closes the result set and frees any associated resources.
   * 
   * @exception SQLException If an error occurs.
   */
    void close() throws SQLException;

    /**
   * This method tests whether the value of the last column that was fetched
   * was actually a SQL NULL value.
   *
   * @return <code>true</code> if the last column fetched was a NULL,
   *         <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean wasNull() throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>String</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>String</code>.
   * @exception SQLException If an error occurs.
   */
    String getString(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>boolean</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>boolean</code>.
   * @exception SQLException If an error occurs.
   */
    boolean getBoolean(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>byte</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>byte</code>.
   * @exception SQLException If an error occurs.
   */
    byte getByte(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>short</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>short</code>.
   * @exception SQLException If an error occurs.
   */
    short getShort(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>int</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>int</code>.
   * @exception SQLException If an error occurs.
   */
    int getInt(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>long</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>long</code>.
   * @exception SQLException If an error occurs.
   */
    long getLong(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>float</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>float</code>.
   * @exception SQLException If an error occurs.
   */
    float getFloat(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>double</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>double</code>.
   * @exception SQLException If an error occurs.
   */
    double getDouble(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>BigDecimal</code>.
   *
   * @param index The index of the column to return.
   * @param scale The number of digits to the right of the decimal to return.
   * @return The column value as a <code>BigDecimal</code>.
   * @exception SQLException If an error occurs.
   * @deprecated
   */
    BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * byte array.
   *
   * @param index The index of the column to return.
   * @return The column value as a byte array
   * @exception SQLException If an error occurs.
   */
    byte[] getBytes(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Date</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>java.sql.Date</code>.
   * @exception SQLException If an error occurs.
   */
    Date getDate(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Time</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>java.sql.Time</code>.
   * @exception SQLException If an error occurs.
   */
    Time getTime(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Timestamp</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>java.sql.Timestamp</code>.
   * @exception SQLException If an error occurs.
   */
    Timestamp getTimestamp(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as an ASCII 
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param index The index of the column to return.
   * @return The column value as an ASCII <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   */
    InputStream getAsciiStream(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Unicode UTF-8
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param index The index of the column to return.
   * @return The column value as a Unicode UTF-8 <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   * @deprecated Use getCharacterStream instead.
   */
    InputStream getUnicodeStream(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a raw byte
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param index The index of the column to return.
   * @return The column value as a raw byte <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   */
    InputStream getBinaryStream(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>String</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>String</code>.
   * @exception SQLException If an error occurs.
   */
    String getString(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>boolean</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>boolean</code>.
   * @exception SQLException If an error occurs.
   */
    boolean getBoolean(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>byte</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>byte</code>.
   * @exception SQLException If an error occurs.
   */
    byte getByte(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>short</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>short</code>.
   * @exception SQLException If an error occurs.
   */
    short getShort(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>int</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>int</code>.
   * @exception SQLException If an error occurs.
   */
    int getInt(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>long</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>long</code>.
   * @exception SQLException If an error occurs.
   */
    long getLong(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>float</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>float</code>.
   * @exception SQLException If an error occurs.
   */
    float getFloat(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>double</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>double</code>.
   * @exception SQLException If an error occurs.
   */
    double getDouble(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>BigDecimal</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>BigDecimal</code>.
   * @exception SQLException If an error occurs.
   * @deprecated
   */
    BigDecimal getBigDecimal(String columnName, int scale) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * byte array.
   *
   * @param column The name of the column to return.
   * @return The column value as a byte array
   * @exception SQLException If an error occurs.
   */
    byte[] getBytes(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Date</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>java.sql.Date</code>.
   * @exception SQLException If an error occurs.
   */
    Date getDate(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Time</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>java.sql.Time</code>.
   * @exception SQLException If an error occurs.
   */
    Time getTime(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>java.sql.Timestamp</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>java.sql.Timestamp</code>.
   * @exception SQLException If an error occurs.
   */
    Timestamp getTimestamp(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as an ASCII 
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param column The name of the column to return.
   * @return The column value as an ASCII <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   */
    InputStream getAsciiStream(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Unicode UTF-8
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param column The name of the column to return.
   * @return The column value as a Unicode UTF-8 <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   * @deprecated Use getCharacterStream instead.
   */
    InputStream getUnicodeStream(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a raw byte
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param column The name of the column to return.
   * @return The column value as a raw byte <code>InputStream</code>.
   * @exception SQLException If an error occurs.
   */
    InputStream getBinaryStream(String columnName) throws SQLException;

    /**
   * This method returns the first SQL warning associated with this result
   * set.  Any additional warnings will be chained to this one.
   *
   * @return The first SQLWarning for this result set, or <code>null</code> if
   *         there are no warnings.
   * @exception SQLException If an error occurs.
   */
    SQLWarning getWarnings() throws SQLException;

    /**
   * This method clears all warnings associated with this result set.
   *
   * @exception SQLException If an error occurs.
   */
    void clearWarnings() throws SQLException;

    /**
   * This method returns the name of the database cursor used by this
   * result set.
   *
   * @return The name of the database cursor used by this result set.
   * @exception SQLException If an error occurs.
   */
    String getCursorName() throws SQLException;

    /**
   * This method returns data about the columns returned as part of the
   * result set as a <code>ResultSetMetaData</code> instance.
   *
   * @return The <code>ResultSetMetaData</code> instance for this result set.
   * @exception SQLException If an error occurs.
   */
    ResultSetMetaData getMetaData() throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>Object</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as an <code>Object</code>.
   * @exception SQLException If an error occurs.
   */
    Object getObject(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>Object</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as an <code>Object</code>.
   * @exception SQLException If an error occurs.
   */
    Object getObject(String columnName) throws SQLException;

    /**
   * This method returns the column index of the specified named column.
   *
   * @param column The name of the column.
   * @return The index of the column.
   * @exception SQLException If an error occurs.
   */
    int findColumn(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a character
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param index The index of the column to return.
   * @return The column value as an character <code>Reader</code>.
   * @exception SQLException If an error occurs.
   */
    Reader getCharacterStream(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a character
   * stream.  Note that all the data from this stream must be read before
   * fetching the value of any other column.  Please also be aware that 
   * calling <code>next()</code> or <code>close()</code> on this result set
   * will close this stream as well.
   *
   * @param column The name of the column to return.
   * @return The column value as an character <code>Reader</code>.
   * @exception SQLException If an error occurs.
   */
    Reader getCharacterStream(String columnName) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>BigDecimal</code>.
   *
   * @param index The index of the column to return.
   * @return The column value as a <code>BigDecimal</code>.
   * @exception SQLException If an error occurs.
   */
    BigDecimal getBigDecimal(int columnIndex) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>BigDecimal</code>.
   *
   * @param column The name of the column to return.
   * @return The column value as a <code>BigDecimal</code>.
   * @exception SQLException If an error occurs.
   */
    BigDecimal getBigDecimal(String columnName) throws SQLException;

    /**
   * This method tests whether or not the cursor is before the first row
   * in the result set.
   *
   * @return <code>true</code> if the cursor is positioned before the first
   *         row, <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean isBeforeFirst() throws SQLException;

    /**
   * This method tests whether or not the cursor is after the last row
   * in the result set.
   *
   * @return <code>true</code> if the cursor is positioned after the last
   *         row, <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean isAfterLast() throws SQLException;

    /**
   * This method tests whether or not the cursor is positioned on the first
   * row in the result set.
   *
   * @return <code>true</code> if the cursor is positioned on the first
   *         row, <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean isFirst() throws SQLException;

    /**
   * This method tests whether or not the cursor is on the last row
   * in the result set.
   *
   * @return <code>true</code> if the cursor is positioned on the last
   *         row, <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean isLast() throws SQLException;

    /**
   * This method repositions the cursor to before the first row in the
   * result set.
   * 
   * @exception SQLException If an error occurs.
   */
    void beforeFirst() throws SQLException;

    /**
   * This method repositions the cursor to after the last row in the result
   * set.
   * 
   * @exception SQLException If an error occurs.
   */
    void afterLast() throws SQLException;

    /**
   * This method repositions the cursor on the first row in the
   * result set.
   *
   * @return <code>true</code> if the cursor is on a valid row;
   *         <code>false</code> if there are no rows in the result set.
   * @exception SQLException If an error occurs.
   */
    boolean first() throws SQLException;

    /**
   * This method repositions the cursor on the last row in the result
   * set.
   * 
   * @return <code>true</code> if the cursor is on a valid row;
   *         <code>false</code> if there are no rows in the result set.
   * @exception SQLException If an error occurs.
   */
    boolean last() throws SQLException;

    /**
   * This method returns the current row number in the cursor.  Numbering
   * begins at index 1.
   *
   * @return The current row number, or 0 if there is not current row.
   * @exception SQLException If an error occurs.
   */
    int getRow() throws SQLException;

    /**
   * This method positions the result set to the specified absolute row.
   * Positive numbers are row offsets from the beginning of the result
   * set (numbering starts from row 1) and negative numbers are row offsets
   * from the end of the result set (numbering starts from -1).
   *
   * @param row The row to position the result set to.
   *
   * @return <code>true</code> if the current position was changed,
   *         <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean absolute(int row) throws SQLException;

    /**
   * This method moves the result set position relative to the current row.
   * The offset can be positive or negative.
   *
   * @param row The relative row position to move to.
   * @return <code>true</code> if the current position was changed,
   *         <code>false</code> otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean relative(int rows) throws SQLException;

    /**
   * This method moves the current position to the previous row in the
   * result set.
   *
   * @return <code>true</code> if the previous row exists, <code>false</code>
   *         otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean previous() throws SQLException;

    /**
   * This method provides a hint to the driver about which direction the
   * result set will be processed in. 
   *
   * @param direction The direction in which rows will be processed. (Values?)
   * @exception SQLException If an error occurs.
   */
    void setFetchDirection(int direction) throws SQLException;

    /**
   * This method returns the current fetch direction for this result set.
   *
   * @return The fetch direction for this result set.
   * @exception SQLException If an error occurs.
   */
    int getFetchDirection() throws SQLException;

    /**
   * This method provides a hint to the driver about how many rows at a
   * time it should fetch from the database.
   *
   * @param rows The number of rows the driver should fetch per call.
   * @exception SQLException If an error occurs.
   */
    void setFetchSize(int rows) throws SQLException;

    /**
   * This method returns the current number of rows that will be fetched 
   * from the database at a time.
   *
   * @return The current fetch size for this result set.
   * @exception SQLException If an error occurs.
   */
    int getFetchSize() throws SQLException;

    /**
   * This method returns the result set type of this result set.  This will
   * be one of the TYPE_* constants defined in this interface.
   *
   * @return The result set type.
   * @exception SQLException If an error occurs.
   */
    int getType() throws SQLException;

    /**
   * This method returns the concurrency type of this result set.  This will
   * be one of the CONCUR_* constants defined in this interface.
   *
   * @return The result set concurrency type.
   * @exception SQLException If an error occurs.
   */
    int getConcurrency() throws SQLException;

    /**
   * This method tests whether or not the current row in the result set
   * has been updated.  Updates must be visible in order of this method to
   * detect the update.
   *
   * @return <code>true</code> if the row has been updated, <code>false</code>
   *         otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean rowUpdated() throws SQLException;

    /**
   * This method tests whether or not the current row in the result set
   * has been inserted.  Inserts must be visible in order of this method to
   * detect the insert.
   *
   * @return <code>true</code> if the row has been inserted, <code>false</code>
   *         otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean rowInserted() throws SQLException;

    /**
   * This method tests whether or not the current row in the result set
   * has been deleted.  Deletes must be visible in order of this method to
   * detect the deletion.
   *
   * @return <code>true</code> if the row has been deleted, <code>false</code>
   *         otherwise.
   * @exception SQLException If an error occurs.
   */
    boolean rowDeleted() throws SQLException;

    /**
   * This method updates the specified column to have a NULL value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @return index The index of the column to update.
   * @exception SQLException If an error occurs.
   */
    void updateNull(int columnIndex) throws SQLException;

    /**
   * This method updates the specified column to have a boolean value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBoolean(int columnIndex, boolean x) throws SQLException;

    /**
   * This method updates the specified column to have a byte value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateByte(int columnIndex, byte x) throws SQLException;

    /**
   * This method updates the specified column to have a short value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateShort(int columnIndex, short x) throws SQLException;

    /**
   * This method updates the specified column to have an int value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateInt(int columnIndex, int x) throws SQLException;

    /**
   * This method updates the specified column to have a long value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateLong(int columnIndex, long x) throws SQLException;

    /**
   * This method updates the specified column to have a float value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateFloat(int columnIndex, float x) throws SQLException;

    /**
   * This method updates the specified column to have a double value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateDouble(int columnIndex, double x) throws SQLException;

    /**
   * This method updates the specified column to have a BigDecimal value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException;

    /**
   * This method updates the specified column to have a String value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateString(int columnIndex, String x) throws SQLException;

    /**
   * This method updates the specified column to have a byte array value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBytes(int columnIndex, byte[] x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Date value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateDate(int columnIndex, Date x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Time value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateTime(int columnIndex, Time x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Timestamp value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateTimestamp(int columnIndex, Timestamp x) throws SQLException;

    /**
   * This method updates the specified column from an ASCII text stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   * @exception SQLException If an error occurs.
   */
    void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException;

    /**
   * This method updates the specified column from a binary stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   * @exception SQLException If an error occurs.
   */
    void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException;

    /**
   * This method updates the specified column from a character stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   * @exception SQLException If an error occurs.
   */
    void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException;

    /**
   * This method updates the specified column to have an Object value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   *
   * @exception SQLException If an error occurs.
   */
    void updateObject(int columnIndex, Object x, int scale) throws SQLException;

    /**
   * This method updates the specified column to have an Object value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param index The index of the column to update.
   * @param value The new value of the column.
   * @param scale The scale of the object in question, which is used only
   *        for numeric type objects.
   * @exception SQLException If an error occurs.
   */
    void updateObject(int columnIndex, Object x) throws SQLException;

    /**
   * This method updates the specified column to have a NULL value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @return name The name of the column to update.
   * @exception SQLException If an error occurs.
   */
    void updateNull(String columnName) throws SQLException;

    /**
   * This method updates the specified column to have a boolean value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBoolean(String columnName, boolean x) throws SQLException;

    /**
   * This method updates the specified column to have a byte value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateByte(String columnName, byte x) throws SQLException;

    /**
   * This method updates the specified column to have a short value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateShort(String columnName, short x) throws SQLException;

    /**
   * This method updates the specified column to have an int value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateInt(String columnName, int x) throws SQLException;

    /**
   * This method updates the specified column to have a long value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateLong(String columnName, long x) throws SQLException;

    /**
   * This method updates the specified column to have a float value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateFloat(String columnName, float x) throws SQLException;

    /**
   * This method updates the specified column to have a double value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateDouble(String columnName, double x) throws SQLException;

    /**
   * This method updates the specified column to have a BigDecimal value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBigDecimal(String columnName, BigDecimal x) throws SQLException;

    /**
   * This method updates the specified column to have a String value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateString(String columnName, String x) throws SQLException;

    /**
   * This method updates the specified column to have a byte array value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateBytes(String columnName, byte[] x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Date value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateDate(String columnName, Date x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Time value.  This
   * does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateTime(String columnName, Time x) throws SQLException;

    /**
   * This method updates the specified column to have a java.sql.Timestamp value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateTimestamp(String columnName, Timestamp x) throws SQLException;

    /**
   * This method updates the specified column from an ASCII text stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   * @exception SQLException If an error occurs.
   */
    void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException;

    /**
   * This method updates the specified column from a binary stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   * @exception SQLException If an error occurs.
   */
    void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException;

    /**
   * This method updates the specified column from a character stream.
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @param length The length of the stream.
   *
   * @exception SQLException If an error occurs.
   */
    void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException;

    /**
   * This method updates the specified column to have an Object value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @exception SQLException If an error occurs.
   */
    void updateObject(String columnName, Object x, int scale) throws SQLException;

    /**
   * This method updates the specified column to have an Object value.  
   * This does not update the actual database.  <code>updateRow</code> must be
   * called in order to do that.
   *
   * @param name The name of the column to update.
   * @param value The new value of the column.
   * @param scale The scale of the object in question, which is used only
   *        for numeric type objects.
   * @exception SQLException If an error occurs.
   */
    void updateObject(String columnName, Object x) throws SQLException;

    /**
   * This method inserts the current row into the database.  The result set
   * must be positioned on the insert row in order to call this method
   * successfully.
   *
   * @exception SQLException If an error occurs.
   */
    void insertRow() throws SQLException;

    /**
   * This method updates the current row in the database.
   *
   * @exception SQLException If an error occurs.
   */
    void updateRow() throws SQLException;

    /**
   * This method deletes the current row in the database.
   *
   * @exception SQLException If an error occurs.
   */
    void deleteRow() throws SQLException;

    /**
   * This method refreshes the contents of the current row from the database.
   *
   * @exception SQLException If an error occurs.
   */
    void refreshRow() throws SQLException;

    /**
   * This method cancels any changes that have been made to a row.  If 
   * the <code>rowUpdate</code> method has been called, then the changes
   * cannot be undone.
   *
   * @exception SQLException If an error occurs.
   */
    void cancelRowUpdates() throws SQLException;

    /**
   * This method positions the result set to the "insert row", which allows
   * a new row to be inserted into the database from the result set.
   *
   * @exception SQLException If an error occurs.
   */
    void moveToInsertRow() throws SQLException;

    /**
   * This method moves the result set position from the insert row back to
   * the current row that was selected prior to moving to the insert row.
   *
   * @exception SQLException If an error occurs.
   */
    void moveToCurrentRow() throws SQLException;

    /**
   * This method returns a the <code>Statement</code> that was used to
   * produce this result set.
   *
   * @return The <code>Statement</code> used to produce this result set.
   *
   * @exception SQLException If an error occurs.
   */
    Statement getStatement() throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>Object</code> using the specified SQL type to Java type map.
   *
   * @param index The index of the column to return.
   * @param map The SQL type to Java type map to use.
   * @return The value of the column as an <code>Object</code>.
   * @exception SQLException If an error occurs.
   */
    Object getObject(int i, Map map) throws SQLException;

    /**
   * This method returns a <code>Ref</code> for the specified column which
   * represents the structured type for the column.
   *
   * @param index  The index of the column to return.
   * @return A <code>Ref</code> object for the column
   * @exception SQLException If an error occurs.
   */
    Ref getRef(int i) throws SQLException;

    /**
   * This method returns the specified column value as a BLOB.
   *
   * @param index The index of the column value to return.
   * @return The value of the column as a BLOB.
   * @exception SQLException If an error occurs.
   */
    Blob getBlob(int i) throws SQLException;

    /**
   * This method returns the specified column value as a CLOB.
   *
   * @param index The index of the column value to return.
   * @return The value of the column as a CLOB.
   * @exception SQLException If an error occurs.
   */
    Clob getClob(int i) throws SQLException;

    /**
   * This method returns the specified column value as an <code>Array</code>.
   *
   * @param index The index of the column value to return.
   * @return The value of the column as an <code>Array</code>.
   * @exception SQLException If an error occurs.
   */
    Array getArray(int i) throws SQLException;

    /**
   * This method returns the value of the specified column as a Java
   * <code>Object</code> using the specified SQL type to Java type map.
   *
   * @param name The name of the column to return.
   * @param map The SQL type to Java type map to use.
   * @return The value of the column as an <code>Object</code>.
   * @exception SQLException If an error occurs.
   */
    Object getObject(String colName, Map map) throws SQLException;

    /**
   * This method returns a <code>Ref</code> for the specified column which
   * represents the structured type for the column.
   *
   * @param index  The index of the column to return.
   * @return A <code>Ref</code> object for the column
   * @exception SQLException If an error occurs.
   */
    Ref getRef(String colName) throws SQLException;

    /**
   * This method returns the specified column value as a BLOB.
   *
   * @param name The name of the column value to return.
   * @return The value of the column as a BLOB.
   * @exception SQLException If an error occurs.
   */
    Blob getBlob(String colName) throws SQLException;

    /**
   * This method returns the specified column value as a CLOB.
   *
   * @param name The name of the column value to return.
   * @return The value of the column as a CLOB.
   * @exception SQLException If an error occurs.
   */
    Clob getClob(String colName) throws SQLException;

    /**
   * This method returns the specified column value as an <code>Array</code>.
   *
   * @param name The name of the column value to return.
   * @return The value of the column as an <code>Array</code>.
   * @exception SQLException If an error occurs.
   */
    Array getArray(String colName) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Date</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the date if the database does not support
   * timezones.
   *
   * @param index The index of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   * @return The value of the column as a <code>java.sql.Date</code>.
   * @exception SQLException If an error occurs.
   */
    Date getDate(int columnIndex, Calendar cal) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Date</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the date if the database does not support
   * timezones.
   *
   * @param name The name of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   * @return The value of the column as a <code>java.sql.Date</code>.
   * @exception SQLException If an error occurs.
   */
    Date getDate(String columnName, Calendar cal) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Time</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the time if the database does not support
   * timezones.
   *
   * @param index The index of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   * @return The value of the column as a <code>java.sql.Time</code>.
   * @exception SQLException If an error occurs.
   */
    Time getTime(int columnIndex, Calendar cal) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Time</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the time if the database does not support
   * timezones.
   *
   * @param name The name of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   * @return The value of the column as a <code>java.sql.Time</code>.
   * @exception SQLException If an error occurs.
   */
    Time getTime(String columnName, Calendar cal) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Timestamp</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the timestamp if the database does not support
   * timezones.
   *
   * @param index The index of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   * @return The value of the column as a <code>java.sql.Timestamp</code>.
   * @exception SQLException If an error occurs.
   */
    Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException;

    /**
   * This method returns the specified column value as a 
   * <code>java.sql.Timestamp</code>.  The specified <code>Calendar</code> is used
   * to generate a value for the timestamp if the database does not support
   * timezones.
   *
   * @param name The name of the column value to return.
   * @param cal The <code>Calendar</code> to use for calculating timezones.
   *
   * @return The value of the column as a <code>java.sql.Timestamp</code>.
   *
   * @exception SQLException If an error occurs.
   */
    Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException;

    /**
   * @since 1.4
   */
    URL getURL(int columnIndex) throws SQLException;

    /**
   * @since 1.4
   */
    URL getURL(String columnName) throws SQLException;

    /**
   * @since 1.4
   */
    void updateRef(int columnIndex, Ref x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateRef(String columnName, Ref x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateBlob(int columnIndex, Blob x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateBlob(String columnName, Blob x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateClob(int columnIndex, Clob x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateClob(String columnName, Clob x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateArray(int columnIndex, Array x) throws SQLException;

    /**
   * @since 1.4
   */
    void updateArray(String columnName, Array x) throws SQLException;
}

package java.sql;

import java.util.Map;

/**
 * This interface provides methods for accessing SQL array types.
 *
 * @author Aaron M. Renn (arenn@urbanophile.com)
 */
public interface Array {

    /**
   * Returns the name of the SQL type of the elements in this
   * array.  This name is database specific.
   *
   * @param The name of the SQL type of the elements in this array.
   * @exception SQLException If an error occurs.
   */
    String getBaseTypeName() throws SQLException;

    /**
   * Returns the JDBC type identifier of the elements in this
   * array.  This will be one of the values defined in the 
   * <code>Types</code> class.
   *
   * @return The JDBC type of the elements in this array.
   * @exception SQLException If an error occurs.
   * @see Types
   */
    int getBaseType() throws SQLException;

    /**
   * Returns the contents of this array.  This object returned
   * will be an array of Java objects of the appropriate types.
   *
   * @return The contents of the array as an array of Java objects.
   * @exception SQLException If an error occurs.
   */
    Object getArray() throws SQLException;

    /**
   * Returns the contents of this array.  The specified
   * <code>Map</code> will be used to override selected mappings 
   * between SQL types and Java classes.
   * 
   * @param map A mapping of SQL types to Java classes.
   * @return The contents of the array as an array of Java objects.
   * @exception SQLException If an error occurs.
   */
    Object getArray(Map map) throws SQLException;

    /**
   * Returns a portion of this array starting at <code>index</code>
   * into the array and continuing for <code>count</code>
   * elements.  Fewer than the requested number of elements will be
   * returned if the array does not contain the requested number of elements.
   * The object returned will be an array of Java objects of
   * the appropriate types.
   *
   * @param offset The offset into this array to start returning elements from.
   * @param count The requested number of elements to return.
   * @return The requested portion of the array.
   * @exception SQLException If an error occurs.
   */
    Object getArray(long index, int count) throws SQLException;

    /**
   * This method returns a portion of this array starting at <code>index</code>
   * into the array and continuing for <code>count</code>
   * elements.  Fewer than the requested number of elements will be
   * returned if the array does not contain the requested number of elements.
   * The object returned will be an array of Java objects.  The specified
   * <code>Map</code> will be used for overriding selected SQL type to
   * Java class mappings.
   *
   * @param offset The offset into this array to start returning elements from.
   * @param count The requested number of elements to return.
   * @param map A mapping of SQL types to Java classes.
   * @return The requested portion of the array.
   * @exception SQLException If an error occurs.
   */
    Object getArray(long index, int count, Map map) throws SQLException;

    /**
   * Returns the elements in the array as a <code>ResultSet</code>.
   * Each row of the result set will have two columns.  The first will be
   * the index into the array of that row's contents.  The second will be
   * the actual value of that array element.
   *
   * @return The elements of this array as a <code>ResultSet</code>.
   * @exception SQLException If an error occurs.
   * @see ResultSet
   */
    ResultSet getResultSet() throws SQLException;

    /**
   * This method returns the elements in the array as a <code>ResultSet</code>.
   * Each row of the result set will have two columns.  The first will be
   * the index into the array of that row's contents.  The second will be
   * the actual value of that array element.  The specified <code>Map</code>
   * will be used to override selected default mappings of SQL types to
   * Java classes.
   *
   * @param map A mapping of SQL types to Java classes.
   * @return The elements of this array as a <code>ResultSet</code>.
   * @exception SQLException If an error occurs.
   * @see ResultSet
   */
    ResultSet getResultSet(Map map) throws SQLException;

    /**
   * This method returns a portion of the array as a <code>ResultSet</code>.
   * The returned portion will start at <code>index</code> into the
   * array and up to <code>count</code> elements will be returned.
   * <p>
   * Each row of the result set will have two columns.  The first will be
   * the index into the array of that row's contents.  The second will be
   * the actual value of that array element.
   *
   * @param offset The index into the array to start returning elements from.
   * @param length The requested number of elements to return.
   * @return The requested elements of this array as a <code>ResultSet</code>.
   * @exception SQLException If an error occurs.
   * @see ResultSet
   */
    ResultSet getResultSet(long index, int count) throws SQLException;

    /**
   * This method returns a portion of the array as a <code>ResultSet</code>.
   * The returned portion will start at <code>index</code> into the
   * array and up to <code>count</code> elements will be returned.
   *
   * <p> Each row of the result set will have two columns.  The first will be
   * the index into the array of that row's contents.  The second will be
   * the actual value of that array element.  The specified <code>Map</code>
   * will be used to override selected default mappings of SQL types to
   * Java classes.</p>
   *
   * @param offset The index into the array to start returning elements from.
   * @param length The requested number of elements to return.
   * @param map A mapping of SQL types to Java classes.
   * @return The requested elements of this array as a <code>ResultSet</code>.
   * @exception SQLException If an error occurs.
   * @see ResultSet
   */
    ResultSet getResultSet(long index, int count, Map map) throws SQLException;
}

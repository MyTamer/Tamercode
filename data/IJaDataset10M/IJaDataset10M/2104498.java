package nz.ac.waikato.modeljunit.storytest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.IndexOutOfBoundsException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.HashSet;
import nz.ac.waikato.modeljunit.storytest.parse.Function;
import nz.ac.waikato.modeljunit.storytest.parse.Parser;
import nz.ac.waikato.modeljunit.storytest.parse.TypeRange;

/**
 *  Holds a table of Data Values for Testing
 */
public class CalcTable extends AbstractSubject implements StoryTestInterface {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    /** The name of this table */
    private final String mName;

    /** The Column Names */
    private final List<ColumnName> mColumns;

    /** The Matrix of test values */
    private final List<List<String>> mMatrix;

    /** The Assumed Types of each Column **/
    private final List<Class<?>> mTypes;

    /** Whether or not there is an Contradiction in a row */
    private final Set<Integer> mContradiction;

    /** Whether or not there is an Contradiction in a row */
    private final Set<Integer> mHighlighted;

    private final List<TypeRange> mTypeRanges;

    public CalcTable() {
        this("", Collections.singletonList("a"));
    }

    /**
    *  Creates a CalcTable with the given Column headings
    *
    *  @param columns  The column headings for this calc table
    */
    public CalcTable(String name, List<String> columns) {
        mName = name;
        mColumns = new ArrayList<ColumnName>(columns.size());
        mTypes = new ArrayList<Class<?>>();
        mTypeRanges = new ArrayList<TypeRange>();
        mContradiction = new HashSet<Integer>();
        for (int i = 0; i < columns.size(); i++) {
            mColumns.add(new ColumnName(columns.get(i).trim()));
            mTypes.add(null);
            try {
                mTypeRanges.add(Parser.parseTypeRange("", i, this));
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(0);
            }
        }
        mMatrix = new ArrayList<List<String>>();
        mHighlighted = new HashSet<Integer>();
        System.out.println("addrow");
        addRow();
    }

    public int getColumnNum(ColumnName name) {
        return mColumns.indexOf(name);
    }

    private Tester getTester(String string) {
        if (string.equals("T") || string.equals("F")) {
            return new BooleanTester();
        }
        try {
            Double.parseDouble(string);
            return new ConstructorTester(Double.class.getConstructor(String.class));
        } catch (NumberFormatException nfe) {
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isHighlighted(int row) {
        return mHighlighted.contains(row);
    }

    public boolean isEditable(int column) {
        return getTypeRange(column).isEditable();
    }

    public void setTypeRange(int column, TypeRange typerange) {
        mTypeRanges.set(column, typerange);
    }

    public void setHighlighted(int[] rows) {
        mHighlighted.clear();
        mContradiction.clear();
        for (int i = 0; i < rows.length; i++) {
            mHighlighted.add(rows[i]);
        }
        inform();
    }

    public boolean isContradiction(int row) {
        return mContradiction.contains(row);
    }

    public void setContradiction(int[] rows) {
        mHighlighted.clear();
        mContradiction.clear();
        for (int i = 0; i < rows.length; i++) {
            mContradiction.add(rows[i]);
        }
        inform();
    }

    public void toggleResultColumn(int column) {
        String header = getColumnHeader(column);
        if (isResult(column)) {
            header = header.substring(0, header.length() - 1);
        } else {
            header = header + "?";
        }
        setColumnHeader(column, header);
    }

    public void updateType(int column) {
        if (rows() == 0) {
            mTypes.set(column, null);
            return;
        }
        int i = 0;
        for (; i < mMatrix.size(); i++) {
            if (!getValue(i, column).equals("")) {
                break;
            }
        }
        if (i >= mMatrix.size()) {
            mTypes.set(column, null);
            return;
        }
        Tester tester = getTester(getValue(i, column));
        if (tester == null) {
            mTypes.set(column, null);
            return;
        }
        for (int r = 0; r < mMatrix.size(); r++) {
            if (getValue(r, column).equals("")) {
                continue;
            }
            if (getValue(r, column).equals("...")) {
                continue;
            }
            if (!tester.satisfies(getValue(r, column))) {
                mTypes.set(column, null);
                return;
            }
        }
        mTypes.set(column, tester.getType());
    }

    public String getName() {
        return mName;
    }

    public Class<?> guessType(int column) {
        return mTypes.get(column);
    }

    public Class<?> getType(int column) {
        return mTypeRanges.get(column).getType();
    }

    public TypeRange getTypeRange(int column) {
        return mTypeRanges.get(column);
    }

    /**
    * Creates a list filled with empty Strings the same size as mColumns
    */
    private List<String> createNewRow() {
        System.out.println("columns(): = " + columns() + "\tmColumns.size() = " + mColumns.size());
        List<String> list = new ArrayList<String>(columns());
        for (int i = 0; i < mColumns.size(); i++) {
            list.add("");
        }
        return list;
    }

    /**
    * Adds a row at index the end of the matrix
    *
    * The row is added to be before the row currently at index. Therefore if
    * index == 0 then it is added at the start of the matrix, if index == size() - 1
    * then the row is added just before the end of the matrix and if index == size()
    * it is added at the end
    *
    * @param index   Where to insert the row
    *
    * @throws IndexOutOfBoundsException If index is out of bounds
    */
    public void addRow(int index, List<String> row) {
        mMatrix.add(index, new ArrayList<String>(row));
        for (int c = 0; c < columns(); c++) {
            updateType(c);
        }
        inform();
    }

    public void addRow(List<String> row) {
        addRow(rows(), row);
    }

    /**
    * Adds a row at index the end of the matrix
    *
    * The row is added to be before the row currently at index. Therefore if
    * index == 0 then it is added at the start of the matrix, if index == size() - 1
    * then the row is added just before the end of the matrix and if index == size()
    * it is added at the end
    *
    * @param index   Where to insert the row
    *
    * @throws IndexOutOfBoundsException If index is out of bounds
    */
    public void addRow(int index) throws IndexOutOfBoundsException {
        addRow(index, createNewRow());
    }

    /**
    * Adds a row at the end of the matrix
    */
    public void addRow() {
        addRow(rows());
    }

    public List<String> getRow(int row) {
        return mMatrix.get(row);
    }

    /**
    * Adds a column with name columnName at index with fillValue.
    *
    * The column is added to be before the column currently at index. Therefore if
    * index == 0 then it is added at the start of the matrix, if index == size() - 1
    * then the column is added just before the end of the matrix and if index == size()
    * it is added at the end
    *
    * @param columnName The name of the new Column
    * @param index   Where to insert the column
    * @param fillValue  This falue will be used to fill the new column
    *
    * @throws IndexOutOfBoundsException if index is out of bounds
    */
    public void addColumn(String columnName, int index, String fillValue) throws IndexOutOfBoundsException {
        mColumns.add(index, new ColumnName(columnName));
        mTypes.add(index, null);
        for (List<String> list : mMatrix) {
            list.add(index, fillValue);
        }
        try {
            mTypeRanges.add(index, Parser.parseTypeRange("", index, this));
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(0);
        }
        updateType(index);
        inform();
    }

    /**
    * Adds a column with name columnName at index
    *
    * The column is added to be before the column currently at index. Therefore if
    * index == 0 then it is added at the start of the matrix, if index == size() - 1
    * then the column is added just before the end of the matrix and if index == size()
    * it is added at the end
    *
    * @param columnName The name of the new Column
    * @param index   Where to insert the column
    *
    * @throws IndexOutOfBoundsException if index is out of bounds
    */
    public void addColumn(String columnName, int index) throws IndexOutOfBoundsException {
        addColumn(columnName, index, "");
    }

    /**
    * Adds a column with name columnName
    */
    public void addColumn(String columnName) {
        addColumn(columnName, columns());
    }

    /**
    * Removes the Specified Column
    *
    * @param index the column to be removed
    *
    * @throws IndexOutOfBoundsException if the index is out of bounds
    */
    public void removeColumn(int index) throws IndexOutOfBoundsException {
        mColumns.remove(index);
        for (List<String> list : mMatrix) {
            list.remove(index);
        }
        inform();
    }

    /**
    * Removes the Specified Row
    *
    * @param index the row to be removed
    *
    * @throws IndexOutOfBoundsException if the index is out of bounds
    */
    public void removeRow(int index) throws IndexOutOfBoundsException {
        mMatrix.remove(index);
        inform();
    }

    /**
    * Gets the number of rows
    */
    public int rows() {
        return mMatrix.size();
    }

    /**
    * Gets the number of columns
    */
    public int columns() {
        return mColumns.size();
    }

    /**
    * Sets the cell at row, column to value
    *
    * @param row  The Row to be edited
    * @param column  The column to be edited
    *
    * @throws IndexOutOfBoundsException if row or column is out of bounds
    */
    public void setValue(int row, int column, String value) throws IndexOutOfBoundsException {
        mMatrix.get(row).set(column, value);
        updateType(column);
        inform();
    }

    /**
    * gets the contents of the cell at row, column;
    *
    * @param row  The Row to be retrieved
    * @param column  The column to be retrieved
    *
    * @throws IndexOutOfBoundsException if row or column is out of bounds
    */
    public String getValue(int row, int column) throws IndexOutOfBoundsException {
        if (isEditable(column)) {
            return mMatrix.get(row).get(column);
        } else {
            return "" + getTypeRange(column).calculateRow(row);
        }
    }

    /**
    * returns the column header for index
    *
    * @throws IndexOutOfBoundsException if index is out of bounds
    */
    public String getColumnHeader(int index) throws IndexOutOfBoundsException {
        return mColumns.get(index).mName;
    }

    public ColumnName getColumnName(int index) {
        return mColumns.get(index);
    }

    public ColumnName getColumnName(String name) {
        for (ColumnName cn : mColumns) {
            if (cn.getName().equals(name)) {
                return cn;
            }
        }
        return null;
    }

    /**
    * Sets columns name to the new String name
    *
    * @throws IndexOutOfBoundsException if index is out of bounds
    */
    public void setColumnHeader(int column, String name) throws IndexOutOfBoundsException {
        mColumns.get(column).setName(name.trim());
        inform();
    }

    /** 
    * Determines whether the Specified Column is considered to give a result
    *
    * @throws IndexOutOfBoundsException if index is out of bounds
    */
    public boolean isResult(int column) throws IndexOutOfBoundsException {
        return getColumnHeader(column).endsWith("?");
    }

    /**
    * Returns a String representation of this CalcTable
    */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (ColumnName name : mColumns) {
            sb.append("|" + name.getName());
        }
        sb.append("|\n");
        for (List<String> list : mMatrix) {
            for (String value : list) {
                sb.append("|" + value);
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public String toHTML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"1\" cellspacing=\"1\" cellpadding=\"5\">\n");
        sb.append("\t<tr>\n");
        for (int c = 0; c < columns(); c++) {
            sb.append("\t\t<th>");
            sb.append(getColumnHeader(c));
            sb.append("</th>\n");
        }
        sb.append("\t</tr>\n");
        for (int r = 0; r < rows(); r++) {
            sb.append("\t<tr>\n");
            for (int c = 0; c < columns(); c++) {
                sb.append("\t\t<td>");
                sb.append(getValue(r, c));
                sb.append("</td>\n");
            }
            sb.append("\t</tr>\n");
        }
        sb.append("</table>\n");
        return sb.toString();
    }

    public Object accept(StoryTestVisitor<?> visitor, Object other) {
        return visitor.visit(this, other);
    }

    private static interface Tester {

        public boolean satisfies(String s);

        public Class<?> getType();
    }

    private static class BooleanTester implements Tester {

        public boolean satisfies(String s) {
            return s.equals("T") || s.equals("F");
        }

        public Class<?> getType() {
            return Boolean.class;
        }
    }

    private static class ConstructorTester implements Tester {

        private final Constructor<?> mConstructor;

        public ConstructorTester(Constructor<?> constructor) {
            mConstructor = constructor;
        }

        public boolean satisfies(String s) {
            try {
                mConstructor.newInstance(s);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                System.out.println(s);
                e.printStackTrace();
            }
            return false;
        }

        public Class<?> getType() {
            return mConstructor.getDeclaringClass();
        }
    }
}

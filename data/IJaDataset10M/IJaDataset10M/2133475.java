package org.joda.primitives.list.impl;

import java.util.Collection;
import org.joda.primitives.CharUtils;

/**
 * Immutable array-based implementation of <code>CharList</code> for
 * primitive <code>int</code> elements.
 * <p>
 * This class implements {@link java.util.List List} allowing
 * seamless integration with other APIs.
 * <p>
 * Add, Remove, Set and Clear are not supported as this class is immutable.
 *
 * @author Stephen Colebourne
 * @version CODE GENERATED
 * @since 1.0
 */
public final class ImmutableArrayCharList extends AbstractCharList {

    /** The empty singleton. */
    private static final ImmutableArrayCharList EMPTY = new ImmutableArrayCharList(CharUtils.EMPTY_CHAR_ARRAY);

    /** The array of elements. */
    private char[] data;

    /**
     * Gets a list that is empty.
     */
    public static ImmutableArrayCharList empty() {
        return EMPTY;
    }

    /**
     * Creates a list copying the specified array.
     * 
     * @param values  an array of values to copy, null treated as zero size array
     * @return the created list, not null
     */
    public static ImmutableArrayCharList copyOf(char[] values) {
        if (values == null) {
            return EMPTY;
        } else {
            return new ImmutableArrayCharList(values.clone());
        }
    }

    /**
     * Creates a list copying the values from the specified collection.
     * <p>
     * If the collection is an instance of this class, then it is simply returned.
     * 
     * @param coll  a collection of values to copy, null treated as zero size collection
     * @return the created list, not null
     */
    public static ImmutableArrayCharList copyOf(Collection<Character> coll) {
        if (coll == null) {
            return EMPTY;
        } else if (coll instanceof ImmutableArrayCharList) {
            return (ImmutableArrayCharList) coll;
        } else {
            return new ImmutableArrayCharList(CharUtils.toPrimitiveArray(coll));
        }
    }

    /**
     * Constructor that copies the specified values.
     * 
     * @param values  the array to assign
     */
    private ImmutableArrayCharList(char[] values) {
        super();
        data = values;
    }

    /**
     * Gets the current size of the collection.
     * 
     * @return the current size
     */
    public int size() {
        return data.length;
    }

    /**
     * Gets the primitive value at the specified index.
     *
     * @param index  the index to get from
     * @return value at the index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public char getChar(int index) {
        checkIndexExists(index);
        return data[index];
    }

    /**
     * Checks whether this collection contains a specified primitive value.
     * <p>
     * This implementation accesses the internal storage array directly.
     *
     * @param value  the value to search for
     * @return <code>true</code> if the value is found
     */
    public boolean contains(char value) {
        for (int i = 0; i < data.length; i++) {
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clone implementation that returns {@code this}.
     * 
     * @return {@code this}
     */
    public Object clone() {
        return this;
    }

    /**
     * Copies data from this collection into the specified array.
     * This method is pre-validated.
     * 
     * @param fromIndex  the index to start from
     * @param dest  the destination array
     * @param destIndex  the destination start index
     * @param size  the number of items to copy
     */
    protected void arrayCopy(int fromIndex, char[] dest, int destIndex, int size) {
        System.arraycopy(data, fromIndex, dest, destIndex, size);
    }
}

package org.joda.primitives.iterator.impl;

import java.util.NoSuchElementException;
import org.joda.primitives.ShortUtils;
import org.joda.primitives.iterator.ShortIterator;

/**
 * An iterator over an array of <code>short</code> values.
 * <p>
 * This class implements {@link java.util.Iterator Iterator} allowing
 * seamless integration with other APIs.
 * <p>
 * The iterator can be reset to the start if required.
 * It is unmodifiable and <code>remove()</code> is unsupported.
 *
 * @author Stephen Colebourne
 * @author Jason Tiscione
 * @version CODE GENERATED
 * @since 1.0
 */
public class ArrayShortIterator implements ShortIterator {

    /** The array to iterate over */
    protected final short[] array;

    /** Cursor position */
    protected int cursor = 0;

    /**
     * Creates an iterator over a copy of an array of <code>short</code> values.
     * <p>
     * The specified array is copied, making this class effectively immutable.
     * Note that the class is not {@code final} thus it is not truly immutable.
     * 
     * @param array  the array to iterate over, must not be null
     * @throws IllegalArgumentException if the array is null
     */
    public static ArrayShortIterator copyOf(short[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        return new ArrayShortIterator(array.clone());
    }

    /**
     * Constructs an iterator over an array of <code>short</code> values.
     * <p>
     * The array is assigned internally, thus the caller holds a reference to
     * the internal state of the returned iterator. It is not recommended to
     * modify the state of the array after construction.
     * 
     * @param array  the array to iterate over, must not be null
     * @throws IllegalArgumentException if the array is null
     */
    public ArrayShortIterator(short[] array) {
        super();
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        this.array = array;
    }

    public boolean isModifiable() {
        return false;
    }

    public boolean isResettable() {
        return true;
    }

    public boolean hasNext() {
        return (cursor < array.length);
    }

    public short nextShort() {
        if (hasNext() == false) {
            throw new NoSuchElementException("No more elements available");
        }
        return array[cursor++];
    }

    public Short next() {
        return ShortUtils.toObject(nextShort());
    }

    public void remove() {
        throw new UnsupportedOperationException("ArrayShortIterator does not support remove");
    }

    public void reset() {
        cursor = 0;
    }
}

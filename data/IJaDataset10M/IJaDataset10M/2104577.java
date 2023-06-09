package org.joda.primitives.listiterator.impl;

import java.util.NoSuchElementException;
import org.joda.primitives.ByteUtils;
import org.joda.primitives.listiterator.ByteListIterator;

/**
 * An iterator over an array of <code>byte</code> values.
 * <p>
 * This class implements {@link java.util.ListIterator ListIterator} allowing
 * seamless integration with other APIs.
 * <p>
 * The iterator can be reset to the start if required.
 * <code>add()</code> and <code>remove()</code> are unsupported, but
 * <code>set()</code> is supported.
 *
 * @author Stephen Colebourne
 * @author Jason Tiscione
 * @version CODE GENERATED
 * @since 1.0
 */
public class ArrayByteListIterator implements ByteListIterator {

    /** The array to iterate over */
    protected final byte[] array;

    /** Cursor position */
    protected int cursor = 0;

    /** Last returned position */
    protected int last = -1;

    /**
     * Creates an iterator over a copy of an array of <code>byte</code> values.
     * <p>
     * The specified array is copied, ensuring the original data is unaltered.
     * Note that the class is not immutable due to the {@code set} methods.
     * 
     * @param array  the array to iterate over, must not be null
     * @throws IllegalArgumentException if the array is null
     */
    public static ArrayByteListIterator copyOf(byte[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        return new ArrayByteListIterator(array.clone());
    }

    /**
     * Constructs an iterator over an array of <code>byte</code> values.
     * <p>
     * The array is assigned internally, thus the caller holds a reference to
     * the internal state of the returned iterator. It is not recommended to
     * modify the state of the array after construction.
     * 
     * @param array  the array to iterate over, must not be null
     * @throws IllegalArgumentException if the array is null
     */
    public ArrayByteListIterator(byte[] array) {
        super();
        if (array == null) {
            throw new IllegalArgumentException("Array must not be null");
        }
        this.array = array;
    }

    public boolean isModifiable() {
        return true;
    }

    public boolean isResettable() {
        return true;
    }

    public boolean hasNext() {
        return (cursor < array.length);
    }

    public int nextIndex() {
        return cursor;
    }

    public byte nextByte() {
        if (hasNext() == false) {
            throw new NoSuchElementException("No more elements available");
        }
        last = cursor;
        return array[cursor++];
    }

    public Byte next() {
        return ByteUtils.toObject(nextByte());
    }

    public boolean hasPrevious() {
        return (cursor > 0);
    }

    public int previousIndex() {
        return cursor - 1;
    }

    public byte previousByte() {
        if (hasPrevious() == false) {
            throw new NoSuchElementException("No more elements available");
        }
        last = --cursor;
        return array[cursor];
    }

    public Byte previous() {
        return ByteUtils.toObject(previousByte());
    }

    public void add(byte value) {
        throw new UnsupportedOperationException("ArrayByteListIterator does not support add");
    }

    public void add(Byte value) {
        throw new UnsupportedOperationException("ArrayByteListIterator does not support add");
    }

    public void remove() {
        throw new UnsupportedOperationException("ArrayByteListIterator does not support remove");
    }

    public void set(byte value) {
        if (last < 0) {
            throw new IllegalStateException("ArrayByteListIterator cannot be set until next is called");
        }
        array[last] = value;
    }

    public void set(Byte value) {
        set(ByteUtils.toPrimitive(value));
    }

    public void reset() {
        cursor = 0;
        last = -1;
    }
}

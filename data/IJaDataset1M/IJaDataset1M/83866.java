package org.armedbear.lisp;

import static org.armedbear.lisp.Nil.NIL;
import static org.armedbear.lisp.Lisp.*;

public final class ComplexBitVector extends AbstractBitVector {

    private int fillPointer = -1;

    private boolean isDisplaced;

    private LispArray array;

    private int displacement;

    public ComplexBitVector(int capacity) throws ConditionThrowable {
        this.capacity = capacity;
        int size = capacity >>> 6;
        if ((capacity & LONG_MASK) != 0) ++size;
        bits = new long[size];
    }

    public ComplexBitVector(int capacity, LispArray array, int displacement) {
        this.capacity = capacity;
        this.array = array;
        this.displacement = displacement;
        isDisplaced = true;
    }

    @Override
    public LispObject typeOf() {
        return list(SymbolConstants.BIT_VECTOR, Fixnum.makeFixnum(capacity));
    }

    @Override
    public boolean hasFillPointer() {
        return fillPointer >= 0;
    }

    @Override
    public int getFillPointer() {
        return fillPointer;
    }

    @Override
    public void setFillPointer(int n) {
        fillPointer = n;
    }

    @Override
    public void setFillPointer(LispObject obj) throws ConditionThrowable {
        if (obj == T) fillPointer = capacity(); else {
            int n = obj.intValue();
            if (n > capacity()) {
                StringBuffer sb = new StringBuffer("The new fill pointer (");
                sb.append(n);
                sb.append(") exceeds the capacity of the vector (");
                sb.append(capacity());
                sb.append(").");
                error(new LispError(sb.toString()));
            } else if (n < 0) {
                StringBuffer sb = new StringBuffer("The new fill pointer (");
                sb.append(n);
                sb.append(") is negative.");
                error(new LispError(sb.toString()));
            } else fillPointer = n;
        }
    }

    @Override
    public LispObject arrayDisplacement() throws ConditionThrowable {
        LispObject value1, value2;
        if (array != null) {
            value1 = array;
            value2 = Fixnum.makeFixnum(displacement);
        } else {
            value1 = NIL;
            value2 = Fixnum.ZERO;
        }
        return LispThread.currentThread().setValues(value1, value2);
    }

    @Override
    public int size() {
        return fillPointer >= 0 ? fillPointer : capacity;
    }

    @Override
    public LispObject elt(int index) throws ConditionThrowable {
        if (index >= size()) badIndex(index, size());
        return AREF(index);
    }

    @Override
    public LispObject AREF(int index) throws ConditionThrowable {
        if (index < 0 || index >= capacity) badIndex(index, capacity);
        if (bits != null) {
            int offset = index >> 6;
            return (bits[offset] & (1L << index)) != 0 ? Fixnum.ONE : Fixnum.ZERO;
        } else {
            return array.AREF(index + displacement);
        }
    }

    @Override
    protected int getBit(int index) throws ConditionThrowable {
        if (bits != null) {
            int offset = index >> 6;
            return (bits[offset] & (1L << index)) != 0 ? 1 : 0;
        } else return array.AREF(index + displacement).intValue();
    }

    @Override
    public void aset(int index, LispObject newValue) throws ConditionThrowable {
        if (index < 0 || index >= capacity) badIndex(index, capacity);
        if (newValue instanceof Fixnum) {
            switch(newValue.intValue()) {
                case 0:
                    if (bits != null) {
                        final int offset = index >> 6;
                        bits[offset] &= ~(1L << index);
                    } else clearBit(index);
                    return;
                case 1:
                    if (bits != null) {
                        final int offset = index >> 6;
                        bits[offset] |= 1L << index;
                    } else setBit(index);
                    return;
            }
        }
        type_error(newValue, SymbolConstants.BIT);
    }

    @Override
    protected void setBit(int index) throws ConditionThrowable {
        if (bits != null) {
            int offset = index >> 6;
            bits[offset] |= 1L << index;
        } else array.aset(index + displacement, Fixnum.ONE);
    }

    @Override
    protected void clearBit(int index) throws ConditionThrowable {
        if (bits != null) {
            int offset = index >> 6;
            bits[offset] &= ~(1L << index);
        } else array.aset(index + displacement, Fixnum.ZERO);
    }

    @Override
    public void shrink(int n) throws ConditionThrowable {
        if (bits != null) {
            if (n < capacity) {
                int size = n >>> 6;
                if ((n & LONG_MASK) != 0) ++size;
                if (size < bits.length) {
                    long[] newbits = new long[size];
                    System.arraycopy(bits, 0, newbits, 0, size);
                    bits = newbits;
                }
                capacity = n;
                return;
            }
            if (n == capacity) return;
        }
        error(new LispError());
    }

    @Override
    public boolean isSimpleVector() {
        return false;
    }

    @Override
    public void vectorPushExtend(LispObject element) throws ConditionThrowable {
        final int fp = getFillPointer();
        if (fp < 0) noFillPointer();
        if (fp >= capacity()) {
            ensureCapacity(capacity() * 2 + 1);
        }
        aset(fp, element);
        setFillPointer(fp + 1);
    }

    @Override
    public LispObject VECTOR_PUSH_EXTEND(LispObject element) throws ConditionThrowable {
        vectorPushExtend(element);
        return Fixnum.makeFixnum(getFillPointer() - 1);
    }

    @Override
    public LispObject VECTOR_PUSH_EXTEND(LispObject element, LispObject extension) throws ConditionThrowable {
        int ext = extension.intValue();
        final int fp = getFillPointer();
        if (fp < 0) noFillPointer();
        if (fp >= capacity()) {
            ext = Math.max(ext, capacity() + 1);
            ensureCapacity(capacity() + ext);
        }
        aset(fp, element);
        setFillPointer(fp + 1);
        return Fixnum.makeFixnum(fp);
    }

    private final void ensureCapacity(int minCapacity) throws ConditionThrowable {
        if (bits != null) {
            if (capacity < minCapacity) {
                int size = minCapacity >>> 6;
                if ((minCapacity & LONG_MASK) != 0) ++size;
                long[] newBits = new long[size];
                System.arraycopy(bits, 0, newBits, 0, bits.length);
                bits = newBits;
                capacity = minCapacity;
            }
        } else {
            Debug.assertTrue(array != null);
            if (capacity < minCapacity || array.getTotalSize() - displacement < minCapacity) {
                int size = minCapacity >>> 6;
                if ((minCapacity & LONG_MASK) != 0) ++size;
                bits = new long[size];
                final int limit = Math.min(capacity, array.getTotalSize() - displacement);
                for (int i = 0; i < limit; i++) {
                    int n = array.AREF(displacement + i).intValue();
                    if (n == 1) setBit(i); else clearBit(i);
                }
                capacity = minCapacity;
                array = null;
                displacement = 0;
                isDisplaced = false;
            }
        }
    }

    @Override
    public LispVector adjustArray(int newCapacity, LispObject initialElement, LispObject initialContents) throws ConditionThrowable {
        if (bits == null) {
            int size = capacity >>> 6;
            if ((capacity & LONG_MASK) != 0) ++size;
            bits = new long[size];
            for (int i = 0; i < capacity; i++) {
                int n = array.AREF(displacement + i).intValue();
                if (n == 1) setBit(i); else clearBit(i);
            }
            array = null;
            displacement = 0;
            isDisplaced = false;
        }
        if (capacity != newCapacity) {
            int size = newCapacity >>> 6;
            if ((newCapacity & LONG_MASK) != 0) ++size;
            if (initialContents != null) {
                bits = new long[size];
                capacity = newCapacity;
                if (initialContents.isList()) {
                    LispObject list = initialContents;
                    for (int i = 0; i < newCapacity; i++) {
                        aset(i, list.CAR());
                        list = list.CDR();
                    }
                } else if (initialContents.isVector()) {
                    for (int i = 0; i < newCapacity; i++) aset(i, initialContents.elt(i));
                } else type_error(initialContents, SymbolConstants.SEQUENCE);
            } else {
                long[] newBits = new long[size];
                System.arraycopy(bits, 0, newBits, 0, Math.min(bits.length, newBits.length));
                bits = newBits;
                if (newCapacity > capacity && initialElement != null) {
                    int n = initialElement.intValue();
                    if (n == 1) for (int i = capacity; i < newCapacity; i++) setBit(i); else for (int i = capacity; i < newCapacity; i++) clearBit(i);
                }
            }
            capacity = newCapacity;
        }
        return this;
    }

    @Override
    public LispVector adjustArray(int size, LispArray displacedTo, int displacement) throws ConditionThrowable {
        capacity = size;
        array = displacedTo;
        this.displacement = displacement;
        bits = null;
        isDisplaced = true;
        return this;
    }
}

package gnu.trove.impl.unmodifiable;

import gnu.trove.iterator.*;
import gnu.trove.procedure.*;
import gnu.trove.set.*;
import gnu.trove.list.*;
import gnu.trove.function.*;
import gnu.trove.map.*;
import gnu.trove.*;
import java.util.*;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class TUnmodifiableObjectFloatMap<K> implements TObjectFloatMap<K>, Serializable {

    private static final long serialVersionUID = -1034234728574286014L;

    private final TObjectFloatMap<K> m;

    public TUnmodifiableObjectFloatMap(TObjectFloatMap<K> m) {
        if (m == null) throw new NullPointerException();
        this.m = m;
    }

    public int size() {
        return m.size();
    }

    public boolean isEmpty() {
        return m.isEmpty();
    }

    public boolean containsKey(Object key) {
        return m.containsKey(key);
    }

    public boolean containsValue(float val) {
        return m.containsValue(val);
    }

    public float get(Object key) {
        return m.get(key);
    }

    public float put(K key, float value) {
        throw new UnsupportedOperationException();
    }

    public float remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(TObjectFloatMap m) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends Float> map) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    private transient Set<K> keySet = null;

    private transient TFloatCollection values = null;

    public Set<K> keySet() {
        if (keySet == null) keySet = Collections.unmodifiableSet(m.keySet());
        return keySet;
    }

    public Object[] keys() {
        return m.keys();
    }

    public K[] keys(K[] array) {
        return m.keys(array);
    }

    public TFloatCollection valueCollection() {
        if (values == null) values = TCollections.unmodifiableCollection(m.valueCollection());
        return values;
    }

    public float[] values() {
        return m.values();
    }

    public float[] values(float[] array) {
        return m.values(array);
    }

    public boolean equals(Object o) {
        return o == this || m.equals(o);
    }

    public int hashCode() {
        return m.hashCode();
    }

    public String toString() {
        return m.toString();
    }

    public float getNoEntryValue() {
        return m.getNoEntryValue();
    }

    public boolean forEachKey(TObjectProcedure<K> procedure) {
        return m.forEachKey(procedure);
    }

    public boolean forEachValue(TFloatProcedure procedure) {
        return m.forEachValue(procedure);
    }

    public boolean forEachEntry(TObjectFloatProcedure<K> procedure) {
        return m.forEachEntry(procedure);
    }

    public TObjectFloatIterator<K> iterator() {
        return new TObjectFloatIterator<K>() {

            TObjectFloatIterator<K> iter = m.iterator();

            public K key() {
                return iter.key();
            }

            public float value() {
                return iter.value();
            }

            public void advance() {
                iter.advance();
            }

            public boolean hasNext() {
                return iter.hasNext();
            }

            public float setValue(float val) {
                throw new UnsupportedOperationException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public float putIfAbsent(K key, float value) {
        throw new UnsupportedOperationException();
    }

    public void transformValues(TFloatFunction function) {
        throw new UnsupportedOperationException();
    }

    public boolean retainEntries(TObjectFloatProcedure procedure) {
        throw new UnsupportedOperationException();
    }

    public boolean increment(K key) {
        throw new UnsupportedOperationException();
    }

    public boolean adjustValue(K key, float amount) {
        throw new UnsupportedOperationException();
    }

    public float adjustOrPutValue(K key, float adjust_amount, float put_amount) {
        throw new UnsupportedOperationException();
    }
}

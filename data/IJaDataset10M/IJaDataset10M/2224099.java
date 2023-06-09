package com.google.gwt.dev.util.collect;

import org.apache.commons.collections.set.AbstractTestSet;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Test for {@link HashMap}.
 */
public class IdentityHashSetTest extends AbstractTestSet {

    private static final Float FLOAT_6 = 6.0f;

    private static final Double DOUBLE_5 = 5.0;

    public IdentityHashSetTest(String testName) {
        super(testName);
    }

    @Override
    public boolean areEqualElementsDistinguishable() {
        return true;
    }

    /**
   * Must use stable identities.
   */
    @Override
    public Object[] getFullNonNullElements() {
        return new Object[] { "", "One", 2, "Three", 4, "One", DOUBLE_5, FLOAT_6, "Seven", "Eight", "Nine", 10, (short) 11, 12L, "Thirteen", "14", "15", (byte) 16 };
    }

    /**
   * Must use stable identities.
   */
    @Override
    public Object[] getOtherNonNullElements() {
        return new Object[] { 0, 0f, 0.0, "Zero", (short) 0, (byte) 0, 0L, ' ', "0" };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection makeConfirmedCollection() {
        final java.util.IdentityHashMap map = new java.util.IdentityHashMap();
        return new AbstractSet() {

            @Override
            public boolean add(Object e) {
                return map.put(e, e) == null;
            }

            @Override
            public Iterator iterator() {
                return map.keySet().iterator();
            }

            @Override
            public int size() {
                return map.size();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set makeEmptySet() {
        return new IdentityHashSet();
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        return true;
    }

    /**
   * This can't possible work due to non-stable identities.
   */
    @Override
    public void testSerializeDeserializeThenCompare() throws Exception {
    }
}

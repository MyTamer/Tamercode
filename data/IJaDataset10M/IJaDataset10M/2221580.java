package org.qedeq.base.utility;

import org.qedeq.base.test.QedeqTestCase;

/**
 * Test {@link EqualsUtility}.
 *
 * @author  Michael Meyling
 */
public class EqualsUtilityTest extends QedeqTestCase {

    /**
     * Test {@link EqualsUtility#equals(Object, Object)}.
     *
     * @throws Exception
     */
    public void testEqualsObject() throws Exception {
        assertTrue(EqualsUtility.equals("A", "A"));
        final Object obj1 = new Object();
        final Object obj2 = new Object();
        assertFalse(EqualsUtility.equals(obj1, obj2));
        assertFalse(EqualsUtility.equals(obj2, obj1));
        assertTrue(EqualsUtility.equals(obj1, obj1));
        assertTrue(EqualsUtility.equals(obj2, obj2));
        assertFalse(EqualsUtility.equals(obj1, null));
        assertTrue(EqualsUtility.equals((Object) null, null));
        assertFalse(EqualsUtility.equals(null, obj1));
        assertFalse(EqualsUtility.equals(obj2, null));
        assertFalse(EqualsUtility.equals(null, obj2));
    }

    /**
     * Test {@link EqualsUtility#equals(byte[], byte[])}.
     *
     * @throws Exception
     */
    public void testEqualsByteArray() throws Exception {
        final byte[] obj1 = new byte[10];
        final byte[] obj2 = new byte[10];
        final byte[] obj3 = new byte[9];
        assertTrue(EqualsUtility.equals(obj1, obj2));
        assertTrue(EqualsUtility.equals(obj2, obj1));
        assertTrue(EqualsUtility.equals(obj1, obj1));
        assertTrue(EqualsUtility.equals(obj2, obj2));
        assertFalse(EqualsUtility.equals(obj1, null));
        assertTrue(EqualsUtility.equals((byte[]) null, null));
        assertFalse(EqualsUtility.equals(null, obj1));
        assertFalse(EqualsUtility.equals(obj2, null));
        assertFalse(EqualsUtility.equals(null, obj2));
        assertFalse(EqualsUtility.equals(obj1, obj3));
        assertFalse(EqualsUtility.equals(obj3, obj2));
        obj1[5] = 7;
        assertFalse(EqualsUtility.equals(obj1, obj2));
        assertFalse(EqualsUtility.equals(obj2, obj1));
    }

    /**
     * Test {@link EqualsUtility#equals(int[], int[])}.
     *
     * @throws Exception
     */
    public void testEqualsIntArray() throws Exception {
        final int[] obj1 = new int[10];
        final int[] obj2 = new int[10];
        final int[] obj3 = new int[9];
        assertTrue(EqualsUtility.equals(obj1, obj2));
        assertTrue(EqualsUtility.equals(obj2, obj1));
        assertTrue(EqualsUtility.equals(obj1, obj1));
        assertTrue(EqualsUtility.equals(obj2, obj2));
        assertFalse(EqualsUtility.equals(obj1, null));
        assertTrue(EqualsUtility.equals((int[]) null, null));
        assertFalse(EqualsUtility.equals(null, obj1));
        assertFalse(EqualsUtility.equals(obj2, null));
        assertFalse(EqualsUtility.equals(null, obj2));
        assertFalse(EqualsUtility.equals(obj1, obj3));
        assertFalse(EqualsUtility.equals(obj3, obj2));
        obj1[5] = 7;
        assertFalse(EqualsUtility.equals(obj1, obj2));
        assertFalse(EqualsUtility.equals(obj2, obj1));
    }

    /**
     * Test {@link EqualsUtility#equals(Object[], Object[])}.
     *
     * @throws Exception
     */
    public void testEqualsObjectArray() throws Exception {
        final Object[] obj1 = new Object[10];
        final Object[] obj2 = new Object[10];
        final Object[] obj3 = new Object[9];
        assertTrue(EqualsUtility.equals(obj1, obj2));
        assertTrue(EqualsUtility.equals(obj2, obj1));
        assertTrue(EqualsUtility.equals(obj1, obj1));
        assertTrue(EqualsUtility.equals(obj2, obj2));
        assertFalse(EqualsUtility.equals(obj1, null));
        assertTrue(EqualsUtility.equals((Object[]) null, null));
        assertFalse(EqualsUtility.equals(null, obj1));
        assertFalse(EqualsUtility.equals(obj2, null));
        assertFalse(EqualsUtility.equals(null, obj2));
        assertFalse(EqualsUtility.equals(obj1, obj3));
        assertFalse(EqualsUtility.equals(obj3, obj2));
        obj1[5] = new Integer(7);
        assertFalse(EqualsUtility.equals(obj1, obj2));
        assertFalse(EqualsUtility.equals(obj2, obj1));
    }
}

package cspfj.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public final class LargeBitVectorTest {

    private LargeBitVector bitVector;

    @Before
    public void setUp() {
        bitVector = new LargeBitVector(125);
    }

    @Test
    public void testInitBooleanArray() {
        bitVector.fill(true);
        assertTrue(bitVector.get(64));
        assertTrue(bitVector.get(65));
        assertTrue(bitVector.get(124));
        assertFalse(bitVector.get(125));
        bitVector.fill(false);
        assertEquals(-1, bitVector.nextSetBit(0));
    }

    @Test
    public void testBooleanArraySize() {
        assertEquals(LargeBitVector.nbWords(0), 0);
        assertEquals(LargeBitVector.nbWords(1), 1);
        assertEquals(LargeBitVector.nbWords(64), 1);
        assertEquals(LargeBitVector.nbWords(65), 2);
    }

    @Test
    public void testSet() {
        assertFalse(bitVector.set(100, false));
        assertTrue(bitVector.set(100, true));
        assertFalse(bitVector.set(100, true));
    }

    @Test
    public void testGet() {
        bitVector.set(46);
        assertFalse(bitVector.get(0));
        assertFalse(bitVector.get(45));
        assertTrue(bitVector.get(46));
    }

    @Test
    public void testNextSetBit() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        assertEquals(46, bitVector.nextSetBit(0));
        assertEquals(46, bitVector.nextSetBit(46));
        assertEquals(49, bitVector.nextSetBit(47));
        assertEquals(100, bitVector.nextSetBit(63));
        assertEquals(100, bitVector.nextSetBit(64));
        assertEquals(-1, bitVector.nextSetBit(101));
    }

    @Test
    public void testPrevClearBit() {
        bitVector.fill(true);
        bitVector.clear(46);
        bitVector.clear(49);
        bitVector.clear(100);
        assertEquals(46, bitVector.prevClearBit(47));
        assertEquals(-1, bitVector.prevClearBit(46));
        assertEquals(-1, bitVector.prevClearBit(45));
        assertEquals(100, bitVector.prevClearBit(110));
        assertEquals(49, bitVector.prevClearBit(64));
        assertEquals(49, bitVector.prevClearBit(63));
        bitVector.clear(64);
        assertEquals(64, bitVector.prevClearBit(65));
        assertEquals(49, bitVector.prevClearBit(64));
        bitVector.set(64);
        bitVector.clear(63);
        assertEquals(63, bitVector.prevClearBit(65));
        assertEquals(49, bitVector.prevClearBit(63));
    }

    @Test
    public void testPrevSetBit() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        assertEquals(46, bitVector.prevSetBit(47));
        assertEquals(-1, bitVector.prevSetBit(46));
        assertEquals(-1, bitVector.prevSetBit(45));
        assertEquals(100, bitVector.prevSetBit(110));
        assertEquals(49, bitVector.prevSetBit(64));
        assertEquals(49, bitVector.prevSetBit(63));
        bitVector.set(64);
        assertEquals(64, bitVector.prevSetBit(65));
        assertEquals(49, bitVector.prevSetBit(64));
        bitVector.clear(64);
        bitVector.set(63);
        assertEquals(63, bitVector.prevSetBit(65));
        assertEquals(49, bitVector.prevSetBit(63));
    }

    @Test
    public void testToStringIntArray() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        assertEquals("{46, 49, 100}", bitVector.toString());
    }

    @Test
    public void testWord() {
        assertEquals(0, LargeBitVector.word(0));
        assertEquals(0, LargeBitVector.word(63));
        assertEquals(1, LargeBitVector.word(64));
    }

    @Test
    public void testClearFrom() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        assertTrue(bitVector.clearFrom(47));
        assertEquals(1, bitVector.cardinality());
        assertTrue(bitVector.get(46));
        assertFalse(bitVector.get(49));
        assertFalse(bitVector.get(100));
    }

    @Test
    public void testClearTo() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        assertTrue(bitVector.clearTo(49));
        assertEquals(2, bitVector.cardinality());
        assertFalse(bitVector.get(46));
        assertTrue(bitVector.get(49));
        assertTrue(bitVector.get(100));
    }

    @Test
    public void testSetFrom() {
        assertTrue(bitVector.setFrom(80));
        assertEquals(bitVector.toString(), 45, bitVector.cardinality());
        for (int i = 0; i < 80; i++) {
            assertFalse(bitVector.get(i));
        }
        for (int i = 80; i < 125; i++) {
            assertTrue(Integer.toString(i), bitVector.get(i));
        }
        for (int i = 125; i < 200; i++) {
            assertFalse(Integer.toString(i), bitVector.get(i));
        }
        final BitVector bv = BitVector.newBitVector(2000);
        bv.fill(true);
        assertFalse(bitVector.setFrom(100));
    }

    @Test
    public void testSubset() {
        bitVector.set(46);
        bitVector.set(49);
        bitVector.set(100);
        BitVector bv2 = BitVector.newBitVector(125);
        bv2.set(46);
        assertFalse(bitVector.subsetOf(bv2));
        assertTrue(bv2.subsetOf(bitVector));
    }

    @Test
    public void testSubset2() {
        BitVector bv1 = BitVector.newBitVector(70);
        BitVector bv2 = BitVector.newBitVector(70);
        for (int i = 0; i < 70; i++) {
            bv1.set(i);
            bv2.set(i);
        }
        assertTrue(bv1.subsetOf(bv2));
        assertTrue(bv2.subsetOf(bv1));
        assertEquals(bv1, bv2);
    }

    @Test
    public void testHashcode() {
        bitVector.set(46);
        BitVector bv2 = BitVector.newBitVector(400);
        bv2.set(46);
        BitVector bv3 = BitVector.newBitVector(50);
        bv3.set(46);
        assertEquals(bitVector, bv2);
        assertEquals(bitVector, bv3);
        assertEquals(bitVector.hashCode(), bv2.hashCode());
        assertEquals(bitVector.hashCode(), bv3.hashCode());
    }

    @Test
    public void testXor() {
        bitVector.set(59);
        bitVector.set(11);
        BitVector bv2 = BitVector.newBitVector(20);
        bv2.set(10);
        bv2.set(11);
        BitVector bv3 = bitVector.xor(bv2);
        BitVector bv4 = bv2.xor(bitVector);
        assertEquals(bv3, bv4);
        assertEquals(2, bv3.cardinality());
        assertTrue(bv3.get(10));
        assertFalse(bv3.get(11));
        assertTrue(bv3.get(59));
    }
}

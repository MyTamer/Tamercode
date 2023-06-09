package com.limegroup.gnutella.messages;

import junit.framework.Test;
import org.limewire.collection.IntervalSet;
import org.limewire.collection.Range;
import org.limewire.util.Base32;
import org.limewire.util.ByteOrder;
import com.limegroup.gnutella.settings.SharingSettings;
import com.limegroup.gnutella.util.LimeTestCase;

public class IntervalEncoderTest extends LimeTestCase {

    public IntervalEncoderTest(String name) {
        super(name);
    }

    public static Test suite() {
        return buildTestSuite(IntervalEncoderTest.class);
    }

    /** 
     * tests that empty interval sets are not the same as non-existent ones
     */
    public void testEmptyAndNone() throws Exception {
        GGEP g = new GGEP();
        assertNull(IntervalEncoder.decode(1024 * 1024L, g));
        IntervalSet s = new IntervalSet();
        IntervalEncoder.encode(1024 * 1024L, g, s);
        assertNotNull(IntervalEncoder.decode(1024 * 1024L, g));
    }

    public void testEncode() throws Exception {
        SharingSettings.MAX_PARTIAL_ENCODING_SIZE.setValue(40);
        GGEP g = new GGEP();
        IntervalSet s = new IntervalSet();
        s.add(Range.createRange(0, 1024 * 1024L - 1));
        s.add(Range.createRange(3 * 1024L * 1024L, 5 * 1024L * 1024L * 1024L - 1));
        s.add(Range.createRange(7 * 1024L * 1024L * 1024L, 7 * 1024L * 1024L * 1024L + 1023));
        IntervalEncoder.encode(8 * 1024L * 1024L * 1024L, g, s);
        assertNull(g.get("PR4"));
        byte[] l3 = g.get("PR3");
        assertEquals(3, l3.length);
        byte[] tmp = new byte[4];
        System.arraycopy(l3, 0, tmp, 1, 3);
        int carried = ByteOrder.beb2int(tmp, 0);
        assertEquals(15728640, carried);
        byte[] l2 = g.get("PR2");
        assertEquals(12, l2.length);
        assertEquals(257, ByteOrder.beb2short(l2, 0));
        assertEquals(513, ByteOrder.beb2short(l2, 2));
        assertEquals(1025, ByteOrder.beb2short(l2, 4));
        assertEquals(2049, ByteOrder.beb2short(l2, 6));
        assertEquals(8192, ByteOrder.beb2short(l2, 8));
        assertEquals(8195, ByteOrder.beb2short(l2, 10));
        byte[] l1 = g.get("PR1");
        assertEquals(7, l1.length);
        assertEquals(5, l1[0]);
        assertEquals(9, l1[1]);
        assertEquals(12, l1[2]);
        assertEquals(17, l1[3]);
        assertEquals(33, l1[4]);
        assertEquals(65, l1[5]);
        assertEquals(129, l1[6] & 0xFF);
    }

    public void testLimitedEncode() throws Exception {
        SharingSettings.MAX_PARTIAL_ENCODING_SIZE.setValue(20);
        GGEP g = new GGEP();
        IntervalSet s = new IntervalSet();
        s.add(Range.createRange(0, 1024 * 1024L - 1));
        s.add(Range.createRange(3 * 1024L * 1024L, 5 * 1024L * 1024L * 1024L - 1));
        s.add(Range.createRange(7 * 1024L * 1024L * 1024L, 7 * 1024L * 1024L * 1024L + 1023));
        IntervalEncoder.encode(8 * 1024L * 1024L * 1024L, g, s);
        assertNull(g.get("PR4"));
        assertNull(g.get("PR3"));
        byte[] l2 = g.get("PR2");
        assertEquals(12, l2.length);
        assertEquals(257, ByteOrder.beb2short(l2, 0));
        assertEquals(513, ByteOrder.beb2short(l2, 2));
        assertEquals(1025, ByteOrder.beb2short(l2, 4));
        assertEquals(2049, ByteOrder.beb2short(l2, 6));
        assertEquals(8192, ByteOrder.beb2short(l2, 8));
        assertEquals(8195, ByteOrder.beb2short(l2, 10));
        byte[] l1 = g.get("PR1");
        assertEquals(7, l1.length);
        assertEquals(5, l1[0]);
        assertEquals(9, l1[1]);
        assertEquals(12, l1[2]);
        assertEquals(17, l1[3]);
        assertEquals(33, l1[4]);
        assertEquals(65, l1[5]);
        assertEquals(129, l1[6] & 0xFF);
        SharingSettings.MAX_PARTIAL_ENCODING_SIZE.setValue(10);
        g = new GGEP();
        s = new IntervalSet();
        s.add(Range.createRange(0, 1024 * 1024L - 1));
        s.add(Range.createRange(3 * 1024L * 1024L, 5 * 1024L * 1024L * 1024L - 1));
        s.add(Range.createRange(7 * 1024L * 1024L * 1024L, 7 * 1024L * 1024L * 1024L + 1023));
        IntervalEncoder.encode(8 * 1024L * 1024L * 1024L, g, s);
        assertNull(g.get("PR4"));
        assertNull(g.get("PR3"));
        l2 = g.get("PR2");
        assertEquals(2, l2.length);
        assertEquals(257, ByteOrder.beb2short(l2, 0));
        l1 = g.get("PR1");
        assertEquals(7, l1.length);
        assertEquals(5, l1[0]);
        assertEquals(9, l1[1]);
        assertEquals(12, l1[2]);
        assertEquals(17, l1[3]);
        assertEquals(33, l1[4]);
        assertEquals(65, l1[5]);
        assertEquals(129, l1[6] & 0xFF);
    }

    public void testDecode() throws Exception {
        byte[] tmp = new byte[4];
        ByteOrder.int2beb(15728640, tmp, 0);
        byte[] l3 = new byte[3];
        System.arraycopy(tmp, 1, l3, 0, 3);
        byte[] l2 = new byte[12];
        ByteOrder.short2beb((short) 257, l2, 0);
        ByteOrder.short2beb((short) 513, l2, 2);
        ByteOrder.short2beb((short) 1025, l2, 4);
        ByteOrder.short2beb((short) 2049, l2, 6);
        ByteOrder.short2beb((short) 8192, l2, 8);
        ByteOrder.short2beb((short) 8195, l2, 10);
        byte[] l1 = new byte[7];
        l1[0] = 5;
        l1[1] = 9;
        l1[2] = 12;
        l1[3] = 17;
        l1[4] = 33;
        l1[5] = 65;
        l1[6] = (byte) (129);
        GGEP g = new GGEP();
        g.put("PR1", l1);
        g.put("PR2", l2);
        g.put("PR3", l3);
        IntervalSet s = IntervalEncoder.decode(8 * 1024L * 1024L * 1024L, g);
        assertEquals(3, s.getNumberOfIntervals());
        assertTrue(s.contains(Range.createRange(0, 1024 * 1024L - 1)));
        assertTrue(s.contains(Range.createRange(3 * 1024L * 1024L, 5 * 1024L * 1024L * 1024L - 1)));
        assertTrue(s.contains(Range.createRange(7 * 1024L * 1024L * 1024L, 7 * 1024L * 1024L * 1024L + 1023)));
    }

    public void testBigFile() throws Exception {
        GGEP g = new GGEP();
        IntervalSet s = new IntervalSet();
        final long halfTB = 1024L * 1024 * 1024 * 512;
        Range r = Range.createRange(halfTB - 1024, halfTB - 1);
        s.add(r);
        IntervalEncoder.encode(halfTB * 2, g, s);
        assertNull(g.get("PR0"));
        assertNull(g.get("PR1"));
        assertNull(g.get("PR2"));
        assertNull(g.get("PR3"));
        assertNotNull(g.get("PR4"));
        byte[] b = g.get("PR4");
        assertEquals(4, b.length);
        assertEquals((Integer.MAX_VALUE / 2 + Integer.MAX_VALUE / 4 + 1), ByteOrder.beb2int(b, 0));
        IntervalSet decoded = IntervalEncoder.decode(halfTB * 2, g);
        assertEquals(1, decoded.getNumberOfIntervals());
        assertEquals(r, decoded.getFirst());
    }

    public void testPR3Fix() throws Exception {
        GGEP g = new GGEP();
        final long size = 733765632;
        IntervalSet s = new IntervalSet();
        s.add(Range.createRange(500000, 505000));
        s.add(Range.createRange(600000, 605000));
        IntervalEncoder.encode(size, g, s);
        assertNotNull(g.get("PR3"));
        byte[] b = g.get("PR3");
        assertGreaterThan(3, b.length);
        IntervalSet decoded = IntervalEncoder.decode(size, g);
        assertEquals(2, decoded.getNumberOfIntervals());
        assertGreaterThanOrEquals(500000, decoded.getFirst().getLow());
        assertGreaterThanOrEquals(600000, decoded.getLast().getLow());
        assertLessThanOrEquals(505000, decoded.getFirst().getHigh());
        assertLessThanOrEquals(605000, decoded.getLast().getHigh());
    }

    public void testInvalid() throws Exception {
        GGEP g = new GGEP();
        byte[] pr1 = new byte[] { 2, 100 };
        g.put("PR1", pr1);
        IntervalSet decoded = IntervalEncoder.decode(2048, g);
        assertEquals(1, decoded.getNumberOfIntervals());
        assertEquals(0, decoded.getFirst().getLow());
        assertEquals(1023, decoded.getFirst().getHigh());
        String encoded = "AEBAIBAQCAA4IAAAAAAA";
        byte[] pr3 = Base32.decode(encoded);
        g = new GGEP();
        g.put("PR3", pr3);
        IntervalEncoder.decode(734147846L, g);
    }
}

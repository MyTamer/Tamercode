package org.jgap.util;

import org.jgap.*;
import junit.framework.*;

/**
 * Tests the NumberKit class.
 *
 * @author Klaus Meffert
 * @since 3.3.3
 */
public class NumberKitTest extends JGAPTestCase {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.1 $";

    public static Test suite() {
        return new TestSuite(NumberKitTest.class);
    }

    public NumberKitTest() {
    }

    public void testNiceDecimalNumber_0() throws Exception {
        double d = 12d;
        String s = NumberKit.niceDecimalNumber(d, 2);
        assertEquals("12.0", s);
    }

    public void testNiceDecimalNumber_1() throws Exception {
        double d = 12.123d;
        String s = NumberKit.niceDecimalNumber(d, 2);
        assertEquals("12.12", s);
        d = 12.127d;
        s = NumberKit.niceDecimalNumber(d, 2);
        assertEquals("12.12", s);
    }

    public void testNiceDecimalNumber_2() throws Exception {
        double d = 0d;
        String s = NumberKit.niceDecimalNumber(d, 2);
        assertEquals("0.0", s);
    }

    public void testNiceDecimalNumber_3() throws Exception {
        double d = 127.98765d;
        String s = NumberKit.niceDecimalNumber(d, 2);
        assertEquals("127.98", s);
        s = NumberKit.niceDecimalNumber(d, 3);
        assertEquals("127.987", s);
        s = NumberKit.niceDecimalNumber(d, 0);
        assertEquals("127", s);
        s = NumberKit.niceDecimalNumber(d, 1);
        assertEquals("127.9", s);
        s = NumberKit.niceDecimalNumber(d, 6);
        assertEquals("127.98765", s);
    }
}

package jmri.jmrit.logix;

import jmri.Block;
import junit.framework.*;

/**
 * Tests for the OPath class
 *
 * @author	    Bob Jacobsen  Copyright 2010
 * @version         $Revision: 17977 $
 */
public class OPathTest extends TestCase {

    public void testCtor() {
        Block b = new Block("IB1");
        OPath op = new OPath(b, "name");
        Assert.assertEquals("name", "name", op.getName());
        Assert.assertEquals("block", b, op.getBlock());
    }

    public void testNullBlockCtor() {
        OPath op = new OPath(null, "name");
        Assert.assertEquals("name", "name", op.getName());
        Assert.assertEquals("block", null, op.getBlock());
    }

    public void testSetBlockNonNull() {
        Block b1 = new Block("IB1");
        Block b2 = new Block("IB2");
        OPath op = new OPath(b1, "name");
        op.setBlock(b2);
        Assert.assertEquals("block", b2, op.getBlock());
    }

    public void testSetBlockWasNull() {
        Block b = new Block("IB1");
        OPath op = new OPath(null, "name");
        op.setBlock(b);
        Assert.assertEquals("block", b, op.getBlock());
    }

    public void testSetBlockToNull() {
        Block b1 = new Block("IB1");
        OPath op = new OPath(b1, "name");
        op.setBlock(null);
        Assert.assertEquals("block", null, op.getBlock());
    }

    public OPathTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", OPathTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        return new TestSuite(OPathTest.class);
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }
}

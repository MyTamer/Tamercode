package jmri.jmrix.lenz.liusbserver;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * LIUSBServerAdapterTest.java
 *
 * Description:	    tests for the jmri.jmrix.lenz.liusbserver.LIUSBServerAdapter class
 * @author			Paul Bender
 * @version         $Revision: 17977 $
 */
public class LIUSBServerAdapterTest extends TestCase {

    public void testCtor() {
        LIUSBServerAdapter a = new LIUSBServerAdapter();
        Assert.assertNotNull(a);
    }

    public LIUSBServerAdapterTest(String s) {
        super(s);
    }

    public static void main(String[] args) {
        String[] testCaseName = { "-noloading", LIUSBServerAdapterTest.class.getName() };
        junit.swingui.TestRunner.main(testCaseName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(LIUSBServerAdapterTest.class);
        return suite;
    }

    protected void setUp() {
        apps.tests.Log4JFixture.setUp();
    }

    protected void tearDown() {
        apps.tests.Log4JFixture.tearDown();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LIUSBServerAdapterTest.class.getName());
}

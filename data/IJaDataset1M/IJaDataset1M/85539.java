package tests.api.java.util;

import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.Vector;

@TestTargetClass(PropertyResourceBundle.class)
public class PropertyResourceBundleTest extends junit.framework.TestCase {

    static PropertyResourceBundle prb;

    /**
     * @tests java.util.PropertyResourceBundle#PropertyResourceBundle(java.io.InputStream)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "PropertyResourceBundle", args = { java.io.InputStream.class })
    public void test_ConstructorLjava_io_InputStream() {
        assertTrue("Used to test", true);
    }

    /**
     * @tests java.util.PropertyResourceBundle#getKeys()
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getKeys", args = {  })
    public void test_getKeys() {
        Enumeration keyEnum = prb.getKeys();
        Vector test = new Vector();
        int keyCount = 0;
        while (keyEnum.hasMoreElements()) {
            test.addElement(keyEnum.nextElement());
            keyCount++;
        }
        assertEquals("Returned the wrong number of keys", 2, keyCount);
        assertTrue("Returned the wrong keys", test.contains("p1") && test.contains("p2"));
    }

    /**
     * @tests java.util.PropertyResourceBundle#handleGetObject(java.lang.String)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "handleGetObject", args = { java.lang.String.class })
    public void test_handleGetObjectLjava_lang_String() {
        try {
            assertTrue("Returned incorrect objects", prb.getObject("p1").equals("one") && prb.getObject("p2").equals("two"));
        } catch (MissingResourceException e) {
            fail("Threw MisingResourceException for a key contained in the bundle");
        }
        try {
            prb.getObject("Not in the bundle");
        } catch (MissingResourceException e) {
            return;
        }
        fail("Failed to throw MissingResourceException for object not in the bundle");
    }

    /**
     * Sets up the fixture, for example, open a network connection. This method
     * is called before a test is executed.
     */
    protected void setUp() {
        java.io.InputStream propertiesStream = new java.io.ByteArrayInputStream("p1=one\np2=two".getBytes());
        try {
            prb = new PropertyResourceBundle(propertiesStream);
        } catch (java.io.IOException e) {
            fail("Contruction of PropertyResourceBundle threw IOException");
        }
    }

    /**
     * Tears down the fixture, for example, close a network connection. This
     * method is called after a test is executed.
     */
    protected void tearDown() {
    }
}

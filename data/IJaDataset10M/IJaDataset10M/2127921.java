package com.google.code.appengine.awt;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import com.google.code.appengine.awt.Frame;
import com.google.code.appengine.awt.Frame.AccessibleAWTFrame;
import junit.framework.TestCase;

/**
 * AccessibleAWTFrameTest
 */
public class AccessibleAWTFrameTest extends TestCase {

    AccessibleContext ac;

    private Frame frame;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        frame = new Frame();
        ac = frame.getAccessibleContext();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if ((frame != null) && frame.isDisplayable()) {
            frame.dispose();
        }
    }

    public final void testGetAccessibleRole() {
        assertSame(AccessibleRole.FRAME, ac.getAccessibleRole());
    }

    public final void testGetAccessibleStateSet() {
        AccessibleStateSet aStateSet = ac.getAccessibleStateSet();
        assertFalse("accessible frame is active", aStateSet.contains(AccessibleState.ACTIVE));
        assertTrue("accessible frame is resizable", aStateSet.contains(AccessibleState.RESIZABLE));
        frame.setResizable(false);
        aStateSet = ac.getAccessibleStateSet();
        assertFalse("accessible frame is NOT resizable", aStateSet.contains(AccessibleState.RESIZABLE));
    }

    public final void testAccessibleAWTFrame() {
        assertTrue(ac instanceof AccessibleAWTFrame);
    }
}

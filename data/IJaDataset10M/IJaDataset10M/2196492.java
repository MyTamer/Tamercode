package net.sf.echopm.controller;

import java.util.HashSet;
import java.util.Set;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import net.sf.echopm.TestCase;
import net.sf.echopm.TestActionListener;
import net.sf.echopm.TestController;
import net.sf.echopm.navigation.event.NavigationEvent;
import net.sf.echopm.navigation.event.OpenPanelEvent;

/**
 * @author ron
 */
public class AbstractControllerTest extends TestCase {

    /**
	 * Test method for
	 * {@link net.sf.echopm.controller.AbstractController#hashCode()}.
	 */
    public void testHashCode() {
        assertEquals(TestController.class.hashCode(), new TestController().hashCode());
    }

    /**
	 * Test method for:<br>
	 * {@link net.sf.echopm.controller.AbstractController#getListeners()}
	 * {@link net.sf.echopm.controller.AbstractController#addActionListener(nextapp.echo2.app.event.ActionListener)}
	 * {@link net.sf.echopm.controller.AbstractController#removeActionListener(nextapp.echo2.app.event.ActionListener)}
	 */
    public void testAddRemoveGetListeners() {
        Set<ActionListener> expectedActionListeners = new HashSet<ActionListener>();
        AbstractController testController = new TestController();
        assertEquals(0, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
        TestActionListener actionListener = new TestActionListener("actionListener");
        expectedActionListeners.add(actionListener);
        testController.addActionListener(actionListener);
        assertEquals(1, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
        expectedActionListeners.add(actionListener);
        testController.addActionListener(actionListener);
        assertEquals(1, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
        TestActionListener actionListener2 = new TestActionListener("actionListener2");
        expectedActionListeners.add(actionListener2);
        testController.addActionListener(actionListener2);
        assertEquals(2, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
        expectedActionListeners.remove(actionListener);
        testController.removeActionListener(actionListener);
        assertEquals(1, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
        expectedActionListeners.remove(actionListener2);
        testController.removeActionListener(actionListener2);
        assertEquals(0, testController.getListeners().size());
        assertEquals(expectedActionListeners, testController.getListeners());
    }

    /**
	 * Test method for:<br>
	 * {@link net.sf.echopm.controller.AbstractController#addEventTypeToListenFor(java.lang.Class)}
	 * {@link net.sf.echopm.controller.AbstractController#removeEventTypeToListenFor(java.lang.Class)}
	 * {@link net.sf.echopm.controller.AbstractController#equals(java.lang.Object)}
	 */
    public void testAddRemoveGetEventTypeToListenFor() {
        Set<Class<? extends ActionEvent>> expectedEventTypesToListenFor = new HashSet<Class<? extends ActionEvent>>();
        AbstractController testController = new TestController();
        assertEquals(0, testController.getListeners().size());
        assertEquals(expectedEventTypesToListenFor, testController.getListeners());
        Class<? extends ActionEvent> navigationEventType = NavigationEvent.class;
        expectedEventTypesToListenFor.add(navigationEventType);
        testController.addEventTypeToListenFor(navigationEventType);
        assertEquals(1, testController.getEventTypesToListenFor().size());
        assertEquals(expectedEventTypesToListenFor, testController.getEventTypesToListenFor());
        expectedEventTypesToListenFor.add(navigationEventType);
        testController.addEventTypeToListenFor(navigationEventType);
        assertEquals(1, testController.getEventTypesToListenFor().size());
        assertEquals(expectedEventTypesToListenFor, testController.getEventTypesToListenFor());
        Class<? extends ActionEvent> openEventType = OpenPanelEvent.class;
        expectedEventTypesToListenFor.add(openEventType);
        testController.addEventTypeToListenFor(openEventType);
        assertEquals(2, testController.getEventTypesToListenFor().size());
        assertEquals(expectedEventTypesToListenFor, testController.getEventTypesToListenFor());
        expectedEventTypesToListenFor.remove(navigationEventType);
        testController.removeEventTypeToListenFor(navigationEventType);
        assertEquals(1, testController.getEventTypesToListenFor().size());
        assertEquals(expectedEventTypesToListenFor, testController.getEventTypesToListenFor());
        expectedEventTypesToListenFor.remove(openEventType);
        testController.removeEventTypeToListenFor(openEventType);
        assertEquals(0, testController.getEventTypesToListenFor().size());
        assertEquals(expectedEventTypesToListenFor, testController.getEventTypesToListenFor());
    }

    /**
	 * Test method for equals() and hashCode().
	 */
    public void testEqualsHashCodeObject() {
        AbstractController testController = new TestController();
        assertTrue(testController.equals(testController));
        assertEquals(testController.hashCode(), testController.hashCode());
        AbstractController testController2 = new TestController();
        assertTrue(testController.equals(testController2));
        assertTrue(testController2.equals(testController));
        assertEquals(testController.hashCode(), testController2.hashCode());
        AbstractController testController3 = new TestController() {

            static final long serialVersionUID = 0;
        };
        assertFalse(testController.equals(testController3));
        assertFalse(testController3.equals(testController));
        assertNotSame(testController.hashCode(), testController3.hashCode());
    }

    /**
	 * Test method for
	 * {@link net.sf.echopm.controller.AbstractController#toString()}.
	 */
    public void testToString() {
        AbstractController testController = new TestController();
        assertEquals(testController.getClass().getSimpleName(), testController.toString());
    }

    /**
	 * Test method for
	 * {@link net.sf.echopm.controller.AbstractController#fireEvent(nextapp.echo2.app.event.ActionEvent)}.
	 */
    public void testFireEvent() {
        AbstractController testController = new TestController();
        try {
            testController.fireEvent(null);
        } catch (Exception e) {
            fail("exception firing a null event: " + e);
        }
        ActionEvent testEvent1 = new ActionEvent(this, "test event1");
        ActionEvent testEvent2 = new ActionEvent(this, "test event2");
        TestActionListener listener1 = new TestActionListener("listener1");
        TestActionListener listener2 = new TestActionListener("listener2");
        assertNull(listener1.getEventReceived());
        assertNull(listener2.getEventReceived());
        testController.addActionListener(listener1);
        testController.addActionListener(listener2);
        testController.fireEvent(testEvent1);
        assertEquals(testEvent1, listener1.getEventReceived());
        assertEquals(testEvent1, listener2.getEventReceived());
        testController.removeActionListener(listener1);
        testController.fireEvent(testEvent2);
        assertEquals(testEvent1, listener1.getEventReceived());
        assertEquals(testEvent2, listener2.getEventReceived());
    }
}

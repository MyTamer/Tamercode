package com.google.gwt.user.client.ui;

import com.google.gwt.junit.client.GWTTestCase;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tests {@link WidgetIterators}.
 */
public class WidgetIteratorsTest extends GWTTestCase {

    /**
   * Provides a class from which to construct mock containers in this test.
   */
    private static class MockWidget implements HasWidgets {

        public void add(Widget w) {
            fail("Unexpected call to add(Widget)");
        }

        public void clear() {
            fail("Unexpected call to clear()");
        }

        public Iterator iterator() {
            fail("Unexpected call to iterator()");
            return null;
        }

        public boolean remove(Widget w) {
            fail("Unexpected call to remove(Widget)");
            return false;
        }
    }

    public String getModuleName() {
        return "com.google.gwt.user.User";
    }

    /**
   * Tests that empty arrays operate properly.
   */
    public void testEmptyArray() {
        final Iterator subject = WidgetIterators.createWidgetIterator(new MockWidget(), new Widget[] { null, null });
        assertFalse(subject.hasNext());
        assertNextFails(subject);
        assertRemoveFails(subject);
    }

    /**
   * Tests that remove operates properly on an array of widgets that includes
   * <code>null</code>.
   */
    public void testRemove() {
        final int[] expectedRemoveIndex = new int[1];
        final Widget[] widgets = new Widget[] { null, createTestWidget(), null, createTestWidget(), null, null };
        final MockWidget mock = new MockWidget() {

            public boolean remove(Widget w) {
                assertEquals(widgets[expectedRemoveIndex[0]], w);
                return true;
            }
        };
        final Iterator subject = WidgetIterators.createWidgetIterator(mock, widgets);
        expectedRemoveIndex[0] = 1;
        assertTrue(subject.hasNext());
        assertEquals(widgets[1], subject.next());
        subject.remove();
        assertRemoveFails(subject);
        expectedRemoveIndex[0] = 3;
        assertTrue(subject.hasNext());
        assertEquals(widgets[3], subject.next());
        subject.remove();
        assertRemoveFails(subject);
        assertFalse(subject.hasNext());
        assertNextFails(subject);
    }

    /**
   * Tests that the common iteration pattern works on an array of widgets that
   * contains <code>null</code>.
   */
    public void testStandardIteration() {
        final MockWidget mock = new MockWidget();
        final Widget[] widgets = new Widget[] { null, createTestWidget(), null, createTestWidget(), null, null };
        final Iterator subject = WidgetIterators.createWidgetIterator(mock, widgets);
        assertTrue(subject.hasNext());
        assertEquals(widgets[1], subject.next());
        assertTrue(subject.hasNext());
        assertEquals(widgets[3], subject.next());
        assertFalse(subject.hasNext());
        assertNextFails(subject);
    }

    private void assertNextFails(Iterator iterator) {
        try {
            iterator.next();
            fail("Expected NoSuchElementException.");
        } catch (NoSuchElementException e) {
        }
    }

    private void assertRemoveFails(Iterator iterator) {
        try {
            iterator.remove();
            fail("Expected IllegalStateException.");
        } catch (IllegalStateException e) {
        }
    }

    private Widget createTestWidget() {
        return new Label("Shazam!");
    }
}

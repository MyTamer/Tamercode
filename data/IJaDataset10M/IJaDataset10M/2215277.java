package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.user.client.Timer;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Tests for {@link HeaderPanel}.
 */
public class HeaderPanelTest extends PanelTestBase<HeaderPanel> {

    public void testAdd() {
        HeaderPanel panel = createPanel();
        Label header = new Label("header");
        panel.add(header);
        assertEquals(header, panel.getHeaderWidget());
        assertEquals(null, panel.getContentWidget());
        assertEquals(null, panel.getFooterWidget());
        Label content = new Label("content");
        panel.add(content);
        assertEquals(header, panel.getHeaderWidget());
        assertEquals(content, panel.getContentWidget());
        assertEquals(null, panel.getFooterWidget());
        Label footer = new Label("footer");
        panel.add(footer);
        assertEquals(header, panel.getHeaderWidget());
        assertEquals(content, panel.getContentWidget());
        assertEquals(footer, panel.getFooterWidget());
        try {
            panel.add(new Label());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    public void testIterator() {
        final HeaderPanel panel = createPanel();
        Iterator<Widget> iter = panel.iterator();
        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
        try {
            iter.remove();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
        Label content = new Label("content");
        panel.setContentWidget(content);
        iter = panel.iterator();
        assertTrue(iter.hasNext());
        assertEquals(content, iter.next());
        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
        Label header = new Label("header");
        Label footer = new Label("footer");
        panel.setHeaderWidget(header);
        panel.setFooterWidget(footer);
        iter = panel.iterator();
        assertTrue(iter.hasNext());
        assertEquals(header, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(content, iter.next());
        assertTrue(iter.hasNext());
        assertEquals(footer, iter.next());
        assertFalse(iter.hasNext());
        try {
            iter.next();
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
        }
        iter = panel.iterator();
        assertEquals(header, iter.next());
        assertEquals(content, iter.next());
        iter.remove();
        assertEquals(header, panel.getHeaderWidget());
        assertNull(panel.getContentWidget());
        assertEquals(footer, panel.getFooterWidget());
        try {
            iter.remove();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
        }
        assertEquals(footer, iter.next());
    }

    @DoNotRunWith(Platform.HtmlUnitLayout)
    public void testResizeFooter() {
        final HeaderPanel panel = createPanel();
        panel.setSize("200px", "400px");
        RootPanel.get().add(panel);
        final Label header = new Label();
        header.setSize("100%", "50px");
        panel.setHeaderWidget(header);
        final Label content = new Label();
        content.setHeight("100%");
        panel.setContentWidget(content);
        final Label footer = new Label();
        footer.setSize("100%", "50px");
        panel.setFooterWidget(footer);
        delayTestFinish(5000);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                assertEquals(300, content.getOffsetHeight());
                footer.setHeight("75px");
                new Timer() {

                    @Override
                    public void run() {
                        assertEquals(275, content.getOffsetHeight());
                        RootPanel.get().remove(panel);
                        finishTest();
                    }
                }.schedule(250);
                ;
            }
        });
    }

    @DoNotRunWith(Platform.HtmlUnitLayout)
    public void testResizeHeader() {
        final HeaderPanel panel = createPanel();
        panel.setSize("200px", "400px");
        RootPanel.get().add(panel);
        final Label header = new Label();
        header.setSize("100%", "50px");
        panel.setHeaderWidget(header);
        final Label content = new Label();
        content.setHeight("100%");
        panel.setContentWidget(content);
        final Label footer = new Label();
        footer.setSize("100%", "50px");
        panel.setFooterWidget(footer);
        delayTestFinish(5000);
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            public void execute() {
                assertEquals(300, content.getOffsetHeight());
                header.setHeight("75px");
                new Timer() {

                    @Override
                    public void run() {
                        assertEquals(275, content.getOffsetHeight());
                        RootPanel.get().remove(panel);
                        finishTest();
                    }
                }.schedule(250);
                ;
            }
        });
    }

    public void testSetContentWidget() {
        HeaderPanel panel = createPanel();
        Label widget = new Label("hello world");
        panel.setContentWidget(widget);
        assertEquals(widget, panel.getContentWidget());
        panel.remove(widget);
        assertNull(panel.getContentWidget());
    }

    public void testSetFooterWidget() {
        HeaderPanel panel = createPanel();
        Label widget = new Label("hello world");
        panel.setHeaderWidget(widget);
        assertEquals(widget, panel.getHeaderWidget());
        panel.remove(widget);
        assertNull(panel.getHeaderWidget());
    }

    public void testSetHeaderWidget() {
        HeaderPanel panel = createPanel();
        Label widget = new Label("hello world");
        panel.setFooterWidget(widget);
        assertEquals(widget, panel.getFooterWidget());
        panel.remove(widget);
        assertNull(panel.getFooterWidget());
    }

    @Override
    protected HeaderPanel createPanel() {
        return new HeaderPanel();
    }

    @Override
    protected boolean supportsMultipleWidgets() {
        return false;
    }
}

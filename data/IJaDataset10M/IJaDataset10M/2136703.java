package com.google.gwt.user.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.http.client.URL;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test Case for {@link Window}.
 */
public class WindowTest extends GWTTestCase {

    private static native String getNodeName(Element elem);

    /**
   * Removes all elements in the body, except scripts and iframes.
   */
    private static void clearBodyContent() {
        Element bodyElem = RootPanel.getBodyElement();
        List<Element> toRemove = new ArrayList<Element>();
        for (int i = 0, n = DOM.getChildCount(bodyElem); i < n; ++i) {
            Element elem = DOM.getChild(bodyElem, i);
            String nodeName = getNodeName(elem);
            if (!"script".equals(nodeName) && !"iframe".equals(nodeName)) {
                toRemove.add(elem);
            }
        }
        for (int i = 0, n = toRemove.size(); i < n; ++i) {
            DOM.removeChild(bodyElem, toRemove.get(i));
        }
    }

    @Override
    public String getModuleName() {
        return "com.google.gwt.user.User";
    }

    public void testLocation() {
        History.newItem("foo bar");
        String hash = Window.Location.getHash();
        String host = Window.Location.getHost();
        String hostName = Window.Location.getHostName();
        String href = Window.Location.getHref();
        assertNull(Window.Location.getParameter("fuzzy bunny"));
        String path = Window.Location.getPath();
        String port = Window.Location.getPort();
        String protocol = Window.Location.getProtocol();
        String query = Window.Location.getQueryString();
        assertEquals(host, hostName + ":" + port);
        assertEquals(href, protocol + "//" + host + path + query + hash);
    }

    public void testLocationCreateUrlBuilder() {
        History.newItem("theHash");
        String expected = Location.getHref();
        UrlBuilder builder = Location.createUrlBuilder();
        String actual = builder.buildString();
        {
            String[] expectedParts = expected.split("#");
            String[] actualParts = actual.split("#");
            assertEquals(2, actualParts.length);
            assertEquals(expectedParts[1], actualParts[1]);
            expected = expectedParts[0];
            actual = actualParts[0];
        }
        {
            String[] expectedParts = expected.split("[?]");
            String[] actualParts = actual.split("[?]");
            if (expectedParts.length > 1) {
                assertEquals(2, actualParts.length);
                String[] expectedPairs = expectedParts[1].split("&");
                String[] actualPairs = actualParts[1].split("&");
                assertEquals(expectedPairs.length, actualPairs.length);
                for (String actualPair : actualPairs) {
                    String[] kv = actualPair.split("=");
                    assertEquals(Location.getParameter(kv[0]), kv.length > 1 ? URL.decodeQueryString(kv[1]) : "");
                }
            }
            expected = expectedParts[0];
            actual = actualParts[0];
        }
        assertEquals(expected, actual);
    }

    public void testLocationParsing() {
        Map<String, List<String>> map;
        map = Window.Location.buildListParamMap("?fuzzy=bunnies&foo=bar&num=42");
        assertEquals(map.size(), 3);
        assertEquals(map.get("foo").get(0), "bar");
        assertEquals(map.get("fuzzy").get(0), "bunnies");
        map = Window.Location.buildListParamMap("?fuzzy=bunnies&foo=bar&num=42&foo=baz");
        assertEquals(map.size(), 3);
        assertEquals(map.get("foo").get(0), "bar");
        assertEquals(map.get("foo").get(1), "baz");
        map = Window.Location.buildListParamMap("");
        assertEquals(map.size(), 0);
        map = Window.Location.buildListParamMap("?&& &a&b=&c=c&d=d=d&=e&f=2&f=1&");
        assertEquals(map.size(), 6);
        assertEquals(map.get(" ").get(0), "");
        assertEquals(map.get("a").get(0), "");
        assertEquals(map.get("b").get(0), "");
        assertEquals(map.get("c").get(0), "c");
        assertEquals(map.get("d").get(0), "d=d");
        assertEquals(map.get("f").get(0), "2");
        assertEquals(map.get("f").get(1), "1");
        map = Window.Location.buildListParamMap("?foo=bar%20baz%3aqux");
        assertEquals(map.get("foo").get(0), "bar baz:qux");
    }

    public void testNavigator() {
        assertNotNull(Navigator.getAppCodeName());
        assertNotNull(Navigator.getAppName());
        assertNotNull(Navigator.getAppVersion());
        assertNotNull(Navigator.getPlatform());
        assertNotNull(Navigator.getUserAgent());
        assertTrue(Navigator.isCookieEnabled());
        try {
            Navigator.isJavaEnabled();
        } catch (JavaScriptException e) {
            throw e;
        }
    }

    /**
   * Tests the ability of the Window to get the client size correctly with and
   * without visible scroll bars.
   * Failed in all modes due to HtmlUnit bug:
   * https://sourceforge.net/tracker/?func=detail&aid=2944261&group_id=47038&atid=448266
   */
    @DoNotRunWith(Platform.HtmlUnitBug)
    public void testGetClientSize() {
        clearBodyContent();
        Window.enableScrolling(false);
        final int oldClientHeight = Window.getClientHeight();
        final int oldClientWidth = Window.getClientWidth();
        assertTrue("Expect positive oldClientHeight. " + "This will fail in WebKit if run headless", oldClientHeight > 0);
        assertTrue(oldClientWidth > 0);
        if (oldClientHeight < 49 && Navigator.getUserAgent().contains("Firefox")) {
            return;
        }
        Window.enableScrolling(true);
        final Label largeDOM = new Label();
        largeDOM.setPixelSize(oldClientWidth + 100, oldClientHeight + 100);
        RootPanel.get().add(largeDOM);
        delayTestFinish(200);
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                int newClientHeight = Window.getClientHeight();
                int newClientWidth = Window.getClientWidth();
                assertTrue(newClientHeight < oldClientHeight);
                assertTrue(newClientWidth < oldClientWidth);
                RootPanel.get().remove(largeDOM);
                finishTest();
            }
        });
    }

    /**
   * Calculates the sizes for Window extras such as border, menu, tool bar, and
   * stores the original sizes to restore at the end of test.
   */
    public static final class ResizeHelper {

        private static int clientHeight;

        private static int clientWidth;

        private static int extraWidth;

        private static int extraHeight;

        private static boolean initialized;

        public static int getExtraHeight() {
            ensureInitialized();
            return extraHeight;
        }

        public static int getExtraWidth() {
            ensureInitialized();
            return extraWidth;
        }

        /**
     * Wraps {@code Window#resizeBy(int, int)} to ensure initialized. This may
     * be a no-op in Chrome.
     *
     * @param width
     * @param height
     * @return Whether this operation is done
     */
        public static boolean resizeBy(int width, int height) {
            if (ensureInitialized()) {
                Window.resizeBy(width, height);
            }
            return initialized;
        }

        /**
     * Wraps {@code Window#resizeTo(int, int)} to ensure initialized. This may
     * be a no-op in Chrome.
     *
     * @param width
     * @param height
     * @return Whether this operation is done
     */
        public static boolean resizeTo(int width, int height) {
            if (ensureInitialized()) {
                Window.resizeTo(width, height);
            }
            return initialized;
        }

        public static void restoreSize() {
            if (initialized) {
                Window.resizeTo(clientWidth + extraWidth, clientHeight + extraHeight);
            }
        }

        private static synchronized boolean ensureInitialized() {
            if (!initialized) {
                init();
            }
            return initialized;
        }

        private static void init() {
            if (Navigator.getUserAgent().toLowerCase().contains("chrome")) {
                return;
            }
            Window.moveTo(10, 10);
            Window.resizeTo(700, 500);
            clientHeight = Window.getClientHeight();
            clientWidth = Window.getClientWidth();
            Window.moveTo(0, 0);
            Window.resizeTo(750, 550);
            extraWidth = 750 - Window.getClientWidth();
            extraHeight = 550 - Window.getClientHeight();
            initialized = true;
            restoreSize();
        }
    }

    private static final class TestResizeHandler implements ResizeHandler {

        private int height;

        private int width;

        public void onResize(ResizeEvent event) {
            width = event.getWidth();
            height = event.getHeight();
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }

    /**
   * Tests the ability of resize the Window and catch resize events.
   */
    @DoNotRunWith({ Platform.HtmlUnitLayout })
    public void testResizing() {
        clearBodyContent();
        final TestResizeHandler resizeHandler = new TestResizeHandler();
        final HandlerRegistration handlerRegistration = Window.addResizeHandler(resizeHandler);
        delayTestFinish(2000);
        DeferredCommand.addCommand(new Command() {

            public void execute() {
                int width = 600;
                int height = 500;
                if (!ResizeHelper.resizeTo(width, height)) {
                    handlerRegistration.removeHandler();
                    finishTest();
                }
                assertEquals(width, Window.getClientWidth() + ResizeHelper.getExtraWidth());
                assertEquals(height, Window.getClientHeight() + ResizeHelper.getExtraHeight());
                ResizeHelper.resizeBy(10, 20);
                assertEquals(width + 10, Window.getClientWidth() + ResizeHelper.getExtraWidth());
                assertEquals(height + 20, Window.getClientHeight() + ResizeHelper.getExtraHeight());
                handlerRegistration.removeHandler();
                ResizeHelper.restoreSize();
                finishTest();
            }
        });
    }

    /**
   * Tests the ability of scroll the Window and catch scroll events.
   * Failed in all modes due to HtmlUnit bug:
   * https://sourceforge.net/tracker/?func=detail&aid=2897457&group_id=47038&atid=448266
   * <p>
   * TODO(flin): it is marked fixed, but is still not fixed.
   */
    @DoNotRunWith(Platform.HtmlUnitBug)
    public void testScrolling() {
        Window.enableScrolling(true);
        int clientHeight = Window.getClientHeight();
        int clientWidth = Window.getClientWidth();
        final Label largeDOM = new Label();
        largeDOM.setPixelSize(clientWidth + 500, clientHeight + 500);
        RootPanel.get().add(largeDOM);
        Window.scrollTo(100, 200);
        assertEquals(100, Window.getScrollLeft());
        assertEquals(200, Window.getScrollTop());
        Window.scrollTo(0, 0);
        assertEquals(0, Window.getScrollLeft());
        assertEquals(0, Window.getScrollTop());
        RootPanel.get().remove(largeDOM);
    }

    @SuppressWarnings("deprecation")
    static class ListenerTester implements WindowResizeListener {

        static int resize = 0;

        public void onWindowResized(int width, int height) {
            ++resize;
        }

        public static void fire() {
            resize = 0;
            ResizeEvent.fire(Window.handlers, 0, 0);
        }
    }

    @SuppressWarnings("deprecation")
    public void testListenerRemoval() {
        WindowResizeListener r1 = new ListenerTester();
        WindowResizeListener r2 = new ListenerTester();
        Window.addWindowResizeListener(r1);
        Window.addWindowResizeListener(r2);
        ListenerTester.fire();
        assertEquals(ListenerTester.resize, 2);
        Window.removeWindowResizeListener(r1);
        ListenerTester.fire();
        assertEquals(ListenerTester.resize, 1);
        Window.removeWindowResizeListener(r2);
        ListenerTester.fire();
        assertEquals(ListenerTester.resize, 0);
    }
}

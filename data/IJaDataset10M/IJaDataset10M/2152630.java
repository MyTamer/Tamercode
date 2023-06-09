package etch.util.core.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import etch.util.FlexBuffer;
import etch.util.Log;
import etch.util.Monitor;
import etch.util.core.Who;

/**
 * Tests TcpConnection.
 */
public class TestTcpConnection {

    private static final MyListener lh = new MyListener();

    private static final TcpListener l = new TcpListener("tcp://0.0.0.0:0", null);

    private static final int TIMEOUT = 4000;

    private static int port;

    /** @throws Exception */
    @BeforeClass
    public static void startListener() throws Exception {
        l.setSession(lh);
        l.start();
        l.waitUp(TIMEOUT);
        port = ((InetSocketAddress) l.localAddress()).getPort();
        Log.addSink(null);
        Log.report("TestTcpConnection");
    }

    /** @throws Exception */
    @AfterClass
    public static void stopListener() throws Exception {
        l.stop();
        l.waitDown(TIMEOUT);
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void constructor1() throws Exception {
        new TcpConnection(null, "tcp:", null);
    }

    /** @throws Exception */
    @Test
    public void constructor2() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        assertNull(c.getSession());
        s.close();
        t.close();
    }

    /** @throws Exception */
    @Test
    public void constructor3() throws Exception {
        SessionData dh = new MyDataHandler();
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertSame(dh, c.getSession());
        s.close();
        t.close();
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void constructor5a() throws Exception {
        new TcpConnection(null, "tcp://localhost", null);
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void constructor5b() throws Exception {
        new TcpConnection(null, "tcp://localhost:0", null);
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void constructor6() throws Exception {
        new TcpConnection(null, "tcp://localhost:65536", null);
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void constructor7() throws Exception {
        new TcpConnection(null, "tcp://localhost:1?TcpTransport.reconnectDelay=-1", null);
    }

    /** @throws Exception */
    @Test
    public void constructor8() throws Exception {
        TcpTransport c = new TcpConnection(null, "tcp://localhost:" + port, null);
        assertNull(c.getSession());
    }

    /** @throws Exception */
    @Test
    public void constructor9() throws Exception {
        SessionData dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://localhost:" + port, null);
        c.setSession(dh);
        assertSame(dh, c.getSession());
    }

    /** @throws Exception */
    @Test
    public void start1() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        c.waitUp(TIMEOUT);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        c.flush();
        assertNotNull(c.remoteAddress());
        assertNotNull(c.localAddress());
        assertEquals(c.remoteAddress(), t.getLocalSocketAddress());
        assertEquals(c.localAddress(), t.getRemoteSocketAddress());
        t.close();
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
    }

    /** @throws Exception */
    @Test
    public void start2() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        assertTrue(c.isStarted());
        c.stop();
        assertFalse(c.isStarted());
        c.waitDown(TIMEOUT);
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void start3() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        assertTrue(c.isStarted());
        c.close();
        c.waitDown(TIMEOUT);
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void start4() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        assertTrue(c.isStarted());
        c.close(false);
        c.waitDown(TIMEOUT);
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void start5() throws Exception {
        Socket s = new Socket("127.0.0.1", port);
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(s, "tcp:", null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        assertTrue(c.isStarted());
        c.close(true);
        c.waitDown(TIMEOUT);
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void start6() throws Exception {
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port, null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        assertTrue(c.isStarted());
        c.stop();
        assertFalse(c.isStarted());
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void restart1() throws Exception {
        Log.report("--- restart1 ------------------------------");
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port + "?TcpTransport.reconnectDelay=200", null);
        c.setSession(dh);
        c.start();
        c.waitUp(TIMEOUT);
        int n = 10;
        int i = 0;
        while (++i <= n) {
            Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
            Log.report("accepted", "i", i, "t.local", t.getLocalSocketAddress(), "t.remote", t.getRemoteSocketAddress());
            assertWhat(What.UP, dh.what);
            assertEquals(t.getLocalSocketAddress(), c.remoteAddress());
            assertEquals(t.getRemoteSocketAddress(), c.localAddress());
            t.getOutputStream().write(i);
            assertWhat(What.DATA, dh.what);
            assertNull(dh.xsender);
            assertNotNull(dh.xbuf);
            assertEquals(1, dh.xbuf.length);
            assertEquals((byte) i, dh.xbuf[0]);
            t.shutdownOutput();
            t.close();
            t = null;
            assertWhat(What.DOWN, dh.what);
        }
        Thread t1 = new MonitorHarvester<Socket>(lh.accepted);
        t1.start();
        Thread t2 = new MonitorHarvester<What>(dh.what);
        t2.start();
        Log.report("stopping");
        c.stop();
        c.waitDown(TIMEOUT);
        Log.report("stoppped");
        t1.join();
        t2.join();
    }

    /**
	 * @param <T>
	 */
    public static class MonitorHarvester<T> extends Thread {

        /**
		 * @param mon
		 */
        public MonitorHarvester(Monitor<T> mon) {
            this.mon = mon;
        }

        private final Monitor<T> mon;

        @Override
        public void run() {
            try {
                Object t;
                while ((t = mon.waitUntilNotEqAndSet(null, TIMEOUT, null)) != null) Log.report("harvested", "mon", mon, "t", t);
            } catch (Exception e) {
            }
        }
    }

    /** @throws Exception */
    @Test
    public void restart2() throws Exception {
        Log.report("--- restart2 ------------------------------");
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port + "?TcpTransport.reconnectDelay=200", null);
        c.setSession(dh);
        c.start();
        c.waitUp(TIMEOUT);
        int n = 10;
        int i = 0;
        while (++i <= n) {
            Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
            Log.report("accepted", "i", i, "t.local", t.getLocalSocketAddress(), "t.remote", t.getRemoteSocketAddress());
            assertWhat(What.UP, dh.what);
            assertEquals(t.getLocalSocketAddress(), c.remoteAddress());
            assertEquals(t.getRemoteSocketAddress(), c.localAddress());
            t.getOutputStream().write(i);
            assertWhat(What.DATA, dh.what);
            assertNull(dh.xsender);
            assertNotNull(dh.xbuf);
            assertEquals(1, dh.xbuf.length);
            assertEquals((byte) i, dh.xbuf[0]);
            c.shutdownOutput();
            assertEquals(-1, t.getInputStream().read());
            t.shutdownOutput();
            t.close();
            t = null;
            assertWhat(What.DOWN, dh.what);
        }
        Thread t1 = new MonitorHarvester<Socket>(lh.accepted);
        t1.start();
        Thread t2 = new MonitorHarvester<What>(dh.what);
        t2.start();
        Log.report("stopping");
        c.stop();
        c.waitDown(TIMEOUT);
        Log.report("stopped");
        t1.join();
        t2.join();
    }

    /** @throws Exception */
    @Test
    public void data1() throws Exception {
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port, null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.getOutputStream().write(23);
        t.getOutputStream().flush();
        assertWhat(What.DATA, dh.what);
        assertNull(dh.xsender);
        assertNotNull(dh.xbuf);
        assertEquals(1, dh.xbuf.length);
        assertEquals((byte) 23, dh.xbuf[0]);
        assertTrue(c.isStarted());
        c.stop();
        assertFalse(c.isStarted());
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void data2() throws Exception {
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port, null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        byte[] buf = { 21, 22, 23 };
        t.getOutputStream().write(buf);
        t.getOutputStream().flush();
        assertWhat(What.DATA, dh.what);
        assertNull(dh.xsender);
        assertNotNull(dh.xbuf);
        assertEquals(3, dh.xbuf.length);
        assertEquals((byte) 21, dh.xbuf[0]);
        assertEquals((byte) 22, dh.xbuf[1]);
        assertEquals((byte) 23, dh.xbuf[2]);
        assertTrue(c.isStarted());
        c.stop();
        assertFalse(c.isStarted());
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    /** @throws Exception */
    @Test
    public void data3() throws Exception {
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port, null);
        c.setSession(dh);
        assertFalse(c.isStarted());
        c.start();
        assertTrue(c.isStarted());
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.shutdownOutput();
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
        c.close();
    }

    /** @throws Exception */
    @Test
    public void data4() throws Exception {
        MyDataHandler dh = new MyDataHandler();
        TcpTransport c = new TcpConnection(null, "tcp://127.0.0.1:" + port, null);
        c.setSession(dh);
        c.start();
        Socket t = lh.accepted.waitUntilNotEqAndSet(null, TIMEOUT, null);
        assertWhat(What.UP, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        c.transportData(null, new FlexBuffer(new byte[] { 21, 22, 23 }));
        c.flush();
        InputStream is = t.getInputStream();
        assertEquals(21, is.read());
        assertEquals(22, is.read());
        assertEquals(23, is.read());
        c.shutdownOutput();
        assertEquals(-1, is.read());
        c.close();
        assertWhat(What.DOWN, dh.what);
        assertNull(dh.xsender);
        assertNull(dh.xbuf);
        t.close();
    }

    private void assertWhat(What what, Monitor<What> mon) throws Exception {
        mon.waitUntilEqAndSet(what, TIMEOUT, null);
    }

    /**
	 * Test events.
	 */
    enum What {

        /** connection is up */
        UP, /** connection is down */
        DOWN, /** connection read some data */
        DATA
    }

    /**
	 * ListenerHandler for testing.
	 */
    static class MyListener implements SessionListener {

        /** monitor to report accepted socket */
        public Monitor<Socket> accepted = new Monitor<Socket>("accepted");

        public void sessionAccepted(Socket s) throws Exception {
            accepted.waitUntilEqAndSet(null, TIMEOUT, s);
        }

        public Object sessionQuery(Object query) {
            throw new UnsupportedOperationException("unknown query " + query);
        }

        public void sessionControl(Object control, Object value) {
            throw new UnsupportedOperationException("unknown control " + control);
        }

        public void sessionNotify(Object event) {
            if (event instanceof Exception) Log.report("listener.notify", "event", event);
        }
    }

    /**
	 * DataHandler for testing.
	 */
    static class MyDataHandler implements SessionData {

        /** holds event type */
        public Monitor<What> what = new Monitor<What>("what");

        /** sender of the event if DATA */
        public Who xsender;

        /** data of the event if DATA */
        public byte[] xbuf;

        public void sessionData(Who sender, FlexBuffer buf) throws Exception {
            xsender = sender;
            xbuf = buf.getAvailBytes();
            what.waitUntilEqAndSet(null, TIMEOUT, What.DATA);
        }

        public Object sessionQuery(Object query) throws Exception {
            throw new UnsupportedOperationException("unknown query " + query);
        }

        public void sessionControl(Object control, Object value) throws Exception {
            throw new UnsupportedOperationException("unknown control " + control);
        }

        public void sessionNotify(Object event) throws Exception {
            if (event.equals(Session.UP)) {
                xbuf = null;
                what.waitUntilEqAndSet(null, TIMEOUT, What.UP);
                return;
            }
            if (event.equals(Session.DOWN)) {
                xbuf = null;
                what.waitUntilEqAndSet(null, TIMEOUT, What.DOWN);
                return;
            }
            Log.report("data.notify", "event", event);
        }
    }
}

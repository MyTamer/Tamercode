package com.limegroup.gnutella;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;
import junit.framework.Test;
import com.google.inject.Injector;
import static com.limegroup.gnutella.ConnectionManagerImpl.MAX_TCP_CONNECT_BACK_ATTEMPTS;
import com.limegroup.gnutella.connection.BlockingConnection;
import com.limegroup.gnutella.messages.Message;
import com.limegroup.gnutella.messages.PingRequest;
import com.limegroup.gnutella.messages.PingRequestFactory;
import com.limegroup.gnutella.messages.vendor.MessagesSupportedVendorMessage;
import com.limegroup.gnutella.messages.vendor.ReplyNumberVendorMessage;
import com.limegroup.gnutella.messages.vendor.ReplyNumberVendorMessageFactory;
import com.limegroup.gnutella.messages.vendor.TCPConnectBackVendorMessage;
import com.limegroup.gnutella.messages.vendor.UDPConnectBackVendorMessage;
import com.limegroup.gnutella.settings.ConnectionSettings;

/**
 * Checks whether leaves request redirects properly.  
 * The test includes a leaf attached to two ultrapeers.
 */
public class ClientSideValidateIncomingTest extends ClientSideTestCase {

    protected final int PORT = 6669;

    {
        TIMEOUT = 1000;
    }

    private static final long MY_EXPIRE_TIME = 6 * 1000;

    private static final long MY_WAIT_TIME = 500;

    private static final long MY_VALIDATE_TIME = 3 * 1000;

    private static byte[] cbGuid = null;

    private AcceptorImpl acceptor;

    private MessagesSupportedVendorMessage messagesSupportedVendorMessage;

    private NetworkManager networkManager;

    private UDPService udpService;

    private ConnectionManager connectionManager;

    private PingRequestFactory pingRequestFactory;

    private ReplyNumberVendorMessageFactory replyNumberVendorMessageFactory;

    public ClientSideValidateIncomingTest(String name) {
        super(name);
    }

    public static Test suite() {
        return buildTestSuite(ClientSideValidateIncomingTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void setUp() throws Exception {
        Injector injector = LimeTestUtils.createInjector();
        acceptor = (AcceptorImpl) injector.getInstance(Acceptor.class);
        acceptor.setIncomingExpireTime(MY_EXPIRE_TIME);
        acceptor.setWaitTimeAfterRequests(MY_WAIT_TIME);
        acceptor.setTimeBetweenValidates(MY_VALIDATE_TIME);
        super.setUp(injector);
        messagesSupportedVendorMessage = injector.getInstance(MessagesSupportedVendorMessage.class);
        networkManager = injector.getInstance(NetworkManager.class);
        udpService = injector.getInstance(UDPService.class);
        connectionManager = injector.getInstance(ConnectionManager.class);
        pingRequestFactory = injector.getInstance(PingRequestFactory.class);
        replyNumberVendorMessageFactory = injector.getInstance(ReplyNumberVendorMessageFactory.class);
        exchangeSupportedMessages();
    }

    @Override
    public void setSettings() throws Exception {
        ConnectionSettings.LOCAL_IS_PRIVATE.setValue(false);
    }

    private void exchangeSupportedMessages() throws Exception {
        testUP[0].send(messagesSupportedVendorMessage);
        testUP[0].flush();
        testUP[1].send(messagesSupportedVendorMessage);
        testUP[1].flush();
        Message m = null;
        boolean gotTCP = false, gotUDP = false;
        do {
            m = testUP[0].receive(TIMEOUT);
            if (m instanceof TCPConnectBackVendorMessage) gotTCP = true;
            if (m instanceof UDPConnectBackVendorMessage) {
                cbGuid = m.getGUID();
                gotUDP = true;
            }
        } while (!gotTCP || !gotUDP);
    }

    /**
     * Tests that only a limited number of connect back messages are 
     * sent upon connection initialization.
     */
    public void testLimitedConnectBacksSent() throws Exception {
        final int max = MAX_TCP_CONNECT_BACK_ATTEMPTS + 3;
        int received = 0;
        for (int i = 0; i < max; i++) {
            testUP[0].send(messagesSupportedVendorMessage);
            testUP[0].flush();
            testUP[1].send(messagesSupportedVendorMessage);
            testUP[1].flush();
            while (BlockingConnectionUtils.getFirstInstanceOfMessageType(testUP[0], TCPConnectBackVendorMessage.class, 100) != null) received++;
            while (BlockingConnectionUtils.getFirstInstanceOfMessageType(testUP[1], TCPConnectBackVendorMessage.class, 100) != null) received++;
        }
        assertLessThan(max, received);
    }

    public void testTCPExpireRequestsNotSent() throws Exception {
        drainAll();
        for (int i = 0; i < 2; i++) {
            assertFalse(networkManager.acceptedIncomingConnection());
            try {
                testUP[0].receive(TIMEOUT);
            } catch (InterruptedIOException expected) {
            }
            Thread.sleep(MY_EXPIRE_TIME + MY_VALIDATE_TIME);
            assertFalse(networkManager.acceptedIncomingConnection());
            Thread.sleep(100);
            Message m = null;
            do {
                m = testUP[0].receive(TIMEOUT);
            } while (!(m instanceof TCPConnectBackVendorMessage));
        }
    }

    public void testTCPExpireRequestsSent() throws Exception {
        drainAll();
        for (int i = 0; i < 2; i++) {
            Socket s = new Socket("localhost", PORT);
            s.close();
            Thread.sleep(100);
            assertFalse(networkManager.acceptedIncomingConnection());
            s = new Socket("localhost", PORT);
            s.getOutputStream().write("CONNECT BACK\r\r".getBytes());
            Thread.sleep(500);
            s.close();
            assertTrue(networkManager.acceptedIncomingConnection());
            Thread.sleep(MY_EXPIRE_TIME + MY_VALIDATE_TIME + 1000);
            assertFalse(networkManager.acceptedIncomingConnection());
            Message m = null;
            do {
                m = testUP[0].receive(TIMEOUT);
            } while (!(m instanceof TCPConnectBackVendorMessage));
        }
    }

    /**
     * Tests that if the leaf is connected to only one ultrapeer it will 
     * send a few redundant requests
     */
    public void testTCPRedundantRequestsSent() throws Exception {
        BlockingConnectionUtils.drainAllParallel(testUP);
        Thread.sleep(MY_EXPIRE_TIME + 1000);
        readNumConnectBacks(1, testUP[0], TIMEOUT);
        readNumConnectBacks(1, testUP[1], TIMEOUT);
        assertGreaterThan(1, connectionManager.getNumInitializedConnections());
        testUP[1].close();
        Thread.sleep(500);
        assertEquals(1, connectionManager.getNumInitializedConnections());
        drainAll();
        Thread.sleep(MY_VALIDATE_TIME + 1000);
        readNumConnectBacks(ConnectionManager.CONNECT_BACK_REDUNDANT_REQUESTS, testUP[0], TIMEOUT);
    }

    private void readNumConnectBacks(int num, BlockingConnection conn, int timeout) throws Exception {
        Message m;
        for (int i = 0; i < num; i++) {
            do {
                m = conn.receive(timeout);
            } while (!(m instanceof TCPConnectBackVendorMessage));
        }
        try {
            do {
                m = conn.receive(timeout);
            } while (!(m instanceof TCPConnectBackVendorMessage));
            fail("got extra message " + m.getClass() + " on " + conn);
        } catch (IOException expected) {
        }
    }

    public void testUDPExpireRequestsNotSent() throws Exception {
        drainAll();
        UDPService udpServ = udpService;
        for (int i = 0; i < 2; i++) {
            assertFalse(udpServ.canReceiveUnsolicited());
            try {
                testUP[0].receive(TIMEOUT);
            } catch (InterruptedIOException expected) {
            }
            Thread.sleep(MY_EXPIRE_TIME + MY_VALIDATE_TIME);
            assertFalse(udpServ.canReceiveUnsolicited());
            Thread.sleep(100);
            Message m = null;
            do {
                m = testUP[0].receive(TIMEOUT);
            } while (!(m instanceof UDPConnectBackVendorMessage));
            cbGuid = m.getGUID();
        }
    }

    public void testUDPExpireRequestsSent() throws Exception {
        drainAll();
        UDPService udpServ = udpService;
        for (int i = 0; i < 2; i++) {
            Message m = null;
            try {
                while (true) {
                    m = testUP[0].receive(200);
                }
            } catch (InterruptedIOException drained) {
            }
            do {
                m = testUP[0].receive();
            } while (!(m instanceof UDPConnectBackVendorMessage));
            Thread.sleep(1000);
            assertFalse(udpServ.canReceiveUnsolicited());
            do {
                m = testUP[0].receive();
            } while (!(m instanceof UDPConnectBackVendorMessage));
            cbGuid = ((UDPConnectBackVendorMessage) m).getConnectBackGUID().bytes();
            DatagramSocket s = new DatagramSocket();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PingRequest ping = pingRequestFactory.createPingRequest(cbGuid, (byte) 1, (byte) 1);
            ping.write(baos);
            DatagramPacket pack = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, InetAddress.getLocalHost(), PORT);
            s.send(pack);
            s.close();
            Thread.sleep(1000);
            assertTrue(udpServ.canReceiveUnsolicited());
        }
    }

    public void testUDPInterleavingRequestsSent() throws Exception {
        drainAll();
        UDPService udpServ = udpService;
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            if (rand.nextBoolean()) {
                DatagramSocket s = new DatagramSocket();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ReplyNumberVendorMessage vm = replyNumberVendorMessageFactory.create(new GUID(cbGuid), 1);
                vm.write(baos);
                DatagramPacket pack = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, InetAddress.getLocalHost(), PORT);
                s.send(pack);
                s.close();
                Thread.sleep(100);
            }
            Thread.sleep(MY_EXPIRE_TIME + MY_VALIDATE_TIME);
            if (udpServ.canReceiveUnsolicited()) {
                DatagramSocket s = new DatagramSocket();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ReplyNumberVendorMessage vm = replyNumberVendorMessageFactory.create(new GUID(cbGuid), 1);
                vm.write(baos);
                DatagramPacket pack = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, InetAddress.getLocalHost(), PORT);
                s.send(pack);
                s.close();
                Thread.sleep(100);
                assertTrue(udpServ.canReceiveUnsolicited());
                try {
                    testUP[0].receive(TIMEOUT);
                } catch (InterruptedIOException expected) {
                }
            } else {
                Thread.sleep(MY_VALIDATE_TIME);
                Thread.sleep(100);
                Message m = null;
                do {
                    m = testUP[0].receive(TIMEOUT);
                } while (!(m instanceof UDPConnectBackVendorMessage));
                cbGuid = ((UDPConnectBackVendorMessage) m).getConnectBackGUID().bytes();
                DatagramSocket s = new DatagramSocket();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PingRequest ping = pingRequestFactory.createPingRequest(cbGuid, (byte) 1, (byte) 1);
                ping.write(baos);
                DatagramPacket pack = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, InetAddress.getLocalHost(), PORT);
                s.send(pack);
                s.close();
            }
        }
    }

    public int getNumberOfPeers() {
        return 2;
    }
}

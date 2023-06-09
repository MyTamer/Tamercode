package biz.xsoftware.test.nio.udp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import biz.xsoftware.api.nio.BufferHelper;
import biz.xsoftware.api.nio.ChannelManager;
import biz.xsoftware.api.nio.ChannelService;
import biz.xsoftware.api.nio.ChannelServiceFactory;
import biz.xsoftware.api.nio.channels.UDPChannel;
import biz.xsoftware.api.nio.handlers.DataListener;
import biz.xsoftware.api.nio.libs.BufferFactory;
import biz.xsoftware.api.nio.libs.FactoryCreator;
import biz.xsoftware.api.nio.testutil.CloneByteBuffer;
import biz.xsoftware.mock.CalledMethod;
import biz.xsoftware.mock.MockObject;
import biz.xsoftware.mock.testcase.MockTestCase;

/**
 */
public class TestUdpIntegration extends MockTestCase {

    private static final BufferHelper HELPER = ChannelServiceFactory.bufferHelper(null);

    private static final Logger log = Logger.getLogger(TestUdpIntegration.class.getName());

    private ChannelService svc;

    private UDPChannel client;

    private UDPChannel server;

    private MockObject clientHandler;

    private MockObject svrHandler;

    private BufferFactory bufFactory;

    public TestUdpIntegration(String name) {
        super(name);
    }

    protected void setUpImpl() throws Exception {
        if (bufFactory == null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(FactoryCreator.KEY_IS_DIRECT, false);
            FactoryCreator creator = FactoryCreator.createFactory(null);
            bufFactory = creator.createBufferFactory(map);
        }
        ChannelServiceFactory basic = ChannelServiceFactory.createFactory(null);
        Map<String, Object> props2 = new HashMap<String, Object>();
        props2.put(ChannelServiceFactory.KEY_IMPLEMENTATION_CLASS, ChannelServiceFactory.VAL_EXCEPTION_CHANNEL_MGR);
        props2.put(ChannelServiceFactory.KEY_CHILD_CHANNELMGR_FACTORY, basic);
        ChannelServiceFactory factory = ChannelServiceFactory.createFactory(props2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ChannelManager.KEY_BUFFER_FACTORY, bufFactory);
        map.put(ChannelManager.KEY_ID, "server+client");
        svc = factory.createChannelManager(map);
        svc.start();
        client = svc.createUDPChannel("client", null);
        server = svc.createUDPChannel("server", null);
        clientHandler = createMock(DataListener.class);
        svrHandler = createMock(DataListener.class);
        clientHandler.setDefaultBehavior("incomingData", new CloneByteBuffer());
        svrHandler.setDefaultBehavior("incomingData", new CloneByteBuffer());
    }

    protected void tearDownImpl() throws Exception {
        svc.stop();
        clientHandler.expect(MockObject.NONE);
        svrHandler.expect(MockObject.NONE);
    }

    public void testRawDatagram() throws Exception {
        Selector sel = Selector.open();
        DatagramChannel peer1 = DatagramChannel.open();
        DatagramChannel peer2 = DatagramChannel.open();
        int port1 = 19858;
        int port2 = 19533;
        InetSocketAddress peer1Addr = new InetSocketAddress(port1);
        InetSocketAddress peer2Addr = new InetSocketAddress(port2);
        peer1.configureBlocking(false);
        peer2.configureBlocking(false);
        Object o = new Object();
        peer1.register(sel, SelectionKey.OP_READ, o);
        peer2.register(sel, SelectionKey.OP_READ, o);
        peer1.socket().bind(peer1Addr);
        peer2.socket().bind(peer2Addr);
        InetAddress localHost = InetAddress.getLocalHost();
        peer1Addr = new InetSocketAddress(localHost, port1);
        peer2Addr = new InetSocketAddress(localHost, port2);
        peer1.connect(peer2Addr);
        peer2.connect(peer1Addr);
        String msg = "Asfdsf";
        sendMessage(peer1, peer2, msg);
        peer1.disconnect();
        String msg2 = "shouldBedropped";
        writePacket(peer2, msg2);
        Thread.sleep(1000);
        peer1.connect(peer2Addr);
        sel.select(10000);
        sendMessage(peer1, peer2, msg2);
    }

    private void sendMessage(DatagramChannel peer1, DatagramChannel peer2, String msg) throws IOException, InterruptedException {
        ByteBuffer b = writePacket(peer2, msg);
        Thread.sleep(1000);
        b.clear();
        peer1.read(b);
        b.flip();
        String actual = HELPER.readString(b, b.remaining());
        assertEquals(msg, actual);
    }

    private ByteBuffer writePacket(DatagramChannel peer2, String msg) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(1000);
        HELPER.putString(b, msg);
        HELPER.doneFillingBuffer(b);
        peer2.write(b);
        return b;
    }

    public void xxxtestDisconnect() throws IOException, InterruptedException {
        InetSocketAddress svrAddr = runBasic();
        client.disconnect();
        String msg = "hello";
        writePacket(server, msg);
        Thread.sleep(1000);
        clientHandler.expect(MockObject.NONE);
        System.out.println("addr=" + svrAddr);
        client.connect(svrAddr);
        String msg2 = "abxdefg";
        writePacket(server, msg2);
        String[] methodNames = new String[] { "incomingData", "incomingData" };
        CalledMethod[] methods = clientHandler.expect(methodNames);
        ByteBuffer actualBuf1 = (ByteBuffer) methods[0].getAllParams()[1];
        String actual1 = HELPER.readString(actualBuf1, actualBuf1.remaining());
        assertEquals(msg, actual1);
        ByteBuffer actualBuf2 = (ByteBuffer) methods[1].getAllParams()[1];
        String actual2 = HELPER.readString(actualBuf2, actualBuf2.remaining());
        assertEquals(msg2, actual2);
    }

    public void testBasic() throws IOException, InterruptedException {
        runBasic();
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    private InetSocketAddress runBasic() throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getLocalHost();
        client.bind(new InetSocketAddress(localhost, 0));
        server.bind(new InetSocketAddress(localhost, 0));
        InetSocketAddress clientAddr = client.getLocalAddress();
        InetSocketAddress svrAddr = server.getLocalAddress();
        assertTrue(clientAddr.getPort() > 0);
        assertTrue(svrAddr.getPort() > 0);
        client.connect(svrAddr);
        server.connect(clientAddr);
        client.registerForReads((DataListener) clientHandler);
        server.registerForReads((DataListener) svrHandler);
        sendReceivePacket();
        return svrAddr;
    }

    /**
     * @throws IOException
     */
    private void sendReceivePacket() throws IOException {
        String msg = "Asfdsf";
        writePacket(client, msg);
        CalledMethod m = svrHandler.expect("incomingData");
        ByteBuffer actualBuf = (ByteBuffer) m.getAllParams()[1];
        String actual = HELPER.readString(actualBuf, actualBuf.remaining());
        assertEquals(msg, actual);
        String msg2 = "xyasdf";
        writePacket(server, msg2);
        CalledMethod m2 = clientHandler.expect("incomingData");
        ByteBuffer actualBuf2 = (ByteBuffer) m2.getAllParams()[1];
        String actual2 = HELPER.readString(actualBuf2, actualBuf2.remaining());
        assertEquals(msg2, actual2);
    }

    public void xxtestPortUnreachable() throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getLocalHost();
        InetSocketAddress svrAddr = new InetSocketAddress(localhost, 19191);
        InetSocketAddress clientAddr = setupPortUnreachable(svrAddr);
        server.bind(svrAddr);
        server.connect(clientAddr);
        server.registerForReads((DataListener) svrHandler);
        sendReceivePacket();
    }

    /**
     * @param svrAddr
     * @throws IOException
     * @throws InterruptedException
     */
    private InetSocketAddress setupPortUnreachable(InetSocketAddress svrAddr) throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getLocalHost();
        client.bind(new InetSocketAddress(localhost, 0));
        InetSocketAddress clientAddr = client.getLocalAddress();
        client.connect(svrAddr);
        client.registerForReads((DataListener) clientHandler);
        String msg = "aaaaa";
        writePacket(client, msg);
        CalledMethod m = clientHandler.expect("failure");
        PortUnreachableException exc = (PortUnreachableException) m.getAllParams()[2];
        log.log(Level.FINE, "this is expected", exc);
        return clientAddr;
    }

    /**
     * @param msg 
     * @throws IOException
     */
    private void writePacket(UDPChannel c, String msg) throws IOException {
        ByteBuffer b = ByteBuffer.allocate(1000);
        HELPER.putString(b, msg);
        HELPER.doneFillingBuffer(b);
        c.write(b);
    }
}

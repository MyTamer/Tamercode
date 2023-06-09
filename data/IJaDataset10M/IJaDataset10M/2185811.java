package org.opennms.netmgt.dhcpd;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.UndeclaredThrowableException;
import java.lang.NumberFormatException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import org.apache.log4j.Category;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.config.DhcpdConfigFactory;
import org.opennms.netmgt.utils.IpValidator;
import edu.bucknell.net.JDHCP.DHCPMessage;

/**
 * <P>
 * Establishes a TCP socket connection with the DHCP daemon and formats and
 * sends request messages.
 * </P>
 * 
 * @author <A HREF="mailto:mike@opennms.org">Mike </A>
 * @author <A HREF="http://www.opennms.org/">OpenNMS </A>
 * @version CVS 1.1.1.1
 */
final class Poller {

    /**
     * The hardware address (ex: 00:06:0D:BE:9C:B2)
     */
    private static final byte[] DEFAULT_MAC_ADDRESS = { (byte) 0x00, (byte) 0x06, (byte) 0x0d, (byte) 0xbe, (byte) 0x9c, (byte) 0xb2, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

    private static byte[] s_hwAddress = null;

    private static byte[] s_myIpAddress = null;

    private static byte[] s_requestIpAddress = null;

    private static boolean reqTargetIp = true;

    private static boolean targetOffset = true;

    private static boolean relayMode = false;

    private static boolean paramsChecked = false;

    private static Boolean extendedMode = false;

    /**
     * Broadcast flag...when set in the 'flags' portion of the DHCP query
     * packet, it forces the DHCP server to broadcast the DHCP response. This
     * is useful when we are not setting the relay address in the outgoing
     * DHCP query. Otherwise, we would not receive the response.
     */
    static final short BROADCAST_FLAG = (short) 0x8000;

    /**
     * Default retries
     */
    static final int DEFAULT_RETRIES = 2;

    /**
     * Default timeout
     */
    static final long DEFAULT_TIMEOUT = 3000L;

    /**
     * The message type option for the DHCP request.
     */
    private static final int MESSAGE_TYPE = 53;

    /**
     * The requested ip option for the DHCP request.
     */
    private static final int REQUESTED_IP = 50;

    /**
     * Holds the value for the next identifier sent to the DHCP server.
     */
    private static int m_nextXid = (new java.util.Random(System.currentTimeMillis())).nextInt();

    /**
     * TCP Socket connection with DHCP Daemon
     */
    private Socket m_connection;

    /**
     * Output Object stream
     */
    private ObjectOutputStream m_outs;

    /**
     * Objects from the server.
     */
    private ObjectInputStream m_ins;

    /**
     * Returns a disconnection request message that can be sent to the server.
     * 
     * @return A disconnection message.
     */
    private static Message getDisconnectRequest() throws UnknownHostException {
        return new Message(InetAddress.getByName("0.0.0.0"), new DHCPMessage());
    }

    /**
     * Returns a DHCP DISCOVER, INFORM, or REQUEST message that can be sent to
     * the DHCP server. DHCP server should respond with a DHCP OFFER, ACK, or
     * NAK message in response..
     * 
     * @param (InetAddress) addr The address to poll
     * @param (byte) mType The type of DHCP message to send (DISCOVER, INFORM,
     *        or REQUEST)
     * @return The message to send to the DHCP server.
     */
    private static Message getPollingRequest(InetAddress addr, byte mType) {
        int xid = 0;
        synchronized (Poller.class) {
            xid = ++m_nextXid;
        }
        DHCPMessage messageOut = new DHCPMessage();
        byte[] rawIp = addr.getAddress();
        if (targetOffset) {
            if (rawIp[3] % 2 == 0 && rawIp[3] != 0) {
                --rawIp[3];
            } else {
                ++rawIp[3];
            }
        }
        messageOut.setOp((byte) 1);
        messageOut.setHtype((byte) 1);
        messageOut.setHlen((byte) 6);
        messageOut.setXid(xid);
        messageOut.setSecs((short) 0);
        messageOut.setChaddr(s_hwAddress);
        if (relayMode) {
            messageOut.setHops((byte) 1);
            messageOut.setGiaddr(s_myIpAddress);
        } else {
            messageOut.setHops((byte) 0);
            messageOut.setFlags(BROADCAST_FLAG);
        }
        messageOut.setOption(MESSAGE_TYPE, new byte[] { mType });
        if (mType == DHCPMessage.REQUEST) {
            if (reqTargetIp) {
                messageOut.setOption(REQUESTED_IP, rawIp);
                messageOut.setCiaddr(rawIp);
            } else {
                messageOut.setOption(REQUESTED_IP, s_requestIpAddress);
                messageOut.setCiaddr(s_requestIpAddress);
            }
        }
        if (mType == DHCPMessage.INFORM) {
            messageOut.setOption(REQUESTED_IP, s_myIpAddress);
            messageOut.setCiaddr(s_myIpAddress);
        }
        return new Message(addr, messageOut);
    }

    /**
     * Ensures that during garbage collection the resources used by this
     * object are released!
     */
    protected void finalize() throws Throwable {
        close();
    }

    /**
     * Constructor. Establishes a TCP socket connection with the DHCP client
     * daemon on port 5818.
     * 
     * @throws IOException
     *             if unable to establish the connection with the DHCP client
     *             daemon.
     */
    private Poller(long timeout) throws IOException {
        Category log = ThreadCategory.getInstance(this.getClass());
        DhcpdConfigFactory dcf = DhcpdConfigFactory.getInstance();
        try {
            if (log.isDebugEnabled()) {
                log.debug("Poller.ctor: opening socket connection with DHCP client daemon on port " + dcf.getPort());
            }
            m_connection = new Socket(InetAddress.getByName("127.0.0.1"), dcf.getPort());
            if (log.isDebugEnabled()) {
                log.debug("Poller.ctor: setting socket timeout to " + timeout);
            }
            m_connection.setSoTimeout((int) timeout);
            m_ins = new ObjectInputStream(m_connection.getInputStream());
            m_outs = new ObjectOutputStream(m_connection.getOutputStream());
            m_outs.reset();
            m_outs.flush();
        } catch (IOException ex) {
            log.error("IO Exception during socket connection establishment with DHCP client daemon.", ex);
            if (m_connection != null) {
                try {
                    m_ins.close();
                    m_outs.close();
                    m_connection.close();
                } catch (Throwable t) {
                }
            }
            throw ex;
        } catch (Throwable t) {
            log.error("Unexpected exception during socket connection establishment with DHCP client daemon.", t);
            if (m_connection != null) {
                try {
                    m_ins.close();
                    m_outs.close();
                    m_connection.close();
                } catch (Throwable tx) {
                }
            }
            throw new UndeclaredThrowableException(t);
        }
    }

    /**
     * Closes the client's socket connection to the DHCP daemon.
     * 
     * @throws IOException
     *             if the socket close() method fails.
     */
    public void close() {
        Category log = ThreadCategory.getInstance(Poller.class);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Closing connection");
            }
            m_ins.close();
            m_outs.close();
            m_connection.close();
        } catch (Throwable ex) {
        }
    }

    /**
     * <p>
     * This method actually tests the remote host to determine if it is
     * running a functional DHCP server.
     * </p>
     * <p>
     * Formats a DHCP query and encodes it in a client request message which
     * is sent to the DHCP daemon over the established TCP socket connection.
     * If a matching DHCP response packet is not received from the DHCP daemon
     * within the specified timeout the client request message will be re-sent
     * up to the specified number of retries.
     * </p>
     * <p>
     * If a response is received from the DHCP daemon it is validated to
     * ensure that:
     * </p>
     * <ul>
     * <li>The DHCP response packet was sent from the remote host to which the
     * original request packet was directed.</li>
     * <li>The XID of the DHCP response packet matches the XID of the original
     * DHCP request packet.</li>
     * </ul>
     * <p>
     * If the response validates 'true' is returned. Otherwise the request is
     * resent until max retry count is exceeded.
     * </p>
     * <p>
     * Before returning, a client disconnect message (remote host field set to
     * zero) is sent to the DHCP daemon.
     * </p>
     * 
     * @return response time in milliseconds if the specified host responded
     *         with a valid DHCP offer datagram within the context of the
     *         specified timeout and retry values or negative one (-1)
     *         otherwise.
     */
    static long isServer(InetAddress host, long timeout, int retries) throws IOException {
        Category log = ThreadCategory.getInstance(Poller.class);
        boolean isDhcpServer = false;
        byte[] typeList = { (byte) DHCPMessage.DISCOVER, (byte) DHCPMessage.INFORM, (byte) DHCPMessage.REQUEST };
        String[] typeName = { "DISCOVER", "INFORM", "REQUEST" };
        DhcpdConfigFactory dcf = DhcpdConfigFactory.getInstance();
        if (!paramsChecked) {
            String s_extendedMode = dcf.getExtendedMode();
            if (s_extendedMode == null) {
                extendedMode = false;
            } else {
                extendedMode = Boolean.parseBoolean(s_extendedMode);
            }
            if (log.isDebugEnabled()) {
                log.debug("isServer: DHCP extended mode is " + extendedMode);
            }
            String hwAddressStr = dcf.getMacAddress();
            if (log.isDebugEnabled()) {
                log.debug("isServer: DHCP query hardware/MAC address is " + hwAddressStr);
            }
            setHwAddress(hwAddressStr);
            String myIpStr = dcf.getMyIpAddress();
            if (log.isDebugEnabled()) {
                log.debug("isServer: DHCP relay agent address is " + myIpStr);
            }
            if (myIpStr == null || myIpStr.equals("") || myIpStr.equalsIgnoreCase("broadcast")) {
            } else if (IpValidator.isIpValid(myIpStr)) {
                s_myIpAddress = setIpAddress(myIpStr);
                relayMode = true;
            }
            if (extendedMode == true) {
                String requestStr = dcf.getRequestIpAddress();
                if (log.isDebugEnabled()) {
                    log.debug("isServer: REQUEST query target is " + requestStr);
                }
                if (requestStr == null || requestStr.equals("") || requestStr.equalsIgnoreCase("targetSubnet")) {
                } else if (requestStr.equalsIgnoreCase("targetHost")) {
                    targetOffset = false;
                } else if (IpValidator.isIpValid(requestStr)) {
                    s_requestIpAddress = setIpAddress(requestStr);
                    reqTargetIp = false;
                    targetOffset = false;
                }
                if (log.isDebugEnabled()) {
                    log.debug("REQUEST query options are: reqTargetIp = " + reqTargetIp + ", targetOffset = " + targetOffset);
                }
            }
            paramsChecked = true;
        }
        int j = 1;
        if (extendedMode == true) {
            j = typeList.length;
        }
        if (timeout < 500) {
            timeout = 500;
        }
        Poller p = new Poller(timeout);
        long responseTime = -1;
        try {
            pollit: for (int i = 0; i < j; i++) {
                Message ping = getPollingRequest(host, (byte) typeList[i]);
                int rt = retries;
                while (rt >= 0 && !isDhcpServer) {
                    if (log.isDebugEnabled()) {
                        log.debug("isServer: sending DHCP " + typeName[i] + " query to host " + host.getHostAddress() + " with Xid: " + ping.getMessage().getXid());
                    }
                    long start = System.currentTimeMillis();
                    p.m_outs.writeObject(ping);
                    long end;
                    do {
                        Message resp = null;
                        try {
                            resp = (Message) p.m_ins.readObject();
                        } catch (InterruptedIOException ex) {
                            resp = null;
                        }
                        if (resp != null) {
                            responseTime = System.currentTimeMillis() - start;
                            if (log.isDebugEnabled()) {
                                log.debug("isServer: got a DHCP response from host " + resp.getAddress().getHostAddress() + " with Xid: " + resp.getMessage().getXid());
                            }
                            if (host.equals(resp.getAddress()) && ping.getMessage().getXid() == resp.getMessage().getXid()) {
                                byte[] type = resp.getMessage().getOption(MESSAGE_TYPE);
                                if (log.isDebugEnabled()) {
                                    if (type[0] == DHCPMessage.OFFER) {
                                        log.debug("isServer: got a DHCP OFFER response, validating...");
                                    } else if (type[0] == DHCPMessage.ACK) {
                                        log.debug("isServer: got a DHCP ACK response, validating...");
                                    } else if (type[0] == DHCPMessage.NAK) {
                                        log.debug("isServer: got a DHCP NAK response, validating...");
                                    }
                                }
                                if (type[0] == DHCPMessage.OFFER || (extendedMode == true && (type[0] == DHCPMessage.ACK || type[0] == DHCPMessage.NAK))) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("isServer: got a valid DHCP response. responseTime= " + responseTime + "ms");
                                    }
                                    isDhcpServer = true;
                                    break pollit;
                                }
                            }
                        }
                        end = System.currentTimeMillis();
                    } while ((end - start) < timeout);
                    if (log.isDebugEnabled()) {
                        if (!isDhcpServer) {
                            log.debug("Timed out waiting for DHCP response, remaining retries: " + rt);
                        }
                    }
                    --rt;
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("Sending disconnect request");
            }
            p.m_outs.writeObject(getDisconnectRequest());
            if (log.isDebugEnabled()) {
                log.debug("wait half a sec before closing connection");
            }
            Thread.sleep(500);
            p.close();
        } catch (IOException ex) {
            log.error("IO Exception caught.", ex);
            p.close();
            throw ex;
        } catch (Throwable t) {
            log.error("Unexpected Exception caught.", t);
            p.close();
            throw new UndeclaredThrowableException(t);
        }
        if (isDhcpServer) {
            return responseTime;
        } else {
            return -1;
        }
    }

    private static void setHwAddress(String hwAddressStr) {
        Category log = ThreadCategory.getInstance(Poller.class);
        s_hwAddress = DEFAULT_MAC_ADDRESS;
        StringTokenizer token = new StringTokenizer(hwAddressStr, ":");
        if (token.countTokens() != 6) {
            if (log.isDebugEnabled()) {
                log.debug("Invalid format for hwAddress " + hwAddressStr);
            }
        }
        int temp;
        int i = 0;
        while (i < 6) {
            try {
                temp = Integer.parseInt(token.nextToken(), 16);
                s_hwAddress[i] = (byte) temp;
                i++;
            } catch (NumberFormatException ex) {
                if (log.isDebugEnabled()) {
                    log.debug("Invalid format for hwAddress, " + ex);
                }
            }
        }
    }

    private static byte[] setIpAddress(String ipAddressStr) {
        byte[] ipAddress = new byte[4];
        StringTokenizer token = new StringTokenizer(ipAddressStr, ".");
        int temp;
        int i = 0;
        while (i < 4) {
            temp = Integer.parseInt(token.nextToken(), 10);
            ipAddress[i] = (byte) temp;
            i++;
        }
        return ipAddress;
    }
}

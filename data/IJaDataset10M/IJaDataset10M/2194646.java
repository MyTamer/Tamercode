package org.opennms.netmgt.icmp.jni6;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet6Address;
import java.net.NoRouteToHostException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.icmp.EchoPacket;
import org.opennms.netmgt.icmp.HostIsDownException;
import org.opennms.netmgt.icmp.PingResponseCallback;
import org.opennms.protocols.icmp6.ICMPv6EchoRequest;
import org.opennms.protocols.icmp6.ICMPv6Packet.Type;
import org.opennms.protocols.icmp6.ICMPv6Socket;
import org.opennms.protocols.rt.Request;

/**
 * This class is used to encapsulate a ping request. A request consist of
 * the pingable address and a signaled state.
 *
 * @author <a href="mailto:ranger@opennms.org">Ben Reed</a>
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public class Jni6PingRequest implements Request<Jni6PingRequestId, Jni6PingRequest, Jni6PingResponse>, EchoPacket {

    private static long s_nextTid = 1;

    public static final synchronized long getNextTID() {
        return s_nextTid++;
    }

    /**
     * The id representing the packet
     */
    private final Jni6PingRequestId m_id;

    /**
     * the request packet
     */
    private ICMPv6EchoRequest m_requestPacket = null;

    /**
     * The callback to use when this object is ready to do something
     */
    private final PingResponseCallback m_callback;

    /**
     * How many retries
     */
    private final int m_retries;

    /**
     * how long to wait for a response
     */
    private final long m_timeout;

    /**
     * The ICMP packet size
     */
    private final int m_packetsize;

    /**
     * The expiration time of this request
     */
    private long m_expiration = -1L;

    /**
     * The thread logger associated with this request.
     */
    private final ThreadCategory m_log;

    private final AtomicBoolean m_processed = new AtomicBoolean(false);

    public Jni6PingRequest(Jni6PingRequestId id, long timeout, int retries, int packetsize, ThreadCategory log, PingResponseCallback callback) {
        m_id = id;
        m_timeout = timeout;
        m_retries = retries;
        m_packetsize = packetsize;
        m_log = log;
        m_callback = callback;
    }

    public Jni6PingRequest(Inet6Address addr, int identifier, int sequenceNumber, long threadId, long timeout, int retries, int packetsize, ThreadCategory logger, PingResponseCallback cb) {
        this(new Jni6PingRequestId(addr, identifier, sequenceNumber, threadId), timeout, retries, packetsize, logger, cb);
    }

    public Jni6PingRequest(Inet6Address addr, int identifier, int sequenceNumber, long threadId, long timeout, int retries, int packetsize, PingResponseCallback cb) {
        this(addr, identifier, sequenceNumber, threadId, timeout, retries, packetsize, ThreadCategory.getInstance(Jni6PingRequest.class), cb);
    }

    public Jni6PingRequest(Inet6Address addr, int identifier, int sequenceNumber, long timeout, int retries, int packetsize, PingResponseCallback cb) {
        this(addr, identifier, sequenceNumber, getNextTID(), timeout, retries, packetsize, cb);
    }

    /**
     * <p>processResponse</p>
     *
     * @param reply a {@link org.opennms.netmgt.icmp.Jni6PingResponse.JniPingResponse.PingReply} object.
     * @return a boolean.
     */
    public boolean processResponse(Jni6PingResponse reply) {
        try {
            m_log.debug(System.currentTimeMillis() + ": Ping Response Received " + this);
            m_callback.handleResponse(m_id.getAddress(), reply);
        } finally {
            setProcessed(true);
        }
        return true;
    }

    /**
     * <p>processTimeout</p>
     *
     * @return a {@link org.opennms.netmgt.icmp.jni6.Jni6PingRequest} object.
     */
    public Jni6PingRequest processTimeout() {
        try {
            Jni6PingRequest returnval = null;
            if (this.isExpired()) {
                if (m_retries > 0) {
                    returnval = new Jni6PingRequest(m_id, m_timeout, (m_retries - 1), m_packetsize, m_log, m_callback);
                    m_log.debug(System.currentTimeMillis() + ": Retrying Ping Request " + returnval);
                } else {
                    m_log.debug(System.currentTimeMillis() + ": Ping Request Timed out " + this);
                    m_callback.handleTimeout(m_id.getAddress(), this);
                }
            }
            return returnval;
        } finally {
            setProcessed(true);
        }
    }

    /**
     * <p>isExpired</p>
     *
     * @return a boolean.
     */
    public boolean isExpired() {
        return (System.currentTimeMillis() >= m_expiration);
    }

    /**
     * <p>toString</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        sb.append("ID=").append(m_id).append(',');
        sb.append("Retries=").append(m_retries).append(",");
        sb.append("Timeout=").append(m_timeout).append(",");
        sb.append("Packet-Size=").append(m_packetsize).append(",");
        sb.append("Expiration=").append(m_expiration).append(',');
        sb.append("Callback=").append(m_callback);
        sb.append("]");
        return sb.toString();
    }

    /** {@inheritDoc} */
    public long getDelay(TimeUnit unit) {
        return unit.convert(m_expiration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * <p>compareTo</p>
     *
     * @param request a {@link java.util.concurrent.Delayed} object.
     * @return a int.
     */
    public int compareTo(Delayed request) {
        long myDelay = getDelay(TimeUnit.MILLISECONDS);
        long otherDelay = request.getDelay(TimeUnit.MILLISECONDS);
        if (myDelay < otherDelay) return -1;
        if (myDelay == otherDelay) return 0;
        return 1;
    }

    /**
     * <p>getId</p>
     *
     * @return a {@link org.opennms.netmgt.icmp.Jni6PingRequestId.JniPingRequestId.PingRequestId} object.
     */
    public Jni6PingRequestId getId() {
        return m_id;
    }

    public void processError(Throwable t) {
        try {
            m_callback.handleError(m_id.getAddress(), this, t);
        } finally {
            setProcessed(true);
        }
    }

    private void setProcessed(boolean processed) {
        m_processed.set(processed);
    }

    /**
     * <p>isProcessed</p>
     *
     * @return a boolean.
     */
    public boolean isProcessed() {
        return m_processed.get();
    }

    /**
     * Send this JniPingRequest through the given icmpSocket
     *
     * @param socket a {@link org.opennms.protocols.icmp.IcmpSocket} object.
     */
    public void send(ICMPv6Socket socket) {
        try {
            m_requestPacket = createRequestPacket();
            m_log.debug(System.currentTimeMillis() + ": Sending Ping Request: " + this);
            byte[] data = m_requestPacket.toBytes();
            m_expiration = System.currentTimeMillis() + m_timeout;
            send(socket, new DatagramPacket(data, data.length, m_id.getAddress(), 0));
        } catch (Throwable t) {
            m_callback.handleError(m_id.getAddress(), this, t);
        }
    }

    private void send(ICMPv6Socket socket, DatagramPacket packet) throws IOException {
        try {
            socket.send(packet);
        } catch (IOException e) {
            if (e.getMessage().matches("sendto error \\(65, .*\\)")) {
                throw new NoRouteToHostException("No Route to Host " + m_id.getAddress() + ": " + e.getMessage());
            } else if (e.getMessage().matches("sendto error \\(64, .*\\)")) {
                throw new HostIsDownException("Host " + m_id.getAddress() + " is down: " + e.getMessage());
            } else {
                throw e;
            }
        }
    }

    private ICMPv6EchoRequest getRequestPacket() {
        return m_requestPacket;
    }

    private ICMPv6EchoRequest createRequestPacket() {
        return new ICMPv6EchoRequest(m_id.getIdentifier(), m_id.getSequenceNumber(), m_id.getThreadId(), m_packetsize);
    }

    @Override
    public boolean isEchoReply() {
        return getRequestPacket().getType() == Type.EchoReply;
    }

    @Override
    public int getIdentifier() {
        return getRequestPacket().getIdentifier();
    }

    @Override
    public int getSequenceNumber() {
        return getRequestPacket().getSequenceNumber();
    }

    @Override
    public long getThreadId() {
        return getRequestPacket().getThreadId();
    }

    @Override
    public long getReceivedTimeNanos() {
        return getRequestPacket().getReceiveTime() * 1000000;
    }

    @Override
    public long getSentTimeNanos() {
        return getRequestPacket().getSentTime() * 1000000;
    }

    @Override
    public double elapsedTime(TimeUnit timeUnit) {
        double nanosPerUnit = TimeUnit.NANOSECONDS.convert(1, timeUnit);
        return (getRequestPacket().getRoundTripTime() * 1000) / nanosPerUnit;
    }
}

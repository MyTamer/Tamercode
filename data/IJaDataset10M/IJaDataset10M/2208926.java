package jade.imtp.leap.nio;

import jade.imtp.leap.ICPException;
import jade.imtp.leap.SSLHelper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

/**
 * helper class that holds the ByteBuffers, SSLEngine and other Objects that deal with the non static part of the
 * ssl/nio handshaking/input/output. The contained SSLEngine is hidden to apps, because this helper takes on the responsibility
 * of dealing with concurrency issues
 * 
 * @author eduard
 */
public final class SSLEngineHelper implements BufferTransformer {

    public static final ByteBuffer EMPTY_BUFFER = NIOHelper.EMPTY_BUFFER;

    /**
	 * todo why 5k?? configurable?
	 */
    public static final int INCREASE_SIZE = 5120;

    private SSLEngine ssle = null;

    private ByteBuffer wrapData;

    private ByteBuffer unwrapData;

    private ByteBuffer sendData;

    private NIOJICPConnection connection = null;

    private static Logger log = Logger.getLogger(SSLEngineHelper.class.getName());

    /**
	 * Creates and initializes ByteBuffers and SSLEngine necessary for ssl/nio.
	 * @see NIOHelper
	 * @see SSLHelper
	 * @param host provides a hint for optimization
	 * @param port provides a hint for optimization
	 * @param connection the connection that will use this helper
	 * @throws ICPException
	 */
    public SSLEngineHelper(String host, int port, NIOJICPConnection connection) throws ICPException {
        SSLContext context = SSLHelper.createContext();
        ssle = context.createSSLEngine(host, port);
        ssle.setUseClientMode(false);
        ssle.setNeedClientAuth(SSLHelper.needAuth());
        if (!SSLHelper.needAuth()) {
            ssle.setEnabledCipherSuites(SSLHelper.getSupportedKeys());
        }
        SSLSession session = ssle.getSession();
        unwrapData = ByteBuffer.allocateDirect(session.getApplicationBufferSize() + 1500);
        wrapData = ByteBuffer.allocateDirect(session.getPacketBufferSize());
        sendData = ByteBuffer.allocateDirect(wrapData.capacity());
        ;
        this.connection = connection;
    }

    /**
	 * executes a wrap on the SSLEngine, prevents threads from calling this method concurrently
	 * @param source
	 * @return
	 * @throws SSLException
	 */
    private SSLEngineResult encode(ByteBuffer source) throws SSLException {
        return ssle.wrap(source, wrapData);
    }

    /**
	 * runs all background handshaking tasks, blocks until these are finished
	 * @return the new handshake status after finishing the background tasks
	 */
    private SSLEngineResult.HandshakeStatus runHandshakeTasks() {
        Runnable task = null;
        while ((task = ssle.getDelegatedTask()) != null) {
            task.run();
        }
        return ssle.getHandshakeStatus();
    }

    /**
	 * closes the SSLEngine, tries to send a ssl close message
	 * @throws IOException
	 */
    public void close() throws IOException {
        ssle.closeOutbound();
        sendSSLClose();
        ssle.closeInbound();
    }

    private void sendSSLClose() {
        try {
            while (!ssle.isOutboundDone()) {
                wrapAndSend();
            }
        } catch (IOException e) {
            log.log(Level.FINE, "unable to send ssl close packet", e);
        }
    }

    private final int writeToChannel(ByteBuffer b) throws IOException {
        int m = b.remaining();
        int n = connection.writeToChannel(b);
        if (n != m) {
            throw new IOException("should write " + m + ", written " + n);
        }
        return n;
    }

    private String getRemoteHost() {
        return connection.getRemoteHost();
    }

    private SSLEngineResult unwrapData(ByteBuffer socketData) throws SSLException {
        SSLEngineResult result = null;
        do {
            try {
                result = ssle.unwrap(socketData, unwrapData);
                if (log.isLoggable(Level.FINE)) log.fine("Decoded " + result.bytesConsumed() + " bytes; Produced " + result.bytesProduced() + " application-data bytes [" + getRemoteHost() + "]");
            } catch (SSLException e) {
                log.log(Level.WARNING, "Unwrap failure [" + getRemoteHost() + "]", e);
                try {
                    close();
                } catch (IOException ex) {
                }
                throw e;
            }
        } while (result.getStatus().equals(Status.OK) && result.getHandshakeStatus().equals(HandshakeStatus.NEED_UNWRAP));
        return result;
    }

    private void checkStatusAfterHandshakeTasks(SSLEngineResult.HandshakeStatus handshakeStatus) throws SSLException, IOException {
        if (handshakeStatus.equals(HandshakeStatus.FINISHED)) {
            log.warning("Unexpected FINISHED SSL handshake status after execution of handshake tasks [" + getRemoteHost() + "]");
        } else if (handshakeStatus.equals(HandshakeStatus.NEED_TASK)) {
            log.warning("Unexpected NEED_TASK SSL handshake status after execution of handshake tasks [" + getRemoteHost() + "]");
        } else if (handshakeStatus.equals(HandshakeStatus.NEED_UNWRAP)) {
            if (log.isLoggable(Level.FINE)) log.fine("Need more data to proceed with Handshake [" + getRemoteHost() + "]");
        } else if (handshakeStatus.equals(HandshakeStatus.NEED_WRAP)) {
            if (log.isLoggable(Level.FINE)) log.fine("Send back Handshake data after task execution [" + getRemoteHost() + "]");
            wrapAndSend();
        } else if (handshakeStatus.equals(HandshakeStatus.NOT_HANDSHAKING)) {
            log.warning("Unexpected NOT_HANDSHAKING SSL handshake status after execution of handshake tasks [" + getRemoteHost() + "]");
        }
    }

    /**
     * @return the number of bytes available in the unwrapBuffer
     * @throws IOException
     */
    private synchronized int decrypt(ByteBuffer socketData) throws SSLException, IOException {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Decrypt incoming data: remaining = " + socketData.remaining() + ", position = " + socketData.position() + ", limit = " + socketData.limit() + " [" + getRemoteHost() + "]");
        }
        SSLEngineResult result = unwrapData(socketData);
        if (log.isLoggable(Level.FINE)) {
            log.fine("Checking handshake result [" + getRemoteHost() + "]");
        }
        int n = 0;
        SSLEngineResult.Status status = result.getStatus();
        SSLEngineResult.HandshakeStatus handshakeStatus = result.getHandshakeStatus();
        boolean recurse = true;
        if (status.equals(Status.OK)) {
            if (handshakeStatus.equals(HandshakeStatus.NOT_HANDSHAKING)) {
                n = result.bytesProduced();
            } else if (handshakeStatus.equals(HandshakeStatus.FINISHED)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Handshake finished [" + getRemoteHost() + "]");
                }
            } else if (handshakeStatus.equals(HandshakeStatus.NEED_TASK)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Activate Handshake task [" + getRemoteHost() + "]");
                }
                handshakeStatus = runHandshakeTasks();
                checkStatusAfterHandshakeTasks(handshakeStatus);
            } else if (handshakeStatus.equals(HandshakeStatus.NEED_UNWRAP)) {
                throw new SSLException("Unexpected NEED_UNWRAP SSL handshake status! [" + getRemoteHost() + "]");
            } else if (handshakeStatus.equals(HandshakeStatus.NEED_WRAP)) {
                if (log.isLoggable(Level.FINE)) {
                    log.fine("Send back Handshake data [" + getRemoteHost() + "]");
                }
                wrapAndSend();
            }
        } else if (status.equals(Status.CLOSED)) {
            if (log.isLoggable(Level.FINE)) {
                log.fine(" sslengine closed [" + getRemoteHost() + "]");
            }
            recurse = false;
            close();
        } else if (status.equals(Status.BUFFER_UNDERFLOW)) {
            if (log.isLoggable(Level.FINE)) {
                log.fine("Not enough data to decode a meaningful SSL block. " + socketData.remaining() + " unprocessed bytes. [" + getRemoteHost() + "]");
            }
            return n;
        } else if (status.equals(Status.BUFFER_OVERFLOW)) {
            if (log.isLoggable(Level.FINE)) {
                NIOHelper.logBuffer(socketData, "socketData");
                NIOHelper.logBuffer(unwrapData, "overflow unwrapData");
                log.fine("enlarging unwrap buffer with" + INCREASE_SIZE);
            }
            log.info("Buffer overflow. Enlarge buffer and retry [" + getRemoteHost() + "]");
            unwrapData.flip();
            unwrapData = NIOHelper.enlargeAndFillBuffer(unwrapData, INCREASE_SIZE);
            return decrypt(socketData);
        }
        if (socketData.hasRemaining() && recurse) {
            n += decrypt(socketData);
        }
        return n;
    }

    /**
	 * generates handshake data and calls {@link NIOJICPConnection#writeToChannel(java.nio.ByteBuffer) }, possibly
	 * recursive when required by handshaking
	 * @return the amount of bytes written to the connection
	 * @throws SSLException
	 * @throws IOException
	 */
    private int wrapAndSend() throws SSLException, IOException {
        wrapData.clear();
        int n = 0;
        SSLEngineResult result = encode(EMPTY_BUFFER);
        if (log.isLoggable(Level.FINE)) {
            log.fine("wrapped " + result);
        }
        if (result.bytesProduced() > 0) {
            wrapData.flip();
            n = writeToChannel(wrapData);
            if (result.getHandshakeStatus().equals(HandshakeStatus.NEED_WRAP)) {
                n += wrapAndSend();
            }
            return n;
        } else {
            log.warning("wrap produced no data " + getRemoteHost());
        }
        return n;
    }

    /**
	 * encrypt application data, does not write data to socket. After this method call the data to be
	 * send can be retrieved through {@link SSLEngineHelper#getWrapData() }, the data will be ready for usage.
	 * @param b the appData to wrap
	 * @return the status object for the wrap or null
	 * @throws SSLException
	 * @throws IOException
	 */
    private SSLEngineResult wrapAppData(ByteBuffer b) throws SSLException, IOException {
        wrapData.clear();
        SSLEngineResult result = encode(b);
        if (log.isLoggable(Level.FINE)) {
            log.fine("wrapped " + result);
        }
        if (result.bytesProduced() > 0) {
            wrapData.flip();
            return result;
        } else {
            throw new IOException("wrap produced no data " + getRemoteHost());
        }
    }

    public synchronized ByteBuffer preprocessBufferToWrite(ByteBuffer dataToSend) throws IOException {
        sendData.clear();
        while (dataToSend.hasRemaining()) {
            SSLEngineResult res = wrapAppData(dataToSend);
            if (wrapData.remaining() > sendData.remaining()) {
                int extra = wrapData.remaining() - sendData.remaining();
                sendData.flip();
                sendData = NIOHelper.enlargeAndFillBuffer(sendData, extra);
            }
            NIOHelper.copyAsMuchAsFits(sendData, wrapData);
        }
        sendData.flip();
        return sendData;
    }

    public synchronized ByteBuffer postprocessBufferRead(ByteBuffer socketData) throws PacketIncompleteException, IOException {
        unwrapData.clear();
        int n = decrypt(socketData);
        if (n > 0) {
            unwrapData.flip();
            return unwrapData;
        } else {
            return EMPTY_BUFFER;
        }
    }

    public boolean needSocketData() {
        return false;
    }
}

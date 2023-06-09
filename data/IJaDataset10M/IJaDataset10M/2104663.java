package net.infordata.em.tnprot;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles a telnet protocol connection.
 * A telnet emulator must implement XITelnetEmulator interface to receive notification
 * about the telnet connection.
 * Supports RFC885 End of record. 
 *
 * @see    XITelnetEmulator
 *
 * @author   Valentino Proietti - Infordata S.p.A.
 */
public class XITelnet {

    private static final Logger LOGGER = Logger.getLogger(XITelnet.class.getName());

    /**
   * Telnet options or flags.
   */
    public static final byte TELOPT_BINARY = 0;

    public static final byte TELOPT_ECHO = 1;

    public static final byte TELOPT_RCP = 2;

    public static final byte TELOPT_SGA = 3;

    public static final byte TELOPT_NAMS = 4;

    public static final byte TELOPT_STATUS = 5;

    public static final byte TELOPT_TM = 6;

    public static final byte TELOPT_RCTE = 7;

    public static final byte TELOPT_NAOL = 8;

    public static final byte TELOPT_NAOP = 9;

    public static final byte TELOPT_NAOCRD = 10;

    public static final byte TELOPT_NAOHTS = 11;

    public static final byte TELOPT_NAOHTD = 12;

    public static final byte TELOPT_NAOFFD = 13;

    public static final byte TELOPT_NAOVTS = 14;

    public static final byte TELOPT_NAOVTD = 15;

    public static final byte TELOPT_NAOLFD = 16;

    public static final byte TELOPT_XASCII = 17;

    public static final byte TELOPT_LOGOUT = 18;

    public static final byte TELOPT_BM = 19;

    public static final byte TELOPT_DET = 20;

    public static final byte TELOPT_SUPDUP = 21;

    public static final byte TELOPT_SUPDUPOUTPUT = 22;

    public static final byte TELOPT_SNDLOC = 23;

    public static final byte TELOPT_TTYPE = 24;

    public static final byte TELOPT_EOR = 25;

    public static final byte TELOPT_TUID = 26;

    public static final byte TELOPT_OUTMRK = 27;

    public static final byte TELOPT_TTYLOC = 28;

    public static final byte TELOPT_3270REGIME = 29;

    public static final byte TELOPT_X3PAD = 30;

    public static final byte TELOPT_NAWS = 31;

    public static final byte TELOPT_TSPEED = 32;

    public static final byte TELOPT_LFLOW = 33;

    public static final byte TELOPT_LINEMODE = 34;

    public static final byte TELOPT_XDISPLOC = 35;

    public static final byte TELOPT_OLD_ENVIRON = 36;

    public static final byte TELOPT_AUTHENTICATION = 37;

    public static final byte TELOPT_ENCRYPT = 38;

    public static final byte TELOPT_NEW_ENVIRON = 39;

    public static final String[] TELOPT = { "BINARY", "ECHO", "RCP", "SUPPRESS GO AHEAD", "NAME", "STATUS", "TIMING MARK", "RCTE", "NAOL", "NAOP", "NAOCRD", "NAOHTS", "NAOHTD", "NAOFFD", "NAOVTS", "NAOVTD", "NAOLFD", "EXTEND ASCII", "LOGOUT", "BYTE MACRO", "DATA ENTRY TERMINAL", "SUPDUP", "SUPDUP OUTPUT", "SEND LOCATION", "TERMINAL TYPE", "END OF RECORD", "TACACS UID", "OUTPUT MARKING", "TTYLOC", "3270 REGIME", "X.3 PAD", "NAWS", "TSPEED", "LFLOW", "LINEMODE", "XDISPLOC", "OLD-ENVIRON", "AUTHENTICATION", "ENCRYPT", "NEW-ENVIRON", "<UNKNOWN>" };

    /**
   * Telnet escapes
   */
    static final byte IAC = (byte) 0xFF;

    static final byte DONT = (byte) 0xFE;

    static final byte DO = (byte) 0xFD;

    static final byte WONT = (byte) 0xFC;

    static final byte WILL = (byte) 0xFB;

    static final byte SB = (byte) 0xFA;

    static final byte SE = (byte) 0xF0;

    static final byte EOR = (byte) 0xEF;

    static final String[] TELCMD = { "IAC", "DONT", "DO", "WONT", "WILL", "SB", "GA", "EL", "EC", "AYT", "AO", "IP", "BRK", "DATA MARK", "NOP", "SE", "EOR" };

    static final byte SEND = (byte) 0x01;

    static final byte IS = (byte) 0x00;

    static final int SIAC_START = 0;

    static final int SIAC_WCMD = 1;

    static final int SIAC_WOPT = 2;

    static final int SIAC_WSTR = 3;

    private String ivHost;

    private int ivPort;

    /**
   * if null then the connection is closed
   */
    private transient Socket ivSocket;

    private transient InputStream ivIn;

    private transient OutputStream ivOut;

    private transient RxThread ivReadTh;

    private transient byte ivIACCmd;

    private transient byte ivIACOpt;

    private transient String ivIACStr;

    private transient boolean[] ivLocalFlags = new boolean[128];

    private transient boolean[] ivRemoteFlags = new boolean[128];

    private boolean[] ivLocalReqFlags = new boolean[128];

    private boolean[] ivRemoteReqFlags = new boolean[128];

    private String ivTermType;

    private String ivEnvironment;

    private transient int ivIACParserStatus = SIAC_START;

    private XITelnetEmulator ivEmulator;

    private transient String ivFirstHost;

    private transient String ivSecondHost;

    private transient boolean ivUsed = false;

    /**
   * Converts byte to int without sign
   */
    public static final int toInt(byte bb) {
        return ((int) bb & 0xff);
    }

    /**
   * Converts byte to hexadecimal string rappresentation
   */
    public static final String toHex(byte bb) {
        String hex = Integer.toString(toInt(bb), 16);
        return "00".substring(hex.length()) + hex;
    }

    public static final String toHex(byte[] buf, int len) {
        StringBuilder sb = new StringBuilder(len * 4);
        for (int i = 0; i < len; i++) {
            sb.append(toHex(buf[i])).append(' ');
        }
        return sb.toString();
    }

    public static final String toHex(byte[] buf) {
        return toHex(buf, buf.length);
    }

    /**
   * Uses telnet default port for socket connection.
   */
    public XITelnet(String aHost) {
        this(aHost, 23);
    }

    /**
   * Uses the given port for socket connection.
   */
    public XITelnet(String aHost, int aPort) {
        if (aHost == null) throw new IllegalArgumentException("Host cannot be null");
        ivHost = aHost;
        ivPort = aPort;
        try {
            StringTokenizer st = new StringTokenizer(ivHost, "#");
            ivFirstHost = st.nextToken();
            ivSecondHost = st.nextToken();
        } catch (NoSuchElementException ex) {
        }
    }

    /**
   * Returns the host-name or ip address.
   */
    public String getHost() {
        return ivHost;
    }

    /**
   * Returns the telnet port.
   */
    public int getPort() {
        return ivPort;
    }

    /**
   * Sets the receiving notifications XITelnetEmulator instance.
   */
    public void setEmulator(XITelnetEmulator aEmulator) {
        ivEmulator = aEmulator;
    }

    /**
   */
    public boolean isConnected() {
        return (ivSocket != null);
    }

    /**
   * Sets the telnet terminal type option.
   * Must be used before that a connection is established.
   */
    public void setTerminalType(String aTerminalType) {
        if (isConnected()) throw new IllegalArgumentException("Telnet already connected");
        setLocalReqFlag(TELOPT_TTYPE, true);
        ivTermType = aTerminalType;
    }

    /**
   */
    public String getTerminalType() {
        return ivTermType;
    }

    /**
   * Sets the telnet environment option.
   * Must be used before that a connection is established.
   */
    public void setEnvironment(String aEnv) {
        if (isConnected()) throw new IllegalArgumentException("Telnet already connected");
        setLocalReqFlag(TELOPT_NEW_ENVIRON, true);
        ivEnvironment = aEnv;
    }

    /**
   */
    public String getEnvironment() {
        return ivEnvironment;
    }

    /**
   * Sets the local requested flags.
   * Must be used before that a connection is established.
   *
   * @param    flag  use a TELOPT_ constant.
   * @param    b     to be requested ?
   */
    public void setLocalReqFlag(byte flag, boolean b) {
        if (isConnected()) throw new IllegalArgumentException("Telnet already connected");
        ivLocalReqFlags[flag] = b;
    }

    /**
   * Sets the remote requested flags.
   * Must be used before that a connection is established.
   *
   * @param    flag  use a TELOPT_ constant.
   * @param    b     to be requested ?
   */
    public void setRemoteReqFlag(byte flag, boolean b) {
        if (isConnected()) throw new IllegalArgumentException("Telnet already connected");
        ivRemoteReqFlags[flag] = b;
    }

    /**
   * Can be used to query a local flag status.
   */
    public boolean isLocalFlagON(byte flag) {
        return ivLocalFlags[flag];
    }

    /**
   * Can be used to query a remote flag status.
   */
    public boolean isRemoteFlagON(byte flag) {
        return ivRemoteFlags[flag];
    }

    /**
   * Tryes to establish a telnet connection.
   * If a connection is already established then a call to disconnect() is maded.
   */
    public synchronized void connect() {
        if (ivUsed) throw new IllegalArgumentException("XITelnet cannot be recycled");
        disconnect();
        connecting();
        try {
            ivSocket = new Socket(ivFirstHost, ivPort);
            ivIn = ivSocket.getInputStream();
            ivOut = ivSocket.getOutputStream();
            ivReadTh = new RxThread();
            ivReadTh.start();
            ivUsed = true;
            connected();
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   */
    private void closeSocket() {
        if (ivSocket != null) {
            try {
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("closing...");
                ivSocket.close();
            } catch (IOException ex) {
            }
            ivSocket = null;
            ivIn = null;
            ivOut = null;
            disconnected();
        }
    }

    /**
   * Closes the telnet connection.
   */
    public synchronized void disconnect() {
        if (ivReadTh != null) {
            ivReadTh.terminate();
            ivReadTh = null;
        }
        closeSocket();
    }

    /**
   * Telnet IAC parser.
   */
    protected int processIAC(byte bb) throws IOException {
        int res = 1;
        switch(ivIACParserStatus) {
            case SIAC_START:
                switch(bb) {
                    case IAC:
                        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("IAC");
                        ivIACParserStatus = SIAC_WCMD;
                        res = 0;
                        break;
                }
                break;
            case SIAC_WCMD:
                if (LOGGER.isLoggable(Level.FINE)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" r " + bb + " ");
                    try {
                        sb.append(TELCMD[-(bb + 1)] + " ");
                    } catch (Exception ex) {
                    }
                    LOGGER.fine(sb.toString());
                }
                switch(bb) {
                    case IAC:
                        ivIACParserStatus = SIAC_START;
                        break;
                    case EOR:
                        ivIACParserStatus = SIAC_START;
                        res = 0;
                        if (ivLocalFlags[TELOPT_EOR]) receivedEOR();
                        break;
                    case WILL:
                    case WONT:
                    case DO:
                    case DONT:
                        ivIACCmd = bb;
                        ivIACParserStatus = SIAC_WOPT;
                        res = 0;
                        break;
                    case SB:
                        ivIACStr = "";
                        ivIACCmd = bb;
                        ivIACParserStatus = SIAC_WOPT;
                        res = 0;
                        break;
                    case SE:
                        ivIACParserStatus = SIAC_START;
                        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("SE " + TELOPT[ivIACOpt]);
                        res = 0;
                        if (ivLocalFlags[ivIACOpt]) {
                            switch(ivIACOpt) {
                                case TELOPT_TTYPE:
                                    sendIACStr(SB, TELOPT_TTYPE, true, ivTermType);
                                    break;
                                case TELOPT_NEW_ENVIRON:
                                    sendIACStr(SB, TELOPT_NEW_ENVIRON, true, ivEnvironment);
                                    break;
                                default:
                                    unhandledRequest(ivIACOpt, ivIACStr);
                                    break;
                            }
                        }
                        break;
                    default:
                        ivIACParserStatus = SIAC_START;
                        res = 0;
                        break;
                }
                break;
            case SIAC_WOPT:
                ivIACOpt = bb;
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(TELOPT[ivIACOpt]);
                res = 0;
                switch(ivIACCmd) {
                    case SB:
                        break;
                    case DONT:
                        if (ivLocalFlags[ivIACOpt]) {
                            ivLocalFlags[ivIACOpt] = false;
                            sendIACCmd(WONT, ivIACOpt);
                            localFlagsChanged(ivIACOpt);
                        }
                        break;
                    case DO:
                        if (ivLocalReqFlags[ivIACOpt]) {
                            if (!ivLocalFlags[ivIACOpt]) {
                                ivLocalFlags[ivIACOpt] = true;
                                sendIACCmd(WILL, ivIACOpt);
                                localFlagsChanged(ivIACOpt);
                            }
                        } else sendIACCmd(WONT, ivIACOpt);
                        break;
                    case WONT:
                        if (ivRemoteFlags[ivIACOpt]) {
                            ivRemoteFlags[ivIACOpt] = false;
                            sendIACCmd(DONT, ivIACOpt);
                            remoteFlagsChanged(ivIACOpt);
                        }
                        break;
                    case WILL:
                        if (ivRemoteReqFlags[ivIACOpt]) {
                            if (!ivRemoteFlags[ivIACOpt]) {
                                ivRemoteFlags[ivIACOpt] = true;
                                sendIACCmd(DO, ivIACOpt);
                                remoteFlagsChanged(ivIACOpt);
                            }
                        } else sendIACCmd(DONT, ivIACOpt);
                        break;
                }
                if (ivIACCmd != SB) ivIACParserStatus = SIAC_START; else ivIACParserStatus = SIAC_WSTR;
                break;
            case SIAC_WSTR:
                res = 0;
                switch(bb) {
                    case IAC:
                        ivIACParserStatus = SIAC_WCMD;
                        break;
                    default:
                        ivIACStr += (char) bb;
                        break;
                }
                break;
        }
        return res;
    }

    /**
   * Sends an telnet EOR sequence.
   */
    public void sendEOR() throws IOException {
        byte[] buf = { IAC, EOR };
        try {
            ivOut.write(buf);
            ivOut.flush();
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   * Sends a telnet IAC sequence.
   */
    public void sendIACCmd(byte aCmd, byte aOpt) {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(" t " + aCmd + " " + TELCMD[-(aCmd + 1)] + " " + TELOPT[aOpt]);
        byte[] buf = { IAC, aCmd, aOpt };
        try {
            ivOut.write(buf);
            ivOut.flush();
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   * Sends a telnet IAC sequence with a string argument.
   */
    public void sendIACStr(byte aCmd, byte aOpt, boolean sendIS, String aString) {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("t " + aCmd + " " + TELCMD[-(aCmd + 1)] + " " + TELOPT[aOpt] + " " + aString);
        byte[] endBuf = { IAC, SE };
        byte[] startBuf = sendIS ? new byte[] { IAC, aCmd, aOpt, IS } : new byte[] { IAC, aCmd, aOpt };
        try {
            ivOut.write(startBuf);
            ivOut.write(aString.getBytes());
            ivOut.write(endBuf);
            ivOut.flush();
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   * Sends a data buffer (IAC bytes are doubled).
   */
    public void send(byte[] aBuf, int aLen) {
        try {
            for (int i = 0; i < aLen; i++) {
                ivOut.write(aBuf[i]);
                if (aBuf[i] == IAC) ivOut.write(IAC);
            }
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   * Sends a data buffer (IAC bytes are doubled).
   */
    public void send(byte[] aBuf) {
        send(aBuf, aBuf.length);
    }

    /**
   * Flushes output buffer.
   */
    public void flush() {
        try {
            ivOut.flush();
        } catch (IOException ex) {
            catchedIOException(ex);
        }
    }

    /**
   * Called just before trying to connect.
   */
    protected void connecting() {
        if (ivEmulator != null) ivEmulator.connecting();
    }

    /**
   * Called after that a connection is established.
   */
    protected void connected() {
        if (ivSecondHost != null && !ivSecondHost.equals("")) send((new String(ivSecondHost + "\n")).getBytes());
        if (ivEmulator != null) ivEmulator.connected();
    }

    /**
   * Called after that the connection is closed.
   */
    protected void disconnected() {
        if (ivEmulator != null) ivEmulator.disconnected();
    }

    /**
   * Called when an IOException is catched.
   */
    protected synchronized void catchedIOException(IOException ex) {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, "", ex);
        try {
            if (ivEmulator != null) ivEmulator.catchedIOException(ex);
        } finally {
            disconnect();
        }
    }

    /**
   * Called when an unhandled IAC request is received.
   */
    protected void unhandledRequest(byte aIACOpt, String aIACStr) {
        if (ivEmulator != null) ivEmulator.unhandledRequest(aIACOpt, aIACStr);
    }

    /**
   * Called when a local flags has been changed.
   */
    protected void localFlagsChanged(byte aIACOpt) {
        if (ivEmulator != null) ivEmulator.localFlagsChanged(aIACOpt);
    }

    /**
   * Called when a remote flags has been changed.
   */
    protected void remoteFlagsChanged(byte aIACOpt) {
        if (ivEmulator != null) ivEmulator.remoteFlagsChanged(aIACOpt);
    }

    /**
   * Called when data are received.
   * Data are already cleared from IAC sequence.
   * NOTE: receivedData is always called in the receiving thread.
   */
    protected void receivedData(byte[] buf, int len) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(toHex(buf, len));
        }
        if (ivEmulator != null) ivEmulator.receivedData(buf, len);
    }

    /**
   * Called when a telnet EOR sequence is received.
   * NOTE: receivedEOR is always called in the receiving thread.
   */
    protected void receivedEOR() {
        if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("EOR");
        if (ivEmulator != null) ivEmulator.receivedEOR();
    }

    /**
   */
    @Override
    protected void finalize() throws Throwable {
        disconnect();
        super.finalize();
    }

    /**
   * Used only for test purposes.
   */
    public static void main(String[] argv) {
        XITelnet tn = new XITelnet("192.168.0.1#192.168.0.4");
        tn.setTerminalType("IBM-3477-FC");
        tn.setLocalReqFlag(TELOPT_BINARY, true);
        tn.setLocalReqFlag(TELOPT_TTYPE, true);
        tn.setLocalReqFlag(TELOPT_EOR, true);
        tn.setRemoteReqFlag(TELOPT_BINARY, true);
        tn.setRemoteReqFlag(TELOPT_EOR, true);
        try {
            tn.connect();
            Thread.sleep(10000);
            tn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
   */
    class RxThread extends Thread {

        private boolean ivTerminate = false;

        /**
     */
        public RxThread() {
            super("XITelnet rx thread");
        }

        /**
     */
        public void terminate() {
            ivTerminate = true;
            if (this != Thread.currentThread()) {
                interrupt();
            }
        }

        /**
     * The receiving thread.
     */
        @Override
        public void run() {
            byte[] buf = new byte[1024];
            byte[] rBuf = new byte[1024];
            int len = 0;
            int i, j;
            try {
                while (!ivTerminate) {
                    try {
                        len = ivIn.read(buf);
                    } catch (InterruptedIOException iex) {
                        len = 0;
                    }
                    for (i = 0, j = 0; i < len; i++) {
                        rBuf[j] = buf[i];
                        if ((ivIACParserStatus != SIAC_START) || (buf[i] == IAC)) {
                            if ((ivIACParserStatus == SIAC_START) && (buf[i] == IAC)) {
                                if (j > 0) receivedData(rBuf, j);
                                j = 0;
                            }
                            j += processIAC(buf[i]);
                        } else ++j;
                    }
                    if (j > 0) receivedData(rBuf, j);
                }
            } catch (IOException ex) {
                if (!ivTerminate) catchedIOException(ex);
            }
        }
    }
}

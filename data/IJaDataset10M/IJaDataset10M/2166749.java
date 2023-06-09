package com.mysql.jdbc.profiler;

import java.util.Date;
import com.mysql.jdbc.Util;

/**
 * @author mmatthew
 */
public class ProfilerEvent {

    /**
	 * A Profiler warning event
	 */
    public static final byte TYPE_WARN = 0;

    /**
	 * Profiler creating object type event
	 */
    public static final byte TYPE_OBJECT_CREATION = 1;

    /**
	 * Profiler event for prepared statements being prepared
	 */
    public static final byte TYPE_PREPARE = 2;

    /**
	 * Profiler event for a query being executed
	 */
    public static final byte TYPE_QUERY = 3;

    /**
	 * Profiler event for prepared statements being executed
	 */
    public static final byte TYPE_EXECUTE = 4;

    /**
	 * Profiler event for result sets being retrieved
	 */
    public static final byte TYPE_FETCH = 5;

    /**
	 * Type of event
	 */
    protected byte eventType;

    /**
	 * Associated connection (-1 for none)
	 */
    protected int connectionId;

    /**
	 * Associated statement (-1 for none)
	 */
    protected int statementId;

    /**
	 * Associated result set (-1 for none)
	 */
    protected int resultSetId;

    /**
	 * When was the event created?
	 */
    protected long eventCreationTime;

    /**
	 * How long did the event last?
	 */
    protected int eventDurationMillis;

    /**
	 * The hostname the event occurred on (as an index into a dictionary, used
	 * by 'remote' profilers for efficiency)?
	 */
    protected int hostNameIndex;

    /**
	 * The hostname the event occurred on
	 */
    protected String hostName;

    /**
	 * The catalog the event occurred on (as an index into a dictionary, used by
	 * 'remote' profilers for efficiency)?
	 */
    protected int catalogIndex;

    /**
	 * The catalog the event occurred on
	 */
    protected String catalog;

    /**
	 * Where was the event created (as an index into a dictionary, used by
	 * 'remote' profilers for efficiency)?
	 */
    protected int eventCreationPointIndex;

    /**
	 * Where was the event created (as a Throwable)?
	 */
    protected Throwable eventCreationPoint;

    /**
	 * Where was the event created (as a string description of the
	 * eventCreationPoint)?
	 */
    protected String eventCreationPointDesc;

    /**
	 * Optional event message
	 */
    protected String message;

    /**
	 * Creates a new profiler event
	 * 
	 * @param eventType
	 *            the event type (from the constants TYPE_????)
	 * @param hostName
	 *            the hostname where the event occurs
	 * @param catalog
	 *            the catalog in use
	 * @param connectionId
	 *            the connection id (-1 if N/A)
	 * @param statementId
	 *            the statement id (-1 if N/A)
	 * @param resultSetId
	 *            the result set id (-1 if N/A)
	 * @param eventCreationTime
	 *            when was the event created?
	 * @param eventDurationMillis
	 *            how long did the event last?
	 * @param eventCreationPointDesc
	 *            event creation point as a string
	 * @param eventCreationPoint
	 *            event creation point as a Throwable
	 * @param message
	 *            optional message
	 */
    public ProfilerEvent(byte eventType, String hostName, String catalog, int connectionId, int statementId, int resultSetId, long eventCreationTime, int eventDurationMillis, String eventCreationPointDesc, Throwable eventCreationPoint, String message) {
        this.eventType = eventType;
        this.connectionId = connectionId;
        this.statementId = statementId;
        this.resultSetId = resultSetId;
        this.eventCreationTime = eventCreationTime;
        this.eventDurationMillis = eventDurationMillis;
        this.eventCreationPoint = eventCreationPoint;
        this.eventCreationPointDesc = eventCreationPointDesc;
        this.message = message;
    }

    /**
	 * Returns the description of when this event was created.
	 * 
	 * @return a description of when this event was created.
	 */
    public String getEventCreationPointAsString() {
        if (this.eventCreationPointDesc == null) {
            this.eventCreationPointDesc = Util.stackTraceToString(this.eventCreationPoint);
        }
        return this.eventCreationPointDesc;
    }

    /**
	 * Returns a representation of this event as a String.
	 * 
	 * @return a String representation of this event.
	 */
    public String toString() {
        StringBuffer buf = new StringBuffer(32);
        switch(this.eventType) {
            case TYPE_EXECUTE:
                buf.append("EXECUTE");
                break;
            case TYPE_FETCH:
                buf.append("FETCH");
                break;
            case TYPE_OBJECT_CREATION:
                buf.append("CONSTRUCT");
                break;
            case TYPE_PREPARE:
                buf.append("PREPARE");
                break;
            case TYPE_QUERY:
                buf.append("QUERY");
                break;
            case TYPE_WARN:
                buf.append("WARN");
                break;
            default:
                buf.append("UNKNOWN");
        }
        buf.append(" created: ");
        buf.append(new Date(this.eventCreationTime));
        buf.append(" duration: ");
        buf.append(this.eventDurationMillis);
        buf.append(" connection: ");
        buf.append(this.connectionId);
        buf.append(" statement: ");
        buf.append(this.statementId);
        buf.append(" resultset: ");
        buf.append(this.resultSetId);
        if (this.message != null) {
            buf.append(" message: ");
            buf.append(this.message);
        }
        if (this.eventCreationPointDesc != null) {
            buf.append("\n\nEvent Created at:\n");
            buf.append(this.eventCreationPointDesc);
        }
        return buf.toString();
    }

    /**
	 * Unpacks a binary representation of this event.
	 * 
	 * @param buf
	 *            the binary representation of this event
	 * @return the unpacked Event
	 * @throws Exception
	 *             if an error occurs while unpacking the event
	 */
    public static ProfilerEvent unpack(byte[] buf) throws Exception {
        int pos = 0;
        byte eventType = buf[pos++];
        int connectionId = readInt(buf, pos);
        pos += 4;
        int statementId = readInt(buf, pos);
        pos += 4;
        int resultSetId = readInt(buf, pos);
        pos += 4;
        long eventCreationTime = readLong(buf, pos);
        pos += 8;
        int eventDurationMillis = readInt(buf, pos);
        pos += 4;
        int eventCreationPointIndex = readInt(buf, pos);
        pos += 4;
        byte[] eventCreationAsBytes = readBytes(buf, pos);
        pos += 4;
        if (eventCreationAsBytes != null) {
            pos += eventCreationAsBytes.length;
        }
        byte[] message = readBytes(buf, pos);
        pos += 4;
        if (message != null) {
            pos += message.length;
        }
        return new ProfilerEvent(eventType, "", "", connectionId, statementId, resultSetId, eventCreationTime, eventDurationMillis, new String(eventCreationAsBytes, "ISO8859_1"), null, new String(message, "ISO8859_1"));
    }

    /**
	 * Creates a binary representation of this event.
	 * 
	 * @return a binary representation of this event
	 * @throws Exception
	 *             if an error occurs while packing this event.
	 */
    public byte[] pack() throws Exception {
        int len = 1 + 4 + 4 + 4 + 8 + 4 + 4;
        byte[] eventCreationAsBytes = null;
        getEventCreationPointAsString();
        if (this.eventCreationPointDesc != null) {
            eventCreationAsBytes = this.eventCreationPointDesc.getBytes("ISO8859_1");
            len += (4 + eventCreationAsBytes.length);
        } else {
            len += 4;
        }
        byte[] messageAsBytes = null;
        if (messageAsBytes != null) {
            messageAsBytes = this.message.getBytes("ISO8859_1");
            len += (4 + messageAsBytes.length);
        } else {
            len += 4;
        }
        byte[] buf = new byte[len];
        int pos = 0;
        buf[pos++] = this.eventType;
        pos = writeInt(this.connectionId, buf, pos);
        pos = writeInt(this.statementId, buf, pos);
        pos = writeInt(this.resultSetId, buf, pos);
        pos = writeLong(this.eventCreationTime, buf, pos);
        pos = writeInt(this.eventDurationMillis, buf, pos);
        pos = writeInt(this.eventCreationPointIndex, buf, pos);
        if (eventCreationAsBytes != null) {
            pos = writeBytes(eventCreationAsBytes, buf, pos);
        } else {
            pos = writeInt(0, buf, pos);
        }
        if (messageAsBytes != null) {
            pos = writeBytes(messageAsBytes, buf, pos);
        } else {
            pos = writeInt(0, buf, pos);
        }
        return buf;
    }

    private static int writeInt(int i, byte[] buf, int pos) {
        buf[pos++] = (byte) (i & 0xff);
        buf[pos++] = (byte) (i >>> 8);
        buf[pos++] = (byte) (i >>> 16);
        buf[pos++] = (byte) (i >>> 24);
        return pos;
    }

    private static int writeLong(long l, byte[] buf, int pos) {
        buf[pos++] = (byte) (l & 0xff);
        buf[pos++] = (byte) (l >>> 8);
        buf[pos++] = (byte) (l >>> 16);
        buf[pos++] = (byte) (l >>> 24);
        buf[pos++] = (byte) (l >>> 32);
        buf[pos++] = (byte) (l >>> 40);
        buf[pos++] = (byte) (l >>> 48);
        buf[pos++] = (byte) (l >>> 56);
        return pos;
    }

    private static int writeBytes(byte[] msg, byte[] buf, int pos) {
        pos = writeInt(msg.length, buf, pos);
        System.arraycopy(msg, 0, buf, pos, msg.length);
        return pos + msg.length;
    }

    private static int readInt(byte[] buf, int pos) {
        return (buf[pos++] & 0xff) | ((buf[pos++] & 0xff) << 8) | ((buf[pos++] & 0xff) << 16) | ((buf[pos++] & 0xff) << 24);
    }

    private static long readLong(byte[] buf, int pos) {
        return (long) (buf[pos++] & 0xff) | ((long) (buf[pos++] & 0xff) << 8) | ((long) (buf[pos++] & 0xff) << 16) | ((long) (buf[pos++] & 0xff) << 24) | ((long) (buf[pos++] & 0xff) << 32) | ((long) (buf[pos++] & 0xff) << 40) | ((long) (buf[pos++] & 0xff) << 48) | ((long) (buf[pos++] & 0xff) << 56);
    }

    private static byte[] readBytes(byte[] buf, int pos) {
        int length = readInt(buf, pos);
        pos += 4;
        byte[] msg = new byte[length];
        System.arraycopy(buf, pos, msg, 0, length);
        return msg;
    }

    /**
	 * Returns the catalog in use
	 * 
	 * @return the catalog in use
	 */
    public String getCatalog() {
        return this.catalog;
    }

    /**
	 * Returns the id of the connection in use when this event was created.
	 * 
	 * @return the connection in use
	 */
    public int getConnectionId() {
        return this.connectionId;
    }

    /**
	 * Returns the point (as a Throwable stacktrace) where this event was
	 * created.
	 * 
	 * @return the point where this event was created
	 */
    public Throwable getEventCreationPoint() {
        return this.eventCreationPoint;
    }

    /**
	 * Returns the time (in System.currentTimeMillis() form) when this event was
	 * created
	 * 
	 * @return the time this event was created
	 */
    public long getEventCreationTime() {
        return this.eventCreationTime;
    }

    /**
	 * Returns the duration of the event in milliseconds
	 * 
	 * @return the duration of the event in milliseconds
	 */
    public int getEventDurationMillis() {
        return this.eventDurationMillis;
    }

    /**
	 * Returns the event type flag
	 * 
	 * @return the event type flag
	 */
    public byte getEventType() {
        return this.eventType;
    }

    /**
	 * Returns the id of the result set in use when this event was created.
	 * 
	 * @return the result set in use
	 */
    public int getResultSetId() {
        return this.resultSetId;
    }

    /**
	 * Returns the id of the statement in use when this event was created.
	 * 
	 * @return the statement in use
	 */
    public int getStatementId() {
        return this.statementId;
    }

    /**
	 * Returns the optional message for this event
	 * 
	 * @return the message stored in this event
	 */
    public String getMessage() {
        return this.message;
    }
}

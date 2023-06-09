package org.firebirdsql.gds.impl.wire;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import org.firebirdsql.encodings.EncodingFactory;
import org.firebirdsql.gds.XSQLDA;
import org.firebirdsql.gds.XSQLVAR;
import org.firebirdsql.logging.Logger;
import org.firebirdsql.logging.LoggerFactory;

/**
 * An <code>XdrOutputStream</code> writes data in XDR format to an 
 * underlying <code>java.io.OutputStream</code>.
 *
 * @author <a href="mailto:alberola@users.sourceforge.net">Alejandro Alberola</a>
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @version 1.0
 */
public class XdrOutputStream {

    private static final int BUF_SIZE = 32767;

    private static Logger log = LoggerFactory.getLogger(XdrOutputStream.class, false);

    private static byte[] textPad = new byte[BUF_SIZE];

    private static byte[] zero = new XSQLVAR().encodeInt(0);

    private static byte[] minusOne = new XSQLVAR().encodeInt(-1);

    private byte[] buf = new byte[BUF_SIZE];

    private int count;

    private OutputStream out = null;

    protected XdrOutputStream() {
    }

    /**
     * Create a new instance of <code>XdrOutputStream</code>.
     *
     * @param out The underlying <code>OutputStream</code> to write to
     */
    public XdrOutputStream(OutputStream out) {
        this.out = out;
        count = 0;
        Arrays.fill(textPad, (byte) 32);
    }

    /**
     * Write a <code>byte</code> buffer to the underlying output stream in
     * XDR format.
     *
     * @param buffer The <code>byte</code> buffer to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeBuffer(byte[] buffer) throws IOException {
        if (buffer == null) writeInt(0); else {
            int len = buffer.length;
            writeInt(len);
            write(buffer, len, (4 - len) & 3);
        }
    }

    /**
     * Write a blob buffer to the underlying output stream in XDR format.
     *
     * @param buffer A <code>byte</code> array containing the blob
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeBlobBuffer(byte[] buffer) throws IOException {
        int len = buffer.length;
        if (log != null && log.isDebugEnabled()) log.debug("writeBlobBuffer len: " + len);
        if (len > Short.MAX_VALUE) {
            throw new IOException("");
        }
        writeInt(len + 2);
        writeInt(len + 2);
        checkBufferSize(2);
        buf[count++] = (byte) ((len >> 0) & 0xff);
        buf[count++] = (byte) ((len >> 8) & 0xff);
        write(buffer, len, ((4 - len + 2) & 3));
    }

    /**
     * Write a <code>String</code> to the underlying output stream in
     * XDR format.
     *
     * @param s The <code>String</code> to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeString(String s) throws IOException {
        byte[] buffer = s.getBytes();
        writeBuffer(buffer);
    }

    /**
     * Write content of the specified string using the specified Java encoding.
     */
    public void writeString(String s, String encoding) throws IOException {
        if (encoding != null) writeBuffer(s.getBytes(encoding)); else writeBuffer(s.getBytes());
    }

    /**
     * Write a set of distinct byte values (ie parameter buffer).
     *
     * @param type The type of the parameter value being written, 
     *        e.g. {@link ISCConstants#isc_tpb_version3}
     * @param s The set of byte values to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeSet(int type, byte[] s) throws IOException {
        if (s == null) {
            writeInt(1);
            checkBufferSize(1);
            buf[count++] = (byte) type;
        } else {
            int len = s.length;
            writeInt(len + 1);
            checkBufferSize(1);
            buf[count++] = (byte) type;
            write(s, len, (4 - (len + 1)) & 3);
        }
    }

    /**
     * Write an <code>Xdrable</code> to this output stream.
     *
     * @param type Type of the <code>Xdrable</code> to be written, 
     *        e.g. {@link ISCConstants#isc_tpb_version3}
     * @param item The object to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeTyped(int type, Xdrable item) throws IOException {
        int size;
        if (item == null) {
            writeInt(1);
            checkBufferSize(1);
            buf[count++] = (byte) type;
            size = 1;
        } else {
            size = item.getLength() + 1;
            writeInt(size);
            checkBufferSize(1);
            buf[count++] = (byte) type;
            item.write(this);
        }
        count += (4 - size) & 3;
    }

    /**
     * Write a set of SQL data from a <code>XSQLDA</code> data structure.
     *
     * @param xsqlda The datastructure containing the SQL data to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeSQLData(XSQLDA xsqlda) throws IOException {
        for (int i = 0; i < xsqlda.sqld; i++) {
            XSQLVAR xsqlvar = xsqlda.sqlvar[i];
            if (log != null && log.isDebugEnabled()) {
                if (out == null) {
                    log.debug("db.out null in writeSQLDatum");
                }
                if (xsqlvar.sqldata == null) {
                    log.debug("sqldata null in writeSQLDatum: " + xsqlvar);
                }
                if (xsqlvar.sqldata == null) {
                    log.debug("sqldata still null in writeSQLDatum: " + xsqlvar);
                }
            }
            int len = xsqlda.ioLength[i];
            byte[] buffer = xsqlvar.sqldata;
            if (len == 0) {
                if (buffer != null) {
                    len = buffer.length;
                    writeInt(len);
                    write(buffer, len, (4 - len) & 3);
                    write(zero, 4, 0);
                } else {
                    writeInt(0);
                    write(minusOne, 4, 0);
                }
            } else if (len < 0) {
                if (buffer != null) {
                    write(buffer, -len, 0);
                    write(zero, 4, 0);
                } else {
                    write(textPad, -len, 0);
                    write(minusOne, 4, 0);
                }
            } else {
                len--;
                if (buffer != null) {
                    int buflen = buffer.length;
                    if (buflen >= len) {
                        write(buffer, len, (4 - len) & 3);
                    } else {
                        write(buffer, buflen, 0);
                        write(textPad, len - buflen, (4 - len) & 3);
                    }
                    write(zero, 4, 0);
                } else {
                    write(textPad, len, (4 - len) & 3);
                    write(minusOne, 4, 0);
                }
            }
        }
    }

    /**
     * Write a <code>long</code> value to the underlying stream in XDR format.
     *
     * @param v The <code>long</code> value to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeLong(long v) throws IOException {
        checkBufferSize(8);
        buf[count++] = (byte) (v >>> 56 & 0xFF);
        buf[count++] = (byte) (v >>> 48 & 0xFF);
        buf[count++] = (byte) (v >>> 40 & 0xFF);
        buf[count++] = (byte) (v >>> 32 & 0xFF);
        buf[count++] = (byte) (v >>> 24 & 0xFF);
        buf[count++] = (byte) (v >>> 16 & 0xFF);
        buf[count++] = (byte) (v >>> 8 & 0xFF);
        buf[count++] = (byte) (v >>> 0 & 0xFF);
    }

    /**
     * Write an <code>int</code> value to the underlying stream in XDR format.
     *
     * @param v The <code>int</code> value to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void writeInt(int v) throws IOException {
        checkBufferSize(4);
        buf[count++] = (byte) (v >>> 24);
        buf[count++] = (byte) (v >>> 16);
        buf[count++] = (byte) (v >>> 8);
        buf[count++] = (byte) (v >>> 0);
    }

    /**
     * Write a <code>byte</code> buffer to the underlying output stream
     * in XDR format
     *
     * @param b The <code>byte</code> buffer to be written 
     * @param len The number of bytes to write 
     * @param pad The number of (blank) padding bytes to write
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void write(byte[] b, int len, int pad) throws IOException {
        if (len > 256 || count + len >= BUF_SIZE) {
            if (count > 0) out.write(buf, 0, count);
            out.write(b, 0, len);
            out.write(textPad, 0, pad);
            count = 0;
        } else {
            checkBufferSize(len + pad);
            System.arraycopy(b, 0, buf, count, len);
            count += len;
            System.arraycopy(textPad, 0, buf, count, pad);
            count += pad;
        }
    }

    /**
     * Write a single <code>byte</code> to the underlying output stream in 
     * XDR format.
     *
     * @param b The value to be written, will be truncated to a byte
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void write(int b) throws IOException {
        checkBufferSize(1);
        buf[count++] = (byte) b;
    }

    /**
     * Write an array of <code>byte</code>s to the underlying output stream
     * in XDR format.
     *
     * @param b The <code>byte</code> array to be written
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void write(byte b[]) throws IOException {
        write(b, b.length, 0);
    }

    /**
     * Flush all buffered data to the underlying output stream.
     * 
     * @throws IOException if an error occurs while writing to the 
     *         underlying output stream
     */
    public void flush() throws IOException {
        if (count > 0) {
            out.write(buf, 0, count);
        }
        count = 0;
        out.flush();
    }

    /**
     * Close this stream and the underlying output stream.
     *
     * @throws IOException if an error occurs while closing the 
     *         underlying stream
     */
    public void close() throws IOException {
        out.close();
    }

    /**
     * Ensure that there is room in the buffer for a given number
     * of direct writes to it.
     * @param countToWrite The size of write that is being checked.
     * @throws IOException If a write to the underlying OutputStream fails
     */
    private void checkBufferSize(int countToWrite) throws IOException {
        if (BUF_SIZE - count <= countToWrite) {
            out.write(buf, 0, count);
            count = 0;
        }
    }
}

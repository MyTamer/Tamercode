package com.continuent.tungsten.replicator.thl.log;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import com.continuent.tungsten.commons.io.BufferedFileDataInput;
import com.continuent.tungsten.commons.io.BufferedFileDataOutput;
import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.thl.THLException;

/**
 * This class manages I/O on a physical log file. It handles streams to read or
 * write from the underlying file.
 * 
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @author <a href="mailto:robert.hodges@continuent.com">Robert Hodges</a>
 * @version 1.0
 */
public class LogFile {

    static Logger logger = Logger.getLogger(LogFile.class);

    private static final int MAGIC_NUMBER = 0xC001CAFE;

    private static final short MAJOR_VERSION = 0x0001;

    private static final short MINOR_VERSION = 0x0001;

    private static final int RECORD_LENGTH_SIZE = 4;

    private static final int HEADER_LENGTH = 16;

    private static final int HEADER_WAIT_MILLIS = 5000;

    /**
     * Maximum value of a single record. Larger values indicate file corruption.
     */
    private static final int MAX_RECORD_LENGTH = 1000000000;

    /** Return immediately from write when there are no data. */
    public static final int NO_WAIT = 0;

    /** Current mode of file, namely read or write. */
    private enum AccessMode {

        write, read
    }

    ;

    /** Log file name. */
    private final File file;

    /** Buffer size used for I/O operations. */
    private int bufferSize = 65536;

    /**
     * Flush (or fsync) after this many milliseconds. Higher values defer flush
     */
    private long flushIntervalMillis = 0;

    /** If true, fsync when flushing. */
    private boolean fsyncOnFlush = false;

    private LogFlushTask logFlushTask = null;

    private AccessMode mode = null;

    private BufferedFileDataInput dataInput;

    private BufferedFileDataOutput dataOutput;

    private long nextFlushMillis = 0;

    private long baseSeqno;

    private boolean needsFlush;

    /**
     * Creates a file from a parent directory and child filename. The file must
     * exist.
     * 
     * @param parentDirectory Log file directory
     * @param fileName Log file name
     */
    public LogFile(File parentDirectory, String fileName) {
        this(new File(parentDirectory, fileName));
    }

    /**
     * Creates a log file from a simple file. The file must exist.
     * 
     * @param file Log file specification
     */
    public LogFile(File file) {
        this.file = file;
    }

    /**
     * Returns the log file.
     */
    public synchronized File getFile() {
        return file;
    }

    /** Returns the current log flush task, if any. */
    public synchronized LogFlushTask getLogFlushTask() {
        return logFlushTask;
    }

    /**
     * Sets the log flush task. This enables delayed flush/fsync using
     * flushIntervalMillis.
     */
    public synchronized void setLogSyncTask(LogFlushTask logFlushTask) {
        this.logFlushTask = logFlushTask;
    }

    public synchronized long getFlushIntervalMillis() {
        return flushIntervalMillis;
    }

    public synchronized void setFlushIntervalMillis(long flushIntervalMillis) {
        this.flushIntervalMillis = flushIntervalMillis;
    }

    public synchronized boolean isFsyncOnFlush() {
        return fsyncOnFlush;
    }

    public synchronized void setFsyncOnFlush(boolean fsyncOnFlush) {
        this.fsyncOnFlush = fsyncOnFlush;
    }

    public synchronized int getBufferSize() {
        return bufferSize;
    }

    public synchronized void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    /**
     * Open the log file for reading. The log cannot be written.
     * 
     * @throws ReplicatorException Thrown if file cannot be opened
     * @throws InterruptedException Thrown if thread is interrupted
     */
    public synchronized void openRead() throws ReplicatorException, InterruptedException {
        if (!file.exists()) {
            throw new THLException("Cannot open log file for reading; file does not exist: " + file.getName());
        }
        try {
            dataInput = new BufferedFileDataInput(file, bufferSize);
        } catch (IOException e) {
            throw new THLException("Unable to open file for reading: " + file.getName(), e);
        }
        mode = AccessMode.read;
        checkFileHeader(dataInput);
    }

    /**
     * Prepare the log file for writing. The write offset is automatically set
     * to the end of the file.
     * 
     * @throws ReplicatorException Thrown if file cannot be opened
     * @throws InterruptedException Thrown if we are interrupted
     */
    public synchronized void openWrite() throws ReplicatorException, InterruptedException {
        if (!file.exists()) {
            throw new THLException("Cannot open log file for writing; file does not exist: " + file.getName());
        }
        try {
            BufferedFileDataInput bfdi = new BufferedFileDataInput(file, bufferSize);
            checkFileHeader(bfdi);
            bfdi.close();
            dataOutput = new BufferedFileDataOutput(file, bufferSize);
        } catch (IOException e) {
            throw new THLException("Failed to open existing file for writing: " + file.getName(), e);
        }
        mode = AccessMode.write;
        if (logFlushTask != null) logFlushTask.addLogFile(this);
    }

    /**
     * Create a new log file. We must write a header with a base sequence
     * number. NOTE: The file offset is positioned after the header.
     * 
     * @param seqno Base sequence number of this file (written to header)
     */
    public synchronized void create(long seqno) throws ReplicatorException, InterruptedException {
        if (file.exists()) {
            throw new THLException("Cannot create new log file; file already exists: " + file.getName());
        }
        try {
            dataOutput = new BufferedFileDataOutput(file, bufferSize);
        } catch (IOException e) {
            throw new THLException("Failed to open new file for writing: " + file.getName(), e);
        }
        mode = AccessMode.write;
        try {
            write(MAGIC_NUMBER);
            write(MAJOR_VERSION);
            write(MINOR_VERSION);
            write(seqno);
            flush();
        } catch (IOException e) {
            throw new THLException("Unable to write file header: " + file.getName(), e);
        }
        baseSeqno = seqno;
        if (logFlushTask != null) logFlushTask.addLogFile(this);
    }

    /**
     * Flush and close file. It should be called after all other methods as part
     * of a clean shutdown.
     */
    public synchronized void close() {
        if (mode != null) {
            if (mode == AccessMode.read) {
                if (dataInput != null) {
                    dataInput.close();
                    dataInput = null;
                }
            } else if (mode == AccessMode.write) {
                if (dataOutput != null) {
                    if (logFlushTask != null) logFlushTask.removeLogFile(this);
                    dataOutput.close();
                    dataOutput = null;
                }
            }
            mode = null;
        }
    }

    /**
     * Read the file header and return the log sequence number stored in the
     * file header.
     */
    private long checkFileHeader(BufferedFileDataInput bfdi) throws ReplicatorException, InterruptedException {
        int magic = 0;
        short major = 0;
        short minor = 0;
        try {
            bfdi.waitAvailable(HEADER_LENGTH, HEADER_WAIT_MILLIS);
            magic = bfdi.readInt();
            major = bfdi.readShort();
            minor = bfdi.readShort();
            baseSeqno = bfdi.readLong();
        } catch (IOException e) {
            throw new THLException("Failed to read file header from  " + file.getAbsolutePath(), e);
        }
        if (magic != MAGIC_NUMBER) throw new THLException("Could not open file " + file.getAbsolutePath() + " : invalid magic number");
        if (major != MAJOR_VERSION) throw new THLException("Could not open file " + file.getAbsolutePath() + " : incompatible major version");
        if (minor != MINOR_VERSION) logger.warn("Minor version mismatch : file " + file.getAbsolutePath() + " using format " + major + "." + minor + " - Tungsten running version " + MAJOR_VERSION + "." + MINOR_VERSION);
        return baseSeqno;
    }

    /** Returns only if log file is in read or write mode. */
    protected void assertAnyMode() throws ReplicatorException {
        if (mode == null) throw new THLException("Log file not initialized for access: file=" + file.getName());
    }

    /**
     * Returns only if log file is in write mode. If we are in read mode, this
     * will switch over to write mode and position at the end of the file.
     */
    protected void assertWriteMode() throws ReplicatorException, InterruptedException {
        if (mode != AccessMode.write) {
            close();
            openWrite();
        }
    }

    /** Returns only if log file is in read mode. */
    protected void assertReadMode() throws ReplicatorException, InterruptedException {
        if (mode != AccessMode.read) {
            close();
            openRead();
        }
    }

    /**
     * Returns the base sequence number from the file header.
     */
    public synchronized long getBaseSeqno() {
        return baseSeqno;
    }

    /**
     * Returns the length of the file, including any unbuffered writes.
     */
    public synchronized long getLength() throws ReplicatorException {
        assertAnyMode();
        try {
            if (mode == AccessMode.read) return file.length(); else return dataOutput.getOffset();
        } catch (IOException e) {
            throw new THLException("Unable to determine log file length: name=" + this.file.getAbsolutePath(), e);
        }
    }

    /**
     * Returns the current position in the log file.
     */
    public synchronized long getOffset() throws ReplicatorException {
        assertAnyMode();
        try {
            if (mode == AccessMode.read) return dataInput.getOffset(); else return dataOutput.getOffset();
        } catch (IOException e) {
            throw new THLException("Unable to determine log file offset: name=" + this.file.getAbsolutePath(), e);
        }
    }

    /**
     * Seeks to a particular offset in the file.
     * 
     * @throws IOException If positioning results in an error.
     */
    public synchronized void seekOffset(long offset) throws IOException, ReplicatorException, InterruptedException {
        assertReadMode();
        dataInput.seek(offset);
        if (logger.isDebugEnabled()) {
            logger.debug("Skipping to position " + offset + " into file " + this.file.getName());
        }
    }

    /**
     * Reads a record from the file into a byte array. We may encounter a number
     * of unpredictable conditions at this point that we need to report
     * accurately to layers above us that will decide whether it represents a
     * problem.
     * 
     * @param waitMillis Number of milliseconds to wait for data to be
     *            available. 0 means do not wait.
     * @return A log record if we can read one before timing out
     * @throws IOException Thrown if there is an I/O error
     * @throws InterruptedException Thrown if we are interrupted
     * @throws LogTimeoutException Thrown if we timeout while waiting for data
     *             to appear
     */
    public synchronized LogRecord readRecord(int waitMillis) throws IOException, InterruptedException, LogTimeoutException, ReplicatorException {
        assertReadMode();
        long offset = dataInput.getOffset();
        if (logger.isDebugEnabled()) logger.debug("Reading log file position=" + offset);
        dataInput.mark(65636);
        long startIntervalMillis = System.currentTimeMillis();
        long available = dataInput.waitAvailable(RECORD_LENGTH_SIZE, waitMillis);
        if (available < RECORD_LENGTH_SIZE) {
            dataInput.reset();
            if (waitMillis > 0) {
                throw new LogTimeoutException("Log read timeout: waitMillis=" + waitMillis + " file=" + file.getName() + " offset=" + offset);
            } else if (available == 0) {
                if (logger.isDebugEnabled()) logger.debug("Read empty record");
                return new LogRecord(offset, false);
            } else if (available < RECORD_LENGTH_SIZE) {
                if (logger.isDebugEnabled()) logger.debug("Length is truncated; returning immediately");
                return new LogRecord(offset, true);
            }
        }
        int recordLength = dataInput.readInt();
        if (recordLength < LogRecord.NON_DATA_BYTES || recordLength > MAX_RECORD_LENGTH) {
            logger.warn("Record length is invalid, log may be corrupt: offset=" + offset + " record length=" + recordLength);
            dataInput.reset();
            return new LogRecord(offset, true);
        }
        if (logger.isDebugEnabled()) logger.debug("Record length=" + recordLength);
        waitMillis = waitMillis + (int) (startIntervalMillis - System.currentTimeMillis());
        int remainingRecordLength = recordLength - RECORD_LENGTH_SIZE;
        available = dataInput.waitAvailable(remainingRecordLength, waitMillis);
        if (available < remainingRecordLength) {
            dataInput.reset();
            if (waitMillis > 0) {
                throw new LogTimeoutException("Log read timeout: waitMillis=" + waitMillis + " file=" + file.getName() + " offset=" + offset);
            } else {
                return new LogRecord(offset, true);
            }
        }
        byte[] bytesToRead = new byte[recordLength - LogRecord.NON_DATA_BYTES];
        dataInput.readFully(bytesToRead);
        byte crcType = dataInput.readByte();
        long crc = dataInput.readLong();
        return new LogRecord(offset, bytesToRead, crcType, crc);
    }

    /** Reads a single short. */
    protected short readShort() throws IOException, ReplicatorException, InterruptedException {
        assertReadMode();
        return dataInput.readShort();
    }

    /** Read a single integer. */
    protected int readInt() throws IOException, ReplicatorException, InterruptedException {
        assertReadMode();
        return dataInput.readInt();
    }

    /** Reads a single long. */
    protected long readLong() throws IOException, ReplicatorException, InterruptedException {
        assertReadMode();
        return dataInput.readLong();
    }

    /**
     * Truncate the file to the provided length. Performs an automatic fsync.
     * 
     * @param length New file length
     */
    public synchronized void setLength(long length) throws ReplicatorException, InterruptedException {
        assertWriteMode();
        try {
            dataOutput.setLength(length);
        } catch (IOException e) {
            throw new THLException("Unable to set log file length: " + file.getName(), e);
        }
    }

    protected void write(int myInt) throws IOException, ReplicatorException, InterruptedException {
        assertWriteMode();
        dataOutput.writeInt(myInt);
    }

    protected void write(long seqno) throws IOException, ReplicatorException, InterruptedException {
        assertWriteMode();
        dataOutput.writeLong(seqno);
    }

    protected void write(short myShort) throws IOException, ReplicatorException, InterruptedException {
        assertWriteMode();
        dataOutput.writeShort(myShort);
    }

    /**
     * Writes a buffer to the log file and returns true if we have exceeded the
     * log file size.
     * 
     * @param record Log record to write
     * @param logFileSize Maximum log file size
     * @return true if log file size exceeded
     */
    public synchronized boolean writeRecord(LogRecord record, int logFileSize) throws IOException, InterruptedException, ReplicatorException {
        assertWriteMode();
        dataOutput.writeInt((int) record.getRecordLength());
        dataOutput.write(record.getData());
        dataOutput.writeByte(record.getCrcType());
        dataOutput.writeLong(record.getCrc());
        needsFlush = true;
        if (logFileSize > 0 && dataOutput.getOffset() > logFileSize) return true; else return false;
    }

    /**
     * Synchronizes file writes using flush with optional fsync. You must call
     * this method to commit data.
     */
    public synchronized void flush() throws IOException, ReplicatorException, InterruptedException {
        if (!needsFlush) {
            return;
        }
        assertWriteMode();
        if (flushIntervalMillis == 0) {
            flushPrivate();
        } else if (nextFlushMillis == 0) {
            nextFlushMillis = System.currentTimeMillis() + this.flushIntervalMillis;
        } else if (System.currentTimeMillis() >= nextFlushMillis) {
            flushPrivate();
        }
    }

    private void flushPrivate() throws IOException {
        if (fsyncOnFlush) dataOutput.fsync(); else dataOutput.flush();
        nextFlushMillis = System.currentTimeMillis() + this.flushIntervalMillis;
        needsFlush = false;
    }

    /**
     * Returns a nicely formatting description of the file.
     */
    public synchronized String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getSimpleName()).append(": ");
        sb.append("name=").append(file.getName());
        sb.append(" mode=").append(mode);
        if (dataInput != null) {
            sb.append(" open=y size=").append(file.length());
            sb.append(" offset=").append(dataInput.getOffset());
        } else if (dataOutput != null) {
            sb.append(" open=y size=").append(file.length());
            try {
                sb.append(" offset=").append(dataOutput.getOffset());
            } catch (IOException e) {
                sb.append(" [unable to get offset due to i/o error]");
            }
        } else {
            sb.append(" open=n");
        }
        return sb.toString();
    }
}

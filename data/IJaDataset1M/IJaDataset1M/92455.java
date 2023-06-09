package org.xsocket.connection;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xsocket.DataConverter;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.AbstractNonBlockingStream.ISource;

/**
 *
 * 
 * @author grro
 */
final class ReadQueue {

    private static final Logger LOG = Logger.getLogger(ReadQueue.class.getName());

    private final Queue queue = new Queue();

    private ByteBuffer[] readMarkBuffer;

    private boolean isReadMarked = false;

    private int readMarkVersion = -1;

    public void reset() {
        readMarkBuffer = null;
        isReadMarked = false;
        queue.reset();
    }

    /**
	 * returns true, if empty
	 *
	 * @return true, if empty
	 */
    public boolean isEmpty() {
        return (queue.getSize() == 0);
    }

    /**
	 * return a int, which represent the modification version.
	 * this value will increase with each modification
	 *
	 * @return the modify version
	 */
    public int geVersion() {
        return queue.getVersion(false);
    }

    /**
	 * append a byte buffer array to this queue. By adding a list,
	 * the list becomes part of to the buffer, and should not be modified outside the buffer
	 * to avoid side effects
	 *
	 * @param bufs  the ByteBuffers
	 * @param size  the size
	 */
    public void append(ByteBuffer[] bufs, int size) {
        assert (size == computeSize(bufs));
        if (size > 0) {
            queue.append(bufs, size);
        }
    }

    private static int computeSize(ByteBuffer[] bufs) {
        int size = 0;
        for (ByteBuffer buf : bufs) {
            size += buf.remaining();
        }
        return size;
    }

    /**
	 * return the index of the delimiter or -1 if the delimiter has not been found 
	 * 
	 * @param delimiter         the delimiter
	 * @param maxReadSize       the max read size
	 * @return the position of the first delimiter byte
	 * @throws IOException in an exception occurs
	 * @throws MaxReadSizeExceededException if the max read size has been reached 
	 */
    public int retrieveIndexOf(byte[] delimiter, int maxReadSize) throws IOException, MaxReadSizeExceededException {
        return queue.retrieveIndexOf(delimiter, maxReadSize);
    }

    /**
	 * return the current size
	 *
	 * @return  the current size
	 */
    public int getSize() {
        return queue.getSize();
    }

    public ByteBuffer[] readAvailable() {
        ByteBuffer[] buffers = queue.drain();
        onExtracted(buffers);
        return buffers;
    }

    public ByteBuffer[] copyAvailable() {
        ByteBuffer[] buffers = queue.copy();
        onExtracted(buffers);
        return buffers;
    }

    public ByteBuffer[] readByteBufferByDelimiter(byte[] delimiter, int maxLength) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        ByteBuffer[] buffers = queue.readByteBufferByDelimiter(delimiter, maxLength);
        onExtracted(buffers, delimiter);
        return buffers;
    }

    public void unread(ByteBuffer[] buffers) throws IOException {
        if (isReadMarked) {
            throw new IOException("unread() is not supported in marked read mode");
        }
        queue.addFirst(buffers);
    }

    public ByteBuffer[] readByteBufferByLength(int length) throws BufferUnderflowException {
        ByteBuffer[] buffers = queue.readByteBufferByLength(length);
        onExtracted(buffers);
        return buffers;
    }

    public ByteBuffer readSingleByteBuffer(int length) throws BufferUnderflowException {
        if (getSize() < length) {
            throw new BufferUnderflowException();
        }
        ByteBuffer buffer = queue.readSingleByteBuffer(length);
        onExtracted(buffer);
        return buffer;
    }

    public void markReadPosition() {
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("mark read position");
        }
        removeReadMark();
        isReadMarked = true;
        readMarkVersion = queue.getVersion(true);
    }

    public boolean resetToReadMark() {
        if (isReadMarked) {
            if (readMarkBuffer != null) {
                queue.addFirst(readMarkBuffer);
                removeReadMark();
                queue.setVersion(readMarkVersion);
            } else {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("reset read mark. nothing to return to read queue");
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public void removeReadMark() {
        isReadMarked = false;
        readMarkBuffer = null;
    }

    private void onExtracted(ByteBuffer[] buffers) {
        if (isReadMarked) {
            for (ByteBuffer buffer : buffers) {
                onExtracted(buffer);
            }
        }
    }

    private void onExtracted(ByteBuffer[] buffers, byte[] delimiter) {
        if (isReadMarked) {
            if (buffers != null) {
                for (ByteBuffer buffer : buffers) {
                    onExtracted(buffer);
                }
            }
            onExtracted(ByteBuffer.wrap(delimiter));
        }
    }

    private void onExtracted(ByteBuffer buffer) {
        if (isReadMarked) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("add data (" + DataConverter.toFormatedBytesSize(buffer.remaining()) + ") to read mark buffer ");
            }
            if (readMarkBuffer == null) {
                readMarkBuffer = new ByteBuffer[1];
                readMarkBuffer[0] = buffer.duplicate();
            } else {
                ByteBuffer[] newReadMarkBuffer = new ByteBuffer[readMarkBuffer.length + 1];
                System.arraycopy(readMarkBuffer, 0, newReadMarkBuffer, 0, readMarkBuffer.length);
                newReadMarkBuffer[readMarkBuffer.length] = buffer.duplicate();
                readMarkBuffer = newReadMarkBuffer;
            }
        }
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    public String toString(String encoding) {
        return queue.toString(encoding);
    }

    private static final class Queue implements ISource {

        private static final int THRESHOLD_COMPACT_BUFFER_COUNT_TOTAL = 20;

        private static final int THRESHOLD_COMPACT_BUFFER_COUNT_EMPTY = 10;

        private ByteBuffer[] buffers = null;

        private Integer currentSize = null;

        private int version = 0;

        private boolean isAppended = false;

        private Index cachedIndex = null;

        /**
		 * clean the queue
		 */
        public synchronized void reset() {
            buffers = null;
            currentSize = null;
            cachedIndex = null;
            isAppended = false;
        }

        /**
		 * return a int, which represent the  version.
		 * this value will increase with modification
		 *
		 * @return the modify version
		 */
        public synchronized int getVersion(boolean mark) {
            if (mark) {
                isAppended = false;
            }
            return version;
        }

        public synchronized void setVersion(int version) {
            if (!isAppended) {
                this.version = version;
            }
        }

        /**
		 * return the current size
		 *
		 * @return  the current size
		 */
        public synchronized int getSize() {
            return size();
        }

        private int size() {
            if (currentSize != null) {
                return currentSize;
            }
            if (buffers == null) {
                return 0;
            } else {
                int size = 0;
                for (int i = 0; i < buffers.length; i++) {
                    if (buffers[i] != null) {
                        size += buffers[i].remaining();
                    }
                }
                currentSize = size;
                return size;
            }
        }

        /**
		 * append a byte buffer array to this queue. By adding a array,
		 * the array becomes part of to the buffer, and should not be modified outside the buffer
		 * to avoid side effects
		 *
		 * @param bufs  the ByteBuffers
		 * @param size  the size 
		 */
        public synchronized void append(ByteBuffer[] bufs, int size) {
            isAppended = true;
            version++;
            assert (!containsEmptyBuffer(bufs));
            if (buffers == null) {
                buffers = bufs;
                currentSize = size;
            } else {
                currentSize = null;
                ByteBuffer[] newBuffers = new ByteBuffer[buffers.length + bufs.length];
                System.arraycopy(buffers, 0, newBuffers, 0, buffers.length);
                System.arraycopy(bufs, 0, newBuffers, buffers.length, bufs.length);
                buffers = newBuffers;
            }
        }

        public synchronized void addFirst(ByteBuffer[] bufs) {
            version++;
            currentSize = null;
            cachedIndex = null;
            assert (!containsEmptyBuffer(bufs));
            addFirstSilence(bufs);
        }

        private void addFirstSilence(ByteBuffer[] bufs) {
            currentSize = null;
            if (buffers == null) {
                buffers = bufs;
            } else {
                ByteBuffer[] newBuffers = new ByteBuffer[buffers.length + bufs.length];
                System.arraycopy(bufs, 0, newBuffers, 0, bufs.length);
                System.arraycopy(buffers, 0, newBuffers, bufs.length, buffers.length);
                buffers = newBuffers;
            }
        }

        private static boolean containsEmptyBuffer(ByteBuffer[] bufs) {
            boolean containsEmtpyBuffer = false;
            for (ByteBuffer buf : bufs) {
                if (buf == null) {
                    containsEmtpyBuffer = true;
                }
            }
            return containsEmtpyBuffer;
        }

        /**
		 * drain the queue
		 * 
		 * @return the content
		 */
        public synchronized ByteBuffer[] drain() {
            currentSize = null;
            cachedIndex = null;
            ByteBuffer[] result = buffers;
            buffers = null;
            if (result != null) {
                version++;
            }
            return removeEmptyBuffers(result);
        }

        public synchronized ByteBuffer[] copy() {
            if (buffers == null) {
                return new ByteBuffer[0];
            }
            ByteBuffer[] result = new ByteBuffer[buffers.length];
            for (int i = 0; i < buffers.length; i++) {
                result[i] = buffers[i].duplicate();
            }
            return removeEmptyBuffers(result);
        }

        /**
		 * read bytes
		 *
		 * @param length  the length
		 * @return the read bytes
	 	 * @throws BufferUnderflowException if the buffer`s limit has been reached
		 */
        public synchronized ByteBuffer readSingleByteBuffer(int length) throws BufferUnderflowException {
            if (buffers == null) {
                throw new BufferUnderflowException();
            }
            if (!isSizeEqualsOrLargerThan(length)) {
                throw new BufferUnderflowException();
            }
            if (length == 0) {
                return ByteBuffer.allocate(0);
            }
            ByteBuffer result = null;
            int countEmptyBuffer = 0;
            bufLoop: for (int i = 0; i < buffers.length; i++) {
                if (buffers[i] == null) {
                    countEmptyBuffer++;
                    continue;
                }
                if (buffers[i].remaining() == length) {
                    result = buffers[i];
                    buffers[i] = null;
                    break bufLoop;
                } else if (buffers[i].remaining() > length) {
                    int savedLimit = buffers[i].limit();
                    int savedPos = buffers[i].position();
                    buffers[i].limit(buffers[i].position() + length);
                    result = buffers[i].slice();
                    buffers[i].position(savedPos + length);
                    buffers[i].limit(savedLimit);
                    buffers[i] = buffers[i].slice();
                    break bufLoop;
                } else {
                    result = ByteBuffer.allocate(length);
                    int written = 0;
                    for (int j = i; j < buffers.length; j++) {
                        if (buffers[j] == null) {
                            countEmptyBuffer++;
                            continue;
                        } else {
                            while (buffers[j].hasRemaining()) {
                                result.put(buffers[j].get());
                                written++;
                                if (written == length) {
                                    if (buffers[j].position() < buffers[j].limit()) {
                                        buffers[j] = buffers[j].slice();
                                    } else {
                                        buffers[j] = null;
                                    }
                                    result.clear();
                                    break bufLoop;
                                }
                            }
                        }
                        buffers[j] = null;
                    }
                }
            }
            if (result == null) {
                throw new BufferUnderflowException();
            } else {
                if (countEmptyBuffer >= THRESHOLD_COMPACT_BUFFER_COUNT_EMPTY) {
                    compact();
                }
                currentSize = null;
                cachedIndex = null;
                version++;
                return result;
            }
        }

        public ByteBuffer[] readByteBufferByLength(int length) throws BufferUnderflowException {
            if (length == 0) {
                return new ByteBuffer[0];
            }
            return extract(length, 0);
        }

        public ByteBuffer[] readByteBufferByDelimiter(byte[] delimiter, int maxLength) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            synchronized (this) {
                if (buffers == null) {
                    throw new BufferUnderflowException();
                }
            }
            int index = retrieveIndexOf(delimiter, maxLength);
            if (index >= 0) {
                return extract(index, delimiter.length);
            } else {
                throw new BufferUnderflowException();
            }
        }

        synchronized ByteBuffer[] extract(int length, int countTailingBytesToRemove) throws BufferUnderflowException {
            ByteBuffer[] extracted = null;
            int size = size();
            if (length == size) {
                return drain();
            }
            if (size < length) {
                throw new BufferUnderflowException();
            }
            extracted = extractBuffers(length);
            if (countTailingBytesToRemove > 0) {
                extractBuffers(countTailingBytesToRemove);
            }
            compact();
            currentSize = null;
            cachedIndex = null;
            version++;
            return extracted;
        }

        /**
		 * return the index of the delimiter or -1 if the delimiter has not been found 
		 * 
		 * @param delimiter         the delimiter
		 * @param maxReadSize       the max read size
		 * @return the position of the first delimiter byte
		 * @throws IOException in an exception occurs
		 * @throws MaxReadSizeExceededException if the max read size has been reached 
		 */
        public int retrieveIndexOf(byte[] delimiter, int maxReadSize) throws IOException, MaxReadSizeExceededException {
            ByteBuffer[] bufs = null;
            synchronized (this) {
                if (buffers == null) {
                    return -1;
                }
                bufs = buffers;
                buffers = null;
            }
            int index = retrieveIndexOf(delimiter, bufs, maxReadSize);
            synchronized (this) {
                addFirstSilence(bufs);
            }
            if (index == -2) {
                throw new MaxReadSizeExceededException();
            } else {
                return index;
            }
        }

        private int retrieveIndexOf(byte[] delimiter, ByteBuffer[] buffers, int maxReadSize) {
            Integer length = null;
            if (buffers == null) {
                return -1;
            }
            Index index = scanByDelimiter(buffers, delimiter);
            if (index.hasDelimiterFound()) {
                if (index.getReadBytes() <= maxReadSize) {
                    length = index.getReadBytes() - delimiter.length;
                } else {
                    length = null;
                }
            } else {
                length = null;
            }
            cachedIndex = index;
            if (length == null) {
                if (index.getReadBytes() >= maxReadSize) {
                    return -2;
                }
                return -1;
            } else {
                return length;
            }
        }

        private Index scanByDelimiter(ByteBuffer[] buffers, byte[] delimiter) {
            if ((cachedIndex != null) && (cachedIndex.isDelimiterEquals(delimiter))) {
                if (cachedIndex.hasDelimiterFound()) {
                    return cachedIndex;
                } else {
                    return find(buffers, cachedIndex);
                }
            } else {
                return find(buffers, delimiter);
            }
        }

        private boolean isSizeEqualsOrLargerThan(int requiredSize) {
            if (buffers == null) {
                return false;
            }
            int bufferSize = 0;
            for (int i = 0; i < buffers.length; i++) {
                if (buffers[i] != null) {
                    bufferSize += buffers[i].remaining();
                    if (bufferSize >= requiredSize) {
                        return true;
                    }
                }
            }
            return false;
        }

        private ByteBuffer[] extractBuffers(int length) {
            ByteBuffer[] extracted = null;
            int remainingToExtract = length;
            ByteBuffer buffer = null;
            for (int i = 0; i < buffers.length; i++) {
                buffer = buffers[i];
                if (buffer == null) {
                    continue;
                }
                int bufLength = buffer.limit() - buffer.position();
                if (remainingToExtract >= bufLength) {
                    extracted = appendBuffer(extracted, buffer);
                    remainingToExtract -= bufLength;
                    buffers[i] = null;
                } else {
                    int savedLimit = buffer.limit();
                    buffer.limit(buffer.position() + remainingToExtract);
                    ByteBuffer leftPart = buffer.slice();
                    extracted = appendBuffer(extracted, leftPart);
                    buffer.position(buffer.limit());
                    buffer.limit(savedLimit);
                    ByteBuffer rightPart = buffer.slice();
                    buffers[i] = rightPart;
                    remainingToExtract = 0;
                }
                if (remainingToExtract == 0) {
                    return extracted;
                }
            }
            return new ByteBuffer[0];
        }

        private static ByteBuffer[] appendBuffer(ByteBuffer[] buffers, ByteBuffer buffer) {
            if (buffers == null) {
                ByteBuffer[] result = new ByteBuffer[1];
                result[0] = buffer;
                return result;
            } else {
                ByteBuffer[] result = new ByteBuffer[buffers.length + 1];
                System.arraycopy(buffers, 0, result, 0, buffers.length);
                result[buffers.length] = buffer;
                return result;
            }
        }

        private void compact() {
            if ((buffers != null) && (buffers.length > THRESHOLD_COMPACT_BUFFER_COUNT_TOTAL)) {
                int emptyBuffers = 0;
                for (int i = 0; i < buffers.length; i++) {
                    if (buffers[i] == null) {
                        emptyBuffers++;
                    }
                }
                if (emptyBuffers > THRESHOLD_COMPACT_BUFFER_COUNT_EMPTY) {
                    if (emptyBuffers == buffers.length) {
                        buffers = null;
                    } else {
                        ByteBuffer[] newByteBuffer = new ByteBuffer[buffers.length - emptyBuffers];
                        int num = 0;
                        for (int i = 0; i < buffers.length; i++) {
                            if (buffers[i] != null) {
                                newByteBuffer[num] = buffers[i];
                                num++;
                            }
                        }
                        buffers = newByteBuffer;
                    }
                }
            }
        }

        private static ByteBuffer[] removeEmptyBuffers(ByteBuffer[] buffers) {
            if (buffers == null) {
                return buffers;
            }
            int countEmptyBuffers = 0;
            for (int i = 0; i < buffers.length; i++) {
                if (buffers[i] == null) {
                    countEmptyBuffers++;
                }
            }
            if (countEmptyBuffers > 0) {
                if (countEmptyBuffers == buffers.length) {
                    return new ByteBuffer[0];
                } else {
                    ByteBuffer[] newBuffers = new ByteBuffer[buffers.length - countEmptyBuffers];
                    int num = 0;
                    for (int i = 0; i < buffers.length; i++) {
                        if (buffers[i] != null) {
                            newBuffers[num] = buffers[i];
                            num++;
                        }
                    }
                    return newBuffers;
                }
            } else {
                return buffers;
            }
        }

        @Override
        public String toString() {
            try {
                ByteBuffer[] copy = buffers.clone();
                for (int i = 0; i < copy.length; i++) {
                    if (copy[i] != null) {
                        copy[i] = copy[i].duplicate();
                    }
                }
                return DataConverter.toTextAndHexString(copy, "UTF-8", 300);
            } catch (NullPointerException npe) {
                return "";
            } catch (Exception e) {
                return e.toString();
            }
        }

        public synchronized String toString(String encoding) {
            try {
                ByteBuffer[] copy = buffers.clone();
                for (int i = 0; i < copy.length; i++) {
                    if (copy[i] != null) {
                        copy[i] = copy[i].duplicate();
                    }
                }
                return DataConverter.toString(copy, encoding);
            } catch (NullPointerException npe) {
                return "";
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Index find(ByteBuffer[] bufferQueue, byte[] delimiter) {
            return find(bufferQueue, new Index(delimiter));
        }

        private Index find(ByteBuffer[] buffers, Index index) {
            int i = findFirstBufferToScan(buffers, index);
            for (; (i < buffers.length) && !index.hasDelimiterFound; i++) {
                ByteBuffer buffer = buffers[i];
                if (buffer == null) {
                    continue;
                }
                int savedPos = buffer.position();
                int savedLimit = buffer.limit();
                findInBuffer(buffer, index);
                buffer.position(savedPos);
                buffer.limit(savedLimit);
            }
            return index;
        }

        private int findFirstBufferToScan(ByteBuffer[] buffers, Index index) {
            int i = 0;
            if (index.lastScannedBuffer != null) {
                for (int j = 0; j < buffers.length; j++) {
                    if (buffers[j] == index.lastScannedBuffer) {
                        i = j;
                        break;
                    }
                }
                i++;
                if (i >= buffers.length) {
                    return i;
                }
                for (int k = i; k < buffers.length; k++) {
                    if (buffers[k] != null) {
                        i = k;
                        break;
                    }
                }
                if (i >= buffers.length) {
                    return i;
                }
            }
            return i;
        }

        private void findInBuffer(ByteBuffer buffer, Index index) {
            index.lastScannedBuffer = buffer;
            int dataSize = buffer.remaining();
            byte[] delimiter = index.delimiterBytes;
            int delimiterLength = index.delimiterLength;
            int delimiterPosition = index.delimiterPos;
            byte nextDelimiterByte = delimiter[delimiterPosition];
            boolean delimiterPartsFound = delimiterPosition > 0;
            for (int i = 0; i < dataSize; i++) {
                byte b = buffer.get();
                if (b == nextDelimiterByte) {
                    delimiterPosition++;
                    if (delimiterLength == 1) {
                        index.hasDelimiterFound = true;
                        index.delimiterPos = delimiterPosition;
                        index.readBytes += (i + 1);
                        return;
                    } else {
                        index.delimiterPos = delimiterPosition;
                        if (delimiterPosition == delimiterLength) {
                            index.hasDelimiterFound = true;
                            index.readBytes += (i + 1);
                            return;
                        }
                        nextDelimiterByte = delimiter[delimiterPosition];
                    }
                    delimiterPartsFound = true;
                } else {
                    if (delimiterPartsFound) {
                        delimiterPosition = 0;
                        index.delimiterPos = 0;
                        nextDelimiterByte = delimiter[delimiterPosition];
                        delimiterPartsFound = false;
                        if ((delimiterLength > 1) && (b == nextDelimiterByte)) {
                            delimiterPosition++;
                            nextDelimiterByte = delimiter[delimiterPosition];
                            index.delimiterPos = delimiterPosition;
                        }
                    }
                }
            }
            index.readBytes += dataSize;
        }
    }

    private static final class Index implements Cloneable {

        private boolean hasDelimiterFound = false;

        private byte[] delimiterBytes = null;

        private int delimiterLength = 0;

        private int delimiterPos = 0;

        private int readBytes = 0;

        ByteBuffer lastScannedBuffer = null;

        Index(byte[] delimiterBytes) {
            this.delimiterBytes = delimiterBytes;
            this.delimiterLength = delimiterBytes.length;
        }

        public boolean hasDelimiterFound() {
            return hasDelimiterFound;
        }

        public int getReadBytes() {
            return readBytes;
        }

        public boolean isDelimiterEquals(byte[] other) {
            if (other.length != delimiterLength) {
                return false;
            }
            for (int i = 0; i < delimiterLength; i++) {
                if (other[i] != delimiterBytes[i]) {
                    return false;
                }
            }
            return true;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Index copy = (Index) super.clone();
            return copy;
        }

        @Override
        public String toString() {
            return "found=" + hasDelimiterFound + " delimiterPos=" + delimiterPos + " delimiterLength=" + delimiterLength + " readBytes=" + readBytes;
        }
    }
}

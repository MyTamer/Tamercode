package org.limewire.rudp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.limewire.collection.LongHashMap;
import org.limewire.rudp.messages.DataMessage;

public class DataWindow {

    private static final Log LOG = LogFactory.getLog(DataWindow.class);

    public static final int MAX_SEQUENCE_NUMBER = 0xFFFF;

    private static final int HIST_SIZE = 4;

    private static final float RTT_GAIN = 1.0f / 8.0f;

    private static final float DEVIATION_GAIN = 1.0f / 4.0f;

    private final LongHashMap<DataRecord> window;

    private long windowStart;

    private int windowSize;

    private long averageRTT;

    private long averageLowRTT;

    private int lowRTTCount;

    private float srtt;

    private float rttvar;

    private float rto;

    /**
     * optimization: use this instead of iterating through data to see
     * if there is any readable data.
     * must be set/unset when data is added/removed.
     */
    private boolean readableData;

    public DataWindow(int size, long start) {
        if (size < 1) {
            throw new IllegalArgumentException("size must be > 0");
        }
        windowStart = start;
        windowSize = size;
        window = new LongHashMap<DataRecord>(size + 2);
    }

    /**
     * Adds a new message to the window.  
     */
    public DataRecord addData(DataMessage msg) {
        if (windowStart > msg.getSequenceNumber()) {
            throw new IllegalStateException("message is not in current window: " + windowStart + " > " + msg.getSequenceNumber());
        }
        long seqNo = msg.getSequenceNumber();
        if (seqNo == windowStart) readableData = true;
        DataRecord d = window.get(seqNo);
        if (d != null) {
            if (LOG.isDebugEnabled()) LOG.debug("received duplicate message seq: " + msg.getSequenceNumber() + ", window start: " + windowStart);
            return d;
        }
        if (LOG.isDebugEnabled()) LOG.debug("adding message seq: " + msg.getSequenceNumber() + ", window start: " + windowStart);
        d = new DataRecord(msg);
        window.put(seqNo, d);
        return d;
    }

    /** 
     *  Get the block based on the sequenceNumber.
     */
    public DataRecord getBlock(long pnum) {
        return window.get(pnum);
    }

    /** 
     *  Get the start of the data window. The start will generally be the
     *  sequence number of the lowest un-ACK'ed message.
     */
    public long getWindowStart() {
        return windowStart;
    }

    /** 
     *  Get the size of the data window.
     */
    public int getWindowSize() {
        return windowSize;
    }

    /** 
     *  Get the number of slots in use. This excludes read data.
     */
    public int getUsedSpots() {
        DataRecord d;
        int count = 0;
        for (long i = windowStart; i < windowStart + windowSize + 3; i++) {
            if ((d = window.get(i)) != null && (!d.read || i != windowStart)) count++;
        }
        return (count);
    }

    /** 
     *  Get the number of slots available to be used.
     */
    public int getWindowSpace() {
        return (windowSize - getUsedSpots());
    }

    /** 
     *  Clear out the acknowledged blocks at the beginning and advance the 
     *  window forward. Return the number of un-ACK'ed blocks.
     */
    public int clearLowAckedBlocks(ChunkReleaser releaser) {
        DataRecord d;
        int count = 0;
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            d = window.get(i);
            if (d != null && d.acks > 0) {
                window.remove(i);
                count++;
                if (releaser != null) releaser.releaseChunk(d.msg.getChunk());
            } else {
                break;
            }
        }
        windowStart += count;
        return count;
    }

    /** 
     *  From the window, find the number for the next block. 
     *  i.e. sequenceNumber
     */
    public long getLowestUnsentBlock() {
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            if (window.get(i) == null) return (i);
        }
        return -1;
    }

    /** 
     *  Count the number of ACKs from higher number blocks.
     *  This should give you a hint that a block went missing.
     *  Note that this assumes that the low block isn't ACK'ed since
     *  it would get cleared if it was ACK'ed.
     */
    public int countHigherAckBlocks() {
        DataRecord d;
        int count = 0;
        for (long i = windowStart + 1; i < windowStart + windowSize + 1; i++) {
            d = window.get(i);
            if (d != null && d.acks > 0) {
                count++;
            }
        }
        return (count);
    }

    public boolean acksAppearToBeMissing(long time, int multiple) {
        int irto = (int) rto;
        DataRecord drec = getBlock(windowStart);
        if (irto > 0 && drec != null && drec.acks < 1 && drec.sentTime + (multiple * irto) < time) {
            return true;
        }
        return false;
    }

    /** 
     *  Return the RTO based on window data and ACKs.
     *  <p>
     *  The RTO is the duration of the retransmission timer which ensures data
     *  delivery in the absence of any feedback from the remote data receiver.
     */
    public int getRTO() {
        return (int) rto;
    }

    /** 
     *  Return the Round-Trip Time Variation (RTTVar) which is a measure of the
     *  range of Round-Trip Time (RTT) values.
     */
    public float getRTTVar() {
        return rttvar;
    }

    /** 
     *  Returns the SRRT estimate. The "smoothed" round-trip time estimate 
     *  is an attempt to predict future round-trip times by sampling 
     *  the behavior of packets sent over a connection and averaging those 
     *  samples.
     */
    public float getSRTT() {
        return srtt;
    }

    /** 
     *  Return the current measure of low round trip time.
     */
    public int lowRoundTripTime() {
        return (int) averageLowRTT;
    }

    /** 
     *  Record that a block was ACK'ed and calculate the 
     *  round trip time and averages from it.
     */
    public void ackBlock(long pnum) {
        if (LOG.isDebugEnabled()) LOG.debug("entered ackBlock with # " + pnum);
        DataRecord drec = getBlock(pnum);
        if (drec != null) {
            drec.acks++;
            drec.ackTime = System.currentTimeMillis();
            if (drec.acks == 1 && drec.sends == 1) {
                long rtt = (drec.ackTime - drec.sentTime);
                float delta = rtt - srtt;
                if (rtt > 0) {
                    if (srtt <= 0.1) srtt = delta; else srtt = srtt + RTT_GAIN * delta;
                    rttvar = rttvar + DEVIATION_GAIN * (Math.abs(delta) - rttvar);
                    rto = (float) (srtt + 4 * rttvar + 0.5);
                    if (averageRTT == 0) averageRTT = rtt; else {
                        float avgRTT = ((float) (averageRTT * (HIST_SIZE - 1) + rtt)) / HIST_SIZE;
                        averageRTT = (long) avgRTT;
                    }
                    if (lowRTTCount < 10 || rtt < averageLowRTT) {
                        if (averageLowRTT == 0) averageLowRTT = rtt; else {
                            float lowRtt = ((float) (averageLowRTT * (HIST_SIZE - 1) + rtt)) / HIST_SIZE;
                            averageLowRTT = (long) lowRtt;
                        }
                        lowRTTCount++;
                    }
                }
            }
        }
    }

    /** 
     *  Record an ACK if not yet present for blocks up to the receiving 
	 *  windowStart sent from the receiving connection.
     */
    public void pseudoAckToReceiverWindow(long wStart) {
        if (wStart <= windowStart) return;
        DataRecord drec;
        for (long i = windowStart; i < wStart; i++) {
            drec = getBlock(i);
            if (drec != null && drec.acks == 0) {
                drec.acks++;
                drec.ackTime = drec.sentTime + (int) rto;
            }
        }
    }

    /** 
     *  Get the oldest un-ACK'ed block.
     */
    public DataRecord getOldestUnackedBlock() {
        DataRecord d;
        DataRecord oldest = null;
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            d = getBlock(i);
            if (d != null) {
                if (d.acks == 0 && (oldest == null || d.sentTime < oldest.sentTime)) {
                    oldest = d;
                }
            }
        }
        return oldest;
    }

    /**
     * Checks if we have at least one block that can be read (in order).
     * Specifically, this will return false if there is at least one null or
     * already-read block nearer to the windowStart than the first non-null non-read
     * block.
     */
    public boolean hasReadableData() {
        return readableData;
    }

    /** 
     *  Get a readable block. This will return the first unread block starting from
     *  windowStart.  
     */
    public DataRecord getReadableBlock() {
        if (LOG.isDebugEnabled()) LOG.debug("wStart " + windowStart + " wSize " + windowSize);
        DataRecord d;
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            d = getBlock(i);
            if (d != null) {
                if (d.read) continue; else return d;
            } else {
                break;
            }
        }
        return null;
    }

    /** 
     *  To advance the window of the reader, higher blocks need to come in.
	 *  Once they do, older read blocks below the new window can be cleared.
	 *  Return the size of the window advancement.
     */
    public int clearEarlyReadBlocks() {
        DataRecord d;
        int count = 0;
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            d = window.get(i);
            if (d != null && d.read) {
                window.remove(i);
                count++;
            } else {
                if (d == null) readableData = false; else readableData = true;
                break;
            }
        }
        windowStart += count;
        return (count);
    }

    /** 
     *  Find the number of unread records
     */
    public int numNotRead() {
        DataRecord d;
        int count = 0;
        for (long i = windowStart; i < windowStart + windowSize + 1; i++) {
            d = getBlock(i);
            if (d != null && !d.read) {
                count++;
            }
        }
        return count;
    }
}

/**
 *  Record information about data messages either getting written to the 
 *  network or getting read from the network. In the first case, the 
 *  ACKs is important. In the second case, the read state is important.  
 *  For writing, the sentTime and the ackTime form the basis for the 
 *  round trip time and a calculation for timeout resends.
 */
class DataRecord {

    final DataMessage msg;

    int sends;

    boolean read;

    int acks;

    long sentTime;

    long ackTime;

    DataRecord(DataMessage msg) {
        this.msg = msg;
    }
}

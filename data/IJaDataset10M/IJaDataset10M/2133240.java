package org.mmtk.policy.immix;

import static org.mmtk.policy.immix.ImmixConstants.*;
import org.mmtk.utility.Constants;
import org.mmtk.vm.VM;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.Extent;
import org.vmmagic.unboxed.Offset;

/**
 * This class defines operations over block-granularity meta-data
 *
 */
@Uninterruptible
public class Block implements Constants {

    static Address align(final Address ptr) {
        return ptr.toWord().and(BLOCK_MASK.not()).toAddress();
    }

    public static boolean isAligned(final Address address) {
        return address.EQ(align(address));
    }

    private static int getChunkIndex(final Address block) {
        return block.toWord().and(CHUNK_MASK).rshl(LOG_BYTES_IN_BLOCK).toInt();
    }

    /***************************************************************************
   * Block marking
   */
    public static boolean isUnused(final Address address) {
        return getBlockMarkState(address) == UNALLOCATED_BLOCK_STATE;
    }

    static boolean isUnusedState(Address cursor) {
        return cursor.loadShort() == UNALLOCATED_BLOCK_STATE;
    }

    static short getMarkState(Address cursor) {
        return cursor.loadShort();
    }

    static void setState(Address cursor, short value) {
        cursor.store(value);
    }

    public static short getBlockMarkState(Address address) {
        return getBlockMarkStateAddress(address).loadShort();
    }

    static void setBlockAsInUse(Address address) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(isUnused(address));
        setBlockState(address, UNMARKED_BLOCK_STATE);
    }

    public static void setBlockAsReused(Address address) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(!isUnused(address));
        setBlockState(address, REUSED_BLOCK_STATE);
    }

    static void setBlockAsUnallocated(Address address) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(!isUnused(address));
        getBlockMarkStateAddress(address).store(UNALLOCATED_BLOCK_STATE);
    }

    private static void setBlockState(Address address, short value) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(value != UNALLOCATED_BLOCK_STATE);
        getBlockMarkStateAddress(address).store(value);
    }

    static Address getBlockMarkStateAddress(Address address) {
        Address chunk = Chunk.align(address);
        int index = getChunkIndex(address);
        Address rtn = chunk.plus(Chunk.BLOCK_STATE_TABLE_OFFSET).plus(index << LOG_BYTES_IN_BLOCK_STATE_ENTRY);
        if (VM.VERIFY_ASSERTIONS) {
            Address block = chunk.plus(index << LOG_BYTES_IN_BLOCK);
            VM.assertions._assert(isAligned(block));
            boolean valid = rtn.GE(chunk.plus(Chunk.BLOCK_STATE_TABLE_OFFSET)) && rtn.LT(chunk.plus(Chunk.BLOCK_STATE_TABLE_OFFSET + BLOCK_STATE_TABLE_BYTES));
            VM.assertions._assert(valid);
        }
        return rtn;
    }

    /***************************************************************************
   * Sweeping
   */
    static short sweepOneBlock(Address block, int[] markHistogram, final byte markState, final boolean resetMarkState) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(isAligned(block));
        final boolean unused = isUnused(block);
        if (unused && !SANITY_CHECK_LINE_MARKS) return 0;
        Address markTable = Line.getBlockMarkTable(block);
        short markCount = 0;
        short conservativeSpillCount = 0;
        byte mark, lastMark = 0;
        for (int offset = 0; offset < (LINES_IN_BLOCK << Line.LOG_BYTES_IN_LINE_STATUS); offset += Line.BYTES_IN_LINE_STATUS) {
            if (VM.VERIFY_ASSERTIONS) {
                VM.assertions._assert(markTable.plus(offset).GE(Chunk.align(block).plus(Chunk.LINE_MARK_TABLE_OFFSET)));
                VM.assertions._assert(markTable.plus(offset).LT(Chunk.align(block).plus(Chunk.LINE_MARK_TABLE_OFFSET + Line.LINE_MARK_TABLE_BYTES)));
            }
            mark = markTable.loadByte(Offset.fromIntZeroExtend(offset));
            if (resetMarkState) markTable.store((byte) (mark == markState ? RESET_LINE_MARK_STATE : 0), Offset.fromIntZeroExtend(offset));
            if (mark == markState) markCount++; else if (lastMark == markState) conservativeSpillCount++; else if (SANITY_CHECK_LINE_MARKS && lastMark != markState) {
                VM.memory.zero(block.plus(offset << (LOG_BYTES_IN_LINE - Line.LOG_BYTES_IN_LINE_STATUS)), Extent.fromIntZeroExtend(BYTES_IN_LINE));
            }
            lastMark = mark;
        }
        if (VM.VERIFY_ASSERTIONS) {
            VM.assertions._assert(markCount <= LINES_IN_BLOCK);
            VM.assertions._assert(markCount + conservativeSpillCount <= LINES_IN_BLOCK);
            VM.assertions._assert(markCount == 0 || !isUnused(block));
        }
        getDefragStateAddress(block).store(conservativeSpillCount);
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(markCount >= conservativeSpillCount);
        markHistogram[conservativeSpillCount] += markCount;
        markCount = (short) (markCount + conservativeSpillCount);
        return markCount;
    }

    /****************************************************************************
   * Block defrag state
   */
    public static boolean isDefragSource(Address address) {
        return getDefragStateAddress(address).loadShort() == BLOCK_IS_DEFRAG_SOURCE;
    }

    static void clearConservativeSpillCount(Address address) {
        getDefragStateAddress(address).store((short) 0);
    }

    static short getConservativeSpillCount(Address address) {
        return getDefragStateAddress(address).loadShort();
    }

    static Address getDefragStateAddress(Address address) {
        Address chunk = Chunk.align(address);
        int index = getChunkIndex(address);
        Address rtn = chunk.plus(Chunk.BLOCK_DEFRAG_STATE_TABLE_OFFSET).plus(index << LOG_BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY);
        if (VM.VERIFY_ASSERTIONS) {
            Address block = chunk.plus(index << LOG_BYTES_IN_BLOCK);
            VM.assertions._assert(isAligned(block));
            boolean valid = rtn.GE(chunk.plus(Chunk.BLOCK_DEFRAG_STATE_TABLE_OFFSET)) && rtn.LT(chunk.plus(Chunk.BLOCK_DEFRAG_STATE_TABLE_OFFSET + BLOCK_DEFRAG_STATE_TABLE_BYTES));
            VM.assertions._assert(valid);
        }
        return rtn;
    }

    static void resetLineMarksAndDefragStateTable(short threshold, Address markStateBase, Address defragStateBase, Address lineMarkBase, int block) {
        Offset csOffset = Offset.fromIntZeroExtend(block << LOG_BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY);
        short state = defragStateBase.loadShort(csOffset);
        short defragState = BLOCK_IS_NOT_DEFRAG_SOURCE;
        if (state >= threshold) defragState = BLOCK_IS_DEFRAG_SOURCE;
        defragStateBase.store(defragState, csOffset);
    }

    private static final short UNALLOCATED_BLOCK_STATE = 0;

    private static final short UNMARKED_BLOCK_STATE = (short) (MAX_BLOCK_MARK_STATE + 1);

    private static final short REUSED_BLOCK_STATE = (short) (MAX_BLOCK_MARK_STATE + 2);

    private static final short BLOCK_IS_NOT_DEFRAG_SOURCE = 0;

    private static final short BLOCK_IS_DEFRAG_SOURCE = 1;

    static final int LOG_BYTES_IN_BLOCK_STATE_ENTRY = LOG_BYTES_IN_SHORT;

    static final int BYTES_IN_BLOCK_STATE_ENTRY = 1 << LOG_BYTES_IN_BLOCK_STATE_ENTRY;

    static final int BLOCK_STATE_TABLE_BYTES = BLOCKS_IN_CHUNK << LOG_BYTES_IN_BLOCK_STATE_ENTRY;

    static final int LOG_BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY = LOG_BYTES_IN_SHORT;

    static final int BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY = 1 << LOG_BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY;

    static final int BLOCK_DEFRAG_STATE_TABLE_BYTES = BLOCKS_IN_CHUNK << LOG_BYTES_IN_BLOCK_DEFRAG_STATE_ENTRY;
}

package org.mmtk.utility.alloc;

import org.mmtk.policy.Space;
import org.mmtk.policy.immix.Block;
import org.mmtk.policy.immix.Chunk;
import org.mmtk.policy.immix.Line;
import org.mmtk.policy.immix.ImmixSpace;
import static org.mmtk.policy.immix.ImmixConstants.*;
import org.mmtk.utility.Constants;
import org.mmtk.utility.Log;
import org.mmtk.utility.options.Options;
import org.mmtk.vm.VM;
import org.vmmagic.unboxed.*;
import org.vmmagic.pragma.*;

/**
 *
 */
@Uninterruptible
public class ImmixAllocator extends Allocator implements Constants {

    /****************************************************************************
   *
   * Instance variables
   */
    protected final ImmixSpace space;

    private final boolean hot;

    private final boolean copy;

    private Address cursor;

    private Address limit;

    private Address largeCursor;

    private Address largeLimit;

    private boolean requestForLarge;

    private boolean straddle;

    private int lineUseCount;

    private Address markTable;

    private Address recyclableBlock;

    private int line;

    private boolean recyclableExhausted;

    /**
   * Constructor.
   *
   * @param space The space to bump point into.
   * @param hot TODO
   * @param copy TODO
   */
    public ImmixAllocator(ImmixSpace space, boolean hot, boolean copy) {
        this.space = space;
        this.hot = hot;
        this.copy = copy;
        reset();
    }

    /**
   * Reset the allocator. Note that this does not reset the space.
   */
    public void reset() {
        cursor = Address.zero();
        limit = Address.zero();
        largeCursor = Address.zero();
        largeLimit = Address.zero();
        markTable = Address.zero();
        recyclableBlock = Address.zero();
        requestForLarge = false;
        recyclableExhausted = false;
        line = LINES_IN_BLOCK;
        lineUseCount = 0;
    }

    /**
   * Allocate space for a new object.  This is frequently executed code and
   * the coding is deliberaetly sensitive to the optimizing compiler.
   * After changing this, always check the IR/MC that is generated.
   *
   * @param bytes The number of bytes allocated
   * @param align The requested alignment
   * @param offset The offset from the alignment
   * @return The address of the first byte of the allocated region
   */
    @Inline
    public final Address alloc(int bytes, int align, int offset) {
        Address start = alignAllocationNoFill(cursor, align, offset);
        Address end = start.plus(bytes);
        if (end.GT(limit)) {
            if (bytes > BYTES_IN_LINE) return overflowAlloc(bytes, align, offset); else return allocSlowHot(bytes, align, offset);
        }
        fillAlignmentGap(cursor, start);
        cursor = end;
        return start;
    }

    /**
   * Allocate space for a new object.  This is frequently executed code and
   * the coding is deliberaetly sensitive to the optimizing compiler.
   * After changing this, always check the IR/MC that is generated.
   *
   * @param bytes The number of bytes allocated
   * @param align The requested alignment
   * @param offset The offset from the alignment
   * @return The address of the first byte of the allocated region
   */
    public final Address overflowAlloc(int bytes, int align, int offset) {
        Address start = alignAllocationNoFill(largeCursor, align, offset);
        Address end = start.plus(bytes);
        if (end.GT(largeLimit)) {
            requestForLarge = true;
            Address rtn = allocSlowInline(bytes, align, offset);
            requestForLarge = false;
            return rtn;
        }
        fillAlignmentGap(largeCursor, start);
        largeCursor = end;
        return start;
    }

    @Inline
    public final boolean getLastAllocLineStraddle() {
        return straddle;
    }

    /**
   * External allocation slow path (called by superclass when slow path is
   * actually taken.  This is necessary (rather than a direct call
   * from the fast path) because of the possibility of a thread switch
   * and corresponding re-association of bump pointers to kernel
   * threads.
   *
   * @param bytes The number of bytes allocated
   * @param align The requested alignment
   * @param offset The offset from the alignment
   * @return The address of the first byte of the allocated region or
   * zero on failure
   */
    protected final Address allocSlowOnce(int bytes, int align, int offset) {
        Address ptr = space.getSpace(hot, copy, lineUseCount);
        if (ptr.isZero()) {
            lineUseCount = 0;
            return ptr;
        }
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(Block.isAligned(ptr));
        lineUseCount = LINES_IN_BLOCK;
        zeroBlock(ptr);
        if (requestForLarge) {
            largeCursor = ptr;
            largeLimit = ptr.plus(BYTES_IN_BLOCK);
        } else {
            cursor = ptr;
            limit = ptr.plus(BYTES_IN_BLOCK);
        }
        return alloc(bytes, align, offset);
    }

    /**
   * Internal allocation slow path.  This is called whenever the bump
   * pointer reaches the internal limit.  The code is forced out of
   * line.  If required we perform an external slow path take, which
   * we inline into this method since this is already out of line.
   *
   * @param bytes The number of bytes allocated
   * @param align The requested alignment
   * @param offset The offset from the alignment
   * @return The address of the first byte of the allocated region
   */
    @NoInline
    private Address allocSlowHot(int bytes, int align, int offset) {
        if (acquireRecyclableLines(bytes, align, offset)) return alloc(bytes, align, offset); else return allocSlowInline(bytes, align, offset);
    }

    private boolean acquireRecyclableLines(int bytes, int align, int offset) {
        while (line < LINES_IN_BLOCK || acquireRecyclableBlock()) {
            line = space.getNextAvailableLine(markTable, line);
            if (line < LINES_IN_BLOCK) {
                int endLine = space.getNextUnavailableLine(markTable, line);
                cursor = recyclableBlock.plus(Extent.fromIntSignExtend(line << LOG_BYTES_IN_LINE));
                limit = recyclableBlock.plus(Extent.fromIntSignExtend(endLine << LOG_BYTES_IN_LINE));
                if (SANITY_CHECK_LINE_MARKS) {
                    Address tmp = cursor;
                    while (tmp.LT(limit)) {
                        if (tmp.loadByte() != (byte) 0) {
                            Log.write("cursor: ");
                            Log.writeln(cursor);
                            Log.write(" limit: ");
                            Log.writeln(limit);
                            Log.write("current: ");
                            Log.write(tmp);
                            Log.write("  value: ");
                            Log.write(tmp.loadByte());
                            Log.write("   line: ");
                            Log.write(line);
                            Log.write("endline: ");
                            Log.write(endLine);
                            Log.write("  chunk: ");
                            Log.write(Chunk.align(cursor));
                            Log.write("     hw: ");
                            Log.write(Chunk.getHighWater(Chunk.align(cursor)));
                            Log.writeln(" values: ");
                            Address tmp2 = cursor;
                            while (tmp2.LT(limit)) {
                                Log.write(tmp2.loadByte());
                                Log.write(" ");
                            }
                            Log.writeln();
                        }
                        VM.assertions._assert(tmp.loadByte() == (byte) 0);
                        tmp = tmp.plus(1);
                    }
                }
                if (VM.VERIFY_ASSERTIONS && bytes <= BYTES_IN_LINE) {
                    Address start = alignAllocationNoFill(cursor, align, offset);
                    Address end = start.plus(bytes);
                    VM.assertions._assert(end.LE(limit));
                }
                VM.memory.zero(cursor, limit.diff(cursor).toWord().toExtent());
                if (VM.VERIFY_ASSERTIONS && Options.verbose.getValue() >= 9) {
                    Log.write("Z[");
                    Log.write(cursor);
                    Log.write("->");
                    Log.write(limit);
                    Log.writeln("]");
                }
                line = endLine;
                if (VM.VERIFY_ASSERTIONS && copy) VM.assertions._assert(!Block.isDefragSource(cursor));
                return true;
            }
        }
        return false;
    }

    private boolean acquireRecyclableBlock() {
        boolean rtn;
        rtn = acquireRecyclableBlockAddressOrder();
        if (rtn) {
            markTable = Line.getBlockMarkTable(recyclableBlock);
            line = 0;
        }
        return rtn;
    }

    @Inline
    private boolean acquireRecyclableBlockAddressOrder() {
        if (recyclableExhausted) {
            if (VM.VERIFY_ASSERTIONS && Options.verbose.getValue() >= 9) {
                Log.writeln("[no recyclable available]");
            }
            return false;
        }
        int markState = 0;
        boolean usable = false;
        while (!usable) {
            Address next = recyclableBlock.plus(BYTES_IN_BLOCK);
            if (recyclableBlock.isZero() || ImmixSpace.isRecycleAllocChunkAligned(next)) {
                recyclableBlock = space.acquireReusableBlocks();
                if (recyclableBlock.isZero()) {
                    recyclableExhausted = true;
                    if (VM.VERIFY_ASSERTIONS && Options.verbose.getValue() >= 9) {
                        Log.writeln("[recyclable exhausted]");
                    }
                    line = LINES_IN_BLOCK;
                    return false;
                }
            } else {
                recyclableBlock = next;
            }
            markState = Block.getBlockMarkState(recyclableBlock);
            usable = (markState > 0 && markState <= ImmixSpace.getReusuableMarkStateThreshold(copy));
            if (copy && Block.isDefragSource(recyclableBlock)) usable = false;
        }
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(!Block.isUnused(recyclableBlock));
        Block.setBlockAsReused(recyclableBlock);
        lineUseCount += (LINES_IN_BLOCK - markState);
        return true;
    }

    private void zeroBlock(Address block) {
        if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(block.toWord().and(Word.fromIntSignExtend(BYTES_IN_BLOCK - 1)).isZero());
        VM.memory.zero(block, Extent.fromIntZeroExtend(BYTES_IN_BLOCK));
    }

    /** @return the space associated with this squish allocator */
    public final Space getSpace() {
        return space;
    }

    /**
   * Print out the status of the allocator (for debugging)
   */
    public final void show() {
        Log.write("cursor = ");
        Log.write(cursor);
        Log.write(" limit = ");
        Log.writeln(limit);
    }
}

package org.mmtk.plan;

import org.mmtk.policy.SegregatedFreeListSpace;
import org.vmmagic.pragma.*;

/**
 * This class and its subclasses communicate to the host VM/Runtime
 * any features of the selected plan that it needs to know.  This is
 * separate from the main Plan/PlanLocal class in order to bypass any
 * issues with ordering of static initialization.
 */
@Uninterruptible
public abstract class PlanConstraints {

    /** @return True if this Plan requires read barriers on java.lang.reference types. */
    public boolean needsJavaLangReferenceReadBarrier() {
        return false;
    }

    /** @return True if this Plan requires write barriers on booleans. */
    public boolean needsBooleanWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on booleans. */
    public boolean needsBooleanReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk boolean arraycopy barriers. */
    public boolean booleanBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on bytes. */
    public boolean needsByteWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on bytes. */
    public boolean needsByteReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk byte arraycopy barriers. */
    public boolean byteBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on chars. */
    public boolean needsCharWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on chars. */
    public boolean needsCharReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk char arraycopy barriers. */
    public boolean charBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on shorts. */
    public boolean needsShortWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on shorts. */
    public boolean needsShortReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk short arraycopy barriers. */
    public boolean shortBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on ints. */
    public boolean needsIntWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on ints. */
    public boolean needsIntReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk int arraycopy barriers. */
    public boolean intBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on longs. */
    public boolean needsLongWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on longs. */
    public boolean needsLongReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk long arraycopy barriers. */
    public boolean longBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on floats. */
    public boolean needsFloatWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on floats. */
    public boolean needsFloatReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk float arraycopy barriers. */
    public boolean floatBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on doubles. */
    public boolean needsDoubleWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on doubles. */
    public boolean needsDoubleReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk double arraycopy barriers. */
    public boolean doubleBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on Words. */
    public boolean needsWordWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on Words. */
    public boolean needsWordReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk Word arraycopy barriers. */
    public boolean wordBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on Address's. */
    public boolean needsAddressWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on Address's. */
    public boolean needsAddressReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk Address arraycopy barriers. */
    public boolean addressBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on Extents. */
    public boolean needsExtentWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on Extents. */
    public boolean needsExtentReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk Extent arraycopy barriers. */
    public boolean extentBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on Offsets. */
    public boolean needsOffsetWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on Offsets. */
    public boolean needsOffsetReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk Offset arraycopy barriers. */
    public boolean offsetBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires write barriers on object references. */
    public boolean needsObjectReferenceWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires read barriers on object references. */
    public boolean needsObjectReferenceReadBarrier() {
        return false;
    }

    /** @return True if this Plan requires non-heap write barriers on object references. */
    public boolean needsObjectReferenceNonHeapWriteBarrier() {
        return false;
    }

    /** @return True if this Plan requires non-heap read barriers on object references. */
    public boolean needsObjectReferenceNonHeapReadBarrier() {
        return false;
    }

    /** @return True if this Plan can perform bulk object arraycopy barriers. */
    public boolean objectReferenceBulkCopySupported() {
        return false;
    }

    /** @return True if this Plan requires linear scanning. */
    public boolean needsLinearScan() {
        return org.mmtk.utility.Constants.SUPPORT_CARD_SCANNING;
    }

    /** @return True if this Plan moves objects. */
    public boolean movesObjects() {
        return false;
    }

    /** @return Size (in bytes) beyond which new regular objects must be allocated to the LOS */
    public int maxNonLOSDefaultAllocBytes() {
        return org.mmtk.utility.Constants.MAX_INT;
    }

    /** @return Size (in bytes) beyond which new non-moving objects must be allocated to the LOS */
    public int maxNonLOSNonMovingAllocBytes() {
        return SegregatedFreeListSpace.MAX_FREELIST_OBJECT_BYTES;
    }

    /** @return Size (in bytes) beyond which copied objects must be copied to the LOS */
    public int maxNonLOSCopyBytes() {
        return org.mmtk.utility.Constants.MAX_INT;
    }

    /** @return True if this object forwards objects <i>after</i>
   * determining global object liveness (e.g. many compacting collectors). */
    public boolean needsForwardAfterLiveness() {
        return false;
    }

    /** @return Is this plan generational in nature. */
    public boolean generational() {
        return false;
    }

    /** @return The number of header bits that are required. */
    public abstract int gcHeaderBits();

    /** @return The number of header words that are required. */
    public abstract int gcHeaderWords();

    /** @return True if this plan contains GCspy. */
    public boolean withGCspy() {
        return false;
    }

    /** @return True if this plan contains GCTrace. */
    public boolean generateGCTrace() {
        return false;
    }

    /** @return The specialized scan methods required */
    public int numSpecializedScans() {
        return 0;
    }

    /** @return True if this plan requires concurrent worker threads */
    public boolean needsConcurrentWorkers() {
        return false;
    }

    /** @return True if this Plan requires a header bit for object logging */
    public boolean needsLogBitInHeader() {
        return false;
    }
}

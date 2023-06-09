package org.mmtk.plan.refcount.backuptrace;

import org.mmtk.plan.TransitiveClosure;
import org.mmtk.plan.refcount.RCBase;
import org.mmtk.plan.refcount.RCHeader;
import org.mmtk.policy.Space;
import org.mmtk.vm.VM;
import org.vmmagic.pragma.*;
import org.vmmagic.unboxed.*;

/**
 * This class is the fundamental mechanism for performing a
 * transitive closure over an object graph.<p>
 *
 * @see org.mmtk.plan.TraceLocal
 */
@Uninterruptible
public final class BTDecMarkedAndZero extends TransitiveClosure {

    /**
   * Trace an edge during GC.
   *
   * @param source The source of the reference.
   * @param slot The location containing the object reference.
   */
    @Inline
    public void processEdge(ObjectReference source, Address slot) {
        ObjectReference object = slot.loadObjectReference();
        slot.store(ObjectReference.nullReference());
        if (!object.isNull()) {
            if ((Space.isInSpace(RCBase.REF_COUNT, object) && RCBase.rcSpace.isLive(object)) || Space.isInSpace(RCBase.REF_COUNT_LOS, object) || Space.isInSpace(RCBase.IMMORTAL, object)) {
                int result = RCHeader.decRC(object);
                if (VM.VERIFY_ASSERTIONS) VM.assertions._assert(result != RCHeader.DEC_KILL || !RCHeader.isMarked(object));
            }
        }
    }
}

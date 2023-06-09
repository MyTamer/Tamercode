package org.jikesrvm.ppc;

import org.jikesrvm.runtime.VM_Magic;
import org.vmmagic.pragma.NoInline;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.unboxed.Address;

/**
 * Machine specific helper functions for dynamic linking.
 */
@Uninterruptible
public abstract class VM_DynamicLinkerHelper implements VM_RegisterConstants {

    /**
   * Reach up two stack frames into a frame that is compiled
   * with the DynamicBridge register protocol and grap
   * the receiver object of the invoke (ie the first param).
   * NOTE: assumes that caller has disabled GC.
   */
    @NoInline
    public static Object getReceiverObject() {
        Address callingFrame = VM_Magic.getCallerFramePointer(VM_Magic.getFramePointer());
        callingFrame = VM_Magic.getCallerFramePointer(callingFrame);
        callingFrame = VM_Magic.getCallerFramePointer(callingFrame);
        Address location = callingFrame.minus((LAST_NONVOLATILE_FPR - FIRST_VOLATILE_FPR + 1) * BYTES_IN_DOUBLE + (LAST_NONVOLATILE_GPR - FIRST_VOLATILE_GPR + 1) * BYTES_IN_ADDRESS);
        return VM_Magic.addressAsObject(location.loadAddress());
    }
}

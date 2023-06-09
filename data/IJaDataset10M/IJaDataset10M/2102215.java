package com.ibm.JikesRVM.memoryManagers.JMTk;

import com.ibm.JikesRVM.memoryManagers.vmInterface.VM_Interface;
import com.ibm.JikesRVM.memoryManagers.vmInterface.Constants;
import com.ibm.JikesRVM.VM_Uninterruptible;
import com.ibm.JikesRVM.VM_PragmaInline;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Magic;

/**
 * This class implements tracing functionality for a simple copying
 * space.  Since no state needs to be held globally or locally, all
 * methods are static.
 *
 * @author Perry Cheng
 * @author <a href="http://cs.anu.edu.au/~Steve.Blackburn">Steve Blackburn</a>
 * @version $Revision: 3566 $
 * @date $Date: 2003-05-22 16:51:25 -0400 (Thu, 22 May 2003) $
 */
final class CopySpace extends BasePolicy implements Constants, VM_Uninterruptible {

    public static final String Id = "$Id: CopySpace.java 3566 2003-05-22 20:51:25Z dgrove-oss $";

    public static void prepare(VMResource vm, MemoryResource mr) {
    }

    public static void release(VMResource vm, MemoryResource mr) {
    }

    /**
   * Trace an object under a copying collection policy.
   * If the object is already copied, the copy is returned.
   * Otherwise, a copy is created and returned.
   * In either case, the object will be marked on return.
   *
   * @param object The object to be copied.
   */
    public static VM_Address traceObject(VM_Address object) throws VM_PragmaInline {
        int forwardingPtr = CopyingHeader.attemptToForward(object);
        VM_Magic.isync();
        if (CopyingHeader.stateIsForwardedOrBeingForwarded(forwardingPtr)) {
            while (CopyingHeader.stateIsBeingForwarded(forwardingPtr)) forwardingPtr = CopyingHeader.getForwardingWord(object);
            VM_Magic.isync();
            VM_Address newObject = VM_Address.fromInt(forwardingPtr & ~CopyingHeader.GC_FORWARDING_MASK);
            return newObject;
        }
        VM_Address newObject = VM_Interface.copy(object, forwardingPtr);
        CopyingHeader.setForwardingPointer(object, newObject);
        Plan.enqueue(newObject);
        return newObject;
    }

    public static boolean isLive(VM_Address obj) {
        return CopyingHeader.isForwarded(VM_Magic.addressAsObject(obj));
    }
}

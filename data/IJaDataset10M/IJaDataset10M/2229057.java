package com.ibm.JikesRVM.memoryManagers.watson;

import com.ibm.JikesRVM.classloader.*;
import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Constants;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_Magic;
import com.ibm.JikesRVM.VM_ObjectModel;
import com.ibm.JikesRVM.VM_PragmaInline;
import com.ibm.JikesRVM.VM_PragmaNoInline;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;
import com.ibm.JikesRVM.VM_PragmaLogicallyUninterruptible;
import com.ibm.JikesRVM.VM_Scheduler;
import com.ibm.JikesRVM.VM_Memory;
import com.ibm.JikesRVM.VM_Time;
import com.ibm.JikesRVM.VM_Entrypoints;
import com.ibm.JikesRVM.VM_Reflection;
import com.ibm.JikesRVM.VM_Synchronization;
import com.ibm.JikesRVM.VM_Synchronizer;
import com.ibm.JikesRVM.VM_EventLogger;

/**
 * Defines utility routines for manipulating the various GC header bits.  
 * NOTE: Not all of these routines are meaningful for every collector!
 * 
 * @see VM_ObjectModel
 * @see VM_AllocatorHeader
 * @see VM_AllocatorHeaderConstants
 * 
 * @author David Bacon
 * @author Steve Fink
 * @author Dave Grove
 */
public class VM_CommonAllocatorHeader implements VM_AllocatorHeaderConstants {

    public static final int GC_MARK_BIT_MASK = 0x1;

    public static final int GC_BARRIER_BIT_MASK = (1 << GC_BARRIER_BIT_IDX);

    /**
   * test to see if the barrier bit is set
   */
    public static boolean testBarrierBit(Object ref) throws VM_PragmaUninterruptible {
        return VM_ObjectModel.testAvailableBit(ref, GC_BARRIER_BIT_IDX);
    }

    /**
   * clear the barrier bit (indicates that object is in write buffer)
   */
    static void clearBarrierBit(Object ref) throws VM_PragmaUninterruptible {
        VM_ObjectModel.setAvailableBit(ref, GC_BARRIER_BIT_IDX, false);
    }

    /**
   * set the barrier bit (indicates that object needs to be put in write buffer
   * if a reference is stored into it).
   */
    static void setBarrierBit(Object ref) throws VM_PragmaUninterruptible {
        VM_ObjectModel.setAvailableBit(ref, GC_BARRIER_BIT_IDX, true);
    }

    /**
   * test to see if the mark bit has the given value
   */
    static boolean testMarkBit(Object ref, int value) throws VM_PragmaUninterruptible {
        return (VM_ObjectModel.readAvailableBitsWord(ref) & value) != 0;
    }

    /**
   * write the given value in the mark bit.
   */
    static void writeMarkBit(Object ref, int value) throws VM_PragmaUninterruptible {
        int oldValue = VM_ObjectModel.readAvailableBitsWord(ref);
        int newValue = (oldValue & ~GC_MARK_BIT_MASK) | value;
        VM_ObjectModel.writeAvailableBitsWord(ref, newValue);
    }

    /**
   * atomically write the given value in the mark bit.
   */
    static void atomicWriteMarkBit(Object ref, int value) throws VM_PragmaUninterruptible {
        while (true) {
            int oldValue = VM_ObjectModel.prepareAvailableBits(ref);
            int newValue = (oldValue & ~GC_MARK_BIT_MASK) | value;
            if (VM_ObjectModel.attemptAvailableBits(ref, oldValue, newValue)) break;
        }
    }

    /**
   * used to mark boot image objects during a parallel scan of objects during GC
   */
    static boolean testAndMark(Object ref, int value) throws VM_PragmaUninterruptible {
        int oldValue;
        do {
            oldValue = VM_ObjectModel.prepareAvailableBits(ref);
            int markBit = oldValue & GC_MARK_BIT_MASK;
            if (markBit == value) return false;
        } while (!VM_ObjectModel.attemptAvailableBits(ref, oldValue, oldValue ^ GC_MARK_BIT_MASK));
        return true;
    }

    static final int GC_FORWARDING_MASK = GC_FORWARDED | GC_BEING_FORWARDED;

    /**
   * Either return the forwarding pointer 
   * if the object is already forwarded (or being forwarded)
   * or write the bit pattern that indicates that the object is being forwarded
   */
    static int attemptToForward(Object base) throws VM_PragmaInline, VM_PragmaUninterruptible {
        int oldValue;
        do {
            oldValue = VM_ObjectModel.prepareAvailableBits(base);
            if ((oldValue & GC_FORWARDING_MASK) == GC_FORWARDED) return oldValue;
        } while (!VM_ObjectModel.attemptAvailableBits(base, oldValue, oldValue | GC_BEING_FORWARDED));
        return oldValue;
    }

    /**
   * Non-atomic read of forwarding pointer word
   */
    static int getForwardingWord(Object base) throws VM_PragmaUninterruptible {
        return VM_ObjectModel.readAvailableBitsWord(base);
    }

    /**
   * Has the object been forwarded?
   */
    static boolean isForwarded(Object base) throws VM_PragmaUninterruptible {
        return stateIsForwarded(getForwardingWord(base));
    }

    /**
   * Has the object been forwarded?
   */
    static boolean isBeingForwarded(Object base) throws VM_PragmaUninterruptible {
        return stateIsBeingForwarded(getForwardingWord(base));
    }

    /**
   * is the state of the forwarding word forwarded?
   */
    static boolean stateIsForwarded(int fw) throws VM_PragmaUninterruptible {
        return (fw & GC_FORWARDING_MASK) == GC_FORWARDED;
    }

    /**
   * is the state of the forwarding word being forwarded?
   */
    static boolean stateIsBeingForwarded(int fw) throws VM_PragmaUninterruptible {
        return (fw & GC_FORWARDING_MASK) == GC_BEING_FORWARDED;
    }

    /**
   * is the state of the forwarding word being forwarded?
   */
    static boolean stateIsForwardedOrBeingForwarded(int fw) throws VM_PragmaUninterruptible {
        return (fw & GC_FORWARDED) != 0;
    }

    /**
   * Non-atomic read of forwarding pointer word
   */
    static Object getForwardingPointer(Object base) throws VM_PragmaUninterruptible {
        int forwarded = getForwardingWord(base);
        return VM_Magic.addressAsObject(VM_Address.fromInt(forwarded & ~GC_FORWARDING_MASK));
    }

    /**
   * Non-atomic write of forwarding pointer word
   * (assumption, thread doing the set has done attempt to forward
   *  and owns the right to copy the object)
   */
    static void setForwardingPointer(Object base, Object ptr) throws VM_PragmaUninterruptible {
        VM_ObjectModel.writeAvailableBitsWord(base, VM_Magic.objectAsAddress(ptr).toInt() | GC_FORWARDED);
    }
}

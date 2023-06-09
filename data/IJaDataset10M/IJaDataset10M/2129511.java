package org.mmtk.utility;

import com.ibm.JikesRVM.VM_Magic;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_PragmaInline;
import com.ibm.JikesRVM.VM_Uninterruptible;

/**
 * A pointer enumeration class.  This class is used by the trial
 * deletion cycle detector to perform transitive closure of its
 * "collect" phase.
 *
 * @author <a href="http://cs.anu.edu.au/~Steve.Blackburn">Steve Blackburn</a>
 * @version $Revision: 6395 $
 * @date $date: $
 */
class TDCollectEnumerator extends Enumerate implements VM_Uninterruptible {

    private TrialDeletion td;

    /**
   * Constructor.
   *
   * @param plan The plan instance with respect to which the
   * enumeration will occur.
   */
    TDCollectEnumerator(TrialDeletion td) {
        this.td = td;
    }

    /**
   * Enumerate a pointer.  In this case it is a collect event.
   *
   * @param location The address of the field being enumerated.
   */
    public void enumeratePointerLocation(VM_Address objLoc) throws VM_PragmaInline {
        td.enumerateCollect(VM_Magic.getMemoryAddress(objLoc));
    }
}

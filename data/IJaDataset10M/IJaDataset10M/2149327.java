package org.jikesrvm;

import org.vmmagic.pragma.*;
import org.jikesrvm.ArchitectureSpecific.VM_BaselineConstants;

/**
 * Scratch space for JSR processing.  Used from VM_ReferenceMaps
 * 
 * @author Anthony Cocchi
 * @modified Perry Cheng
 * @modified Dave Grove
 */
@Uninterruptible
public final class VM_JSRInfo implements VM_BaselineConstants {

    int numberUnusualMaps;

    VM_UnusualMaps[] unusualMaps;

    byte[] unusualReferenceMaps;

    int freeMapSlot = 0;

    VM_UnusualMaps extraUnusualMap = new VM_UnusualMaps();

    int tempIndex = 0;

    int mergedReferenceMap = 0;

    int mergedReturnAddressMap = 0;

    VM_JSRInfo(int initialMaps) {
        unusualMaps = new VM_UnusualMaps[initialMaps];
    }

    /**
   * show the basic information for each of the unusual maps
   *    this is for testing use
   */
    public void showUnusualMapInfo(int bytesPerMap) {
        VM.sysWrite("-------------------------------------------------\n");
        VM.sysWriteln("     numberUnusualMaps = ", numberUnusualMaps);
        for (int i = 0; i < numberUnusualMaps; i++) {
            VM.sysWrite("-----------------\n");
            VM.sysWrite("Unusual map #", i);
            VM.sysWrite(":\n");
            unusualMaps[i].showInfo();
            VM.sysWrite("    -- reference Map:   ");
            showAnUnusualMap(unusualMaps[i].getReferenceMapIndex(), bytesPerMap);
            VM.sysWrite("\n");
            VM.sysWrite("    -- non-reference Map:   ");
            showAnUnusualMap(unusualMaps[i].getNonReferenceMapIndex(), bytesPerMap);
            VM.sysWrite("\n");
            VM.sysWrite("    -- returnAddress Map:   ");
            showAnUnusualMap(unusualMaps[i].getReturnAddressMapIndex(), bytesPerMap);
            VM.sysWrite("\n");
        }
        VM.sysWrite("------ extraUnusualMap:   ");
        extraUnusualMap.showInfo();
        showAnUnusualMap(extraUnusualMap.getReferenceMapIndex(), bytesPerMap);
        showAnUnusualMap(extraUnusualMap.getNonReferenceMapIndex(), bytesPerMap);
        showAnUnusualMap(extraUnusualMap.getReturnAddressMapIndex(), bytesPerMap);
        VM.sysWrite("\n");
    }

    /**
   * show the basic information for a single unusualmap
   *    this is for testing use
   */
    public void showAnUnusualMap(int mapIndex, int bytesPerMap) {
        VM.sysWrite("unusualMap with index = ", mapIndex);
        VM.sysWrite("   Map bytes =  ");
        for (int i = 0; i < bytesPerMap; i++) {
            VM.sysWrite(unusualReferenceMaps[mapIndex + i]);
            VM.sysWrite("   ");
        }
        VM.sysWrite("   ");
    }
}

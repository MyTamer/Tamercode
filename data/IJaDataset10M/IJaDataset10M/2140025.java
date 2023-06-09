package com.android.dx.ssa.back;

import com.android.dx.ssa.SsaMethod;
import com.android.dx.ssa.SsaBasicBlock;
import com.android.dx.ssa.SsaInsn;
import com.android.dx.ssa.PhiInsn;
import com.android.dx.ssa.SetFactory;
import com.android.dx.rop.code.RegisterSpec;
import com.android.dx.util.IntSet;
import com.android.dx.util.BitIntSet;
import com.android.dx.util.ListIntSet;
import java.util.BitSet;
import java.util.List;
import java.util.ArrayList;

/**
 * A register interference graph
 */
public class InterferenceGraph {

    /**
     * {@code non-null;} interference graph, indexed by register in
     * both dimensions
     */
    private final ArrayList<IntSet> interference;

    /**
     * Creates a new graph.
     *
     * @param countRegs {@code >= 0;} the start count of registers in
     * the namespace. New registers can be added subsequently.
     */
    public InterferenceGraph(int countRegs) {
        interference = new ArrayList<IntSet>(countRegs);
        for (int i = 0; i < countRegs; i++) {
            interference.add(SetFactory.makeInterferenceSet(countRegs));
        }
    }

    /**
     * Adds a register pair to the interference/liveness graph. Parameter
     * order is insignificant.
     *
     * @param regV one register index
     * @param regW another register index
     */
    public void add(int regV, int regW) {
        ensureCapacity(Math.max(regV, regW) + 1);
        interference.get(regV).add(regW);
        interference.get(regW).add(regV);
    }

    /**
     * Dumps interference graph to stdout for debugging.
     */
    public void dumpToStdout() {
        int oldRegCount = interference.size();
        for (int i = 0; i < oldRegCount; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("Reg " + i + ":" + interference.get(i).toString());
            System.out.println(sb.toString());
        }
    }

    /**
     * Merges the interference set for a register into a given bit set
     *
     * @param reg {@code >= 0;} register
     * @param set {@code non-null;} interference set; will be merged
     * with set for given register
     */
    public void mergeInterferenceSet(int reg, IntSet set) {
        if (reg < interference.size()) {
            set.merge(interference.get(reg));
        }
    }

    /**
     * Ensures that the interference graph is appropriately sized.
     *
     * @param size requested minumum size
     */
    private void ensureCapacity(int size) {
        int countRegs = interference.size();
        interference.ensureCapacity(size);
        for (int i = countRegs; i < size; i++) {
            interference.add(SetFactory.makeInterferenceSet(size));
        }
    }
}

package com.ibm.wala.ssa;

import com.ibm.wala.util.collections.Pair;

/**
 * A pi node policy with the following rule:
 * 
 * If we have the following code:
 * <p>
 * <verbatim> S1: c = v1 instanceof T S2: if (c == 0) { ... } </verbatim>
 * 
 * replace it with:
 * <p>
 * <verbatim> S1: c = v1 instanceof T S2: if (c == 0) { v2 = PI(v1, S1) .... } </verbatim>
 * 
 * The same pattern holds if the test is c == 1. This renaming allows SSA-based analysis to reason about the type of v2 depending on
 * the outcome of the branch.
 */
public class InstanceOfPiPolicy implements SSAPiNodePolicy {

    private static final InstanceOfPiPolicy singleton = new InstanceOfPiPolicy();

    public static InstanceOfPiPolicy createInstanceOfPiPolicy() {
        return singleton;
    }

    private InstanceOfPiPolicy() {
    }

    public Pair<Integer, SSAInstruction> getPi(SSAConditionalBranchInstruction cond, SSAInstruction def1, SSAInstruction def2, SymbolTable symbolTable) {
        if (def1 instanceof SSAInstanceofInstruction) {
            if (symbolTable.isBooleanOrZeroOneConstant(cond.getUse(1))) {
                return Pair.make(def1.getUse(0), def1);
            }
        }
        if (def2 instanceof SSAInstanceofInstruction) {
            if (symbolTable.isBooleanOrZeroOneConstant(cond.getUse(0))) {
                return Pair.make(def2.getUse(0), def2);
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return 12;
    }

    public Pair<Integer, SSAInstruction> getPi(SSAAbstractInvokeInstruction call, SymbolTable symbolTable) {
        return null;
    }
}

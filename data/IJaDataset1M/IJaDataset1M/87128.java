package com.ibm.wala.cast.js.ssa;

import java.util.Collection;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAInstructionFactory;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.types.TypeReference;

public class JavaScriptWithRegion extends SSAInstruction {

    private final int expr;

    private final boolean isEnter;

    public JavaScriptWithRegion(int expr, boolean isEnter) {
        this.expr = expr;
        this.isEnter = isEnter;
    }

    @Override
    public SSAInstruction copyForSSA(SSAInstructionFactory insts, int[] defs, int[] uses) {
        return ((JSInstructionFactory) insts).WithRegion(uses == null ? expr : uses[0], isEnter);
    }

    @Override
    public Collection<TypeReference> getExceptionTypes() {
        return null;
    }

    @Override
    public int hashCode() {
        return 353456 * expr * (isEnter ? 1 : -1);
    }

    @Override
    public boolean isFallThrough() {
        return true;
    }

    @Override
    public String toString(SymbolTable symbolTable) {
        return (isEnter ? "enter" : "exit") + " of with " + getValueString(symbolTable, expr);
    }

    @Override
    public void visit(IVisitor v) {
        ((JSInstructionVisitor) v).visitWithRegion(this);
    }

    public int getNumberOfUses() {
        return 1;
    }

    public int getUse(int i) {
        assert i == 0;
        return expr;
    }
}

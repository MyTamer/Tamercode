package org.jikesrvm.opt.ir;

import org.jikesrvm.ArchitectureSpecific.OPT_PhysicalRegisterSet;

/**
 * Represents a symbolic or physical register. 
 * OPT_Registers are shared among all OPT_Operands -- for a given register 
 * pool, there is only one instance of an OPT_Register with each number.
 * 
 * @see OPT_RegisterOperand
 * @see org.jikesrvm.ArchitectureSpecific.OPT_RegisterPool
 * 
 * @author Mauricio Serrano
 * @author John Whaley
 * @modified Stephen Fink
 * @modified Dave Grove
 * @modified Vivek Sarkar
 */
public final class OPT_Register {

    /**
   * Index number relative to register pool.
   */
    public final int number;

    /**
   * Encoding of register properties & scratch bits
   */
    private int flags;

    private static final int LOCAL = 0x00001;

    private static final int SPAN_BASIC_BLOCK = 0x00002;

    private static final int SSA = 0x00004;

    private static final int SEEN_USE = 0x00008;

    private static final int PHYSICAL = 0x00010;

    private static final int TYPE_SHIFT = 6;

    private static final int ADDRESS = 0x00040;

    private static final int INTEGER = 0x00080;

    private static final int FLOAT = 0x00100;

    private static final int DOUBLE = 0x00200;

    private static final int CONDITION = 0x00400;

    private static final int LONG = 0x00800;

    private static final int VALIDATION = 0x01000;

    private static final int VOLATILE = 0x02000;

    private static final int NON_VOLATILE = 0x04000;

    private static final int EXCLUDE_LIVEANAL = 0x08000;

    private static final int SPILLED = 0x10000;

    private static final int TOUCHED = 0x20000;

    private static final int ALLOCATED = 0x40000;

    private static final int PINNED = 0x80000;

    private static final int TYPE_MASK = (ADDRESS | INTEGER | FLOAT | DOUBLE | CONDITION | LONG | VALIDATION);

    public static final int ADDRESS_TYPE = ADDRESS >>> TYPE_SHIFT;

    public static final int INTEGER_TYPE = INTEGER >>> TYPE_SHIFT;

    public static final int FLOAT_TYPE = FLOAT >>> TYPE_SHIFT;

    public static final int DOUBLE_TYPE = DOUBLE >>> TYPE_SHIFT;

    public static final int CONDITION_TYPE = CONDITION >>> TYPE_SHIFT;

    public static final int LONG_TYPE = LONG >>> TYPE_SHIFT;

    public static final int VALIDATION_TYPE = VALIDATION >>> TYPE_SHIFT;

    public boolean isTemp() {
        return (flags & LOCAL) == 0;
    }

    public boolean isLocal() {
        return (flags & LOCAL) != 0;
    }

    public boolean spansBasicBlock() {
        return (flags & SPAN_BASIC_BLOCK) != 0;
    }

    public boolean isSSA() {
        return (flags & SSA) != 0;
    }

    public boolean seenUse() {
        return (flags & SEEN_USE) != 0;
    }

    public boolean isPhysical() {
        return (flags & PHYSICAL) != 0;
    }

    public boolean isSymbolic() {
        return (flags & PHYSICAL) == 0;
    }

    public boolean isAddress() {
        return (flags & ADDRESS) != 0;
    }

    public boolean isInteger() {
        return (flags & INTEGER) != 0;
    }

    public boolean isLong() {
        return (flags & LONG) != 0;
    }

    public boolean isNatural() {
        return (flags & (INTEGER | LONG | ADDRESS)) != 0;
    }

    public boolean isFloat() {
        return (flags & FLOAT) != 0;
    }

    public boolean isDouble() {
        return (flags & DOUBLE) != 0;
    }

    public boolean isFloatingPoint() {
        return (flags & (FLOAT | DOUBLE)) != 0;
    }

    public boolean isCondition() {
        return (flags & CONDITION) != 0;
    }

    public boolean isValidation() {
        return (flags & VALIDATION) != 0;
    }

    public boolean isExcludedLiveA() {
        return (flags & EXCLUDE_LIVEANAL) != 0;
    }

    public int getType() {
        return (flags & TYPE_MASK) >>> TYPE_SHIFT;
    }

    public boolean isVolatile() {
        return (flags & VOLATILE) != 0;
    }

    public boolean isNonVolatile() {
        return (flags & NON_VOLATILE) != 0;
    }

    public void setLocal() {
        flags |= LOCAL;
    }

    public void setSpansBasicBlock() {
        flags |= SPAN_BASIC_BLOCK;
    }

    public void setSSA() {
        flags |= SSA;
    }

    public void setSeenUse() {
        flags |= SEEN_USE;
    }

    public void setPhysical() {
        flags |= PHYSICAL;
    }

    public void setAddress() {
        flags |= ADDRESS;
    }

    public void setInteger() {
        flags |= INTEGER;
    }

    public void setFloat() {
        flags |= FLOAT;
    }

    public void setDouble() {
        flags |= DOUBLE;
    }

    public void setLong() {
        flags |= LONG;
    }

    public void setCondition() {
        flags = (flags & ~TYPE_MASK) | CONDITION;
    }

    public void setValidation() {
        flags |= VALIDATION;
    }

    public void setExcludedLiveA() {
        flags |= EXCLUDE_LIVEANAL;
    }

    public void setVolatile() {
        flags |= VOLATILE;
    }

    public void setNonVolatile() {
        flags |= NON_VOLATILE;
    }

    public void putSSA(boolean a) {
        if (a) setSSA(); else clearSSA();
    }

    public void putSpansBasicBlock(boolean a) {
        if (a) setSpansBasicBlock(); else clearSpansBasicBlock();
    }

    public void clearLocal() {
        flags &= ~LOCAL;
    }

    public void clearSpansBasicBlock() {
        flags &= ~SPAN_BASIC_BLOCK;
    }

    public void clearSSA() {
        flags &= ~SSA;
    }

    public void clearSeenUse() {
        flags &= ~SEEN_USE;
    }

    public void clearPhysical() {
        flags &= ~PHYSICAL;
    }

    public void clearAddress() {
        flags &= ~ADDRESS;
    }

    public void clearInteger() {
        flags &= ~INTEGER;
    }

    public void clearFloat() {
        flags &= ~FLOAT;
    }

    public void clearDouble() {
        flags &= ~DOUBLE;
    }

    public void clearLong() {
        flags &= ~LONG;
    }

    public void clearCondition() {
        flags &= ~CONDITION;
    }

    public void clearType() {
        flags &= ~TYPE_MASK;
    }

    public void clearValidation() {
        flags &= ~VALIDATION;
    }

    public Object scratchObject;

    /** 
   * Used in dependence graph construction.
   */
    public void setdNode(org.jikesrvm.opt.OPT_DepGraphNode a) {
        scratchObject = a;
    }

    public org.jikesrvm.opt.OPT_DepGraphNode dNode() {
        return (org.jikesrvm.opt.OPT_DepGraphNode) scratchObject;
    }

    /**
   * Used to store register lists.
   * Computed on demand by OPT_IR.computeDU().
   */
    public OPT_RegisterOperand defList, useList;

    /** 
   * This accessor is only valid when register lists are valid 
   */
    public OPT_Instruction getFirstDef() {
        if (defList == null) return null; else return defList.instruction;
    }

    /**
   * The number of uses; used by flow-insensitive optimizations
   */
    public int useCount;

    /**
   * A field optimizations can use as they choose
   */
    public int scratch;

    public OPT_Register(int Number) {
        number = Number;
    }

    public int getNumber() {
        int start = OPT_PhysicalRegisterSet.getSize();
        return number - start;
    }

    /**
   * Returns the string representation of this register.
   */
    public String toString() {
        if (isPhysical()) return OPT_PhysicalRegisterSet.getName(number);
        String s = isLocal() ? "l" : "t";
        s = s + getNumber() + (spansBasicBlock() ? "p" : "") + (isSSA() ? "s" : "") + typeName();
        return s;
    }

    public String typeName() {
        String s = "";
        if (isCondition()) s += "c";
        if (isAddress()) s += "a";
        if (isInteger()) s += "i";
        if (isDouble()) s += "d";
        if (isFloat()) s += "f";
        if (isLong()) s += "l";
        if (isValidation()) s += "v";
        if (s == null) s = "_";
        return s;
    }

    public OPT_Register mapsToRegister;

    public void clearAllocationFlags() {
        flags &= ~(PINNED | TOUCHED | ALLOCATED | SPILLED);
    }

    public void pinRegister() {
        flags |= PINNED | TOUCHED;
    }

    public void reserveRegister() {
        flags |= PINNED;
    }

    public void touchRegister() {
        flags |= TOUCHED;
    }

    public void allocateRegister() {
        flags = (flags & ~SPILLED) | (ALLOCATED | TOUCHED);
    }

    public void allocateRegister(OPT_Register reg) {
        flags = (flags & ~SPILLED) | (ALLOCATED | TOUCHED);
        mapsToRegister = reg;
    }

    public void allocateToRegister(OPT_Register reg) {
        this.allocateRegister(reg);
        reg.allocateRegister(this);
    }

    public void deallocateRegister() {
        flags &= ~ALLOCATED;
        mapsToRegister = null;
    }

    public void freeRegister() {
        deallocateRegister();
        OPT_Register symbReg = mapsToRegister;
        if (symbReg != null) symbReg.clearSpill();
    }

    public void spillRegister() {
        flags = (flags & ~ALLOCATED) | SPILLED;
    }

    public void clearSpill() {
        flags &= ~SPILLED;
    }

    public void unpinRegister() {
        flags &= ~PINNED;
    }

    public boolean isTouched() {
        return (flags & TOUCHED) != 0;
    }

    public boolean isAllocated() {
        return (flags & ALLOCATED) != 0;
    }

    public boolean isSpilled() {
        return (flags & SPILLED) != 0;
    }

    public boolean isPinned() {
        return (flags & PINNED) != 0;
    }

    public boolean isAvailable() {
        return (flags & (ALLOCATED | PINNED)) == 0;
    }

    public OPT_Register getRegisterAllocated() {
        return mapsToRegister;
    }

    public int getSpillAllocated() {
        return scratch;
    }

    public int hashCode() {
        return number;
    }

    OPT_Register next, prev;

    public OPT_Register getNext() {
        return next;
    }

    void setNext(OPT_Register e) {
        next = e;
    }

    public OPT_Register getPrev() {
        return prev;
    }

    public void linkWithNext(OPT_Register Next) {
        next = Next;
        Next.prev = this;
    }

    void append(OPT_Register l) {
        next = l;
        l.prev = this;
    }

    OPT_Register remove() {
        OPT_Register Prev = prev, Next = next;
        if (Prev != null) Prev.next = Next;
        if (Next != null) Next.prev = Prev;
        return Next;
    }
}

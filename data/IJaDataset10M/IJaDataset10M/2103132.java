package org.jikesrvm.opt;

import org.jikesrvm.*;
import org.jikesrvm.opt.ir.*;
import org.vmmagic.unboxed.*;

/**
 * Contains common BURS helper functions for platforms with memory operands.
 * 
 * @author Dave Grove
 * @author Stephen Fink
 */
public abstract class OPT_BURS_MemOp_Helpers extends OPT_BURS_Common_Helpers {

    protected static final byte B = 0x01;

    protected static final byte W = 0x02;

    protected static final byte DW = 0x04;

    protected static final byte QW = 0x08;

    protected static final byte B_S = 0x00;

    protected static final byte W_S = 0x01;

    protected static final byte DW_S = 0x02;

    protected static final byte QW_S = 0x03;

    protected OPT_BURS_MemOp_Helpers(OPT_BURS burs) {
        super(burs);
    }

    protected final int ADDRESS_EQUAL(OPT_Instruction store, OPT_Instruction load, int trueCost) {
        return ADDRESS_EQUAL(store, load, trueCost, INFINITE);
    }

    protected final int ADDRESS_EQUAL(OPT_Instruction store, OPT_Instruction load, int trueCost, int falseCost) {
        if (Store.getAddress(store).similar(Load.getAddress(load)) && Store.getOffset(store).similar(Load.getOffset(load))) {
            return trueCost;
        } else {
            return falseCost;
        }
    }

    protected final int ARRAY_ADDRESS_EQUAL(OPT_Instruction store, OPT_Instruction load, int trueCost) {
        return ARRAY_ADDRESS_EQUAL(store, load, trueCost, INFINITE);
    }

    protected final int ARRAY_ADDRESS_EQUAL(OPT_Instruction store, OPT_Instruction load, int trueCost, int falseCost) {
        if (AStore.getArray(store).similar(ALoad.getArray(load)) && AStore.getIndex(store).similar(ALoad.getIndex(load))) {
            return trueCost;
        } else {
            return falseCost;
        }
    }

    private static final class AddrStackElement {

        OPT_RegisterOperand base;

        OPT_RegisterOperand index;

        byte scale;

        Offset displacement;

        AddrStackElement next;

        AddrStackElement(OPT_RegisterOperand b, OPT_RegisterOperand i, byte s, Offset d, AddrStackElement n) {
            base = b;
            index = i;
            scale = s;
            displacement = d;
            next = n;
        }
    }

    private AddrStackElement AddrStack;

    protected final void pushAddress(OPT_RegisterOperand base, OPT_RegisterOperand index, byte scale, Offset disp) {
        AddrStack = new AddrStackElement(base, index, scale, disp, AddrStack);
    }

    protected final void augmentAddress(OPT_Operand op) {
        if (VM.VerifyAssertions) VM._assert(AddrStack != null, "No address to augment");
        if (op.isRegister()) {
            OPT_RegisterOperand rop = op.asRegister();
            if (AddrStack.base == null) {
                AddrStack.base = rop;
            } else if (AddrStack.index == null) {
                if (VM.VerifyAssertions) VM._assert(AddrStack.scale == (byte) 0);
                AddrStack.index = rop;
            } else {
                throw new OPT_OptimizingCompilerException("three base registers in address");
            }
        } else {
            int disp = ((OPT_IntConstantOperand) op).value;
            AddrStack.displacement = AddrStack.displacement.plus(disp);
        }
    }

    protected final void combineAddresses() {
        if (VM.VerifyAssertions) VM._assert(AddrStack != null, "No address to combine");
        AddrStackElement tmp = AddrStack;
        AddrStack = AddrStack.next;
        if (VM.VerifyAssertions) VM._assert(AddrStack != null, "only 1 address to combine");
        if (tmp.base != null) {
            if (AddrStack.base == null) {
                AddrStack.base = tmp.base;
            } else if (AddrStack.index == null) {
                if (VM.VerifyAssertions) VM._assert(AddrStack.scale == (byte) 0);
                AddrStack.index = tmp.base;
            } else {
                throw new OPT_OptimizingCompilerException("three base registers in address");
            }
        }
        if (tmp.index != null) {
            if (AddrStack.index == null) {
                if (VM.VerifyAssertions) VM._assert(AddrStack.scale == (byte) 0);
                AddrStack.index = tmp.index;
                AddrStack.scale = tmp.scale;
            } else if (AddrStack.base == null && tmp.scale == (byte) 0) {
                AddrStack.base = tmp.base;
            } else {
                throw new OPT_OptimizingCompilerException("two scaled registers in address");
            }
        }
        AddrStack.displacement = AddrStack.displacement.plus(tmp.displacement.toInt());
    }

    protected final OPT_MemoryOperand consumeAddress(byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        if (VM.VerifyAssertions) VM._assert(AddrStack != null, "No address to consume");
        OPT_MemoryOperand mo = new OPT_MemoryOperand(AddrStack.base, AddrStack.index, AddrStack.scale, AddrStack.displacement, size, loc, guard);
        AddrStack = AddrStack.next;
        return mo;
    }

    private static final class MOStackElement {

        OPT_MemoryOperand mo;

        MOStackElement next;

        MOStackElement(OPT_MemoryOperand m, MOStackElement n) {
            mo = m;
            next = n;
        }
    }

    private MOStackElement MOStack;

    protected final void pushMO(OPT_MemoryOperand mo) {
        MOStack = new MOStackElement(mo, MOStack);
    }

    protected final OPT_MemoryOperand consumeMO() {
        if (VM.VerifyAssertions) VM._assert(MOStack != null, "No memory operand to consume");
        OPT_MemoryOperand mo = MOStack.mo;
        MOStack = MOStack.next;
        return mo;
    }

    protected final OPT_MemoryOperand MO_L(OPT_Instruction s, byte size) {
        return MO(Load.getAddress(s), Load.getOffset(s), size, Load.getLocation(s), Load.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_S(OPT_Instruction s, byte size) {
        return MO(Store.getAddress(s), Store.getOffset(s), size, Store.getLocation(s), Store.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_AL(OPT_Instruction s, byte scale, byte size) {
        return MO_ARRAY(ALoad.getArray(s), ALoad.getIndex(s), scale, size, ALoad.getLocation(s), ALoad.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_AS(OPT_Instruction s, byte scale, byte size) {
        return MO_ARRAY(AStore.getArray(s), AStore.getIndex(s), scale, size, AStore.getLocation(s), AStore.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_L(OPT_Instruction s, byte size, int disp) {
        return MO(Load.getAddress(s), Load.getOffset(s), size, Offset.fromIntSignExtend(disp), Load.getLocation(s), Load.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_S(OPT_Instruction s, byte size, int disp) {
        return MO(Store.getAddress(s), Store.getOffset(s), size, Offset.fromIntSignExtend(disp), Store.getLocation(s), Store.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_AL(OPT_Instruction s, byte scale, byte size, int disp) {
        return MO_ARRAY(ALoad.getArray(s), ALoad.getIndex(s), scale, size, Offset.fromIntSignExtend(disp), ALoad.getLocation(s), ALoad.getGuard(s));
    }

    protected final OPT_MemoryOperand MO_AS(OPT_Instruction s, byte scale, byte size, int disp) {
        return MO_ARRAY(AStore.getArray(s), AStore.getIndex(s), scale, size, Offset.fromIntSignExtend(disp), AStore.getLocation(s), AStore.getGuard(s));
    }

    protected final OPT_MemoryOperand MO(OPT_Operand base, OPT_Operand offset, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        if (base instanceof OPT_IntConstantOperand) {
            if (offset instanceof OPT_IntConstantOperand) {
                return MO_D(Offset.fromIntSignExtend(IV(base) + IV(offset)), size, loc, guard);
            } else {
                return MO_BD(offset, Offset.fromIntSignExtend(IV(base)), size, loc, guard);
            }
        } else {
            if (offset instanceof OPT_IntConstantOperand) {
                return MO_BD(base, Offset.fromIntSignExtend(IV(offset)), size, loc, guard);
            } else {
                return MO_BI(base, offset, size, loc, guard);
            }
        }
    }

    protected final OPT_MemoryOperand MO_ARRAY(OPT_Operand base, OPT_Operand index, byte scale, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        if (index instanceof OPT_IntConstantOperand) {
            return MO_BD(base, Offset.fromIntZeroExtend(IV(index) << scale), size, loc, guard);
        } else {
            return MO_BIS(base, index, scale, size, loc, guard);
        }
    }

    protected final OPT_MemoryOperand MO(OPT_Operand base, OPT_Operand offset, byte size, Offset disp, OPT_LocationOperand loc, OPT_Operand guard) {
        if (base instanceof OPT_IntConstantOperand) {
            if (offset instanceof OPT_IntConstantOperand) {
                return MO_D(disp.plus(IV(base) + IV(offset)), size, loc, guard);
            } else {
                return MO_BD(offset, disp.plus(IV(base)), size, loc, guard);
            }
        } else {
            if (offset instanceof OPT_IntConstantOperand) {
                return MO_BD(base, disp.plus(IV(offset)), size, loc, guard);
            } else {
                return MO_BID(base, offset, disp, size, loc, guard);
            }
        }
    }

    protected final OPT_MemoryOperand MO_ARRAY(OPT_Operand base, OPT_Operand index, byte scale, byte size, Offset disp, OPT_LocationOperand loc, OPT_Operand guard) {
        if (index instanceof OPT_IntConstantOperand) {
            return MO_BD(base, disp.plus(IV(index) << scale), size, loc, guard);
        } else {
            return new OPT_MemoryOperand(R(base), R(index), scale, disp, size, loc, guard);
        }
    }

    protected final OPT_MemoryOperand MO_B(OPT_Operand base, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.B(R(base), size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_BI(OPT_Operand base, OPT_Operand index, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.BI(R(base), R(index), size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_BD(OPT_Operand base, Offset disp, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.BD(R(base), disp, size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_BID(OPT_Operand base, OPT_Operand index, Offset disp, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.BID(R(base), R(index), disp, size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_BIS(OPT_Operand base, OPT_Operand index, byte scale, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.BIS(R(base), R(index), scale, size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_D(Offset disp, byte size, OPT_LocationOperand loc, OPT_Operand guard) {
        return OPT_MemoryOperand.D(disp.toWord().toAddress(), size, loc, guard);
    }

    protected final OPT_MemoryOperand MO_MC(OPT_Instruction s) {
        OPT_Operand base = Binary.getVal1(s);
        OPT_Operand val = Binary.getVal2(s);
        if (val instanceof OPT_FloatConstantOperand) {
            OPT_FloatConstantOperand fc = (OPT_FloatConstantOperand) val;
            Offset offset = fc.offset;
            OPT_LocationOperand loc = new OPT_LocationOperand(offset);
            if (base instanceof OPT_IntConstantOperand) {
                return MO_D(offset.plus(IV(base)), DW, loc, TG());
            } else {
                return MO_BD(Binary.getVal1(s), offset, DW, loc, TG());
            }
        } else {
            OPT_DoubleConstantOperand dc = (OPT_DoubleConstantOperand) val;
            Offset offset = dc.offset;
            OPT_LocationOperand loc = new OPT_LocationOperand(offset);
            if (base instanceof OPT_IntConstantOperand) {
                return MO_D(offset.plus(IV(base)), QW, loc, TG());
            } else {
                return MO_BD(Binary.getVal1(s), dc.offset, QW, loc, TG());
            }
        }
    }
}

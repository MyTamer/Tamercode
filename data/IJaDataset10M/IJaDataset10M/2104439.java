package org.jikesrvm.compilers.opt.ppc;

import java.util.Enumeration;
import java.util.Iterator;
import org.jikesrvm.VM;
import static org.jikesrvm.VM_Constants.NOT_REACHED;
import org.jikesrvm.compilers.opt.OPT_Bits;
import org.jikesrvm.compilers.opt.OPT_GenericStackManager;
import org.jikesrvm.compilers.opt.OPT_OptimizingCompilerException;
import org.jikesrvm.compilers.opt.ir.MIR_Binary;
import org.jikesrvm.compilers.opt.ir.MIR_Load;
import org.jikesrvm.compilers.opt.ir.MIR_Move;
import org.jikesrvm.compilers.opt.ir.MIR_Store;
import org.jikesrvm.compilers.opt.ir.MIR_StoreUpdate;
import org.jikesrvm.compilers.opt.ir.MIR_Trap;
import org.jikesrvm.compilers.opt.ir.MIR_Unary;
import org.jikesrvm.compilers.opt.ir.OPT_IR;
import org.jikesrvm.compilers.opt.ir.OPT_Instruction;
import org.jikesrvm.compilers.opt.ir.OPT_IntConstantOperand;
import org.jikesrvm.compilers.opt.ir.OPT_Operand;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.CALL_SAVE_VOLATILE;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.IR_PROLOGUE_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_ADDI;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_BCTRL_SYS;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_BLR_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_BL_SYS;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_CMPI;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_FMR;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_FMR_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LAddr;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LAddr_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LDI;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LDIS;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LFD;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LFD_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LFS;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LFS_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LInt;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LInt_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LMW;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LWZ;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_LWZ_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_MFSPR;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_MOVE;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_MOVE_opcode;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_MTSPR;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_ORI;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STAddr;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STAddrU;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STFD;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STFS;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STMW;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_STW;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.PPC_TAddr;
import static org.jikesrvm.compilers.opt.ir.OPT_Operators.YIELDPOINT_OSR;
import org.jikesrvm.compilers.opt.ir.OPT_Register;
import org.jikesrvm.compilers.opt.ir.OPT_RegisterOperand;
import org.jikesrvm.compilers.opt.ir.OPT_TrapCodeOperand;
import org.jikesrvm.compilers.opt.ir.ppc.OPT_PhysicalRegisterSet;
import org.jikesrvm.compilers.opt.ir.ppc.OPT_PowerPCTrapOperand;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.BYTES_IN_ADDRESS;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.BYTES_IN_DOUBLE;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.BYTES_IN_FLOAT;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.BYTES_IN_INT;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.CONDITION_VALUE;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.DOUBLE_REG;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.DOUBLE_VALUE;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.FIRST_SCRATCH_GPR;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.FLOAT_VALUE;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.INT_REG;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.INT_VALUE;
import static org.jikesrvm.compilers.opt.ppc.OPT_PhysicalRegisterConstants.LAST_SCRATCH_GPR;
import static org.jikesrvm.ppc.VM_StackframeLayoutConstants.STACKFRAME_ALIGNMENT;
import static org.jikesrvm.ppc.VM_StackframeLayoutConstants.STACKFRAME_METHOD_ID_OFFSET;
import static org.jikesrvm.ppc.VM_StackframeLayoutConstants.STACKFRAME_NEXT_INSTRUCTION_OFFSET;
import static org.jikesrvm.ppc.VM_StackframeLayoutConstants.STACK_SIZE_GUARD;
import org.jikesrvm.runtime.VM_Entrypoints;
import org.vmmagic.unboxed.Offset;

/**
 * Class to manage the allocation of the "compiler-specific" portion of
 * the stackframe.  This class holds only the architecture-specific
 * functions.
 * <p>
 */
public abstract class OPT_StackManager extends OPT_GenericStackManager {

    /**
   * stack locaiton to save the XER register
   */
    private int saveXERLocation;

    /**
   * stack locaiton to save the CTR register
   */
    private int saveCTRLocation;

    /**
   * Return the size of the fixed portion of the stack.
   * @return size in bytes of the fixed portion of the stackframe
   */
    public final int getFrameFixedSize() {
        return frameSize;
    }

    /**
   * Allocate a new spill location and grow the
   * frame size to reflect the new layout.
   *
   * @param type the type to spill
   * @return the spill location
   */
    public final int allocateNewSpillLocation(int type) {
        int spillSize = OPT_PhysicalRegisterSet.getSpillSize(type);
        spillPointer = align(spillPointer, spillSize);
        spillPointer += spillSize;
        if (spillPointer > frameSize) {
            frameSize = spillPointer;
        }
        return spillPointer - spillSize;
    }

    /**
   * Clean up some junk that's left in the IR after register allocation,
   * and add epilogue code.
   */
    public void cleanUpAndInsertEpilogue() {
        OPT_Instruction inst = ir.firstInstructionInCodeOrder().nextInstructionInCodeOrder();
        for (; inst != null; inst = inst.nextInstructionInCodeOrder()) {
            switch(inst.getOpcode()) {
                case PPC_MOVE_opcode:
                case PPC_FMR_opcode:
                    if (MIR_Move.getResult(inst).register.number == MIR_Move.getValue(inst).register.number) {
                        inst = inst.remove();
                    }
                    break;
                case PPC_BLR_opcode:
                    if (frameIsRequired()) {
                        insertEpilogue(inst);
                    }
                    break;
                case PPC_LFS_opcode:
                case PPC_LFD_opcode:
                case PPC_LInt_opcode:
                case PPC_LWZ_opcode:
                case PPC_LAddr_opcode:
                    if (MIR_Load.getAddress(inst).register == ir.regpool.getPhysicalRegisterSet().getFP()) {
                        OPT_Operand one = MIR_Load.getOffset(inst);
                        if (one instanceof OPT_IntConstantOperand) {
                            int offset = ((OPT_IntConstantOperand) one).value;
                            if (offset <= -256) {
                                if (frameIsRequired()) {
                                    MIR_Load.setOffset(inst, IC(frameSize - offset - 256));
                                } else {
                                    MIR_Load.setOffset(inst, IC(-offset - 256));
                                }
                            }
                        }
                    }
                default:
                    break;
            }
        }
    }

    /**
   * Insert a spill of a physical register before instruction s.
   *
   * @param s the instruction before which the spill should occur
   * @param r the register (should be physical) to spill
   * @param type one of INT_VALUE, FLOAT_VALUE, DOUBLE_VALUE, or
   *                    CONDITION_VALUE
   * @param location the spill location
   */
    public final void insertSpillBefore(OPT_Instruction s, OPT_Register r, byte type, int location) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        if (type == FLOAT_VALUE) {
            s.insertBefore(MIR_Store.create(PPC_STFS, F(r), A(FP), IC(location + BYTES_IN_ADDRESS - BYTES_IN_FLOAT)));
        } else if (type == DOUBLE_VALUE) {
            s.insertBefore(MIR_Store.create(PPC_STFD, D(r), A(FP), IC(location)));
        } else if (type == INT_VALUE) {
            s.insertBefore(MIR_Store.create(PPC_STAddr, A(r), A(FP), IC(location)));
        } else {
            throw new OPT_OptimizingCompilerException("insertSpillBefore", "unsupported type " + type);
        }
    }

    /**
   * Create an MIR instruction to move rhs into lhs
   */
    final OPT_Instruction makeMoveInstruction(OPT_Register lhs, OPT_Register rhs) {
        if (rhs.isFloatingPoint() && lhs.isFloatingPoint()) {
            return MIR_Move.create(PPC_FMR, D(lhs), D(rhs));
        } else if (rhs.isAddress() && lhs.isAddress()) {
            return MIR_Move.create(PPC_MOVE, A(lhs), A(rhs));
        } else {
            throw new OPT_OptimizingCompilerException("RegAlloc", "unknown register:", lhs.toString());
        }
    }

    /**
   * Insert a load of a physical register from a spill location before
   * instruction s.
   *
   * @param s the instruction before which the spill should occur
   * @param r the register (should be physical) to spill
   * @param type one of INT_VALUE, FLOAT_VALUE, DOUBLE_VALUE, or
   *                    CONDITION_VALUE
   * @param location the spill location
   */
    public final void insertUnspillBefore(OPT_Instruction s, OPT_Register r, byte type, int location) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        if (type == CONDITION_VALUE) {
            OPT_Register temp = phys.getTemp();
            s.insertBefore(MIR_Load.create(PPC_LWZ, I(temp), A(FP), IC(location + BYTES_IN_ADDRESS - BYTES_IN_INT)));
        } else if (type == DOUBLE_VALUE) {
            s.insertBefore(MIR_Load.create(PPC_LFD, D(r), A(FP), IC(location)));
        } else if (type == FLOAT_VALUE) {
            s.insertBefore(MIR_Load.create(PPC_LFS, F(r), A(FP), IC(location + BYTES_IN_ADDRESS - BYTES_IN_FLOAT)));
        } else if (type == INT_VALUE) {
            s.insertBefore(MIR_Load.create(PPC_LAddr, A(r), A(FP), IC(location)));
        } else {
            throw new OPT_OptimizingCompilerException("insertUnspillBefore", "unknown type:" + type);
        }
    }

    /**
   * Insert the epilogue before a particular return instruction.
   *
   * @param ret the return instruction.
   */
    private void insertEpilogue(OPT_Instruction ret) {
        if (ir.compiledMethod.isSaveVolatile()) {
            restoreVolatileRegisters(ret);
        }
        restoreNonVolatiles(ret);
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register temp = phys.getTemp();
        OPT_Register FP = phys.getFP();
        ret.insertBefore(MIR_Load.create(PPC_LAddr, A(temp), A(FP), IC(STACKFRAME_NEXT_INSTRUCTION_OFFSET + frameSize)));
        ret.insertBefore(MIR_Move.create(PPC_MTSPR, A(phys.getLR()), A(phys.getTemp())));
        ret.insertBefore(MIR_Binary.create(PPC_ADDI, A(FP), A(FP), IC(frameSize)));
    }

    /**
   * Insert code in the prologue to save the
   * volatile registers.
   *
   * @param inst
   */
    private void saveVolatiles(OPT_Instruction inst) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        int i = 0;
        for (Enumeration<OPT_Register> e = phys.enumerateVolatileGPRs(); e.hasMoreElements(); i++) {
            OPT_Register r = e.nextElement();
            int location = saveVolatileGPRLocation[i];
            inst.insertBefore(MIR_Store.create(PPC_STAddr, A(r), A(FP), IC(location)));
        }
        i = 0;
        for (Enumeration<OPT_Register> e = phys.enumerateVolatileFPRs(); e.hasMoreElements(); i++) {
            OPT_Register r = e.nextElement();
            int location = saveVolatileFPRLocation[i];
            inst.insertBefore(MIR_Store.create(PPC_STFD, D(r), A(FP), IC(location)));
        }
        OPT_Register temp = phys.getTemp();
        inst.insertBefore(MIR_Move.create(PPC_MFSPR, I(temp), I(phys.getXER())));
        inst.insertBefore(MIR_Store.create(PPC_STW, I(temp), A(FP), IC(saveXERLocation)));
        inst.insertBefore(MIR_Move.create(PPC_MFSPR, A(temp), A(phys.getCTR())));
        inst.insertBefore(MIR_Store.create(PPC_STAddr, A(temp), A(FP), IC(saveCTRLocation)));
    }

    /**
   * Insert code into the prologue to save any used non-volatile
   * registers.
   *
   * @param inst the first instruction after the prologue.
   */
    private void saveNonVolatiles(OPT_Instruction inst) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        int nNonvolatileGPRS = ir.compiledMethod.getNumberOfNonvolatileGPRs();
        if (ir.compiledMethod.isSaveVolatile()) {
            nNonvolatileGPRS = OPT_PhysicalRegisterSet.getNumberOfNonvolatileGPRs();
        }
        int n = nNonvolatileGPRS - 1;
        OPT_Register FP = phys.getFP();
        if (VM.BuildFor32Addr && n > MULTIPLE_CUTOFF) {
            OPT_Register nv = null;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                nv = e.nextElement();
            }
            n++;
            OPT_RegisterOperand range = I(nv);
            int offset = getNonvolatileGPROffset(n);
            inst.insertBefore(MIR_Store.create(PPC_STMW, range, A(FP), IC(offset)));
        } else {
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                OPT_Register nv = e.nextElement();
                int offset = getNonvolatileGPROffset(n);
                inst.insertBefore(MIR_Store.create(PPC_STAddr, A(nv), A(FP), IC(offset)));
            }
        }
        if (ir.compiledMethod.isSaveVolatile()) {
        } else {
            int nNonvolatileFPRS = ir.compiledMethod.getNumberOfNonvolatileFPRs();
            n = nNonvolatileFPRS - 1;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileFPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                OPT_Register nv = e.nextElement();
                int offset = getNonvolatileFPROffset(n);
                inst.insertBefore(MIR_Store.create(PPC_STFD, D(nv), A(FP), IC(offset)));
            }
        }
    }

    /**
   * Insert code before a return instruction to restore the nonvolatile
   * registers.
   *
   * @param inst the return instruction
   */
    private void restoreNonVolatiles(OPT_Instruction inst) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        int nNonvolatileGPRS = ir.compiledMethod.getNumberOfNonvolatileGPRs();
        int n = nNonvolatileGPRS - 1;
        OPT_Register FP = phys.getFP();
        if (VM.BuildFor32Addr && n > MULTIPLE_CUTOFF) {
            OPT_Register nv = null;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                nv = e.nextElement();
            }
            n++;
            OPT_RegisterOperand range = I(nv);
            int offset = getNonvolatileGPROffset(n);
            inst.insertBefore(MIR_Load.create(PPC_LMW, range, A(FP), IC(offset)));
        } else {
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                OPT_Register nv = e.nextElement();
                int offset = getNonvolatileGPROffset(n);
                inst.insertBefore(MIR_Load.create(PPC_LAddr, A(nv), A(FP), IC(offset)));
            }
        }
        if (!ir.compiledMethod.isSaveVolatile()) {
            int nNonvolatileFPRS = ir.compiledMethod.getNumberOfNonvolatileFPRs();
            n = nNonvolatileFPRS - 1;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileFPRsBackwards(); e.hasMoreElements() && n >= 0; n--) {
                OPT_Register nv = e.nextElement();
                int offset = getNonvolatileFPROffset(n);
                inst.insertBefore(MIR_Load.create(PPC_LFD, D(nv), A(FP), IC(offset)));
            }
        }
    }

    /**
   * Insert code before a return instruction to restore the
   * volatile registers.
   *
   * @param inst the return instruction
   */
    private void restoreVolatileRegisters(OPT_Instruction inst) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        int i = 0;
        for (Enumeration<OPT_Register> e = phys.enumerateVolatileGPRs(); e.hasMoreElements(); i++) {
            OPT_Register r = e.nextElement();
            int location = saveVolatileGPRLocation[i];
            inst.insertBefore(MIR_Load.create(PPC_LAddr, A(r), A(FP), IC(location)));
        }
        i = 0;
        for (Enumeration<OPT_Register> e = phys.enumerateVolatileFPRs(); e.hasMoreElements(); i++) {
            OPT_Register r = e.nextElement();
            int location = saveVolatileFPRLocation[i];
            inst.insertBefore(MIR_Load.create(PPC_LFD, D(r), A(FP), IC(location)));
        }
        OPT_Register temp = phys.getTemp();
        inst.insertBefore(MIR_Load.create(PPC_LInt, I(temp), A(FP), IC(saveXERLocation)));
        inst.insertBefore(MIR_Move.create(PPC_MTSPR, I(phys.getXER()), I(temp)));
        inst.insertBefore(MIR_Load.create(PPC_LAddr, A(temp), A(FP), IC(saveCTRLocation)));
        inst.insertBefore(MIR_Move.create(PPC_MTSPR, A(phys.getCTR()), A(temp)));
    }

    /**
   * Schedule prologue for 'normal' case (see above)
   */
    public final void insertNormalPrologue() {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        OPT_Register PR = phys.getPR();
        OPT_Register TSR = phys.getTSR();
        OPT_Register R0 = phys.getTemp();
        OPT_Register S0 = phys.getGPR(FIRST_SCRATCH_GPR);
        OPT_Register S1 = phys.getGPR(LAST_SCRATCH_GPR);
        boolean interruptible = ir.method.isInterruptible();
        boolean stackOverflow = interruptible;
        boolean yp = hasPrologueYieldpoint();
        int frameFixedSize = getFrameFixedSize();
        ir.compiledMethod.setFrameFixedSize(frameFixedSize);
        if (frameFixedSize >= STACK_SIZE_GUARD || ir.compiledMethod.isSaveVolatile()) {
            insertExceptionalPrologue();
            return;
        }
        OPT_Instruction ptr = ir.firstInstructionInCodeOrder().nextInstructionInCodeOrder();
        if (VM.VerifyAssertions) VM._assert(ptr.getOpcode() == IR_PROLOGUE_opcode);
        ptr.insertBefore(MIR_Move.create(PPC_MFSPR, A(R0), A(phys.getLR())));
        if (yp) {
            Offset offset = VM_Entrypoints.takeYieldpointField.getOffset();
            if (VM.VerifyAssertions) VM._assert(OPT_Bits.fits(offset, 16));
            ptr.insertBefore(MIR_Load.create(PPC_LInt, I(S1), A(PR), IC(OPT_Bits.PPCMaskLower16(offset))));
        }
        ptr.insertBefore(MIR_StoreUpdate.create(PPC_STAddrU, A(FP), A(FP), IC(-frameSize)));
        if (stackOverflow) {
            Offset offset = VM_Entrypoints.activeThreadStackLimitField.getOffset();
            if (VM.VerifyAssertions) VM._assert(OPT_Bits.fits(offset, 16));
            ptr.insertBefore(MIR_Load.create(PPC_LAddr, A(S0), A(phys.getPR()), IC(OPT_Bits.PPCMaskLower16(offset))));
        }
        saveNonVolatiles(ptr);
        if (yp) {
            ptr.insertBefore(MIR_Binary.create(PPC_CMPI, I(TSR), I(S1), IC(0)));
        }
        int cmid = ir.compiledMethod.getId();
        if (cmid <= 0x7fff) {
            ptr.insertBefore(MIR_Unary.create(PPC_LDI, I(S1), IC(cmid)));
        } else {
            ptr.insertBefore(MIR_Unary.create(PPC_LDIS, I(S1), IC(cmid >>> 16)));
            ptr.insertBefore(MIR_Binary.create(PPC_ORI, I(S1), I(S1), IC(cmid & 0xffff)));
        }
        ptr.insertBefore(MIR_Store.create(PPC_STAddr, A(R0), A(FP), IC(frameSize + STACKFRAME_NEXT_INSTRUCTION_OFFSET)));
        ptr.insertBefore(MIR_Store.create(PPC_STW, I(S1), A(FP), IC(STACKFRAME_METHOD_ID_OFFSET)));
        if (stackOverflow) {
            MIR_Trap.mutate(ptr, PPC_TAddr, OPT_PowerPCTrapOperand.GREATER(), A(S0), A(FP), OPT_TrapCodeOperand.StackOverflow());
        } else {
            ptr.remove();
        }
    }

    /**
   * prologue for the exceptional case.
   * (1) R0 is the only available scratch register.
   * (2) stack overflow check has to come first.
   */
    final void insertExceptionalPrologue() {
        if (VM.VerifyAssertions) {
            VM._assert((frameSize & (STACKFRAME_ALIGNMENT - 1)) == 0, "Stack frame alignment error");
        }
        if (frameSize >= 0x7ff0) {
            throw new OPT_OptimizingCompilerException("Stackframe size exceeded!");
        }
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        OPT_Register FP = phys.getFP();
        OPT_Register PR = phys.getPR();
        OPT_Register TSR = phys.getTSR();
        OPT_Register R0 = phys.getTemp();
        OPT_Register S1 = phys.getGPR(LAST_SCRATCH_GPR);
        boolean interruptible = ir.method.isInterruptible();
        boolean stackOverflow = interruptible;
        boolean yp = hasPrologueYieldpoint();
        OPT_Instruction ptr = ir.firstInstructionInCodeOrder().nextInstructionInCodeOrder();
        if (VM.VerifyAssertions) VM._assert(ptr.getOpcode() == IR_PROLOGUE_opcode);
        if (stackOverflow) {
            ptr.insertBefore(MIR_Store.create(PPC_STAddr, A(S1), A(FP), IC(STACKFRAME_NEXT_INSTRUCTION_OFFSET)));
            Offset offset = VM_Entrypoints.activeThreadStackLimitField.getOffset();
            if (VM.VerifyAssertions) VM._assert(OPT_Bits.fits(offset, 16));
            ptr.insertBefore(MIR_Load.create(PPC_LAddr, A(S1), A(phys.getPR()), IC(OPT_Bits.PPCMaskLower16(offset))));
            ptr.insertBefore(MIR_Binary.create(PPC_ADDI, A(R0), A(S1), IC(frameSize)));
            ptr.insertBefore(MIR_Load.create(PPC_LAddr, A(S1), A(FP), IC(STACKFRAME_NEXT_INSTRUCTION_OFFSET)));
            MIR_Trap.mutate(ptr, PPC_TAddr, OPT_PowerPCTrapOperand.LESS(), A(FP), A(R0), OPT_TrapCodeOperand.StackOverflow());
            ptr = ptr.nextInstructionInCodeOrder();
        } else {
            OPT_Instruction next = ptr.nextInstructionInCodeOrder();
            ptr.remove();
            ptr = next;
        }
        ptr.insertBefore(MIR_Move.create(PPC_MFSPR, A(R0), A(phys.getLR())));
        ptr.insertBefore(MIR_StoreUpdate.create(PPC_STAddrU, A(FP), A(FP), IC(-frameSize)));
        ptr.insertBefore(MIR_Store.create(PPC_STAddr, A(R0), A(FP), IC(frameSize + STACKFRAME_NEXT_INSTRUCTION_OFFSET)));
        int cmid = ir.compiledMethod.getId();
        if (cmid <= 0x7fff) {
            ptr.insertBefore(MIR_Unary.create(PPC_LDI, I(R0), IC(cmid)));
        } else {
            ptr.insertBefore(MIR_Unary.create(PPC_LDIS, I(R0), IC(cmid >>> 16)));
            ptr.insertBefore(MIR_Binary.create(PPC_ORI, I(R0), I(R0), IC(cmid & 0xffff)));
        }
        ptr.insertBefore(MIR_Store.create(PPC_STW, I(R0), A(FP), IC(STACKFRAME_METHOD_ID_OFFSET)));
        if (ir.compiledMethod.isSaveVolatile()) {
            saveVolatiles(ptr);
        }
        saveNonVolatiles(ptr);
        if (yp) {
            Offset offset = VM_Entrypoints.takeYieldpointField.getOffset();
            if (VM.VerifyAssertions) VM._assert(OPT_Bits.fits(offset, 16));
            ptr.insertBefore(MIR_Load.create(PPC_LInt, I(R0), A(PR), IC(OPT_Bits.PPCMaskLower16(offset))));
            ptr.insertBefore(MIR_Binary.create(PPC_CMPI, I(TSR), I(R0), IC(0)));
        }
    }

    /**
   * Compute the number of stack words needed to hold nonvolatile
   * registers.
   *
   * Side effects:
   * <ul>
   * <li> updates the VM_OptCompiler structure
   * <li> updates the <code>frameSize</code> field of this object
   * <li> updates the <code>frameRequired</code> field of this object
   * </ul>
   */
    public void computeNonVolatileArea() {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        if (ir.compiledMethod.isSaveVolatile()) {
            int numGprNv = OPT_PhysicalRegisterSet.getNumberOfNonvolatileGPRs();
            ir.compiledMethod.setNumberOfNonvolatileGPRs((short) numGprNv);
            frameSize += numGprNv * BYTES_IN_ADDRESS;
            int numFprNv = OPT_PhysicalRegisterSet.getNumberOfNonvolatileFPRs();
            ir.compiledMethod.setNumberOfNonvolatileFPRs((short) numFprNv);
            frameSize += numFprNv * BYTES_IN_DOUBLE;
            setFrameRequired();
            int i = 0;
            for (Enumeration<OPT_Register> e = phys.enumerateVolatileGPRs(); e.hasMoreElements(); i++) {
                e.nextElement();
                saveVolatileGPRLocation[i] = allocateNewSpillLocation(INT_REG);
            }
            i = 0;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRs(); e.hasMoreElements(); i++) {
                e.nextElement();
                nonVolatileGPRLocation[i] = allocateNewSpillLocation(INT_REG);
            }
            saveXERLocation = allocateNewSpillLocation(INT_REG);
            saveCTRLocation = allocateNewSpillLocation(INT_REG);
            i = 0;
            for (Enumeration<OPT_Register> e = phys.enumerateVolatileFPRs(); e.hasMoreElements(); i++) {
                e.nextElement();
                saveVolatileFPRLocation[i] = allocateNewSpillLocation(DOUBLE_REG);
            }
            int gprOffset = getNonvolatileGPROffset(0);
            ir.compiledMethod.setUnsignedNonVolatileOffset(gprOffset);
        } else {
            int numGprNv = 0;
            int i = 0;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileGPRs(); e.hasMoreElements(); ) {
                OPT_Register r = e.nextElement();
                if (r.isTouched()) {
                    nonVolatileGPRLocation[i++] = allocateNewSpillLocation(INT_REG);
                    numGprNv++;
                }
            }
            i = 0;
            int numFprNv = 0;
            for (Enumeration<OPT_Register> e = phys.enumerateNonvolatileFPRs(); e.hasMoreElements(); ) {
                OPT_Register r = e.nextElement();
                if (r.isTouched()) {
                    nonVolatileFPRLocation[i++] = allocateNewSpillLocation(DOUBLE_REG);
                    numFprNv++;
                }
            }
            ir.compiledMethod.setNumberOfNonvolatileGPRs((short) numGprNv);
            ir.compiledMethod.setNumberOfNonvolatileFPRs((short) numFprNv);
            if (numGprNv > 0 || numFprNv > 0) {
                int gprOffset = getNonvolatileGPROffset(0);
                ir.compiledMethod.setUnsignedNonVolatileOffset(gprOffset);
                setFrameRequired();
            } else {
                ir.compiledMethod.setUnsignedNonVolatileOffset(0);
            }
        }
        frameSize = align(frameSize, STACKFRAME_ALIGNMENT);
    }

    /**
   * Walk over the currently available scratch registers.
   *
   * <p>For any scratch register r which is def'ed by instruction s,
   * spill r before s and remove r from the pool of available scratch
   * registers.
   *
   * <p>For any scratch register r which is used by instruction s,
   * restore r before s and remove r from the pool of available scratch
   * registers.
   *
   * <p>For any scratch register r which has current contents symb, and
   * symb is spilled to location M, and s defs M: the old value of symb is
   * dead.  Mark this.
   *
   * <p>Invalidate any scratch register assignments that are illegal in s.
   */
    public void restoreScratchRegistersBefore(OPT_Instruction s) {
        for (Iterator<ScratchRegister> i = scratchInUse.iterator(); i.hasNext(); ) {
            ScratchRegister scratch = i.next();
            if (scratch.currentContents == null) continue;
            if (verboseDebug) {
                System.out.println("RESTORE: consider " + scratch);
            }
            boolean removed = false;
            boolean unloaded = false;
            if (definedIn(scratch.scratch, s) || (s.isCall() && s.operator != CALL_SAVE_VOLATILE && scratch.scratch.isVolatile())) {
                if (verboseDebug) {
                    System.out.println("RESTORE : unload because defined " + scratch);
                }
                unloadScratchRegisterBefore(s, scratch);
                if (verboseDebug) {
                    System.out.println("RSRB: End scratch interval " + scratch.scratch + " " + s);
                }
                scratchMap.endScratchInterval(scratch.scratch, s);
                OPT_Register scratchContents = scratch.currentContents;
                if (scratchContents != null) {
                    if (verboseDebug) {
                        System.out.println("RSRB: End symbolic interval " + scratch.currentContents + " " + s);
                    }
                    scratchMap.endSymbolicInterval(scratch.currentContents, s);
                }
                i.remove();
                removed = true;
                unloaded = true;
            }
            if (usedIn(scratch.scratch, s) || !isLegal(scratch.currentContents, scratch.scratch, s)) {
                if (!unloaded) {
                    if (verboseDebug) {
                        System.out.println("RESTORE : unload because used " + scratch);
                    }
                    unloadScratchRegisterBefore(s, scratch);
                    if (verboseDebug) {
                        System.out.println("RSRB2: End scratch interval " + scratch.scratch + " " + s);
                    }
                    scratchMap.endScratchInterval(scratch.scratch, s);
                    OPT_Register scratchContents = scratch.currentContents;
                    if (scratchContents != null) {
                        if (verboseDebug) {
                            System.out.println("RSRB2: End symbolic interval " + scratch.currentContents + " " + s);
                        }
                        scratchMap.endSymbolicInterval(scratch.currentContents, s);
                    }
                }
                if (verboseDebug) {
                    System.out.println("RESTORE : reload because used " + scratch);
                }
                reloadScratchRegisterBefore(s, scratch);
                if (!removed) {
                    i.remove();
                    removed = true;
                }
            }
        }
    }

    protected static final int MULTIPLE_CUTOFF = 4;

    /**
   * Initializes the "tmp" regs for this object
   * @param ir the governing ir
   */
    public final void initForArch(OPT_IR ir) {
        OPT_PhysicalRegisterSet phys = ir.regpool.getPhysicalRegisterSet();
        phys.getJTOC().reserveRegister();
        phys.getFirstConditionRegister().reserveRegister();
    }

    /**
   * Is a particular instruction a system call?
   */
    public boolean isSysCall(OPT_Instruction s) {
        return s.operator == PPC_BCTRL_SYS || s.operator == PPC_BL_SYS;
    }

    /**
   * Given symbolic register r in instruction s, do we need to ensure that
   * r is in a scratch register is s (as opposed to a memory operand)
   */
    public boolean needScratch(OPT_Register r, OPT_Instruction s) {
        if (s.operator == YIELDPOINT_OSR) return false;
        return true;
    }

    /**
   * In instruction s, replace all appearances of a symbolic register
   * operand with uses of the appropriate spill location, as cached by the
   * register allocator.
   *
   * @param s the instruction to mutate.
   * @param symb the symbolic register operand to replace
   */
    public void replaceOperandWithSpillLocation(OPT_Instruction s, OPT_RegisterOperand symb) {
        if (VM.VerifyAssertions) VM._assert(NOT_REACHED);
    }
}

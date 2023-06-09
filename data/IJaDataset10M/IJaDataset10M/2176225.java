package com.ibm.JikesRVM.opt;

import com.ibm.JikesRVM.*;
import java.util.Enumeration;
import com.ibm.JikesRVM.opt.ir.*;

public final class OPT_Simple extends OPT_CompilerPhase implements OPT_Operators, OPT_Constants {

    private OPT_BranchOptimizations branchOpts = new OPT_BranchOptimizations(-1, false, false);

    /**
   * Perform type propagation?
   */
    private final boolean typeProp;

    /**
   * Attempt to eliminate bounds and cast checks?
   */
    private final boolean foldChecks;

    /**
   * Fold conditional branches with constant operands?
   */
    private final boolean foldBranches;

    public final boolean shouldPerform(OPT_Options options) {
        return options.SIMPLE_OPT;
    }

    public final String getName() {
        return "Simple Opts";
    }

    public final boolean printingEnabled(OPT_Options options, boolean before) {
        return false;
    }

    /**
   * The constructor is used to specify what pieces of OPT_Simple will
   * be enabled for this instance.  Some pieces are always enabled. 
   * Customizing can be useful because some of the optimizations are not 
   * valid/useful on LIR or even on "late stage" HIR.  With this
   * constructor, branches will be folded.
   * 
   * @param typeProp should type propagation be peformed
   * @param foldChecks should we attempt to eliminate boundscheck
   */
    public OPT_Simple(boolean typeProp, boolean foldChecks) {
        this.typeProp = typeProp;
        this.foldChecks = foldChecks;
        this.foldBranches = true;
    }

    /**
   * The constructor is used to specify what pieces of OPT_Simple will
   * be enabled for this instance.  Some pieces are always enabled. 
   * Customizing can be useful because some of the optimizations are not 
   * valid/useful on LIR or even on "late stage" HIR.  
   * 
   * @param typeProp should type propagation be peformed?
   * @param foldChecks should we attempt to eliminate boundscheck?
   * @param foldBranches should we attempt to constant fold conditional
   * branches?
   */
    OPT_Simple(boolean typeProp, boolean foldChecks, boolean foldBranches) {
        this.typeProp = typeProp;
        this.foldChecks = foldChecks;
        this.foldBranches = foldBranches;
    }

    /**
   * By default, perform all optimizations.
   */
    OPT_Simple() {
        typeProp = true;
        foldChecks = true;
        foldBranches = true;
    }

    /**
   * Main driver for the simple optimizations
   *
   * @param ir the IR to optimize
   */
    public void perform(OPT_IR ir) {
        OPT_DefUse.computeDU(ir);
        OPT_DefUse.recomputeSSA(ir);
        copyPropagation(ir);
        if (typeProp) typePropagation(ir);
        if (foldChecks) arrayPropagation(ir);
        eliminateDeadInstructions(ir);
        foldConstants(ir);
        if (foldBranches) simplifyConstantBranches(ir);
    }

    /**
   * Perform flow-insensitive copy and constant propagation using 
   * register list information. 
   *
   * <ul>
   * <li> Note: register list MUST be initialized BEFORE calling this routine.
   * <li> Note: this function incrementally maintains the register list.
   * </ul>
   *
   * @param ir the IR in question
   */
    static void copyPropagation(OPT_IR ir) {
        OPT_Register elemNext;
        boolean reiterate = true;
        while (reiterate) {
            reiterate = false;
            instructions: for (OPT_Register reg = ir.regpool.getFirstSymbolicRegister(); reg != null; reg = elemNext) {
                elemNext = reg.getNext();
                if (reg.useList == null || reg.defList == null || !reg.isSSA()) continue;
                OPT_RegisterOperand lhs = reg.defList;
                OPT_Instruction defInstr = lhs.instruction;
                OPT_Operand rhs;
                if (defInstr.isMove()) {
                    rhs = Move.getVal(defInstr);
                } else if (defInstr.operator() == PHI) {
                    OPT_Operand phiVal = equivalentValforPHI(defInstr);
                    if (phiVal == null) continue instructions;
                    rhs = phiVal;
                } else {
                    continue instructions;
                }
                if (rhs.isRegister() && !rhs.asRegister().register.isSSA()) continue;
                reiterate = ir.options.getOptLevel() > 1;
                if (rhs.isRegister()) {
                    OPT_RegisterOperand nextUse;
                    OPT_RegisterOperand rhsRegOp = rhs.asRegister();
                    for (OPT_RegisterOperand use = reg.useList; use != null; use = nextUse) {
                        nextUse = use.getNext();
                        if (VM.VerifyAssertions) VM._assert(rhsRegOp.register.getType() == use.register.getType());
                        OPT_DefUse.transferUse(use, rhsRegOp);
                    }
                } else if (rhs.isConstant()) {
                    for (OPT_RegisterOperand use = reg.useList; use != null; use = use.getNext()) {
                        int index = use.getIndexInInstruction();
                        use.instruction.putOperand(index, rhs);
                    }
                } else {
                    throw new OPT_OptimizingCompilerException("OPT_Simple.copyPropagation: unexpected operand type");
                }
                defInstr.remove();
                if (rhs.isRegister()) OPT_DefUse.removeUse(rhs.asRegister());
                ir.regpool.removeRegister(lhs.register);
            }
        }
    }

    /**
   * Try to find an operand that is equivalent to the result of a
   * given phi instruction.
   *
   * @param phi the instruction to be simplified
   * @return one of the phi's operands that is equivalent to the phi's result,
   * or null if the phi can not be simplified.
   */
    static OPT_Operand equivalentValforPHI(OPT_Instruction phi) {
        if (!Phi.conforms(phi)) return null;
        OPT_Operand result = Phi.getResult(phi), equiv = result;
        int i = 0, n = Phi.getNumberOfValues(phi);
        while (i < n) {
            equiv = Phi.getValue(phi, i++);
            if (!equiv.similar(result)) break;
        }
        while (i < n) {
            OPT_Operand opi = Phi.getValue(phi, i++);
            if (!opi.similar(equiv) && !opi.similar(result)) return null;
        }
        return equiv;
    }

    /**
   * Perform flow-insensitive type propagation using register list
   * information. Note: register list MUST be initialized BEFORE
   * calling this routine.
   *
   * <p> Kept separate from copyPropagation loop to enable clients 
   * more flexibility.
   *
   * @param ir the IR in question
   */
    static void typePropagation(OPT_IR ir) {
        OPT_Register elemNext;
        for (OPT_Register reg = ir.regpool.getFirstSymbolicRegister(); reg != null; reg = elemNext) {
            elemNext = (OPT_Register) reg.getNext();
            if (reg.useList == null) continue;
            if (reg.defList == null) continue;
            if (!reg.isSSA()) continue;
            OPT_RegisterOperand lhs = reg.defList;
            OPT_Instruction instr = lhs.instruction;
            OPT_Operator op = instr.operator();
            if (!op.isMove()) continue;
            OPT_Operand rhsOp = Move.getVal(instr);
            if (!(rhsOp instanceof OPT_RegisterOperand)) continue;
            OPT_RegisterOperand rhs = (OPT_RegisterOperand) rhsOp;
            lhs.type = rhs.type;
            for (OPT_RegisterOperand use = reg.useList; use != null; use = (OPT_RegisterOperand) use.getNext()) {
                if (OPT_ClassLoaderProxy.includesType(rhs.type, use.type) == YES) continue;
                if (rhs.type.isPrimitiveType() && !use.type.isPrimitiveType()) continue;
                use.type = rhs.type;
            }
        }
    }

    /**
   * Perform flow-insensitive propagation to eliminate bounds checks
   * and arraylength for arrays with static lengths. Only useful on the HIR
   * (because BOUNDS_CHECK is expanded in LIR into multiple instrs)
   *
   * <p> Note: this function incrementally maintains the register list.
   * 
   * @param ir the IR in question
   */
    static void arrayPropagation(OPT_IR ir) {
        OPT_Register elemNext;
        for (OPT_Register reg = ir.regpool.getFirstSymbolicRegister(); reg != null; reg = elemNext) {
            elemNext = (OPT_Register) reg.getNext();
            if (reg.useList == null) continue;
            if (reg.defList == null) continue;
            if (!reg.isSSA()) continue;
            OPT_RegisterOperand lhs = reg.defList;
            OPT_Instruction instr = lhs.instruction;
            OPT_Operator op = instr.operator();
            if (!(op == NEWARRAY || op == NEWARRAY_UNRESOLVED)) continue;
            OPT_Operand sizeOp = NewArray.getSize(instr);
            boolean boundsCheckOK = false;
            boolean arraylengthOK = false;
            int size = -1;
            if (sizeOp instanceof OPT_IntConstantOperand) {
                size = ((OPT_IntConstantOperand) sizeOp).value;
                boundsCheckOK = true;
                arraylengthOK = true;
            } else if (sizeOp instanceof OPT_RegisterOperand) {
                if (sizeOp.asRegister().register.isSSA()) arraylengthOK = true;
            }
            for (OPT_RegisterOperand use = reg.useList; use != null; use = (OPT_RegisterOperand) use.getNext()) {
                OPT_Instruction i = use.instruction;
                if (boundsCheckOK && i.getOpcode() == BOUNDS_CHECK_opcode) {
                    OPT_Operand indexOp = BoundsCheck.getIndex(i);
                    if (indexOp instanceof OPT_IntConstantOperand) {
                        if (((OPT_IntConstantOperand) indexOp).value <= size) {
                            OPT_Instruction s = Move.create(GUARD_MOVE, BoundsCheck.getGuardResult(i).copyD2D(), new OPT_TrueGuardOperand());
                            s.position = i.position;
                            s.bcIndex = i.bcIndex;
                            i.insertAfter(s);
                            OPT_DefUse.updateDUForNewInstruction(s);
                            OPT_DefUse.removeInstructionAndUpdateDU(i);
                        }
                    }
                } else if (arraylengthOK && i.getOpcode() == ARRAYLENGTH_opcode) {
                    OPT_Operand newSizeOp = sizeOp.copy();
                    OPT_RegisterOperand result = (OPT_RegisterOperand) GuardedUnary.getResult(i).copy();
                    OPT_Instruction s = Move.create(INT_MOVE, result, newSizeOp);
                    s.position = i.position;
                    s.bcIndex = i.bcIndex;
                    i.insertAfter(s);
                    OPT_DefUse.updateDUForNewInstruction(s);
                    OPT_DefUse.removeInstructionAndUpdateDU(i);
                }
            }
        }
    }

    /**
   * Simple conservative dead code elimination.
   * An instruction is eliminated if:
   * <ul>
   *  <li> 1. it is not a PEI, store or call
   *  <li> 2. it DEFs only registers
   *  <li> 3. all registers it DEFS are dead
   * </ul>
   *
   * <p> Note: this function incrementally maintains the register list.
   *
   * @param ir the IR to optimize
   */
    static void eliminateDeadInstructions(OPT_IR ir) {
        eliminateDeadInstructions(ir, false);
    }

    /**
   * Simple conservative dead code elimination.
   * An instruction is eliminated if:
   * <ul>
   *  <li> 1. it is not a PEI, store or call
   *  <li> 2. it DEFs only registers
   *  <li> 3. all registers it DEFS are dead
   * </ul>
   *
   * <p> Note: this function incrementally maintains the register list.
   *
   * @param ir IR to optimize
   * @param preserveImplicitSSA if this is true, do not eliminate dead
   * instructions that have implicit operands for heap array SSA form
   */
    static void eliminateDeadInstructions(OPT_IR ir, boolean preserveImplicitSSA) {
        for (OPT_Instruction instr = ir.lastInstructionInCodeOrder(), prevInstr = null; instr != null; instr = prevInstr) {
            prevInstr = instr.prevInstructionInCodeOrder();
            if (instr.isPEI() || instr.isImplicitStore() || instr.isBranch() || instr.isCall()) continue;
            if (preserveImplicitSSA && (instr.isImplicitLoad() || instr.isAllocation() || instr.operator() == PHI)) continue;
            if (instr.operator() == NOP) {
                OPT_DefUse.removeInstructionAndUpdateDU(instr);
            }
            if (instr.operator() == UNINT_BEGIN) {
                OPT_Instruction s = instr.nextInstructionInCodeOrder();
                if (s.operator() == UNINT_END) {
                    OPT_DefUse.removeInstructionAndUpdateDU(s);
                    OPT_DefUse.removeInstructionAndUpdateDU(instr);
                }
            }
            if (Move.conforms(instr)) {
                OPT_Register lhs = Move.getResult(instr).asRegister().register;
                if (Move.getVal(instr).isRegister()) {
                    OPT_Register rhs = Move.getVal(instr).asRegister().register;
                    if (lhs == rhs) {
                        OPT_DefUse.removeInstructionAndUpdateDU(instr);
                        continue;
                    }
                }
            }
            boolean isDead = true;
            boolean foundRegisterDef = false;
            for (OPT_OperandEnumeration defs = instr.getDefs(); defs.hasMoreElements(); ) {
                OPT_Operand def = (OPT_Operand) defs.nextElement();
                if (!def.isRegister()) {
                    isDead = false;
                    break;
                }
                foundRegisterDef = true;
                OPT_RegisterOperand r = def.asRegister();
                if (r.register.useList != null) {
                    isDead = false;
                    break;
                }
                ;
                if (r.register.isPhysical()) {
                    isDead = false;
                    break;
                }
            }
            if (!isDead) continue;
            if (!foundRegisterDef) continue;
            OPT_DefUse.removeInstructionAndUpdateDU(instr);
        }
    }

    /**
   * Perform constant folding.
   *
   * @param ir the IR to optimize
   */
    void foldConstants(OPT_IR ir) {
        boolean recomputeRegList = false;
        for (OPT_Instruction s = ir.firstInstructionInCodeOrder(); s != null; s = s.nextInstructionInCodeOrder()) {
            byte code = 0;
            OPT_Simplifier.simplify(s);
            recomputeRegList |= (code == OPT_Simplifier.MOVE_REDUCED || code == OPT_Simplifier.TRAP_REDUCED || code == OPT_Simplifier.REDUCED);
        }
        if (recomputeRegList) {
            OPT_DefUse.computeDU(ir);
            OPT_DefUse.recomputeSSA(ir);
        }
    }

    /**
   * Simplify branches whose operands are constants.
   *
   * <p> NOTE: This pass ensures that the register list is still valid after it 
   * is done.
   *
   * @param ir the IR to optimize
   */
    void simplifyConstantBranches(OPT_IR ir) {
        boolean didSomething = false;
        for (OPT_BasicBlockEnumeration e = ir.forwardBlockEnumerator(); e.hasMoreElements(); ) {
            OPT_BasicBlock bb = e.next();
            didSomething |= OPT_BranchSimplifier.simplify(bb, ir);
        }
        if (didSomething) {
            branchOpts.perform(ir, true);
            OPT_DefUse.computeDU(ir);
            OPT_DefUse.recomputeSSA(ir);
        }
    }
}

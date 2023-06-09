package com.ibm.JikesRVM.opt;

import com.ibm.JikesRVM.*;
import com.ibm.JikesRVM.classloader.*;
import com.ibm.JikesRVM.opt.ir.*;
import com.ibm.JikesRVM.memoryManagers.vmInterface.MM_Interface;

/**
 * As part of the expansion of HIR into LIR, this compile phase
 * replaces all HIR operators that are implemented as calls to
 * VM service routines with CALLs to those routines.  
 * For some (common and performance critical) operators, we 
 * may optionally inline expand the call (depending on the 
 * the values of the relevant compiler options and/or VM_Controls).
 * This pass is also responsible for inserting write barriers
 * if we are using an allocator that requires them. Write barriers
 * are always inline expanded.
 *   
 * @author Stephen Fink
 * @author Dave Grove
 * @author Martin Trapp
 */
public final class OPT_ExpandRuntimeServices extends OPT_CompilerPhase implements OPT_Operators, VM_Constants, OPT_Constants {

    public boolean shouldPerform(OPT_Options options) {
        return true;
    }

    public final String getName() {
        return "Expand Runtime Services";
    }

    public void reportAdditionalStats() {
        VM.sysWrite("  ");
        VM.sysWrite(container.counter1 / container.counter2 * 100, 2);
        VM.sysWrite("% Infrequent RS calls");
    }

    /** 
   * Given an HIR, expand operators that are implemented as calls to
   * runtime service methods. This method should be called as one of the
   * first steps in lowering HIR into LIR.
   * 
   * @param OPT_IR HIR to expand
   */
    public void perform(OPT_IR ir) {
        ir.gc.resync();
        OPT_Instruction next;
        for (OPT_Instruction inst = ir.firstInstructionInCodeOrder(); inst != null; inst = next) {
            next = inst.nextInstructionInCodeOrder();
            int opcode = inst.getOpcode();
            switch(opcode) {
                case NEW_opcode:
                    {
                        OPT_TypeOperand Type = New.getClearType(inst);
                        VM_Class cls = (VM_Class) Type.getVMType();
                        OPT_IntConstantOperand hasFinalizer = new OPT_IntConstantOperand(cls.hasFinalizer() ? 1 : 0);
                        OPT_IntConstantOperand allocator = new OPT_IntConstantOperand(MM_Interface.pickAllocator(cls));
                        VM_Method target = VM_Entrypoints.resolvedNewScalarMethod;
                        Call.mutate4(inst, CALL, New.getClearResult(inst), new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), new OPT_IntConstantOperand(cls.getInstanceSize()), OPT_ConvertToLowLevelIR.getTIB(inst, ir, Type), hasFinalizer, allocator);
                        if (ir.options.INLINE_NEW) {
                            if (inst.getBasicBlock().getInfrequent()) container.counter1++;
                            container.counter2++;
                            if (!ir.options.FREQ_FOCUS_EFFORT || !inst.getBasicBlock().getInfrequent()) {
                                inline(inst, ir);
                            }
                        }
                    }
                    break;
                case NEW_UNRESOLVED_opcode:
                    {
                        int typeRefId = New.getType(inst).getTypeRef().getId();
                        VM_Method target = VM_Entrypoints.unresolvedNewScalarMethod;
                        Call.mutate1(inst, CALL, New.getClearResult(inst), new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), new OPT_IntConstantOperand(typeRefId));
                    }
                    break;
                case NEWARRAY_opcode:
                    {
                        OPT_TypeOperand Array = NewArray.getClearType(inst);
                        VM_Array array = (VM_Array) Array.getVMType();
                        OPT_Operand numberElements = NewArray.getClearSize(inst);
                        OPT_Operand size = null;
                        if (numberElements instanceof OPT_RegisterOperand) {
                            int width = array.getLogElementSize();
                            OPT_RegisterOperand temp = numberElements.asRegister();
                            if (width != 0) {
                                temp = OPT_ConvertToLowLevelIR.InsertBinary(inst, ir, INT_SHL, VM_TypeReference.Int, temp, new OPT_IntConstantOperand(width));
                            }
                            size = OPT_ConvertToLowLevelIR.InsertBinary(inst, ir, INT_ADD, VM_TypeReference.Int, temp, new OPT_IntConstantOperand(VM_ObjectModel.computeArrayHeaderSize(array)));
                        } else {
                            size = new OPT_IntConstantOperand(array.getInstanceSize(numberElements.asIntConstant().value));
                        }
                        OPT_IntConstantOperand allocator = new OPT_IntConstantOperand(MM_Interface.pickAllocator(array));
                        VM_Method target = VM_Entrypoints.resolvedNewArrayMethod;
                        Call.mutate4(inst, CALL, NewArray.getClearResult(inst), new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), numberElements.copy(), size, OPT_ConvertToLowLevelIR.getTIB(inst, ir, Array), allocator);
                        if (ir.options.INLINE_NEW) {
                            if (inst.getBasicBlock().getInfrequent()) container.counter1++;
                            container.counter2++;
                            if (!ir.options.FREQ_FOCUS_EFFORT || !inst.getBasicBlock().getInfrequent()) {
                                inline(inst, ir);
                            }
                        }
                    }
                    break;
                case NEWARRAY_UNRESOLVED_opcode:
                    {
                        int typeRefId = NewArray.getType(inst).getTypeRef().getId();
                        OPT_Operand numberElements = NewArray.getClearSize(inst);
                        VM_Method target = VM_Entrypoints.unresolvedNewArrayMethod;
                        Call.mutate2(inst, CALL, NewArray.getClearResult(inst), new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), numberElements, new OPT_IntConstantOperand(typeRefId));
                    }
                    break;
                case NEWOBJMULTIARRAY_opcode:
                    {
                        int typeRefId = NewArray.getType(inst).getTypeRef().getId();
                        VM_Method target = VM_Entrypoints.optNewArrayArrayMethod;
                        Call.mutate2(inst, CALL, NewArray.getClearResult(inst), new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), NewArray.getClearSize(inst), new OPT_IntConstantOperand(typeRefId));
                    }
                    break;
                case ATHROW_opcode:
                    {
                        VM_Method target = VM_Entrypoints.athrowMethod;
                        OPT_MethodOperand methodOp = OPT_MethodOperand.STATIC(target);
                        methodOp.setIsNonReturningCall(true);
                        Call.mutate1(inst, CALL, null, new OPT_IntConstantOperand(target.getOffset()), methodOp, Athrow.getClearValue(inst));
                    }
                    break;
                case MONITORENTER_opcode:
                    {
                        if (ir.options.NO_SYNCHRO) {
                            inst.remove();
                        } else {
                            OPT_Operand ref = MonitorOp.getClearRef(inst);
                            VM_Type refType = ref.getType().peekResolvedType();
                            if (refType != null && refType.getThinLockOffset() != -1) {
                                VM_Method target = VM_Entrypoints.inlineLockMethod;
                                Call.mutate2(inst, CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), MonitorOp.getClearGuard(inst), ref, new OPT_IntConstantOperand(refType.getThinLockOffset()));
                                if (inst.getBasicBlock().getInfrequent()) container.counter1++;
                                container.counter2++;
                                if (!ir.options.FREQ_FOCUS_EFFORT || !inst.getBasicBlock().getInfrequent()) {
                                    inline(inst, ir);
                                }
                            } else {
                                VM_Method target = VM_Entrypoints.lockMethod;
                                Call.mutate1(inst, CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), MonitorOp.getClearGuard(inst), ref);
                            }
                        }
                        break;
                    }
                case MONITOREXIT_opcode:
                    {
                        if (ir.options.NO_SYNCHRO) {
                            inst.remove();
                        } else {
                            OPT_Operand ref = MonitorOp.getClearRef(inst);
                            VM_Type refType = ref.getType().peekResolvedType();
                            if (refType != null && refType.getThinLockOffset() != -1) {
                                VM_Method target = VM_Entrypoints.inlineUnlockMethod;
                                Call.mutate2(inst, CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), MonitorOp.getClearGuard(inst), ref, new OPT_IntConstantOperand(refType.getThinLockOffset()));
                                if (inst.getBasicBlock().getInfrequent()) container.counter1++;
                                container.counter2++;
                                if (!ir.options.FREQ_FOCUS_EFFORT || !inst.getBasicBlock().getInfrequent()) {
                                    inline(inst, ir);
                                }
                            } else {
                                VM_Method target = VM_Entrypoints.unlockMethod;
                                Call.mutate1(inst, CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), MonitorOp.getClearGuard(inst), ref);
                            }
                        }
                    }
                    break;
                case REF_ASTORE_opcode:
                    {
                        if (MM_Interface.NEEDS_WRITE_BARRIER) {
                            VM_Method target = VM_Entrypoints.arrayStoreWriteBarrierMethod;
                            OPT_Instruction wb = Call.create3(CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), AStore.getArray(inst).copy(), AStore.getIndex(inst).copy(), AStore.getValue(inst).copy());
                            wb.bcIndex = RUNTIME_SERVICES_BCI;
                            wb.position = inst.position;
                            inst.replace(wb);
                            next = wb.nextInstructionInCodeOrder();
                            inline(wb, ir, true);
                        }
                    }
                    break;
                case PUTFIELD_opcode:
                    {
                        if (MM_Interface.NEEDS_WRITE_BARRIER) {
                            OPT_LocationOperand loc = PutField.getClearLocation(inst);
                            VM_FieldReference field = loc.getFieldRef();
                            if (!field.getFieldContentsType().isPrimitiveType()) {
                                VM_Method target = VM_Entrypoints.putfieldWriteBarrierMethod;
                                OPT_Instruction wb = Call.create3(CALL, null, new OPT_IntConstantOperand(target.getOffset()), OPT_MethodOperand.STATIC(target), PutField.getRef(inst).copy(), PutField.getOffset(inst).copy(), PutField.getValue(inst).copy());
                                wb.bcIndex = RUNTIME_SERVICES_BCI;
                                wb.position = inst.position;
                                inst.replace(wb);
                                next = wb.nextInstructionInCodeOrder();
                                inline(wb, ir);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        if (didSomething) {
            branchOpts.perform(ir, true);
            _os.perform(ir);
        }
        ir.gc.close();
    }

    /**
   * Inline a call instruction
   */
    private void inline(OPT_Instruction inst, OPT_IR ir) {
        inline(inst, ir, false);
    }

    /**
   * Inline a call instruction
   */
    private void inline(OPT_Instruction inst, OPT_IR ir, boolean noCalleeExceptions) {
        boolean savedInliningOption = ir.options.INLINE;
        boolean savedExceptionOption = ir.options.NO_CALLEE_EXCEPTIONS;
        ir.options.INLINE = true;
        ir.options.NO_CALLEE_EXCEPTIONS = noCalleeExceptions;
        boolean savedOsrGI = ir.options.OSR_GUARDED_INLINING;
        ir.options.OSR_GUARDED_INLINING = false;
        try {
            OPT_InlineDecision inlDec = OPT_InlineDecision.YES(Call.getMethod(inst).getTarget(), "Expansion of runtime service");
            OPT_Inliner.execute(inlDec, ir, inst);
        } finally {
            ir.options.INLINE = savedInliningOption;
            ir.options.NO_CALLEE_EXCEPTIONS = savedExceptionOption;
            ir.options.OSR_GUARDED_INLINING = savedOsrGI;
        }
        didSomething = true;
    }

    private OPT_Simple _os = new OPT_Simple(false, false);

    private OPT_BranchOptimizations branchOpts = new OPT_BranchOptimizations(-1, true, true);

    private boolean didSomething = false;
}

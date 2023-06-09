package org.eclipse.jdt.internal.compiler.ast;

import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.jdt.internal.compiler.codegen.CodeStream;
import org.eclipse.jdt.internal.compiler.codegen.Opcodes;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.impl.Constant;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.InvocationSite;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReasons;
import org.eclipse.jdt.internal.compiler.lookup.ProblemReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;

public class FieldReference extends Reference implements InvocationSite {

    public static final int READ = 0;

    public static final int WRITE = 1;

    public Expression receiver;

    public char[] token;

    public FieldBinding binding;

    public MethodBinding[] syntheticAccessors;

    public long nameSourcePosition;

    public TypeBinding actualReceiverType;

    public TypeBinding genericCast;

    public FieldReference(char[] source, long pos) {
        this.token = source;
        this.nameSourcePosition = pos;
        this.sourceStart = (int) (pos >>> 32);
        this.sourceEnd = (int) (pos & 0x00000000FFFFFFFFL);
        this.bits |= Binding.FIELD;
    }

    public FlowInfo analyseAssignment(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Assignment assignment, boolean isCompound) {
        if (isCompound) {
            if (this.binding.isBlankFinal() && this.receiver.isThis() && currentScope.needBlankFinalFieldInitializationCheck(this.binding)) {
                FlowInfo fieldInits = flowContext.getInitsForFinalBlankInitializationCheck(this.binding.declaringClass.original(), flowInfo);
                if (!fieldInits.isDefinitelyAssigned(this.binding)) {
                    currentScope.problemReporter().uninitializedBlankFinalField(this.binding, this);
                }
            }
            manageSyntheticAccessIfNecessary(currentScope, flowInfo, true);
        }
        flowInfo = this.receiver.analyseCode(currentScope, flowContext, flowInfo, !this.binding.isStatic()).unconditionalInits();
        if (assignment.expression != null) {
            flowInfo = assignment.expression.analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
        }
        manageSyntheticAccessIfNecessary(currentScope, flowInfo, false);
        if (this.binding.isFinal()) {
            if (this.binding.isBlankFinal() && !isCompound && this.receiver.isThis() && !(this.receiver instanceof QualifiedThisReference) && ((this.receiver.bits & ASTNode.ParenthesizedMASK) == 0) && currentScope.allowBlankFinalFieldAssignment(this.binding)) {
                if (flowInfo.isPotentiallyAssigned(this.binding)) {
                    currentScope.problemReporter().duplicateInitializationOfBlankFinalField(this.binding, this);
                } else {
                    flowContext.recordSettingFinal(this.binding, this, flowInfo);
                }
                flowInfo.markAsDefinitelyAssigned(this.binding);
            } else {
                currentScope.problemReporter().cannotAssignToFinalField(this.binding, this);
            }
        }
        return flowInfo;
    }

    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) {
        return analyseCode(currentScope, flowContext, flowInfo, true);
    }

    public FlowInfo analyseCode(BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) {
        boolean nonStatic = !this.binding.isStatic();
        this.receiver.analyseCode(currentScope, flowContext, flowInfo, nonStatic);
        if (nonStatic) {
            this.receiver.checkNPE(currentScope, flowContext, flowInfo);
        }
        if (valueRequired || currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4) {
            manageSyntheticAccessIfNecessary(currentScope, flowInfo, true);
        }
        return flowInfo;
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.ast.Expression#computeConversion(org.eclipse.jdt.internal.compiler.lookup.Scope, org.eclipse.jdt.internal.compiler.lookup.TypeBinding, org.eclipse.jdt.internal.compiler.lookup.TypeBinding)
 */
    public void computeConversion(Scope scope, TypeBinding runtimeTimeType, TypeBinding compileTimeType) {
        if (runtimeTimeType == null || compileTimeType == null) return;
        if (this.binding != null && this.binding.isValidBinding()) {
            FieldBinding originalBinding = this.binding.original();
            TypeBinding originalType = originalBinding.type;
            if (originalType.leafComponentType().isTypeVariable()) {
                TypeBinding targetType = (!compileTimeType.isBaseType() && runtimeTimeType.isBaseType()) ? compileTimeType : runtimeTimeType;
                this.genericCast = originalBinding.type.genericCast(targetType);
                if (this.genericCast instanceof ReferenceBinding) {
                    ReferenceBinding referenceCast = (ReferenceBinding) this.genericCast;
                    if (!referenceCast.canBeSeenBy(scope)) {
                        scope.problemReporter().invalidType(this, new ProblemReferenceBinding(CharOperation.splitOn('.', referenceCast.shortReadableName()), referenceCast, ProblemReasons.NotVisible));
                    }
                }
            }
        }
        super.computeConversion(scope, runtimeTimeType, compileTimeType);
    }

    public FieldBinding fieldBinding() {
        return this.binding;
    }

    public void generateAssignment(BlockScope currentScope, CodeStream codeStream, Assignment assignment, boolean valueRequired) {
        int pc = codeStream.position;
        FieldBinding codegenBinding = this.binding.original();
        this.receiver.generateCode(currentScope, codeStream, !codegenBinding.isStatic());
        codeStream.recordPositionsFrom(pc, this.sourceStart);
        assignment.expression.generateCode(currentScope, codeStream, true);
        fieldStore(currentScope, codeStream, codegenBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[FieldReference.WRITE], this.actualReceiverType, this.receiver.isImplicitThis(), valueRequired);
        if (valueRequired) {
            codeStream.generateImplicitConversion(assignment.implicitConversion);
        }
    }

    /**
 * Field reference code generation
 *
 * @param currentScope org.eclipse.jdt.internal.compiler.lookup.BlockScope
 * @param codeStream org.eclipse.jdt.internal.compiler.codegen.CodeStream
 * @param valueRequired boolean
 */
    public void generateCode(BlockScope currentScope, CodeStream codeStream, boolean valueRequired) {
        int pc = codeStream.position;
        if (this.constant != Constant.NotAConstant) {
            if (valueRequired) {
                codeStream.generateConstant(this.constant, this.implicitConversion);
            }
            codeStream.recordPositionsFrom(pc, this.sourceStart);
            return;
        }
        FieldBinding codegenBinding = this.binding.original();
        boolean isStatic = codegenBinding.isStatic();
        boolean isThisReceiver = this.receiver instanceof ThisReference;
        Constant fieldConstant = codegenBinding.constant();
        if (fieldConstant != Constant.NotAConstant) {
            if (!isThisReceiver) {
                this.receiver.generateCode(currentScope, codeStream, !isStatic);
                if (!isStatic) {
                    codeStream.invokeObjectGetClass();
                    codeStream.pop();
                }
            }
            if (valueRequired) {
                codeStream.generateConstant(fieldConstant, this.implicitConversion);
            }
            codeStream.recordPositionsFrom(pc, this.sourceStart);
            return;
        }
        if (valueRequired || (!isThisReceiver && currentScope.compilerOptions().complianceLevel >= ClassFileConstants.JDK1_4) || ((this.implicitConversion & TypeIds.UNBOXING) != 0) || (this.genericCast != null)) {
            this.receiver.generateCode(currentScope, codeStream, !isStatic);
            if ((this.bits & NeedReceiverGenericCast) != 0) {
                codeStream.checkcast(this.actualReceiverType);
            }
            pc = codeStream.position;
            if (codegenBinding.declaringClass == null) {
                codeStream.arraylength();
                if (valueRequired) {
                    codeStream.generateImplicitConversion(this.implicitConversion);
                } else {
                    codeStream.pop();
                }
            } else {
                if (this.syntheticAccessors == null || this.syntheticAccessors[FieldReference.READ] == null) {
                    TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                    if (isStatic) {
                        codeStream.fieldAccess(Opcodes.OPC_getstatic, codegenBinding, constantPoolDeclaringClass);
                    } else {
                        codeStream.fieldAccess(Opcodes.OPC_getfield, codegenBinding, constantPoolDeclaringClass);
                    }
                } else {
                    codeStream.invoke(Opcodes.OPC_invokestatic, this.syntheticAccessors[FieldReference.READ], null);
                }
                if (this.genericCast != null) codeStream.checkcast(this.genericCast);
                if (valueRequired) {
                    codeStream.generateImplicitConversion(this.implicitConversion);
                } else {
                    boolean isUnboxing = (this.implicitConversion & TypeIds.UNBOXING) != 0;
                    if (isUnboxing) codeStream.generateImplicitConversion(this.implicitConversion);
                    switch(isUnboxing ? postConversionType(currentScope).id : codegenBinding.type.id) {
                        case T_long:
                        case T_double:
                            codeStream.pop2();
                            break;
                        default:
                            codeStream.pop();
                    }
                }
            }
        } else {
            if (isThisReceiver) {
                if (isStatic) {
                    if (this.binding.original().declaringClass != this.actualReceiverType.erasure()) {
                        MethodBinding accessor = this.syntheticAccessors == null ? null : this.syntheticAccessors[FieldReference.READ];
                        if (accessor == null) {
                            TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                            codeStream.fieldAccess(Opcodes.OPC_getstatic, codegenBinding, constantPoolDeclaringClass);
                        } else {
                            codeStream.invoke(Opcodes.OPC_invokestatic, accessor, null);
                        }
                        switch(codegenBinding.type.id) {
                            case T_long:
                            case T_double:
                                codeStream.pop2();
                                break;
                            default:
                                codeStream.pop();
                        }
                    }
                }
            } else {
                this.receiver.generateCode(currentScope, codeStream, !isStatic);
                if (!isStatic) {
                    codeStream.invokeObjectGetClass();
                    codeStream.pop();
                }
            }
        }
        codeStream.recordPositionsFrom(pc, this.sourceEnd);
    }

    public void generateCompoundAssignment(BlockScope currentScope, CodeStream codeStream, Expression expression, int operator, int assignmentImplicitConversion, boolean valueRequired) {
        boolean isStatic;
        FieldBinding codegenBinding = this.binding.original();
        this.receiver.generateCode(currentScope, codeStream, !(isStatic = codegenBinding.isStatic()));
        if (isStatic) {
            if (this.syntheticAccessors == null || this.syntheticAccessors[FieldReference.READ] == null) {
                TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                codeStream.fieldAccess(Opcodes.OPC_getstatic, codegenBinding, constantPoolDeclaringClass);
            } else {
                codeStream.invoke(Opcodes.OPC_invokestatic, this.syntheticAccessors[FieldReference.READ], null);
            }
        } else {
            codeStream.dup();
            if (this.syntheticAccessors == null || this.syntheticAccessors[FieldReference.READ] == null) {
                TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                codeStream.fieldAccess(Opcodes.OPC_getfield, codegenBinding, constantPoolDeclaringClass);
            } else {
                codeStream.invoke(Opcodes.OPC_invokestatic, this.syntheticAccessors[FieldReference.READ], null);
            }
        }
        int operationTypeID;
        switch(operationTypeID = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4) {
            case T_JavaLangString:
            case T_JavaLangObject:
            case T_undefined:
                codeStream.generateStringConcatenationAppend(currentScope, null, expression);
                break;
            default:
                if (this.genericCast != null) codeStream.checkcast(this.genericCast);
                codeStream.generateImplicitConversion(this.implicitConversion);
                if (expression == IntLiteral.One) {
                    codeStream.generateConstant(expression.constant, this.implicitConversion);
                } else {
                    expression.generateCode(currentScope, codeStream, true);
                }
                codeStream.sendOperator(operator, operationTypeID);
                codeStream.generateImplicitConversion(assignmentImplicitConversion);
        }
        fieldStore(currentScope, codeStream, codegenBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[FieldReference.WRITE], this.actualReceiverType, this.receiver.isImplicitThis(), valueRequired);
    }

    public void generatePostIncrement(BlockScope currentScope, CodeStream codeStream, CompoundAssignment postIncrement, boolean valueRequired) {
        boolean isStatic;
        FieldBinding codegenBinding = this.binding.original();
        this.receiver.generateCode(currentScope, codeStream, !(isStatic = codegenBinding.isStatic()));
        if (isStatic) {
            if (this.syntheticAccessors == null || this.syntheticAccessors[FieldReference.READ] == null) {
                TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                codeStream.fieldAccess(Opcodes.OPC_getstatic, codegenBinding, constantPoolDeclaringClass);
            } else {
                codeStream.invoke(Opcodes.OPC_invokestatic, this.syntheticAccessors[FieldReference.READ], null);
            }
        } else {
            codeStream.dup();
            if (this.syntheticAccessors == null || this.syntheticAccessors[FieldReference.READ] == null) {
                TypeBinding constantPoolDeclaringClass = CodeStream.getConstantPoolDeclaringClass(currentScope, codegenBinding, this.actualReceiverType, this.receiver.isImplicitThis());
                codeStream.fieldAccess(Opcodes.OPC_getfield, codegenBinding, constantPoolDeclaringClass);
            } else {
                codeStream.invoke(Opcodes.OPC_invokestatic, this.syntheticAccessors[FieldReference.READ], null);
            }
        }
        TypeBinding operandType;
        if (this.genericCast != null) {
            codeStream.checkcast(this.genericCast);
            operandType = this.genericCast;
        } else {
            operandType = codegenBinding.type;
        }
        if (valueRequired) {
            if (isStatic) {
                switch(operandType.id) {
                    case TypeIds.T_long:
                    case TypeIds.T_double:
                        codeStream.dup2();
                        break;
                    default:
                        codeStream.dup();
                        break;
                }
            } else {
                switch(operandType.id) {
                    case TypeIds.T_long:
                    case TypeIds.T_double:
                        codeStream.dup2_x1();
                        break;
                    default:
                        codeStream.dup_x1();
                        break;
                }
            }
        }
        codeStream.generateImplicitConversion(this.implicitConversion);
        codeStream.generateConstant(postIncrement.expression.constant, this.implicitConversion);
        codeStream.sendOperator(postIncrement.operator, this.implicitConversion & TypeIds.COMPILE_TYPE_MASK);
        codeStream.generateImplicitConversion(postIncrement.preAssignImplicitConversion);
        fieldStore(currentScope, codeStream, codegenBinding, this.syntheticAccessors == null ? null : this.syntheticAccessors[FieldReference.WRITE], this.actualReceiverType, this.receiver.isImplicitThis(), false);
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.lookup.InvocationSite#genericTypeArguments()
 */
    public TypeBinding[] genericTypeArguments() {
        return null;
    }

    public boolean isSuperAccess() {
        return this.receiver.isSuper();
    }

    public boolean isTypeAccess() {
        return this.receiver != null && this.receiver.isTypeReference();
    }

    public void manageSyntheticAccessIfNecessary(BlockScope currentScope, FlowInfo flowInfo, boolean isReadAccess) {
        if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) != 0) return;
        FieldBinding codegenBinding = this.binding.original();
        if (this.binding.isPrivate()) {
            if ((currentScope.enclosingSourceType() != codegenBinding.declaringClass) && this.binding.constant() == Constant.NotAConstant) {
                if (this.syntheticAccessors == null) this.syntheticAccessors = new MethodBinding[2];
                this.syntheticAccessors[isReadAccess ? FieldReference.READ : FieldReference.WRITE] = ((SourceTypeBinding) codegenBinding.declaringClass).addSyntheticMethod(codegenBinding, isReadAccess, false);
                currentScope.problemReporter().needToEmulateFieldAccess(codegenBinding, this, isReadAccess);
                return;
            }
        } else if (this.receiver instanceof QualifiedSuperReference) {
            SourceTypeBinding destinationType = (SourceTypeBinding) (((QualifiedSuperReference) this.receiver).currentCompatibleType);
            if (this.syntheticAccessors == null) this.syntheticAccessors = new MethodBinding[2];
            this.syntheticAccessors[isReadAccess ? FieldReference.READ : FieldReference.WRITE] = destinationType.addSyntheticMethod(codegenBinding, isReadAccess, isSuperAccess());
            currentScope.problemReporter().needToEmulateFieldAccess(codegenBinding, this, isReadAccess);
            return;
        } else if (this.binding.isProtected()) {
            SourceTypeBinding enclosingSourceType;
            if (((this.bits & ASTNode.DepthMASK) != 0) && this.binding.declaringClass.getPackage() != (enclosingSourceType = currentScope.enclosingSourceType()).getPackage()) {
                SourceTypeBinding currentCompatibleType = (SourceTypeBinding) enclosingSourceType.enclosingTypeAt((this.bits & ASTNode.DepthMASK) >> ASTNode.DepthSHIFT);
                if (this.syntheticAccessors == null) this.syntheticAccessors = new MethodBinding[2];
                this.syntheticAccessors[isReadAccess ? FieldReference.READ : FieldReference.WRITE] = currentCompatibleType.addSyntheticMethod(codegenBinding, isReadAccess, isSuperAccess());
                currentScope.problemReporter().needToEmulateFieldAccess(codegenBinding, this, isReadAccess);
                return;
            }
        }
    }

    public int nullStatus(FlowInfo flowInfo) {
        return FlowInfo.UNKNOWN;
    }

    public Constant optimizedBooleanConstant() {
        switch(this.resolvedType.id) {
            case T_boolean:
            case T_JavaLangBoolean:
                return this.constant != Constant.NotAConstant ? this.constant : this.binding.constant();
            default:
                return Constant.NotAConstant;
        }
    }

    /**
 * @see org.eclipse.jdt.internal.compiler.ast.Expression#postConversionType(Scope)
 */
    public TypeBinding postConversionType(Scope scope) {
        TypeBinding convertedType = this.resolvedType;
        if (this.genericCast != null) convertedType = this.genericCast;
        int runtimeType = (this.implicitConversion & TypeIds.IMPLICIT_CONVERSION_MASK) >> 4;
        switch(runtimeType) {
            case T_boolean:
                convertedType = TypeBinding.BOOLEAN;
                break;
            case T_byte:
                convertedType = TypeBinding.BYTE;
                break;
            case T_short:
                convertedType = TypeBinding.SHORT;
                break;
            case T_char:
                convertedType = TypeBinding.CHAR;
                break;
            case T_int:
                convertedType = TypeBinding.INT;
                break;
            case T_float:
                convertedType = TypeBinding.FLOAT;
                break;
            case T_long:
                convertedType = TypeBinding.LONG;
                break;
            case T_double:
                convertedType = TypeBinding.DOUBLE;
                break;
            default:
        }
        if ((this.implicitConversion & TypeIds.BOXING) != 0) {
            convertedType = scope.environment().computeBoxingType(convertedType);
        }
        return convertedType;
    }

    public StringBuffer printExpression(int indent, StringBuffer output) {
        return this.receiver.printExpression(0, output).append('.').append(this.token);
    }

    public TypeBinding resolveType(BlockScope scope) {
        boolean receiverCast = false;
        if (this.receiver instanceof CastExpression) {
            this.receiver.bits |= ASTNode.DisableUnnecessaryCastCheck;
            receiverCast = true;
        }
        this.actualReceiverType = this.receiver.resolveType(scope);
        if (this.actualReceiverType == null) {
            this.constant = Constant.NotAConstant;
            return null;
        }
        if (receiverCast) {
            if (((CastExpression) this.receiver).expression.resolvedType == this.actualReceiverType) {
                scope.problemReporter().unnecessaryCast((CastExpression) this.receiver);
            }
        }
        FieldBinding fieldBinding = this.binding = scope.getField(this.actualReceiverType, this.token, this);
        if (!fieldBinding.isValidBinding()) {
            this.constant = Constant.NotAConstant;
            if (this.receiver.resolvedType instanceof ProblemReferenceBinding) {
                return null;
            }
            scope.problemReporter().invalidField(this, this.actualReceiverType);
            return null;
        }
        TypeBinding oldReceiverType = this.actualReceiverType;
        this.actualReceiverType = this.actualReceiverType.getErasureCompatibleType(fieldBinding.declaringClass);
        this.receiver.computeConversion(scope, this.actualReceiverType, this.actualReceiverType);
        if (this.actualReceiverType != oldReceiverType && this.receiver.postConversionType(scope) != this.actualReceiverType) {
            this.bits |= NeedReceiverGenericCast;
        }
        if (isFieldUseDeprecated(fieldBinding, scope, (this.bits & ASTNode.IsStrictlyAssigned) != 0)) {
            scope.problemReporter().deprecatedField(fieldBinding, this);
        }
        boolean isImplicitThisRcv = this.receiver.isImplicitThis();
        this.constant = isImplicitThisRcv ? fieldBinding.constant() : Constant.NotAConstant;
        if (fieldBinding.isStatic()) {
            if (!(isImplicitThisRcv || (this.receiver instanceof NameReference && (((NameReference) this.receiver).bits & Binding.TYPE) != 0))) {
                scope.problemReporter().nonStaticAccessToStaticField(this, fieldBinding);
            }
            ReferenceBinding declaringClass = this.binding.declaringClass;
            if (!isImplicitThisRcv && declaringClass != this.actualReceiverType && declaringClass.canBeSeenBy(scope)) {
                scope.problemReporter().indirectAccessToStaticField(this, fieldBinding);
            }
            if (declaringClass.isEnum()) {
                MethodScope methodScope = scope.methodScope();
                SourceTypeBinding sourceType = scope.enclosingSourceType();
                if (this.constant == Constant.NotAConstant && !methodScope.isStatic && (sourceType == declaringClass || sourceType.superclass == declaringClass) && methodScope.isInsideInitializerOrConstructor()) {
                    scope.problemReporter().enumStaticFieldUsedDuringInitialization(this.binding, this);
                }
            }
        }
        TypeBinding fieldType = fieldBinding.type;
        if (fieldType != null) {
            if ((this.bits & ASTNode.IsStrictlyAssigned) == 0) {
                fieldType = fieldType.capture(scope, this.sourceEnd);
            }
            this.resolvedType = fieldType;
            if ((fieldType.tagBits & TagBits.HasMissingType) != 0) {
                scope.problemReporter().invalidType(this, fieldType);
                return null;
            }
        }
        return fieldType;
    }

    public void setActualReceiverType(ReferenceBinding receiverType) {
        this.actualReceiverType = receiverType;
    }

    public void setDepth(int depth) {
        this.bits &= ~ASTNode.DepthMASK;
        if (depth > 0) {
            this.bits |= (depth & 0xFF) << ASTNode.DepthSHIFT;
        }
    }

    public void setFieldIndex(int index) {
    }

    public void traverse(ASTVisitor visitor, BlockScope scope) {
        if (visitor.visit(this, scope)) {
            this.receiver.traverse(visitor, scope);
        }
        visitor.endVisit(this, scope);
    }
}

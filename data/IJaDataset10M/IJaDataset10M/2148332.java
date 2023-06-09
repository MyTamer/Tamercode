package hu.e.parser.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;
import com.google.inject.Inject;
import hu.e.parser.services.ESyntaxGrammarAccess;

public class ESyntaxParser extends AbstractContentAssistParser {

    @Inject
    private ESyntaxGrammarAccess grammarAccess;

    private Map<AbstractElement, String> nameMappings;

    @Override
    protected hu.e.parser.ui.contentassist.antlr.internal.InternalESyntaxParser createParser() {
        hu.e.parser.ui.contentassist.antlr.internal.InternalESyntaxParser result = new hu.e.parser.ui.contentassist.antlr.internal.InternalESyntaxParser(null);
        result.setGrammarAccess(grammarAccess);
        return result;
    }

    @Override
    protected String getRuleName(AbstractElement element) {
        if (nameMappings == null) {
            nameMappings = new HashMap<AbstractElement, String>() {

                private static final long serialVersionUID = 1L;

                {
                    put(grammarAccess.getTopLevelItemAccess().getAlternatives(), "rule__TopLevelItem__Alternatives");
                    put(grammarAccess.getLITERALAccess().getAlternatives(), "rule__LITERAL__Alternatives");
                    put(grammarAccess.getTypeDefAccess().getAlternatives(), "rule__TypeDef__Alternatives");
                    put(grammarAccess.getOperationStepAccess().getAlternatives(), "rule__OperationStep__Alternatives");
                    put(grammarAccess.getWordSectionAccess().getAlternatives_0(), "rule__WordSection__Alternatives_0");
                    put(grammarAccess.getVariableRefSectionAccess().getAlternatives(), "rule__VariableRefSection__Alternatives");
                    put(grammarAccess.getOperationCallParameterAccess().getAlternatives(), "rule__OperationCallParameter__Alternatives");
                    put(grammarAccess.getBinarySectionAccess().getAlternatives_0(), "rule__BinarySection__Alternatives_0");
                    put(grammarAccess.getXPrimaryExpressionAccess().getAlternatives(), "rule__XPrimaryExpression__Alternatives");
                    put(grammarAccess.getXTopLevelExpressionAccess().getAlternatives(), "rule__XTopLevelExpression__Alternatives");
                    put(grammarAccess.getPrimitiveKindAccess().getAlternatives(), "rule__PrimitiveKind__Alternatives");
                    put(grammarAccess.getParameterKindAccess().getAlternatives(), "rule__ParameterKind__Alternatives");
                    put(grammarAccess.getOperationRoleAccess().getAlternatives(), "rule__OperationRole__Alternatives");
                    put(grammarAccess.getBinaryTypeAccess().getAlternatives(), "rule__BinaryType__Alternatives");
                    put(grammarAccess.getUNARY_OPERATORAccess().getAlternatives(), "rule__UNARY_OPERATOR__Alternatives");
                    put(grammarAccess.getMULTIPLICATIVE_OPERATORAccess().getAlternatives(), "rule__MULTIPLICATIVE_OPERATOR__Alternatives");
                    put(grammarAccess.getADDITIVE_OPERATORAccess().getAlternatives(), "rule__ADDITIVE_OPERATOR__Alternatives");
                    put(grammarAccess.getEQUALITY_OPERATORAccess().getAlternatives(), "rule__EQUALITY_OPERATOR__Alternatives");
                    put(grammarAccess.getBOOLEAN_OPERATORAccess().getAlternatives(), "rule__BOOLEAN_OPERATOR__Alternatives");
                    put(grammarAccess.getPackageAccess().getGroup(), "rule__Package__Group__0");
                    put(grammarAccess.getPackageAccess().getGroup_3(), "rule__Package__Group_3__0");
                    put(grammarAccess.getTopLevelItemAccess().getGroup_3(), "rule__TopLevelItem__Group_3__0");
                    put(grammarAccess.getTopLevelItemAccess().getGroup_4(), "rule__TopLevelItem__Group_4__0");
                    put(grammarAccess.getTopLevelItemAccess().getGroup_5(), "rule__TopLevelItem__Group_5__0");
                    put(grammarAccess.getTopLevelItemAccess().getGroup_6(), "rule__TopLevelItem__Group_6__0");
                    put(grammarAccess.getQualifiedNameAccess().getGroup(), "rule__QualifiedName__Group__0");
                    put(grammarAccess.getQualifiedNameAccess().getGroup_1(), "rule__QualifiedName__Group_1__0");
                    put(grammarAccess.getTypeAccess().getGroup(), "rule__Type__Group__0");
                    put(grammarAccess.getDataTypeDefAccess().getGroup(), "rule__DataTypeDef__Group__0");
                    put(grammarAccess.getArrayTypeDefAccess().getGroup(), "rule__ArrayTypeDef__Group__0");
                    put(grammarAccess.getStructTypeDefAccess().getGroup(), "rule__StructTypeDef__Group__0");
                    put(grammarAccess.getStructTypeDefAccess().getGroup_4(), "rule__StructTypeDef__Group_4__0");
                    put(grammarAccess.getStructTypeDefMemberAccess().getGroup(), "rule__StructTypeDefMember__Group__0");
                    put(grammarAccess.getConstantVariableAccess().getGroup(), "rule__ConstantVariable__Group__0");
                    put(grammarAccess.getRegisterVariableAccess().getGroup(), "rule__RegisterVariable__Group__0");
                    put(grammarAccess.getCompileContextVariableAccess().getGroup(), "rule__CompileContextVariable__Group__0");
                    put(grammarAccess.getCompileContextVariableAccess().getGroup_3(), "rule__CompileContextVariable__Group_3__0");
                    put(grammarAccess.getOperationStepAccess().getGroup_2(), "rule__OperationStep__Group_2__0");
                    put(grammarAccess.getOperationStepAccess().getGroup_3(), "rule__OperationStep__Group_3__0");
                    put(grammarAccess.getLabelAccess().getGroup(), "rule__Label__Group__0");
                    put(grammarAccess.getInstructionWordAccess().getGroup(), "rule__InstructionWord__Group__0");
                    put(grammarAccess.getWordSectionAccess().getGroup(), "rule__WordSection__Group__0");
                    put(grammarAccess.getWordSectionAccess().getGroup_3(), "rule__WordSection__Group_3__0");
                    put(grammarAccess.getLabelReferenceAccess().getGroup(), "rule__LabelReference__Group__0");
                    put(grammarAccess.getVariableAccess().getGroup(), "rule__Variable__Group__0");
                    put(grammarAccess.getVariableReferenceAccess().getGroup(), "rule__VariableReference__Group__0");
                    put(grammarAccess.getArrayRefAccess().getGroup(), "rule__ArrayRef__Group__0");
                    put(grammarAccess.getStructRefAccess().getGroup(), "rule__StructRef__Group__0");
                    put(grammarAccess.getOperationCallAccess().getGroup(), "rule__OperationCall__Group__0");
                    put(grammarAccess.getOperationCallAccess().getGroup_2(), "rule__OperationCall__Group_2__0");
                    put(grammarAccess.getOperationCallAccess().getGroup_2_1(), "rule__OperationCall__Group_2_1__0");
                    put(grammarAccess.getOperationAccess().getGroup(), "rule__Operation__Group__0");
                    put(grammarAccess.getOperationAccess().getGroup_3(), "rule__Operation__Group_3__0");
                    put(grammarAccess.getOperationAccess().getGroup_3_1(), "rule__Operation__Group_3_1__0");
                    put(grammarAccess.getOperationAccess().getGroup_8(), "rule__Operation__Group_8__0");
                    put(grammarAccess.getOperationBlockAccess().getGroup(), "rule__OperationBlock__Group__0");
                    put(grammarAccess.getParameterVariableAccess().getGroup(), "rule__ParameterVariable__Group__0");
                    put(grammarAccess.getParameterVariableAccess().getGroup_2(), "rule__ParameterVariable__Group_2__0");
                    put(grammarAccess.getOperatorDefinitionAccess().getGroup(), "rule__OperatorDefinition__Group__0");
                    put(grammarAccess.getOperatorDefinitionAccess().getGroup_4(), "rule__OperatorDefinition__Group_4__0");
                    put(grammarAccess.getLinkedBinaryAccess().getGroup(), "rule__LinkedBinary__Group__0");
                    put(grammarAccess.getBinarySectionAccess().getGroup(), "rule__BinarySection__Group__0");
                    put(grammarAccess.getFunctionBinarySectionAccess().getGroup(), "rule__FunctionBinarySection__Group__0");
                    put(grammarAccess.getFunctionBinarySectionAccess().getGroup_7(), "rule__FunctionBinarySection__Group_7__0");
                    put(grammarAccess.getFunctionMemoryAccess().getGroup(), "rule__FunctionMemory__Group__0");
                    put(grammarAccess.getConstantBinarySectionAccess().getGroup(), "rule__ConstantBinarySection__Group__0");
                    put(grammarAccess.getConstantBinarySectionAccess().getGroup_2(), "rule__ConstantBinarySection__Group_2__0");
                    put(grammarAccess.getReferenceBinarySectionAccess().getGroup(), "rule__ReferenceBinarySection__Group__0");
                    put(grammarAccess.getXSizeOfExpressionAccess().getGroup(), "rule__XSizeOfExpression__Group__0");
                    put(grammarAccess.getXStructExpressionAccess().getGroup(), "rule__XStructExpression__Group__0");
                    put(grammarAccess.getXStructExpressionAccess().getGroup_3(), "rule__XStructExpression__Group_3__0");
                    put(grammarAccess.getXExpression1Access().getGroup(), "rule__XExpression1__Group__0");
                    put(grammarAccess.getXExpression2Access().getGroup(), "rule__XExpression2__Group__0");
                    put(grammarAccess.getXExpression2Access().getGroup_1(), "rule__XExpression2__Group_1__0");
                    put(grammarAccess.getXExpression3Access().getGroup(), "rule__XExpression3__Group__0");
                    put(grammarAccess.getXExpression3Access().getGroup_1(), "rule__XExpression3__Group_1__0");
                    put(grammarAccess.getXExpression4Access().getGroup(), "rule__XExpression4__Group__0");
                    put(grammarAccess.getXExpression4Access().getGroup_1(), "rule__XExpression4__Group_1__0");
                    put(grammarAccess.getXExpression5Access().getGroup(), "rule__XExpression5__Group__0");
                    put(grammarAccess.getXExpression5Access().getGroup_1(), "rule__XExpression5__Group_1__0");
                    put(grammarAccess.getXAssignmentAccess().getGroup(), "rule__XAssignment__Group__0");
                    put(grammarAccess.getXAddressOfVarAccess().getGroup(), "rule__XAddressOfVar__Group__0");
                    put(grammarAccess.getXIsLiteralExpressionAccess().getGroup(), "rule__XIsLiteralExpression__Group__0");
                    put(grammarAccess.getXIfExpressionAccess().getGroup(), "rule__XIfExpression__Group__0");
                    put(grammarAccess.getXIfExpressionAccess().getGroup_7(), "rule__XIfExpression__Group_7__0");
                    put(grammarAccess.getXParenthesizedExpressionAccess().getGroup(), "rule__XParenthesizedExpression__Group__0");
                    put(grammarAccess.getPackageAccess().getNameAssignment_1(), "rule__Package__NameAssignment_1");
                    put(grammarAccess.getPackageAccess().getUsesAssignment_3_1(), "rule__Package__UsesAssignment_3_1");
                    put(grammarAccess.getPackageAccess().getItemsAssignment_4(), "rule__Package__ItemsAssignment_4");
                    put(grammarAccess.getTypeAccess().getNameAssignment_1(), "rule__Type__NameAssignment_1");
                    put(grammarAccess.getTypeAccess().getDefAssignment_3(), "rule__Type__DefAssignment_3");
                    put(grammarAccess.getDataTypeDefAccess().getKindAssignment_0(), "rule__DataTypeDef__KindAssignment_0");
                    put(grammarAccess.getDataTypeDefAccess().getBitsAssignment_1(), "rule__DataTypeDef__BitsAssignment_1");
                    put(grammarAccess.getArrayTypeDefAccess().getTypeAssignment_0(), "rule__ArrayTypeDef__TypeAssignment_0");
                    put(grammarAccess.getArrayTypeDefAccess().getLengthAssignment_2(), "rule__ArrayTypeDef__LengthAssignment_2");
                    put(grammarAccess.getStructTypeDefAccess().getMembersAssignment_3(), "rule__StructTypeDef__MembersAssignment_3");
                    put(grammarAccess.getStructTypeDefAccess().getMembersAssignment_4_1(), "rule__StructTypeDef__MembersAssignment_4_1");
                    put(grammarAccess.getStructTypeDefMemberAccess().getTypeAssignment_0(), "rule__StructTypeDefMember__TypeAssignment_0");
                    put(grammarAccess.getStructTypeDefMemberAccess().getNameAssignment_1(), "rule__StructTypeDefMember__NameAssignment_1");
                    put(grammarAccess.getConstantVariableAccess().getTypeAssignment_2(), "rule__ConstantVariable__TypeAssignment_2");
                    put(grammarAccess.getConstantVariableAccess().getNameAssignment_3(), "rule__ConstantVariable__NameAssignment_3");
                    put(grammarAccess.getConstantVariableAccess().getValueAssignment_5(), "rule__ConstantVariable__ValueAssignment_5");
                    put(grammarAccess.getRegisterVariableAccess().getTypeAssignment_2(), "rule__RegisterVariable__TypeAssignment_2");
                    put(grammarAccess.getRegisterVariableAccess().getNameAssignment_3(), "rule__RegisterVariable__NameAssignment_3");
                    put(grammarAccess.getRegisterVariableAccess().getAddrAssignment_5(), "rule__RegisterVariable__AddrAssignment_5");
                    put(grammarAccess.getCompileContextVariableAccess().getNameAssignment_2(), "rule__CompileContextVariable__NameAssignment_2");
                    put(grammarAccess.getCompileContextVariableAccess().getValueAssignment_3_1(), "rule__CompileContextVariable__ValueAssignment_3_1");
                    put(grammarAccess.getLabelAccess().getNameAssignment_1(), "rule__Label__NameAssignment_1");
                    put(grammarAccess.getInstructionWordAccess().getSectionsAssignment_1(), "rule__InstructionWord__SectionsAssignment_1");
                    put(grammarAccess.getWordSectionAccess().getSizeAssignment_2(), "rule__WordSection__SizeAssignment_2");
                    put(grammarAccess.getWordSectionAccess().getShiftAssignment_3_1(), "rule__WordSection__ShiftAssignment_3_1");
                    put(grammarAccess.getLiteralValueAccess().getValueAssignment(), "rule__LiteralValue__ValueAssignment");
                    put(grammarAccess.getLabelReferenceAccess().getLabelAssignment_1(), "rule__LabelReference__LabelAssignment_1");
                    put(grammarAccess.getVariableAccess().getTypeAssignment_0(), "rule__Variable__TypeAssignment_0");
                    put(grammarAccess.getVariableAccess().getNameAssignment_1(), "rule__Variable__NameAssignment_1");
                    put(grammarAccess.getVariableReferenceAccess().getVarAssignment_0(), "rule__VariableReference__VarAssignment_0");
                    put(grammarAccess.getVariableReferenceAccess().getRefAssignment_1(), "rule__VariableReference__RefAssignment_1");
                    put(grammarAccess.getArrayRefAccess().getVAssignment_1(), "rule__ArrayRef__VAssignment_1");
                    put(grammarAccess.getStructRefAccess().getRefAssignment_1(), "rule__StructRef__RefAssignment_1");
                    put(grammarAccess.getOperationCallAccess().getOperationAssignment_0(), "rule__OperationCall__OperationAssignment_0");
                    put(grammarAccess.getOperationCallAccess().getParamsAssignment_2_0(), "rule__OperationCall__ParamsAssignment_2_0");
                    put(grammarAccess.getOperationCallAccess().getParamsAssignment_2_1_1(), "rule__OperationCall__ParamsAssignment_2_1_1");
                    put(grammarAccess.getOperationAccess().getReturntypeAssignment_0(), "rule__Operation__ReturntypeAssignment_0");
                    put(grammarAccess.getOperationAccess().getNameAssignment_1(), "rule__Operation__NameAssignment_1");
                    put(grammarAccess.getOperationAccess().getParamsAssignment_3_0(), "rule__Operation__ParamsAssignment_3_0");
                    put(grammarAccess.getOperationAccess().getParamsAssignment_3_1_1(), "rule__Operation__ParamsAssignment_3_1_1");
                    put(grammarAccess.getOperationAccess().getBlockAssignment_6(), "rule__Operation__BlockAssignment_6");
                    put(grammarAccess.getOperationAccess().getReturnAssignment_8_1(), "rule__Operation__ReturnAssignment_8_1");
                    put(grammarAccess.getOperationBlockAccess().getStepsAssignment_1(), "rule__OperationBlock__StepsAssignment_1");
                    put(grammarAccess.getParameterVariableAccess().getKindAssignment_0(), "rule__ParameterVariable__KindAssignment_0");
                    put(grammarAccess.getParameterVariableAccess().getVarAssignment_1(), "rule__ParameterVariable__VarAssignment_1");
                    put(grammarAccess.getParameterVariableAccess().getDefaultAssignment_2_1(), "rule__ParameterVariable__DefaultAssignment_2_1");
                    put(grammarAccess.getOperatorDefinitionAccess().getRoleAssignment_1(), "rule__OperatorDefinition__RoleAssignment_1");
                    put(grammarAccess.getOperatorDefinitionAccess().getCandidateAssignment_3(), "rule__OperatorDefinition__CandidateAssignment_3");
                    put(grammarAccess.getOperatorDefinitionAccess().getCandidateAssignment_4_1(), "rule__OperatorDefinition__CandidateAssignment_4_1");
                    put(grammarAccess.getLinkedBinaryAccess().getNameAssignment_1(), "rule__LinkedBinary__NameAssignment_1");
                    put(grammarAccess.getLinkedBinaryAccess().getTypeAssignment_2(), "rule__LinkedBinary__TypeAssignment_2");
                    put(grammarAccess.getLinkedBinaryAccess().getSectionsAssignment_4(), "rule__LinkedBinary__SectionsAssignment_4");
                    put(grammarAccess.getBinarySectionAccess().getStartAssignment_2(), "rule__BinarySection__StartAssignment_2");
                    put(grammarAccess.getFunctionBinarySectionAccess().getOperationAssignment_1(), "rule__FunctionBinarySection__OperationAssignment_1");
                    put(grammarAccess.getFunctionBinarySectionAccess().getMemwidthAssignment_5(), "rule__FunctionBinarySection__MemwidthAssignment_5");
                    put(grammarAccess.getFunctionBinarySectionAccess().getMemsAssignment_7_0(), "rule__FunctionBinarySection__MemsAssignment_7_0");
                    put(grammarAccess.getFunctionMemoryAccess().getStartAssignment_1(), "rule__FunctionMemory__StartAssignment_1");
                    put(grammarAccess.getFunctionMemoryAccess().getEndAssignment_3(), "rule__FunctionMemory__EndAssignment_3");
                    put(grammarAccess.getConstantBinarySectionAccess().getDataAssignment_2_0(), "rule__ConstantBinarySection__DataAssignment_2_0");
                    put(grammarAccess.getReferenceBinarySectionAccess().getIncAssignment_1(), "rule__ReferenceBinarySection__IncAssignment_1");
                    put(grammarAccess.getXSizeOfExpressionAccess().getTypeAssignment_2(), "rule__XSizeOfExpression__TypeAssignment_2");
                    put(grammarAccess.getXStructExpressionAccess().getTypeAssignment_0(), "rule__XStructExpression__TypeAssignment_0");
                    put(grammarAccess.getXStructExpressionAccess().getValuesAssignment_2(), "rule__XStructExpression__ValuesAssignment_2");
                    put(grammarAccess.getXStructExpressionAccess().getValuesAssignment_3_1(), "rule__XStructExpression__ValuesAssignment_3_1");
                    put(grammarAccess.getXExpression1Access().getOperatorAssignment_0(), "rule__XExpression1__OperatorAssignment_0");
                    put(grammarAccess.getXExpression1Access().getAAssignment_1(), "rule__XExpression1__AAssignment_1");
                    put(grammarAccess.getXExpression2Access().getAAssignment_0(), "rule__XExpression2__AAssignment_0");
                    put(grammarAccess.getXExpression2Access().getOpAssignment_1_0(), "rule__XExpression2__OpAssignment_1_0");
                    put(grammarAccess.getXExpression2Access().getBAssignment_1_1(), "rule__XExpression2__BAssignment_1_1");
                    put(grammarAccess.getXExpression3Access().getAAssignment_0(), "rule__XExpression3__AAssignment_0");
                    put(grammarAccess.getXExpression3Access().getOpAssignment_1_0(), "rule__XExpression3__OpAssignment_1_0");
                    put(grammarAccess.getXExpression3Access().getBAssignment_1_1(), "rule__XExpression3__BAssignment_1_1");
                    put(grammarAccess.getXExpression4Access().getAAssignment_0(), "rule__XExpression4__AAssignment_0");
                    put(grammarAccess.getXExpression4Access().getOpAssignment_1_0(), "rule__XExpression4__OpAssignment_1_0");
                    put(grammarAccess.getXExpression4Access().getBAssignment_1_1(), "rule__XExpression4__BAssignment_1_1");
                    put(grammarAccess.getXExpression5Access().getAAssignment_0(), "rule__XExpression5__AAssignment_0");
                    put(grammarAccess.getXExpression5Access().getOpAssignment_1_0(), "rule__XExpression5__OpAssignment_1_0");
                    put(grammarAccess.getXExpression5Access().getBAssignment_1_1(), "rule__XExpression5__BAssignment_1_1");
                    put(grammarAccess.getXExpressionLiteralAccess().getValueAssignment(), "rule__XExpressionLiteral__ValueAssignment");
                    put(grammarAccess.getXAssignmentAccess().getRefAssignment_0(), "rule__XAssignment__RefAssignment_0");
                    put(grammarAccess.getXAssignmentAccess().getValueAssignment_2(), "rule__XAssignment__ValueAssignment_2");
                    put(grammarAccess.getXAddressOfVarAccess().getRefAssignment_2(), "rule__XAddressOfVar__RefAssignment_2");
                    put(grammarAccess.getXIsLiteralExpressionAccess().getRefAssignment_2(), "rule__XIsLiteralExpression__RefAssignment_2");
                    put(grammarAccess.getXIfExpressionAccess().getIfAssignment_2(), "rule__XIfExpression__IfAssignment_2");
                    put(grammarAccess.getXIfExpressionAccess().getThenAssignment_5(), "rule__XIfExpression__ThenAssignment_5");
                    put(grammarAccess.getXIfExpressionAccess().getElseAssignment_7_2(), "rule__XIfExpression__ElseAssignment_7_2");
                    put(grammarAccess.getXParenthesizedExpressionAccess().getAAssignment_1(), "rule__XParenthesizedExpression__AAssignment_1");
                }
            };
        }
        return nameMappings.get(element);
    }

    @Override
    protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
        try {
            hu.e.parser.ui.contentassist.antlr.internal.InternalESyntaxParser typedParser = (hu.e.parser.ui.contentassist.antlr.internal.InternalESyntaxParser) parser;
            typedParser.entryRulePackage();
            return typedParser.getFollowElements();
        } catch (RecognitionException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected String[] getInitialHiddenTokens() {
        return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
    }

    public ESyntaxGrammarAccess getGrammarAccess() {
        return this.grammarAccess;
    }

    public void setGrammarAccess(ESyntaxGrammarAccess grammarAccess) {
        this.grammarAccess = grammarAccess;
    }
}

package com.google.clearsilver.jsilver.syntax.analysis;

import java.util.*;
import com.google.clearsilver.jsilver.syntax.node.*;

public class AnalysisAdapter implements Analysis {

    private Hashtable<Node, Object> in;

    private Hashtable<Node, Object> out;

    public Object getIn(Node node) {
        if (this.in == null) {
            return null;
        }
        return this.in.get(node);
    }

    public void setIn(Node node, Object o) {
        if (this.in == null) {
            this.in = new Hashtable<Node, Object>(1);
        }
        if (o != null) {
            this.in.put(node, o);
        } else {
            this.in.remove(node);
        }
    }

    public Object getOut(Node node) {
        if (this.out == null) {
            return null;
        }
        return this.out.get(node);
    }

    public void setOut(Node node, Object o) {
        if (this.out == null) {
            this.out = new Hashtable<Node, Object>(1);
        }
        if (o != null) {
            this.out.put(node, o);
        } else {
            this.out.remove(node);
        }
    }

    public void caseStart(Start node) {
        defaultCase(node);
    }

    public void caseAMultipleCommand(AMultipleCommand node) {
        defaultCase(node);
    }

    public void caseACommentCommand(ACommentCommand node) {
        defaultCase(node);
    }

    public void caseADataCommand(ADataCommand node) {
        defaultCase(node);
    }

    public void caseAVarCommand(AVarCommand node) {
        defaultCase(node);
    }

    public void caseALvarCommand(ALvarCommand node) {
        defaultCase(node);
    }

    public void caseAEvarCommand(AEvarCommand node) {
        defaultCase(node);
    }

    public void caseAUvarCommand(AUvarCommand node) {
        defaultCase(node);
    }

    public void caseASetCommand(ASetCommand node) {
        defaultCase(node);
    }

    public void caseANameCommand(ANameCommand node) {
        defaultCase(node);
    }

    public void caseAEscapeCommand(AEscapeCommand node) {
        defaultCase(node);
    }

    public void caseAAutoescapeCommand(AAutoescapeCommand node) {
        defaultCase(node);
    }

    public void caseAWithCommand(AWithCommand node) {
        defaultCase(node);
    }

    public void caseALoopToCommand(ALoopToCommand node) {
        defaultCase(node);
    }

    public void caseALoopCommand(ALoopCommand node) {
        defaultCase(node);
    }

    public void caseALoopIncCommand(ALoopIncCommand node) {
        defaultCase(node);
    }

    public void caseAEachCommand(AEachCommand node) {
        defaultCase(node);
    }

    public void caseADefCommand(ADefCommand node) {
        defaultCase(node);
    }

    public void caseACallCommand(ACallCommand node) {
        defaultCase(node);
    }

    public void caseAIfCommand(AIfCommand node) {
        defaultCase(node);
    }

    public void caseAAltCommand(AAltCommand node) {
        defaultCase(node);
    }

    public void caseAIncludeCommand(AIncludeCommand node) {
        defaultCase(node);
    }

    public void caseAHardIncludeCommand(AHardIncludeCommand node) {
        defaultCase(node);
    }

    public void caseALincludeCommand(ALincludeCommand node) {
        defaultCase(node);
    }

    public void caseAHardLincludeCommand(AHardLincludeCommand node) {
        defaultCase(node);
    }

    public void caseAContentTypeCommand(AContentTypeCommand node) {
        defaultCase(node);
    }

    public void caseAInlineCommand(AInlineCommand node) {
        defaultCase(node);
    }

    public void caseANoopCommand(ANoopCommand node) {
        defaultCase(node);
    }

    public void caseACsOpenPosition(ACsOpenPosition node) {
        defaultCase(node);
    }

    public void caseAStringExpression(AStringExpression node) {
        defaultCase(node);
    }

    public void caseANumericExpression(ANumericExpression node) {
        defaultCase(node);
    }

    public void caseADecimalExpression(ADecimalExpression node) {
        defaultCase(node);
    }

    public void caseAHexExpression(AHexExpression node) {
        defaultCase(node);
    }

    public void caseAVariableExpression(AVariableExpression node) {
        defaultCase(node);
    }

    public void caseAFunctionExpression(AFunctionExpression node) {
        defaultCase(node);
    }

    public void caseASequenceExpression(ASequenceExpression node) {
        defaultCase(node);
    }

    public void caseANegativeExpression(ANegativeExpression node) {
        defaultCase(node);
    }

    public void caseANotExpression(ANotExpression node) {
        defaultCase(node);
    }

    public void caseAExistsExpression(AExistsExpression node) {
        defaultCase(node);
    }

    public void caseACommaExpression(ACommaExpression node) {
        defaultCase(node);
    }

    public void caseAEqExpression(AEqExpression node) {
        defaultCase(node);
    }

    public void caseANumericEqExpression(ANumericEqExpression node) {
        defaultCase(node);
    }

    public void caseANeExpression(ANeExpression node) {
        defaultCase(node);
    }

    public void caseANumericNeExpression(ANumericNeExpression node) {
        defaultCase(node);
    }

    public void caseALtExpression(ALtExpression node) {
        defaultCase(node);
    }

    public void caseAGtExpression(AGtExpression node) {
        defaultCase(node);
    }

    public void caseALteExpression(ALteExpression node) {
        defaultCase(node);
    }

    public void caseAGteExpression(AGteExpression node) {
        defaultCase(node);
    }

    public void caseAAndExpression(AAndExpression node) {
        defaultCase(node);
    }

    public void caseAOrExpression(AOrExpression node) {
        defaultCase(node);
    }

    public void caseAAddExpression(AAddExpression node) {
        defaultCase(node);
    }

    public void caseANumericAddExpression(ANumericAddExpression node) {
        defaultCase(node);
    }

    public void caseASubtractExpression(ASubtractExpression node) {
        defaultCase(node);
    }

    public void caseAMultiplyExpression(AMultiplyExpression node) {
        defaultCase(node);
    }

    public void caseADivideExpression(ADivideExpression node) {
        defaultCase(node);
    }

    public void caseAModuloExpression(AModuloExpression node) {
        defaultCase(node);
    }

    public void caseANoopExpression(ANoopExpression node) {
        defaultCase(node);
    }

    public void caseANameVariable(ANameVariable node) {
        defaultCase(node);
    }

    public void caseADecNumberVariable(ADecNumberVariable node) {
        defaultCase(node);
    }

    public void caseAHexNumberVariable(AHexNumberVariable node) {
        defaultCase(node);
    }

    public void caseADescendVariable(ADescendVariable node) {
        defaultCase(node);
    }

    public void caseAExpandVariable(AExpandVariable node) {
        defaultCase(node);
    }

    public void caseTData(TData node) {
        defaultCase(node);
    }

    public void caseTComment(TComment node) {
        defaultCase(node);
    }

    public void caseTVar(TVar node) {
        defaultCase(node);
    }

    public void caseTLvar(TLvar node) {
        defaultCase(node);
    }

    public void caseTEvar(TEvar node) {
        defaultCase(node);
    }

    public void caseTUvar(TUvar node) {
        defaultCase(node);
    }

    public void caseTSet(TSet node) {
        defaultCase(node);
    }

    public void caseTIf(TIf node) {
        defaultCase(node);
    }

    public void caseTElseIf(TElseIf node) {
        defaultCase(node);
    }

    public void caseTElse(TElse node) {
        defaultCase(node);
    }

    public void caseTWith(TWith node) {
        defaultCase(node);
    }

    public void caseTEscape(TEscape node) {
        defaultCase(node);
    }

    public void caseTAutoescape(TAutoescape node) {
        defaultCase(node);
    }

    public void caseTLoop(TLoop node) {
        defaultCase(node);
    }

    public void caseTEach(TEach node) {
        defaultCase(node);
    }

    public void caseTAlt(TAlt node) {
        defaultCase(node);
    }

    public void caseTName(TName node) {
        defaultCase(node);
    }

    public void caseTDef(TDef node) {
        defaultCase(node);
    }

    public void caseTCall(TCall node) {
        defaultCase(node);
    }

    public void caseTInclude(TInclude node) {
        defaultCase(node);
    }

    public void caseTLinclude(TLinclude node) {
        defaultCase(node);
    }

    public void caseTContentType(TContentType node) {
        defaultCase(node);
    }

    public void caseTInline(TInline node) {
        defaultCase(node);
    }

    public void caseTComma(TComma node) {
        defaultCase(node);
    }

    public void caseTBang(TBang node) {
        defaultCase(node);
    }

    public void caseTAssignment(TAssignment node) {
        defaultCase(node);
    }

    public void caseTEq(TEq node) {
        defaultCase(node);
    }

    public void caseTNe(TNe node) {
        defaultCase(node);
    }

    public void caseTLt(TLt node) {
        defaultCase(node);
    }

    public void caseTGt(TGt node) {
        defaultCase(node);
    }

    public void caseTLte(TLte node) {
        defaultCase(node);
    }

    public void caseTGte(TGte node) {
        defaultCase(node);
    }

    public void caseTAnd(TAnd node) {
        defaultCase(node);
    }

    public void caseTOr(TOr node) {
        defaultCase(node);
    }

    public void caseTString(TString node) {
        defaultCase(node);
    }

    public void caseTHash(THash node) {
        defaultCase(node);
    }

    public void caseTPlus(TPlus node) {
        defaultCase(node);
    }

    public void caseTMinus(TMinus node) {
        defaultCase(node);
    }

    public void caseTStar(TStar node) {
        defaultCase(node);
    }

    public void caseTPercent(TPercent node) {
        defaultCase(node);
    }

    public void caseTBracketOpen(TBracketOpen node) {
        defaultCase(node);
    }

    public void caseTBracketClose(TBracketClose node) {
        defaultCase(node);
    }

    public void caseTParenOpen(TParenOpen node) {
        defaultCase(node);
    }

    public void caseTParenClose(TParenClose node) {
        defaultCase(node);
    }

    public void caseTDot(TDot node) {
        defaultCase(node);
    }

    public void caseTDollar(TDollar node) {
        defaultCase(node);
    }

    public void caseTQuestion(TQuestion node) {
        defaultCase(node);
    }

    public void caseTDecNumber(TDecNumber node) {
        defaultCase(node);
    }

    public void caseTHexNumber(THexNumber node) {
        defaultCase(node);
    }

    public void caseTWord(TWord node) {
        defaultCase(node);
    }

    public void caseTArgWhitespace(TArgWhitespace node) {
        defaultCase(node);
    }

    public void caseTSlash(TSlash node) {
        defaultCase(node);
    }

    public void caseTCsOpen(TCsOpen node) {
        defaultCase(node);
    }

    public void caseTCommentStart(TCommentStart node) {
        defaultCase(node);
    }

    public void caseTCommandDelimiter(TCommandDelimiter node) {
        defaultCase(node);
    }

    public void caseTHardDelimiter(THardDelimiter node) {
        defaultCase(node);
    }

    public void caseTCsClose(TCsClose node) {
        defaultCase(node);
    }

    public void caseEOF(EOF node) {
        defaultCase(node);
    }

    public void defaultCase(@SuppressWarnings("unused") Node node) {
    }
}

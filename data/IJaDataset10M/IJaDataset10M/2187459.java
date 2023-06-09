package net.sf.saxon.instruct;

import net.sf.saxon.expr.*;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.om.ValueRepresentation;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.trace.ExpressionPresenter;
import java.util.List;

/**
* An instruction derived from a xsl:with-param element in the stylesheet. <br>
*/
public class WithParam extends GeneralVariable {

    int parameterId;

    public WithParam() {
    }

    /**
     * Allocate a number which is essentially an alias for the parameter name,
     * unique within a stylesheet
     * @param id the parameter id
     */
    public void setParameterId(int id) {
        parameterId = id;
    }

    /**
     * Get the parameter id, which is essentially an alias for the parameter name,
     * unique within a stylesheet
     * @return the parameter id
     */
    public int getParameterId() {
        return parameterId;
    }

    public int getInstructionNameCode() {
        return StandardNames.XSL_WITH_PARAM;
    }

    public TailCall processLeavingTail(XPathContext context) throws XPathException {
        return null;
    }

    public static void simplify(WithParam[] params, ExpressionVisitor visitor) throws XPathException {
        for (int i = 0; i < params.length; i++) {
            Expression select = params[i].getSelectExpression();
            if (select != null) {
                params[i].setSelectExpression(visitor.simplify(select));
            }
        }
    }

    public static void typeCheck(WithParam[] params, ExpressionVisitor visitor, ItemType contextItemType) throws XPathException {
        for (int i = 0; i < params.length; i++) {
            Expression select = params[i].getSelectExpression();
            if (select != null) {
                params[i].setSelectExpression(visitor.typeCheck(select, contextItemType));
            }
        }
    }

    public static void optimize(ExpressionVisitor visitor, WithParam[] params, ItemType contextItemType) throws XPathException {
        for (int i = 0; i < params.length; i++) {
            visitor.optimize(params[i], contextItemType);
        }
    }

    /**
     * Promote the expressions in a set of with-param elements. This is a convenience
     * method for use by subclasses.
     */
    public static void promoteParams(WithParam[] params, PromotionOffer offer) throws XPathException {
        for (int i = 0; i < params.length; i++) {
            Expression select = params[i].getSelectExpression();
            if (select != null) {
                params[i].setSelectExpression(select.promote(offer));
            }
        }
    }

    /**
     * Get the XPath expressions used in an array of WithParam parameters (add them to the supplied list)
     */
    public static void getXPathExpressions(WithParam[] params, List list) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                list.add(params[i]);
            }
        }
    }

    /**
     * DIsplay the parameter expressions
     */
    public static void displayExpressions(WithParam[] params, ExpressionPresenter out) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                out.startElement("withParam");
                out.emitAttribute("name", params[i].getVariableQName().getDisplayName());
                params[i].getSelectExpression().explain(out);
                out.endElement();
            }
        }
    }

    /**
     * Replace a subexpression
     */
    public static boolean replaceXPathExpression(WithParam[] params, Expression original, Expression replacement) {
        boolean found = false;
        for (int i = 0; i < params.length; i++) {
            boolean f = params[i].replaceSubExpression(original, replacement);
            found |= f;
        }
        return found;
    }

    /**
     * Evaluate the variable (method exists only to satisfy the interface)
     */
    public ValueRepresentation evaluateVariable(XPathContext context) throws XPathException {
        throw new UnsupportedOperationException();
    }
}

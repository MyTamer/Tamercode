package com.google.gwt.dev.jjs.impl;

import com.google.gwt.dev.jjs.InternalCompilerException;
import com.google.gwt.dev.jjs.ast.Context;
import com.google.gwt.dev.jjs.ast.JAbsentArrayDimension;
import com.google.gwt.dev.jjs.ast.JArrayLength;
import com.google.gwt.dev.jjs.ast.JArrayRef;
import com.google.gwt.dev.jjs.ast.JBinaryOperation;
import com.google.gwt.dev.jjs.ast.JBooleanLiteral;
import com.google.gwt.dev.jjs.ast.JCastOperation;
import com.google.gwt.dev.jjs.ast.JCharLiteral;
import com.google.gwt.dev.jjs.ast.JClassLiteral;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JConditional;
import com.google.gwt.dev.jjs.ast.JDoubleLiteral;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JFieldRef;
import com.google.gwt.dev.jjs.ast.JFloatLiteral;
import com.google.gwt.dev.jjs.ast.JGwtCreate;
import com.google.gwt.dev.jjs.ast.JInstanceOf;
import com.google.gwt.dev.jjs.ast.JIntLiteral;
import com.google.gwt.dev.jjs.ast.JLocalRef;
import com.google.gwt.dev.jjs.ast.JLongLiteral;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JNameOf;
import com.google.gwt.dev.jjs.ast.JNewArray;
import com.google.gwt.dev.jjs.ast.JNewInstance;
import com.google.gwt.dev.jjs.ast.JNullLiteral;
import com.google.gwt.dev.jjs.ast.JNumericEntry;
import com.google.gwt.dev.jjs.ast.JParameterRef;
import com.google.gwt.dev.jjs.ast.JPostfixOperation;
import com.google.gwt.dev.jjs.ast.JPrefixOperation;
import com.google.gwt.dev.jjs.ast.JRunAsync;
import com.google.gwt.dev.jjs.ast.JStringLiteral;
import com.google.gwt.dev.jjs.ast.JThisRef;
import com.google.gwt.dev.jjs.ast.JVisitor;
import com.google.gwt.dev.jjs.ast.js.JMultiExpression;
import com.google.gwt.dev.jjs.ast.js.JsniFieldRef;
import com.google.gwt.dev.jjs.ast.js.JsniMethodRef;
import java.util.ArrayList;
import java.util.List;

/**
 * A general purpose expression cloner.
 */
public class CloneExpressionVisitor extends JVisitor {

    private JExpression expression;

    public CloneExpressionVisitor() {
    }

    @SuppressWarnings("unchecked")
    public <T extends JExpression> T cloneExpression(T expr) {
        if (expr == null) {
            return null;
        }
        expression = null;
        this.accept(expr);
        if (expression == null) {
            throw new InternalCompilerException(expr, "Unable to clone expression", null);
        }
        Class<T> originalClass = (Class<T>) expr.getClass();
        return originalClass.cast(expression);
    }

    public ArrayList<JExpression> cloneExpressions(List<JExpression> exprs) {
        if (exprs == null) {
            return null;
        }
        ArrayList<JExpression> result = new ArrayList<JExpression>();
        for (JExpression expr : exprs) {
            result.add(cloneExpression(expr));
        }
        return result;
    }

    @Override
    public boolean visit(JAbsentArrayDimension x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JArrayLength x, Context ctx) {
        expression = new JArrayLength(x.getSourceInfo(), cloneExpression(x.getInstance()));
        return false;
    }

    @Override
    public boolean visit(JArrayRef x, Context ctx) {
        expression = new JArrayRef(x.getSourceInfo(), cloneExpression(x.getInstance()), cloneExpression(x.getIndexExpr()));
        return false;
    }

    @Override
    public boolean visit(JBinaryOperation x, Context ctx) {
        expression = new JBinaryOperation(x.getSourceInfo(), x.getType(), x.getOp(), cloneExpression(x.getLhs()), cloneExpression(x.getRhs()));
        return false;
    }

    @Override
    public boolean visit(JBooleanLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JCastOperation x, Context ctx) {
        expression = new JCastOperation(x.getSourceInfo(), x.getCastType(), cloneExpression(x.getExpr()));
        return false;
    }

    @Override
    public boolean visit(JCharLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JClassLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JConditional x, Context ctx) {
        expression = new JConditional(x.getSourceInfo(), x.getType(), cloneExpression(x.getIfTest()), cloneExpression(x.getThenExpr()), cloneExpression(x.getElseExpr()));
        return false;
    }

    @Override
    public boolean visit(JDoubleLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JFieldRef x, Context ctx) {
        expression = new JFieldRef(x.getSourceInfo(), cloneExpression(x.getInstance()), x.getField(), x.getEnclosingType());
        return false;
    }

    @Override
    public boolean visit(JFloatLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JGwtCreate x, Context ctx) {
        ArrayList<JExpression> clonedExprs = new ArrayList<JExpression>();
        for (JExpression expr : x.getInstantiationExpressions()) {
            clonedExprs.add(cloneExpression(expr));
        }
        JGwtCreate gwtCreate = new JGwtCreate(x.getSourceInfo(), x.getSourceType(), x.getResultTypes(), x.getType(), cloneExpressions(x.getInstantiationExpressions()));
        expression = gwtCreate;
        return false;
    }

    @Override
    public boolean visit(JInstanceOf x, Context ctx) {
        expression = new JInstanceOf(x.getSourceInfo(), x.getTestType(), cloneExpression(x.getExpr()));
        return false;
    }

    @Override
    public boolean visit(JIntLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JLocalRef x, Context ctx) {
        expression = new JLocalRef(x.getSourceInfo(), x.getLocal());
        return false;
    }

    @Override
    public boolean visit(JLongLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JMethodCall x, Context ctx) {
        JMethodCall newMethodCall = new JMethodCall(x, cloneExpression(x.getInstance()));
        newMethodCall.addArgs(cloneExpressions(x.getArgs()));
        expression = newMethodCall;
        return false;
    }

    @Override
    public boolean visit(JRunAsync x, Context ctx) {
        JExpression runAsyncCall = cloneExpression(x.getRunAsyncCall());
        expression = new JRunAsync(x.getSourceInfo(), x.getSplitPoint(), x.getName(), runAsyncCall, x.getOnSuccessCall());
        return false;
    }

    @Override
    public boolean visit(JMultiExpression x, Context ctx) {
        JMultiExpression multi = new JMultiExpression(x.getSourceInfo());
        multi.exprs.addAll(cloneExpressions(x.exprs));
        expression = multi;
        return false;
    }

    @Override
    public boolean visit(JNameOf x, Context ctx) {
        expression = new JNameOf(x.getSourceInfo(), (JClassType) x.getType().getUnderlyingType(), x.getNode());
        return false;
    }

    @Override
    public boolean visit(JNewArray x, Context ctx) {
        expression = new JNewArray(x.getSourceInfo(), x.getArrayType(), cloneExpressions(x.dims), cloneExpressions(x.initializers), x.getClassLiterals());
        return false;
    }

    @Override
    public boolean visit(JNewInstance x, Context ctx) {
        JNewInstance newInstance = new JNewInstance(x);
        newInstance.addArgs(cloneExpressions(x.getArgs()));
        expression = newInstance;
        return false;
    }

    @Override
    public boolean visit(JNumericEntry x, Context ctx) {
        expression = new JNumericEntry(x.getSourceInfo(), x.getKey(), x.getValue());
        return false;
    }

    @Override
    public boolean visit(JNullLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JParameterRef x, Context ctx) {
        expression = new JParameterRef(x.getSourceInfo(), x.getParameter());
        return false;
    }

    @Override
    public boolean visit(JPostfixOperation x, Context ctx) {
        expression = new JPostfixOperation(x.getSourceInfo(), x.getOp(), cloneExpression(x.getArg()));
        return false;
    }

    @Override
    public boolean visit(JPrefixOperation x, Context ctx) {
        expression = new JPrefixOperation(x.getSourceInfo(), x.getOp(), cloneExpression(x.getArg()));
        return false;
    }

    @Override
    public boolean visit(JsniFieldRef x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JsniMethodRef x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JStringLiteral x, Context ctx) {
        expression = x;
        return false;
    }

    @Override
    public boolean visit(JThisRef x, Context ctx) {
        expression = new JThisRef(x.getSourceInfo(), x.getClassType());
        return false;
    }
}

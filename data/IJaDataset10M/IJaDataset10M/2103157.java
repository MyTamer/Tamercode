package com.eteks.parser.node;

import com.eteks.parser.Interpreter;

/**
public class ConditionNode implements ParameterizedNode {

    private ExpressionNode ifExpression;

    private ExpressionNode thenExpression;

    private ExpressionNode elseExpression;

    /**
    public void addParameter(ExpressionNode parameter) {
        if (ifExpression == null) ifExpression = parameter; else if (thenExpression == null) thenExpression = parameter; else elseExpression = parameter;
    }

    public int getParameterCount() {
        return ifExpression != null ? (thenExpression != null ? (elseExpression != null ? 3 : 2) : 1) : 0;
    }

    /**
    public ExpressionNode getIfExpression() {
        return ifExpression;
    }

    /**
    public ExpressionNode getThenExpression() {
        return thenExpression;
    }

    /**
    public ExpressionNode getElseExpression() {
        return elseExpression;
    }

    /**
    public Object computeExpression(Interpreter interpreter, Object[] parametersValue) {
        if (interpreter.supportsRecursiveCall()) return interpreter.isTrue(ifExpression.computeExpression(interpreter, parametersValue)) ? thenExpression.computeExpression(interpreter, parametersValue) : elseExpression.computeExpression(interpreter, parametersValue); else return interpreter.getConditionValue(ifExpression.computeExpression(interpreter, parametersValue), thenExpression.computeExpression(interpreter, parametersValue), elseExpression.computeExpression(interpreter, parametersValue));
    }

    /**
    public double computeExpression(double[] parametersValue) {
        return ifExpression.computeExpression(parametersValue) != ConstantNode.FALSE_DOUBLE ? thenExpression.computeExpression(parametersValue) : elseExpression.computeExpression(parametersValue);
    }
}
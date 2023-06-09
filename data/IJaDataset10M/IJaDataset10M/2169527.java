package com.ibm.wala.fixedpoint.impl;

import com.ibm.wala.fixpoint.IFixedPointStatement;
import com.ibm.wala.fixpoint.IVariable;
import com.ibm.wala.util.graph.impl.NodeWithNumber;

/**
 * Represents a single step in an iterative solver
 * 
 * @author Stephen Fink
 * @author Julian Dolby
 */
public abstract class AbstractStatement<T extends IVariable, O extends AbstractOperator<T>> extends NodeWithNumber implements IFixedPointStatement<T> {

    public abstract O getOperator();

    /**
   * Subclasses must implement this, to prevent non-determinism.
   */
    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object o);

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("");
        if (getLHS() == null) {
            result.append("null ");
        } else {
            result.append(getLHS().toString());
            result.append(" ");
        }
        result.append(getOperator().toString());
        result.append(" ");
        for (int i = 0; i < getRHS().length; i++) {
            if (getRHS()[i] == null) {
                result.append("null");
            } else {
                result.append(getRHS()[i].toString());
            }
            result.append(" ");
        }
        return result.toString();
    }

    public final int getOrderNumber() {
        IVariable lhs = getLHS();
        return (lhs == null) ? 0 : lhs.getOrderNumber();
    }
}

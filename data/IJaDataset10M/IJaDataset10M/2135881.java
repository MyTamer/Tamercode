package com.google.clearsilver.jsilver.syntax.node;

import com.google.clearsilver.jsilver.syntax.analysis.*;

@SuppressWarnings("nls")
public final class ASubtractExpression extends PExpression {

    private PExpression _left_;

    private PExpression _right_;

    public ASubtractExpression() {
    }

    public ASubtractExpression(@SuppressWarnings("hiding") PExpression _left_, @SuppressWarnings("hiding") PExpression _right_) {
        setLeft(_left_);
        setRight(_right_);
    }

    @Override
    public Object clone() {
        return new ASubtractExpression(cloneNode(this._left_), cloneNode(this._right_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASubtractExpression(this);
    }

    public PExpression getLeft() {
        return this._left_;
    }

    public void setLeft(PExpression node) {
        if (this._left_ != null) {
            this._left_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._left_ = node;
    }

    public PExpression getRight() {
        return this._right_;
    }

    public void setRight(PExpression node) {
        if (this._right_ != null) {
            this._right_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._right_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._left_) + toString(this._right_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._left_ == child) {
            this._left_ = null;
            return;
        }
        if (this._right_ == child) {
            this._right_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._left_ == oldChild) {
            setLeft((PExpression) newChild);
            return;
        }
        if (this._right_ == oldChild) {
            setRight((PExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

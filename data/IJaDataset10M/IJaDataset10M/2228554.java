package org.sableccsupport.scclexer.node;

import org.sableccsupport.scclexer.analysis.*;

@SuppressWarnings("nls")
public final class ADecChar extends PChar {

    private TDecChar _decChar_;

    public ADecChar() {
    }

    public ADecChar(@SuppressWarnings("hiding") TDecChar _decChar_) {
        setDecChar(_decChar_);
    }

    @Override
    public Object clone() {
        return new ADecChar(cloneNode(this._decChar_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseADecChar(this);
    }

    public TDecChar getDecChar() {
        return this._decChar_;
    }

    public void setDecChar(TDecChar node) {
        if (this._decChar_ != null) {
            this._decChar_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._decChar_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._decChar_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._decChar_ == child) {
            this._decChar_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._decChar_ == oldChild) {
            setDecChar((TDecChar) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

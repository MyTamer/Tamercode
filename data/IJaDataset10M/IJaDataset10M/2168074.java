package net.sourceforge.texlipse.bibparser.node;

import net.sourceforge.texlipse.bibparser.analysis.*;

@SuppressWarnings("nls")
public final class ANumValOrSid extends PValOrSid {

    private TNumber _number_;

    public ANumValOrSid() {
    }

    public ANumValOrSid(@SuppressWarnings("hiding") TNumber _number_) {
        setNumber(_number_);
    }

    @Override
    public Object clone() {
        return new ANumValOrSid(cloneNode(this._number_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseANumValOrSid(this);
    }

    public TNumber getNumber() {
        return this._number_;
    }

    public void setNumber(TNumber node) {
        if (this._number_ != null) {
            this._number_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._number_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._number_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._number_ == child) {
            this._number_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._number_ == oldChild) {
            setNumber((TNumber) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

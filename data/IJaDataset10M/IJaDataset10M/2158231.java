package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AVolatileModifier extends PModifier {

    private TVolatile _volatile_;

    public AVolatileModifier() {
    }

    public AVolatileModifier(@SuppressWarnings("hiding") TVolatile _volatile_) {
        setVolatile(_volatile_);
    }

    @Override
    public Object clone() {
        return new AVolatileModifier(cloneNode(this._volatile_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVolatileModifier(this);
    }

    public TVolatile getVolatile() {
        return this._volatile_;
    }

    public void setVolatile(TVolatile node) {
        if (this._volatile_ != null) {
            this._volatile_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._volatile_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._volatile_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._volatile_ == child) {
            this._volatile_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._volatile_ == oldChild) {
            setVolatile((TVolatile) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

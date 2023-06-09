package ru.amse.baltijsky.javascheme.importer.sablecc.java15.node;

import ru.amse.baltijsky.javascheme.importer.sablecc.java15.analysis.Analysis;

@SuppressWarnings("nls")
public final class AProtectedModifier extends PModifier {

    private TProtected _protected_;

    public AProtectedModifier() {
    }

    public AProtectedModifier(@SuppressWarnings("hiding") TProtected _protected_) {
        setProtected(_protected_);
    }

    @Override
    public Object clone() {
        return new AProtectedModifier(cloneNode(this._protected_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAProtectedModifier(this);
    }

    public TProtected getProtected() {
        return this._protected_;
    }

    public void setProtected(TProtected node) {
        if (this._protected_ != null) {
            this._protected_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._protected_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._protected_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._protected_ == child) {
            this._protected_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._protected_ == oldChild) {
            setProtected((TProtected) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

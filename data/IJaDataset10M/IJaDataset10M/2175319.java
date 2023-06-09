package org.sableccsupport.sccparser.node;

import org.sableccsupport.sccparser.analysis.*;

@SuppressWarnings("nls")
public final class ASimpleTerm extends PTerm {

    private PSpecifier _specifier_;

    private TId _id_;

    private PSimpleTermTail _simpleTermTail_;

    public ASimpleTerm() {
    }

    public ASimpleTerm(@SuppressWarnings("hiding") PSpecifier _specifier_, @SuppressWarnings("hiding") TId _id_, @SuppressWarnings("hiding") PSimpleTermTail _simpleTermTail_) {
        setSpecifier(_specifier_);
        setId(_id_);
        setSimpleTermTail(_simpleTermTail_);
    }

    @Override
    public Object clone() {
        return new ASimpleTerm(cloneNode(this._specifier_), cloneNode(this._id_), cloneNode(this._simpleTermTail_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleTerm(this);
    }

    public PSpecifier getSpecifier() {
        return this._specifier_;
    }

    public void setSpecifier(PSpecifier node) {
        if (this._specifier_ != null) {
            this._specifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._specifier_ = node;
    }

    public TId getId() {
        return this._id_;
    }

    public void setId(TId node) {
        if (this._id_ != null) {
            this._id_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._id_ = node;
    }

    public PSimpleTermTail getSimpleTermTail() {
        return this._simpleTermTail_;
    }

    public void setSimpleTermTail(PSimpleTermTail node) {
        if (this._simpleTermTail_ != null) {
            this._simpleTermTail_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._simpleTermTail_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._specifier_) + toString(this._id_) + toString(this._simpleTermTail_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._specifier_ == child) {
            this._specifier_ = null;
            return;
        }
        if (this._id_ == child) {
            this._id_ = null;
            return;
        }
        if (this._simpleTermTail_ == child) {
            this._simpleTermTail_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._specifier_ == oldChild) {
            setSpecifier((PSpecifier) newChild);
            return;
        }
        if (this._id_ == oldChild) {
            setId((TId) newChild);
            return;
        }
        if (this._simpleTermTail_ == oldChild) {
            setSimpleTermTail((PSimpleTermTail) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

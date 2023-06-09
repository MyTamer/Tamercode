package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ASlashIdIdMultiplicativeExpression extends PMultiplicativeExpression {

    private TIdentifier _identifier1_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers1_ = new LinkedList<PAdditionalIdentifier>();

    private TSlash _slash_;

    private TIdentifier _identifier2_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers2_ = new LinkedList<PAdditionalIdentifier>();

    public ASlashIdIdMultiplicativeExpression() {
    }

    public ASlashIdIdMultiplicativeExpression(@SuppressWarnings("hiding") TIdentifier _identifier1_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers1_, @SuppressWarnings("hiding") TSlash _slash_, @SuppressWarnings("hiding") TIdentifier _identifier2_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers2_) {
        setIdentifier1(_identifier1_);
        setAdditionalIdentifiers1(_additionalIdentifiers1_);
        setSlash(_slash_);
        setIdentifier2(_identifier2_);
        setAdditionalIdentifiers2(_additionalIdentifiers2_);
    }

    @Override
    public Object clone() {
        return new ASlashIdIdMultiplicativeExpression(cloneNode(this._identifier1_), cloneList(this._additionalIdentifiers1_), cloneNode(this._slash_), cloneNode(this._identifier2_), cloneList(this._additionalIdentifiers2_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASlashIdIdMultiplicativeExpression(this);
    }

    public TIdentifier getIdentifier1() {
        return this._identifier1_;
    }

    public void setIdentifier1(TIdentifier node) {
        if (this._identifier1_ != null) {
            this._identifier1_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier1_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers1() {
        return this._additionalIdentifiers1_;
    }

    public void setAdditionalIdentifiers1(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers1_.clear();
        this._additionalIdentifiers1_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public TSlash getSlash() {
        return this._slash_;
    }

    public void setSlash(TSlash node) {
        if (this._slash_ != null) {
            this._slash_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._slash_ = node;
    }

    public TIdentifier getIdentifier2() {
        return this._identifier2_;
    }

    public void setIdentifier2(TIdentifier node) {
        if (this._identifier2_ != null) {
            this._identifier2_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier2_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers2() {
        return this._additionalIdentifiers2_;
    }

    public void setAdditionalIdentifiers2(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers2_.clear();
        this._additionalIdentifiers2_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier1_) + toString(this._additionalIdentifiers1_) + toString(this._slash_) + toString(this._identifier2_) + toString(this._additionalIdentifiers2_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier1_ == child) {
            this._identifier1_ = null;
            return;
        }
        if (this._additionalIdentifiers1_.remove(child)) {
            return;
        }
        if (this._slash_ == child) {
            this._slash_ = null;
            return;
        }
        if (this._identifier2_ == child) {
            this._identifier2_ = null;
            return;
        }
        if (this._additionalIdentifiers2_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._identifier1_ == oldChild) {
            setIdentifier1((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers1_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._slash_ == oldChild) {
            setSlash((TSlash) newChild);
            return;
        }
        if (this._identifier2_ == oldChild) {
            setIdentifier2((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers2_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PAdditionalIdentifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        throw new RuntimeException("Not a child.");
    }
}

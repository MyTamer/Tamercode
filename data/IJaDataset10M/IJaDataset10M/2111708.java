package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class APrimitiveMethodHeader extends PMethodHeader {

    private final LinkedList<PModifier> _modifiers_ = new LinkedList<PModifier>();

    private PTypeParameters _typeParameters_;

    private PPrimitiveType _primitiveType_;

    private final LinkedList<PDim> _dims_ = new LinkedList<PDim>();

    private PMethodDeclarator _methodDeclarator_;

    private PThrows _throws_;

    public APrimitiveMethodHeader() {
    }

    public APrimitiveMethodHeader(@SuppressWarnings("hiding") List<PModifier> _modifiers_, @SuppressWarnings("hiding") PTypeParameters _typeParameters_, @SuppressWarnings("hiding") PPrimitiveType _primitiveType_, @SuppressWarnings("hiding") List<PDim> _dims_, @SuppressWarnings("hiding") PMethodDeclarator _methodDeclarator_, @SuppressWarnings("hiding") PThrows _throws_) {
        setModifiers(_modifiers_);
        setTypeParameters(_typeParameters_);
        setPrimitiveType(_primitiveType_);
        setDims(_dims_);
        setMethodDeclarator(_methodDeclarator_);
        setThrows(_throws_);
    }

    @Override
    public Object clone() {
        return new APrimitiveMethodHeader(cloneList(this._modifiers_), cloneNode(this._typeParameters_), cloneNode(this._primitiveType_), cloneList(this._dims_), cloneNode(this._methodDeclarator_), cloneNode(this._throws_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAPrimitiveMethodHeader(this);
    }

    public LinkedList<PModifier> getModifiers() {
        return this._modifiers_;
    }

    public void setModifiers(List<PModifier> list) {
        this._modifiers_.clear();
        this._modifiers_.addAll(list);
        for (PModifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public PTypeParameters getTypeParameters() {
        return this._typeParameters_;
    }

    public void setTypeParameters(PTypeParameters node) {
        if (this._typeParameters_ != null) {
            this._typeParameters_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeParameters_ = node;
    }

    public PPrimitiveType getPrimitiveType() {
        return this._primitiveType_;
    }

    public void setPrimitiveType(PPrimitiveType node) {
        if (this._primitiveType_ != null) {
            this._primitiveType_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._primitiveType_ = node;
    }

    public LinkedList<PDim> getDims() {
        return this._dims_;
    }

    public void setDims(List<PDim> list) {
        this._dims_.clear();
        this._dims_.addAll(list);
        for (PDim e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public PMethodDeclarator getMethodDeclarator() {
        return this._methodDeclarator_;
    }

    public void setMethodDeclarator(PMethodDeclarator node) {
        if (this._methodDeclarator_ != null) {
            this._methodDeclarator_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._methodDeclarator_ = node;
    }

    public PThrows getThrows() {
        return this._throws_;
    }

    public void setThrows(PThrows node) {
        if (this._throws_ != null) {
            this._throws_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._throws_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._modifiers_) + toString(this._typeParameters_) + toString(this._primitiveType_) + toString(this._dims_) + toString(this._methodDeclarator_) + toString(this._throws_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._modifiers_.remove(child)) {
            return;
        }
        if (this._typeParameters_ == child) {
            this._typeParameters_ = null;
            return;
        }
        if (this._primitiveType_ == child) {
            this._primitiveType_ = null;
            return;
        }
        if (this._dims_.remove(child)) {
            return;
        }
        if (this._methodDeclarator_ == child) {
            this._methodDeclarator_ = null;
            return;
        }
        if (this._throws_ == child) {
            this._throws_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        for (ListIterator<PModifier> i = this._modifiers_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PModifier) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._typeParameters_ == oldChild) {
            setTypeParameters((PTypeParameters) newChild);
            return;
        }
        if (this._primitiveType_ == oldChild) {
            setPrimitiveType((PPrimitiveType) newChild);
            return;
        }
        for (ListIterator<PDim> i = this._dims_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PDim) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._methodDeclarator_ == oldChild) {
            setMethodDeclarator((PMethodDeclarator) newChild);
            return;
        }
        if (this._throws_ == oldChild) {
            setThrows((PThrows) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

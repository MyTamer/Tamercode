package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AReferenceMethodHeader extends PMethodHeader {

    private final LinkedList<PModifier> _modifiers_ = new LinkedList<PModifier>();

    private PTypeParameters _typeParameters_;

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    private final LinkedList<PTypeComponent> _typeComponents_ = new LinkedList<PTypeComponent>();

    private PTypeArguments _typeArguments_;

    private final LinkedList<PDim> _dims_ = new LinkedList<PDim>();

    private PMethodDeclarator _methodDeclarator_;

    private PThrows _throws_;

    public AReferenceMethodHeader() {
    }

    public AReferenceMethodHeader(@SuppressWarnings("hiding") List<PModifier> _modifiers_, @SuppressWarnings("hiding") PTypeParameters _typeParameters_, @SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_, @SuppressWarnings("hiding") List<PTypeComponent> _typeComponents_, @SuppressWarnings("hiding") PTypeArguments _typeArguments_, @SuppressWarnings("hiding") List<PDim> _dims_, @SuppressWarnings("hiding") PMethodDeclarator _methodDeclarator_, @SuppressWarnings("hiding") PThrows _throws_) {
        setModifiers(_modifiers_);
        setTypeParameters(_typeParameters_);
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
        setTypeComponents(_typeComponents_);
        setTypeArguments(_typeArguments_);
        setDims(_dims_);
        setMethodDeclarator(_methodDeclarator_);
        setThrows(_throws_);
    }

    @Override
    public Object clone() {
        return new AReferenceMethodHeader(cloneList(this._modifiers_), cloneNode(this._typeParameters_), cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_), cloneList(this._typeComponents_), cloneNode(this._typeArguments_), cloneList(this._dims_), cloneNode(this._methodDeclarator_), cloneNode(this._throws_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAReferenceMethodHeader(this);
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

    public TIdentifier getIdentifier() {
        return this._identifier_;
    }

    public void setIdentifier(TIdentifier node) {
        if (this._identifier_ != null) {
            this._identifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._identifier_ = node;
    }

    public LinkedList<PAdditionalIdentifier> getAdditionalIdentifiers() {
        return this._additionalIdentifiers_;
    }

    public void setAdditionalIdentifiers(List<PAdditionalIdentifier> list) {
        this._additionalIdentifiers_.clear();
        this._additionalIdentifiers_.addAll(list);
        for (PAdditionalIdentifier e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public LinkedList<PTypeComponent> getTypeComponents() {
        return this._typeComponents_;
    }

    public void setTypeComponents(List<PTypeComponent> list) {
        this._typeComponents_.clear();
        this._typeComponents_.addAll(list);
        for (PTypeComponent e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    public PTypeArguments getTypeArguments() {
        return this._typeArguments_;
    }

    public void setTypeArguments(PTypeArguments node) {
        if (this._typeArguments_ != null) {
            this._typeArguments_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._typeArguments_ = node;
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
        return "" + toString(this._modifiers_) + toString(this._typeParameters_) + toString(this._identifier_) + toString(this._additionalIdentifiers_) + toString(this._typeComponents_) + toString(this._typeArguments_) + toString(this._dims_) + toString(this._methodDeclarator_) + toString(this._throws_);
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
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        if (this._typeComponents_.remove(child)) {
            return;
        }
        if (this._typeArguments_ == child) {
            this._typeArguments_ = null;
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
        if (this._identifier_ == oldChild) {
            setIdentifier((TIdentifier) newChild);
            return;
        }
        for (ListIterator<PAdditionalIdentifier> i = this._additionalIdentifiers_.listIterator(); i.hasNext(); ) {
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
        for (ListIterator<PTypeComponent> i = this._typeComponents_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PTypeComponent) newChild);
                    newChild.parent(this);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (this._typeArguments_ == oldChild) {
            setTypeArguments((PTypeArguments) newChild);
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

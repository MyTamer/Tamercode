package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AVoidMethodHeader extends PMethodHeader {

    private final LinkedList<PModifier> _modifiers_ = new LinkedList<PModifier>();

    private PTypeParameters _typeParameters_;

    private TVoid _void_;

    private PMethodDeclarator _methodDeclarator_;

    private PThrows _throws_;

    public AVoidMethodHeader() {
    }

    public AVoidMethodHeader(@SuppressWarnings("hiding") List<PModifier> _modifiers_, @SuppressWarnings("hiding") PTypeParameters _typeParameters_, @SuppressWarnings("hiding") TVoid _void_, @SuppressWarnings("hiding") PMethodDeclarator _methodDeclarator_, @SuppressWarnings("hiding") PThrows _throws_) {
        setModifiers(_modifiers_);
        setTypeParameters(_typeParameters_);
        setVoid(_void_);
        setMethodDeclarator(_methodDeclarator_);
        setThrows(_throws_);
    }

    @Override
    public Object clone() {
        return new AVoidMethodHeader(cloneList(this._modifiers_), cloneNode(this._typeParameters_), cloneNode(this._void_), cloneNode(this._methodDeclarator_), cloneNode(this._throws_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAVoidMethodHeader(this);
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

    public TVoid getVoid() {
        return this._void_;
    }

    public void setVoid(TVoid node) {
        if (this._void_ != null) {
            this._void_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._void_ = node;
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
        return "" + toString(this._modifiers_) + toString(this._typeParameters_) + toString(this._void_) + toString(this._methodDeclarator_) + toString(this._throws_);
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
        if (this._void_ == child) {
            this._void_ = null;
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
        if (this._void_ == oldChild) {
            setVoid((TVoid) newChild);
            return;
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

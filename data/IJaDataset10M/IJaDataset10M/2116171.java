package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class AQmakrIdExOrConditionalExpression extends PConditionalExpression {

    private TIdentifier _identifier_;

    private final LinkedList<PAdditionalIdentifier> _additionalIdentifiers_ = new LinkedList<PAdditionalIdentifier>();

    private TQmark _qmark_;

    private PExpression _expression_;

    private TColon _colon_;

    private PConditionalExpression _conditionalExpression_;

    public AQmakrIdExOrConditionalExpression() {
    }

    public AQmakrIdExOrConditionalExpression(@SuppressWarnings("hiding") TIdentifier _identifier_, @SuppressWarnings("hiding") List<PAdditionalIdentifier> _additionalIdentifiers_, @SuppressWarnings("hiding") TQmark _qmark_, @SuppressWarnings("hiding") PExpression _expression_, @SuppressWarnings("hiding") TColon _colon_, @SuppressWarnings("hiding") PConditionalExpression _conditionalExpression_) {
        setIdentifier(_identifier_);
        setAdditionalIdentifiers(_additionalIdentifiers_);
        setQmark(_qmark_);
        setExpression(_expression_);
        setColon(_colon_);
        setConditionalExpression(_conditionalExpression_);
    }

    @Override
    public Object clone() {
        return new AQmakrIdExOrConditionalExpression(cloneNode(this._identifier_), cloneList(this._additionalIdentifiers_), cloneNode(this._qmark_), cloneNode(this._expression_), cloneNode(this._colon_), cloneNode(this._conditionalExpression_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAQmakrIdExOrConditionalExpression(this);
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

    public TQmark getQmark() {
        return this._qmark_;
    }

    public void setQmark(TQmark node) {
        if (this._qmark_ != null) {
            this._qmark_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._qmark_ = node;
    }

    public PExpression getExpression() {
        return this._expression_;
    }

    public void setExpression(PExpression node) {
        if (this._expression_ != null) {
            this._expression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._expression_ = node;
    }

    public TColon getColon() {
        return this._colon_;
    }

    public void setColon(TColon node) {
        if (this._colon_ != null) {
            this._colon_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._colon_ = node;
    }

    public PConditionalExpression getConditionalExpression() {
        return this._conditionalExpression_;
    }

    public void setConditionalExpression(PConditionalExpression node) {
        if (this._conditionalExpression_ != null) {
            this._conditionalExpression_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._conditionalExpression_ = node;
    }

    @Override
    public String toString() {
        return "" + toString(this._identifier_) + toString(this._additionalIdentifiers_) + toString(this._qmark_) + toString(this._expression_) + toString(this._colon_) + toString(this._conditionalExpression_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._identifier_ == child) {
            this._identifier_ = null;
            return;
        }
        if (this._additionalIdentifiers_.remove(child)) {
            return;
        }
        if (this._qmark_ == child) {
            this._qmark_ = null;
            return;
        }
        if (this._expression_ == child) {
            this._expression_ = null;
            return;
        }
        if (this._colon_ == child) {
            this._colon_ = null;
            return;
        }
        if (this._conditionalExpression_ == child) {
            this._conditionalExpression_ = null;
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
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
        if (this._qmark_ == oldChild) {
            setQmark((TQmark) newChild);
            return;
        }
        if (this._expression_ == oldChild) {
            setExpression((PExpression) newChild);
            return;
        }
        if (this._colon_ == oldChild) {
            setColon((TColon) newChild);
            return;
        }
        if (this._conditionalExpression_ == oldChild) {
            setConditionalExpression((PConditionalExpression) newChild);
            return;
        }
        throw new RuntimeException("Not a child.");
    }
}

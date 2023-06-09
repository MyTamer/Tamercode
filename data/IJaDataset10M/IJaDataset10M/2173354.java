package org.sablecc.java.node;

import java.util.*;
import org.sablecc.java.analysis.*;

@SuppressWarnings("nls")
public final class ACatchTryStatement extends PTryStatement {

    private TTry _try_;

    private PBlock _block_;

    private final LinkedList<PCatchClause> _catchClauses_ = new LinkedList<PCatchClause>();

    public ACatchTryStatement() {
    }

    public ACatchTryStatement(@SuppressWarnings("hiding") TTry _try_, @SuppressWarnings("hiding") PBlock _block_, @SuppressWarnings("hiding") List<PCatchClause> _catchClauses_) {
        setTry(_try_);
        setBlock(_block_);
        setCatchClauses(_catchClauses_);
    }

    @Override
    public Object clone() {
        return new ACatchTryStatement(cloneNode(this._try_), cloneNode(this._block_), cloneList(this._catchClauses_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseACatchTryStatement(this);
    }

    public TTry getTry() {
        return this._try_;
    }

    public void setTry(TTry node) {
        if (this._try_ != null) {
            this._try_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._try_ = node;
    }

    public PBlock getBlock() {
        return this._block_;
    }

    public void setBlock(PBlock node) {
        if (this._block_ != null) {
            this._block_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        this._block_ = node;
    }

    public LinkedList<PCatchClause> getCatchClauses() {
        return this._catchClauses_;
    }

    public void setCatchClauses(List<PCatchClause> list) {
        this._catchClauses_.clear();
        this._catchClauses_.addAll(list);
        for (PCatchClause e : list) {
            if (e.parent() != null) {
                e.parent().removeChild(e);
            }
            e.parent(this);
        }
    }

    @Override
    public String toString() {
        return "" + toString(this._try_) + toString(this._block_) + toString(this._catchClauses_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child) {
        if (this._try_ == child) {
            this._try_ = null;
            return;
        }
        if (this._block_ == child) {
            this._block_ = null;
            return;
        }
        if (this._catchClauses_.remove(child)) {
            return;
        }
        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
        if (this._try_ == oldChild) {
            setTry((TTry) newChild);
            return;
        }
        if (this._block_ == oldChild) {
            setBlock((PBlock) newChild);
            return;
        }
        for (ListIterator<PCatchClause> i = this._catchClauses_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set((PCatchClause) newChild);
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

package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AAlt extends PAlt {

    private TId _altName_;

    private final LinkedList _elems_ = new TypedLinkedList(new Elems_Cast());

    private PAltTransform _altTransform_;

    public AAlt() {
    }

    public AAlt(TId _altName_, List _elems_, PAltTransform _altTransform_) {
        setAltName(_altName_);
        {
            this._elems_.clear();
            this._elems_.addAll(_elems_);
        }
        setAltTransform(_altTransform_);
    }

    public Object clone() {
        return new AAlt((TId) cloneNode(_altName_), cloneList(_elems_), (PAltTransform) cloneNode(_altTransform_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAAlt(this);
    }

    public TId getAltName() {
        return _altName_;
    }

    public void setAltName(TId node) {
        if (_altName_ != null) {
            _altName_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _altName_ = node;
    }

    public LinkedList getElems() {
        return _elems_;
    }

    public void setElems(List list) {
        _elems_.clear();
        _elems_.addAll(list);
    }

    public PAltTransform getAltTransform() {
        return _altTransform_;
    }

    public void setAltTransform(PAltTransform node) {
        if (_altTransform_ != null) {
            _altTransform_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _altTransform_ = node;
    }

    public String toString() {
        return "" + toString(_altName_) + toString(_elems_) + toString(_altTransform_);
    }

    void removeChild(Node child) {
        if (_altName_ == child) {
            _altName_ = null;
            return;
        }
        if (_elems_.remove(child)) {
            return;
        }
        if (_altTransform_ == child) {
            _altTransform_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_altName_ == oldChild) {
            setAltName((TId) newChild);
            return;
        }
        for (ListIterator i = _elems_.listIterator(); i.hasNext(); ) {
            if (i.next() == oldChild) {
                if (newChild != null) {
                    i.set(newChild);
                    oldChild.parent(null);
                    return;
                }
                i.remove();
                oldChild.parent(null);
                return;
            }
        }
        if (_altTransform_ == oldChild) {
            setAltTransform((PAltTransform) newChild);
            return;
        }
    }

    private class Elems_Cast implements Cast {

        public Object cast(Object o) {
            PElem node = (PElem) o;
            if ((node.parent() != null) && (node.parent() != AAlt.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AAlt.this)) {
                node.parent(AAlt.this);
            }
            return node;
        }
    }
}

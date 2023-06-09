package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AProdName extends PProdName {

    private TId _id_;

    private TId _prodNameTail_;

    public AProdName() {
    }

    public AProdName(TId _id_, TId _prodNameTail_) {
        setId(_id_);
        setProdNameTail(_prodNameTail_);
    }

    public Object clone() {
        return new AProdName((TId) cloneNode(_id_), (TId) cloneNode(_prodNameTail_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAProdName(this);
    }

    public TId getId() {
        return _id_;
    }

    public void setId(TId node) {
        if (_id_ != null) {
            _id_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _id_ = node;
    }

    public TId getProdNameTail() {
        return _prodNameTail_;
    }

    public void setProdNameTail(TId node) {
        if (_prodNameTail_ != null) {
            _prodNameTail_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _prodNameTail_ = node;
    }

    public String toString() {
        return "" + toString(_id_) + toString(_prodNameTail_);
    }

    void removeChild(Node child) {
        if (_id_ == child) {
            _id_ = null;
            return;
        }
        if (_prodNameTail_ == child) {
            _prodNameTail_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_id_ == oldChild) {
            setId((TId) newChild);
            return;
        }
        if (_prodNameTail_ == oldChild) {
            setProdNameTail((TId) newChild);
            return;
        }
    }
}

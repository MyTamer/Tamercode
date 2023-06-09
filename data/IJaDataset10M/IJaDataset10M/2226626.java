package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class ASimpleListTerm extends PListTerm {

    private PSpecifier _specifier_;

    private TId _id_;

    private TId _simpleTermTail_;

    public ASimpleListTerm() {
    }

    public ASimpleListTerm(PSpecifier _specifier_, TId _id_, TId _simpleTermTail_) {
        setSpecifier(_specifier_);
        setId(_id_);
        setSimpleTermTail(_simpleTermTail_);
    }

    public Object clone() {
        return new ASimpleListTerm((PSpecifier) cloneNode(_specifier_), (TId) cloneNode(_id_), (TId) cloneNode(_simpleTermTail_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseASimpleListTerm(this);
    }

    public PSpecifier getSpecifier() {
        return _specifier_;
    }

    public void setSpecifier(PSpecifier node) {
        if (_specifier_ != null) {
            _specifier_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _specifier_ = node;
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

    public TId getSimpleTermTail() {
        return _simpleTermTail_;
    }

    public void setSimpleTermTail(TId node) {
        if (_simpleTermTail_ != null) {
            _simpleTermTail_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _simpleTermTail_ = node;
    }

    public String toString() {
        return "" + toString(_specifier_) + toString(_id_) + toString(_simpleTermTail_);
    }

    void removeChild(Node child) {
        if (_specifier_ == child) {
            _specifier_ = null;
            return;
        }
        if (_id_ == child) {
            _id_ = null;
            return;
        }
        if (_simpleTermTail_ == child) {
            _simpleTermTail_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_specifier_ == oldChild) {
            setSpecifier((PSpecifier) newChild);
            return;
        }
        if (_id_ == oldChild) {
            setId((TId) newChild);
            return;
        }
        if (_simpleTermTail_ == oldChild) {
            setSimpleTermTail((TId) newChild);
            return;
        }
    }
}

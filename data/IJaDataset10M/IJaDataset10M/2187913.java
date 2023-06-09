package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class ARegExpBasic extends PBasic {

    private PRegExp _regExp_;

    public ARegExpBasic() {
    }

    public ARegExpBasic(PRegExp _regExp_) {
        setRegExp(_regExp_);
    }

    public Object clone() {
        return new ARegExpBasic((PRegExp) cloneNode(_regExp_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARegExpBasic(this);
    }

    public PRegExp getRegExp() {
        return _regExp_;
    }

    public void setRegExp(PRegExp node) {
        if (_regExp_ != null) {
            _regExp_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _regExp_ = node;
    }

    public String toString() {
        return "" + toString(_regExp_);
    }

    void removeChild(Node child) {
        if (_regExp_ == child) {
            _regExp_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_regExp_ == oldChild) {
            setRegExp((PRegExp) newChild);
            return;
        }
    }
}

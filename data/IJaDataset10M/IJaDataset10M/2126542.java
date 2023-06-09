package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class AUsageIs extends PUsageIs {

    private TUsage _usage_;

    private TIs _is_;

    public AUsageIs() {
    }

    public AUsageIs(TUsage _usage_, TIs _is_) {
        setUsage(_usage_);
        setIs(_is_);
    }

    public Object clone() {
        return new AUsageIs((TUsage) cloneNode(_usage_), (TIs) cloneNode(_is_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAUsageIs(this);
    }

    public TUsage getUsage() {
        return _usage_;
    }

    public void setUsage(TUsage node) {
        if (_usage_ != null) {
            _usage_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _usage_ = node;
    }

    public TIs getIs() {
        return _is_;
    }

    public void setIs(TIs node) {
        if (_is_ != null) {
            _is_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _is_ = node;
    }

    public String toString() {
        return "" + toString(_usage_) + toString(_is_);
    }

    void removeChild(Node child) {
        if (_usage_ == child) {
            _usage_ = null;
            return;
        }
        if (_is_ == child) {
            _is_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_usage_ == oldChild) {
            setUsage((TUsage) newChild);
            return;
        }
        if (_is_ == oldChild) {
            setIs((TIs) newChild);
            return;
        }
    }
}

package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class ARecordDescription extends PRecordDescription {

    private PGroupItem _groupItem_;

    private TDot _dot_;

    public ARecordDescription() {
    }

    public ARecordDescription(PGroupItem _groupItem_, TDot _dot_) {
        setGroupItem(_groupItem_);
        setDot(_dot_);
    }

    public Object clone() {
        return new ARecordDescription((PGroupItem) cloneNode(_groupItem_), (TDot) cloneNode(_dot_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseARecordDescription(this);
    }

    public PGroupItem getGroupItem() {
        return _groupItem_;
    }

    public void setGroupItem(PGroupItem node) {
        if (_groupItem_ != null) {
            _groupItem_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _groupItem_ = node;
    }

    public TDot getDot() {
        return _dot_;
    }

    public void setDot(TDot node) {
        if (_dot_ != null) {
            _dot_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _dot_ = node;
    }

    public String toString() {
        return "" + toString(_groupItem_) + toString(_dot_);
    }

    void removeChild(Node child) {
        if (_groupItem_ == child) {
            _groupItem_ = null;
            return;
        }
        if (_dot_ == child) {
            _dot_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_groupItem_ == oldChild) {
            setGroupItem((PGroupItem) newChild);
            return;
        }
        if (_dot_ == oldChild) {
            setDot((TDot) newChild);
            return;
        }
    }
}

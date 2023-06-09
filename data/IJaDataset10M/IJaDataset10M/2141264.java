package net.sf.cb2xml.sablecc.node;

import java.util.*;
import net.sf.cb2xml.sablecc.analysis.*;

public final class AThroughSequenceLiteralSequence extends PLiteralSequence {

    private PLiteralSequence _literalSequence_;

    private TComma _comma_;

    private PLiteral _from_;

    private TThrough _through_;

    private PLiteral _to_;

    public AThroughSequenceLiteralSequence() {
    }

    public AThroughSequenceLiteralSequence(PLiteralSequence _literalSequence_, TComma _comma_, PLiteral _from_, TThrough _through_, PLiteral _to_) {
        setLiteralSequence(_literalSequence_);
        setComma(_comma_);
        setFrom(_from_);
        setThrough(_through_);
        setTo(_to_);
    }

    public Object clone() {
        return new AThroughSequenceLiteralSequence((PLiteralSequence) cloneNode(_literalSequence_), (TComma) cloneNode(_comma_), (PLiteral) cloneNode(_from_), (TThrough) cloneNode(_through_), (PLiteral) cloneNode(_to_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAThroughSequenceLiteralSequence(this);
    }

    public PLiteralSequence getLiteralSequence() {
        return _literalSequence_;
    }

    public void setLiteralSequence(PLiteralSequence node) {
        if (_literalSequence_ != null) {
            _literalSequence_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _literalSequence_ = node;
    }

    public TComma getComma() {
        return _comma_;
    }

    public void setComma(TComma node) {
        if (_comma_ != null) {
            _comma_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _comma_ = node;
    }

    public PLiteral getFrom() {
        return _from_;
    }

    public void setFrom(PLiteral node) {
        if (_from_ != null) {
            _from_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _from_ = node;
    }

    public TThrough getThrough() {
        return _through_;
    }

    public void setThrough(TThrough node) {
        if (_through_ != null) {
            _through_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _through_ = node;
    }

    public PLiteral getTo() {
        return _to_;
    }

    public void setTo(PLiteral node) {
        if (_to_ != null) {
            _to_.parent(null);
        }
        if (node != null) {
            if (node.parent() != null) {
                node.parent().removeChild(node);
            }
            node.parent(this);
        }
        _to_ = node;
    }

    public String toString() {
        return "" + toString(_literalSequence_) + toString(_comma_) + toString(_from_) + toString(_through_) + toString(_to_);
    }

    void removeChild(Node child) {
        if (_literalSequence_ == child) {
            _literalSequence_ = null;
            return;
        }
        if (_comma_ == child) {
            _comma_ = null;
            return;
        }
        if (_from_ == child) {
            _from_ = null;
            return;
        }
        if (_through_ == child) {
            _through_ = null;
            return;
        }
        if (_to_ == child) {
            _to_ = null;
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        if (_literalSequence_ == oldChild) {
            setLiteralSequence((PLiteralSequence) newChild);
            return;
        }
        if (_comma_ == oldChild) {
            setComma((TComma) newChild);
            return;
        }
        if (_from_ == oldChild) {
            setFrom((PLiteral) newChild);
            return;
        }
        if (_through_ == oldChild) {
            setThrough((TThrough) newChild);
            return;
        }
        if (_to_ == oldChild) {
            setTo((PLiteral) newChild);
            return;
        }
    }
}

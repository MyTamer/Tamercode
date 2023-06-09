package org.sablecc.sablecc.node;

import java.util.*;
import org.sablecc.sablecc.analysis.*;

public final class AStates extends PStates {

    private final LinkedList _listId_ = new TypedLinkedList(new ListId_Cast());

    public AStates() {
    }

    public AStates(List _listId_) {
        {
            this._listId_.clear();
            this._listId_.addAll(_listId_);
        }
    }

    public Object clone() {
        return new AStates(cloneList(_listId_));
    }

    public void apply(Switch sw) {
        ((Analysis) sw).caseAStates(this);
    }

    public LinkedList getListId() {
        return _listId_;
    }

    public void setListId(List list) {
        _listId_.clear();
        _listId_.addAll(list);
    }

    public String toString() {
        return "" + toString(_listId_);
    }

    void removeChild(Node child) {
        if (_listId_.remove(child)) {
            return;
        }
    }

    void replaceChild(Node oldChild, Node newChild) {
        for (ListIterator i = _listId_.listIterator(); i.hasNext(); ) {
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
    }

    private class ListId_Cast implements Cast {

        public Object cast(Object o) {
            TId node = (TId) o;
            if ((node.parent() != null) && (node.parent() != AStates.this)) {
                node.parent().removeChild(node);
            }
            if ((node.parent() == null) || (node.parent() != AStates.this)) {
                node.parent(AStates.this);
            }
            return node;
        }
    }
}

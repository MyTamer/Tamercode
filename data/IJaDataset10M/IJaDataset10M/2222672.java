package org.oboedit.gui;

import org.obo.datamodel.OBOSession;
import org.obo.history.*;
import org.oboedit.controller.SessionManager;
import java.util.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import org.apache.log4j.*;

public class HistoryTreeModel implements TreeModel {

    protected static final Logger logger = Logger.getLogger(HistoryTreeModel.class);

    protected Vector<HistoryList> histories;

    private Vector<TreeModelListener> listeners;

    private Object root = "All histories";

    public HistoryTreeModel(OBOSession history) {
        histories = new Vector<HistoryList>();
        histories.addAll(history.getArchivedHistories());
        histories.add(history.getCurrentHistory());
        listeners = new Vector<TreeModelListener>();
    }

    public HistoryTreeModel(HistoryList historyList) {
        histories = new Vector<HistoryList>();
        histories.add(historyList);
        listeners = new Vector<TreeModelListener>();
    }

    public HistoryTreeModel(Vector<HistoryList> histories) {
        listeners = new Vector<TreeModelListener>();
        this.histories = histories;
    }

    public void addTreeModelListener(TreeModelListener l) {
        listeners.addElement(l);
    }

    protected void fireTreeStructureChanged(TreeModelEvent e) {
        for (int i = 0; i < listeners.size(); i++) {
            TreeModelListener tml = listeners.elementAt(i);
            tml.treeStructureChanged(e);
        }
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listeners.removeElement(l);
    }

    public TreePath getActiveHistoryPath() {
        Object[] nodes = new Object[2];
        nodes[0] = root;
        nodes[1] = SessionManager.getManager().getSession();
        return new TreePath(nodes);
    }

    public void reloadActiveHistory() {
        fireTreeStructureChanged(new TreeModelEvent(this, getActiveHistoryPath()));
    }

    public Object getChild(Object parent, int index) {
        if (parent == root) {
            if (index < histories.size()) return histories.elementAt(index); else return SessionManager.getManager().getSession();
        } else if (parent instanceof OBOSession) {
            OBOSession editHistory = (OBOSession) parent;
            if (index < editHistory.getArchivedHistories().size()) return ((OBOSession) parent).getArchivedHistories().get(index); else return ((OBOSession) parent).getCurrentHistory();
        } else if (parent instanceof HistoryList) {
            return ((HistoryList) parent).getItemAt(index);
        } else if (parent instanceof TermMacroHistoryItem) {
            return ((TermMacroHistoryItem) parent).getItemAt(index);
        } else if (parent instanceof TermMoveHistoryItem) {
            return "Moved";
        } else if (parent instanceof CreateLinkHistoryItem) {
            return "Copied";
        } else if (parent instanceof DeleteLinkHistoryItem) {
            return "Deleted";
        } else if (parent instanceof TermMergeHistoryItem) {
            return "Merge";
        } else return null;
    }

    public int getChildCount(Object parent) {
        if (parent == root) {
            return histories.size();
        } else if (parent instanceof OBOSession) {
            return ((OBOSession) parent).getArchivedHistories().size() + 1;
        } else if (parent instanceof HistoryList) return ((HistoryList) parent).size(); else if (parent instanceof TermMacroHistoryItem) return ((TermMacroHistoryItem) parent).size(); else if (parent instanceof TermMoveHistoryItem) {
            return 0;
        } else if (parent instanceof CreateLinkHistoryItem) {
            return 0;
        } else if (parent instanceof DeleteLinkHistoryItem) {
            return 0;
        } else if (parent instanceof TermMergeHistoryItem) return 0; else return 0;
    }

    public int getIndexOfChild(Object parent, Object child) {
        if (parent == root) return histories.indexOf(child); else if (parent instanceof OBOSession) return ((OBOSession) parent).getCurrentHistory().getIndex((HistoryItem) child); else if (parent instanceof HistoryList) return ((HistoryList) parent).getIndex((HistoryItem) child); else return -1;
    }

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }
}

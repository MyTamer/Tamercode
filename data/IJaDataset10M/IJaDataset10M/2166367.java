package net.datao.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.lang.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

public class ComponentTable extends JTable {

    public ComponentTable() {
        setDefaultRenderer(JComponent.class, new JComponentCellRenderer());
        setDefaultEditor(JComponent.class, new JComponentCellEditor());
    }

    public ComponentTable(int rows, int columns) {
        super(rows, columns);
        setDefaultRenderer(JComponent.class, new JComponentCellRenderer());
        setDefaultEditor(JComponent.class, new JComponentCellEditor());
    }

    public TableCellRenderer getCellRenderer(int row, int column) {
        TableColumn tableColumn = getColumnModel().getColumn(column);
        TableCellRenderer renderer = tableColumn.getCellRenderer();
        if (renderer == null) {
            Class c = getColumnClass(column);
            if (c.equals(Object.class)) {
                Object o = getValueAt(row, column);
                if (o != null) c = getValueAt(row, column).getClass();
            }
            renderer = getDefaultRenderer(c);
        }
        return renderer;
    }

    public TableCellEditor getCellEditor(int row, int column) {
        TableColumn tableColumn = getColumnModel().getColumn(column);
        TableCellEditor editor = tableColumn.getCellEditor();
        if (editor == null) {
            Class c = getColumnClass(column);
            if (c.equals(Object.class)) {
                Object o = getValueAt(row, column);
                if (o != null) c = getValueAt(row, column).getClass();
            }
            editor = getDefaultEditor(c);
        }
        return editor;
    }

    class JComponentCellRenderer implements TableCellRenderer {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return (JComponent) value;
        }
    }

    public class JComponentCellEditor implements TableCellEditor, Serializable {

        protected EventListenerList listenerList = new EventListenerList();

        protected transient ChangeEvent changeEvent = null;

        protected JComponent editorComponent = null;

        protected JComponent container = null;

        public Component getComponent() {
            return editorComponent;
        }

        public Object getCellEditorValue() {
            return editorComponent;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            if (editorComponent != null && anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getID() == MouseEvent.MOUSE_PRESSED) {
                Component dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, 3, 3);
                MouseEvent e = (MouseEvent) anEvent;
                MouseEvent e2 = new MouseEvent(dispatchComponent, MouseEvent.MOUSE_RELEASED, e.getWhen() + 100000, e.getModifiers(), 3, 3, e.getClickCount(), e.isPopupTrigger());
                dispatchComponent.dispatchEvent(e2);
                e2 = new MouseEvent(dispatchComponent, MouseEvent.MOUSE_CLICKED, e.getWhen() + 100001, e.getModifiers(), 3, 3, 1, e.isPopupTrigger());
                dispatchComponent.dispatchEvent(e2);
            }
            return false;
        }

        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

        protected void fireEditingStopped() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null) changeEvent = new ChangeEvent(this);
                    ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
                }
            }
        }

        protected void fireEditingCanceled() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    if (changeEvent == null) changeEvent = new ChangeEvent(this);
                    ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
                }
            }
        }

        public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, false);
            editorComponent = (JComponent) value;
            container = tree;
            return editorComponent;
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editorComponent = (JComponent) value;
            container = table;
            return editorComponent;
        }
    }
}

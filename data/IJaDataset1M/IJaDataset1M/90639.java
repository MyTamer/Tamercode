package jmri.jmrit.symbolicprog;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

/**
 * JTable editor for cells representing CV values.  This is a somewhat unconventional
 * thing in several ways:
 * <UL>
 * <LI>The returned value is not the String edited into the cell, but an
 * Integer value.  It is not clear how that arose, and it should probably be
 * changed at some point.
 * <LI>This is also a focus listener.  People are not used to having to
 * hit return/enter to "set" their value in place, and are rather expecting
 * that the value they typed will be in effect as soon as they type it.  We
 * use a focusListener to do that.
 * </UL>
 *
 * @author			Bob Jacobsen   Copyright (C) 2001
 * @version             $Revision: 1.15 $
 */
public class ValueEditor extends JComboBox implements TableCellEditor, FocusListener {

    protected transient Vector<CellEditorListener> listeners;

    protected transient String originalValue = null;

    protected Object mValue;

    public ValueEditor() {
        super();
        listeners = new Vector<CellEditorListener>();
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        mValue = value;
        if (log.isDebugEnabled()) log.debug("getTableCellEditorComponent " + row + " " + column + " " + isSelected + " " + value.getClass());
        table.setRowSelectionInterval(row, row);
        table.setColumnSelectionInterval(column, column);
        if (value instanceof Component) {
            if (value instanceof JTextField) {
                JTextField tempField = (JTextField) value;
                originalValue = tempField.getText();
                tempField.addFocusListener(this);
                return tempField;
            } else return (Component) value;
        } else if (value instanceof String) return new JLabel((String) value); else return new JLabel("Unknown value type!");
    }

    /** FocusListener implementations */
    public void focusGained(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusGained");
        if (mValue instanceof JTextField) {
            JTextField tempField = (JTextField) mValue;
            originalValue = tempField.getText();
        }
    }

    public void focusLost(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusLost");
        if (!(mValue instanceof JTextField) || !(originalValue.equals(((JTextField) mValue).getText()))) fireEditingStopped();
    }

    void removeFocusListener() {
        if (mValue instanceof JTextField) {
            JTextField tempField = (JTextField) mValue;
            originalValue = null;
            tempField.removeFocusListener(this);
        }
    }

    public void cancelCellEditing() {
        if (log.isDebugEnabled()) log.debug("cancelCellEditing");
        removeFocusListener();
        fireEditingCanceled();
    }

    public Object getCellEditorValue() {
        if (log.isDebugEnabled()) {
            log.debug("getCellEditorValue with 'value' object of type " + mValue.getClass());
        }
        if (mValue instanceof JTextField) {
            return Integer.valueOf(((JTextField) mValue).getText());
        } else if (mValue instanceof Component) {
            return mValue;
        } else {
            log.error("getCellEditorValue unable to return a value from unknown type " + mValue.getClass());
            return null;
        }
    }

    public boolean isCellEditable(EventObject eo) {
        return true;
    }

    public boolean shouldSelectCell(EventObject eo) {
        return true;
    }

    public boolean stopCellEditing() {
        if (log.isDebugEnabled()) log.debug("stopCellEditing");
        removeFocusListener();
        fireEditingStopped();
        return true;
    }

    public void addCellEditorListener(CellEditorListener cel) {
        listeners.addElement(cel);
    }

    public void removeCellEditorListener(CellEditorListener cel) {
        listeners.removeElement(cel);
    }

    protected void fireEditingCanceled() {
        if (log.isDebugEnabled()) log.debug("fireEditingCancelled, but we are not setting back the old value");
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.elementAt(i).editingCanceled(ce);
        }
    }

    protected void fireEditingStopped() {
        if (log.isDebugEnabled()) log.debug("fireEditingStopped");
        ChangeEvent ce = new ChangeEvent(this);
        for (int i = listeners.size() - 1; i >= 0; i--) {
            listeners.elementAt(i).editingStopped(ce);
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ValueEditor.class.getName());
}

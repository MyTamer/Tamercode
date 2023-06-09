/*
 * DecisionView.java
 *
 * Created on December 17, 2001, 9:52 PM
 */

package saga.view;

import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JRadioButton;

import saga.inout.creation.Alternative;
import saga.inout.creation.Decision;

/**
 *
 * @author  kre
 */
public class DecisionView extends javax.swing.JPanel implements ActionListener, ItemSelectable {
    
    /** Property key to attach the alternative to the radio button. */
    public static final String ALTERNATIVE_PROPERTY =
        "DecisionView.alternative";

    /** Creates new form DecisionView */
    public DecisionView() {
        initComponents();
    }
    
    /** Creates new form DecisionView with the given <var>model</var>. */
    public DecisionView(Decision model) {
        this();
        setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        alternativeGroup = new javax.swing.ButtonGroup();
        questionArea = new javax.swing.JTextArea();
        answerPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        questionArea.setColumns(32);
        questionArea.setEditable(false);
        questionArea.setFont(new java.awt.Font("Serif", 1, 12));
        questionArea.setLineWrap(true);
        questionArea.setRows(4);
        questionArea.setWrapStyleWord(true);
        add(questionArea, java.awt.BorderLayout.CENTER);

        answerPanel.setLayout(new java.awt.GridLayout(0, 1));

        add(answerPanel, java.awt.BorderLayout.SOUTH);

    }//GEN-END:initComponents

    /**
     * Add an alternative. This method is used by the setter.
     * @param alternative the alternative to add
     */
    protected void addAlternative(Alternative alternative) {
        JRadioButton button = new JRadioButton(alternative.getAnswer());
        button.putClientProperty(ALTERNATIVE_PROPERTY, alternative);
        button.addActionListener(this);
        alternativeGroup.add(button);
        answerPanel.add(button);
    }
    
    /**
     * Get the currently selected alternative.
     * @return the selected alternative, or null if no selection has been made yet.
     */
    public Alternative getAlternative() {
        Enumeration enum = alternativeGroup.getElements();
        while (enum.hasMoreElements()) {
            AbstractButton button = (AbstractButton)enum.nextElement();
            if (alternativeGroup.isSelected(button.getModel())) {
                return (Alternative)button.getClientProperty(ALTERNATIVE_PROPERTY);
            }
        }
        return null;
    }

    /**
     * Getter for property model.
     * @return Value of property model.
     */
    public Decision getModel() {
        return this.model;
    }
    
    /**
     * Setter for property model.
     * @param model New value of property model.
     */
    public void setModel(Decision model) {
        while (alternativeGroup.getButtonCount() > 0) {
            AbstractButton button = (AbstractButton)alternativeGroup
                .getElements().nextElement();
            answerPanel.remove(button);
            alternativeGroup.remove(button);
        }
        
        this.model = model;
        
        if (model != null) {
            questionArea.setText(model.getQuestion());
            Iterator i = model.getAlternatives();
            while (i.hasNext()) {
                addAlternative((Alternative)i.next());
            }
        } else {
            questionArea.setText("");
        }
    }
        
    /**
     * Registers ItemListener to receive events.
     * @param listener The listener to register.
     */
    public synchronized void addItemListener(java.awt.event.ItemListener listener) {
        if (listenerList == null ) {
            listenerList = new javax.swing.event.EventListenerList();
        }
        listenerList.add(java.awt.event.ItemListener.class, listener);
    }
    
    /**
     * Removes ItemListener from the list of listeners.
     * @param listener The listener to remove.
     */
    public synchronized void removeItemListener(java.awt.event.ItemListener listener) {
        listenerList.remove(java.awt.event.ItemListener.class, listener);
    }
    
    /**
     * Notifies all registered listeners about the event.
     */
    private void fireItemListenerItemStateChanged() {
        ItemEvent e = null;
        if (listenerList == null) return;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ItemListener.class) {
                if (e == null) {
                    e = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                        getAlternative(), ItemEvent.SELECTED);
                }
                ((ItemListener)listeners[i+1]).itemStateChanged(e);
            }
        }
    }
    
    /**
     * Get the selected objects.
     * @return an array of the selected objects
     * @see getAlternative
     */
    public Object[] getSelectedObjects() {
        Alternative alternative = getAlternative();
        if (alternative != null) {
            return new Object[] { alternative };
        } else {
            return new Object[0];
        }
    }
    
    public void actionPerformed(ActionEvent actionEvent) {
        fireItemListenerItemStateChanged();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel answerPanel;
    private javax.swing.JTextArea questionArea;
    private javax.swing.ButtonGroup alternativeGroup;
    // End of variables declaration//GEN-END:variables

    /** Holds value of property model. */
    private Decision model;    

    /** Utility field used by event firing mechanism. */
    private javax.swing.event.EventListenerList listenerList =  null;
    
}

package com.sodad.weka.gui.sql.event;

import java.util.EventObject;
import javax.swing.DefaultListModel;

/**
 * An event that is generated when a history is modified.
 *
 * @see         HistoryChangedListener
 * @author      FracPete (fracpete at waikato dot ac dot nz)
 * @version     $Revision: 1.2 $
 */
public class HistoryChangedEvent extends EventObject {

    /** for serialization */
    private static final long serialVersionUID = 7476087315774869973L;

    /** the name of the history */
    protected String m_HistoryName;

    /** the history model */
    protected DefaultListModel m_History;

    /**
   * constructs the event
   * @param name        the name of the history
   * @param history     the model of the history
   */
    public HistoryChangedEvent(Object source, String name, DefaultListModel history) {
        super(source);
        m_HistoryName = name;
        m_History = history;
    }

    /**
   * returns the name of the history
   */
    public String getHistoryName() {
        return m_HistoryName;
    }

    /**
   * returns the history model
   */
    public DefaultListModel getHistory() {
        return m_History;
    }

    /**
   * returns the event in a string representation
   * @return        the event in a string representation
   */
    public String toString() {
        String result;
        result = super.toString();
        result = result.substring(0, result.length() - 1);
        result += ",name=" + getHistoryName() + ",history=" + getHistory() + "]";
        return result;
    }
}

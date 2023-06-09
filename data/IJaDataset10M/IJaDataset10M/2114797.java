package jmri.jmrit.symbolicprog;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Extends VariableValue to represent a indexed variable
 * split across two indexed CVs.
 *
 * Factor is the multiplier of the value in the high order CV
 *
 * Value to put in High CV = (value in text field)/Factor
 * Value to put in Low CV = (value in text field) - High CV value
 *
 * Value to put in text field = ((value in High CV) * Factor) + Low CV
 *
 * @author   Howard G. Penny  Copyright (C) 2005
 * @version  $Revision: 1.15 $
 *
 */
public class IndexedPairVariableValue extends VariableValue implements ActionListener, PropertyChangeListener, FocusListener {

    public IndexedPairVariableValue(int row, String name, String comment, String cvName, boolean readOnly, boolean infoOnly, boolean writeOnly, boolean opsOnly, int cvNum, String mask, int minVal, int maxVal, Vector<CvValue> v, JLabel status, String stdname, int secondCVrow, String pSecondCV, int pFactor, int pOffset, String uppermask) {
        super(name, comment, cvName, readOnly, infoOnly, writeOnly, opsOnly, cvNum, mask, v, status, stdname);
        _row = row;
        _maxVal = maxVal;
        _minVal = minVal;
        _value = new JTextField("0", 3);
        _secondCVrow = secondCVrow;
        _secondCV = pSecondCV;
        _Factor = pFactor;
        _Offset = pOffset;
        lowerbitmask = maskVal(mask);
        lowerbitoffset = offsetVal(mask);
        upperbitmask = maskVal(uppermask);
        upperbitoffset = offsetVal(uppermask);
        CvValue cv = (_cvVector.elementAt(_row));
        cv.addPropertyChangeListener(this);
        cv.setState(CvValue.FROMFILE);
        CvValue cv1 = (_cvVector.elementAt(_secondCVrow));
        cv1.addPropertyChangeListener(this);
        cv1.setState(CvValue.FROMFILE);
    }

    public CvValue[] usesCVs() {
        return new CvValue[] { _cvVector.elementAt(_row), _cvVector.elementAt(_secondCVrow) };
    }

    String _secondCV;

    int _secondCVrow;

    int _Factor;

    int _Offset;

    int lowerbitmask;

    int lowerbitoffset;

    int upperbitmask;

    int upperbitoffset;

    int _row;

    int _maxVal;

    int _minVal;

    public void setToolTipText(String t) {
        super.setToolTipText(t);
        _value.setToolTipText(t);
    }

    public Object rangeVal() {
        return "Split value";
    }

    String oldContents = "";

    void enterField() {
        oldContents = _value.getText();
    }

    void exitField() {
        if (_value != null && !oldContents.equals(_value.getText())) {
            int newVal = Integer.valueOf(_value.getText()).intValue();
            int oldVal = Integer.valueOf(oldContents).intValue();
            updatedTextField();
            prop.firePropertyChange("Value", Integer.valueOf(oldVal), Integer.valueOf(newVal));
        }
    }

    void updatedTextField() {
        if (log.isDebugEnabled()) log.debug("enter updatedTextField");
        CvValue cvLow = _cvVector.elementAt(_row);
        CvValue cvHigh = _cvVector.elementAt(_secondCVrow);
        int newEntry;
        try {
            newEntry = Integer.valueOf(_value.getText()).intValue();
        } catch (java.lang.NumberFormatException ex) {
            newEntry = 0;
        }
        int newHigh = (newEntry - _Offset) / _Factor;
        int newLow = newEntry - (newHigh * _Factor);
        if (log.isDebugEnabled()) log.debug("new value " + newEntry + " gives first=" + newLow + " second=" + newHigh);
        if (cvLow.getValue() != newLow || cvHigh.getValue() != newHigh) {
            cvLow.setValue(newLow);
            cvHigh.setValue(newHigh);
        }
        if (log.isDebugEnabled()) log.debug("exit updatedTextField");
    }

    /** ActionListener implementations */
    public void actionPerformed(ActionEvent e) {
        if (log.isDebugEnabled()) log.debug("actionPerformed");
        int newVal = ((Integer.valueOf(_value.getText()).intValue()) - _Offset) / _Factor;
        updatedTextField();
        prop.firePropertyChange("Value", null, Integer.valueOf(newVal));
    }

    /** FocusListener implementations */
    public void focusGained(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusGained");
        enterField();
    }

    public void focusLost(FocusEvent e) {
        if (log.isDebugEnabled()) log.debug("focusLost");
        exitField();
    }

    public String getValueString() {
        int newVal = ((Integer.valueOf(_value.getText()).intValue()) - _Offset) / _Factor;
        return String.valueOf(newVal);
    }

    public void setIntValue(int i) {
        setValue((i - _Offset) / _Factor);
    }

    public int getIntValue() {
        return ((Integer.valueOf(_value.getText()).intValue()) - _Offset) / _Factor;
    }

    public Object getValueObject() {
        return Integer.valueOf(_value.getText());
    }

    public Component getCommonRep() {
        if (getReadOnly()) {
            JLabel r = new JLabel(_value.getText());
            updateRepresentation(r);
            return r;
        } else return _value;
    }

    public void setValue(int value) {
        if (log.isDebugEnabled()) log.debug("setValue " + value);
        int oldVal;
        try {
            oldVal = (Integer.valueOf(_value.getText()).intValue() - _Offset) / _Factor;
        } catch (java.lang.NumberFormatException ex) {
            oldVal = -999;
        }
        if (log.isDebugEnabled()) log.debug("setValue with new value " + value + " old value " + oldVal);
        if (oldVal != value) {
            _value.setText("" + value);
            updatedTextField();
            prop.firePropertyChange("Value", Integer.valueOf(oldVal), Integer.valueOf(value));
        }
    }

    Color _defaultColor;

    void setColor(Color c) {
        if (c != null) _value.setBackground(c); else _value.setBackground(_defaultColor);
    }

    public Component getNewRep(String format) {
        JTextField value = new VarTextField(_value.getDocument(), _value.getText(), 3, this);
        if (getReadOnly() || getInfoOnly()) {
            value.setEditable(false);
        }
        reps.add(value);
        updateRepresentation(value);
        return value;
    }

    public void setAvailable(boolean a) {
        _value.setVisible(a);
        for (Component c : reps) c.setVisible(a);
        super.setAvailable(a);
    }

    java.util.List<Component> reps = new java.util.ArrayList<Component>();

    private int _progState = 0;

    private boolean programmingLow = true;

    private static final int IDLE = 0;

    private static final int WRITING_PI4R = 1;

    private static final int WRITING_PI4W = 2;

    private static final int WRITING_SI4R = 3;

    private static final int WRITING_SI4W = 4;

    private static final int READING_CV = 5;

    private static final int WRITING_CV = 6;

    private static final int WRITING_PI4C = 7;

    private static final int WRITING_SI4C = 8;

    private static final int COMPARE_CV = 9;

    /**
     * Count number of retries done
     */
    private int retries = 0;

    /**
     * Define maximum number of retries of read/write operations before moving on
     */
    private static final int RETRY_MAX = 2;

    /**
     * Notify the connected CVs of a state change from above
     * @param state
     */
    public void setCvState(int state) {
        (_cvVector.elementAt(_row)).setState(state);
        (_cvVector.elementAt(_secondCVrow)).setState(state);
    }

    public void setToRead(boolean state) {
        if (getInfoOnly() || getWriteOnly()) state = false;
        (_cvVector.elementAt(_row)).setToRead(state);
        (_cvVector.elementAt(_secondCVrow)).setToRead(state);
    }

    public boolean isToRead() {
        return (_cvVector.elementAt(_row)).isToRead();
    }

    public void setToWrite(boolean state) {
        if (getInfoOnly() || getReadOnly()) state = false;
        (_cvVector.elementAt(_row)).setToWrite(state);
        (_cvVector.elementAt(_secondCVrow)).setToWrite(state);
    }

    public boolean isToWrite() {
        return (_cvVector.elementAt(_row)).isToWrite();
    }

    public boolean isChanged() {
        CvValue cvLow = (_cvVector.elementAt(_row));
        CvValue cvHigh = (_cvVector.elementAt(_secondCVrow));
        return (considerChanged(cvLow) || considerChanged(cvHigh));
    }

    public void readChanges() {
        if (isChanged()) readAll();
    }

    public void writeChanges() {
        if (isChanged()) writeAll();
    }

    public void readAll() {
        setBusy(true);
        setToRead(false);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in read()");
        if ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).siVal() >= 0) {
            _progState = WRITING_PI4R;
        } else {
            _progState = WRITING_SI4R;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV read");
        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writePI(_status);
    }

    public void writeAll() {
        if (getReadOnly()) {
            log.error("unexpected write operation when readOnly is set");
        }
        setBusy(true);
        setToWrite(false);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in write()");
        if ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).siVal() >= 0) {
            _progState = WRITING_PI4W;
        } else {
            _progState = WRITING_SI4W;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV write");
        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writePI(_status);
    }

    public void confirmAll() {
        setBusy(true);
        setToRead(false);
        if (_progState != IDLE) log.warn("Programming state " + _progState + ", not IDLE, in confirm()");
        if ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).siVal() >= 0) {
            _progState = WRITING_PI4C;
        } else {
            _progState = WRITING_SI4C;
        }
        retries = 0;
        if (log.isDebugEnabled()) log.debug("invoke PI write for CV confirm");
        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writePI(_status);
    }

    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (log.isDebugEnabled()) log.debug("property changed event - name: " + e.getPropertyName());
        if (e.getPropertyName().equals("Busy") && ((Boolean) e.getNewValue()).equals(Boolean.FALSE)) {
            switch(_progState) {
                case IDLE:
                    if (log.isDebugEnabled()) log.error("Busy goes false with state IDLE");
                    return;
                case WRITING_PI4R:
                case WRITING_PI4C:
                case WRITING_PI4W:
                    if (log.isDebugEnabled()) log.debug("Busy goes false with state WRITING_PI");
                    if ((retries < RETRY_MAX) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.STORED)) {
                        log.debug("retry");
                        retries++;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writePI(_status);
                        return;
                    }
                    retries = 0;
                    if (_progState == WRITING_PI4R) _progState = WRITING_SI4R; else if (_progState == WRITING_PI4C) _progState = WRITING_SI4C; else _progState = WRITING_SI4W;
                    (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writeSI(_status);
                    return;
                case WRITING_SI4R:
                case WRITING_SI4C:
                case WRITING_SI4W:
                    if (log.isDebugEnabled()) log.debug("Busy goes false with state WRITING_SI");
                    if ((retries < RETRY_MAX) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.STORED)) {
                        log.debug("retry");
                        retries++;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writeSI(_status);
                        return;
                    }
                    retries = 0;
                    if (_progState == WRITING_SI4R) {
                        _progState = READING_CV;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).readIcV(_status);
                    } else if (_progState == WRITING_SI4C) {
                        _progState = COMPARE_CV;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).confirmIcV(_status);
                    } else {
                        _progState = WRITING_CV;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writeIcV(_status);
                    }
                    return;
                case READING_CV:
                    if (log.isDebugEnabled()) log.debug("Finished reading the Indexed CV");
                    if ((retries < RETRY_MAX) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.READ)) {
                        log.debug("retry");
                        retries++;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).readIcV(_status);
                        return;
                    }
                    retries = 0;
                    _progState = IDLE;
                    if (programmingLow) {
                        programmingLow = false;
                        readAll();
                    } else {
                        programmingLow = true;
                        setBusy(false);
                    }
                    return;
                case COMPARE_CV:
                    if (log.isDebugEnabled()) log.debug("Finished reading the Indexed CV for compare");
                    if ((retries < RETRY_MAX) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.SAME) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.DIFF)) {
                        log.debug("retry");
                        retries++;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).confirmIcV(_status);
                    }
                    return;
                case WRITING_CV:
                    if (log.isDebugEnabled()) log.debug("Finished writing the Indexed CV");
                    if ((retries < RETRY_MAX) && ((_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).getState() != CvValue.STORED)) {
                        log.debug("retry");
                        retries++;
                        (_cvVector.elementAt(programmingLow ? _row : _secondCVrow)).writeIcV(_status);
                        return;
                    }
                    retries = 0;
                    _progState = IDLE;
                    if (programmingLow) {
                        programmingLow = false;
                        writeAll();
                    } else {
                        programmingLow = true;
                        super.setState(STORED);
                        setBusy(false);
                    }
                    return;
                default:
                    log.error("Unexpected state found: " + _progState);
                    _progState = IDLE;
                    return;
            }
        } else if (e.getPropertyName().equals("State")) {
            CvValue cvLow = _cvVector.elementAt(_row);
            CvValue cvHigh = _cvVector.elementAt(_secondCVrow);
            if (log.isDebugEnabled()) log.debug("CV State changed to " + cvLow.getState());
            if (cvHigh.getState() == VariableValue.UNKNOWN) {
                if (cvLow.getState() == VariableValue.EDITED) {
                    setState(VariableValue.EDITED);
                } else {
                    setState(VariableValue.UNKNOWN);
                }
            } else {
                setState(cvLow.getState());
            }
        } else if (e.getPropertyName().equals("Value")) {
            CvValue cvLow = _cvVector.elementAt(_row);
            CvValue cvHigh = _cvVector.elementAt(_secondCVrow);
            int newVal = (cvLow.getValue() + (cvHigh.getValue() * _Factor));
            if (log.isDebugEnabled()) log.debug("set value to " + newVal + " based on cv0=" + cvLow.getValue() + " cv1=" + cvHigh.getValue());
            setValue(newVal);
            if (cvHigh.getState() == VariableValue.UNKNOWN) {
                if (cvLow.getState() == VariableValue.EDITED) {
                    setState(VariableValue.EDITED);
                } else {
                    setState(VariableValue.UNKNOWN);
                }
            } else {
                setState(cvLow.getState());
            }
        }
    }

    JTextField _value = null;

    public class VarTextField extends JTextField {

        VarTextField(Document doc, String text, int col, IndexedPairVariableValue var) {
            super(doc, text, col);
            _var = var;
            setBackground(_var._value.getBackground());
            addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    thisActionPerformed(e);
                }
            });
            addFocusListener(new java.awt.event.FocusListener() {

                public void focusGained(FocusEvent e) {
                    if (log.isDebugEnabled()) log.debug("focusGained");
                    enterField();
                }

                public void focusLost(FocusEvent e) {
                    if (log.isDebugEnabled()) log.debug("focusLost");
                    exitField();
                }
            });
            _var.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    originalPropertyChanged(e);
                }
            });
        }

        IndexedPairVariableValue _var;

        void thisActionPerformed(java.awt.event.ActionEvent e) {
            _var.actionPerformed(e);
        }

        void originalPropertyChanged(java.beans.PropertyChangeEvent e) {
            if (e.getPropertyName().equals("State")) {
                setBackground(_var._value.getBackground());
            }
        }
    }

    public void dispose() {
        if (log.isDebugEnabled()) log.debug("dispose");
        if (_value != null) {
            _value.removeActionListener(this);
            _value.removeFocusListener(this);
            _value.removePropertyChangeListener(this);
            _value = null;
        }
        (_cvVector.elementAt(_row)).removePropertyChangeListener(this);
        (_cvVector.elementAt(_secondCVrow)).removePropertyChangeListener(this);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(IndexedPairVariableValue.class.getName());
}

package eu.seebetter.ini.chips.config;

import java.awt.event.ActionEvent;
import java.util.prefs.PreferenceChangeEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import net.sf.jaer.chip.Chip;

/**
 * Base class for configuration bits - both originating from USB chip and CPLD logic chip.
 * @author tobi
 */
public class AbstractConfigBit extends AbstractConfigValue implements ConfigBit {

    protected volatile boolean value;

    protected boolean def;

    /** This Action can be used in GUIs */
    private SelectAction action = new SelectAction();

    public AbstractConfigBit(Chip chip, String name, String tip, boolean def) {
        super(chip, name, tip);
        this.def = def;
        loadPreference();
        prefs.addPreferenceChangeListener(this);
        action.putValue(Action.SHORT_DESCRIPTION, tip);
        action.putValue(Action.NAME, name);
        action.putValue(Action.SELECTED_KEY, value);
    }

    /** Sets the value and notifies observers if it changes.
     * 
     * @param value the new value
     */
    @Override
    public void set(boolean value) {
        if (this.value != value) {
            setChanged();
        }
        this.value = value;
        action.putValue(Action.SELECTED_KEY, value);
        notifyObservers();
    }

    @Override
    public boolean isSet() {
        return value;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent e) {
        if (e.getKey().equals(key)) {
            boolean newv = Boolean.parseBoolean(e.getNewValue());
            set(newv);
        }
    }

    @Override
    public void loadPreference() {
        set(prefs.getBoolean(key, def));
    }

    @Override
    public void storePreference() {
        prefs.putBoolean(key, value);
    }

    @Override
    public String toString() {
        return String.format("AbstractConfigBit name=%s key=%s value=%s", name, key, value);
    }

    /**
     * @return the action
     */
    public SelectAction getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(SelectAction action) {
        this.action = action;
    }

    /** This action toggles the bit */
    public class SelectAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            set(!value);
        }
    }
}

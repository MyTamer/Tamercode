package org.eclipse.jface.bindings.keys.formatting;

import org.eclipse.jface.bindings.keys.IKeyLookup;
import org.eclipse.jface.bindings.keys.KeyLookupFactory;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;

/**
 * <p>
 * Formats the keys in the internal key sequence grammar. This is used for
 * persistence, and is not really intended for display to the user.
 * </p>
 * 
 * @since 3.1
 */
public final class FormalKeyFormatter extends AbstractKeyFormatter {

    public String format(final int key) {
        final IKeyLookup lookup = KeyLookupFactory.getDefault();
        return lookup.formalNameLookup(key);
    }

    protected String getKeyDelimiter() {
        return KeyStroke.KEY_DELIMITER;
    }

    protected String getKeyStrokeDelimiter() {
        return KeySequence.KEY_STROKE_DELIMITER;
    }

    protected int[] sortModifierKeys(final int modifierKeys) {
        final IKeyLookup lookup = KeyLookupFactory.getDefault();
        final int[] sortedKeys = new int[4];
        int index = 0;
        if ((modifierKeys & lookup.getAlt()) != 0) {
            sortedKeys[index++] = lookup.getAlt();
        }
        if ((modifierKeys & lookup.getCommand()) != 0) {
            sortedKeys[index++] = lookup.getCommand();
        }
        if ((modifierKeys & lookup.getCtrl()) != 0) {
            sortedKeys[index++] = lookup.getCtrl();
        }
        if ((modifierKeys & lookup.getShift()) != 0) {
            sortedKeys[index++] = lookup.getShift();
        }
        return sortedKeys;
    }
}

package org.eclipse.ui.commands;

import org.eclipse.ui.keys.KeySequence;

/**
 * <p>
 * An instance of <code>IKeySequenceBinding</code> represents a binding
 * between a command and a key sequence. This is a wrapper for the a key
 * sequence.
 * </p>
 * <p>
 * This interface is not intended to be extended or implemented by clients.
 * </p>
 * 
 * @since 3.0
 * @see org.eclipse.ui.commands.ICommand
 * @deprecated Please use the bindings support in the "org.eclipse.jface"
 * plug-in instead.
 * @see org.eclipse.jface.bindings.keys.KeyBinding
 */
public interface IKeySequenceBinding extends Comparable {

    /**
     * Returns the key sequence represented in this binding.
     * 
     * @return the key sequence. Guaranteed not to be <code>null</code>.
     */
    KeySequence getKeySequence();
}

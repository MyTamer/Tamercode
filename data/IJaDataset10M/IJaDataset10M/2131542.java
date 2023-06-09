package org.dishevelled.iconbundle;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.io.Serializable;

/**
 * Typesafe enumeration of icon states.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class IconState implements Serializable {

    /** State name. */
    private String name;

    /**
     * Create a new IconState with the specified name.
     *
     * @param name state name
     */
    private IconState(final String name) {
        this.name = name;
    }

    /**
     * Return the state name.
     *
     * @return the state name
     */
    public String toString() {
        return name;
    }

    /**
     * Return true if this icon state is <code>IconState.NORMAL</code>.
     *
     * @return true if this icon state is <code>IconState.NORMAL</code>
     */
    public boolean isNormal() {
        return (this == NORMAL);
    }

    /**
     * Return true if this icon state is <code>IconState.ACTIVE</code>.
     *
     * @return true if this icon state is <code>IconState.ACTIVE</code>
     */
    public boolean isActive() {
        return (this == ACTIVE);
    }

    /**
     * Return true if this icon state is <code>IconState.MOUSEOVER</code>.
     *
     * @return true if this icon state is <code>IconState.MOUSEOVER</code>
     */
    public boolean isMouseover() {
        return (this == MOUSEOVER);
    }

    /**
     * Return true if this icon state is <code>IconState.SELECTED</code>.
     *
     * @return true if this icon state is <code>IconState.SELECTED</code>
     */
    public boolean isSelected() {
        return (this == SELECTED);
    }

    /**
     * Return true if this icon state is <code>IconState.SELECTED_MOUSEOVER</code>.
     *
     * @return true if this icon state is <code>IconState.SELECTED_MOUSEOVER</code>
     */
    public boolean isSelectedMouseover() {
        return (this == SELECTED_MOUSEOVER);
    }

    /**
     * Return true if this icon state is <code>IconState.DRAGGING</code>.
     *
     * @return true if this icon state is <code>IconState.DRAGGING</code>
     */
    public boolean isDragging() {
        return (this == DRAGGING);
    }

    /**
     * Return true if this icon state is <code>IconState.DISABLED</code>.
     *
     * @return true if this icon state is <code>IconState.DISABLED</code>
     */
    public boolean isDisabled() {
        return (this == DISABLED);
    }

    /** Normal icon state. */
    public static final IconState NORMAL = new IconState("normal");

    /** Active icon state. */
    public static final IconState ACTIVE = new IconState("active");

    /** Mouseover icon state. */
    public static final IconState MOUSEOVER = new IconState("mouseover");

    /** Selected icon state. */
    public static final IconState SELECTED = new IconState("selected");

    /** Selected mouseover icon state. */
    public static final IconState SELECTED_MOUSEOVER = new IconState("selected-mouseover");

    /** Dragging icon state. */
    public static final IconState DRAGGING = new IconState("dragging");

    /** Disabled icon state. */
    public static final IconState DISABLED = new IconState("disabled");

    /**
     * Private array of enumeration values.
     */
    private static final IconState[] VALUES_ARRAY = { NORMAL, ACTIVE, MOUSEOVER, SELECTED, SELECTED_MOUSEOVER, DRAGGING, DISABLED };

    /**
     * Public list of enumeration values.
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));
}

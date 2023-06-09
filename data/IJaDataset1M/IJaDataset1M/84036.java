package org.pandcorps.pandam;

import java.util.*;
import org.pandcorps.core.Coltil;
import org.pandcorps.core.col.*;
import org.pandcorps.pandam.Panput.*;
import org.pandcorps.pandam.event.action.*;

public abstract class Panteraction {

    private final Key[] keys;

    private final HashMultimap<Panput, ActionStartListener> startListeners = new HashMultimap<Panput, ActionStartListener>();

    private final HashMultimap<Panput, ActionListener> listeners = new HashMultimap<Panput, ActionListener>();

    private final HashMultimap<Panput, ActionEndListener> endListeners = new HashMultimap<Panput, ActionEndListener>();

    public final Key KEY_ESCAPE;

    public final Key KEY_1;

    public final Key KEY_MINUS;

    public final Key KEY_EQUALS;

    public final Key KEY_BACKSPACE;

    public final Key KEY_TAB;

    public final Key KEY_Q;

    public final Key KEY_BRACKET_LEFT;

    public final Key KEY_BRACKET_RIGHT;

    public final Key KEY_ENTER;

    public final Key KEY_CTRL_LEFT;

    public final Key KEY_A;

    public final Key KEY_SEMICOLON;

    public final Key KEY_APOSTROPHE;

    public final Key KEY_GRAVE;

    public final Key KEY_SHIFT_LEFT;

    public final Key KEY_BACKSLASH;

    public final Key KEY_Z;

    public final Key KEY_COMMA;

    public final Key KEY_PERIOD;

    public final Key KEY_SLASH;

    public final Key KEY_SHIFT_RIGHT;

    public final Key KEY_ALT_LEFT;

    public final Key KEY_SPACE;

    public final Key KEY_CAPS_LOCK;

    public final Key KEY_F1;

    public final Key KEY_CTRL_RIGHT;

    public final Key KEY_ALT_RIGHT;

    public final Key KEY_HOME;

    public final Key KEY_UP;

    public final Key KEY_PG_UP;

    public final Key KEY_LEFT;

    public final Key KEY_RIGHT;

    public final Key KEY_END;

    public final Key KEY_DOWN;

    public final Key KEY_PG_DN;

    public final Key KEY_INS;

    public final Key KEY_DEL;

    public final int IND_1 = 2;

    public final int IND_Q = 16;

    public final int IND_A = 30;

    public final int IND_GRAVE = 41;

    public final int IND_SHIFT_LEFT = 42;

    public final int IND_Z = 44;

    public final int IND_SHIFT_RIGHT = 54;

    public final int IND_SPACE = 57;

    private final IdentityHashMap<Panctor, ActionGroup> actors = new IdentityHashMap<Panctor, ActionGroup>();

    public Panteraction() {
        final int size = 256;
        keys = new Key[size];
        final char[][][] kb = { { { '`' }, { '~' } }, { { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=' }, { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+' } }, { { 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', '[', ']' }, { 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', '{', '}' } }, { { 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', ';', '\'' }, { 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', ':', '"' } }, { { 'z', 'x', 'c', 'v', 'b', 'n', 'm', ',', '.', '/' }, { 'Z', 'X', 'C', 'V', 'B', 'N', 'M', '<', '>', '?' } }, { { ' ' }, { ' ' } }, { { '\\' }, { '|' } } };
        final int indBackslash = getIndexBackslash();
        final int[] offs = { IND_GRAVE, IND_1, IND_Q, IND_A, IND_Z, IND_SPACE, indBackslash };
        for (int i = 0; i < size; i++) {
            char[][] thePair = null;
            int theOff = 0;
            for (int row = 0; row < kb.length; row++) {
                final int off = offs[row];
                final char[][] pair = kb[row];
                if (i >= off && i < off + pair[0].length) {
                    thePair = pair;
                    theOff = off;
                    break;
                }
            }
            final Character bc, sc;
            final boolean l;
            if (thePair == null) {
                bc = null;
                sc = null;
                l = false;
            } else {
                final int ind = i - theOff;
                final char b = thePair[0][ind];
                bc = Character.valueOf(b);
                sc = Character.valueOf(thePair[1][ind]);
                l = Character.isLetter(b);
            }
            keys[i] = new Key(this, i, bc, sc, l);
        }
        KEY_ESCAPE = keys[1];
        KEY_1 = keys[IND_1];
        KEY_MINUS = keys[12];
        KEY_EQUALS = keys[13];
        KEY_BACKSPACE = keys[14];
        KEY_TAB = keys[15];
        KEY_Q = keys[IND_Q];
        KEY_BRACKET_LEFT = keys[26];
        KEY_BRACKET_RIGHT = keys[27];
        KEY_ENTER = keys[28];
        KEY_CTRL_LEFT = keys[29];
        KEY_A = keys[IND_A];
        KEY_SEMICOLON = keys[39];
        KEY_APOSTROPHE = keys[40];
        KEY_GRAVE = keys[IND_GRAVE];
        KEY_SHIFT_LEFT = keys[IND_SHIFT_LEFT];
        KEY_BACKSLASH = keys[indBackslash];
        KEY_Z = keys[IND_Z];
        KEY_COMMA = keys[51];
        KEY_PERIOD = keys[52];
        KEY_SLASH = keys[53];
        KEY_SHIFT_RIGHT = keys[IND_SHIFT_RIGHT];
        KEY_ALT_LEFT = keys[56];
        KEY_SPACE = keys[IND_SPACE];
        KEY_CAPS_LOCK = keys[58];
        KEY_F1 = keys[59];
        KEY_CTRL_RIGHT = keys[157];
        KEY_ALT_RIGHT = keys[184];
        KEY_HOME = keys[199];
        KEY_UP = keys[200];
        KEY_PG_UP = keys[201];
        KEY_LEFT = keys[203];
        KEY_RIGHT = keys[205];
        KEY_END = keys[207];
        KEY_DOWN = keys[208];
        KEY_PG_DN = keys[209];
        KEY_INS = keys[210];
        KEY_DEL = keys[211];
    }

    public boolean isShiftActive() {
        return KEY_SHIFT_LEFT.isActive() || KEY_SHIFT_RIGHT.isActive();
    }

    public boolean isCtrlActive() {
        return KEY_CTRL_LEFT.isActive() || KEY_CTRL_RIGHT.isActive();
    }

    public boolean isAltActive() {
        return KEY_ALT_LEFT.isActive() || KEY_ALT_RIGHT.isActive();
    }

    public abstract boolean isCapsLockEnabled();

    public abstract boolean isInsEnabled();

    public abstract int getKeyCount();

    protected abstract int getIndexBackslash();

    public final Key getKey(final int index) {
        return keys[index];
    }

    public final void inactivateAll() {
        Panput.inactivate(keys);
    }

    public final void register(final Panctor actor, final ActionStartListener listener) {
        register(actor, Panput.any, listener);
    }

    public final void register(final Panctor actor, final Panput input, final ActionStartListener listener) {
        startListeners.add(input, listener);
        if (actor != null) {
            get(actor).add(listener);
        }
    }

    final ActionGroup get(final Panctor actor) {
        ActionGroup g = actors.get(actor);
        if (g == null) {
            g = new ActionGroup();
            actors.put(actor, g);
        }
        return g;
    }

    final void unregister(final Panctor actor) {
        final ActionGroup g = actors.remove(actor);
        if (g != null) {
            g.unregister();
        }
    }

    public final Iterable<ActionStartListener> getStartListeners(final Panput input) {
        return input == Panput.any ? startListeners.get(input) : SequenceIterable.create(startListeners.get(input), startListeners.get(Panput.any));
    }

    public final void unregister(final ActionStartListener listener) {
        unregister(startListeners, listener);
    }

    public final void unregisterAllStart(final Iterable<ActionStartListener> list) {
        unregister(startListeners, list);
    }

    private static final <T> void unregister(final HashMultimap<?, T> map, final Iterable<T> list) {
        for (final T listener : Coltil.unnull(list)) {
            unregister(map, listener);
        }
    }

    private static final <T> void unregister(final HashMultimap<?, T> listeners, final T listener) {
        for (final ArrayList<T> list : listeners.values()) {
            final Iterator<T> iter = list.iterator();
            while (iter.hasNext()) {
                if (listener == iter.next()) {
                    iter.remove();
                }
            }
        }
    }

    public final void register(final Panctor actor, final Panput input, final ActionListener listener) {
        listeners.add(input, listener);
        if (actor != null) {
            get(actor).add(listener);
        }
    }

    public final Iterable<ActionListener> getListeners(final Panput input) {
        return listeners.get(input);
    }

    public final void unregister(final ActionListener listener) {
        unregister(listeners, listener);
    }

    public final void unregisterAll(final Iterable<ActionListener> list) {
        unregister(listeners, list);
    }

    public final void register(final Panctor actor, final Panput input, final ActionEndListener listener) {
        endListeners.add(input, listener);
        if (actor != null) {
            get(actor).add(listener);
        }
    }

    public final Iterable<ActionEndListener> getEndListeners(final Panput input) {
        return endListeners.get(input);
    }

    public final void unregister(final ActionEndListener listener) {
        unregister(endListeners, listener);
    }

    public final void unregisterAllEnd(final Iterable<ActionEndListener> list) {
        unregister(endListeners, list);
    }

    public final void unregisterAll() {
        startListeners.clear();
        listeners.clear();
        endListeners.clear();
    }
}

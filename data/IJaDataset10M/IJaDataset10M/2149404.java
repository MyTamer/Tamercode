package com.sun.midp.lcdui;

import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;

/**
 * These are the methods called by the default event handler.
 */
public interface DisplayEvents {

    /**
     * Called by event delivery when an abstract Command is fired.
     * The parameter is an index into the list of Commands that are
     * current, i.e. those associated with the visible Screen.
     * @param id The integer id of the Command that fired (as returned
     *           by Command.getID())
     */
    void commandAction(int id);

    /**
     * Called by event delivery when a pen event is processed.
     * The type is one of EventHandler.PRESSED, EventHandler.RELEASED,
     * or EventHandler.DRAGGED.
     * @param type The type of event (press, release or drag)
     * @param x The x coordinate of the location of the pen     
     * @param y The y coordinate of the location of the pen     
     */
    void pointerEvent(int type, int x, int y);

    /**
     * Called by event delivery when a key event is processed.
     * The type is one of EventHandler.PRESSED, EventHandler.RELEASED,
     * or EventHandler.REPEATED.
     * @param type The type of event (press, release or repeat)
     * @param keyCode The key code for the key that registered the event
     */
    void keyEvent(int type, int keyCode);

    /**
     * Called by event delivery when a repaint should occur
     *
     * @param x1 The origin x coordinate of the repaint region
     * @param y1 The origin y coordinate of the repaint region
     * @param x2 The bounding x coordinate of the repaint region
     * @param y2 The bounding y coordinate of the repaint region
     * @param target The optional paint target
     */
    void repaint(int x1, int y1, int x2, int y2, Object target);

    /**
     * Called by event delivery to batch process all pending serial
     * callbacks
     */
    void callSerially();

    /**
     * Called by event delivery to process a Form invalidation
     *
     * @param src the Item which may have caused the invalidation
     */
    void callInvalidate(Item src);

    /**
     * Called by event delivery to notify an ItemStateChangeListener
     * of a change in an item
     *
     * @param src the Item which has changed
     */
    void callItemStateChanged(Item src);

    /**
     * Called when the system needs to temporarily prevent the application
     * from painting the screen.  The primary use of this method is to allow
     * a system service to temporarily utilize the screen, e.g. to provide
     * input method or abstract command processing.
     *
     * This method should prevent application-based paints (i.e. those
     * generated by Canvas.repaint(), Canvas.serviceRepaints() or some
     * internal paint method on another kind of Displayable) from changing
     * the contents of the screen in any way.
     */
    void suspendPainting();

    /**
     * Called when the system is ready to give up its control over the
     * screen.  The application should receive a request for a full
     * repaint when this is called, and is subsequently free to process
     * paint events from Canvas.repaint(), Canvas.serviceRepaints() or
     * internal paint methods on Displayable.
     */
    void resumePainting();

    /**
     * Called by event delivery when an input method event is processed.
     * @param str The string from the input method.
     */
    void inputMethodEvent(String str);
}

package de.enough.polish.blackberry.ui;

import net.rim.device.api.ui.container.FullScreen;
import de.enough.polish.ui.Ticker;

/**
 * An object that has the capability of being placed on the display.
 * 
 * An object that has the capability of being placed on the display.  A
 * <code>Displayable</code> object may have a title, a ticker,
 * zero or more commands and a listener associated with it.  The
 * contents displayed and their interaction with the user are defined by
 * subclasses.
 * 
 * <p>The title string may contain
 * <A HREF="Form.html#linebreak">line breaks</a>.
 * The display of the title string must break accordingly.
 * For example, if only a single line is available for a
 * title and the string contains a line break then only the characters
 * up to the line break are displayed.</p>
 * 
 * <p>Unless otherwise specified by a subclass, the default state of newly
 * created <code>Displayable</code> objects is as follows:</p>
 * 
 * <ul>
 * <li>it is not visible on the <code>Display</code>;</li>
 * <li>there is no <code>Ticker</code> associated with this
 * <code>Displayable</code>;</li>
 * <li>the title is <code>null</code>;</li>
 * <li>there are no <code>Commands</code> present; and</li>
 * <li>there is no <code>CommandListener</code> present.</li>
 * </ul>
 * <HR>
 * 
 * 
 * @since MIDP 1.0
 */
public abstract class Displayable extends FullScreen {

    private String title;

    private Ticker ticker;

    private CommandListener commandListener;

    protected boolean isShown;

    /**
	 * Gets the title of the <code>Displayable</code>. 
	 * Returns
	 * <code>null</code> if there is no title.
	 * 
	 * @return the title of the instance, or null if no title
	 * @see #setTitle(java.lang.String)
	 * @since  MIDP 2.0
	 */
    public String getTitle() {
        return this.title;
    }

    /**
	 * Sets the title of the <code>Displayable</code>. If
	 * <code>null</code> is given,
	 * removes the title.
	 * 
	 * <P>If the <code>Displayable</code> is actually visible on
	 * the display,
	 * the implementation should update
	 * the display as soon as it is feasible to do so.</P>
	 * 
	 * <P>The existence of a title  may affect the size
	 * of the area available for <code>Displayable</code> content.
	 * Addition, removal, or the setting of the title text at runtime
	 * may dynamically change the size of the content area.
	 * This is most important to be aware of when using the
	 * <code>Canvas</code> class.
	 * If the available area does change, the application will be notified
	 * via a call to <A HREF="../../../javax/microedition/lcdui/Displayable.html#sizeChanged(int, int)"><CODE>sizeChanged()</CODE></A>. </p>
	 * 
	 * @param s - the new title, or null for no title
	 * @see #getTitle()
	 * @since  MIDP 2.0
	 */
    public void setTitle(String s) {
        this.title = s;
    }

    /**
	 * Gets the ticker used by this <code>Displayable</code>.
	 * 
	 * @return ticker object used, or null if no ticker is present
	 * @see #setTicker(Ticker)
	 * @since  MIDP 2.0
	 */
    public Ticker getTicker() {
        return this.ticker;
    }

    /**
	 * Sets a ticker for use with this <code>Displayable</code>,
	 * replacing any
	 * previous ticker.
	 * If <code>null</code>, removes the ticker object
	 * from this <code>Displayable</code>. The same ticker may be shared by
	 * several <code>Displayable</code>
	 * objects within an application. This is done by calling
	 * <code>setTicker()</code>
	 * with the same <code>Ticker</code> object on several
	 * different <code>Displayable</code> objects.
	 * If the <code>Displayable</code> is actually visible on the display,
	 * the implementation should update
	 * the display as soon as it is feasible to do so.
	 * 
	 * <p>The existence of a ticker may affect the size
	 * of the area available for <code>Displayable's</code> contents.
	 * Addition, removal, or the setting of the ticker at runtime
	 * may dynamically change the size of the content area.
	 * This is most important to be aware of when using the
	 * <code>Canvas</code> class.
	 * If the available area does change, the application will be notified
	 * via a call to <A HREF="../../../javax/microedition/lcdui/Displayable.html#sizeChanged(int, int)"><CODE>sizeChanged()</CODE></A>. </p>
	 * 
	 * @param ticker - the ticker object used on this screen
	 * @see #getTicker()
	 * @since  MIDP 2.0
	 */
    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    /**
	 * Checks if the <code>Displayable</code> is actually visible
	 * on the display.  In order
	 * for a <code>Displayable</code> to be visible, all of the
	 * following must be true:
	 * the <code>Display's</code> <code>MIDlet</code> must be
	 * running in the foreground, the <code>Displayable</code>
	 * must be the <code>Display's</code> current screen, and the
	 * <code>Displayable</code> must not be
	 * obscured by a <a href="Display.html#systemscreens">
	 * system screen</a>.
	 * 
	 * @return true if the Displayable is currently visible
	 */
    public boolean isShown() {
        return this.isShown;
    }

    /**
	 * Adds a command to the <code>Displayable</code>. The
	 * implementation may choose,
	 * for example,
	 * to add the command to any of the available soft buttons or place it
	 * in a menu.
	 * If the added command is already in the screen (tested by comparing the
	 * object references), the method has no effect.
	 * If the <code>Displayable</code> is actually visible on the
	 * display, and this call
	 * affects the set of visible commands, the implementation should update
	 * the display as soon as it is feasible to do so.
	 * 
	 * @param cmd - the command to be added
	 * @throws NullPointerException - if cmd is null
	 */
    public void addCommand(Command cmd) {
    }

    /**
	 * Removes a command from the <code>Displayable</code>.
	 * If the command is not in the <code>Displayable</code>
	 * (tested by comparing the
	 * object references), the method has no effect.
	 * If the <code>Displayable</code> is actually visible on the
	 * display, and this call
	 * affects the set of visible commands, the implementation should update
	 * the display as soon as it is feasible to do so.
	 * If <code>cmd</code> is <code>null</code>, this method
	 * does nothing.
	 * 
	 * @param cmd - the command to be removed
	 */
    public void removeCommand(Command cmd) {
    }

    /**
	 * Sets a listener for <A HREF="../../../javax/microedition/lcdui/Command.html"><CODE>Commands</CODE></A> to this
	 * <code>Displayable</code>,
	 * replacing any previous <code>CommandListener</code>. A
	 * <code>null</code> reference is
	 * allowed and has the effect of removing any existing listener.
	 * 
	 * @param l - the new listener, or null.
	 */
    public void setCommandListener(CommandListener l) {
        this.commandListener = l;
    }

    /**
	 * The implementation calls this method when the available area of the
	 * <code>Displayable</code> has been changed.
	 * The &quot;available area&quot; is the area of the display that
	 * may be occupied by
	 * the application's contents, such as <code>Items</code> in a
	 * <code>Form</code> or graphics within
	 * a <code>Canvas</code>.  It does not include space occupied
	 * by a title, a ticker,
	 * command labels, scroll bars, system status area, etc.  A size change
	 * can occur as a result of the addition, removal, or changed contents of
	 * any of these display features.
	 * 
	 * <p> This method is called at least once before the
	 * <code>Displayable</code> is shown for the first time.
	 * If the size of a <code>Displayable</code> changes while
	 * it is visible,
	 * <CODE>sizeChanged</CODE> will be called.  If the size of a
	 * <code>Displayable</code>
	 * changes while it is <em>not</em> visible, calls to
	 * <CODE>sizeChanged</CODE> may be deferred.  If the size had changed
	 * while the <code>Displayable</code> was not visible,
	 * <CODE>sizeChanged</CODE> will be
	 * called at least once at the time the
	 * <code>Displayable</code> becomes visible once
	 * again.</p>
	 * 
	 * <p>The default implementation of this method in <code>Displayable</code>
	 * and its
	 * subclasses defined in this specification must be empty.
	 * This method is intended solely for being overridden by the
	 * application. This method is defined on <code>Displayable</code>
	 * even though applications are prohibited from creating
	 * direct subclasses of <code>Displayable</code>.
	 * It is defined here so that applications can override it in
	 * subclasses of <code>Canvas</code> and <code>Form</code>.
	 * This is useful for <code>Canvas</code> subclasses to tailor
	 * their graphics and for <code>Forms</code> to modify
	 * <code>Item</code> sizes and layout
	 * directives in order to fit their contents within the the available
	 * display area.</p>
	 * 
	 * @param w - the new width in pixels of the available area
	 * @param h - the new height in pixels of the available area
	 * @since  MIDP 2.0
	 */
    protected void sizeChanged(int w, int h) {
    }
}

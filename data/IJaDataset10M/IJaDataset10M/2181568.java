package net.sourceforge.jcomplete.ui;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.SwingUtilities;
import java.awt.Component;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;

/**
 *  Popup manager allows to display an arbitrary popup component
 *  over the underlying text component.
 *
 *  @author  Martin Roskanin, Miloslav Metelka
 *  @since   03/2002
 */
public class PopupManager {

    private JComponent popup = null;

    private JTextComponent textComponent;

    /** Place popup always above cursor */
    public static final Placement Above = new Placement("Above");

    /** Place popup always below cursor */
    public static final Placement Below = new Placement("Below");

    /** Place popup to larger area. i.e. if place below cursor is 
        larger than place above, then popup will be placed below cursor. */
    public static final Placement Largest = new Placement("Largest");

    /** Place popup above cursor. If a place above cursor is insufficient, 
        then popup will be placed below cursor. */
    public static final Placement AbovePreferred = new Placement("AbovePreferred");

    /** Place popup below cursor. If a place below cursor is insufficient, 
        then popup will be placed above cursor. */
    public static final Placement BelowPreferred = new Placement("BelowPreferred");

    private KeyListener keyListener;

    private TextComponentListener componentListener;

    /** Creates a new instance of PopupManager */
    public PopupManager(JTextComponent textComponent) {
        this.textComponent = textComponent;
        keyListener = new PopupKeyListener();
        textComponent.addKeyListener(keyListener);
        componentListener = new TextComponentListener();
        textComponent.addComponentListener(componentListener);
    }

    /** Install popup component to textComponent root pane
     *  based on caret coordinates with the <CODE>Largest</CODE> placement.
     *  @param popup popup component to be installed into
     *  root pane of the text component.
     */
    public void install(JComponent popup) {
        int caretPos = textComponent.getCaret().getDot();
        try {
            Rectangle caretBounds = textComponent.modelToView(caretPos);
            install(popup, caretBounds, Largest);
        } catch (BadLocationException e) {
        }
    }

    public void install(JComponent popup, Rectangle cursorBounds, Placement placement) {
        this.popup = popup;
        Rectangle bounds = computeBounds(this.popup, textComponent, cursorBounds, placement);
        if (bounds != null) {
            bounds = SwingUtilities.convertRectangle(textComponent, bounds, textComponent.getRootPane().getLayeredPane());
            this.popup.setBounds(bounds);
        } else {
            this.popup.setVisible(false);
        }
        if (this.popup != null) {
            removeFromRootPane(this.popup);
        }
        if (this.popup != null) {
            installToRootPane(this.popup);
        }
    }

    /** Returns installed popup panel component */
    public JComponent get() {
        return popup;
    }

    /** Install popup panel to current textComponent root pane */
    private void installToRootPane(JComponent c) {
        JRootPane rp = textComponent.getRootPane();
        if (rp != null) {
            rp.getLayeredPane().add(c, JLayeredPane.POPUP_LAYER, 0);
        }
    }

    /** Remove popup panel from previous textComponent root pane */
    private void removeFromRootPane(JComponent c) {
        JRootPane rp = c.getRootPane();
        if (rp != null) {
            rp.getLayeredPane().remove(c);
        }
    }

    /** Variation of the method for computing the bounds
     * for the concrete view component. As the component can possibly
     * be placed in a scroll pane it's first necessary
     * to translate the cursor bounds and also translate
     * back the resulting popup bounds.
     * @param popup  popup panel to be displayed
     * @param view component over which the popup is displayed.
     * @param cursorBounds the bounds of the caret or mouse cursor
     *    relative to the upper-left corner of the visible view.
     * @param placement where to place the popup panel according to
     *    the cursor position.
     * @return bounds of popup panel relative to the upper-left corner
     *    of the underlying view component.
     *    <CODE>null</CODE> if there is no place to display popup.
     */
    protected static Rectangle computeBounds(JComponent popup, JComponent view, Rectangle cursorBounds, Placement placement) {
        Rectangle ret;
        Component viewParent = view.getParent();
        if (viewParent instanceof JViewport) {
            Rectangle viewBounds = ((JViewport) viewParent).getViewRect();
            Rectangle translatedCursorBounds = (Rectangle) cursorBounds.clone();
            translatedCursorBounds.translate(-viewBounds.x, -viewBounds.y);
            ret = computeBounds(popup, viewBounds.width, viewBounds.height, translatedCursorBounds, placement);
            if (ret != null) {
                ret.translate(viewBounds.x, viewBounds.y);
            }
        } else {
            ret = computeBounds(popup, view.getWidth(), view.getHeight(), cursorBounds, placement);
        }
        return ret;
    }

    /** Computes a best-fit bounds of popup panel
     *  according to available space in the underlying view
     *  (visible part of the pane).
     *  The placement is first evaluated and put into the popup's client property
     *  by <CODE>popup.putClientProperty(Placement.class, actual-placement)</CODE>.
     *  The actual placement is <UL>
     *  <LI> <CODE>Above</CODE> if the original placement was <CODE>Above</CODE>.
     *  Or if the original placement was <CODE>AbovePreferred</CODE>
     *  or <CODE>Largest</CODE>
     *  and there is more space above the cursor than below it.
     *  <LI> <CODE>Below</CODE> if the original placement was <CODE>Below</CODE>.
     *  Or if the original placement was <CODE>BelowPreferred</CODE>
     *  or <CODE>Largest</CODE>
     *  and there is more space below the cursor than above it.
     *  <LI> <CODE>AbovePreferred</CODE> if the original placement
     *  was <CODE>AbovePreferred</CODE>
     *  and there is less space above the cursor than below it.
     *  <LI> <CODE>BelowPreferred</CODE> if the original placement
     *  was <CODE>BelowPreferred</CODE>
     *  and there is less space below the cursor than above it.
     *  <P>Once the placement client property is set
     *  the <CODE>popup.setSize()</CODE> is called with the size of the area
     *  above/below the cursor (indicated by the placement).
     *  The popup responds by updating its size to the equal or smaller
     *  size. If it cannot physically fit into the requested area
     *  it can call
     *  <CODE>putClientProperty(Placement.class, null)</CODE>
     *  on itself to indicate that it cannot fit. The method scans
     *  the content of the client property upon return from
     *  <CODE>popup.setSize()</CODE> and if it finds null there it returns
     *  null bounds in that case. The only exception is
     *  if the placement was either <CODE>AbovePreferred</CODE>
     *  or <CODE>BelowPreferred</CODE>. In that case the method
     *  gives it one more try
     *  by attempting to fit the popup into (bigger) complementary
     *  <CODE>Below</CODE> and <CODE>Above</CODE> areas (respectively).
     *  The popup either fits into these (bigger) areas or it again responds
     *  by returning <CODE>null</CODE> in the client property in which case
     *  the method finally gives up and returns null bounds.
     *   
     *  @param popup popup panel to be displayed
     *  @param viewWidth width of the visible view area.
     *  @param viewHeight height of the visible view area.
     *  @param cursorBounds the bounds of the caret or mouse cursor
     *    relative to the upper-left corner of the visible view
     *  @param placement where to place the popup panel according to
     *    the cursor position
     *  @return bounds of popup panel relative to the upper-left corner
     *    of the underlying view.
     *    <CODE>null</CODE> if there is no place to display popup.
     */
    protected static Rectangle computeBounds(JComponent popup, int viewWidth, int viewHeight, Rectangle cursorBounds, Placement placement) {
        if (placement == null) {
            throw new NullPointerException("placement cannot be null");
        }
        int aboveCursorHeight = cursorBounds.y;
        int belowCursorY = cursorBounds.y + cursorBounds.height;
        int belowCursorHeight = viewHeight - belowCursorY;
        if (placement == Largest) {
            placement = (aboveCursorHeight < belowCursorHeight) ? Below : Above;
        } else if (placement == AbovePreferred && aboveCursorHeight > belowCursorHeight) {
            placement = Above;
        } else if (placement == BelowPreferred && belowCursorHeight > aboveCursorHeight) {
            placement = Below;
        }
        Rectangle popupBounds = null;
        while (true) {
            popup.putClientProperty(Placement.class, placement);
            int height = (placement == Above || placement == AbovePreferred) ? aboveCursorHeight : belowCursorHeight;
            popup.setSize(viewWidth, height);
            popupBounds = popup.getBounds();
            Placement updatedPlacement = (Placement) popup.getClientProperty(Placement.class);
            if (updatedPlacement != placement) {
                if (placement == AbovePreferred && updatedPlacement == null) {
                    placement = Below;
                    continue;
                } else if (placement == BelowPreferred && updatedPlacement == null) {
                    placement = Above;
                    continue;
                }
            }
            if (updatedPlacement == null) {
                popupBounds = null;
            }
            break;
        }
        if (popupBounds != null) {
            popupBounds.x = Math.min(cursorBounds.x, viewWidth - popupBounds.width);
            popupBounds.y = (placement == Above || placement == AbovePreferred) ? (aboveCursorHeight - popupBounds.height) : belowCursorY;
        }
        return popupBounds;
    }

    /** Popup's key filter */
    private class PopupKeyListener implements KeyListener {

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }

        public void keyPressed(KeyEvent e) {
            if (e == null) return;
            if (popup != null && popup.isShowing()) {
                ActionMap am = popup.getActionMap();
                InputMap im = popup.getInputMap();
                Object obj = im.get(KeyStroke.getKeyStrokeForEvent(e));
                if (obj != null) {
                    Action action = am.get(obj);
                    if (action != null) {
                        action.actionPerformed(null);
                        e.consume();
                    }
                }
            }
        }
    }

    private final class TextComponentListener extends ComponentAdapter {

        public void componentHidden(ComponentEvent evt) {
            install(null);
        }
    }

    /** Placement of popup panel specification */
    public static final class Placement {

        private final String representation;

        private Placement(String representation) {
            this.representation = representation;
        }

        public String toString() {
            return representation;
        }
    }
}

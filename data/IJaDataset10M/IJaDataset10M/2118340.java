package gov.nasa.worldwindx.examples.util;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.pick.*;
import gov.nasa.worldwind.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Controller to forward selection, keyboard, and mouse events on the World Window to the active {@link
 * gov.nasa.worldwind.util.HotSpot}. The active HotSpot is updated on {@link gov.nasa.worldwind.event.SelectEvent#ROLLOVER}
 * select events, but not during a drag operation. This ensures that the active HotSpot remains active while it's being
 * dragged, regardless of what's under the cursor.
 * <p/>
 * The active HotSpot is updated during non-drag rollover select events as follows: <ul> <li>The select event's top
 * picked object, if the top picked object implements {@link gov.nasa.worldwind.util.HotSpot}.</li> <li>The value for
 * {@code SelectEvent.getTopPickedObject().getValue(AVKey.HOT_SPOT)},if the value for key {@link
 * gov.nasa.worldwind.avlist.AVKey#HOT_SPOT} implements HotSpot.</li> <li>{@code null} if neither of the above
 * conditions are true, or if the event is {@code null}.</li> </ul>
 *
 * @author pabercrombie
 * @version $Id: HotSpotController.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class HotSpotController implements SelectListener, MouseMotionListener {

    protected WorldWindow wwd;

    protected HotSpot activeHotSpot;

    protected boolean dragging = false;

    /** Indicates that the active HotSpot has set a custom cursor that must be reset when the HotSpot is deactivated. */
    protected boolean customCursor;

    /**
     * Creates a new HotSpotController for a specified World Window, and assigns the controller as a {@link
     * gov.nasa.worldwind.event.SelectListener} on the World Window.
     *
     * @param wwd The World Window to monitor selection events for.
     */
    public HotSpotController(WorldWindow wwd) {
        if (wwd == null) {
            String message = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.wwd = wwd;
        this.wwd.addSelectListener(this);
        this.wwd.getInputHandler().addMouseMotionListener(this);
    }

    /**
     * Updates the active {@link gov.nasa.worldwind.util.HotSpot} if necessary, and forwards the select event to the
     * active HotSpot. This does nothing if the select event is {@code null}.
     * <p/>
     * This forwards the select event to {@link #doSelected(gov.nasa.worldwind.event.SelectEvent)}, and catches and logs
     * any exceptions thrown by {@code doSelected}.
     *
     * @param event A select event on the World Window we're monitoring.
     */
    public void selected(SelectEvent event) {
        if (event == null) return;
        try {
            this.doSelected(event);
        } catch (Exception e) {
            Logging.logger().warning(e.getMessage() != null ? e.getMessage() : e.toString());
        }
    }

    /**
     * Updates the active {@link gov.nasa.worldwind.util.HotSpot} depending on the specified select event action: <ul>
     * <li>{@link gov.nasa.worldwind.event.SelectEvent#DRAG_END} - Forwards the event to the active {@link
     * gov.nasa.worldwind.util.HotSpot}, then updates the active HotSpot.</li> <li>{@link
     * gov.nasa.worldwind.event.SelectEvent#ROLLOVER} while not dragging - Updates the active HotSpot, then forwards the
     * event to the active HotSpot.</li> <li>Other event types - forwards the event to the active HotSpot</li> </ul>
     *
     * @param event A select event on the World Window we're monitoring.
     */
    protected void doSelected(SelectEvent event) {
        HotSpot activeHotSpot = this.getActiveHotSpot();
        if (event.isDragEnd()) {
            if (activeHotSpot != null) activeHotSpot.selected(event);
            this.setDragging(false);
            PickedObjectList list = this.wwd.getObjectsAtCurrentPosition();
            PickedObject po = list != null ? list.getTopPickedObject() : null;
            this.updateActiveHotSpot(po);
        } else if (!this.isDragging() && event.isRollover()) {
            PickedObject po = event.getTopPickedObject();
            this.updateActiveHotSpot(po);
        }
        if (activeHotSpot != null) {
            if (event.isDrag()) {
                boolean wasConsumed = event.isConsumed();
                activeHotSpot.selected(event);
                this.setDragging(event.isConsumed() && !wasConsumed);
            } else if (!event.isDragEnd()) {
                activeHotSpot.selected(event);
            }
        }
    }

    /**
     * Update the cursor when the mouse moves.
     *
     * @param e Mouse event.
     */
    public void mouseMoved(MouseEvent e) {
        HotSpot hotSpot = this.getActiveHotSpot();
        if (hotSpot != null) {
            Cursor cursor = hotSpot.getCursor();
            if (cursor != null) {
                ((Component) this.wwd).setCursor(cursor);
                this.customCursor = true;
            }
        }
    }

    /**
     * Returns whether the user is dragging the object under the cursor.
     *
     * @return {@code true} if the user is dragging the object under the cursor, otherwise {@code false}.
     */
    protected boolean isDragging() {
        return this.dragging;
    }

    /**
     * Specifies whether the user is dragging the object under the cursor.
     *
     * @param dragging {@code true} if the user is dragging the object under the cursor, otherwise {@code false}.
     */
    protected void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    /**
     * Returns the currently active {@link gov.nasa.worldwind.util.HotSpot}, or {@code null} if there is no active
     * HotSpot.
     *
     * @return The currently active HotSpot, or {@code null}.
     */
    protected HotSpot getActiveHotSpot() {
        return this.activeHotSpot;
    }

    /**
     * Sets the active {@link gov.nasa.worldwind.util.HotSpot} to the specified HotSpot. The HotSpot may be {@code
     * null}, indicating that there is no active HotSpot. This registers the new HotSpot as key listener, mouse
     * listener, mouse motion listener, and mouse wheel listener on the World Window's {@link
     * gov.nasa.worldwind.event.InputHandler}. This removes the previously active HotSpot as a listener on the World
     * Window's InputHandler. This does nothing if the active HotSpot and the specified HotSpot are the same object.
     * </p> Additionally, this updates the World Window's {@link java.awt.Cursor} to the value returned by {@code
     * hotSpot.getCursor()}, or {@code null} if the specified hotSpot is {@code null}.
     *
     * @param hotSpot The HotSpot that becomes the active HotSpot. {@code null} to indicate that there is no active
     *                HotSpot.
     */
    protected void setActiveHotSpot(HotSpot hotSpot) {
        if (this.wwd instanceof Component) {
            if (this.activeHotSpot != hotSpot && this.customCursor) {
                ((Component) this.wwd).setCursor(null);
                this.customCursor = false;
            }
            if (hotSpot != null) {
                Cursor cursor = hotSpot.getCursor();
                if (cursor != null) {
                    ((Component) this.wwd).setCursor(cursor);
                    this.customCursor = true;
                }
            }
        }
        if (this.activeHotSpot == hotSpot) return;
        if (this.activeHotSpot != null) {
            this.wwd.getInputHandler().removeKeyListener(this.activeHotSpot);
            this.wwd.getInputHandler().removeMouseListener(this.activeHotSpot);
            this.wwd.getInputHandler().removeMouseMotionListener(this.activeHotSpot);
            this.wwd.getInputHandler().removeMouseWheelListener(this.activeHotSpot);
            this.activeHotSpot.setActive(false);
        }
        this.activeHotSpot = hotSpot;
        if (this.activeHotSpot != null) {
            this.activeHotSpot.setActive(true);
            this.wwd.getInputHandler().addKeyListener(this.activeHotSpot);
            this.wwd.getInputHandler().addMouseListener(this.activeHotSpot);
            this.wwd.getInputHandler().addMouseMotionListener(this.activeHotSpot);
            this.wwd.getInputHandler().addMouseWheelListener(this.activeHotSpot);
        }
    }

    /**
     * Updates the active {@link gov.nasa.worldwind.util.HotSpot} and the currently displayed cursor according to the
     * picked objects in the specified event. The active HotSpot is assigned as follows: <ul> <li>The value for {@code
     * event.getTopPickedObject().getValue(AVKey.HOT_SPOT)}, if the value for the key {@link
     * gov.nasa.worldwind.avlist.AVKey#HOT_SPOT} implements HotSpot.</li> <li>The event's top picked object, if the top
     * picked object implements HotSpot.</li> <li>{@code null} if neither of the above conditions are true, or if the
     * event is {@code null}.</li> </ul>
     *
     * @param po Top picked object, which will provide the active HotSpot.
     */
    protected void updateActiveHotSpot(PickedObject po) {
        if (po != null && po.getValue(AVKey.HOT_SPOT) instanceof HotSpot) {
            this.setActiveHotSpot((HotSpot) po.getValue(AVKey.HOT_SPOT));
        } else if (po != null && po.getObject() instanceof HotSpot) {
            this.setActiveHotSpot((HotSpot) po.getObject());
        } else {
            this.setActiveHotSpot(null);
        }
    }

    /** {@inheritDoc} */
    public void mouseDragged(MouseEvent e) {
    }
}

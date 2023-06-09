package org.apache.batik.dom.svg12;

import org.apache.batik.dom.events.DOMUIEvent;
import org.w3c.dom.views.AbstractView;

/**
 * SVG 1.2 mouse wheel event.
 *
 * @author <a href="mailto:cam%40mcc%2eid%2eau">Cameron McCormack</a>
 * @version $Id: SVGOMWheelEvent.java 475477 2006-11-15 22:44:28Z cam $
 */
public class SVGOMWheelEvent extends DOMUIEvent {

    /**
     * The number of clicks the mouse wheel has been moved.
     */
    protected int wheelDelta;

    /**
     * Returns the number of mouse wheel clicks.
     */
    public int getWheelDelta() {
        return wheelDelta;
    }

    /**
     * Initializes this SVGOMWheelEvent object.
     * @param typeArg Specifies the event type.
     * @param canBubbleArg Specifies whether or not the event can bubble.
     * @param cancelableArg Specifies whether or not the event's default action 
     *   can be prevented.
     * @param viewArg Specifies the <code>Event</code>'s 
     *   <code>AbstractView</code>.
     * @param wheelDeltaArg Specifices the number of clicks the mouse wheel has
     *   been moved.
     */
    public void initWheelEvent(String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int wheelDeltaArg) {
        initUIEvent(typeArg, canBubbleArg, cancelableArg, viewArg, 0);
        wheelDelta = wheelDeltaArg;
    }

    /**
     * Initializes this KeyboardEvent object.
     * @param namespaceURIArg Specifies the event namespace URI.
     * @param typeArg Specifies the event type.
     * @param canBubbleArg Specifies whether or not the event can bubble.
     * @param cancelableArg Specifies whether or not the event's default action 
     *   can be prevented.
     * @param viewArg Specifies the <code>Event</code>'s 
     *   <code>AbstractView</code>.
     * @param wheelDeltaArg Specifices the number of clicks the mouse wheel has
     *   been moved.
     */
    public void initWheelEventNS(String namespaceURIArg, String typeArg, boolean canBubbleArg, boolean cancelableArg, AbstractView viewArg, int wheelDeltaArg) {
        initUIEventNS(namespaceURIArg, typeArg, canBubbleArg, cancelableArg, viewArg, 0);
        wheelDelta = wheelDeltaArg;
    }
}

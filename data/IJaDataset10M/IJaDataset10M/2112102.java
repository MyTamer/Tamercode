package com.calclab.emite.core.client.xmpp.stanzas;

import com.calclab.emite.core.client.packet.IPacket;

/**
 * A Presence stanza
 */
public class Presence extends BasicStanza {

    /**
     * @see http://www.xmpp.org/rfcs/rfc3921.html#stanzas
     * 
     *      2.2.2.1. Show
     * 
     *      <p>
     *      If no 'show' element is provided, the entity is assumed to be online
     *      and available.
     *      </p>
     * 
     *      <p>
     *      If provided, the XML character data value MUST be one of the
     *      following (additional availability types could be defined through a
     *      properly-namespaced child element of the presence stanza):
     *      </p>
     */
    public static enum Show {

        away, chat, dnd, xa, notSpecified, unknown
    }

    public enum Type {

        /**
	 * error -- An error has occurred regarding processing or delivery of a
	 * previously-sent presence stanza.
	 */
        error, /**
	 * probe -- A request for an entity's current presence; SHOULD be
	 * generated only by a server on behalf of a user.
	 */
        probe, /**
	 * subscribe -- The sender wishes to subscribe to the recipient's
	 * presence.
	 */
        subscribe, /**
	 * subscribed -- The sender has allowed the recipient to receive their
	 * presence.
	 */
        subscribed, /**
	 * unavailable -- Events that the entity is no longer available for
	 * communication.
	 */
        unavailable, /**
	 * unsubscribe -- The sender is unsubscribing from another entity's
	 * presence.
	 */
        unsubscribe, /**
	 * unsubscribed -- The subscription request has been denied or a
	 * previously-granted subscription has been cancelled.
	 */
        unsubscribed
    }

    /**
     * Create a new presence with the given status message and the given Show
     * 
     * @param statusMessage
     *            the given status message if not null
     * @param show
     *            the show or Show.notSpecified if null
     * @return
     */
    public static Presence build(final String statusMessage, final Show show) {
        final Presence presence = new Presence();
        if (show != null && show != Show.notSpecified) {
            presence.setShow(show);
        }
        if (statusMessage != null) {
            presence.setStatus(statusMessage);
        }
        return presence;
    }

    public Presence() {
        this(null, null, null);
    }

    public Presence(final IPacket stanza) {
        super(stanza);
    }

    public Presence(final Type type, final XmppURI from, final XmppURI to) {
        super("presence", null);
        if (type != null) {
            setType(type.toString());
        }
        setFrom(from);
        setTo(to);
    }

    public Presence(final XmppURI from) {
        this(null, from, null);
    }

    /**
     * Get the priority of the presence
     * 
     * @return The priority (1-10), 0 if not specified
     */
    public int getPriority() {
        int value = 0;
        final String priority = getFirstChild("priority").getText();
        if (priority != null) {
            try {
                value = Integer.parseInt(priority);
            } catch (final NumberFormatException e) {
                value = 0;
            }
        }
        return value;
    }

    /**
     * Return the show of the presence
     * 
     * @return The show, never null
     */
    public Show getShow() {
        final String value = getFirstChild("show").getText();
        try {
            return value != null ? Show.valueOf(value) : Show.notSpecified;
        } catch (final IllegalArgumentException e) {
            return Show.unknown;
        }
    }

    /**
     * Return the status of the presence.
     * 
     * @return The status, null if not specified
     */
    public String getStatus() {
        return getFirstChild("status").getText();
    }

    /**
     * Get the presence's type. If null returned, available (state) is supposed
     * 
     * @return The type, can return null (means available)
     * @see http://www.xmpp.org/rfcs/rfc3921.html#presence
     */
    public Type getType() {
        final String type = getAttribute(BasicStanza.TYPE);
        try {
            return type != null ? Type.valueOf(type) : null;
        } catch (final IllegalArgumentException e) {
            return Type.error;
        }
    }

    public void setPriority(final int value) {
        setTextToChild("priority", Integer.toString(value >= 0 ? value : 0));
    }

    public void setShow(final Show value) {
        setTextToChild("show", (value != null && (value != Show.notSpecified || value != Show.unknown)) ? value.toString() : null);
    }

    public void setStatus(final String statusMessage) {
        setTextToChild("status", statusMessage);
    }

    public void setType(final Type type) {
        setType(type.toString());
    }

    public Presence With(final Show value) {
        setShow(value);
        return this;
    }
}

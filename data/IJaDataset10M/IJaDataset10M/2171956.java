package net.sf.jradius.server;

import java.io.PrintStream;
import net.sf.jradius.exception.RadiusException;
import net.sf.jradius.packet.AccountingRequest;
import net.sf.jradius.packet.RadiusPacket;
import net.sf.jradius.packet.attribute.AttributeList;
import net.sf.jradius.packet.attribute.RadiusAttribute;
import net.sf.jradius.server.config.Configuration;
import net.sf.jradius.session.JRadiusSession;

/**
 * An abstract class representing a JRadius Server Request.
 * 
 * @author David Bird
 * @author Gert Jan Verhoog
 */
public abstract class JRadiusRequest extends JRadiusEvent {

    private JRadiusSession session;

    /**
     * @return Returns the return value of the JRadiusRequest
     */
    public abstract int getReturnValue();

    /**
     * @param returnValue The new return value to set
     */
    public abstract void setReturnValue(int returnValue);

    /**
     * @return Returns the RADIUS Server "Configuration Items" as AttributeList
     */
    public abstract AttributeList getConfigItems();

    /**
     * @return Returns an array of the RadiusPackets received
     */
    public abstract RadiusPacket[] getPackets();

    /**
     * @param configItems The new "Configuration Items" to set in the RADIUS Server
     */
    public abstract void setConfigItems(AttributeList configItems);

    /**
     * @param packets The RadiusPacket array to return to the RADIUS Server
     */
    public abstract void setPackets(RadiusPacket[] packets);

    /**
     * Get the RadiusSession assinged to this JRadiusRequest
     * @return Returns the session.
     */
    public JRadiusSession getSession() {
        return session;
    }

    /**
     * Assign a RadiusSession to a JRadiusRequest
     * @param session The session to set.
     */
    public void setSession(JRadiusSession session) {
        this.session = session;
    }

    /**
     * @return Returns the sessionKey, if one exists
     */
    public String getSessionKey() {
        if (session != null) return session.getSessionKey();
        return null;
    }

    /**
     * Convenience method, returns the Request RadiusPacket of a
     * JRadiusRequest. This is the first packet in the packet array.
     * @return a RadiusPacket containing the radius request
     * @throws RadiusException
     */
    public RadiusPacket getRequestPacket() throws RadiusException {
        RadiusPacket p[] = getPackets();
        if (p.length == 0) {
            throw new RadiusException("No Request packet in JRadiusRequest");
        }
        return p[0];
    }

    /**
     * Convenience method, returns the Reply RadiusPacket of a
     * JRadiusRequest. This is the second packet in the packet array.
     * Use hasReplyPacket() to ensure there is a reply packet in the JRadiusRequest.
     * 
     * @return RadiusPacket containing the radius reply
     * @throws RadiusException
     */
    public RadiusPacket getReplyPacket() throws RadiusException {
        RadiusPacket p[] = getPackets();
        if (p.length < 2) {
            throw new RadiusException("No Reply packet in JRadiusRequest");
        }
        return p[1];
    }

    /**
     * @return True if the JRadiusRequest has a reply packet
     */
    public boolean hasReplyPacket() {
        return getPackets().length == 2;
    }

    /**
     * @return Returns true if the request is an Accounting-Request
     */
    public boolean isAccountingRequest() {
        try {
            return (getRequestPacket() instanceof AccountingRequest);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Add an attribute to the reply by adding to the reply packet, if one,
     * or the configItems.
     * @param a The RadiusAttribute
     */
    public void addReplyAttribute(RadiusAttribute a) {
        if (hasReplyPacket()) try {
            getReplyPacket().addAttribute(a);
        } catch (RadiusException e) {
        } else getConfigItems().add(a);
    }

    public void printDebugInfo(PrintStream out) {
        if (!Configuration.isDebug()) return;
        RadiusPacket[] rp = getPackets();
        out.println("\n>>> packets in request from \"" + getSender() + "\":");
        for (int i = 0; i < rp.length; i++) if (rp[i] != null) {
            System.out.println("--- packet " + (i + 1) + " of " + rp.length);
            System.out.println(rp[i].toString());
        }
        out.println("Configuration Items:");
        out.println(getConfigItems().toString());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("{");
        sb.append(" requester = ").append(getSender());
        sb.append(", type = ").append(getTypeString());
        sb.append(" }");
        return sb.toString();
    }
}

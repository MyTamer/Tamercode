package org.jivesoftware.smackx;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import java.util.List;

/**
 * A SharedGroupManager provides services for discovering the shared groups where a user belongs.<p>
 *
 * Important note: This functionality is not part of the XMPP spec and it will only work
 * with Wildfire.
 *
 * @author Gaston Dombiak
 */
public class SharedGroupManager {

    /**
     * Returns the collection that will contain the name of the shared groups where the user
     * logged in with the specified session belongs.
     *
     * @param connection connection to use to get the user's shared groups.
     * @return collection with the shared groups' name of the logged user.
     */
    public static List getSharedGroups(XMPPConnection connection) throws XMPPException {
        SharedGroupsInfo info = new SharedGroupsInfo();
        info.setType(IQ.Type.GET);
        PacketCollector collector = connection.createPacketCollector(new PacketIDFilter(info.getPacketID()));
        connection.sendPacket(info);
        IQ result = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
        collector.cancel();
        if (result == null) {
            throw new XMPPException("No response from the server.");
        }
        if (result.getType() == IQ.Type.ERROR) {
            throw new XMPPException(result.getError());
        }
        return ((SharedGroupsInfo) result).getGroups();
    }
}

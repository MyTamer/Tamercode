package org.jumpmind.symmetric.transport;

import java.io.BufferedWriter;
import org.jumpmind.symmetric.model.ChannelMap;
import org.jumpmind.symmetric.service.IConfigurationService;

public interface IOutgoingTransport {

    public BufferedWriter open();

    public void close();

    public boolean isOpen();

    /**
     * This returns a (combined) list of suspended or ignored channels. In
     * addition, it will optionally do a reservation in the case of a Push
     * request
     */
    public ChannelMap getSuspendIgnoreChannelLists(IConfigurationService configurationService);
}

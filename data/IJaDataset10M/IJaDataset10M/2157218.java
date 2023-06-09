package org.cishell.reference.remoting.client;

import java.util.Vector;
import org.cishell.reference.remoting.RemotingClient;
import org.cishell.remoting.service.framework.CIShellFramework;

public class CIShellFrameworkClient extends RemotingClient implements CIShellFramework {

    public CIShellFrameworkClient() {
        super("/soap/services/CIShellFramework");
        setCacheing("createSession", false);
        setCacheing("getAlgorithmFactories", true);
    }

    /**
     * @see org.cishell.remoting.service.framework.CIShellFramework#createSession(java.lang.String)
     */
    public String createSession(String clientURL) {
        return (String) doCall("createSession", clientURL);
    }

    public void closeSession(String sessionID) {
        doCall("closeSession", sessionID);
    }

    /**
     * @see org.cishell.remoting.service.framework.CIShellFramework#getAlgorithmFactories()
     */
    public Vector getAlgorithmFactories() {
        return (Vector) doCall("getAlgorithmFactories");
    }

    /**
     * @see org.cishell.remoting.service.framework.CIShellFramework#getEvents(java.lang.String)
     */
    public Vector getEvents(String sessionID) {
        return (Vector) doCall("getEvents", sessionID);
    }

    /**
     * @see org.cishell.remoting.service.framework.CIShellFramework#putEvents(java.lang.String, java.util.Vector)
     */
    public void putEvents(String sessionID, Vector events) {
        doCall("putEvents", new Object[] { sessionID, events });
    }
}

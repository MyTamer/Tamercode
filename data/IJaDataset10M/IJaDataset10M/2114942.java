package net.sourceforge.cruisecontrol.distributed.core;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * Allow progress updates from distributed agents.
 * @author Dan Rollo
 * Date: Jul 25, 2007
 * Time: 10:18:04 PM
 */
public interface ProgressRemote extends Remote, Serializable {

    /**
     * @param value new progress value.
     * @throws java.rmi.RemoteException if a remote call fails
     */
    public void setValueRemote(String value) throws RemoteException;

    /**
     * @return current progress value, prefixed with last updated time..
     * @throws RemoteException if a remote call fails
     */
    public String getValueRemote() throws RemoteException;

    /**
     * @return the date when current progress value was set.
     * @throws RemoteException if a remote call fails
     */
    public Date getLastUpdatedRemote() throws RemoteException;

    /**
     * @return the current progress value (not prefixed by last updated).
     * Goofy, but don't want to change behavior of {@link #getValueRemote()} to preserve backwards compatibility.
     * @throws RemoteException if a remote call fails
     */
    public String getTextRemote() throws RemoteException;
}

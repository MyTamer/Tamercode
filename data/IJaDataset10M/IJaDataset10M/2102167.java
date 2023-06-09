package edu.uiuc.ncsa.security.delegation.client.storage;

import edu.uiuc.ncsa.security.delegation.client.ClientTransaction;
import edu.uiuc.ncsa.security.delegation.storage.TransactionStore;

/**
 * <p>Created by Jeff Gaynor<br>
 * on May 31, 2011 at  2:41:22 PM
 */
public interface ClientTransactionStore<V extends ClientTransaction> extends TransactionStore<V> {

    /**
     * The client key is a unique string generated by the portal as an identifier. This in general
     * has nothing to do with other identifiers and is used by the portal only (e.g. stored in a
     * browser cookie for the client).
     *
     * @param identifier
     * @return
     */
    public V getByClientKey(String identifier);
}

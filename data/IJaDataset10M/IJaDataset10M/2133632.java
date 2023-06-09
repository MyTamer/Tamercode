package org.ofbiz.entity.transaction;

import org.ofbiz.base.util.Debug;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import javax.transaction.xa.XAException;
import javax.transaction.*;

/**
 * GenericXaResource - Abstract XA Resource implementation supporting a single transaction
 */
public abstract class GenericXaResource implements XAResource {

    public static final String module = GenericXaResource.class.getName();

    protected boolean active = false;

    protected int timeout = 0;

    protected Xid xid = null;

    /**
     * Enlists this resource in the current transaction
     * @throws XAException
     */
    public void enlist() throws XAException {
        TransactionManager tm = TransactionFactory.getTransactionManager();
        try {
            if (tm != null && tm.getStatus() == Status.STATUS_ACTIVE) {
                Transaction tx = tm.getTransaction();
                if (tx != null) {
                    tx.enlistResource(this);
                } else {
                    throw new XAException(XAException.XAER_NOTA);
                }
            } else {
                throw new XAException("No transaction manager or invalid status");
            }
        } catch (SystemException e) {
            throw new XAException("Unable to get transaction status");
        } catch (RollbackException e) {
            throw new XAException("Unable to enlist resource with transaction");
        }
    }

    /**
     * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid xid, int flag)
     */
    public void start(Xid xid, int flag) throws XAException {
        if (this.active) {
            if (this.xid != null && this.xid.equals(xid)) {
                throw new XAException(XAException.XAER_DUPID);
            } else {
                throw new XAException(XAException.XAER_PROTO);
            }
        }
        if (this.xid != null && !this.xid.equals(xid)) {
            throw new XAException(XAException.XAER_NOTA);
        }
        this.xid = xid;
        this.active = true;
    }

    /**
     * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid xid, int flag)
     */
    public void end(Xid xid, int flag) throws XAException {
        if (!this.active) {
            throw new XAException(XAException.XAER_PROTO);
        }
        if (this.xid == null || !this.xid.equals(xid)) {
            throw new XAException(XAException.XAER_NOTA);
        }
        this.active = false;
    }

    /**
     * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid xid)
     */
    public void forget(Xid xid) throws XAException {
        if (this.xid == null || !this.xid.equals(xid)) {
            throw new XAException(XAException.XAER_NOTA);
        }
        this.xid = null;
        if (active) {
            Debug.logWarning("forget() called without end()", module);
        }
    }

    /**
     * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid xid)
     */
    public int prepare(Xid xid) throws XAException {
        if (this.xid == null || !this.xid.equals(xid)) {
            throw new XAException(XAException.XAER_NOTA);
        }
        return XA_OK;
    }

    /**
     * @see javax.transaction.xa.XAResource#recover(int flag)
     */
    public Xid[] recover(int flag) throws XAException {
        if (this.xid == null) {
            return new Xid[0];
        } else {
            return new Xid[] { this.xid };
        }
    }

    /**
     * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource xaResource)
     */
    public boolean isSameRM(XAResource xaResource) throws XAException {
        return xaResource == this;
    }

    /**
     * @see javax.transaction.xa.XAResource#getTransactionTimeout()
     */
    public int getTransactionTimeout() throws XAException {
        return this.timeout;
    }

    /**
     * @see javax.transaction.xa.XAResource#setTransactionTimeout(int seconds)
     * Note: the valus is saved but in the current implementation this is not used.
     */
    public boolean setTransactionTimeout(int seconds) throws XAException {
        this.timeout = seconds;
        return true;
    }

    public Xid getXid() {
        return this.xid;
    }

    /**
     * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid xid, boolean onePhase)
     */
    public abstract void commit(Xid xid, boolean onePhase) throws XAException;

    /**
     * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid xid)
     */
    public abstract void rollback(Xid xid) throws XAException;
}

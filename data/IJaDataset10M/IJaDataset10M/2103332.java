package org.hypergraphdb.transaction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.hypergraphdb.HGException;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.event.HGTransactionEndEvent;
import org.hypergraphdb.util.Cons;

/**
 * 
 * <p>
 * Implements a transaction in HyperGraphDB.
 * </p>
 * 
 * <p>
 * Each transaction can carry an arbitrary set of attributes along with it. This
 * is useful for attaching contextual information to transactions without
 * intruding to otherwise simple APIs. When a transaction is committed or
 * aborted, all attributes are removed.
 * </p>
 * 
 * @author Borislav Iordanov
 * 
 */
@SuppressWarnings("unchecked")
public final class HGTransaction implements HGStorageTransaction {

    static final Object NULL_VALUE = new Object();

    private HGTransaction parent;

    private HGTransactionContext context;

    private HGStorageTransaction stran = null;

    private Map<Object, Object> attributes = new HashMap<Object, Object>();

    Map<VBox<?>, VBoxBody<?>> bodiesRead = new HashMap<VBox<?>, VBoxBody<?>>();

    private Map<VBox<?>, Object> boxesWritten = new HashMap<VBox<?>, Object>();

    private long number;

    private boolean readonly = false;

    private ActiveTransactionsRecord activeTxRecord;

    private Set<Runnable> abortActions = new HashSet<Runnable>();

    long getNumber() {
        return number;
    }

    <T> T getLocalValue(VBox<T> vbox) {
        T value = null;
        HGTransaction tx = this;
        do {
            value = (T) tx.boxesWritten.get(vbox);
        } while (value == null && (tx = tx.parent) != null);
        return value;
    }

    <T> T getBoxValue(VBox<T> vbox) {
        T value = getLocalValue(vbox);
        if (value == null) {
            VBoxBody<T> body = vbox.body.getBody(number);
            if (!readonly) bodiesRead.put(vbox, body);
            value = body.value;
        }
        return (value == NULL_VALUE) ? null : value;
    }

    <T> void setBoxValue(VBox<T> vbox, T value) {
        boxesWritten.put(vbox, value == null ? NULL_VALUE : value);
    }

    private boolean isWriteTransaction() {
        return !boxesWritten.isEmpty();
    }

    /**
     * A commit can proceed only if none of the values we've read during
     * the transaction has changed (i.e. has been committed) since we read
     * them. In order words, the latest/current body of each VBox is the same
     * as the one tagged with this transaction's number.
     */
    protected boolean validateCommit() {
        if (!readonly) for (Map.Entry<VBox<?>, VBoxBody<?>> entry : bodiesRead.entrySet()) {
            if (entry.getKey().body.version != entry.getValue().version) {
                return false;
            }
        }
        return true;
    }

    protected Cons<VBoxBody<?>> performValidCommit() {
        number = context.getManager().mostRecentRecord.transactionNumber + 1;
        return doCommit();
    }

    /**
     * Commit all "written boxes" and return a linked list of the newly attached bodies
     * to them. Those bodies will be garbage collected eventually when it is determined
     * that no current transaction (or future) could be using them (see ActiveTransactionRecord.clean()). 
     */
    protected Cons<VBoxBody<?>> doCommit() {
        Cons<VBoxBody<?>> newBodies = Cons.EMPTY;
        for (Map.Entry<VBox<?>, Object> entry : boxesWritten.entrySet()) {
            VBox<Object> vbox = (VBox<Object>) entry.getKey();
            Object newValue = entry.getValue();
            VBoxBody<?> newBody = vbox.commit(this, (newValue == NULL_VALUE) ? null : newValue, number);
            newBodies = newBodies.cons(newBody);
        }
        return newBodies;
    }

    void finish() {
        if (!readonly) for (Map.Entry<VBox<?>, VBoxBody<?>> entry : bodiesRead.entrySet()) entry.getKey().finish(this);
        for (Map.Entry<VBox<?>, Object> entry : boxesWritten.entrySet()) entry.getKey().finish(this);
        bodiesRead = null;
        boxesWritten = null;
        activeTxRecord.decrementRunning();
    }

    private void fatalFailure(Throwable t) {
        context.getManager().setEnabled(false);
        context.getManager().mostRecentRecord = null;
        t.printStackTrace();
        if (t instanceof Error) throw (Error) t; else {
            if (t instanceof RuntimeException) throw (RuntimeException) t; else throw new HGException(t);
        }
    }

    HGTransaction(HGTransactionContext context, HGTransaction parent, ActiveTransactionsRecord activeTxRecord, HGStorageTransaction impl, boolean readonly) {
        this.stran = impl;
        this.context = context;
        this.parent = parent;
        this.activeTxRecord = activeTxRecord;
        this.number = activeTxRecord.transactionNumber;
        this.readonly = readonly;
    }

    public HGStorageTransaction getStorageTransaction() {
        return stran;
    }

    public void commit() throws HGTransactionException {
        if (isReadOnly() && isWriteTransaction()) {
            for (Object obj : boxesWritten.values()) {
                System.out.println("written object:" + obj);
            }
            throw new HGException("Transaction configured as read-only was used to modify data!");
        }
        if (parent != null) {
            if (stran != null) stran.commit();
            if (!readonly) parent.bodiesRead.putAll(bodiesRead);
            parent.boxesWritten.putAll(boxesWritten);
            finish();
            HyperGraph graph = context.getManager().getHyperGraph();
            graph.getEventManager().dispatch(graph, new HGTransactionEndEvent(this, true));
            return;
        }
        if (isWriteTransaction()) {
            context.getManager().COMMIT_LOCK.lock();
            try {
                if (validateCommit()) {
                    if (stran != null) stran.commit();
                    Cons<VBoxBody<?>> bodiesCommitted = performValidCommit();
                    ActiveTransactionsRecord newRecord = new ActiveTransactionsRecord(number, bodiesCommitted);
                    context.getManager().mostRecentRecord.setNext(newRecord);
                    context.getManager().mostRecentRecord = newRecord;
                    newRecord.incrementRunning();
                    this.activeTxRecord.decrementRunning();
                    this.activeTxRecord = newRecord;
                } else {
                    try {
                        privateAbort();
                    } catch (Throwable t) {
                        fatalFailure(t);
                    }
                    throw new TransactionConflictException();
                }
            } catch (TransactionConflictException rethrowme) {
                throw rethrowme;
            } catch (Throwable t) {
                fatalFailure(t);
            } finally {
                context.getManager().COMMIT_LOCK.unlock();
            }
        } else {
            if (stran != null) stran.commit();
        }
        HyperGraph graph = context.getManager().getHyperGraph();
        graph.getEventManager().dispatch(graph, new HGTransactionEndEvent(this, true));
        finish();
    }

    private void privateAbort() throws HGTransactionException {
        for (Runnable r : abortActions) r.run();
        if (stran != null) stran.abort();
        finish();
    }

    public void abort() throws HGTransactionException {
        privateAbort();
        HyperGraph graph = context.getManager().getHyperGraph();
        graph.getEventManager().dispatch(graph, new HGTransactionEndEvent(this, false));
    }

    public <T> T getAttribute(Object key) {
        return (T) attributes.get(key);
    }

    public Iterator<Object> getAttributeKeys() {
        return attributes.keySet().iterator();
    }

    public void removeAttribute(Object key) {
        attributes.remove(key);
    }

    public void setAttribute(Object key, Object value) {
        attributes.put(key, value);
    }

    public boolean isReadOnly() {
        return this.readonly;
    }

    public void addAbortAction(Runnable r) {
        this.abortActions.add(r);
    }
}

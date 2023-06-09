package org.exist.xquery.update;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.exist.EXistException;
import org.exist.collections.triggers.DocumentTriggersVisitor;
import org.exist.collections.triggers.TriggerException;
import org.exist.dom.DefaultDocumentSet;
import org.exist.dom.DocumentImpl;
import org.exist.dom.DocumentSet;
import org.exist.dom.MutableDocumentSet;
import org.exist.dom.NodeIndexListener;
import org.exist.dom.NodeProxy;
import org.exist.dom.StoredNode;
import org.exist.memtree.DocumentBuilderReceiver;
import org.exist.memtree.MemTreeBuilder;
import org.exist.security.PermissionDeniedException;
import org.exist.storage.DBBroker;
import org.exist.storage.StorageAddress;
import org.exist.storage.lock.Lock;
import org.exist.storage.serializers.Serializer;
import org.exist.storage.txn.TransactionException;
import org.exist.storage.txn.TransactionManager;
import org.exist.storage.txn.Txn;
import org.exist.util.LockException;
import org.exist.util.hashtable.Int2ObjectHashMap;
import org.exist.xquery.AbstractExpression;
import org.exist.xquery.AnalyzeContextInfo;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Expression;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * @author wolf
 *
 */
public abstract class Modification extends AbstractExpression {

    protected static final Logger LOG = Logger.getLogger(Modification.class);

    protected final Expression select;

    protected final Expression value;

    protected DocumentSet lockedDocuments = null;

    protected MutableDocumentSet modifiedDocuments = new DefaultDocumentSet();

    protected Int2ObjectHashMap<DocumentTriggersVisitor> triggers;

    /**
	 * @param context
	 */
    public Modification(XQueryContext context, Expression select, Expression value) {
        super(context);
        this.select = select;
        this.value = value;
        this.triggers = new Int2ObjectHashMap<DocumentTriggersVisitor>(10);
    }

    public int getCardinality() {
        return Cardinality.EMPTY;
    }

    public int returnsType() {
        return Type.EMPTY;
    }

    public void resetState(boolean postOptimization) {
        super.resetState(postOptimization);
        select.resetState(postOptimization);
        if (value != null) value.resetState(postOptimization);
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        contextInfo.setParent(this);
        contextInfo.addFlag(IN_UPDATE);
        select.analyze(contextInfo);
        if (value != null) value.analyze(contextInfo);
    }

    /**
	 * Acquire a lock on all documents processed by this modification.
	 * We have to avoid that node positions change during the
	 * operation.
	 * 
	 * @param nodes
	 * 
	 * @throws LockException
	 * @throws TriggerException 
	 */
    protected StoredNode[] selectAndLock(Txn transaction, Sequence nodes) throws LockException, PermissionDeniedException, XPathException, TriggerException {
        Lock globalLock = context.getBroker().getBrokerPool().getGlobalUpdateLock();
        try {
            globalLock.acquire(Lock.READ_LOCK);
            lockedDocuments = nodes.getDocumentSet();
            lockedDocuments.lock(context.getBroker(), true, false);
            StoredNode ql[] = new StoredNode[nodes.getItemCount()];
            for (int i = 0; i < ql.length; i++) {
                Item item = nodes.itemAt(i);
                if (!Type.subTypeOf(item.getType(), Type.NODE)) throw new XPathException(this, "XQuery update expressions can only be applied to nodes. Got: " + item.getStringValue());
                NodeValue nv = (NodeValue) item;
                if (nv.getImplementationType() == NodeValue.IN_MEMORY_NODE) throw new XPathException(this, "XQuery update expressions can not be applied to in-memory nodes.");
                Node n = nv.getNode();
                if (n.getNodeType() == Node.DOCUMENT_NODE) throw new XPathException(this, "Updating the document object is not allowed.");
                ql[i] = (StoredNode) n;
                DocumentImpl doc = (DocumentImpl) ql[i].getOwnerDocument();
                prepareTrigger(transaction, doc);
            }
            return ql;
        } finally {
            globalLock.release(Lock.READ_LOCK);
        }
    }

    protected Sequence deepCopy(Sequence inSeq) throws XPathException {
        context.pushDocumentContext();
        MemTreeBuilder builder = context.getDocumentBuilder();
        DocumentBuilderReceiver receiver = new DocumentBuilderReceiver(builder);
        Serializer serializer = context.getBroker().getSerializer();
        serializer.setReceiver(receiver);
        try {
            Sequence out = new ValueSequence();
            for (SequenceIterator i = inSeq.iterate(); i.hasNext(); ) {
                Item item = i.nextItem();
                if (item.getType() == Type.DOCUMENT) {
                    if (((NodeValue) item).getImplementationType() == NodeValue.PERSISTENT_NODE) {
                        StoredNode root = (StoredNode) ((NodeProxy) item).getDocument().getDocumentElement();
                        item = new NodeProxy(root.getDocument(), root.getNodeId(), root.getInternalAddress());
                    } else {
                        item = (Item) ((NodeValue) item).getOwnerDocument().getDocumentElement();
                    }
                }
                if (Type.subTypeOf(item.getType(), Type.NODE)) {
                    if (((NodeValue) item).getImplementationType() == NodeValue.PERSISTENT_NODE) {
                        int last = builder.getDocument().getLastNode();
                        NodeProxy p = (NodeProxy) item;
                        serializer.toReceiver(p, false, false);
                        if (p.getNodeType() == Node.ATTRIBUTE_NODE) item = builder.getDocument().getLastAttr(); else item = builder.getDocument().getNode(last + 1);
                    } else {
                        ((org.exist.memtree.NodeImpl) item).deepCopy();
                    }
                }
                out.add(item);
            }
            return out;
        } catch (SAXException e) {
            throw new XPathException(this, e.getMessage(), e);
        } catch (DOMException e) {
            throw new XPathException(this, e.getMessage(), e);
        } finally {
            context.popDocumentContext();
        }
    }

    protected void finishTriggers(Txn transaction) throws TriggerException {
        Iterator<DocumentImpl> iterator = modifiedDocuments.getDocumentIterator();
        while (iterator.hasNext()) {
            DocumentImpl doc = iterator.next();
            context.addModifiedDoc(doc);
            finishTrigger(transaction, doc);
        }
        triggers.clear();
    }

    /**
	 * Release all acquired document locks.
	 */
    protected void unlockDocuments() {
        if (lockedDocuments == null) return;
        modifiedDocuments.clear();
        lockedDocuments.unlock(true);
        lockedDocuments = null;
    }

    public static void checkFragmentation(XQueryContext context, DocumentSet docs) throws EXistException {
        int fragmentationLimit = -1;
        Object property = context.getBroker().getBrokerPool().getConfiguration().getProperty(DBBroker.PROPERTY_XUPDATE_FRAGMENTATION_FACTOR);
        if (property != null) fragmentationLimit = ((Integer) property).intValue();
        checkFragmentation(context, docs, fragmentationLimit);
    }

    /**
	 * Check if any of the modified documents needs defragmentation.
	 * 
	 * Defragmentation will take place if the number of split pages in the
	 * document exceeds the limit defined in the configuration file.
	 *  
	 * @param docs
	 */
    public static void checkFragmentation(XQueryContext context, DocumentSet docs, int splitCount) throws EXistException {
        DBBroker broker = context.getBroker();
        TransactionManager txnMgr = context.getBroker().getBrokerPool().getTransactionManager();
        Txn transaction = txnMgr.beginTransaction();
        try {
            for (Iterator<DocumentImpl> i = docs.getDocumentIterator(); i.hasNext(); ) {
                DocumentImpl next = i.next();
                if (next.getMetadata().getSplitCount() > splitCount) try {
                    next.getUpdateLock().acquire(Lock.WRITE_LOCK);
                    broker.defragXMLResource(transaction, next);
                } finally {
                    next.getUpdateLock().release(Lock.WRITE_LOCK);
                }
                broker.checkXMLResourceConsistency(next);
            }
            txnMgr.commit(transaction);
        } catch (Exception e) {
            txnMgr.abort(transaction);
        }
    }

    /**
	 * Fires the prepare function for the UPDATE_DOCUMENT_EVENT trigger for the Document doc
	 *  
	 * @param transaction	The transaction
	 * @param doc	The document to trigger for
	 * 
	 * @throws TriggerException 
	 */
    private void prepareTrigger(Txn transaction, DocumentImpl doc) throws TriggerException {
        DocumentTriggersVisitor triggersVisitor = doc.getCollection().getConfiguration(context.getBroker()).getDocumentTriggerProxies().instantiateVisitor(context.getBroker());
        triggersVisitor.beforeUpdateDocument(context.getBroker(), transaction, doc);
        triggers.put(doc.getDocId(), triggersVisitor);
    }

    /** Fires the finish function for UPDATE_DOCUMENT_EVENT for the documents trigger
	 * 
	 * @param transaction	The transaction
	 * @param doc	The document to trigger for
	 * 
	 * @throws TriggerException 
	 */
    private void finishTrigger(Txn transaction, DocumentImpl doc) throws TriggerException {
        DocumentTriggersVisitor triggersVisitor = triggers.get(doc.getDocId());
        if (triggersVisitor != null) {
            triggersVisitor.afterUpdateDocument(context.getBroker(), transaction, doc);
        }
    }

    /**
	 * Gets the Transaction to use for the update (can be batch or individual)
	 * 
	 * @return The transaction
	 */
    public Txn getTransaction() {
        TransactionManager txnMgr = context.getBroker().getBrokerPool().getTransactionManager();
        Txn transaction = txnMgr.beginTransaction();
        return transaction;
    }

    /**
	 * Commit's the transaction for the update unless it is a batch update and then the commit is defered
	 * 
	 * @param transaction The Transaction to commit
	 */
    public void commitTransaction(Txn transaction) throws TransactionException {
        TransactionManager txnMgr = context.getBroker().getBrokerPool().getTransactionManager();
        txnMgr.commit(transaction);
    }

    static final class IndexListener implements NodeIndexListener {

        StoredNode[] nodes;

        public IndexListener(StoredNode[] nodes) {
            this.nodes = nodes;
        }

        public void nodeChanged(StoredNode node) {
            long address = node.getInternalAddress();
            for (int i = 0; i < nodes.length; i++) {
                if (StorageAddress.equals(nodes[i].getInternalAddress(), address)) {
                    nodes[i] = node;
                }
            }
        }
    }
}

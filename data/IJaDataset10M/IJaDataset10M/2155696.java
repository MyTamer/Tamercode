package org.exist.xquery.functions.xmldb;

import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.exist.dom.NodeProxy;
import org.exist.xmldb.LocalCollection;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.AnyURIValue;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.NodeValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.Type;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;

/**
 *
 *  @author Luigi P. Bai, finder@users.sf.net, 2004
 *  @author gev
 *  @author delirium
 *
 *
 */
public abstract class XMLDBAbstractCollectionManipulator extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(XMLDBAbstractCollectionManipulator.class);

    private final boolean errorIfAbsent;

    private int paramNumber = 0;

    protected void setCollectionParameterNumber(int paramNumber) {
        this.paramNumber = paramNumber;
    }

    protected int getCollectionParameterNumber() {
        return paramNumber;
    }

    public XMLDBAbstractCollectionManipulator(XQueryContext context, FunctionSignature signature) {
        this(context, signature, true);
    }

    public XMLDBAbstractCollectionManipulator(XQueryContext context, FunctionSignature signature, boolean errorIfAbsent) {
        super(context, signature);
        this.errorIfAbsent = errorIfAbsent;
    }

    protected LocalCollection createLocalCollection(String name) throws XMLDBException {
        try {
            return new LocalCollection(context.getSubject(), context.getBroker().getBrokerPool(), new AnyURIValue(name).toXmldbURI(), context.getAccessContext());
        } catch (XPathException e) {
            throw new XMLDBException(ErrorCodes.INVALID_URI, e);
        }
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (0 == args.length) throw new XPathException(this, "Expected a collection as the argument " + (paramNumber + 1) + ".");
        boolean collectionNeedsClose = false;
        Collection collection = null;
        Item item = args[paramNumber].itemAt(0);
        if (Type.subTypeOf(item.getType(), Type.NODE)) {
            NodeValue node = (NodeValue) item;
            logger.debug("Found node");
            if (node.getImplementationType() == NodeValue.PERSISTENT_NODE) {
                org.exist.collections.Collection internalCol = ((NodeProxy) node).getDocument().getCollection();
                logger.debug("Found node");
                try {
                    collection = createLocalCollection(internalCol.getURI().toString());
                    logger.debug("Loaded collection " + collection.getName());
                } catch (XMLDBException e) {
                    throw new XPathException(this, "Failed to access collection: " + internalCol.getURI(), e);
                }
            } else {
                return Sequence.EMPTY_SEQUENCE;
            }
        }
        if (collection == null) {
            String collectionURI = args[paramNumber].getStringValue();
            if (collectionURI != null) {
                try {
                    if (!collectionURI.startsWith("xmldb:")) {
                        collection = createLocalCollection(collectionURI);
                    } else if (collectionURI.startsWith("xmldb:exist:///")) {
                        collection = createLocalCollection(collectionURI.replaceFirst("xmldb:exist://", ""));
                    } else if (collectionURI.startsWith("xmldb:exist://embedded-eXist-server")) {
                        collection = createLocalCollection(collectionURI.replaceFirst("xmldb:exist://embedded-eXist-server", ""));
                    } else if (collectionURI.startsWith("xmldb:exist://localhost")) {
                        collection = createLocalCollection(collectionURI.replaceFirst("xmldb:exist://localhost", ""));
                    } else if (collectionURI.startsWith("xmldb:exist://127.0.0.1")) {
                        collection = createLocalCollection(collectionURI.replaceFirst("xmldb:exist://127.0.0.1", ""));
                    } else {
                        collection = org.xmldb.api.DatabaseManager.getCollection(collectionURI);
                    }
                } catch (XMLDBException xe) {
                    if (errorIfAbsent) throw new XPathException(this, "Could not locate collection: " + collectionURI, xe);
                    collection = null;
                }
            }
            if (collection == null && errorIfAbsent) {
                throw new XPathException(this, "Unable to find collection: " + collectionURI);
            }
        }
        Sequence s = Sequence.EMPTY_SEQUENCE;
        try {
            s = evalWithCollection(collection, args, contextSequence);
        } finally {
            if (collectionNeedsClose && collection != null) {
                try {
                    collection.close();
                } catch (Exception e) {
                    throw new XPathException(this, "Unable to close collection", e);
                }
            }
        }
        return s;
    }

    protected abstract Sequence evalWithCollection(Collection c, Sequence[] args, Sequence contextSequence) throws XPathException;

    public static final Collection createCollection(Collection parentColl, String name) throws XMLDBException, XPathException {
        Collection child = parentColl.getChildCollection(name);
        if (child == null) {
            CollectionManagementService mgtService = (CollectionManagementService) parentColl.getService("CollectionManagementService", "1.0");
            return mgtService.createCollection(name);
        }
        return child;
    }

    public static final Collection createCollectionPath(Collection parentColl, String relPath) throws XMLDBException, XPathException {
        Collection current = parentColl;
        StringTokenizer tok = new StringTokenizer(new AnyURIValue(relPath).toXmldbURI().toString(), "/");
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            current = createCollection(current, token);
        }
        return current;
    }
}

package org.exist.xquery.functions.xmldb;

import org.exist.dom.DocumentImpl;
import org.exist.dom.NewArrayNodeSet;
import org.exist.dom.NodeProxy;
import org.exist.dom.NodeSet;
import org.exist.dom.NodeSetIterator;
import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.DateTimeValue;
import org.exist.xquery.value.DayTimeDurationValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

public class FindLastModifiedSince extends BasicFunction {

    public static final FunctionSignature signature = new FunctionSignature(new QName("find-last-modified-since", XMLDBModule.NAMESPACE_URI, XMLDBModule.PREFIX), "Filters the given node set to only include nodes from resources which were modified since the specified " + "date time.", new SequenceType[] { new FunctionParameterSequenceType("node-set", Type.NODE, Cardinality.ZERO_OR_MORE, "A node set"), new FunctionParameterSequenceType("since", Type.DATE_TIME, Cardinality.EXACTLY_ONE, "Date") }, new FunctionReturnSequenceType(Type.NODE, Cardinality.ZERO_OR_ONE, "the mime-type if available, otherwise the empty sequence"));

    public FindLastModifiedSince(XQueryContext context) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        NodeSet nodes = args[0].toNodeSet();
        if (nodes.isEmpty()) return Sequence.EMPTY_SEQUENCE;
        NodeSet result = new NewArrayNodeSet();
        DateTimeValue dtv = (DateTimeValue) args[1].itemAt(0);
        long lastModified = dtv.getDate().getTime();
        for (NodeSetIterator i = nodes.iterator(); i.hasNext(); ) {
            NodeProxy proxy = i.next();
            DocumentImpl doc = proxy.getDocument();
            long modified = doc.getMetadata().getLastModified();
            if (modified > lastModified) result.add(proxy);
        }
        return result;
    }
}

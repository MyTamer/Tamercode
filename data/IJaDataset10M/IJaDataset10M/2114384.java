package org.exist.xquery.functions.fn;

import org.exist.dom.QName;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Dependency;
import org.exist.xquery.ErrorCodes;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Profiler;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.QNameValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 * @author wolf
 *
 */
public class FunQName extends BasicFunction {

    public static final FunctionSignature signature = new FunctionSignature(new QName("QName", Function.BUILTIN_FUNCTION_NS), "Returns an xs:QName with the namespace URI given in $uri. If $uri is " + "the zero-length string or the empty sequence, it represents \"no namespace\"; in " + "this case, if the value of $qname contains a colon (:), an error is " + "raised [err:FOCA0002]. The prefix (or absence of a prefix) in $qname is " + "retained in the returned xs:QName value. The local name in the result is " + "taken from the local part of $qname.\n\nIf $qname does not have " + "the correct lexical form for xs:QName an error is raised [err:FOCA0002].\n\n" + "Note that unlike xs:QName this function does not require a xs:string literal as the argument.", new SequenceType[] { new FunctionParameterSequenceType("uri", Type.STRING, Cardinality.ZERO_OR_ONE, "The namespace URI"), new FunctionParameterSequenceType("qname", Type.STRING, Cardinality.EXACTLY_ONE, "The prefix") }, new FunctionReturnSequenceType(Type.QNAME, Cardinality.EXACTLY_ONE, "the xs:QName with the namespace URI given in $uri"));

    /**
	 * @param context
	 */
    public FunQName(XQueryContext context) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
        }
        if (!args[0].isEmpty() && args[0].getItemType() != Type.STRING) throw new XPathException(this, ErrorCodes.XPTY0004, "Namespace URI is of type '" + Type.getTypeName(args[0].getItemType()) + "', 'xs:string' expected", args[0]);
        String namespace;
        if (args[0].isEmpty()) namespace = ""; else namespace = args[0].getStringValue();
        String param = args[1].getStringValue();
        String prefix = null;
        String localName = null;
        try {
            prefix = QName.extractPrefix(param);
            localName = QName.extractLocalName(param);
        } catch (IllegalArgumentException e) {
            ValueSequence argsSeq = new ValueSequence(args[0]);
            argsSeq.addAll(args[1]);
            throw new XPathException(this, ErrorCodes.FOCA0002, "Invalid lexical form of either prefix or local name.", argsSeq);
        }
        if ((prefix != null && prefix.length() > 0) && (namespace == null || namespace.length() == 0)) {
            ValueSequence argsSeq = new ValueSequence(args[0]);
            argsSeq.addAll(args[1]);
            throw new XPathException(this, ErrorCodes.FOCA0002, "Non-empty namespace prefix with empty namespace URI", argsSeq);
        }
        QName qname = new QName(localName, namespace, prefix);
        if (prefix != null && namespace != null) {
            if (context.getURIForPrefix(prefix) == null) {
                context.declareNamespace(prefix, namespace);
            }
        }
        Sequence result = new QNameValue(context, qname);
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }
}

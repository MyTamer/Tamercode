package org.exist.xquery.functions.securitymanager;

import java.util.Set;
import org.exist.dom.QName;
import org.exist.security.AXSchemaType;
import org.exist.security.Account;
import org.exist.security.SchemaType;
import org.exist.security.SecurityManager;
import org.exist.security.Subject;
import org.exist.storage.DBBroker;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.AnyURIValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.StringValue;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.ValueSequence;

/**
 *
 * @author Adam Retter <adam@existsolutions.com>
 */
public class GetAccountMetadataFunction extends BasicFunction {

    private static final QName qnGetAccountMetadataKeys = new QName("get-account-metadata-keys", SecurityManagerModule.NAMESPACE_URI, SecurityManagerModule.PREFIX);

    private static final QName qnGetAccountMetadata = new QName("get-account-metadata", SecurityManagerModule.NAMESPACE_URI, SecurityManagerModule.PREFIX);

    public static final FunctionSignature signatures[] = { new FunctionSignature(qnGetAccountMetadataKeys, "Gets a sequence of the metadata attribute namespaces present for an account", new SequenceType[] { new FunctionParameterSequenceType("username", Type.STRING, Cardinality.EXACTLY_ONE, "The username of the account to retrieve metadata from.") }, new FunctionReturnSequenceType(Type.ANY_URI, Cardinality.ZERO_OR_MORE, "The metadata attribute namespaces if any")), new FunctionSignature(qnGetAccountMetadata, "Gets a metadata attribute value for an account", new SequenceType[] { new FunctionParameterSequenceType("username", Type.STRING, Cardinality.EXACTLY_ONE, "The username of the account to retrieve metadata from."), new FunctionParameterSequenceType("attribute", Type.ANY_URI, Cardinality.EXACTLY_ONE, "The metadata attribute namespace as defined by axschema.org") }, new FunctionReturnSequenceType(Type.STRING, Cardinality.ZERO_OR_ONE, "The metadata value (if any).")) };

    public GetAccountMetadataFunction(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    @Override
    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        DBBroker broker = getContext().getBroker();
        Subject currentUser = broker.getSubject();
        if (currentUser.getName().equals(SecurityManager.GUEST_USER)) {
            throw new XPathException("You must be an authenticated user");
        }
        final String username = args[0].getStringValue();
        if (isCalledAs(qnGetAccountMetadataKeys.getLocalName())) {
            return getAccountMetadataKeys(broker, currentUser, username);
        } else if (isCalledAs(qnGetAccountMetadata.getLocalName())) {
            final String metadataAttributeNamespace = args[1].getStringValue();
            return getAccountMetadata(broker, currentUser, username, metadataAttributeNamespace);
        } else {
            throw new XPathException("Unknown function");
        }
    }

    private Sequence getAccountMetadata(DBBroker broker, Subject currentUser, String username, String metadataAttributeNamespace) {
        SecurityManager securityManager = broker.getBrokerPool().getSecurityManager();
        Account account = securityManager.getAccount(currentUser, username);
        AXSchemaType axSchemaType = AXSchemaType.valueOfNamespace(metadataAttributeNamespace);
        String metadataValue = null;
        if (axSchemaType != null) {
            metadataValue = account.getMetadataValue(axSchemaType);
        }
        if (metadataValue == null || metadataValue.isEmpty()) {
            return Sequence.EMPTY_SEQUENCE;
        } else {
            return new StringValue(metadataValue);
        }
    }

    private Sequence getAccountMetadataKeys(DBBroker broker, Subject currentUser, String username) throws XPathException {
        SecurityManager securityManager = broker.getBrokerPool().getSecurityManager();
        Account account = securityManager.getAccount(currentUser, username);
        Set<SchemaType> metadataKeys = account.getMetadataKeys();
        Sequence seq = new ValueSequence(metadataKeys.size());
        for (SchemaType schemaType : metadataKeys) {
            seq.add(new AnyURIValue(schemaType.getNamespace()));
        }
        return seq;
    }
}

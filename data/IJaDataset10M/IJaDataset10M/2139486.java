package org.apache.axis2.jaxws.message.impl;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMXMLParserWrapper;
import org.apache.axiom.om.impl.builder.StAXBuilder;
import org.apache.axiom.soap.RolePlayer;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axis2.jaxws.ExceptionFactory;
import org.apache.axis2.jaxws.i18n.Messages;
import org.apache.axis2.jaxws.message.Block;
import org.apache.axis2.jaxws.message.Message;
import org.apache.axis2.jaxws.message.Protocol;
import org.apache.axis2.jaxws.message.XMLFault;
import org.apache.axis2.jaxws.message.XMLPart;
import org.apache.axis2.jaxws.message.factory.BlockFactory;
import org.apache.axis2.jaxws.message.util.XMLFaultUtils;
import org.apache.axis2.jaxws.utility.JavaUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.ws.WebServiceException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * XMLPartBase class for an XMLPart An XMLPart is an abstraction of the xml portion of the message.
 * The actual representation can be in one of three different forms: * An OM tree * A SAAJ
 * SOAPEnvelope * An XMLSpine (an optimized representation of the message) The representation is
 * stored in the private variable (content)
 * <p/>
 * The representation changes as the Message flows through the JAX-WS framework.  For example, here
 * is a typical flow on the inbound case: a) Message is built from OM
 * (representation: OM) b) Message flows into SOAP Handler chain              (representation:
 * OM->SOAPEnvelope) c) Message flows out of the SOAP Handler chain d) Message flows into the
 * logical dispatch processing (representation: SOAPEnvelope->XMLSpine)
 * <p/>
 * The key to performance is the implementation of the transformations between OM, SAAJ SOAPEnvelope
 * and XMLSpine.   This base class defines all of the methods that are required on an XMLPart, the
 * actual transformations are provided by the derived class.  This division of work allows the
 * derived class to concentrate on the optimization of the transformations.  For example, the
 * derived class may implement XMLSpine -> OM using OMObjectWrapperElement constructs...thus avoid
 * expensive parsing.
 * <p/>
 * Here are the methods that the derived XMLPart should implement. OMElement
 * _convertSE2OM(SOAPEnvelope se) OMElement _convertSpine2OM(XMLSpine spine) SOAPEnvelope
 * _convertOM2SE(OMElement om) SOAPEnvelope _convertSpine2SE(XMLSpine spine) XMLSpine
 * _convertOM2Spine(OMElement om) XMLSpine _convertSE2Spine(SOAPEnvelope se) XMLSpine
 * _createSpine(Protocol protocol)
 * <p/>
 * NOTE: For XML/HTTP (REST), a SOAP 1.1. Envelope is built and the rest payload is placed in the
 * body.  This purposely mimics the Axis2 implementation.
 *
 * @see org.apache.axis2.jaxws.message.XMLPart
 * @see XMLPartImpl
 */
public abstract class XMLPartBase implements XMLPart {

    private static Log log = LogFactory.getLog(XMLPartBase.class);

    Protocol protocol = Protocol.unknown;

    Style style = Style.DOCUMENT;

    int indirection = 0;

    Object content = null;

    int contentType = UNKNOWN;

    static final int UNKNOWN = 0;

    static final int OM = 1;

    static final int SOAPENVELOPE = 2;

    static final int SPINE = 3;

    boolean consumed = false;

    MessageImpl parent;

    /**
     * XMLPart should be constructed via the XMLPartFactory. This constructor constructs an empty
     * XMLPart with the specified protocol
     *
     * @param protocol
     * @throws WebServiceException
     */
    XMLPartBase(Protocol protocol) throws WebServiceException {
        super();
        if (protocol.equals(Protocol.unknown)) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("ProtocolIsNotKnown"));
        }
        content = _createSpine(protocol, getStyle(), getIndirection(), null);
        this.protocol = ((XMLSpine) content).getProtocol();
        contentType = SPINE;
    }

    /**
     * XMLPart should be constructed via the XMLPartFactory. This constructor creates an XMLPart from
     * the specified root.
     *
     * @param root
     * @param protocol (if null, the soap protocol is inferred from the namespace)
     * @throws WebServiceException
     */
    XMLPartBase(OMElement root, Protocol protocol) throws WebServiceException {
        content = root;
        contentType = OM;
        QName qName = root.getQName();
        if (protocol == null) {
            if (qName.getNamespaceURI().equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
                this.protocol = Protocol.soap11;
            } else if (qName.getNamespaceURI().equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
                this.protocol = Protocol.soap12;
            }
        } else if (protocol == Protocol.rest) {
            this.protocol = Protocol.rest;
            if (qName.getNamespaceURI().equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            } else if (qName.getNamespaceURI().equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            } else {
                content = _createSpine(Protocol.rest, Style.DOCUMENT, 0, root);
                contentType = SPINE;
            }
        } else {
            this.protocol = protocol;
        }
    }

    /**
     * XMLPart should be constructed via the XMLPartFactory. This constructor creates an XMLPart from
     * the specified root.
     *
     * @param root
     * @throws WebServiceException
     */
    XMLPartBase(SOAPEnvelope root) throws WebServiceException {
        content = root;
        contentType = SOAPENVELOPE;
        String ns = root.getNamespaceURI();
        if (ns.equals(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            protocol = Protocol.soap11;
        } else if (ns.equals(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI)) {
            protocol = Protocol.soap12;
        } else {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("RESTIsNotSupported"));
        }
    }

    private void setContent(Object content, int contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    private OMElement getContentAsOMElement() throws WebServiceException {
        OMElement om = null;
        switch(contentType) {
            case (OM):
                om = (OMElement) content;
                break;
            case (SPINE):
                om = _convertSpine2OM((XMLSpine) content);
                break;
            case (SOAPENVELOPE):
                om = _convertSE2OM((SOAPEnvelope) content);
                break;
            default:
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr2"));
        }
        setContent(om, OM);
        return om;
    }

    private SOAPEnvelope getContentAsSOAPEnvelope() throws WebServiceException {
        SOAPEnvelope se = null;
        switch(contentType) {
            case (SOAPENVELOPE):
                se = (SOAPEnvelope) content;
                break;
            case (SPINE):
                se = _convertSpine2SE((XMLSpine) content);
                break;
            case (OM):
                se = _convertOM2SE((OMElement) content);
                break;
            default:
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr2"));
        }
        setContent(se, SOAPENVELOPE);
        return se;
    }

    private XMLSpine getContentAsXMLSpine() throws WebServiceException {
        XMLSpine spine = null;
        switch(contentType) {
            case (SPINE):
                spine = (XMLSpine) content;
                break;
            case (SOAPENVELOPE):
                spine = _convertSE2Spine((SOAPEnvelope) content);
                break;
            case (OM):
                spine = _convertOM2Spine((OMElement) content);
                break;
            default:
                throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr2"));
        }
        spine.setParent(getParent());
        setContent(spine, SPINE);
        return spine;
    }

    public OMElement getAsOMElement() throws WebServiceException {
        return getContentAsOMElement();
    }

    public SOAPEnvelope getAsSOAPEnvelope() throws WebServiceException {
        return getContentAsSOAPEnvelope();
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Style getStyle() {
        return style;
    }

    public int getIndirection() {
        return indirection;
    }

    public void setStyle(Style style) throws WebServiceException {
        if (this.style != style) {
            if (contentType == SPINE) {
                getContentAsOMElement();
            }
        }
        this.style = style;
        if (style == Style.RPC) {
            setIndirection(1);
        } else {
            setIndirection(0);
        }
    }

    public void setIndirection(int indirection) {
        if (this.indirection != indirection) {
            if (contentType == SPINE) {
                getContentAsOMElement();
            }
        }
        this.indirection = indirection;
    }

    public QName getOperationElement() throws WebServiceException {
        try {
            if (style != Style.RPC) {
                return null;
            }
            switch(contentType) {
                case OM:
                    return ((org.apache.axiom.soap.SOAPEnvelope) content).getBody().getFirstElement().getQName();
                case SPINE:
                    return ((XMLSpine) content).getOperationElement();
                case SOAPENVELOPE:
                    Iterator it = ((SOAPEnvelope) content).getBody().getChildElements();
                    while (it.hasNext()) {
                        Node node = (Node) it.next();
                        if (node instanceof SOAPElement) {
                            Name name = ((SOAPElement) node).getElementName();
                            return new QName(name.getURI(), name.getLocalName(), name.getPrefix());
                        }
                    }
            }
            return null;
        } catch (SOAPException se) {
            throw ExceptionFactory.makeWebServiceException(se);
        }
    }

    public void setOperationElement(QName operationQName) throws WebServiceException {
        if (indirection == 1) {
            this.getContentAsXMLSpine().setOperationElement(operationQName);
        }
    }

    public String getXMLPartContentType() {
        switch(contentType) {
            case OM:
                return "OM";
            case SOAPENVELOPE:
                return "SOAPENVELOPE";
            case SPINE:
                return "SPINE";
            default:
                return "UNKNOWN";
        }
    }

    public XMLStreamReader getXMLStreamReader(boolean consume) throws WebServiceException {
        if (consumed) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr1"));
        }
        XMLStreamReader reader = null;
        if (contentType == SPINE) {
            reader = getContentAsXMLSpine().getXMLStreamReader(consume);
        } else {
            OMElement omElement = getContentAsOMElement();
            if (consume) {
                reader = omElement.getXMLStreamReaderWithoutCaching();
            } else {
                reader = omElement.getXMLStreamReader();
            }
        }
        setConsumed(consume);
        return reader;
    }

    public XMLFault getXMLFault() throws WebServiceException {
        XMLFault xmlFault = null;
        if (isFault()) {
            XMLSpine spine = getContentAsXMLSpine();
            xmlFault = spine.getXMLFault();
            Block[] blocks = xmlFault.getDetailBlocks();
            if (blocks != null) {
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i].setParent(getParent());
                }
            }
        }
        return xmlFault;
    }

    public void setXMLFault(XMLFault xmlFault) throws WebServiceException {
        Block[] blocks = xmlFault.getDetailBlocks();
        if (blocks != null) {
            for (int i = 0; i < blocks.length; i++) {
                blocks[i].setParent(getParent());
            }
        }
        XMLSpine spine = getContentAsXMLSpine();
        spine.setXMLFault(xmlFault);
    }

    public boolean isFault() throws WebServiceException {
        if (consumed) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr1"));
        }
        try {
            switch(contentType) {
                case OM:
                    return XMLFaultUtils.isFault((org.apache.axiom.soap.SOAPEnvelope) getContentAsOMElement());
                case SOAPENVELOPE:
                    return XMLFaultUtils.isFault(getContentAsSOAPEnvelope());
                case SPINE:
                    return getContentAsXMLSpine().isFault();
            }
        } catch (SOAPException se) {
            throw ExceptionFactory.makeWebServiceException(se);
        }
        return false;
    }

    public boolean isConsumed() {
        return consumed;
    }

    public void outputTo(XMLStreamWriter writer, boolean consume) throws XMLStreamException, WebServiceException {
        if (consumed) {
            throw ExceptionFactory.makeWebServiceException(Messages.getMessage("XMLPartImplErr1"));
        }
        if (contentType == SPINE) {
            getContentAsXMLSpine().outputTo(writer, consume);
        } else {
            OMElement omElement = getContentAsOMElement();
            if (consume) {
                omElement.serializeAndConsume(writer);
            } else {
                omElement.serialize(writer);
            }
        }
        setConsumed(consume);
        return;
    }

    public String traceString(String indent) {
        return null;
    }

    public Block getBodyBlock(int index, Object context, BlockFactory blockFactory) throws WebServiceException {
        Block block = getContentAsXMLSpine().getBodyBlock(index, context, blockFactory);
        if (block != null) {
            block.setParent(getParent());
        }
        return block;
    }

    public Block getBodyBlock(Object context, BlockFactory blockFactory) throws WebServiceException {
        Block block = getContentAsXMLSpine().getBodyBlock(context, blockFactory);
        if (block != null) {
            block.setParent(getParent());
        }
        return block;
    }

    public Block getHeaderBlock(String namespace, String localPart, Object context, BlockFactory blockFactory) throws WebServiceException {
        Block block = getContentAsXMLSpine().getHeaderBlock(namespace, localPart, context, blockFactory);
        if (block != null) {
            block.setParent(getParent());
        }
        return block;
    }

    public Set<QName> getHeaderQNames() {
        try {
            switch(contentType) {
                case OM:
                    {
                        HashSet<QName> qnames = new HashSet<QName>();
                        OMElement om = this.getAsOMElement();
                        if (om instanceof org.apache.axiom.soap.SOAPEnvelope) {
                            org.apache.axiom.soap.SOAPEnvelope se = (org.apache.axiom.soap.SOAPEnvelope) om;
                            org.apache.axiom.soap.SOAPHeader header = se.getHeader();
                            if (header != null) {
                                Iterator it = header.getChildElements();
                                while (it != null && it.hasNext()) {
                                    Object node = it.next();
                                    if (node instanceof OMElement) {
                                        qnames.add(((OMElement) node).getQName());
                                    }
                                }
                            }
                        }
                        return qnames;
                    }
                case SOAPENVELOPE:
                    {
                        HashSet<QName> qnames = new HashSet<QName>();
                        SOAPEnvelope se = this.getContentAsSOAPEnvelope();
                        if (se != null) {
                            SOAPHeader header = se.getHeader();
                            if (header != null) {
                                Iterator it = header.getChildElements();
                                while (it != null && it.hasNext()) {
                                    Object node = it.next();
                                    if (node instanceof SOAPElement) {
                                        qnames.add(((SOAPElement) node).getElementQName());
                                    }
                                }
                            }
                        }
                        return qnames;
                    }
                case SPINE:
                    return getContentAsXMLSpine().getHeaderQNames();
                default:
                    return null;
            }
        } catch (SOAPException se) {
            throw ExceptionFactory.makeWebServiceException(se);
        }
    }

    public List<Block> getHeaderBlocks(String namespace, String localPart, Object context, BlockFactory blockFactory, RolePlayer rolePlayer) throws WebServiceException {
        List<Block> blocks = getContentAsXMLSpine().getHeaderBlocks(namespace, localPart, context, blockFactory, rolePlayer);
        for (Block block : blocks) {
            if (block != null) {
                block.setParent(getParent());
            }
        }
        return blocks;
    }

    public int getNumBodyBlocks() throws WebServiceException {
        return getContentAsXMLSpine().getNumBodyBlocks();
    }

    public int getNumHeaderBlocks() throws WebServiceException {
        return getContentAsXMLSpine().getNumHeaderBlocks();
    }

    public void removeBodyBlock(int index) throws WebServiceException {
        getContentAsXMLSpine().removeBodyBlock(index);
    }

    public void removeHeaderBlock(String namespace, String localPart) throws WebServiceException {
        getContentAsXMLSpine().removeHeaderBlock(namespace, localPart);
    }

    public void setBodyBlock(int index, Block block) throws WebServiceException {
        block.setParent(getParent());
        getContentAsXMLSpine().setBodyBlock(index, block);
    }

    public void setBodyBlock(Block block) throws WebServiceException {
        block.setParent(getParent());
        getContentAsXMLSpine().setBodyBlock(block);
    }

    public void setHeaderBlock(String namespace, String localPart, Block block) throws WebServiceException {
        block.setParent(getParent());
        getContentAsXMLSpine().setHeaderBlock(namespace, localPart, block);
    }

    public void appendHeaderBlock(String namespace, String localPart, Block block) throws WebServiceException {
        block.setParent(getParent());
        getContentAsXMLSpine().appendHeaderBlock(namespace, localPart, block);
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message p) {
        parent = (MessageImpl) p;
    }

    /**
     * Convert SOAPEnvelope into an OM tree
     *
     * @param se SOAPEnvelope
     * @return OM
     * @throws WebServiceException
     */
    protected abstract OMElement _convertSE2OM(SOAPEnvelope se) throws WebServiceException;

    /**
     * Convert XMLSpine into an OM tree
     *
     * @param spine XMLSpine
     * @return OM
     * @throws WebServiceException
     */
    protected abstract OMElement _convertSpine2OM(XMLSpine spine) throws WebServiceException;

    /**
     * Convert OM tree into a SOAPEnvelope
     *
     * @param om
     * @return SOAPEnvelope
     * @throws WebServiceException
     */
    protected abstract SOAPEnvelope _convertOM2SE(OMElement om) throws WebServiceException;

    /**
     * Convert XMLSpine into a SOAPEnvelope
     *
     * @param spine
     * @return SOAPEnvelope
     * @throws WebServiceException
     */
    protected abstract SOAPEnvelope _convertSpine2SE(XMLSpine spine) throws WebServiceException;

    /**
     * Convert OM into XMLSpine
     *
     * @param om
     * @return
     * @throws WebServiceException
     */
    protected abstract XMLSpine _convertOM2Spine(OMElement om) throws WebServiceException;

    /**
     * Convert SOAPEnvelope into XMLSPine
     *
     * @param SOAPEnvelope
     * @return XMLSpine
     * @throws WebServiceException
     */
    protected abstract XMLSpine _convertSE2Spine(SOAPEnvelope se) throws WebServiceException;

    /**
     * Create an empty, default spine for the specificed protocol
     *
     * @param protocol
     * @return
     * @throws WebServiceException
     */
    protected static XMLSpine _createSpine(Protocol protocol, Style style, int indirection, OMElement payload) throws WebServiceException {
        return new XMLSpineImpl(protocol, style, indirection, payload);
    }

    private void setConsumed(boolean consume) {
        if (consume) {
            this.consumed = true;
            if (log.isDebugEnabled()) {
                log.debug("Debug Monitoring When Block is Consumed");
                log.trace(JavaUtils.stackToString());
            }
        } else {
            consumed = false;
        }
    }

    public void close() {
        OMElement om = getContentAsOMElement();
        if (om != null) {
            OMXMLParserWrapper builder = om.getBuilder();
            if (builder instanceof StAXBuilder) {
                StAXBuilder staxBuilder = (StAXBuilder) builder;
                staxBuilder.releaseParserOnClose(true);
                if (!staxBuilder.isClosed()) {
                    staxBuilder.close();
                }
            }
        }
    }
}

package be.fedict.eid.applet.service.signer;

import be.fedict.eid.applet.service.spi.DigestInfo;
import be.fedict.eid.applet.service.spi.SignatureService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.jcp.xml.dsig.internal.dom.DOMReference;
import org.jcp.xml.dsig.internal.dom.DOMSignedInfo;
import org.jcp.xml.dsig.internal.dom.DOMXMLSignature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.URIDereferencer;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.MalformedURLException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for an XML Signature Service implementation.
 *
 * @author Frank Cornelis
 */
public abstract class AbstractXmlSignatureService implements SignatureService {

    static final Log LOG = LogFactory.getLog(AbstractXmlSignatureService.class);

    private static final String SIGNATURE_ID_ATTRIBUTE = "signature-id";

    private final List<SignatureFacet> signatureFacets;

    private String signatureNamespacePrefix;

    private String signatureId;

    private final DigestAlgo digestAlgo;

    /**
     * Main constructor.
     */
    public AbstractXmlSignatureService(DigestAlgo digestAlgo) {
        this.signatureFacets = new LinkedList<SignatureFacet>();
        this.signatureNamespacePrefix = null;
        this.signatureId = null;
        this.digestAlgo = digestAlgo;
    }

    /**
     * Sets the signature Id attribute value used to create the XML signature. A
     * <code>null</code> value will trigger an automatically generated signature
     * Id.
     *
     * @param signatureId
     */
    protected void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    /**
     * Sets the XML Signature namespace prefix to be used for signature
     * creation. A <code>null</code> value will omit the prefixing.
     *
     * @param signatureNamespacePrefix
     */
    protected void setSignatureNamespacePrefix(String signatureNamespacePrefix) {
        this.signatureNamespacePrefix = signatureNamespacePrefix;
    }

    /**
     * Adds a signature facet to this XML signature service.
     *
     * @param signatureFacet
     */
    protected void addSignatureFacet(SignatureFacet signatureFacet) {
        this.signatureFacets.add(signatureFacet);
    }

    /**
     * Gives back the signature digest algorithm. Allowed values are SHA-1,
     * SHA-256, SHA-384, SHA-512, RIPEND160. The default algorithm is SHA-1.
     * Override this method to select another signature digest algorithm.
     *
     * @return
     */
    protected DigestAlgo getSignatureDigestAlgorithm() {
        return null != this.digestAlgo ? this.digestAlgo : DigestAlgo.SHA1;
    }

    /**
     * Gives back the enveloping document. Return <code>null</code> in case
     * ds:Signature should be the top-level element. Implementations can
     * override this method to provide a custom enveloping document.
     *
     * @return
     * @throws SAXException
     * @throws IOException
     */
    protected Document getEnvelopingDocument() throws ParserConfigurationException, IOException, SAXException {
        return null;
    }

    /**
     * Override this method to change the URI dereferener used by the signing
     * engine.
     *
     * @return
     */
    protected URIDereferencer getURIDereferencer() {
        return null;
    }

    /**
     * Gives back the human-readable description of what the citizen will be
     * signing. The default value is "XML Document". Override this method to
     * provide the citizen with another description.
     *
     * @return
     */
    protected String getSignatureDescription() {
        return "XML Document";
    }

    /**
     * Gives back a temporary data storage component. This component is used for
     * temporary storage of the XML signature documents.
     *
     * @return
     */
    protected abstract TemporaryDataStorage getTemporaryDataStorage();

    /**
     * Gives back the output stream to which to write the signed XML document.
     *
     * @return
     */
    protected abstract OutputStream getSignedDocumentOutputStream();

    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws NoSuchAlgorithmException {
        LOG.debug("preSign");
        DigestAlgo digestAlgo = getSignatureDigestAlgorithm();
        byte[] digestValue;
        try {
            digestValue = getXmlSignatureDigestValue(digestAlgo, digestInfos, signingCertificateChain);
        } catch (Exception e) {
            throw new RuntimeException("XML signature error: " + e.getMessage(), e);
        }
        String description = getSignatureDescription();
        return new DigestInfo(digestValue, digestAlgo.getAlgoId(), description);
    }

    public void postSign(byte[] signatureValue, List<X509Certificate> signingCertificateChain) {
        LOG.debug("postSign");
        TemporaryDataStorage temporaryDataStorage = getTemporaryDataStorage();
        InputStream documentInputStream = temporaryDataStorage.getTempInputStream();
        String signatureId = (String) temporaryDataStorage.getAttribute(SIGNATURE_ID_ATTRIBUTE);
        LOG.debug("signature Id: " + signatureId);
        Document document;
        try {
            document = loadDocument(documentInputStream);
        } catch (Exception e) {
            throw new RuntimeException("DOM error: " + e.getMessage(), e);
        }
        Element nsElement = document.createElement("ns");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ds", Constants.SignatureSpecNS);
        Element signatureElement;
        try {
            signatureElement = (Element) XPathAPI.selectSingleNode(document, "//ds:Signature[@Id='" + signatureId + "']", nsElement);
        } catch (TransformerException e) {
            throw new RuntimeException("XPATH error: " + e.getMessage(), e);
        }
        if (null == signatureElement) {
            throw new RuntimeException("ds:Signature not found for @Id: " + signatureId);
        }
        NodeList signatureValueNodeList = signatureElement.getElementsByTagNameNS(javax.xml.crypto.dsig.XMLSignature.XMLNS, "SignatureValue");
        Element signatureValueElement = (Element) signatureValueNodeList.item(0);
        signatureValueElement.setTextContent(Base64.encode(signatureValue));
        for (SignatureFacet signatureFacet : this.signatureFacets) {
            signatureFacet.postSign(signatureElement, signingCertificateChain);
        }
        OutputStream signedDocumentOutputStream = getSignedDocumentOutputStream();
        if (null == signedDocumentOutputStream) {
            throw new IllegalArgumentException("signed document output stream is null");
        }
        try {
            writeDocument(document, signedDocumentOutputStream);
        } catch (Exception e) {
            LOG.debug("error writing the signed XML document: " + e.getMessage(), e);
            throw new RuntimeException("error writing the signed XML document: " + e.getMessage(), e);
        }
    }

    protected String getCanonicalizationMethod() {
        return CanonicalizationMethod.EXCLUSIVE;
    }

    @SuppressWarnings("unchecked")
    private byte[] getXmlSignatureDigestValue(DigestAlgo digestAlgo, List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws ParserConfigurationException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, MarshalException, javax.xml.crypto.dsig.XMLSignatureException, TransformerFactoryConfigurationError, TransformerException, IOException, SAXException {
        Document document = getEnvelopingDocument();
        if (null == document) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        }
        Key key = new Key() {

            private static final long serialVersionUID = 1L;

            public String getAlgorithm() {
                return null;
            }

            public byte[] getEncoded() {
                return null;
            }

            public String getFormat() {
                return null;
            }
        };
        XMLSignContext xmlSignContext = new DOMSignContext(key, document);
        URIDereferencer uriDereferencer = getURIDereferencer();
        if (null != uriDereferencer) {
            xmlSignContext.setURIDereferencer(uriDereferencer);
        }
        if (null != this.signatureNamespacePrefix) {
            xmlSignContext.putNamespacePrefix(javax.xml.crypto.dsig.XMLSignature.XMLNS, this.signatureNamespacePrefix);
        }
        XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI());
        List<Reference> references = new LinkedList<Reference>();
        addDigestInfosAsReferences(digestInfos, signatureFactory, references);
        String localSignatureId;
        if (null == this.signatureId) {
            localSignatureId = "xmldsig-" + UUID.randomUUID().toString();
        } else {
            localSignatureId = this.signatureId;
        }
        List<XMLObject> objects = new LinkedList<XMLObject>();
        for (SignatureFacet signatureFacet : this.signatureFacets) {
            LOG.debug("invoking signature facet: " + signatureFacet.getClass().getSimpleName());
            signatureFacet.preSign(signatureFactory, document, localSignatureId, signingCertificateChain, references, objects);
        }
        SignatureMethod signatureMethod = signatureFactory.newSignatureMethod(getSignatureMethod(digestAlgo), null);
        CanonicalizationMethod canonicalizationMethod = signatureFactory.newCanonicalizationMethod(getCanonicalizationMethod(), (C14NMethodParameterSpec) null);
        SignedInfo signedInfo = signatureFactory.newSignedInfo(canonicalizationMethod, signatureMethod, references);
        String signatureValueId = localSignatureId + "-signature-value";
        javax.xml.crypto.dsig.XMLSignature xmlSignature = signatureFactory.newXMLSignature(signedInfo, null, objects, localSignatureId, signatureValueId);
        DOMXMLSignature domXmlSignature = (DOMXMLSignature) xmlSignature;
        Node documentNode = document.getDocumentElement();
        if (null == documentNode) {
            documentNode = document;
        }
        domXmlSignature.marshal(documentNode, this.signatureNamespacePrefix, (DOMCryptoContext) xmlSignContext);
        for (XMLObject object : objects) {
            LOG.debug("object java type: " + object.getClass().getName());
            List<XMLStructure> objectContentList = object.getContent();
            for (XMLStructure objectContent : objectContentList) {
                LOG.debug("object content java type: " + objectContent.getClass().getName());
                if (false == objectContent instanceof Manifest) {
                    continue;
                }
                Manifest manifest = (Manifest) objectContent;
                List<Reference> manifestReferences = manifest.getReferences();
                for (Reference manifestReference : manifestReferences) {
                    if (null != manifestReference.getDigestValue()) {
                        continue;
                    }
                    DOMReference manifestDOMReference = (DOMReference) manifestReference;
                    manifestDOMReference.digest(xmlSignContext);
                }
            }
        }
        List<Reference> signedInfoReferences = signedInfo.getReferences();
        for (Reference signedInfoReference : signedInfoReferences) {
            DOMReference domReference = (DOMReference) signedInfoReference;
            if (null != domReference.getDigestValue()) {
                continue;
            }
            domReference.digest(xmlSignContext);
        }
        TemporaryDataStorage temporaryDataStorage = getTemporaryDataStorage();
        OutputStream tempDocumentOutputStream = temporaryDataStorage.getTempOutputStream();
        writeDocument(document, tempDocumentOutputStream);
        temporaryDataStorage.setAttribute(SIGNATURE_ID_ATTRIBUTE, localSignatureId);
        DOMSignedInfo domSignedInfo = (DOMSignedInfo) signedInfo;
        ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        domSignedInfo.canonicalize(xmlSignContext, dataStream);
        byte[] octets = dataStream.toByteArray();
        MessageDigest jcaMessageDigest = MessageDigest.getInstance(digestAlgo.getAlgoId());
        byte[] digestValue = jcaMessageDigest.digest(octets);
        return digestValue;
    }

    private void addDigestInfosAsReferences(List<DigestInfo> digestInfos, XMLSignatureFactory signatureFactory, List<Reference> references) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, MalformedURLException {
        if (null == digestInfos) {
            return;
        }
        for (DigestInfo digestInfo : digestInfos) {
            byte[] documentDigestValue = digestInfo.digestValue;
            DigestMethod digestMethod = signatureFactory.newDigestMethod(getXmlDigestAlgo(digestInfo.digestAlgo), null);
            String uri = FilenameUtils.getName(new File(digestInfo.description).toURI().toURL().getFile());
            Reference reference = signatureFactory.newReference(uri, digestMethod, null, null, null, documentDigestValue);
            references.add(reference);
        }
    }

    private String getXmlDigestAlgo(String digestAlgo) {
        if ("SHA-1".equals(digestAlgo)) {
            return DigestMethod.SHA1;
        }
        if ("SHA-256".equals(digestAlgo)) {
            return DigestMethod.SHA256;
        }
        if ("SHA-512".equals(digestAlgo)) {
            return DigestMethod.SHA512;
        }
        throw new RuntimeException("unsupported digest algo: " + digestAlgo);
    }

    private String getSignatureMethod(DigestAlgo digestAlgo) {
        if (null == digestAlgo) {
            throw new RuntimeException("digest algo is null");
        }
        switch(digestAlgo) {
            case SHA1:
                return SignatureMethod.RSA_SHA1;
            case SHA256:
                return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256;
            case SHA512:
                return XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA512;
        }
        throw new RuntimeException("unsupported sign algo: " + digestAlgo);
    }

    protected void writeDocument(Document document, OutputStream documentOutputStream) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        writeDocumentNoClosing(document, documentOutputStream);
        documentOutputStream.close();
    }

    protected void writeDocumentNoClosing(Document document, OutputStream documentOutputStream) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        writeDocumentNoClosing(document, documentOutputStream, false);
    }

    protected void writeDocumentNoClosing(Document document, OutputStream documentOutputStream, boolean omitXmlDeclaration) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException, IOException {
        NoCloseOutputStream outputStream = new NoCloseOutputStream(documentOutputStream);
        Result result = new StreamResult(outputStream);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        if (omitXmlDeclaration) {
            xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        Source source = new DOMSource(document);
        xformer.transform(source, result);
    }

    protected Document loadDocument(InputStream documentInputStream) throws ParserConfigurationException, SAXException, IOException {
        InputSource inputSource = new InputSource(documentInputStream);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);
        return document;
    }

    protected Document loadDocumentNoClose(InputStream documentInputStream) throws ParserConfigurationException, SAXException, IOException {
        NoCloseInputStream noCloseInputStream = new NoCloseInputStream(documentInputStream);
        InputSource inputSource = new InputSource(noCloseInputStream);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputSource);
        return document;
    }
}

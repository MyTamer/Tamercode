package xades4j.providers.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.signature.XMLSignature;
import xades4j.algorithms.Algorithm;
import xades4j.algorithms.GenericAlgorithm;
import xades4j.UnsupportedAlgorithmException;
import xades4j.algorithms.CanonicalXMLWithoutComments;
import xades4j.providers.AlgorithmsProviderEx;

/**
 * The default implementation of {@link AlgorithmsProviderEx}. The defaults
 * are:
 * <ul>
 *  <li>Signature: RSA(RSA_SHA256), DSA(DSA_SHA1)</li>
 *  <li>Canonicalization: Canonical XML 1.0 without comments</li>
 *  <li>Digest: SHA256 (data objs and refs properties); SHA1 (time-stamps)</li>
 * </ul>
 * @author Luís
 */
public class DefaultAlgorithmsProviderEx implements AlgorithmsProviderEx {

    private static final Map<String, Algorithm> signatureAlgsMaps;

    static {
        signatureAlgsMaps = new HashMap<String, Algorithm>(2);
        signatureAlgsMaps.put("DSA", new GenericAlgorithm(XMLSignature.ALGO_ID_SIGNATURE_DSA));
        signatureAlgsMaps.put("RSA", new GenericAlgorithm(XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256));
    }

    @Override
    public Algorithm getSignatureAlgorithm(String keyAlgorithmName) throws UnsupportedAlgorithmException {
        Algorithm sigAlg = signatureAlgsMaps.get(keyAlgorithmName);
        if (null == sigAlg) {
            throw new UnsupportedAlgorithmException("Signature algorithm not supported by the provider", keyAlgorithmName);
        }
        return sigAlg;
    }

    @Override
    public Algorithm getCanonicalizationAlgorithmForSignature() {
        return new CanonicalXMLWithoutComments();
    }

    @Override
    public Algorithm getCanonicalizationAlgorithmForTimeStampProperties() {
        return new CanonicalXMLWithoutComments();
    }

    @Override
    public String getDigestAlgorithmForDataObjsReferences() {
        return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA256;
    }

    @Override
    public String getDigestAlgorithmForReferenceProperties() {
        return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA256;
    }

    @Override
    public String getDigestAlgorithmForTimeStampProperties() {
        return MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1;
    }
}

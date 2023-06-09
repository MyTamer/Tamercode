package gnu.classpath.tools.jarsigner;

import gnu.classpath.Configuration;
import gnu.java.security.OID;
import gnu.java.security.Registry;
import gnu.java.security.der.DER;
import gnu.java.security.der.DERValue;
import gnu.java.security.pkcs.PKCS7Data;
import gnu.java.security.pkcs.PKCS7SignedData;
import gnu.java.security.pkcs.SignerInfo;
import gnu.java.security.sig.ISignature;
import gnu.java.security.sig.ISignatureCodec;
import gnu.java.security.sig.dss.DSSSignature;
import gnu.java.security.sig.dss.DSSSignatureX509Codec;
import gnu.java.security.sig.rsa.RSAPKCS1V1_5Signature;
import gnu.java.security.sig.rsa.RSAPKCS1V1_5SignatureX509Codec;
import gnu.java.security.util.Util;
import gnu.java.util.jar.JarUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509CRL;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;

/**
 * A helper class for the .SF file found in signed jars.
 */
public class SFHelper {

    private static final Logger log = Logger.getLogger(SFHelper.class.getName());

    private static final int READY = 0;

    private static final int STARTED = 1;

    private static final int FINISHED = 2;

    private static final int SF_GENERATED = 3;

    private static final int DSA_GENERATED = 4;

    /** http://asn1.elibel.tm.fr/cgi-bin/oid/display?oid=1.3.14.3.2.26&action=display */
    private static final OID hashAlgorithmIdentifierSHA1 = new OID("1.3.14.3.2.26");

    private int state;

    private JarFile jar;

    private Manifest manifest;

    private Attributes sfMainAttributes;

    private Map sfEntries;

    private byte[] sfBytes;

    private HashUtils util;

    /**
   * @param jar the JAR archive the .SF file belongs to.
   */
    public SFHelper(JarFile jar) {
        super();
        this.jar = jar;
        this.state = READY;
    }

    /**
   * Writes the contents of the <code>.SF</code> file to the designated JAR
   * output stream. Line-endings are platform-independent and consist of the
   * 2-codepoint sequence <code>0x0D</code> and <code>0x0A</code>.
   * 
   * @param jar the JAR output stream to write a <code>.SF</code> file to.
   * @throws IOException if an I/O related exception occurs during the process.
   */
    void writeSF(JarOutputStream jar) throws IOException {
        if (this.state != FINISHED) throw new IllegalStateException(Messages.getString("SFHelper.1"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JarUtils.writeSFManifest(sfMainAttributes, sfEntries, baos);
        sfBytes = baos.toByteArray();
        if (Configuration.DEBUG) log.fine("\n" + Util.dumpString(sfBytes, "+++ sfBytes "));
        jar.write(sfBytes);
        jar.flush();
        this.state = SF_GENERATED;
    }

    /**
   * The contents of the .DSA file is the DER encoded form of a PKCS#7
   * ContentInfo of the type SignedData.
   * <p>
   * The ContentInfo ASN.1 syntax is as described in the "PKCS#7 Cryptographic
   * Message Syntax Standard" (RSA Labs) specifications:
   * <pre>
   * ContentInfo ::= SEQUENCE {
   *   contentType     ContentType,
   *   content     [0] EXPLICIT ANY DEFINED BY contentType OPTIONAL
   * }
   * 
   * ContentType ::= OBJECT IDENTIFIER
   * </pre>
   * <p>
   * The ContentType is an OID which determines the type of the contents field
   * that follows it. For the .DSA file the OID is "1.2.840.113549.1.7.2", while
   * the content field is the byte array representing the DER encoded form of a
   * SignedData content-type. The ASN.1 syntax of the SignedData type is as
   * follows:
   * <pre>
   * SignedData ::= SEQUENCE {
   *   version          Version, -- always 1 for PKCS#7 1.5
   *   digestAlgorithms DigestAlgorithmIdentifiers,
   *   contentInfo      ContentInfo,
   *   certificates [0] IMPLICIT ExtendedCertificatesAndCertificates OPTIONAL,
   *   crls         [1] IMPLICIT CertificateRevocationLists OPTIONAL,
   *   signerInfos      SignerInfos
   * }
   * 
   * DigestAlgorithmIdentifiers ::= SET OF DigestAlgorithmIdentifier
   * 
   * SignerInfos ::= SET OF SignerInfo
   * </pre>
   * <p>
   * Finally the SignerInfo is a per-signer structure. Its ASN.1 syntax looks
   * like so:
   * <pre>
   * SignerInfo ::= SEQUENCE {
   *   version                       Version, -- always 1 for PKCS#7 1.5
   *   issuerAndSerialNumber         IssuerAndSerialNumber,
   *   digestAlgorithm               DigestAlgorithmIdentifier,
   *   authenticatedAttributes   [0] IMPLICIT Attributes OPTIONAL,
   *   digestEncryptionAlgorithm     DigestEncryptionAlgorithmIdentifier,
   *   encryptedDigest               EncryptedDigest,
   *   unauthenticatedAttributes [1] IMPLICIT Attributes OPTIONAL
   * }
   * 
   * EncryptedDigest ::= OCTET STRING
   * </pre>
   * 
   * @param jar the JAR output stream to write a <code>.DSA</code> file to.
   * @param signerKey the private key to sign with.
   * @param certificates the possibly null signer certificate chain.
   * @param internalSF if <code>true</code> then include the .SF file contents
   * in the signed .DSA file; otherwise don't. 
   * @throws IOException if an I/O related exception occurs during the process.
   * @throws CRLException 
   * @throws CertificateEncodingException 
   */
    void writeDSA(JarOutputStream jar, PrivateKey signerKey, Certificate[] certificates, boolean internalSF) throws IOException, CertificateEncodingException, CRLException {
        if (this.state != SF_GENERATED) throw new IllegalStateException(Messages.getString("SFHelper.4"));
        if (Configuration.DEBUG) log.fine("+++ signer private key = " + signerKey);
        ISignature signatureAlgorithm;
        ISignatureCodec signatureCodec;
        OID digestEncryptionAlgorithmOID;
        if (signerKey instanceof DSAPrivateKey) {
            signatureAlgorithm = new DSSSignature();
            signatureCodec = new DSSSignatureX509Codec();
            digestEncryptionAlgorithmOID = Main.DSA_SIGNATURE_OID;
        } else if (signerKey instanceof RSAPrivateKey) {
            signatureAlgorithm = new RSAPKCS1V1_5Signature(Registry.MD5_HASH);
            signatureCodec = new RSAPKCS1V1_5SignatureX509Codec();
            digestEncryptionAlgorithmOID = Main.RSA_SIGNATURE_OID;
        } else throw new SecurityException(Messages.getString("SFHelper.6"));
        Map signatureAttributes = new HashMap();
        signatureAttributes.put(ISignature.SIGNER_KEY, signerKey);
        signatureAlgorithm.setupSign(signatureAttributes);
        signatureAlgorithm.update(sfBytes, 0, sfBytes.length);
        Object signature = signatureAlgorithm.sign();
        byte[] signedSFBytes = signatureCodec.encodeSignature(signature);
        if (Configuration.DEBUG) log.fine("\n" + Util.dumpString(signedSFBytes, "+++ signedSFBytes "));
        Set digestAlgorithms = new HashSet();
        List digestAlgorithm = new ArrayList(2);
        DERValue derDigestAlgorithmOID = new DERValue(DER.OBJECT_IDENTIFIER, hashAlgorithmIdentifierSHA1);
        DERValue derDigestAlgorithmParams = new DERValue(DER.NULL, null);
        digestAlgorithm.add(derDigestAlgorithmOID);
        digestAlgorithm.add(derDigestAlgorithmParams);
        DERValue derDigestAlgorithm = new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, digestAlgorithm);
        digestAlgorithms.add(derDigestAlgorithm);
        PKCS7Data data = internalSF ? new PKCS7Data(sfBytes) : null;
        X509CRL[] crls = null;
        Set signerInfos = new HashSet();
        X509Certificate cert = (X509Certificate) certificates[0];
        try {
            cert.checkValidity();
        } catch (CertificateExpiredException x) {
            String issuerName = getIssuerName(cert);
            String subjectName = getSubjectName(cert);
            Date notAfterDate = getNotAfterDate(cert);
            System.out.println(Messages.getFormattedString("SFHelper.0", new Object[] { issuerName, subjectName, notAfterDate }));
        } catch (CertificateNotYetValidException x) {
            String issuerName = getIssuerName(cert);
            String subjectName = getSubjectName(cert);
            Date notBeforeDate = getNotBeforeDate(cert);
            System.out.println(Messages.getFormattedString("SFHelper.11", new Object[] { issuerName, subjectName, notBeforeDate }));
        }
        X500Principal issuer = cert.getIssuerX500Principal();
        BigInteger serialNumber = cert.getSerialNumber();
        byte[] authenticatedAttributes = null;
        byte[] encryptedDigest = signedSFBytes;
        byte[] unauthenticatedAttributes = null;
        SignerInfo signerInfo = new SignerInfo(issuer, serialNumber, hashAlgorithmIdentifierSHA1, authenticatedAttributes, digestEncryptionAlgorithmOID, encryptedDigest, unauthenticatedAttributes);
        signerInfos.add(signerInfo);
        PKCS7SignedData dsaContents = new PKCS7SignedData(digestAlgorithms, data, certificates, crls, signerInfos);
        dsaContents.encode(jar);
        jar.flush();
        this.state = DSA_GENERATED;
    }

    Manifest getManifest() {
        return this.manifest;
    }

    void startSigning() throws IOException {
        if (this.state != READY) throw new IllegalStateException(Messages.getString("SFHelper.9"));
        Manifest oldManifest = jar.getManifest();
        this.manifest = oldManifest == null ? new Manifest() : new Manifest(oldManifest);
        this.sfMainAttributes = new Attributes();
        this.sfEntries = new HashMap();
        util = new HashUtils();
        this.state = STARTED;
    }

    /**
   * Hashes the designated JAR entry (the file itself); adds the resulting hash
   * as an attribute to the manifest, and computes the hash of the added (to
   * the Manifest) two headers and add the result as an attribute of the
   * corresponding entry in the .SF file.
   */
    void updateEntry(JarEntry entry) throws IOException {
        if (this.state != STARTED) throw new IllegalStateException(Messages.getString("SFHelper.10"));
        String name = entry.getName();
        InputStream jeis = jar.getInputStream(entry);
        String hash = util.hashStream(jeis);
        if (Configuration.DEBUG) log.fine("Hash of " + name + " = " + hash);
        Attributes mainfestAttributes = manifest.getAttributes(name);
        if (mainfestAttributes == null) {
            mainfestAttributes = new Attributes();
            manifest.getEntries().put(name, mainfestAttributes);
        }
        mainfestAttributes.putValue(Main.DIGEST, hash);
        String sfHash = util.hashManifestEntry(name, hash);
        Attributes sfAttributes = (Attributes) sfEntries.get(name);
        if (sfAttributes == null) {
            sfAttributes = new Attributes();
            sfEntries.put(name, sfAttributes);
        }
        sfAttributes.putValue(Main.DIGEST, sfHash);
        if (Configuration.DEBUG) {
            log.fine("Name: " + name);
            log.fine(Main.DIGEST + ": " + sfHash);
            log.fine("");
        }
    }

    /**
   * @param sectionsOnly whether to compute, in addition to the files, the hash
   * of the mainfest itself (<code>false</code>) or not (<code>true</code>).
   */
    void finishSigning(boolean sectionsOnly) throws IOException {
        if (state != STARTED) throw new IllegalStateException(Messages.getString("SFHelper.10"));
        if (sectionsOnly) return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        manifest.write(baos);
        baos.flush();
        String manifestHash = util.hashByteArray(baos.toByteArray());
        if (Configuration.DEBUG) log.fine("Hashed Manifest " + manifestHash);
        sfMainAttributes.putValue(Main.DIGEST_MANIFEST, manifestHash);
        this.state = FINISHED;
    }

    /**
   * Given an X.509 certificate this method returns the string representation of
   * the Issuer Distinguished Name.
   * 
   * @param cert an X.509 certificate.
   * @return the string representation of the Issuer's DN.
   */
    private String getIssuerName(X509Certificate cert) {
        X500Principal xp = cert.getIssuerX500Principal();
        if (xp == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has null Issuer. Return [unknown]");
            return Messages.getString("SFHelper.14");
        }
        String result = xp.getName();
        if (result == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has an Issuer with null DN. Return [unnamed]");
            return Messages.getString("SFHelper.17");
        }
        return result;
    }

    /**
   * Given an X.509 certificate this method returns the string representation of
   * the Subject Distinguished Name.
   * 
   * @param cert an X.509 certificate.
   * @return the string representation of the Subject's DN.
   */
    private String getSubjectName(X509Certificate cert) {
        X500Principal xp = cert.getSubjectX500Principal();
        if (xp == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has null Subject. Return [unknown]");
            return Messages.getString("SFHelper.14");
        }
        String result = xp.getName();
        if (result == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has a Subject with null DN. Return [unnamed]");
            return Messages.getString("SFHelper.17");
        }
        return result;
    }

    /**
   * Given an X.509 certificate this method returns the end validity date of 
   * this certificate.
   * 
   * @param cert an X.509 certificate.
   * @return the date when this certificate stops being valid.
   */
    private Date getNotAfterDate(X509Certificate cert) {
        Date result = cert.getNotAfter();
        if (result == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has null start-validity date. Return epoch");
            return new Date(0);
        }
        return result;
    }

    /**
   * Given an X.509 certificate this method returns the start validity date of
   * this certificate.
   * 
   * @param cert an X.509 certificate.
   * @return the date when this certificate starts being valid.
   */
    private Date getNotBeforeDate(X509Certificate cert) {
        Date result = cert.getNotBefore();
        if (result == null) {
            if (Configuration.DEBUG) log.fine("Certiticate, with serial number " + cert.getSerialNumber() + ", has null end-validity date. Return epoch");
            return new Date(0);
        }
        return result;
    }
}

package org.dpr.mykeys.app.test;

import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpr.mykeys.app.CertificateInfo;

public class ListAlgorithms {

    public static final Log log = LogFactory.getLog(ListAlgorithms.class);

    public static void printSet(String setName, Set algorithms) {
        log.trace(setName + ":");
        if (algorithms.isEmpty()) {
            log.trace("            None available.");
        } else {
            Iterator it = algorithms.iterator();
            while (it.hasNext()) {
                String name = (String) it.next();
                log.trace("            " + name);
            }
        }
    }

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Provider[] providers = Security.getProviders();
        Set<String> ciphers = new HashSet<String>();
        Set<String> keyAgreements = new HashSet<String>();
        Set<String> macs = new HashSet<String>();
        Set<String> messageDigests = new HashSet<String>();
        Set<String> signatures = new HashSet<String>();
        Set<String> keyFactory = new HashSet<String>();
        Set<String> keyPairGenerator = new HashSet<String>();
        Set<String> keyGenerator = new HashSet<String>();
        for (int i = 0; i != providers.length; i++) {
            Iterator it = providers[i].keySet().iterator();
            while (it.hasNext()) {
                String entry = (String) it.next();
                if (entry.startsWith("Alg.Alias.")) {
                    entry = entry.substring("Alg.Alias.".length());
                }
                if (entry.startsWith("Cipher.")) {
                    ciphers.add(entry.substring("Cipher.".length()));
                } else if (entry.startsWith("KeyAgreement.")) {
                    keyAgreements.add(entry.substring("KeyAgreement.".length()));
                } else if (entry.startsWith("Mac.")) {
                    macs.add(entry.substring("Mac.".length()));
                } else if (entry.startsWith("MessageDigest.")) {
                    messageDigests.add(entry.substring("MessageDigest.".length()));
                } else if (entry.startsWith("Signature.")) {
                    signatures.add(entry.substring("Signature.".length()));
                } else if (entry.startsWith("KeyPairGenerator.")) {
                    keyPairGenerator.add(entry.substring("KeyPairGenerator.".length()));
                } else if (entry.startsWith("KeyFactory.")) {
                    keyFactory.add(entry.substring("KeyFactory.".length()));
                } else if (entry.startsWith("KeyGenerator.")) {
                    keyGenerator.add(entry.substring("KeyGenerator.".length()));
                } else {
                    log.trace(entry);
                }
            }
        }
        printSet("KeyGenerator", keyGenerator);
        printSet("KeyFactory", keyFactory);
        printSet("KeyPairGenerator", keyPairGenerator);
        printSet("Ciphers", ciphers);
        printSet("KeyAgreeents", keyAgreements);
        printSet("Macs", macs);
        printSet("MessageDigests", messageDigests);
        printSet("Signatures", signatures);
    }
}

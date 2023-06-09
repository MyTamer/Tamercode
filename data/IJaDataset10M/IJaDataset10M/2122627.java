package com.sun.security.sasl;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * The SASL provider.
 * Provides client support for
 * - EXTERNAL
 * - PLAIN
 * - CRAM-MD5
 * - DIGEST-MD5
 * - GSSAPI/Kerberos v5
 * And server support for
 * - CRAM-MD5
 * - DIGEST-MD5
 * - GSSAPI/Kerberos v5
 */
public final class Provider extends java.security.Provider {

    private static final long serialVersionUID = 8622598936488630849L;

    private static final String info = "Sun SASL provider" + "(implements client mechanisms for: " + "DIGEST-MD5, GSSAPI, EXTERNAL, PLAIN, CRAM-MD5;" + " server mechanisms for: DIGEST-MD5, GSSAPI, CRAM-MD5)";

    public Provider() {
        super("SunSASL", 1.7d, info);
        AccessController.doPrivileged(new PrivilegedAction<Void>() {

            public Void run() {
                put("SaslClientFactory.DIGEST-MD5", "com.sun.security.sasl.digest.FactoryImpl");
                put("SaslClientFactory.GSSAPI", "com.sun.security.sasl.gsskerb.FactoryImpl");
                put("SaslClientFactory.EXTERNAL", "com.sun.security.sasl.ClientFactoryImpl");
                put("SaslClientFactory.PLAIN", "com.sun.security.sasl.ClientFactoryImpl");
                put("SaslClientFactory.CRAM-MD5", "com.sun.security.sasl.ClientFactoryImpl");
                put("SaslServerFactory.CRAM-MD5", "com.sun.security.sasl.ServerFactoryImpl");
                put("SaslServerFactory.GSSAPI", "com.sun.security.sasl.gsskerb.FactoryImpl");
                put("SaslServerFactory.DIGEST-MD5", "com.sun.security.sasl.digest.FactoryImpl");
                return null;
            }
        });
    }
}

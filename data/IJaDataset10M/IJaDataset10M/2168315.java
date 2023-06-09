package org.eclipse.mylyn.web.core;

import java.net.Proxy;
import java.net.SocketAddress;

/**
 * Abstraction for a proxy that supports user authentication.
 * 
 * @author Rob Elves
 * @since 2.0
 */
public class AuthenticatedProxy extends Proxy {

    private String userName = "";

    private String password = "";

    public AuthenticatedProxy(Type type, SocketAddress sa, String userName, String password) {
        super(type, sa);
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

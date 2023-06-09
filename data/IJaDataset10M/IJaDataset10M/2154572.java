package org.atricore.idbus.idojos.ldapidentitystore;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.kernel.main.authn.*;
import org.atricore.idbus.kernel.main.store.AbstractStore;
import org.atricore.idbus.kernel.main.store.SimpleUserKey;
import org.atricore.idbus.kernel.main.store.UserKey;
import org.atricore.idbus.kernel.main.store.exceptions.NoSuchUserException;
import org.atricore.idbus.kernel.main.store.exceptions.SSOIdentityException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;

/**
 * An implementation of an Identity and Credential Store which obtains credential, user and
 * role information from an LDAP server using JNDI, based on the configuration properties.
 * <p/>
 * It allows to set whatever options your LDAP JNDI provider supports your Gateway
 * configuration file.
 * Examples of standard property names are:
 * <ul>
 * <li><code>initialContextFactory = "java.naming.factory.initial"</code>
 * <li><code>securityProtocol = "java.naming.security.protocol"</code>
 * <li><code>providerUrl = "java.naming.provider.url"</code>
 * <li><code>securityAuthentication = "java.naming.security.authentication"</code>
 * </ul>
 * <p/>
 * This store implementation is both an Identity Store and Credential Store.
 * Since in JOSSO the authentication of the user is left to the configured Authentication Scheme,
 * this store implementation cannot delegate user identity assertion by binding to the
 * LDAP server. For that reason it retrieves the required credentials from the directory
 * leaving the authentication procedure to the configured Authentication Scheme.
 * The store must be supplied with the configuratoin parameters so that it can retrieve user
 * identity information.
 * <p/>
 * <p/>
 * Additional component properties include:
 * <ul>
 * <li>securityPrincipal: the DN of the user to be used to bind to the LDAP Server
 * <li>securityCredential: the securityPrincipal password to be used for binding to the
 * LDAP Server.
 * <li>securityAuthentication: the security level to be used with the LDAP Server session.
 * Its value is one of the following strings:
 * "none", "simple", "strong".
 * If not set, "simple" will be used.
 * <li>ldapSearchScope : alows control over LDAP search scope : valid values are ONELEVEL, SUBTREE</li>
 * <li>usersCtxDN : the fixed distinguished name to the context to search for user accounts.
 * <li>principalUidAttributeID: the name of the attribute that contains the user login name.
 * This is used to locate the user.
 * <li>rolesCtxDN : The fixed distinguished name to the context to search for user roles.
 * <li>uidAttributeID: the name of the attribute that, in the object containing the user roles,
 * references role members. The attribute value should be the DN of the user associated with the
 * role. This is used to locate the user roles.
 * <li>roleAttributeID : The name of the attribute that contains the role name
 * <li>roleMatchingMOde : The way JOSSO gets users roles, values UDN (default) and UID.
 * <li>credentialQueryString : The query string to obtain user credentials. It should have the
 * following format : user_attribute_name=credential_attribute_name,...
 * For example :
 * uid=username,userPassword=password
 * <li>userPropertiesQueryString : The query string to obtain user properties. It should have
 * the following format : ldap_attribute_name=user_attribute_name,...
 * For example :
 * mail=mail,cn=description
 * </ul>
 * A sample LDAP Identity Store configuration :
 * <p/>
 * <pre>
 * &lt;sso-identity-store&gt;
 * &lt;class&gt;org.josso.gateway.identity.service.store.ldap.LDAPIdentityStore&lt;/class&gt;
 * &lt;initialContextFactory&gt;com.sun.jndi.ldap.LdapCtxFactory&lt;/initialContextFactory&gt;
 * &lt;providerUrl&gt;ldap://localhost&lt;/providerUrl&gt;
 * &lt;securityPrincipal&gt;cn=Manager\,dc=my-domain\,dc=com&lt;/securityPrincipal&gt;
 * &lt;securityCredential&gt;secret&lt;/securityCredential&gt;
 * &lt;securityAuthentication&gt;simple&lt;/securityAuthentication&gt;
 * &lt;usersCtxDN&gt;ou=People\,dc=my-domain\,dc=com&lt;/usersCtxDN&gt;
 * &lt;principalUidAttributeID&gt;uid&lt;/principalUidAttributeID&gt;
 * &lt;rolesCtxDN&gt;ou=Roles\,dc=my-domain\,dc=com&lt;/rolesCtxDN&gt;
 * &lt;uidAttributeID&gt;uniquemember&lt;/uidAttributeID&gt;
 * &lt;roleMatchingMode&gt;UDN&lt;/roleMatchingMode&gt;
 * &lt;roleAttributeID&gt;cn&lt;/roleAttributeID&gt;
 * &lt;credentialQueryString&gt;uid=username\,userPassword=password&lt;/credentialQueryString&gt;
 * &lt;userPropertiesQueryString&gt;mail=mail\,cn=description&lt;/userPropertiesQueryString&gt;
 * &lt;ldapSearchScope&gt;SUBTREE&lt;/ldapSearchScope&gt;
 * &lt;/sso-identity-store&gt;
 * </pre>
 * <p/>
 * A sample LDAP Credential Store configuration :
 * <p/>
 * <pre>
 * &lt;credential-store&gt;
 * &lt;class&gt;org.josso.gateway.identity.service.store.ldap.LDAPIdentityStore&lt;/class&gt;
 * &lt;initialContextFactory&gt;com.sun.jndi.ldap.LdapCtxFactory&lt;/initialContextFactory&gt;
 * &lt;providerUrl&gt;ldap://localhost&lt;/providerUrl&gt;
 * &lt;securityPrincipal&gt;cn=Manager\,dc=my-domain\,dc=com&lt;/securityPrincipal&gt;
 * &lt;securityCredential&gt;secret&lt;/securityCredential&gt;
 * &lt;securityAuthentication&gt;simple&lt;/securityAuthentication&gt;
 * &lt;usersCtxDN&gt;ou=People\,dc=my-domain\,dc=com&lt;/usersCtxDN&gt;
 * &lt;principalUidAttributeID&gt;uid&lt;/principalUidAttributeID&gt;
 * &lt;rolesCtxDN&gt;ou=Roles\,dc=my-domain\,dc=com&lt;/rolesCtxDN&gt;
 * &lt;uidAttributeID&gt;uniquemember&lt;/uidAttributeID&gt;
 * &lt;roleAttributeID&gt;cn&lt;/roleAttributeID&gt;
 * &lt;credentialQueryString&gt;uid=username\,userPassword=password&lt;/credentialQueryString&gt;
 * &lt;userPropertiesQueryString&gt;mail=mail\,cn=description&lt;/userPropertiesQueryString&gt;
 * &lt;/credential-store&gt;
 * </pre>
 *
 * @author <a href="mailto:gbrigand@josso.org">Gianluca Brigandi</a>
 * @version CVS $Id: LDAPIdentityStore.java 1040 2009-03-05 00:56:52Z gbrigand $
 *
 * @org.apache.xbean.XBean element="ldap-store"
 */
public class LDAPIdentityStore extends AbstractStore {

    private static final Log logger = LogFactory.getLog(LDAPIdentityStore.class);

    /**
     * Valid userPassword schemes according to RFC 2307
     */
    private static final String USERPASSWORD_SCHEME_MD5 = "{md5}";

    private static final String USERPASSWORD_SCHEME_CRYPT = "{crypt}";

    private static final String USERPASSWORD_SCHEME_SHA = "{sha}";

    private String _initialContextFactory;

    private String _providerUrl;

    private String _securityAuthentication;

    private String _rolesCtxDN;

    private String _uidAttributeID;

    private String _roleAttributeID;

    private String _roleMatchingMode;

    private String _securityProtocol;

    private String _securityPrincipal;

    private String _securityCredential;

    private String _principalUidAttributeID;

    private String _usersCtxDN;

    private String _credentialQueryString;

    private String _userPropertiesQueryString;

    private String _ldapSearchScope;

    private String _updateableCredentialAttribute;

    public LDAPIdentityStore() {
    }

    @Override
    public boolean userExists(UserKey key) throws SSOIdentityException {
        try {
            String dn = selectUserDN(((SimpleUserKey) key).getId());
            return dn != null;
        } catch (NamingException e) {
            throw new SSOIdentityException(e);
        }
    }

    /**
     * Loads user information and its user attributes from the LDAP server.
     *
     * @param key the userid value to fetch the user in the LDAP server.
     * @return the user instance with the provided userid
     * @throws NoSuchUserException  if the user does not exist
     * @throws SSOIdentityException a fatal exception loading the requested user
     */
    public BaseUser loadUser(UserKey key) throws NoSuchUserException, SSOIdentityException {
        try {
            if (!(key instanceof SimpleUserKey)) {
                throw new SSOIdentityException("Unsupported key type : " + key.getClass().getName());
            }
            String uid = selectUser(((SimpleUserKey) key).getId());
            if (uid == null) throw new NoSuchUserException(key);
            BaseUser bu = new BaseUserImpl();
            bu.setName(uid);
            List userProperties = new ArrayList();
            if (getUserPropertiesQueryString() != null) {
                HashMap userPropertiesResultSet = selectUserProperties(((SimpleUserKey) key).getId());
                Iterator i = userPropertiesResultSet.keySet().iterator();
                while (i.hasNext()) {
                    String pName = (String) i.next();
                    String pValue = (String) userPropertiesResultSet.get(pName);
                    SSONameValuePair vp = new SSONameValuePair(pName, pValue);
                    userProperties.add(vp);
                }
            }
            String dn = selectUserDN(((SimpleUserKey) key).getId());
            userProperties.add(new SSONameValuePair("josso.user.dn", dn));
            SSONameValuePair[] props = (SSONameValuePair[]) userProperties.toArray(new SSONameValuePair[userProperties.size()]);
            bu.setProperties(props);
            return bu;
        } catch (NamingException e) {
            logger.error("NamingException while obtaining user", e);
            throw new SSOIdentityException("Error obtaining user : " + key);
        }
    }

    /**
     * Retrieves the roles for the supplied user.
     *
     * @param key the user id of the user for whom role information is to be retrieved.
     * @return the roles associated with the supplied user.
     * @throws SSOIdentityException fatal exception obtaining user roles.
     */
    public BaseRole[] findRolesByUserKey(UserKey key) throws SSOIdentityException {
        try {
            if (!(key instanceof SimpleUserKey)) {
                throw new SSOIdentityException("Unsupported key type : " + key.getClass().getName());
            }
            String[] roleNames = selectRolesByUsername(((SimpleUserKey) key).getId());
            List roles = new ArrayList();
            for (int i = 0; i < roleNames.length; i++) {
                String roleName = roleNames[i];
                BaseRole role = new BaseRoleImpl();
                role.setName(roleName);
                roles.add(role);
            }
            return (BaseRole[]) roles.toArray(new BaseRole[roles.size()]);
        } catch (NamingException e) {
            logger.error("NamingException while obtaining roles", e);
            throw new SSOIdentityException("Error obtaining roles for user : " + key);
        }
    }

    /**
     * Loads user credential information for the supplied user from the LDAP server.
     *
     * @param key the user id of the user for whom credential information is to be retrieved.
     * @return the credentials associated with the supplied user.
     * @throws SSOIdentityException fatal exception obtaining user credentials
     */
    public Credential[] loadCredentials(CredentialKey key, CredentialProvider cp) throws SSOIdentityException {
        try {
            if (!(key instanceof CredentialKey)) {
                throw new SSOIdentityException("Unsupported key type : " + key.getClass().getName());
            }
            List credentials = new ArrayList();
            HashMap credentialResultSet = selectCredentials(((SimpleUserKey) key).getId());
            Iterator i = credentialResultSet.keySet().iterator();
            while (i.hasNext()) {
                String cName = (String) i.next();
                Object cValue = (Object) credentialResultSet.get(cName);
                Credential c = cp.newCredential(cName, cValue);
                credentials.add(c);
            }
            return (Credential[]) credentials.toArray(new Credential[credentialResultSet.size()]);
        } catch (NamingException e) {
            throw new SSOIdentityException("Error obtaining credentials for user : " + key + " (" + e.getMessage() + ")", e);
        }
    }

    /**
     * Obtains the roles for the given user.
     *
     * @param username the user name to fetch user data.
     * @return the list of roles to which the user is associated to.
     * @throws NamingException LDAP error obtaining roles fro the given user
     */
    protected String[] selectRolesByUsername(String username) throws NamingException, NoSuchUserException {
        List userRoles = new ArrayList();
        InitialLdapContext ctx = createLdapInitialContext();
        String rolesCtxDN = getRolesCtxDN();
        if (rolesCtxDN != null) {
            String uidAttributeID = getUidAttributeID();
            if (uidAttributeID == null) uidAttributeID = "uniquemember";
            String roleAttrName = getRoleAttributeID();
            if (roleAttrName == null) roleAttrName = "roles";
            String userDN;
            if ("UID".equals(getRoleMatchingMode())) {
                userDN = username;
            } else if ("PRINCIPAL".equals(getRoleMatchingMode())) {
                userDN = _principalUidAttributeID + "=" + username;
            } else {
                userDN = selectUserDN(username);
            }
            if (logger.isDebugEnabled()) logger.debug("Searching Roles for user '" + userDN + "' in Uid attribute name '" + uidAttributeID + "'");
            if (userDN == null) throw new NoSuchUserException(username);
            try {
                if (userDN.contains("\\")) {
                    logger.debug("Escaping '\\' character");
                    userDN = userDN.replace("\\", "\\\\\\");
                }
                NamingEnumeration answer = ctx.search(rolesCtxDN, "(&(" + uidAttributeID + "=" + userDN + "))", getSearchControls());
                if (logger.isDebugEnabled()) logger.debug("Search Name:  " + rolesCtxDN);
                if (logger.isDebugEnabled()) logger.debug("Search Filter:  (&(" + uidAttributeID + "=" + userDN + "))");
                if (!answer.hasMore()) logger.info("No roles found for user " + username);
                while (answer.hasMore()) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attrs = sr.getAttributes();
                    Attribute roles = attrs.get(roleAttrName);
                    for (int r = 0; r < roles.size(); r++) {
                        Object value = roles.get(r);
                        String roleName = null;
                        roleName = value.toString();
                        if (roleName != null) {
                            if (logger.isDebugEnabled()) logger.debug("Saving role '" + roleName + "' for user '" + username + "'");
                            userRoles.add(roleName);
                        }
                    }
                }
            } catch (NamingException e) {
                if (logger.isDebugEnabled()) logger.debug("Failed to locate roles", e);
            }
        }
        ctx.close();
        return (String[]) userRoles.toArray(new String[userRoles.size()]);
    }

    /**
     * Fetches the supplied user DN.
     *
     * @param uid the user id
     * @return the user DN for the supplied uid
     * @throws NamingException LDAP error obtaining user information.
     */
    protected String selectUserDN(String uid) throws NamingException {
        String dn = null;
        InitialLdapContext ctx = createLdapInitialContext();
        try {
            dn = selectUserDN(ctx, uid);
        } finally {
            ctx.close();
        }
        return dn;
    }

    /**
     * Fetches the supplied user DN.
     *
     * @param uid the user id
     * @return the user DN for the supplied uid
     * @throws NamingException LDAP error obtaining user information.
     */
    protected String selectUserDN(InitialLdapContext ctx, String uid) throws NamingException {
        String dn = null;
        String principalUidAttrName = this.getPrincipalUidAttributeID();
        String usersCtxDN = this.getUsersCtxDN();
        try {
            NamingEnumeration answer = ctx.search(usersCtxDN, "(&(" + principalUidAttrName + "=" + uid + "))", getSearchControls());
            while (answer.hasMore()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                Attribute uidAttr = attrs.get(principalUidAttrName);
                if (uidAttr == null) {
                    logger.warn("Invalid user uid attribute '" + principalUidAttrName + "'");
                    continue;
                }
                String uidValue = uidAttr.get().toString();
                if (uidValue != null) {
                    dn = sr.getName() + "," + usersCtxDN;
                    if (logger.isDebugEnabled()) logger.debug("Found user '" + principalUidAttrName + "=" + uidValue + "' for user '" + uid + "' DN=" + dn);
                } else {
                    if (logger.isDebugEnabled()) logger.debug("User not found for user '" + uid + "'");
                }
            }
        } catch (NamingException e) {
            if (logger.isDebugEnabled()) logger.debug("Failed to locate user", e);
        }
        return dn;
    }

    protected String selectUser(String uid) throws NamingException {
        return selectUser(this.getPrincipalUidAttributeID(), uid);
    }

    /**
     * Fetches the supplied user.
     *
     * @param attrValue the user id
     * @return the user id for the supplied uid
     * @throws NamingException LDAP error obtaining user information.
     */
    protected String selectUser(String attrId, String attrValue) throws NamingException {
        String uidValue = null;
        InitialLdapContext ctx = createLdapInitialContext();
        String uidAttrName = this.getPrincipalUidAttributeID();
        String usersCtxDN = this.getUsersCtxDN();
        try {
            NamingEnumeration answer = ctx.search(usersCtxDN, "(&(" + attrId + "=" + attrValue + "))", getSearchControls());
            while (answer.hasMore()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                Attribute uidAttr = attrs.get(uidAttrName);
                if (uidAttr == null) {
                    logger.warn("Invalid user attrValue attribute '" + uidAttrName + "'");
                    continue;
                }
                uidValue = uidAttr.get().toString();
                if (uidValue != null) {
                    if (logger.isDebugEnabled()) logger.debug("Found user '" + uidAttrName + "=" + uidValue + "' for user '" + attrValue + "'");
                } else {
                    if (logger.isDebugEnabled()) logger.debug("User not found for user '" + attrValue + "'");
                }
            }
        } catch (NamingException e) {
            if (logger.isDebugEnabled()) logger.debug("Failed to locate user", e);
        } finally {
            ctx.close();
        }
        return uidValue;
    }

    /**
     * Fetch the Ldap user attributes to be used as credentials.
     *
     * @param uid the user id for whom credentials are required
     * @return the hash map containing user credentials as name/value pairs
     * @throws NamingException LDAP error obtaining user credentials.
     */
    protected HashMap selectCredentials(String uid) throws NamingException {
        HashMap credentialResultSet = new HashMap();
        InitialLdapContext ctx = createLdapInitialContext();
        String principalUidAttrName = this.getPrincipalUidAttributeID();
        String usersCtxDN = this.getUsersCtxDN();
        String credentialQueryString = getCredentialQueryString();
        HashMap credentialQueryMap = parseQueryString(credentialQueryString);
        Iterator i = credentialQueryMap.keySet().iterator();
        List credentialAttrList = new ArrayList();
        while (i.hasNext()) {
            String o = (String) i.next();
            credentialAttrList.add(o);
        }
        String[] credentialAttr = (String[]) credentialAttrList.toArray(new String[credentialAttrList.size()]);
        try {
            NamingEnumeration answer = ctx.search(usersCtxDN, "(&(" + principalUidAttrName + "=" + uid + "))", getSearchControls());
            while (answer.hasMore()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                for (int j = 0; j < credentialAttr.length; j++) {
                    Object credentialObject = attrs.get(credentialAttr[j]).get();
                    String credentialName = (String) credentialQueryMap.get(credentialAttr[j]);
                    String credentialValue = null;
                    if (logger.isDebugEnabled()) logger.debug("Found user credential '" + credentialName + "' of type '" + credentialObject.getClass().getName() + "" + (credentialObject.getClass().isArray() ? "[" + Array.getLength(credentialObject) + "]" : "") + "'");
                    if (credentialObject.getClass().isArray()) {
                        try {
                            byte[] credentialData = (byte[]) credentialObject;
                            ByteBuffer in = ByteBuffer.allocate(credentialData.length);
                            in.put(credentialData);
                            in.flip();
                            Charset charset = Charset.forName("UTF-8");
                            CharsetDecoder decoder = charset.newDecoder();
                            CharBuffer charBuffer = decoder.decode(in);
                            credentialValue = charBuffer.toString();
                        } catch (CharacterCodingException e) {
                            if (logger.isDebugEnabled()) logger.debug("Can't convert credential value to String using UTF-8");
                        }
                    } else if (credentialObject instanceof String) {
                        credentialValue = (String) credentialObject;
                    }
                    if (credentialValue != null) {
                        credentialValue = getSchemeFreeValue(credentialValue);
                        credentialResultSet.put(credentialName, credentialValue);
                    } else {
                        credentialResultSet.put(credentialName, credentialObject);
                    }
                    if (logger.isDebugEnabled()) logger.debug("Found user credential '" + credentialName + "' with value '" + (credentialValue != null ? credentialValue : credentialObject) + "'");
                }
            }
        } catch (NamingException e) {
            if (logger.isDebugEnabled()) logger.debug("Failed to locate user", e);
        } finally {
            ctx.close();
        }
        return credentialResultSet;
    }

    /**
     * Obtain the properties for the user associated with the given uid using the
     * configured user properties query string.
     *
     * @param uid the user id of the user for whom its user properties are required.
     * @return the hash map containing user properties as name/value pairs.
     * @throws NamingException LDAP error obtaining user properties.
     */
    protected HashMap selectUserProperties(String uid) throws NamingException {
        HashMap userPropertiesResultSet = new HashMap();
        InitialLdapContext ctx = createLdapInitialContext();
        BasicAttributes matchAttrs = new BasicAttributes(true);
        String principalUidAttrName = this.getPrincipalUidAttributeID();
        String usersCtxDN = this.getUsersCtxDN();
        matchAttrs.put(principalUidAttrName, uid);
        String userPropertiesQueryString = getUserPropertiesQueryString();
        HashMap userPropertiesQueryMap = parseQueryString(userPropertiesQueryString);
        Iterator i = userPropertiesQueryMap.keySet().iterator();
        List propertiesAttrList = new ArrayList();
        while (i.hasNext()) {
            String o = (String) i.next();
            propertiesAttrList.add(o);
        }
        String[] propertiesAttr = (String[]) propertiesAttrList.toArray(new String[propertiesAttrList.size()]);
        try {
            NamingEnumeration answer = ctx.search(usersCtxDN, "(&(" + principalUidAttrName + "=" + uid + "))", getSearchControls());
            while (answer.hasMore()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                for (int j = 0; j < propertiesAttr.length; j++) {
                    Attribute attribute = attrs.get(propertiesAttr[j]);
                    if (attribute == null) {
                        logger.warn("Invalid user property attribute '" + propertiesAttr[j] + "'");
                        continue;
                    }
                    Object propertyObject = attrs.get(propertiesAttr[j]).get();
                    if (propertyObject == null) {
                        logger.warn("Found a 'null' value for user property '" + propertiesAttr[j] + "'");
                        continue;
                    }
                    String propertyValue = propertyObject.toString();
                    String propertyName = (String) userPropertiesQueryMap.get(propertiesAttr[j]);
                    userPropertiesResultSet.put(propertyName, propertyValue);
                    if (logger.isDebugEnabled()) logger.debug("Found user property '" + propertyName + "' with value '" + propertyValue + "'");
                }
            }
        } catch (NamingException e) {
            if (logger.isDebugEnabled()) logger.debug("Failed to locate user", e);
        } finally {
            ctx.close();
        }
        return userPropertiesResultSet;
    }

    protected void replaceAttributes(String bane, Attributes atts) throws NamingException {
        InitialLdapContext ctx = this.createLdapInitialContext();
        ctx.modifyAttributes(bane, InitialLdapContext.REPLACE_ATTRIBUTE, atts);
    }

    /**
     * Creates an InitialLdapContext by logging into the configured Ldap Server using the configured
     * username and credential.
     *
     * @return the Initial Ldap Context to be used to perform searches, etc.
     * @throws NamingException LDAP binding error.
     */
    protected InitialLdapContext createLdapInitialContext() throws NamingException {
        String securityPrincipal = getSecurityPrincipal();
        if (securityPrincipal == null) securityPrincipal = "";
        String securityCredential = getSecurityCredential();
        if (securityCredential == null) securityCredential = "";
        return createLdapInitialContext(securityPrincipal, securityCredential);
    }

    /**
     * Creates an InitialLdapContext by logging into the configured Ldap Server using the provided
     * username and credential.
     *
     * @return the Initial Ldap Context to be used to perform searches, etc.
     * @throws NamingException LDAP binding error.
     */
    protected InitialLdapContext createLdapInitialContext(String securityPrincipal, String securityCredential) throws NamingException {
        Properties env = new Properties();
        env.setProperty(Context.INITIAL_CONTEXT_FACTORY, getInitialContextFactory());
        env.setProperty(Context.SECURITY_AUTHENTICATION, getSecurityAuthentication());
        env.setProperty(Context.PROVIDER_URL, getProviderUrl());
        env.setProperty(Context.SECURITY_PROTOCOL, (getSecurityProtocol() == null ? "" : getSecurityProtocol()));
        String factoryName = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if (factoryName == null) {
            factoryName = "com.sun.jndi.ldap.LdapCtxFactory";
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY, factoryName);
        }
        String authType = env.getProperty(Context.SECURITY_AUTHENTICATION);
        if (authType == null) env.setProperty(Context.SECURITY_AUTHENTICATION, "simple");
        String protocol = env.getProperty(Context.SECURITY_PROTOCOL);
        String providerURL = getProviderUrl();
        if (providerURL == null) {
            providerURL = "ldap://localhost:" + ((protocol != null && protocol.equals("ssl")) ? "636" : "389");
        } else {
            if (providerURL.startsWith("ldaps")) {
                protocol = "ssl";
                env.setProperty(Context.SECURITY_PROTOCOL, "ssl");
            }
        }
        env.setProperty(Context.PROVIDER_URL, providerURL);
        if (securityPrincipal != null && !"".equals(securityPrincipal)) env.setProperty(Context.SECURITY_PRINCIPAL, securityPrincipal);
        if (securityCredential != null && !"".equals(securityCredential)) env.put(Context.SECURITY_CREDENTIALS, securityCredential);
        if (logger.isDebugEnabled()) logger.debug("Logging into LDAP server, env=" + env);
        InitialLdapContext ctx = new InitialLdapContext(env, null);
        if (logger.isDebugEnabled()) logger.debug("Logged into LDAP server, " + ctx);
        return ctx;
    }

    /**
     * Returns the supplied attribute value without the scheme prefix.
     * This method should be invoked for 'userPassword' type attributes.
     *
     * @param attributeValue the attribute value
     * @return the scheme
     */
    protected String getSchemeFreeValue(String attributeValue) {
        String targetValue = attributeValue;
        if (attributeValue.toLowerCase().startsWith(LDAPIdentityStore.USERPASSWORD_SCHEME_CRYPT)) {
            targetValue = attributeValue.substring(USERPASSWORD_SCHEME_CRYPT.length());
        } else if (attributeValue.toLowerCase().startsWith(LDAPIdentityStore.USERPASSWORD_SCHEME_MD5)) {
            targetValue = attributeValue.substring(USERPASSWORD_SCHEME_MD5.length());
        } else if (attributeValue.toLowerCase().startsWith(LDAPIdentityStore.USERPASSWORD_SCHEME_SHA)) {
            targetValue = attributeValue.substring(USERPASSWORD_SCHEME_SHA.length());
        }
        return targetValue;
    }

    /**
     * Parses a credential query string and builds a HashMap object
     * with key-value pairs.
     * The query string should be in the form of a string
     * and should have key-value pairs in the form <i>key=value</i>,
     * with each pair separated from the next by a ',' character.
     *
     * @param s a string containing the query to be parsed
     * @return a HashMap object built from the parsed key-value pairs
     * @throws IllegalArgumentException if the query string
     *                                  is invalid
     */
    protected HashMap parseQueryString(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        HashMap hm = new HashMap();
        StringTokenizer st = new StringTokenizer(s, ",");
        while (st.hasMoreTokens()) {
            String pair = (String) st.nextToken();
            int pos = pair.indexOf('=');
            if (pos == -1) {
                throw new IllegalArgumentException();
            }
            String key = pair.substring(0, pos);
            String val = pair.substring(pos + 1, pair.length());
            hm.put(key, val);
        }
        return hm;
    }

    /**
     * This method returns the proper search controls to be used when querying the LDAP..
     */
    protected SearchControls getSearchControls() {
        SearchControls sc = new SearchControls();
        sc.setSearchScope(_ldapSearchScope == null || _ldapSearchScope.equalsIgnoreCase("ONELEVEL") ? SearchControls.ONELEVEL_SCOPE : SearchControls.SUBTREE_SCOPE);
        return sc;
    }

    public void setInitialContextFactory(String initialContextFactory) {
        _initialContextFactory = initialContextFactory;
    }

    public String getInitialContextFactory() {
        return _initialContextFactory;
    }

    public void setProviderUrl(String providerUrl) {
        _providerUrl = providerUrl;
    }

    public String getProviderUrl() {
        return _providerUrl;
    }

    public void setSecurityAuthentication(String securityAuthentication) {
        _securityAuthentication = securityAuthentication;
    }

    public String getSecurityAuthentication() {
        return _securityAuthentication;
    }

    public void setSecurityProtocol(String securityProtocol) {
        _securityProtocol = securityProtocol;
    }

    public String getSecurityProtocol() {
        return _securityProtocol;
    }

    public void setSecurityPrincipal(String securityPrincipal) {
        _securityPrincipal = securityPrincipal;
    }

    public String getSecurityPrincipal() {
        return _securityPrincipal;
    }

    public void setSecurityCredential(String securityCredential) {
        _securityCredential = securityCredential;
    }

    protected String getSecurityCredential() {
        return _securityCredential;
    }

    public String getLdapSearchScope() {
        return _ldapSearchScope;
    }

    public void setLdapSearchScope(String ldapSearchScope) {
        _ldapSearchScope = ldapSearchScope;
    }

    public void setUsersCtxDN(String usersCtxDN) {
        _usersCtxDN = usersCtxDN;
    }

    public String getUsersCtxDN() {
        return _usersCtxDN;
    }

    public void setRolesCtxDN(String rolesCtxDN) {
        _rolesCtxDN = rolesCtxDN;
    }

    public String getRolesCtxDN() {
        return _rolesCtxDN;
    }

    public void setPrincipalUidAttributeID(String principalUidAttributeID) {
        _principalUidAttributeID = principalUidAttributeID;
    }

    public String getPrincipalUidAttributeID() {
        return _principalUidAttributeID;
    }

    public void setUidAttributeID(String uidAttributeID) {
        _uidAttributeID = uidAttributeID;
    }

    public String getRoleMatchingMode() {
        return _roleMatchingMode;
    }

    public void setRoleMatchingMode(String roleMatchingMode) {
        this._roleMatchingMode = roleMatchingMode;
    }

    public String getUidAttributeID() {
        return _uidAttributeID;
    }

    public void setRoleAttributeID(String roleAttributeID) {
        _roleAttributeID = roleAttributeID;
    }

    public String getRoleAttributeID() {
        return _roleAttributeID;
    }

    public void setCredentialQueryString(String credentialQueryString) {
        _credentialQueryString = credentialQueryString;
    }

    public String getCredentialQueryString() {
        return _credentialQueryString;
    }

    public void setUserPropertiesQueryString(String userPropertiesQueryString) {
        _userPropertiesQueryString = userPropertiesQueryString;
    }

    public String getUserPropertiesQueryString() {
        return _userPropertiesQueryString;
    }

    public String getUpdateableCredentialAttribute() {
        return _updateableCredentialAttribute;
    }

    public void setUpdateableCredentialAttribute(String updateableCredentialAttribute) {
        this._updateableCredentialAttribute = updateableCredentialAttribute;
    }
}

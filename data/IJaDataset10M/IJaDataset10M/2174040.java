package org.apache.shindig.social.core.oauth;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.SimpleOAuthValidator;
import net.oauth.OAuthProblemException;
import net.oauth.server.OAuthServlet;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shindig.auth.AuthenticationHandler;
import org.apache.shindig.auth.OAuthConstants;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.util.CharsetUtil;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

/**
 * Handle both 2-legged consumer and full 3-legged OAuth requests.
 */
public class OAuthAuthenticationHandler implements AuthenticationHandler {

    public static final String REQUESTOR_ID_PARAM = "xoauth_requestor_id";

    private final OAuthDataStore store;

    @Deprecated
    private final boolean allowLegacyBodySigning;

    @Inject
    public OAuthAuthenticationHandler(OAuthDataStore store, @Named("shindig.oauth.legacy-body-signing") boolean allowLegacyBodySigning) {
        this.store = store;
        this.allowLegacyBodySigning = allowLegacyBodySigning;
    }

    public String getName() {
        return "OAuth";
    }

    public String getWWWAuthenticateHeader(String realm) {
        return String.format("OAuth realm=\"%s\"", realm);
    }

    public SecurityToken getSecurityTokenFromRequest(HttpServletRequest request) throws InvalidAuthenticationException {
        OAuthMessage message = OAuthServlet.getMessage(request, null);
        if (StringUtils.isEmpty(getParameter(message, OAuth.OAUTH_SIGNATURE))) {
            return null;
        }
        String bodyHash = getParameter(message, OAuthConstants.OAUTH_BODY_HASH);
        if (!StringUtils.isEmpty(bodyHash)) {
            verifyBodyHash(request, bodyHash);
        }
        try {
            return verifyMessage(message);
        } catch (OAuthProblemException oauthException) {
            if (allowLegacyBodySigning && (StringUtils.isEmpty(request.getContentType()) || !request.getContentType().contains(OAuth.FORM_ENCODED))) {
                try {
                    message.addParameter(readBodyString(request), "");
                    return verifyMessage(message);
                } catch (OAuthProblemException ioe) {
                } catch (IOException e) {
                }
            }
            throw new InvalidAuthenticationException("OAuth Authentication Failure", oauthException);
        }
    }

    protected SecurityToken verifyMessage(OAuthMessage message) throws OAuthProblemException {
        OAuthEntry entry = getOAuthEntry(message);
        OAuthConsumer authConsumer = getConsumer(message);
        OAuthAccessor accessor = new OAuthAccessor(authConsumer);
        if (entry != null) {
            accessor.tokenSecret = entry.tokenSecret;
            accessor.accessToken = entry.token;
        }
        try {
            message.validateMessage(accessor, new SimpleOAuthValidator());
        } catch (OAuthProblemException e) {
            throw e;
        } catch (OAuthException e) {
            OAuthProblemException ope = new OAuthProblemException(OAuth.Problems.SIGNATURE_INVALID);
            ope.setParameter(OAuth.Problems.OAUTH_PROBLEM_ADVICE, e.getMessage());
            throw ope;
        } catch (IOException e) {
            OAuthProblemException ope = new OAuthProblemException(OAuth.Problems.SIGNATURE_INVALID);
            ope.setParameter(OAuth.Problems.OAUTH_PROBLEM_ADVICE, e.getMessage());
            throw ope;
        } catch (URISyntaxException e) {
            OAuthProblemException ope = new OAuthProblemException(OAuth.Problems.SIGNATURE_INVALID);
            ope.setParameter(OAuth.Problems.OAUTH_PROBLEM_ADVICE, e.getMessage());
            throw ope;
        }
        return getTokenFromVerifiedRequest(message, entry, authConsumer);
    }

    protected OAuthEntry getOAuthEntry(OAuthMessage message) throws OAuthProblemException {
        OAuthEntry entry = null;
        String token = getParameter(message, OAuth.OAUTH_TOKEN);
        if (!StringUtils.isEmpty(token)) {
            entry = store.getEntry(token);
            if (entry == null) {
                OAuthProblemException e = new OAuthProblemException(OAuth.Problems.TOKEN_REJECTED);
                e.setParameter(OAuth.Problems.OAUTH_PROBLEM_ADVICE, "cannot find token");
                throw e;
            } else if (entry.type != OAuthEntry.Type.ACCESS) {
                OAuthProblemException e = new OAuthProblemException(OAuth.Problems.TOKEN_REJECTED);
                e.setParameter(OAuth.Problems.OAUTH_PROBLEM_ADVICE, "token is not an access token");
                throw e;
            } else if (entry.isExpired()) {
                throw new OAuthProblemException(OAuth.Problems.TOKEN_EXPIRED);
            }
        }
        return entry;
    }

    protected OAuthConsumer getConsumer(OAuthMessage message) throws OAuthProblemException {
        String consumerKey = getParameter(message, OAuth.OAUTH_CONSUMER_KEY);
        OAuthConsumer authConsumer = store.getConsumer(consumerKey);
        if (authConsumer == null) {
            throw new OAuthProblemException(OAuth.Problems.CONSUMER_KEY_UNKNOWN);
        }
        return authConsumer;
    }

    protected SecurityToken getTokenFromVerifiedRequest(OAuthMessage message, OAuthEntry entry, OAuthConsumer authConsumer) throws OAuthProblemException {
        if (entry != null) {
            return new OAuthSecurityToken(entry.userId, entry.callbackUrl, entry.appId, entry.domain, entry.container);
        } else {
            String userId = getParameter(message, REQUESTOR_ID_PARAM);
            return store.getSecurityTokenForConsumerRequest(authConsumer.consumerKey, userId);
        }
    }

    public static byte[] readBody(HttpServletRequest request) throws IOException {
        if (request.getAttribute(AuthenticationHandler.STASHED_BODY) != null) {
            return (byte[]) request.getAttribute(AuthenticationHandler.STASHED_BODY);
        }
        byte[] rawBody = IOUtils.toByteArray(request.getInputStream());
        request.setAttribute(AuthenticationHandler.STASHED_BODY, rawBody);
        return rawBody;
    }

    public static String readBodyString(HttpServletRequest request) throws IOException {
        byte[] rawBody = readBody(request);
        return IOUtils.toString(new ByteArrayInputStream(rawBody), request.getCharacterEncoding());
    }

    public static void verifyBodyHash(HttpServletRequest request, String oauthBodyHash) throws InvalidAuthenticationException {
        if (request.getContentType() != null && request.getContentType().contains(OAuth.FORM_ENCODED)) {
            throw new AuthenticationHandler.InvalidAuthenticationException("Cannot use oauth_body_hash with a Content-Type of application/x-www-form-urlencoded", null);
        } else {
            try {
                byte[] rawBody = readBody(request);
                byte[] received = Base64.decodeBase64(CharsetUtil.getUtf8Bytes(oauthBodyHash));
                byte[] expected = DigestUtils.sha(rawBody);
                if (!Arrays.equals(received, expected)) {
                    throw new AuthenticationHandler.InvalidAuthenticationException("oauth_body_hash failed verification", null);
                }
            } catch (IOException ioe) {
                throw new AuthenticationHandler.InvalidAuthenticationException("Unable to read content body for oauth_body_hash verification", null);
            }
        }
    }

    public static String getParameter(OAuthMessage requestMessage, String key) {
        try {
            return StringUtils.trim(requestMessage.getParameter(key));
        } catch (IOException e) {
            return null;
        }
    }
}

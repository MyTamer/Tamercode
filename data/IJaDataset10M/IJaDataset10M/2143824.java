package org.apache.shiro.session.mgt;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.*;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Abstract implementation supporting the {@link NativeSessionManager NativeSessionManager} interface, supporting
 * {@link SessionListener SessionListener}s and application of the
 * {@link #getGlobalSessionTimeout() globalSessionTimeout}.
 *
 * @since 1.0
 */
public abstract class AbstractNativeSessionManager extends AbstractSessionManager implements NativeSessionManager {

    private static final Logger log = LoggerFactory.getLogger(AbstractSessionManager.class);

    private Collection<SessionListener> listeners;

    public AbstractNativeSessionManager() {
        this.listeners = new ArrayList<SessionListener>();
    }

    public void setSessionListeners(Collection<SessionListener> listeners) {
        this.listeners = listeners != null ? listeners : new ArrayList<SessionListener>();
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public Collection<SessionListener> getSessionListeners() {
        return this.listeners;
    }

    public Session start(SessionContext context) {
        Session session = createSession(context);
        applyGlobalSessionTimeout(session);
        onStart(session, context);
        notifyStart(session);
        return createExposedSession(session, context);
    }

    /**
     * Creates a new {@code Session Session} instance based on the specified (possibly {@code null})
     * initialization data.  Implementing classes must manage the persistent state of the returned session such that it
     * could later be acquired via the {@link #getSession(SessionKey)} method.
     *
     * @param context the initialization data that can be used by the implementation or underlying
     *                {@link SessionFactory} when instantiating the internal {@code Session} instance.
     * @return the new {@code Session} instance.
     * @throws org.apache.shiro.authz.HostUnauthorizedException
     *                                if the system access control policy restricts access based
     *                                on client location/IP and the specified hostAddress hasn't been enabled.
     * @throws AuthorizationException if the system access control policy does not allow the currently executing
     *                                caller to start sessions.
     */
    protected abstract Session createSession(SessionContext context) throws AuthorizationException;

    protected void applyGlobalSessionTimeout(Session session) {
        session.setTimeout(getGlobalSessionTimeout());
        onChange(session);
    }

    /**
     * Template method that allows subclasses to react to a new session being created.
     * <p/>
     * This method is invoked <em>before</em> any session listeners are notified.
     *
     * @param session the session that was just {@link #createSession created}.
     * @param context the {@link SessionContext SessionContext} that was used to start the session.
     */
    protected void onStart(Session session, SessionContext context) {
    }

    public Session getSession(SessionKey key) throws SessionException {
        Session session = lookupSession(key);
        return session != null ? createExposedSession(session, key) : null;
    }

    private Session lookupSession(SessionKey key) throws SessionException {
        if (key == null) {
            throw new NullPointerException("SessionKey argument cannot be null.");
        }
        return doGetSession(key);
    }

    private Session lookupRequiredSession(SessionKey key) throws SessionException {
        Session session = lookupSession(key);
        if (session == null) {
            String msg = "Unable to locate required Session instance based on SessionKey [" + key + "].";
            throw new UnknownSessionException(msg);
        }
        return session;
    }

    protected abstract Session doGetSession(SessionKey key) throws InvalidSessionException;

    protected Session createExposedSession(Session session, SessionContext context) {
        return new DelegatingSession(this, new DefaultSessionKey(session.getId()));
    }

    protected Session createExposedSession(Session session, SessionKey key) {
        return new DelegatingSession(this, new DefaultSessionKey(session.getId()));
    }

    /**
     * Returns the session instance to use to pass to registered {@code SessionListener}s for notification
     * that the session has been invalidated (stopped or expired).
     * <p/>
     * The default implementation returns an {@link ImmutableProxiedSession ImmutableProxiedSession} instance to ensure
     * that the specified {@code session} argument is not modified by any listeners.
     *
     * @param session the {@code Session} object being invalidated.
     * @return the {@code Session} instance to use to pass to registered {@code SessionListener}s for notification.
     */
    protected Session beforeInvalidNotification(Session session) {
        return new ImmutableProxiedSession(session);
    }

    /**
     * Notifies any interested {@link SessionListener}s that a Session has started.  This method is invoked
     * <em>after</em> the {@link #onStart onStart} method is called.
     *
     * @param session the session that has just started that will be delivered to any
     *                {@link #setSessionListeners(java.util.Collection) registered} session listeners.
     * @see SessionListener#onStart(org.apache.shiro.session.Session)
     */
    protected void notifyStart(Session session) {
        for (SessionListener listener : this.listeners) {
            listener.onStart(session);
        }
    }

    protected void notifyStop(Session session) {
        Session forNotification = beforeInvalidNotification(session);
        for (SessionListener listener : this.listeners) {
            listener.onStop(forNotification);
        }
    }

    protected void notifyExpiration(Session session) {
        Session forNotification = beforeInvalidNotification(session);
        for (SessionListener listener : this.listeners) {
            listener.onExpiration(forNotification);
        }
    }

    public Date getStartTimestamp(SessionKey key) {
        return lookupRequiredSession(key).getStartTimestamp();
    }

    public Date getLastAccessTime(SessionKey key) {
        return lookupRequiredSession(key).getLastAccessTime();
    }

    public long getTimeout(SessionKey key) throws InvalidSessionException {
        return lookupRequiredSession(key).getTimeout();
    }

    public void setTimeout(SessionKey key, long maxIdleTimeInMillis) throws InvalidSessionException {
        Session s = lookupRequiredSession(key);
        s.setTimeout(maxIdleTimeInMillis);
        onChange(s);
    }

    public void touch(SessionKey key) throws InvalidSessionException {
        Session s = lookupRequiredSession(key);
        s.touch();
        onChange(s);
    }

    public String getHost(SessionKey key) {
        return lookupRequiredSession(key).getHost();
    }

    public Collection<Object> getAttributeKeys(SessionKey key) {
        Collection<Object> c = lookupRequiredSession(key).getAttributeKeys();
        if (!CollectionUtils.isEmpty(c)) {
            return Collections.unmodifiableCollection(c);
        }
        return Collections.emptySet();
    }

    public Object getAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        return lookupRequiredSession(sessionKey).getAttribute(attributeKey);
    }

    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
        if (value == null) {
            removeAttribute(sessionKey, attributeKey);
        } else {
            Session s = lookupRequiredSession(sessionKey);
            s.setAttribute(attributeKey, value);
            onChange(s);
        }
    }

    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        Session s = lookupRequiredSession(sessionKey);
        Object removed = s.removeAttribute(attributeKey);
        if (removed != null) {
            onChange(s);
        }
        return removed;
    }

    public boolean isValid(SessionKey key) {
        try {
            checkValid(key);
            return true;
        } catch (InvalidSessionException e) {
            return false;
        }
    }

    public void stop(SessionKey key) throws InvalidSessionException {
        Session session = lookupRequiredSession(key);
        if (log.isDebugEnabled()) {
            log.debug("Stopping session with id [" + session.getId() + "]");
        }
        session.stop();
        onStop(session, key);
        notifyStop(session);
        afterStopped(session);
    }

    protected void onStop(Session session, SessionKey key) {
        onStop(session);
    }

    protected void onStop(Session session) {
        onChange(session);
    }

    protected void afterStopped(Session session) {
    }

    public void checkValid(SessionKey key) throws InvalidSessionException {
        lookupRequiredSession(key);
    }

    protected void onChange(Session s) {
    }
}

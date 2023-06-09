package org.mortbay.jetty.handler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.io.Buffer;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HandlerContainer;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.HttpException;
import org.mortbay.jetty.MimeTypes;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.log.Log;
import org.mortbay.log.Logger;
import org.mortbay.resource.Resource;
import org.mortbay.util.Attributes;
import org.mortbay.util.AttributesMap;
import org.mortbay.util.LazyList;
import org.mortbay.util.Loader;
import org.mortbay.util.URIUtil;

/** ContextHandler.
 * 
 * This handler wraps a call to handle by setting the context and
 * servlet path, plus setting the context classloader.
 * 
 * Note. Because of http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4425695
 * directly replacing war or jar files in a context is not supported.
 * You should use classes instead of jars if they will change, or deploy
 * a packed war file that gets extracted on deployment.
 * 
 * 
 * @org.apache.xbean.XBean description="Creates a basic HTTP context"
 *
 * @author gregw
 *
 */
public class ContextHandler extends HandlerWrapper implements Attributes {

    private static ThreadLocal __context = new ThreadLocal();

    /** Get the current ServletContext implementation.
     * This call is only valid during a call to doStart and is available to
     * nested handlers to access the context.
     * 
     * @return ServletContext implementation
     */
    public static SContext getCurrentContext() {
        SContext context = (SContext) __context.get();
        return context;
    }

    protected SContext _scontext;

    private Attributes _attributes;

    private Attributes _contextAttributes;

    private ClassLoader _classLoader;

    private String _contextPath;

    private Map _initParams;

    private String _displayName;

    private String _docRoot;

    private Resource _baseResource;

    private MimeTypes _mimeTypes;

    private Map _localeEncodingMap;

    private String[] _welcomeFiles;

    private ErrorHandler _errorHandler;

    private String[] _vhosts;

    private Set _connectors;

    private EventListener[] _eventListeners;

    private Logger _logger;

    private boolean _shutdown;

    private boolean _allowNullPathInfo;

    private int _maxFormContentSize = Integer.getInteger("org.mortbay.jetty.Request.maxFormContentSize", 200000).intValue();

    ;

    private Object _contextListeners;

    private Object _contextAttributeListeners;

    private Object _requestListeners;

    private Object _requestAttributeListeners;

    /**
     * 
     */
    public ContextHandler() {
        super();
        _scontext = new SContext();
        _attributes = new AttributesMap();
        _initParams = new HashMap();
    }

    /**
     * 
     */
    protected ContextHandler(SContext context) {
        super();
        _scontext = context;
        _attributes = new AttributesMap();
        _initParams = new HashMap();
    }

    /**
     * 
     */
    public ContextHandler(String contextPath) {
        this();
        setContextPath(contextPath);
    }

    /**
     * 
     */
    public ContextHandler(HandlerContainer parent, String contextPath) {
        this();
        setContextPath(contextPath);
        parent.addHandler(this);
    }

    public SContext getServletContext() {
        return _scontext;
    }

    /**
     * @return the allowNullPathInfo true if /context is not redirected to /context/
     */
    public boolean getAllowNullPathInfo() {
        return _allowNullPathInfo;
    }

    /**
     * @param allowNullPathInfo  true if /context is not redirected to /context/
     */
    public void setAllowNullPathInfo(boolean allowNullPathInfo) {
        _allowNullPathInfo = allowNullPathInfo;
    }

    public void setServer(Server server) {
        if (_errorHandler != null) {
            Server old_server = getServer();
            if (old_server != null && old_server != server) old_server.getContainer().update(this, _errorHandler, null, "error", true);
            super.setServer(server);
            if (server != null && server != old_server) server.getContainer().update(this, null, _errorHandler, "error", true);
            _errorHandler.setServer(server);
        } else super.setServer(server);
    }

    /** Set the virtual hosts for the context.
     * Only requests that have a matching host header or fully qualified
     * URL will be passed to that context with a virtual host name.
     * A context with no virtual host names or a null virtual host name is
     * available to all requests that are not served by a context with a
     * matching virtual host name.
     * @param vhosts Array of virtual hosts that this context responds to. A
     * null host name or null/empty array means any hostname is acceptable.
     * Host names may String representation of IP addresses.
     */
    public void setVirtualHosts(String[] vhosts) {
        _vhosts = vhosts;
    }

    /** Get the virtual hosts for the context.
     * Only requests that have a matching host header or fully qualified
     * URL will be passed to that context with a virtual host name.
     * A context with no virtual host names or a null virtual host name is
     * available to all requests that are not served by a context with a
     * matching virtual host name.
     * @return Array of virtual hosts that this context responds to. A
     * null host name or empty array means any hostname is acceptable.
     * Host names may be String representation of IP addresses.
     */
    public String[] getVirtualHosts() {
        return _vhosts;
    }

    /** 
     * @deprecated use {@link #setConnectorNames(String[])} 
     */
    public void setHosts(String[] hosts) {
        setConnectorNames(hosts);
    }

    /** Get the hosts for the context.
     * @deprecated
     */
    public String[] getHosts() {
        return getConnectorNames();
    }

    /**
     * @return an array of connector names that this context
     * will accept a request from.
     */
    public String[] getConnectorNames() {
        if (_connectors == null || _connectors.size() == 0) return null;
        return (String[]) _connectors.toArray(new String[_connectors.size()]);
    }

    /** Set the names of accepted connectors.
     * 
     * Names are either "host:port" or a specific configured name for a connector.
     * 
     * @param connectors If non null, an array of connector names that this context
     * will accept a request from.
     */
    public void setConnectorNames(String[] connectors) {
        if (connectors == null || connectors.length == 0) _connectors = null; else _connectors = new HashSet(Arrays.asList(connectors));
    }

    public Object getAttribute(String name) {
        return _attributes.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return AttributesMap.getAttributeNamesCopy(_attributes);
    }

    /**
     * @return Returns the attributes.
     */
    public Attributes getAttributes() {
        return _attributes;
    }

    /**
     * @return Returns the classLoader.
     */
    public ClassLoader getClassLoader() {
        return _classLoader;
    }

    /**
     * Make best effort to extract a file classpath from the context classloader
     * @return Returns the classLoader.
     */
    public String getClassPath() {
        if (_classLoader == null || !(_classLoader instanceof URLClassLoader)) return null;
        URLClassLoader loader = (URLClassLoader) _classLoader;
        URL[] urls = loader.getURLs();
        StringBuffer classpath = new StringBuffer();
        for (int i = 0; i < urls.length; i++) {
            try {
                Resource resource = Resource.newResource(urls[i]);
                File file = resource.getFile();
                if (file.exists()) {
                    if (classpath.length() > 0) classpath.append(File.pathSeparatorChar);
                    classpath.append(file.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.debug(e);
            }
        }
        if (classpath.length() == 0) return null;
        return classpath.toString();
    }

    /**
     * @return Returns the _contextPath.
     */
    public String getContextPath() {
        return _contextPath;
    }

    public String getInitParameter(String name) {
        return (String) _initParams.get(name);
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(_initParams.keySet());
    }

    /**
     * @return Returns the initParams.
     */
    public Map getInitParams() {
        return _initParams;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public EventListener[] getEventListeners() {
        return _eventListeners;
    }

    public void setEventListeners(EventListener[] eventListeners) {
        _contextListeners = null;
        _contextAttributeListeners = null;
        _requestListeners = null;
        _requestAttributeListeners = null;
        _eventListeners = eventListeners;
        for (int i = 0; eventListeners != null && i < eventListeners.length; i++) {
            EventListener listener = _eventListeners[i];
            if (listener instanceof ServletContextListener) _contextListeners = LazyList.add(_contextListeners, listener);
            if (listener instanceof ServletContextAttributeListener) _contextAttributeListeners = LazyList.add(_contextAttributeListeners, listener);
            if (listener instanceof ServletRequestListener) _requestListeners = LazyList.add(_requestListeners, listener);
            if (listener instanceof ServletRequestAttributeListener) _requestAttributeListeners = LazyList.add(_requestAttributeListeners, listener);
        }
    }

    public void addEventListener(EventListener listener) {
        setEventListeners((EventListener[]) LazyList.addToArray(getEventListeners(), listener, EventListener.class));
    }

    /**
     * @return true if this context is accepting new requests
     */
    public boolean isShutdown() {
        return !_shutdown;
    }

    /** Set shutdown status.
     * This field allows for graceful shutdown of a context. A started context may be put into non accepting state so
     * that existing requests can complete, but no new requests are accepted.
     * @param accepting true if this context is accepting new requests
     */
    public void setShutdown(boolean shutdown) {
        _shutdown = shutdown;
    }

    protected void doStart() throws Exception {
        _logger = Log.getLogger(getDisplayName() == null ? getContextPath() : getDisplayName());
        ClassLoader old_classloader = null;
        Thread current_thread = null;
        Object old_context = null;
        _contextAttributes = new AttributesMap();
        try {
            if (_classLoader != null) {
                current_thread = Thread.currentThread();
                old_classloader = current_thread.getContextClassLoader();
                current_thread.setContextClassLoader(_classLoader);
            }
            if (_mimeTypes == null) _mimeTypes = new MimeTypes();
            old_context = __context.get();
            __context.set(_scontext);
            if (_errorHandler == null) setErrorHandler(new ErrorHandler());
            startContext();
        } finally {
            __context.set(old_context);
            if (_classLoader != null) {
                current_thread.setContextClassLoader(old_classloader);
            }
        }
    }

    protected void startContext() throws Exception {
        super.doStart();
        if (_errorHandler != null) _errorHandler.start();
        if (_contextListeners != null) {
            ServletContextEvent event = new ServletContextEvent(_scontext);
            for (int i = 0; i < LazyList.size(_contextListeners); i++) {
                ((ServletContextListener) LazyList.get(_contextListeners, i)).contextInitialized(event);
            }
        }
    }

    protected void doStop() throws Exception {
        ClassLoader old_classloader = null;
        Thread current_thread = null;
        Object old_context = __context.get();
        __context.set(_scontext);
        try {
            if (_classLoader != null) {
                current_thread = Thread.currentThread();
                old_classloader = current_thread.getContextClassLoader();
                current_thread.setContextClassLoader(_classLoader);
            }
            super.doStop();
            if (_contextListeners != null) {
                ServletContextEvent event = new ServletContextEvent(_scontext);
                for (int i = LazyList.size(_contextListeners); i-- > 0; ) {
                    ((ServletContextListener) LazyList.get(_contextListeners, i)).contextDestroyed(event);
                }
            }
            if (_errorHandler != null) _errorHandler.stop();
        } finally {
            __context.set(old_context);
            if (_classLoader != null) current_thread.setContextClassLoader(old_classloader);
        }
        if (_contextAttributes != null) _contextAttributes.clearAttributes();
        _contextAttributes = null;
    }

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
        boolean new_context = false;
        SContext old_context = null;
        String old_context_path = null;
        String old_servlet_path = null;
        String old_path_info = null;
        ClassLoader old_classloader = null;
        Thread current_thread = null;
        Request base_request = (request instanceof Request) ? (Request) request : HttpConnection.getCurrentConnection().getRequest();
        if (!isStarted() || _shutdown || (dispatch == REQUEST && base_request.isHandled())) return;
        old_context = base_request.getContext();
        if (old_context != _scontext) {
            new_context = true;
            if (_vhosts != null && _vhosts.length > 0) {
                String vhost = request.getServerName();
                boolean match = false;
                for (int i = 0; !match && i < _vhosts.length; i++) match = _vhosts[i] != null && _vhosts[i].equalsIgnoreCase(vhost);
                if (!match) return;
            }
            if (_connectors != null && _connectors.size() > 0) {
                String connector = HttpConnection.getCurrentConnection().getConnector().getName();
                if (connector == null || !_connectors.contains(connector)) return;
            }
            if (dispatch == REQUEST) {
                if (target.equals(_contextPath)) {
                    if (!_allowNullPathInfo && !target.endsWith(URIUtil.SLASH)) {
                        base_request.setHandled(true);
                        if (request.getQueryString() != null) response.sendRedirect(URIUtil.addPaths(request.getRequestURI(), URIUtil.SLASH) + "?" + request.getQueryString()); else response.sendRedirect(URIUtil.addPaths(request.getRequestURI(), URIUtil.SLASH));
                        return;
                    }
                    if (_contextPath.length() > 1) {
                        target = URIUtil.SLASH;
                        request.setAttribute("org.mortbay.jetty.nullPathInfo", target);
                    }
                } else if (target.startsWith(_contextPath) && (_contextPath.length() == 1 || target.charAt(_contextPath.length()) == '/')) {
                    if (_contextPath.length() > 1) target = target.substring(_contextPath.length());
                } else {
                    return;
                }
            }
        }
        try {
            old_context_path = base_request.getContextPath();
            old_servlet_path = base_request.getServletPath();
            old_path_info = base_request.getPathInfo();
            base_request.setContext(_scontext);
            if (dispatch != INCLUDE && target.startsWith("/")) {
                if (_contextPath.length() == 1) base_request.setContextPath(""); else base_request.setContextPath(_contextPath);
                base_request.setServletPath(null);
                base_request.setPathInfo(target);
            }
            ServletRequestEvent event = null;
            if (new_context) {
                if (_classLoader != null) {
                    current_thread = Thread.currentThread();
                    old_classloader = current_thread.getContextClassLoader();
                    current_thread.setContextClassLoader(_classLoader);
                }
                if (_requestListeners != null) {
                    event = new ServletRequestEvent(_scontext, request);
                    for (int i = 0; i < LazyList.size(_requestListeners); i++) ((ServletRequestListener) LazyList.get(_requestListeners, i)).requestInitialized(event);
                }
                for (int i = 0; i < LazyList.size(_requestAttributeListeners); i++) base_request.addEventListener(((EventListener) LazyList.get(_requestAttributeListeners, i)));
            }
            try {
                if (dispatch == REQUEST && isProtectedTarget(target)) throw new HttpException(HttpServletResponse.SC_NOT_FOUND);
                Handler handler = getHandler();
                if (handler != null) handler.handle(target, request, response, dispatch);
            } catch (HttpException e) {
                Log.debug(e);
                response.sendError(e.getStatus(), e.getReason());
            } finally {
                if (new_context) {
                    for (int i = LazyList.size(_requestListeners); i-- > 0; ) ((ServletRequestListener) LazyList.get(_requestListeners, i)).requestDestroyed(event);
                    for (int i = 0; i < LazyList.size(_requestAttributeListeners); i++) base_request.removeEventListener(((EventListener) LazyList.get(_requestAttributeListeners, i)));
                }
            }
        } finally {
            if (old_context != _scontext) {
                if (_classLoader != null) {
                    current_thread.setContextClassLoader(old_classloader);
                }
                base_request.setContext(old_context);
                base_request.setContextPath(old_context_path);
                base_request.setServletPath(old_servlet_path);
                base_request.setPathInfo(old_path_info);
            }
        }
    }

    protected boolean isProtectedTarget(String target) {
        return false;
    }

    public void removeAttribute(String name) {
        _attributes.removeAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        _attributes.setAttribute(name, value);
    }

    /**
     * @param attributes The attributes to set.
     */
    public void setAttributes(Attributes attributes) {
        _attributes = attributes;
    }

    public void clearAttributes() {
        _attributes.clearAttributes();
    }

    /**
     * @param classLoader The classLoader to set.
     */
    public void setClassLoader(ClassLoader classLoader) {
        _classLoader = classLoader;
    }

    /**
     * @param contextPath The _contextPath to set.
     */
    public void setContextPath(String contextPath) {
        if (contextPath != null && contextPath.length() > 1 && contextPath.endsWith("/")) throw new IllegalArgumentException("ends with /");
        _contextPath = contextPath;
        if (getServer() != null && (getServer().isStarting() || getServer().isStarted())) {
            Handler[] contextCollections = getServer().getChildHandlersByClass(ContextHandlerCollection.class);
            for (int h = 0; contextCollections != null && h < contextCollections.length; h++) ((ContextHandlerCollection) contextCollections[h]).mapContexts();
        }
    }

    /**
     * @param initParams The initParams to set.
     */
    public void setInitParams(Map initParams) {
        if (initParams == null) return;
        _initParams = new HashMap(initParams);
    }

    /**
     * @param servletContextName The servletContextName to set.
     */
    public void setDisplayName(String servletContextName) {
        _displayName = servletContextName;
        if (_classLoader != null && _classLoader instanceof WebAppClassLoader) ((WebAppClassLoader) _classLoader).setName(servletContextName);
    }

    /**
     * @return Returns the resourceBase.
     */
    public Resource getBaseResource() {
        if (_baseResource == null) return null;
        return _baseResource;
    }

    /**
     * @return Returns the base resource as a string.
     */
    public String getResourceBase() {
        if (_baseResource == null) return null;
        return _baseResource.toString();
    }

    /**
     * @param base The resourceBase to set.
     */
    public void setBaseResource(Resource base) {
        _baseResource = base;
        _docRoot = null;
        try {
            File file = _baseResource.getFile();
            if (file != null) {
                _docRoot = file.getCanonicalPath();
                if (_docRoot.endsWith(File.pathSeparator)) _docRoot = _docRoot.substring(0, _docRoot.length() - 1);
            }
        } catch (Exception e) {
            Log.warn(e);
            throw new IllegalArgumentException(base.toString());
        }
    }

    /**
     * @param resourceBase The base resource as a string.
     */
    public void setResourceBase(String resourceBase) {
        try {
            setBaseResource(Resource.newResource(resourceBase));
        } catch (Exception e) {
            Log.warn(e);
            throw new IllegalArgumentException(resourceBase);
        }
    }

    /**
     * @return Returns the mimeTypes.
     */
    public MimeTypes getMimeTypes() {
        return _mimeTypes;
    }

    /**
     * @param mimeTypes The mimeTypes to set.
     */
    public void setMimeTypes(MimeTypes mimeTypes) {
        _mimeTypes = mimeTypes;
    }

    /**
     */
    public void setWelcomeFiles(String[] files) {
        _welcomeFiles = files;
    }

    /**
     * @return The names of the files which the server should consider to be welcome files in this context.
     * @see <a href="http://jcp.org/aboutJava/communityprocess/final/jsr154/index.html">The Servlet Specification</a>
     * @see #setWelcomeFiles
     */
    public String[] getWelcomeFiles() {
        return _welcomeFiles;
    }

    /**
     * @return Returns the errorHandler.
     */
    public ErrorHandler getErrorHandler() {
        return _errorHandler;
    }

    /**
     * @param errorHandler The errorHandler to set.
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler != null) errorHandler.setServer(getServer());
        if (getServer() != null) getServer().getContainer().update(this, _errorHandler, errorHandler, "errorHandler", true);
        _errorHandler = errorHandler;
    }

    public int getMaxFormContentSize() {
        return _maxFormContentSize;
    }

    public void setMaxFormContentSize(int maxSize) {
        _maxFormContentSize = maxSize;
    }

    public String toString() {
        return this.getClass().getName() + "@" + Integer.toHexString(hashCode()) + "{" + getContextPath() + "," + getBaseResource() + "}";
    }

    public synchronized Class loadClass(String className) throws ClassNotFoundException {
        if (className == null) return null;
        if (_classLoader == null) return Loader.loadClass(this.getClass(), className);
        return _classLoader.loadClass(className);
    }

    public void addLocaleEncoding(String locale, String encoding) {
        if (_localeEncodingMap == null) _localeEncodingMap = new HashMap();
        _localeEncodingMap.put(locale, encoding);
    }

    /**
     * Get the character encoding for a locale. The full locale name is first
     * looked up in the map of encodings. If no encoding is found, then the
     * locale language is looked up. 
     *
     * @param locale a <code>Locale</code> value
     * @return a <code>String</code> representing the character encoding for
     * the locale or null if none found.
     */
    public String getLocaleEncoding(Locale locale) {
        if (_localeEncodingMap == null) return null;
        String encoding = (String) _localeEncodingMap.get(locale.toString());
        if (encoding == null) encoding = (String) _localeEncodingMap.get(locale.getLanguage());
        return encoding;
    }

    public Resource getResource(String path) throws MalformedURLException {
        if (path == null || !path.startsWith(URIUtil.SLASH)) throw new MalformedURLException(path);
        if (_baseResource == null) return null;
        try {
            path = URIUtil.canonicalPath(path);
            Resource resource = _baseResource.addPath(path);
            return resource;
        } catch (Exception e) {
            Log.ignore(e);
        }
        return null;
    }

    public Set getResourcePaths(String path) {
        try {
            path = URIUtil.canonicalPath(path);
            Resource resource = getResource(path);
            if (resource != null && resource.exists()) {
                if (!path.endsWith(URIUtil.SLASH)) path = path + URIUtil.SLASH;
                String[] l = resource.list();
                if (l != null) {
                    HashSet set = new HashSet();
                    for (int i = 0; i < l.length; i++) set.add(path + l[i]);
                    return set;
                }
            }
        } catch (Exception e) {
            Log.ignore(e);
        }
        return Collections.EMPTY_SET;
    }

    /** Context.
     * <p>
     * Implements {@link javax.servlet.ServletContext} from the {@link javax.servlet} package.   
     * </p>
     * @author gregw
     *
     */
    public class SContext implements ServletContext {

        protected SContext() {
        }

        public ContextHandler getContextHandler() {
            return ContextHandler.this;
        }

        public ServletContext getContext(String uripath) {
            ContextHandler context = null;
            Handler[] handlers = getServer().getChildHandlersByClass(ContextHandler.class);
            for (int i = 0; i < handlers.length; i++) {
                if (handlers[i] == null || !handlers[i].isStarted()) continue;
                ContextHandler ch = (ContextHandler) handlers[i];
                String context_path = ch.getContextPath();
                if (uripath.equals(context_path) || (uripath.startsWith(context_path) && uripath.charAt(context_path.length()) == '/')) {
                    if (context == null || context_path.length() > context.getContextPath().length()) context = ch;
                }
            }
            if (context != null) return context._scontext;
            return null;
        }

        public int getMajorVersion() {
            return 2;
        }

        public String getMimeType(String file) {
            if (_mimeTypes == null) return null;
            Buffer mime = _mimeTypes.getMimeByExtension(file);
            if (mime != null) return mime.toString();
            return null;
        }

        public int getMinorVersion() {
            return 5;
        }

        public RequestDispatcher getNamedDispatcher(String name) {
            return null;
        }

        public String getRealPath(String path) {
            if (_docRoot == null) return null;
            if (path == null) return null;
            path = URIUtil.canonicalPath(path);
            if (!path.startsWith(URIUtil.SLASH)) path = URIUtil.SLASH + path;
            if (File.separatorChar != '/') path = path.replace('/', File.separatorChar);
            return _docRoot + path;
        }

        public RequestDispatcher getRequestDispatcher(String uriInContext) {
            return null;
        }

        public URL getResource(String path) throws MalformedURLException {
            Resource resource = ContextHandler.this.getResource(path);
            if (resource != null && resource.exists()) return resource.getURL();
            return null;
        }

        public InputStream getResourceAsStream(String path) {
            try {
                URL url = getResource(path);
                if (url == null) return null;
                return url.openStream();
            } catch (Exception e) {
                Log.ignore(e);
                return null;
            }
        }

        public Set getResourcePaths(String path) {
            return ContextHandler.this.getResourcePaths(path);
        }

        public String getServerInfo() {
            return "jetty-" + getServer().getVersion();
        }

        public Servlet getServlet(String name) throws ServletException {
            return null;
        }

        public Enumeration getServletNames() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public Enumeration getServlets() {
            return Collections.enumeration(Collections.EMPTY_LIST);
        }

        public void log(Exception exception, String msg) {
            _logger.warn(msg, exception);
        }

        public void log(String msg) {
            _logger.info(msg, null, null);
        }

        public void log(String message, Throwable throwable) {
            _logger.warn(message, throwable);
        }

        public String getInitParameter(String name) {
            return ContextHandler.this.getInitParameter(name);
        }

        public Enumeration getInitParameterNames() {
            return ContextHandler.this.getInitParameterNames();
        }

        public synchronized Object getAttribute(String name) {
            Object o = ContextHandler.this.getAttribute(name);
            if (o == null) o = _contextAttributes.getAttribute(name);
            return o;
        }

        public synchronized Enumeration getAttributeNames() {
            HashSet set = new HashSet();
            Enumeration e = _contextAttributes.getAttributeNames();
            while (e.hasMoreElements()) set.add(e.nextElement());
            e = ContextHandler.this.getAttributeNames();
            while (e.hasMoreElements()) set.add(e.nextElement());
            return Collections.enumeration(set);
        }

        public synchronized void setAttribute(String name, Object value) {
            if (_contextAttributes == null) return;
            Object old_value = _contextAttributes == null ? null : _contextAttributes.getAttribute(name);
            if (value == null) _contextAttributes.removeAttribute(name); else _contextAttributes.setAttribute(name, value);
            if (_contextAttributeListeners != null) {
                ServletContextAttributeEvent event = new ServletContextAttributeEvent(_scontext, name, old_value == null ? value : old_value);
                for (int i = 0; i < LazyList.size(_contextAttributeListeners); i++) {
                    ServletContextAttributeListener l = (ServletContextAttributeListener) LazyList.get(_contextAttributeListeners, i);
                    if (old_value == null) l.attributeAdded(event); else if (value == null) l.attributeRemoved(event); else l.attributeReplaced(event);
                }
            }
        }

        public synchronized void removeAttribute(String name) {
            Object old_value = _contextAttributes.getAttribute(name);
            _contextAttributes.removeAttribute(name);
            if (old_value != null) {
                if (_contextAttributeListeners != null) {
                    ServletContextAttributeEvent event = new ServletContextAttributeEvent(_scontext, name, old_value);
                    for (int i = 0; i < LazyList.size(_contextAttributeListeners); i++) ((ServletContextAttributeListener) LazyList.get(_contextAttributeListeners, i)).attributeRemoved(event);
                }
            }
        }

        public String getServletContextName() {
            String name = ContextHandler.this.getDisplayName();
            if (name == null) name = ContextHandler.this.getContextPath();
            return name;
        }

        /**
         * @return Returns the _contextPath.
         */
        public String getContextPath() {
            if ((_contextPath != null) && _contextPath.equals(URIUtil.SLASH)) return "";
            return _contextPath;
        }

        public String toString() {
            return "ServletContext@" + Integer.toHexString(hashCode()) + "{" + (getContextPath().equals("") ? URIUtil.SLASH : getContextPath()) + "," + getBaseResource() + "}";
        }
    }
}

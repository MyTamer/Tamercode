package org.ignition.blojsom.plugin.comment;

import org.ignition.blojsom.blog.Blog;
import org.ignition.blojsom.blog.BlogComment;
import org.ignition.blojsom.blog.BlogEntry;
import org.ignition.blojsom.plugin.BlojsomPluginException;
import org.ignition.blojsom.plugin.common.IPBanningPlugin;
import org.ignition.blojsom.plugin.email.EmailUtils;
import org.ignition.blojsom.util.BlojsomConstants;
import org.ignition.blojsom.util.BlojsomUtils;
import javax.servlet.ServletConfig;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * CommentPlugin
 *
 * @author David Czarnecki
 * @version $Id: CommentPlugin.java,v 1.41 2003-08-09 17:00:10 intabulas Exp $
 */
public class CommentPlugin extends IPBanningPlugin {

    /**
     * Default prefix for comment e-mail notification
     */
    private static final String DEFAULT_COMMENT_PREFIX = "[blojsom] Comment on: ";

    /**
     * Initialization parameter for e-mail prefix
     */
    private static final String COMMENT_PREFIX_IP = "plugin-comment-email-prefix";

    /**
     * Initialization parameter for the duration of the "remember me" cookies
     */
    private static final String COMMENT_COOKIE_EXPIRATION_DURATION_IP = "plugin-comment-expiration-duration";

    /**
     * Request parameter for the "comment"
     */
    private static final String COMMENT_PARAM = "comment";

    /**
     * Request parameter for the "author"
     */
    private static final String AUTHOR_PARAM = "author";

    /**
     * Request parameter for the "authorEmail"
     */
    private static final String AUTHOR_EMAIL_PARAM = "authorEmail";

    /**
     * Request parameter for the "authorURL"
     */
    private static final String AUTHOR_URL_PARAM = "authorURL";

    /**
     * Request parameter for the "commentText"
     */
    private static final String COMMENT_TEXT_PARAM = "commentText";

    /**
     * Request parameter to "remember" the poster
     */
    private static final String REMEMBER_ME_PARAM = "remember";

    /**
     * Comment "Remember Me" Cookie for the Authors Name
     */
    private static final String COOKIE_AUTHOR = "blojsom.cookie.author";

    /**
     * Comment "Remember Me" Cookie for the Authors Email
     */
    private static final String COOKIE_EMAIL = "blojsom.cookie.authorEmail";

    /**
     * Comment "Remember Me" Cookie for the Authors URL
     */
    private static final String COOKIE_URL = "blojsom.cookie.authorURL";

    /**
     * Comment "Remember Me" Cookie for the "Remember Me" checkbox
     */
    private static final String COOKIE_REMEMBER_ME = "blojsom.cookie.rememberme";

    /**
     * Expiration age for the cookie (1 week)
     */
    private static final int COOKIE_EXPIRATION_AGE = 604800;

    /**
     * Key under which the indicator this plugin is "live" will be placed
     * (example: on the request for the JSPDispatcher)
     */
    public static final String BLOJSOM_COMMENT_PLUGIN_ENABLED = "BLOJSOM_COMMENT_PLUGIN_ENABLED";

    /**
     * Key under which the author from the "remember me" cookie will be placed
     * (example: on the request for the JSPDispatcher)
     */
    public static final String BLOJSOM_COMMENT_PLUGIN_AUTHOR = "BLOJSOM_COMMENT_PLUGIN_AUTHOR";

    /**
     * Key under which the author's e-mail from the "remember me" cookie will be placed
     * (example: on the request for the JSPDispatcher)
     */
    public static final String BLOJSOM_COMMENT_PLUGIN_AUTHOR_EMAIL = "BLOJSOM_COMMENT_PLUGIN_AUTHOR_EMAIL";

    /**
     * Key under which the author's URL from the "remember me" cookie will be placed
     * (example: on the request for the JSPDispatcher)
     */
    public static final String BLOJSOM_COMMENT_PLUGIN_AUTHOR_URL = "BLOJSOM_COMMENT_PLUGIN_AUTHOR_URL";

    /**
     * Key under which the "remember me" checkbox from the "remember me" cookie will be placed
     * (example: on the request for the JSPDispatcher)
     */
    public static final String BLOJSOM_COMMENT_PLUGIN_REMEMBER_ME = "BLOJSOM_COMMENT_PLUGIN_REMEMBER_ME";

    private Boolean _blogCommentsEnabled;

    private Boolean _blogEmailEnabled;

    private String[] _blogFileExtensions;

    private String _blogHome;

    private String _blogCommentsDirectory;

    private String _blogUrlPrefix;

    private String _blogFileEncoding;

    private String _emailPrefix;

    private int _cookieExpiration;

    /**
     * Initialize this plugin. This method only called when the plugin is instantiated.
     *
     * @param servletConfig Servlet config object for the plugin to retrieve any initialization parameters
     * @param blog {@link Blog} instance
     * @throws BlojsomPluginException If there is an error initializing the plugin
     */
    public void init(ServletConfig servletConfig, Blog blog) throws BlojsomPluginException {
        super.init(servletConfig, blog);
        _blogFileExtensions = blog.getBlogFileExtensions();
        _blogHome = blog.getBlogHome();
        _blogCommentsEnabled = blog.getBlogCommentsEnabled();
        _blogEmailEnabled = blog.getBlogEmailEnabled();
        _blogCommentsDirectory = blog.getBlogCommentsDirectory();
        _blogUrlPrefix = blog.getBlogURL();
        _blogFileEncoding = blog.getBlogFileEncoding();
        _emailPrefix = blog.getBlogProperty(COMMENT_PREFIX_IP);
        if (_emailPrefix == null) {
            _emailPrefix = DEFAULT_COMMENT_PREFIX;
        }
        String cookieExpiration = blog.getBlogProperty(COMMENT_COOKIE_EXPIRATION_DURATION_IP);
        if (cookieExpiration == null || "".equals(cookieExpiration)) {
            _cookieExpiration = COOKIE_EXPIRATION_AGE;
        } else {
            try {
                _cookieExpiration = Integer.parseInt(cookieExpiration);
            } catch (NumberFormatException e) {
                _cookieExpiration = COOKIE_EXPIRATION_AGE;
            }
        }
    }

    /**
     * Return a cookie given a particular key
     *
     * @param httpServletRequest Request
     * @param cookieKey Cookie key
     * @return <code>Cookie</code> of the requested key or <code>null</code> if no cookie
     * under that name is found
     */
    private Cookie getCommentCookie(HttpServletRequest httpServletRequest, String cookieKey) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(cookieKey)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Add a cookie with a key and value to the response
     *
     * @param httpServletResponse Response
     * @param cookieKey Cookie key
     * @param cookieValue Cookie value
     */
    private void addCommentCookie(HttpServletResponse httpServletResponse, String cookieKey, String cookieValue) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setMaxAge(_cookieExpiration);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * Process the blog entries
     *
     * @param httpServletRequest Request
     * @param httpServletResponse Response
     * @param context Context
     * @param entries Blog entries retrieved for the particular request
     * @return Modified set of blog entries
     * @throws BlojsomPluginException If there is an error processing the blog entries
     */
    public BlogEntry[] process(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Map context, BlogEntry[] entries) throws BlojsomPluginException {
        context.put(BLOJSOM_COMMENT_PLUGIN_ENABLED, Boolean.valueOf(true));
        if (entries.length == 0) {
            return entries;
        }
        String remoteIPAddress = httpServletRequest.getRemoteAddr();
        if (isIPBanned(remoteIPAddress)) {
            _logger.debug("Attempted comment from banned IP address: " + remoteIPAddress);
            return entries;
        }
        String author = httpServletRequest.getParameter(AUTHOR_PARAM);
        String authorEmail = httpServletRequest.getParameter(AUTHOR_EMAIL_PARAM);
        String authorURL = httpServletRequest.getParameter(AUTHOR_URL_PARAM);
        String rememberMe = httpServletRequest.getParameter(REMEMBER_ME_PARAM);
        Cookie authorCookie = getCommentCookie(httpServletRequest, COOKIE_AUTHOR);
        if ((authorCookie != null) && ((author == null) || "".equals(author))) {
            author = authorCookie.getValue();
            _logger.debug("Pulling author from cookie: " + author);
            if ("".equals(author)) {
                author = null;
            } else {
                context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR, author);
            }
            Cookie authorEmailCookie = getCommentCookie(httpServletRequest, COOKIE_EMAIL);
            if ((authorEmailCookie != null) && ((authorEmail == null) || "".equals(authorEmail))) {
                authorEmail = authorEmailCookie.getValue();
                _logger.debug("Pulling author email from cookie: " + authorEmail);
                if (authorEmail == null) {
                    authorEmail = "";
                } else {
                    context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR_EMAIL, authorEmail);
                }
            }
            Cookie authorUrlCookie = getCommentCookie(httpServletRequest, COOKIE_URL);
            if ((authorUrlCookie != null) && ((authorURL == null) || "".equals(authorURL))) {
                authorURL = authorUrlCookie.getValue();
                _logger.debug("Pulling author URL from cookie: " + authorURL);
                if (authorURL == null) {
                    authorURL = "";
                } else {
                    context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR_URL, authorURL);
                }
            }
            Cookie rememberMeCookie = getCommentCookie(httpServletRequest, COOKIE_REMEMBER_ME);
            if ((rememberMeCookie != null) && ((rememberMe == null) || "".equals(rememberMe))) {
                rememberMe = rememberMeCookie.getValue();
                if (rememberMe == null) {
                    rememberMe = "";
                } else {
                    context.put(BLOJSOM_COMMENT_PLUGIN_REMEMBER_ME, rememberMe);
                }
            }
        }
        if ("y".equalsIgnoreCase(httpServletRequest.getParameter(COMMENT_PARAM)) && _blogCommentsEnabled.booleanValue()) {
            String commentText = httpServletRequest.getParameter(COMMENT_TEXT_PARAM);
            String permalink = httpServletRequest.getParameter(BlojsomConstants.PERMALINK_PARAM);
            String category = httpServletRequest.getParameter(BlojsomConstants.CATEGORY_PARAM);
            String remember = httpServletRequest.getParameter(REMEMBER_ME_PARAM);
            String title = entries[0].getTitle();
            if ((author != null && !"".equals(author)) && (commentText != null && !"".equals(commentText)) && (permalink != null && !"".equals(permalink)) && (category != null && !"".equals(category))) {
                author = author.trim();
                commentText = commentText.trim();
                if (authorEmail != null) {
                    authorEmail = authorEmail.trim();
                } else {
                    authorEmail = "";
                }
                if (authorURL != null) {
                    authorURL = authorURL.trim();
                } else {
                    authorURL = "";
                }
                if (!category.endsWith("/")) {
                    category += "/";
                }
                if ((authorURL != null) && (!"".equals(authorURL))) {
                    if (!authorURL.toLowerCase().startsWith("http://")) {
                        authorURL = "http://" + authorURL;
                    }
                }
                BlogComment _comment = addBlogComment(category, permalink, author, authorEmail, authorURL, commentText);
                httpServletRequest.getSession().setAttribute(BlojsomConstants.BLOJSOM_LAST_MODIFIED, new Long(new Date().getTime()));
                if (_comment != null) {
                    List blogComments = entries[0].getComments();
                    if (blogComments == null) {
                        blogComments = new ArrayList(1);
                    }
                    blogComments.add(_comment);
                    entries[0].setComments(blogComments);
                    if (_blogEmailEnabled.booleanValue()) {
                        sendCommentEmail(title, category, permalink, author, authorEmail, authorURL, commentText, context);
                    }
                }
                if ((remember != null) && (!"".equals(remember))) {
                    addCommentCookie(httpServletResponse, COOKIE_AUTHOR, author);
                    context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR, author);
                    addCommentCookie(httpServletResponse, COOKIE_EMAIL, authorEmail);
                    context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR_EMAIL, authorEmail);
                    addCommentCookie(httpServletResponse, COOKIE_URL, authorURL);
                    context.put(BLOJSOM_COMMENT_PLUGIN_AUTHOR_URL, authorURL);
                    addCommentCookie(httpServletResponse, COOKIE_REMEMBER_ME, "true");
                    context.put(BLOJSOM_COMMENT_PLUGIN_REMEMBER_ME, "true");
                }
            }
        }
        return entries;
    }

    /**
     * Send Comment Email to Blog Author
     *
     * @param category Blog entry category
     * @param permalink Blog entry permalink
     * @param author Comment author
     * @param authorEmail Comment author e-mail
     * @param authorURL Comment author URL
     * @param userComment Comment
     * @param context Context
     */
    private synchronized void sendCommentEmail(String title, String category, String permalink, String author, String authorEmail, String authorURL, String userComment, Map context) {
        String url = _blogUrlPrefix + BlojsomUtils.removeInitialSlash(category);
        String emailComment = CommentUtils.constructCommentEmail(permalink, author, authorEmail, authorURL, userComment, url);
        EmailUtils.notifyBlogAuthor(_emailPrefix + title, emailComment, context);
    }

    /**
     * Add a comment to a particular blog entry
     *
     * @param category Blog entry category
     * @param permalink Blog entry permalink
     * @param author Comment author
     * @param authorEmail Comment author e-mail
     * @param authorURL Comment author URL
     * @param userComment Comment
     * @return BlogComment Entry
     */
    private synchronized BlogComment addBlogComment(String category, String permalink, String author, String authorEmail, String authorURL, String userComment) {
        BlogComment comment = null;
        if (_blogCommentsEnabled.booleanValue()) {
            userComment = BlojsomUtils.escapeMetaAndLink(userComment);
            comment = new BlogComment();
            comment.setAuthor(author);
            comment.setAuthorEmail(authorEmail);
            comment.setAuthorURL(authorURL);
            comment.setComment(userComment);
            comment.setCommentDate(new Date());
            StringBuffer commentDirectory = new StringBuffer();
            String permalinkFilename = BlojsomUtils.getFilenameForPermalink(permalink, _blogFileExtensions);
            permalinkFilename = BlojsomUtils.urlDecode(permalinkFilename);
            if (permalinkFilename == null) {
                _logger.debug("Invalid permalink comment for: " + permalink);
                return null;
            }
            commentDirectory.append(_blogHome);
            commentDirectory.append(BlojsomUtils.removeInitialSlash(category));
            File blogEntry = new File(commentDirectory.toString() + File.separator + permalinkFilename);
            if (!blogEntry.exists()) {
                _logger.error("Trying to create comment for invalid blog entry: " + permalink);
                return null;
            }
            commentDirectory.append(_blogCommentsDirectory);
            commentDirectory.append(File.separator);
            commentDirectory.append(permalinkFilename);
            commentDirectory.append(File.separator);
            String commentHashable = author + userComment;
            String hashedComment = BlojsomUtils.digestString(commentHashable).toUpperCase();
            String commentFilename = commentDirectory.toString() + hashedComment + BlojsomConstants.COMMENT_EXTENSION;
            File commentDir = new File(commentDirectory.toString());
            if (!commentDir.exists()) {
                if (!commentDir.mkdirs()) {
                    _logger.error("Could not create directory for comments: " + commentDirectory);
                    return null;
                }
            }
            File commentEntry = new File(commentFilename);
            if (!commentEntry.exists()) {
                try {
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(commentEntry), _blogFileEncoding));
                    bw.write(BlojsomUtils.nullToBlank(comment.getAuthor()).trim());
                    bw.newLine();
                    bw.write(BlojsomUtils.nullToBlank(comment.getAuthorEmail()).trim());
                    bw.newLine();
                    bw.write(BlojsomUtils.nullToBlank(comment.getAuthorURL()).trim());
                    bw.newLine();
                    bw.write(BlojsomUtils.nullToBlank(comment.getComment()).trim());
                    bw.newLine();
                    bw.close();
                    _logger.debug("Added blog comment: " + commentFilename);
                } catch (IOException e) {
                    _logger.error(e);
                    return null;
                }
            } else {
                _logger.error("Duplicate comment submission detected, ignoring subsequent submission");
                return null;
            }
        }
        return comment;
    }

    /**
     * Perform any cleanup for the plugin. Called after {@link #process}.
     *
     * @throws BlojsomPluginException If there is an error performing cleanup for this plugin
     */
    public void cleanup() throws BlojsomPluginException {
    }

    /**
     * Called when BlojsomServlet is taken out of service
     *
     * @throws BlojsomPluginException If there is an error in finalizing this plugin
     */
    public void destroy() throws BlojsomPluginException {
    }
}

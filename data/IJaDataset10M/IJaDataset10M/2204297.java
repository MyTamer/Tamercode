package org.kunlong.netix.config.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.descriptor.JspConfigDescriptor;

public class NetixServletContextWarpper implements ServletContext {

    public Dynamic addFilter(String arg0, String arg1) {
        return null;
    }

    public Dynamic addFilter(String arg0, Filter arg1) {
        return null;
    }

    public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
        return null;
    }

    public void addListener(String arg0) {
    }

    public <T extends EventListener> void addListener(T arg0) {
    }

    public void addListener(Class<? extends EventListener> arg0) {
    }

    public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, String arg1) {
        return null;
    }

    public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Servlet arg1) {
        return null;
    }

    public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0, Class<? extends Servlet> arg1) {
        return null;
    }

    public <T extends Filter> T createFilter(Class<T> arg0) throws ServletException {
        return null;
    }

    public <T extends EventListener> T createListener(Class<T> arg0) throws ServletException {
        return null;
    }

    public <T extends Servlet> T createServlet(Class<T> arg0) throws ServletException {
        return null;
    }

    public void declareRoles(String... arg0) {
    }

    public Object getAttribute(String arg0) {
        return null;
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public ClassLoader getClassLoader() {
        return null;
    }

    public ServletContext getContext(String arg0) {
        return null;
    }

    public String getContextPath() {
        return null;
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return null;
    }

    public int getEffectiveMajorVersion() {
        return 0;
    }

    public int getEffectiveMinorVersion() {
        return 0;
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return null;
    }

    public FilterRegistration getFilterRegistration(String arg0) {
        return null;
    }

    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return null;
    }

    public String getInitParameter(String arg0) {
        return null;
    }

    public Enumeration<String> getInitParameterNames() {
        return null;
    }

    public JspConfigDescriptor getJspConfigDescriptor() {
        return null;
    }

    public int getMajorVersion() {
        return 0;
    }

    public String getMimeType(String arg0) {
        return null;
    }

    public int getMinorVersion() {
        return 0;
    }

    public RequestDispatcher getNamedDispatcher(String arg0) {
        return null;
    }

    public String getRealPath(String arg0) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String arg0) {
        return null;
    }

    public URL getResource(String arg0) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String arg0) {
        return null;
    }

    public Set<String> getResourcePaths(String arg0) {
        return null;
    }

    public String getServerInfo() {
        return null;
    }

    public Servlet getServlet(String arg0) throws ServletException {
        return null;
    }

    public String getServletContextName() {
        return null;
    }

    public Enumeration<String> getServletNames() {
        return null;
    }

    public ServletRegistration getServletRegistration(String arg0) {
        return null;
    }

    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return null;
    }

    public Enumeration<Servlet> getServlets() {
        return null;
    }

    public SessionCookieConfig getSessionCookieConfig() {
        return null;
    }

    public void log(String arg0) {
    }

    public void log(Exception arg0, String arg1) {
    }

    public void log(String arg0, Throwable arg1) {
    }

    public void removeAttribute(String arg0) {
    }

    public void setAttribute(String arg0, Object arg1) {
    }

    public boolean setInitParameter(String arg0, String arg1) {
        return false;
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> arg0) {
    }
}

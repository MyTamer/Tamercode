package org.springframework.web.servlet.mvc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.TestBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.mvc.multiaction.ParameterMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver;
import org.springframework.web.servlet.support.SessionRequiredException;

/**
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Colin Sampaleanu
 */
public class MultiActionControllerTests extends TestCase {

    public void testDefaultInternalPathMethodNameResolver() throws Exception {
        doDefaultTestInternalPathMethodNameResolver("/foo.html", "foo");
        doDefaultTestInternalPathMethodNameResolver("/foo/bar.html", "bar");
        doDefaultTestInternalPathMethodNameResolver("/bugal.xyz", "bugal");
        doDefaultTestInternalPathMethodNameResolver("/x/y/z/q/foo.html", "foo");
        doDefaultTestInternalPathMethodNameResolver("qqq.q", "qqq");
    }

    private void doDefaultTestInternalPathMethodNameResolver(String in, String expected) throws Exception {
        MultiActionController rc = new MultiActionController();
        HttpServletRequest request = new MockHttpServletRequest("GET", in);
        String actual = rc.getMethodNameResolver().getHandlerMethodName(request);
        assertEquals("Wrong method name resolved", expected, actual);
    }

    public void testCustomizedInternalPathMethodNameResolver() throws Exception {
        doTestCustomizedInternalPathMethodNameResolver("/foo.html", "my", null, "myfoo");
        doTestCustomizedInternalPathMethodNameResolver("/foo/bar.html", null, "Handler", "barHandler");
        doTestCustomizedInternalPathMethodNameResolver("/Bugal.xyz", "your", "Method", "yourBugalMethod");
    }

    private void doTestCustomizedInternalPathMethodNameResolver(String in, String prefix, String suffix, String expected) throws Exception {
        MultiActionController rc = new MultiActionController();
        InternalPathMethodNameResolver resolver = new InternalPathMethodNameResolver();
        if (prefix != null) {
            resolver.setPrefix(prefix);
        }
        if (suffix != null) {
            resolver.setSuffix(suffix);
        }
        rc.setMethodNameResolver(resolver);
        HttpServletRequest request = new MockHttpServletRequest("GET", in);
        String actual = rc.getMethodNameResolver().getHandlerMethodName(request);
        assertEquals("Wrong method name resolved", expected, actual);
    }

    public void testParameterMethodNameResolver() throws NoSuchRequestHandlingMethodException {
        ParameterMethodNameResolver mnr = new ParameterMethodNameResolver();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo.html");
        request.addParameter("action", "bar");
        assertEquals("bar", mnr.getHandlerMethodName(request));
        request = new MockHttpServletRequest("GET", "/foo.html");
        try {
            mnr.getHandlerMethodName(request);
        } catch (NoSuchRequestHandlingMethodException ex) {
        }
    }

    public void testParameterMethodNameResolverWithCustomParamName() throws NoSuchRequestHandlingMethodException {
        ParameterMethodNameResolver mnr = new ParameterMethodNameResolver();
        mnr.setParamName("myparam");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo.html");
        request.addParameter("myparam", "bar");
        assertEquals("bar", mnr.getHandlerMethodName(request));
    }

    public void testParameterMethodNameResolverWithParamNames() throws NoSuchRequestHandlingMethodException {
        ParameterMethodNameResolver resolver = new ParameterMethodNameResolver();
        resolver.setDefaultMethodName("default");
        resolver.setMethodParamNames(new String[] { "hello", "spring", "colin" });
        Properties logicalMappings = new Properties();
        logicalMappings.setProperty("hello", "goodbye");
        logicalMappings.setProperty("nina", "colin");
        resolver.setLogicalMappings(logicalMappings);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("nomatch", "whatever");
        try {
            resolver.getHandlerMethodName(request);
        } catch (NoSuchRequestHandlingMethodException ex) {
        }
        request = new MockHttpServletRequest();
        request.addParameter("this will not match anything", "whatever");
        assertEquals("default", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("action", "reset");
        assertEquals("reset", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("action", "nina");
        assertEquals("colin", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("hello", "whatever");
        assertEquals("goodbye", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("spring", "whatever");
        assertEquals("spring", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("hello", "whatever");
        request.addParameter("spring", "whatever");
        assertEquals("goodbye", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("colin", "whatever");
        request.addParameter("spring", "whatever");
        assertEquals("spring", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("spring.x", "whatever");
        assertEquals("spring", resolver.getHandlerMethodName(request));
        request = new MockHttpServletRequest();
        request.addParameter("hello.x", "whatever");
        request.addParameter("spring", "whatever");
        assertEquals("goodbye", resolver.getHandlerMethodName(request));
    }

    public void testParameterMethodNameResolverWithDefaultMethodName() throws NoSuchRequestHandlingMethodException {
        ParameterMethodNameResolver mnr = new ParameterMethodNameResolver();
        mnr.setDefaultMethodName("foo");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo.html");
        request.addParameter("action", "bar");
        assertEquals("bar", mnr.getHandlerMethodName(request));
        request = new MockHttpServletRequest("GET", "/foo.html");
        assertEquals("foo", mnr.getHandlerMethodName(request));
    }

    public void testInvokesCorrectMethod() throws Exception {
        TestMaController mc = new TestMaController();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/welcome.html");
        HttpServletResponse response = new MockHttpServletResponse();
        Properties p = new Properties();
        p.put("/welcome.html", "welcome");
        PropertiesMethodNameResolver mnr = new PropertiesMethodNameResolver();
        mnr.setMappings(p);
        mc.setMethodNameResolver(mnr);
        ModelAndView mv = mc.handleRequest(request, response);
        assertTrue("Invoked welcome method", mc.wasInvoked("welcome"));
        assertTrue("view name is welcome", mv.getViewName().equals("welcome"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
        mc = new TestMaController();
        request = new MockHttpServletRequest("GET", "/subdir/test.html");
        response = new MockHttpServletResponse();
        mv = mc.handleRequest(request, response);
        assertTrue("Invoked test method", mc.wasInvoked("test"));
        assertTrue("view name is subdir_test", mv.getViewName().equals("test"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
    }

    public void testPathMatching() throws Exception {
        TestMaController mc = new TestMaController();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/welcome.html");
        HttpServletResponse response = new MockHttpServletResponse();
        Properties p = new Properties();
        p.put("/welc*.html", "welcome");
        PropertiesMethodNameResolver mn = new PropertiesMethodNameResolver();
        mn.setMappings(p);
        mc.setMethodNameResolver(mn);
        ModelAndView mv = mc.handleRequest(request, response);
        assertTrue("Invoked welcome method", mc.wasInvoked("welcome"));
        assertTrue("view name is welcome", mv.getViewName().equals("welcome"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
        mc = new TestMaController();
        mc.setMethodNameResolver(mn);
        request = new MockHttpServletRequest("GET", "/nomatch");
        response = new MockHttpServletResponse();
        try {
            mv = mc.handleRequest(request, response);
        } catch (Exception e) {
        }
        assertFalse("Not invoking welcome method", mc.wasInvoked("welcome"));
        assertTrue("No method invoked", mc.getInvokedMethods() == 0);
    }

    public static class TestDelegate {

        boolean invoked;

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            invoked = true;
            return new ModelAndView("test");
        }
    }

    public void testInvokesCorrectMethodOnDelegate() throws Exception {
        MultiActionController mac = new MultiActionController();
        TestDelegate d = new TestDelegate();
        mac.setDelegate(d);
        HttpServletRequest request = new MockHttpServletRequest("GET", "/test.html");
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = mac.handleRequest(request, response);
        assertTrue("view name is test", mv.getViewName().equals("test"));
        assertTrue("Delegate was invoked", d.invoked);
    }

    public void testInvokesCorrectMethodWithSession() throws Exception {
        TestMaController mc = new TestMaController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/inSession.html");
        request.setSession(new MockHttpSession(null));
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = mc.handleRequest(request, response);
        assertTrue("Invoked inSession method", mc.wasInvoked("inSession"));
        assertTrue("view name is welcome", mv.getViewName().equals("inSession"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
        request = new MockHttpServletRequest("GET", "/inSession.html");
        response = new MockHttpServletResponse();
        try {
            mc.handleRequest(request, response);
            fail("Should have rejected request without session");
        } catch (ServletException ex) {
        }
    }

    public void testInvokesCommandMethodNoSession() throws Exception {
        TestMaController mc = new TestMaController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/commandNoSession.html");
        request.addParameter("name", "rod");
        request.addParameter("age", "32");
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = mc.handleRequest(request, response);
        assertTrue("Invoked commandNoSession method", mc.wasInvoked("commandNoSession"));
        assertTrue("view name is commandNoSession", mv.getViewName().equals("commandNoSession"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
    }

    public void testInvokesCommandMethodWithSession() throws Exception {
        TestMaController mc = new TestMaController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/commandInSession.html");
        request.addParameter("name", "rod");
        request.addParameter("age", "32");
        request.setSession(new MockHttpSession(null));
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = mc.handleRequest(request, response);
        assertTrue("Invoked commandInSession method", mc.wasInvoked("commandInSession"));
        assertTrue("view name is commandInSession", mv.getViewName().equals("commandInSession"));
        assertTrue("Only one method invoked", mc.getInvokedMethods() == 1);
        request = new MockHttpServletRequest("GET", "/commandInSession.html");
        response = new MockHttpServletResponse();
        try {
            mc.handleRequest(request, response);
            fail("Should have rejected request without session");
        } catch (ServletException ex) {
        }
    }

    public void testSessionRequiredCatchable() throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/testSession.html");
        HttpServletResponse response = new MockHttpServletResponse();
        TestMaController contr = new TestSessionRequiredController();
        try {
            contr.handleRequest(request, response);
            fail("Should have thrown exception");
        } catch (SessionRequiredException ex) {
        }
        request = new MockHttpServletRequest("GET", "/testSession.html");
        response = new MockHttpServletResponse();
        contr = new TestSessionRequiredExceptionHandler();
        ModelAndView mv = contr.handleRequest(request, response);
        assertTrue("Name is ok", mv.getViewName().equals("handle(SRE)"));
    }

    private void testExceptionNoHandler(TestMaController mc, Throwable t) throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/testException.html");
        request.setAttribute(TestMaController.THROWABLE_ATT, t);
        HttpServletResponse response = new MockHttpServletResponse();
        try {
            mc.handleRequest(request, response);
            fail("Should have thrown exception");
        } catch (Throwable ex) {
            assertTrue(ex.equals(t));
        }
    }

    private void testExceptionNoHandler(Throwable t) throws Exception {
        testExceptionNoHandler(new TestMaController(), t);
    }

    public void testExceptionNoHandler() throws Exception {
        testExceptionNoHandler(new Exception());
        testExceptionNoHandler(new ServletException());
        testExceptionNoHandler(new ServletRequestBindingException("foo"));
        testExceptionNoHandler(new RuntimeException());
        testExceptionNoHandler(new Error());
    }

    public void testLastModifiedDefault() throws Exception {
        TestMaController mc = new TestMaController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/welcome.html");
        long lastMod = mc.getLastModified(request);
        assertTrue("default last modified is -1", lastMod == -1L);
    }

    public void testLastModifiedWithMethod() throws Exception {
        LastModController mc = new LastModController();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/welcome.html");
        long lastMod = mc.getLastModified(request);
        assertTrue("last modified with method is > -1", lastMod == mc.getLastModified(request));
    }

    private ModelAndView testHandlerCaughtException(TestMaController mc, Throwable t) throws Exception {
        HttpServletRequest request = new MockHttpServletRequest("GET", "/testException.html");
        request.setAttribute(TestMaController.THROWABLE_ATT, t);
        HttpServletResponse response = new MockHttpServletResponse();
        ModelAndView mv = mc.handleRequest(request, response);
        return mv;
    }

    public void testHandlerCaughtException() throws Exception {
        TestMaController mc = new TestExceptionHandler();
        ModelAndView mv = testHandlerCaughtException(mc, new Exception());
        assertTrue("mv name is handle(Exception)", mv.getViewName().equals("handle(Exception)"));
        assertTrue("Invoked correct method", mc.wasInvoked("handle(Exception)"));
        testExceptionNoHandler(mc, new Error());
        mc = new TestServletExceptionHandler();
        mv = testHandlerCaughtException(mc, new ServletException());
        assertTrue(mv.getViewName().equals("handle(ServletException)"));
        assertTrue("Invoke correct method", mc.wasInvoked("handle(ServletException)"));
        mv = testHandlerCaughtException(mc, new ServletRequestBindingException("foo"));
        assertTrue(mv.getViewName().equals("handle(ServletException)"));
        assertTrue("Invoke correct method", mc.wasInvoked("handle(ServletException)"));
        testExceptionNoHandler(mc, new RuntimeException());
        testExceptionNoHandler(mc, new Error());
        testExceptionNoHandler(mc, new SQLException());
        testExceptionNoHandler(mc, new Exception());
        mc = new TestRuntimeExceptionHandler();
        mv = testHandlerCaughtException(mc, new RuntimeException());
        assertTrue(mv.getViewName().equals("handle(RTE)"));
        assertTrue("Invoke correct method", mc.wasInvoked("handle(RTE)"));
        mv = testHandlerCaughtException(mc, new FatalBeanException(null, null));
        assertTrue(mv.getViewName().equals("handle(RTE)"));
        assertTrue("Invoke correct method", mc.wasInvoked("handle(RTE)"));
        testExceptionNoHandler(mc, new SQLException());
        testExceptionNoHandler(mc, new Exception());
    }

    /** No error handlers */
    public static class TestMaController extends MultiActionController {

        public static final String THROWABLE_ATT = "throwable";

        /** Method name -> object */
        protected Map invoked = new HashMap();

        public void clear() {
            invoked.clear();
        }

        public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
            invoked.put("welcome", Boolean.TRUE);
            return new ModelAndView("welcome");
        }

        public ModelAndView commandNoSession(HttpServletRequest request, HttpServletResponse response, TestBean command) {
            invoked.put("commandNoSession", Boolean.TRUE);
            String pname = request.getParameter("name");
            String page = request.getParameter("age");
            if (pname == null) assertTrue("name null", command.getName() == null); else assertTrue("name param set", pname.equals(command.getName()));
            return new ModelAndView("commandNoSession");
        }

        public ModelAndView inSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
            invoked.put("inSession", Boolean.TRUE);
            assertTrue("session non null", session != null);
            return new ModelAndView("inSession");
        }

        public ModelAndView commandInSession(HttpServletRequest request, HttpServletResponse response, HttpSession session, TestBean command) {
            invoked.put("commandInSession", Boolean.TRUE);
            assertTrue("session non null", session != null);
            return new ModelAndView("commandInSession");
        }

        public ModelAndView test(HttpServletRequest request, HttpServletResponse response) {
            invoked.put("test", Boolean.TRUE);
            return new ModelAndView("test");
        }

        public ModelAndView testException(HttpServletRequest request, HttpServletResponse response) throws Throwable {
            invoked.put("testException", Boolean.TRUE);
            Throwable t = (Throwable) request.getAttribute(THROWABLE_ATT);
            if (t != null) throw t; else return new ModelAndView("no throwable");
        }

        public boolean wasInvoked(String method) {
            return invoked.get(method) != null;
        }

        public int getInvokedMethods() {
            return invoked.size();
        }
    }

    public static class TestExceptionHandler extends TestMaController {

        public ModelAndView handleAnyException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
            invoked.put("handle(Exception)", Boolean.TRUE);
            return new ModelAndView("handle(Exception)");
        }
    }

    public static class TestRuntimeExceptionHandler extends TestMaController {

        public ModelAndView handleRuntimeProblem(HttpServletRequest request, HttpServletResponse response, RuntimeException ex) {
            invoked.put("handle(RTE)", Boolean.TRUE);
            return new ModelAndView("handle(RTE)");
        }
    }

    public static class TestSessionRequiredController extends TestMaController {

        public ModelAndView testSession(HttpServletRequest request, HttpServletResponse response, HttpSession sess) {
            return null;
        }
    }

    /** Extends previous to handle exception */
    public static class TestSessionRequiredExceptionHandler extends TestSessionRequiredController {

        public ModelAndView handleServletException(HttpServletRequest request, HttpServletResponse response, SessionRequiredException ex) {
            invoked.put("handle(SRE)", Boolean.TRUE);
            return new ModelAndView("handle(SRE)");
        }
    }

    public static class TestServletExceptionHandler extends TestMaController {

        public ModelAndView handleServletException(HttpServletRequest request, HttpServletResponse response, ServletException ex) {
            invoked.put("handle(ServletException)", Boolean.TRUE);
            return new ModelAndView("handle(ServletException)");
        }
    }

    public static class LastModController extends MultiActionController {

        public static final String THROWABLE_ATT = "throwable";

        /** Method name -> object */
        protected HashMap invoked = new HashMap();

        public void clear() {
            invoked.clear();
        }

        public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response) {
            invoked.put("welcome", Boolean.TRUE);
            return new ModelAndView("welcome");
        }

        /** Always says content is up to date */
        public long welcomeLastModified(HttpServletRequest request) {
            return 1111L;
        }
    }
}

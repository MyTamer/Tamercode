package fr.cnes.sitools;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.engine.Engine;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import fr.cnes.sitools.api.DocAPI;
import fr.cnes.sitools.api.DocWadl;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.server.Consts;
import fr.cnes.sitools.util.FileUtils;

/**
 * Classe de base pour les tests unitaires des Serveurs/Applications Restlet et de la persistance des données Permet
 * d'utiliser un port en paramètre (1337 par défaut)
 * 
 * @author AKKA Technologies
 * @see org.restlet.test.RestletTestCase
 */
public abstract class AbstractSitoolsTestCase {

    /**
   * common logger for all tests
   */
    public static final Logger LOGGER = Logger.getLogger(AbstractSitoolsTestCase.class.getName());

    /**
   * BASE URL of global Sitools application
   */
    public static final String SITOOLS_URL = SitoolsSettings.getInstance().getString(Consts.APP_URL);

    /**
   * Root path for storing files
   */
    public static final String TEST_FILES_REPOSITORY = SitoolsSettings.getInstance().getString("Starter.ROOT_DIRECTORY") + SitoolsSettings.getInstance().getString("Tests.STORE_DIR");

    /**
   * Default test port for all tests
   */
    public static final int DEFAULT_TEST_PORT = 1337;

    /**
   * Helper for generating API documentation
   */
    protected static DocAPI docAPI;

    /**
   * system property name for test port
   */
    private static final String PROPERTY_TEST_PORT = "sitools.test.port";

    /**
   * MEDIA type to be set in concrete subclasses of test case
   */
    private static MediaType mediaTest = MediaType.APPLICATION_XML;

    /**
   * TITLE for DocAPI of the test case
   */
    private static String titleTest = "TEST " + new Date().toString();

    /**
   * Class for DocAPI of the test case
   */
    private static Class<? extends AbstractSitoolsTestCase> classTest = AbstractSitoolsTestCase.class;

    /**
   * Port for test defined in this order : 1. System property sitools.test.port 2. default test port (1337)
   * 
   * @return test port
   */
    public static int getTestPort() {
        if (System.getProperties().containsKey(PROPERTY_TEST_PORT)) {
            return Integer.parseInt(System.getProperty(PROPERTY_TEST_PORT));
        }
        return DEFAULT_TEST_PORT;
    }

    /**
   * Gets the mediaTest value
   * 
   * @return the mediaTest
   */
    public static MediaType getMediaTest() {
        return mediaTest;
    }

    /**
   * Sets the value of mediaTest
   * 
   * @param mediaTest
   *          the mediaTest to set
   */
    public static void setMediaTest(MediaType mediaTest) {
        AbstractSitoolsTestCase.mediaTest = mediaTest;
    }

    /**
   * Try to remove files from directory
   * 
   * @param dir
   *          directory to be cleaned
   */
    public static void cleanDirectory(File dir) {
        if (dir == null) {
            LOGGER.warning("Null directory");
            return;
        }
        LOGGER.info("Clean XML files from directory: " + dir.getAbsolutePath());
        try {
            FileUtils.cleanDirectory(dir, new String[] { "xml" }, false);
        } catch (IOException e) {
            Logger.getLogger(AbstractSitoolsTestCase.class.getName()).warning("Unable to clean " + dir.getPath() + "\n cause:" + e.getMessage());
        }
    }

    /**
   * Absolute path location for data files
   * 
   * @return path
   */
    protected String getTestRepository() {
        return TEST_FILES_REPOSITORY;
    }

    /**
   * absolute url for sitools REST API
   * 
   * @return url
   */
    protected String getBaseUrl() {
        return "http://localhost:" + getTestPort() + SITOOLS_URL;
    }

    /**
   * To be executed before every tests methods.
   * 
   * @throws Exception
   *           if failed
   */
    @Before
    protected void setUp() throws Exception {
        Engine.clearThreadLocalVariables();
    }

    /**
   * To be executed after every tests methods.
   * 
   * @throws Exception
   *           if failed
   */
    @After
    protected void tearDown() throws Exception {
        Engine.clearThreadLocalVariables();
    }

    /**
   * Trace global parameters for the test.
   */
    @Test
    public void testConfig() {
        LOGGER.info(this.getClass().getName() + " TEST BASE URL = " + getBaseUrl());
        LOGGER.info(this.getClass().getName() + " TEST REPOSITORY = " + getTestRepository());
        LOGGER.info(this.getClass().getName() + " TEST PORT = " + getTestPort());
        LOGGER.info(this.getClass().getName() + " TEST MEDIATYPE = " + getMediaTest());
        assertTrue("Check data directory presence", (new File(getTestRepository()).exists()));
    }

    /**
   * Sets the title of the test for doc API
   * 
   * @param title
   *          title of the test
   */
    public static void setTitleTest(String title) {
        titleTest = title;
    }

    /**
   * Sets the class of the test for doc API
   * 
   * @param aClass
   *          class name
   */
    public static void setClassTest(Class<? extends AbstractSitoolsTestCase> aClass) {
        classTest = aClass;
    }

    /**
   * Invoke GET
   * 
   * @param uri
   *          String
   * @param params
   *          String
   * @param parameters
   *          Map<String, String>
   * @param uriTemplate
   *          String
   */
    public void retrieveDocAPI(String uri, String params, Map<String, String> parameters, String uriTemplate) {
        ClientResource cr = new ClientResource(uri);
        System.out.println("URI: " + uriTemplate);
        Representation result = cr.get(getMediaTest());
        docAPI.appendSection("Format");
        ClientResource crLocal = new ClientResource(uriTemplate);
        docAPI.appendRequest(Method.GET, crLocal);
        docAPI.appendParameters(parameters);
        docAPI.appendSection("Example");
        docAPI.appendRequest(Method.GET, cr);
        docAPI.appendResponse(result);
    }

    /**
   * Invoke POST
   * 
   * @param uri
   *          String
   * @param params
   *          String
   * @param object
   *          Representation
   * @param parameters
   *          Map<String, String>
   * @param uriTemplate
   *          String
   */
    public void postDocAPI(String uri, String params, Representation object, Map<String, String> parameters, String uriTemplate) {
        ClientResource cr = new ClientResource(uri);
        System.out.println("URI: " + uriTemplate);
        docAPI.appendSection("Format");
        ClientResource crLocal = new ClientResource(uriTemplate);
        docAPI.appendRequest(Method.POST, crLocal);
        docAPI.appendParameters(parameters);
        docAPI.appendSection("Example");
        docAPI.appendRequest(Method.POST, cr, object);
        Representation result = cr.post(object, getMediaTest());
        docAPI.appendResponse(result);
    }

    /**
   * Invoke PUT
   * 
   * @param uri
   *          String
   * @param params
   *          String
   * @param object
   *          Representation
   * @param parameters
   *          Map<String, String>
   * @param uriTemplate
   *          String
   */
    public void putDocAPI(String uri, String params, Representation object, Map<String, String> parameters, String uriTemplate) {
        ClientResource cr = new ClientResource(uri);
        System.out.println("URI: " + uriTemplate);
        docAPI.appendSection("Format");
        ClientResource crLocal = new ClientResource(uriTemplate);
        docAPI.appendRequest(Method.PUT, crLocal);
        docAPI.appendParameters(parameters);
        docAPI.appendSection("Example");
        docAPI.appendRequest(Method.PUT, cr, object);
        Representation result = cr.put(object, getMediaTest());
        docAPI.appendResponse(result);
    }

    /**
   * Invoke PUT
   * 
   * @param uri
   *          String
   * @param params
   *          String
   * @param parameters
   *          Map<String, String>
   * @param uriTemplate
   *          String
   */
    public void deleteDocAPI(String uri, String params, Map<String, String> parameters, String uriTemplate) {
        ClientResource cr = new ClientResource(uri);
        Representation result = cr.delete(getMediaTest());
        docAPI.appendSection("Format");
        ClientResource crLocal = new ClientResource(uriTemplate);
        docAPI.appendRequest(Method.DELETE, crLocal);
        docAPI.appendParameters(parameters);
        docAPI.appendSection("Example");
        docAPI.appendRequest(Method.DELETE, cr);
        docAPI.appendResponse(result);
    }

    /**
   * Create WADL documentation
   * 
   * @param url
   *          application url
   * @param docPath
   *          repository name
   */
    protected void createWadl(String url, String docPath) {
        ClientResource cr = new ClientResource(url);
        DocWadl dw = new DocWadl(docPath);
        try {
            cr.options().write(dw.getWadlPrintStream());
            cr.options(MediaType.TEXT_HTML).write(dw.getHtmlPrintStream());
        } catch (ResourceException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

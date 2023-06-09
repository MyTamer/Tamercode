package org.opencms.importexport;

import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProject;
import org.opencms.file.CmsResource;
import org.opencms.file.CmsResourceFilter;
import org.opencms.file.CmsUser;
import org.opencms.file.types.CmsResourceTypeFolder;
import org.opencms.file.types.CmsResourceTypeImage;
import org.opencms.file.types.CmsResourceTypeJsp;
import org.opencms.file.types.CmsResourceTypePlain;
import org.opencms.file.types.CmsResourceTypeXmlPage;
import org.opencms.file.types.I_CmsResourceType;
import org.opencms.i18n.CmsEncoder;
import org.opencms.i18n.CmsLocaleManager;
import org.opencms.lock.CmsLockFilter;
import org.opencms.main.OpenCms;
import org.opencms.relations.CmsCategory;
import org.opencms.relations.CmsCategoryService;
import org.opencms.relations.CmsLink;
import org.opencms.relations.CmsRelation;
import org.opencms.relations.CmsRelationFilter;
import org.opencms.relations.CmsRelationType;
import org.opencms.relations.I_CmsLinkParseable;
import org.opencms.report.CmsShellReport;
import org.opencms.security.CmsDefaultPasswordHandler;
import org.opencms.security.I_CmsPasswordHandler;
import org.opencms.security.I_CmsPrincipal;
import org.opencms.staticexport.CmsLinkTable;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;
import org.opencms.test.OpenCmsTestResourceConfigurableFilter;
import org.opencms.test.OpenCmsTestResourceFilter;
import org.opencms.util.CmsDateUtil;
import org.opencms.util.CmsResourceTranslator;
import org.opencms.util.CmsStringUtil;
import org.opencms.xml.CmsXmlEntityResolver;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.page.CmsXmlPage;
import org.opencms.xml.page.CmsXmlPageFactory;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Comment for <code>TestCmsImportExport</code>.<p>
 */
public class TestCmsImportExport extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestCmsImportExport(String arg0) {
        super(arg0);
    }

    /**
     * Test suite for this test class.<p>
     * 
     * @return the test suite
     */
    public static Test suite() {
        OpenCmsTestProperties.initialize(org.opencms.test.AllTests.TEST_PROPERTIES_PATH);
        TestSuite suite = new TestSuite();
        suite.setName(TestCmsImportExport.class.getName());
        suite.addTest(new TestCmsImportExport("testImportValidation"));
        suite.addTest(new TestCmsImportExport("testImportSiblingIssue"));
        suite.addTest(new TestCmsImportExport("testImportPermissionIssue"));
        suite.addTest(new TestCmsImportExport("testImportMovedFolder"));
        suite.addTest(new TestCmsImportExport("testImportWrongSite"));
        suite.addTest(new TestCmsImportExport("testSetup"));
        suite.addTest(new TestCmsImportExport("testUserImport"));
        suite.addTest(new TestCmsImportExport("testImportExportFolder"));
        suite.addTest(new TestCmsImportExport("testImportExportId"));
        suite.addTest(new TestCmsImportExport("testImportExportBrokenLinksHtml"));
        suite.addTest(new TestCmsImportExport("testImportExportBrokenLinksXml"));
        suite.addTest(new TestCmsImportExport("testImportResourceTranslator"));
        suite.addTest(new TestCmsImportExport("testImportResourceTranslatorMultipleSite"));
        suite.addTest(new TestCmsImportExport("testImportRecreatedFile"));
        suite.addTest(new TestCmsImportExport("testImportSibling"));
        suite.addTest(new TestCmsImportExport("testImportRecreatedSibling"));
        suite.addTest(new TestCmsImportExport("testImportMovedResource"));
        suite.addTest(new TestCmsImportExport("testImportChangedContent"));
        suite.addTest(new TestCmsImportExport("testImportRelations"));
        suite.addTest(new TestCmsImportExport("testImportContentIssue"));
        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                setupOpenCms("simpletest", "/sites/default/");
            }

            protected void tearDown() {
                removeOpenCms();
            }
        };
        return wrapper;
    }

    /**
     * Tests the import of a resource that has been edited.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportChangedContent() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource that has been edited.");
        String filename1 = "/newfile6.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportChangedContent.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource res1 = cms.createResource(filename1, CmsResourceTypePlain.getStaticTypeId());
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            String content = "new content";
            CmsFile file = new CmsFile(cms.readResource(filename1));
            file.setContents(content.getBytes());
            cms.lockResource(filename1);
            cms.writeFile(file);
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFilter(cms, cms.readResource(filename1), OpenCmsTestResourceFilter.FILTER_REPLACERESOURCE);
            cms.readResource(res1.getStructureId());
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            assertFilter(cms, cms.readResource(filename1), OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT);
            cms.getRequestContext().setCurrentProject(cms.readProject(CmsProject.ONLINE_PROJECT_ID));
            assertFilter(cms, filename1, OpenCmsTestResourceFilter.FILTER_EQUAL);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource overwriting a sibling with different type.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportContentIssue() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource overwriting a sibling with different type.");
        String filename = "/newfileContentIssue.html";
        String sibname = "/system/sib.jsp";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportContentIssue.zip");
        String site = cms.getRequestContext().getSiteRoot();
        try {
            cms.createResource(filename, CmsResourceTypeJsp.getStaticTypeId(), "hello jsp".getBytes(), null);
            cms.unlockResource(filename);
            cms.getRequestContext().setSiteRoot("/");
            cms.createSibling(site + filename, sibname, null);
            cms.unlockResource(sibname);
            OpenCms.getPublishManager().publishResource(cms, sibname, true, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, site + filename);
            storeResources(cms, sibname);
            cms.getRequestContext().setSiteRoot(site);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename);
            cms.deleteResource(filename, CmsResource.DELETE_PRESERVE_SIBLINGS);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            cms.createResource(filename, CmsResourceTypePlain.getStaticTypeId(), "hello txt".getBytes(), null);
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename, true, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getPublishManager().waitWhileRunning();
            cms.getRequestContext().setSiteRoot("/");
            OpenCmsTestResourceConfigurableFilter filter = new OpenCmsTestResourceConfigurableFilter(OpenCmsTestResourceFilter.FILTER_EQUAL);
            filter.disableSiblingCountTest();
            assertFilter(cms, cms.readResource(sibname), filter);
            filter = new OpenCmsTestResourceConfigurableFilter(OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT);
            filter.disableSiblingCountTest();
            filter.disableDateCreatedTest();
            filter.disableDateCreatedSecTest();
            filter.disableResourceIdTest();
            filter.disableStructureIdTest();
            assertFilter(cms, cms.readResource(site + filename), filter);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of linked XmlPages in a different site, so that the link paths get broken.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportExportBrokenLinksHtml() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of linked XmlPages in a different site, so that the link paths get broken.");
        String filename1 = "xmlpage1.html";
        String filename2 = "xmlpage2.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportBrokenLinks.zip");
        String storedSiteRoot = cms.getRequestContext().getSiteRoot();
        try {
            cms.getRequestContext().setSiteRoot("/sites/default/");
            CmsResource res1 = cms.createResource(filename1, CmsResourceTypeXmlPage.getStaticTypeId());
            CmsResource res2 = cms.createResource(filename2, CmsResourceTypeXmlPage.getStaticTypeId());
            CmsFile file2 = cms.readFile(res2);
            CmsXmlPage page2 = CmsXmlPageFactory.unmarshal(cms, file2, true);
            page2.addValue("test", Locale.ENGLISH);
            page2.setStringValue(cms, "test", Locale.ENGLISH, "<a href='" + filename1 + "'>test</a>");
            file2.setContents(page2.marshal());
            cms.writeFile(file2);
            CmsFile file1 = cms.readFile(res1);
            CmsXmlPage page1 = CmsXmlPageFactory.unmarshal(cms, file1, true);
            page1.addValue("test", Locale.ENGLISH);
            page1.setStringValue(cms, "test", Locale.ENGLISH, "<a href='" + filename2 + "'>test</a>");
            file1.setContents(page1.marshal());
            cms.writeFile(file1);
            cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
            OpenCms.getPublishManager().publishProject(cms);
            OpenCms.getPublishManager().waitWhileRunning();
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            exportPaths.add(filename2);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename1);
            cms.lockResource(filename2);
            cms.deleteResource(filename1, CmsResource.DELETE_REMOVE_SIBLINGS);
            cms.deleteResource(filename2, CmsResource.DELETE_REMOVE_SIBLINGS);
            cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
            OpenCms.getPublishManager().publishProject(cms);
            OpenCms.getPublishManager().waitWhileRunning();
        } finally {
            cms.getRequestContext().setSiteRoot(storedSiteRoot);
        }
        try {
            cms.getRequestContext().setSiteRoot("/");
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            I_CmsResourceType type = OpenCms.getResourceManager().getResourceType(CmsResourceTypeXmlPage.getStaticTypeId());
            I_CmsLinkParseable validatable = (I_CmsLinkParseable) type;
            CmsFile res1 = cms.readFile(filename1);
            List links1 = validatable.parseLinks(cms, res1);
            assertEquals(links1.size(), 1);
            assertEquals(links1.get(0).toString(), cms.getRequestContext().addSiteRoot(filename2));
            CmsFile res2 = cms.readFile(filename2);
            List links2 = validatable.parseLinks(cms, res2);
            assertEquals(links2.size(), 1);
            assertEquals(links2.get(0).toString(), cms.getRequestContext().addSiteRoot(filename1));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of linked XmlContent in a different site, so that the link paths get broken.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportExportBrokenLinksXml() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of linked Xml Content in a different site, so that the link paths get broken.");
        String filename1 = "/xmlcontent.html";
        String filename2 = "/xmlcontent2.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportBrokenLinks.zip");
        CmsXmlEntityResolver resolver = new CmsXmlEntityResolver(cms);
        cms.getRequestContext().setSiteRoot("/sites/default/");
        CmsResource res1 = cms.createResource(filename1, OpenCmsTestCase.ARTICLE_TYPEID);
        CmsResource res2 = cms.createResource(filename2, OpenCmsTestCase.ARTICLE_TYPEID);
        CmsFile file2 = cms.readFile(res2);
        String content2 = new String(file2.getContents(), CmsEncoder.ENCODING_UTF_8);
        CmsXmlContent xmlcontent2 = CmsXmlContentFactory.unmarshal(content2, CmsEncoder.ENCODING_UTF_8, resolver);
        xmlcontent2.getValue("Text", Locale.ENGLISH, 0).setStringValue(cms, "<a href='" + filename1 + "'>test</a>");
        if (!xmlcontent2.hasValue("Homepage", Locale.ENGLISH, 0)) {
            xmlcontent2.addValue(cms, "Homepage", Locale.ENGLISH, 0);
        }
        xmlcontent2.getValue("Homepage", Locale.ENGLISH, 0).setStringValue(cms, filename1);
        file2.setContents(xmlcontent2.marshal());
        cms.writeFile(file2);
        CmsFile file1 = cms.readFile(res1);
        String content1 = new String(file1.getContents(), CmsEncoder.ENCODING_UTF_8);
        CmsXmlContent xmlcontent1 = CmsXmlContentFactory.unmarshal(content1, CmsEncoder.ENCODING_UTF_8, resolver);
        xmlcontent1.getValue("Text", Locale.ENGLISH, 0).setStringValue(cms, "<a href='" + filename2 + "'>test</a>");
        if (!xmlcontent1.hasValue("Homepage", Locale.ENGLISH, 0)) {
            xmlcontent1.addValue(cms, "Homepage", Locale.ENGLISH, 0);
        }
        xmlcontent1.getValue("Homepage", Locale.ENGLISH, 0).setStringValue(cms, filename2);
        file1.setContents(xmlcontent1.marshal());
        cms.writeFile(file1);
        cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
        OpenCms.getPublishManager().publishProject(cms);
        OpenCms.getPublishManager().waitWhileRunning();
        CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
        List exportPaths = new ArrayList(1);
        exportPaths.add(filename1);
        exportPaths.add(filename2);
        CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
        vfsExportHandler.setExportParams(params);
        OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
        cms.lockResource(filename1);
        cms.lockResource(filename2);
        cms.deleteResource(filename1, CmsResource.DELETE_REMOVE_SIBLINGS);
        cms.deleteResource(filename2, CmsResource.DELETE_REMOVE_SIBLINGS);
        cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
        OpenCms.getPublishManager().publishProject(cms);
        OpenCms.getPublishManager().waitWhileRunning();
        cms.getRequestContext().setSiteRoot("/");
        try {
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            I_CmsResourceType type = OpenCms.getResourceManager().getResourceType(OpenCmsTestCase.ARTICLE_TYPEID);
            I_CmsLinkParseable validatable = (I_CmsLinkParseable) type;
            CmsFile newRes1 = cms.readFile(filename1);
            List links1 = validatable.parseLinks(cms, newRes1);
            assertEquals(links1.size(), 2);
            assertEquals(links1.get(0).toString(), cms.getRequestContext().addSiteRoot(filename2));
            assertEquals(links1.get(1).toString(), cms.getRequestContext().addSiteRoot(filename2));
            CmsFile newRes2 = cms.readFile(filename2);
            List links2 = validatable.parseLinks(cms, newRes2);
            assertEquals(links2.size(), 2);
            assertEquals(links2.get(0).toString(), cms.getRequestContext().addSiteRoot(filename1));
            assertEquals(links2.get(1).toString(), cms.getRequestContext().addSiteRoot(filename1));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests an overwriting import of VFS data.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportExportFolder() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing an overwriting import of VFS data.");
        String filename = "folder1/";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportFolder.zip");
        try {
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests import with structure id.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportExportId() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing import with structure id.");
        String filename = "/dummy2.txt";
        String contentStr = "This is a comment. I love comments.";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportId.zip");
        byte[] content = contentStr.getBytes();
        try {
            cms.getRequestContext().setSiteRoot("/");
            cms.createResource(filename, CmsResourceTypePlain.getStaticTypeId(), content, null);
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename);
            cms.deleteResource(filename, CmsResource.DELETE_REMOVE_SIBLINGS);
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFilter(cms, cms.readResource(filename), OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a folder that has been moved.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportMovedFolder() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a folder that has been moved.");
        String filename = "newtestfile.html";
        String foldername = "/folderToMove/";
        String folder2 = "/movedFolder/";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportMovedFolder.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource folderBefore = cms.createResource(foldername, CmsResourceTypeFolder.getStaticTypeId());
            cms.createResource(foldername + filename, CmsResourceTypePlain.getStaticTypeId());
            cms.unlockResource(foldername);
            OpenCms.getPublishManager().publishResource(cms, foldername);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, foldername, true);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(foldername);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(foldername);
            cms.moveResource(foldername, folder2);
            cms.unlockResource(folder2);
            OpenCms.getPublishManager().publishResource(cms, folder2);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFalse(cms.existsResource(foldername));
            assertFalse(cms.existsResource(foldername + filename));
            assertTrue(cms.existsResource(folder2));
            assertTrue(cms.existsResource(folder2 + filename));
            cms.readResource(folderBefore.getStructureId());
            try {
                cms.readResource(foldername);
                fail("should not be found");
            } catch (Exception e) {
            }
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            assertTrue(cms.existsResource(folder2));
            assertTrue(cms.existsResource(folder2 + filename));
            assertFalse(cms.existsResource(foldername));
            assertFalse(cms.existsResource(foldername + filename));
            List lockedResources = cms.getLockedResources("/", CmsLockFilter.FILTER_ALL);
            assertFalse(lockedResources.contains(foldername));
            assertFalse(lockedResources.contains(folder2));
            String offlineContent = new String(cms.readFile(folder2 + filename).getContents());
            cms.getRequestContext().setCurrentProject(cms.readProject(CmsProject.ONLINE_PROJECT_ID));
            assertEquals(offlineContent, new String(cms.readFile(folder2 + filename).getContents()));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource that has been moved.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportMovedResource() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource that has been moved.");
        String filename1 = "/newfile4.html";
        String filename2 = "/movedfile.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportExportMovedResource.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource resBefore = cms.createResource(filename1, CmsResourceTypePlain.getStaticTypeId());
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename1);
            cms.moveResource(filename1, filename2);
            cms.unlockResource(filename2);
            OpenCms.getPublishManager().publishResource(cms, filename2);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFalse(cms.existsResource(filename1));
            assertTrue(cms.existsResource(filename2));
            cms.readResource(resBefore.getStructureId());
            try {
                cms.readResource(filename1);
                fail("should not be found");
            } catch (Exception e) {
            }
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            assertFalse(cms.existsResource(filename1));
            assertTrue(cms.existsResource(filename2));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource with permissions.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportPermissionIssue() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource with permissions.");
        String filename = "/index.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportPermissionIssue.zip");
        try {
            cms.lockResource(filename);
            cms.chacc(filename, I_CmsPrincipal.PRINCIPAL_USER, "test1", "+r-v");
            cms.unlockResource(filename);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename);
            cms.chacc(filename, I_CmsPrincipal.PRINCIPAL_USER, "test1", "-r+v");
            cms.chacc(filename, I_CmsPrincipal.PRINCIPAL_USER, "test2", "+r+w");
            cms.unlockResource(filename);
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", false));
            cms.unlockResource(filename);
            OpenCms.getPublishManager().publishResource(cms, filename, true, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getPublishManager().waitWhileRunning();
            assertPermissionString(cms, filename, cms.readUser("test1"), "+r-v-i-l");
            assertPermissionString(cms, filename, cms.readUser("test2"), null);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource that has been recreated.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportRecreatedFile() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource that has been recreated.");
        String filename1 = "/newfile5.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportRecreatedFile.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource resBefore = cms.createResource(filename1, CmsResourceTypePlain.getStaticTypeId());
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename1);
            cms.deleteResource(filename1, CmsResource.DELETE_REMOVE_SIBLINGS);
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFalse(cms.existsResource(filename1));
            cms.createResource(filename1, CmsResourceTypeImage.getStaticTypeId());
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            cms.readResource(filename1);
            try {
                cms.readResource(resBefore.getStructureId());
                fail("should not be found");
            } catch (Exception e) {
            }
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            CmsResource resAfter = cms.readResource(filename1);
            assertFilter(cms, resAfter, OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT_OVERWRITE);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a sibling that has been recreated.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportRecreatedSibling() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a sibling that has been recreated.");
        String filename1 = "/newfile3.html";
        String filename2 = "sibling2.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportRecreatedFile.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource res1 = cms.createResource(filename1, CmsResourceTypePlain.getStaticTypeId());
            cms.createSibling(filename1, filename2, null);
            cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
            OpenCms.getPublishManager().publishProject(cms);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename1);
            cms.deleteResource(filename1, CmsResource.DELETE_PRESERVE_SIBLINGS);
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFalse(cms.existsResource(filename1));
            cms.createSibling(filename2, filename1, null);
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            cms.readResource(filename1);
            try {
                cms.readResource(res1.getStructureId());
                fail("should not be found");
            } catch (Exception e) {
            }
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            CmsResource resAfter = cms.readResource(filename1);
            assertFilter(cms, resAfter, OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT_OVERWRITE);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource that has been edited.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportRelations() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource with relations.");
        String filename = "/index.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportRelations.zip");
        try {
            assertTrue(cms.getRelationsForResource(filename, CmsRelationFilter.TARGETS.filterNotDefinedInContent()).isEmpty());
            cms.lockResource(filename);
            CmsCategoryService catService = CmsCategoryService.getInstance();
            CmsCategory cat = catService.createCategory(cms, null, "abc", "title", "description");
            catService.addResourceToCategory(cms, filename, cat.getPath());
            List relations = cms.getRelationsForResource(filename, CmsRelationFilter.TARGETS.filterNotDefinedInContent());
            assertEquals(1, relations.size());
            assertRelation(new CmsRelation(cms.readResource(filename), cms.readResource(cat.getId()), CmsRelationType.CATEGORY), (CmsRelation) relations.get(0));
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.deleteResource(filename, CmsResource.DELETE_PRESERVE_SIBLINGS);
            OpenCms.getPublishManager().publishResource(cms, filename);
            OpenCms.getPublishManager().waitWhileRunning();
            cms.createResource(filename, CmsResourceTypePlain.getStaticTypeId());
            assertTrue(cms.getRelationsForResource(filename, CmsRelationFilter.TARGETS.filterNotDefinedInContent()).isEmpty());
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            relations = cms.getRelationsForResource(filename, CmsRelationFilter.TARGETS.filterNotDefinedInContent());
            assertEquals(1, relations.size());
            assertRelation(new CmsRelation(cms.readResource(filename), cms.readResource(cat.getId()), CmsRelationType.CATEGORY), (CmsRelation) relations.get(0));
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the resource translation during import.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportResourceTranslator() throws Exception {
        echo("Testing resource translator for import");
        CmsObject cms = OpenCms.initCmsObject(getCmsObject());
        cms.getRequestContext().setSiteRoot("/");
        cms.createResource("/system/galleries", CmsResourceTypeFolder.RESOURCE_TYPE_ID);
        cms.unlockResource("/system/galleries");
        CmsResourceTranslator oldFolderTranslator = OpenCms.getResourceManager().getFolderTranslator();
        CmsResourceTranslator folderTranslator = new CmsResourceTranslator(new String[] { "s#^/sites/default/content/bodys(.*)#/system/bodies$1#", "s#^/sites/default/pics/system(.*)#/system/workplace/resources$1#", "s#^/sites/default/pics(.*)#/system/galleries/pics$1#", "s#^/sites/default/download(.*)#/system/galleries/download$1#", "s#^/sites/default/externallinks(.*)#/system/galleries/externallinks$1#", "s#^/sites/default/htmlgalleries(.*)#/system/galleries/htmlgalleries$1#", "s#^/sites/default/content(.*)#/system$1#" }, false);
        OpenCms.getResourceManager().setTranslators(folderTranslator, OpenCms.getResourceManager().getFileTranslator());
        cms = getCmsObject();
        cms.getRequestContext().setSiteRoot("/sites/default");
        String importFile = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testimport01.zip");
        OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(importFile, "/", true));
        CmsXmlPage page;
        CmsFile file;
        CmsLinkTable table;
        List links;
        Iterator i;
        List siblings;
        file = cms.readFile("/importtest/index.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
        }
        assertTrue(links.size() == 2);
        assertTrue(links.contains("/sites/default/importtest/page2.html"));
        assertTrue(links.contains("/sites/default/importtest/page3.html"));
        siblings = cms.readSiblings("/importtest/index.html", CmsResourceFilter.ALL);
        i = siblings.iterator();
        links = new ArrayList();
        while (i.hasNext()) {
            CmsResource sibling = (CmsResource) i.next();
            links.add(sibling.getRootPath());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/default/importtest/index.html"));
        assertTrue(links.contains("/sites/default/importtest/linktest.html"));
        file = cms.readFile("/importtest/page2.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
            System.out.println("Link: " + link.toString());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/system/galleries/pics/_anfang/bg_teaser_test2.jpg"));
        assertTrue(links.contains("/sites/default/importtest/index.html"));
        file = cms.readFile("/importtest/linktest.html");
        assertEquals(CmsResourceTypeXmlPage.getStaticTypeId(), file.getTypeId());
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
            System.out.println("Link: " + link.toString());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/default/importtest/page2.html"));
        assertTrue(links.contains("/sites/default/importtest/page3.html"));
        siblings = cms.readSiblings("/importtest/linktest.html", CmsResourceFilter.ALL);
        i = siblings.iterator();
        links = new ArrayList();
        while (i.hasNext()) {
            CmsResource sibling = (CmsResource) i.next();
            links.add(sibling.getRootPath());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/default/importtest/index.html"));
        assertTrue(links.contains("/sites/default/importtest/linktest.html"));
        file = cms.readFile("/othertest/index.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
            System.out.println("Link: " + link.toString());
        }
        assertTrue(links.size() == 2);
        assertTrue(links.contains("/sites/default/importtest/page2.html"));
        assertTrue(links.contains("/sites/default/importtest/page3.html"));
        cms.getRequestContext().setSiteRoot("/");
        cms.lockResource("/sites/default");
        cms.lockResource("/system");
        cms.deleteResource("/sites/default/importtest", CmsResource.DELETE_PRESERVE_SIBLINGS);
        cms.deleteResource("/system/galleries/pics", CmsResource.DELETE_PRESERVE_SIBLINGS);
        cms.unlockResource("/sites/default");
        cms.unlockResource("/system");
        OpenCms.getPublishManager().publishProject(cms);
        OpenCms.getPublishManager().waitWhileRunning();
        OpenCms.getResourceManager().setTranslators(oldFolderTranslator, OpenCms.getResourceManager().getFileTranslator());
    }

    /**
     * Tests the resource translation during import with multiple sites.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportResourceTranslatorMultipleSite() throws Exception {
        echo("Testing resource translator with multiple sites");
        CmsObject cms = getCmsObject();
        cms.getRequestContext().setSiteRoot("/");
        cms.createResource("/sites/mysite", CmsResourceTypeFolder.RESOURCE_TYPE_ID);
        cms.unlockResource("/sites/mysite");
        cms.createResource("/sites/othersite", CmsResourceTypeFolder.RESOURCE_TYPE_ID);
        cms.unlockResource("/sites/othersite");
        CmsResourceTranslator oldFolderTranslator = OpenCms.getResourceManager().getFolderTranslator();
        CmsResourceTranslator folderTranslator = new CmsResourceTranslator(new String[] { "s#^/sites(.*)#/sites$1#", "s#^/system(.*)#/system$1#", "s#^/content/bodys(.*)#/system/bodies$1#", "s#^/pics(.*)#/system/galleries/pics$1#", "s#^/download(.*)#/system/galleries/download$1#", "s#^/externallinks(.*)#/system/galleries/externallinks$1#", "s#^/htmlgalleries(.*)#/system/galleries/htmlgalleries$1#", "s#^/content(.*)#/system$1#", "s#^/othertest(.*)#/sites/othersite$1#", "s#^/(.*)#/sites/mysite/$1#" }, false);
        OpenCms.getResourceManager().setTranslators(folderTranslator, OpenCms.getResourceManager().getFileTranslator());
        cms = getCmsObject();
        cms.getRequestContext().setSiteRoot("/");
        String importFile = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testimport02.zip");
        OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(importFile, "/", true));
        cms.getRequestContext().setSiteRoot("/sites/mysite/");
        CmsXmlPage page;
        CmsFile file;
        CmsLinkTable table;
        List links;
        Iterator i;
        List siblings;
        file = cms.readFile("/importtest/index.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
        }
        assertTrue(links.size() == 2);
        assertTrue(links.contains("/sites/mysite/importtest/page2.html"));
        assertTrue(links.contains("/sites/mysite/importtest/page3.html"));
        siblings = cms.readSiblings("/importtest/index.html", CmsResourceFilter.ALL);
        i = siblings.iterator();
        links = new ArrayList();
        while (i.hasNext()) {
            CmsResource sibling = (CmsResource) i.next();
            links.add(sibling.getRootPath());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/mysite/importtest/index.html"));
        assertTrue(links.contains("/sites/mysite/importtest/linktest.html"));
        file = cms.readFile("/importtest/page2.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/system/galleries/pics/_anfang/bg_teaser_test2.jpg"));
        assertTrue(links.contains("/sites/mysite/importtest/index.html"));
        file = cms.readFile("/importtest/linktest.html");
        assertEquals(CmsResourceTypeXmlPage.getStaticTypeId(), file.getTypeId());
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
            System.out.println("Link: " + link.toString());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/mysite/importtest/page2.html"));
        assertTrue(links.contains("/sites/mysite/importtest/page3.html"));
        siblings = cms.readSiblings("/importtest/linktest.html", CmsResourceFilter.ALL);
        i = siblings.iterator();
        links = new ArrayList();
        while (i.hasNext()) {
            CmsResource sibling = (CmsResource) i.next();
            links.add(sibling.getRootPath());
            System.out.println("Sibling: " + sibling.toString());
        }
        assertEquals(2, links.size());
        assertTrue(links.contains("/sites/mysite/importtest/index.html"));
        assertTrue(links.contains("/sites/mysite/importtest/linktest.html"));
        cms.getRequestContext().setSiteRoot("/sites/othersite/");
        file = cms.readFile("/index.html");
        page = CmsXmlPageFactory.unmarshal(cms, file);
        table = page.getLinkTable("body", CmsLocaleManager.getDefaultLocale());
        links = new ArrayList();
        i = table.iterator();
        while (i.hasNext()) {
            CmsLink link = (CmsLink) i.next();
            links.add(link.toString());
            System.out.println("Link: " + link.toString());
        }
        assertTrue(links.size() == 2);
        assertTrue(links.contains("/sites/mysite/importtest/page2.html"));
        assertTrue(links.contains("/sites/mysite/importtest/page3.html"));
        OpenCms.getResourceManager().setTranslators(oldFolderTranslator, OpenCms.getResourceManager().getFileTranslator());
    }

    /**
     * Tests the import of a sibling.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportSibling() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a sibling.");
        String filename1 = "/newfile2.html";
        String filename2 = "sibling.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportRecreatedFile.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsResource res1 = cms.createResource(filename1, CmsResourceTypePlain.getStaticTypeId());
            cms.createSibling(filename1, filename2, null);
            cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
            OpenCms.getPublishManager().publishProject(cms);
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, filename1);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(filename1);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(filename1);
            cms.deleteResource(filename1, CmsResource.DELETE_PRESERVE_SIBLINGS);
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFalse(cms.existsResource(filename1));
            try {
                cms.readResource(filename1);
                fail("should not be found");
            } catch (Exception e) {
            }
            try {
                cms.readResource(res1.getStructureId());
                fail("should not be found");
            } catch (Exception e) {
            }
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(filename1);
            OpenCms.getPublishManager().publishResource(cms, filename1);
            OpenCms.getPublishManager().waitWhileRunning();
            assertFilter(cms, cms.readResource(filename1), OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT_SIBLING);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a (non-existing) sibling of a file in a different site.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportSiblingIssue() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a (non-existing) sibling of a file in a different site.");
        String filename = "/newfileSiblingIssue.html";
        String sibname = "/system/sibIssue.jsp";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportSiblingIssue.zip");
        String site = cms.getRequestContext().getSiteRoot();
        try {
            cms.createResource(filename, CmsResourceTypeJsp.getStaticTypeId(), "hello jsp".getBytes(), null);
            cms.unlockResource(filename);
            cms.getRequestContext().setSiteRoot("/");
            cms.createSibling(site + filename, sibname, null);
            cms.unlockResource(sibname);
            OpenCms.getPublishManager().publishResource(cms, sibname, true, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getPublishManager().waitWhileRunning();
            storeResources(cms, site + filename);
            storeResources(cms, sibname);
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(sibname);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, true, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.lockResource(sibname);
            cms.deleteResource(sibname, CmsResource.DELETE_PRESERVE_SIBLINGS);
            OpenCms.getPublishManager().publishResource(cms, sibname);
            OpenCms.getPublishManager().waitWhileRunning();
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.unlockResource(sibname);
            OpenCms.getPublishManager().publishResource(cms, sibname, true, new CmsShellReport(cms.getRequestContext().getLocale()));
            OpenCms.getPublishManager().waitWhileRunning();
            OpenCmsTestResourceConfigurableFilter filter = new OpenCmsTestResourceConfigurableFilter(OpenCmsTestResourceFilter.FILTER_EQUAL);
            filter.disableDateContentTest();
            filter.disableDateLastModifiedSecTest();
            filter.disableDateLastModifiedTest();
            assertFilter(cms, cms.readResource(site + filename), filter);
            assertFilter(cms, cms.readResource(sibname), OpenCmsTestResourceFilter.FILTER_IMPORTEXPORT);
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import xml validation.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportValidation() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import xml validation.");
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportValidation.zip");
        try {
            cms.getRequestContext().setSiteRoot("/");
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add("/");
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, true, true, exportPaths, true, true, 0, true, false);
            params.setXmlValidation(true);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            CmsImportParameters impar = new CmsImportParameters(zipExportFilename, "/", true);
            impar.setXmlValidation(true);
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), impar);
            cms.unlockProject(cms.getRequestContext().currentProject().getUuid());
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of a resource in the wrong site.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testImportWrongSite() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of a resource in the wrong site.");
        String site = "/sites/default";
        String filename = "/newfileWrongSite.html";
        String zipExportFilename = OpenCms.getSystemInfo().getAbsoluteRfsPathRelativeToWebInf("packages/testImportWrongSite.zip");
        try {
            cms.getRequestContext().setSiteRoot("");
            CmsResource res = cms.createResource(site + filename, CmsResourceTypePlain.getStaticTypeId());
            cms.unlockResource(site + filename);
            OpenCms.getPublishManager().publishResource(cms, site + filename);
            OpenCms.getPublishManager().waitWhileRunning();
            CmsVfsImportExportHandler vfsExportHandler = new CmsVfsImportExportHandler();
            List exportPaths = new ArrayList(1);
            exportPaths.add(site + filename);
            CmsExportParameters params = new CmsExportParameters(zipExportFilename, null, true, false, false, exportPaths, false, true, 0, true, false);
            vfsExportHandler.setExportParams(params);
            OpenCms.getImportExportManager().exportData(cms, vfsExportHandler, new CmsShellReport(cms.getRequestContext().getLocale()));
            cms.getRequestContext().setSiteRoot(site);
            OpenCms.getImportExportManager().importData(cms, new CmsShellReport(cms.getRequestContext().getLocale()), new CmsImportParameters(zipExportFilename, "/", true));
            cms.getRequestContext().setSiteRoot("");
            cms.readResource(res.getStructureId());
            String path = "/";
            Iterator itPath = CmsStringUtil.splitAsList(site + filename, "/").iterator();
            while (itPath.hasNext()) {
                String pathPart = (String) itPath.next();
                path += pathPart + "/";
                assertTrue(cms.existsResource(path));
                assertFalse(cms.existsResource(site + path));
            }
        } finally {
            try {
                if (zipExportFilename != null) {
                    File file = new File(zipExportFilename);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            } catch (Throwable t) {
            }
        }
    }

    /**
     * Tests the import of resources during setup.<p>
     * 
     * @throws Exception if something goes wrong
     */
    public void testSetup() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the import of resources during setup.");
        CmsResource resource = cms.readResource("index.html");
        long expectedDateCreated = convertTimestamp("Tue, 01 Jun 2004 09:11:24 GMT");
        long expectedDateLastModified = convertTimestamp("Wed, 27 Sep 2006 15:11:58 GMT");
        assertEquals(expectedDateCreated, resource.getDateCreated());
        assertEquals(expectedDateLastModified, resource.getDateLastModified());
    }

    /**
     * Tests if the user passwords are imported correctly.<p>
     * The password digests are hex-128 (legacy) encoded, and
     * should be converted to base-64. 
     * 
     * @throws Exception if something goes wrong
     */
    public void testUserImport() throws Exception {
        CmsObject cms = getCmsObject();
        echo("Testing the user import.");
        I_CmsPasswordHandler passwordHandler = new CmsDefaultPasswordHandler();
        CmsUser user;
        echo("Testing passwords of imported users");
        user = cms.readUser("Admin");
        assertEquals(user.getPassword(), passwordHandler.digest("admin", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("Guest");
        assertEquals(user.getPassword(), passwordHandler.digest("", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("test1");
        assertEquals(user.getPassword(), passwordHandler.digest("test1", "MD5", CmsEncoder.ENCODING_UTF_8));
        user = cms.readUser("test2");
        assertEquals(user.getPassword(), passwordHandler.digest("test2", "MD5", CmsEncoder.ENCODING_UTF_8));
    }

    /**
     * Convert a given timestamp from a String format to a long value.<p>
     * 
     * The timestamp is either the string representation of a long value (old export format)
     * or a user-readable string format.
     * 
     * @param timestamp timestamp to convert
     * @return long value of the timestamp
     */
    private long convertTimestamp(String timestamp) {
        long value = 0;
        try {
            value = Long.parseLong(timestamp);
        } catch (NumberFormatException e) {
            try {
                value = CmsDateUtil.parseHeaderDate(timestamp);
            } catch (ParseException pe) {
                value = System.currentTimeMillis();
            }
        }
        return value;
    }
}

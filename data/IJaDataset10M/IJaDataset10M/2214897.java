package fll.web.slideshow;

import java.io.IOException;
import java.io.InputStream;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.thoughtworks.selenium.SeleneseTestBase;
import fll.TestUtils;
import fll.util.LogUtils;
import fll.web.InitializeDatabaseTest;
import fll.web.IntegrationTestUtils;

/**
 * 
 */
public class SlideshowTest extends SeleneseTestBase {

    @Before
    @Override
    public void setUp() throws Exception {
        LogUtils.initializeLogging();
        super.setUp(TestUtils.URL_ROOT + "/setup");
    }

    /**
   * Test setting slideshow interval and make sure it doesn't error.
   * @throws IOException 
   */
    @Test
    public void testSlideshowInterval() throws IOException {
        final InputStream challengeStream = InitializeDatabaseTest.class.getResourceAsStream("data/challenge-ft.xml");
        IntegrationTestUtils.initializeDatabase(selenium, challengeStream, true);
        try {
            selenium.click("link=Admin Index");
            selenium.waitForPageToLoad(IntegrationTestUtils.WAIT_FOR_PAGE_TIMEOUT);
            selenium.click("link=Remote control of display");
            selenium.waitForPageToLoad(IntegrationTestUtils.WAIT_FOR_PAGE_TIMEOUT);
            selenium.click("slideshow");
            selenium.type("slideInterval", "5");
            selenium.click("submit");
            selenium.waitForPageToLoad(IntegrationTestUtils.WAIT_FOR_PAGE_TIMEOUT);
            Assert.assertTrue("Didn't get success from commit", selenium.isTextPresent("Successfully set remote control parameters"));
            selenium.open(TestUtils.URL_ROOT + "/slideshow/index.jsp");
            selenium.waitForPageToLoad(IntegrationTestUtils.WAIT_FOR_PAGE_TIMEOUT);
            Assert.assertFalse("Got error", selenium.isTextPresent("An error has occurred"));
        } catch (final RuntimeException e) {
            IntegrationTestUtils.storeScreenshot(selenium);
            throw e;
        } catch (final AssertionError e) {
            IntegrationTestUtils.storeScreenshot(selenium);
            throw e;
        }
    }
}

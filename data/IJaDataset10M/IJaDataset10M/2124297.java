package org.fest.swing.testng.listener;

import java.lang.reflect.Method;
import java.util.logging.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.fest.swing.annotation.GUITestFinder;
import org.fest.swing.image.ImageException;
import org.fest.swing.image.ScreenshotTaker;
import static java.io.File.separator;
import static java.util.logging.Level.SEVERE;
import static org.fest.swing.testng.listener.ScreenshotFileNameGenerator.screenshotFileNameFrom;
import static org.fest.util.Strings.*;

/**
 * Understands a <a href="http://testng.org" target="_blank">TestNG</a> listener that takes a screenshot when a GUI test 
 * fails.
 * <p>
 * <strong>Note:</strong> A test is consider a GUI test if it is marked with the annotation
 * <code>{@link org.fest.swing.annotation.GUITest}</code>.
 * </p>
 * <p>
 * To use this listener, we just need to make TestNG aware of it. The following is an example using Ant:
 * <pre>
 * &lt;testng <strong><span style="text-decoration: underline">listeners=&quot;org.fest.swing.testng.listener.ScreenshotOnFailureListener&quot;</span></strong> outputDir=&quot;${target.test.results.dir}&quot; haltOnFailure=&quot;true&quot; verbose=&quot;2&quot;&gt;
 *   &lt;classfileset dir=&quot;${target.test.classes.dir}&quot; includes=&quot;&#42;&#42;/&#42;Test.class&quot; /&gt;
 *   &lt;classpath location=&quot;${target.test.classes.dir}&quot; /&gt;
 *   &lt;classpath location=&quot;${target.classes.dir}&quot; /&gt;
 *   &lt;classpath refid=&quot;test.classpath&quot; /&gt;
 * &lt;/testng&gt; 
 * </pre>
 * </p>
 * <p>
 * You can find more information 
 * <a href="http://www.jroller.com/page/alexRuiz?entry=screenshots_of_failures_in_test" target="_blank">here</a>.
 * </p>
 * 
 * @author Alex Ruiz
 */
public class ScreenshotOnFailureListener extends AbstractTestListener {

    private static Logger logger = Logger.getAnonymousLogger();

    private ScreenshotTaker screenshotTaker;

    private String output;

    private boolean ready;

    /**
   * Creates a new <code>{@link ScreenshotOnFailureListener}</code>.
   */
    public ScreenshotOnFailureListener() {
        try {
            screenshotTaker = new ScreenshotTaker();
        } catch (ImageException e) {
            logger.log(SEVERE, "Unable to create ScreenshotTaker", e);
        }
    }

    String output() {
        return output;
    }

    /**
   * Gets the output directory from the given context after the test class is instantiated and before any configuration 
   * method is called.
   * @param context the given method context.
   */
    @Override
    public void onStart(ITestContext context) {
        output = context.getOutputDirectory();
        logger.info(concat("TestNG output directory: ", quote(output)));
        ready = !isEmpty(output) && screenshotTaker != null;
    }

    /**
   * When a test fails, this method takes a screenshot of the desktop and adds an hyperlink to the screenshot it in the 
   * HTML test report.
   * @param result contains information about the failing test.
   */
    @Override
    public void onTestFailure(ITestResult result) {
        if (!ready || !isGUITest(result)) return;
        String screenshotFileName = takeScreenshotAndReturnFileName(result);
        if (isEmpty(screenshotFileName)) return;
        logger.info(concat("Screenshot of desktop saved as: ", quote(screenshotFileName)));
        Reporter.setCurrentTestResult(result);
        Reporter.log(concat("<a href=\"", screenshotFileName, "\">Screenshot</a>"));
    }

    private static boolean isGUITest(ITestResult testResult) {
        Class<?> realClass = testResult.getTestClass().getRealClass();
        Method testMethod = testResult.getMethod().getMethod();
        return GUITestFinder.isGUITest(realClass, testMethod);
    }

    private String takeScreenshotAndReturnFileName(ITestResult result) {
        String imageName = screenshotFileNameFrom(result);
        String imagePath = concat(output, separator, imageName);
        try {
            screenshotTaker.saveDesktopAsPng(imagePath);
        } catch (ImageException e) {
            logger.log(SEVERE, e.getMessage(), e);
            return null;
        }
        return imageName;
    }
}

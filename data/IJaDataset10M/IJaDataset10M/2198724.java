package test;

import static org.junit.Assert.assertTrue;
import injection.GuiceInjector;
import jam.Main;
import jam.ui.ConsoleLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dale Visser
 */
public class TestStartup {

    /**
     * @throws Exception
     *             if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        Main.main(null);
    }

    /**
     * @throws Exception
     *             if an error occurs
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Make sure log has welcome string in it.
     */
    @Test
    public void testToString() {
        final String welcome = "Welcome to Jam";
        final String logString = GuiceInjector.getObjectInstance(ConsoleLog.class).toString();
        assertTrue("Expected log to start with \"" + welcome + "\".", logString.contains(welcome));
    }
}

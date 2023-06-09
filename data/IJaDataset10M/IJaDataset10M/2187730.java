package net.azib.ipscan;

import static org.junit.Assert.*;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.junit.Test;
import net.azib.ipscan.config.Labels;
import net.azib.ipscan.feeders.FeederException;

public class MainTest {

    @Test
    public void getLocalizedMessage() {
        final boolean wasStackTraceLogged[] = { false };
        Throwable e = new Exception("hello, test!");
        Main.LOG.setUseParentHandlers(false);
        Main.LOG.addHandler(new Handler() {

            public void close() throws SecurityException {
            }

            public void flush() {
            }

            public void publish(LogRecord record) {
                wasStackTraceLogged[0] = true;
            }
        });
        assertEquals(e.toString(), Main.getLocalizedMessage(e));
        assertTrue(wasStackTraceLogged[0]);
        assertEquals(Labels.getLabel("exception.FeederException.range.greaterThan"), Main.getLocalizedMessage(new FeederException("range.greaterThan")));
        assertEquals(Labels.getLabel("exception.OutOfMemoryError"), Main.getLocalizedMessage(new OutOfMemoryError()));
    }
}

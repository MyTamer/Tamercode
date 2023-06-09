package jam.util;

import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import javax.swing.ProgressMonitor;
import com.google.inject.Inject;

/**
 * Scans YaleCAEN event files for scaler blocks.
 * @author <a href="mailto:dwvisser@users.sourceforge.net">Dale W Visser</a>
 */
public class YaleCAENgetScalers {

    private static final Logger LOGGER = Logger.getLogger(YaleCAENgetScalers.class.getPackage().getName());

    private transient String fileName;

    private final transient Frame frame;

    private transient ProgressMonitor pBstatus;

    private transient String strScalerText;

    private final transient ExecutorService executor;

    /**
     * Constructs an object that can scan YaleCAEN event files for scaler
     * blocks.
     * @param frame
     *            application frame
     * @param executor
     *            provides threads for tasks
     */
    @Inject
    public YaleCAENgetScalers(final Frame frame, final ExecutorService executor) {
        super();
        this.frame = frame;
        this.executor = executor;
    }

    private void display() {
        new TextDisplayDialog(frame, fileName, strScalerText);
    }

    /**
     * Takes an event file, searches for scaler blocks in it and creates
     * tab-delimited text listing each scaler block on one row of text.
     * @param events
     *            the file to search
     * @return whether we were successful
     */
    private boolean doIt(final File events, final StringBuilder strError) {
        final int mega = 1024 * 1024;
        final long fileLength = events.length();
        final int lengthMB = (int) (fileLength / mega);
        pBstatus = new ProgressMonitor(frame, "Scanning " + events.getName() + " for scaler blocks", "Initializing", 0, lengthMB);
        boolean rtnState = true;
        final StringBuffer strBuff = new StringBuffer();
        final int SCALER_HEADER = 0x01cccccc;
        DataInputStream dis = null;
        strError.delete(0, strError.length());
        int counter = 0;
        int megaCounter = 0;
        try {
            dis = new DataInputStream(new BufferedInputStream(new FileInputStream(events)));
            counter += dis.skipBytes(256);
            while (true) {
                final int readVal = dis.readInt();
                counter += 4;
                if (readVal == SCALER_HEADER) {
                    final int numScalers = dis.readInt();
                    counter += 4;
                    counter = appendScalerValues(strBuff, dis, counter, numScalers);
                    strBuff.append('\n');
                }
                if (counter >= mega) {
                    counter -= mega;
                    megaCounter++;
                    updateProgressBar(megaCounter + " of " + lengthMB + " MB read.", megaCounter);
                }
            }
        } catch (EOFException eofe) {
            if (dis != null) {
                try {
                    dis.close();
                    updateProgressBar("Done.", lengthMB);
                    rtnState = true;
                } catch (Exception e) {
                    strError.append(e.getMessage());
                    rtnState = false;
                }
            }
        } catch (IOException ioe) {
            strError.append("Reading file: ").append(ioe.getMessage());
            rtnState = false;
        }
        fileName = events.getName();
        strScalerText = strBuff.toString();
        return rtnState;
    }

    /**
     * @param strBuff
     * @param dis
     * @param counter
     * @param numScalers
     * @return
     * @throws IOException
     */
    private int appendScalerValues(final StringBuffer strBuff, final DataInputStream dis, final int counter, final int numScalers) throws IOException {
        int rval = counter;
        for (int i = 1; i <= numScalers; i++) {
            strBuff.append(dis.readInt());
            rval += 4;
            if (i < numScalers) {
                strBuff.append('\t');
            }
        }
        return rval;
    }

    /**
     * Scans the given event file for scaler blocks.
     * @param events
     *            file to scan
     */
    public void processEventFile(final File events) {
        final Runnable runnable = new Runnable() {

            public void run() {
                StringBuilder error = new StringBuilder();
                if (doIt(events, error)) {
                    display();
                } else {
                    LOGGER.severe("Reading Yale CAEN Scalers " + error.toString());
                }
            }
        };
        this.executor.submit(runnable);
    }

    private void updateProgressBar(final String text, final int value) {
        pBstatus.setNote(text);
        pBstatus.setProgress(value);
    }
}

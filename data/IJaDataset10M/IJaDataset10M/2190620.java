package jam.util;

import injection.GuiceInjector;
import jam.global.LoggerConfig;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This program will check event files of the type generated by
 * jam.sort.stream.YaleCAEN_InputStream for the presence of the buggy behavior
 * whereby the last buffer from the previous run gets put as the first buffer as
 * the next run. Give it a directory and it will print which event files have a
 * first buffer with and end-of-run character at the end of it.
 * @author Dale Visser
 * @version 6 August 2002
 */
public final class FullCheckEventFiles {

    private static final String PACKAGENAME = FullCheckEventFiles.class.getPackage().getName();

    static {
        new LoggerConfig(PACKAGENAME);
    }

    private static final Logger LOGGER = Logger.getLogger(PACKAGENAME);

    private FullCheckEventFiles(final File dir) {
        super();
        final File[] eventFiles = getEventFiles(dir);
        if (eventFiles.length > 0) {
            LOGGER.info("Found event files, starting to examine...");
            checkAllBuffers(Arrays.asList(eventFiles));
        } else {
            LOGGER.warning("Didn't find event files in " + dir);
        }
        LOGGER.info("Done.");
    }

    private void checkAllBuffers(final List<File> eventFiles) {
        for (File eventFile : eventFiles) {
            LOGGER.info("Checking File " + eventFile);
            checkFile(eventFile);
        }
    }

    private void checkFile(final File eventFile) {
        final int initialSkip = 256 + 8192 - 4;
        final int usualBytesToSkip = 8192 - 4;
        try {
            final DataInputStream instream = new DataInputStream(new BufferedInputStream(new FileInputStream(eventFile)));
            boolean skipSuccess = (initialSkip == instream.skip(initialSkip));
            int bufferNum = 0;
            if (skipSuccess) {
                bufferNum++;
                final int word = instream.readInt();
                if (word == 0x01EEEEEE) {
                    LOGGER.info("Buffer " + bufferNum + " contains end-of-run-word.");
                }
            }
            while (skipSuccess) {
                skipSuccess = (usualBytesToSkip == instream.skipBytes(usualBytesToSkip));
                if (skipSuccess) {
                    bufferNum++;
                    final int word = instream.readInt();
                    if (word == 0x01EEEEEE) {
                        LOGGER.info("Buffer " + bufferNum + " contains end-of-run-word.");
                    }
                }
            }
            instream.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private File[] getEventFiles(final File path) {
        return path.listFiles(new FilenameFilter() {

            public boolean accept(final File dir, final String name) {
                return name.endsWith("evn");
            }
        });
    }

    /**
     * @param args
     *            one argument--directory where event files are to be checked
     */
    public static void main(final String[] args) {
        boolean printHelp = false;
        if (args.length >= 1) {
            final File file1 = GuiceInjector.getObjectInstance(FileUtilities.class).getDir(args[0]);
            new FullCheckEventFiles(file1);
        } else {
            printHelp = true;
        }
        if (printHelp) {
            LOGGER.info("CheckEventFiles needs 1 argument:");
            LOGGER.info("\t1st arg: directory containing event files");
        }
    }
}

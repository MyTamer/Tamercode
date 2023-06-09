package org.ccnx.ccn.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import org.ccnx.ccn.CCNInterestListener;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.Interest;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

/**
 * Express interest in a name and report content retrieved.
 * This program has not been updated to handle interests in a sophisticated way
 * and so it is likely to receive the same content over and over.
 *
 */
public class watch extends Thread implements CCNInterestListener {

    protected boolean _stop = false;

    protected ArrayList<Interest> _interests = new ArrayList<Interest>();

    protected CCNHandle _handle = null;

    public watch(CCNHandle handle) {
        _handle = handle;
    }

    public void initialize() {
    }

    public void work() {
    }

    public void run() {
        _stop = false;
        initialize();
        System.out.println("Watching: " + new Date().toString() + ".");
        Log.info("Watching: " + new Date().toString() + ".");
        do {
            try {
                work();
            } catch (Exception e) {
                Log.warning("Error in watcher thread: " + e.getMessage());
                Log.warningStackTrace(e);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        } while (!_stop);
    }

    public static void usage() {
        System.out.println("usage: watch <ccnname> [<ccnname>...]");
    }

    public Interest handleContent(ContentObject data, Interest interest) {
        System.out.println("New content: " + data.name());
        return interest;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
            return;
        }
        try {
            CCNHandle library = CCNHandle.open();
            watch listener = new watch(library);
            for (int i = 0; i < args.length; ++i) {
                Interest interest = new Interest(args[i]);
                library.expressInterest(interest, listener);
            }
            listener.run();
            try {
                listener.join();
            } catch (InterruptedException e) {
            }
            System.exit(0);
        } catch (ConfigurationException e) {
            System.out.println("Configuration exception in watch: " + e.getMessage());
            e.printStackTrace();
        } catch (MalformedContentNameStringException e) {
            System.out.println("Malformed name: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException in enumerate: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

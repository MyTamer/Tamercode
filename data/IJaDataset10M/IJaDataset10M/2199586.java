package jmri.jmrit.symbolicprog;

import jmri.jmrit.*;
import java.io.*;

/**
 * Functions for use with programmer files, including the default file name.
 * <P>
 * This was refactored from LocoSelPane in JMRI 1.5.3, which was the
 * the right thing to do anyway.  But the real reason was that
 * on MacOS Classic the static member holding the default programmer name
 * was being overwritten when the class was (erroneously) initialized 
 * for a second time.  This refactoring did not fix the problem.
 * What did fix it was an ugly hack in the 
 * {@link CombinedLocoSelPane} class; see comments there for 
 * more information.
 *
 * @author	Bob Jacobsen   Copyright (C) 2001, 2002
 * @version	$Revision: 1.2 $
 */
public class ProgDefault {

    public static String[] findListOfProgFiles() {
        int i;
        int np = 0;
        String[] sp = null;
        XmlFile.ensurePrefsPresent(XmlFile.prefsDir() + "programmers");
        File fp = new File(XmlFile.prefsDir() + "programmers");
        if (fp.exists()) {
            sp = fp.list();
            for (i = 0; i < sp.length; i++) {
                if (sp[i].endsWith(".xml")) np++;
            }
        } else {
            log.warn(XmlFile.prefsDir() + "programmers was missing, though tried to create it");
        }
        String[] sx = (new File(XmlFile.xmlDir() + "programmers")).list();
        int nx = 0;
        for (i = 0; i < sx.length; i++) {
            if (sx[i].endsWith(".xml")) nx++;
        }
        String sbox[] = new String[np + nx];
        int n = 0;
        if (sp != null && np > 0) for (i = 0; i < sp.length; i++) {
            if (sp[i].endsWith(".xml")) sbox[n++] = sp[i].substring(0, sp[i].length() - 1 - 3);
        }
        for (i = 0; i < sx.length; i++) {
            if (sx[i].endsWith(".xml")) sbox[n++] = sx[i].substring(0, sx[i].length() - 1 - 3);
        }
        return sbox;
    }

    static volatile String defaultProgFile;

    public static synchronized String getDefaultProgFile() {
        if (log.isDebugEnabled()) log.debug("get programmer: " + defaultProgFile);
        return defaultProgFile;
    }

    public static synchronized void setDefaultProgFile(String s) {
        if (log.isDebugEnabled()) log.debug("set programmer: " + s);
        defaultProgFile = s;
    }

    static {
        defaultProgFile = null;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProgDefault.class.getName());
}

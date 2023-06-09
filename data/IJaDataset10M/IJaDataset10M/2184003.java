package jmri.jmrix.loconet;

import java.util.ResourceBundle;

/**
 * Common access to the LocoNetBundle of properties.
 *
 * Putting this in a class allows it to be loaded only
 * once.
 *
 * @author Bob Jacobsen  Copyright 2010
 * @since 2.9.4
 * @version $Revision: 1.1 $
 */
public class LocoNetBundle {

    public static final ResourceBundle b = java.util.ResourceBundle.getBundle("jmri.jmrix.loconet.LocoNetBundle");

    public static ResourceBundle bundle() {
        return b;
    }
}

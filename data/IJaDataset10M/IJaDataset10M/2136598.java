package com.ibm.celldt.environment.cellsimulator.conf;

import org.eclipse.osgi.util.NLS;

/**
 * Default values that are shared by Remote and Local Cell Simulator Target.
 * This values are used to initialize preference pages. Also used as fallback
 * when the preferences or a target instance contains invalid data.
 * 
 * All values are represented as strings.
 * 
 * @author Richard Maciel, Daniel Ferber
 *
 * @since 1.2.1
 */
public class CommonDefaultValues extends NLS {

    private static final String BUNDLE_ID = "com.ibm.celldt.environment.cellsimulator.conf.commondefaults";

    public static String SIMULATOR_BASE_DIRECTORY;

    public static String ARCHITECTURE_ID;

    public static String MEMORY_SIZE;

    public static String PROFILE_ID;

    public static String EXTRA_COMMAND_LINE_SWITCHES;

    public static String AUTOMATIC_AUTHENTICATION;

    public static String USERNAME;

    public static String PASSWORD;

    public static String TIMEOUT;

    public static String EXTRA_IMAGE_INIT;

    public static String EXTRA_IMAGE_PATH;

    public static String EXTRA_IMAGE_PERSISTENCE;

    public static String EXTRA_IMAGE_JOURNAL_PATH;

    public static String EXTRA_IMAGE_TYPE;

    public static String EXTRA_IMAGE_MOUNTPOINT;

    public static String KERNEL_IMAGE_PATH;

    public static String ROOT_IMAGE_PATH;

    public static String ROOT_IMAGE_PERSISTENCE;

    public static String ROOT_IMAGE_JOURNAL_PATH;

    public static String CUSTOMIZATION_SCRIPT;

    static {
        NLS.initializeMessages(BUNDLE_ID, CommonDefaultValues.class);
    }

    private CommonDefaultValues() {
    }
}

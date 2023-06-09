package org.objectstyle.wolips.projectbuild.builder;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author uli
 */
public class AntBuildMessages {

    private static final String RESOURCE_BUNDLE = AntBuildMessages.class.getName();

    private static ResourceBundle fgResourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

    private AntBuildMessages() {
        super();
    }

    /**
	 * @param key
	 * @return
	 */
    public static String getString(String key) {
        try {
            return fgResourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }

    /**
	 * Gets a string from the resource bundle and formats it with the argument
	 *
	 * @param key	the string used to get the bundle value, must not be null
	 * @param arg
	 * @return
	 */
    public static String getFormattedString(String key, Object arg) {
        return MessageFormat.format(getString(key), new Object[] { arg });
    }

    /**
	 * Gets a string from the resource bundle and formats it with arguments
	 * @param key
	 * @param args
	 * @return
	 */
    public static String getFormattedString(String key, Object[] args) {
        return MessageFormat.format(getString(key), args);
    }
}

package imtek.usb.iow;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class IowPlugin extends Plugin {

    private static IowPlugin plugin;

    private ResourceBundle resourceBundle;

    /**
	 * The constructor.
	 */
    public IowPlugin() {
        super();
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
        resourceBundle = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static IowPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
    public static String getResourceString(String key) {
        ResourceBundle bundle = IowPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
	 * Returns the plugin's resource bundle,
	 */
    public ResourceBundle getResourceBundle() {
        try {
            if (resourceBundle == null) resourceBundle = ResourceBundle.getBundle("imtek.usb.iow.IowPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
        return resourceBundle;
    }
}

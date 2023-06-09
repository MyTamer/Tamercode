package com.aptana.ide.syncing;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class SyncingPlugin extends AbstractUIPlugin {

    private static SyncingPlugin plugin;

    /**
	 * The constructor.
	 */
    public SyncingPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 * 
	 * @param context
	 * @throws Exception
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 * 
	 * @param context
	 * @throws Exception
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 * 
	 * @return FtpPlugin
	 */
    public static SyncingPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.aptana.ide.syncing", path);
    }
}

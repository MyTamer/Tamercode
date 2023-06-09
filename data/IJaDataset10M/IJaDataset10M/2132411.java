package org.eclipse.genforms.editor;

import java.net.MalformedURLException;
import java.net.URL;
import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class GenformsPlugin extends AbstractUIPlugin {

    private static GenformsPlugin plugin;

    /**
	 * The constructor.
	 */
    public GenformsPlugin() {
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
    }

    /**
	 * Returns the shared instance.
	 */
    public static GenformsPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return AbstractUIPlugin.imageDescriptorFromPlugin("org.eclipse.genforms.editor", path);
    }

    public ImageDescriptor getIcon(String name) {
        String iconPath = "";
        URL pluginUrl = getBundle().getEntry("/");
        try {
            return ImageDescriptor.createFromURL(new URL(pluginUrl, iconPath + name));
        } catch (MalformedURLException e) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }
}

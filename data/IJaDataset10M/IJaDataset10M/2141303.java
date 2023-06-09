package com.aptana.ide.editor.php.tests;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TestsPlugin extends AbstractUIPlugin {

    private static TestsPlugin plugin;

    /**
	 * The constructor.
	 */
    public TestsPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 * @param context 
	 * @throws Exception 
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 * @param context 
	 * @throws Exception 
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 * @return TestsPlugin
	 */
    public static TestsPlugin getDefault() {
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
        return AbstractUIPlugin.imageDescriptorFromPlugin("com.aptana.ide.editor.php.tests", path);
    }
}

package org.jivesoftware.spark.plugin;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.spark.util.URLFileSystem;
import org.jivesoftware.spark.util.log.Log;
import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A simple classloader to extend the classpath to
 * include all jars in a lib directory.<p>
 * <p/>
 * The new classpath includes all <tt>*.jar files.
 *
 * @author Derek DeMoro
 */
public class PluginClassLoader extends URLClassLoader {

    /**
     * Constructs the classloader.
     *
     * @param parent the parent class loader (or null for none).
     * @param libDir the directory to load jar files from.
     * @throws java.net.MalformedURLException if the libDir path is not valid.
     */
    public PluginClassLoader(ClassLoader parent, File libDir) throws MalformedURLException {
        super(new URL[] { libDir.toURI().toURL() }, parent);
    }

    /**
     * Adds all archives in a plugin to the classpath.
     *
     * @param pluginDir the directory of the plugin.
     * @throws MalformedURLException the exception thrown if URL is not valid.
     */
    public void addPlugin(File pluginDir) throws MalformedURLException {
        File libDir = new File(pluginDir, "lib");
        File[] jars = libDir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                boolean accept = false;
                String smallName = name.toLowerCase();
                if (smallName.endsWith(".jar")) {
                    accept = true;
                } else if (smallName.endsWith(".zip")) {
                    accept = true;
                }
                return accept;
            }
        });
        if (jars == null) {
            return;
        }
        for (File jar : jars) {
            if (jar.isFile()) {
                final URL url = jar.toURL();
                addURL(url);
                try {
                    checkForSmackProviders(url);
                } catch (Throwable e) {
                    Log.error(e);
                }
            }
        }
    }

    private void checkForSmackProviders(URL jarURL) throws Throwable {
        ZipFile zipFile = new JarFile(URLFileSystem.url2File(jarURL));
        ZipEntry entry = zipFile.getEntry("META-INF/smack.providers");
        if (entry != null) {
            InputStream zin = zipFile.getInputStream(entry);
            loadSmackProvider(zin);
        }
    }

    private void loadSmackProvider(InputStream providerStream) throws Exception {
        try {
            XmlPullParser parser = new MXParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(providerStream, "UTF-8");
            int eventType = parser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("iqProvider")) {
                        parser.next();
                        parser.next();
                        String elementName = parser.nextText();
                        parser.next();
                        parser.next();
                        String namespace = parser.nextText();
                        parser.next();
                        parser.next();
                        String className = parser.nextText();
                        try {
                            Class<?> provider = this.loadClass(className);
                            if (IQProvider.class.isAssignableFrom(provider)) {
                                ProviderManager.getInstance().addIQProvider(elementName, namespace, provider.newInstance());
                            } else if (IQ.class.isAssignableFrom(provider)) {
                                ProviderManager.getInstance().addIQProvider(elementName, namespace, provider.newInstance());
                            }
                        } catch (ClassNotFoundException cnfe) {
                            cnfe.printStackTrace();
                        }
                    } else if (parser.getName().equals("extensionProvider")) {
                        parser.next();
                        parser.next();
                        String elementName = parser.nextText();
                        parser.next();
                        parser.next();
                        String namespace = parser.nextText();
                        parser.next();
                        parser.next();
                        String className = parser.nextText();
                        try {
                            Class<?> provider = this.loadClass(className);
                            if (PacketExtensionProvider.class.isAssignableFrom(provider)) {
                                ProviderManager.getInstance().addExtensionProvider(elementName, namespace, provider.newInstance());
                            } else if (PacketExtension.class.isAssignableFrom(provider)) {
                                ProviderManager.getInstance().addExtensionProvider(elementName, namespace, provider.newInstance());
                            }
                        } catch (ClassNotFoundException cnfe) {
                            cnfe.printStackTrace();
                        }
                    }
                }
                eventType = parser.next();
            } while (eventType != XmlPullParser.END_DOCUMENT);
        } finally {
            try {
                providerStream.close();
            } catch (Exception e) {
            }
        }
    }
}

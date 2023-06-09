package org.asteriskjava.fastagi;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.asteriskjava.util.Log;
import org.asteriskjava.util.LogFactory;

/**
 * Abstract base class for common mapping strategies.
 * <p/>
 * If you implement your own mapping strategy you can derive from this class.
 * 
 * @author srt
 * @since 0.3
 */
public abstract class AbstractMappingStrategy implements MappingStrategy {

    /**
     * Reference to Asterisk-Java's logging subsystem.
     */
    protected Log logger = LogFactory.getLog(getClass());

    private static final String[] DEFAULT_SCRIPT_PATH = new String[] { "agi" };

    private ClassLoader defaultClassLoader = null;

    /**
     * Returns the ClassLoader to use for loading AgiScript classes and load
     * other resources like the mapping properties file.<p>
     * By default this method returns a class loader that searches for classes in the
     * "agi" subdirectory (if it exists) and uses the context class loader of the
     * current thread as the parent class loader.<p>
     * You can override this method if you prefer using a different class loader.
     *
     * @return the ClassLoader to use for loading AgiScript classes and load
     *         other resources like the mapping properties file.
     * @since 1.0.0
     */
    protected synchronized ClassLoader getClassLoader() {
        if (defaultClassLoader == null) {
            final ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
            final List<URL> dirUrls = new ArrayList<URL>();
            for (String scriptPathEntry : DEFAULT_SCRIPT_PATH) {
                final File scriptDir = new File(scriptPathEntry);
                if (!scriptDir.isDirectory()) {
                    continue;
                }
                try {
                    dirUrls.add(scriptDir.toURI().toURL());
                } catch (MalformedURLException e) {
                }
            }
            if (dirUrls.size() == 0) {
                return parentClassLoader;
            }
            defaultClassLoader = new URLClassLoader(dirUrls.toArray(new URL[dirUrls.size()]), parentClassLoader);
        }
        return defaultClassLoader;
    }

    /**
     * Creates a new instance of an AGI script.
     * 
     * @param className Class name of the AGI script. The class must implement
     *            {@link AgiScript}.
     * @return the created instance of the AGI script class. If the instance
     *         can't be created an error is logged and <code>null</code> is
     *         returned.
     */
    @SuppressWarnings("unchecked")
    protected AgiScript createAgiScriptInstance(String className) {
        Class tmpClass;
        Class<AgiScript> agiScriptClass;
        Constructor<AgiScript> constructor;
        AgiScript agiScript;
        agiScript = null;
        try {
            tmpClass = getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e1) {
            logger.debug("Unable to create AgiScript instance of type " + className + ": Class not found, make sure the class exists and is available on the CLASSPATH");
            return null;
        }
        if (!AgiScript.class.isAssignableFrom(tmpClass)) {
            logger.error("Unable to create AgiScript instance of type " + className + ": Class does not implement the AgiScript interface");
            return null;
        }
        agiScriptClass = (Class<AgiScript>) tmpClass;
        try {
            constructor = agiScriptClass.getConstructor();
            agiScript = constructor.newInstance();
        } catch (Exception e) {
            logger.warn("Unable to create AgiScript instance of type " + className, e);
        }
        return agiScript;
    }
}

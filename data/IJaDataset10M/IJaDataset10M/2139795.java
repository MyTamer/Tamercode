package com.sun.mail.util.logging;

import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.security.*;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.*;
import javax.mail.Authenticator;

/**
 * An adapter class to allow the Mail API to access the LogManager properties.
 * The LogManager properties are treated as the root of all properties.
 * First, the parent properties are searched. If no value is found, then,
 * the LogManager is searched with prefix value.  If not found, then, just the
 * key itself is searched in the LogManager. If a value is found in the
 * LogManager it is then copied to this properties object with no key prefix.
 * If no value is found in the LogManager or the parent properties, then this
 * properties object is searched only by passing the key value.
 *
 * <p>
 * This class also emulates the LogManager functions for creating new objects
 * from string class names.  This is to support initial setup of objects such as
 * log filters, formatters, error managers, etc.
 *
 * <p>
 * This class should never be exposed outside of this package.  Keep this
 * class package private (default access).
 * 
 * @author Jason Mehrens
 * @since JavaMail 1.4.3
 */
final class LogManagerProperties extends Properties {

    private static final long serialVersionUID = -2239983349056806252L;

    /**
     * Caches the LogManager so we only read the config once.
     */
    private static final LogManager LOG_MANAGER = LogManager.getLogManager();

    /**
     * Gets the LogManger for the running JVM.
     * @return the LogManager.
     * @since JavaMail 1.4.5
     */
    static LogManager getLogManager() {
        return LOG_MANAGER;
    }

    /**
     * Converts a locale to a language tag.
     * @param locale the locale to convert.
     * @return the language tag.
     * @throws NullPointerException if the given locale is null.
     * @since JavaMail 1.4.5
     */
    static String toLanguageTag(final Locale locale) {
        final String l = locale.getLanguage();
        final String c = locale.getCountry();
        final String v = locale.getVariant();
        final char[] b = new char[l.length() + c.length() + v.length() + 2];
        int count = l.length();
        l.getChars(0, count, b, 0);
        if (c.length() != 0 || (l.length() != 0 && v.length() != 0)) {
            b[count] = '-';
            ++count;
            c.getChars(0, c.length(), b, count);
            count += c.length();
        }
        if (v.length() != 0 && (l.length() != 0 || c.length() != 0)) {
            b[count] = '-';
            ++count;
            v.getChars(0, v.length(), b, count);
            count += v.length();
        }
        return String.valueOf(b, 0, count);
    }

    /**
     * Creates a new filter from the given class name.
     * @param name the fully qualified class name.
     * @return a new filter.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     */
    static Filter newFilter(String name) throws Exception {
        return (Filter) newObjectFrom(name, Filter.class);
    }

    /**
     * Creates a new formatter from the given class name.
     * @param name the fully qualified class name.
     * @return a new formatter.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     */
    static Formatter newFormatter(String name) throws Exception {
        return (Formatter) newObjectFrom(name, Formatter.class);
    }

    /**
     * Creates a new log record comparator from the given class name.
     * @param name the fully qualified class name.
     * @return a new comparator.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     * @see java.util.logging.LogRecord
     */
    static Comparator newComparator(String name) throws Exception {
        return (Comparator) newObjectFrom(name, Comparator.class);
    }

    /**
     * Creates a new error manager from the given class name.
     * @param name the fully qualified class name.
     * @return a new error manager.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     */
    static ErrorManager newErrorManager(String name) throws Exception {
        return (ErrorManager) newObjectFrom(name, ErrorManager.class);
    }

    /**
     * Creates a new authenticator from the given class name.
     * @param name the fully qualified class name.
     * @return a new authenticator.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     */
    static Authenticator newAuthenticator(String name) throws Exception {
        return (Authenticator) newObjectFrom(name, Authenticator.class);
    }

    /**
     * Creates a new object from the given class name.
     * @param name the fully qualified class name.
     * @param type the assignable type for the given name.
     * @return a new object assignable to the given type.
     * @throws ClassCastException if class name does not match the type.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws IllegalAccessException if the constructor is inaccessible.
     * @throws InstantiationException if the given class name is abstract.
     * @throws InvocationTargetException if the constructor throws an exception.
     * @throws LinkageError if the linkage fails.
     * @throws ExceptionInInitializerError if the static initializer fails.
     * @throws Exception to match the error method of the ErrorManager.
     * @throws NoSuchMethodException if the class name does not have a no
     * argument constructor.
     * @since JavaMail 1.4.5
     */
    private static Object newObjectFrom(String name, Class type) throws Exception {
        try {
            final Class clazz = LogManagerProperties.findClass(name);
            if (type.isAssignableFrom(clazz)) {
                try {
                    return clazz.getConstructor((Class[]) null).newInstance((Object[]) null);
                } catch (final InvocationTargetException ITE) {
                    throw paramOrError(ITE);
                }
            } else {
                throw new ClassCastException(clazz.getName() + " cannot be cast to " + type.getName());
            }
        } catch (final NoClassDefFoundError NCDFE) {
            throw new ClassNotFoundException(NCDFE.toString(), NCDFE);
        } catch (final ExceptionInInitializerError EIIE) {
            if (EIIE.getCause() instanceof Error) {
                throw EIIE;
            } else {
                throw new InvocationTargetException(EIIE);
            }
        }
    }

    /**
     * Returns the given exception or throws the escaping cause.
     * @param ite any invocation target.
     * @return the exception.
     * @throws VirtualMachineError if present as cause.
     * @throws ThreadDeath if present as cause.
     * @since JavaMail 1.4.5
     */
    private static Exception paramOrError(InvocationTargetException ite) {
        final Throwable cause = ite.getCause();
        if (cause != null) {
            if (cause instanceof VirtualMachineError || cause instanceof ThreadDeath) {
                throw (Error) cause;
            }
        }
        return ite;
    }

    /**
     * This code is modified from the LogManager, which explictly states
     * searching the system class loader first, then the context class loader.
     * There is resistance (compatibility) to change this behavior to simply
     * searching the context class loader.
     * @param name full class name
     * @return the class.
     * @throws LinkageError if the linkage fails.
     * @throws ClassNotFoundException if the class name was not found.
     * @throws ExceptionInInitializerError if static initializer fails.
     */
    private static Class findClass(String name) throws ClassNotFoundException {
        ClassLoader[] loaders = getClassLoaders();
        assert loaders.length == 2 : loaders.length;
        Class clazz;
        if (loaders[0] != null) {
            try {
                clazz = Class.forName(name, false, loaders[0]);
            } catch (ClassNotFoundException tryContext) {
                clazz = tryLoad(name, loaders[1]);
            }
        } else {
            clazz = tryLoad(name, loaders[1]);
        }
        return clazz;
    }

    private static Class tryLoad(String name, ClassLoader l) throws ClassNotFoundException {
        if (l != null) {
            return Class.forName(name, false, l);
        } else {
            return Class.forName(name);
        }
    }

    private static ClassLoader[] getClassLoaders() {
        return (ClassLoader[]) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                final ClassLoader[] loaders = new ClassLoader[2];
                try {
                    loaders[0] = ClassLoader.getSystemClassLoader();
                } catch (SecurityException ignore) {
                    loaders[0] = null;
                }
                try {
                    loaders[1] = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException ignore) {
                    loaders[1] = null;
                }
                return loaders;
            }
        });
    }

    /**
     * The namespace prefix to search LogManager and defaults.
     */
    private final String prefix;

    /**
     * Creates a log manager properties object.
     * @param parent the parent properties.
     * @param prefix the namespace prefix.
     * @throws NullPointerException if <tt>prefix</tt> or <tt>parent</tt> is
     * <tt>null</tt>.
     */
    LogManagerProperties(final Properties parent, final String prefix) {
        super(parent);
        parent.isEmpty();
        if (prefix == null) {
            throw new NullPointerException();
        }
        this.prefix = prefix;
        super.isEmpty();
    }

    /**
     * Returns a properties object that contains a snapshot of the current
     * state.  This method violates the clone contract so that no instances
     * of LogManagerProperties is exported for public use.
     * @return the snapshot.
     * @since JavaMail 1.4.4
     */
    public synchronized Object clone() {
        return exportCopy(defaults);
    }

    /**
     * Searches defaults, then searches the log manager
     * by the prefix property, and then by the key itself.
     * @param key a non null key.
     * @return the value for that key.
     */
    public synchronized String getProperty(final String key) {
        String value = defaults.getProperty(key);
        if (value == null) {
            final LogManager manager = getLogManager();
            if (key.length() > 0) {
                value = manager.getProperty(prefix + '.' + key);
            }
            if (value == null) {
                value = manager.getProperty(key);
            }
            if (value != null) {
                super.put(key, value);
            } else {
                Object v = super.get(key);
                value = v instanceof String ? (String) v : null;
            }
        }
        return value;
    }

    /**
     * Calls getProperty directly.  If getProperty returns null the default
     * value is returned.
     * @param key a key to search for.
     * @param def the default value to use if not found.
     * @return the value for the key.
     * @since JavaMail 1.4.4
     */
    public String getProperty(final String key, final String def) {
        final String value = this.getProperty(key);
        return value == null ? def : value;
    }

    /**
     * Required to work with PropUtil.  Calls getProperty directly if the
     * given key is a string.  Otherwise, performs a normal get operation.
     * @param key any key.
     * @return the value for the key or null.
     * @since JavaMail 1.4.5
     */
    public Object get(final Object key) {
        if (key instanceof String) {
            return this.getProperty((String) key);
        } else {
            return super.get(key);
        }
    }

    /**
     * Required to work with PropUtil.  An updated copy of the key is fetched
     * from the log manager if the key doesn't exist in this properties.
     * @param key any key.
     * @return the value for the key or the default value for the key.
     * @since JavaMail 1.4.5
     */
    public synchronized Object put(final Object key, final Object value) {
        final Object def = preWrite(key);
        final Object man = super.put(key, value);
        return man == null ? def : man;
    }

    /**
     * Calls the put method directly.
     * @param key any key.
     * @return the value for the key or the default value for the key.
     * @since JavaMail 1.4.5
     */
    public Object setProperty(String key, String value) {
        return this.put(key, value);
    }

    /**
     * Required to work with PropUtil.  An updated copy of the key is fetched
     * from the log manager prior to returning.
     * @param key any key.
     * @return the value for the key or null.
     * @since JavaMail 1.4.5
     */
    public boolean containsKey(final Object key) {
        if (key instanceof String) {
            return this.getProperty((String) key) != null;
        } else {
            return super.containsKey(key);
        }
    }

    /**
     * Required to work with PropUtil.  An updated copy of the key is fetched
     * from the log manager if the key doesn't exist in this properties.
     * @param key any key.
     * @return the value for the key or the default value for the key.
     * @since JavaMail 1.4.5
     */
    public synchronized Object remove(final Object key) {
        final Object def = preWrite(key);
        final Object man = super.remove(key);
        return man == null ? def : man;
    }

    /**
     * It is assumed that this method will never be called.
     * No way to get the property names from LogManager.
     * @return the property names
     */
    public Enumeration propertyNames() {
        assert false;
        return super.propertyNames();
    }

    /**
     * It is assumed that this method will never be called.
     * The prefix value is not used for the equals method.
     * @param o any object or null.
     * @return true if equal, otherwise false.
     */
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o instanceof Properties == false) {
            return false;
        }
        assert false : prefix;
        return super.equals(o);
    }

    /**
     * It is assumed that this method will never be called.  See equals.
     * @return the hash code.
     */
    public int hashCode() {
        assert false : prefix.hashCode();
        return super.hashCode();
    }

    /**
     * Called before a write operation of a key.
     * Caches a key read from the log manager in this properties object.
     * The key is only cached if it is an instance of a String and
     * this properties doesn't contain a copy of the key.
     * @param key the key to search.
     * @return the default value for the key.
     */
    private Object preWrite(final Object key) {
        assert Thread.holdsLock(this);
        Object value;
        if (key instanceof String && !super.containsKey(key)) {
            value = this.getProperty((String) key);
        } else {
            value = null;
        }
        return value;
    }

    /**
     * Creates a public snapshot of this properties object using
     * the given parent properties.
     * @param parent the defaults to use with the snapshot.
     * @return the safe snapshot.
     */
    private Properties exportCopy(final Properties parent) {
        Thread.holdsLock(this);
        final Properties child = new Properties(parent);
        child.putAll(this);
        return child;
    }

    /**
     * It is assumed that this method will never be called.
     * We return a safe copy for export to avoid locking this properties
     * object or the defaults during write.
     * @return the parent properties.
     * @throws ObjectStreamException if there is a problem.
     */
    private synchronized Object writeReplace() throws ObjectStreamException {
        assert false;
        return exportCopy((Properties) defaults.clone());
    }
}

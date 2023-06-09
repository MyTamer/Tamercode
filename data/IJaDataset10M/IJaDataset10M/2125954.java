package com.db4o.internal;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import com.db4o.*;
import com.db4o.ext.*;
import com.db4o.foundation.*;
import com.db4o.reflect.*;
import com.db4o.reflect.core.*;
import com.db4o.reflect.jdk.*;

/**
 * @sharpen.ignore
 */
@decaf.Remove(decaf.Platform.JDK11)
class JDK_1_4 extends JDK_1_3 {

    private Hashtable fileLocks;

    private Object _reflectionFactory;

    private Constructor _objectConstructor;

    private Method _factoryMethod;

    @decaf.Remove(decaf.Platform.JDK11)
    public static final class Factory implements JDKFactory {

        public JDK tryToCreate() {
            if (!classIsAvailable("java.nio.channels.FileLock")) {
                return null;
            }
            return new JDK_1_4();
        }
    }

    synchronized void lockFile(String path, Object file) {
        String canonicalPath;
        try {
            canonicalPath = new File(path).getCanonicalPath();
        } catch (IOException e) {
            throw new Db4oIOException(e);
        }
        if (fileLocks == null) {
            fileLocks = new Hashtable();
        }
        if (fileLocks.containsKey(canonicalPath)) {
            throw new DatabaseFileLockedException(canonicalPath);
        }
        Object lock = null;
        Object channel = Reflection4.invoke(file, "getChannel");
        try {
            lock = Reflection4.invoke(channel, "tryLock");
        } catch (ReflectException rex) {
            throw new DatabaseFileLockedException(canonicalPath, rex);
        }
        if (lock == null) {
            throw new DatabaseFileLockedException(canonicalPath);
        }
        fileLocks.put(canonicalPath, lock);
    }

    synchronized void unlockFile(String path, Object file) {
        if (fileLocks == null) {
            return;
        }
        Object fl = fileLocks.get(path);
        if (fl == null) {
            return;
        }
        Reflection4.invoke("java.nio.channels.FileLock", "release", null, null, fl);
        fileLocks.remove(path);
    }

    public ReflectConstructor serializableConstructor(Reflector reflector, Class clazz) {
        if (_reflectionFactory == null) {
            if (!initSerializableConstructor()) {
                Platform4.callConstructorCheck = TernaryBool.YES;
                return null;
            }
        }
        Constructor serializableConstructor = (Constructor) Reflection4.invoke(new Object[] { clazz, _objectConstructor }, _reflectionFactory, _factoryMethod);
        if (null == serializableConstructor) {
            return null;
        }
        return new JdkConstructor(reflector, serializableConstructor);
    }

    private boolean initSerializableConstructor() {
        try {
            _reflectionFactory = Reflection4.invoke(Platform4.REFLECTIONFACTORY, "getReflectionFactory", null, null, null);
            _factoryMethod = Reflection4.getMethod(Platform4.REFLECTIONFACTORY, "newConstructorForSerialization", new Class[] { Class.class, Constructor.class });
            if (_factoryMethod == null) {
                return false;
            }
        } catch (ReflectException e) {
            return false;
        }
        try {
            _objectConstructor = Object.class.getDeclaredConstructor((Class[]) null);
            return true;
        } catch (Exception e) {
            if (Debug4.atHome) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public int ver() {
        return 4;
    }
}

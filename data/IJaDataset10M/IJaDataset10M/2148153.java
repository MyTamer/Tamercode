package util;

import java.util.Hashtable;

/**
 * A simple test class loader capable of loading from
 * multiple sources, such as local files or a URL.
 *
 * This class is derived from an article by Chuck McManis
 * http://www.javaworld.com/javaworld/jw-10-1996/indepth.src.html
 * with large modifications.
 *
 * Note that this has been updated to use the non-deprecated version of
 * defineClass() -- JDM.
 *
 * @author Jack Harich - 8/18/97
 * @author John D. Mitchell - 99.03.04
 */
public abstract class MultiClassLoader extends ClassLoader {

    private Hashtable classes = new Hashtable();

    private char classNameReplacementChar;

    protected boolean monitorOn = true;

    protected boolean sourceMonitorOn = true;

    public MultiClassLoader() {
    }

    /**
 * This is a simple version for external clients since they
 * will always want the class resolved before it is returned
 * to them.
 */
    public Class loadClass(String className) throws ClassNotFoundException {
        return (loadClass(className, true));
    }

    public synchronized Class loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
        Class result;
        byte[] classBytes;
        monitor(">> MultiClassLoader.loadClass(" + className + ", " + resolveIt + ")");
        result = (Class) classes.get(className);
        if (result != null) {
            monitor(">> returning cached result.");
            return result;
        }
        try {
            result = super.findSystemClass(className);
            monitor(">> returning system class (in CLASSPATH).");
            return result;
        } catch (ClassNotFoundException e) {
            monitor(">> Not a system class.");
        }
        classBytes = loadClassBytes(className);
        if (classBytes == null) {
            throw new ClassNotFoundException(className);
        }
        result = defineClass(className, classBytes, 0, classBytes.length);
        if (result == null) {
            throw new ClassFormatError();
        }
        if (resolveIt) resolveClass(result);
        classes.put(className, result);
        monitor(">> Returning newly loaded class.");
        return result;
    }

    /**
 * This optional call allows a class name such as
 * "COM.test.Hello" to be changed to "COM_test_Hello",
 * which is useful for storing classes from different
 * packages in the same retrival directory.
 * In the above example the char would be '_'.
 */
    public void setClassNameReplacementChar(char replacement) {
        classNameReplacementChar = replacement;
    }

    protected abstract byte[] loadClassBytes(String className);

    protected String formatClassName(String className) {
        if (classNameReplacementChar == ' ') {
            return className.replace('.', '/') + ".class";
        } else {
            return className.replace('.', classNameReplacementChar) + ".class";
        }
    }

    protected void monitor(String text) {
        if (monitorOn) print(text);
    }

    protected static void print(String text) {
        System.out.println(text);
    }
}

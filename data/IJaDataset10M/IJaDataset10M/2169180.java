package net.sf.katta.util.nonblocking;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * Simple class to obtain access to the {@link Unsafe} object. {@link Unsafe} is
 * required to allow efficient CAS operations on arrays. Note that the versions
 * in {@link java.util.concurrent.atomic}, such as {@link
 * java.util.concurrent.atomic.AtomicLongArray}, require extra memory ordering
 * guarantees which are generally not needed in these algorithms and are also
 * expensive on most processors.
 */
class UtilUnsafe {

    private UtilUnsafe() {
    }

    /** Fetch the Unsafe. Use With Caution. */
    public static Unsafe getUnsafe() {
        if (UtilUnsafe.class.getClassLoader() == null) return Unsafe.getUnsafe();
        try {
            final Field fld = Unsafe.class.getDeclaredField("theUnsafe");
            fld.setAccessible(true);
            return (Unsafe) fld.get(UtilUnsafe.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not obtain access to sun.misc.Unsafe", e);
        }
    }
}

package com.amazon.carbonado.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.cojen.classfile.ClassFile;
import org.cojen.classfile.CodeBuilder;
import org.cojen.classfile.Label;
import org.cojen.classfile.MethodDesc;
import org.cojen.classfile.Modifiers;
import org.cojen.classfile.TypeDesc;
import org.cojen.util.ClassInjector;

/**
 * Generic one-shot factory which supports late object creation. If the object
 * creation results in an exception or is taking too long, the object produced
 * instead is a bogus one. After retrying, if the real object is created, then
 * the bogus object turns into a wrapper to the real object.
 *
 * <p>Note: If a bogus object is created, the wrapper cannot always be a drop-in
 * replacement for the real object. If the wrapper is cloned, it won't have the
 * same behavior as cloning the real object. Also, synchronizing on the wrapper
 * will not synchronize the real object.
 *
 * @author Brian S O'Neill
 */
public abstract class BelatedCreator<T, E extends Exception> {

    private static final String REF_FIELD_NAME = "ref";

    private static final SoftValuedCache<Class<?>, Class<?>> cWrapperCache;

    private static final ExecutorService cThreadPool;

    static {
        cWrapperCache = SoftValuedCache.newCache(11);
        cThreadPool = Executors.newCachedThreadPool(new TFactory());
    }

    private final Class<T> mType;

    final int mMinRetryDelayMillis;

    private T mReal;

    private boolean mFailed;

    private Throwable mFailedError;

    private T mBogus;

    private AtomicReference<T> mRef;

    private CreateThread mCreateThread;

    /**
     * @param type type of object created
     * @param minRetryDelayMillis minimum milliseconds to wait before retrying
     * to create object after failure; if negative, never retry
     * @throws IllegalArgumentException if type is null or is not an interface
     */
    protected BelatedCreator(Class<T> type, int minRetryDelayMillis) {
        if (type == null) {
            throw new IllegalArgumentException("Type is null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Type must be an interface: " + type);
        }
        mType = type;
        mMinRetryDelayMillis = minRetryDelayMillis;
    }

    /**
     * Returns real or bogus object. If real object is returned, then future
     * invocations of this method return the same real object instance. This
     * method waits for the real object to be created, if it is blocked. If
     * real object creation fails immediately, then this method will not wait,
     * returning a bogus object immediately instead.
     *
     * @param timeoutMillis maximum time to wait for real object before
     * returning bogus one; if negative, potentially wait forever
     * @throws E exception thrown from createReal
     */
    public synchronized T get(final int timeoutMillis) throws E {
        if (mReal != null) {
            return mReal;
        }
        if (mBogus != null && mMinRetryDelayMillis < 0) {
            return mBogus;
        }
        if (mCreateThread == null) {
            mCreateThread = new CreateThread();
            cThreadPool.submit(mCreateThread);
        }
        if (timeoutMillis != 0) {
            final long start = System.nanoTime();
            try {
                if (timeoutMillis < 0) {
                    while (mReal == null && mCreateThread != null) {
                        if (timeoutMillis < 0) {
                            wait();
                        }
                    }
                } else {
                    long remaining = timeoutMillis;
                    while (mReal == null && mCreateThread != null && !mFailed) {
                        wait(remaining);
                        long elapsed = (System.nanoTime() - start) / 1000000L;
                        if ((remaining -= elapsed) <= 0) {
                            break;
                        }
                    }
                }
            } catch (InterruptedException e) {
            }
            if (mReal != null) {
                return mReal;
            }
            long elapsed = (System.nanoTime() - start) / 1000000L;
            if (elapsed >= timeoutMillis) {
                timedOutNotification(elapsed);
            }
        }
        if (mFailedError != null) {
            Throwable error = mFailedError;
            mFailedError = null;
            StackTraceElement[] trace = error.getStackTrace();
            error.fillInStackTrace();
            StackTraceElement[] localTrace = error.getStackTrace();
            StackTraceElement[] completeTrace = new StackTraceElement[trace.length + localTrace.length];
            System.arraycopy(trace, 0, completeTrace, 0, trace.length);
            System.arraycopy(localTrace, 0, completeTrace, trace.length, localTrace.length);
            error.setStackTrace(completeTrace);
            org.cojen.util.ThrowUnchecked.fire(error);
        }
        if (mBogus == null) {
            mRef = new AtomicReference<T>(createBogus());
            try {
                mBogus = getWrapper().newInstance(mRef);
            } catch (Exception e) {
                org.cojen.util.ThrowUnchecked.fire(e);
            }
        }
        return mBogus;
    }

    /**
     * Create instance of real object. If there is a recoverable error creating
     * the object, return null. Any error logging must be performed by the
     * implementation of this method. If null is returned, expect this method
     * to be called again in the future.
     *
     * @return real object, or null if there was a recoverable error
     * @throws E unrecoverable error
     */
    protected abstract T createReal() throws E;

    /**
     * Create instance of bogus object.
     */
    protected abstract T createBogus();

    /**
     * Notification that createReal is taking too long. This can be used to log
     * a message.
     *
     * @param timedOutMillis milliseconds waited before giving up
     */
    protected abstract void timedOutNotification(long timedOutMillis);

    /**
     * Notification that createReal has produced the real object. The default
     * implementation does nothing.
     */
    protected void createdNotification(T object) {
    }

    synchronized void created(T object) {
        mReal = object;
        if (mBogus != null) {
            mBogus = null;
            if (mRef != null) {
                mRef.set(object);
            }
        }
        mFailed = false;
        notifyAll();
        createdNotification(object);
    }

    synchronized void failed() {
        if (mReal == null) {
            mFailed = true;
        }
        notifyAll();
    }

    /**
     * @param error optional error to indicate thread is exiting because of this
     */
    synchronized void handleThreadExit(Throwable error) {
        if (mReal == null) {
            mFailed = true;
            if (error != null) {
                mFailedError = error;
            }
        }
        mCreateThread = null;
        notifyAll();
    }

    /**
     * Returns a Constructor that accepts an AtomicReference to the wrapped
     * object.
     */
    private Constructor<T> getWrapper() {
        Class<T> clazz;
        synchronized (cWrapperCache) {
            clazz = (Class<T>) cWrapperCache.get(mType);
            if (clazz == null) {
                clazz = createWrapper();
                cWrapperCache.put(mType, clazz);
            }
        }
        try {
            return clazz.getConstructor(AtomicReference.class);
        } catch (NoSuchMethodException e) {
            org.cojen.util.ThrowUnchecked.fire(e);
            return null;
        }
    }

    private Class<T> createWrapper() {
        ClassInjector ci = ClassInjector.create();
        ClassFile cf = new ClassFile(ci.getClassName());
        cf.addInterface(mType);
        cf.markSynthetic();
        cf.setSourceFile(BelatedCreator.class.getName());
        cf.setTarget("1.5");
        final TypeDesc atomicRefType = TypeDesc.forClass(AtomicReference.class);
        cf.addField(Modifiers.PRIVATE.toFinal(true), REF_FIELD_NAME, atomicRefType);
        CodeBuilder b = new CodeBuilder(cf.addConstructor(Modifiers.PUBLIC, new TypeDesc[] { atomicRefType }));
        b.loadThis();
        b.invokeSuperConstructor(null);
        b.loadThis();
        b.loadLocal(b.getParameter(0));
        b.storeField(REF_FIELD_NAME, atomicRefType);
        b.returnVoid();
        for (Method m : mType.getMethods()) {
            try {
                Object.class.getMethod(m.getName(), m.getParameterTypes());
                continue;
            } catch (NoSuchMethodException e) {
            }
            addWrappedCall(cf, new CodeBuilder(cf.addMethod(m)), m);
        }
        for (Method m : Object.class.getMethods()) {
            int modifiers = m.getModifiers();
            if (!Modifier.isFinal(modifiers) && Modifier.isPublic(modifiers)) {
                b = new CodeBuilder(cf.addMethod(Modifiers.PUBLIC, m.getName(), MethodDesc.forMethod(m)));
                addWrappedCall(cf, b, m);
            }
        }
        Class<T> clazz = ci.defineClass(cf);
        return clazz;
    }

    private void addWrappedCall(ClassFile cf, CodeBuilder b, Method m) {
        boolean isEqualsMethod = false;
        if (m.getName().equals("equals") && m.getReturnType().equals(boolean.class)) {
            Class[] paramTypes = m.getParameterTypes();
            isEqualsMethod = paramTypes.length == 1 && paramTypes[0].equals(Object.class);
        }
        if (isEqualsMethod) {
            b.loadThis();
            b.loadLocal(b.getParameter(0));
            Label notEqual = b.createLabel();
            b.ifEqualBranch(notEqual, false);
            b.loadConstant(true);
            b.returnValue(TypeDesc.BOOLEAN);
            notEqual.setLocation();
            b.loadLocal(b.getParameter(0));
            b.instanceOf(cf.getType());
            Label isInstance = b.createLabel();
            b.ifZeroComparisonBranch(isInstance, "!=");
            b.loadConstant(false);
            b.returnValue(TypeDesc.BOOLEAN);
            isInstance.setLocation();
        }
        final TypeDesc atomicRefType = TypeDesc.forClass(AtomicReference.class);
        b.loadThis();
        b.loadField(REF_FIELD_NAME, atomicRefType);
        b.invokeVirtual(atomicRefType, "get", TypeDesc.OBJECT, null);
        b.checkCast(TypeDesc.forClass(mType));
        for (int i = 0; i < b.getParameterCount(); i++) {
            b.loadLocal(b.getParameter(i));
        }
        if (isEqualsMethod) {
            b.checkCast(cf.getType());
            b.loadField(REF_FIELD_NAME, atomicRefType);
            b.invokeVirtual(atomicRefType, "get", TypeDesc.OBJECT, null);
            b.checkCast(TypeDesc.forClass(mType));
        }
        b.invoke(m);
        if (m.getReturnType() == void.class) {
            b.returnVoid();
        } else {
            b.returnValue(TypeDesc.forClass(m.getReturnType()));
        }
    }

    private class CreateThread implements Runnable {

        public void run() {
            try {
                while (true) {
                    T real = createReal();
                    if (real != null) {
                        created(real);
                        break;
                    }
                    failed();
                    if (mMinRetryDelayMillis < 0) {
                        break;
                    }
                    try {
                        Thread.sleep(mMinRetryDelayMillis);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                handleThreadExit(null);
            } catch (Throwable e) {
                handleThreadExit(e);
            }
        }
    }

    private static class TFactory implements ThreadFactory {

        private static int cCount;

        private static synchronized int nextID() {
            return ++cCount;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("BelatedCreator-" + nextID());
            return t;
        }
    }
}

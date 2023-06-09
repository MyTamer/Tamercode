package org.ogre4j;

import org.xbig.base.*;

public class ArchiveManager extends org.xbig.base.NativeObject implements org.ogre4j.IArchiveManager {

    static {
        System.loadLibrary("ogre4j");
    }

    protected static class ArchiveFactoryMap extends org.xbig.base.NativeObject implements org.ogre4j.IArchiveManager.IArchiveFactoryMap {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public ArchiveFactoryMap(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected ArchiveFactoryMap(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public ArchiveFactoryMap(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public ArchiveFactoryMap() {
            super(new org.xbig.base.InstancePointer(__createArchiveFactoryMap()), false);
        }

        private static native long __createArchiveFactoryMap();

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public int count(String key) {
            return _count__sR(this.object.pointer, key);
        }

        private native int _count__sR(long _pointer_, String key);

        /** **/
        public boolean empty() {
            return _empty_const(this.object.pointer);
        }

        private native boolean _empty_const(long _pointer_);

        /** **/
        public int erase(String key) {
            return _erase__sR(this.object.pointer, key);
        }

        private native int _erase__sR(long _pointer_, String key);

        /** **/
        public int max_size() {
            return _max_size_const(this.object.pointer);
        }

        private native int _max_size_const(long _pointer_);

        /** **/
        public int size() {
            return _size_const(this.object.pointer);
        }

        private native int _size_const(long _pointer_);

        /** **/
        public org.ogre4j.IArchiveFactory get(String key) {
            return new org.ogre4j.ArchiveFactory(new InstancePointer(_get__sR(this.object.pointer, key)));
        }

        private native long _get__sR(long _pointer_, String key);

        /** **/
        public void insert(String key, org.ogre4j.IArchiveFactory value) {
            _insert__sROgre_ArchiveFactoryp(this.object.pointer, key, value.getInstancePointer().pointer);
        }

        private native void _insert__sROgre_ArchiveFactoryp(long _pointer_, String key, long value);
    }

    protected static class ArchiveMap extends org.xbig.base.NativeObject implements org.ogre4j.IArchiveManager.IArchiveMap {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public ArchiveMap(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected ArchiveMap(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public ArchiveMap(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public ArchiveMap() {
            super(new org.xbig.base.InstancePointer(__createArchiveMap()), false);
        }

        private static native long __createArchiveMap();

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public int count(String key) {
            return _count__sR(this.object.pointer, key);
        }

        private native int _count__sR(long _pointer_, String key);

        /** **/
        public boolean empty() {
            return _empty_const(this.object.pointer);
        }

        private native boolean _empty_const(long _pointer_);

        /** **/
        public int erase(String key) {
            return _erase__sR(this.object.pointer, key);
        }

        private native int _erase__sR(long _pointer_, String key);

        /** **/
        public int max_size() {
            return _max_size_const(this.object.pointer);
        }

        private native int _max_size_const(long _pointer_);

        /** **/
        public int size() {
            return _size_const(this.object.pointer);
        }

        private native int _size_const(long _pointer_);

        /** **/
        public org.ogre4j.IArchive get(String key) {
            return new org.ogre4j.Archive(new InstancePointer(_get__sR(this.object.pointer, key)));
        }

        private native long _get__sR(long _pointer_, String key);

        /** **/
        public void insert(String key, org.ogre4j.IArchive value) {
            _insert__sROgre_Archivep(this.object.pointer, key, value.getInstancePointer().pointer);
        }

        private native void _insert__sROgre_Archivep(long _pointer_, String key, long value);
    }

    public static class ArchiveMapIterator extends org.xbig.base.NativeObject implements org.ogre4j.IArchiveManager.IArchiveMapIterator {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public ArchiveMapIterator(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected ArchiveMapIterator(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public ArchiveMapIterator(org.xbig.base.WithoutNativeObject val) {
            super(val);
        }

        public void delete() {
            if (this.remote) {
                throw new RuntimeException("can't dispose object created by native library");
            }
            if (!this.deleted) {
                __delete(object.pointer);
                this.deleted = true;
                this.object.pointer = 0;
            }
        }

        public void finalize() {
            if (!this.remote && !this.deleted) {
                delete();
            }
        }

        private final native void __delete(long _pointer_);

        /** **/
        public boolean hasMoreElements() {
            return _hasMoreElements_const(this.object.pointer);
        }

        private native boolean _hasMoreElements_const(long _pointer_);

        /** **/
        public org.ogre4j.IArchive getNext() {
            return new org.ogre4j.Archive(new InstancePointer(_getNext(this.object.pointer)));
        }

        private native long _getNext(long _pointer_);

        /** **/
        public org.ogre4j.IArchive peekNextValue() {
            return new org.ogre4j.Archive(new InstancePointer(_peekNextValue(this.object.pointer)));
        }

        private native long _peekNextValue(long _pointer_);

        /** **/
        public String peekNextKey() {
            return _peekNextKey(this.object.pointer);
        }

        private native String _peekNextKey(long _pointer_);

        /** **/
        public org.ogre4j.IArchiveManager.IArchiveMapIterator operatorAssignment(org.ogre4j.IArchiveManager.IArchiveMapIterator rhs) {
            return new org.ogre4j.ArchiveManager.ArchiveMapIterator(new InstancePointer(_operatorAssignment___Ogre_ArchiveManager_ArchiveMapIteratorr(this.object.pointer, rhs.getInstancePointer().pointer)));
        }

        private native long _operatorAssignment___Ogre_ArchiveManager_ArchiveMapIteratorr(long _pointer_, long rhs);

        /** **/
        public NativeObjectPointer<org.ogre4j.IArchive> peekNextValuePtr() {
            return new NativeObjectPointer<org.ogre4j.IArchive>(new InstancePointer(_peekNextValuePtr(this.object.pointer)));
        }

        private native long _peekNextValuePtr(long _pointer_);

        /** **/
        public void moveNext() {
            _moveNext(this.object.pointer);
        }

        private native void _moveNext(long _pointer_);
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ArchiveManager(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ArchiveManager(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ArchiveManager(org.xbig.base.WithoutNativeObject val) {
        super(val);
    }

    public void delete() {
        if (this.remote) {
            throw new RuntimeException("can't dispose object created by native library");
        }
        if (!this.deleted) {
            __delete(object.pointer);
            this.deleted = true;
            this.object.pointer = 0;
        }
    }

    public void finalize() {
        if (!this.remote && !this.deleted) {
            delete();
        }
    }

    private final native void __delete(long _pointer_);

    /** 
    Default constructor - should never get called by a client app. **/
    public ArchiveManager() {
        super(new org.xbig.base.InstancePointer(__createArchiveManager()), false);
    }

    private static native long __createArchiveManager();

    /** 
    Opens an archive for file reading. **/
    public org.ogre4j.IArchive load(String filename, String archiveType) {
        return new org.ogre4j.Archive(new InstancePointer(_load__StringRStringR(this.object.pointer, filename, archiveType)));
    }

    private native long _load__StringRStringR(long _pointer_, String filename, String archiveType);

    /** 
    Unloads an archive. **/
    public void unload(org.ogre4j.IArchive arch) {
        _unload__Archivep(this.object.pointer, arch.getInstancePointer().pointer);
    }

    private native void _unload__Archivep(long _pointer_, long arch);

    /** 
    Unloads an archive by name. **/
    public void unload(String filename) {
        _unload__StringR(this.object.pointer, filename);
    }

    private native void _unload__StringR(long _pointer_, String filename);

    /** 
    Get an iterator over the Archives in this Manager. **/
    public void getArchiveIterator(org.ogre4j.IArchiveManager.IArchiveMapIterator returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getArchiveIterator(this.object.pointer)), false);
    }

    private native long _getArchiveIterator(long _pointer_);

    /** 
    Adds a new  to the list of available factories. **/
    public void addArchiveFactory(org.ogre4j.IArchiveFactory factory) {
        _addArchiveFactory__ArchiveFactoryp(this.object.pointer, factory.getInstancePointer().pointer);
    }

    private native void _addArchiveFactory__ArchiveFactoryp(long _pointer_, long factory);

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IArchiveManager getSingleton() {
        return new org.ogre4j.ArchiveManager(new InstancePointer(_getSingleton()));
    }

    private static native long _getSingleton();

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IArchiveManager getSingletonPtr() {
        return new org.ogre4j.ArchiveManager(new InstancePointer(_getSingletonPtr()));
    }

    private static native long _getSingletonPtr();
}

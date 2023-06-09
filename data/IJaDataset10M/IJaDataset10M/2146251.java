package org.ogre4j;

import org.xbig.base.*;

public class FileInfoList extends org.xbig.base.NativeObject implements org.ogre4j.IFileInfoList {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public FileInfoList(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected FileInfoList(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public FileInfoList(org.xbig.base.WithoutNativeObject val) {
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
    public FileInfoList() {
        super(new org.xbig.base.InstancePointer(__createFileInfoList()), false);
    }

    private static native long __createFileInfoList();

    /** **/
    public void assign(int num, org.ogre4j.IFileInfo val) {
        _assign__ivOgre_FileInfoR(this.object.pointer, num, val.getInstancePointer().pointer);
    }

    private native void _assign__ivOgre_FileInfoR(long _pointer_, int num, long val);

    /** **/
    public org.ogre4j.IFileInfo at(int loc) {
        return new org.ogre4j.FileInfo(new InstancePointer(_at__iv(this.object.pointer, loc)));
    }

    private native long _at__iv(long _pointer_, int loc);

    /** **/
    public org.ogre4j.IFileInfo back() {
        return new org.ogre4j.FileInfo(new InstancePointer(_back(this.object.pointer)));
    }

    private native long _back(long _pointer_);

    /** **/
    public int capacity() {
        return _capacity(this.object.pointer);
    }

    private native int _capacity(long _pointer_);

    /** **/
    public void clear() {
        _clear(this.object.pointer);
    }

    private native void _clear(long _pointer_);

    /** **/
    public boolean empty() {
        return _empty(this.object.pointer);
    }

    private native boolean _empty(long _pointer_);

    /** **/
    public org.ogre4j.IFileInfo front() {
        return new org.ogre4j.FileInfo(new InstancePointer(_front(this.object.pointer)));
    }

    private native long _front(long _pointer_);

    /** **/
    public int max_size() {
        return _max_size(this.object.pointer);
    }

    private native int _max_size(long _pointer_);

    /** **/
    public void pop_back() {
        _pop_back(this.object.pointer);
    }

    private native void _pop_back(long _pointer_);

    /** **/
    public void push_back(org.ogre4j.IFileInfo val) {
        _push_back__Ogre_FileInfoR(this.object.pointer, val.getInstancePointer().pointer);
    }

    private native void _push_back__Ogre_FileInfoR(long _pointer_, long val);

    /** **/
    public void reserve(int size) {
        _reserve__iV(this.object.pointer, size);
    }

    private native void _reserve__iV(long _pointer_, int size);

    /** **/
    public int size() {
        return _size(this.object.pointer);
    }

    private native int _size(long _pointer_);
}

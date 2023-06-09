package org.ogre4j;

import org.xbig.base.*;

public class DataStreamList extends org.xbig.base.NativeObject implements org.ogre4j.IDataStreamList {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public DataStreamList(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected DataStreamList(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public DataStreamList(org.xbig.base.WithoutNativeObject val) {
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
    public DataStreamList() {
        super(new org.xbig.base.InstancePointer(__createDataStreamList()), false);
    }

    private static native long __createDataStreamList();

    /** **/
    public void assign(int num, org.ogre4j.IDataStreamPtr val) {
        _assign__ivOgre_DataStreamPtrR(this.object.pointer, num, val.getInstancePointer().pointer);
    }

    private native void _assign__ivOgre_DataStreamPtrR(long _pointer_, int num, long val);

    /** **/
    public org.ogre4j.IDataStreamPtr back() {
        return new org.ogre4j.DataStreamPtr(new InstancePointer(_back(this.object.pointer)));
    }

    private native long _back(long _pointer_);

    /** **/
    public void clear() {
        _clear(this.object.pointer);
    }

    private native void _clear(long _pointer_);

    /** **/
    public boolean empty() {
        return _empty_const(this.object.pointer);
    }

    private native boolean _empty_const(long _pointer_);

    /** **/
    public org.ogre4j.IDataStreamPtr front() {
        return new org.ogre4j.DataStreamPtr(new InstancePointer(_front(this.object.pointer)));
    }

    private native long _front(long _pointer_);

    /** **/
    public int max_size() {
        return _max_size_const(this.object.pointer);
    }

    private native int _max_size_const(long _pointer_);

    /** **/
    public void pop_back() {
        _pop_back(this.object.pointer);
    }

    private native void _pop_back(long _pointer_);

    /** **/
    public void pop_front() {
        _pop_front(this.object.pointer);
    }

    private native void _pop_front(long _pointer_);

    /** **/
    public void push_back(org.ogre4j.IDataStreamPtr val) {
        _push_back__Ogre_DataStreamPtrR(this.object.pointer, val.getInstancePointer().pointer);
    }

    private native void _push_back__Ogre_DataStreamPtrR(long _pointer_, long val);

    /** **/
    public void push_front(org.ogre4j.IDataStreamPtr val) {
        _push_front__Ogre_DataStreamPtrR(this.object.pointer, val.getInstancePointer().pointer);
    }

    private native void _push_front__Ogre_DataStreamPtrR(long _pointer_, long val);

    /** **/
    public void remove(org.ogre4j.IDataStreamPtr val) {
        _remove__Ogre_DataStreamPtrR(this.object.pointer, val.getInstancePointer().pointer);
    }

    private native void _remove__Ogre_DataStreamPtrR(long _pointer_, long val);

    /** **/
    public void reverse() {
        _reverse(this.object.pointer);
    }

    private native void _reverse(long _pointer_);

    /** **/
    public int size() {
        return _size_const(this.object.pointer);
    }

    private native int _size_const(long _pointer_);

    /** **/
    public void unique() {
        _unique(this.object.pointer);
    }

    private native void _unique(long _pointer_);
}

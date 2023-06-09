package org.ogre4j;

import org.xbig.base.*;

public class FileNotFoundException extends org.xbig.base.NativeObject implements org.ogre4j.IFileNotFoundException {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public FileNotFoundException(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected FileNotFoundException(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public FileNotFoundException(org.xbig.base.WithoutNativeObject val) {
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
    public FileNotFoundException(int number, String description, String source, String file, long line) {
        super(new org.xbig.base.InstancePointer(__createFileNotFoundException__ivStringRStringRcPlv(number, description, source, file, line)), false);
    }

    private static native long __createFileNotFoundException__ivStringRStringRcPlv(int number, String description, String source, String file, long line);

    /** 
    Assignment operator. **/
    public void operatorAssignment(org.ogre4j.IException rhs) {
        _operatorAssignment__ExceptionR(this.object.pointer, rhs.getInstancePointer().pointer);
    }

    private native void _operatorAssignment__ExceptionR(long _pointer_, long rhs);

    /** 
    Returns a string with the full description of this error. **/
    public String getFullDescription() {
        return _getFullDescription_const(this.object.pointer);
    }

    private native String _getFullDescription_const(long _pointer_);

    /** 
    Gets the error code. **/
    public int getNumber() {
        return _getNumber_const(this.object.pointer);
    }

    private native int _getNumber_const(long _pointer_);

    /** 
    Gets the source function. **/
    public String getSource() {
        return _getSource_const(this.object.pointer);
    }

    private native String _getSource_const(long _pointer_);

    /** 
    Gets source file name. **/
    public String getFile() {
        return _getFile_const(this.object.pointer);
    }

    private native String _getFile_const(long _pointer_);

    /** 
    Gets line number. **/
    public long getLine() {
        return _getLine_const(this.object.pointer);
    }

    private native long _getLine_const(long _pointer_);

    /** 
    Returns a string with only the 'description' field of this exception. Use getFullDescriptionto get a full description of the error including line number, error number and what function threw the exception. **/
    public String getDescription() {
        return _getDescription_const(this.object.pointer);
    }

    private native String _getDescription_const(long _pointer_);

    /** **/
    public String what() {
        return _what_const(this.object.pointer);
    }

    private native String _what_const(long _pointer_);
}

package org.ogre4j;

import org.xbig.base.*;

public class ImageCodec extends org.xbig.base.NativeObject implements org.ogre4j.IImageCodec {

    static {
        System.loadLibrary("ogre4j");
    }

    public static class ImageData extends org.xbig.base.NativeObject implements org.ogre4j.IImageCodec.IImageData {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public ImageData(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected ImageData(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public ImageData(org.xbig.base.WithoutNativeObject val) {
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
        public ImageData() {
            super(new org.xbig.base.InstancePointer(__createImageData()), false);
        }

        private static native long __createImageData();

        /** **/
        public String dataType() {
            return _dataType_const(this.object.pointer);
        }

        private native String _dataType_const(long _pointer_);

        /** **/
        public int getheight() {
            return _getheight(this.object.pointer);
        }

        private native int _getheight(long _pointer_);

        /** **/
        public void setheight(int _jni_value_) {
            _setheight(this.object.pointer, _jni_value_);
        }

        private native void _setheight(long _pointer_, int _jni_value_);

        /** **/
        public int getwidth() {
            return _getwidth(this.object.pointer);
        }

        private native int _getwidth(long _pointer_);

        /** **/
        public void setwidth(int _jni_value_) {
            _setwidth(this.object.pointer, _jni_value_);
        }

        private native void _setwidth(long _pointer_, int _jni_value_);

        /** **/
        public int getdepth() {
            return _getdepth(this.object.pointer);
        }

        private native int _getdepth(long _pointer_);

        /** **/
        public void setdepth(int _jni_value_) {
            _setdepth(this.object.pointer, _jni_value_);
        }

        private native void _setdepth(long _pointer_, int _jni_value_);

        /** **/
        public int getsize() {
            return _getsize(this.object.pointer);
        }

        private native int _getsize(long _pointer_);

        /** **/
        public void setsize(int _jni_value_) {
            _setsize(this.object.pointer, _jni_value_);
        }

        private native void _setsize(long _pointer_, int _jni_value_);

        /** **/
        public int getnum_mipmaps() {
            return _getnum_mipmaps(this.object.pointer);
        }

        private native int _getnum_mipmaps(long _pointer_);

        /** **/
        public void setnum_mipmaps(int _jni_value_) {
            _setnum_mipmaps(this.object.pointer, _jni_value_);
        }

        private native void _setnum_mipmaps(long _pointer_, int _jni_value_);

        /** **/
        public long getflags() {
            return _getflags(this.object.pointer);
        }

        private native long _getflags(long _pointer_);

        /** **/
        public void setflags(long _jni_value_) {
            _setflags(this.object.pointer, _jni_value_);
        }

        private native void _setflags(long _pointer_, long _jni_value_);

        /** **/
        public org.ogre4j.PixelFormat getformat() {
            return org.ogre4j.PixelFormat.toEnum(_getformat(this.object.pointer));
        }

        private native int _getformat(long _pointer_);

        /** **/
        public void setformat(org.ogre4j.PixelFormat _jni_value_) {
            _setformat(this.object.pointer, _jni_value_.getValue());
        }

        private native void _setformat(long _pointer_, int _jni_value_);
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public ImageCodec(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected ImageCodec(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public ImageCodec(org.xbig.base.WithoutNativeObject val) {
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
    public String getDataType() {
        return _getDataType_const(this.object.pointer);
    }

    private native String _getDataType_const(long _pointer_);

    /** 
    Codes the data in the input stream and saves the result in the output stream. **/
    public void code(org.ogre4j.IDataStreamPtr returnValue, org.ogre4j.IMemoryDataStreamPtr input, org.ogre4j.ICodec.ICodecDataPtr pData) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_code__MemoryDataStreamPtrrCodecDataPtrr_const(this.object.pointer, input.getInstancePointer().pointer, pData.getInstancePointer().pointer)), false);
    }

    private native long _code__MemoryDataStreamPtrrCodecDataPtrr_const(long _pointer_, long input, long pData);

    /** 
    Codes the data in the input chunk and saves the result in the output filename provided. Provided for efficiency since coding to memory is progressive therefore memory required is unknown leading to reallocations. **/
    public void codeToFile(org.ogre4j.IMemoryDataStreamPtr input, String outFileName, org.ogre4j.ICodec.ICodecDataPtr pData) {
        _codeToFile__MemoryDataStreamPtrrStringRCodecDataPtrr_const(this.object.pointer, input.getInstancePointer().pointer, outFileName, pData.getInstancePointer().pointer);
    }

    private native void _codeToFile__MemoryDataStreamPtrrStringRCodecDataPtrr_const(long _pointer_, long input, String outFileName, long pData);

    /** 
    Codes the data from the input chunk into the output chunk. **/
    public void decode(org.ogre4j.ICodec.IDecodeResult returnValue, org.ogre4j.IDataStreamPtr input) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_decode__DataStreamPtrr_const(this.object.pointer, input.getInstancePointer().pointer)), false);
    }

    private native long _decode__DataStreamPtrr_const(long _pointer_, long input);

    /** 
    Returns the type of the codec as a String **/
    public String getType() {
        return _getType_const(this.object.pointer);
    }

    private native String _getType_const(long _pointer_);

    /** 
    Returns whether a magic number header matches this codec. **/
    public boolean magicNumberMatch(String magicNumberPtr, int maxbytes) {
        return _magicNumberMatch__cPiv_const(this.object.pointer, magicNumberPtr, maxbytes);
    }

    private native boolean _magicNumberMatch__cPiv_const(long _pointer_, String magicNumberPtr, int maxbytes);

    /** 
    Maps a magic number header to a file extension, if this codec recognises it. **/
    public String magicNumberToFileExt(String magicNumberPtr, int maxbytes) {
        return _magicNumberToFileExt__cPiv_const(this.object.pointer, magicNumberPtr, maxbytes);
    }

    private native String _magicNumberToFileExt__cPiv_const(long _pointer_, String magicNumberPtr, int maxbytes);

    /** 
    Registers a new codec in the database. **/
    public static void registerCodec(org.ogre4j.ICodec pCodec) {
        _registerCodec__Codecp(pCodec.getInstancePointer().pointer);
    }

    private static native void _registerCodec__Codecp(long pCodec);

    /** 
    Unregisters a codec from the database. **/
    public static void unRegisterCodec(org.ogre4j.ICodec pCodec) {
        _unRegisterCodec__Codecp(pCodec.getInstancePointer().pointer);
    }

    private static native void _unRegisterCodec__Codecp(long pCodec);

    /** 
    Gets the iterator for the registered codecs. **/
    public static void getCodecIterator(org.ogre4j.ICodec.ICodecIterator returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getCodecIterator()), false);
    }

    private static native long _getCodecIterator();

    /** 
    Gets the file extension list for the registered codecs. **/
    public static void getExtensions(org.ogre4j.IStringVector returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getExtensions()), false);
    }

    private static native long _getExtensions();

    /** 
    Gets the codec registered for the passed in file extension. **/
    public static org.ogre4j.ICodec getCodec(String extension) {
        return new org.ogre4j.Codec(new InstancePointer(_getCodec__StringR(extension)));
    }

    private static native long _getCodec__StringR(String extension);

    /** 
    Gets the codec that can handle the given 'magic' identifier. **/
    public static org.ogre4j.ICodec getCodec(BytePointer magicNumberPtr, int maxbytes) {
        return new org.ogre4j.Codec(new InstancePointer(_getCodec__cpiv(magicNumberPtr.object.pointer, maxbytes)));
    }

    private static native long _getCodec__cpiv(long magicNumberPtr, int maxbytes);
}

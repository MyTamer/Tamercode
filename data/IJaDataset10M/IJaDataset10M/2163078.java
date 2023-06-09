package org.ogre4j;

import org.xbig.base.*;

public class Image extends org.xbig.base.NativeObject implements org.ogre4j.IImage {

    static {
        System.loadLibrary("ogre4j");
    }

    public enum Filter implements INativeEnum<Filter> {

        FILTER_NEAREST(FilterHelper.ENUM_VALUES[0]), FILTER_LINEAR(FilterHelper.ENUM_VALUES[1]), FILTER_BILINEAR(FilterHelper.ENUM_VALUES[2]), FILTER_BOX(FilterHelper.ENUM_VALUES[3]), FILTER_TRIANGLE(FilterHelper.ENUM_VALUES[4]), FILTER_BICUBIC(FilterHelper.ENUM_VALUES[5]);

        private int value;

        Filter(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }

        public Filter getEnum(int val) {
            return toEnum(val);
        }

        public static final Filter toEnum(int retval) {
            if (retval == FILTER_NEAREST.value) return Filter.FILTER_NEAREST; else if (retval == FILTER_LINEAR.value) return Filter.FILTER_LINEAR; else if (retval == FILTER_BILINEAR.value) return Filter.FILTER_BILINEAR; else if (retval == FILTER_BOX.value) return Filter.FILTER_BOX; else if (retval == FILTER_TRIANGLE.value) return Filter.FILTER_TRIANGLE; else if (retval == FILTER_BICUBIC.value) return Filter.FILTER_BICUBIC;
            throw new RuntimeException("wrong number in jni call for an enum");
        }
    }

    static class FilterHelper {

        public static final int[] ENUM_VALUES = getEnumValues();

        private static native int[] getEnumValues();
    }

    ;

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public Image(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected Image(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public Image(org.xbig.base.WithoutNativeObject val) {
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
    Standard constructor. **/
    public Image() {
        super(new org.xbig.base.InstancePointer(__createImage()), false);
    }

    private static native long __createImage();

    /** 
    Copy-constructor - copies all the data from the target image. **/
    public Image(org.ogre4j.IImage img) {
        super(new org.xbig.base.InstancePointer(__createImage__ImageR(img.getInstancePointer().pointer)), false);
    }

    private static native long __createImage__ImageR(long img);

    /** 
    Assignment operator - copies all the data from the target image. **/
    public org.ogre4j.IImage operatorAssignment(org.ogre4j.IImage img) {
        return new org.ogre4j.Image(new InstancePointer(_operatorAssignment__ImageR(this.object.pointer, img.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment__ImageR(long _pointer_, long img);

    /** 
    Flips (mirrors) the image around the Y-axis. **/
    public org.ogre4j.IImage flipAroundY() {
        return new org.ogre4j.Image(new InstancePointer(_flipAroundY(this.object.pointer)));
    }

    private native long _flipAroundY(long _pointer_);

    /** 
    Flips (mirrors) the image around the X-axis. **/
    public org.ogre4j.IImage flipAroundX() {
        return new org.ogre4j.Image(new InstancePointer(_flipAroundX(this.object.pointer)));
    }

    private native long _flipAroundX(long _pointer_);

    /** 
    Stores a pointer to raw data in memory. The pixel format has to be specified. **/
    public org.ogre4j.IImage loadDynamicImage(ShortPointer pData, int uWidth, int uHeight, int depth, org.ogre4j.PixelFormat eFormat, boolean autoDelete, int numFaces, int numMipMaps) {
        return new org.ogre4j.Image(new InstancePointer(_loadDynamicImage__ucharpivivivPixelFormatvbviviv(this.object.pointer, pData.object.pointer, uWidth, uHeight, depth, eFormat.getValue(), autoDelete, numFaces, numMipMaps)));
    }

    private native long _loadDynamicImage__ucharpivivivPixelFormatvbviviv(long _pointer_, long pData, int uWidth, int uHeight, int depth, int eFormat, boolean autoDelete, int numFaces, int numMipMaps);

    /** 
    Stores a pointer to raw data in memory. The pixel format has to be specified. **/
    public org.ogre4j.IImage loadDynamicImage(ShortPointer pData, int uWidth, int uHeight, org.ogre4j.PixelFormat eFormat) {
        return new org.ogre4j.Image(new InstancePointer(_loadDynamicImage__ucharpivivPixelFormatv(this.object.pointer, pData.object.pointer, uWidth, uHeight, eFormat.getValue())));
    }

    private native long _loadDynamicImage__ucharpivivPixelFormatv(long _pointer_, long pData, int uWidth, int uHeight, int eFormat);

    /** 
    Loads raw data from a stream. See the function loadDynamicImage for a description of the parameters. **/
    public org.ogre4j.IImage loadRawData(org.ogre4j.IDataStreamPtr stream, int uWidth, int uHeight, int uDepth, org.ogre4j.PixelFormat eFormat, int numFaces, int numMipMaps) {
        return new org.ogre4j.Image(new InstancePointer(_loadRawData__DataStreamPtrrivivivPixelFormatviviv(this.object.pointer, stream.getInstancePointer().pointer, uWidth, uHeight, uDepth, eFormat.getValue(), numFaces, numMipMaps)));
    }

    private native long _loadRawData__DataStreamPtrrivivivPixelFormatviviv(long _pointer_, long stream, int uWidth, int uHeight, int uDepth, int eFormat, int numFaces, int numMipMaps);

    /** 
    Loads raw data from a stream. The pixel format has to be specified. **/
    public org.ogre4j.IImage loadRawData(org.ogre4j.IDataStreamPtr stream, int uWidth, int uHeight, org.ogre4j.PixelFormat eFormat) {
        return new org.ogre4j.Image(new InstancePointer(_loadRawData__DataStreamPtrrivivPixelFormatv(this.object.pointer, stream.getInstancePointer().pointer, uWidth, uHeight, eFormat.getValue())));
    }

    private native long _loadRawData__DataStreamPtrrivivPixelFormatv(long _pointer_, long stream, int uWidth, int uHeight, int eFormat);

    /** 
    Loads an image file. **/
    public org.ogre4j.IImage load(String strFileName, String groupName) {
        return new org.ogre4j.Image(new InstancePointer(_load__StringRStringR(this.object.pointer, strFileName, groupName)));
    }

    private native long _load__StringRStringR(long _pointer_, String strFileName, String groupName);

    /** 
    Loads an image file from a stream. **/
    public org.ogre4j.IImage load(org.ogre4j.IDataStreamPtr stream, String type) {
        return new org.ogre4j.Image(new InstancePointer(_load__DataStreamPtrrStringR(this.object.pointer, stream.getInstancePointer().pointer, type)));
    }

    private native long _load__DataStreamPtrrStringR(long _pointer_, long stream, String type);

    /** 
    Save the image as a file. **/
    public void save(String filename) {
        _save__StringR(this.object.pointer, filename);
    }

    private native void _save__StringR(long _pointer_, String filename);

    /** 
    Encode the image and return a stream to the data. **/
    public void encode(org.ogre4j.IDataStreamPtr returnValue, String formatextension) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_encode__StringR(this.object.pointer, formatextension)), false);
    }

    private native long _encode__StringR(long _pointer_, String formatextension);

    /** 
    Returns a pointer to the internal image buffer. **/
    public ShortPointer getData() {
        return new ShortPointer(new InstancePointer(_getData(this.object.pointer)));
    }

    private native long _getData(long _pointer_);

    /** **/
    public ShortPointer getData_const() {
        return new ShortPointer(new InstancePointer(_getData_const_const(this.object.pointer)));
    }

    private native long _getData_const_const(long _pointer_);

    /** 
    Returns the size of the data buffer. **/
    public int getSize() {
        return _getSize_const(this.object.pointer);
    }

    private native int _getSize_const(long _pointer_);

    /** 
    Returns the number of mipmaps contained in the image. **/
    public int getNumMipmaps() {
        return _getNumMipmaps_const(this.object.pointer);
    }

    private native int _getNumMipmaps_const(long _pointer_);

    /** 
    Returns true if the image has the appropriate flag set. **/
    public boolean hasFlag(org.ogre4j.ImageFlags imgFlag) {
        return _hasFlag__ImageFlagsV_const(this.object.pointer, imgFlag.getValue());
    }

    private native boolean _hasFlag__ImageFlagsV_const(long _pointer_, int imgFlag);

    /** 
    Gets the width of the image in pixels. **/
    public int getWidth() {
        return _getWidth_const(this.object.pointer);
    }

    private native int _getWidth_const(long _pointer_);

    /** 
    Gets the height of the image in pixels. **/
    public int getHeight() {
        return _getHeight_const(this.object.pointer);
    }

    private native int _getHeight_const(long _pointer_);

    /** 
    Gets the depth of the image. **/
    public int getDepth() {
        return _getDepth_const(this.object.pointer);
    }

    private native int _getDepth_const(long _pointer_);

    /** 
    Get the number of faces of the image. This is usually 6 for a cubemap, and 1 for a normal image. **/
    public int getNumFaces() {
        return _getNumFaces_const(this.object.pointer);
    }

    private native int _getNumFaces_const(long _pointer_);

    /** 
    Gets the physical width in bytes of each row of pixels. **/
    public int getRowSpan() {
        return _getRowSpan_const(this.object.pointer);
    }

    private native int _getRowSpan_const(long _pointer_);

    /** 
    Returns the image format. **/
    public org.ogre4j.PixelFormat getFormat() {
        return org.ogre4j.PixelFormat.toEnum(_getFormat_const(this.object.pointer));
    }

    private native int _getFormat_const(long _pointer_);

    /** 
    Returns the number of bits per pixel. **/
    public short getBPP() {
        return _getBPP_const(this.object.pointer);
    }

    private native short _getBPP_const(long _pointer_);

    /** 
    Returns true if the image has an alpha component. **/
    public boolean getHasAlpha() {
        return _getHasAlpha_const(this.object.pointer);
    }

    private native boolean _getHasAlpha_const(long _pointer_);

    /** 
    Get colour value from a certain location in the image. The z coordinate is only valid for cubemaps and volume textures. This uses the first (largest) mipmap. **/
    public void getColourAt(org.ogre4j.IColourValue returnValue, int x, int y, int z) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getColourAt__iviviv_const(this.object.pointer, x, y, z)), false);
    }

    private native long _getColourAt__iviviv_const(long _pointer_, int x, int y, int z);

    /** 
    Get a  encapsulating the image data of a mipmap **/
    public void getPixelBox(org.ogre4j.IPixelBox returnValue, int face, int mipmap) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getPixelBox__iviv_const(this.object.pointer, face, mipmap)), false);
    }

    private native long _getPixelBox__iviv_const(long _pointer_, int face, int mipmap);

    /** 
    Resize a 2D image, applying the appropriate filter. **/
    public void resize(int width, int height, org.ogre4j.Image.Filter filter) {
        _resize__ushortvushortvFilterv(this.object.pointer, width, height, filter.getValue());
    }

    private native void _resize__ushortvushortvFilterv(long _pointer_, int width, int height, int filter);

    /** 
    Does gamma adjustment. **/
    public static void applyGamma(ShortPointer buffer, float gamma, int size, short bpp) {
        _applyGamma__ucharpRealvivucharv(buffer.object.pointer, gamma, size, bpp);
    }

    private static native void _applyGamma__ucharpRealvivucharv(long buffer, float gamma, int size, short bpp);

    /** 
    Scale a 1D, 2D or 3D image volume. **/
    public static void scale(org.ogre4j.IPixelBox src, org.ogre4j.IPixelBox dst, org.ogre4j.Image.Filter filter) {
        _scale__PixelBoxRPixelBoxRFilterv(src.getInstancePointer().pointer, dst.getInstancePointer().pointer, filter.getValue());
    }

    private static native void _scale__PixelBoxRPixelBoxRFilterv(long src, long dst, int filter);

    /** **/
    public static int calculateSize(int mipmaps, int faces, int width, int height, int depth, org.ogre4j.PixelFormat format) {
        return _calculateSize__ivivivivivPixelFormatv(mipmaps, faces, width, height, depth, format.getValue());
    }

    private static native int _calculateSize__ivivivivivPixelFormatv(int mipmaps, int faces, int width, int height, int depth, int format);

    /** **/
    public static String getFileExtFromMagic(org.ogre4j.IDataStreamPtr stream) {
        return _getFileExtFromMagic__DataStreamPtrv(stream.getInstancePointer().pointer);
    }

    private static native String _getFileExtFromMagic__DataStreamPtrv(long stream);
}

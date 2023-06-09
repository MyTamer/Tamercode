package org.ogre4j;

import org.xbig.base.*;

public class MeshManager extends org.xbig.base.NativeObject implements org.ogre4j.IMeshManager {

    static {
        System.loadLibrary("ogre4j");
    }

    protected static class MeshBuildParams extends org.xbig.base.NativeObject implements org.ogre4j.IMeshManager.IMeshBuildParams {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public MeshBuildParams(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected MeshBuildParams(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public MeshBuildParams(org.xbig.base.WithoutNativeObject val) {
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
        public MeshBuildParams() {
            super(new org.xbig.base.InstancePointer(__createMeshBuildParams()), false);
        }

        private static native long __createMeshBuildParams();

        /** **/
        public org.ogre4j.MeshManager.MeshBuildType gettype() {
            return org.ogre4j.MeshManager.MeshBuildType.toEnum(_gettype(this.object.pointer));
        }

        private native int _gettype(long _pointer_);

        /** **/
        public void settype(org.ogre4j.MeshManager.MeshBuildType _jni_value_) {
            _settype(this.object.pointer, _jni_value_.getValue());
        }

        private native void _settype(long _pointer_, int _jni_value_);

        /** **/
        public void getplane(org.ogre4j.IPlane returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getplane(this.object.pointer)), false);
        }

        private native long _getplane(long _pointer_);

        /** **/
        public void setplane(org.ogre4j.IPlane _jni_value_) {
            _setplane(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setplane(long _pointer_, long _jni_value_);

        /** **/
        public float getwidth() {
            return _getwidth(this.object.pointer);
        }

        private native float _getwidth(long _pointer_);

        /** **/
        public void setwidth(float _jni_value_) {
            _setwidth(this.object.pointer, _jni_value_);
        }

        private native void _setwidth(long _pointer_, float _jni_value_);

        /** **/
        public float getheight() {
            return _getheight(this.object.pointer);
        }

        private native float _getheight(long _pointer_);

        /** **/
        public void setheight(float _jni_value_) {
            _setheight(this.object.pointer, _jni_value_);
        }

        private native void _setheight(long _pointer_, float _jni_value_);

        /** **/
        public float getcurvature() {
            return _getcurvature(this.object.pointer);
        }

        private native float _getcurvature(long _pointer_);

        /** **/
        public void setcurvature(float _jni_value_) {
            _setcurvature(this.object.pointer, _jni_value_);
        }

        private native void _setcurvature(long _pointer_, float _jni_value_);

        /** **/
        public int getxsegments() {
            return _getxsegments(this.object.pointer);
        }

        private native int _getxsegments(long _pointer_);

        /** **/
        public void setxsegments(int _jni_value_) {
            _setxsegments(this.object.pointer, _jni_value_);
        }

        private native void _setxsegments(long _pointer_, int _jni_value_);

        /** **/
        public int getysegments() {
            return _getysegments(this.object.pointer);
        }

        private native int _getysegments(long _pointer_);

        /** **/
        public void setysegments(int _jni_value_) {
            _setysegments(this.object.pointer, _jni_value_);
        }

        private native void _setysegments(long _pointer_, int _jni_value_);

        /** **/
        public boolean getnormals() {
            return _getnormals(this.object.pointer);
        }

        private native boolean _getnormals(long _pointer_);

        /** **/
        public void setnormals(boolean _jni_value_) {
            _setnormals(this.object.pointer, _jni_value_);
        }

        private native void _setnormals(long _pointer_, boolean _jni_value_);

        /** **/
        public int getnumTexCoordSets() {
            return _getnumTexCoordSets(this.object.pointer);
        }

        private native int _getnumTexCoordSets(long _pointer_);

        /** **/
        public void setnumTexCoordSets(int _jni_value_) {
            _setnumTexCoordSets(this.object.pointer, _jni_value_);
        }

        private native void _setnumTexCoordSets(long _pointer_, int _jni_value_);

        /** **/
        public float getxTile() {
            return _getxTile(this.object.pointer);
        }

        private native float _getxTile(long _pointer_);

        /** **/
        public void setxTile(float _jni_value_) {
            _setxTile(this.object.pointer, _jni_value_);
        }

        private native void _setxTile(long _pointer_, float _jni_value_);

        /** **/
        public float getyTile() {
            return _getyTile(this.object.pointer);
        }

        private native float _getyTile(long _pointer_);

        /** **/
        public void setyTile(float _jni_value_) {
            _setyTile(this.object.pointer, _jni_value_);
        }

        private native void _setyTile(long _pointer_, float _jni_value_);

        /** **/
        public void getupVector(org.ogre4j.IVector3 returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getupVector(this.object.pointer)), false);
        }

        private native long _getupVector(long _pointer_);

        /** **/
        public void setupVector(org.ogre4j.IVector3 _jni_value_) {
            _setupVector(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setupVector(long _pointer_, long _jni_value_);

        /** **/
        public void getorientation(org.ogre4j.IQuaternion returnValue) {
            returnValue.delete();
            returnValue.setInstancePointer(new InstancePointer(_getorientation(this.object.pointer)), false);
        }

        private native long _getorientation(long _pointer_);

        /** **/
        public void setorientation(org.ogre4j.IQuaternion _jni_value_) {
            _setorientation(this.object.pointer, _jni_value_.getInstancePointer().pointer);
        }

        private native void _setorientation(long _pointer_, long _jni_value_);

        /** **/
        public org.ogre4j.HardwareBuffer.Usage getvertexBufferUsage() {
            return org.ogre4j.HardwareBuffer.Usage.toEnum(_getvertexBufferUsage(this.object.pointer));
        }

        private native int _getvertexBufferUsage(long _pointer_);

        /** **/
        public void setvertexBufferUsage(org.ogre4j.HardwareBuffer.Usage _jni_value_) {
            _setvertexBufferUsage(this.object.pointer, _jni_value_.getValue());
        }

        private native void _setvertexBufferUsage(long _pointer_, int _jni_value_);

        /** **/
        public org.ogre4j.HardwareBuffer.Usage getindexBufferUsage() {
            return org.ogre4j.HardwareBuffer.Usage.toEnum(_getindexBufferUsage(this.object.pointer));
        }

        private native int _getindexBufferUsage(long _pointer_);

        /** **/
        public void setindexBufferUsage(org.ogre4j.HardwareBuffer.Usage _jni_value_) {
            _setindexBufferUsage(this.object.pointer, _jni_value_.getValue());
        }

        private native void _setindexBufferUsage(long _pointer_, int _jni_value_);

        /** **/
        public boolean getvertexShadowBuffer() {
            return _getvertexShadowBuffer(this.object.pointer);
        }

        private native boolean _getvertexShadowBuffer(long _pointer_);

        /** **/
        public void setvertexShadowBuffer(boolean _jni_value_) {
            _setvertexShadowBuffer(this.object.pointer, _jni_value_);
        }

        private native void _setvertexShadowBuffer(long _pointer_, boolean _jni_value_);

        /** **/
        public boolean getindexShadowBuffer() {
            return _getindexShadowBuffer(this.object.pointer);
        }

        private native boolean _getindexShadowBuffer(long _pointer_);

        /** **/
        public void setindexShadowBuffer(boolean _jni_value_) {
            _setindexShadowBuffer(this.object.pointer, _jni_value_);
        }

        private native void _setindexShadowBuffer(long _pointer_, boolean _jni_value_);

        /** **/
        public int getySegmentsToKeep() {
            return _getySegmentsToKeep(this.object.pointer);
        }

        private native int _getySegmentsToKeep(long _pointer_);

        /** **/
        public void setySegmentsToKeep(int _jni_value_) {
            _setySegmentsToKeep(this.object.pointer, _jni_value_);
        }

        private native void _setySegmentsToKeep(long _pointer_, int _jni_value_);
    }

    protected enum MeshBuildType implements INativeEnum<MeshBuildType> {

        MBT_PLANE(MeshBuildTypeHelper.ENUM_VALUES[0]), MBT_CURVED_ILLUSION_PLANE(MeshBuildTypeHelper.ENUM_VALUES[1]), MBT_CURVED_PLANE(MeshBuildTypeHelper.ENUM_VALUES[2]);

        private int value;

        MeshBuildType(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }

        public MeshBuildType getEnum(int val) {
            return toEnum(val);
        }

        public static final MeshBuildType toEnum(int retval) {
            if (retval == MBT_PLANE.value) return MeshBuildType.MBT_PLANE; else if (retval == MBT_CURVED_ILLUSION_PLANE.value) return MeshBuildType.MBT_CURVED_ILLUSION_PLANE; else if (retval == MBT_CURVED_PLANE.value) return MeshBuildType.MBT_CURVED_PLANE;
            throw new RuntimeException("wrong number in jni call for an enum");
        }
    }

    static class MeshBuildTypeHelper {

        public static final int[] ENUM_VALUES = getEnumValues();

        private static native int[] getEnumValues();
    }

    ;

    protected static class MeshBuildParamsMap extends org.xbig.base.NativeObject implements org.ogre4j.IMeshManager.IMeshBuildParamsMap {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public MeshBuildParamsMap(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected MeshBuildParamsMap(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public MeshBuildParamsMap(org.xbig.base.WithoutNativeObject val) {
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
        public MeshBuildParamsMap() {
            super(new org.xbig.base.InstancePointer(__createMeshBuildParamsMap()), false);
        }

        private static native long __createMeshBuildParamsMap();

        /** **/
        public void clear() {
            _clear(this.object.pointer);
        }

        private native void _clear(long _pointer_);

        /** **/
        public int count(org.ogre4j.IResource key) {
            return _count__Ogre_ResourceP(this.object.pointer, key.getInstancePointer().pointer);
        }

        private native int _count__Ogre_ResourceP(long _pointer_, long key);

        /** **/
        public boolean empty() {
            return _empty_const(this.object.pointer);
        }

        private native boolean _empty_const(long _pointer_);

        /** **/
        public int erase(org.ogre4j.IResource key) {
            return _erase__Ogre_ResourceP(this.object.pointer, key.getInstancePointer().pointer);
        }

        private native int _erase__Ogre_ResourceP(long _pointer_, long key);

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
        public org.ogre4j.IMeshManager.IMeshBuildParams get(org.ogre4j.IResource key) {
            return new org.ogre4j.MeshManager.MeshBuildParams(new InstancePointer(_get__Ogre_ResourceP(this.object.pointer, key.getInstancePointer().pointer)));
        }

        private native long _get__Ogre_ResourceP(long _pointer_, long key);

        /** **/
        public void insert(org.ogre4j.IResource key, org.ogre4j.IMeshManager.IMeshBuildParams value) {
            _insert__Ogre_ResourcePOgre_MeshManager_MeshBuildParamsr(this.object.pointer, key.getInstancePointer().pointer, value.getInstancePointer().pointer);
        }

        private native void _insert__Ogre_ResourcePOgre_MeshManager_MeshBuildParamsr(long _pointer_, long key, long value);
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public MeshManager(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected MeshManager(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public MeshManager(org.xbig.base.WithoutNativeObject val) {
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
    public MeshManager() {
        super(new org.xbig.base.InstancePointer(__createMeshManager()), false);
    }

    private static native long __createMeshManager();

    /** 
    Initialises the manager, only to be called by OGRE internally. **/
    public void _initialise() {
        __initialise(this.object.pointer);
    }

    private native void __initialise(long _pointer_);

    /** 
    Create a new mesh, or retrieve an existing one with the same name if it already exists. **/
    public void createOrRetrieve(org.ogre4j.IResourceManager.IResourceCreateOrRetrieveResult returnValue, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList params, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createOrRetrieve__StringRStringRbvManualResourceLoaderpNameValuePairListPHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, name, group, isManual, loader.getInstancePointer().pointer, params.getInstancePointer().pointer, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexBufferShadowed, indexBufferShadowed)), false);
    }

    private native long _createOrRetrieve__StringRStringRbvManualResourceLoaderpNameValuePairListPHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String name, String group, boolean isManual, long loader, long params, int vertexBufferUsage, int indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed);

    /** 
    Prepares a mesh for loading from a file. This does the IO in advance of the call to . **/
    public void prepare(org.ogre4j.IMeshPtr returnValue, String filename, String groupName, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_prepare__StringRStringRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, filename, groupName, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexBufferShadowed, indexBufferShadowed)), false);
    }

    private native long _prepare__StringRStringRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String filename, String groupName, int vertexBufferUsage, int indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed);

    /** 
    Loads a mesh from a file, making it immediately available for use. **/
    public void load(org.ogre4j.IMeshPtr returnValue, String filename, String groupName, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_load__StringRStringRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, filename, groupName, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexBufferShadowed, indexBufferShadowed)), false);
    }

    private native long _load__StringRStringRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String filename, String groupName, int vertexBufferUsage, int indexBufferUsage, boolean vertexBufferShadowed, boolean indexBufferShadowed);

    /** 
    Creates a new  specifically for manual definition rather than loading from an object file. **/
    public void createManual(org.ogre4j.IMeshPtr returnValue, String name, String groupName, org.ogre4j.IManualResourceLoader loader) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createManual__StringRStringRManualResourceLoaderp(this.object.pointer, name, groupName, loader.getInstancePointer().pointer)), false);
    }

    private native long _createManual__StringRStringRManualResourceLoaderp(long _pointer_, String name, String groupName, long loader);

    /** 
    Creates a basic plane, by default majoring on the x/y axes facing positive Z. **/
    public void createPlane(org.ogre4j.IMeshPtr returnValue, String name, String groupName, org.ogre4j.IPlane plane, float width, float height, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float uTile, float vTile, org.ogre4j.IVector3 upVector, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createPlane__StringRStringRPlaneRRealvRealvivivbvivRealvRealvVector3RHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, name, groupName, plane.getInstancePointer().pointer, width, height, xsegments, ysegments, normals, numTexCoordSets, uTile, vTile, upVector.getInstancePointer().pointer, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexShadowBuffer, indexShadowBuffer)), false);
    }

    private native long _createPlane__StringRStringRPlaneRRealvRealvivivbvivRealvRealvVector3RHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String name, String groupName, long plane, float width, float height, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float uTile, float vTile, long upVector, int vertexBufferUsage, int indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer);

    /** 
    Creates a plane, which because of it's texture coordinates looks like a curved surface, useful for skies in a skybox. **/
    public void createCurvedIllusionPlane(org.ogre4j.IMeshPtr returnValue, String name, String groupName, org.ogre4j.IPlane plane, float width, float height, float curvature, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float uTile, float vTile, org.ogre4j.IVector3 upVector, org.ogre4j.IQuaternion orientation, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer, int ySegmentsToKeep) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createCurvedIllusionPlane__StringRStringRPlaneRRealvRealvRealvivivbvivRealvRealvVector3RQuaternionRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbviv(this.object.pointer, name, groupName, plane.getInstancePointer().pointer, width, height, curvature, xsegments, ysegments, normals, numTexCoordSets, uTile, vTile, upVector.getInstancePointer().pointer, orientation.getInstancePointer().pointer, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexShadowBuffer, indexShadowBuffer, ySegmentsToKeep)), false);
    }

    private native long _createCurvedIllusionPlane__StringRStringRPlaneRRealvRealvRealvivivbvivRealvRealvVector3RQuaternionRHardwareBuffer_UsagevHardwareBuffer_Usagevbvbviv(long _pointer_, String name, String groupName, long plane, float width, float height, float curvature, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float uTile, float vTile, long upVector, long orientation, int vertexBufferUsage, int indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer, int ySegmentsToKeep);

    /** 
    Creates a genuinely curved plane, by default majoring on the x/y axes facing positive Z. **/
    public void createCurvedPlane(org.ogre4j.IMeshPtr returnValue, String name, String groupName, org.ogre4j.IPlane plane, float width, float height, float bow, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float xTile, float yTile, org.ogre4j.IVector3 upVector, org.ogre4j.HardwareBuffer.Usage vertexBufferUsage, org.ogre4j.HardwareBuffer.Usage indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createCurvedPlane__StringRStringRPlaneRRealvRealvRealvivivbvivRealvRealvVector3RHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, name, groupName, plane.getInstancePointer().pointer, width, height, bow, xsegments, ysegments, normals, numTexCoordSets, xTile, yTile, upVector.getInstancePointer().pointer, vertexBufferUsage.getValue(), indexBufferUsage.getValue(), vertexShadowBuffer, indexShadowBuffer)), false);
    }

    private native long _createCurvedPlane__StringRStringRPlaneRRealvRealvRealvivivbvivRealvRealvVector3RHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String name, String groupName, long plane, float width, float height, float bow, int xsegments, int ysegments, boolean normals, int numTexCoordSets, float xTile, float yTile, long upVector, int vertexBufferUsage, int indexBufferUsage, boolean vertexShadowBuffer, boolean indexShadowBuffer);

    /** 
    Creates a Bezier patch based on an array of control vertices. **/
    public void createBezierPatch(org.ogre4j.IPatchMeshPtr returnValue, String name, String groupName, VoidPointer controlPointBuffer, org.ogre4j.IVertexDeclaration declaration, int width, int height, int uMaxSubdivisionLevel, int vMaxSubdivisionLevel, org.ogre4j.PatchSurface.VisibleSide visibleSide, org.ogre4j.HardwareBuffer.Usage vbUsage, org.ogre4j.HardwareBuffer.Usage ibUsage, boolean vbUseShadow, boolean ibUseShadow) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createBezierPatch__StringRStringRvpVertexDeclarationpivivivivPatchSurface_VisibleSidevHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(this.object.pointer, name, groupName, controlPointBuffer.object.pointer, declaration.getInstancePointer().pointer, width, height, uMaxSubdivisionLevel, vMaxSubdivisionLevel, visibleSide.getValue(), vbUsage.getValue(), ibUsage.getValue(), vbUseShadow, ibUseShadow)), false);
    }

    private native long _createBezierPatch__StringRStringRvpVertexDeclarationpivivivivPatchSurface_VisibleSidevHardwareBuffer_UsagevHardwareBuffer_Usagevbvbv(long _pointer_, String name, String groupName, long controlPointBuffer, long declaration, int width, int height, int uMaxSubdivisionLevel, int vMaxSubdivisionLevel, int visibleSide, int vbUsage, int ibUsage, boolean vbUseShadow, boolean ibUseShadow);

    /** 
    Tells the mesh manager that all future meshes should prepare themselves for shadow volumes on loading. **/
    public void setPrepareAllMeshesForShadowVolumes(boolean enable) {
        _setPrepareAllMeshesForShadowVolumes__bv(this.object.pointer, enable);
    }

    private native void _setPrepareAllMeshesForShadowVolumes__bv(long _pointer_, boolean enable);

    /** 
    Retrieves whether all Meshes should prepare themselves for shadow volumes. **/
    public boolean getPrepareAllMeshesForShadowVolumes() {
        return _getPrepareAllMeshesForShadowVolumes(this.object.pointer);
    }

    private native boolean _getPrepareAllMeshesForShadowVolumes(long _pointer_);

    /** 
    Gets the factor by which the bounding box of an entity is padded. Default is 0.01 **/
    public float getBoundsPaddingFactor() {
        return _getBoundsPaddingFactor(this.object.pointer);
    }

    private native float _getBoundsPaddingFactor(long _pointer_);

    /** 
    Sets the factor by which the bounding box of an entity is padded **/
    public void setBoundsPaddingFactor(float paddingFactor) {
        _setBoundsPaddingFactor__Realv(this.object.pointer, paddingFactor);
    }

    private native void _setBoundsPaddingFactor__Realv(long _pointer_, float paddingFactor);

    /** 
    Sets the listener used to control mesh loading through the serializer. **/
    public void setListener(org.ogre4j.IMeshSerializerListener listener) {
        _setListener__MeshSerializerListenerp(this.object.pointer, listener.getInstancePointer().pointer);
    }

    private native void _setListener__MeshSerializerListenerp(long _pointer_, long listener);

    /** 
    Gets the listener used to control mesh loading through the serializer. **/
    public org.ogre4j.IMeshSerializerListener getListener() {
        return new org.ogre4j.MeshSerializerListener(new InstancePointer(_getListener(this.object.pointer)));
    }

    private native long _getListener(long _pointer_);

    /** 
    **/
    public void loadResource(org.ogre4j.IResource res) {
        _loadResource__Resourcep(this.object.pointer, res.getInstancePointer().pointer);
    }

    private native void _loadResource__Resourcep(long _pointer_, long res);

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IMeshManager getSingleton() {
        return new org.ogre4j.MeshManager(new InstancePointer(_getSingleton()));
    }

    private static native long _getSingleton();

    /** 
    Override standard  retrieval. **/
    public static org.ogre4j.IMeshManager getSingletonPtr() {
        return new org.ogre4j.MeshManager(new InstancePointer(_getSingletonPtr()));
    }

    private static native long _getSingletonPtr();

    /** 
    Creates a new blank resource, but does not immediately load it. **/
    public void create(org.ogre4j.IResourcePtr returnValue, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList createParams) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_create__StringRStringRbvManualResourceLoaderpNameValuePairListP(this.object.pointer, name, group, isManual, loader.getInstancePointer().pointer, createParams.getInstancePointer().pointer)), false);
    }

    private native long _create__StringRStringRbvManualResourceLoaderpNameValuePairListP(long _pointer_, String name, String group, boolean isManual, long loader, long createParams);

    /** 
    Create a new resource, or retrieve an existing one with the same name if it already exists. **/
    public void createOrRetrieve(org.ogre4j.IResourceManager.IResourceCreateOrRetrieveResult returnValue, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList createParams) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_createOrRetrieve__StringRStringRbvManualResourceLoaderpNameValuePairListP(this.object.pointer, name, group, isManual, loader.getInstancePointer().pointer, createParams.getInstancePointer().pointer)), false);
    }

    private native long _createOrRetrieve__StringRStringRbvManualResourceLoaderpNameValuePairListP(long _pointer_, String name, String group, boolean isManual, long loader, long createParams);

    /** 
    Set a limit on the amount of memory this resource handler may use. **/
    public void setMemoryBudget(int bytes) {
        _setMemoryBudget__iv(this.object.pointer, bytes);
    }

    private native void _setMemoryBudget__iv(long _pointer_, int bytes);

    /** 
    Get the limit on the amount of memory this resource handler may use. **/
    public int getMemoryBudget() {
        return _getMemoryBudget_const(this.object.pointer);
    }

    private native int _getMemoryBudget_const(long _pointer_);

    /** 
    Gets the current memory usage, in bytes. **/
    public int getMemoryUsage() {
        return _getMemoryUsage_const(this.object.pointer);
    }

    private native int _getMemoryUsage_const(long _pointer_);

    /** 
    Unloads a single resource by name. **/
    public void unload(String name) {
        _unload__StringR(this.object.pointer, name);
    }

    private native void _unload__StringR(long _pointer_, String name);

    /** 
    Unloads a single resource by handle. **/
    public void unload(long handle) {
        _unload__ResourceHandlev(this.object.pointer, handle);
    }

    private native void _unload__ResourceHandlev(long _pointer_, long handle);

    /** 
    Unloads all resources. **/
    public void unloadAll(boolean reloadableOnly) {
        _unloadAll__bv(this.object.pointer, reloadableOnly);
    }

    private native void _unloadAll__bv(long _pointer_, boolean reloadableOnly);

    /** 
    Caused all currently loaded resources to be reloaded. **/
    public void reloadAll(boolean reloadableOnly) {
        _reloadAll__bv(this.object.pointer, reloadableOnly);
    }

    private native void _reloadAll__bv(long _pointer_, boolean reloadableOnly);

    /** 
    Unload all resources which are not referenced by any other object. **/
    public void unloadUnreferencedResources(boolean reloadableOnly) {
        _unloadUnreferencedResources__bv(this.object.pointer, reloadableOnly);
    }

    private native void _unloadUnreferencedResources__bv(long _pointer_, boolean reloadableOnly);

    /** 
    Caused all currently loaded but not referenced by any other object resources to be reloaded. **/
    public void reloadUnreferencedResources(boolean reloadableOnly) {
        _reloadUnreferencedResources__bv(this.object.pointer, reloadableOnly);
    }

    private native void _reloadUnreferencedResources__bv(long _pointer_, boolean reloadableOnly);

    /** 
    Remove a single resource. **/
    public void remove(org.ogre4j.IResourcePtr r) {
        _remove__ResourcePtrr(this.object.pointer, r.getInstancePointer().pointer);
    }

    private native void _remove__ResourcePtrr(long _pointer_, long r);

    /** 
    Remove a single resource by name. **/
    public void remove(String name) {
        _remove__StringR(this.object.pointer, name);
    }

    private native void _remove__StringR(long _pointer_, String name);

    /** 
    Remove a single resource by handle. **/
    public void remove(long handle) {
        _remove__ResourceHandlev(this.object.pointer, handle);
    }

    private native void _remove__ResourceHandlev(long _pointer_, long handle);

    /** 
    Removes all resources. **/
    public void removeAll() {
        _removeAll(this.object.pointer);
    }

    private native void _removeAll(long _pointer_);

    /** 
    Retrieves a pointer to a resource by name, or null if the resource does not exist. **/
    public void getByName(org.ogre4j.IResourcePtr returnValue, String name) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getByName__StringR(this.object.pointer, name)), false);
    }

    private native long _getByName__StringR(long _pointer_, String name);

    /** 
    Retrieves a pointer to a resource by handle, or null if the resource does not exist. **/
    public void getByHandle(org.ogre4j.IResourcePtr returnValue, long handle) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getByHandle__ResourceHandlev(this.object.pointer, handle)), false);
    }

    private native long _getByHandle__ResourceHandlev(long _pointer_, long handle);

    /** **/
    public boolean resourceExists(String name) {
        return _resourceExists__StringR(this.object.pointer, name);
    }

    private native boolean _resourceExists__StringR(long _pointer_, String name);

    /** **/
    public boolean resourceExists(long handle) {
        return _resourceExists__ResourceHandlev(this.object.pointer, handle);
    }

    private native boolean _resourceExists__ResourceHandlev(long _pointer_, long handle);

    /** 
    Notify this manager that a resource which it manages has been 'touched', i.e. used. **/
    public void _notifyResourceTouched(org.ogre4j.IResource res) {
        __notifyResourceTouched__Resourcep(this.object.pointer, res.getInstancePointer().pointer);
    }

    private native void __notifyResourceTouched__Resourcep(long _pointer_, long res);

    /** 
    Notify this manager that a resource which it manages has been loaded. **/
    public void _notifyResourceLoaded(org.ogre4j.IResource res) {
        __notifyResourceLoaded__Resourcep(this.object.pointer, res.getInstancePointer().pointer);
    }

    private native void __notifyResourceLoaded__Resourcep(long _pointer_, long res);

    /** 
    Notify this manager that a resource which it manages has been unloaded. **/
    public void _notifyResourceUnloaded(org.ogre4j.IResource res) {
        __notifyResourceUnloaded__Resourcep(this.object.pointer, res.getInstancePointer().pointer);
    }

    private native void __notifyResourceUnloaded__Resourcep(long _pointer_, long res);

    /** 
    Generic prepare method, used to create a  specific to this  without using one of the specialised 'prepare' methods (containing per-Resource-type parameters). **/
    public void prepare(org.ogre4j.IResourcePtr returnValue, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList loadParams) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_prepare__StringRStringRbvManualResourceLoaderpNameValuePairListP(this.object.pointer, name, group, isManual, loader.getInstancePointer().pointer, loadParams.getInstancePointer().pointer)), false);
    }

    private native long _prepare__StringRStringRbvManualResourceLoaderpNameValuePairListP(long _pointer_, String name, String group, boolean isManual, long loader, long loadParams);

    /** 
    Generic load method, used to create a  specific to this  without using one of the specialised 'load' methods (containing per-Resource-type parameters). **/
    public void load(org.ogre4j.IResourcePtr returnValue, String name, String group, boolean isManual, org.ogre4j.IManualResourceLoader loader, org.ogre4j.INameValuePairList loadParams) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_load__StringRStringRbvManualResourceLoaderpNameValuePairListP(this.object.pointer, name, group, isManual, loader.getInstancePointer().pointer, loadParams.getInstancePointer().pointer)), false);
    }

    private native long _load__StringRStringRbvManualResourceLoaderpNameValuePairListP(long _pointer_, String name, String group, boolean isManual, long loader, long loadParams);

    /** 
    Gets the file patterns which should be used to find scripts for this . **/
    public org.ogre4j.IStringVector getScriptPatterns() {
        return new org.ogre4j.StringVector(new InstancePointer(_getScriptPatterns_const(this.object.pointer)));
    }

    private native long _getScriptPatterns_const(long _pointer_);

    /** 
    Parse the definition of a set of resources from a script file. **/
    public void parseScript(org.ogre4j.IDataStreamPtr stream, String groupName) {
        _parseScript__DataStreamPtrrStringR(this.object.pointer, stream.getInstancePointer().pointer, groupName);
    }

    private native void _parseScript__DataStreamPtrrStringR(long _pointer_, long stream, String groupName);

    /** 
    Gets the relative loading order of resources of this type. **/
    public float getLoadingOrder() {
        return _getLoadingOrder_const(this.object.pointer);
    }

    private native float _getLoadingOrder_const(long _pointer_);

    /** 
    Gets a string identifying the type of resource this manager handles. **/
    public String getResourceType() {
        return _getResourceType_const(this.object.pointer);
    }

    private native String _getResourceType_const(long _pointer_);

    /** 
    Sets whether this manager and its resources habitually produce log output **/
    public void setVerbose(boolean v) {
        _setVerbose__bv(this.object.pointer, v);
    }

    private native void _setVerbose__bv(long _pointer_, boolean v);

    /** 
    Gets whether this manager and its resources habitually produce log output **/
    public boolean getVerbose() {
        return _getVerbose(this.object.pointer);
    }

    private native boolean _getVerbose(long _pointer_);

    /** 
    Returns an iterator over all resources in this manager. **/
    public void getResourceIterator(org.ogre4j.IResourceManager.IResourceMapIterator returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getResourceIterator(this.object.pointer)), false);
    }

    private native long _getResourceIterator(long _pointer_);

    /** 
    Called when a resource wishes to load. Note that this could get called in a background thread even in just a semithreaded ogre (OGRE_THREAD_SUPPORT==2). Thus, you must not access the rendersystem from this callback. Do that stuff in loadResource. **/
    public void prepareResource(org.ogre4j.IResource resource) {
        _prepareResource__Resourcep(this.object.pointer, resource.getInstancePointer().pointer);
    }

    private native void _prepareResource__Resourcep(long _pointer_, long resource);
}

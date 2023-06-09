package org.ogre4j;

import org.xbig.base.*;

public class RenderToVertexBuffer extends org.xbig.base.NativeObject implements org.ogre4j.IRenderToVertexBuffer {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public RenderToVertexBuffer(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected RenderToVertexBuffer(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public RenderToVertexBuffer(org.xbig.base.WithoutNativeObject val) {
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
    Get the vertex declaration that the pass will output. **/
    public org.ogre4j.IVertexDeclaration getVertexDeclaration() {
        return new org.ogre4j.VertexDeclaration(new InstancePointer(_getVertexDeclaration(this.object.pointer)));
    }

    private native long _getVertexDeclaration(long _pointer_);

    /** 
    Get the maximum number of vertices that the buffer will hold **/
    public long getMaxVertexCount() {
        return _getMaxVertexCount_const(this.object.pointer);
    }

    private native long _getMaxVertexCount_const(long _pointer_);

    /** 
    Set the maximum number of vertices that the buffer will hold **/
    public void setMaxVertexCount(long maxVertexCount) {
        _setMaxVertexCount__Iv(this.object.pointer, maxVertexCount);
    }

    private native void _setMaxVertexCount__Iv(long _pointer_, long maxVertexCount);

    /** 
    What type of primitives does this object generate? **/
    public org.ogre4j.RenderOperation.OperationType getOperationType() {
        return org.ogre4j.RenderOperation.OperationType.toEnum(_getOperationType_const(this.object.pointer));
    }

    private native int _getOperationType_const(long _pointer_);

    /** 
    Set the type of primitives that this object generates **/
    public void setOperationType(org.ogre4j.RenderOperation.OperationType operationType) {
        _setOperationType__RenderOperation_OperationTypev(this.object.pointer, operationType.getValue());
    }

    private native void _setOperationType__RenderOperation_OperationTypev(long _pointer_, int operationType);

    /** 
    Set wether this object resets its buffers each time it updates. **/
    public void setResetsEveryUpdate(boolean resetsEveryUpdate) {
        _setResetsEveryUpdate__bv(this.object.pointer, resetsEveryUpdate);
    }

    private native void _setResetsEveryUpdate__bv(long _pointer_, boolean resetsEveryUpdate);

    /** 
    Does this object reset its buffer each time it updates? **/
    public boolean getResetsEveryUpdate() {
        return _getResetsEveryUpdate_const(this.object.pointer);
    }

    private native boolean _getResetsEveryUpdate_const(long _pointer_);

    /** 
    Get the render operation for this buffer **/
    public void getRenderOperation(org.ogre4j.IRenderOperation op) {
        _getRenderOperation__RenderOperationr(this.object.pointer, op.getInstancePointer().pointer);
    }

    private native void _getRenderOperation__RenderOperationr(long _pointer_, long op);

    /** 
    Update the contents of this vertex buffer by rendering **/
    public void update(org.ogre4j.ISceneManager sceneMgr) {
        _update__SceneManagerp(this.object.pointer, sceneMgr.getInstancePointer().pointer);
    }

    private native void _update__SceneManagerp(long _pointer_, long sceneMgr);

    /** 
    Reset the vertex buffer to the initial state. In the next update, the source renderable will be used as input. **/
    public void reset() {
        _reset(this.object.pointer);
    }

    private native void _reset(long _pointer_);

    /** 
    Set the source renderable of this object. During the first (and perhaps later) update of this object, this object's data will be used as input) **/
    public void setSourceRenderable(org.ogre4j.IRenderable source) {
        _setSourceRenderable__Renderablep(this.object.pointer, source.getInstancePointer().pointer);
    }

    private native void _setSourceRenderable__Renderablep(long _pointer_, long source);

    /** 
    Get the source renderable of this object **/
    public org.ogre4j.IRenderable getSourceRenderable() {
        return new org.ogre4j.Renderable(new InstancePointer(_getSourceRenderable_const(this.object.pointer)));
    }

    private native long _getSourceRenderable_const(long _pointer_);

    /** 
    Get the material which is used to render the geometry into the vertex buffer. **/
    public org.ogre4j.IMaterialPtr getRenderToBufferMaterial() {
        return new org.ogre4j.MaterialPtr(new InstancePointer(_getRenderToBufferMaterial(this.object.pointer)));
    }

    private native long _getRenderToBufferMaterial(long _pointer_);

    /** 
    Set the material name which is used to render the geometry into the vertex buffer **/
    public void setRenderToBufferMaterialName(String materialName) {
        _setRenderToBufferMaterialName__StringR(this.object.pointer, materialName);
    }

    private native void _setRenderToBufferMaterialName__StringR(long _pointer_, String materialName);
}

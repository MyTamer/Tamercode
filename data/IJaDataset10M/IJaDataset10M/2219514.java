package org.ogre4j;

import org.xbig.base.*;

public class Bone extends org.xbig.base.NativeObject implements org.ogre4j.IBone {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public Bone(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected Bone(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public Bone(org.xbig.base.WithoutNativeObject val) {
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
    Constructor, not to be used directly (use  or ) **/
    public Bone(int handle, org.ogre4j.ISkeleton creator) {
        super(new org.xbig.base.InstancePointer(__createBone__HvSkeletonp(handle, creator.getInstancePointer().pointer)), false);
    }

    private static native long __createBone__HvSkeletonp(int handle, long creator);

    /** 
    Constructor, not to be used directly (use  or ) **/
    public Bone(String name, int handle, org.ogre4j.ISkeleton creator) {
        super(new org.xbig.base.InstancePointer(__createBone__StringRHvSkeletonp(name, handle, creator.getInstancePointer().pointer)), false);
    }

    private static native long __createBone__StringRHvSkeletonp(String name, int handle, long creator);

    /** 
    Creates a new  as a child of this bone. **/
    public org.ogre4j.IBone createChild(int handle, org.ogre4j.IVector3 translate, org.ogre4j.IQuaternion rotate) {
        return new org.ogre4j.Bone(new InstancePointer(_createChild__HvVector3RQuaternionR(this.object.pointer, handle, translate.getInstancePointer().pointer, rotate.getInstancePointer().pointer)));
    }

    private native long _createChild__HvVector3RQuaternionR(long _pointer_, int handle, long translate, long rotate);

    /** 
    Gets the numeric handle for this bone (unique within the skeleton). **/
    public int getHandle() {
        return _getHandle_const(this.object.pointer);
    }

    private native int _getHandle_const(long _pointer_);

    /** 
    Sets the current position / orientation to be the 'binding pose' ie the layout in which bones were originally bound to a mesh. **/
    public void setBindingPose() {
        _setBindingPose(this.object.pointer);
    }

    private native void _setBindingPose(long _pointer_);

    /** 
    Resets the position and orientation of this  to the original binding position. **/
    public void reset() {
        _reset(this.object.pointer);
    }

    private native void _reset(long _pointer_);

    /** 
    Sets whether or not this bone is manually controlled. **/
    public void setManuallyControlled(boolean manuallyControlled) {
        _setManuallyControlled__bv(this.object.pointer, manuallyControlled);
    }

    private native void _setManuallyControlled__bv(long _pointer_, boolean manuallyControlled);

    /** 
    Getter for mManuallyControlled Flag **/
    public boolean isManuallyControlled() {
        return _isManuallyControlled_const(this.object.pointer);
    }

    private native boolean _isManuallyControlled_const(long _pointer_);

    /** 
    Gets the transform which takes bone space to current from the binding pose. **/
    public void _getOffsetTransform(org.ogre4j.IMatrix4 m) {
        __getOffsetTransform__Matrix4r_const(this.object.pointer, m.getInstancePointer().pointer);
    }

    private native void __getOffsetTransform__Matrix4r_const(long _pointer_, long m);

    /** 
    Gets the inverted binding pose scale. **/
    public org.ogre4j.IVector3 _getBindingPoseInverseScale() {
        return new org.ogre4j.Vector3(new InstancePointer(__getBindingPoseInverseScale_const(this.object.pointer)));
    }

    private native long __getBindingPoseInverseScale_const(long _pointer_);

    /** 
    Gets the inverted binding pose position. **/
    public org.ogre4j.IVector3 _getBindingPoseInversePosition() {
        return new org.ogre4j.Vector3(new InstancePointer(__getBindingPoseInversePosition_const(this.object.pointer)));
    }

    private native long __getBindingPoseInversePosition_const(long _pointer_);

    /** 
    Gets the inverted binding pose orientation. **/
    public org.ogre4j.IQuaternion _getBindingPoseInverseOrientation() {
        return new org.ogre4j.Quaternion(new InstancePointer(__getBindingPoseInverseOrientation_const(this.object.pointer)));
    }

    private native long __getBindingPoseInverseOrientation_const(long _pointer_);

    /** **/
    public void needUpdate(boolean forceParentUpdate) {
        _needUpdate__bv(this.object.pointer, forceParentUpdate);
    }

    private native void _needUpdate__bv(long _pointer_, boolean forceParentUpdate);

    /** 
    Returns the name of the node. **/
    public String getName() {
        return _getName_const(this.object.pointer);
    }

    private native String _getName_const(long _pointer_);

    /** 
    Gets this node's parent (NULL if this is the root). **/
    public org.ogre4j.INode getParent() {
        return new org.ogre4j.Node(new InstancePointer(_getParent_const(this.object.pointer)));
    }

    private native long _getParent_const(long _pointer_);

    /** 
    Returns a quaternion representing the nodes orientation. **/
    public org.ogre4j.IQuaternion getOrientation() {
        return new org.ogre4j.Quaternion(new InstancePointer(_getOrientation_const(this.object.pointer)));
    }

    private native long _getOrientation_const(long _pointer_);

    /** 
    Sets the orientation of this node via a quaternion. **/
    public void setOrientation(org.ogre4j.IQuaternion q) {
        _setOrientation__QuaternionR(this.object.pointer, q.getInstancePointer().pointer);
    }

    private native void _setOrientation__QuaternionR(long _pointer_, long q);

    /** 
    Sets the orientation of this node via quaternion parameters. **/
    public void setOrientation(float w, float x, float y, float z) {
        _setOrientation__RealvRealvRealvRealv(this.object.pointer, w, x, y, z);
    }

    private native void _setOrientation__RealvRealvRealvRealv(long _pointer_, float w, float x, float y, float z);

    /** 
    Resets the nodes orientation (local axes as world axes, no rotation). **/
    public void resetOrientation() {
        _resetOrientation(this.object.pointer);
    }

    private native void _resetOrientation(long _pointer_);

    /** 
    Sets the position of the node relative to it's parent. **/
    public void setPosition(org.ogre4j.IVector3 pos) {
        _setPosition__Vector3R(this.object.pointer, pos.getInstancePointer().pointer);
    }

    private native void _setPosition__Vector3R(long _pointer_, long pos);

    /** 
    Sets the position of the node relative to it's parent. **/
    public void setPosition(float x, float y, float z) {
        _setPosition__RealvRealvRealv(this.object.pointer, x, y, z);
    }

    private native void _setPosition__RealvRealvRealv(long _pointer_, float x, float y, float z);

    /** 
    Gets the position of the node relative to it's parent. **/
    public org.ogre4j.IVector3 getPosition() {
        return new org.ogre4j.Vector3(new InstancePointer(_getPosition_const(this.object.pointer)));
    }

    private native long _getPosition_const(long _pointer_);

    /** 
    Sets the scaling factor applied to this node. **/
    public void setScale(org.ogre4j.IVector3 scale) {
        _setScale__Vector3R(this.object.pointer, scale.getInstancePointer().pointer);
    }

    private native void _setScale__Vector3R(long _pointer_, long scale);

    /** 
    Sets the scaling factor applied to this node. **/
    public void setScale(float x, float y, float z) {
        _setScale__RealvRealvRealv(this.object.pointer, x, y, z);
    }

    private native void _setScale__RealvRealvRealv(long _pointer_, float x, float y, float z);

    /** 
    Gets the scaling factor of this node. **/
    public org.ogre4j.IVector3 getScale() {
        return new org.ogre4j.Vector3(new InstancePointer(_getScale_const(this.object.pointer)));
    }

    private native long _getScale_const(long _pointer_);

    /** 
    Tells the node whether it should inherit orientation from it's parent node. **/
    public void setInheritOrientation(boolean inherit) {
        _setInheritOrientation__bv(this.object.pointer, inherit);
    }

    private native void _setInheritOrientation__bv(long _pointer_, boolean inherit);

    /** 
    Returns true if this node is affected by orientation applied to the parent node. **/
    public boolean getInheritOrientation() {
        return _getInheritOrientation_const(this.object.pointer);
    }

    private native boolean _getInheritOrientation_const(long _pointer_);

    /** 
    Tells the node whether it should inherit scaling factors from it's parent node. **/
    public void setInheritScale(boolean inherit) {
        _setInheritScale__bv(this.object.pointer, inherit);
    }

    private native void _setInheritScale__bv(long _pointer_, boolean inherit);

    /** 
    Returns true if this node is affected by scaling factors applied to the parent node. **/
    public boolean getInheritScale() {
        return _getInheritScale_const(this.object.pointer);
    }

    private native boolean _getInheritScale_const(long _pointer_);

    /** 
    Scales the node, combining it's current scale with the passed in scaling factor. **/
    public void scale(org.ogre4j.IVector3 scale) {
        _scale__Vector3R(this.object.pointer, scale.getInstancePointer().pointer);
    }

    private native void _scale__Vector3R(long _pointer_, long scale);

    /** 
    Scales the node, combining it's current scale with the passed in scaling factor. **/
    public void scale(float x, float y, float z) {
        _scale__RealvRealvRealv(this.object.pointer, x, y, z);
    }

    private native void _scale__RealvRealvRealv(long _pointer_, float x, float y, float z);

    /** 
    Moves the node along the Cartesian axes. **/
    public void translate(org.ogre4j.IVector3 d, org.ogre4j.Node.TransformSpace relativeTo) {
        _translate__Vector3RTransformSpacev(this.object.pointer, d.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _translate__Vector3RTransformSpacev(long _pointer_, long d, int relativeTo);

    /** 
    Moves the node along the Cartesian axes. **/
    public void translate(float x, float y, float z, org.ogre4j.Node.TransformSpace relativeTo) {
        _translate__RealvRealvRealvTransformSpacev(this.object.pointer, x, y, z, relativeTo.getValue());
    }

    private native void _translate__RealvRealvRealvTransformSpacev(long _pointer_, float x, float y, float z, int relativeTo);

    /** 
    Moves the node along arbitrary axes. **/
    public void translate(org.ogre4j.IMatrix3 axes, org.ogre4j.IVector3 move, org.ogre4j.Node.TransformSpace relativeTo) {
        _translate__Matrix3RVector3RTransformSpacev(this.object.pointer, axes.getInstancePointer().pointer, move.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _translate__Matrix3RVector3RTransformSpacev(long _pointer_, long axes, long move, int relativeTo);

    /** 
    Moves the node along arbitrary axes. **/
    public void translate(org.ogre4j.IMatrix3 axes, float x, float y, float z, org.ogre4j.Node.TransformSpace relativeTo) {
        _translate__Matrix3RRealvRealvRealvTransformSpacev(this.object.pointer, axes.getInstancePointer().pointer, x, y, z, relativeTo.getValue());
    }

    private native void _translate__Matrix3RRealvRealvRealvTransformSpacev(long _pointer_, long axes, float x, float y, float z, int relativeTo);

    /** 
    Rotate the node around the Z-axis. **/
    public void roll(org.ogre4j.IRadian angle, org.ogre4j.Node.TransformSpace relativeTo) {
        _roll__RadianRTransformSpacev(this.object.pointer, angle.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _roll__RadianRTransformSpacev(long _pointer_, long angle, int relativeTo);

    /** 
    Rotate the node around the X-axis. **/
    public void pitch(org.ogre4j.IRadian angle, org.ogre4j.Node.TransformSpace relativeTo) {
        _pitch__RadianRTransformSpacev(this.object.pointer, angle.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _pitch__RadianRTransformSpacev(long _pointer_, long angle, int relativeTo);

    /** 
    Rotate the node around the Y-axis. **/
    public void yaw(org.ogre4j.IRadian angle, org.ogre4j.Node.TransformSpace relativeTo) {
        _yaw__RadianRTransformSpacev(this.object.pointer, angle.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _yaw__RadianRTransformSpacev(long _pointer_, long angle, int relativeTo);

    /** 
    Rotate the node around an arbitrary axis. **/
    public void rotate(org.ogre4j.IVector3 axis, org.ogre4j.IRadian angle, org.ogre4j.Node.TransformSpace relativeTo) {
        _rotate__Vector3RRadianRTransformSpacev(this.object.pointer, axis.getInstancePointer().pointer, angle.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _rotate__Vector3RRadianRTransformSpacev(long _pointer_, long axis, long angle, int relativeTo);

    /** 
    Rotate the node around an aritrary axis using a Quarternion. **/
    public void rotate(org.ogre4j.IQuaternion q, org.ogre4j.Node.TransformSpace relativeTo) {
        _rotate__QuaternionRTransformSpacev(this.object.pointer, q.getInstancePointer().pointer, relativeTo.getValue());
    }

    private native void _rotate__QuaternionRTransformSpacev(long _pointer_, long q, int relativeTo);

    /** 
    Gets a matrix whose columns are the local axes based on the nodes orientation relative to it's parent. **/
    public void getLocalAxes(org.ogre4j.IMatrix3 returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getLocalAxes_const(this.object.pointer)), false);
    }

    private native long _getLocalAxes_const(long _pointer_);

    /** 
    Creates an unnamed new  as a child of this node. **/
    public org.ogre4j.INode createChild(org.ogre4j.IVector3 translate, org.ogre4j.IQuaternion rotate) {
        return new org.ogre4j.Node(new InstancePointer(_createChild__Vector3RQuaternionR(this.object.pointer, translate.getInstancePointer().pointer, rotate.getInstancePointer().pointer)));
    }

    private native long _createChild__Vector3RQuaternionR(long _pointer_, long translate, long rotate);

    /** 
    Creates a new named  as a child of this node. **/
    public org.ogre4j.INode createChild(String name, org.ogre4j.IVector3 translate, org.ogre4j.IQuaternion rotate) {
        return new org.ogre4j.Node(new InstancePointer(_createChild__StringRVector3RQuaternionR(this.object.pointer, name, translate.getInstancePointer().pointer, rotate.getInstancePointer().pointer)));
    }

    private native long _createChild__StringRVector3RQuaternionR(long _pointer_, String name, long translate, long rotate);

    /** 
    Adds a (precreated) child scene node to this node. If it is attached to another node, it must be detached first. **/
    public void addChild(org.ogre4j.INode child) {
        _addChild__Nodep(this.object.pointer, child.getInstancePointer().pointer);
    }

    private native void _addChild__Nodep(long _pointer_, long child);

    /** 
    Reports the number of child nodes under this one. **/
    public int numChildren() {
        return _numChildren_const(this.object.pointer);
    }

    private native int _numChildren_const(long _pointer_);

    /** 
    Gets a pointer to a child node. **/
    public org.ogre4j.INode getChild(int index) {
        return new org.ogre4j.Node(new InstancePointer(_getChild__Hv_const(this.object.pointer, index)));
    }

    private native long _getChild__Hv_const(long _pointer_, int index);

    /** 
    Gets a pointer to a named child node. **/
    public org.ogre4j.INode getChild(String name) {
        return new org.ogre4j.Node(new InstancePointer(_getChild__StringR_const(this.object.pointer, name)));
    }

    private native long _getChild__StringR_const(long _pointer_, String name);

    /** 
    Retrieves an iterator for efficiently looping through all children of this node. **/
    public void getChildIterator(org.ogre4j.INode.IChildNodeIterator returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getChildIterator(this.object.pointer)), false);
    }

    private native long _getChildIterator(long _pointer_);

    /** **/
    public void getChildIterator_const(org.ogre4j.INode.IConstChildNodeIterator returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_getChildIterator_const_const(this.object.pointer)), false);
    }

    private native long _getChildIterator_const_const(long _pointer_);

    /** 
    Drops the specified child from this node. **/
    public org.ogre4j.INode removeChild(int index) {
        return new org.ogre4j.Node(new InstancePointer(_removeChild__Hv(this.object.pointer, index)));
    }

    private native long _removeChild__Hv(long _pointer_, int index);

    /** 
    Drops the specified child from this node. **/
    public org.ogre4j.INode removeChild(org.ogre4j.INode child) {
        return new org.ogre4j.Node(new InstancePointer(_removeChild__Nodep(this.object.pointer, child.getInstancePointer().pointer)));
    }

    private native long _removeChild__Nodep(long _pointer_, long child);

    /** 
    Drops the named child from this node. **/
    public org.ogre4j.INode removeChild(String name) {
        return new org.ogre4j.Node(new InstancePointer(_removeChild__StringR(this.object.pointer, name)));
    }

    private native long _removeChild__StringR(long _pointer_, String name);

    /** 
    Removes all child Nodes attached to this node. Does not delete the nodes, just detaches them from this parent, potentially to be reattached elsewhere. **/
    public void removeAllChildren() {
        _removeAllChildren(this.object.pointer);
    }

    private native void _removeAllChildren(long _pointer_);

    /** 
    Gets the orientation of the node as derived from all parents. **/
    public org.ogre4j.IQuaternion _getDerivedOrientation() {
        return new org.ogre4j.Quaternion(new InstancePointer(__getDerivedOrientation_const(this.object.pointer)));
    }

    private native long __getDerivedOrientation_const(long _pointer_);

    /** 
    Gets the position of the node as derived from all parents. **/
    public org.ogre4j.IVector3 _getDerivedPosition() {
        return new org.ogre4j.Vector3(new InstancePointer(__getDerivedPosition_const(this.object.pointer)));
    }

    private native long __getDerivedPosition_const(long _pointer_);

    /** 
    Gets the scaling factor of the node as derived from all parents. **/
    public org.ogre4j.IVector3 _getDerivedScale() {
        return new org.ogre4j.Vector3(new InstancePointer(__getDerivedScale_const(this.object.pointer)));
    }

    private native long __getDerivedScale_const(long _pointer_);

    /** 
    Gets the full transformation matrix for this node. **/
    public org.ogre4j.IMatrix4 _getFullTransform() {
        return new org.ogre4j.Matrix4(new InstancePointer(__getFullTransform_const(this.object.pointer)));
    }

    private native long __getFullTransform_const(long _pointer_);

    /** 
    Internal method to update the . **/
    public void _update(boolean updateChildren, boolean parentHasChanged) {
        __update__bvbv(this.object.pointer, updateChildren, parentHasChanged);
    }

    private native void __update__bvbv(long _pointer_, boolean updateChildren, boolean parentHasChanged);

    /** 
    Sets a listener for this . **/
    public void setListener(org.ogre4j.INode.IListener listener) {
        _setListener__Listenerp(this.object.pointer, listener.getInstancePointer().pointer);
    }

    private native void _setListener__Listenerp(long _pointer_, long listener);

    /** 
    Gets the current listener for this . **/
    public org.ogre4j.INode.IListener getListener() {
        return new org.ogre4j.Node.Listener(new InstancePointer(_getListener_const(this.object.pointer)));
    }

    private native long _getListener_const(long _pointer_);

    /** 
    Overridden from . **/
    public org.ogre4j.IMaterialPtr getMaterial() {
        return new org.ogre4j.MaterialPtr(new InstancePointer(_getMaterial_const(this.object.pointer)));
    }

    private native long _getMaterial_const(long _pointer_);

    /** 
    Overridden from . **/
    public void getRenderOperation(org.ogre4j.IRenderOperation op) {
        _getRenderOperation__RenderOperationr(this.object.pointer, op.getInstancePointer().pointer);
    }

    private native void _getRenderOperation__RenderOperationr(long _pointer_, long op);

    /** 
    Overridden from . **/
    public void getWorldTransforms(org.ogre4j.IMatrix4 xform) {
        _getWorldTransforms__Matrix4p_const(this.object.pointer, xform.getInstancePointer().pointer);
    }

    private native void _getWorldTransforms__Matrix4p_const(long _pointer_, long xform);

    /** 
    Sets the current transform of this node to be the 'initial state' ie that position / orientation / scale to be used as a basis for delta values used in keyframe animation. **/
    public void setInitialState() {
        _setInitialState(this.object.pointer);
    }

    private native void _setInitialState(long _pointer_);

    /** 
    Resets the position / orientation / scale of this node to it's initial state, see setInitialState for more info. **/
    public void resetToInitialState() {
        _resetToInitialState(this.object.pointer);
    }

    private native void _resetToInitialState(long _pointer_);

    /** 
    Gets the initial position of this node, see setInitialState for more info. **/
    public org.ogre4j.IVector3 getInitialPosition() {
        return new org.ogre4j.Vector3(new InstancePointer(_getInitialPosition_const(this.object.pointer)));
    }

    private native long _getInitialPosition_const(long _pointer_);

    /** 
    Gets the initial orientation of this node, see setInitialState for more info. **/
    public org.ogre4j.IQuaternion getInitialOrientation() {
        return new org.ogre4j.Quaternion(new InstancePointer(_getInitialOrientation_const(this.object.pointer)));
    }

    private native long _getInitialOrientation_const(long _pointer_);

    /** 
    Gets the initial position of this node, see setInitialState for more info. **/
    public org.ogre4j.IVector3 getInitialScale() {
        return new org.ogre4j.Vector3(new InstancePointer(_getInitialScale_const(this.object.pointer)));
    }

    private native long _getInitialScale_const(long _pointer_);

    /** 
    Overridden, see **/
    public float getSquaredViewDepth(org.ogre4j.ICamera cam) {
        return _getSquaredViewDepth__CameraP_const(this.object.pointer, cam.getInstancePointer().pointer);
    }

    private native float _getSquaredViewDepth__CameraP_const(long _pointer_, long cam);

    /** 
    Called by children to notify their parent that they need an update. **/
    public void requestUpdate(org.ogre4j.INode child, boolean forceParentUpdate) {
        _requestUpdate__Nodepbv(this.object.pointer, child.getInstancePointer().pointer, forceParentUpdate);
    }

    private native void _requestUpdate__Nodepbv(long _pointer_, long child, boolean forceParentUpdate);

    /** 
    Called by children to notify their parent that they no longer need an update. **/
    public void cancelUpdate(org.ogre4j.INode child) {
        _cancelUpdate__Nodep(this.object.pointer, child.getInstancePointer().pointer);
    }

    private native void _cancelUpdate__Nodep(long _pointer_, long child);

    /** 
    **/
    public org.ogre4j.ILightList getLights() {
        return new org.ogre4j.LightList(new InstancePointer(_getLights_const(this.object.pointer)));
    }

    private native long _getLights_const(long _pointer_);

    /** 
    Queue a 'needUpdate' call to a node safely. **/
    public static void queueNeedUpdate(org.ogre4j.INode n) {
        _queueNeedUpdate__Nodep(n.getInstancePointer().pointer);
    }

    private static native void _queueNeedUpdate__Nodep(long n);

    /** 
    Process queued 'needUpdate' calls. **/
    public static void processQueuedUpdates() {
        _processQueuedUpdates();
    }

    private static native void _processQueuedUpdates();

    /** 
    Retrieves a pointer to the  this renderable object uses. **/
    public org.ogre4j.ITechnique getTechnique() {
        return new org.ogre4j.Technique(new InstancePointer(_getTechnique_const(this.object.pointer)));
    }

    private native long _getTechnique_const(long _pointer_);

    /** 
    Called just prior to the  being rendered. **/
    public boolean preRender(org.ogre4j.ISceneManager sm, org.ogre4j.IRenderSystem rsys) {
        return _preRender__SceneManagerpRenderSystemp(this.object.pointer, sm.getInstancePointer().pointer, rsys.getInstancePointer().pointer);
    }

    private native boolean _preRender__SceneManagerpRenderSystemp(long _pointer_, long sm, long rsys);

    /** 
    Called immediately after the  has been rendered. **/
    public void postRender(org.ogre4j.ISceneManager sm, org.ogre4j.IRenderSystem rsys) {
        _postRender__SceneManagerpRenderSystemp(this.object.pointer, sm.getInstancePointer().pointer, rsys.getInstancePointer().pointer);
    }

    private native void _postRender__SceneManagerpRenderSystemp(long _pointer_, long sm, long rsys);

    /** 
    Returns the number of world transform matrices this renderable requires. **/
    public int getNumWorldTransforms() {
        return _getNumWorldTransforms_const(this.object.pointer);
    }

    private native int _getNumWorldTransforms_const(long _pointer_);

    /** 
    Sets whether or not to use an 'identity' projection. **/
    public void setUseIdentityProjection(boolean useIdentityProjection) {
        _setUseIdentityProjection__bv(this.object.pointer, useIdentityProjection);
    }

    private native void _setUseIdentityProjection__bv(long _pointer_, boolean useIdentityProjection);

    /** 
    Returns whether or not to use an 'identity' projection. **/
    public boolean getUseIdentityProjection() {
        return _getUseIdentityProjection_const(this.object.pointer);
    }

    private native boolean _getUseIdentityProjection_const(long _pointer_);

    /** 
    Sets whether or not to use an 'identity' view. **/
    public void setUseIdentityView(boolean useIdentityView) {
        _setUseIdentityView__bv(this.object.pointer, useIdentityView);
    }

    private native void _setUseIdentityView__bv(long _pointer_, boolean useIdentityView);

    /** 
    Returns whether or not to use an 'identity' view. **/
    public boolean getUseIdentityView() {
        return _getUseIdentityView_const(this.object.pointer);
    }

    private native boolean _getUseIdentityView_const(long _pointer_);

    /** 
    Method which reports whether this renderable would normally cast a shadow. **/
    public boolean getCastsShadows() {
        return _getCastsShadows_const(this.object.pointer);
    }

    private native boolean _getCastsShadows_const(long _pointer_);

    /** 
    Sets a custom parameter for this , which may be used to drive calculations for this specific , like GPU program parameters. **/
    public void setCustomParameter(int index, org.ogre4j.IVector4 value) {
        _setCustomParameter__ivVector4R(this.object.pointer, index, value.getInstancePointer().pointer);
    }

    private native void _setCustomParameter__ivVector4R(long _pointer_, int index, long value);

    /** 
    Gets the custom value associated with this  at the given index. **/
    public org.ogre4j.IVector4 getCustomParameter(int index) {
        return new org.ogre4j.Vector4(new InstancePointer(_getCustomParameter__iv_const(this.object.pointer, index)));
    }

    private native long _getCustomParameter__iv_const(long _pointer_, int index);

    /** 
    Update a custom  constant which is derived from information only this  knows. **/
    public void _updateCustomGpuParameter(org.ogre4j.IGpuProgramParameters.IAutoConstantEntry constantEntry, org.ogre4j.IGpuProgramParameters params) {
        __updateCustomGpuParameter__GpuProgramParameters_AutoConstantEntryRGpuProgramParametersp_const(this.object.pointer, constantEntry.getInstancePointer().pointer, params.getInstancePointer().pointer);
    }

    private native void __updateCustomGpuParameter__GpuProgramParameters_AutoConstantEntryRGpuProgramParametersp_const(long _pointer_, long constantEntry, long params);

    /** 
    Sets whether this renderable's chosen detail level can be overridden (downgraded) by the camera setting. **/
    public void setPolygonModeOverrideable(boolean override) {
        _setPolygonModeOverrideable__bv(this.object.pointer, override);
    }

    private native void _setPolygonModeOverrideable__bv(long _pointer_, boolean override);

    /** 
    Gets whether this renderable's chosen detail level can be overridden (downgraded) by the camera setting. **/
    public boolean getPolygonModeOverrideable() {
        return _getPolygonModeOverrideable_const(this.object.pointer);
    }

    private native boolean _getPolygonModeOverrideable_const(long _pointer_);

    /** 
    Sets any kind of user value on this object. **/
    public void setUserAny(org.ogre4j.IAny anything) {
        _setUserAny__AnyR(this.object.pointer, anything.getInstancePointer().pointer);
    }

    private native void _setUserAny__AnyR(long _pointer_, long anything);

    /** 
    Retrieves the custom user value associated with this object. **/
    public org.ogre4j.IAny getUserAny() {
        return new org.ogre4j.Any(new InstancePointer(_getUserAny_const(this.object.pointer)));
    }

    private native long _getUserAny_const(long _pointer_);

    /** 
    Sets render system private data **/
    public org.ogre4j.IRenderable.IRenderSystemData getRenderSystemData() {
        return new org.ogre4j.Renderable.RenderSystemData(new InstancePointer(_getRenderSystemData_const(this.object.pointer)));
    }

    private native long _getRenderSystemData_const(long _pointer_);

    /** 
    gets render system private data **/
    public void setRenderSystemData(org.ogre4j.IRenderable.IRenderSystemData val) {
        _setRenderSystemData__RenderSystemDatap_const(this.object.pointer, val.getInstancePointer().pointer);
    }

    private native void _setRenderSystemData__RenderSystemDatap_const(long _pointer_, long val);
}

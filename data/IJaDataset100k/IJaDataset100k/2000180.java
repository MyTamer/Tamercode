package org.ogre4j;

import org.xbig.base.*;

public class Vector4 extends org.xbig.base.NativeObject implements org.ogre4j.IVector4 {

    static {
        System.loadLibrary("ogre4j");
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public Vector4(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected Vector4(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public Vector4(org.xbig.base.WithoutNativeObject val) {
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
    public Vector4() {
        super(new org.xbig.base.InstancePointer(__createVector4()), false);
    }

    private static native long __createVector4();

    /** **/
    public Vector4(float fX, float fY, float fZ, float fW) {
        super(new org.xbig.base.InstancePointer(__createVector4__RealVRealVRealVRealV(fX, fY, fZ, fW)), false);
    }

    private static native long __createVector4__RealVRealVRealVRealV(float fX, float fY, float fZ, float fW);

    /** **/
    public Vector4(int afCoordinate) {
        super(new org.xbig.base.InstancePointer(__createVector4__iV(afCoordinate)), false);
    }

    private static native long __createVector4__iV(int afCoordinate);

    /** **/
    public Vector4(FloatPointer r) {
        super(new org.xbig.base.InstancePointer(__createVector4__Realp(r.object.pointer)), false);
    }

    private static native long __createVector4__Realp(long r);

    /** **/
    public Vector4(org.ogre4j.IVector3 rhs) {
        super(new org.xbig.base.InstancePointer(__createVector4__Vector3R(rhs.getInstancePointer().pointer)), false);
    }

    private static native long __createVector4__Vector3R(long rhs);

    /** **/
    public float operatorIndex_const(int i) {
        return _operatorIndex_const__iV_const(this.object.pointer, i);
    }

    private native float _operatorIndex_const__iV_const(long _pointer_, int i);

    /** **/
    public FloatPointer operatorIndex(int i) {
        return new FloatPointer(new InstancePointer(_operatorIndex__iV(this.object.pointer, i)));
    }

    private native long _operatorIndex__iV(long _pointer_, int i);

    /** **/
    public FloatPointer ptr() {
        return new FloatPointer(new InstancePointer(_ptr(this.object.pointer)));
    }

    private native long _ptr(long _pointer_);

    /** **/
    public FloatPointer ptr_const() {
        return new FloatPointer(new InstancePointer(_ptr_const_const(this.object.pointer)));
    }

    private native long _ptr_const_const(long _pointer_);

    /** 
    Assigns the value of the other vector. **/
    public org.ogre4j.IVector4 operatorAssignment(org.ogre4j.IVector4 rkVector) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorAssignment__Vector4R(this.object.pointer, rkVector.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment__Vector4R(long _pointer_, long rkVector);

    /** **/
    public org.ogre4j.IVector4 operatorAssignment(float fScalar) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorAssignment__RealV(this.object.pointer, fScalar)));
    }

    private native long _operatorAssignment__RealV(long _pointer_, float fScalar);

    /** **/
    public boolean operatorEqual(org.ogre4j.IVector4 rkVector) {
        return _operatorEqual__Vector4R_const(this.object.pointer, rkVector.getInstancePointer().pointer);
    }

    private native boolean _operatorEqual__Vector4R_const(long _pointer_, long rkVector);

    /** **/
    public boolean operatorNotEqual(org.ogre4j.IVector4 rkVector) {
        return _operatorNotEqual__Vector4R_const(this.object.pointer, rkVector.getInstancePointer().pointer);
    }

    private native boolean _operatorNotEqual__Vector4R_const(long _pointer_, long rkVector);

    /** **/
    public org.ogre4j.IVector4 operatorAssignment(org.ogre4j.IVector3 rhs) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorAssignment__Vector3R(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment__Vector3R(long _pointer_, long rhs);

    /** **/
    public void operatorAddition(org.ogre4j.IVector4 returnValue, org.ogre4j.IVector4 rkVector) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorAddition__Vector4R_const(this.object.pointer, rkVector.getInstancePointer().pointer)), false);
    }

    private native long _operatorAddition__Vector4R_const(long _pointer_, long rkVector);

    /** **/
    public void operatorSubtraction(org.ogre4j.IVector4 returnValue, org.ogre4j.IVector4 rkVector) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorSubtraction__Vector4R_const(this.object.pointer, rkVector.getInstancePointer().pointer)), false);
    }

    private native long _operatorSubtraction__Vector4R_const(long _pointer_, long rkVector);

    /** **/
    public void operatorMultiplication(org.ogre4j.IVector4 returnValue, float fScalar) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorMultiplication__RealV_const(this.object.pointer, fScalar)), false);
    }

    private native long _operatorMultiplication__RealV_const(long _pointer_, float fScalar);

    /** **/
    public void operatorMultiplication(org.ogre4j.IVector4 returnValue, org.ogre4j.IVector4 rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorMultiplication__Vector4R_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorMultiplication__Vector4R_const(long _pointer_, long rhs);

    /** **/
    public void operatorDivision(org.ogre4j.IVector4 returnValue, float fScalar) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorDivision__RealV_const(this.object.pointer, fScalar)), false);
    }

    private native long _operatorDivision__RealV_const(long _pointer_, float fScalar);

    /** **/
    public void operatorDivision(org.ogre4j.IVector4 returnValue, org.ogre4j.IVector4 rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorDivision__Vector4R_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorDivision__Vector4R_const(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IVector4 operatorAddition() {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorAddition_const(this.object.pointer)));
    }

    private native long _operatorAddition_const(long _pointer_);

    /** **/
    public void operatorSubtraction(org.ogre4j.IVector4 returnValue) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorSubtraction_const(this.object.pointer)), false);
    }

    private native long _operatorSubtraction_const(long _pointer_);

    /** **/
    public org.ogre4j.IVector4 operatorIncrementAndAssign(org.ogre4j.IVector4 rkVector) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorIncrementAndAssign__Vector4R(this.object.pointer, rkVector.getInstancePointer().pointer)));
    }

    private native long _operatorIncrementAndAssign__Vector4R(long _pointer_, long rkVector);

    /** **/
    public org.ogre4j.IVector4 operatorDecrementAndAssign(org.ogre4j.IVector4 rkVector) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorDecrementAndAssign__Vector4R(this.object.pointer, rkVector.getInstancePointer().pointer)));
    }

    private native long _operatorDecrementAndAssign__Vector4R(long _pointer_, long rkVector);

    /** **/
    public org.ogre4j.IVector4 operatorMultiplyAndAssign(float fScalar) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorMultiplyAndAssign__RealV(this.object.pointer, fScalar)));
    }

    private native long _operatorMultiplyAndAssign__RealV(long _pointer_, float fScalar);

    /** **/
    public org.ogre4j.IVector4 operatorIncrementAndAssign(float fScalar) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorIncrementAndAssign__RealV(this.object.pointer, fScalar)));
    }

    private native long _operatorIncrementAndAssign__RealV(long _pointer_, float fScalar);

    /** **/
    public org.ogre4j.IVector4 operatorDecrementAndAssign(float fScalar) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorDecrementAndAssign__RealV(this.object.pointer, fScalar)));
    }

    private native long _operatorDecrementAndAssign__RealV(long _pointer_, float fScalar);

    /** **/
    public org.ogre4j.IVector4 operatorMultiplyAndAssign(org.ogre4j.IVector4 rkVector) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorMultiplyAndAssign__Vector4R(this.object.pointer, rkVector.getInstancePointer().pointer)));
    }

    private native long _operatorMultiplyAndAssign__Vector4R(long _pointer_, long rkVector);

    /** **/
    public org.ogre4j.IVector4 operatorDivideAndAssign(float fScalar) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorDivideAndAssign__RealV(this.object.pointer, fScalar)));
    }

    private native long _operatorDivideAndAssign__RealV(long _pointer_, float fScalar);

    /** **/
    public org.ogre4j.IVector4 operatorDivideAndAssign(org.ogre4j.IVector4 rkVector) {
        return new org.ogre4j.Vector4(new InstancePointer(_operatorDivideAndAssign__Vector4R(this.object.pointer, rkVector.getInstancePointer().pointer)));
    }

    private native long _operatorDivideAndAssign__Vector4R(long _pointer_, long rkVector);

    /** 
    Calculates the dot (scalar) product of this vector with another. **/
    public float dotProduct(org.ogre4j.IVector4 vec) {
        return _dotProduct__Vector4R_const(this.object.pointer, vec.getInstancePointer().pointer);
    }

    private native float _dotProduct__Vector4R_const(long _pointer_, long vec);

    /** **/
    public float getx() {
        return _getx(this.object.pointer);
    }

    private native float _getx(long _pointer_);

    /** **/
    public void setx(float _jni_value_) {
        _setx(this.object.pointer, _jni_value_);
    }

    private native void _setx(long _pointer_, float _jni_value_);

    /** **/
    public float gety() {
        return _gety(this.object.pointer);
    }

    private native float _gety(long _pointer_);

    /** **/
    public void sety(float _jni_value_) {
        _sety(this.object.pointer, _jni_value_);
    }

    private native void _sety(long _pointer_, float _jni_value_);

    /** **/
    public float getz() {
        return _getz(this.object.pointer);
    }

    private native float _getz(long _pointer_);

    /** **/
    public void setz(float _jni_value_) {
        _setz(this.object.pointer, _jni_value_);
    }

    private native void _setz(long _pointer_, float _jni_value_);

    /** **/
    public float getw() {
        return _getw(this.object.pointer);
    }

    private native float _getw(long _pointer_);

    /** **/
    public void setw(float _jni_value_) {
        _setw(this.object.pointer, _jni_value_);
    }

    private native void _setw(long _pointer_, float _jni_value_);

    /** **/
    public static org.ogre4j.IVector4 getZERO() {
        return new org.ogre4j.Vector4(new InstancePointer(_getZERO()));
    }

    private static native long _getZERO();
}

package org.ogre4j;

import org.xbig.base.*;

public class AnyNumeric extends org.xbig.base.NativeObject implements org.ogre4j.IAnyNumeric {

    static {
        System.loadLibrary("ogre4j");
    }

    protected static class numplaceholder extends org.xbig.base.NativeObject implements org.ogre4j.IAnyNumeric.Inumplaceholder {

        static {
            System.loadLibrary("ogre4j");
        }

        /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
        public numplaceholder(org.xbig.base.InstancePointer p) {
            super(p);
        }

        /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
        protected numplaceholder(org.xbig.base.InstancePointer p, boolean remote) {
            super(p, remote);
        }

        /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
        public numplaceholder(org.xbig.base.WithoutNativeObject val) {
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
        public org.ogre4j.IAny.Iplaceholder add(org.ogre4j.IAny.Iplaceholder rhs) {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_add__placeholderp(this.object.pointer, rhs.getInstancePointer().pointer)));
        }

        private native long _add__placeholderp(long _pointer_, long rhs);

        /** **/
        public org.ogre4j.IAny.Iplaceholder subtract(org.ogre4j.IAny.Iplaceholder rhs) {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_subtract__placeholderp(this.object.pointer, rhs.getInstancePointer().pointer)));
        }

        private native long _subtract__placeholderp(long _pointer_, long rhs);

        /** **/
        public org.ogre4j.IAny.Iplaceholder multiply(org.ogre4j.IAny.Iplaceholder rhs) {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_multiply__placeholderp(this.object.pointer, rhs.getInstancePointer().pointer)));
        }

        private native long _multiply__placeholderp(long _pointer_, long rhs);

        /** **/
        public org.ogre4j.IAny.Iplaceholder multiply(float factor) {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_multiply__Realv(this.object.pointer, factor)));
        }

        private native long _multiply__Realv(long _pointer_, float factor);

        /** **/
        public org.ogre4j.IAny.Iplaceholder divide(org.ogre4j.IAny.Iplaceholder rhs) {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_divide__placeholderp(this.object.pointer, rhs.getInstancePointer().pointer)));
        }

        private native long _divide__placeholderp(long _pointer_, long rhs);

        /** **/
        public org.std.Itype_info getType() {
            return new org.std.type_info(new InstancePointer(_getType_const(this.object.pointer)));
        }

        private native long _getType_const(long _pointer_);

        /** **/
        public org.ogre4j.IAny.Iplaceholder clone() {
            return new org.ogre4j.Any.placeholder(new InstancePointer(_clone_const(this.object.pointer)));
        }

        private native long _clone_const(long _pointer_);

        /** **/
        public void writeToStream(org.std.Iostream o) {
            _writeToStream__std_ostreamr(this.object.pointer, o.getInstancePointer().pointer);
        }

        private native void _writeToStream__std_ostreamr(long _pointer_, long o);
    }

    /**
	 * 
	 * This constructor is public for internal useage only!
	 * Do not use it!
	 * 
	 */
    public AnyNumeric(org.xbig.base.InstancePointer p) {
        super(p);
    }

    /**
	 * 
	 * Creates a Java wrapper object for an existing C++ object.
	 * If remote is set to 'true' this object cannot be deleted in Java.
	 * 
	 */
    protected AnyNumeric(org.xbig.base.InstancePointer p, boolean remote) {
        super(p, remote);
    }

    /**
     * Allows creation of Java objects without C++ objects.
     * 
     * @see org.xbig.base.WithoutNativeObject
     * @see org.xbig.base.INativeObject#disconnectFromNativeObject()
     */
    public AnyNumeric(org.xbig.base.WithoutNativeObject val) {
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
    public AnyNumeric() {
        super(new org.xbig.base.InstancePointer(__createAnyNumeric()), false);
    }

    private static native long __createAnyNumeric();

    /** **/
    public AnyNumeric(org.ogre4j.IAnyNumeric other) {
        super(new org.xbig.base.InstancePointer(__createAnyNumeric__AnyNumericR(other.getInstancePointer().pointer)), false);
    }

    private static native long __createAnyNumeric__AnyNumericR(long other);

    /** **/
    public org.ogre4j.IAnyNumeric operatorAssignment(org.ogre4j.IAnyNumeric rhs) {
        return new org.ogre4j.AnyNumeric(new InstancePointer(_operatorAssignment__AnyNumericR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment__AnyNumericR(long _pointer_, long rhs);

    /** **/
    public void operatorAddition(org.ogre4j.IAnyNumeric returnValue, org.ogre4j.IAnyNumeric rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorAddition__AnyNumericR_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorAddition__AnyNumericR_const(long _pointer_, long rhs);

    /** **/
    public void operatorSubtraction(org.ogre4j.IAnyNumeric returnValue, org.ogre4j.IAnyNumeric rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorSubtraction__AnyNumericR_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorSubtraction__AnyNumericR_const(long _pointer_, long rhs);

    /** **/
    public void operatorMultiplication(org.ogre4j.IAnyNumeric returnValue, org.ogre4j.IAnyNumeric rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorMultiplication__AnyNumericR_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorMultiplication__AnyNumericR_const(long _pointer_, long rhs);

    /** **/
    public void operatorMultiplication(org.ogre4j.IAnyNumeric returnValue, float factor) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorMultiplication__Realv_const(this.object.pointer, factor)), false);
    }

    private native long _operatorMultiplication__Realv_const(long _pointer_, float factor);

    /** **/
    public void operatorDivision(org.ogre4j.IAnyNumeric returnValue, org.ogre4j.IAnyNumeric rhs) {
        returnValue.delete();
        returnValue.setInstancePointer(new InstancePointer(_operatorDivision__AnyNumericR_const(this.object.pointer, rhs.getInstancePointer().pointer)), false);
    }

    private native long _operatorDivision__AnyNumericR_const(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAnyNumeric operatorIncrementAndAssign(org.ogre4j.IAnyNumeric rhs) {
        return new org.ogre4j.AnyNumeric(new InstancePointer(_operatorIncrementAndAssign__AnyNumericR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorIncrementAndAssign__AnyNumericR(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAnyNumeric operatorDecrementAndAssign(org.ogre4j.IAnyNumeric rhs) {
        return new org.ogre4j.AnyNumeric(new InstancePointer(_operatorDecrementAndAssign__AnyNumericR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorDecrementAndAssign__AnyNumericR(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAnyNumeric operatorMultiplyAndAssign(org.ogre4j.IAnyNumeric rhs) {
        return new org.ogre4j.AnyNumeric(new InstancePointer(_operatorMultiplyAndAssign__AnyNumericR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorMultiplyAndAssign__AnyNumericR(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAnyNumeric operatorDivideAndAssign(org.ogre4j.IAnyNumeric rhs) {
        return new org.ogre4j.AnyNumeric(new InstancePointer(_operatorDivideAndAssign__AnyNumericR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorDivideAndAssign__AnyNumericR(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAny swap(org.ogre4j.IAny rhs) {
        return new org.ogre4j.Any(new InstancePointer(_swap__Anyr(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _swap__Anyr(long _pointer_, long rhs);

    /** **/
    public org.ogre4j.IAny operatorAssignment(org.ogre4j.IAny rhs) {
        return new org.ogre4j.Any(new InstancePointer(_operatorAssignment__AnyR(this.object.pointer, rhs.getInstancePointer().pointer)));
    }

    private native long _operatorAssignment__AnyR(long _pointer_, long rhs);

    /** **/
    public boolean isEmpty() {
        return _isEmpty_const(this.object.pointer);
    }

    private native boolean _isEmpty_const(long _pointer_);

    /** **/
    public org.std.Itype_info getType() {
        return new org.std.type_info(new InstancePointer(_getType_const(this.object.pointer)));
    }

    private native long _getType_const(long _pointer_);
}

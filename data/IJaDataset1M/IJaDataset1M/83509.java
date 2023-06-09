package dot.junit.opcodes.sub_float_2addr;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_1;
import dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_5;

public class Test_sub_float_2addr extends DxTestCase {

    /**
     * @title Arguments = 2.7f, 3.14f
     */
    public void testN1() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(-0.44000006f, t.run(2.7f, 3.14f));
    }

    /**
     * @title Arguments = 0, -3.14f
     */
    public void testN2() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(3.14f, t.run(0, -3.14f));
    }

    /**
     * @title 
     */
    public void testN3() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(-0.44000006f, t.run(-3.14f, -2.7f));
    }

    /**
     * @title Types of arguments - int, float. Dalvik doens't distinguish 32-bits types internally,
     * so this subtraction of float and int makes no sense but shall not crash the VM.  
     */
    public void testN4() {
        T_sub_float_2addr_5 t = new T_sub_float_2addr_5();
        try {
            t.run(1, 3.14f);
        } catch (Throwable e) {
        }
    }

    /**
     * @title Arguments = Float.MAX_VALUE, Float.NaN
     */
    public void testB1() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(Float.NaN, t.run(Float.MAX_VALUE, Float.NaN));
    }

    /**
     * @title Arguments = Float.POSITIVE_INFINITY,
     * Float.NEGATIVE_INFINITY
     */
    public void testB2() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(Float.POSITIVE_INFINITY, t.run(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
    }

    /**
     * @title Arguments = Float.POSITIVE_INFINITY,
     * Float.POSITIVE_INFINITY
     */
    public void testB3() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(Float.NaN, t.run(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
    }

    /**
     * @title Arguments = Float.POSITIVE_INFINITY, -2.7f
     */
    public void testB4() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(Float.POSITIVE_INFINITY, t.run(Float.POSITIVE_INFINITY, -2.7f));
    }

    /**
     * @title Arguments = +0, -0f
     */
    public void testB5() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(+0f, t.run(+0f, -0f));
    }

    /**
     * @title Arguments = -0f, -0f
     */
    public void testB6() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(0f, t.run(-0f, -0f));
    }

    /**
     * @title Arguments = +0f, +0f
     */
    public void testB7() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(+0f, t.run(+0f, +0f));
    }

    /**
     * @title Arguments = 2.7f, 2.7f
     */
    public void testB8() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(0f, t.run(2.7f, 2.7f));
    }

    /**
     * @title Arguments = Float.MAX_VALUE, Float.MAX_VALUE
     */
    public void testB9() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(0f, t.run(Float.MAX_VALUE, Float.MAX_VALUE));
    }

    /**
     * @title Arguments = Float.MIN_VALUE, -1.4E-45f
     */
    public void testB10() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(0f, t.run(Float.MIN_VALUE, 1.4E-45f));
    }

    /**
     * @title Arguments = Float.MAX_VALUE, -Float.MAX_VALUE
     */
    public void testB11() {
        T_sub_float_2addr_1 t = new T_sub_float_2addr_1();
        assertEquals(Float.POSITIVE_INFINITY, t.run(Float.MAX_VALUE, -3.402823E+38F));
    }

    /**
     * @constraint B1 
     * @title types of arguments - float, double
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - long, float
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - reference, float
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE5() {
        try {
            Class.forName("dot.junit.opcodes.sub_float_2addr.d.T_sub_float_2addr_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}

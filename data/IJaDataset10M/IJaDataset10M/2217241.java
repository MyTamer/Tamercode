package dot.junit.opcodes.xor_int_lit16;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_1;
import dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_2;
import dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_3;
import dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_4;
import dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_7;

public class Test_xor_int_lit16 extends DxTestCase {

    /**
     * @title Arguments = 15, 8
     */
    public void testN1() {
        T_xor_int_lit16_1 t = new T_xor_int_lit16_1();
        assertEquals(7, t.run());
    }

    /**
     * @title Arguments = 0xfffffff8, 0xfff1
     */
    public void testN2() {
        T_xor_int_lit16_2 t = new T_xor_int_lit16_2();
        assertEquals(9, t.run());
    }

    /**
     * @title Type of argument - float. Dalvik doens't distinguish 32-bits types internally,
     * so this (float ^ int) makes no sense but shall not crash the VM.  
     */
    public void testN3() {
        T_xor_int_lit16_7 t = new T_xor_int_lit16_7();
        try {
            t.run(3.14f);
        } catch (Throwable e) {
        }
    }

    /**
     * @title Arguments = 0, -1
     */
    public void testB1() {
        T_xor_int_lit16_3 t = new T_xor_int_lit16_3();
        assertEquals(-1, t.run());
    }

    /** 
     * @title Arguments = Integer.MAX_VALUE, Short.MIN_VALUE
     */
    public void testB2() {
        T_xor_int_lit16_4 t = new T_xor_int_lit16_4();
        assertEquals(-2147450881, t.run());
    }

    /**
     * @constraint B1 
     * @title types of arguments - long & int
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title  types of arguments - reference & int
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint A23 
     * @title  number of registers
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.xor_int_lit16.d.T_xor_int_lit16_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}

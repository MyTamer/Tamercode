package dot.junit.opcodes.sget;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.sget.d.T_sget_1;
import dot.junit.opcodes.sget.d.T_sget_11;
import dot.junit.opcodes.sget.d.T_sget_2;
import dot.junit.opcodes.sget.d.T_sget_5;
import dot.junit.opcodes.sget.d.T_sget_9;

public class Test_sget extends DxTestCase {

    /**
     * @title type - int
     */
    public void testN1() {
        T_sget_1 t = new T_sget_1();
        assertEquals(5, t.run());
    }

    /**
     * @title type - float
     */
    public void testN2() {
        T_sget_2 t = new T_sget_2();
        assertEquals(123f, t.run());
    }

    /**
     * @title access protected field from subclass
     */
    public void testN3() {
        T_sget_11 t = new T_sget_11();
        assertEquals(10, t.run());
    }

    /**
     * @constraint A12
     * @title attempt to access non-static field
     */
    public void testE1() {
        T_sget_5 t = new T_sget_5();
        try {
            t.run();
            fail("expected IncompatibleClassChangeError");
        } catch (IncompatibleClassChangeError e) {
        }
    }

    /**
     * @title initialization of referenced class throws exception
     */
    public void testE6() {
        T_sget_9 t = new T_sget_9();
        try {
            t.run();
            fail("expected Error");
        } catch (Error e) {
        }
    }

    /**
     * @constraint A12 
     * @title constant pool index
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B13 
     * @title read integer from long field - only field with same name but 
     * different type exists
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_13");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read inaccessible field. Java throws IllegalAccessError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read field of undefined class. Java throws NoClassDefFoundError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE5() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read undefined field. Java throws NoSuchFieldError 
     * on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE6() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint n/a
     * @title Attempt to read superclass' private field from subclass. Java 
     * throws IllegalAccessError on first access but Dalvik throws VerifyError on class loading.
     */
    public void testVFE7() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_12");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title sget shall not work for reference fields
     */
    public void testVFE8() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_14");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for short fields
     */
    public void testVFE9() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_15");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for boolean fields
     */
    public void testVFE10() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_16");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for char fields
     */
    public void testVFE11() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_17");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for byte fields
     */
    public void testVFE12() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_18");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for double fields
     */
    public void testVFE13() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_19");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * 
     * @constraint B1 
     * @title sget shall not work for long fields
     */
    public void testVFE14() {
        try {
            Class.forName("dot.junit.opcodes.sget.d.T_sget_20");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}

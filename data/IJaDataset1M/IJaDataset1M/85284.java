package cheesymock;

import java.io.InputStream;
import junit.framework.*;

/**
 * @author fredrik
 */
@SuppressWarnings({ "unused", "static-access" })
public class CheesyTest extends TestCase {

    public void testCreateSimpleMockObject() throws Exception {
        Appendable a = C.mock(Appendable.class);
        assertNotNull(a);
        C.check(a);
    }

    public void testSimpleInvocation1() throws Exception {
        Simple1 o = C.mock(Simple1.class, new Object() {

            public String method1() {
                return "hej";
            }
        });
        assertEquals("hej", o.method1());
        assertNull(o.method2("hej"));
        assertNull(o.method3(1));
    }

    public void testSimpeInvocation2() throws Exception {
        Simple1 o = C.mock(Simple1.class, new D() {

            String method1() {
                return "hej";
            }
        });
        assertEquals("hej", o.method1());
    }

    public void testSimpeInvocation3() throws Exception {
        D d = new D() {

            void method4(String s) {
                assertEquals("test", s);
                state++;
            }
        };
        Simple1 o = C.mock(Simple1.class, d);
        o.method4("test");
        assertEquals(1, d.state);
    }

    public void testOverloadedInvocation1() throws Exception {
        Overloaded1 o = C.mock(Overloaded1.class, new D() {

            public String method1(String s) {
                return "str";
            }

            public String method1(int i) {
                return "int";
            }
        });
        assertEquals("str", o.method1("str"));
        assertEquals("str", o.method1((String) null));
        assertEquals("int", o.method1(0));
    }

    public void testOverloadedInvocation4() throws Exception {
        Overloaded2 o = C.mock(Overloaded2.class, new D() {

            public String method1(Object o) {
                return "obj";
            }

            public String method1(int i) {
                return "int";
            }

            public String method1(String s) {
                return "str";
            }
        });
        assertEquals("obj", o.method1(new Object()));
        assertEquals("str", o.method1("str"));
        assertEquals("str", o.method1((String) null));
        assertEquals("int", o.method1(0));
    }

    public void testOverloadedInvocation5() throws Exception {
        Overloaded3 o = C.mock(Overloaded3.class, new D() {

            public String method1(int i) {
                return "int";
            }

            public String method1(String s) {
                return "str";
            }

            public String method1(Object o) {
                return "obj";
            }
        });
        assertEquals("obj", o.method1(new Object()));
        assertEquals("str", o.method1("str"));
        assertEquals("str", o.method1((String) null));
        assertEquals("int", o.method1(0));
    }

    public void testPropertyAccess1() throws Exception {
        Properties1 o = C.mock(Properties1.class, new D() {

            String message;

            boolean valid = true;
        });
        assertNull(o.getMessage());
        o.setMessage("test!");
        assertEquals("test!", o.getMessage());
        o.setMessage(null);
        assertNull(o.getMessage());
        assertTrue(o.isValid());
    }

    public void testSimpleValidation1() throws Exception {
        try {
            Simple1 o = C.mock(Simple1.class, new D() {

                String myMethod() {
                    return null;
                }
            });
            fail();
        } catch (IllegalArgumentException iae) {
        }
    }

    public void testSimpleValidationWithCheck() throws Exception {
        Simple1 o = C.mock(Simple1.class, new Object() {

            @Ignore
            int i = 0;

            String method1() {
                i++;
                return "method1";
            }

            @Check
            void check() {
                if (i > 0) {
                    throw new IllegalStateException();
                }
            }
        });
        assertEquals("method1", o.method1());
        try {
            C.check(o);
        } catch (IllegalStateException ise) {
        }
    }

    public void testStrictValidationMissingMethod() throws Exception {
        Simple1 s = C.mockStrict(Simple1.class, new D() {

            public String method1() {
                return "method1";
            }
        });
        assertEquals("method1", s.method1());
        try {
            s.method2("method2");
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testMockAbstractClass() throws Exception {
        InputStream s = C.mockStrict(InputStream.class, new D() {

            int read() {
                return 23;
            }
        });
        assertEquals(23, s.read());
        try {
            s.close();
            fail();
        } catch (UnsupportedOperationException e) {
        }
    }

    public void testReturnValues() throws Exception {
        Return1 r = Cheesy.mock(Return1.class);
        assertEquals(null, r.method1());
        assertEquals(0, r.method2());
        assertEquals(null, r.method3());
        assertEquals(0, r.method4());
        assertEquals(null, r.method5());
        assertEquals(0.0f, r.method6());
        assertEquals(null, r.method7());
        assertEquals(0.0, r.method8());
        assertEquals(null, r.method9());
        assertEquals(0, r.method10());
        assertEquals(null, r.method11());
        assertEquals(' ', r.method12());
        assertEquals((byte) 0, r.method13());
    }

    public void testStrictMockBasicMethodsWithoutDelegate() throws Exception {
        Simple1 s = Cheesy.mockStrict(Simple1.class);
        assertTrue(s.hashCode() != 0);
        assertNotNull(s.toString());
    }

    public void testStrictEqualsWithoutDelegate() throws Exception {
        Simple1 s = Cheesy.mockStrict(Simple1.class);
        assertTrue(s.equals(s));
    }

    public void testStrictMockBasicMethodsWithDelegate() throws Exception {
        Simple1 s = Cheesy.mockStrict(Simple1.class, new Object());
        assertTrue(s.hashCode() != 0);
        assertNotNull(s.toString());
    }

    public void testStrictEqualsWithDelegate() throws Exception {
        Simple1 s = Cheesy.mockStrict(Simple1.class, new Object());
        assertTrue(s.equals(s));
    }

    public void testRegularMockAsFinalVariable() throws Exception {
        final String hello = "hello";
        final Object obj = Cheesy.mock(Comparable.class);
        Simple1 s = Cheesy.mock(Simple1.class, new Object() {

            String method2(String s) {
                assertEquals(hello, s);
                assertFalse(obj.equals(hello));
                return s;
            }
        });
        assertEquals("hello", s.method2("hello"));
    }

    public void testStrictMockAsFinalVariable() throws Exception {
        final String hello = "hello";
        final Object obj = Cheesy.mock(Comparable.class);
        Simple1 s = Cheesy.mockStrict(Simple1.class, new Object() {

            String method2(String s) {
                assertEquals(hello, s);
                assertFalse(obj.equals(hello));
                return s;
            }
        });
        assertEquals("hello", s.method2("hello"));
    }

    public void testAccessPrivateProperty() throws Exception {
        Properties1 p = Cheesy.mock(Properties1.class, new Object() {

            private String message = "hello";
        });
        assertEquals("hello", p.getMessage());
    }

    public void testPropertyAccessWithNoDelegate() throws Exception {
        Properties1 p = Cheesy.mock(Properties1.class);
        assertEquals(null, p.getMessage());
    }

    public void testUnwrapExceptionsInMock() throws Exception {
        Simple1 s = Cheesy.mock(Simple1.class, new Object() {

            String method1() {
                throw new UnsupportedOperationException();
            }
        });
        try {
            s.method1();
            fail();
        } catch (UnsupportedOperationException uoe) {
        }
    }

    public static interface Simple1 {

        public String method1();

        public String method2(String s);

        public String method3(int i);

        public void method4(String s);
    }

    public static interface Overloaded1 {

        public String method1(String s);

        public String method1(int i);
    }

    public static interface Overloaded2 {

        public String method1(String s);

        public String method1(int i);

        public String method1(Object o);
    }

    public static interface Overloaded3 {

        public String method1(Object o);

        public String method1(int i);

        public String method1(String s);
    }

    public static interface Properties1 {

        public String getMessage();

        public void setMessage(String s);

        public boolean isValid();
    }

    public static interface Return1 {

        public String method1();

        public int method2();

        public Integer method3();

        public long method4();

        public Long method5();

        public float method6();

        public Float method7();

        public double method8();

        public Double method9();

        public short method10();

        public Short method11();

        public char method12();

        public byte method13();
    }
}

package net.sf.oval.test.integration.spring;

import junit.framework.TestCase;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.exception.ConstraintsViolatedException;
import net.sf.oval.guard.Guarded;
import net.sf.oval.guard.SuppressOValWarnings;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @author Sebastian Thomschke
*/
public class SpringAOPAllianceTest extends TestCase {

    public static interface TestServiceInterface {

        @MaxLength(value = 5, message = "MAX_LENGTH")
        String getSomething(@NotNull(message = "NOT_NULL") final String input);
    }

    /**
	 * interface based service
	 */
    @Guarded(inspectInterfaces = true)
    public static class TestServiceWithInterface implements TestServiceInterface {

        public String getSomething(final String input) {
            return input;
        }
    }

    /**
	 * class based service
	 */
    public static class TestServiceWithoutInterface {

        @SuppressOValWarnings
        @MaxLength(value = 5, message = "MAX_LENGTH")
        public String getSomething(@NotNull(message = "NOT_NULL") final String input) {
            return input;
        }
    }

    public void testCGLIBProxying() {
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("SpringAOPAllianceTestCGLIBProxy.xml", SpringAOPAllianceTest.class);
        {
            final TestServiceWithoutInterface testServiceWithoutInterface = (TestServiceWithoutInterface) ctx.getBean("testServiceWithoutInterface");
            try {
                testServiceWithoutInterface.getSomething(null);
                fail();
            } catch (final ConstraintsViolatedException ex) {
                assertEquals("NOT_NULL", ex.getConstraintViolations()[0].getMessage());
            }
            try {
                testServiceWithoutInterface.getSomething("123456");
                fail();
            } catch (final ConstraintsViolatedException ex) {
                assertEquals("MAX_LENGTH", ex.getConstraintViolations()[0].getMessage());
            }
        }
        {
            final TestServiceInterface testServiceWithInterface = ctx.getBean("testServiceWithInterface", TestServiceInterface.class);
            try {
                testServiceWithInterface.getSomething(null);
                fail();
            } catch (final ConstraintsViolatedException ex) {
                assertEquals("NOT_NULL", ex.getConstraintViolations()[0].getMessage());
            }
            try {
                testServiceWithInterface.getSomething("123456");
                fail();
            } catch (final ConstraintsViolatedException ex) {
                assertEquals("MAX_LENGTH", ex.getConstraintViolations()[0].getMessage());
            }
        }
    }

    public void testJDKProxying() {
        final ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("SpringAOPAllianceTestJDKProxy.xml", SpringAOPAllianceTest.class);
        final TestServiceInterface testServiceWithInterface = ctx.getBean("testServiceWithInterface", TestServiceInterface.class);
        try {
            testServiceWithInterface.getSomething(null);
            fail();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals("NOT_NULL", ex.getConstraintViolations()[0].getMessage());
        }
        try {
            testServiceWithInterface.getSomething("123456");
            fail();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals("MAX_LENGTH", ex.getConstraintViolations()[0].getMessage());
        }
    }
}

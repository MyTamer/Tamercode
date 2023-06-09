package net.sf.oval.test.guard;

import java.math.BigDecimal;
import java.util.Date;
import junit.framework.TestCase;
import net.sf.oval.constraint.Assert;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.exception.ConstraintsViolatedException;
import net.sf.oval.guard.Guard;
import net.sf.oval.guard.Guarded;
import net.sf.oval.guard.Post;
import net.sf.oval.guard.Pre;

/**
 * @author Sebastian Thomschke
 */
public class PrePostGroovyTest extends TestCase {

    @Guarded
    public static class TestTransaction {

        @SuppressWarnings("unused")
        protected Date date;

        @SuppressWarnings("unused")
        protected String description;

        protected BigDecimal value;

        @Post(expr = "_this.valuePost != null", lang = "groovy", message = "POST")
        public BigDecimal getValuePost() {
            return value;
        }

        @Post(expr = "_this.valuePost != null && _old.valuePost != null", old = "[valuePost:_this.value]", lang = "groovy", message = "POST")
        public BigDecimal getValuePostWithOld() {
            return value;
        }

        @Pre(expr = "_this.valuePre != null", lang = "groovy", message = "PRE")
        public BigDecimal getValuePre() {
            return value;
        }

        @Pre(expr = "_this.value!=null && value2add!=null && _args[0]!=null", lang = "groovy", message = "PRE")
        public void increase1(@Assert(expr = "_value!=null", lang = "groovy", message = "ASSERT") final BigDecimal value2add) {
            value = value.add(value2add);
        }

        @Post(expr = "_this.value>_old.value", old = "[value:_this.value]", lang = "groovy", message = "POST")
        public void increase2(@NotNull final BigDecimal value2add) {
            value = value.add(value2add);
        }

        @Post(expr = "_this.value>_old.value", old = "[value:_this.value]", lang = "groovy", message = "POST")
        public void increase2buggy(@NotNull final BigDecimal value2add) {
            value = value.subtract(value2add);
        }
    }

    public void testCircularConditionsGroovy() {
        final Guard guard = new Guard();
        TestGuardAspect.aspectOf().setGuard(guard);
        final TestTransaction t = new TestTransaction();
        try {
            t.getValuePre();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "PRE");
        }
        try {
            t.getValuePost();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "POST");
        }
        try {
            t.getValuePostWithOld();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "POST");
        }
        t.value = new BigDecimal(0);
        t.getValuePre();
        t.getValuePost();
        t.getValuePostWithOld();
    }

    public void testPostGroovy() {
        final Guard guard = new Guard();
        TestGuardAspect.aspectOf().setGuard(guard);
        final TestTransaction t = new TestTransaction();
        try {
            t.value = new BigDecimal(-2);
            t.increase2buggy(new BigDecimal(1));
            fail();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "POST");
        }
        t.increase2(new BigDecimal(1));
    }

    public void testPreGroovy() {
        final Guard guard = new Guard();
        TestGuardAspect.aspectOf().setGuard(guard);
        final TestTransaction t = new TestTransaction();
        try {
            t.increase1(new BigDecimal(1));
            fail();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "PRE");
        }
        try {
            t.value = new BigDecimal(2);
            t.increase1(null);
            fail();
        } catch (final ConstraintsViolatedException ex) {
            assertEquals(ex.getConstraintViolations()[0].getMessage(), "ASSERT");
        }
        try {
            t.increase1(new BigDecimal(1));
        } catch (final ConstraintsViolatedException ex) {
            System.out.println(ex.getConstraintViolations()[0].getMessage());
        }
    }
}

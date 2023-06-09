package org.apache.shindig.protocol.conversion;

import junit.framework.TestCase;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;

/**
 * The Class BeanJsonConverterInjectedClassTest.
 */
public class BeanJsonConverterInjectedClassTest extends TestCase {

    /** The bean json converter. */
    private BeanJsonConverter beanJsonConverter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.beanJsonConverter = new BeanJsonConverter(Guice.createInjector(new TestModule()));
    }

    /**
   * Test json conversion of a TestInterface into a TestObject
   * 
   * @throws Exception the exception
   */
    public void testJsonToObject() throws Exception {
        String json = "{x:'xValue',y:'yValue'}";
        TestObject object = (TestObject) beanJsonConverter.convertToObject(json, TestInterface.class);
        assertNotNull("expected 'x' field not set after json conversion", object.getX());
        assertNotNull("expected 'y' field not set after json conversion", object.getY());
    }

    /**
   * TestModule that binds TestObject to TestInterface
   */
    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(TestInterface.class).to(TestObject.class);
        }
    }

    /**
   * TestInterface.
   */
    public interface TestInterface {

        public String getX();

        public void setX(String x);
    }

    /**
   * TestObject.
   */
    public static class TestObject implements TestInterface {

        private String x;

        private String y;

        public String getX() {
            return x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y;
        }

        public void setY(String y) {
            this.y = y;
        }
    }
}

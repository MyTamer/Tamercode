package org.powermock.api.mockito.internal.configuration;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import java.lang.reflect.Field;

/**
 * More or less a copy of the {@link SpyAnnotationEngine} but it uses
 * {@link PowerMockito#spy(Object)} instead.
 */
public class PowerMockitoSpyAnnotationEngine extends SpyAnnotationEngine {

    @SuppressWarnings("deprecation")
    @Override
    public void process(Class<?> context, Object testClass) {
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                try {
                    Whitebox.invokeMethod(this, Spy.class, field, new Class<?>[] { Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class });
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
                boolean wasAccessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Object instance = field.get(testClass);
                    if (instance == null) {
                        throw new MockitoException("Cannot create a @Spy for '" + field.getName() + "' field because the *instance* is missing\n" + "Example of correct usage of @Spy:\n" + "   @Spy List mock = new LinkedList();\n");
                    }
                    field.set(testClass, PowerMockito.spy(instance));
                } catch (IllegalAccessException e) {
                    throw new MockitoException("Problems initiating spied field " + field.getName(), e);
                } finally {
                    field.setAccessible(wasAccessible);
                }
            }
        }
    }
}

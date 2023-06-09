package org.powermock.reflect.internal.matcherstrategies;

import org.powermock.reflect.exceptions.FieldNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class FieldAnnotationMatcherStrategy extends FieldMatcherStrategy {

    final Class<? extends Annotation>[] annotations;

    public FieldAnnotationMatcherStrategy(Class<? extends Annotation>[] annotations) {
        if (annotations == null || annotations.length == 0) {
            throw new IllegalArgumentException("You must specify atleast one annotation.");
        }
        this.annotations = annotations;
    }

    @Override
    public boolean matches(Field field) {
        for (Class<? extends Annotation> annotation : annotations) {
            if (field.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notFound(Class<?> type, boolean isInstanceField) throws FieldNotFoundException {
        throw new FieldNotFoundException("No field that has any of the annotation types \"" + getAnnotationNames() + "\" could be found in the class hierarchy of " + type.getName() + ".");
    }

    @Override
    public String toString() {
        return "annotations " + getAnnotationNames();
    }

    private String getAnnotationNames() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < annotations.length; i++) {
            builder.append(annotations[i].getName());
            if (i != annotations.length - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}

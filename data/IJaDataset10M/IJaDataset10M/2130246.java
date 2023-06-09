package org.xmappr.converters;

/**
 * @author peter
 */
public class FloatConverter extends ValueConverter {

    public boolean canConvert(Class type) {
        return type.equals(float.class) || Float.class.isAssignableFrom(type);
    }

    public Object fromValue(String value, String format, Class targetType, Object targetObject) {
        return Float.valueOf(value);
    }

    public String toValue(Object object, String format) {
        return ((Float) object).toString();
    }
}

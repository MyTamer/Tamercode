package com.totsp.gwt.freezedry.rebind;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import java.util.Locale;

class Shared {

    /**
   * Property used to control whether or not the RPC system will enforce the
   * versioning scheme or not.
   */
    static final String RPC_PROP_ENFORCE_TYPE_VERSIONING = "gwt.enforceRPCTypeVersioning";

    /**
   * Capitalizes a name.
   * 
   * @param name the string to be capitalized
   * @return the capitalized string
   */
    static String capitalize(String name) {
        return name.substring(0, 1).toUpperCase(Locale.US) + name.substring(1);
    }

    /**
   * Gets the suffix needed to make a call for a particular type. For example,
   * the <code>int</code> class needs methods named "readInt" and "writeInt".
   * 
   * @param type the type in question
   * @return the suffix of the method to call
   */
    static String getCallSuffix(JType type) {
        JParameterizedType isParameterized = type.isParameterized();
        if (isParameterized != null) {
            return getCallSuffix(isParameterized.getRawType());
        } else if (type.isPrimitive() != null) {
            if (type == JPrimitiveType.BOOLEAN) {
                return "Boolean";
            } else if (type == JPrimitiveType.BYTE) {
                return "Byte";
            } else if (type == JPrimitiveType.CHAR) {
                return "Char";
            } else if (type == JPrimitiveType.DOUBLE) {
                return "Double";
            } else if (type == JPrimitiveType.FLOAT) {
                return "Float";
            } else if (type == JPrimitiveType.INT) {
                return "Int";
            } else if (type == JPrimitiveType.LONG) {
                return "Long";
            } else if (type == JPrimitiveType.SHORT) {
                return "Short";
            } else {
                return null;
            }
        } else if (type.getQualifiedSourceName().equals("java.lang.String")) {
            return "String";
        } else {
            return "Object";
        }
    }

    /**
   * Returns <code>true</code> if the generated code should enforce type
   * versioning.
   */
    static boolean shouldEnforceTypeVersioning(TreeLogger logger, PropertyOracle propertyOracle) {
        try {
            String propVal = propertyOracle.getPropertyValue(logger, RPC_PROP_ENFORCE_TYPE_VERSIONING);
            if (propVal.equals("false")) {
                return false;
            }
        } catch (BadPropertyValueException e) {
        }
        return true;
    }

    /**
   * Computes a good name for a class related to the specified type, such that
   * the computed name can be a top-level class in the same package as the
   * specified type.
   * 
   * <p>
   * This method does not currently check for collisions between the synthesized
   * name and an existing top-level type in the same package. It is actually
   * tricky to do so, because on subsequent runs, we'll view our own generated
   * classes as collisions. There's probably some trick we can use in the future
   * to make it totally bulletproof.
   * </p>
   * 
   * @param type the name of the base type, whose name will be built upon to
   *          synthesize a new type name
   * @param suffix a suffix to be used to make the new synthesized type name
   * @return an array of length 2 such that the first element is the package
   *         name and the second element is the synthesized class name
   */
    static String[] synthesizeTopLevelClassName(JClassType type, String suffix) {
        String className = type.getName();
        className += suffix;
        className = className.replace('.', '_');
        String packageName = type.getPackage().getName();
        return new String[] { packageName, className };
    }

    /**
   * Determines whether a particular type needs to be cast to become its final
   * type. Primitives and Strings do not, as they are read directly as the
   * correct type. All other Objects need a cast, except for Object itself.
   * 
   * @param type the type in question
   * @return <code>true</code> if the results of a read method must be cast,
   *         otherwise <code>false</code>.
   */
    static boolean typeNeedsCast(JType type) {
        return type.isPrimitive() == null && !type.getQualifiedSourceName().equals("java.lang.String") && !type.getQualifiedSourceName().equals("java.lang.Object");
    }
}

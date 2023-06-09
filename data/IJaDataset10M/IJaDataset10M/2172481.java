package com.google.gwt.junit;

import com.google.gwt.dev.cfg.BindingProperty;
import com.google.gwt.dev.cfg.ModuleDef;
import com.google.gwt.dev.cfg.Properties;
import com.google.gwt.dev.util.Util;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.junit.client.WithProperties;
import com.google.gwt.junit.client.WithProperties.Property;
import junit.framework.TestCase;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * A {@link JUnitShell.Strategy} that will alter the module the tests are run
 * in by defining module properties as requested by annotations on the tests.
 */
public class PropertyDefiningStrategy extends GWTTestCase.BaseStrategy {

    private TestCase testCase;

    private Set<Property> properties;

    public PropertyDefiningStrategy(TestCase testCase) {
        this.testCase = testCase;
    }

    protected String getBaseModuleExtension() {
        return super.getSyntheticModuleExtension();
    }

    @Override
    public String getSyntheticModuleExtension() {
        String extension = getBaseModuleExtension();
        computePropertiesMap();
        if (properties.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Property property : properties) {
                sb.append(".").append(property.name()).append("$").append(property.value());
            }
            extension += sb.toString();
        }
        return extension;
    }

    @Override
    public void processModule(ModuleDef module) {
        super.processModule(module);
        computePropertiesMap();
        if (properties.size() > 0) {
            Properties props = module.getProperties();
            for (Property property : properties) {
                BindingProperty binding = props.createBinding(property.name());
                if (!binding.isDefinedValue(property.value())) {
                    binding.addDefinedValue(binding.getRootCondition(), property.value());
                }
                binding.setAllowedValues(binding.getRootCondition(), property.value());
            }
        }
    }

    private Property checkProperty(Property property) {
        String[] tokens = (property.name() + ". ").split("\\.");
        for (int i = 0; i < tokens.length - 1; i++) {
            if (!Util.isValidJavaIdent(tokens[i])) {
                throw new AssertionError("Property name invalid: " + property.name());
            }
        }
        if (!Util.isValidJavaIdent(property.value())) {
            throw new AssertionError("Property value invalid: " + property.value());
        }
        return property;
    }

    private void computePropertiesMap() {
        if (properties == null) {
            Set<Property> props = new TreeSet<Property>(new Comparator<Property>() {

                public int compare(Property p1, Property p2) {
                    int r = p1.name().compareTo(p2.name());
                    if (r == 0) {
                        r = p1.value().compareTo(p2.value());
                    }
                    return r;
                }
            });
            try {
                String name = testCase.getName();
                if (name != null) {
                    Method testMethod = testCase.getClass().getMethod(testCase.getName());
                    if (testMethod.isAnnotationPresent(WithProperties.class)) {
                        WithProperties annotation = testMethod.getAnnotation(WithProperties.class);
                        for (Property property : annotation.value()) {
                            props.add(checkProperty(property));
                        }
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            properties = props;
        }
    }
}

package com.thoughtworks.xstream.tools.benchmark.metrics;

import com.thoughtworks.xstream.tools.benchmark.Metric;
import com.thoughtworks.xstream.tools.benchmark.Product;
import com.thoughtworks.xstream.tools.benchmark.Target;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * Determines how long it takes to deserialize an object (in ms).
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 * @see com.thoughtworks.xstream.tools.benchmark.Harness
 * @see Metric
 */
public class DeserializationSpeedMetric implements Metric {

    private final int iterations;

    private final boolean validate;

    /**
     * Measure deserialization speed.
     * 
     * @param iterations
     * @deprecated since 1.3, use {@link #DeserializationSpeedMetric(int, boolean)}
     */
    public DeserializationSpeedMetric(int iterations) {
        this(iterations, false);
    }

    /**
     * Measure deserialization speed.
     * @param iterations 
     * @param validate flag to compare result of last iteration with original data
     * @since 1.3
     */
    public DeserializationSpeedMetric(int iterations, boolean validate) {
        this.iterations = iterations;
        this.validate = validate;
    }

    public double run(Product product, Target target) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        product.serialize(target.target(), output);
        byte[] data = output.toByteArray();
        product.deserialize(new ByteArrayInputStream(data));
        Object lastResult = null;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            lastResult = product.deserialize(new ByteArrayInputStream(data));
        }
        long end = System.currentTimeMillis();
        if (validate && iterations > 0) {
            if (!target.isEqual(lastResult)) {
                throw new RuntimeException("Deserialized object is not equal");
            }
        }
        return (end - start);
    }

    /**
     *@deprecated since 1.3
     */
    public double run(Product product, Object object) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        product.serialize(object, output);
        byte[] data = output.toByteArray();
        product.deserialize(new ByteArrayInputStream(data));
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            product.deserialize(new ByteArrayInputStream(data));
        }
        long end = System.currentTimeMillis();
        return (end - start);
    }

    public String unit() {
        return "ms";
    }

    public boolean biggerIsBetter() {
        return false;
    }

    public String toString() {
        return "Deserialization speed (" + iterations + " iteration" + (iterations == 1 ? "" : "s") + ")";
    }
}

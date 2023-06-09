package org.apache.tapestry5.ioc;

/**
 * Object passed into a service contributor method that allows the method provide contributed values to the service's
 * configuration.
 * <p/>
 * A service can <em>collect</em> contributions in three different ways: <ul> <li>As an un-ordered collection of
 * values</li> <li>As an ordered list of values (where each value has a unique id, pre-requisites and
 * post-requisites)</li> <li>As a map of keys and values </ul>
 * <p/>
 * The service defines the <em>type</em> of contribution, in terms of a base class or service interface. Contributions
 * must be compatible with the type.
 */
public interface MappedConfiguration<K, V> {

    /**
     * Adds a keyed object to the service's contribution.
     *
     * @param key   unique id for the value
     * @param value to contribute
     * @throws IllegalArgumentException if key is not unique
     */
    void add(K key, V value);

    /**
     * Overrides an existing contribution by its key.
     *
     * @param key   unique id of value to override
     * @param value new value, or null to remove the key entirely
     * @since 5.1.0.0
     */
    void override(K key, V value);

    /**
     * Adds a keyed object as an instantiated instance (with dependencies injected) of a class.
     *
     * @param key   unique id for the value
     * @param clazz class to instantiate and contribute
     * @since 5.1.0.0
     */
    void addInstance(K key, Class<? extends V> clazz);

    /**
     * Overrides an existing contribution with a new instance.
     *
     * @param key   unique id of value to override
     * @param clazz class to instantiate as override
     */
    void overrideInstance(K key, Class<? extends V> clazz);
}

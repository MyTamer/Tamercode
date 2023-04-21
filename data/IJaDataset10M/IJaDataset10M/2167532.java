package com.volantis.mcs.xml.schema.model;

/**
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface SchemaNamespaces {

    ElementType getElementType(String namespaceURI, String localName) throws IllegalArgumentException;
}
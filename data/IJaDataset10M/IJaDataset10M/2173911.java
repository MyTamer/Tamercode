package org.sventon.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * FileRevision.
 *
 * @author jorgen@sventon.org
 * @author jesper@sventon.org
 */
public class FileRevision extends PathRevision implements Serializable {

    private static final long serialVersionUID = -308367920544609564L;

    /**
   * Map holding the RevisionProperties and the corresponding (String) value.
   */
    private final Map<RevisionProperty, String> properties = new HashMap<RevisionProperty, String>();

    /**
   * Constructor.
   *
   * @param path     Path.
   * @param revision Revision.
   */
    public FileRevision(final String path, final Revision revision) {
        super(path, revision);
    }

    /**
   * Adds a revision property.
   *
   * @param property      Revision property to add.
   * @param propertyValue Value of revision property.
   */
    public void addProperty(final RevisionProperty property, final String propertyValue) {
        properties.put(property, propertyValue);
    }

    /**
   * @param property Revision property key to get value for.
   * @return Value of revision property
   */
    public String getProperty(final RevisionProperty property) {
        return properties.get(property);
    }

    @Override
    public String toString() {
        return super.toString() + " " + ToStringBuilder.reflectionToString(this);
    }
}

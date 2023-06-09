package wicket.examples.displaytag.export;

import org.apache.commons.collections.iterators.ArrayIterator;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.Iterator;

/**
 * Enumeration for media types.
 *
 * @author Fabrizio Giustina
 * @version $Revision: 5394 $ ($Author: jdonnerstag $)
 */
public final class MediaTypeEnum {

    /**
     * Media type CSV = 1.
     */
    public static final MediaTypeEnum CSV = new MediaTypeEnum(1, "csv");

    /**
     * media type EXCEL = 2.
     */
    public static final MediaTypeEnum EXCEL = new MediaTypeEnum(2, "excel");

    /**
     * media type XML = 3.
     */
    public static final MediaTypeEnum XML = new MediaTypeEnum(3, "xml");

    /**
     * media type HTML = 4.
     */
    public static final MediaTypeEnum HTML = new MediaTypeEnum(4, "html");

    /**
     * array containing all the export types.
     */
    public static final MediaTypeEnum[] ALL = { EXCEL, XML, CSV, HTML };

    /**
     * Code; this is the primary key for these objects.
     */
    private final int enumCode;

    /**
     * description.
     */
    private final String enumName;

    /**
     * private constructor. Use only constants.
     *
     * @param code int code
     * @param name description of media type
     */
    private MediaTypeEnum(int code, String name) {
        this.enumCode = code;
        this.enumName = name;
    }

    /**
     * returns the int code.
     *
     * @return int code
     */
    public int getCode() {
        return this.enumCode;
    }

    /**
     * returns the description.
     *
     * @return String description of the media type ("excel", "xml", "csv",
     *         "html")
     */
    public String getName() {
        return this.enumName;
    }

    /**
     * lookup a media type by key.
     *
     * @param key int code
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromCode(int key) {
        for (int i = 0; i < ALL.length; i++) {
            if (key == ALL[i].getCode()) {
                return ALL[i];
            }
        }
        return null;
    }

    /**
     * lookup a media type by an Integer key.
     *
     * @param key Integer code - null safe: a null key returns a null Enum
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromIntegerCode(Integer key) {
        if (key == null) {
            return null;
        } else {
            return fromCode(key.intValue());
        }
    }

    /**
     * Lookup a media type by a String key.
     *
     * @param code String code - null safe: a null key returns a null Enum
     * @return MediaTypeEnum or null if no mediaType is found with the given key
     */
    public static MediaTypeEnum fromName(String code) {
        for (int i = 0; i < ALL.length; i++) {
            if (ALL[i].getName().equals(code)) {
                return ALL[i];
            }
        }
        return null;
    }

    /**
     * returns an iterator on all the media type.
     *
     * @return iterator
     */
    public static Iterator iterator() {
        return new ArrayIterator(ALL);
    }

    /**
     * returns the media type description.
     *
     * @see Object#toString()
     */
    public String toString() {
        return getName();
    }

    /**
     * Only a single instance of a specific MediaTypeEnum can be created, so we
     * can check using ==.
     *
     * @param o the object to compare to
     * @return hashCode
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder(1188997057, -1289297553).append(this.enumCode).toHashCode();
    }
}

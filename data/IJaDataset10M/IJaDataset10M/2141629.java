package org.jfree.chart.text;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Used to indicate the position of an anchor point for a text string.  This is
 * frequently used to align a string to a fixed point in some coordinate space.
 */
public final class TextAnchor implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 8219158940496719660L;

    /** Top/left. */
    public static final TextAnchor TOP_LEFT = new TextAnchor("TextAnchor.TOP_LEFT");

    /** Top/center. */
    public static final TextAnchor TOP_CENTER = new TextAnchor("TextAnchor.TOP_CENTER");

    /** Top/right. */
    public static final TextAnchor TOP_RIGHT = new TextAnchor("TextAnchor.TOP_RIGHT");

    /** Half-ascent/left. */
    public static final TextAnchor HALF_ASCENT_LEFT = new TextAnchor("TextAnchor.HALF_ASCENT_LEFT");

    /** Half-ascent/center. */
    public static final TextAnchor HALF_ASCENT_CENTER = new TextAnchor("TextAnchor.HALF_ASCENT_CENTER");

    /** Half-ascent/right. */
    public static final TextAnchor HALF_ASCENT_RIGHT = new TextAnchor("TextAnchor.HALF_ASCENT_RIGHT");

    /** Middle/left. */
    public static final TextAnchor CENTER_LEFT = new TextAnchor("TextAnchor.CENTER_LEFT");

    /** Middle/center. */
    public static final TextAnchor CENTER = new TextAnchor("TextAnchor.CENTER");

    /** Middle/right. */
    public static final TextAnchor CENTER_RIGHT = new TextAnchor("TextAnchor.CENTER_RIGHT");

    /** Baseline/left. */
    public static final TextAnchor BASELINE_LEFT = new TextAnchor("TextAnchor.BASELINE_LEFT");

    /** Baseline/center. */
    public static final TextAnchor BASELINE_CENTER = new TextAnchor("TextAnchor.BASELINE_CENTER");

    /** Baseline/right. */
    public static final TextAnchor BASELINE_RIGHT = new TextAnchor("TextAnchor.BASELINE_RIGHT");

    /** Bottom/left. */
    public static final TextAnchor BOTTOM_LEFT = new TextAnchor("TextAnchor.BOTTOM_LEFT");

    /** Bottom/center. */
    public static final TextAnchor BOTTOM_CENTER = new TextAnchor("TextAnchor.BOTTOM_CENTER");

    /** Bottom/right. */
    public static final TextAnchor BOTTOM_RIGHT = new TextAnchor("TextAnchor.BOTTOM_RIGHT");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private TextAnchor(String name) {
        this.name = name;
    }

    /**
     * Returns a string representing the object.
     *
     * @return The string.
     */
    public String toString() {
        return this.name;
    }

    /**
     * Returns <code>true</code> if this object is equal to the specified
     * object, and <code>false</code> otherwise.
     *
     * @param obj  the other object.
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TextAnchor)) {
            return false;
        }
        TextAnchor order = (TextAnchor) obj;
        if (!this.name.equals(order.name)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return The hashcode
     */
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Ensures that serialization returns the unique instances.
     *
     * @return The object.
     *
     * @throws ObjectStreamException if there is a problem.
     */
    private Object readResolve() throws ObjectStreamException {
        TextAnchor result = null;
        if (this.equals(TextAnchor.TOP_LEFT)) {
            result = TextAnchor.TOP_LEFT;
        } else if (this.equals(TextAnchor.TOP_CENTER)) {
            result = TextAnchor.TOP_CENTER;
        } else if (this.equals(TextAnchor.TOP_RIGHT)) {
            result = TextAnchor.TOP_RIGHT;
        } else if (this.equals(TextAnchor.BOTTOM_LEFT)) {
            result = TextAnchor.BOTTOM_LEFT;
        } else if (this.equals(TextAnchor.BOTTOM_CENTER)) {
            result = TextAnchor.BOTTOM_CENTER;
        } else if (this.equals(TextAnchor.BOTTOM_RIGHT)) {
            result = TextAnchor.BOTTOM_RIGHT;
        } else if (this.equals(TextAnchor.BASELINE_LEFT)) {
            result = TextAnchor.BASELINE_LEFT;
        } else if (this.equals(TextAnchor.BASELINE_CENTER)) {
            result = TextAnchor.BASELINE_CENTER;
        } else if (this.equals(TextAnchor.BASELINE_RIGHT)) {
            result = TextAnchor.BASELINE_RIGHT;
        } else if (this.equals(TextAnchor.CENTER_LEFT)) {
            result = TextAnchor.CENTER_LEFT;
        } else if (this.equals(TextAnchor.CENTER)) {
            result = TextAnchor.CENTER;
        } else if (this.equals(TextAnchor.CENTER_RIGHT)) {
            result = TextAnchor.CENTER_RIGHT;
        } else if (this.equals(TextAnchor.HALF_ASCENT_LEFT)) {
            result = TextAnchor.HALF_ASCENT_LEFT;
        } else if (this.equals(TextAnchor.HALF_ASCENT_CENTER)) {
            result = TextAnchor.HALF_ASCENT_CENTER;
        } else if (this.equals(TextAnchor.HALF_ASCENT_RIGHT)) {
            result = TextAnchor.HALF_ASCENT_RIGHT;
        }
        return result;
    }
}

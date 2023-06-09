package org.knopflerfish.util.framework;

import org.osgi.framework.Version;

/**
 * Class representing OSGi version ranges.
 *
 * @author Jan Stein
 */
public class VersionRange implements Comparable {

    private final Version low;

    private final Version high;

    private final boolean lowIncluded;

    private final boolean highIncluded;

    /**
   * The empty version range "[0.0.0,inf)".
   */
    public static final VersionRange defaultVersionRange = new VersionRange();

    /**
   * Construct a VersionRange object.
   * Format for a range:
   *   ( "(" | "[" ) LOW_VERSION ","  HIGH_VERSION ( ")" | "]" )
   * Format for at least a version:
   *   VERSION
   *
   * @param vr Input string.
   */
    public VersionRange(String vr) throws NumberFormatException {
        boolean op = vr.startsWith("(");
        boolean ob = vr.startsWith("[");
        if (op || ob) {
            boolean cp = vr.endsWith(")");
            boolean cb = vr.endsWith("]");
            int comma = vr.indexOf(',');
            if (comma > 0 && (cp || cb)) {
                low = new Version(vr.substring(1, comma).trim());
                high = new Version(vr.substring(comma + 1, vr.length() - 1).trim());
                lowIncluded = ob;
                highIncluded = cb;
            } else {
                throw new NumberFormatException("Illegal version range: " + vr);
            }
        } else {
            low = new Version(vr);
            high = null;
            lowIncluded = true;
            highIncluded = false;
        }
    }

    /**
   * Construct the default VersionRange object.
   *
   */
    protected VersionRange() {
        low = Version.emptyVersion;
        high = null;
        lowIncluded = true;
        highIncluded = false;
    }

    public boolean isSpecified() {
        return this != defaultVersionRange;
    }

    /**
   * Check if specified version is within our range.
   *
   * @param ver Version to compare to.
   * @return Return true if within range, otherwise false.
   */
    public boolean withinRange(Version ver) {
        if (this == defaultVersionRange) {
            return true;
        }
        int c = low.compareTo(ver);
        if (c < 0 || (c == 0 && lowIncluded)) {
            if (high == null) {
                return true;
            }
            c = high.compareTo(ver);
            return c > 0 || (c == 0 && highIncluded);
        }
        return false;
    }

    /**
   * Check if objects range is within another VersionRange.
   *
   * @param range VersionRange to compare to.
   * @return Return true if within range, otherwise false.
   */
    public boolean withinRange(VersionRange range) {
        if (this == range) {
            return true;
        }
        int c = low.compareTo(range.low);
        if (c < 0 || (c == 0 && lowIncluded == range.lowIncluded)) {
            if (high == null) {
                return true;
            }
            c = high.compareTo(range.high);
            return c > 0 || (c == 0 && highIncluded == range.highIncluded);
        }
        return false;
    }

    /**
   * Compare object to another VersionRange. VersionRanges are compared on the
   * lower bound.
   *
   * @param obj VersionRange to compare to.
   * @return Return 0 if equals, negative if this object is less than obj
   *         and positive if this object is larger then obj.
   * @exception ClassCastException if object is not a VersionRange object.
   */
    public int compareTo(Object obj) throws ClassCastException {
        VersionRange o = (VersionRange) obj;
        return low.compareTo(o.low);
    }

    /**
   * String with version number. If version is not specified return
   * an empty string.
   *
   * @return String.
   */
    public String toString() {
        if (high != null) {
            StringBuffer res = new StringBuffer();
            if (lowIncluded) {
                res.append('[');
            } else {
                res.append('(');
            }
            res.append(low.toString());
            res.append(',');
            res.append(high.toString());
            if (highIncluded) {
                res.append(']');
            } else {
                res.append(')');
            }
            return res.toString();
        } else {
            return low.toString();
        }
    }

    /**
   * Check if object is equal to this object.
   *
   * @param obj Package entry to compare to.
   * @return true if equal, otherwise false.
   */
    public boolean equals(Object obj) throws ClassCastException {
        VersionRange o = (VersionRange) obj;
        if (low.equals(o.low)) {
            if (high != null) {
                return high.equals(o.high) && lowIncluded == o.lowIncluded && highIncluded == o.highIncluded;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
   * Hash code for this package entry.
   *
   * @return int value.
   */
    public int hashCode() {
        if (high != null) {
            return low.hashCode() + high.hashCode();
        } else {
            return low.hashCode();
        }
    }
}

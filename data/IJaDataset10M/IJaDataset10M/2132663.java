package com.vividsolutions.jts.operation.buffer;

/**
 * Contains the parameters which 
 * describe how a buffer should be constructed.
 * 
 * @author Martin Davis
 *
 */
public class BufferParameters {

    /**
   * Specifies a round line buffer end cap style.
   */
    public static final int CAP_ROUND = 1;

    /**
   * Specifies a flat line buffer end cap style.
   */
    public static final int CAP_FLAT = 2;

    /**
   * Specifies a square line buffer end cap style.
   */
    public static final int CAP_SQUARE = 3;

    /**
   * Specifies a round join style.
   */
    public static final int JOIN_ROUND = 1;

    /**
   * Specifies a mitre join style.
   */
    public static final int JOIN_MITRE = 2;

    /**
   * Specifies a bevel join style.
   */
    public static final int JOIN_BEVEL = 3;

    /**
   * The default number of facets into which to divide a fillet of 90 degrees.
   * A value of 8 gives less than 2% max error in the buffer distance.
   * For a max error of < 1%, use QS = 12.
   * For a max error of < 0.1%, use QS = 18.
   */
    public static final int DEFAULT_QUADRANT_SEGMENTS = 8;

    /**
   * The default mitre limit
   * Allows fairly pointy mitres.
   */
    public static final double DEFAULT_MITRE_LIMIT = 5.0;

    private int quadrantSegments = DEFAULT_QUADRANT_SEGMENTS;

    private int endCapStyle = CAP_ROUND;

    private int joinStyle = JOIN_ROUND;

    private double mitreLimit = DEFAULT_MITRE_LIMIT;

    /**
   * Creates a default set of parameters
   *
   */
    public BufferParameters() {
    }

    /**
   * Creates a set of parameters with the
   * given quadrantSegments value.
   * 
   * @param quadrantSegments the number of quadrant segments to use
   */
    public BufferParameters(int quadrantSegments) {
        setQuadrantSegments(quadrantSegments);
    }

    /**
   * Creates a set of parameters with the
   * given quadrantSegments and endCapStyle values.
   * 
   * @param quadrantSegments the number of quadrant segments to use
   * @param endCapStyle the end cap style to use
   */
    public BufferParameters(int quadrantSegments, int endCapStyle) {
        setQuadrantSegments(quadrantSegments);
        setEndCapStyle(endCapStyle);
    }

    /**
   * Creates a set of parameters with the
   * given parameter values.
   * 
   * @param quadrantSegments the number of quadrant segments to use
   * @param endCapStyle the end cap style to use
   * @param joinStyle the join style to use
   * @param mitreLimit the mitre limit to use
   */
    public BufferParameters(int quadrantSegments, int endCapStyle, int joinStyle, double mitreLimit) {
        setQuadrantSegments(quadrantSegments);
        setEndCapStyle(endCapStyle);
        setJoinStyle(joinStyle);
        setMitreLimit(mitreLimit);
    }

    /**
   * Gets the number of quadrant segments which will be used
   * 
   * @return the number of quadrant segments
   */
    public int getQuadrantSegments() {
        return quadrantSegments;
    }

    /**
   * Sets the number of line segments used to approximate an angle fillet.
   * <ul>
   * <li>If <tt>quadSegs</tt> >= 1, joins are round, and <tt>quadSegs</tt> indicates the number of 
   * segments to use to approximate a quarter-circle.
   * <li>If <tt>quadSegs</tt> = 0, joins are bevelled (flat)
   * <li>If <tt>quadSegs</tt> < 0, joins are mitred, and the value of qs
   * indicates the mitre ration limit as
   * <pre>
   * mitreLimit = |<tt>quadSegs</tt>|
   * </pre>
   * </ul>
   * For round joins, <tt>quadSegs</tt> determines the maximum
   * error in the approximation to the true buffer curve.
   * The default value of 8 gives less than 2% max error in the buffer distance.
   * For a max error of < 1%, use QS = 12.
   * For a max error of < 0.1%, use QS = 18.
   * The error is always less than the buffer distance 
   * (in other words, the computed buffer curve is always inside the true
   * curve).
   * 
   * @param quadrantSegments the number of segments in a fillet for a quadrant
   */
    public void setQuadrantSegments(int quadSegs) {
        quadrantSegments = quadSegs;
        if (quadrantSegments == 0) joinStyle = JOIN_BEVEL;
        if (quadrantSegments < 0) {
            joinStyle = JOIN_MITRE;
            mitreLimit = Math.abs(quadrantSegments);
        }
        if (quadSegs <= 0) {
            quadrantSegments = 1;
        }
        if (joinStyle != JOIN_ROUND) {
            quadrantSegments = DEFAULT_QUADRANT_SEGMENTS;
        }
    }

    /**
   * Computes the maximum distance error due to a given level
   * of approximation to a true arc.
   * 
   * @param quadSegs the number of segments used to approximate a quarter-circle
   * @return the error of approximation
   */
    public static double bufferDistanceError(int quadSegs) {
        double alpha = Math.PI / 2.0 / quadSegs;
        return 1 - Math.cos(alpha / 2.0);
    }

    /**
   * Gets the end cap style.
   * 
   * @return the end cap style
   */
    public int getEndCapStyle() {
        return endCapStyle;
    }

    /**
   * Specifies the end cap style of the generated buffer.
   * The styles supported are {@link #CAP_ROUND}, {@link #CAP_BUTT}, and {@link #CAP_SQUARE}.
   * The default is CAP_ROUND.
   *
   * @param endCapStyle the end cap style to specify
   */
    public void setEndCapStyle(int endCapStyle) {
        this.endCapStyle = endCapStyle;
    }

    /**
   * Gets the join style
   * 
   * @return the join style code
   */
    public int getJoinStyle() {
        return joinStyle;
    }

    /**
   * Sets the join style for outside (reflex) corners between line segments.
   * Allowable values are {@link JOIN_ROUND} (which is the default),
   * {@link JOIN_MITRE} and {link JOIN_BEVEL}.
   * 
   * @param joinStyle the code for the join style
   */
    public void setJoinStyle(int joinStyle) {
        this.joinStyle = joinStyle;
    }

    /**
   * Gets the mitre ratio limit.
   * 
   * @return the limit value
   */
    public double getMitreLimit() {
        return mitreLimit;
    }

    /**
   * Sets the limit on the mitre ratio used for very sharp corners.
   * The mitre ratio is the ratio of the distance from the corner
   * to the end of the mitred offset corner.
   * When two line segments meet at a sharp angle, 
   * a miter join will extend far beyond the original geometry.
   * (and in the extreme case will be infinitely far.)
   * To prevent unreasonable geometry, the mitre limit 
   * allows controlling the maximum length of the join corner.
   * Corners with a ratio which exceed the limit will be beveled.
   *
   * @param mitreLimit the mitre ratio limit
   */
    public void setMitreLimit(double mitreLimit) {
        this.mitreLimit = mitreLimit;
    }
}

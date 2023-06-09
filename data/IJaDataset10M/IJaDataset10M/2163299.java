package javax.media.j3d;

import java.util.Enumeration;

/**
 * PathInterpolator behavior.  This class defines the base class for
 * all Path Interpolators.  Subclasses have access to the
 * computePathInterpolation() method, which computes the
 * currentInterpolationValue given the current time and alpha.
 * The method also computes the currentKnotIndex, which is based on
 * the currentInterpolationValue.
 * The currentInterpolationValue is calculated
 * by linearly interpolating among a series of predefined knots
 * (using the value generated by the specified Alpha object). 
 * The first knot must have a value of 0.0 and the last knot must have a
 * value of 1.0.  An intermediate knot with index k must have a value
 * strictly greater than any knot with index less than k.
 */
public abstract class PathInterpolator extends TransformInterpolator {

    private float knots[];

    /**
     * This value is the ratio between knot values indicated by
     * the currentKnotIndex variable.  So if a subclass wanted to
     * interpolate between knot values, it would use the currentKnotIndex
     * to get the bounding knots for the "real" value, then use the
     * currentInterpolationValue to interpolate between the knots.
     * To calculate this variable, a subclass needs to call
     * the <code>computePathInterpolation(alphaValue)</code> method from the subclass's
     * computeTransform() method.  Then this variable will hold a valid
     * value which can be used in further calculations by the subclass.
     */
    protected float currentInterpolationValue;

    /**
     * This value is the index of the current base knot value, as
     * determined by the alpha function.  A subclass wishing to
     * interpolate between bounding knots would use this index and
     * the one following it, and would use the currentInterpolationValue
     * variable as the ratio between these indices.
     * To calculate this variable, a subclass needs to call
     * the <code>computePathInterpolation(alphaValue)</code> method from the subclass's
     * computeTransform() method.  Then this variable will hold a valid
     * value which can be used in further calculations by the subclass.
     */
    protected int currentKnotIndex;

    /**
     * Constructs a PathInterpolator node with a null alpha value and
     * a null target of TransformGroup
     *
     * since Java 3D 1.3
     */
    PathInterpolator() {
    }

    /**
     * Constructs a new PathInterpolator object that interpolates
     * between the knot values in the knots array.  The array of knots
     * is copied into this PathInterpolator object.
     * @param alpha the alpha object for this interpolator.
     * @param knots an array of knot values that specify interpolation
     * points.
     *
     * @deprecated As of Java 3D version 1.3, replaced by
     * <code>PathInterpolator(Alpha, TransformGroup, float[]) </code>
     */
    public PathInterpolator(Alpha alpha, float[] knots) {
        this(alpha, null, knots);
    }

    /**
     * Constructs a new PathInterpolator object that interpolates
     * between the knot values in the knots array.  The array of knots
     * is copied into this PathInterpolator object.
     * @param alpha the alpha object for this interpolator.
     * @param target the transformgroup node effected by this pathInterpolator
     * @param knots an array of knot values that specify interpolation
     * points.
     *
     * @since Java 3D 1.3
     */
    public PathInterpolator(Alpha alpha, TransformGroup target, float[] knots) {
        super(alpha, target);
        setKnots(knots);
    }

    /**
     * Constructs a new PathInterpolator object that interpolates
     * between the knot values in the knots array.  The array of knots
     * is copied into this PathInterpolator object.
     * @param alpha the alpha object for this interpolator.
     * @param target the transform node effected by this positionInterpolator
     * @param axisOfTransform the transform that defines the local coordinate
     * @param knots an array of knot values that specify interpolation
     * points.
     *
     * @since Java 3D 1.3
     */
    public PathInterpolator(Alpha alpha, TransformGroup target, Transform3D axisOfTransform, float[] knots) {
        super(alpha, target, axisOfTransform);
        setKnots(knots);
    }

    /**
     * Retrieves the length of the knots array.
     * @return the array length
     */
    public int getArrayLengths() {
        return knots.length;
    }

    /**
     * Sets the knot at the specified index for this interpolator.
     * @param index the index to be changed
     * @param knot the new knot value
     */
    public void setKnot(int index, float knot) {
        this.knots[index] = knot;
    }

    /**
     * Retrieves the knot at the specified index.
     * @param index the index of the value requested
     * @return the interpolator's knot value at the associated index
     */
    public float getKnot(int index) {
        return this.knots[index];
    }

    /**
     * Replaces the existing array of knot values with
     * the specified array.  The array of knots is copied into this
     * interpolator object.  Prior to calling this method,
     * subclasses should verify that the lengths of the new knots array
     * and subclass-specific parameter arrays are the same.
     * @param knots a new array of knot values that specify
     * interpolation points.
     *
     * @since Java 3D 1.2
     */
    protected void setKnots(float[] knots) {
        if (knots[0] < -0.0001 || knots[0] > 0.0001) {
            throw new IllegalArgumentException(J3dI18N.getString("PathInterpolator0"));
        }
        if ((knots[knots.length - 1] - 1.0f) < -0.0001 || (knots[knots.length - 1] - 1.0f) > 0.0001) {
            throw new IllegalArgumentException(J3dI18N.getString("PathInterpolator1"));
        }
        this.knots = new float[knots.length];
        for (int i = 0; i < knots.length; i++) {
            if (i > 0 && knots[i] < knots[i - 1]) {
                throw new IllegalArgumentException(J3dI18N.getString("PathInterpolator2"));
            }
            this.knots[i] = knots[i];
        }
    }

    /**
     * Copies the array of knots from this interpolator
     * into the specified array.
     * The array must be large enough to hold all of the knots.
     * @param knots array that will receive the knots.
     *
     * @since Java 3D 1.2
     */
    public void getKnots(float[] knots) {
        for (int i = 0; i < this.knots.length; i++) {
            knots[i] = this.knots[i];
        }
    }

    /**
     * Computes the base knot index and interpolation value
     * given the specified value of alpha and the knots[] array.  If
     * the index is 0 and there should be no interpolation, both the
     * index variable and the interpolation variable are set to 0.
     * Otherwise, currentKnotIndex is set to the lower index of the
     * two bounding knot points and the currentInterpolationValue
     * variable is set to the ratio of the alpha value between these
     * two bounding knot points.
     * @param alphaValue alpha value between 0.0 and 1.0
     *
     * @since Java 3D 1.3
     */
    protected void computePathInterpolation(float alphaValue) {
        int i;
        for (i = 0; i < knots.length; i++) {
            if ((i == 0 && alphaValue <= knots[i]) || (i > 0 && alphaValue >= knots[i - 1] && alphaValue <= knots[i])) {
                if (i == 0) {
                    currentInterpolationValue = 0f;
                    currentKnotIndex = 0;
                } else {
                    currentInterpolationValue = (alphaValue - knots[i - 1]) / (knots[i] - knots[i - 1]);
                    currentKnotIndex = i - 1;
                }
                break;
            }
        }
    }

    /**
     * @deprecated As of Java 3D version 1.3, replaced by
     * <code>computePathInterpolation(float)</code>
     */
    protected void computePathInterpolation() {
        float value = this.alpha.value();
        computePathInterpolation(value);
    }

    /**
     * Copies all PathInterpolator information from
     * <code>originalNode</code> into
     * the current node.  This method is called from the
     * <code>cloneNode</code> method which is, in turn, called by the
     * <code>cloneTree</code> method.<P> 
     *
     * @param originalNode the original node to duplicate.
     * @param forceDuplicate when set to <code>true</code>, causes the
     *  <code>duplicateOnCloneTree</code> flag to be ignored.  When
     *  <code>false</code>, the value of each node's
     *  <code>duplicateOnCloneTree</code> variable determines whether
     *  NodeComponent data is duplicated or copied.
     *
     * @exception RestrictedAccessException if this object is part of a live
     *  or compiled scenegraph.
     *
     * @see Node#duplicateNode
     * @see Node#cloneTree
     * @see NodeComponent#setDuplicateOnCloneTree
     */
    void duplicateAttributes(Node originalNode, boolean forceDuplicate) {
        super.duplicateAttributes(originalNode, forceDuplicate);
        PathInterpolator pi = (PathInterpolator) originalNode;
        int len = pi.getArrayLengths();
        knots = new float[len];
        for (int i = 0; i < len; i++) setKnot(i, pi.getKnot(i));
    }
}

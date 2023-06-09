package org.opencv.ml;

/**
 * Boosting training parameters.
 *
 * The structure is derived from "CvDTreeParams" but not all of the decision
 * tree parameters are supported. In particular, cross-validation is not
 * supported.
 *
 * All parameters are public. You can initialize them by a constructor and then
 * override some of them directly if you want.
 *
 * @see <a href="http://opencv.itseez.com/modules/ml/doc/boosting.html#cvboostparams">org.opencv.ml.CvBoostParams</a>
 */
public class CvBoostParams {

    protected final long nativeObj;

    protected CvBoostParams(long addr) {
        nativeObj = addr;
    }

    public int get_boost_type() {
        int retVal = get_boost_type_0(nativeObj);
        return retVal;
    }

    public void set_boost_type(int boost_type) {
        set_boost_type_0(nativeObj, boost_type);
        return;
    }

    public int get_weak_count() {
        int retVal = get_weak_count_0(nativeObj);
        return retVal;
    }

    public void set_weak_count(int weak_count) {
        set_weak_count_0(nativeObj, weak_count);
        return;
    }

    public int get_split_criteria() {
        int retVal = get_split_criteria_0(nativeObj);
        return retVal;
    }

    public void set_split_criteria(int split_criteria) {
        set_split_criteria_0(nativeObj, split_criteria);
        return;
    }

    public double get_weight_trim_rate() {
        double retVal = get_weight_trim_rate_0(nativeObj);
        return retVal;
    }

    public void set_weight_trim_rate(double weight_trim_rate) {
        set_weight_trim_rate_0(nativeObj, weight_trim_rate);
        return;
    }

    /**
 * The constructors.
 *
 * See "CvDTreeParams.CvDTreeParams" for description of other parameters.
 *
 * Also there is one structure member that you can set directly:
 *
 * Default parameters are:
 *
 * @see <a href="http://opencv.itseez.com/modules/ml/doc/boosting.html#cvboostparams-cvboostparams">org.opencv.ml.CvBoostParams.CvBoostParams</a>
 */
    public CvBoostParams() {
        nativeObj = n_newObj();
    }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
        super.finalize();
    }

    static {
        System.loadLibrary("opencv_java");
    }

    private static native int get_boost_type_0(long nativeObj);

    private static native void set_boost_type_0(long nativeObj, int boost_type);

    private static native int get_weak_count_0(long nativeObj);

    private static native void set_weak_count_0(long nativeObj, int weak_count);

    private static native int get_split_criteria_0(long nativeObj);

    private static native void set_split_criteria_0(long nativeObj, int split_criteria);

    private static native double get_weight_trim_rate_0(long nativeObj);

    private static native void set_weight_trim_rate_0(long nativeObj, double weight_trim_rate);

    private static native long n_newObj();

    private static native void delete(long nativeObj);
}

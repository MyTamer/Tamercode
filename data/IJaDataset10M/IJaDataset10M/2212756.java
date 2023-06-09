package org.jquantlib.math;

/**
 * In mathematics, the error function (also called the Gauss error function)
 * is a non-elementary function which occurs in probability, statistics and
 * partial differential equations. It is defined as:
 * 
 * {@latex[
 * \operatorname{erf}(x) = \frac{2}{\sqrt{\pi}}\int_0^x e^{-t^2} dt
 * }
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Error_function">Error Function on Wikipedia</a>
 * 
 * @author Richard Gomes
 */
public class ErrorFunction implements Ops.DoubleOp {

    private static final double tiny = Constants.QL_EPSILON;

    private static final double one = 1.00000000000000000000e+00;

    private static final double erx = 8.45062911510467529297e-01;

    private static final double efx = 1.28379167095512586316e-01;

    private static final double efx8 = 1.02703333676410069053e+00;

    private static final double pp0 = 1.28379167095512558561e-01;

    private static final double pp1 = -3.25042107247001499370e-01;

    private static final double pp2 = -2.84817495755985104766e-02;

    private static final double pp3 = -5.77027029648944159157e-03;

    private static final double pp4 = -2.37630166566501626084e-05;

    private static final double qq1 = 3.97917223959155352819e-01;

    private static final double qq2 = 6.50222499887672944485e-02;

    private static final double qq3 = 5.08130628187576562776e-03;

    private static final double qq4 = 1.32494738004321644526e-04;

    private static final double qq5 = -3.96022827877536812320e-06;

    private static final double pa0 = -2.36211856075265944077e-03;

    private static final double pa1 = 4.14856118683748331666e-01;

    private static final double pa2 = -3.72207876035701323847e-01;

    private static final double pa3 = 3.18346619901161753674e-01;

    private static final double pa4 = -1.10894694282396677476e-01;

    private static final double pa5 = 3.54783043256182359371e-02;

    private static final double pa6 = -2.16637559486879084300e-03;

    private static final double qa1 = 1.06420880400844228286e-01;

    private static final double qa2 = 5.40397917702171048937e-01;

    private static final double qa3 = 7.18286544141962662868e-02;

    private static final double qa4 = 1.26171219808761642112e-01;

    private static final double qa5 = 1.36370839120290507362e-02;

    private static final double qa6 = 1.19844998467991074170e-02;

    private static final double ra0 = -9.86494403484714822705e-03;

    private static final double ra1 = -6.93858572707181764372e-01;

    private static final double ra2 = -1.05586262253232909814e+01;

    private static final double ra3 = -6.23753324503260060396e+01;

    private static final double ra4 = -1.62396669462573470355e+02;

    private static final double ra5 = -1.84605092906711035994e+02;

    private static final double ra6 = -8.12874355063065934246e+01;

    private static final double ra7 = -9.81432934416914548592e+00;

    private static final double sa1 = 1.96512716674392571292e+01;

    private static final double sa2 = 1.37657754143519042600e+02;

    private static final double sa3 = 4.34565877475229228821e+02;

    private static final double sa4 = 6.45387271733267880336e+02;

    private static final double sa5 = 4.29008140027567833386e+02;

    private static final double sa6 = 1.08635005541779435134e+02;

    private static final double sa7 = 6.57024977031928170135e+00;

    private static final double sa8 = -6.04244152148580987438e-02;

    private static final double rb0 = -9.86494292470009928597e-03;

    private static final double rb1 = -7.99283237680523006574e-01;

    private static final double rb2 = -1.77579549177547519889e+01;

    private static final double rb3 = -1.60636384855821916062e+02;

    private static final double rb4 = -6.37566443368389627722e+02;

    private static final double rb5 = -1.02509513161107724954e+03;

    private static final double rb6 = -4.83519191608651397019e+02;

    private static final double sb1 = 3.03380607434824582924e+01;

    private static final double sb2 = 3.25792512996573918826e+02;

    private static final double sb3 = 1.53672958608443695994e+03;

    private static final double sb4 = 3.19985821950859553908e+03;

    private static final double sb5 = 2.55305040643316442583e+03;

    private static final double sb6 = 4.74528541206955367215e+02;

    private static final double sb7 = -2.24409524465858183362e+01;

    public double op(final double x) {
        double R, S, P, Q, s, y, z, r, ax;
        ax = Math.abs(x);
        if (ax < 0.84375) {
            if (ax < 3.7252902984e-09) {
                if (ax < Constants.DBL_MIN * 16) return 0.125 * (8.0 * x + efx8 * x);
                return x + efx * x;
            }
            z = x * x;
            r = pp0 + z * (pp1 + z * (pp2 + z * (pp3 + z * pp4)));
            s = one + z * (qq1 + z * (qq2 + z * (qq3 + z * (qq4 + z * qq5))));
            y = r / s;
            return x + x * y;
        }
        if (ax < 1.25) {
            s = ax - one;
            P = pa0 + s * (pa1 + s * (pa2 + s * (pa3 + s * (pa4 + s * (pa5 + s * pa6)))));
            Q = one + s * (qa1 + s * (qa2 + s * (qa3 + s * (qa4 + s * (qa5 + s * qa6)))));
            if (x >= 0) return erx + P / Q; else return -erx - P / Q;
        }
        if (ax >= 6) {
            if (x >= 0) return one - tiny; else return tiny - one;
        }
        s = one / (ax * ax);
        if (ax < 2.85714285714285) {
            R = ra0 + s * (ra1 + s * (ra2 + s * (ra3 + s * (ra4 + s * (ra5 + s * (ra6 + s * ra7))))));
            S = one + s * (sa1 + s * (sa2 + s * (sa3 + s * (sa4 + s * (sa5 + s * (sa6 + s * (sa7 + s * sa8)))))));
        } else {
            R = rb0 + s * (rb1 + s * (rb2 + s * (rb3 + s * (rb4 + s * (rb5 + s * rb6)))));
            S = one + s * (sb1 + s * (sb2 + s * (sb3 + s * (sb4 + s * (sb5 + s * (sb6 + s * sb7))))));
        }
        r = Math.exp(-ax * ax - 0.5625 + R / S);
        if (x >= 0) return one - r / ax; else return r / ax - one;
    }
}

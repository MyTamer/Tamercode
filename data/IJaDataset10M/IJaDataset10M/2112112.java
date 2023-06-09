package org.das2.math.fft;

/**
 *
 * @author  Edward West
 */
public final class SimpleFFT {

    private static final double LOG_2 = Math.log(2);

    /** Creates a new instance of SimpleFFT */
    private SimpleFFT() {
    }

    public static double[][] fft(double[][] array) {
        double u_r, u_i, w_r, w_i, t_r, t_i;
        int ln, nv2, k, l, le, le1, j, ip, i, n;
        n = array.length;
        ln = (int) (Math.log((double) n) / LOG_2 + 0.5);
        nv2 = n / 2;
        j = 1;
        for (i = 1; i < n; i++) {
            if (i < j) {
                t_r = array[i - 1][0];
                t_i = array[i - 1][1];
                array[i - 1][0] = array[j - 1][0];
                array[i - 1][1] = array[j - 1][1];
                array[j - 1][0] = t_r;
                array[j - 1][1] = t_i;
            }
            k = nv2;
            while (k < j) {
                j = j - k;
                k = k / 2;
            }
            j = j + k;
        }
        for (l = 1; l <= ln; l++) {
            le = (int) (Math.exp((double) l * LOG_2) + 0.5);
            le1 = le / 2;
            u_r = 1.0;
            u_i = 0.0;
            w_r = Math.cos(Math.PI / (double) le1);
            w_i = -Math.sin(Math.PI / (double) le1);
            for (j = 1; j <= le1; j++) {
                for (i = j; i <= n; i += le) {
                    ip = i + le1;
                    t_r = array[ip - 1][0] * u_r - u_i * array[ip - 1][1];
                    t_i = array[ip - 1][1] * u_r + u_i * array[ip - 1][0];
                    array[ip - 1][0] = array[i - 1][0] - t_r;
                    array[ip - 1][1] = array[i - 1][1] - t_i;
                    array[i - 1][0] = array[i - 1][0] + t_r;
                    array[i - 1][1] = array[i - 1][1] + t_i;
                }
                t_r = u_r * w_r - w_i * u_i;
                u_i = w_r * u_i + w_i * u_r;
                u_r = t_r;
            }
        }
        return array;
    }
}

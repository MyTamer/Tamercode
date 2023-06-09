package jmri.jmrix.rps;

import javax.vecmath.Point3d;

/**
 * Implementation of 2.1th algorithm for reducing Readings
 * <P>
 * This algorithm was provided by Robert Ashenfelter
 * based in part on the work of Ralph Bucher in his paper
 * "Exact Solution for Three Dimensional Hyperbolic Positioning  Algorithm and 
 * Synthesizable VHDL Model for Hardware Implementation".
 * <P>
 * Neither Ashenfelter nor Bucher provide any guarantee as to the
 * intellectual property status of this algorithm.  
 * Use it at your own risk.
 *
 *

* 
* <h2>RPSpos2.2 program description.</h2>
*  
* <p>
* As in previous versions, the first thing it does is sort the receivers in order of increasing time delay, discarding those that failed or are too far or too near, and using the closest ones.  There is a maximum that are used, still set at 50. 
* <p>
* Next it discards those receivers with gross measurement errors.  All possible pairs of receivers are checked to see if the sum of their measured ranges is less than, or the difference is greater than, the distance between the receivers.  Counts are maintained for each receiver and the one with the largest count is booted out.  The procedure is repeated until there are no more failures.  If fewer than three receivers are left there can be no solution and an error code is returned. 
* <p> 
* Two iterative techniques are used which I call "One-At-A-Time" and "All-Together."  The first looks at one receiver at a time and moves the estimated position directly toward or away from it such that the distance is equal to the measured value.  This simple technique usually converges quite rapidly.  The second technique accumulates the adjustments for all receivers and then computes and applies an average for all.  It also computes the variance of the residual errors (differences between measured receiver distances and those from the computed position)  which is used to monitor the progress of the iterative solution and it implements the rejection of the measurement with the largest residual when it is deemed to be an outlier.  The second technique is not as fast as the first, but is ultimately more accurate. 
* <p> 
* The solution proceeds in seven stages, much like those in versions 2.0 and 2.1.  Stage 0 does 50 One-At-A-Time iterations with the receivers in the sorted order.  As in previous versions, it starts from a position far, far below any likely final point.  Stage 1 continues the One-At-A-Time iterations with the receivers in reverse order.  Every 50 such iterations the All-Together procedure is run to check the variance but not to reject outliers.   The One-At-A-Time procedure usually converges in 20-50 iterations, however for occasional positions the convergence is much slower.  The program moves on to the next stage after a total of 750 iterations, or sooner if it appears that no further improvement can be had.  If the variance indicates that convergence has been achieved and there are no significant errors, then the program skips directly to Stage 6. 
* <p> 
* Stage 2 is where the outliers are rejected.  This is only run when there are more than 6 receivers; if there are 5 or 6, Stage 3 is run instead; if 4 receivers, Stage 4 (sometimes).  By this time a correct, but not particularly accurate, position should have been obtained.  The One-At-A-Time technique is continued for an additional 200 iterations with the receivers in reverse order ending with the closest receiver.  Weights are applied assuming that close measurements are more accurate than distant ones.  The weights fade out during the iterations so that at the end the adjustments are very small.  This fade-out fixes a problem with the One-At-A-Time technique in that it gives undue weight to the last receiver.  The result at this point should be quite accurate, at least for the measurements that are still in play.  At this point the All-Together procedure is run to compute the variance and eliminate an outlier.  Unlike version 2.1, the receiver with the largest residual error is always discarded--if it is actually OK, it will be put back in Stage 5.  The entire stage-2 procedure is repeated until the number of receivers has been reduced to 6 or until the iteration count reaches 2000, then moves on to Stage 3. 
* <p> 
* Stages 3 and 5 are new to version 2.2.  Stage 3 runs only with 6 and 5 receivers.  It solves for all subsets with one receiver removed by running the One-At-A-Time iteration 250 times, with weights and fade-out, and then using All-Together to check the variances of the residuals.  The receiver which was removed resulting in the subset with the smallest variance is then discarded as possibly being errored.  Unlike the outlier rejection of Stage 2, which often discards the wrong receiver, this procedure usually gets it right.  But it is too computationally intensive to use with a large number of receivers. 
* <p> 
* Stage 4 runs only with 4 receivers and rejects an "outlier" using the same procedure as Stage 2.  However, it is only run if the variance is considerably more than just marginally large, i.e. not if there are only small random measurement errors.  With one error in 4 receivers it is theoretically impossible to determine which one.  But, what the heck, there's no harm in trying.  And if it should happen to succeed in ousting the errored receiver and there are also previously-rejected good receivers that Stage 5 can put back, then the measurement is salvaged and the correct position is reported with no error code. 
* <p> 
* At this point we may be down to four receivers only (or perhaps three), but they should be all good receivers.  Stage 5 runs the One-At-A-Time iteration 300 times, with weights and fade-out, and then the All-Together procedure to get a good solution with only the remaining receivers.  Then all of the rejected receivers are checked to see if any of them agree with this solution and if so they are put back into the list of good receivers.  The reason for doing this is that the outlier rejection often rejects the wrong receiver.  Since this means we need to do this put-back anyway, Stages 2 to 4 are arranged to keep on rejecting receivers regardless of whether it does any good.  But Stages 2 to 4 do check the variance and skip to this stage if it indicates that there are no more significant errors. 
* <p> 
* Stage 6 is similar to Stage 3 of version 2.1 except that it runs the One-At-A-Time iteration in addition to the All-Together iteration, both using weights according to distance, with the intent to produce a more refined result.  First it runs the One-At-A-Time iteration 250 more times, with weights and fade-out, to make sure the solution is close since the All-Together iterations sometimes converge rather slowly. Unlike version 2.1, it does not attempt to discard outliers.  The iterations continue until the All-Together procedure can do no more or until the total iteration count reaches 5000.  (Note that a single instance of All-Together increments the iteration count by the number of receivers currently in use.)   
* <p> 
* Like version 2.1, this version does not always, or even usually, run through all the iterations, but instead cuts them short and also cuts out stages when the solution has converged.  The total number of iterations is between 342 (with only 3 receivers) and 5000.  The estimated execution time ranges from 0.13 to ~2.0 milliseconds (1.0 GHz Pentium III). 
* <p> 
* Input/output is the same as for previous versions and is described in the e-mail with version 1.0 sent on 11/17/06. 
* <p> 
* Return values are the same as with version 2.1.  These are as follows. 
*<pre>
       >= 4    OK.  Value = number of receivers used.
            3    Probably OK.  3 receivers used.
         1,2    Questionable.  Maybe OK, maybe not.
            0    No solution.  Don't even think about using it.  (Position outside the known universe.)
       -1,-2    Not used.
      <= -3    Variance of residuals too big.  Probably no good.  Value = minus number of receivers used. 
                  (Perhaps close if input is noisy or receiver locations are sloppy.)
</pre>
* Variance too big means RMS residuals > 30 microseconds, i.e. about 0.4 inch or 1.0 cm.  This is about as small a threshold as I dare use lest random errors occasionally make good measurements appear bad. 
*<p> 
* The restrictions on the configuration of transmitters and receivers, necessary to prevent the program from reporting a spurious position, are the same as those for version 1.1.  These are described in the e-mail with that version sent on 12/9/06. 
*
* <P>
 * @author	Robert Ashenfelter  Copyright (C) 2008
 * @author	Bob Jacobsen  Copyright (C) 2008
 * @version	$Revision: 1.12 $
 */
public class Ash2_2Algorithm extends AbstractCalculator {

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD")
    public Ash2_2Algorithm(Point3d[] sensors, double vsound, int offset) {
        this(sensors, vsound);
        Ash2_2Algorithm.offset = offset;
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "EI_EXPOSE_REP2")
    public Ash2_2Algorithm(Point3d[] sensors, double vsound) {
        this.sensors = sensors;
        this.Vs = vsound;
    }

    public Ash2_2Algorithm(Point3d sensor1, Point3d sensor2, Point3d sensor3, double vsound) {
        this(null, vsound);
        sensors = new Point3d[3];
        sensors[0] = sensor1;
        sensors[1] = sensor2;
        sensors[2] = sensor3;
    }

    public Ash2_2Algorithm(Point3d sensor1, Point3d sensor2, Point3d sensor3, Point3d sensor4, double vsound) {
        this(null, vsound);
        sensors = new Point3d[4];
        sensors[0] = sensor1;
        sensors[1] = sensor2;
        sensors[2] = sensor3;
        sensors[3] = sensor4;
    }

    double Vs;

    double Xt = 0.0;

    double Yt = 0.0;

    double Zt = 0.0;

    public Measurement convert(Reading r) {
        if (log.isDebugEnabled()) {
            log.debug("Reading: " + r.toString());
            log.debug("Sensors: " + sensors.length);
            if (sensors.length >= 1 && sensors[0] != null) log.debug("Sensor[0]: " + sensors[0].x + "," + sensors[0].y + "," + sensors[0].z);
            if (sensors.length >= 2 && sensors[1] != null) log.debug("Sensor[1]: " + sensors[1].x + "," + sensors[1].y + "," + sensors[1].z);
        }
        prep(r);
        RetVal result = RPSpos(nr, Tr, Xr, Yr, Zr, Vs, Xt, Yt, Zt);
        Xt = result.x;
        Yt = result.y;
        Zt = result.z;
        Vs = result.vs;
        log.debug("x = " + Xt + " y = " + Yt + " z0 = " + Zt + " code = " + result.code);
        return new Measurement(r, Xt, Yt, Zt, Vs, result.code, "Ash2_2Algorithm");
    }

    /**
     * Seed the conversion using an estimated position
     */
    public Measurement convert(Reading r, Point3d guess) {
        this.Xt = guess.x;
        this.Yt = guess.y;
        this.Zt = guess.z;
        return convert(r);
    }

    /**
     * Seed the conversion using a last measurement
     */
    public Measurement convert(Reading r, Measurement last) {
        if (last != null) {
            this.Xt = last.getX();
            this.Yt = last.getY();
            this.Zt = last.getZ();
        }
        if (this.Xt > 9.E99) this.Xt = 0;
        if (this.Yt > 9.E99) this.Yt = 0;
        if (this.Zt > 9.E99) this.Zt = 0;
        return convert(r);
    }

    static int offset = 0;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    public static int TMAX = 35000;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    public static int TMIN = 150;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    public static int SMAX = 30;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    public static int NMAX = 50;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "MS_SHOULD_BE_FINAL")
    public static int NERR = 6;

    RetVal RPSpos(int nr, double Tr[], double Xr[], double Yr[], double Zr[], double Vs, double Xt, double Yt, double Zt) {
        int i = 0, j = 0, jmax = 0, k = 0, l = 0, ns, nss, nxx, nox = 0, S, cmax;
        int[] ce = new int[NMAX];
        double Rq;
        double[] Rs = new double[NMAX], Xs = new double[NMAX], Ys = new double[NMAX], Zs = new double[NMAX];
        double x, y, z, xo = 0., yo = 0., zo = 0., Rmax, Ww, Xw, Yw, Zw, w, q;
        double err, emax, var = 0, vmax, vmin, vold;
        double[] vex = new double[NERR];
        vmax = SMAX * SMAX * Vs * Vs;
        vmin = 1.0 * Vs * Vs;
        ns = 0;
        Rmax = Vs * TMAX;
        Rs[NMAX - 1] = TMAX;
        for (i = 0; i < nr; i++) {
            if (Tr[i] == 0.0) continue;
            Rq = Vs * (Tr[i] + offset);
            if ((Rq >= Rmax) || (Rq < Vs * TMIN)) continue;
            if (ns == 0) {
                Rs[0] = Rq;
                Xs[0] = Xr[i];
                Ys[0] = Yr[i];
                Zs[0] = Zr[i];
                ns = 1;
            } else {
                j = ((ns == NMAX) ? (ns - 1) : (ns++));
                for (; ; j--) {
                    if ((j > 0) && (Rq < Rs[j - 1])) {
                        Rs[j] = Rs[j - 1];
                        Xs[j] = Xs[j - 1];
                        Ys[j] = Ys[j - 1];
                        Zs[j] = Zs[j - 1];
                    } else {
                        if ((j < NMAX - 1) || (Rq < Rs[j])) {
                            Rs[j] = Rq;
                            Xs[j] = Xr[i];
                            Ys[j] = Yr[i];
                            Zs[j] = Zr[i];
                        }
                        break;
                    }
                }
            }
        }
        for (i = 0; i < ns; i++) ce[i] = 0;
        for (i = 0; i < ns - 1; i++) {
            for (j = i + 1; j < ns; j++) {
                q = Math.sqrt((Xs[i] - Xs[j]) * (Xs[i] - Xs[j]) + (Ys[i] - Ys[j]) * (Ys[i] - Ys[j]) + (Zs[i] - Zs[j]) * (Zs[i] - Zs[j]));
                if ((Rs[i] + Rs[j] < q) || (Rs[i] - Rs[j] > q) || (Rs[j] - Rs[i] > q)) {
                    ++ce[i];
                    ++ce[j];
                }
            }
        }
        cmax = 1;
        nxx = 0;
        while (cmax != 0) {
            cmax = 0;
            for (i = 0; i < ns; i++) {
                if (ce[i] >= cmax) {
                    if (ce[i] > 0) nxx = ((ce[i] == cmax) ? nxx + 1 : 1);
                    cmax = ce[i];
                    j = i;
                }
            }
            if (cmax > 0) {
                for (i = 0; i < ns; i++) {
                    if (i == j) continue;
                    q = Math.sqrt((Xs[i] - Xs[j]) * (Xs[i] - Xs[j]) + (Ys[i] - Ys[j]) * (Ys[i] - Ys[j]) + (Zs[i] - Zs[j]) * (Zs[i] - Zs[j]));
                    if ((Rs[i] + Rs[j] < q) || (Rs[i] - Rs[j] > q) || (Rs[j] - Rs[i] > q)) {
                        --ce[i];
                    }
                }
                for (i = j; i < ns - 1; i++) {
                    Rs[i] = Rs[i + 1];
                    Xs[i] = Xs[i + 1];
                    Ys[i] = Ys[i + 1];
                    Zs[i] = Zs[i + 1];
                    ce[i] = ce[i + 1];
                }
                --ns;
            }
        }
        nss = ns;
        if (ns < 3) {
            Xt = Yt = Zt = 9.9999999e99;
            return new RetVal(0, Xt, Yt, Zt, Vs);
        }
        S = i = 0;
        x = y = 0.0;
        z = -100000.0;
        while (++i < 5000) {
            if (S == 0) {
                j = k = (i - 1) % ns;
                w = 1.0;
            } else if (S == 1) {
                j = k = ns - 1 - i % ns;
                w = 1.0;
            } else {
                if (--k < 0) {
                    j = k = 0;
                    w = 0.0;
                } else {
                    j = k % ns;
                    if ((S == 3) && (j == l)) {
                        j = ((--k > 0) ? k % ns : 0);
                    }
                    w = 1.0 - Rs[j] / Rmax;
                    w = w * w;
                    if (k < 50) w *= 0.02 * (k + 1); else w *= 0.005 * (k + 150);
                }
            }
            if (k >= 0) {
                q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                q = w * (1.0 - Rs[j] / q);
                x += q * (Xs[j] - x);
                y += q * (Ys[j] - y);
                z += q * (Zs[j] - z);
            }
            if (((S == 1) && (i % (50 + ns) == 51)) || ((S >= 2) && (k <= 0))) {
                vold = var;
                Ww = Xw = Yw = Zw = var = emax = 0.0;
                for (j = 0; j < ns; j++) {
                    if ((S == 3) && (j == l)) continue;
                    q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                    err = q - Rs[j];
                    err = err * err;
                    q = 1.0 - Rs[j] / q;
                    if (S >= 2) {
                        w = 1.0 - Rs[j] / Rmax;
                        w = w * w;
                    } else w = 1.0;
                    Xw += w * (x + q * (Xs[j] - x));
                    Yw += w * (y + q * (Ys[j] - y));
                    Zw += w * (z + q * (Zs[j] - z));
                    Ww += w;
                    var += w * err;
                    if (w * err > emax) {
                        emax = w * err;
                        jmax = j;
                    }
                }
                x = Xw / Ww;
                y = Yw / Ww;
                z = Zw / Ww;
                var = var / Ww;
                i += ns - 1;
                if (((S == 2) && (ns > NERR) && (var > 3 * vmax)) || ((S == 4) && (ns == 4))) {
                    --ns;
                    nox = 0;
                    q = Rs[jmax];
                    Rs[jmax] = Rs[ns];
                    Rs[ns] = q;
                    q = Xs[jmax];
                    Xs[jmax] = Xs[ns];
                    Xs[ns] = q;
                    q = Ys[jmax];
                    Ys[jmax] = Ys[ns];
                    Ys[ns] = q;
                    q = Zs[jmax];
                    Zs[jmax] = Zs[ns];
                    Zs[ns] = q;
                } else ++nox;
                if (S == 1) {
                    if (var < vmax) {
                        k = 250;
                        nox = 0;
                        S = 6;
                    } else if ((var < 3 * vmax) || (i >= 750)) {
                        if (ns <= 4) {
                            k = 300;
                            S = (var > 36 * vmax) ? 4 : 5;
                        } else if (ns <= NERR) {
                            l = 0;
                            xo = x;
                            yo = y;
                            zo = z;
                            k = 250;
                            S = 3;
                        } else {
                            k = 200;
                            S = 2;
                        }
                    }
                } else if (S == 2) {
                    if (var < vmax) {
                        k = 300;
                        S = 5;
                    } else if (ns <= NERR) {
                        l = 0;
                        xo = x;
                        yo = y;
                        zo = z;
                        k = 250;
                        S = 3;
                    } else if (i >= 2000) {
                        ns = NERR;
                        l = 0;
                        xo = x;
                        yo = y;
                        zo = z;
                        k = 250;
                        S = 3;
                    }
                    k = 200;
                } else if (S == 3) {
                    if (ns > 4) {
                        vex[l] = var;
                        if (++l < ns) {
                            k = 250;
                            x = xo;
                            y = yo;
                            z = zo;
                        } else {
                            var = vex[j = 0];
                            for (l = 1; l < ns; l++) {
                                if (vex[l] < var) var = vex[j = l];
                            }
                            --ns;
                            q = Rs[j];
                            Rs[j] = Rs[ns];
                            Rs[ns] = q;
                            q = Xs[j];
                            Xs[j] = Xs[ns];
                            Xs[ns] = q;
                            q = Ys[j];
                            Ys[j] = Ys[ns];
                            Ys[ns] = q;
                            q = Zs[j];
                            Zs[j] = Zs[ns];
                            Zs[ns] = q;
                            if (var < vmax) {
                                k = 300;
                                S = 5;
                            } else if (ns <= 4) {
                                k = 300;
                                S = (var > 36 * vmax) ? 4 : 5;
                            } else {
                                l = 0;
                                xo = x;
                                yo = y;
                                zo = z;
                                k = 250;
                            }
                        }
                    }
                } else if (S == 4) {
                    k = 300;
                    S = 5;
                } else if (S == 5) {
                    for (j = ns; j < nss; j++) {
                        q = Math.sqrt((Xs[j] - x) * (Xs[j] - x) + (Ys[j] - y) * (Ys[j] - y) + (Zs[j] - z) * (Zs[j] - z));
                        if ((Rs[j] - q) * (Rs[j] - q) < 4 * vmax) {
                            q = Rs[j];
                            Rs[j] = Rs[ns];
                            Rs[ns] = q;
                            q = Xs[j];
                            Xs[j] = Xs[ns];
                            Xs[ns] = q;
                            q = Ys[j];
                            Ys[j] = Ys[ns];
                            Ys[ns] = q;
                            q = Zs[j];
                            Zs[j] = Zs[ns];
                            Zs[ns] = q;
                            ++ns;
                        }
                    }
                    k = 250;
                    nox = 0;
                    S = 6;
                } else if (S == 6) {
                    if ((nox >= 1 + 110 / (ns + 5)) && ((var > 0.999 * vold) || (var < vmin))) {
                        break;
                    }
                }
            }
            if ((S == 0) && (i >= 50)) {
                k = j;
                S = 1;
            }
        }
        Xt = x;
        Yt = y;
        Zt = z;
        if ((var > vmax) || ((ns == 3) && (var > vmin))) {
            return new RetVal(-ns, Xt, Yt, Zt, Vs);
        }
        if ((ns == 3) && (nxx > 1)) {
            return new RetVal(1, Xt, Yt, Zt, Vs);
        }
        if (nss >= (3 * ns - 5)) {
            return new RetVal(2, Xt, Yt, Zt, Vs);
        }
        return new RetVal(ns, Xt, Yt, Zt, Vs);
    }

    /**
     * Internal class to handle return value.
     *
     * More of a struct, really
     */
    static class RetVal {

        RetVal(int code, double x, double y, double z, double vs) {
            this.code = code;
            this.x = x;
            this.y = y;
            this.z = z;
            this.vs = vs;
        }

        int code;

        @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "UUF_UNUSED_FIELD")
        double x, y, z, t, vs;
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Ash2_2Algorithm.class.getName());
}

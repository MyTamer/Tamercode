package com.jgraph.gaeawt.java.awt.geom;

import java.util.NoSuchElementException;
import org.apache.harmony.awt.internal.nls.Messages;

public class FlatteningPathIterator implements PathIterator {

    /**
     * The default points buffer size
     */
    private static final int BUFFER_SIZE = 16;

    /**
     * The default curve subdivision limit
     */
    private static final int BUFFER_LIMIT = 16;

    /**
     * The points buffer capacity
     */
    private static final int BUFFER_CAPACITY = 16;

    /**
     * The type of current segment to be flat
     */
    int bufType;

    /**
     * The curve subdivision limit
     */
    int bufLimit;

    /**
     * The current points buffer size
     */
    int bufSize;

    /**
     * The inner cursor position in points buffer 
     */
    int bufIndex;

    /**
     * The current subdivision count
     */
    int bufSubdiv;

    /**
     * The points buffer 
     */
    double buf[];

    /**
     * The indicator of empty points buffer
     */
    boolean bufEmpty = true;

    /**
     * The source PathIterator
     */
    PathIterator p;

    /**
     * The flatness of new path 
     */
    double flatness;

    /**
     * The square of flatness
     */
    double flatness2;

    /**
     * The x coordinate of previous path segment
     */
    double px;

    /**
     * The y coordinate of previous path segment
     */
    double py;

    /**
     * The tamporary buffer for getting points from PathIterator
     */
    double coords[] = new double[6];

    public FlatteningPathIterator(PathIterator path, double flatness) {
        this(path, flatness, BUFFER_LIMIT);
    }

    public FlatteningPathIterator(PathIterator path, double flatness, int limit) {
        if (flatness < 0.0) {
            throw new IllegalArgumentException(Messages.getString("awt.206"));
        }
        if (limit < 0) {
            throw new IllegalArgumentException(Messages.getString("awt.207"));
        }
        if (path == null) {
            throw new NullPointerException(Messages.getString("awt.208"));
        }
        this.p = path;
        this.flatness = flatness;
        this.flatness2 = flatness * flatness;
        this.bufLimit = limit;
        this.bufSize = Math.min(bufLimit, BUFFER_SIZE);
        this.buf = new double[bufSize];
        this.bufIndex = bufSize;
    }

    public double getFlatness() {
        return flatness;
    }

    public int getRecursionLimit() {
        return bufLimit;
    }

    public int getWindingRule() {
        return p.getWindingRule();
    }

    public boolean isDone() {
        return bufEmpty && p.isDone();
    }

    /**
     * Calculates flat path points for current segment of the source shape.
     * 
     * Line segment is flat by itself. Flatness of quad and cubic curves evaluated by getFlatnessSq() method.  
     * Curves subdivided until current flatness is bigger than user defined and subdivision limit isn't exhausted.
     * Single source segment translated to series of buffer points. The less flatness the bigger serries.
     * Every currentSegment() call extract one point from the buffer. When series completed evaluate() takes next source shape segment.        
     */
    void evaluate() {
        if (bufEmpty) {
            bufType = p.currentSegment(coords);
        }
        switch(bufType) {
            case SEG_MOVETO:
            case SEG_LINETO:
                px = coords[0];
                py = coords[1];
                break;
            case SEG_QUADTO:
                if (bufEmpty) {
                    bufIndex -= 6;
                    buf[bufIndex + 0] = px;
                    buf[bufIndex + 1] = py;
                    System.arraycopy(coords, 0, buf, bufIndex + 2, 4);
                    bufSubdiv = 0;
                }
                while (bufSubdiv < bufLimit) {
                    if (QuadCurve2D.getFlatnessSq(buf, bufIndex) < flatness2) {
                        break;
                    }
                    if (bufIndex <= 4) {
                        double tmp[] = new double[bufSize + BUFFER_CAPACITY];
                        System.arraycopy(buf, bufIndex, tmp, bufIndex + BUFFER_CAPACITY, bufSize - bufIndex);
                        buf = tmp;
                        bufSize += BUFFER_CAPACITY;
                        bufIndex += BUFFER_CAPACITY;
                    }
                    QuadCurve2D.subdivide(buf, bufIndex, buf, bufIndex - 4, buf, bufIndex);
                    bufIndex -= 4;
                    bufSubdiv++;
                }
                bufIndex += 4;
                px = buf[bufIndex];
                py = buf[bufIndex + 1];
                bufEmpty = (bufIndex == bufSize - 2);
                if (bufEmpty) {
                    bufIndex = bufSize;
                    bufType = SEG_LINETO;
                }
                break;
            case SEG_CUBICTO:
                if (bufEmpty) {
                    bufIndex -= 8;
                    buf[bufIndex + 0] = px;
                    buf[bufIndex + 1] = py;
                    System.arraycopy(coords, 0, buf, bufIndex + 2, 6);
                    bufSubdiv = 0;
                }
                while (bufSubdiv < bufLimit) {
                    if (CubicCurve2D.getFlatnessSq(buf, bufIndex) < flatness2) {
                        break;
                    }
                    if (bufIndex <= 6) {
                        double tmp[] = new double[bufSize + BUFFER_CAPACITY];
                        System.arraycopy(buf, bufIndex, tmp, bufIndex + BUFFER_CAPACITY, bufSize - bufIndex);
                        buf = tmp;
                        bufSize += BUFFER_CAPACITY;
                        bufIndex += BUFFER_CAPACITY;
                    }
                    CubicCurve2D.subdivide(buf, bufIndex, buf, bufIndex - 6, buf, bufIndex);
                    bufIndex -= 6;
                    bufSubdiv++;
                }
                bufIndex += 6;
                px = buf[bufIndex];
                py = buf[bufIndex + 1];
                bufEmpty = (bufIndex == bufSize - 2);
                if (bufEmpty) {
                    bufIndex = bufSize;
                    bufType = SEG_LINETO;
                }
                break;
        }
    }

    public void next() {
        if (bufEmpty) {
            p.next();
        }
    }

    public int currentSegment(float[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(Messages.getString("awt.4Bx"));
        }
        evaluate();
        int type = bufType;
        if (type != SEG_CLOSE) {
            coords[0] = (float) px;
            coords[1] = (float) py;
            if (type != SEG_MOVETO) {
                type = SEG_LINETO;
            }
        }
        return type;
    }

    public int currentSegment(double[] coords) {
        if (isDone()) {
            throw new NoSuchElementException(Messages.getString("awt.4B"));
        }
        evaluate();
        int type = bufType;
        if (type != SEG_CLOSE) {
            coords[0] = px;
            coords[1] = py;
            if (type != SEG_MOVETO) {
                type = SEG_LINETO;
            }
        }
        return type;
    }
}

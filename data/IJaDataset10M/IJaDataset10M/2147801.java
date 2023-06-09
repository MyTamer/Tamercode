package com.rapitasystems.jgraphviz.spline;

/** this class represents a cubic polynomial */
public class Cubic {

    float a, b, c, d;

    public Cubic(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /** evaluate cubic */
    public float eval(float u) {
        return (((d * u) + c) * u + b) * u + a;
    }
}

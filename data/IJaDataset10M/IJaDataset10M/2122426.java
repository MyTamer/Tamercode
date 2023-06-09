package org.drools.benchmark.waltzdb;

public class Line {

    private int p1;

    private int p2;

    public Line() {
        super();
    }

    public int getP1() {
        return p1;
    }

    public void setP1(int p1) {
        this.p1 = p1;
    }

    public int getP2() {
        return p2;
    }

    public void setP2(int p2) {
        this.p2 = p2;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + p1;
        result = PRIME * result + p2;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Line other = (Line) obj;
        if (p1 != other.p1) return false;
        if (p2 != other.p2) return false;
        return true;
    }

    public Line(int p1, int p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }
}

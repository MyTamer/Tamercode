package com.kreative.rsrc.pict;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Polygon implements Shape {

    public int polySize;

    public Rect polyBBox;

    public List<Point> polyPoints;

    public static Polygon read(DataInputStream in) throws IOException {
        Polygon p = new Polygon();
        p.polySize = in.readUnsignedShort();
        p.polyBBox = Rect.read(in);
        p.polyPoints = new Vector<Point>();
        for (int i = 10; i < p.polySize; i += 4) {
            p.polyPoints.add(Point.read(in));
        }
        return p;
    }

    public Polygon() {
        polySize = 10;
        polyBBox = new Rect();
        polyPoints = new Vector<Point>();
    }

    public Polygon(java.awt.Polygon p) {
        polyPoints = new Vector<Point>();
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        for (int i = 0; i < p.npoints; i++) {
            polyPoints.add(new Point(p.xpoints[i], p.ypoints[i]));
            if (p.xpoints[i] < minx) minx = p.xpoints[i];
            if (p.xpoints[i] > maxx) maxx = p.xpoints[i];
            if (p.ypoints[i] < miny) miny = p.ypoints[i];
            if (p.ypoints[i] > maxy) maxy = p.ypoints[i];
        }
        if (maxx < minx || maxy < miny) {
            polyBBox = new Rect();
        } else {
            polyBBox = new Rect(minx, miny, maxx - minx, maxy - miny);
        }
        polySize = 10 + polyPoints.size() * 4;
    }

    public void write(DataOutputStream out) throws IOException {
        polySize = 10 + polyPoints.size() * 4;
        int minx = Integer.MAX_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int maxy = Integer.MIN_VALUE;
        for (Point p : polyPoints) {
            if (p.x < minx) minx = p.x;
            if (p.x > maxx) maxx = p.x;
            if (p.y < miny) miny = p.y;
            if (p.y > maxy) maxy = p.y;
        }
        if (maxx < minx || maxy < miny) {
            polyBBox = new Rect();
        } else {
            polyBBox = new Rect(minx, miny, maxx - minx, maxy - miny);
        }
        out.writeShort(polySize);
        polyBBox.write(out);
        for (Point p : polyPoints) {
            p.write(out);
        }
    }

    public java.awt.Polygon toPolygon() {
        java.awt.Polygon pg = new java.awt.Polygon();
        for (Point pt : polyPoints) {
            pg.addPoint(pt.x, pt.y);
        }
        return pg;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (Point pt : polyPoints) {
            sb.append(" " + pt.toString());
        }
        return "Polygon[" + sb.toString().trim() + "]";
    }

    public boolean contains(Point2D p) {
        return toPolygon().contains(p);
    }

    public boolean contains(Rectangle2D r) {
        return toPolygon().contains(r);
    }

    public boolean contains(double x, double y) {
        return toPolygon().contains(x, y);
    }

    public boolean contains(double x, double y, double w, double h) {
        return toPolygon().contains(x, y, w, h);
    }

    public Rectangle getBounds() {
        return toPolygon().getBounds();
    }

    public Rectangle2D getBounds2D() {
        return toPolygon().getBounds2D();
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return toPolygon().getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return toPolygon().getPathIterator(at, flatness);
    }

    public boolean intersects(Rectangle2D r) {
        return toPolygon().intersects(r);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return toPolygon().intersects(x, y, w, h);
    }
}

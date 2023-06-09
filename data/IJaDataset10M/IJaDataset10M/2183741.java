package org.openscience.jchempaint.renderer.elements;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * @cdk.module  renderbasic
 */
public class ElementGroup implements IRenderingElement, Iterable<IRenderingElement> {

    private final Collection<IRenderingElement> elements = new ArrayList<IRenderingElement>();

    double scalex = 10;

    double scaley = 10;

    Rectangle2D extent;

    public Iterator<IRenderingElement> iterator() {
        return elements.iterator();
    }

    public void add(IRenderingElement element) {
        if (element == null) return;
        elements.add(element);
    }

    public void visitChildren(IRenderingVisitor visitor) {
        for (IRenderingElement child : this.elements) {
            child.accept(visitor);
        }
    }

    public void setScale(double[] scale) {
        if (scale != null && scale.length != 2) return;
        scalex = Math.min(scale[0], scale[1]);
        scaley = scalex;
    }

    public double[] getDimensions(IAtomContainer ac, Dimension size) {
        Rectangle2D aSize = getExtent(ac);
        double[] scale = new double[] { (size.getWidth() / (aSize.getWidth())), (size.getHeight() / (aSize.getHeight())) };
        setScale(scale);
        return scale;
    }

    public double getScale(IAtomContainer ac, Dimension size) {
        double[] scale = getDimensions(ac, size);
        return Math.min(scale[0], scale[1]);
    }

    Rectangle2D getExtent(IAtomContainer ac) {
        double xmin = Double.POSITIVE_INFINITY, xmax = Double.NEGATIVE_INFINITY, ymin = Double.POSITIVE_INFINITY, ymax = Double.NEGATIVE_INFINITY;
        for (IAtom atom : ac.atoms()) {
            double x = atom.getPoint2d().x;
            double y = atom.getPoint2d().y;
            xmin = Math.min(xmin, x);
            xmax = Math.max(xmax, x);
            ymin = Math.min(ymin, y);
            ymax = Math.max(ymax, y);
        }
        extent = new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
        return extent;
    }

    public void accept(IRenderingVisitor v) {
        v.visit(this);
    }

    public Point2D center(Dimension size) {
        double aWidth = extent.getWidth() * scalex;
        double aHeight = extent.getHeight() * -scaley;
        double xDiff = size.getWidth() - aWidth;
        double yDiff = size.getHeight() - aHeight;
        return new Point2D.Float((float) (-(extent.getX() * scalex) + xDiff / 2), (float) (-(extent.getY() * -scaley) + yDiff / 2));
    }
}

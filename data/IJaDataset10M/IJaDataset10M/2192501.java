package org.jhotdraw.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.jhotdraw.geom.Geom;

/**
 * A diamond with vertices at the midpoints of its enclosing rectangle.
 *
 *
 * @author Werner Randelshofer
 * @version 1.1 2007-05-12 Removed convenience getters and setters for 
 * IS_QUADRATIC attribute. 
 * <br>1.0 2006-03-27 Created.
 */
public class DiamondFigure extends AbstractAttributedFigure {

    /**
     * If the attribute IS_QUADRATIC is set to true, all sides of the diamond have
     * the same length.
     */
    public static final AttributeKey<Boolean> IS_QUADRATIC = new AttributeKey<Boolean>("isQuadratic", false);

    /**
     * The bounds of the diamond figure.
     */
    private Rectangle2D.Double rectangle;

    protected static java.awt.Color fillcolor = new java.awt.Color(255, 255, 100);

    /** Creates a new instance. */
    public DiamondFigure() {
        this(0, 0, 0, 0);
    }

    public DiamondFigure(double x, double y, double width, double height) {
        rectangle = new Rectangle2D.Double(x, y, width, height);
    }

    protected void drawFill(Graphics2D g) {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        g.setPaint(fillcolor);
        if (IS_QUADRATIC.get(this)) {
            double side = Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        double growx, growy;
        if (grow == 0d) {
            growx = growy = 0d;
        } else {
            double w = r.width / 2d;
            double h = r.height / 2d;
            double lineLength = Math.sqrt(w * w + h * h);
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            growx = ((yb * yb) / xa + xa);
            growy = ((xa * xa) / yb + yb);
            Geom.grow(r, growx, growy);
        }
        GeneralPath diamond = new GeneralPath();
        diamond.moveTo((float) (r.x + r.width / 2), (float) r.y);
        diamond.lineTo((float) (r.x + r.width), (float) (r.y + r.height / 2));
        diamond.lineTo((float) (r.x + r.width / 2), (float) (r.y + r.height));
        diamond.lineTo((float) r.x, (float) (r.y + r.height / 2));
        diamond.closePath();
        g.fill(diamond);
    }

    protected void drawStroke(Graphics2D g) {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        if (IS_QUADRATIC.get(this)) {
            double side = Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = AttributeKeys.getPerpendicularDrawGrowth(this);
        double growx, growy;
        if (grow == 0d) {
            growx = growy = 0d;
        } else {
            double w = r.width / 2d;
            double h = r.height / 2d;
            double lineLength = Math.sqrt(w * w + h * h);
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            growx = ((yb * yb) / xa + xa);
            growy = ((xa * xa) / yb + yb);
            Geom.grow(r, growx, growy);
        }
        GeneralPath diamond = new GeneralPath();
        diamond.moveTo((float) (r.x + r.width / 2), (float) r.y);
        diamond.lineTo((float) (r.x + r.width), (float) (r.y + r.height / 2));
        diamond.lineTo((float) (r.x + r.width / 2), (float) (r.y + r.height));
        diamond.lineTo((float) r.x, (float) (r.y + r.height / 2));
        diamond.closePath();
        g.draw(diamond);
    }

    public Rectangle2D.Double getBounds() {
        Rectangle2D.Double bounds = (Rectangle2D.Double) rectangle.clone();
        return bounds;
    }

    public Rectangle2D.Double getDrawingArea() {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        if (IS_QUADRATIC.get(this)) {
            double side = Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = AttributeKeys.getPerpendicularHitGrowth(this);
        double growx, growy;
        if (grow == 0d) {
            growx = growy = 0d;
        } else {
            double w = r.width / 2d;
            double h = r.height / 2d;
            double lineLength = Math.sqrt(w * w + h * h);
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            growx = ((yb * yb) / xa + xa);
            growy = ((xa * xa) / yb + yb);
            Geom.grow(r, growx, growy);
        }
        return r;
    }

    /**
     * Checks if a Point2D.Double is inside the figure.
     */
    public boolean contains(Point2D.Double p) {
        Rectangle2D.Double r = (Rectangle2D.Double) rectangle.clone();
        if (IS_QUADRATIC.get(this)) {
            double side = Math.max(r.width, r.height);
            r.x -= (side - r.width) / 2;
            r.y -= (side - r.height) / 2;
            r.width = r.height = side;
        }
        double grow = AttributeKeys.getPerpendicularFillGrowth(this);
        double growx, growy;
        if (grow == 0d) {
            growx = growy = 0d;
        } else {
            double w = r.width / 2d;
            double h = r.height / 2d;
            double lineLength = Math.sqrt(w * w + h * h);
            double scale = grow / lineLength;
            double yb = scale * w;
            double xa = scale * h;
            growx = ((yb * yb) / xa + xa);
            growy = ((xa * xa) / yb + yb);
            Geom.grow(r, growx, growy);
        }
        GeneralPath diamond = new GeneralPath();
        diamond.moveTo((float) (r.x + r.width / 2), (float) r.y);
        diamond.lineTo((float) (r.x + r.width), (float) (r.y + r.height / 2));
        diamond.lineTo((float) (r.x + r.width / 2), (float) (r.y + r.height));
        diamond.lineTo((float) r.x, (float) (r.y + r.height / 2));
        diamond.closePath();
        return diamond.contains(p);
    }

    public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
        rectangle.x = Math.min(anchor.x, lead.x);
        rectangle.y = Math.min(anchor.y, lead.y);
        rectangle.width = Math.max(0.1, Math.abs(lead.x - anchor.x));
        rectangle.height = Math.max(0.1, Math.abs(lead.y - anchor.y));
    }

    /**
     * Moves the Figure to a new location.
     * @param tx the transformation matrix.
     */
    public void transform(AffineTransform tx) {
        Point2D.Double anchor = getStartPoint();
        Point2D.Double lead = getEndPoint();
        setBounds((Point2D.Double) tx.transform(anchor, anchor), (Point2D.Double) tx.transform(lead, lead));
    }

    public void restoreTransformTo(Object geometry) {
        Rectangle2D.Double r = (Rectangle2D.Double) geometry;
        rectangle.x = r.x;
        rectangle.y = r.y;
        rectangle.width = r.width;
        rectangle.height = r.height;
    }

    public Object getTransformRestoreData() {
        return rectangle.clone();
    }

    /**
     * Returns the Figures connector for the specified location.
     * By default a ChopDiamondConnector is returned.
     * @see ChopDiamondConnector
     */
    public Connector findConnector(Point2D.Double p, ConnectionFigure prototype) {
        return new ChopDiamondConnector(this);
    }

    public Connector findCompatibleConnector(Connector c, boolean isStart) {
        return new ChopDiamondConnector(this);
    }

    public DiamondFigure clone() {
        DiamondFigure that = (DiamondFigure) super.clone();
        that.rectangle = (Rectangle2D.Double) this.rectangle.clone();
        return that;
    }
}

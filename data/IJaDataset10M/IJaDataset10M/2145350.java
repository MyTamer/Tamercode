package org.newdawn.slick.fills;

import org.newdawn.slick.Color;
import org.newdawn.slick.ShapeFill;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 * A fill effect used to define gradients when filling and drawing shapes. A gradient is defined
 * by two control points. Each point that is rendered is coloured based on it's proximity to the 
 * points. Note that the points are defined relative to the center of the shape being drawn. This 
 * is with the intention that the gradient fills can be used and do not need to be updated when
 * the geometry is moved
 *
 * @author kevin
 */
public class GradientFill implements ShapeFill {

    /** The contant offset */
    private Vector2f none = new Vector2f(0, 0);

    /** The start position of the gradient */
    private Vector2f start;

    /** The end poisition of the gradient */
    private Vector2f end;

    /** The starting colour of the gradient */
    private Color startCol;

    /** The ending colour of the gradient */
    private Color endCol;

    /**
	 * Create a gradient fill
	 * 
	 * @param sx The x coordinate of the starting control point
	 * @param sy The y coordinate of the starting control point
	 * @param startCol The colour to apply at the starting control point
	 * @param ex The x coordinate of the ending control point
	 * @param ey The y coordinate of the ending control point
	 * @param endCol The colour to apply at the ending control point
	 */
    public GradientFill(float sx, float sy, Color startCol, float ex, float ey, Color endCol) {
        this(new Vector2f(sx, sy), startCol, new Vector2f(ex, ey), endCol);
    }

    /**
	 * Create a gradient fill
	 * 
	 * @param start The position of the starting control point
	 * @param startCol The colour to apply at the starting control point
	 * @param end The position of the ending control point
	 * @param endCol The colour to apply at the ending control point
	 */
    public GradientFill(Vector2f start, Color startCol, Vector2f end, Color endCol) {
        this.start = new Vector2f(start);
        this.end = new Vector2f(end);
        this.startCol = new Color(startCol);
        this.endCol = new Color(endCol);
    }

    /**
	 * Get the position of the start control point
	 * 
	 * @return The position of the start control point
	 */
    public Vector2f getStart() {
        return start;
    }

    /**
	 * Get the position of the end control point
	 * 
	 * @return The position of the end control point
	 */
    public Vector2f getEnd() {
        return end;
    }

    /**
	 * Get the colour at the start control point
	 * 
	 * @return The color at the start control point
	 */
    public Color getStartColor() {
        return startCol;
    }

    /**
	 * Get the colour at the end control point
	 * 
	 * @return The color at the end control point
	 */
    public Color getEndColor() {
        return endCol;
    }

    /**
	 * Set the start point's position
	 * 
	 * @param x The x coordinate of the start control point
	 * @param y The y coordinate of the start control point
	 */
    public void setStart(float x, float y) {
        setStart(new Vector2f(x, y));
    }

    /**
	 * Set the start control point's position
	 * 
	 * @param start The new poisition for the start point
	 */
    public void setStart(Vector2f start) {
        this.start = new Vector2f(start);
    }

    /**
	 * Set the end control point's position
	 * 
	 * @param x The x coordinate of the end control point
	 * @param y The y coordinate of the end control point
	 */
    public void setEnd(float x, float y) {
        setEnd(new Vector2f(x, y));
    }

    /**
	 * Set the end control point's position
	 * 
	 * @param end The new position for the end point
	 */
    public void setEnd(Vector2f end) {
        this.end = new Vector2f(end);
    }

    /**
	 * Set the colour to apply at the start control's position
	 * 
	 * @param color The colour to apply at the start control point
	 */
    public void setStartColor(Color color) {
        this.startCol = new Color(color);
    }

    /**
	 * Set the colour to apply at the end control's position
	 * 
	 * @param color The colour to apply at the end control point
	 */
    public void setEndColor(Color color) {
        this.endCol = new Color(color);
    }

    /**
	 * Get the colour that should be applied at the specified location
	 * 
	 * @param shape The shape being filled
	 * @param x The x coordinate of the point being coloured 
	 * @param y The y coordinate of the point being coloured
	 * @return The colour that should be applied based on the control points of this gradient
	 */
    public Color colorAt(Shape shape, float x, float y) {
        return colorAt(x, y);
    }

    /**
	 * Get the colour that should be applied at the specified location
	 * 
	 * @param x The x coordinate of the point being coloured 
	 * @param y The y coordinate of the point being coloured
	 * @return The colour that should be applied based on the control points of this gradient
	 */
    public Color colorAt(float x, float y) {
        float dx1 = end.getX() - start.getX();
        float dy1 = end.getY() - start.getY();
        float dx2 = -dy1;
        float dy2 = dx1;
        float denom = (dy2 * dx1) - (dx2 * dy1);
        if (denom == 0) {
            return Color.black;
        }
        float ua = (dx2 * (start.getY() - y)) - (dy2 * (start.getX() - x));
        ua /= denom;
        float ub = (dx1 * (start.getY() - y)) - (dy1 * (start.getX() - x));
        ub /= denom;
        float u = ua;
        float v = 1 - u;
        Color col = new Color(1, 1, 1, 1);
        col.r = (u * endCol.r) + (v * startCol.r);
        col.b = (u * endCol.b) + (v * startCol.b);
        col.g = (u * endCol.g) + (v * startCol.g);
        col.a = (u * endCol.a) + (v * startCol.a);
        return col;
    }

    /**
	 * @see org.newdawn.slick.ShapeFill#getOffsetAt(org.newdawn.slick.geom.Shape, float, float)
	 */
    public Vector2f getOffsetAt(Shape shape, float x, float y) {
        return none;
    }
}

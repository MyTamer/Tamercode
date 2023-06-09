package jahuwaldt.plot;

import java.awt.*;

/**
*  <p> This class represents a generic plot symbol shown
*      for a data point on a plot.
*  </p>
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  September 22, 2000
*  @version November 17, 2000
**/
public abstract class PlotSymbol extends Object implements Cloneable, java.io.Serializable {

    /**
	*  The approximate width of the plot symbol in pixels.
	**/
    private int width = 8;

    /**
	*  The border color of the plot symbol.  "null" is transparent.
	**/
    private Color borderColor = Color.black;

    /**
	*  The fill color of the plot symbol.  "null" is transparent.
	**/
    private Color fillColor = null;

    /**
	*  This object may only be instantiated through sub-classes.
	**/
    protected PlotSymbol() {
    }

    /**
	*  Return the approximate diameter of this plot symbol in pixels.
	**/
    public int getSize() {
        return width;
    }

    /**
	*  Set the approximate diameter of this plot symbol in pixels.
	**/
    public void setSize(int size) {
        width = size;
    }

    /**
	*  Return the fill color for this plot symbol.  A value
	*  of "null" indicates that the plot symbol is to be
	*  drawn "transparent".
	**/
    public Color getFillColor() {
        return fillColor;
    }

    /**
	*  Set the fill color used by this plot symbol.
	*  A value of "null" indicates that the plot symbol should
	*  be drawn "transparent".
	**/
    public void setFillColor(Color color) {
        fillColor = color;
    }

    /**
	*  Return the border color of this plot symbol.  A value
	*  of "null" indicates that this plot symbol's border
	*  should not be drawn.
	**/
    public Color getBorderColor() {
        return borderColor;
    }

    /**
	*  Set the border color of this plot symbol.  A value
	*  of "null" indicates that this plot symbol's outline
	*  should not be drawn.
	**/
    public void setBorderColor(Color color) {
        borderColor = color;
    }

    /**
	*  Draws the plot symbol into the specified graphics
	*  context at the indicated coordinate.
	*
	*  @param  gc  The graphics context where the symbol will be drawn.
	*  @param  x   The horizontal position of the center of the symbol.
	*  @param  y   The vertical position of the center of the symbol.
	**/
    public abstract void draw(Graphics gc, int x, int y);

    /**
	*  Make a copy of this PlotSymbol object.
	*
	*  @return  Returns a clone of this object.
	**/
    public Object clone() {
        PlotSymbol newObject = null;
        try {
            newObject = (PlotSymbol) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newObject;
    }
}

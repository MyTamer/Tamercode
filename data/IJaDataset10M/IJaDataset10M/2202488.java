package puce.swing;

import puce.awt.*;
import java.awt.*;

public abstract class AbstractTableLayout implements TableLayout {

    private static final Dimension MAXIMUM_LAYOUT_SIZE = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

    /** Indicates whether or not the size of the cells are known for the last known
   * size of the container.  If valid is false or the container has been resized,
   * the cell sizes must be recalculated using calculateSize. */
    private boolean valid;

    /** Previous known width of the container */
    protected int oldWidth;

    /** Previous known height of the container */
    protected int oldHeight;

    protected abstract void calculateRowSizes(int innerHeight);

    protected abstract void calculateColumnSizes(int innerWidth);

    protected abstract void calculateColumnOffsets(int x, int width);

    protected abstract void calculateRowOffsets(int y, int height);

    /**
   * Calculates the sizes of the rows and columns based on the absolute and
   * relative sizes specified in <code>rowSpec</code> and <code>columnSpec</code>
   * and the size of the container.  The result is stored in <code>rowSize</code>
   * and <code>columnSize</code>.
   *
   * @param container    container using this TableLayout
   */
    protected void validateLayout(Container container) {
        Rectangle innerArea = AWTUtilities.calculateInnerArea(container, null);
        calculateSize(innerArea);
        setValid(true);
        oldWidth = innerArea.width;
        oldHeight = innerArea.height;
    }

    private void calculateSize(final Rectangle innerArea) {
        calculateColumnSizes(innerArea.width);
        calculateRowSizes(innerArea.height);
        calculateColumnOffsets(innerArea.x, innerArea.width);
        calculateRowOffsets(innerArea.y, innerArea.height);
    }

    protected boolean isValid() {
        return valid;
    }

    protected void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
   * Invalidates the layout, indicating that if the layout manager has cached
   * information it should be discarded.
   */
    public void invalidateLayout(Container target) {
        setValid(false);
    }

    protected boolean isValid(Container container) {
        Dimension d = container.getSize();
        return isValid() && (d.width == oldWidth) && (d.height == oldHeight);
    }

    /**
   * Returns the alignment along the x axis.  This specifies how the component
   * would like to be aligned relative to other components.  The value should be
   * a number between 0 and 1 where 0 represents alignment along the origin, 1 is
   * aligned the furthest away from the origin, 0.5 is centered, etc.
   *
   * @return unconditionally, 0.5
   */
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    /**
   * Returns the alignment along the y axis.  This specifies how the component
   * would like to be aligned relative to other components.  The value should be
   * a number between 0 and 1 where 0 represents alignment along the origin, 1 is
   * aligned the furthest away from the origin, 0.5 is centered, etc.
   *
   * @return unconditionally, 0.5
   */
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    /**
   * Returns the maximum dimensions for this layout given the components in the
   * specified target container.
   *
   * @param target the component which needs to be laid out
   *
   * @return unconditionally, a Dimension of Integer.MAX_VALUE by
   *         Integer.MAX_VALUE since TableLayout does not limit the
   *         maximum size of a container
   */
    public Dimension maximumLayoutSize(Container target) {
        return MAXIMUM_LAYOUT_SIZE;
    }

    public void ensureValidity(Container container) {
        if (!isValid(container)) {
            validateLayout(container);
        }
    }
}

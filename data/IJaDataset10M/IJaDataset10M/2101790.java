package com.ds.asm;

import prefuse.Constants;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.ImageFactory;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.util.StringLib;
import prefuse.visual.VisualItem;
import java.awt.*;
import java.awt.geom.*;

/**
 * Renderer that draws a label, which consists of a text string,
 * an image, or both.
 *
 * <p>When created using the default constructor, the renderer attempts
 * to use text from the "label" field. To use a different field, use the
 * appropriate constructor or use the {@link #setTextField(String)} method.
 * To perform custom String selection, subclass this Renderer and override the
 * {@link #getText(VisualItem)} method. When the text field is
 * <code>null</code>, no text label will be shown. Labels can span multiple
 * lines of text, determined by the presence of newline characters ('\n')
 * within the text string.</p>
 *
 * <p>By default, no image is shown. To show an image, the image field needs
 * to be set, either using the appropriate constructor or the
 * {@link #setImageField(String)} method. The value of the image field should
 * be a text string indicating the location of the image file to use. The
 * string should be either a URL, a file located on the current classpath,
 * or a file on the local filesystem. If found, the image will be managed
 * internally by an {@link ImageFactory} instance, which maintains a
 * cache of loaded images.</p>
 *
 * <p>The position of the image relative to text can be set using the
 * {@link #setImagePosition(int)} method. Images can be placed to the
 * left, right, above, or below the text. The horizontal and vertical
 * alignments of either the text or the image can be set explicitly
 * using the appropriate methods of this class (e.g.,
 * {@link #setHorizontalTextAlignment(int)}). By default, both the
 * text and images are centered along both the horizontal and
 * vertical directions.</p>
 *
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class BulletRenderer extends AbstractShapeRenderer {

    protected ImageFactory m_images = null;

    protected String m_delim = "\n";

    protected String m_labelName = "label";

    protected String m_imageName = null;

    protected int m_xAlign = Constants.CENTER;

    protected int m_yAlign = Constants.CENTER;

    protected int m_hTextAlign = Constants.CENTER;

    protected int m_vTextAlign = Constants.CENTER;

    protected int m_hImageAlign = Constants.CENTER;

    protected int m_vImageAlign = Constants.CENTER;

    protected int m_imagePos = Constants.LEFT;

    protected int m_horizBorder = 2;

    protected int m_vertBorder = 0;

    protected int m_imageMargin = 2;

    protected int m_arcWidth = 0;

    protected int m_arcHeight = 0;

    protected int m_maxTextWidth = -1;

    /** Transform used to scale and position images */
    AffineTransform m_transform = new AffineTransform();

    /** The holder for the currently computed bounding box */
    protected RectangularShape m_bbox = new Rectangle2D.Double();

    protected Point2D m_pt = new Point2D.Double();

    protected Font m_font;

    protected String m_text;

    protected Dimension m_textDim = new Dimension();

    /**
     * Create a new LabelRenderer. By default the field "label" is used
     * as the field name for looking up text, and no image is used.
     */
    public BulletRenderer() {
    }

    /**
     * Create a new LabelRenderer. Draws a text label using the given
     * text data field and does not draw an image.
     * @param textField the data field for the text label.
     */
    public BulletRenderer(String textField) {
        this.setTextField(textField);
    }

    /**
     * Create a new LabelRenderer. Draws a text label using the given text
     * data field, and draws the image at the location reported by the
     * given image data field.
     * @param textField the data field for the text label
     * @param imageField the data field for the image location. This value
     * in the data field should be a URL, a file within the current classpath,
     * a file on the filesystem, or null for no image. If the
     * <code>imageField</code> parameter is null, no images at all will be
     * drawn.
     */
    public BulletRenderer(String textField, String imageField) {
        setTextField(textField);
        setImageField(imageField);
    }

    /**
     * Rounds the corners of the bounding rectangle in which the text
     * string is rendered. This will only be seen if either the stroke
     * or fill color is non-transparent.
     * @param arcWidth the width of the curved corner
     * @param arcHeight the height of the curved corner
     */
    public void setRoundedCorner(int arcWidth, int arcHeight) {
        if ((arcWidth == 0 || arcHeight == 0) && !(m_bbox instanceof Rectangle2D)) {
            m_bbox = new Rectangle2D.Double();
        } else {
            if (!(m_bbox instanceof RoundRectangle2D)) m_bbox = new RoundRectangle2D.Double();
            ((RoundRectangle2D) m_bbox).setRoundRect(0, 0, 10, 10, arcWidth, arcHeight);
            m_arcWidth = arcWidth;
            m_arcHeight = arcHeight;
        }
    }

    /**
     * Get the field name to use for text labels.
     * @return the data field for text labels, or null for no text
     */
    public String getTextField() {
        return m_labelName;
    }

    /**
     * Set the field name to use for text labels.
     * @param textField the data field for text labels, or null for no text
     */
    public void setTextField(String textField) {
        m_labelName = textField;
    }

    /**
     * Sets the maximum width that should be allowed of the text label.
     * A value of -1 specifies no limit (this is the default).
     * @param maxWidth the maximum width of the text or -1 for no limit
     */
    public void setMaxTextWidth(int maxWidth) {
        m_maxTextWidth = maxWidth;
    }

    /**
     * Returns the text to draw. Subclasses can override this class to
     * perform custom text selection.
     * @param item the item to represent as a <code>String</code>
     * @return a <code>String</code> to draw
     */
    protected String getText(VisualItem item) {
        String s = null;
        if (item.canGetString(m_labelName)) {
            return item.getString(m_labelName);
        }
        return s;
    }

    /**
     * Get the data field for image locations. The value stored
     * in the data field should be a URL, a file within the current classpath,
     * a file on the filesystem, or null for no image.
     * @return the data field for image locations, or null for no images
     */
    public String getImageField() {
        return m_imageName;
    }

    /**
     * Set the data field for image locations. The value stored
     * in the data field should be a URL, a file within the current classpath,
     * a file on the filesystem, or null for no image. If the
     * <code>imageField</code> parameter is null, no images at all will be
     * drawn.
     * @param imageField the data field for image locations, or null for
     * no images
     */
    public void setImageField(String imageField) {
        if (imageField != null) m_images = new ImageFactory();
        m_imageName = imageField;
    }

    /**
     * Sets the maximum image dimensions, used to control scaling of loaded
     * images. This scaling is enforced immediately upon loading of the image.
     * @param width the maximum width of images (-1 for no limit)
     * @param height the maximum height of images (-1 for no limit)
     */
    public void setMaxImageDimensions(int width, int height) {
        if (m_images == null) m_images = new ImageFactory();
        m_images.setMaxImageDimensions(width, height);
    }

    /**
     * Returns a location string for the image to draw. Subclasses can override
     * this class to perform custom image selection beyond looking up the value
     * from a data field.
     * @param item the item for which to select an image to draw
     * @return the location string for the image to use, or null for no image
     */
    protected String getImageLocation(VisualItem item) {
        return item.canGetString(m_imageName) ? item.getString(m_imageName) : null;
    }

    /**
     * Get the image to include in the label for the given VisualItem.
     * @param item the item to get an image for
     * @return the image for the item, or null for no image
     */
    protected Image getImage(VisualItem item) {
        String imageLoc = getImageLocation(item);
        return (imageLoc == null ? null : m_images.getImage(imageLoc));
    }

    private String computeTextDimensions(VisualItem item, String text, double size) {
        m_font = item.getFont();
        if (size != 1) {
            m_font = FontLib.getFont(m_font.getName(), m_font.getStyle(), size * m_font.getSize());
        }
        FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
        StringBuffer str = null;
        int nlines = 1, w = 0, start = 0, end = text.indexOf(m_delim);
        m_textDim.width = 0;
        String line;
        for (; end >= 0; ++nlines) {
            w = fm.stringWidth(line = text.substring(start, end));
            if (m_maxTextWidth > -1 && w > m_maxTextWidth) {
                if (str == null) str = new StringBuffer(text.substring(0, start));
                str.append(StringLib.abbreviate(line, fm, m_maxTextWidth));
                str.append(m_delim);
                w = m_maxTextWidth;
            } else if (str != null) {
                str.append(line).append(m_delim);
            }
            m_textDim.width = Math.max(m_textDim.width, w);
            start = end + 1;
            end = text.indexOf(m_delim, start);
        }
        w = fm.stringWidth(line = text.substring(start));
        if (m_maxTextWidth > -1 && w > m_maxTextWidth) {
            if (str == null) str = new StringBuffer(text.substring(0, start));
            str.append(StringLib.abbreviate(line, fm, m_maxTextWidth));
            w = m_maxTextWidth;
        } else if (str != null) {
            str.append(line);
        }
        m_textDim.width = Math.max(m_textDim.width, w);
        m_textDim.height = fm.getHeight() * nlines;
        return str == null ? text : str.toString();
    }

    /**
     * @see prefuse.render.AbstractShapeRenderer#getRawShape(prefuse.visual.VisualItem)
     */
    protected Shape getRawShape(VisualItem item) {
        m_text = getText(item);
        Image img = getImage(item);
        double size = item.getSize();
        double iw = 0, ih = 0;
        if (img != null) {
            ih = img.getHeight(null);
            iw = img.getWidth(null);
        }
        int tw = 0, th = 0;
        if (m_text != null) {
            m_text = computeTextDimensions(item, m_text, size);
            th = m_textDim.height;
            tw = m_textDim.width;
        }
        double w = 0, h = 0;
        switch(m_imagePos) {
            case Constants.LEFT:
            case Constants.RIGHT:
                w = tw + size * (iw + 2 * m_horizBorder + (tw > 0 && iw > 0 ? m_imageMargin : 0));
                h = Math.max(th, size * ih) + size * 2 * m_vertBorder;
                break;
            case Constants.TOP:
            case Constants.BOTTOM:
                w = Math.max(tw, size * iw) + size * 2 * m_horizBorder;
                h = th + size * (ih + 2 * m_vertBorder + (th > 0 && ih > 0 ? m_imageMargin : 0));
                break;
            default:
                throw new IllegalStateException("Unrecognized image alignment setting.");
        }
        getAlignedPoint(m_pt, item, w, h, m_xAlign, m_yAlign);
        if (m_bbox instanceof RoundRectangle2D) {
            RoundRectangle2D rr = (RoundRectangle2D) m_bbox;
            rr.setRoundRect(m_pt.getX(), m_pt.getY(), w, h, size * m_arcWidth, size * m_arcHeight);
        } else {
            m_bbox.setFrame(m_pt.getX(), m_pt.getY(), w, h);
        }
        return m_bbox;
    }

    /**
     * Helper method, which calculates the top-left co-ordinate of an item
     * given the item's alignment.
     */
    protected static void getAlignedPoint(Point2D p, VisualItem item, double w, double h, int xAlign, int yAlign) {
        double x = item.getX(), y = item.getY();
        if (Double.isNaN(x) || Double.isInfinite(x)) x = 0;
        if (Double.isNaN(y) || Double.isInfinite(y)) y = 0;
        if (xAlign == Constants.CENTER) {
            x = x - (w / 2);
        } else if (xAlign == Constants.RIGHT) {
            x = x - w;
        }
        if (yAlign == Constants.CENTER) {
            y = y - (h / 2);
        } else if (yAlign == Constants.BOTTOM) {
            y = y - h;
        }
        p.setLocation(x, y);
    }

    /**
     * @see prefuse.render.Renderer#render(java.awt.Graphics2D, prefuse.visual.VisualItem)
     */
    public void render(Graphics2D g, VisualItem item) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RectangularShape shape = (RectangularShape) getShape(item);
        if (shape == null) return;
        int type = getRenderType(item);
        if (type == RENDER_TYPE_FILL || type == RENDER_TYPE_DRAW_AND_FILL) {
            int w = (int) shape.getWidth();
            int h = (int) shape.getHeight();
            int x = (int) shape.getX();
            int y = (int) shape.getY();
            g.setPaint(new GradientPaint(x + w / 2, y, new Color(250, 189, 96), x + w / 2, y + h, new Color(240, 226, 161, 150), false));
            GeneralPath shape1 = new GeneralPath();
            Arc2D arc = new Arc2D.Double(new Rectangle(x, y, 15, h), 90.0, 180.0, Arc2D.OPEN);
            Arc2D arc1 = new Arc2D.Double(new Rectangle(x + w - 15, y, 15, h), 270.0, 180.0, Arc2D.OPEN);
            shape1.append(arc, false);
            shape1.append(new Rectangle(x + 7, y, w - 7 - 7, h), false);
            shape1.append(arc1, false);
            g.fill(shape1);
        }
        String text = m_text;
        Image img = getImage(item);
        if (text == null && img == null) return;
        double size = item.getSize();
        boolean useInt = 1.5 > Math.max(g.getTransform().getScaleX(), g.getTransform().getScaleY());
        double x = shape.getMinX() + size * m_horizBorder;
        double y = shape.getMinY() + size * m_vertBorder;
        if (img != null) {
            double w = size * img.getWidth(null);
            double h = size * img.getHeight(null);
            double ix = x, iy = y;
            switch(m_imagePos) {
                case Constants.LEFT:
                    x += w + size * m_imageMargin;
                    break;
                case Constants.RIGHT:
                    ix = shape.getMaxX() - size * m_horizBorder - w;
                    break;
                case Constants.TOP:
                    y += h + size * m_imageMargin;
                    break;
                case Constants.BOTTOM:
                    iy = shape.getMaxY() - size * m_vertBorder - h;
                    break;
                default:
                    throw new IllegalStateException("Unrecognized image alignment setting.");
            }
            switch(m_imagePos) {
                case Constants.LEFT:
                case Constants.RIGHT:
                    switch(m_vImageAlign) {
                        case Constants.TOP:
                            break;
                        case Constants.BOTTOM:
                            iy = shape.getMaxY() - size * m_vertBorder - h;
                            break;
                        case Constants.CENTER:
                            iy = shape.getCenterY() - h / 2;
                            break;
                    }
                    break;
                case Constants.TOP:
                case Constants.BOTTOM:
                    switch(m_hImageAlign) {
                        case Constants.LEFT:
                            break;
                        case Constants.RIGHT:
                            ix = shape.getMaxX() - size * m_horizBorder - w;
                            break;
                        case Constants.CENTER:
                            ix = shape.getCenterX() - w / 2;
                            break;
                    }
                    break;
            }
            if (useInt && size == 1.0) {
                g.drawImage(img, (int) ix, (int) iy, null);
            } else {
                m_transform.setTransform(size, 0, 0, size, ix, iy);
                g.drawImage(img, m_transform, null);
            }
        }
        int textColor = item.getTextColor();
        if (text != null && ColorLib.alpha(textColor) > 0) {
            g.setPaint(ColorLib.getColor(textColor));
            g.setFont(m_font);
            FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
            double tw;
            switch(m_imagePos) {
                case Constants.TOP:
                case Constants.BOTTOM:
                    tw = shape.getWidth() - 2 * size * m_horizBorder;
                    break;
                default:
                    tw = m_textDim.width;
            }
            double th;
            switch(m_imagePos) {
                case Constants.LEFT:
                case Constants.RIGHT:
                    th = shape.getHeight() - 2 * size * m_vertBorder;
                    break;
                default:
                    th = m_textDim.height;
            }
            y += fm.getAscent();
            switch(m_vTextAlign) {
                case Constants.TOP:
                    break;
                case Constants.BOTTOM:
                    y += th - m_textDim.height;
                    break;
                case Constants.CENTER:
                    y += (th - m_textDim.height) / 2;
            }
            int lh = fm.getHeight();
            int start = 0, end = text.indexOf(m_delim);
            for (; end >= 0; y += lh) {
                drawString(g, fm, text.substring(start, end), useInt, x, y, tw);
                start = end + 1;
                end = text.indexOf(m_delim, start);
            }
            drawString(g, fm, text.substring(start), useInt, x, y, tw);
        }
        if (type == RENDER_TYPE_DRAW || type == RENDER_TYPE_DRAW_AND_FILL) {
        }
    }

    private final void drawString(Graphics2D g, FontMetrics fm, String text, boolean useInt, double x, double y, double w) {
        double tx;
        switch(m_hTextAlign) {
            case Constants.LEFT:
                tx = x;
                break;
            case Constants.RIGHT:
                tx = x + w - fm.stringWidth(text);
                break;
            case Constants.CENTER:
                tx = x + (w - fm.stringWidth(text)) / 2;
                break;
            default:
                throw new IllegalStateException("Unrecognized text alignment setting.");
        }
        if (useInt) {
            g.drawString(text, (int) tx, (int) y);
        } else {
            g.drawString(text, (float) tx, (float) y);
        }
    }

    /**
     * Returns the image factory used by this renderer.
     * @return the image factory
     */
    public ImageFactory getImageFactory() {
        if (m_images == null) m_images = new ImageFactory();
        return m_images;
    }

    /**
     * Sets the image factory used by this renderer.
     * @param ifact the image factory
     */
    public void setImageFactory(ImageFactory ifact) {
        m_images = ifact;
    }

    /**
     * Get the horizontal text alignment within the layout. One of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}. The default is centered text.
     * @return the horizontal text alignment
     */
    public int getHorizontalTextAlignment() {
        return m_hTextAlign;
    }

    /**
     * Set the horizontal text alignment within the layout. One of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}. The default is centered text.
     * @param halign the desired horizontal text alignment
     */
    public void setHorizontalTextAlignment(int halign) {
        if (halign != Constants.LEFT && halign != Constants.RIGHT && halign != Constants.CENTER) throw new IllegalArgumentException("Illegal horizontal text alignment value.");
        m_hTextAlign = halign;
    }

    /**
     * Get the vertical text alignment within the layout. One of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}. The default is centered text.
     * @return the vertical text alignment
     */
    public int getVerticalTextAlignment() {
        return m_vTextAlign;
    }

    /**
     * Set the vertical text alignment within the layout. One of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}. The default is centered text.
     * @param valign the desired vertical text alignment
     */
    public void setVerticalTextAlignment(int valign) {
        if (valign != Constants.TOP && valign != Constants.BOTTOM && valign != Constants.CENTER) throw new IllegalArgumentException("Illegal vertical text alignment value.");
        m_vTextAlign = valign;
    }

    /**
     * Get the horizontal image alignment within the layout. One of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}. The default is a centered image.
     * @return the horizontal image alignment
     */
    public int getHorizontalImageAlignment() {
        return m_hImageAlign;
    }

    /**
     * Set the horizontal image alignment within the layout. One of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}. The default is a centered image.
     * @param halign the desired horizontal image alignment
     */
    public void setHorizontalImageAlignment(int halign) {
        if (halign != Constants.LEFT && halign != Constants.RIGHT && halign != Constants.CENTER) throw new IllegalArgumentException("Illegal horizontal text alignment value.");
        m_hImageAlign = halign;
    }

    /**
     * Get the vertical image alignment within the layout. One of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}. The default is a centered image.
     * @return the vertical image alignment
     */
    public int getVerticalImageAlignment() {
        return m_vImageAlign;
    }

    /**
     * Set the vertical image alignment within the layout. One of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}. The default is a centered image.
     * @param valign the desired vertical image alignment
     */
    public void setVerticalImageAlignment(int valign) {
        if (valign != Constants.TOP && valign != Constants.BOTTOM && valign != Constants.CENTER) throw new IllegalArgumentException("Illegal vertical text alignment value.");
        m_vImageAlign = valign;
    }

    /**
     * Get the image position, determining where the image is placed with
     * respect to the text. One of {@link Constants#LEFT},
     * {@link Constants#RIGHT}, {@link Constants#TOP}, or
     * {@link Constants#BOTTOM}.  The default is left.
     * @return the image position
     */
    public int getImagePosition() {
        return m_imagePos;
    }

    /**
     * Set the image position, determining where the image is placed with
     * respect to the text. One of {@link Constants#LEFT},
     * {@link Constants#RIGHT}, {@link Constants#TOP}, or
     * {@link Constants#BOTTOM}.  The default is left.
     * @param pos the desired image position
     */
    public void setImagePosition(int pos) {
        if (pos != Constants.TOP && pos != Constants.BOTTOM && pos != Constants.LEFT && pos != Constants.RIGHT && pos != Constants.CENTER) throw new IllegalArgumentException("Illegal image position value.");
        m_imagePos = pos;
    }

    /**
     * Get the horizontal alignment of this node with respect to its
     * x, y coordinates.
     * @return the horizontal alignment, one of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}.
     */
    public int getHorizontalAlignment() {
        return m_xAlign;
    }

    /**
     * Get the vertical alignment of this node with respect to its
     * x, y coordinates.
     * @return the vertical alignment, one of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}.
     */
    public int getVerticalAlignment() {
        return m_yAlign;
    }

    /**
     * Set the horizontal alignment of this node with respect to its
     * x, y coordinates.
     * @param align the horizontal alignment, one of
     * {@link prefuse.Constants#LEFT}, {@link prefuse.Constants#RIGHT}, or
     * {@link prefuse.Constants#CENTER}.
     */
    public void setHorizontalAlignment(int align) {
        m_xAlign = align;
    }

    /**
     * Set the vertical alignment of this node with respect to its
     * x, y coordinates.
     * @param align the vertical alignment, one of
     * {@link prefuse.Constants#TOP}, {@link prefuse.Constants#BOTTOM}, or
     * {@link prefuse.Constants#CENTER}.
     */
    public void setVerticalAlignment(int align) {
        m_yAlign = align;
    }

    /**
     * Returns the amount of padding in pixels between the content
     * and the border of this item along the horizontal dimension.
     * @return the horizontal padding
     */
    public int getHorizontalPadding() {
        return m_horizBorder;
    }

    /**
     * Sets the amount of padding in pixels between the content
     * and the border of this item along the horizontal dimension.
     * @param xpad the horizontal padding to set
     */
    public void setHorizontalPadding(int xpad) {
        m_horizBorder = xpad;
    }

    /**
     * Returns the amount of padding in pixels between the content
     * and the border of this item along the vertical dimension.
     * @return the vertical padding
     */
    public int getVerticalPadding() {
        return m_vertBorder;
    }

    /**
     * Sets the amount of padding in pixels between the content
     * and the border of this item along the vertical dimension.
     * @param ypad the vertical padding
     */
    public void setVerticalPadding(int ypad) {
        m_vertBorder = ypad;
    }

    /**
     * Get the padding, in pixels, between an image and text.
     * @return the padding between an image and text
     */
    public int getImageTextPadding() {
        return m_imageMargin;
    }

    /**
     * Set the padding, in pixels, between an image and text.
     * @param pad the padding to use between an image and text
     */
    public void setImageTextPadding(int pad) {
        m_imageMargin = pad;
    }
}

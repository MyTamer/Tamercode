package gnu.java.awt.java2d;

import java.awt.AWTError;
import java.awt.AlphaComposite;
import java.awt.AWTPermission;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a 100% Java implementation of the Java2D rendering pipeline. It is
 * meant as a base class for Graphics2D implementations.
 *
 * <h2>Backend interface</h2>
 * <p>
 * The backend must at the very least provide a Raster which the the rendering
 * pipeline can paint into. This must be implemented in
 * {@link #getDestinationRaster()}. For some backends that might be enough, like
 * when the target surface can be directly access via the raster (like in
 * BufferedImages). Other targets need some way to synchronize the raster with
 * the surface, which can be achieved by implementing the
 * {@link #updateRaster(Raster, int, int, int, int)} method, which always gets
 * called after a chunk of data got painted into the raster.
 * </p>
 * <p>The backend is free to provide implementations for the various raw*
 * methods for optimized AWT 1.1 style painting of some primitives. This should
 * accelerate painting of Swing greatly. When doing so, the backend must also
 * keep track of the clip and translation, probably by overriding
 * some clip and translate methods. Don't forget to message super in such a
 * case.</p>
 *
 * <h2>Acceleration options</h2>
 * <p>
 * The fact that it is
 * pure Java makes it a little slow. However, there are several ways of
 * accelerating the rendering pipeline:
 * <ol>
 * <li><em>Optimization hooks for AWT 1.1 - like graphics operations.</em>
 *   The most important methods from the {@link java.awt.Graphics} class
 *   have a corresponding <code>raw*</code> method, which get called when
 *   several optimization conditions are fullfilled. These conditions are
 *   described below. Subclasses can override these methods and delegate
 *   it directly to a native backend.</li>
 * <li><em>Native PaintContexts and CompositeContext.</em> The implementations
 *   for the 3 PaintContexts and AlphaCompositeContext can be accelerated
 *   using native code. These have proved to two of the most performance
 *   critical points in the rendering pipeline and cannot really be done quickly
 *   in plain Java because they involve lots of shuffling around with large
 *   arrays. In fact, you really would want to let the graphics card to the
 *   work, they are made for this.</li>
 * </ol>
 * </p>
 *
 * @author Roman Kennke (kennke@aicas.com)
 */
public abstract class AbstractGraphics2D extends Graphics2D implements Cloneable {

    /**
   * Accuracy of the sampling in the anti-aliasing shape filler.
   * Lower values give more speed, while higher values give more quality.
   * It is advisable to choose powers of two.
   */
    private static final int AA_SAMPLING = 8;

    /**
   * The transformation for this Graphics2D instance
   */
    protected AffineTransform transform;

    /**
   * The foreground.
   */
    private Paint paint;

    /**
   * The background.
   */
    private Color background;

    /**
   * The current font.
   */
    private Font font;

    /**
   * The current composite setting.
   */
    private Composite composite;

    /**
   * The current stroke setting.
   */
    private Stroke stroke;

    /**
   * The current clip. This clip is in user coordinate space.
   */
    private Shape clip;

    /**
   * The rendering hints.
   */
    private RenderingHints renderingHints;

    /**
   * The paint raster.
   */
    private Raster paintRaster;

    /**
   * The raster of the destination surface. This is where the painting is
   * performed.
   */
    private WritableRaster destinationRaster;

    /**
   * Stores the alpha values for a scanline in the anti-aliasing shape
   * renderer.
   */
    private transient int[] alpha;

    /**
   * The edge table for the scanline conversion algorithms.
   */
    private transient ArrayList[] edgeTable;

    /**
   * Indicates if certain graphics primitives can be rendered in an optimized
   * fashion. This will be the case if the following conditions are met:
   * - The transform may only be a translation, no rotation, shearing or
   *   scaling.
   * - The paint must be a solid color.
   * - The composite must be an AlphaComposite.SrcOver.
   * - The clip must be a Rectangle.
   * - The stroke must be a plain BasicStroke().
   *
   * These conditions represent the standard settings of a new
   * AbstractGraphics2D object and will be the most commonly used setting
   * in Swing rendering and should therefore be optimized as much as possible.
   */
    private boolean isOptimized;

    /**
   * Creates a new AbstractGraphics2D instance.
   */
    protected AbstractGraphics2D() {
        transform = new AffineTransform();
        background = Color.WHITE;
        composite = AlphaComposite.SrcOver;
        stroke = new BasicStroke();
        HashMap hints = new HashMap();
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        renderingHints = new RenderingHints(hints);
    }

    /**
   * Draws the specified shape. The shape is passed through the current stroke
   * and is then forwarded to {@link #fillShape}.
   *
   * @param shape the shape to draw
   */
    public void draw(Shape shape) {
        Shape strokedShape = stroke.createStrokedShape(shape);
        fillShape(strokedShape, false);
    }

    /**
   * Draws the specified image and apply the transform for image space ->
   * user space conversion.
   *
   * This method is implemented to special case RenderableImages and
   * RenderedImages and delegate to
   * {@link #drawRenderableImage(RenderableImage, AffineTransform)} and
   * {@link #drawRenderedImage(RenderedImage, AffineTransform)} accordingly.
   * Other image types are not yet handled.
   *
   * @param image the image to be rendered
   * @param xform the transform from image space to user space
   * @param obs the image observer to be notified
   */
    public boolean drawImage(Image image, AffineTransform xform, ImageObserver obs) {
        boolean ret = false;
        Rectangle areaOfInterest = new Rectangle(0, 0, image.getWidth(obs), image.getHeight(obs));
        return drawImageImpl(image, xform, obs, areaOfInterest);
    }

    /**
   * Draws the specified image and apply the transform for image space ->
   * user space conversion. This method only draw the part of the image
   * specified by <code>areaOfInterest</code>.
   *
   * This method is implemented to special case RenderableImages and
   * RenderedImages and delegate to
   * {@link #drawRenderableImage(RenderableImage, AffineTransform)} and
   * {@link #drawRenderedImage(RenderedImage, AffineTransform)} accordingly.
   * Other image types are not yet handled.
   *
   * @param image the image to be rendered
   * @param xform the transform from image space to user space
   * @param obs the image observer to be notified
   * @param areaOfInterest the area in image space that is rendered
   */
    private boolean drawImageImpl(Image image, AffineTransform xform, ImageObserver obs, Rectangle areaOfInterest) {
        boolean ret;
        if (image == null) {
            ret = true;
        } else if (image instanceof RenderedImage) {
            drawRenderedImageImpl((RenderedImage) image, xform, areaOfInterest);
            ret = true;
        } else if (image instanceof RenderableImage) {
            drawRenderableImageImpl((RenderableImage) image, xform, areaOfInterest);
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
   * Renders a BufferedImage and applies the specified BufferedImageOp before
   * to filter the BufferedImage somehow. The resulting BufferedImage is then
   * passed on to {@link #drawRenderedImage(RenderedImage, AffineTransform)}
   * to perform the final rendering.
   *
   * @param image the source buffered image
   * @param op the filter to apply to the buffered image before rendering
   * @param x the x coordinate to render the image to 
   * @param y the y coordinate to render the image to 
   */
    public void drawImage(BufferedImage image, BufferedImageOp op, int x, int y) {
        BufferedImage filtered = op.createCompatibleDestImage(image, image.getColorModel());
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        drawRenderedImage(filtered, t);
    }

    /**
   * Renders the specified image to the destination raster. The specified
   * transform is used to convert the image into user space. The transform
   * of this AbstractGraphics2D object is used to transform from user space
   * to device space.
   * 
   * The rendering is performed using the scanline algorithm that performs the
   * rendering of other shapes and a custom Paint implementation, that supplies
   * the pixel values of the rendered image.
   *
   * @param image the image to render to the destination raster
   * @param xform the transform from image space to user space
   */
    public void drawRenderedImage(RenderedImage image, AffineTransform xform) {
        Rectangle areaOfInterest = new Rectangle(image.getMinX(), image.getHeight(), image.getWidth(), image.getHeight());
        drawRenderedImageImpl(image, xform, areaOfInterest);
    }

    /**
   * Renders the specified image to the destination raster. The specified
   * transform is used to convert the image into user space. The transform
   * of this AbstractGraphics2D object is used to transform from user space
   * to device space. Only the area specified by <code>areaOfInterest</code>
   * is finally rendered to the target.
   * 
   * The rendering is performed using the scanline algorithm that performs the
   * rendering of other shapes and a custom Paint implementation, that supplies
   * the pixel values of the rendered image.
   *
   * @param image the image to render to the destination raster
   * @param xform the transform from image space to user space
   */
    private void drawRenderedImageImpl(RenderedImage image, AffineTransform xform, Rectangle areaOfInterest) {
        AffineTransform t = new AffineTransform();
        t.translate(-areaOfInterest.x - image.getMinX(), -areaOfInterest.y - image.getMinY());
        t.concatenate(xform);
        t.concatenate(transform);
        AffineTransform it = null;
        try {
            it = t.createInverse();
        } catch (NoninvertibleTransformException ex) {
        }
        if (it != null) {
            GeneralPath aoi = new GeneralPath(areaOfInterest);
            aoi.transform(xform);
            ImagePaint p = new ImagePaint(image, it);
            Paint savedPaint = paint;
            try {
                paint = p;
                fillShape(aoi, false);
            } finally {
                paint = savedPaint;
            }
        }
    }

    /**
   * Renders a renderable image. This produces a RenderedImage, which is
   * then passed to {@link #drawRenderedImage(RenderedImage, AffineTransform)}
   * to perform the final rendering.
   *
   * @param image the renderable image to be rendered
   * @param xform the transform from image space to user space
   */
    public void drawRenderableImage(RenderableImage image, AffineTransform xform) {
        Rectangle areaOfInterest = new Rectangle((int) image.getMinX(), (int) image.getHeight(), (int) image.getWidth(), (int) image.getHeight());
        drawRenderableImageImpl(image, xform, areaOfInterest);
    }

    /**
   * Renders a renderable image. This produces a RenderedImage, which is
   * then passed to {@link #drawRenderedImage(RenderedImage, AffineTransform)}
   * to perform the final rendering. Only the area of the image specified
   * by <code>areaOfInterest</code> is rendered.
   *
   * @param image the renderable image to be rendered
   * @param xform the transform from image space to user space
   */
    private void drawRenderableImageImpl(RenderableImage image, AffineTransform xform, Rectangle areaOfInterest) {
        RenderedImage rendered = image.createDefaultRendering();
        drawRenderedImageImpl(rendered, xform, areaOfInterest);
    }

    /**
   * Draws the specified string at the specified location.
   *
   * @param text the string to draw
   * @param x the x location, relative to the bounding rectangle of the text
   * @param y the y location, relative to the bounding rectangle of the text
   */
    public void drawString(String text, int x, int y) {
        if (isOptimized) rawDrawString(text, x, y); else {
            FontRenderContext ctx = getFontRenderContext();
            GlyphVector gv = font.createGlyphVector(ctx, text.toCharArray());
            drawGlyphVector(gv, x, y);
        }
    }

    /**
   * Draws the specified string at the specified location.
   *
   * @param text the string to draw
   * @param x the x location, relative to the bounding rectangle of the text
   * @param y the y location, relative to the bounding rectangle of the text
   */
    public void drawString(String text, float x, float y) {
        FontRenderContext ctx = getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(ctx, text.toCharArray());
        drawGlyphVector(gv, x, y);
    }

    /**
   * Draws the specified string (as AttributedCharacterIterator) at the
   * specified location.
   *
   * @param iterator the string to draw
   * @param x the x location, relative to the bounding rectangle of the text
   * @param y the y location, relative to the bounding rectangle of the text
   */
    public void drawString(AttributedCharacterIterator iterator, int x, int y) {
        FontRenderContext ctx = getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(ctx, iterator);
        drawGlyphVector(gv, x, y);
    }

    /**
   * Draws the specified string (as AttributedCharacterIterator) at the
   * specified location.
   *
   * @param iterator the string to draw
   * @param x the x location, relative to the bounding rectangle of the text
   * @param y the y location, relative to the bounding rectangle of the text
   */
    public void drawString(AttributedCharacterIterator iterator, float x, float y) {
        FontRenderContext ctx = getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(ctx, iterator);
        drawGlyphVector(gv, x, y);
    }

    /**
   * Fills the specified shape with the current foreground.
   *
   * @param shape the shape to fill
   */
    public void fill(Shape shape) {
        fillShape(shape, false);
    }

    public boolean hit(Rectangle rect, Shape text, boolean onStroke) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
   * Sets the composite.
   *
   * @param comp the composite to set
   */
    public void setComposite(Composite comp) {
        if (!(comp instanceof AlphaComposite)) {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) sm.checkPermission(new AWTPermission("readDisplayPixels"));
        }
        composite = comp;
        if (!(comp.equals(AlphaComposite.SrcOver))) isOptimized = false; else updateOptimization();
    }

    /**
   * Sets the current foreground.
   *
   * @param p the foreground to set.
   */
    public void setPaint(Paint p) {
        if (p != null) {
            paint = p;
            if (!(paint instanceof Color)) isOptimized = false; else {
                updateOptimization();
            }
        }
    }

    /**
   * Sets the stroke for this graphics object.
   *
   * @param s the stroke to set
   */
    public void setStroke(Stroke s) {
        stroke = s;
        if (!stroke.equals(new BasicStroke())) isOptimized = false; else updateOptimization();
    }

    /**
   * Sets the specified rendering hint.
   *
   * @param hintKey the key of the rendering hint
   * @param hintValue the value
   */
    public void setRenderingHint(Key hintKey, Object hintValue) {
        renderingHints.put(hintKey, hintValue);
    }

    /**
   * Returns the rendering hint for the specified key.
   *
   * @param hintKey the rendering hint key
   *
   * @return the rendering hint for the specified key
   */
    public Object getRenderingHint(Key hintKey) {
        return renderingHints.get(hintKey);
    }

    /**
   * Sets the specified rendering hints.
   *
   * @param hints the rendering hints to set
   */
    public void setRenderingHints(Map hints) {
        renderingHints.clear();
        renderingHints.putAll(hints);
    }

    /**
   * Adds the specified rendering hints.
   *
   * @param hints the rendering hints to add
   */
    public void addRenderingHints(Map hints) {
        renderingHints.putAll(hints);
    }

    /**
   * Returns the current rendering hints.
   *
   * @return the current rendering hints
   */
    public RenderingHints getRenderingHints() {
        return (RenderingHints) renderingHints.clone();
    }

    /**
   * Translates the coordinate system by (x, y).
   *
   * @param x the translation X coordinate
   * @param y the translation Y coordinate 
   */
    public void translate(int x, int y) {
        transform.translate(x, y);
        if (clip != null) {
            if (clip instanceof Rectangle) {
                Rectangle r = (Rectangle) clip;
                r.x -= x;
                r.y -= y;
                setClip(r);
            } else {
                AffineTransform clipTransform = new AffineTransform();
                clipTransform.translate(-x, -y);
                updateClip(clipTransform);
            }
        }
    }

    /**
   * Translates the coordinate system by (tx, ty).
   *
   * @param tx the translation X coordinate
   * @param ty the translation Y coordinate 
   */
    public void translate(double tx, double ty) {
        transform.translate(tx, ty);
        if (clip != null) {
            if (clip instanceof Rectangle) {
                Rectangle r = (Rectangle) clip;
                r.x -= tx;
                r.y -= ty;
            } else {
                AffineTransform clipTransform = new AffineTransform();
                clipTransform.translate(-tx, -ty);
                updateClip(clipTransform);
            }
        }
    }

    /**
   * Rotates the coordinate system by <code>theta</code> degrees.
   *
   * @param theta the angle be which to rotate the coordinate system
   */
    public void rotate(double theta) {
        transform.rotate(theta);
        if (clip != null) {
            AffineTransform clipTransform = new AffineTransform();
            clipTransform.rotate(-theta);
            updateClip(clipTransform);
        }
        updateOptimization();
    }

    /**
   * Rotates the coordinate system by <code>theta</code> around the point
   * (x, y).
   *
   * @param theta the angle by which to rotate the coordinate system
   * @param x the point around which to rotate, X coordinate
   * @param y the point around which to rotate, Y coordinate
   */
    public void rotate(double theta, double x, double y) {
        transform.rotate(theta, x, y);
        if (clip != null) {
            AffineTransform clipTransform = new AffineTransform();
            clipTransform.rotate(-theta, x, y);
            updateClip(clipTransform);
        }
        updateOptimization();
    }

    /**
   * Scales the coordinate system by the factors <code>scaleX</code> and
   * <code>scaleY</code>.
   *
   * @param scaleX the factor by which to scale the X axis
   * @param scaleY the factor by which to scale the Y axis
   */
    public void scale(double scaleX, double scaleY) {
        transform.scale(scaleX, scaleY);
        if (clip != null) {
            AffineTransform clipTransform = new AffineTransform();
            clipTransform.scale(1 / scaleX, 1 / scaleY);
            updateClip(clipTransform);
        }
        updateOptimization();
    }

    /**
   * Shears the coordinate system by <code>shearX</code> and
   * <code>shearY</code>.
   *
   * @param shearX the X shearing
   * @param shearY the Y shearing
   */
    public void shear(double shearX, double shearY) {
        transform.shear(shearX, shearY);
        if (clip != null) {
            AffineTransform clipTransform = new AffineTransform();
            clipTransform.shear(-shearX, -shearY);
            updateClip(clipTransform);
        }
        updateOptimization();
    }

    /**
   * Transforms the coordinate system using the specified transform
   * <code>t</code>.
   *
   * @param t the transform
   */
    public void transform(AffineTransform t) {
        transform.concatenate(t);
        try {
            AffineTransform clipTransform = t.createInverse();
            updateClip(clipTransform);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        updateOptimization();
    }

    /**
   * Sets the transformation for this Graphics object.
   *
   * @param t the transformation to set
   */
    public void setTransform(AffineTransform t) {
        updateClip(transform);
        transform.setTransform(t);
        try {
            updateClip(transform.createInverse());
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        updateOptimization();
    }

    /**
   * Returns the transformation of this coordinate system.
   *
   * @return the transformation of this coordinate system
   */
    public AffineTransform getTransform() {
        return (AffineTransform) transform.clone();
    }

    /**
   * Returns the current foreground.
   *
   * @return the current foreground
   */
    public Paint getPaint() {
        return paint;
    }

    /**
   * Returns the current composite.
   *
   * @return the current composite
   */
    public Composite getComposite() {
        return composite;
    }

    /**
   * Sets the current background.
   *
   * @param color the background to set.
   */
    public void setBackground(Color color) {
        background = color;
    }

    /**
   * Returns the current background.
   *
   * @return the current background
   */
    public Color getBackground() {
        return background;
    }

    /**
   * Returns the current stroke.
   *
   * @return the current stroke
   */
    public Stroke getStroke() {
        return stroke;
    }

    /**
   * Intersects the clip of this graphics object with the specified clip.
   *
   * @param s the clip with which the current clip should be intersected
   */
    public void clip(Shape s) {
        if (clip == null) clip = s; else if (clip instanceof Rectangle && s instanceof Rectangle) {
            Rectangle clipRect = (Rectangle) clip;
            Rectangle r = (Rectangle) s;
            computeIntersection(r.x, r.y, r.width, r.height, clipRect);
            setClip(clipRect);
        } else {
            Area current;
            if (clip instanceof Area) current = (Area) clip; else current = new Area(clip);
            Area intersect;
            if (s instanceof Area) intersect = (Area) s; else intersect = new Area(s);
            current.intersect(intersect);
            clip = current;
            isOptimized = false;
            setClip(clip);
        }
    }

    public FontRenderContext getFontRenderContext() {
        return new FontRenderContext(transform, false, true);
    }

    /**
   * Draws the specified glyph vector at the specified location.
   *
   * @param gv the glyph vector to draw
   * @param x the location, x coordinate
   * @param y the location, y coordinate
   */
    public void drawGlyphVector(GlyphVector gv, float x, float y) {
        int numGlyphs = gv.getNumGlyphs();
        translate(x, y);
        for (int i = 0; i < numGlyphs; i++) {
            Shape o = gv.getGlyphOutline(i);
            fillShape(o, true);
        }
        translate(-x, -y);
    }

    /**
   * Creates a copy of this graphics object.
   *
   * @return a copy of this graphics object
   */
    public Graphics create() {
        AbstractGraphics2D copy = (AbstractGraphics2D) clone();
        return copy;
    }

    /**
   * Creates and returns a copy of this Graphics object. This should
   * be overridden by subclasses if additional state must be handled when
   * cloning. This is called by {@link #create()}.
   *
   * @return a copy of this Graphics object
   */
    protected Object clone() {
        try {
            AbstractGraphics2D copy = (AbstractGraphics2D) super.clone();
            if (clip instanceof Rectangle) copy.clip = new Rectangle((Rectangle) clip); else copy.clip = new GeneralPath(clip);
            copy.renderingHints = new RenderingHints(null);
            copy.renderingHints.putAll(renderingHints);
            copy.transform = new AffineTransform(transform);
            return copy;
        } catch (CloneNotSupportedException ex) {
            AWTError err = new AWTError("Unexpected exception while cloning");
            err.initCause(ex);
            throw err;
        }
    }

    /**
   * Returns the current foreground.
   */
    public Color getColor() {
        Color c = null;
        if (paint instanceof Color) c = (Color) paint;
        return c;
    }

    /**
   * Sets the current foreground.
   *
   * @param color the foreground to set
   */
    public void setColor(Color color) {
        setPaint(color);
    }

    public void setPaintMode() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setXORMode(Color color) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
   * Returns the current font.
   *
   * @return the current font
   */
    public Font getFont() {
        return font;
    }

    /**
   * Sets the font on this graphics object. When <code>f == null</code>, the
   * current setting is not changed.
   *
   * @param f the font to set
   */
    public void setFont(Font f) {
        if (f != null) font = f;
    }

    /**
   * Returns the font metrics for the specified font.
   *
   * @param font the font for which to fetch the font metrics
   *
   * @return the font metrics for the specified font
   */
    public FontMetrics getFontMetrics(Font font) {
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    /**
   * Returns the bounds of the current clip.
   *
   * @return the bounds of the current clip
   */
    public Rectangle getClipBounds() {
        Rectangle b = null;
        if (clip != null) b = clip.getBounds();
        return b;
    }

    /**
   * Intersects the current clipping region with the specified rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
    public void clipRect(int x, int y, int width, int height) {
        clip(new Rectangle(x, y, width, height));
    }

    /**
   * Sets the clip to the specified rectangle.
   *
   * @param x the x coordinate of the clip rectangle
   * @param y the y coordinate of the clip rectangle
   * @param width the width of the clip rectangle
   * @param height the height of the clip rectangle
   */
    public void setClip(int x, int y, int width, int height) {
        setClip(new Rectangle(x, y, width, height));
    }

    /**
   * Returns the current clip.
   *
   * @return the current clip
   */
    public Shape getClip() {
        return clip;
    }

    /**
   * Sets the current clipping area to <code>clip</code>.
   *
   * @param c the clip to set
   */
    public void setClip(Shape c) {
        clip = c;
        if (!(clip instanceof Rectangle)) isOptimized = false; else updateOptimization();
    }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {
        if (isOptimized) rawCopyArea(x, y, width, height, dx, dy); else copyAreaImpl(x, y, width, height, dx, dy);
    }

    /**
   * Draws a line from (x1, y1) to (x2, y2).
   *
   * This implementation transforms the coordinates and forwards the call to
   * {@link #rawDrawLine}.
   */
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (isOptimized) {
            int tx = (int) transform.getTranslateX();
            int ty = (int) transform.getTranslateY();
            rawDrawLine(x1 + tx, y1 + ty, x2 + tx, y2 + ty);
        } else {
            Line2D line = new Line2D.Double(x1, y1, x2, y2);
            draw(line);
        }
    }

    /**
   * Fills a rectangle with the current paint.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
    public void fillRect(int x, int y, int width, int height) {
        if (isOptimized) {
            int tx = (int) transform.getTranslateX();
            int ty = (int) transform.getTranslateY();
            rawFillRect(x + tx, y + ty, width, height);
        } else {
            fill(new Rectangle(x, y, width, height));
        }
    }

    /**
   * Fills a rectangle with the current background color.
   *
   * This implementation temporarily sets the foreground color to the 
   * background and forwards the call to {@link #fillRect(int, int, int, int)}.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   */
    public void clearRect(int x, int y, int width, int height) {
        if (isOptimized) rawClearRect(x, y, width, height); else {
            Paint savedForeground = getPaint();
            setPaint(getBackground());
            fillRect(x, y, width, height);
            setPaint(savedForeground);
        }
    }

    /**
   * Draws a rounded rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param arcWidth the width of the arcs
   * @param arcHeight the height of the arcs
   */
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        draw(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
    }

    /**
   * Fills a rounded rectangle.
   *
   * @param x the x coordinate of the rectangle
   * @param y the y coordinate of the rectangle
   * @param width the width of the rectangle
   * @param height the height of the rectangle
   * @param arcWidth the width of the arcs
   * @param arcHeight the height of the arcs
   */
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        fill(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight));
    }

    /**
   * Draws the outline of an oval.
   *
   * @param x the upper left corner of the bounding rectangle of the ellipse
   * @param y the upper left corner of the bounding rectangle of the ellipse
   * @param width the width of the ellipse
   * @param height the height of the ellipse
   */
    public void drawOval(int x, int y, int width, int height) {
        draw(new Ellipse2D.Double(x, y, width, height));
    }

    /**
   * Fills an oval.
   *
   * @param x the upper left corner of the bounding rectangle of the ellipse
   * @param y the upper left corner of the bounding rectangle of the ellipse
   * @param width the width of the ellipse
   * @param height the height of the ellipse
   */
    public void fillOval(int x, int y, int width, int height) {
        fill(new Ellipse2D.Double(x, y, width, height));
    }

    /**
   * Draws an arc.
   */
    public void drawArc(int x, int y, int width, int height, int arcStart, int arcAngle) {
        draw(new Arc2D.Double(x, y, width, height, arcStart, arcAngle, Arc2D.OPEN));
    }

    /**
   * Fills an arc.
   */
    public void fillArc(int x, int y, int width, int height, int arcStart, int arcAngle) {
        fill(new Arc2D.Double(x, y, width, height, arcStart, arcAngle, Arc2D.OPEN));
    }

    public void drawPolyline(int[] xPoints, int[] yPoints, int npoints) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
   * Draws the outline of a polygon.
   */
    public void drawPolygon(int[] xPoints, int[] yPoints, int npoints) {
        draw(new Polygon(xPoints, yPoints, npoints));
    }

    /**
   * Fills the outline of a polygon.
   */
    public void fillPolygon(int[] xPoints, int[] yPoints, int npoints) {
        fill(new Polygon(xPoints, yPoints, npoints));
    }

    /**
   * Draws the specified image at the specified location. This forwards
   * to {@link #drawImage(Image, AffineTransform, ImageObserver)}.
   *
   * @param image the image to render
   * @param x the x location to render to
   * @param y the y location to render to
   * @param observer the image observer to receive notification
   */
    public boolean drawImage(Image image, int x, int y, ImageObserver observer) {
        boolean ret;
        if (isOptimized) ret = rawDrawImage(image, x, y, observer); else {
            AffineTransform t = new AffineTransform();
            t.translate(x, y);
            ret = drawImage(image, t, observer);
        }
        return ret;
    }

    /**
   * Draws the specified image at the specified location. The image
   * is scaled to the specified width and height. This forwards
   * to {@link #drawImage(Image, AffineTransform, ImageObserver)}.
   *
   * @param image the image to render
   * @param x the x location to render to
   * @param y the y location to render to
   * @param width the target width of the image
   * @param height the target height of the image
   * @param observer the image observer to receive notification
   */
    public boolean drawImage(Image image, int x, int y, int width, int height, ImageObserver observer) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        double scaleX = (double) width / (double) image.getWidth(observer);
        double scaleY = (double) height / (double) image.getHeight(observer);
        t.scale(scaleX, scaleY);
        return drawImage(image, t, observer);
    }

    /**
   * Draws the specified image at the specified location. This forwards
   * to {@link #drawImage(Image, AffineTransform, ImageObserver)}.
   *
   * @param image the image to render
   * @param x the x location to render to
   * @param y the y location to render to
   * @param bgcolor the background color to use for transparent pixels
   * @param observer the image observer to receive notification
   */
    public boolean drawImage(Image image, int x, int y, Color bgcolor, ImageObserver observer) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        return drawImage(image, t, observer);
    }

    /**
   * Draws the specified image at the specified location. The image
   * is scaled to the specified width and height. This forwards
   * to {@link #drawImage(Image, AffineTransform, ImageObserver)}.
   *
   * @param image the image to render
   * @param x the x location to render to
   * @param y the y location to render to
   * @param width the target width of the image
   * @param height the target height of the image
   * @param bgcolor the background color to use for transparent pixels
   * @param observer the image observer to receive notification
   */
    public boolean drawImage(Image image, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        double scaleX = (double) image.getWidth(observer) / (double) width;
        double scaleY = (double) image.getHeight(observer) / (double) height;
        t.scale(scaleX, scaleY);
        return drawImage(image, t, observer);
    }

    /**
   * Draws an image fragment to a rectangular area of the target.
   *
   * @param image the image to render
   * @param dx1 the first corner of the destination rectangle
   * @param dy1 the first corner of the destination rectangle
   * @param dx2 the second corner of the destination rectangle
   * @param dy2 the second corner of the destination rectangle
   * @param sx1 the first corner of the source rectangle
   * @param sy1 the first corner of the source rectangle
   * @param sx2 the second corner of the source rectangle
   * @param sy2 the second corner of the source rectangle
   * @param observer the image observer to be notified
   */
    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
        int sx = Math.min(sx1, sx1);
        int sy = Math.min(sy1, sy2);
        int sw = Math.abs(sx1 - sx2);
        int sh = Math.abs(sy1 - sy2);
        int dx = Math.min(dx1, dx1);
        int dy = Math.min(dy1, dy2);
        int dw = Math.abs(dx1 - dx2);
        int dh = Math.abs(dy1 - dy2);
        AffineTransform t = new AffineTransform();
        t.translate(sx - dx, sy - dy);
        double scaleX = (double) sw / (double) dw;
        double scaleY = (double) sh / (double) dh;
        t.scale(scaleX, scaleY);
        Rectangle areaOfInterest = new Rectangle(sx, sy, sw, sh);
        return drawImageImpl(image, t, observer, areaOfInterest);
    }

    /**
   * Draws an image fragment to a rectangular area of the target.
   *
   * @param image the image to render
   * @param dx1 the first corner of the destination rectangle
   * @param dy1 the first corner of the destination rectangle
   * @param dx2 the second corner of the destination rectangle
   * @param dy2 the second corner of the destination rectangle
   * @param sx1 the first corner of the source rectangle
   * @param sy1 the first corner of the source rectangle
   * @param sx2 the second corner of the source rectangle
   * @param sy2 the second corner of the source rectangle
   * @param bgcolor the background color to use for transparent pixels
   * @param observer the image observer to be notified
   */
    public boolean drawImage(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
        return drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
    }

    /**
   * Disposes this graphics object.
   */
    public void dispose() {
    }

    /**
   * Fills the specified shape. The shape has already been clipped against the
   * current clip.
   *
   * @param s the shape to fill
   * @param isFont <code>true</code> if the shape is a font outline
   */
    protected void fillShape(Shape s, boolean isFont) {
        boolean antialias = false;
        if (isFont) {
            Object v = renderingHints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
            antialias = (v == RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            Object v = renderingHints.get(RenderingHints.KEY_ANTIALIASING);
            antialias = (v == RenderingHints.VALUE_ANTIALIAS_ON);
        }
        Rectangle2D userBounds = s.getBounds2D();
        Rectangle2D deviceBounds = new Rectangle2D.Double();
        ArrayList segs = getSegments(s, transform, deviceBounds, false);
        Rectangle2D clipBounds = new Rectangle2D.Double();
        ArrayList clipSegs = getSegments(clip, transform, clipBounds, true);
        segs.addAll(clipSegs);
        Rectangle2D inclClipBounds = new Rectangle2D.Double();
        Rectangle2D.union(clipBounds, deviceBounds, inclClipBounds);
        if (segs.size() > 0) {
            if (antialias) fillShapeAntialias(segs, deviceBounds, userBounds, inclClipBounds); else fillShapeImpl(segs, deviceBounds, userBounds, inclClipBounds);
        }
    }

    /**
   * Returns the color model of this Graphics object.
   *
   * @return the color model of this Graphics object
   */
    protected abstract ColorModel getColorModel();

    /**
   * Returns the bounds of the target.
   *
   * @return the bounds of the target
   */
    protected Rectangle getDeviceBounds() {
        return destinationRaster.getBounds();
    }

    /**
   * Draws a line in optimization mode. The implementation should respect the
   * clip and translation. It can assume that the clip is a rectangle and that
   * the transform is only a translating transform.
   *
   * @param x0 the starting point, X coordinate
   * @param y0 the starting point, Y coordinate
   * @param x1 the end point, X coordinate 
   * @param y1 the end point, Y coordinate
   */
    protected void rawDrawLine(int x0, int y0, int x1, int y1) {
        draw(new Line2D.Float(x0, y0, x1, y1));
    }

    /**
   * Draws a string in optimization mode. The implementation should respect the
   * clip and translation. It can assume that the clip is a rectangle and that
   * the transform is only a translating transform.
   *
   * @param text the string to be drawn
   * @param x the start of the baseline, X coordinate
   * @param y the start of the baseline, Y coordinate
   */
    protected void rawDrawString(String text, int x, int y) {
        FontRenderContext ctx = getFontRenderContext();
        GlyphVector gv = font.createGlyphVector(ctx, text.toCharArray());
        drawGlyphVector(gv, x, y);
    }

    /**
   * Clears a rectangle in optimization mode. The implementation should respect the
   * clip and translation. It can assume that the clip is a rectangle and that
   * the transform is only a translating transform.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param w the width
   * @param h the height
   */
    protected void rawClearRect(int x, int y, int w, int h) {
        Paint savedForeground = getPaint();
        setPaint(getBackground());
        rawFillRect(x, y, w, h);
        setPaint(savedForeground);
    }

    /**
   * Fills a rectangle in optimization mode. The implementation should respect
   * the clip but can assume that it is a rectangle.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param w the width
   * @param h the height
   */
    protected void rawFillRect(int x, int y, int w, int h) {
        fill(new Rectangle(x, y, w, h));
    }

    /**
   * Draws an image in optimization mode. The implementation should respect
   * the clip but can assume that it is a rectangle.
   *
   * @param image the image to be painted
   * @param x the location, X coordinate
   * @param y the location, Y coordinate
   * @param obs the image observer to be notified
   *
   * @return <code>true</code> when the image is painted completely,
   *         <code>false</code> if it is still rendered
   */
    protected boolean rawDrawImage(Image image, int x, int y, ImageObserver obs) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        return drawImage(image, t, obs);
    }

    /**
   * Copies a rectangular region to another location.
   *
   * @param x the upper left corner, X coordinate
   * @param y the upper left corner, Y coordinate
   * @param w the width
   * @param h the height
   * @param dx
   * @param dy
   */
    protected void rawCopyArea(int x, int y, int w, int h, int dx, int dy) {
        copyAreaImpl(x, y, w, h, dx, dy);
    }

    /**
   * Copies a rectangular area of the target raster to a different location.
   */
    private void copyAreaImpl(int x, int y, int w, int h, int dx, int dy) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
   * Fills the specified polygon. This should be overridden by backends
   * that support accelerated (native) polygon filling, which is the
   * case for most toolkit window and offscreen image implementations.
   *
   * The polygon is already clipped when this method is called.
   */
    private void fillShapeImpl(ArrayList segs, Rectangle2D deviceBounds2D, Rectangle2D userBounds, Rectangle2D inclClipBounds) {
        double minX = deviceBounds2D.getMinX();
        double minY = deviceBounds2D.getMinY();
        double maxX = deviceBounds2D.getMaxX();
        double maxY = deviceBounds2D.getMaxY();
        double icMinY = inclClipBounds.getMinY();
        double icMaxY = inclClipBounds.getMaxY();
        Rectangle deviceBounds = new Rectangle((int) minX, (int) minY, (int) Math.ceil(maxX) - (int) minX, (int) Math.ceil(maxY) - (int) minY);
        PaintContext pCtx = paint.createContext(getColorModel(), deviceBounds, userBounds, transform, renderingHints);
        ArrayList[] edgeTable = new ArrayList[(int) Math.ceil(icMaxY) - (int) Math.ceil(icMinY) + 1];
        for (Iterator i = segs.iterator(); i.hasNext(); ) {
            PolyEdge edge = (PolyEdge) i.next();
            int yindex = (int) ((int) Math.ceil(edge.y0) - (int) Math.ceil(icMinY));
            if (edgeTable[yindex] == null) edgeTable[yindex] = new ArrayList();
            edgeTable[yindex].add(edge);
        }
        ArrayList activeEdges = new ArrayList();
        PolyEdgeComparator comparator = new PolyEdgeComparator();
        int minYInt = (int) Math.ceil(icMinY);
        Rectangle devClip = getDeviceBounds();
        int scanlineMax = (int) Math.min(maxY, devClip.getMaxY());
        for (int y = minYInt; y < scanlineMax; y++) {
            ArrayList bucket = edgeTable[y - minYInt];
            for (Iterator i = activeEdges.iterator(); i.hasNext(); ) {
                PolyEdge edge = (PolyEdge) i.next();
                if (y > edge.y1) i.remove(); else {
                    edge.xIntersection += edge.slope;
                }
            }
            if (bucket != null) activeEdges.addAll(bucket);
            int size = activeEdges.size();
            if (size > 1) {
                for (int i = 1; i < size; i++) {
                    PolyEdge e1 = (PolyEdge) activeEdges.get(i - 1);
                    PolyEdge e2 = (PolyEdge) activeEdges.get(i);
                    if (comparator.compare(e1, e2) > 0) {
                        int j = i;
                        do {
                            activeEdges.set(j, e1);
                            activeEdges.set(j - 1, e2);
                            j--;
                            if (j >= 1) e1 = (PolyEdge) activeEdges.get(j - 1);
                        } while (j >= 1 && comparator.compare(e1, e2) > 0);
                    }
                }
            }
            PolyEdge previous = null;
            boolean insideShape = false;
            boolean insideClip = false;
            for (Iterator i = activeEdges.iterator(); i.hasNext(); ) {
                PolyEdge edge = (PolyEdge) i.next();
                if (edge.y1 <= y) continue;
                if (insideClip && insideShape) {
                    int x0 = (int) previous.xIntersection;
                    int x1 = (int) edge.xIntersection;
                    if (x0 < x1) fillScanline(pCtx, x0, x1, y);
                }
                previous = edge;
                if (edge.isClip) insideClip = !insideClip; else insideShape = !insideShape;
            }
        }
        pCtx.dispose();
    }

    /**
   * Paints a scanline between x0 and x1.
   *
   * @param x0 the left offset
   * @param x1 the right offset
   * @param y the scanline
   */
    protected void fillScanline(PaintContext pCtx, int x0, int x1, int y) {
        Raster paintRaster = pCtx.getRaster(x0, y, x1 - x0, 1);
        ColorModel paintColorModel = pCtx.getColorModel();
        CompositeContext cCtx = composite.createContext(paintColorModel, getColorModel(), renderingHints);
        WritableRaster targetChild = destinationRaster.createWritableTranslatedChild(-x0, -y);
        cCtx.compose(paintRaster, targetChild, targetChild);
        updateRaster(destinationRaster, x0, y, x1 - x0, 1);
        cCtx.dispose();
    }

    /**
   * Fills arbitrary shapes in an anti-aliased fashion.
   *
   * @param segs the line segments which define the shape which is to be filled
   */
    private void fillShapeAntialias(ArrayList segs, Rectangle2D deviceBounds2D, Rectangle2D userBounds, Rectangle2D inclClipBounds) {
        double minX = deviceBounds2D.getMinX();
        double minY = deviceBounds2D.getMinY();
        double maxX = deviceBounds2D.getMaxX();
        double maxY = deviceBounds2D.getMaxY();
        double icMinY = inclClipBounds.getMinY();
        double icMaxY = inclClipBounds.getMaxY();
        double icMinX = inclClipBounds.getMinX();
        double icMaxX = inclClipBounds.getMaxX();
        Rectangle deviceBounds = new Rectangle((int) minX, (int) minY, (int) Math.ceil(maxX) - (int) minX, (int) Math.ceil(maxY) - (int) minY);
        PaintContext pCtx = paint.createContext(ColorModel.getRGBdefault(), deviceBounds, userBounds, transform, renderingHints);
        int numScanlines = (int) Math.ceil(icMaxY) - (int) icMinY;
        int numScanlinePixels = (int) Math.ceil(icMaxX) - (int) icMinX + 1;
        if (alpha == null || alpha.length < (numScanlinePixels + 1)) alpha = new int[numScanlinePixels + 1];
        int firstLine = (int) icMinY;
        int firstSubline = (int) (Math.ceil((icMinY - Math.floor(icMinY)) * AA_SAMPLING));
        double firstLineDouble = firstLine + firstSubline / (double) AA_SAMPLING;
        if (edgeTable == null || edgeTable.length < numScanlines * AA_SAMPLING + AA_SAMPLING) edgeTable = new ArrayList[numScanlines * AA_SAMPLING + AA_SAMPLING];
        for (Iterator i = segs.iterator(); i.hasNext(); ) {
            PolyEdge edge = (PolyEdge) i.next();
            int yindex = (int) (Math.ceil((edge.y0 - firstLineDouble) * AA_SAMPLING));
            edge.slope = ((edge.x1 - edge.x0) / (edge.y1 - edge.y0)) / AA_SAMPLING;
            if (edge.y0 == edge.y1) edge.xIntersection = Math.min(edge.x0, edge.x1); else {
                double alignedFirst = Math.ceil(edge.y0 * AA_SAMPLING) / AA_SAMPLING;
                edge.xIntersection = edge.x0 + (edge.slope * AA_SAMPLING) * (alignedFirst - edge.y0);
            }
            if (yindex >= 0 && yindex < edgeTable.length) {
                if (edgeTable[yindex] == null) edgeTable[yindex] = new ArrayList();
                edgeTable[yindex].add(edge);
            }
        }
        ArrayList activeEdges = new ArrayList();
        PolyEdgeComparator comparator = new PolyEdgeComparator();
        int yindex = 0;
        for (int y = firstLine; y <= icMaxY; y++) {
            int leftX = (int) icMaxX;
            int rightX = (int) icMinX;
            boolean emptyScanline = true;
            for (int subY = firstSubline; subY < AA_SAMPLING; subY++) {
                ArrayList bucket = edgeTable[yindex];
                for (Iterator i = activeEdges.iterator(); i.hasNext(); ) {
                    PolyEdge edge = (PolyEdge) i.next();
                    if ((y + ((double) subY / (double) AA_SAMPLING)) > edge.y1) i.remove(); else {
                        edge.xIntersection += edge.slope;
                    }
                }
                if (bucket != null) {
                    activeEdges.addAll(bucket);
                    edgeTable[yindex].clear();
                }
                int size = activeEdges.size();
                if (size > 1) {
                    for (int i = 1; i < size; i++) {
                        PolyEdge e1 = (PolyEdge) activeEdges.get(i - 1);
                        PolyEdge e2 = (PolyEdge) activeEdges.get(i);
                        if (comparator.compare(e1, e2) > 0) {
                            int j = i;
                            do {
                                activeEdges.set(j, e1);
                                activeEdges.set(j - 1, e2);
                                j--;
                                if (j >= 1) e1 = (PolyEdge) activeEdges.get(j - 1);
                            } while (j >= 1 && comparator.compare(e1, e2) > 0);
                        }
                    }
                }
                PolyEdge previous = null;
                boolean insideClip = false;
                boolean insideShape = false;
                for (Iterator i = activeEdges.iterator(); i.hasNext(); ) {
                    PolyEdge edge = (PolyEdge) i.next();
                    if (edge.y1 <= (y + (subY / (double) AA_SAMPLING))) continue;
                    if (insideClip && insideShape) {
                        if (edge.y1 > (y + (subY / (double) AA_SAMPLING))) {
                            int x0 = (int) Math.min(Math.max(previous.xIntersection, minX), maxX);
                            int x1 = (int) Math.min(Math.max(edge.xIntersection, minX), maxX);
                            int left = x0 - (int) minX;
                            int right = x1 - (int) minX + 1;
                            alpha[left]++;
                            alpha[right]--;
                            leftX = Math.min(x0, leftX);
                            rightX = Math.max(x1 + 2, rightX);
                            emptyScanline = false;
                        }
                    }
                    previous = edge;
                    if (edge.isClip) insideClip = !insideClip; else insideShape = !insideShape;
                }
                yindex++;
            }
            firstSubline = 0;
            if (!emptyScanline) fillScanlineAA(alpha, leftX, (int) y, rightX - leftX, pCtx, (int) minX);
        }
        pCtx.dispose();
    }

    /**
   * Fills a horizontal line between x0 and x1 for anti aliased rendering.
   * the alpha array contains the deltas of the alpha values from one pixel
   * to the next.
   *
   * @param alpha the alpha values in the scanline
   * @param x0 the beginning of the scanline
   * @param y the y coordinate of the line
   */
    private void fillScanlineAA(int[] alpha, int x0, int yy, int numPixels, PaintContext pCtx, int offs) {
        CompositeContext cCtx = composite.createContext(pCtx.getColorModel(), getColorModel(), renderingHints);
        Raster paintRaster = pCtx.getRaster(x0, yy, numPixels, 1);
        WritableRaster aaRaster = paintRaster.createCompatibleWritableRaster();
        int numBands = paintRaster.getNumBands();
        ColorModel cm = pCtx.getColorModel();
        double lastAlpha = 0.;
        int lastAlphaInt = 0;
        Object pixel = null;
        int[] comps = null;
        int x1 = x0 + numPixels;
        for (int x = x0; x < x1; x++) {
            int i = x - offs;
            if (alpha[i] != 0) {
                lastAlphaInt += alpha[i];
                lastAlpha = (double) lastAlphaInt / (double) AA_SAMPLING;
                alpha[i] = 0;
            }
            pixel = paintRaster.getDataElements(x - x0, 0, pixel);
            comps = cm.getComponents(pixel, comps, 0);
            if (cm.hasAlpha() && !cm.isAlphaPremultiplied()) comps[comps.length - 1] *= lastAlpha; else {
                int max;
                if (cm.hasAlpha()) max = comps.length - 2; else max = comps.length - 1;
                for (int j = 0; j < max; j++) comps[j] *= lastAlpha;
            }
            pixel = cm.getDataElements(comps, 0, pixel);
            aaRaster.setDataElements(x - x0, 0, pixel);
        }
        WritableRaster targetChild = destinationRaster.createWritableTranslatedChild(-x0, -yy);
        cCtx.compose(aaRaster, targetChild, targetChild);
        updateRaster(destinationRaster, x0, yy, numPixels, 1);
        cCtx.dispose();
    }

    /**
   * Initializes this graphics object. This must be called by subclasses in
   * order to correctly initialize the state of this object.
   */
    protected void init() {
        setPaint(Color.BLACK);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        isOptimized = true;
        destinationRaster = getDestinationRaster();
        clip = getDeviceBounds();
    }

    /**
   * Returns a WritableRaster that is used by this class to perform the
   * rendering in. It is not necessary that the target surface immediately
   * reflects changes in the raster. Updates to the raster are notified via
   * {@link #updateRaster}.
   *
   * @return the destination raster
   */
    protected WritableRaster getDestinationRaster() {
        Rectangle db = getDeviceBounds();
        if (destinationRaster == null) {
            int[] bandMasks = new int[] { 0xFF0000, 0xFF00, 0xFF };
            destinationRaster = Raster.createPackedRaster(DataBuffer.TYPE_INT, db.width, db.height, bandMasks, null);
            int x0 = destinationRaster.getMinX();
            int x1 = destinationRaster.getWidth() + x0;
            int y0 = destinationRaster.getMinY();
            int y1 = destinationRaster.getHeight() + y0;
            int numBands = destinationRaster.getNumBands();
            for (int y = y0; y < y1; y++) {
                for (int x = x0; x < x1; x++) {
                    for (int b = 0; b < numBands; b++) destinationRaster.setSample(x, y, b, 255);
                }
            }
        }
        return destinationRaster;
    }

    /**
   * Notifies the backend that the raster has changed in the specified
   * rectangular area. The raster that is provided in this method is always
   * the same as the one returned in {@link #getDestinationRaster}.
   * Backends that reflect changes to this raster directly don't need to do
   * anything here.
   *
   * @param raster the updated raster, identical to the raster returned
   *        by {@link #getDestinationRaster()}
   * @param x the upper left corner of the updated region, X coordinate
   * @param y the upper lef corner of the updated region, Y coordinate
   * @param w the width of the updated region
   * @param h the height of the updated region
   */
    protected void updateRaster(Raster raster, int x, int y, int w, int h) {
    }

    /**
   * Helper method to check and update the optimization conditions.
   */
    private void updateOptimization() {
        int transformType = transform.getType();
        boolean optimizedTransform = false;
        if (transformType == AffineTransform.TYPE_IDENTITY || transformType == AffineTransform.TYPE_TRANSLATION) optimizedTransform = true;
        boolean optimizedClip = (clip == null || clip instanceof Rectangle);
        isOptimized = optimizedClip && optimizedTransform && paint instanceof Color && composite == AlphaComposite.SrcOver && stroke.equals(new BasicStroke());
    }

    /**
   * Calculates the intersection of two rectangles. The result is stored
   * in <code>rect</code>. This is basically the same
   * like {@link Rectangle#intersection(Rectangle)}, only that it does not
   * create new Rectangle instances. The tradeoff is that you loose any data in
   * <code>rect</code>.
   *
   * @param x upper-left x coodinate of first rectangle
   * @param y upper-left y coodinate of first rectangle
   * @param w width of first rectangle
   * @param h height of first rectangle
   * @param rect a Rectangle object of the second rectangle
   *
   * @throws NullPointerException if rect is null
   *
   * @return a rectangle corresponding to the intersection of the
   *         two rectangles. An empty rectangle is returned if the rectangles
   *         do not overlap
   */
    private static Rectangle computeIntersection(int x, int y, int w, int h, Rectangle rect) {
        int x2 = (int) rect.x;
        int y2 = (int) rect.y;
        int w2 = (int) rect.width;
        int h2 = (int) rect.height;
        int dx = (x > x2) ? x : x2;
        int dy = (y > y2) ? y : y2;
        int dw = (x + w < x2 + w2) ? (x + w - dx) : (x2 + w2 - dx);
        int dh = (y + h < y2 + h2) ? (y + h - dy) : (y2 + h2 - dy);
        if (dw >= 0 && dh >= 0) rect.setBounds(dx, dy, dw, dh); else rect.setBounds(0, 0, 0, 0);
        return rect;
    }

    /**
   * Helper method to transform the clip. This is called by the various
   * transformation-manipulation methods to update the clip (which is in
   * userspace) accordingly.
   *
   * The transform usually is the inverse transform that was applied to the
   * graphics object.
   *
   * @param t the transform to apply to the clip
   */
    private void updateClip(AffineTransform t) {
        if (!(clip instanceof GeneralPath)) clip = new GeneralPath(clip);
        GeneralPath p = (GeneralPath) clip;
        p.transform(t);
    }

    /**
   * Converts the specified shape into a list of segments.
   *
   * @param s the shape to convert
   * @param t the transformation to apply before converting
   * @param deviceBounds an output parameter; holds the bounding rectangle of
   *        s in device space after return
   * @param isClip true when the shape is a clip, false for normal shapes;
   *        this influences the settings in the created PolyEdge instances.
   *
   * @return a list of PolyEdge that form the shape in device space
   */
    private ArrayList getSegments(Shape s, AffineTransform t, Rectangle2D deviceBounds, boolean isClip) {
        PathIterator path = s.getPathIterator(getTransform(), 1.0);
        double[] seg = new double[6];
        ArrayList segs = new ArrayList();
        double segX = 0.;
        double segY = 0.;
        double polyX = 0.;
        double polyY = 0.;
        double minX = Integer.MAX_VALUE;
        double maxX = Integer.MIN_VALUE;
        double minY = Integer.MAX_VALUE;
        double maxY = Integer.MIN_VALUE;
        while (!path.isDone()) {
            int segType = path.currentSegment(seg);
            minX = Math.min(minX, seg[0]);
            maxX = Math.max(maxX, seg[0]);
            minY = Math.min(minY, seg[1]);
            maxY = Math.max(maxY, seg[1]);
            if (segType == PathIterator.SEG_MOVETO) {
                segX = seg[0];
                segY = seg[1];
                polyX = seg[0];
                polyY = seg[1];
            } else if (segType == PathIterator.SEG_CLOSE) {
                PolyEdge edge = new PolyEdge(segX, segY, polyX, polyY, isClip);
                segs.add(edge);
            } else if (segType == PathIterator.SEG_LINETO) {
                PolyEdge edge = new PolyEdge(segX, segY, seg[0], seg[1], isClip);
                segs.add(edge);
                segX = seg[0];
                segY = seg[1];
            }
            path.next();
        }
        deviceBounds.setRect(minX, minY, maxX - minX, maxY - minY);
        return segs;
    }
}

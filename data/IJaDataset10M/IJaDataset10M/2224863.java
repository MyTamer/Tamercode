package java.awt.peer;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;

/**
 * Defines the methods that a component peer is required to implement.
 */
public interface ComponentPeer {

    /**
   * Returns the construction status of the specified image. This is called
   * by {@link Component#checkImage(Image, int, int, ImageObserver)}.
   *
   * @param img the image
   * @param width the width of the image
   * @param height the height of the image
   * @param ob the image observer to be notified of updates of the status
   *
   * @return a bitwise ORed set of ImageObserver flags
   */
    int checkImage(Image img, int width, int height, ImageObserver ob);

    /**
   * Creates an image by starting the specified image producer. This is called
   * by {@link Component#createImage(ImageProducer)}.
   *
   * @param prod the image producer to be used to create the image
   *
   * @return the created image
   */
    Image createImage(ImageProducer prod);

    /**
   * Creates an empty image with the specified <code>width</code> and
   * <code>height</code>.
   *
   * @param width the width of the image to be created
   * @param height the height of the image to be created
   *
   * @return the created image
   */
    Image createImage(int width, int height);

    /**
   * Disables the component. This is called by {@link Component#disable()}.
   */
    void disable();

    /**
   * Disposes the component peer. This should release all resources held by the
   * peer. This is called when the component is no longer in use.
   */
    void dispose();

    /**
   * Enables the component. This is called by {@link Component#enable()}.
   */
    void enable();

    /**
   * Returns the color model of the component. This is currently not used.
   *
   * @return the color model of the component
   */
    ColorModel getColorModel();

    /**
   * Returns the font metrics for the specified font. This is called by
   * {@link Component#getFontMetrics(Font)}.
   *
   * @param f the font for which to query the font metrics
   *
   * @return the font metrics for the specified font
   */
    FontMetrics getFontMetrics(Font f);

    /**
   * Returns a {@link Graphics} object suitable for drawing on this component.
   * This is called by {@link Component#getGraphics()}.
   *
   * @return a graphics object suitable for drawing on this component
   */
    Graphics getGraphics();

    /**
   * Returns the location of this component in screen coordinates. This is
   * called by {@link Component#getLocationOnScreen()}.
   *
   * @return the location of this component in screen coordinates
   */
    Point getLocationOnScreen();

    /**
   * Returns the minimum size for the component. This is called by
   * {@link Component#getMinimumSize()}.
   *
   * @return the minimum size for the component
   */
    Dimension getMinimumSize();

    /**
   * Returns the preferred size for the component. This is called by
   * {@link Component#getPreferredSize()}.
   *
   * @return the preferred size for the component
   */
    Dimension getPreferredSize();

    /**
   * Returns the toolkit that created this peer.
   *
   * @return the toolkit that created this peer
   */
    Toolkit getToolkit();

    /**
   * Handles the given event. This is called from
   * {@link Component#dispatchEvent(AWTEvent)} to give the peer a chance to 
   * react to events for the component.
   *
   * @param e the event
   */
    void handleEvent(AWTEvent e);

    /**
   * Makes the component invisible. This is called from
   * {@link Component#hide()}.
   */
    void hide();

    /**
   * Returns <code>true</code> if the component can receive keyboard input
   * focus. This is called from {@link Component#isFocusTraversable()}.
   * 
   * @specnote Part of the earlier 1.1 API, replaced by isFocusable().
   */
    boolean isFocusTraversable();

    /**
   * Returns <code>true</code> if the component can receive keyboard input
   * focus. This is called from {@link Component#isFocusable()}.
   */
    boolean isFocusable();

    /**
   * Returns the minimum size for the component. This is called by
   * {@link Component#minimumSize()}.
   *
   * @return the minimum size for the component
   */
    Dimension minimumSize();

    /**
   * Returns the preferred size for the component. This is called by
   * {@link Component#getPreferredSize()}.
   *
   * @return the preferred size for the component
   */
    Dimension preferredSize();

    void paint(Graphics graphics);

    /**
   * Prepares an image for rendering on this component. This is called by
   * {@link Component#prepareImage(Image, int, int, ImageObserver)}.
   *
   * @param img the image to prepare
   * @param width the desired width of the rendered image
   * @param height the desired height of the rendered image
   * @param ob the image observer to be notified of updates in the preparation
   *        process
   *
   * @return <code>true</code> if the image has been fully prepared,
   *         <code>false</code> otherwise (in which case the image observer
   *         receives updates)
   */
    boolean prepareImage(Image img, int width, int height, ImageObserver ob);

    void print(Graphics graphics);

    /**
   * Repaints the specified rectangle of this component. This is called from
   * {@link Component#repaint(long, int, int, int, int)}.
   *
   * @param tm number of milliseconds to wait with repainting
   * @param x the X coordinate of the upper left corner of the damaged rectangle
   * @param y the Y coordinate of the upper left corner of the damaged rectangle
   * @param width the width of the damaged rectangle
   * @param height the height of the damaged rectangle
   */
    void repaint(long tm, int x, int y, int width, int height);

    /**
   * Requests that this component receives the focus. This is called from
   * {@link Component#requestFocus()}.
   * 
   * @specnote Part of the earlier 1.1 API, apparently replaced by argument 
   *           form of the same method.
   */
    void requestFocus();

    /**
   * Requests that this component receives the focus. This is called from
   * {@link Component#requestFocus()}.
   *
   * @param source TODO
   * @param bool1 TODO
   * @param bool2 TODO
   * @param x TODO
   */
    boolean requestFocus(Component source, boolean bool1, boolean bool2, long x);

    /**
   * Notifies the peer that the bounds of this component have changed. This
   * is called by {@link Component#reshape(int, int, int, int)}.
   *
   * @param x the X coordinate of the upper left corner of the component
   * @param y the Y coordinate of the upper left corner of the component
   * @param width the width of the component
   * @param height the height of the component
   */
    void reshape(int x, int y, int width, int height);

    /**
   * Sets the background color of the component. This is called by
   * {@link Component#setBackground(Color)}.
   *
   * @param color the background color to set
   */
    void setBackground(Color color);

    /**
   * Notifies the peer that the bounds of this component have changed. This
   * is called by {@link Component#setBounds(int, int, int, int)}.
   *
   * @param x the X coordinate of the upper left corner of the component
   * @param y the Y coordinate of the upper left corner of the component
   * @param width the width of the component
   * @param height the height of the component
   */
    void setBounds(int x, int y, int width, int height);

    /**
   * Sets the cursor of the component. This is called by
   * {@link Component#setCursor(Cursor)}.
   *
   * @specnote Part of the earlier 1.1 API, apparently no longer needed.
   */
    void setCursor(Cursor cursor);

    /**
   * Sets the enabled/disabled state of this component. This is called by
   * {@link Component#setEnabled(boolean)}.
   *
   * @param enabled <code>true</code> to enable the component,
   *        <code>false</code> to disable it
   */
    void setEnabled(boolean enabled);

    /**
   * Sets the font of the component. This is called by
   * {@link Component#setFont(Font)}.
   *
   * @param font the font to set
   */
    void setFont(Font font);

    /**
   * Sets the foreground color of the component. This is called by
   * {@link Component#setForeground(Color)}.
   *
   * @param color the foreground color to set
   */
    void setForeground(Color color);

    /**
   * Sets the visibility state of the component. This is called by
   * {@link Component#setVisible(boolean)}.
   *
   * @param visible <code>true</code> to make the component visible,
   *        <code>false</code> to make it invisible
   */
    void setVisible(boolean visible);

    /**
   * Makes the component visible. This is called by {@link Component#show()}.
   */
    void show();

    /** 
   * Get the graphics configuration of the component. The color model
   * of the component can be derived from the configuration.
   *
   * @return the graphics configuration of the component
   */
    GraphicsConfiguration getGraphicsConfiguration();

    /**
   * Part of an older API, no longer needed.
   */
    void setEventMask(long mask);

    /**
   * Returns <code>true</code> if this component has been obscured,
   * <code>false</code> otherwise. This will only work if
   * {@link #canDetermineObscurity()} also returns <code>true</code>.
   *
   * @return <code>true</code> if this component has been obscured,
   *         <code>false</code> otherwise.
   */
    boolean isObscured();

    /**
   * Returns <code>true</code> if this component peer can determine if the
   * component has been obscured, <code>false</code> otherwise.
   *
   * @return <code>true</code> if this component peer can determine if the
   *         component has been obscured, <code>false</code> otherwise
   */
    boolean canDetermineObscurity();

    /**
   * Coalesces the specified paint event.
   *
   * @param e the paint event
   */
    void coalescePaintEvent(PaintEvent e);

    /**
   * Updates the cursor.
   */
    void updateCursorImmediately();

    /**
   * Returns true, if this component can handle wheel scrolling,
   * <code>false</code> otherwise.
   *
   * @return true, if this component can handle wheel scrolling,
   *         <code>false</code> otherwise
   */
    boolean handlesWheelScrolling();

    /**
   * A convenience method that creates a volatile image.  The volatile
   * image is created on the screen device on which this component is
   * displayed, in the device's current graphics configuration.
   *
   * @param width width of the image
   * @param height height of the image
   *
   * @see VolatileImage
   *
   * @since 1.2
   */
    VolatileImage createVolatileImage(int width, int height);

    /**
   * Create a number of image buffers that implement a buffering
   * strategy according to the given capabilities.
   *
   * @param numBuffers the number of buffers
   * @param caps the buffering capabilities
   *
   * @throws AWTException if the specified buffering strategy is not
   * implemented
   *
   * @since 1.2
   */
    void createBuffers(int numBuffers, BufferCapabilities caps) throws AWTException;

    /**
   * Return the back buffer of this component.
   *
   * @return the back buffer of this component.
   *
   * @since 1.2
   */
    Image getBackBuffer();

    /**
   * Perform a page flip, leaving the contents of the back buffer in
   * the specified state.
   *
   * @param contents the state in which to leave the back buffer
   *
   * @since 1.2
   */
    void flip(BufferCapabilities.FlipContents contents);

    /**
   * Destroy the resources created by createBuffers.
   *
   * @since 1.2
   */
    void destroyBuffers();

    /**
   * Get the bounds of this component peer.
   * 
   * @return component peer bounds
   * @since 1.5
   */
    Rectangle getBounds();

    /**
   * Reparent this component under another container.
   * 
   * @param parent
   * @since 1.5
   */
    void reparent(ContainerPeer parent);

    /**
   * Set the bounds of this component peer.
   * 
   * @param x the new x co-ordinate
   * @param y the new y co-ordinate
   * @param width the new width
   * @param height the new height
   * @param z the new stacking level
   * @since 1.5
   */
    void setBounds(int x, int y, int width, int height, int z);

    /**
   * Check if this component supports being reparented.
   * 
   * @return true if this component can be reparented,
   * false otherwise.
   * @since 1.5
   */
    boolean isReparentSupported();

    /**
   * Layout this component peer.
   *
   * @since 1.5
   */
    void layout();
}

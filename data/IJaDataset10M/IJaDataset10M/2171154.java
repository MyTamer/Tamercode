package com.sun.midp.chameleon;

import javax.microedition.lcdui.*;

/**
 * Chameleon graphics utility class. This class is a collection of
 * commonly used graphics routines. These routines can be used by
 * layer implementations as well as widgets themselves.
 */
public class CGraphicsUtil {

    /**
     * Fill width from <code>start</code> to <code>end</code>
     * with imgTop at <code>y1</code> coordinate and 
     * with imgBottom at <code>y2</code>coordinate.
     *
     * @param g The Graphics context to paint to
     * @param imgTop The top image.
     * @param imgBottom The bottom image.
     * @param start start x co-ordinate of ImageItem
     * @param end end x co-ordinate of ImageItem
     * @param y1  y co-ordinate of Top ImageItem
     * @param y2  y co-ordinate of Bottom ImageItem
     */
    public static void drawTop_BottomBorder(Graphics g, Image imgTop, Image imgBottom, int start, int end, int y1, int y2) {
        int imgWidth = imgTop.getWidth();
        int xClip = g.getClipX();
        int clipWidth = g.getClipWidth();
        g.clipRect(start, g.getClipY(), end - start, g.getClipHeight());
        for (int x = start; x < end; x += imgWidth) {
            g.drawImage(imgTop, x, y1, Graphics.TOP | Graphics.LEFT);
            g.drawImage(imgBottom, x, y2, Graphics.TOP | Graphics.LEFT);
        }
        g.setClip(xClip, g.getClipY(), clipWidth, g.getClipHeight());
    }

    /**
     * Fill width from <code>start</code> to <code>end</code>
     * with imgTop at <code>y1</code> coordinate and 
     * with imgBottom at <code>y2</code>coordinate.
     *
     * @param g The Graphics context to paint to
     * @param imgLeft The left image.
     * @param imgRight The right image.
     * @param start start y co-ordinate of ImageItem
     * @param end end y co-ordinate of ImageItem
     * @param x1  x co-ordinate of Left ImageItem
     * @param x2  x co-ordinate of Right ImageItem
     */
    public static void drawLeft_RightBorder(Graphics g, Image imgLeft, Image imgRight, int start, int end, int x1, int x2) {
        int imgHeight = imgLeft.getHeight();
        int yClip = g.getClipY();
        int clipHeight = g.getClipHeight();
        g.clipRect(g.getClipX(), start, g.getClipWidth(), end - start);
        for (int y = start; y < end; y += imgHeight) {
            g.drawImage(imgLeft, x1, y, Graphics.TOP | Graphics.LEFT);
            g.drawImage(imgRight, x2, y, Graphics.TOP | Graphics.LEFT);
        }
        g.setClip(g.getClipX(), yClip, g.getClipWidth(), clipHeight);
    }

    /**
     * Draws the button background of the size specified (x, y, w, h).
     * Different background can be drawn when the ImageItem has and 
     * does not have focus.
     * IMPL_NOTE: update params
     * @param g The graphics context to be used for rendering button
     * @param x The x coordinate of the button's background top left corner
     * @param y The y coordinate of the button's background top left corner
     * @param w The width of the button's background
     * @param h The height of the button's background
     * @param image Array of background images to render.
     */
    public static void draw9pcsBackground(Graphics g, int x, int y, int w, int h, Image[] image) {
        if (image == null || image.length != 9) {
            return;
        }
        g.translate(x, y);
        int iW = image[1].getWidth();
        g.drawImage(image[0], 0, 0, Graphics.LEFT | Graphics.TOP);
        w -= image[2].getWidth();
        for (int i = image[0].getWidth(); i < w; i += iW) {
            g.drawImage(image[1], i, 0, Graphics.LEFT | Graphics.TOP);
        }
        w += image[2].getWidth();
        g.drawImage(image[2], w, 0, Graphics.RIGHT | Graphics.TOP);
        if (image[4] != null) {
            iW = image[4].getWidth();
        }
        int iH = image[3].getHeight();
        h -= image[6].getHeight();
        w -= image[5].getWidth();
        for (int i = image[0].getHeight(); i <= h; i += iH) {
            g.drawImage(image[3], 0, i, Graphics.LEFT | Graphics.TOP);
            for (int j = image[3].getWidth(); j <= w; j += iW) {
                g.drawImage(image[4], j, i, Graphics.LEFT | Graphics.TOP);
            }
            g.drawImage(image[5], w + image[5].getWidth(), i, Graphics.RIGHT | Graphics.TOP);
        }
        w += image[5].getWidth();
        h += image[6].getHeight();
        iW = image[7].getWidth();
        g.drawImage(image[6], 0, h, Graphics.LEFT | Graphics.BOTTOM);
        w -= image[8].getWidth();
        for (int i = image[6].getWidth(); i < w; i += iW) {
            g.drawImage(image[7], i, h, Graphics.LEFT | Graphics.BOTTOM);
        }
        w += image[8].getWidth();
        g.drawImage(image[8], w, h, Graphics.RIGHT | Graphics.BOTTOM);
        g.translate(-x, -y);
    }

    /**
     * Draws the button background of the size specified (x, y, w).
     * Different background can be drawn when the ImageItem has and 
     * does not have focus.
     * IMPL_NOTE: update params
     * @param g The graphics context to be used for rendering button
     * @param x The x coordinate of the button's background top left corner
     * @param y The y coordinate of the button's background top left corner
     * @param w The width of the button's background
     * @param image Array of background images to render.
     */
    public static void draw3pcsBackground(Graphics g, int x, int y, int w, Image[] image) {
        if (image == null || image.length != 3) {
            return;
        }
        int iW = image[1].getWidth();
        g.drawImage(image[0], x, y, Graphics.LEFT | Graphics.TOP);
        w -= image[2].getWidth();
        for (int i = image[0].getWidth() + x; i < w; i += iW) {
            g.drawImage(image[1], i, y, Graphics.LEFT | Graphics.TOP);
        }
        w += image[2].getWidth();
        g.drawImage(image[2], w, y, Graphics.RIGHT | Graphics.TOP);
    }

    /**
     * Draws a border of <code>borderWidth</code> at location 
     * <code>x, y</code> with dimensions <code>width, height</code>.
     * The border is drawn with one color.
     * IMPL_NOTE: update params
     * @param g The graphics context to be used for rendering button
     * @param x The x coordinate of the button's background top left corner
     * @param y The y coordinate of the button's background top left corner
     * @param w The width of the button's background
     * @param h The height of the button's background
     * @param borderWidth The width of the border line.
     * @param borderColor The color for the border line.
     */
    public static void draw1ColorBorder(Graphics g, int x, int y, int w, int h, int borderWidth, int borderColor) {
        g.setColor(borderColor);
        w--;
        h--;
        if (borderWidth == 1) {
            g.drawRect(x, y, w, h);
            return;
        }
        g.translate(x, y);
        for (int i = 0; i < borderWidth; i++, w -= 2, h -= 2) {
            g.drawRect(i, i, w, h);
        }
        g.translate(-x, -y);
    }

    /**
     * Draws a border of <code>borderWidth</code> at location 
     * <code>x, y</code> with dimensions <code>width, height</code>.
     * The border is drawn out of two colors.
     * IMPL_NOTE: update params
     * @param g The graphics context to be used for rendering button
     * @param x The x coordinate of the button's background top left corner
     * @param y The y coordinate of the button's background top left corner
     * @param w The width of the button's background
     * @param h The height of the button's background
     * @param hasFocus The flag indicating the item has input focus.
     * @param darkBorder The color of the dark border.
     * @param lightBorder The color of the light border.
     * @param borderWidth The wodth of the border line.
     */
    public static void draw2ColorBorder(Graphics g, int x, int y, int w, int h, boolean hasFocus, int darkBorder, int lightBorder, int borderWidth) {
        g.setColor(hasFocus ? darkBorder : lightBorder);
        g.fillRect(x, y, w, borderWidth);
        g.fillRect(x, y, borderWidth, h);
        g.setColor(hasFocus ? lightBorder : darkBorder);
        g.fillTriangle(x, y + h, x + borderWidth, y + h - borderWidth, x + borderWidth, y + h);
        g.fillRect(x + borderWidth, y + h - borderWidth, w - borderWidth, borderWidth);
        g.fillTriangle(x + w, y, x + w - borderWidth, y + borderWidth, x + w, y + borderWidth);
        g.fillRect(x + w - borderWidth, y + borderWidth, borderWidth, h - borderWidth);
    }

    /**
     * Paints the background according to image or color
     * requested.
     * @param g The graphics context to be used for rendering button.
     * @param bgImage The background image to render or null.
     * @param tileBG Flag to indicate the background image should be
     * tiled.
     * @param bgColor The background color to paint if bgImage is null.
     * @param width The width of the background.
     * @param height The height of the background.
     */
    public static void paintBackground(Graphics g, Image bgImage, boolean tileBG, int bgColor, int width, int height) {
        if (g == null) {
            return;
        }
        if (bgImage != null) {
            if (!tileBG) {
                g.drawImage(bgImage, 0, 0, Graphics.TOP | Graphics.LEFT);
            } else {
                int bgX = bgImage.getWidth();
                int bgY = bgImage.getHeight();
                int clipX = g.getClipX();
                int clipY = g.getClipY();
                int leftX = (clipX / bgX) * bgX;
                int leftY = (clipY / bgY) * bgY;
                int rightX = clipX + g.getClipWidth();
                int rightY = clipY + g.getClipHeight();
                for (int i = leftY; i < rightY; i += bgY) {
                    for (int j = leftX; j < rightX; j += bgX) {
                        g.drawImage(bgImage, j, i, Graphics.TOP | Graphics.LEFT);
                    }
                }
            }
            return;
        }
        if (bgColor >= 0) {
            int color = g.getColor();
            g.setColor(bgColor);
            g.fillRect(g.getClipX(), g.getClipY(), g.getClipWidth(), g.getClipHeight());
            g.setColor(color);
        }
    }

    /**
     * Draws a drop shadow box with a border of border of 
     * <code>borderColor</code>, a drop shadow of 
     * <code>shadowColor</code>, and a filled area of <code>color</code>
     * at location <code>x, y</code> with dimensions 
     * <code>width, height</code>.  
     * IMPL_NOTE:  update params
     * @param g The graphics context to be used for rendering button
     * @param x The x coordinate of the button's background top left corner
     * @param y The y coordinate of the button's background top left corner
     * @param w The width of the button's background
     * @param h The height of the button's background
     * @param borderColor The border color.
     * @param shadowColor The shadow color.
     * @param color The drawing color.
     */
    public static void drawDropShadowBox(Graphics g, int x, int y, int w, int h, int borderColor, int shadowColor, int color) {
        g.setColor(color);
        w--;
        h--;
        g.fillRect(x, y, w, h);
        g.setColor(shadowColor);
        g.drawRect(x + 1, y + 1, w - 1, h - 1);
        g.setColor(borderColor);
        g.drawRect(x, y, w, h);
    }
}

package com.android.ide.common.layout;

import com.android.ide.common.api.DrawingStyle;
import com.android.ide.common.api.IColor;
import com.android.ide.common.api.IGraphics;
import com.android.ide.common.api.Point;
import com.android.ide.common.api.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestGraphics implements IGraphics {

    /** List of things we have drawn */
    private List<String> mDrawn = new ArrayList<String>();

    private IColor mBackground = new TestColor(0x000000);

    private IColor mForeground = new TestColor(0xFFFFFF);

    private int mAlpha = 128;

    /** Return log of graphics calls */
    public List<String> getDrawn() {
        return Collections.unmodifiableList(mDrawn);
    }

    /** Wipe out log of graphics calls */
    public void clear() {
        mDrawn.clear();
    }

    public void drawBoxedStrings(int x, int y, List<?> strings) {
        mDrawn.add("drawBoxedStrings(" + x + "," + y + "," + strings + ")");
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        mDrawn.add("drawLine(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void drawLine(Point p1, Point p2) {
        mDrawn.add("drawLine(" + p1 + "," + p2 + ")");
    }

    public void drawRect(int x1, int y1, int x2, int y2) {
        mDrawn.add("drawRect(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void drawRect(Point p1, Point p2) {
        mDrawn.add("drawRect(" + p1 + "," + p2 + ")");
    }

    public void drawRect(Rect r) {
        mDrawn.add("drawRect(" + rectToString(r) + ")");
    }

    public void drawString(String string, int x, int y) {
        mDrawn.add("drawString(" + x + "," + y + "," + string + ")");
    }

    public void drawString(String string, Point topLeft) {
        mDrawn.add("drawString(" + string + "," + topLeft + ")");
    }

    public void fillRect(int x1, int y1, int x2, int y2) {
        mDrawn.add("fillRect(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")");
    }

    public void fillRect(Point p1, Point p2) {
        mDrawn.add("fillRect(" + p1 + "," + p2 + ")");
    }

    public void fillRect(Rect r) {
        mDrawn.add("fillRect(" + rectToString(r) + ")");
    }

    public int getAlpha() {
        return mAlpha;
    }

    public IColor getBackground() {
        return mBackground;
    }

    public int getFontHeight() {
        return 12;
    }

    public IColor getForeground() {
        return mForeground;
    }

    public IColor registerColor(int rgb) {
        mDrawn.add("registerColor(" + Integer.toHexString(rgb) + ")");
        return new TestColor(rgb);
    }

    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mDrawn.add("setAlpha(" + alpha + ")");
    }

    public void setBackground(IColor color) {
        mDrawn.add("setBackground(" + color + ")");
        mBackground = color;
    }

    public void setForeground(IColor color) {
        mDrawn.add("setForeground(" + color + ")");
        mForeground = color;
    }

    public void setLineStyle(LineStyle style) {
        mDrawn.add("setLineStyle(" + style + ")");
    }

    public void setLineWidth(int width) {
        mDrawn.add("setLineWidth(" + width + ")");
    }

    public void useStyle(DrawingStyle style) {
        mDrawn.add("useStyle(" + style + ")");
    }

    private static String rectToString(Rect rect) {
        return "Rect[" + rect.x + "," + rect.y + "," + rect.w + "," + rect.h + "]";
    }
}

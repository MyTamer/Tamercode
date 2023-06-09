package net.rptools.maptool.client.ui.zone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import net.rptools.lib.image.ImageUtil;
import net.rptools.maptool.model.drawing.Drawable;
import net.rptools.maptool.model.drawing.DrawnElement;

/**
 */
public class OldDrawableRenderer {

    private int drawableCount = 0;

    private List<DrawableEntry> drawableEntries = new ArrayList<DrawableEntry>();

    private boolean repaint = true;

    public void flush() {
        drawableEntries.clear();
        drawableCount = -1;
    }

    public void renderDrawables(Graphics g, List<DrawnElement> drawableList, int offsetX, int offsetY, double scale) {
        if (drawableCount != drawableList.size()) {
            if (drawableCount > drawableList.size()) {
                drawableEntries.clear();
            }
            consolidateBounds(drawableList);
            validateEntries();
            for (DrawnElement element : drawableList) {
                Drawable drawable = element.getDrawable();
                DrawableEntry entry = getEntryFor(drawable.getBounds());
                Graphics2D g2 = entry.image.createGraphics();
                g2.translate(-entry.bounds.x, -entry.bounds.y);
                drawable.draw(g2, element.getPen());
                g2.translate(entry.bounds.x, entry.bounds.y);
                g2.dispose();
            }
        }
        drawableCount = drawableList.size();
        Rectangle clipBounds = g.getClipBounds();
        for (DrawableEntry entry : drawableEntries) {
            int x = (int) ((entry.bounds.x * scale) + offsetX);
            int y = (int) ((entry.bounds.y * scale) + offsetY);
            int width = (int) (entry.bounds.width * scale);
            int height = (int) (entry.bounds.height * scale);
            if (clipBounds.intersects(x, y, width, height)) {
                g.drawImage(entry.image, x, y, width, height, null);
            }
        }
    }

    private DrawableEntry getEntryFor(Rectangle bounds) {
        for (int i = 0; i < drawableEntries.size(); i++) {
            DrawableEntry entry = drawableEntries.get(i);
            if (entry.bounds.contains(bounds.x, bounds.y, bounds.width > 0 ? bounds.width : 1, bounds.height > 0 ? bounds.height : 1)) {
                return entry;
            }
        }
        throw new IllegalStateException("Could not find appropriate back buffer.");
    }

    private void validateEntries() {
        for (DrawableEntry entry : drawableEntries) {
            entry.validate();
        }
    }

    private synchronized void consolidateBounds(List<DrawnElement> drawableList) {
        OUTTER: for (int i = 0; i < drawableList.size(); i++) {
            DrawnElement drawable = drawableList.get(i);
            int padding = (int) drawable.getPen().getThickness();
            Rectangle bounds = drawable.getDrawable().getBounds();
            bounds.x -= padding;
            bounds.y -= padding;
            bounds.width += padding * 2;
            bounds.height += padding * 2;
            for (int j = 0; j < drawableEntries.size(); j++) {
                DrawableEntry entry = drawableEntries.get(j);
                if (entry.bounds.contains(bounds)) {
                    continue OUTTER;
                }
            }
            drawableEntries.add(new DrawableEntry(bounds));
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < drawableEntries.size(); i++) {
                DrawableEntry outterEntry = drawableEntries.get(i);
                for (ListIterator<DrawableEntry> iter = drawableEntries.listIterator(i + 1); iter.hasNext(); ) {
                    DrawableEntry innerEntry = iter.next();
                    if (outterEntry.bounds.intersects(innerEntry.bounds)) {
                        outterEntry = new DrawableEntry(outterEntry.bounds.union(innerEntry.bounds));
                        iter.remove();
                        changed = true;
                    }
                }
                if (changed) {
                    drawableEntries.set(i, outterEntry);
                }
            }
        }
    }

    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private static class DrawableEntry {

        public Rectangle bounds;

        public BufferedImage image;

        public DrawableEntry(Rectangle bounds) {
            this.bounds = bounds;
        }

        void validate() {
            if (image == null) {
                image = ImageUtil.createCompatibleImage(bounds.width, bounds.height, Transparency.TRANSLUCENT);
            } else {
                Graphics2D g2d = (Graphics2D) image.getGraphics();
                g2d.setBackground(TRANSPARENT);
                g2d.clearRect(0, 0, bounds.width, bounds.height);
                g2d.dispose();
            }
        }
    }
}

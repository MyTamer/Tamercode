package org.eclipse.ui.internal.decorators;

import java.util.List;
import java.util.ListIterator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/** 
 * The Decoration Result is the result of a decoration.
 */
public class DecorationResult {

    private List prefixes;

    private List suffixes;

    private ImageDescriptor[] descriptors;

    private Color foregroundColor;

    private Color backgroundColor;

    private Font font;

    DecorationResult(List prefixList, List suffixList, ImageDescriptor[] imageDescriptors, Color resultForegroundColor, Color resultBackgroundColor, Font resultFont) {
        prefixes = prefixList;
        suffixes = suffixList;
        if (hasOverlays(imageDescriptors)) {
            descriptors = imageDescriptors;
        }
        foregroundColor = resultForegroundColor;
        backgroundColor = resultBackgroundColor;
        font = resultFont;
    }

    /**
     * Return whether or not any of the imageDescriptors
     * are non-null.
     * @param imageDescriptors
     * @return <code>true</code> if there are some non-null
     * overlays
     */
    private boolean hasOverlays(ImageDescriptor[] imageDescriptors) {
        for (int i = 0; i < imageDescriptors.length; i++) {
            if (imageDescriptors[i] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Decorate the Image supplied with the overlays.
     * @param image
     * @param overlayCache
     * @return Image
     */
    Image decorateWithOverlays(Image image, OverlayCache overlayCache) {
        if (image == null || descriptors == null) {
            return image;
        }
        return overlayCache.applyDescriptors(image, descriptors);
    }

    /**
     * Decorate the String supplied with the prefixes and suffixes.
     * This method is public for use by the test suites and is not intended
     * to be referenced by other workbench internals.
     * @param text 
     * @return String
     */
    public String decorateWithText(String text) {
        if (prefixes.isEmpty() && suffixes.isEmpty()) {
            return text;
        }
        StringBuffer result = new StringBuffer();
        ListIterator prefixIterator = prefixes.listIterator();
        while (prefixIterator.hasNext()) {
            result.append(prefixIterator.next());
        }
        result.append(text);
        ListIterator suffixIterator = suffixes.listIterator();
        while (suffixIterator.hasNext()) {
            result.append(suffixIterator.next());
        }
        return result.toString();
    }

    /**
     * Get the descriptor array for the receiver.
     * @return ImageDescriptor[] or <code>null</code>
     */
    ImageDescriptor[] getDescriptors() {
        return descriptors;
    }

    /**
     * Get the prefixes for the receiver.
     * @return List
     */
    List getPrefixes() {
        return prefixes;
    }

    /**
     * Get the suffixes for the receiver.
     * @return List
     */
    List getSuffixes() {
        return suffixes;
    }

    /**
	 * Return the background Color for the result.
	 * @return Color
	 */
    Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
	 * Return the font for the result.
	 * @return Font
	 */
    Font getFont() {
        return font;
    }

    /**
	 * Return the foreground color for the result.
	 * @return Color
	 */
    Color getForegroundColor() {
        return foregroundColor;
    }
}

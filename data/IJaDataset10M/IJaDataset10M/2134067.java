package org.apache.harmony.awt.gl.font;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Hashtable;
import java.util.Locale;
import org.apache.harmony.awt.gl.font.FontManager;
import org.apache.harmony.awt.gl.font.FontPeerImpl;
import org.apache.harmony.awt.gl.font.Glyph;
import org.apache.harmony.awt.gl.font.LineMetricsImpl;
import org.apache.harmony.awt.internal.nls.Messages;

/**
 * Linux platform font peer implementation based on Xft and FreeType libraries.
 */
public class AndroidFont extends FontPeerImpl {

    private int[] fontUnicodeRanges;

    private Hashtable glyphs = new Hashtable();

    private long display = 0;

    private int screen = 0;

    public AndroidFont(String fontName, int fontStyle, int fontSize) {
        Toolkit.getDefaultToolkit();
        this.name = fontName;
        this.size = fontSize;
        this.style = fontStyle;
        initAndroidFont();
    }

    /**
     * Initializes some native dependent font information, e.g. number of glyphs, 
     * font metrics, italic angle etc. 
     */
    public void initAndroidFont() {
        this.nlm = new AndroidLineMetrics(this, null, " ");
        this.ascent = nlm.getLogicalAscent();
        this.descent = nlm.getLogicalDescent();
        this.height = nlm.getHeight();
        this.leading = nlm.getLogicalLeading();
        this.maxAdvance = nlm.getLogicalMaxCharWidth();
        if (this.fontType == FontManager.FONT_TYPE_T1) {
            this.defaultChar = 1;
        } else {
            this.defaultChar = 0;
        }
        this.maxCharBounds = new Rectangle2D.Float(0, -nlm.getAscent(), nlm.getMaxCharWidth(), this.height);
    }

    public boolean canDisplay(char chr) {
        return isGlyphExists(chr);
    }

    public LineMetrics getLineMetrics(String str, FontRenderContext frc, AffineTransform at) {
        nlm.getBaselineOffsets();
        LineMetricsImpl lm = (LineMetricsImpl) (this.nlm.clone());
        lm.setNumChars(str.length());
        if ((at != null) && (!at.isIdentity())) {
            lm.scale((float) at.getScaleX(), (float) at.getScaleY());
        }
        return lm;
    }

    public String getPSName() {
        return psName;
    }

    public String getFamily(Locale l) {
        if (fontType == FontManager.FONT_TYPE_TT) {
            return this.getFamily();
        }
        return this.fontFamilyName;
    }

    public String getFontName(Locale l) {
        if ((pFont == 0) || (this.fontType == FontManager.FONT_TYPE_T1)) {
            return this.name;
        }
        return this.getFontName();
    }

    public int getMissingGlyphCode() {
        return getDefaultGlyph().getGlyphCode();
    }

    public Glyph getGlyph(char index) {
        Glyph result = null;
        Object key = new Integer(index);
        if (glyphs.containsKey(key)) {
            result = (Glyph) glyphs.get(key);
        } else {
            if (this.addGlyph(index)) {
                result = (Glyph) glyphs.get(key);
            } else {
                result = this.getDefaultGlyph();
            }
        }
        return result;
    }

    public Glyph getDefaultGlyph() {
        throw new RuntimeException("DefaultGlyphs not implemented!");
    }

    /**
     * Disposes native font handle. If this font peer was created from InputStream 
     * temporary created font resource file is deleted.
     */
    public void dispose() {
        String tempDirName;
        if (pFont != 0) {
            pFont = 0;
            if (isCreatedFromStream()) {
                File fontFile = new File(getTempFontFileName());
                tempDirName = fontFile.getParent();
                fontFile.delete();
            }
        }
    }

    /**
     * Add glyph to cached Glyph objects in this LinuxFont object.
     * 
     * @param uChar the specified character
     * @return true if glyph of the specified character exists in this
     * LinuxFont or this character is escape sequence character.
     */
    public boolean addGlyph(char uChar) {
        throw new RuntimeException("Not implemented!");
    }

    /**
    * Adds range of existing glyphs to this LinuxFont object
    * 
    * @param uFirst the lowest range's bound, inclusive 
    * @param uLast the highest range's bound, exclusive
    */
    public void addGlyphs(char uFirst, char uLast) {
        char index = uFirst;
        if (uLast < uFirst) {
            throw new IllegalArgumentException(Messages.getString("awt.09"));
        }
        while (index < uLast) {
            addGlyph(index);
            index++;
        }
    }

    /**
     * Returns true if specified character has corresopnding glyph, false otherwise.  
     * 
     * @param uIndex specified char
     */
    public boolean isGlyphExists(char uIndex) {
        throw new RuntimeException("DefaultGlyphs not implemented!");
    }

    /**
     *  Returns an array of unicode ranges that are supported by this LinuxFont. 
     */
    public int[] getUnicodeRanges() {
        int[] ranges = new int[fontUnicodeRanges.length];
        System.arraycopy(fontUnicodeRanges, 0, ranges, 0, fontUnicodeRanges.length);
        return ranges;
    }

    /**
     * Return Font object if it was successfully embedded in System
     */
    public static Font embedFont(String absolutePath) {
        throw new RuntimeException("embedFont not implemented!");
    }

    public String getFontName() {
        if ((pFont != 0) && (faceName == null)) {
            if (this.fontType == FontManager.FONT_TYPE_T1) {
                faceName = getFamily();
            }
        }
        return faceName;
    }

    public String getFamily() {
        return fontFamilyName;
    }

    /**
     * Returns initiated FontExtraMetrics instance of this WindowsFont.
     */
    public FontExtraMetrics getExtraMetrics() {
        throw new RuntimeException("Not implemented!");
    }
}

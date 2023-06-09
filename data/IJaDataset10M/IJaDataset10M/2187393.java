package net.sourceforge.vietocr;

import net.sourceforge.vietocr.utilities.ImageHelper;
import net.sourceforge.vietocr.utilities.ImageIOHelper;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.IIOImage;

public class OCRImageEntity {

    private static final Rectangle EMPTY_RECTANGLE = new Rectangle();

    /** input images */
    private List<IIOImage> oimages;

    /** input image File */
    private File imageFile;

    /** index of pages, such as in multi-page TIFF image */
    private int index;

    /** bounding rectangle */
    private Rectangle rect;

    /** Language code, which follows ISO 639-3 standard */
    private String lang;

    /** Horizontal Resolution */
    private int dpiX;

    /** Vertical Resolution */
    private int dpiY;

    /**
     * Constructor.
     * @param oimages a list of <code>IIOImage</code> objects
     * @param index
     * @param the bounding rectangle defines the region of the image to be recognized. A rectangle of zero dimension or <code>null</code> indicates the whole image.
     * @param lang language code, which follows ISO 639-3 standard
     */
    public OCRImageEntity(List<IIOImage> oimages, int index, Rectangle rect, String lang) {
        this.oimages = oimages;
        this.index = index;
        this.rect = rect;
        this.lang = lang;
    }

    /**
     * Constructor.
     * @param imageFile an image file
     * @param index
     * @param the bounding rectangle defines the region of the image to be recognized. A rectangle of zero dimension or <code>null</code> indicates the whole image.
     * @param lang language code, which follows ISO 639-3 standard
     */
    public OCRImageEntity(File imageFile, int index, Rectangle rect, String lang) {
        this.imageFile = imageFile;
        this.index = index;
        this.rect = rect;
        this.lang = lang;
    }

    /**
     * @return the list of oimages
     */
    public List<IIOImage> getOimages() {
        return oimages;
    }

    /**
     * @return the imageFile
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * @return the ClonedImageFiles
     */
    public List<File> getClonedImageFiles() throws IOException {
        if (oimages != null) {
            if (dpiX == 0 || dpiY == 0) {
                if (rect == null || rect.equals(EMPTY_RECTANGLE)) {
                    return ImageIOHelper.createTiffFiles(oimages, index);
                } else {
                    BufferedImage bi = ImageHelper.getSubImage((BufferedImage) oimages.get(index).getRenderedImage(), rect.x, rect.y, rect.width, rect.height);
                    List<IIOImage> tempList = new ArrayList<IIOImage>();
                    tempList.add(new IIOImage(bi, null, null));
                    return ImageIOHelper.createTiffFiles(tempList, 0);
                }
            } else {
                if (rect == null || rect.equals(EMPTY_RECTANGLE)) {
                    List<IIOImage> tempList = new ArrayList<IIOImage>();
                    for (IIOImage oimage : (index == -1 ? oimages : oimages.subList(index, index + 1))) {
                        BufferedImage bi = (BufferedImage) oimage.getRenderedImage();
                        Map<String, String> metadata = ImageIOHelper.readImageData(oimage);
                        float scale = dpiX / Float.parseFloat(metadata.get("dpiX"));
                        bi = ImageHelper.getScaledInstance(bi, (int) (bi.getWidth() * scale), (int) (bi.getHeight() * scale));
                        tempList.add(new IIOImage(bi, null, null));
                    }
                    return ImageIOHelper.createTiffFiles(tempList, (index == -1 ? index : 0), dpiX, dpiY);
                } else {
                    BufferedImage bi = ((BufferedImage) oimages.get(index).getRenderedImage()).getSubimage(rect.x, rect.y, rect.width, rect.height);
                    Map<String, String> metadata = ImageIOHelper.readImageData(oimages.get(index));
                    float scale = dpiX / Float.parseFloat(metadata.get("dpiX"));
                    bi = ImageHelper.getScaledInstance(bi, (int) (bi.getWidth() * scale), (int) (bi.getHeight() * scale));
                    List<IIOImage> tempList = new ArrayList<IIOImage>();
                    tempList.add(new IIOImage(bi, null, null));
                    return ImageIOHelper.createTiffFiles(tempList, 0, dpiX, dpiY);
                }
            }
        } else {
            return ImageIOHelper.createTiffFiles(imageFile, index);
        }
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the bounding rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Sets screenshot mode.
     * @param mode true for resampling the input image; false for no manipulation of the image
     */
    public void setScreenshotMode(boolean mode) {
        dpiX = mode ? 300 : 0;
        dpiY = mode ? 300 : 0;
    }

    /**
     * Sets resolution (DPI).
     * @param dpiX horizontal resolution
     * @param dpiY vertical resolution
     */
    public void setResolution(int dpiX, int dpiY) {
        this.dpiX = dpiX;
        this.dpiY = dpiY;
    }

    /**
     * @return the language code
     */
    public String getLanguage() {
        return lang;
    }
}

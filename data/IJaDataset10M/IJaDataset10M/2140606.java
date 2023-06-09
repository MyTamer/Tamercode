package java.awt;

import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.datatransfer.Clipboard;
import java.net.URL;
import java.util.Properties;
import sun.awt.image.ByteArrayImageSource;
import sun.awt.image.FileImageSource;
import sun.awt.image.URLImageSource;

/** The toolkit used by this AWT implementation based on the X11 library.
 @version 1.14, 08/19/02
*/
class X11Toolkit extends Toolkit {

    private EventQueue eventQueue;

    private static Clipboard clipboard = new Clipboard("System");

    private static X11GraphicsConfig config = (X11GraphicsConfig) (GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

    /** Pushes a new modal dislog onto the modal dialog stack. If events go to a window that does
     not belong to the modal dialog on the top of the stack then we just beep. */
    static native void pushModal(int xwindow);

    /** Pops the specified dialog off the modal stack. */
    static native void popModal(int xwindow);

    public X11Toolkit() {
        eventQueue = new EventQueue();
    }

    /**
     * Gets the size of the screen.
     * @return    the size of this toolkit's screen, in pixels.
     * @since     JDK1.0
     */
    public Dimension getScreenSize() {
        Rectangle dims = config.getBounds();
        return new Dimension(dims.width, dims.height);
    }

    /**
     * Returns the screen resolution in dots-per-inch.
     * @return    this toolkit's screen resolution, in dots-per-inch.
     * @since     JDK1.0
     */
    public native int getScreenResolution();

    /**
     * Determines the color model of this toolkit's screen.
     * <p>
     * <code>ColorModel</code> is an  class that
     * encapsulates the ability to translate between the
     * pixel values of an image and its red, green, blue,
     * and alpha components.
     * <p>
     * This toolkit method is called by the
     * <code>getColorModel</code> method
     * of the <code>Component</code> class.
     * @return    the color model of this toolkit's screen.
     * @see       java.awt.image.ColorModel
     * @see       java.awt.Component#getColorModel
     * @since     JDK1.0
     */
    public ColorModel getColorModel() {
        return config.getColorModel();
    }

    /**
     * Returns the names of the available fonts in this toolkit.<p>
     * For 1.1, the following font names are deprecated (the replacement
     * name follows):
     * <ul>
     * <li>TimesRoman (use Serif)
     * <li>Helvetica (use SansSerif)
     * <li>Courier (use Monospaced)
     * </ul><p>
     * The ZapfDingbats font is also deprecated in 1.1, but only as a
     * separate fontname.  Unicode defines the ZapfDingbat characters
     * starting at ✀, and as of 1.1 Java supports those characters.
     * @return    the names of the available fonts in this toolkit.
     * @since     JDK1.0
     */
    public String[] getFontList() {
        return X11FontMetrics.getFontList();
    }

    /**
     * Gets the screen metrics of the font.
     * @param     font   a font.
     * @return    the screen metrics of the specified font in this toolkit.
     * @since     JDK1.0
     */
    public FontMetrics getFontMetrics(Font font) {
        return font.getX11FontMetrics();
    }

    /**
     * Synchronizes this toolkit's graphics state. Some window systems
     * may do buffering of graphics events.
     * <p>
     * This method ensures that the display is up-to-date. It is useful
     * for animation.
     * @since     JDK1.0
     */
    public native void sync();

    /**
     * Returns an image which gets pixel data from the specified file.
     * The underlying toolkit attempts to resolve multiple requests
     * with the same filename to the same returned Image.
     * Since the mechanism required to facilitate this sharing of
     * Image objects may continue to hold onto images that are no
     * longer of use for an indefinite period of time, developers
     * are encouraged to implement their own caching of images by
     * using the createImage variant wherever available.
     * <h3>Compatibility</h3>
     * PersonalJava does not require support of the PNG image file format.
     * @param filename Filename must reference an image format that
     *                 is recognized by this toolkit. The toolkit must be able
     *                 to create images from the following image file formats:
     *                   GIF, JPEG(JFIF), XBM, and PNG.
     * @return    an image which gets its pixel data from
     *                         the specified file.
     * @see       java.awt.Image
     * @see       java.awt.Toolkit#createImage(java.lang.String)
     */
    public Image getImage(String filename) {
        return createImage(filename);
    }

    /**
     * Returns an image which gets pixel data from the specified URL.
     * The underlying toolkit attempts to resolve multiple requests
     * with the same URL to the same returned Image.
     * Since the mechanism required to facilitate this sharing of
     * Image objects may continue to hold onto images that are no
     * longer of use for an indefinite period of time, developers
     * are encouraged to implement their own caching of images by
     * using the createImage variant wherever available.
     * <h3>Compatibility</h3>
     * PersonalJava does not require support of the PNG image file format.
     * @param url URL must reference an image format that
     *            is recognized by this toolkit. The toolkit must be
     *            able to create images from the following image file formats:
     *                  GIF, JPEG(JFIF), XBM, and PNG.
     * @return    an image which gets its pixel data from
     *                         the specified URL.
     * @see       java.awt.Image
     * @see       java.awt.Toolkit#createImage(java.net.URL)
     */
    public Image getImage(URL url) {
        return createImage(url);
    }

    /**
     * Returns an image which gets pixel data from the specified file.
     * The returned Image is a new object which will not be shared
     * with any other caller of this method or its getImage variant.
     * @param     filename   the name of a file containing pixel data
     *                         in a recognized file format.
     * @return    an image which gets its pixel data from
     *                         the specified file.
     * @see       java.awt.Toolkit#getImage(java.lang.String)
     */
    public Image createImage(String filename) {
        ImageProducer ip = new FileImageSource(filename);
        Image newImage = createImage(ip);
        return newImage;
    }

    /**
     * Returns an image which gets pixel data from the specified URL.
     * The returned Image is a new object which will not be shared
     * with any other caller of this method or its getImage variant.
     * @param     url   the URL to use in fetching the pixel data.
     * @return    an image which gets its pixel data from
     *                         the specified URL.
     * @see       java.awt.Toolkit#getImage(java.net.URL)
     */
    public Image createImage(URL url) {
        ImageProducer ip = new URLImageSource(url);
        Image newImage = createImage(ip);
        return newImage;
    }

    /**
     * Prepares an image for rendering.
     * <p>
     * If the values of the width and height arguments are both
     * <code>-1</code>, this method prepares the image for rendering
     * on the default screen; otherwise, this method prepares an image
     * for rendering on the default screen at the specified width and height.
     * <p>
     * The image data is downloaded asynchronously in another thread,
     * and an appropriately scaled screen representation of the image is
     * generated.
     * <p>
     * This method is called by components <code>prepareImage</code>
     * methods.
     * <p>
     * Information on the flags returned by this method can be found
     * with the definition of the <code>ImageObserver</code> interface.

     * @param     image      the image for which to prepare a
     *                           screen representation.
     * @param     width      the width of the desired screen
     *                           representation, or <code>-1</code>.
     * @param     height     the height of the desired screen
     *                           representation, or <code>-1</code>.
     * @param     observer   the <code>ImageObserver</code>
     *                           object to be notified as the
     *                           image is being prepared.
     * @return    <code>true</code> if the image has already been
     *                 fully prepared; <code>false</code> otherwise.
     * @see       java.awt.Component#prepareImage(java.awt.Image,
     *                 java.awt.image.ImageObserver)
     * @see       java.awt.Component#prepareImage(java.awt.Image,
     *                 int, int, java.awt.image.ImageObserver)
     * @see       java.awt.image.ImageObserver
     * @since     JDK1.0
     */
    public boolean prepareImage(Image image, int width, int height, ImageObserver observer) {
        return false;
    }

    /**
     * Indicates the construction status of a specified image that is
     * being prepared for display.
     * <p>
     * If the values of the width and height arguments are both
     * <code>-1</code>, this method returns the construction status of
     * a screen representation of the specified image in this toolkit.
     * Otherwise, this method returns the construction status of a
     * scaled representation of the image at the specified width
     * and height.
     * <p>
     * This method does not cause the image to begin loading.
     * An application must call <code>prepareImage</code> to force
     * the loading of an image.
     * <p>
     * This method is called by the component's <code>checkImage</code>
     * methods.
     * <p>
     * Information on the flags returned by this method can be found
     * with the definition of the <code>ImageObserver</code> interface.
     * @param     image   the image whose status is being checked.
     * @param     width   the width of the scaled version whose status is
     *                 being checked, or <code>-1</code>.
     * @param     height  the height of the scaled version whose status
     *                 is being checked, or <code>-1</code>.
     * @param     observer   the <code>ImageObserver</code> object to be
     *                 notified as the image is being prepared.
     * @return    the bitwise inclusive <strong>OR</strong> of the
     *                 <code>ImageObserver</code> flags for the
     *                 image data that is currently available.
     * @see       java.awt.Toolkit#prepareImage(java.awt.Image,
     *                 int, int, java.awt.image.ImageObserver)
     * @see       java.awt.Component#checkImage(java.awt.Image,
     *                 java.awt.image.ImageObserver)
     * @see       java.awt.Component#checkImage(java.awt.Image,
     *                 int, int, java.awt.image.ImageObserver)
     * @see       java.awt.image.ImageObserver
     * @since     JDK1.0
     */
    public int checkImage(Image image, int width, int height, ImageObserver observer) {
        return ImageObserver.ALLBITS;
    }

    /**
     * Creates an image with the specified image producer.
     * @param     producer the image producer to be used.
     * @return    an image with the specified image producer.
     * @see       java.awt.Image
     * @see       java.awt.image.ImageProducer
     * @see       java.awt.Component#createImage(java.awt.image.ImageProducer)
     * @since     JDK1.0
     */
    public Image createImage(ImageProducer producer) {
        return new X11Image(producer);
    }

    /**
     * Creates an image which decodes the image stored in the specified
     * byte array, and at the specified offset and length.
     * The data must be in some image format, such as GIF or JPEG,
     * that is supported by this toolkit.
     * @param     imagedata   an array of bytes, representing
     *                         image data in a supported image format.
     * @param     imageoffset  the offset of the beginning
     *                         of the data in the array.
     * @param     imagelength  the length of the data in the array.
     * @return    an image.
     * @since     JDK1.1
     */
    public Image createImage(byte[] imagedata, int imageoffset, int imagelength) {
        ImageProducer ip = new ByteArrayImageSource(imagedata, imageoffset, imagelength);
        Image newImage = createImage(ip);
        return newImage;
    }

    /**
     * This method creates and returns a new <code>PrintJob</code>
     * instance which is the result of initiating a print operation
     * on the toolkit's platform.
     * <h3>Compatibility</h3>
     * In Both PersonalJava and Personal Profile, the PrintJob class
     * is optional If the platform does not support printing,
     * an <code>UnsupportedOperationException</code> is thrown.
     * @return    a <code>PrintJob</code> object, or
     *                  <code>null</code> if the user
     *                  cancelled the print job.
     * @exception UnsupportedOperationException if the implementation does not support printing.
     * @see       java.awt.PrintJob
     * @since     JDK1.1
     */
    public PrintJob getPrintJob(Frame frame, String jobtitle, Properties props) {
        throw new UnsupportedOperationException();
    }

    /**
     * Emits an audio beep.
     * @since     JDK1.1
     */
    public native void beep();

    /**
     * Gets an instance of the system clipboard which interfaces
     * with clipboard facilities provided by the native platform.
     * <p>
     * This clipboard enables data transfer between Java programs
     * and native applications which use native clipboard facilities.
     * @return    an instance of the system clipboard.
     * @see       java.awt.datatransfer.Clipboard
     * @since     JDK1.1
     */
    public Clipboard getSystemClipboard() {
        return clipboard;
    }

    protected EventQueue getSystemEventQueueImpl() {
        return eventQueue;
    }
}

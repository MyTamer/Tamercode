package org.springframework.richclient.swing.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

/**
 * A resource decorator that represents an underlying graphical AWT image, such
 * as a GIF, JPEG, or PNG.
 * 
 * @author Keith Donald
 */
public class AwtImageResource extends AbstractResource implements ImageObserver {

    private Resource wrappedResource;

    private boolean imageLoaded;

    private boolean imageError;

    public static final String RESOURCE_PREFIX = "image:";

    /**
     * Constructs a AwtImageResource for the following io.core.Resource. This
     * assumes the wrapped resource actually points to a underlying image.
     * 
     * @param resource
     *            The wrapped resource.
     * @throws IllegalArgumentException,
     *             if the resource is invalid.
     */
    public AwtImageResource(Resource resource) {
        Assert.notNull(resource);
        if (resource instanceof AwtImageResource) {
            throw new IllegalArgumentException("Wrapping another AwtImageResource instance is illegal.");
        }
        this.wrappedResource = resource;
    }

    public String getDescription() {
        return wrappedResource.getDescription();
    }

    public Resource createRelative(String relativePath) throws IOException {
        return wrappedResource.createRelative(relativePath);
    }

    public boolean exists() {
        return wrappedResource.exists();
    }

    public boolean isOpen() {
        return wrappedResource.isOpen();
    }

    public URL getURL() throws IOException {
        return wrappedResource.getURL();
    }

    public File getFile() throws IOException {
        return wrappedResource.getFile();
    }

    public String getFilename() throws IllegalStateException {
        return wrappedResource.getFilename();
    }

    public InputStream getInputStream() throws IOException {
        return wrappedResource.getInputStream();
    }

    /**
     * Loads the image from the underlying <code>core.io.Resource.</code>
     * 
     * This method does not cache. Calling it successively will result in a new
     * image being loaded each time.
     * 
     * @throws java.io.IOException
     *             If an error occurred while reading from the resource input
     *             stream.
     */
    public Image getImage() throws IOException {
        return loadImage(getInputStream());
    }

    /**
     * Load an image from the underlying resource's input stream. Constructs a
     * new <code>Image</code> object from the data read from the input stream
     * on each call.
     * <p>
     * This method loads the image fully into memory. This improves UI
     * responsiveness when the image is needed by the GUI event-dispatching
     * thread.
     * 
     * @param stream
     *            The input stream.
     * @return The resulting <code>Image</code> object.
     * @throws java.io.IOException
     *             If an error occurred while reading from the stream.
     */
    private synchronized Image loadImage(InputStream stream) throws IOException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        byte data[] = FileCopyUtils.copyToByteArray(stream);
        Image image = toolkit.createImage(data);
        imageLoaded = false;
        imageError = false;
        toolkit.prepareImage(image, -1, -1, this);
        while (!imageLoaded && !imageError) {
            try {
                wait();
            } catch (InterruptedException ex) {
            }
        }
        if (imageError) {
            throw new IOException("Error preparing image from resource.");
        }
        return image;
    }

    public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        if ((infoflags & (ALLBITS | FRAMEBITS)) != 0) {
            imageLoaded = true;
            notifyAll();
        } else {
            if ((infoflags & ERROR) != 0) {
                imageError = true;
                notifyAll();
            }
        }
        return true;
    }

    public boolean equals(Object o) {
        if (!(o instanceof AwtImageResource)) {
            return false;
        }
        AwtImageResource r = (AwtImageResource) o;
        return wrappedResource.equals(r.wrappedResource);
    }

    public int hashCode() {
        return wrappedResource.hashCode();
    }
}

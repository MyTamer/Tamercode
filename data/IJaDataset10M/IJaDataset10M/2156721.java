package org.springframework.richclient.image;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.util.CachingMapDecorator;

/**
 * The default implementation of ImageIconRegistry. This implementation caches
 * all icons using soft references (TODO it just lazy loads them, but it doesn't use SoftReference).
 * More specifically, cached icons will remain
 * in memory unless there is a shortage of resources in the system.
 * 
 * @author Keith Donald
 */
public class DefaultIconSource implements IconSource {

    protected static final Log logger = LogFactory.getLog(DefaultIconSource.class);

    private IconCache cache;

    /**
     * Default constructor.  Will obtain services dependencies from the ApplicationServices
     * locator.
     */
    public DefaultIconSource() {
        this((ImageSource) ApplicationServicesLocator.services().getService(ImageSource.class));
    }

    /**
     * Constructs a icon registry that loads images from the provided source.
     * 
     * @param images
     *            the image source.
     */
    public DefaultIconSource(ImageSource images) {
        this.cache = new IconCache(images);
    }

    public Icon getIcon(String key) {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Resolving icon with key '" + key + "'");
            }
            return (ImageIcon) cache.get(key);
        } catch (NoSuchImageResourceException e) {
            if (logger.isInfoEnabled()) {
                logger.info("No image resource found for icon with key '" + key + "'; returning a <null> icon.");
            }
            return null;
        }
    }

    public void clear() {
        cache.clear();
    }

    protected String doProcessImageKeyBeforeLookup(String key) {
        return key;
    }

    protected IconCache cache() {
        return cache;
    }

    /**
     * Icon cache using soft references.
     * 
     * @author Keith Donald
     */
    protected static class IconCache extends CachingMapDecorator {

        private ImageSource images;

        public IconCache(ImageSource images) {
            super(true);
            this.images = images;
        }

        public Object create(Object key) {
            Image image = images.getImage((String) key);
            return new ImageIcon(image);
        }

        public ImageSource images() {
            return images;
        }
    }
}

package com.gargoylesoftware.htmlunit.protocol.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A URLConnection for supporting data URLs.
 * @see <a href="http://www.ietf.org/rfc/rfc2397.txt">RFC2397</a>
 * @version $Revision: 6701 $
 * @author Marc Guillemot
 */
public class DataURLConnection extends URLConnection {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(DataURLConnection.class);

    /** The JavaScript "URL" prefix. */
    public static final String DATA_PREFIX = "data:";

    /** The JavaScript code. */
    private final byte[] content_;

    /**
     * Creates an instance.
     * @param url the data URL
     */
    public DataURLConnection(final URL url) {
        super(url);
        byte[] data = null;
        try {
            data = DataUrlDecoder.decode(url).getBytes();
        } catch (final UnsupportedEncodingException e) {
            LOG.error("Exception decoding " + url, e);
        } catch (final DecoderException e) {
            LOG.error("Exception decoding " + url, e);
        }
        content_ = data;
    }

    /**
     * This method does nothing in this implementation but is required to be implemented.
     */
    @Override
    public void connect() {
    }

    /**
     * Returns the input stream - in this case the content of the URL.
     * @return the input stream
     */
    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content_);
    }
}

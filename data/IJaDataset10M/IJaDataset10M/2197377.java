package org.gjt.sp.jedit.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Arrays;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.ServiceManager;

/**
 * A class for some static methods to deal with encodings.
 *
 * @since 4.3pre10
 * @author Kazutoshi Satoda
 */
public class EncodingServer {

    /**
	 * Returns an instance of Encoding for specified name.
	 * The name is used for search the following domains in the
	 * listed order.
	 *   - java.nio.charset.Charset
	 *   - jEdit ServiceManager
	 */
    public static Encoding getEncoding(String name) {
        try {
            return new CharsetEncoding(name);
        } catch (IllegalCharsetNameException e) {
        } catch (UnsupportedCharsetException e) {
        }
        Object namedService = ServiceManager.getService(serviceClass, name);
        if (namedService != null && namedService instanceof Encoding) {
            return (Encoding) namedService;
        }
        throw new UnsupportedCharsetException("No such encoding: \"" + name + "\"");
    }

    /**
	 * Returns the set of all available encoding names.
	 */
    public static Set<String> getAvailableNames() {
        Set<String> set = new HashSet<String>();
        set.addAll(Charset.availableCharsets().keySet());
        set.addAll(Arrays.asList(ServiceManager.getServiceNames(serviceClass)));
        return set;
    }

    /**
	 * Returns the set of user selected encoding names.
	 */
    public static Set<String> getSelectedNames() {
        Set<String> set = getAvailableNames();
        Iterator<String> i = set.iterator();
        while (i.hasNext()) {
            String name = i.next();
            if (jEdit.getBooleanProperty("encoding.opt-out." + name, false)) {
                i.remove();
            }
        }
        return set;
    }

    /**
	 * Returns a Reader object that reads the InputStream with
	 * the encoding. This method is same with
	 * "getEncoding(encoding).getTextReader(in)".
	 */
    public static Reader getTextReader(InputStream in, String encoding) throws IOException {
        return getEncoding(encoding).getTextReader(in);
    }

    /**
	 * Returns a Writer object that writes to the OutputStream with
	 * the encoding. This method is same with
	 * "getEncoding(encoding).getTextWriter(out)".
	 */
    public static Writer getTextWriter(OutputStream out, String encoding) throws IOException {
        return getEncoding(encoding).getTextWriter(out);
    }

    /**
	 * Returns if the specified name is supported as a name for an Encoding.
	 */
    public static boolean hasEncoding(String name) {
        try {
            if (Charset.isSupported(name)) {
                return true;
            }
        } catch (IllegalCharsetNameException e) {
        }
        return Arrays.asList(ServiceManager.getServiceNames(serviceClass)).contains(name);
    }

    private static final String serviceClass = "org.gjt.sp.jedit.io.Encoding";
}

package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.bzip2.CBZip2OutputStream;

/**
 * Compresses a file with the BZIP2 algorithm. Normally used to compress
 * non-compressed archives such as TAR files.
 *
 * @since Ant 1.5
 *
 * @ant.task category="packaging"
 */
public class BZip2 extends Pack {

    /**
     * Compress the zipFile.
     */
    protected void pack() {
        CBZip2OutputStream zOut = null;
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipFile));
            bos.write('B');
            bos.write('Z');
            zOut = new CBZip2OutputStream(bos);
            zipResource(getSrcResource(), zOut);
        } catch (IOException ioe) {
            String msg = "Problem creating bzip2 " + ioe.getMessage();
            throw new BuildException(msg, ioe, getLocation());
        } finally {
            FileUtils.close(zOut);
        }
    }

    /**
     * Whether this task can deal with non-file resources.
     *
     * <p>This implementation returns true only if this task is
     * &lt;bzip2&gt;.  Any subclass of this class that also wants to
     * support non-file resources needs to override this method.  We
     * need to do so for backwards compatibility reasons since we
     * can't expect subclasses to support resources.</p>
     * @return true if this task support non file resources.
     * @since Ant 1.7
     */
    protected boolean supportsNonFileResources() {
        return getClass().equals(BZip2.class);
    }
}

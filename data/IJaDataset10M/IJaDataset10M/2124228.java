package com.foursoft.foureveredit.view.impl.swing.editorcomponent;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.plaf.UIResource;
import com.foursoft.foureveredit.view.impl.swing.ViewManagerImpl;

/**
 * 4Soft Comments. In HTML Editor pane it was required to override
 * BasicTransferable. As BasicTransferable had some important members which had
 * only package visibility, it was necessary to provide a complete copy of the
 * class in the same package as HTMLEditorPane.
 * 
 * SUN Microsystems comments A transferable implementation for the default data
 * transfer of some Swing components.
 * 
 * @author Timothy Prinzing
 * @version 1.5 12/03/01
 */
class OverriddenBasicTransferable implements Transferable, UIResource {

    /**  */
    private static DataFlavor[] htmlFlavors;

    /**  */
    private static DataFlavor[] stringFlavors;

    /**  */
    private static DataFlavor[] plainFlavors;

    static {
        try {
            htmlFlavors = new DataFlavor[3];
            htmlFlavors[0] = new DataFlavor("text/html;class=java.lang.String");
            htmlFlavors[1] = new DataFlavor("text/html;class=java.io.Reader");
            htmlFlavors[2] = new DataFlavor("text/html;charset=unicode;class=java.io.InputStream");
            plainFlavors = new DataFlavor[3];
            plainFlavors[0] = new DataFlavor("text/plain;class=java.lang.String");
            plainFlavors[1] = new DataFlavor("text/plain;class=java.io.Reader");
            plainFlavors[2] = new DataFlavor("text/plain;charset=unicode;class=java.io.InputStream");
            stringFlavors = new DataFlavor[2];
            stringFlavors[0] = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.String");
            stringFlavors[1] = DataFlavor.stringFlavor;
        } catch (ClassNotFoundException cle) {
            ViewManagerImpl.getLog().error("Error initializing javax.swing.plaf.basic.BasicTransferable");
        }
    }

    /**
	 * Returns an array of DataFlavor objects indicating the flavors the data
	 * can be provided in. The array should be ordered according to preference
	 * for providing the data (from most richly descriptive to least
	 * descriptive).
	 * 
	 * @return an array of data flavors in which this data can be transferred
	 */
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] richerFlavors = getRicherFlavors();
        int nRicher = (richerFlavors != null) ? richerFlavors.length : 0;
        int nHTML = (isHTMLSupported()) ? htmlFlavors.length : 0;
        int nPlain = (isPlainSupported()) ? plainFlavors.length : 0;
        int nString = (isPlainSupported()) ? stringFlavors.length : 0;
        int nFlavors = nRicher + nHTML + nPlain + nString;
        DataFlavor[] flavors = new DataFlavor[nFlavors];
        int nDone = 0;
        if (nRicher > 0) {
            System.arraycopy(richerFlavors, 0, flavors, nDone, nRicher);
            nDone += nRicher;
        }
        if (nHTML > 0) {
            System.arraycopy(htmlFlavors, 0, flavors, nDone, nHTML);
            nDone += nHTML;
        }
        if (nPlain > 0) {
            System.arraycopy(plainFlavors, 0, flavors, nDone, nPlain);
            nDone += nPlain;
        }
        if (nString > 0) {
            System.arraycopy(stringFlavors, 0, flavors, nDone, nString);
            nDone += nString;
        }
        return flavors;
    }

    /**
	 * Returns whether or not the specified data flavor is supported for this
	 * object.
	 * 
	 * @param flavor
	 *            the requested flavor for the data
	 * @return boolean indicating whether or not the data flavor is supported
	 */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Returns an object which represents the data to be transferred. The class
	 * of the object returned is defined by the representation class of the
	 * flavor.
	 * 
	 * @param flavor
	 *            the requested flavor for the data
	 * 
	 * @return
	 * @exception IOException
	 *                if the data is no longer available in the requested
	 *                flavor.
	 * @exception UnsupportedFlavorException
	 *                if the requested data flavor is not supported.
	 */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (isRicherFlavor(flavor)) {
            return getRicherData(flavor);
        } else if (isHTMLFlavor(flavor)) {
            String data = getHTMLData();
            data = (data == null) ? "" : data;
            if (String.class.equals(flavor.getRepresentationClass())) {
                return data;
            } else if (Reader.class.equals(flavor.getRepresentationClass())) {
                return new StringReader(data);
            } else if (InputStream.class.equals(flavor.getRepresentationClass())) {
                byte[] bytes = data.getBytes();
                return new ByteArrayInputStream(bytes);
            }
        } else if (isPlainFlavor(flavor)) {
            String data = getPlainData();
            data = (data == null) ? "" : data;
            if (String.class.equals(flavor.getRepresentationClass())) {
                return data;
            } else if (Reader.class.equals(flavor.getRepresentationClass())) {
                return new StringReader(data);
            } else if (InputStream.class.equals(flavor.getRepresentationClass())) {
                byte[] bytes = data.getBytes();
                return new ByteArrayInputStream(bytes);
            }
        } else if (isStringFlavor(flavor)) {
            String data = getPlainData();
            data = (data == null) ? "" : data;
            return data;
        }
        throw new UnsupportedFlavorException(flavor);
    }

    /**
	 * 
	 * @param flavor
	 * 
	 * @return
	 */
    protected boolean isRicherFlavor(DataFlavor flavor) {
        DataFlavor[] richerFlavors = getRicherFlavors();
        int nFlavors = (richerFlavors != null) ? richerFlavors.length : 0;
        for (int i = 0; i < nFlavors; i++) {
            if (richerFlavors != null && richerFlavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Some subclasses will have flavors that are more descriptive than HTML or
	 * plain text. If this method returns a non-null value, it will be placed at
	 * the start of the array of supported flavors.
	 * 
	 * @return
	 */
    protected DataFlavor[] getRicherFlavors() {
        return null;
    }

    /**
	 * 
	 * @param flavor
	 * 
	 * @return
	 * 
	 * @throws UnsupportedFlavorException
	 */
    protected Object getRicherData(DataFlavor flavor) throws UnsupportedFlavorException {
        return null;
    }

    /**
	 * Returns whether or not the specified data flavor is an HTML flavor that
	 * is supported.
	 * 
	 * @param flavor
	 *            the requested flavor for the data
	 * @return boolean indicating whether or not the data flavor is supported
	 */
    protected boolean isHTMLFlavor(DataFlavor flavor) {
        DataFlavor[] flavors = htmlFlavors;
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Should the HTML flavors be offered? If so, the method getHTMLData should
	 * be implemented to provide something reasonable.
	 * 
	 * @return
	 */
    protected boolean isHTMLSupported() {
        return false;
    }

    /**
	 * Fetch the data in a text/html format
	 * 
	 * @return
	 */
    protected String getHTMLData() {
        return null;
    }

    /**
	 * Returns whether or not the specified data flavor is an plain flavor that
	 * is supported.
	 * 
	 * @param flavor
	 *            the requested flavor for the data
	 * @return boolean indicating whether or not the data flavor is supported
	 */
    protected boolean isPlainFlavor(DataFlavor flavor) {
        DataFlavor[] flavors = plainFlavors;
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Should the plain text flavors be offered? If so, the method getPlainData
	 * should be implemented to provide something reasonable.
	 * 
	 * @return
	 */
    protected boolean isPlainSupported() {
        return false;
    }

    /**
	 * Fetch the data in a text/plain format.
	 * 
	 * @return
	 */
    protected String getPlainData() {
        return null;
    }

    /**
	 * Returns whether or not the specified data flavor is a String flavor that
	 * is supported.
	 * 
	 * @param flavor
	 *            the requested flavor for the data
	 * @return boolean indicating whether or not the data flavor is supported
	 */
    protected boolean isStringFlavor(DataFlavor flavor) {
        DataFlavor[] flavors = stringFlavors;
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(flavor)) {
                return true;
            }
        }
        return false;
    }
}

package ncsa.hdf.hdf5lib.exceptions;

/**
 *  The class HDF5LibraryException returns errors raised by the HDF5
 *  library.
 *  <p>
 *  This sub-class represents HDF-5 major error code
 *       <b>H5E_FILE</b>
 */
public class HDF5FileInterfaceException extends HDF5LibraryException {

    /**
     * Constructs an <code>HDF5FileInterfaceException</code> with 
     * no specified detail message.
     */
    public HDF5FileInterfaceException() {
        super();
    }

    /**
     * Constructs an <code>HDF5FileInterfaceException</code> with 
     * the specified detail message.
     *
     * @param   s   the detail message.
     */
    public HDF5FileInterfaceException(String s) {
        super(s);
    }
}

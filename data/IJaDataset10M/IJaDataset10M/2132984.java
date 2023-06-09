package recoder.abstraction;

/**
 * @author Tobias Gutzmann
 *
 */
public interface ElementValuePair {

    /**
	 * Returns the value.
	 * The type of the value can be either of
	 * <ul>
	 * <li> any boxed primitive type
	 * <li> java.lang.String
	 * <li> java.lang.Class
	 * <li> a one-dimensional array of any of the above.
	 * </ul>
	 * @return
	 */
    Object getValue();

    String getElementName();
}

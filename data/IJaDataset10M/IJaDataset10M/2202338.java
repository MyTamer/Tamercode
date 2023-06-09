package ag.ion.bion.officelayer.filter;

import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.noa.filter.AbstractFilter;

/**
 * Contains information in order to export an OpenOffice.org document to encoded
 * text.
 * 
 * @author Markus Kr�ger
 * @author Andreas Br�ker
 * @version $Revision: 11619 $
 */
public class TextEncFilter extends AbstractFilter implements IFilter {

    /** Global filter for encoded text. */
    public static final IFilter FILTER = new TextEncFilter();

    private static final String FILE_EXTENSION = "txt";

    /**
	 * Returns definition of the filter. Returns null if the filter is not
	 * available for the submitted document type.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return definition of the filter or null if the filter is not available
	 *         for the submitted document type
	 * 
	 * @author Markus Kr�ger
	 * @date 13.03.2008
	 */
    public String getFilterDefinition(String documentType) {
        if (documentType.equals(IDocument.WRITER)) {
            return "Text (encoded)";
        } else if (documentType.equals(IDocument.GLOBAL)) {
            return "Text (encoded)(StarWriter/GlobalDocument)";
        } else if (documentType.equals(IDocument.WEB)) {
            return "Text (encoded)(StarWriter/Web)";
        }
        return null;
    }

    /**
	 * Returns file extension of the filter. Returns null if the document type
	 * is not supported by the filter.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return file extension of the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 03.04.2007
	 */
    public String getFileExtension(String documentType) {
        if (documentType == null) return null;
        if (documentType.equals(IDocument.WRITER)) {
            return FILE_EXTENSION;
        } else if (documentType.equals(IDocument.GLOBAL)) {
            return FILE_EXTENSION;
        } else if (documentType.equals(IDocument.WEB)) {
            return FILE_EXTENSION;
        }
        return null;
    }
}

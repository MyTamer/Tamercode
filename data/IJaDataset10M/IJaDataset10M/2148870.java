package ag.ion.bion.officelayer.filter;

import ag.ion.bion.officelayer.document.IDocument;
import ag.ion.noa.filter.AbstractFilter;

/**
 * Contains information in order to export an OpenOffice.org document to MS
 * Office 95.
 * 
 * @author Andreas Br�ker
 * @version $Revision: 11619 $
 */
public class MSOffice95Filter extends AbstractFilter implements IFilter {

    /** Global filter for MS Office 95. */
    public static final IFilter FILTER = new MSOffice95Filter();

    private static final String WORD_FILE_EXTENSION = "doc";

    private static final String EXCEL_FILE_EXTENSION = "xls";

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
            return "MS Word 95";
        } else if (documentType.equals(IDocument.CALC)) {
            return "MS Excel 95";
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
            return WORD_FILE_EXTENSION;
        } else if (documentType.equals(IDocument.CALC)) {
            return EXCEL_FILE_EXTENSION;
        }
        return null;
    }

    /**
	 * Returns name of the filter. Returns null if the submitted document type
	 * is not supported by the filter.
	 * 
	 * @param documentType
	 *            document type to be used
	 * 
	 * @return name of the filter
	 * 
	 * @author Markus Kr�ger
	 * @date 13.03.2008
	 */
    public String getName(String documentType) {
        if (documentType.equals(IDocument.WRITER)) {
            return "Microsoft Word 95";
        } else if (documentType.equals(IDocument.CALC)) {
            return "Microsoft Excel 95";
        }
        return null;
    }
}

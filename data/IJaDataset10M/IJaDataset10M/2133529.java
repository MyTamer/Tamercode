package monet.editors;

import monet.editors.scanners.XMLPartitionScanner;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class XMLDocumentProvider extends FileDocumentProvider {

    protected IDocument createDocument(Object element) throws CoreException {
        IDocument document = super.createDocument(element);
        if (document != null) {
            IDocumentPartitioner partitioner = new XMLPartitioner(new XMLPartitionScanner(), new String[] { XMLPartitionScanner.XML_START_TAG, XMLPartitionScanner.XML_PI, XMLPartitionScanner.XML_DOCTYPE, XMLPartitionScanner.XML_END_TAG, XMLPartitionScanner.XML_TEXT, XMLPartitionScanner.XML_CDATA, XMLPartitionScanner.XML_COMMENT, XMLPartitionScanner.XML_FIELD });
            partitioner.connect(document);
            document.setDocumentPartitioner(partitioner);
        }
        return document;
    }
}

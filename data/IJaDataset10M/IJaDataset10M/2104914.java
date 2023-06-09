package org.pdfbox.examples.fdf;

import java.io.IOException;
import org.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.pdfbox.pdmodel.interactive.form.PDField;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentCatalog;
import org.pdfbox.exceptions.COSVisitorException;
import org.pdfbox.examples.AbstractExample;

/**
 * This example will take a PDF document and set a FDF field in it.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @version $Revision: 1.7 $
 */
public class SetField extends AbstractExample {

    /**
     * This will set a single field in the document.
     *
     * @param pdfDocument The PDF to set the field in.
     * @param name The name of the field to set.
     * @param value The new value of the field.
     *
     * @throws IOException If there is an error setting the field.
     */
    public void setField(PDDocument pdfDocument, String name, String value) throws IOException {
        PDDocumentCatalog docCatalog = pdfDocument.getDocumentCatalog();
        PDAcroForm acroForm = docCatalog.getAcroForm();
        PDField field = acroForm.getField(name);
        if (field != null) {
            field.setValue(value);
        } else {
            System.err.println("No field found with name:" + name);
        }
    }

    /**
     * This will read a PDF file and set a field and then write it the pdf out again.
     * <br />
     * see usage() for commandline
     *
     * @param args command line arguments
     *
     * @throws IOException If there is an error importing the FDF document.
     * @throws COSVisitorException If there is an error writing the PDF.
     */
    public static void main(String[] args) throws IOException, COSVisitorException {
        SetField setter = new SetField();
        setter.setField(args);
    }

    private void setField(String[] args) throws IOException, COSVisitorException {
        PDDocument pdf = null;
        try {
            if (args.length != 3) {
                usage();
            } else {
                SetField example = new SetField();
                pdf = PDDocument.load(args[0]);
                example.setField(pdf, args[1], args[2]);
                pdf.save(args[0]);
            }
        } finally {
            if (pdf != null) {
                pdf.close();
            }
        }
    }

    /**
     * This will print out a message telling how to use this example.
     */
    private static void usage() {
        System.err.println("usage: org.pdfbox.examples.fdf.SetField <pdf-file> <field-name> <field-value>");
    }
}

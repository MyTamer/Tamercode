package org.pdfbox.pdmodel.interactive.form;

import java.io.IOException;
import org.pdfbox.cos.COSArray;
import org.pdfbox.cos.COSBase;
import org.pdfbox.cos.COSDictionary;
import org.pdfbox.cos.COSName;
import org.pdfbox.cos.COSString;
import org.pdfbox.cos.COSInteger;

/**
 * A class for handling the PDF field as a choicefield.
 *
 * @author sug
 * @version $Revision: 1.7 $
 */
public class PDChoiceField extends PDVariableText {

    /**
     * @see org.pdfbox.pdmodel.interactive.form.PDField#PDField(PDAcroForm,COSDictionary)
     *
     * @param theAcroForm The acroForm for this field.
     * @param field The field for this choice field.
     */
    public PDChoiceField(PDAcroForm theAcroForm, COSDictionary field) {
        super(theAcroForm, field);
    }

    /**
     * @see org.pdfbox.pdmodel.interactive.form.PDField#setValue(java.lang.String)
     *
     * @param optionValue The new value for this text field.
     *
     * @throws IOException If there is an error calculating the appearance stream or the value in not one
     *   of the existing options.
     */
    public void setValue(String optionValue) throws IOException {
        int indexSelected = -1;
        COSArray options = (COSArray) getDictionary().getDictionaryObject("Opt");
        if (options.size() == 0) {
            throw new IOException("Error: You cannot set a value for a choice field if there are no options.");
        } else {
            COSBase option = options.getObject(0);
            if (option instanceof COSArray) {
                for (int i = 0; i < options.size() && indexSelected == -1; i++) {
                    COSArray keyValuePair = (COSArray) options.get(i);
                    COSString key = (COSString) keyValuePair.getObject(0);
                    COSString value = (COSString) keyValuePair.getObject(1);
                    if (optionValue.equals(key.getString()) || optionValue.equals(value.getString())) {
                        super.setValue(value.getString());
                        getDictionary().setItem(COSName.getPDFName("V"), key);
                        indexSelected = i;
                    }
                }
            } else {
                for (int i = 0; i < options.size() && indexSelected == -1; i++) {
                    COSString value = (COSString) options.get(i);
                    if (optionValue.equals(value.getString())) {
                        super.setValue(optionValue);
                        indexSelected = i;
                    }
                }
            }
        }
        if (indexSelected == -1) {
            throw new IOException("Error: '" + optionValue + "' was not an available option.");
        } else {
            COSArray indexArray = (COSArray) getDictionary().getDictionaryObject("I");
            if (indexArray != null) {
                indexArray.clear();
                indexArray.add(new COSInteger(indexSelected));
            }
        }
    }
}

package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.FooterRecord;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.ss.usermodel.Footer;

/**
 * Class to read and manipulate the footer.
 * <P>
 * The footer works by having a left, center, and right side.  The total cannot
 * be more that 255 bytes long.  One uses this class by getting the HSSFFooter
 * from HSSFSheet and then getting or setting the left, center, and right side.
 * For special things (such as page numbers and date), one can use a the methods
 * that return the characters used to represent these.  One can also change the
 * fonts by using similar methods.
 * <P>
 * @author Shawn Laubach (slaubach at apache dot org)
 */
public final class HSSFFooter extends HeaderFooter implements Footer {

    private final PageSettingsBlock _psb;

    protected HSSFFooter(PageSettingsBlock psb) {
        _psb = psb;
    }

    protected String getRawText() {
        FooterRecord hf = _psb.getFooter();
        if (hf == null) {
            return "";
        }
        return hf.getText();
    }

    @Override
    protected void setHeaderFooterText(String text) {
        FooterRecord hfr = _psb.getFooter();
        if (hfr == null) {
            hfr = new FooterRecord(text);
            _psb.setFooter(hfr);
        } else {
            hfr.setText(text);
        }
    }
}

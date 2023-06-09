package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.io.StringReader;
import org.w3c.css.sac.InputSource;
import com.gargoylesoftware.htmlunit.Cache;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;

/**
 * The JavaScript object "HTMLStyleElement".
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HTMLStyleElement extends HTMLElement {

    private CSSStyleSheet sheet_;

    /**
     * Creates an instance.
     */
    public HTMLStyleElement() {
    }

    /**
     * Gets the associated sheet.
     * @see <a href="http://www.xulplanet.com/references/objref/HTMLStyleElement.html">Mozilla doc</a>
     * @return the sheet
     */
    public CSSStyleSheet jsxGet_sheet() {
        if (sheet_ != null) {
            return sheet_;
        }
        final HtmlStyle style = (HtmlStyle) getDomNodeOrDie();
        final String css = style.getTextContent();
        final Cache cache = getWindow().getWebWindow().getWebClient().getCache();
        final org.w3c.dom.css.CSSStyleSheet cached = cache.getCachedStyleSheet(css);
        final String uri = getDomNodeOrDie().getPage().getWebResponse().getWebRequest().getUrl().toExternalForm();
        if (cached != null) {
            sheet_ = new CSSStyleSheet(this, cached, uri);
        } else {
            final InputSource source = new InputSource(new StringReader(css));
            sheet_ = new CSSStyleSheet(this, source, uri);
            cache.cache(css, sheet_.getWrappedSheet());
        }
        return sheet_;
    }

    /**
     * Gets the associated sheet (IE).
     * @return the sheet
     */
    public CSSStyleSheet jsxGet_styleSheet() {
        return jsxGet_sheet();
    }
}

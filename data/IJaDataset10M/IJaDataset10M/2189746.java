package org.apache.wml;

/**
 * <p>The interface is modeled after DOM1 Spec for HTML from W3C.
 * The DTD used in this DOM model is from 
 * <a href="http://www.wapforum.org/DTD/wml_1.1.xml">
 * http://www.wapforum.org/DTD/wml_1.1.xml</a></p>
 *
 * <p>'timer' elements declares a card timer.
 * (Section 11.6.7, WAP WML Version 16-Jun-1999)</p>
 *
 * @version $Id: WMLTimerElement.java 447258 2006-09-18 05:41:23Z mrglavas $
 * @author <a href="mailto:david@topware.com.tw">David Li</a>
 */
public interface WMLTimerElement extends WMLElement {

    /**
     * 'name' specifies the name of variable ot be set with the value
     * of the timer.
     * (Section 11.6.7, WAP WML Version 16-Jun-1999)
     */
    public void setName(String newValue);

    public String getName();

    /**
     * 'value' indicates teh default of the variable 'name'
     * (Section 11.6.7, WAP WML Version 16-Jun-1999)
     */
    public void setValue(String newValue);

    public String getValue();
}

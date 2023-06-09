package org.apache.myfaces.shared_impl.taglib;

import org.apache.myfaces.shared_impl.renderkit.JSFAttr;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentELTag;

/**
 * @author Manfred Geiler (latest modification by $Author: matzew $)
 * @author Bruno Aranda (JSR-252)
 * @version $Revision: 557350 $ $Date: 2007-07-18 13:19:50 -0500 (Wed, 18 Jul 2007) $
 */
public abstract class UIComponentELTagBase extends UIComponentELTag {

    private ValueExpression _forceId;

    private ValueExpression _forceIdIndex;

    private static final Boolean DEFAULT_FORCE_ID_INDEX_VALUE = Boolean.TRUE;

    private ValueExpression _javascriptLocation;

    private ValueExpression _imageLocation;

    private ValueExpression _styleLocation;

    private ValueExpression _value;

    private ValueExpression _converter;

    public void release() {
        super.release();
        _forceId = null;
        _forceIdIndex = null;
        _value = null;
        _converter = null;
        _javascriptLocation = null;
        _imageLocation = null;
        _styleLocation = null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.FORCE_ID_ATTR, _forceId);
        setBooleanProperty(component, org.apache.myfaces.shared_impl.renderkit.JSFAttr.FORCE_ID_INDEX_ATTR, _forceIdIndex, DEFAULT_FORCE_ID_INDEX_VALUE);
        if (_javascriptLocation != null) setStringProperty(component, JSFAttr.JAVASCRIPT_LOCATION, _javascriptLocation);
        if (_imageLocation != null) setStringProperty(component, JSFAttr.IMAGE_LOCATION, _imageLocation);
        if (_styleLocation != null) setStringProperty(component, JSFAttr.STYLE_LOCATION, _styleLocation);
        setValueProperty(component, _value);
        setConverterProperty(component, _converter);
    }

    /**
     * Sets the forceId attribute of the tag.  NOTE: Not every tag that extends this class will
     * actually make use of this attribute.  Check the TLD to see which components actually
     * implement it.
     *
     * @param aForceId The value of the forceId attribute.
     */
    public void setForceId(ValueExpression aForceId) {
        _forceId = aForceId;
    }

    /**
     * Sets the forceIdIndex attribute of the tag.  NOTE: Not every tag that extends this class will
     * actually make use of this attribute.  Check the TLD to see which components actually implement it.
     *
     * @param aForceIdIndex The value of the forceIdIndex attribute.
     */
    public void setForceIdIndex(ValueExpression aForceIdIndex) {
        _forceIdIndex = aForceIdIndex;
    }

    public void setValue(ValueExpression value) {
        _value = value;
    }

    public void setConverter(ValueExpression converter) {
        _converter = converter;
    }

    /**
     * Sets the javascript location attribute of the tag.  NOTE: Not every tag that extends this class will
     * actually make use of this attribute.  Check the TLD to see which components actually implement it.
     *
     * @param aJavascriptLocation The alternate javascript location to use.
     */
    public void setJavascriptLocation(ValueExpression aJavascriptLocation) {
        _javascriptLocation = aJavascriptLocation;
    }

    /**
     * Sets the image location attribute of the tag.  NOTE: Not every tag that extends this class will
     * actually make use of this attribute.  Check the TLD to see which components actually implement it.
     *
     * @param aImageLocation The alternate image location to use.
     */
    public void setImageLocation(ValueExpression aImageLocation) {
        _imageLocation = aImageLocation;
    }

    /**
     * Sets the style location attribute of the tag.  NOTE: Not every tag that extends this class will
     * actually make use of this attribute.  Check the TLD to see which components actually implement it.
     *
     * @param aStyleLocation The alternate style location to use.
     */
    public void setStyleLocation(ValueExpression aStyleLocation) {
        _styleLocation = aStyleLocation;
    }

    protected void setIntegerProperty(UIComponent component, String propName, ValueExpression value) {
        UIComponentELTagUtils.setIntegerProperty(component, propName, value);
    }

    protected void setIntegerProperty(UIComponent component, String propName, ValueExpression value, Integer defaultValue) {
        UIComponentELTagUtils.setIntegerProperty(component, propName, value, defaultValue);
    }

    protected void setLongProperty(UIComponent component, String propName, ValueExpression value) {
        UIComponentELTagUtils.setLongProperty(component, propName, value);
    }

    protected void setLongProperty(UIComponent component, String propName, ValueExpression value, Long defaultValue) {
        UIComponentELTagUtils.setLongProperty(component, propName, value, defaultValue);
    }

    @Deprecated
    protected void setStringProperty(UIComponent component, String propName, String value) {
        UIComponentTagUtils.setStringProperty(getFacesContext(), component, propName, value);
    }

    protected void setStringProperty(UIComponent component, String propName, ValueExpression value) {
        UIComponentELTagUtils.setStringProperty(component, propName, value);
    }

    protected void setStringProperty(UIComponent component, String propName, ValueExpression value, String defaultValue) {
        UIComponentELTagUtils.setStringProperty(component, propName, value, defaultValue);
    }

    @Deprecated
    protected void setBooleanProperty(UIComponent component, String propName, String value) {
        UIComponentTagUtils.setBooleanProperty(getFacesContext(), component, propName, value);
    }

    protected void setBooleanProperty(UIComponent component, String propName, ValueExpression value) {
        UIComponentELTagUtils.setBooleanProperty(component, propName, value);
    }

    protected void setBooleanProperty(UIComponent component, String propName, ValueExpression value, Boolean defaultValue) {
        UIComponentELTagUtils.setBooleanProperty(component, propName, value, defaultValue);
    }

    private void setValueProperty(UIComponent component, ValueExpression value) {
        UIComponentELTagUtils.setValueProperty(getFacesContext(), component, value);
    }

    private void setConverterProperty(UIComponent component, ValueExpression value) {
        UIComponentELTagUtils.setConverterProperty(getFacesContext(), component, value);
    }

    protected void addValidatorProperty(UIComponent component, MethodExpression value) {
        UIComponentELTagUtils.addValidatorProperty(getFacesContext(), component, value);
    }

    @Deprecated
    protected void setActionProperty(UIComponent component, String action) {
        UIComponentTagUtils.setActionProperty(getFacesContext(), component, action);
    }

    protected void setActionProperty(UIComponent component, MethodExpression action) {
        UIComponentELTagUtils.setActionProperty(getFacesContext(), component, action);
    }

    @Deprecated
    protected void setActionListenerProperty(UIComponent component, String actionListener) {
        UIComponentTagUtils.setActionListenerProperty(getFacesContext(), component, actionListener);
    }

    protected void setActionListenerProperty(UIComponent component, MethodExpression actionListener) {
        UIComponentELTagUtils.addActionListenerProperty(getFacesContext(), component, actionListener);
    }

    protected void addValueChangedListenerProperty(UIComponent component, MethodExpression valueChangedListener) {
        UIComponentELTagUtils.addValueChangedListenerProperty(getFacesContext(), component, valueChangedListener);
    }

    protected void setValueBinding(UIComponent component, String propName, ValueExpression value) {
        UIComponentELTagUtils.setValueBinding(getFacesContext(), component, propName, value);
    }

    protected Object evaluateValueExpression(ValueExpression expression) {
        return UIComponentELTagUtils.evaluateValueExpression(getFacesContext().getELContext(), expression);
    }
}

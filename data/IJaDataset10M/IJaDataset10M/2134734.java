package com.google.gwt.aria.client;

import com.google.gwt.aria.client.CommonAttributeTypes.BooleanAndUndefined;
import com.google.gwt.aria.client.CommonAttributeTypes.IdReferenceList;
import com.google.gwt.aria.client.PropertyTokenTypes.DropeffectTokenList;
import com.google.gwt.aria.client.PropertyTokenTypes.LiveToken;
import com.google.gwt.aria.client.PropertyTokenTypes.RelevantTokenList;
import com.google.gwt.aria.client.StateTokenTypes.InvalidToken;
import com.google.gwt.dom.client.Element;

/**
 * Implements {@link TooltipRole}.
 */
class TooltipRoleImpl extends Role implements TooltipRole {

    TooltipRoleImpl(String roleName) {
        super(roleName);
    }

    @Override
    public String getAriaAtomicProperty(Element element) {
        return Property.ATOMIC.get(element);
    }

    @Override
    public String getAriaBusyState(Element element) {
        return State.BUSY.get(element);
    }

    @Override
    public String getAriaControlsProperty(Element element) {
        return Property.CONTROLS.get(element);
    }

    @Override
    public String getAriaDescribedbyProperty(Element element) {
        return Property.DESCRIBEDBY.get(element);
    }

    @Override
    public String getAriaDisabledState(Element element) {
        return State.DISABLED.get(element);
    }

    @Override
    public String getAriaDropeffectProperty(Element element) {
        return Property.DROPEFFECT.get(element);
    }

    @Override
    public String getAriaExpandedState(Element element) {
        return State.EXPANDED.get(element);
    }

    @Override
    public String getAriaFlowtoProperty(Element element) {
        return Property.FLOWTO.get(element);
    }

    @Override
    public String getAriaGrabbedState(Element element) {
        return State.GRABBED.get(element);
    }

    @Override
    public String getAriaHaspopupProperty(Element element) {
        return Property.HASPOPUP.get(element);
    }

    @Override
    public String getAriaHiddenState(Element element) {
        return State.HIDDEN.get(element);
    }

    @Override
    public String getAriaInvalidState(Element element) {
        return State.INVALID.get(element);
    }

    @Override
    public String getAriaLabelledbyProperty(Element element) {
        return Property.LABELLEDBY.get(element);
    }

    @Override
    public String getAriaLabelProperty(Element element) {
        return Property.LABEL.get(element);
    }

    @Override
    public String getAriaLiveProperty(Element element) {
        return Property.LIVE.get(element);
    }

    @Override
    public String getAriaOwnsProperty(Element element) {
        return Property.OWNS.get(element);
    }

    @Override
    public String getAriaRelevantProperty(Element element) {
        return Property.RELEVANT.get(element);
    }

    @Override
    public String getTabindexExtraAttribute(Element element) {
        return ExtraAttribute.TABINDEX.get(element);
    }

    @Override
    public void removeAriaAtomicProperty(Element element) {
        Property.ATOMIC.remove(element);
    }

    @Override
    public void removeAriaBusyState(Element element) {
        State.BUSY.remove(element);
    }

    @Override
    public void removeAriaControlsProperty(Element element) {
        Property.CONTROLS.remove(element);
    }

    @Override
    public void removeAriaDescribedbyProperty(Element element) {
        Property.DESCRIBEDBY.remove(element);
    }

    @Override
    public void removeAriaDisabledState(Element element) {
        State.DISABLED.remove(element);
    }

    @Override
    public void removeAriaDropeffectProperty(Element element) {
        Property.DROPEFFECT.remove(element);
    }

    @Override
    public void removeAriaExpandedState(Element element) {
        State.EXPANDED.remove(element);
    }

    @Override
    public void removeAriaFlowtoProperty(Element element) {
        Property.FLOWTO.remove(element);
    }

    @Override
    public void removeAriaGrabbedState(Element element) {
        State.GRABBED.remove(element);
    }

    @Override
    public void removeAriaHaspopupProperty(Element element) {
        Property.HASPOPUP.remove(element);
    }

    @Override
    public void removeAriaHiddenState(Element element) {
        State.HIDDEN.remove(element);
    }

    @Override
    public void removeAriaInvalidState(Element element) {
        State.INVALID.remove(element);
    }

    @Override
    public void removeAriaLabelledbyProperty(Element element) {
        Property.LABELLEDBY.remove(element);
    }

    @Override
    public void removeAriaLabelProperty(Element element) {
        Property.LABEL.remove(element);
    }

    @Override
    public void removeAriaLiveProperty(Element element) {
        Property.LIVE.remove(element);
    }

    @Override
    public void removeAriaOwnsProperty(Element element) {
        Property.OWNS.remove(element);
    }

    @Override
    public void removeAriaRelevantProperty(Element element) {
        Property.RELEVANT.remove(element);
    }

    @Override
    public void removeTabindexExtraAttribute(Element element) {
        ExtraAttribute.TABINDEX.remove(element);
    }

    @Override
    public void setAriaAtomicProperty(Element element, boolean value) {
        Property.ATOMIC.set(element, value);
    }

    @Override
    public void setAriaBusyState(Element element, boolean value) {
        State.BUSY.set(element, value);
    }

    @Override
    public void setAriaControlsProperty(Element element, IdReferenceList value) {
        Property.CONTROLS.set(element, value);
    }

    @Override
    public void setAriaDescribedbyProperty(Element element, IdReferenceList value) {
        Property.DESCRIBEDBY.set(element, value);
    }

    @Override
    public void setAriaDisabledState(Element element, boolean value) {
        State.DISABLED.set(element, value);
    }

    @Override
    public void setAriaDropeffectProperty(Element element, DropeffectTokenList value) {
        Property.DROPEFFECT.set(element, value);
    }

    @Override
    public void setAriaExpandedState(Element element, BooleanAndUndefined value) {
        State.EXPANDED.set(element, value);
    }

    @Override
    public void setAriaFlowtoProperty(Element element, IdReferenceList value) {
        Property.FLOWTO.set(element, value);
    }

    @Override
    public void setAriaGrabbedState(Element element, BooleanAndUndefined value) {
        State.GRABBED.set(element, value);
    }

    @Override
    public void setAriaHaspopupProperty(Element element, boolean value) {
        Property.HASPOPUP.set(element, value);
    }

    @Override
    public void setAriaHiddenState(Element element, boolean value) {
        State.HIDDEN.set(element, value);
    }

    @Override
    public void setAriaInvalidState(Element element, InvalidToken value) {
        State.INVALID.set(element, value);
    }

    @Override
    public void setAriaLabelledbyProperty(Element element, IdReferenceList value) {
        Property.LABELLEDBY.set(element, value);
    }

    @Override
    public void setAriaLabelProperty(Element element, String value) {
        Property.LABEL.set(element, value);
    }

    @Override
    public void setAriaLiveProperty(Element element, LiveToken value) {
        Property.LIVE.set(element, value);
    }

    @Override
    public void setAriaOwnsProperty(Element element, IdReferenceList value) {
        Property.OWNS.set(element, value);
    }

    @Override
    public void setAriaRelevantProperty(Element element, RelevantTokenList value) {
        Property.RELEVANT.set(element, value);
    }

    @Override
    public void setTabindexExtraAttribute(Element element, int value) {
        ExtraAttribute.TABINDEX.set(element, value);
    }
}

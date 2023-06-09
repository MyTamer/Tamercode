package org.wicketopia.editor.component.property;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidator;
import org.metastopheles.PropertyMetaData;
import org.wicketopia.builder.EditorBuilder;
import org.wicketopia.metadata.WicketopiaPropertyFacet;
import org.wicketopia.model.label.DisplayNameModel;

/**
 * @since 1.0
 */
public abstract class AbstractFormComponentPropertyEditor extends Panel implements EditorBuilder {

    private final FormComponent<?> formComponent;

    protected AbstractFormComponentPropertyEditor(String id, PropertyMetaData propertyMetaData, FormComponent<?> formComponent) {
        super(id);
        this.formComponent = formComponent;
        formComponent.setLabel(new DisplayNameModel(WicketopiaPropertyFacet.get(propertyMetaData)));
        add(formComponent);
    }

    public void addBehavior(Behavior behavior) {
        formComponent.add(behavior);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addValidator(IValidator validator) {
        formComponent.add(validator);
    }

    @Override
    public void enabled(boolean enabled) {
        formComponent.setEnabled(enabled);
    }

    public Component build() {
        return this;
    }

    @Override
    public void required(boolean required) {
        formComponent.setRequired(required);
    }

    @Override
    public void visible(boolean visible) {
        formComponent.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return formComponent.isVisible();
    }
}

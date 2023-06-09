package org.wicketrad.propertyeditor.validator;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;

/**
 * IFormValidator to be applied specifically to a form generated by a BeanEditPanel
 * @author wille
 *
 */
public interface IPropertyFormValidator extends IFormValidator {

    /**
	 * Should return a String-array of the bean-properties to which this validation should apply to (related properties)
	 * @return
	 */
    String[] properties();

    /**
	 * Automatically called by the BeanEditPanel, adds a FormComponent to an associated property-name of the bean.
	 * @param property
	 * @param component
	 */
    void addFormComponent(String property, FormComponent component);

    /**
	 * Allows retrieval of a FormComponent for a given bean property (useful for validate() implementation).
	 * @param property
	 * @return
	 */
    FormComponent getFormComponent(String property);
}

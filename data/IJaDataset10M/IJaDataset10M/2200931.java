package org.vaadin.addon.customfield;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import com.vaadin.data.Buffered;
import com.vaadin.data.Property;
import com.vaadin.data.Validatable;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.CompositeErrorMessage;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;

/**
 * A {@link CustomComponent} that implements the {@link Field} interface,
 * enabling the creation of e.g. form fields by composing Vaadin components.
 * Customization of both the visual presentation and the logic of the field is
 * possible.
 * 
 * Subclasses must at least implement the method {@link #getType()} and set the
 * composition root (typically in the constructor). In addition, other methods
 * can be overridden to customize the functionality.
 * 
 * Most custom fields can simply compose a user interface that calls the methods
 * {@link #setValue(Object)} and {@link #getValue()}.
 * 
 * It is also possible to override {@link #commit()},
 * {@link #setPropertyDataSource(Property)} and other logic of the field.
 * 
 * @author Matti Tahvonen
 * @author Henri Sara
 */
public abstract class CustomField extends CustomComponent implements Field {

    private static final long serialVersionUID = 5457282096887625533L;

    /**
     * Value of the abstract field.
     */
    private Object value;

    /**
     * Connected data-source.
     */
    private Property dataSource = null;

    /**
     * The list of validators.
     */
    private LinkedList<Validator> validators = null;

    /**
     * Auto commit mode.
     */
    private boolean writeTroughMode = true;

    /**
     * Reads the value from data-source, when it is not modified.
     */
    private boolean readTroughMode = true;

    /**
     * Is the field modified but not committed.
     */
    private boolean modified = false;

    /**
     * Current source exception.
     */
    private Buffered.SourceException currentBufferedSourceException = null;

    /**
     * Are the invalid values allowed in fields ?
     */
    private boolean invalidAllowed = true;

    /**
     * Are the invalid values committed ?
     */
    private boolean invalidCommitted = false;

    /**
     * The tab order number of this field.
     */
    private int tabIndex = 0;

    /**
     * Required field.
     */
    private boolean required = false;

    /**
     * The error message for the exception that is thrown when the field is
     * required but empty.
     */
    private String requiredError = "";

    /**
     * Is automatic validation enabled.
     */
    private boolean validationVisible = true;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (tabIndex != 0) {
            target.addAttribute("tabindex", tabIndex);
        }
        if (isModified()) {
            target.addAttribute("modified", true);
        }
        if (!isReadOnly() && isRequired()) {
            target.addAttribute("required", true);
        }
        if (isRequired() && isEmpty() && getComponentError() == null && getErrorMessage() != null) {
            target.addAttribute("hideErrors", true);
        }
        super.paintContent(target);
    }

    public abstract Class<?> getType();

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly() || (dataSource != null && dataSource.isReadOnly());
    }

    public boolean isInvalidCommitted() {
        return invalidCommitted;
    }

    public void setInvalidCommitted(boolean isCommitted) {
        invalidCommitted = isCommitted;
    }

    public void commit() throws Buffered.SourceException, InvalidValueException {
        if (dataSource != null && !dataSource.isReadOnly()) {
            if ((isInvalidCommitted() || isValid())) {
                final Object newValue = getValue();
                try {
                    dataSource.setValue(newValue);
                } catch (final Throwable e) {
                    currentBufferedSourceException = new Buffered.SourceException(this, e);
                    requestRepaint();
                    throw currentBufferedSourceException;
                }
            } else {
                validate();
            }
        }
        boolean repaintNeeded = false;
        if (modified) {
            modified = false;
            repaintNeeded = true;
        }
        if (currentBufferedSourceException != null) {
            currentBufferedSourceException = null;
            repaintNeeded = true;
        }
        if (repaintNeeded) {
            requestRepaint();
        }
    }

    public void discard() throws Buffered.SourceException {
        if (dataSource != null) {
            Object newValue;
            try {
                newValue = String.class == getType() ? dataSource.toString() : dataSource.getValue();
                if (currentBufferedSourceException != null) {
                    currentBufferedSourceException = null;
                    requestRepaint();
                }
            } catch (final Throwable e) {
                currentBufferedSourceException = new Buffered.SourceException(this, e);
                requestRepaint();
                throw currentBufferedSourceException;
            }
            final boolean wasModified = isModified();
            modified = false;
            if ((newValue == null && value != null) || (newValue != null && !newValue.equals(value))) {
                setInternalValue(newValue);
                fireValueChange(false);
            } else if (wasModified) {
                requestRepaint();
            }
        }
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isWriteThrough() {
        return writeTroughMode;
    }

    public void setWriteThrough(boolean writeTrough) throws Buffered.SourceException, InvalidValueException {
        if (writeTroughMode == writeTrough) {
            return;
        }
        writeTroughMode = writeTrough;
        if (writeTroughMode) {
            commit();
        }
    }

    public boolean isReadThrough() {
        return readTroughMode;
    }

    public void setReadThrough(boolean readTrough) throws Buffered.SourceException {
        if (readTroughMode == readTrough) {
            return;
        }
        readTroughMode = readTrough;
        if (!isModified() && readTroughMode && dataSource != null) {
            setInternalValue(String.class == getType() ? dataSource.toString() : dataSource.getValue());
            fireValueChange(false);
        }
    }

    /**
     * Returns the value of the Property in human readable textual format.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        final Object value = getValue();
        if (value == null) {
            return null;
        }
        return getValue().toString();
    }

    /**
     * Gets the current value of the field.
     * 
     * <p>
     * This is the visible, modified and possible invalid value the user have
     * entered to the field. In the read-through mode, the abstract buffer is
     * also updated and validation is performed.
     * </p>
     * 
     * <p>
     * Note that the object returned is compatible with getType(). For example,
     * if the type is String, this returns Strings even when the underlying
     * datasource is of some other type. In order to access the datasources
     * native type, use getPropertyDatasource().getValue() instead.
     * </p>
     * 
     * <p>
     * Note that when you extend CustomField, you must reimplement this method
     * if datasource.getValue() is not assignable to class returned by getType()
     * AND getType() is not String. In case of Strings, getValue() calls
     * datasource.toString() instead of datasource.getValue().
     * </p>
     * 
     * @return the current value of the field.
     */
    public Object getValue() {
        if (dataSource == null || !isReadThrough() || isModified()) {
            return value;
        }
        Object newValue = String.class == getType() ? dataSource.toString() : dataSource.getValue();
        if ((newValue == null && value != null) || (newValue != null && !newValue.equals(value))) {
            setInternalValue(newValue);
            fireValueChange(false);
        }
        return newValue;
    }

    public void setValue(Object newValue) throws Property.ReadOnlyException, Property.ConversionException {
        setValue(newValue, false);
    }

    /**
     * Sets the value of the field.
     * 
     * @param newValue
     *            the New value of the field.
     * @param repaintIsNotNeeded
     *            True iff caller is sure that repaint is not needed.
     * @throws Property.ReadOnlyException
     * @throws Property.ConversionException
     */
    protected void setValue(Object newValue, boolean repaintIsNotNeeded) throws Property.ReadOnlyException, Property.ConversionException {
        if ((newValue == null && value != null) || (newValue != null && !newValue.equals(value))) {
            if (isReadOnly()) {
                throw new Property.ReadOnlyException();
            }
            if (repaintIsNotNeeded && (isRequired() || getValidators() != null)) {
                repaintIsNotNeeded = false;
            }
            if (!isInvalidAllowed()) {
                final Collection<Validator> validators = getValidators();
                if (validators != null) {
                    for (Validator v : validators) {
                        v.validate(newValue);
                    }
                }
            }
            setInternalValue(newValue);
            modified = dataSource != null;
            if (isWriteThrough() && dataSource != null && (isInvalidCommitted() || isValid())) {
                try {
                    dataSource.setValue(newValue);
                    modified = false;
                } catch (final Throwable e) {
                    currentBufferedSourceException = new Buffered.SourceException(this, e);
                    requestRepaint();
                    throw currentBufferedSourceException;
                }
            }
            if (currentBufferedSourceException != null) {
                currentBufferedSourceException = null;
                requestRepaint();
            }
            fireValueChange(repaintIsNotNeeded);
        }
    }

    public Property getPropertyDataSource() {
        return dataSource;
    }

    /**
     * <p>
     * Sets the specified Property as the data source for the field. All
     * uncommitted changes to the field are discarded and the value is refreshed
     * from the new data source.
     * </p>
     * 
     * <p>
     * If the datasource has any validators, the same validators are added to
     * the field. Because the default behavior of the field is to allow invalid
     * values, but not to allow committing them, this only adds visual error
     * messages to fields and do not allow committing them as long as the value
     * is invalid. After the value is valid, the error message is not shown and
     * the commit can be done normally.
     * </p>
     * 
     * @param newDataSource
     *            the new data source Property.
     */
    public void setPropertyDataSource(Property newDataSource) {
        final Object oldValue = value;
        try {
            discard();
        } catch (final Buffered.SourceException ignored) {
        }
        if (dataSource != null && Property.ValueChangeNotifier.class.isAssignableFrom(dataSource.getClass())) {
            ((Property.ValueChangeNotifier) dataSource).removeListener(this);
        }
        dataSource = newDataSource;
        try {
            if (dataSource != null) {
                setInternalValue(String.class == getType() ? dataSource.toString() : dataSource.getValue());
            }
            modified = false;
        } catch (final Throwable e) {
            currentBufferedSourceException = new Buffered.SourceException(this, e);
            modified = true;
        }
        if (dataSource instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) dataSource).addListener(this);
        }
        if (dataSource instanceof Validatable) {
            final Collection<Validator> validators = ((Validatable) dataSource).getValidators();
            if (validators != null) {
                for (final Iterator<Validator> i = validators.iterator(); i.hasNext(); ) {
                    addValidator(i.next());
                }
            }
        }
        if ((value != oldValue) && ((value != null && !value.equals(oldValue)) || value == null)) {
            fireValueChange(false);
        }
    }

    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new LinkedList<Validator>();
        }
        validators.add(validator);
        requestRepaint();
    }

    /**
     * Gets the validators of the field.
     * 
     * @return the Unmodifiable collection that holds all validators for the
     *         field, not null.
     */
    public Collection<Validator> getValidators() {
        if (validators == null || validators.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(validators);
    }

    /**
     * Removes the validator from the field.
     * 
     * @param validator
     *            the validator to remove.
     */
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
        requestRepaint();
    }

    /**
     * Tests the current value against all registered validators.
     * 
     * @return <code>true</code> if all registered validators claim that the
     *         current value is valid, <code>false</code> otherwise.
     */
    public boolean isValid() {
        if (isEmpty()) {
            if (isRequired()) {
                return false;
            } else {
                return true;
            }
        }
        if (validators == null) {
            return true;
        }
        final Object value = getValue();
        for (final Iterator<Validator> i = validators.iterator(); i.hasNext(); ) {
            if (!(i.next()).isValid(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks the validity of the Validatable by validating the field with all
     * attached validators.
     * 
     * The "required" validation is a built-in validation feature. If the field
     * is required, but empty, validation will throw an EmptyValueException with
     * the error message set with setRequiredError().
     * 
     * @see com.vaadin.data.Validatable#validate()
     */
    public void validate() throws Validator.InvalidValueException {
        if (isEmpty()) {
            if (isRequired()) {
                throw new Validator.EmptyValueException(requiredError);
            } else {
                return;
            }
        }
        if (validators == null) {
            return;
        }
        Validator.InvalidValueException firstError = null;
        LinkedList<InvalidValueException> errors = null;
        final Object value = getValue();
        for (final Iterator<Validator> i = validators.iterator(); i.hasNext(); ) {
            try {
                (i.next()).validate(value);
            } catch (final Validator.InvalidValueException e) {
                if (firstError == null) {
                    firstError = e;
                } else {
                    if (errors == null) {
                        errors = new LinkedList<InvalidValueException>();
                        errors.add(firstError);
                    }
                    errors.add(e);
                }
            }
        }
        if (firstError == null) {
            return;
        }
        if (errors == null) {
            throw firstError;
        }
        final Validator.InvalidValueException[] exceptions = new Validator.InvalidValueException[errors.size()];
        int index = 0;
        for (final Iterator<InvalidValueException> i = errors.iterator(); i.hasNext(); ) {
            exceptions[index++] = i.next();
        }
        throw new Validator.InvalidValueException(null, exceptions);
    }

    /**
     * Fields allow invalid values by default. In most cases this is wanted,
     * because the field otherwise visually forget the user input immediately.
     * 
     * @return true iff the invalid values are allowed.
     * @see com.vaadin.data.Validatable#isInvalidAllowed()
     */
    public boolean isInvalidAllowed() {
        return invalidAllowed;
    }

    /**
     * Fields allow invalid values by default. In most cases this is wanted,
     * because the field otherwise visually forget the user input immediately.
     * <p>
     * In common setting where the user wants to assure the correctness of the
     * datasource, but allow temporarily invalid contents in the field, the user
     * should add the validators to datasource, that should not allow invalid
     * values. The validators are automatically copied to the field when the
     * datasource is set.
     * </p>
     * 
     * @see com.vaadin.data.Validatable#setInvalidAllowed(boolean)
     */
    public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
        this.invalidAllowed = invalidAllowed;
    }

    /**
     * Error messages shown by the fields are composites of the error message
     * thrown by the superclasses (that is the component error message),
     * validation errors and buffered source errors.
     * 
     * @see com.vaadin.ui.AbstractComponent#getErrorMessage()
     */
    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage validationError = null;
        if (isValidationVisible()) {
            try {
                validate();
            } catch (Validator.InvalidValueException e) {
                if (!e.isInvisible()) {
                    validationError = e;
                }
            }
        }
        final ErrorMessage superError = super.getErrorMessage();
        if (superError == null && validationError == null && currentBufferedSourceException == null) {
            return null;
        }
        return new CompositeErrorMessage(new ErrorMessage[] { superError, validationError, currentBufferedSourceException });
    }

    private static final Method VALUE_CHANGE_METHOD;

    static {
        try {
            VALUE_CHANGE_METHOD = Property.ValueChangeListener.class.getDeclaredMethod("valueChange", new Class[] { Property.ValueChangeEvent.class });
        } catch (final java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Internal error finding methods in AbstractField");
        }
    }

    public void addListener(Property.ValueChangeListener listener) {
        addListener(AbstractField.ValueChangeEvent.class, listener, VALUE_CHANGE_METHOD);
    }

    public void removeListener(Property.ValueChangeListener listener) {
        removeListener(AbstractField.ValueChangeEvent.class, listener, VALUE_CHANGE_METHOD);
    }

    /**
     * Emits the value change event. The value contained in the field is
     * validated before the event is created.
     */
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        fireEvent(new AbstractField.ValueChangeEvent(this));
        if (!repaintIsNotNeeded) {
            requestRepaint();
        }
    }

    /**
     * This method listens to data source value changes and passes the changes
     * forwards.
     * 
     * @param event
     *            the value change event telling the data source contents have
     *            changed.
     */
    public void valueChange(Property.ValueChangeEvent event) {
        if (isReadThrough() || !isModified()) {
            fireValueChange(false);
        }
    }

    @Override
    public void focus() {
        super.focus();
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * Sets the internal field value. This is purely used by CustomField to
     * change the internal Field value. It does not trigger valuechange events.
     * It can be overriden by the inheriting classes to update all dependent
     * variables.
     * 
     * @param newValue
     *            the new value to be set.
     */
    protected void setInternalValue(Object newValue) {
        value = newValue;
        if (validators != null && !validators.isEmpty()) {
            requestRepaint();
        }
    }

    /**
     * Is this field required. Required fields must filled by the user.
     * 
     * If the field is required, it is visually indicated in the user interface.
     * Furthermore, setting field to be required implicitly adds "non-empty"
     * validator and thus isValid() == false or any isEmpty() fields. In those
     * cases validation errors are not painted as it is obvious that the user
     * must fill in the required fields.
     * 
     * On the other hand, for the non-required fields isValid() == true if the
     * field isEmpty() regardless of any attached validators.
     * 
     * 
     * @return <code>true</code> if the field is required .otherwise
     *         <code>false</code>.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Sets the field required. Required fields must filled by the user.
     * 
     * If the field is required, it is visually indicated in the user interface.
     * Furthermore, setting field to be required implicitly adds "non-empty"
     * validator and thus isValid() == false or any isEmpty() fields. In those
     * cases validation errors are not painted as it is obvious that the user
     * must fill in the required fields.
     * 
     * On the other hand, for the non-required fields isValid() == true if the
     * field isEmpty() regardless of any attached validators.
     * 
     * @param required
     *            Is the field required.
     */
    public void setRequired(boolean required) {
        this.required = required;
        requestRepaint();
    }

    /**
     * Set the error that is show if this field is required, but empty. When
     * setting requiredMessage to be "" or null, no error pop-up or exclamation
     * mark is shown for a empty required field. This faults to "". Even in
     * those cases isValid() returns false for empty required fields.
     * 
     * @param requiredMessage
     *            Message to be shown when this field is required, but empty.
     */
    public void setRequiredError(String requiredMessage) {
        requiredError = requiredMessage;
        requestRepaint();
    }

    public String getRequiredError() {
        return requiredError;
    }

    /**
     * Is the field empty?
     * 
     * In general, "empty" state is same as null..
     */
    protected boolean isEmpty() {
        return (getValue() == null);
    }

    /**
     * Is automatic, visible validation enabled?
     * 
     * If automatic validation is enabled, any validators connected to this
     * component are evaluated while painting the component and potential error
     * messages are sent to client. If the automatic validation is turned off,
     * isValid() and validate() methods still work, but one must show the
     * validation in their own code.
     * 
     * @return True, if automatic validation is enabled.
     */
    public boolean isValidationVisible() {
        return validationVisible;
    }

    /**
     * Enable or disable automatic, visible validation.
     * 
     * If automatic validation is enabled, any validators connected to this
     * component are evaluated while painting the component and potential error
     * messages are sent to client. If the automatic validation is turned off,
     * isValid() and validate() methods still work, but one must show the
     * validation in their own code.
     * 
     * @param validateAutomatically
     *            True, if automatic validation is enabled.
     */
    public void setValidationVisible(boolean validateAutomatically) {
        if (validationVisible != validateAutomatically) {
            requestRepaint();
            validationVisible = validateAutomatically;
        }
    }

    public void setCurrentBufferedSourceException(Buffered.SourceException currentBufferedSourceException) {
        this.currentBufferedSourceException = currentBufferedSourceException;
        requestRepaint();
    }
}

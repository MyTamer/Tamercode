package br.com.sysmap.crux.widgets.client.maskedtextbox;

import br.com.sysmap.crux.core.client.formatter.FilterFormatter;
import br.com.sysmap.crux.core.client.formatter.Formatter;
import br.com.sysmap.crux.core.client.formatter.HasFormatter;
import br.com.sysmap.crux.core.client.formatter.MaskedFormatter;
import br.com.sysmap.crux.widgets.client.event.paste.HasPasteHandlers;
import br.com.sysmap.crux.widgets.client.event.paste.PasteEvent;
import br.com.sysmap.crux.widgets.client.event.paste.PasteEventSourceRegisterFactory;
import br.com.sysmap.crux.widgets.client.event.paste.PasteHandler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.event.dom.client.HasAllKeyHandlers;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.TextBox;

/**
 * 
 * @author Thiago da Rosa de Bustamante
 *
 */
public class MaskedTextBox extends Composite implements HasFormatter, HasDirection, HasChangeHandlers, HasValueChangeHandlers<String>, HasClickHandlers, HasAllFocusHandlers, HasAllKeyHandlers, HasAllMouseHandlers, HasName, HasPasteHandlers {

    public static final String DEFAULT_STYLE_NAME = "crux-MaskedTextBox";

    private static int currentId = 0;

    private Formatter formatter;

    private HandlerRegistration addBlurHandler;

    private boolean masked;

    private boolean filtered;

    protected TextBox textBox;

    MaskedInput maskedInput;

    /**
	 * 
	 * @param element
	 * @param formatter
	 * @return
	 */
    public static MaskedTextBox wrap(Element element, Formatter formatter) {
        return new MaskedTextBox(TextBox.wrap(element), formatter);
    }

    /**
	 * Constructor
	 * @param formatter
	 */
    public MaskedTextBox(Formatter formatter) {
        this(new TextBox(), formatter);
    }

    /**
	 * 
	 * @param textBox
	 * @param formatter
	 */
    protected MaskedTextBox(TextBox textBox, Formatter formatter) {
        String id = textBox.getElement().getId();
        if (id == null || id.length() == 0) {
            textBox.getElement().setId(generateNewId());
        }
        this.textBox = textBox;
        this.textBox.setStyleName(DEFAULT_STYLE_NAME);
        setFormatter(formatter);
        initWidget(this.textBox);
        PasteEventSourceRegisterFactory.getRegister().registerPasteEventSource(this, this.textBox.getElement());
    }

    /**
	 * 
	 * @return
	 */
    public Formatter getFormatter() {
        return formatter;
    }

    /**
	 * Sets a formatter for widget.
	 * @param formatter
	 */
    public void setFormatter(Formatter formatter) {
        setFormatter(formatter, true);
    }

    /**
	 * Sets a formatter for widget.
	 * @param formatter
	 * @param applyMask
	 */
    public void setFormatter(final Formatter formatter, boolean applyMask) {
        if (this.formatter != null) {
            if (addBlurHandler != null) {
                addBlurHandler.removeHandler();
            }
            if (this.masked) {
                ((MaskedFormatter) this.formatter).removeMask(this);
            }
            if (this.filtered) {
                ((FilterFormatter) this.formatter).removeFilter(this);
            }
        }
        if (formatter != null) {
            if (applyMask && (formatter instanceof MaskedFormatter)) {
                DeferredCommand.addCommand(new Command() {

                    public void execute() {
                        ((MaskedFormatter) formatter).applyMask(MaskedTextBox.this);
                    }
                });
            } else if (formatter instanceof FilterFormatter) {
                DeferredCommand.addCommand(new Command() {

                    public void execute() {
                        ((FilterFormatter) formatter).applyFilter(MaskedTextBox.this);
                    }
                });
            } else {
                addBlurHandler = addBlurHandler(new BlurHandler() {

                    public void onBlur(BlurEvent event) {
                        setUnformattedValue(getUnformattedValue());
                    }
                });
            }
        }
        this.formatter = formatter;
        this.masked = applyMask && (this.formatter instanceof MaskedFormatter);
        this.filtered = (this.formatter instanceof FilterFormatter);
    }

    /**
	 * @return
	 */
    public int getMaxLength() {
        return this.textBox.getMaxLength();
    }

    /**
	 * @param length
	 */
    public void setMaxLength(int length) {
        this.textBox.setMaxLength(length);
    }

    /**
	 * 
	 * @return
	 */
    public Object getUnformattedValue() {
        if (this.formatter != null) {
            return this.formatter.unformat(getValue());
        } else {
            return getValue();
        }
    }

    public String getValue() {
        return textBox.getValue();
    }

    /**
	 * 
	 * @param value
	 */
    public void setUnformattedValue(Object value) {
        if (this.formatter != null) {
            textBox.setValue(this.formatter.format(value));
        } else {
            textBox.setValue(value != null ? value.toString() : "");
        }
    }

    /**
	 * Creates a sequential id
	 * @return
	 */
    protected static String generateNewId() {
        return "_mask_" + (++currentId);
    }

    public Direction getDirection() {
        return textBox.getDirection();
    }

    public void setDirection(Direction direction) {
        textBox.setDirection(direction);
    }

    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return textBox.addChangeHandler(handler);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return textBox.addValueChangeHandler(handler);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return textBox.addClickHandler(handler);
    }

    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return textBox.addFocusHandler(handler);
    }

    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return textBox.addBlurHandler(handler);
    }

    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return textBox.addKeyUpHandler(handler);
    }

    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return textBox.addKeyDownHandler(handler);
    }

    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return textBox.addKeyPressHandler(handler);
    }

    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return textBox.addMouseDownHandler(handler);
    }

    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return textBox.addMouseUpHandler(handler);
    }

    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return textBox.addMouseOutHandler(handler);
    }

    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return textBox.addMouseOverHandler(handler);
    }

    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return textBox.addMouseMoveHandler(handler);
    }

    public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
        return textBox.addMouseWheelHandler(handler);
    }

    public boolean isReadOnly() {
        return textBox.isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
        textBox.setReadOnly(readOnly);
    }

    public String getName() {
        return textBox.getName();
    }

    public void setName(String name) {
        textBox.setName(name);
    }

    public int getTabIndex() {
        return textBox.getTabIndex();
    }

    public boolean isEnabled() {
        return textBox.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        textBox.setEnabled(enabled);
    }

    public void setFocus(boolean focused) {
        textBox.setFocus(focused);
    }

    public void setTabIndex(int index) {
        textBox.setTabIndex(index);
    }

    public void setAccessKey(char key) {
        textBox.setAccessKey(key);
    }

    public HandlerRegistration addPasteHandler(PasteHandler handler) {
        return addHandler(handler, PasteEvent.getType());
    }
}

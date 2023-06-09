package org.ujoframework.gxt.client.tools;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.user.client.Element;
import org.ujoframework.gxt.client.commons.Icons;

/**
 * Dialog pro zobrazení zpráv.
 * @author Hampl
 */
public class MessageDialog extends Dialog {

    private String message;

    private static MessageDialog instance;

    public MessageDialog(String message) {
        this.message = message;
        setButtons(Dialog.OK);
        setStyleName("commonMessageDialog");
    }

    public static MessageDialog getInstance(String message) {
        if (instance == null) {
            instance = new MessageDialog("");
        }
        instance.setMessage(message);
        instance.setButtons(Dialog.OK);
        return instance;
    }

    public void setMessage(String message) {
        this.message = message;
        if (rendered) {
            addText(message);
            show();
        }
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        setIcon(Icons.Pool.help());
        setHeading("Information");
        setBodyStyleName("pad-text");
        setScrollMode(Scroll.AUTO);
        setHideOnButtonClick(true);
        setModal(true);
        setClosable(true);
        setBlinkModal(true);
        addText(message);
    }

    public boolean isClickedOk(WindowEvent be) {
        return OK.equals(be.getButtonClicked().getItemId());
    }
}

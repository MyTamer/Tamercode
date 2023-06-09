package org.berlin.pino.dev.analy.win.base;

import javax.swing.JComponent;
import org.berlin.pino.dev.analy.win.action.AnalyAction;
import org.berlin.seesaw.app.ITeeterActionHandler;
import org.berlin.seesaw.swing.ITeeterButton;
import org.berlin.seesaw.swing.ITeeterPanel;
import org.berlin.seesaw.swing.ITeeterTextArea;
import org.berlin.seesaw.swing.gl.ITeeterGLCanvas;
import org.berlin.seesaw.swing.layout.ITeeterLayout;

/**
 * 
 * @author NNN 
 *
 */
public class BasicWindow implements IBasicWindow {

    private ITeeterLayout layout;

    private ITeeterPanel windowPanel;

    private ITeeterTextArea chatTextArea;

    private ITeeterTextArea inputTextArea;

    private ITeeterPanel buttonPanel;

    private ITeeterButton buttonEnter;

    private ITeeterButton buttonClear;

    private ITeeterButton buttonExit;

    private ITeeterPanel testSelectPanel;

    private ITeeterActionHandler actionHandler = new AnalyAction(this);

    /**
     * Method toString.
     * @return String
     */
    public String toString() {
        return String.format("#{Basic Window: %s - %s}", super.toString(), this.windowPanel);
    }

    public void setWindowPanel(final ITeeterPanel windowPanel) {
        this.windowPanel = windowPanel;
    }

    public void setChatTextArea(final ITeeterTextArea chatTextArea) {
        this.chatTextArea = chatTextArea;
    }

    public void setInputTextArea(final ITeeterTextArea inputTextArea) {
        this.inputTextArea = inputTextArea;
    }

    public void setButtonPanel(final ITeeterPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public void setButtonEnter(final ITeeterButton buttonEnter) {
        this.buttonEnter = buttonEnter;
    }

    public void setButtonClear(final ITeeterButton buttonClear) {
        this.buttonClear = buttonClear;
    }

    public void setButtonExit(final ITeeterButton buttonExit) {
        this.buttonExit = buttonExit;
    }

    /**
     * Method getComponent.
     * @return JComponent
     * @see org.berlin.seesaw.swing.ITeeterWidget#getComponent()
     */
    public JComponent getComponent() {
        return this.windowPanel.getComponent();
    }

    /**
     * @param layout the layout to set
     * @see org.berlin.pino.dev.analy.win.base.IBasicWindow#setLayout(ITeeterLayout)
     */
    public void setLayout(ITeeterLayout layout) {
        this.layout = layout;
    }

    /**
     * @return the chatTextArea
     * @see org.berlin.pino.dev.analy.win.base.IBasicWindow#getChatTextArea()
     */
    public ITeeterTextArea getChatTextArea() {
        return chatTextArea;
    }

    /**
     * @return the inputTextArea
     * @see org.berlin.pino.dev.analy.win.base.IBasicWindow#getInputTextArea()
     */
    public ITeeterTextArea getInputTextArea() {
        return inputTextArea;
    }

    /**
     * @return the buttonPanel
     * @see org.berlin.pino.dev.analy.win.base.IBasicWindow#getButtonPanel()
     */
    public ITeeterPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * @return the layout
     */
    public ITeeterLayout getLayout() {
        return layout;
    }

    /**
     * Method getText.
     * @return String
     * @see org.berlin.seesaw.swing.ITeeterWidget#getText()
     */
    public String getText() {
        return "";
    }

    /**
     * Method setText.
     * @param text String
     * @see org.berlin.seesaw.swing.ITeeterWidget#setText(String)
     */
    public void setText(String text) {
    }

    /**
     * @return the actionHandler
     * @see org.berlin.seesaw.app.ITeeterWindow#getActionHandler()
     */
    public ITeeterActionHandler getActionHandler() {
        return actionHandler;
    }

    /**
     * @param actionHandler the actionHandler to set
     * @see org.berlin.seesaw.app.ITeeterWindow#setActionHandler(ITeeterActionHandler)
     */
    public void setActionHandler(ITeeterActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    /**
     * @return the testSelectPanel
     */
    public ITeeterPanel getTestSelectPanel() {
        return testSelectPanel;
    }

    /**
     * @param testSelectPanel the testSelectPanel to set
     */
    public void setTestSelectPanel(ITeeterPanel testSelectPanel) {
        this.testSelectPanel = testSelectPanel;
    }
}

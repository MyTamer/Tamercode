package com.nexes.wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

/**
 * This class implements a basic wizard dialog, where the programmer can
 * insert one or more Components to act as panels. These panels can be navigated
 * through arbitrarily using the 'Next' or 'Back' buttons, or the dialog itself
 * can be closed using the 'Cancel' button. Note that even though the dialog
 * uses a CardLayout manager, the order of the panels is not linear. Each panel
 * determines at runtime what its next and previous panel will be.
 */
public class Wizard implements PropertyChangeListener {

    /**
     * Indicates that the 'Finish' button was pressed to close the dialog.
     */
    public static final int FINISH_RETURN_CODE = 0;

    /**
     * Indicates that the 'Cancel' button was pressed to close the dialog.
     */
    public static final int CANCEL_RETURN_CODE = 1;

    /**
     * Indicates that the dialog closed due to an internal error.
     */
    public static final int ERROR_RETURN_CODE = 2;

    /**
     * The String-based action command for the 'Help' button.
     */
    public static final String HELP_BUTTON_ACTION_COMMAND = "HelpButtonActionCommand";

    /**
     * The String-based action command for the 'Next' button.
     */
    public static final String NEXT_BUTTON_ACTION_COMMAND = "NextButtonActionCommand";

    /**
     * The String-based action command for the 'Back' button.
     */
    public static final String BACK_BUTTON_ACTION_COMMAND = "BackButtonActionCommand";

    /**
     * The String-based action command for the 'Cancel' button.
     */
    public static final String CANCEL_BUTTON_ACTION_COMMAND = "CancelButtonActionCommand";

    /**
     * The default text used for the 'Back' button. Good candidate for i18n.
     */
    public static final String DEFAULT_BACK_BUTTON_TEXT = "< Indietro";

    /**
     * The default text used for the 'Next' button. Good candidate for i18n.
     */
    public static final String DEFAULT_NEXT_BUTTON_TEXT = "Avanti >";

    /**
     * The default text used for the 'Finish' button. Good candidate for i18n.
     */
    public static final String DEFAULT_FINISH_BUTTON_TEXT = "Fine";

    /**
     * The default text used for the 'Cancel' button. Good candidate for i18n.
     */
    public static final String DEFAULT_CANCEL_BUTTON_TEXT = "Annulla";

    /**
     * The default text used for the 'Help' button. Good candidate for i18n.
     */
    public static final String DEFAULT_HELP_BUTTON_TEXT = "Aiuto";

    private WizardModel wizardModel;

    private WizardController wizardController;

    private JDialog wizardDialog;

    private int returnCode;

    private JPanel cardPanel;

    private CardLayout cardLayout;

    private JButton helpButton;

    private JButton backButton;

    private JButton nextButton;

    private JButton cancelButton;

    private boolean cancel = false;

    /**
     * Default constructor. This method creates a new WizardModel object and passes it
     * into the overloaded constructor.
     */
    public Wizard() {
        this(new WizardModel(), (Frame) null);
    }

    /**
     * This method creates a new WizardModel object and passes it into the overloaded
     * constructor. It accepts a java.awt.Dialog object as the javax.swing.JDialog's
     * parent.
     * @param owner The java.awt.Dialog object that is the owner of this dialog.
     */
    public Wizard(Dialog owner) {
        this(new WizardModel(), owner);
    }

    /**
     * This method creates a new WizardModel object and passes it into the overloaded
     * constructor. It accepts a java.awt.Dialog object as the javax.swing.JDialog's
     * parent.
     * @param owner The java.awt.Frame object that is the owner of the javax.swing.JDialog.
     */
    public Wizard(Frame owner) {
        this(new WizardModel(), owner);
    }

    /**
     * This constructor accepts a WizardModel object and a java.awt.Dialog object as the
     * wizard's JDialog's parent.
     * @param model The WizardModel object that serves as the model for the wizard dialog.
     * @param owner The java.awt.Dialog that is the owner of the generated javax.swing.JDialog.
     */
    public Wizard(WizardModel model, Dialog owner) {
        wizardDialog = new JDialog(owner);
        wizardDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        wizardModel = model;
        initComponents();
    }

    /**
     * This constructor accepts a WizardModel object and a java.awt.Frame object as the
     * wizard's javax.swing.JDialog's parent.
     * @param model The WizardModel object that serves as the model for this component.
     * @param owner The java.awt.Frame object that serves as the parent of the generated javax.swing.JDialog.
     */
    public Wizard(WizardModel model, Frame owner) {
        wizardDialog = new JDialog(owner);
        wizardDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        wizardModel = model;
        initComponents();
    }

    /**
     * Returns an instance of the JDialog that this class created. This is useful in
     * the event that you want to change any of the JDialog parameters manually.
     * @return The JDialog instance that this class created.
     */
    public JDialog getDialog() {
        return wizardDialog;
    }

    /**
     * Returns the owner of the generated javax.swing.JDialog.
     * @return The owner (java.awt.Frame or java.awt.Dialog) of the javax.swing.JDialog generated
     * by this class.
     */
    public Component getOwner() {
        return wizardDialog.getOwner();
    }

    /**
     * Sets the title of the generated javax.swing.JDialog.
     * @param s The title of the dialog.
     */
    public void setTitle(String s) {
        wizardDialog.setTitle(s);
    }

    /**
     * Returns the current title of the generated dialog.
     * @return The String-based title of the generated dialog.
     */
    public String getTitle() {
        return wizardDialog.getTitle();
    }

    /**
     * Sets the modality of the generated javax.swing.JDialog.
     * @param b the modality of the dialog
     */
    public void setModal(boolean b) {
        wizardDialog.setModal(b);
    }

    /**
     * Returns the modality of the dialog.
     * @return A boolean indicating whether or not the generated javax.swing.JDialog is modal.
     */
    public boolean isModal() {
        return wizardDialog.isModal();
    }

    /**
     * Convienenve method that displays a modal wizard dialog and blocks until the dialog
     * has completed.
     * @return Indicates how the dialog was closed. Compare this value against the RETURN_CODE
     * constants at the beginning of the class.
     */
    public int showModalDialog() {
        wizardDialog.setModal(true);
        wizardDialog.pack();
        wizardDialog.show();
        return returnCode;
    }

    /**
     * Returns the current model of the wizard dialog.
     * @return A WizardModel instance, which serves as the model for the wizard dialog.
     */
    public WizardModel getModel() {
        return wizardModel;
    }

    /**
     * Add a Component as a panel for the wizard dialog by registering its
     * WizardPanelDescriptor object. Each panel is identified by a unique Object-based
     * identifier (often a String), which can be used by the setCurrentPanel()
     * method to display the panel at runtime.
     * @param id An Object-based identifier used to identify the WizardPanelDescriptor object.
     * @param panel The WizardPanelDescriptor object which contains helpful information about the panel.
     */
    public void registerWizardPanel(Object id, WizardPanelDescriptor panel) {
        cardPanel.add(panel.getPanelComponent(), id);
        panel.setWizard(this);
        wizardModel.registerPanel(id, panel);
    }

    /**
     * Displays the panel identified by the object passed in. This is the same Object-based
     * identified used when registering the panel.
     * @param id The Object-based identifier of the panel to be displayed.
     */
    public void setCurrentPanel(Object id) {
        if (id == null) close(ERROR_RETURN_CODE);
        WizardPanelDescriptor oldPanelDescriptor = wizardModel.getCurrentPanelDescriptor();
        if (oldPanelDescriptor != null) if (oldPanelDescriptor.aboutToHidePanel() == -1) return;
        wizardModel.setCurrentPanel(id);
        wizardModel.getCurrentPanelDescriptor().aboutToDisplayPanel();
        cardLayout.show(cardPanel, id.toString());
        wizardModel.getCurrentPanelDescriptor().displayingPanel();
    }

    /**
     * Method used to listen for property change events from the model and update the
     * dialog's graphical components as necessary.
     * @param evt PropertyChangeEvent passed from the model to signal that one of its properties has changed value.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(WizardModel.CURRENT_PANEL_DESCRIPTOR_PROPERTY)) {
            wizardController.resetButtonsToPanelRules();
        } else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_TEXT_PROPERTY)) {
            nextButton.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_TEXT_PROPERTY)) {
            backButton.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_TEXT_PROPERTY)) {
            cancelButton.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(WizardModel.HELP_BUTTON_TEXT_PROPERTY)) {
            helpButton.setText(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_ENABLED_PROPERTY)) {
            nextButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        } else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_ENABLED_PROPERTY)) {
            backButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        } else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_ENABLED_PROPERTY)) {
            cancelButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        } else if (evt.getPropertyName().equals(WizardModel.HELP_BUTTON_ENABLED_PROPERTY)) {
            helpButton.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
        } else if (evt.getPropertyName().equals(WizardModel.NEXT_BUTTON_ICON_PROPERTY)) {
            nextButton.setIcon((Icon) evt.getNewValue());
        } else if (evt.getPropertyName().equals(WizardModel.BACK_BUTTON_ICON_PROPERTY)) {
            backButton.setIcon((Icon) evt.getNewValue());
        } else if (evt.getPropertyName().equals(WizardModel.CANCEL_BUTTON_ICON_PROPERTY)) {
            cancelButton.setIcon((Icon) evt.getNewValue());
        } else if (evt.getPropertyName().equals(WizardModel.HELP_BUTTON_ICON_PROPERTY)) {
            helpButton.setIcon((Icon) evt.getNewValue());
        }
    }

    /**
     * Retrieves the last return code set by the dialog.
     * @return An integer that identifies how the dialog was closed. See the *_RETURN_CODE
     * constants of this class for possible values.
     */
    public int getReturnCode() {
        return returnCode;
    }

    /**
     * Closes the dialog and sets the return code to the integer parameter.
     * @param code The return code.
     */
    void close(int code) {
        returnCode = code;
        wizardDialog.dispose();
    }

    /**
     * This method initializes the components for the wizard dialog: it creates a JDialog
     * as a CardLayout panel surrounded by a small amount of space on each side, as well
     * as three buttons at the bottom.
     */
    private void initComponents() {
        wizardModel.addPropertyChangeListener(this);
        wizardController = new WizardController(this);
        wizardDialog.getContentPane().setLayout(new BorderLayout());
        wizardDialog.setResizable(false);
        JPanel buttonPanel = new JPanel();
        JSeparator separator = new JSeparator();
        Box buttonBox = new Box(BoxLayout.LINE_AXIS);
        cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(550, 400));
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);
        backButton = new JButton();
        nextButton = new JButton();
        cancelButton = new JButton();
        helpButton = new JButton();
        backButton.setActionCommand(BACK_BUTTON_ACTION_COMMAND);
        nextButton.setActionCommand(NEXT_BUTTON_ACTION_COMMAND);
        cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
        helpButton.setActionCommand(HELP_BUTTON_ACTION_COMMAND);
        backButton.addActionListener(wizardController);
        nextButton.addActionListener(wizardController);
        cancelButton.addActionListener(wizardController);
        helpButton.addActionListener(wizardController);
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(separator, BorderLayout.NORTH);
        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonBox.add(helpButton);
        buttonBox.add(Box.createHorizontalStrut(150));
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        if (wizardModel.getBackButtonText() == null) wizardModel.setBackButtonText(Wizard.DEFAULT_BACK_BUTTON_TEXT);
        if (wizardModel.getNextButtonText() == null) wizardModel.setNextButtonText(Wizard.DEFAULT_NEXT_BUTTON_TEXT);
        if (wizardModel.getCancelButtonText() == null) wizardModel.setCancelButtonText(Wizard.DEFAULT_CANCEL_BUTTON_TEXT);
        if (wizardModel.getHelpButtonText() == null) wizardModel.setHelpButtonText(Wizard.DEFAULT_HELP_BUTTON_TEXT);
        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        wizardDialog.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        wizardDialog.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);
    }

    public void setCancel() {
        cancel = true;
    }

    public boolean getCancel() {
        return cancel;
    }
}

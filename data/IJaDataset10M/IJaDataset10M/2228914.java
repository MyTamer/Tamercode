package net.sf.freecol.client.gui.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import net.sf.freecol.FreeCol;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;
import net.sf.freecol.client.gui.i18n.Messages;

/**
 * Dialog for setting some options when loading a game.
 */
public final class LoadingSavegameDialog extends FreeColDialog<Boolean> implements ActionListener {

    private static final Logger logger = Logger.getLogger(LoadingSavegameDialog.class.getName());

    private JPanel buttons = new JPanel(new FlowLayout());

    private JLabel header;

    private JRadioButton singleplayer;

    private JRadioButton privateMultiplayer;

    private JRadioButton publicMultiplayer;

    private JTextField serverNameField;

    private JSpinner portField;

    /**
     * The constructor that will add the items to this panel.
     * @param freeColClient 
     *
     * @param parent The parent of this panel.
     */
    public LoadingSavegameDialog(FreeColClient freeColClient, GUI gui) {
        super(freeColClient, gui);
        setLayout(new BorderLayout());
        buttons.add(okButton);
        buttons.add(cancelButton);
        header = new JLabel(Messages.message("LoadingSavegame.title"), JLabel.CENTER);
        header.setFont(mediumHeaderFont);
        header.setBorder(new EmptyBorder(20, 0, 0, 0));
        add(header, BorderLayout.NORTH);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p1.add(new JLabel(Messages.message("LoadingSavegame.serverName"), JLabel.LEFT));
        panel.add(p1);
        serverNameField = new JTextField();
        panel.add(serverNameField);
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p2.add(new JLabel(Messages.message("LoadingSavegame.port"), JLabel.LEFT));
        panel.add(p2);
        portField = new JSpinner(new SpinnerNumberModel(FreeCol.getDefaultPort(), 1, 65536, 1));
        panel.add(portField);
        ButtonGroup bg = new ButtonGroup();
        singleplayer = new JRadioButton(Messages.message("LoadingSavegame.singleplayer"));
        bg.add(singleplayer);
        panel.add(singleplayer);
        privateMultiplayer = new JRadioButton(Messages.message("LoadingSavegame.privateMultiplayer"));
        bg.add(privateMultiplayer);
        panel.add(privateMultiplayer);
        publicMultiplayer = new JRadioButton(Messages.message("LoadingSavegame.publicMultiplayer"));
        bg.add(publicMultiplayer);
        panel.add(publicMultiplayer);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(panel, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        setSize(getPreferredSize());
    }

    public boolean isSingleplayer() {
        return singleplayer.isSelected();
    }

    public boolean isPublic() {
        return publicMultiplayer.isSelected();
    }

    public int getPort() {
        return ((Integer) portField.getValue()).intValue();
    }

    @Override
    public String getName() {
        return serverNameField.getName();
    }

    public void initialize(boolean publicServer, boolean singleplayer) {
        this.singleplayer.setSelected(false);
        this.privateMultiplayer.setSelected(false);
        this.publicMultiplayer.setSelected(false);
        if (singleplayer) {
            this.singleplayer.setSelected(true);
        } else if (publicServer) {
            this.publicMultiplayer.setSelected(true);
        } else {
            this.privateMultiplayer.setSelected(true);
        }
        this.serverNameField.setText("");
    }

    /**
     * This function analyses an event and calls the right methods to take care
     * of the user's requests.
     *
     * @param event The incoming ActionEvent.
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (OK.equals(command)) {
            getGUI().removeFromCanvas(this);
            setResponse(Boolean.TRUE);
        } else if (CANCEL.equals(command)) {
            getGUI().removeFromCanvas(this);
            setResponse(Boolean.FALSE);
        } else {
            logger.warning("Invalid ActionCommand: " + command);
        }
    }
}

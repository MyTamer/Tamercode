package net.sf.freecol.client.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.GUI;
import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.common.model.Europe;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.common.model.StringTemplate;
import net.sf.freecol.common.model.Unit.Role;
import net.sf.freecol.common.model.UnitType;

/**
 * The panel that allows a user to train people in Europe.
 */
public final class TrainDialog extends FreeColDialog<Integer> implements ActionListener {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(TrainDialog.class.getName());

    private final JLabel question;

    private final List<UnitType> trainableUnits = new ArrayList<UnitType>();

    private final Comparator<UnitType> unitPriceComparator;

    /**
     * The constructor to use.
     * @param freeColClient 
     */
    public TrainDialog(FreeColClient freeColClient, GUI gui, EuropePanel.EuropeAction europeAction) {
        super(freeColClient, gui);
        okButton.setText(Messages.message("trainDialog.done"));
        final Europe europe = getMyPlayer().getEurope();
        unitPriceComparator = new Comparator<UnitType>() {

            public int compare(final UnitType type1, final UnitType type2) {
                return (europe.getUnitPrice(type1) - europe.getUnitPrice(type2));
            }
        };
        switch(europeAction) {
            case TRAIN:
                trainableUnits.addAll(getSpecification().getUnitTypesTrainedInEurope());
                question = new JLabel(Messages.message("trainDialog.clickOn"));
                setLayout(new MigLayout("wrap 3", "[sg]", ""));
                break;
            case PURCHASE:
            default:
                trainableUnits.addAll(getSpecification().getUnitTypesPurchasedInEurope());
                question = new JLabel(Messages.message("purchaseDialog.clickOn"));
                setLayout(new MigLayout("wrap 2", "[sg]", ""));
        }
    }

    /**
     * Updates this panel's labels so that the information it displays is up to
     * date.
     */
    public void initialize() {
        removeAll();
        add(question, "span, wrap 20");
        final Player player = getMyPlayer();
        final Europe europe = player.getEurope();
        Collections.sort(trainableUnits, unitPriceComparator);
        for (UnitType unitType : trainableUnits) {
            int price = europe.getUnitPrice(unitType);
            JButton newButton = new JButton();
            newButton.setLayout(new MigLayout("wrap 2", "[60]", "[30][30]"));
            ImageIcon unitIcon = getLibrary().getUnitImageIcon(unitType, Role.DEFAULT, !player.checkGold(price), 0.66);
            JLabel unitName = localizedLabel(unitType.getNameKey());
            JLabel unitPrice = localizedLabel(StringTemplate.template("goldAmount").addAmount("%amount%", price));
            if (!player.checkGold(price)) {
                unitName.setEnabled(false);
                unitPrice.setEnabled(false);
                newButton.setEnabled(false);
            }
            newButton.add(new JLabel(unitIcon), "span 1 2");
            newButton.add(unitName);
            newButton.add(unitPrice);
            newButton.setActionCommand(unitType.getId());
            newButton.addActionListener(this);
            enterPressesWhenFocused(newButton);
            add(newButton, "grow");
        }
        add(okButton, "newline 20, span, tag ok");
        setSize(getPreferredSize());
        revalidate();
    }

    /**
     * Analyzes an event and calls the right external methods to take care of
     * the user's request.
     *
     * @param event The incoming action event
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (OK.equals(command)) {
            setResponse(new Integer(-1));
        } else {
            UnitType unitType = getSpecification().getUnitType(command);
            getController().trainUnitInEurope(unitType);
            initialize();
        }
    }
}

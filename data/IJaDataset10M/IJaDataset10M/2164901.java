package megamek.client.ui.AWT;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import megamek.client.ui.Messages;
import megamek.client.ui.AWT.widget.AdvancedLabel;
import megamek.common.Entity;
import megamek.common.MiscType;
import megamek.common.Mounted;

/**
 * A dialog displayed to the player when they want to lay mines with their BA
 * unit.
 */
public class MineLayingDialog extends Dialog implements ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 5096876851665428969L;

    private Button butOkay = new Button(Messages.getString("Okay"));

    private Button butCancel = new Button(Messages.getString("Cancel"));

    private AdvancedLabel labMessage;

    private boolean okay = true;

    /** The <code>int</code> ID of the entity that lays the mine. */
    private Entity entity = null;

    private Choice chMines = new Choice();

    private Vector<Integer> vMines = new Vector<Integer>();

    /**
     * Display a dialog that shows the mines on the entity, and allows the
     * player to choose one.
     * 
     * @param parent the <code>Frame</code> parent of this dialog
     * @param entity the <code>Entity</code> that carries the mines.
     */
    public MineLayingDialog(Frame parent, Entity entity) {
        super(parent, Messages.getString("MineLayingDialog.title"), true);
        this.entity = entity;
        labMessage = new AdvancedLabel(Messages.getString("MineLayingDialog.selectMineToLay", new Object[] { entity.getDisplayName() }));
        for (Mounted mount : entity.getMisc()) {
            if (mount.getType().hasFlag(MiscType.F_MINE) && mount.canFire()) {
                StringBuffer message = new StringBuffer();
                message.append(entity.getLocationName(mount.getLocation())).append(" ").append(mount.getDesc());
                chMines.add(message.toString());
                vMines.addElement(entity.getEquipmentNum(mount));
            }
        }
        butOkay.addActionListener(this);
        butCancel.addActionListener(this);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(labMessage, c);
        add(labMessage);
        gridbag.setConstraints(chMines, c);
        add(chMines);
        add(butOkay);
        add(butCancel);
        butOkay.requestFocus();
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        });
        pack();
        Dimension size = getSize();
        boolean updateSize = false;
        if (size.width < GUIPreferences.getInstance().getMinimumSizeWidth()) {
            size.width = GUIPreferences.getInstance().getMinimumSizeWidth();
        }
        if (size.height < GUIPreferences.getInstance().getMinimumSizeHeight()) {
            size.height = GUIPreferences.getInstance().getMinimumSizeHeight();
        }
        if (updateSize) {
            setSize(size);
            size = getSize();
        }
        setResizable(false);
        setLocation(parent.getLocation().x + parent.getSize().width / 2 - size.width / 2, parent.getLocation().y + parent.getSize().height / 2 - size.height / 2);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == butCancel) {
            okay = false;
        }
        this.setVisible(false);
    }

    public boolean getAnswer() {
        return (okay);
    }

    /**
     * Get the id of the mine the player wants to use.
     * 
     * @return the <code>int</code> id of the mine to lay
     */
    public int getMine() {
        Integer equipnr = vMines.elementAt(chMines.getSelectedIndex());
        Mounted mine = entity.getEquipment(equipnr);
        return entity.getEquipmentNum(mine);
    }
}

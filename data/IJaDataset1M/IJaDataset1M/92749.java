package fr.crnan.videso3d.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.mode.ExtendedMode;
import fr.crnan.videso3d.Context;
import fr.crnan.videso3d.DatabaseManager;
import fr.crnan.videso3d.DatabaseManager.Type;
import fr.crnan.videso3d.aip.AIP;
import fr.crnan.videso3d.ihm.components.TitledPanel;
import fr.crnan.videso3d.stip.StipController;
import fr.crnan.videso3d.stpv.StpvController;

/**
 * Panel d'infos contextuelles
 * @author Bruno Spyckerelle
 * @version 0.4.6
 */
public class ContextPanel extends JPanel {

    private JXTaskPaneContainer content = new JXTaskPaneContainer();

    private TitledPanel titleAreaPanel = new TitledPanel("Informations");

    private HashMap<DatabaseManager.Type, Context> taskpanes = new HashMap<DatabaseManager.Type, Context>();

    private DefaultSingleCDockable dockable;

    /**
	 * True if <code>content</code> contains at least one taskpane
	 */
    private boolean hasTaskpanes = false;

    public ContextPanel() {
        super();
        this.setPreferredSize(new Dimension(250, 0));
        this.setLayout(new BorderLayout());
        this.add(titleAreaPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void setDockable(DefaultSingleCDockable dockable) {
        this.dockable = dockable;
        this.remove(titleAreaPanel);
    }

    /**
	 * Ouvre le panneau
	 */
    public void open() {
        if (hasTaskpanes) {
            if (this.getParent() instanceof JSplitPane) {
                if (((JSplitPane) this.getParent()).getLeftComponent().equals(this)) {
                    ((JSplitPane) this.getParent()).setDividerLocation(250);
                }
            } else if (this.dockable != null && this.dockable.getExtendedMode() == ExtendedMode.MINIMIZED) {
                this.dockable.setExtendedMode(ExtendedMode.NORMALIZED);
            }
        }
    }

    /**
	 * Ajoute un JTaskPane avec ses éléments associés
	 * @param pane
	 * @param base type de base données à laquelle se réfère ces données
	 */
    public void addTaskPane(Context pane, DatabaseManager.Type base) {
        this.taskpanes.put(base, pane);
    }

    public void removeTaskPane(DatabaseManager.Type base) {
        this.taskpanes.remove(base);
    }

    /**
	 * Affiche les infos pertinentes pour l'objet en fonction de son type et de son nom
	 * @param name Nom de l'objet
	 */
    public void showInfo(DatabaseManager.Type base, int type, String name) {
        content.removeAll();
        hasTaskpanes = false;
        if (base != null) {
            if (dockable != null) {
                dockable.setTitleText("Informations sur " + name);
            } else {
                titleAreaPanel.setTitle("Informations sur " + name);
            }
            switch(base) {
                case STIP:
                    switch(type) {
                        case StipController.ROUTES:
                            this.addTaskpanes(Type.STIP, type, name);
                            this.addTaskpanes(Type.AIP, AIP.AWY, name);
                            break;
                        case StipController.SECTEUR:
                            this.addTaskpanes(Type.STIP, type, name);
                            this.addTaskpanes(Type.STPV, StpvController.SECTEUR, name);
                            break;
                        case StipController.BALISES:
                            this.addTaskpanes(Type.STIP, type, name);
                            this.addTaskpanes(Type.STPV, StpvController.BALISE, name);
                            break;
                        case StipController.ITI:
                            this.addTaskpanes(Type.STIP, type, name);
                            break;
                        case StipController.CONNEXION:
                            this.addTaskpanes(Type.STIP, type, name);
                            break;
                        case StipController.TRAJET:
                            this.addTaskpanes(Type.STIP, type, name);
                            break;
                    }
                    break;
                case AIP:
                    if (type == AIP.WPT) {
                        this.addTaskpanes(Type.STIP, StipController.BALISES, name);
                        this.addTaskpanes(Type.STPV, StpvController.BALISE, name);
                        this.addTaskpanes(base, type, name);
                    } else {
                        this.addTaskpanes(base, type, name);
                    }
                    break;
                default:
                    this.addTaskpanes(base, type, name);
            }
            content.validate();
            open();
        }
    }

    public void setTitle(String title) {
        if (dockable != null) {
            dockable.setTitleText(title);
        } else {
            this.titleAreaPanel.setTitle(title);
        }
    }

    public void setTaskPanes(Collection<JXTaskPane> taskpanes) {
        content.removeAll();
        hasTaskpanes = false;
        if (taskpanes != null) {
            for (JXTaskPane t : taskpanes) {
                content.add(t, null);
                hasTaskpanes = true;
            }
        }
        content.validate();
    }

    /**
	 * Ajoute les {@link JXTaskPane} demandés.<br />
	 * Prends en compte l'existence de la base demandée.
	 * @param base
	 * @param type
	 * @param name
	 */
    private void addTaskpanes(DatabaseManager.Type base, int type, String name) {
        if (taskpanes.get(base) != null) {
            List<JXTaskPane> panesList = taskpanes.get(base).getTaskPanes(type, name);
            if (panesList != null) {
                for (JXTaskPane pane : panesList) {
                    hasTaskpanes = true;
                    content.add(pane, null);
                }
            }
        }
    }
}

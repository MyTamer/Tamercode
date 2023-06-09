package jmri.jmrix.can.swing.monitor;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Swing action to create and register a
 *       			MonitorFrame object
 *
 * @author			Bob Jacobsen    Copyright (C) 2009
 * @version			$Revision: 1.2 $
 */
public class MonitorAction extends AbstractAction {

    public MonitorAction(String s) {
        super(s);
    }

    public MonitorAction() {
        this("CAN monitor");
    }

    public void actionPerformed(ActionEvent e) {
        MonitorFrame f = new MonitorFrame();
        try {
            f.initComponents();
        } catch (Exception ex) {
            log.warn("MonitorAction starting MonitorFrame: Exception: " + ex.toString());
        }
        f.setVisible(true);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MonitorAction.class.getName());
}

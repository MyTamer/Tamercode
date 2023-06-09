package jmri.jmris.srcp;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Swing action to create and register a
 * JmriSRCPServerControlFrame object
 *
 * @author              Paul Bender Copyright (C) 2009
 * @version             $Revision: 1.2 $
 */
public class JmriSRCPServerAction extends AbstractAction {

    public JmriSRCPServerAction(String s) {
        super(s);
    }

    public JmriSRCPServerAction() {
        this("Start SRCP Jmri Server");
    }

    public void actionPerformed(ActionEvent e) {
        JmriSRCPServer.instance().start();
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JmriSRCPServerAction.class.getName());
}

package gov.nasa.worldwindx.applications.sar.actions;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwindx.examples.util.ScreenShotAction;
import javax.swing.*;

/**
 * @author dcollins
 * @version $Id: SARScreenShotAction.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class SARScreenShotAction extends ScreenShotAction {

    public SARScreenShotAction(WorldWindow wwd, Icon icon) {
        super(wwd);
        this.putValue(Action.NAME, "Screen Shot...");
        this.putValue(Action.SHORT_DESCRIPTION, "Save a screen shot");
        this.putValue(Action.SMALL_ICON, icon);
    }
}

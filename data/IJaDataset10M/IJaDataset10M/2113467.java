package gov.nasa.worldwind.awt;

import java.awt.event.*;

/**
 * @author jym
 * @version $Id: ViewInputActionHandler.java 1 2011-07-16 23:22:47Z dcollins $
 */
public class ViewInputActionHandler implements KeyInputActionHandler, MouseInputActionHandler {

    public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
        return false;
    }

    public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, KeyEvent event, ViewInputAttributes.ActionAttributes viewAction) {
        return false;
    }

    public boolean inputActionPerformed(KeyEventState keys, String target, ViewInputAttributes.ActionAttributes viewAction) {
        return false;
    }

    public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseEvent mouseEvent, ViewInputAttributes.ActionAttributes viewAction) {
        return false;
    }

    public boolean inputActionPerformed(AbstractViewInputHandler inputHandler, java.awt.event.MouseWheelEvent mouseWheelEvent, ViewInputAttributes.ActionAttributes viewAction) {
        return false;
    }
}

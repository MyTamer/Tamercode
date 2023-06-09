package net.infonode.tabbedpanel.hover;

import net.infonode.gui.hover.HoverEvent;
import net.infonode.gui.hover.HoverListener;
import net.infonode.gui.hover.action.DelayedHoverExitAction;
import net.infonode.tabbedpanel.TabbedPanel;
import net.infonode.tabbedpanel.titledtab.TitledTab;
import net.infonode.tabbedpanel.titledtab.TitledTabProperties;
import javax.swing.*;

/**
 * <p>
 * TitledTabDelayedMouseExitHoverAction is an action that wraps a {@link HoverListener} and delays
 * the mouse exit when a {@link TitledTab} is no longer hovered. The action is meant to be set
 * as a {@link HoverListener} in the {@link TitledTabProperties}.
 * </p>
 *
 * <p>
 * If the TitledTab is hovered again before the delay has timed out, the timer is reset. If the
 * TitledTab is removed before the delay has timed out the hover listener's mouseExit() will be
 * called immediately.
 * </p>
 *
 * @author johan
 * @version $Revision: 1.6 $
 * @see TitledTab
 * @see TitledTabProperties
 * @since ITP 1.3.0
 */
public class TitledTabDelayedMouseExitHoverAction implements HoverListener {

    private DelayedHoverExitAction delayedAction;

    private HoverListener hoverListener;

    /**
   * Creates a TitledTabDelayedMouseExitHoverAction object with the given HoverListener as action
   *
   * @param delay         delay in milliseconds before the hover listener is called when the
   *                      titled tab is no longer hovered
   * @param hoverListener reference to a HoverListener
   */
    public TitledTabDelayedMouseExitHoverAction(int delay, HoverListener hoverListener) {
        this.hoverListener = hoverListener;
        delayedAction = new DelayedHoverExitAction(new HoverListener() {

            public void mouseEntered(HoverEvent event) {
                getHoverListener().mouseEntered(event);
            }

            public void mouseExited(HoverEvent event) {
                getHoverListener().mouseExited(event);
            }
        }, delay);
    }

    /**
   * Gets the hover listener
   *
   * @return the hoverListener.
   */
    public HoverListener getHoverListener() {
        return hoverListener;
    }

    /**
   * Gets the TitledTabProperties object for this action.
   *
   * @return reference to the TitledTabProperties or null
   *         if the delayed action is not a TitledTabHoverAction
   */
    public TitledTabProperties getTitledTabProperties() {
        if (getHoverListener() instanceof TitledTabHoverAction) return ((TitledTabHoverAction) getHoverListener()).getTitledTabProperties();
        return null;
    }

    public void mouseEntered(HoverEvent event) {
        delayedAction.mouseEntered(event);
    }

    public void mouseExited(HoverEvent event) {
        final TitledTab tab = (TitledTab) event.getSource();
        final TabbedPanel tp = tab.getTabbedPanel();
        delayedAction.mouseExited(event);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (tab.getTabbedPanel() != tp) {
                    delayedAction.forceExit(tab);
                }
            }
        });
    }
}

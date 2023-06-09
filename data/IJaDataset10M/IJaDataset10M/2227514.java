package xtrememp.ui.label;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.trident.Timeline.RepeatBehavior;
import org.pushingpixels.trident.swing.SwingRepaintTimeline;

/**
 * A simple circular animation, useful for denoting an action is taking
 * place that may take an unknown length of time to complete.
 *
 * @author Besmir Beqiri
 */
public class BusyLabel extends JLabel {

    private SwingRepaintTimeline repaintTimeline;

    private int angle;

    private boolean busy;

    public BusyLabel(Dimension size) {
        super();
        this.setMinimumSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.repaintTimeline = new SwingRepaintTimeline(this);
        this.repaintTimeline.addPropertyToInterpolate("angle", 0, 360);
        this.repaintTimeline.setDuration(36000);
        this.repaintTimeline.setAutoRepaintMode(false);
    }

    /**
     * Gets the current angle used in the animation. Used by the timeline to
     * interpolate angle property.
     *
     * @return the angle.
     */
    public int getAngle() {
        return angle;
    }

    /**
     * Sets the current angle used in the animation. Used by the timeline to
     * interpolate angle property.
     *
     * @param angle The angle to set.
     */
    public void setAngle(int angle) {
        this.angle = angle;
        this.repaintTimeline.forceRepaintOnNextPulse();
    }

    /**
     * Gets whether this <code>BusyLabel</code> is busy. If busy, then
     * the <code>BusyLabel</code> instance will indicate that it is busy,
     * generally by animating some state.
     *
     * @return <code>true</code> if this instance is busy.
     */
    public boolean isBusy() {
        return busy;
    }

    /**
     * Sets whether this <code>BusyLabel</code> instance should consider
     * itself busy. A busy component indicate that it is busy via animation.
     *
     * @param busy Whether this <code>BusyLabel</code> instance should
     *        consider itself busy.
     */
    public void setBusy(boolean busy) {
        this.busy = busy;
        if (busy) {
            repaintTimeline.playLoop(RepeatBehavior.LOOP);
        } else {
            repaintTimeline.abort();
            this.repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        int width = getWidth();
        int height = getHeight();
        int points = 8;
        Color fgColor = getForeground();
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            ComponentState labelState = isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            fgColor = SubstanceLookAndFeel.getCurrentSkin().getColorScheme(this, labelState).getForegroundColor();
        }
        for (int i = 0; i < points; i++) {
            if (busy) {
                int alpha = 255 * ((i + angle) % points) / points;
                g2d.setColor(new Color(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue(), alpha));
            } else {
                g2d.setColor(fgColor);
            }
            double a = 2 * Math.PI / points, x = Math.sin(i * a), y = Math.cos(i * a);
            g2d.drawLine(width / 2 + (int) (5 * x), height / 2 + (int) (5 * y), width / 2 + (int) (width / 2 * x), height / 2 + (int) (height / 2 * y));
        }
        g2d.dispose();
    }
}

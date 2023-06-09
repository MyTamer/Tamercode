package ch.unizh.ini.jaer.projects.gesture.wakeup;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Font;
import javax.media.opengl.*;
import javax.media.opengl.glu.*;
import net.sf.jaer.Description;
import net.sf.jaer.chip.AEChip;
import net.sf.jaer.event.*;
import net.sf.jaer.eventprocessing.EventFilter2D;
import net.sf.jaer.graphics.FrameAnnotater;
import net.sf.jaer.util.SpikeSound;

/**
 * Activity and simple gesture-based wakeup detector.
 * @author tobi & christian
 */
@Description("<html>Activity-level and simple gesture wakeup detector.<br>Uses 2 IF digital neurons that slew towards zero. <br>")
public class WakeupDetector extends EventFilter2D implements FrameAnnotater {

    public int onCount, offCount, maxCount, wakupCount, lastOffThr, lastOnThr, onInterval, offInterval;

    private int minDt = 10000;

    private boolean isOn = false;

    private boolean measureOn = true;

    private SpikeSound spikeSound = new SpikeSound();

    private int tolerance = getInt("tolerance", 50000);

    {
        setPropertyTooltip("tolerance", "time [us] that two intervals are allowed to differ");
    }

    private int tapLength = getInt("tapLength", 270000);

    {
        setPropertyTooltip("tapLength", "time [us] that two intervals are allowed to differ");
    }

    private int decayCount = getPrefs().getInt("decayCount", 3);

    {
        setPropertyTooltip("decayCount", "time [us] to wait for the subtraction of 1000 events");
    }

    private int threshold = getInt("threshold", 8000);

    {
        setPropertyTooltip("threshold", "threshold in accumlated events for counter neuron to swap to detecting other event polarity for wakeup");
    }

    public WakeupDetector(AEChip chip) {
        super(chip);
    }

    @Override
    public EventPacket<?> filterPacket(EventPacket<?> in) {
        for (BasicEvent e : in) {
            PolarityEvent ev = (PolarityEvent) e;
            if (ev.polarity == PolarityEvent.Polarity.On) {
                onCount = onCount + 1;
                if (onCount > maxCount) {
                    maxCount = onCount;
                }
                if (onCount == threshold && ev.timestamp > lastOnThr + minDt && measureOn) {
                    onInterval = ev.timestamp - lastOnThr;
                    lastOnThr = ev.timestamp;
                    System.out.println("On transition interval:" + onInterval);
                    if (onInterval > tapLength - tolerance && onInterval < tapLength + tolerance && offInterval > tapLength - tolerance && offInterval < tapLength + tolerance) {
                        wakeupDetected();
                    }
                    measureOn = false;
                }
            } else {
                offCount = offCount + 1;
                if (offCount > maxCount) {
                    maxCount = offCount;
                }
                if (offCount == threshold && ev.timestamp > lastOffThr + minDt && !measureOn) {
                    offInterval = ev.timestamp - lastOffThr;
                    lastOffThr = ev.timestamp;
                    System.out.println("Off transition interval:" + offInterval);
                    if (onInterval > tapLength - tolerance && onInterval < tapLength + tolerance && offInterval > tapLength - tolerance && offInterval < tapLength + tolerance) {
                        wakeupDetected();
                    }
                    measureOn = true;
                }
            }
        }
        return in;
    }

    private void wakeupDetected() {
        wakupCount++;
        spikeSound.play();
        if (isOn) {
            isOn = false;
        } else {
            isOn = true;
        }
    }

    class DecayThread extends Thread {

        public void run() {
            while (true) {
                if (onCount > 1000) {
                    onCount = onCount - 1000;
                } else if (onCount > 0) {
                    onCount = onCount - 1;
                }
                if (offCount > 1000) {
                    offCount = offCount - 1000;
                } else if (offCount > 0) {
                    offCount = offCount - 1;
                }
                try {
                    this.sleep(decayCount);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    @Override
    public void resetFilter() {
        onCount = 0;
        offCount = 0;
        maxCount = 0;
        wakupCount = 0;
        lastOffThr = 0;
        lastOnThr = 0;
        onInterval = 0;
        offInterval = 0;
        Thread decayThread = new DecayThread();
        decayThread.start();
    }

    @Override
    public void initFilter() {
        resetFilter();
    }

    GLU glu = null;

    private TextRenderer renderer = null;

    @Override
    public void annotate(GLAutoDrawable drawable) {
        if (!isFilterEnabled()) return;
        GL gl = drawable.getGL();
        gl.glPushMatrix();
        gl.glLineWidth(2);
        if (renderer == null) {
            renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 14), true, true);
            renderer.setColor(1, .2f, .2f, 0.4f);
        }
        renderer.begin3DRendering();
        renderer.draw3D(Integer.toString(wakupCount), -30f, 0f, 0f, .4f);
        renderer.end3DRendering();
        gl.glColor3f(0.1f, 0.1f, 0.7f);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2f(-11, 128 * (float) (threshold) / (float) (maxCount));
        gl.glVertex2f(-1, 128 * (float) (threshold) / (float) (maxCount));
        gl.glEnd();
        gl.glColor3f(1, 1, 1);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(-10, 0);
        gl.glVertex2f(-6, 0);
        gl.glVertex2f(-6, 128 * (float) (onCount) / (float) (maxCount));
        gl.glVertex2f(-10, 128 * (float) (onCount) / (float) (maxCount));
        gl.glEnd();
        gl.glColor3f(0, 0, 0);
        gl.glBegin(GL.GL_QUADS);
        gl.glVertex2f(-6, 0);
        gl.glVertex2f(-2, 0);
        gl.glVertex2f(-2, 128 * (float) (offCount) / (float) (maxCount));
        gl.glVertex2f(-6, 128 * (float) (offCount) / (float) (maxCount));
        gl.glEnd();
        if (isOn) {
            gl.glColor3f(0, 1, 0);
            gl.glBegin(GL.GL_QUADS);
            gl.glVertex2f(130, 120);
            gl.glVertex2f(130, 125);
            gl.glVertex2f(135, 125);
            gl.glVertex2f(135, 120);
            gl.glEnd();
        }
        gl.glPopMatrix();
    }

    public int getTolerance() {
        return tolerance;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
        putInt("tolerance", tolerance);
    }

    public int getDecayCount() {
        return decayCount;
    }

    public void setDecayCount(int decayCount) {
        this.decayCount = decayCount;
        putInt("decayCount", decayCount);
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
        putInt("threshold", threshold);
    }

    public int getTapLength() {
        return tapLength;
    }

    public void setTapLength(int tapLength) {
        this.tapLength = tapLength;
        putInt("tapLength", tapLength);
    }
}

package alice.tuprologx.ide;

import java.awt.*;
import java.awt.event.*;

/**
 * A launcher for AWT <code>Frame</code>s.
 * 
 * @author	<a href="mailto:giulio.piancastelli@studio.unibo.it">Giulio Piancastelli</a>
 * @version	1.0 - 17-dic-02
 */
public class AWTFrameLauncher implements FrameLauncher {

    /** The path to the icon to be displayed on the title bar of each frame. */
    private String iconPathName = "";

    public void launchFrame(Component content) {
        this.launchFrame(content, "Frame", 0, 0);
    }

    public void launchFrame(Component content, String title, int width, int height) {
        new AWTFrame(content, title, width, height);
    }

    public void setFrameIcon(String imagePathName) {
        iconPathName = imagePathName;
    }

    public String getFrameIcon() {
        return iconPathName;
    }

    /**
     * A class representing a specialization of an AWT Frame to be used by this
     * FrameLauncher, inspired by the FrameLauncher class in the Thinlet package
     * (see <a href="http://www.thinlet.com">Thinlet web site</a> for details).
     *
     * @author	<a href="mailto:giulio.piancastelli@studio.unibo.it">Giulio Piancastelli</a>
     * @version	1.0 - 17-dic-02
     */
    private class AWTFrame extends Frame {

        private transient Image doubleBuffer;

        public AWTFrame(Component content, String title, int width, int height) {
            super(title);
            initComponents(content, width, height);
        }

        /**
         * Initialize the graphic components.
         */
        private void initComponents(Component content, int width, int height) {
            setLayout(new BorderLayout());
            add(content, BorderLayout.CENTER);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent event) {
                    dispose();
                }
            });
            if (iconPathName != "") setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(getFrameIcon())));
            pack();
            if ((width > 0) && (height > 0)) {
                Insets insets = getInsets();
                width += insets.left + insets.right;
                height += insets.top + insets.bottom;
                Dimension screenSize = getToolkit().getScreenSize();
                width = Math.min(width, screenSize.width);
                height = Math.min(height, screenSize.height);
                setBounds((screenSize.width - width) / 2, (screenSize.height - height) / 2, width, height);
            }
            show();
        }

        public void update(Graphics g) {
            paint(g);
        }

        public void paint(Graphics g) {
            if (doubleBuffer == null) {
                Dimension d = getSize();
                doubleBuffer = createImage(d.width, d.height);
            }
            Graphics dg = doubleBuffer.getGraphics();
            dg.setClip(g.getClipBounds());
            super.paint(dg);
            dg.dispose();
            g.drawImage(doubleBuffer, 0, 0, this);
        }

        public void doLayout() {
            if (doubleBuffer != null) {
                doubleBuffer.flush();
                doubleBuffer = null;
            }
            super.doLayout();
        }
    }
}

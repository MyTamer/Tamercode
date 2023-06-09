package javax.swing.plaf.basic;

import java.beans.PropertyVetoException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.SwingTestCase;

public class BasicDesktopPaneUIActionsTest extends SwingTestCase {

    private JFrame frame;

    private JDesktopPane desktop;

    private JInternalFrame iframe1;

    private JInternalFrame iframe2;

    private BasicDesktopPaneUI ui;

    public BasicDesktopPaneUIActionsTest(final String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        frame = new JFrame();
        desktop = new JDesktopPane();
        ui = new BasicDesktopPaneUI();
        desktop.setUI(ui);
        iframe1 = new JInternalFrame("", true, true, true, true);
        desktop.add(iframe1);
        iframe2 = new JInternalFrame("", true, true, true, true);
        desktop.add(iframe2);
        frame.getContentPane().add(desktop);
        desktop.setSelectedFrame(iframe1);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        frame.dispose();
    }

    public void testNavigateAction() {
        if (!isHarmony()) {
            return;
        }
        frame.setVisible(true);
        class MyButton extends JButton {

            private static final long serialVersionUID = 1L;

            public boolean requestedFocus;

            @Override
            public void requestFocus() {
                requestedFocus = true;
                super.requestFocus();
            }
        }
        MyButton b1 = new MyButton();
        MyButton b2 = new MyButton();
        frame.getContentPane().add(b1, 0);
        frame.getContentPane().add(b2, 2);
        AbstractAction action = ui.new NavigateAction();
        action.putValue(Action.NAME, "navigateNext");
        b1.requestedFocus = false;
        action.actionPerformed(null);
        assertTrue(b1.requestedFocus);
        action.putValue(Action.NAME, "navigatePrevious");
        b2.requestedFocus = false;
        action.actionPerformed(null);
        assertTrue(b2.requestedFocus);
    }

    public void testCloseAction() {
        AbstractAction action = ui.new CloseAction();
        action.actionPerformed(null);
        assertTrue(desktop.getSelectedFrame().isClosed());
    }

    public void testMaximizeAction() {
        AbstractAction action = ui.new MaximizeAction();
        action.actionPerformed(null);
        assertTrue(desktop.getSelectedFrame().isMaximum());
    }

    public void testMinimizeAction() {
        AbstractAction action = ui.new MinimizeAction();
        action.actionPerformed(null);
        assertTrue(desktop.getSelectedFrame().isIcon());
    }

    public void testOpenAction() throws PropertyVetoException {
        AbstractAction action = ui.new OpenAction();
        desktop.getSelectedFrame().setMaximum(true);
        action.actionPerformed(null);
        assertFalse(desktop.getSelectedFrame().isMaximum());
        desktop.getSelectedFrame().setIcon(true);
        action.actionPerformed(null);
        assertFalse(desktop.getSelectedFrame().isIcon());
    }
}

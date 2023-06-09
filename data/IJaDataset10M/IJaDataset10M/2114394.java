package view;

import com.jidesoft.spring.richclient.docking.view.JideAbstractView;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 * <p/>
 * Date: 06.10.2006<br>
 * Time: 13:48:43<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class OtherView extends JideAbstractView {

    protected JComponent createControl() {
        JLabel lblMessage = getComponentFactory().createLabel("otherView.message");
        lblMessage.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JPanel panel = getComponentFactory().createPanel(new BorderLayout());
        panel.add(lblMessage);
        return panel;
    }
}

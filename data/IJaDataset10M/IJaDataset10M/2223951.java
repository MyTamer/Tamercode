package com.jgoodies.looks.plastic;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

/**
 * The JGoodies Plastic look and feel implemenation of <code>MenuBarUI</code>.
 * Can handle optional <code>Border</code> types as specified by the
 * <code>BorderStyle</code> or <code>HeaderStyle</code> client properties.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 1.5 $
 */
public final class PlasticMenuBarUI extends BasicMenuBarUI {

    private PropertyChangeListener listener;

    public static ComponentUI createUI(JComponent b) {
        return new PlasticMenuBarUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        installSpecialBorder();
    }

    protected void installListeners() {
        super.installListeners();
        listener = createBorderStyleListener();
        menuBar.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners() {
        menuBar.removePropertyChangeListener(listener);
        super.uninstallListeners();
    }

    private PropertyChangeListener createBorderStyleListener() {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (prop.equals(Options.HEADER_STYLE_KEY) || prop.equals(PlasticLookAndFeel.BORDER_STYLE_KEY)) {
                    PlasticMenuBarUI.this.installSpecialBorder();
                }
            }
        };
    }

    /**
	 * Installs a special border, if either a look-dependent <code>BorderStyle</code>
	 * or a look-independent <code>HeaderStyle</code> has been specified.
	 * A look specific <code>BorderStyle</code> shadows a <code>HeaderStyle</code>.<p>
	 *
	 * We recommend to specify a <code>HeaderStyle</code>.
	 */
    public void installSpecialBorder() {
        String suffix;
        BorderStyle borderStyle = BorderStyle.from(menuBar, PlasticLookAndFeel.BORDER_STYLE_KEY);
        if (borderStyle == BorderStyle.EMPTY) suffix = "emptyBorder"; else if (borderStyle == BorderStyle.ETCHED) suffix = "etchedBorder"; else if (borderStyle == BorderStyle.SEPARATOR) suffix = "separatorBorder"; else {
            HeaderStyle headerStyle = HeaderStyle.from(menuBar);
            if (headerStyle == HeaderStyle.BOTH) suffix = "headerBorder"; else if (headerStyle == HeaderStyle.SINGLE && is3D()) suffix = "etchedBorder"; else return;
        }
        LookAndFeel.installBorder(menuBar, "MenuBar." + suffix);
    }

    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
            if (is3D()) {
                Rectangle bounds = new Rectangle(0, 0, c.getWidth(), c.getHeight());
                PlasticUtils.addLight3DEffekt(g, bounds, true);
            }
        }
        paint(g, c);
    }

    /**
	 * Checks and answers if we should add a pseudo 3D effect.
	 */
    private boolean is3D() {
        if (PlasticUtils.force3D(menuBar)) return true;
        if (PlasticUtils.forceFlat(menuBar)) return false;
        return PlasticUtils.is3D("MenuBar.") && (HeaderStyle.from(menuBar) != null) && (BorderStyle.from(menuBar, PlasticLookAndFeel.BORDER_STYLE_KEY) != BorderStyle.EMPTY);
    }
}

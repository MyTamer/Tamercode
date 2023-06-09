package com.birosoft.liquid;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import com.birosoft.liquid.skin.Skin;
import com.birosoft.liquid.skin.SkinSimpleButtonIndexModel;

public class LiquidComboBoxButton extends JButton {

    protected JComboBox comboBox;

    protected JList listBox;

    protected CellRendererPane rendererPane;

    protected Icon comboIcon;

    protected boolean iconOnly = false;

    BufferedImage focusImg;

    public final JComboBox getComboBox() {
        return comboBox;
    }

    public final void setComboBox(JComboBox cb) {
        comboBox = cb;
    }

    public final Icon getComboIcon() {
        return comboIcon;
    }

    public final void setComboIcon(Icon i) {
        comboIcon = i;
    }

    public final boolean isIconOnly() {
        return iconOnly;
    }

    public final void setIconOnly(boolean isIconOnly) {
        iconOnly = isIconOnly;
    }

    LiquidComboBoxButton() {
        super("");
        DefaultButtonModel model = new DefaultButtonModel() {

            public void setArmed(boolean armed) {
                super.setArmed(isPressed() ? true : armed);
            }
        };
        setModel(model);
        setBackground(UIManager.getColor("ComboBox.background"));
        setForeground(UIManager.getColor("ComboBox.foreground"));
        ImageIcon icon = LiquidLookAndFeel.loadIcon("comboboxfocus.png", this);
        focusImg = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        Graphics g3 = focusImg.getGraphics();
        icon.paintIcon(this, g3, 0, 0);
    }

    public LiquidComboBoxButton(JComboBox cb, Icon i, CellRendererPane pane, JList list) {
        this();
        comboBox = cb;
        comboIcon = i;
        rendererPane = pane;
        listBox = list;
        setEnabled(comboBox.isEnabled());
    }

    public LiquidComboBoxButton(JComboBox cb, Icon i, boolean onlyIcon, CellRendererPane pane, JList list) {
        this(cb, i, pane, list);
        iconOnly = onlyIcon;
    }

    Skin skinCombo;

    SkinSimpleButtonIndexModel indexModel = new SkinSimpleButtonIndexModel();

    Skin skinArrow;

    Skin skinButton;

    /**
     * Mostly taken from the swing sources
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        boolean leftToRight = getComponentOrientation().isLeftToRight();
        int index = indexModel.getIndexForState(model.isEnabled(), model.isRollover(), model.isArmed() && model.isPressed() | model.isSelected());
        if (iconOnly) {
            ButtonModel model = getModel();
            boolean selected = model.isArmed() && model.isPressed() | model.isSelected();
            getSkinCombo().draw(g, index, getWidth(), getHeight());
            int amnt = getWidth() - getSkinArrow().getHsize();
            int middle = (getHeight() - getSkinArrow().getVsize()) / 2;
            getSkinArrow().draw(g, index, getWidth() - getSkinArrow().getHsize() - 6, middle, getSkinArrow().getHsize(), getSkinArrow().getVsize());
            comboBox.getEditor().getEditorComponent().repaint();
        } else {
            ButtonModel model = getModel();
            boolean selected = model.isArmed() && model.isPressed() | model.isSelected();
            getSkinCombo().draw(g, index, getWidth(), getHeight());
            int middle = (getHeight() - getSkinArrow().getVsize()) / 2;
            getSkinArrow().draw(g, index, getWidth() - getSkinArrow().getHsize() - 6, middle, getSkinArrow().getHsize(), getSkinArrow().getVsize());
        }
        Insets insets = new Insets(0, 12, 2, 2);
        int width = getWidth() - (insets.left + insets.right);
        int widthFocus = width;
        int height = getHeight() - (insets.top + insets.bottom);
        if (height <= 0 || width <= 0) {
            return;
        }
        int left = insets.left;
        int top = insets.top;
        int right = left + (width - 1);
        int bottom = top + (height - 1);
        int iconWidth = LiquidComboBoxUI.comboBoxButtonSize;
        int iconLeft = (leftToRight) ? right : left;
        Component c = null;
        boolean mustResetOpaque = false;
        boolean savedOpaque = false;
        boolean paintFocus = false;
        if (!iconOnly && comboBox != null) {
            ListCellRenderer renderer = comboBox.getRenderer();
            boolean renderPressed = getModel().isPressed();
            c = renderer.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, renderPressed, false);
            c.setFont(rendererPane.getFont());
            if (model.isArmed() && model.isPressed()) {
                if (isOpaque()) {
                    c.setBackground(UIManager.getColor("Button.select"));
                }
                c.setForeground(comboBox.getForeground());
            } else if (!comboBox.isEnabled()) {
                if (isOpaque()) {
                    c.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
                }
                c.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            } else {
                c.setForeground(comboBox.getForeground());
                c.setBackground(comboBox.getBackground());
            }
            if (!mustResetOpaque && c instanceof JComponent) {
                mustResetOpaque = true;
                JComponent jc = (JComponent) c;
                savedOpaque = jc.isOpaque();
                jc.setOpaque(false);
            }
            int cWidth = width - (insets.right + iconWidth);
            boolean shouldValidate = false;
            if (c instanceof JPanel) {
                shouldValidate = true;
            }
            if (leftToRight) {
                rendererPane.paintComponent(g, c, this, left, top, cWidth, height, shouldValidate);
            } else {
                rendererPane.paintComponent(g, c, this, left + iconWidth, top, cWidth, height, shouldValidate);
            }
            if (paintFocus) {
                g.setColor(Color.black);
                Graphics2D g2d = (Graphics2D) g;
                Rectangle r = new Rectangle(left, top, 2, 2);
                TexturePaint tp = new TexturePaint(focusImg, r);
                g2d.setPaint(tp);
                g2d.draw(new Rectangle(left, top, cWidth, height));
            }
        }
        if (mustResetOpaque) {
            JComponent jc = (JComponent) c;
            jc.setOpaque(savedOpaque);
        }
    }

    public Skin getSkinCombo() {
        if (skinCombo == null) {
            skinCombo = new Skin("combobox.png", 4, 10, 6, 18, 4);
        }
        return skinCombo;
    }

    public Skin getSkinArrow() {
        if (skinArrow == null) {
            skinArrow = new Skin("comboboxarrow.png", 4, 0);
        }
        return skinArrow;
    }
}

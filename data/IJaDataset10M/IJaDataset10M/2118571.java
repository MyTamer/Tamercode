package com.sachin.fileview.imageview;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreview extends JComponent implements PropertyChangeListener {

    private static final long serialVersionUID = -933932560862129762L;

    ImageIcon thumbnail = null;

    File file = null;

    public ImagePreview(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 50));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage() {
        if (file == null) {
            return;
        }
        ImageIcon tmpIcon = new ImageIcon(file.getPath());
        if (tmpIcon.getIconWidth() > 90) {
            thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_DEFAULT));
        } else {
            thumbnail = tmpIcon;
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            file = (File) e.getNewValue();
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (thumbnail == null) {
            loadImage();
        }
        if (thumbnail != null) {
            int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
            int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;
            if (y < 0) {
                y = 0;
            }
            if (x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}

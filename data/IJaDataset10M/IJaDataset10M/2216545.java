package jmri.jmrit.analogclock;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Date;
import jmri.*;
import jmri.jmrit.catalog.*;
import jmri.util.JmriJFrame;

/**
 * Creates a JFrame containing an analog clockface and hands.
 *
 * <p> Time code copied from code for the Nixie clock by Bob Jacobsen
 *
 * @author                     Dennis Miller Copyright (C) 2004
 * @version                    $Revision: 1.19 $
 */
public class AnalogClockFrame extends JmriJFrame implements java.beans.PropertyChangeListener {

    Timebase clock;

    double minuteAngle;

    double hourAngle;

    String amPm;

    public AnalogClockFrame() {
        super(java.util.ResourceBundle.getBundle("jmri.jmrit.JmritToolsBundle").getString("MenuItemAnalogClock"));
        clock = InstanceManager.timebaseInstance();
        clock.addPropertyChangeListener(this);
        setPreferredSize(new java.awt.Dimension(200, 200));
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel analogClockPanel = new ClockPanel();
        analogClockPanel.setOpaque(true);
        getContentPane().add(analogClockPanel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(b = new JButton("Pause"));
        if (!clock.getRun()) b.setText("Run");
        b.addActionListener(new ButtonListener());
        b.setOpaque(true);
        b.setVisible(true);
        getContentPane().add(buttonPanel);
        pack();
        update();
        clock.addMinuteChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent e) {
                update();
            }
        });
    }

    public class ClockPanel extends JPanel {

        Image logo;

        Image scaledLogo;

        NamedIcon jmriIcon;

        NamedIcon scaledIcon;

        int hourX[] = { -12, -11, -25, -10, -10, 0, 10, 10, 25, 11, 12 };

        int hourY[] = { -31, -163, -170, -211, -276, -285, -276, -211, -170, -163, -31 };

        int minuteX[] = { -12, -11, -24, -11, -11, 0, 11, 11, 24, 11, 12 };

        int minuteY[] = { -31, -261, -266, -314, -381, -391, -381, -314, -266, -261, -31 };

        int scaledHourX[] = new int[hourX.length];

        int scaledHourY[] = new int[hourY.length];

        int scaledMinuteX[] = new int[minuteX.length];

        int scaledMinuteY[] = new int[minuteY.length];

        int rotatedHourX[] = new int[hourX.length];

        int rotatedHourY[] = new int[hourY.length];

        int rotatedMinuteX[] = new int[minuteX.length];

        int rotatedMinuteY[] = new int[minuteY.length];

        Polygon hourHand;

        Polygon scaledHourHand;

        Polygon minuteHand;

        Polygon scaledMinuteHand;

        int minuteHeight;

        int hourHeight;

        double scaleRatio;

        int faceSize;

        int panelWidth;

        int panelHeight;

        int size;

        int logoWidth;

        int logoHeight;

        int centreX;

        int centreY;

        public ClockPanel() {
            jmriIcon = new NamedIcon("resources/logo.gif", "resources/logo.gif");
            scaledIcon = new NamedIcon("resources/logo.gif", "resources/logo.gif");
            logo = jmriIcon.getImage();
            hourHand = new Polygon(hourX, hourY, 11);
            hourHeight = hourHand.getBounds().getSize().height;
            minuteHand = new Polygon(minuteX, minuteY, 11);
            minuteHeight = minuteHand.getBounds().getSize().height;
            amPm = "AM";
            this.addComponentListener(new ComponentAdapter() {

                public void componentResized(ComponentEvent e) {
                    scaleFace();
                }
            });
        }

        public void paint(Graphics g) {
            g.translate(centreX, centreY);
            g.setColor(Color.white);
            g.fillOval(-faceSize / 2, -faceSize / 2, faceSize, faceSize);
            g.setColor(Color.black);
            g.drawOval(-faceSize / 2, -faceSize / 2, faceSize, faceSize);
            int dotSize = faceSize / 40;
            g.fillOval(-dotSize * 2, -dotSize * 2, 4 * dotSize, 4 * dotSize);
            g.setColor(Color.white);
            g.fillOval(-dotSize, -dotSize, 2 * dotSize, 2 * dotSize);
            g.setColor(Color.black);
            g.fillOval(-dotSize / 2, -dotSize / 2, dotSize, dotSize);
            g.drawImage(scaledLogo, -logoWidth / 2, -faceSize / 4, logoWidth, logoHeight, this);
            int dashSize = size / 60;
            for (int i = 0; i < 360; i = i + 6) {
                g.drawLine(dotX(faceSize / 2., i), dotY(faceSize / 2., i), dotX(faceSize / 2. - dashSize, i), dotY(faceSize / 2. - dashSize, i));
            }
            for (int i = 0; i < 360; i = i + 30) {
                g.drawLine(dotX(faceSize / 2., i), dotY(faceSize / 2., i), dotX(faceSize / 2. - 3 * dashSize, i), dotY(faceSize / 2. - 3 * dashSize, i));
            }
            int fontSize = faceSize / 10;
            if (fontSize < 1) fontSize = 1;
            Font sizedFont = new Font("Serif", Font.PLAIN, fontSize);
            g.setFont(sizedFont);
            FontMetrics fontM = g.getFontMetrics(sizedFont);
            for (int i = 0; i < 12; i++) {
                String hour = Integer.toString(i + 1);
                int xOffset = fontM.stringWidth(hour);
                int yOffset = fontM.getHeight();
                g.drawString(Integer.toString(i + 1), dotX(faceSize / 2 - 6 * dashSize, i * 30 - 60) - xOffset / 2, dotY(faceSize / 2 - 6 * dashSize, i * 30 - 60) + yOffset / 4);
            }
            for (int i = 0; i < scaledMinuteX.length; i++) {
                rotatedMinuteX[i] = (int) (scaledMinuteX[i] * Math.cos(toRadians(minuteAngle)) - scaledMinuteY[i] * Math.sin(toRadians(minuteAngle)));
                rotatedMinuteY[i] = (int) (scaledMinuteX[i] * Math.sin(toRadians(minuteAngle)) + scaledMinuteY[i] * Math.cos(toRadians(minuteAngle)));
            }
            scaledMinuteHand = new Polygon(rotatedMinuteX, rotatedMinuteY, rotatedMinuteX.length);
            for (int i = 0; i < scaledHourX.length; i++) {
                rotatedHourX[i] = (int) (scaledHourX[i] * Math.cos(toRadians(hourAngle)) - scaledHourY[i] * Math.sin(toRadians(hourAngle)));
                rotatedHourY[i] = (int) (scaledHourX[i] * Math.sin(toRadians(hourAngle)) + scaledHourY[i] * Math.cos(toRadians(hourAngle)));
            }
            scaledHourHand = new Polygon(rotatedHourX, rotatedHourY, rotatedHourX.length);
            g.fillPolygon(scaledHourHand);
            g.fillPolygon(scaledMinuteHand);
            int amPmFontSize = (int) (fontSize * .75);
            if (amPmFontSize < 1) amPmFontSize = 1;
            Font amPmSizedFont = new Font("Serif", Font.PLAIN, amPmFontSize);
            g.setFont(amPmSizedFont);
            FontMetrics amPmFontM = g.getFontMetrics(amPmSizedFont);
            g.drawString(amPm, -amPmFontM.stringWidth(amPm) / 2, faceSize / 5);
        }

        double toRadians(double degrees) {
            return degrees / 180.0 * Math.PI;
        }

        int dotX(double radius, double angle) {
            int xDist;
            xDist = (int) Math.round(radius * Math.cos(toRadians(angle)));
            return xDist;
        }

        int dotY(double radius, double angle) {
            int yDist;
            yDist = (int) Math.round(radius * Math.sin(toRadians(angle)));
            return yDist;
        }

        public void scaleFace() {
            int panelHeight = this.getSize().height;
            int panelWidth = this.getSize().width;
            size = Math.min(panelHeight, panelWidth);
            faceSize = (int) (size * .97);
            if (faceSize == 0) {
                faceSize = 1;
            }
            int logoScaleWidth = faceSize / 6;
            int logoScaleHeight = (int) ((float) logoScaleWidth * (float) jmriIcon.getIconHeight() / jmriIcon.getIconWidth());
            scaledLogo = logo.getScaledInstance(logoScaleWidth, logoScaleHeight, Image.SCALE_SMOOTH);
            scaledIcon.setImage(scaledLogo);
            logoWidth = scaledIcon.getIconWidth();
            logoHeight = scaledIcon.getIconHeight();
            scaleRatio = faceSize / 2.7 / minuteHeight;
            for (int i = 0; i < minuteX.length; i++) {
                scaledMinuteX[i] = (int) (minuteX[i] * scaleRatio);
                scaledMinuteY[i] = (int) (minuteY[i] * scaleRatio);
                scaledHourX[i] = (int) (hourX[i] * scaleRatio);
                scaledHourY[i] = (int) (hourY[i] * scaleRatio);
            }
            scaledHourHand = new Polygon(scaledHourX, scaledHourY, scaledHourX.length);
            scaledMinuteHand = new Polygon(scaledMinuteX, scaledMinuteY, scaledMinuteX.length);
            centreX = panelWidth / 2;
            centreY = panelHeight / 2;
            return;
        }
    }

    @SuppressWarnings("deprecation")
    void update() {
        Date now = clock.getTime();
        int hours = now.getHours();
        int minutes = now.getMinutes();
        minuteAngle = minutes * 6.;
        hourAngle = hours * 30. + 30. * minuteAngle / 360.;
        if (hours < 12) {
            amPm = "AM ";
        } else {
            amPm = "PM ";
        }
        if (hours == 12 && minutes == 0) {
            amPm = "Noon";
        }
        if (hours == 0 && minutes == 0) {
            amPm = "Midnight";
        }
        amPm = amPm + " " + (int) clock.userGetRate() + ":1";
        repaint();
    }

    public void dispose() {
        super.dispose();
    }

    /**
     * Handle a change to clock properties
     */
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        boolean now = clock.getRun();
        if (now) b.setText("Pause"); else b.setText("Run");
    }

    JButton b;

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent a) {
            boolean next = !clock.getRun();
            clock.setRun(next);
            if (next) b.setText("Pause"); else b.setText("Run ");
        }
    }
}

package com.bluebrim.layoutmanager.test;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layoutmanager.*;

/**
 * This class handles events that are generated by buttons and responds to
 * them accordingly
 */
class ButtonActionListener implements ActionListener {

    AppController frame;

    ButtonActionListener(AppController frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add")) {
            try {
                double width = Double.parseDouble(frame.collWidth.getText());
                double height = Double.parseDouble(frame.collHeight.getText());
                if ((width > 0) && (height > 0)) {
                    String s = "W=" + width + ", H=" + height;
                    frame.list.add(s);
                    frame.collWidth.setText("");
                    frame.collHeight.setText("");
                } else {
                    MessageBox msgbox = new MessageBox(new Frame(), "Invalid Dimensions for Collection rectangle.", false);
                }
            } catch (NumberFormatException exp) {
                MessageBox msgbox = new MessageBox(new Frame(), "Invalid dimensions for Collection rectangle.", false);
            }
        } else if (e.getActionCommand().equals("Remove")) {
            int index = frame.list.getSelectedIndex();
            if (index > -1) {
                frame.list.remove(index);
            }
        } else if (e.getActionCommand().equals("Place")) {
            try {
                double trgwidth = Double.parseDouble(frame.targetWidth.getText());
                double trgheight = Double.parseDouble(frame.targetHeight.getText());
                double maxpercent = Double.valueOf(frame.maxpercent.getText()).doubleValue() / 100;
                double leftGridWidth = Double.parseDouble(frame.leftGridWidth.getText());
                double rightGridWidth = Double.parseDouble(frame.rightGridWidth.getText());
                double horzGridWidth = Double.parseDouble(frame.horizGridWidth.getText());
                if ((trgwidth > 0) && (trgheight > 0) && (maxpercent > 0) && (maxpercent <= 1.0) && (frame.list.getItemCount() > 0) && (leftGridWidth > 0) && (rightGridWidth > 0) && (horzGridWidth > 0) && (leftGridWidth <= trgwidth) && (rightGridWidth <= trgwidth) && (horzGridWidth <= trgheight)) {
                    frame.target = new RectIMLayoutable(trgwidth, trgheight);
                    frame.collection = new RectangularCollection();
                    for (int i = 0; i < frame.list.getItemCount(); i++) {
                        String s = frame.list.getItem(i);
                        String temp = s.substring(s.indexOf("=") + 1, s.indexOf(","));
                        double width = Double.parseDouble(temp);
                        temp = s.substring(s.lastIndexOf("=") + 1, s.length());
                        double height = Double.parseDouble(temp);
                        CoLayoutableIF rect = new RectIMLayoutable(width, height);
                        frame.collection.addElement(rect);
                    }
                    frame.constraint = new MrConstraint(maxpercent, horzGridWidth, horzGridWidth, leftGridWidth, rightGridWidth);
                    GridArrangement arr = new GridArrangement();
                    SolutionConfig config = (SolutionConfig) arr.place(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_RIGHT);
                    System.out.println("Arranging the rectangles along BOTTOM RIGHT corner");
                    System.out.println("Target Rectangle dimensions: ");
                    frame.target.print();
                    System.out.println();
                    System.out.println("Constraint:");
                    System.out.println("Horz:" + frame.constraint.getHorizontalBottomWidth() + " " + "Vert Left:" + frame.constraint.getVerticalLeftWidth() + " " + "Vert Right:" + frame.constraint.getVerticalRightWidth());
                    System.out.println();
                    System.out.println("Dimension of selected Collection Rectangles: ");
                    MrCollection coll = config.getContainer().getCollection();
                    for (Enumeration e1 = coll.enumerate(); e1.hasMoreElements(); ) {
                        ((RectIMLayoutable) e1.nextElement()).print();
                    }
                    System.out.println();
                    System.out.println("The % area covered is: " + ((double) config.getOccupiedArea() * 100) / frame.target.getArea());
                    System.out.println();
                    System.out.println("The total distance is: " + config.getDistance());
                    System.out.println();
                    System.out.println("The locations for the selection rectangles:");
                    Vector locations = config.getLocations();
                    for (int i = 0; i < locations.size(); i++) System.out.println("Point : x = " + ((Point2D) locations.elementAt(i)).getX() + " y = " + ((Point2D) locations.elementAt(i)).getY());
                    frame.mutex = true;
                    frame.arranger = new Arranger(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_LEFT);
                    frame.arrangerThread = new Thread(frame.arranger);
                    frame.arrangerThread.start();
                    ViewManager viewManager = new ViewManager(frame, config);
                    frame.setVisible(false);
                    try {
                        frame.arrangerThread.join();
                    } catch (InterruptedException exp) {
                    }
                    frame.mutex = false;
                    frame.arrangerThread = null;
                    System.out.println();
                    System.out.println("Arranging the rectangles along BOTTOM LEFT corner");
                    config = (SolutionConfig) arr.place(frame.target, frame.collection, frame.constraint, GridArrangement.BOTTOM_LEFT);
                    System.out.println();
                    System.out.println("Target Rectangle dimensions: ");
                    frame.target.print();
                    System.out.println();
                    System.out.println("Constraint:");
                    System.out.println("Horz:" + frame.constraint.getHorizontalBottomWidth() + " " + "Vert Left:" + frame.constraint.getVerticalLeftWidth() + " " + "Vert Right:" + frame.constraint.getVerticalRightWidth());
                    System.out.println();
                    System.out.println("Dimension of selected Collection Rectangles: ");
                    coll = config.getContainer().getCollection();
                    for (Enumeration e1 = coll.enumerate(); e1.hasMoreElements(); ) {
                        ((RectIMLayoutable) e1.nextElement()).print();
                    }
                    System.out.println();
                    System.out.println("The % area covered is: " + ((double) config.getOccupiedArea() * 100) / frame.target.getArea());
                    System.out.println();
                    System.out.println("The total distance is: " + config.getDistance());
                    System.out.println();
                    System.out.println("The locations for the selection rectangles:");
                    locations = config.getLocations();
                    for (int i = 0; i < locations.size(); i++) System.out.println("Point : x = " + ((Point2D) locations.elementAt(i)).getX() + " y = " + ((Point2D) locations.elementAt(i)).getY());
                } else {
                    if (maxpercent > 1.0 || maxpercent < 0) {
                        MessageBox msgbox = new MessageBox(new Frame(), "Max percent should" + "be less than or equal to 100", false);
                    } else if (frame.list.getItemCount() == 0) {
                        MessageBox msgbox = new MessageBox(new Frame(), "No elements in the collection", false);
                    } else if ((trgwidth <= 0) || (trgheight <= 0)) {
                        MessageBox msgbox = new MessageBox(new Frame(), "Invalid Dimensions for Target Rectangle.", false);
                    } else if ((leftGridWidth <= 0) || (rightGridWidth <= 0) || (horzGridWidth <= 0)) {
                        MessageBox msgbox = new MessageBox(new Frame(), "Dimensions for grids should be positive", false);
                    } else if ((leftGridWidth > trgwidth) || (rightGridWidth > trgwidth) || (horzGridWidth > trgheight)) {
                        MessageBox msgbox = new MessageBox(new Frame(), "Dimensions for grids cannot exceed target rectangle dimensions", false);
                    } else {
                        MessageBox msgbox = new MessageBox(new Frame(), "Invalid Input Specification.", false);
                    }
                }
            } catch (NumberFormatException exp) {
                MessageBox msgbox = new MessageBox(new Frame(), "Invalid dimensions for Target or Constraints.", false);
            }
        }
    }
}

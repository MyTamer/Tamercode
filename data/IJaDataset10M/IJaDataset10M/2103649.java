package edu.mit.wi.haploview;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class GBrowseDialog extends JDialog implements ActionListener, Constants {

    JComboBox cbox, buildBox;

    JTextField minField, maxField;

    HaploView hv;

    public GBrowseDialog(HaploView h, String title) {
        super(h, title);
        hv = h;
        JPanel contents = new JPanel();
        contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
        JPanel chromPanel = new JPanel();
        chromPanel.add(new JLabel("Chromosome"));
        String[] c = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X", "Y" };
        cbox = new JComboBox(c);
        if (hv.currentGroup.theData.getDataChrom() != null) {
            String which = hv.currentGroup.theData.getDataChrom().substring(3);
            for (int i = 0; i < c.length; i++) {
                if (which.equals(c[i])) {
                    cbox.setSelectedIndex(i);
                }
            }
        }
        chromPanel.add(cbox);
        contents.add(chromPanel);
        JPanel boundsPanel = new JPanel();
        boundsPanel.add(new JLabel("from"));
        minField = new JTextField(Long.toString(hv.currentGroup.theData.getMarker(0).getPosition()), 9);
        boundsPanel.add(minField);
        boundsPanel.add(new JLabel("  to"));
        maxField = new JTextField(Long.toString(hv.currentGroup.theData.getMarker(hv.currentGroup.theData.getMarkersSize() - 1).getPosition()), 9);
        boundsPanel.add(maxField);
        contents.add(boundsPanel);
        JPanel buildPanel = new JPanel();
        buildPanel.add(new JLabel("Genome build: "));
        String[] b = { "34", "35", "36" };
        buildBox = new JComboBox(b);
        buildBox.setSelectedIndex(2);
        buildPanel.add(buildBox);
        contents.add(buildPanel);
        JPanel buttonPanel = new JPanel();
        JButton cancelBut = new JButton("Cancel");
        cancelBut.addActionListener(this);
        JButton okBut = new JButton("OK");
        okBut.addActionListener(this);
        buttonPanel.add(okBut);
        buttonPanel.add(cancelBut);
        contents.add(buttonPanel);
        this.getRootPane().setDefaultButton(okBut);
        contents.add(new JLabel("(Note: this option requires an internet connection)"));
        this.setContentPane(contents);
        this.setLocation(this.getParent().getX() + 100, this.getParent().getY() + 100);
        this.setModal(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("OK")) {
            try {
                long minpos = Long.parseLong(minField.getText());
                long maxpos = Long.parseLong(maxField.getText());
                if (maxpos <= minpos) {
                    throw new HaploViewException("Boundary positions out of order.");
                }
                hv.currentGroup.theData.setDataChrom("chr" + cbox.getSelectedItem());
                hv.currentGroup.dPrimeDisplay.setgBrowseLeft(minpos);
                hv.currentGroup.dPrimeDisplay.setgBrowseRight(maxpos);
                hv.currentGroup.theData.setDataBuild("ncbi_b" + buildBox.getSelectedItem());
                hv.currentGroup.dPrimeDisplay.setShowGBrowse(true);
                hv.gbEditItem.setEnabled(true);
                this.dispose();
                hv.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                javax.swing.SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        hv.currentGroup.dPrimeDisplay.computePreferredSize();
                        if (hv.currentGroup.dPrimeDisplay != null && hv.currentGroup.tabs.getSelectedIndex() == VIEW_D_NUM) {
                            hv.currentGroup.dPrimeDisplay.repaint();
                        }
                        hv.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                });
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Boundary positions formatted incorrectly", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (HaploViewException hve) {
                JOptionPane.showMessageDialog(this, hve.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (command.equals("Cancel")) {
            this.dispose();
        }
    }
}

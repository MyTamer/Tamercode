package org.exmaralda.partitureditor.helpers;

import org.exmaralda.partitureditor.jexmaralda.JexmaraldaException;
import org.exmaralda.partitureditor.jexmaralda.BasicTranscription;
import java.io.*;
import java.util.*;
import org.exmaralda.partitureditor.jexmaralda.*;
import org.jdom.JDOMException;
import org.xml.sax.SAXException;

/**
 *
 * @author  thomas
 */
public class TranscriptionSelectionPanel extends javax.swing.JPanel {

    javax.swing.DefaultListModel listModel;

    String lastDirectory = System.getProperty("user.dir");

    /** Creates new form TranscriptionSelectionPanel */
    public TranscriptionSelectionPanel() {
        initComponents();
        listModel = new javax.swing.DefaultListModel();
        transcriptionList.setModel(listModel);
    }

    public Vector<File> getFiles() {
        Vector<File> returnValue = new Vector<File>();
        for (Object o : listModel.toArray()) {
            returnValue.add((File) o);
        }
        return returnValue;
    }

    private void initComponents() {
        transcriptionListScrollPane = new javax.swing.JScrollPane();
        transcriptionList = new javax.swing.JList();
        buttonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        loadComaButton = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        setPreferredSize(new java.awt.Dimension(400, 400));
        transcriptionListScrollPane.setViewportView(transcriptionList);
        add(transcriptionListScrollPane, java.awt.BorderLayout.CENTER);
        buttonPanel.setLayout(new javax.swing.BoxLayout(buttonPanel, javax.swing.BoxLayout.Y_AXIS));
        addButton.setText("Add...");
        addButton.setMaximumSize(new java.awt.Dimension(90, 30));
        addButton.setMinimumSize(new java.awt.Dimension(90, 30));
        addButton.setPreferredSize(new java.awt.Dimension(90, 30));
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(addButton);
        removeButton.setText("Remove");
        removeButton.setMaximumSize(new java.awt.Dimension(90, 30));
        removeButton.setMinimumSize(new java.awt.Dimension(90, 30));
        removeButton.setPreferredSize(new java.awt.Dimension(90, 30));
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(removeButton);
        loadComaButton.setText("CoMa...");
        loadComaButton.setMaximumSize(new java.awt.Dimension(90, 30));
        loadComaButton.setMinimumSize(new java.awt.Dimension(90, 30));
        loadComaButton.setPreferredSize(new java.awt.Dimension(90, 30));
        loadComaButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadComaButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(loadComaButton);
        add(buttonPanel, java.awt.BorderLayout.EAST);
    }

    private void loadComaButtonActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        int rv = fc.showOpenDialog(this);
        if (rv != fc.APPROVE_OPTION) return;
        org.exmaralda.common.corpusbuild.AbstractBasicTranscriptionProcessor tp = new org.exmaralda.common.corpusbuild.AbstractBasicTranscriptionProcessor(fc.getSelectedFile().getAbsolutePath()) {

            public void processTranscription(BasicTranscription bt) {
                File f = new File(currentFilename);
                if (!(listModel.contains(f))) listModel.addElement(f);
            }
        };
        try {
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
            tp.doIt();
            setCursor(java.awt.Cursor.getDefaultCursor());
        } catch (SAXException ex) {
            ex.printStackTrace();
            displayError(ex);
        } catch (JexmaraldaException ex) {
            ex.printStackTrace();
            displayError(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            displayError(ex);
        } catch (JDOMException ex) {
            ex.printStackTrace();
            displayError(ex);
        }
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        for (Object o : transcriptionList.getSelectedValues()) {
            listModel.removeElement(o);
        }
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        org.exmaralda.partitureditor.jexmaraldaswing.fileDialogs.OpenBasicTranscriptionDialog dialog = new org.exmaralda.partitureditor.jexmaraldaswing.fileDialogs.OpenBasicTranscriptionDialog(lastDirectory);
        dialog.setMultiSelectionEnabled(true);
        int val = dialog.showOpenDialog(this);
        if (val != dialog.APPROVE_OPTION) return;
        for (File f : dialog.getSelectedFiles()) {
            if (!(listModel.contains(f))) listModel.addElement(f);
            lastDirectory = f.getAbsolutePath();
        }
    }

    void displayError(Exception e) {
        javax.swing.JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        setCursor(java.awt.Cursor.getDefaultCursor());
    }

    private javax.swing.JButton addButton;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton loadComaButton;

    private javax.swing.JButton removeButton;

    private javax.swing.JList transcriptionList;

    private javax.swing.JScrollPane transcriptionListScrollPane;
}

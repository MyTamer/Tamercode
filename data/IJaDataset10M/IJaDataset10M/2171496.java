package fca.gui.context.assistant;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import fca.core.context.binary.BinaryContext;
import fca.exception.ReaderException;
import fca.gui.context.ContextViewer;
import fca.gui.context.table.ContextTableScrollPane;
import fca.gui.context.table.NestedContextTable;
import fca.gui.util.ColorSet;
import fca.gui.util.DialogBox;
import fca.gui.util.ExampleFileFilter;
import fca.gui.util.constant.LMPreferences;
import fca.io.context.reader.txt.LMBinaryContextReader;
import fca.messages.GUIMessages;

/**
 * Assistant de creation d'un niveau de contexte imbrique
 * @author Genevi�ve Roberge
 * @version 1.0
 */
public class LevelAdditionAssistant extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = -245483351486373234L;

    private ContextViewer viewer;

    private BinaryContext context;

    private JFrame thisFrame;

    private JPanel thisPanel;

    private int level;

    private JButton fileChooserBtn;

    private JScrollPane listScrollPane;

    private JList contextList;

    private JLabel levelContextLabel;

    private Vector<BinaryContext> contexts;

    private Vector<String> contextNames;

    private JRadioButton loadedCtxBtn;

    private JRadioButton ctxFileBtn;

    public LevelAdditionAssistant(int lev, ContextViewer cv, Vector<BinaryContext> loadedContexts) {
        setTitle(GUIMessages.getString("GUI.levelAdditionAssistant"));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        thisFrame = this;
        thisPanel = new JPanel();
        thisPanel.setBorder(BorderFactory.createEtchedBorder());
        thisPanel.setLayout(new GridBagLayout());
        level = lev;
        viewer = cv;
        contexts = loadedContexts;
        contextNames = new Vector<String>();
        for (int i = 0; i < contexts.size(); i++) contextNames.add((contexts.elementAt(i)).getName());
        PanelListener panelListener = new PanelListener();
        PanelMouseListener mouseListener = new PanelMouseListener();
        JPanel labelPanel = new JPanel();
        labelPanel.setPreferredSize(new Dimension(400, 30));
        labelPanel.setBorder(BorderFactory.createEtchedBorder());
        Color panelColor = ColorSet.getColorAt(level - 1);
        labelPanel.setBackground(panelColor);
        JLabel levelLabel = new JLabel(GUIMessages.getString("GUI.contextInLevel") + " " + level);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(2, 2, 2, 2);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        labelPanel.add(levelLabel, gc);
        gc.insets = new Insets(20, 20, 20, 20);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 4;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        thisPanel.add(labelPanel, gc);
        loadedCtxBtn = new JRadioButton(GUIMessages.getString("GUI.loadedContext"), true);
        loadedCtxBtn.addActionListener(panelListener);
        ctxFileBtn = new JRadioButton(GUIMessages.getString("GUI.contextFile"), false);
        ctxFileBtn.addActionListener(panelListener);
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(loadedCtxBtn);
        radioGroup.add(ctxFileBtn);
        gc.insets = new Insets(5, 20, 2, 2);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        thisPanel.add(ctxFileBtn, gc);
        fileChooserBtn = new JButton(GUIMessages.getString("GUI.chooseFile"));
        fileChooserBtn.addActionListener(panelListener);
        fileChooserBtn.setEnabled(false);
        gc.insets = new Insets(2, 20, 5, 20);
        gc.gridx = 2;
        gc.gridy = 1;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.gridwidth = 2;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        thisPanel.add(fileChooserBtn, gc);
        gc.insets = new Insets(2, 20, 5, 2);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 4;
        gc.weightx = 0.0;
        gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        thisPanel.add(loadedCtxBtn, gc);
        if (contextNames.isEmpty()) {
            loadedCtxBtn.setSelected(false);
            ctxFileBtn.setSelected(true);
            fileChooserBtn.setEnabled(true);
        } else {
            loadedCtxBtn.setSelected(true);
            ctxFileBtn.setSelected(false);
            fileChooserBtn.setEnabled(false);
        }
        contextList = new JList(contextNames);
        contextList.setEnabled(true);
        contextList.addMouseListener(mouseListener);
        listScrollPane = new JScrollPane(contextList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listScrollPane.setPreferredSize(new Dimension(400, 60));
        gc.insets = new Insets(5, 20, 5, 20);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.gridwidth = 4;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.HORIZONTAL;
        thisPanel.add(listScrollPane, gc);
        levelContextLabel = new JLabel(GUIMessages.getString("GUI.context") + " : ( " + GUIMessages.getString("GUI.none") + " )");
        gc.insets = new Insets(10, 20, 10, 2);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.weightx = 0.0;
        gc.weighty = 1.0;
        gc.gridwidth = 4;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        thisPanel.add(levelContextLabel, gc);
        JButton finishBtn = new JButton(GUIMessages.getString("GUI.finish"));
        finishBtn.addActionListener(new EndListener());
        gc.insets = new Insets(5, 5, 20, 20);
        gc.gridx = 3;
        gc.gridy = 5;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.SOUTHEAST;
        gc.fill = GridBagConstraints.NONE;
        thisPanel.add(finishBtn, gc);
        getContentPane().add(thisPanel);
        pack();
        setVisible(true);
    }

    /**
	 * @return le contexte asoci�
	 */
    public BinaryContext getContext() {
        return context;
    }

    /**
	 * Genere une fenetre pour pouvoir ouvrir un contexte existant en fichier et verifie les types
	 * et genere le contexte en fonction du type de fichier via les readers
	 */
    private void openContext() {
        JFileChooser fileChooser = new JFileChooser(LMPreferences.getLastDirectory());
        fileChooser.setApproveButtonText(GUIMessages.getString("GUI.open"));
        fileChooser.setDialogTitle(GUIMessages.getString("GUI.openAContext"));
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ExampleFileFilter filterBinary = new ExampleFileFilter("lmb", GUIMessages.getString("GUI.latticeMinerBinaryContext"));
        fileChooser.addChoosableFileFilter(filterBinary);
        int returnVal = fileChooser.showOpenDialog(this);
        LMPreferences.setLastDirectory(fileChooser.getCurrentDirectory().getAbsolutePath());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File contextFile = fileChooser.getSelectedFile();
            if (!contextFile.exists()) {
                DialogBox.showMessageError(this, GUIMessages.getString("GUI.fileDoesntExist"), GUIMessages.getString("GUI.errorWithFile"));
                return;
            }
            try {
                LMBinaryContextReader contextReader = new LMBinaryContextReader(contextFile);
                BinaryContext binCtx = (BinaryContext) contextReader.getContext();
                context = binCtx;
                levelContextLabel.setText(GUIMessages.getString("GUI.context") + " : " + contextFile.getName());
            } catch (FileNotFoundException e) {
                DialogBox.showMessageError(this, GUIMessages.getString("GUI.fileCannotBeFound"), GUIMessages.getString("GUI.errorWithFile"));
                return;
            } catch (ReaderException e) {
                DialogBox.showMessageError(this, e);
                return;
            }
        }
    }

    private class PanelListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == loadedCtxBtn && loadedCtxBtn.isSelected()) {
                fileChooserBtn.setEnabled(false);
                contextList.setEnabled(true);
                thisPanel.repaint();
            } else if (e.getSource() == ctxFileBtn && ctxFileBtn.isSelected()) {
                contextList.setEnabled(false);
                fileChooserBtn.setEnabled(true);
                thisPanel.repaint();
            } else if (e.getSource() == fileChooserBtn) {
                openContext();
            }
        }
    }

    private class PanelMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int index = contextList.locationToIndex(e.getPoint());
            if (contextList.getCellBounds(index, index).contains(e.getPoint()) && contextList.isEnabled()) {
                context = contexts.elementAt(index);
                levelContextLabel.setText(GUIMessages.getString("GUI.context") + " : " + context.getName());
            }
        }
    }

    private class EndListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (context != null) {
                ContextTableScrollPane selectedPane = viewer.getSelectedPane();
                NestedContextTable currentTable = (NestedContextTable) selectedPane.getContextTable();
                currentTable.addLevel(context);
                currentTable.validate();
            }
            thisFrame.setVisible(false);
            thisFrame.dispose();
        }
    }
}

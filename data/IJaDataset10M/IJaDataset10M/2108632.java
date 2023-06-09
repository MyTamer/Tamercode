package net.sourceforge.tessboxeditor;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;

public class GuiWithMRU extends Gui {

    private java.util.List<String> mruList = new java.util.ArrayList<String>();

    private String strClearRecentFiles;

    /**
     * Populates MRU List when the program first starts.
     */
    @Override
    protected void populateMRUList() {
        String[] fileNames = prefs.get("MruList", "").split(File.pathSeparator);
        for (String fileName : fileNames) {
            if (!fileName.equals("")) {
                mruList.add(fileName);
            }
        }
        updateMRUMenu();
    }

    /**
     * Update MRU List after open or save file.
     *
     * @param fileName
     */
    @Override
    protected void updateMRUList(String fileName) {
        if (mruList.contains(fileName)) {
            mruList.remove(fileName);
        }
        mruList.add(0, fileName);
        if (mruList.size() > 10) {
            mruList.remove(10);
        }
        updateMRUMenu();
    }

    /**
     * Update MRU Submenu.
     */
    private void updateMRUMenu() {
        this.jMenuRecentFiles.removeAll();
        if (mruList.isEmpty()) {
            this.jMenuRecentFiles.add(bundle.getString("No_Recent_Files"));
        } else {
            Action mruAction = new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JMenuItem item = (JMenuItem) e.getSource();
                    String fileName = item.getText();
                    if (fileName.equals(strClearRecentFiles)) {
                        mruList.clear();
                        jMenuRecentFiles.removeAll();
                        jMenuRecentFiles.add(bundle.getString("No_Recent_Files"));
                    } else {
                        openFile(new File(fileName));
                    }
                }
            };
            for (String fileName : mruList) {
                JMenuItem item = this.jMenuRecentFiles.add(fileName);
                item.addActionListener(mruAction);
            }
            this.jMenuRecentFiles.addSeparator();
            strClearRecentFiles = bundle.getString("Clear_Recent_Files");
            JMenuItem jMenuItemClear = this.jMenuRecentFiles.add(strClearRecentFiles);
            jMenuItemClear.setMnemonic(bundle.getString("jMenuItemClear.Mnemonic").charAt(0));
            jMenuItemClear.addActionListener(mruAction);
        }
    }

    @Override
    void quit() {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < this.mruList.size(); i++) {
            buf.append(this.mruList.get(i)).append(File.pathSeparatorChar);
        }
        prefs.put("MruList", buf.toString());
        super.quit();
    }
}

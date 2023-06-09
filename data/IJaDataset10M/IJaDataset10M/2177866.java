package net.charabia.jsmoothgen.application.gui.editors;

import net.charabia.jsmoothgen.application.gui.skeleditors.*;
import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.application.*;
import net.charabia.jsmoothgen.application.gui.*;
import net.charabia.jsmoothgen.application.gui.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.l2fprod.common.swing.*;
import se.datadosen.component.RiverLayout;

public class SkeletonPropertiesEditor extends Editor implements JSmoothModelBean.SkeletonChangedListener {

    private String m_currentSkelName = null;

    private SkeletonBean m_skel = null;

    private SkelPanel m_panel = new SkelPanel();

    private Vector m_editors = new Vector();

    public SkeletonPropertiesEditor() {
        setLayout(new BorderLayout());
        add(m_panel, BorderLayout.CENTER);
    }

    public void rebuildProperties() {
        m_skel = null;
        if (m_currentSkelName != null) m_skel = Main.SKELETONS.getSkeleton(m_currentSkelName);
        SkeletonProperty[] sprops = null;
        if (m_skel != null) sprops = m_skel.getSkeletonProperties(); else sprops = new SkeletonProperty[0];
        m_panel.removeAll();
        m_panel.setLayout(new RiverLayout());
        m_editors.clear();
        for (int i = 0; i < sprops.length; i++) {
            SkelPropEditor spe = null;
            if (sprops[i].getType().equalsIgnoreCase(SkeletonProperty.TYPE_STRING)) {
                spe = new StringEditor();
            } else if (sprops[i].getType().equalsIgnoreCase(SkeletonProperty.TYPE_TEXTAREA)) {
                spe = new TextAreaEditor();
            } else if (sprops[i].getType().equalsIgnoreCase(SkeletonProperty.TYPE_BOOLEAN)) {
                spe = new CheckBoxEditor();
            } else if (sprops[i].getType().equalsIgnoreCase(SkeletonProperty.TYPE_AUTODOWNLOADURL)) {
                spe = new AutoDownloadURLEditor();
            } else if (sprops[i].getType().equalsIgnoreCase(SkeletonProperty.TYPE_IMAGESELECTOR)) {
                spe = new ImageSelectorEditor();
            }
            if (spe == null) {
                spe = new StringEditor();
            }
            m_editors.add(spe);
            spe.bind(sprops[i]);
            if (spe.labelAtLeft()) {
                m_panel.add("br", new JLabel(Main.local(sprops[i].getLabel())));
                m_panel.add("tab", new HelpButton(Main.local(sprops[i].getDescription())));
                m_panel.add("tab hfill", spe.getGUI());
            } else {
                m_panel.add("br right", spe.getGUI());
                m_panel.add("tab", new HelpButton(Main.local(sprops[i].getDescription())));
                m_panel.add("tab hfill", new JLabel(Main.local(sprops[i].getLabel())));
            }
        }
        revalidate();
        m_panel.revalidate();
        doLayout();
        m_panel.doLayout();
    }

    public void dataChanged() {
        if (m_model.getSkeletonName() == null) {
            m_currentSkelName = null;
            rebuildProperties();
        }
        if ((m_model != null) && (m_model.getSkeletonName() != null) && (!m_model.getSkeletonName().equalsIgnoreCase(m_currentSkelName))) {
            m_currentSkelName = m_model.getSkeletonName();
            rebuildProperties();
        }
        JSmoothModelBean.Property[] jsprop = m_model.getSkeletonProperties();
        if (jsprop != null) {
            for (Enumeration e = m_editors.elements(); e.hasMoreElements(); ) {
                SkelPropEditor spe = (SkelPropEditor) e.nextElement();
                JSmoothModelBean.Property p = getPropertyInstance(spe.getIdName());
                if (p != null) spe.valueChanged(p.getValue());
            }
        } else {
            SkeletonBean skel = Main.SKELETONS.getSkeleton(m_model.getSkeletonName());
            SkeletonProperty[] sprops = null;
            if (skel != null) sprops = skel.getSkeletonProperties();
            if (sprops != null) {
                for (Enumeration e = m_editors.elements(); e.hasMoreElements(); ) {
                    SkelPropEditor spe = (SkelPropEditor) e.nextElement();
                    for (int i = 0; i < sprops.length; i++) {
                        if (sprops[i].getIdName().equals(spe.getIdName())) spe.valueChanged(sprops[i].getValue());
                    }
                }
            }
        }
        java.io.File root = Main.MAIN.getProjectFile();
        if ((root != null) && (root.getParentFile() != null)) {
            root = root.getParentFile();
            for (Enumeration e = m_editors.elements(); e.hasMoreElements(); ) {
                SkelPropEditor spe = (SkelPropEditor) e.nextElement();
                spe.setBaseDir(root);
            }
        }
    }

    JSmoothModelBean.Property getPropertyInstance(String name) {
        JSmoothModelBean.Property[] jsprop = m_model.getSkeletonProperties();
        for (int i = 0; i < jsprop.length; i++) {
            if (jsprop[i].getKey().equals(name)) return jsprop[i];
        }
        return null;
    }

    public void updateModel() {
        if (m_skel != null) {
            JSmoothModelBean.Property[] props = new JSmoothModelBean.Property[m_editors.size()];
            int index = 0;
            for (Enumeration e = m_editors.elements(); e.hasMoreElements(); ) {
                SkelPropEditor spe = (SkelPropEditor) e.nextElement();
                props[index] = new JSmoothModelBean.Property();
                props[index].setKey(spe.getIdName());
                props[index].setValue(spe.get());
                props[index].isLocalFile = spe.isLocalFile();
                index++;
            }
            m_model.setSkeletonProperties(props);
        }
    }

    public void skeletonChanged() {
        rebuildProperties();
        dataChanged();
    }

    public String getLabel() {
        return "SKELETONPROPERTIES_LABEL";
    }

    public String getDescription() {
        return "SKELETONPROPERTIES_HELP";
    }

    public boolean needsBigSpace() {
        return true;
    }
}

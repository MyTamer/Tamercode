package org.oboedit.controller;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.bbop.framework.GUIManager;
import org.obo.datamodel.impl.NamedIDProfile;
import org.obo.identifier.DefaultIDGenerator;
import org.obo.identifier.IDGenerator;
import org.obo.identifier.IDProfile;
import org.obo.util.IDUtil;
import org.oboedit.gui.event.RootChangeEvent;
import org.oboedit.gui.event.RootChangeListener;
import org.apache.log4j.*;

public class IDManager {

    protected static final Logger logger = Logger.getLogger(IDManager.class);

    protected static IDManager idManager;

    protected IDGenerator idAdapter = new DefaultIDGenerator();

    public static IDManager getManager() {
        if (idManager == null) {
            idManager = new IDManager();
            SessionManager.getManager().addRootChangeListener(new RootChangeListener() {

                public void changeRoot(RootChangeEvent e) {
                    idMismatchInteraction();
                }
            });
            if (getManager().getIDAdapter() instanceof DefaultIDGenerator) {
                ((DefaultIDGenerator) getManager().getIDAdapter()).setProfile(loadCurrentIDProfile());
            }
        }
        return idManager;
    }

    protected static void idMismatchInteraction() {
        if (IDManager.getManager().getIDAdapter() instanceof DefaultIDGenerator) {
            DefaultIDGenerator idGen = (DefaultIDGenerator) IDManager.getManager().getIDAdapter();
            IDProfile currentProfile = idGen.getProfile();
            if (currentProfile != null) {
                IDProfile profile = SessionManager.getManager().getSession().getIDProfile();
                if (!IDUtil.equals(currentProfile, profile)) {
                    String message = "The new ID generation profile (" + profile.getDefaultRule() + ")\n" + "does not match the previously loaded one (" + currentProfile.getDefaultRule() + ").";
                    logger.info(message);
                    int result = JOptionPane.showConfirmDialog(null, message + "\n\nDo you want to use the new one (" + profile.getDefaultRule() + ")?", "ID Profile Mismatch", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        idGen.setProfile(profile);
                    }
                }
            }
        }
    }

    public IDGenerator getIDAdapter() {
        return idAdapter;
    }

    public static IDProfile loadCurrentIDProfile() {
        File profileFile = new File(GUIManager.getPrefsDir(), "idprofiles.xml");
        IDProfile currentProfile = null;
        if (profileFile.exists()) {
            try {
                XMLDecoder decoder = new XMLDecoder(new FileInputStream(profileFile));
                decoder.readObject();
                currentProfile = (IDProfile) decoder.readObject();
                decoder.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            IDProfile defaultProfile = new NamedIDProfile("<default ID profile>");
            currentProfile = defaultProfile;
            defaultProfile.setDefaultRule("ID:$sequence(7,0,9999999)$");
        }
        return currentProfile;
    }

    @SuppressWarnings("unchecked")
    public static List<IDProfile> loadIDProfiles() {
        File profileFile = new File(GUIManager.getPrefsDir(), "idprofiles.xml");
        List<IDProfile> profiles = null;
        if (profileFile.exists()) {
            try {
                XMLDecoder decoder = new XMLDecoder(new FileInputStream(profileFile));
                profiles = (List) decoder.readObject();
                decoder.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (profiles == null) {
            profiles = new LinkedList<IDProfile>();
            IDProfile defaultProfile = new NamedIDProfile("<default ID profile>");
            defaultProfile.setDefaultRule("ID:$sequence(7,0,9999999)$");
            profiles.add(defaultProfile);
        }
        return profiles;
    }
}

package org.exmaralda.folker.gui;

import javax.swing.*;
import org.exmaralda.folker.data.Speaker;
import org.exmaralda.folker.data.Speakerlist;

/**
 *
 * @author thomas
 */
public class SpeakerListModel extends AbstractListModel {

    Speakerlist speakerlist;

    /** Creates a new instance of SpeakerListModel */
    public SpeakerListModel(Speakerlist sl) {
        speakerlist = sl;
    }

    public Object getElementAt(int index) {
        return speakerlist.getSpeakers().elementAt(index);
    }

    public int getSize() {
        return speakerlist.getSpeakers().size();
    }

    public void addSpeaker() {
        speakerlist.addSpeaker("X");
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);
    }

    public void removeSpeaker(Speaker s) {
        int index = speakerlist.getSpeakers().indexOf(s);
        speakerlist.removeSpeaker(s);
        fireIntervalRemoved(this, index, index);
    }

    public void setSpeakerID(Speaker s, String id) {
        speakerlist.setSpeakerID(s, id);
        int index = speakerlist.getSpeakers().indexOf(s);
        fireContentsChanged(this, index, index);
    }

    public void setSpeakerName(Speaker s, String name) {
        s.setName(name);
        int index = speakerlist.getSpeakers().indexOf(s);
        fireContentsChanged(this, index, index);
    }
}

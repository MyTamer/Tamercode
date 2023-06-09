package uk.org.toot.midi.sequence.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import javax.swing.event.ChangeEvent;
import uk.org.toot.misc.ChangeSupport;
import uk.org.toot.midi.sequence.MidiSequence;
import uk.org.toot.midi.sequence.MidiTrack;
import uk.org.toot.midi.sequence.MidiNote;

public class SequenceSelection implements Selection, Cloneable {

    /** @supplierCardinality 1 */
    private MidiSequence sequence;

    private List<TrackSelection> tracks;

    private ChangeSupport changeSupport = null;

    public SequenceSelection(MidiSequence sequence) {
        this.sequence = sequence;
        createTracks();
    }

    protected void createTracks() {
        MidiTrack[] mtracks = getSequence().getMidiTracks();
        int nTracks = mtracks.length;
        tracks = new ArrayList<TrackSelection>(nTracks);
        for (int t = 0; t < nTracks; t++) {
            tracks.add(new TrackSelection(mtracks[t]));
        }
    }

    public void clear() {
        createTracks();
    }

    public int size() {
        int size = 0;
        for (int t = 0; t < tracks.size(); t++) {
            size += tracks.get(t).size();
        }
        return size;
    }

    public boolean isEmpty() {
        for (int t = 0; t < tracks.size(); t++) {
            if (tracks.get(t).size() > 0) return false;
        }
        return true;
    }

    public boolean contains(MidiNote note) {
        for (int t = 0; t < tracks.size(); t++) {
            if (tracks.get(t).contains(note)) return true;
        }
        return false;
    }

    public void fireChanged() {
        if (changeSupport != null) {
            changeSupport.fireChange(new ChangeEvent(this));
        }
    }

    public TrackSelection[] getTracks() {
        return (TrackSelection[]) tracks.toArray(new TrackSelection[0]);
    }

    public TrackSelection getTrack(MidiTrack track) {
        for (TrackSelection sel : tracks) {
            if (sel.getTrack() == track) return sel;
        }
        return null;
    }

    public MidiSequence getSequence() {
        return sequence;
    }

    public ChangeSupport getChangeSupport() {
        if (changeSupport == null) {
            changeSupport = new ChangeSupport(this);
        }
        return changeSupport;
    }

    public boolean cut() {
        Iterator iterator = tracks.iterator();
        while (iterator.hasNext()) {
            TrackSelection tsel = (TrackSelection) iterator.next();
            tsel.cut();
        }
        getSequence().fireChanged();
        return true;
    }

    public boolean paste() {
        Iterator iterator = tracks.iterator();
        while (iterator.hasNext()) {
            TrackSelection tsel = (TrackSelection) iterator.next();
            tsel.paste();
        }
        getSequence().fireChanged();
        return true;
    }

    public boolean transpose(int semitones) {
        Iterator iterator = tracks.iterator();
        while (iterator.hasNext()) {
            TrackSelection tsel = (TrackSelection) iterator.next();
            tsel.transpose(semitones);
        }
        getSequence().fireChanged();
        return true;
    }

    public boolean move(long ticks, int semitones) {
        Iterator iterator = tracks.iterator();
        while (iterator.hasNext()) {
            TrackSelection tsel = (TrackSelection) iterator.next();
            tsel.move(ticks, semitones);
        }
        getSequence().fireChanged();
        return true;
    }

    public Object clone() {
        try {
            SequenceSelection cloned = (SequenceSelection) super.clone();
            for (int t = 0; t < tracks.size(); t++) {
                TrackSelection ts = tracks.get(t);
                cloned.tracks.set(t, (TrackSelection) ts.clone());
            }
            return cloned;
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace();
        }
        return null;
    }

    public Object deepCopy() {
        try {
            SequenceSelection cloned = (SequenceSelection) super.clone();
            cloned.tracks = new ArrayList<TrackSelection>(getSequence().getMidiTrackCount());
            for (int t = 0; t < tracks.size(); t++) {
                TrackSelection ts = tracks.get(t);
                cloned.tracks.add(t, (TrackSelection) ts.deepCopy());
            }
            return cloned;
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace();
        }
        return null;
    }
}

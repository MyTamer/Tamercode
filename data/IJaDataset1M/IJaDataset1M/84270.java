package org.herac.tuxguitar.app.editors.channel;

import java.util.List;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.undo.undoables.channel.UndoableChannelGeneric;
import org.herac.tuxguitar.app.undo.undoables.channel.UndoableModifyChannel;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGChannel;

public class TGChannelHandle {

    public TGChannelHandle() {
        super();
    }

    public void addChannel() {
        UndoableChannelGeneric undoable = UndoableChannelGeneric.startUndo();
        getManager().addChannel();
        TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
        TuxGuitar.instance().getFileHistory().setUnsavedFile();
        TuxGuitar.instance().updateCache(true);
    }

    public void removeChannel(TGChannel channel) {
        UndoableChannelGeneric undoable = UndoableChannelGeneric.startUndo();
        getManager().removeChannel(channel);
        TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
        TuxGuitar.instance().getFileHistory().setUnsavedFile();
        TuxGuitar.instance().updateCache(true);
    }

    public void updateChannel(int id, short bnk, short prg, short vol, short bal, short cho, short rev, short pha, short tre, String name) {
        TGChannel channel = getManager().getChannel(id);
        if (channel != null) {
            boolean programChange = (bnk != channel.getBank() || prg != channel.getProgram());
            UndoableModifyChannel undoable = UndoableModifyChannel.startUndo(id);
            getManager().updateChannel(id, bnk, prg, vol, bal, cho, rev, pha, tre, name);
            TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo());
            TuxGuitar.instance().getFileHistory().setUnsavedFile();
            TuxGuitar.instance().updateCache(true);
            if (TuxGuitar.instance().getPlayer().isRunning()) {
                if (programChange) {
                    TuxGuitar.instance().getPlayer().updatePrograms();
                } else {
                    TuxGuitar.instance().getPlayer().updateControllers();
                }
            }
        }
    }

    public List getChannels() {
        return getManager().getChannels();
    }

    public boolean isAnyTrackConnectedToChannel(TGChannel channel) {
        return getManager().isAnyTrackConnectedToChannel(channel.getChannelId());
    }

    public boolean isAnyPercussionChannel() {
        return getManager().isAnyPercussionChannel();
    }

    public boolean isPlayerRunning() {
        return TuxGuitar.instance().getPlayer().isRunning();
    }

    private TGSongManager getManager() {
        return TuxGuitar.instance().getSongManager();
    }
}

package org.herac.tuxguitar.app.actions.duration;

import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.actions.Action;
import org.herac.tuxguitar.app.actions.ActionData;
import org.herac.tuxguitar.app.editors.tab.Caret;
import org.herac.tuxguitar.app.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDuration;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ChangeDottedDurationAction extends Action {

    public static final String NAME = "action.note.duration.change-dotted";

    public ChangeDottedDurationAction() {
        super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
    }

    protected int execute(ActionData actionData) {
        UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();
        getSelectedDuration().setDotted(!getSelectedDuration().isDotted());
        getSelectedDuration().setDoubleDotted(false);
        setDurations();
        addUndoableEdit(undoable.endUndo());
        return 0;
    }

    private void setDurations() {
        Caret caret = getEditor().getTablature().getCaret();
        caret.changeDuration(getSelectedDuration().clone(getSongManager().getFactory()));
        TuxGuitar.instance().getFileHistory().setUnsavedFile();
        fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
    }

    public TGDuration getSelectedDuration() {
        return getEditor().getTablature().getCaret().getDuration();
    }
}

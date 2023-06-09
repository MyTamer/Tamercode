package CB_Core.GL_UI.Main.Actions;

import CB_Core.GL_UI.SpriteCache;
import CB_Core.GL_UI.Main.TabMainView;
import CB_Core.GL_UI.Views.NotesView;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class CB_Action_ShowNotesView extends CB_Action_ShowView {

    public CB_Action_ShowNotesView() {
        super("Notes", AID_SHOW_NOTES);
    }

    @Override
    public void Execute() {
        if ((TabMainView.notesView == null) && (tabMainView != null) && (tab != null)) TabMainView.notesView = new NotesView(tab.getContentRec(), "NotesView");
        if ((TabMainView.notesView != null) && (tab != null)) tab.ShowView(TabMainView.notesView);
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public Sprite getIcon() {
        return SpriteCache.Icons.get(23);
    }
}

package org.eclipse.babel.editor.i18n.actions;

import java.util.Locale;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Pascal Essiembre
 *
 */
public class ShowSimilarAction extends Action {

    private final String[] keys;

    private final String key;

    private final Locale locale;

    /**
     * 
     */
    public ShowSimilarAction(String[] keys, String key, Locale locale) {
        super();
        this.keys = keys;
        this.key = key;
        this.locale = locale;
        setText("Show similar keys.");
        setImageDescriptor(UIUtils.getImageDescriptor("similar.gif"));
        setToolTipText("TODO put something here");
    }

    public void run() {
        StringBuffer buf = new StringBuffer("\"" + key + "\" (" + UIUtils.getDisplayName(locale) + ") has similar " + "value as the following key(s): \n\n");
        for (int i = 0; i < keys.length; i++) {
            String similarKey = keys[i];
            if (!key.equals(similarKey)) {
                buf.append("    � ");
                buf.append(similarKey);
                buf.append(" (" + UIUtils.getDisplayName(locale) + ")");
                buf.append("\n");
            }
        }
        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Similar value", buf.toString());
    }
}

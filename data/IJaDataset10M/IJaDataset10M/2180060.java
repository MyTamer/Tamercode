package se.kth.cid.conzilla.view;

/**
 * @author matthias
 *
 */
public class CanvasTabManager extends TabManager {

    /**
     * By overriding it empty we make sure that there is 
     * no frame containing the splitpane with tabbs.
     * This is good for example in the applet case.
     * 
     * @see se.kth.cid.conzilla.view.TabManager#initDefaultRootPaneContainer()
     */
    protected void initDefaultRootPaneContainer() {
    }
}

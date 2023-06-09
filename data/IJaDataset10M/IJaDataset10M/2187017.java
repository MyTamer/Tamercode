package com.android.ide.eclipse.hierarchyviewer.views;

import com.android.hierarchyviewerlib.actions.InspectScreenshotAction;
import com.android.hierarchyviewerlib.actions.LoadViewHierarchyAction;
import com.android.hierarchyviewerlib.actions.RefreshWindowsAction;
import com.android.hierarchyviewerlib.ui.DeviceSelector;
import com.android.ide.eclipse.hierarchyviewer.PixelPerfectPespective;
import com.android.ide.eclipse.hierarchyviewer.TreeViewPerspective;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;

public class DeviceSelectorView extends ViewPart implements IPerspectiveListener {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.DeviceSelectorView";

    private DeviceSelector mDeviceSelector;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        IPerspectiveDescriptor perspective = getViewSite().getPage().getPerspective();
        boolean doTreeViewStuff = true;
        boolean doPixelPerfectStuff = true;
        if (perspective.getId().equals(PixelPerfectPespective.ID)) {
            doTreeViewStuff = false;
        } else if (perspective.getId().equals(TreeViewPerspective.ID)) {
            doPixelPerfectStuff = false;
        }
        mDeviceSelector = new DeviceSelector(parent, doTreeViewStuff, doPixelPerfectStuff);
        placeActions(doTreeViewStuff, doPixelPerfectStuff);
        getViewSite().getWorkbenchWindow().addPerspectiveListener(this);
    }

    @Override
    public void dispose() {
        super.dispose();
        getViewSite().getWorkbenchWindow().removePerspectiveListener(this);
    }

    private void placeActions(boolean doTreeViewStuff, boolean doPixelPerfectStuff) {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager mm = actionBars.getMenuManager();
        mm.removeAll();
        mm.add(RefreshWindowsAction.getAction());
        IToolBarManager tm = actionBars.getToolBarManager();
        tm.removeAll();
        tm.add(RefreshWindowsAction.getAction());
        if (doTreeViewStuff) {
            mm.add(LoadViewHierarchyAction.getAction());
            tm.add(LoadViewHierarchyAction.getAction());
        }
        if (doPixelPerfectStuff) {
            mm.add(InspectScreenshotAction.getAction());
            tm.add(InspectScreenshotAction.getAction());
        }
        mm.updateAll(true);
        tm.update(true);
        actionBars.updateActionBars();
    }

    @Override
    public void setFocus() {
        mDeviceSelector.setFocus();
    }

    public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective) {
        if (perspective.getId().equals(PixelPerfectPespective.ID)) {
            mDeviceSelector.setMode(false, true);
            placeActions(false, true);
        } else if (perspective.getId().equals(TreeViewPerspective.ID)) {
            mDeviceSelector.setMode(true, false);
            placeActions(true, false);
        } else {
            mDeviceSelector.setMode(true, true);
            placeActions(true, true);
        }
    }

    public void perspectiveChanged(IWorkbenchPage page, IPerspectiveDescriptor perspective, String changeId) {
    }
}

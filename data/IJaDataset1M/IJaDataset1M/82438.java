package com.android.ide.eclipse.hierarchyviewer.views;

import com.android.ddmuilib.ImageLoader;
import com.android.hierarchyviewerlib.HierarchyViewerDirector;
import com.android.hierarchyviewerlib.models.TreeViewModel;
import com.android.hierarchyviewerlib.models.TreeViewModel.ITreeChangeListener;
import com.android.hierarchyviewerlib.ui.LayoutViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;

public class LayoutView extends ViewPart implements ITreeChangeListener {

    public static final String ID = "com.android.ide.eclipse.hierarchyviewer.views.LayoutView";

    private LayoutViewer mLayoutViewer;

    private Image mOnBlack;

    private Image mOnWhite;

    private Action mShowExtrasAction = new Action("Show &Extras", Action.AS_CHECK_BOX) {

        @Override
        public void run() {
            mLayoutViewer.setShowExtras(isChecked());
        }
    };

    private Action mLoadAllViewsAction = new Action("Load All &Views") {

        @Override
        public void run() {
            HierarchyViewerDirector.getDirector().loadAllViews();
            mShowExtrasAction.setChecked(true);
            mLayoutViewer.setShowExtras(true);
        }
    };

    private Action mOnBlackWhiteAction = new Action("Change Background &Color") {

        @Override
        public void run() {
            boolean newValue = !mLayoutViewer.getOnBlack();
            mLayoutViewer.setOnBlack(newValue);
            if (newValue) {
                setImageDescriptor(ImageDescriptor.createFromImage(mOnWhite));
            } else {
                setImageDescriptor(ImageDescriptor.createFromImage(mOnBlack));
            }
        }
    };

    @Override
    public void createPartControl(Composite parent) {
        mShowExtrasAction.setAccelerator(SWT.MOD1 + 'E');
        ImageLoader imageLoader = ImageLoader.getLoader(HierarchyViewerDirector.class);
        Image image = imageLoader.loadImage("show-extras.png", Display.getDefault());
        mShowExtrasAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        mShowExtrasAction.setToolTipText("Show images");
        mShowExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);
        mOnWhite = imageLoader.loadImage("on-white.png", Display.getDefault());
        mOnBlack = imageLoader.loadImage("on-black.png", Display.getDefault());
        mOnBlackWhiteAction.setAccelerator(SWT.MOD1 + 'C');
        mOnBlackWhiteAction.setImageDescriptor(ImageDescriptor.createFromImage(mOnWhite));
        mOnBlackWhiteAction.setToolTipText("Change layout viewer background color");
        mLoadAllViewsAction.setAccelerator(SWT.MOD1 + 'V');
        image = imageLoader.loadImage("load-all-views.png", Display.getDefault());
        mLoadAllViewsAction.setImageDescriptor(ImageDescriptor.createFromImage(image));
        mLoadAllViewsAction.setToolTipText("Load all view images");
        mLoadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);
        parent.setLayout(new FillLayout());
        mLayoutViewer = new LayoutViewer(parent);
        placeActions();
        TreeViewModel.getModel().addTreeChangeListener(this);
    }

    public void placeActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        IMenuManager mm = actionBars.getMenuManager();
        mm.removeAll();
        mm.add(mOnBlackWhiteAction);
        mm.add(mShowExtrasAction);
        mm.add(mLoadAllViewsAction);
        IToolBarManager tm = actionBars.getToolBarManager();
        tm.removeAll();
        tm.add(mOnBlackWhiteAction);
        tm.add(mShowExtrasAction);
        tm.add(mLoadAllViewsAction);
    }

    @Override
    public void dispose() {
        super.dispose();
        TreeViewModel.getModel().removeTreeChangeListener(this);
    }

    @Override
    public void setFocus() {
        mLayoutViewer.setFocus();
    }

    public void selectionChanged() {
    }

    public void treeChanged() {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                mLoadAllViewsAction.setEnabled(TreeViewModel.getModel().getTree() != null);
                mShowExtrasAction.setEnabled(TreeViewModel.getModel().getTree() != null);
            }
        });
    }

    public void viewportChanged() {
    }

    public void zoomChanged() {
    }
}

package com.android.frameworktest.focus;

import com.android.frameworktest.util.InternalSelectionView;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.view.ViewGroup;

/**
 * {@link android.view.FocusFinder#findNextFocus(android.view.ViewGroup, android.view.View, int)}
 * and
 * {@link android.view.View#requestFocus(int, android.graphics.Rect)}
 * work together to give a newly focused item a hint about the most interesting
 * rectangle of the previously focused view.  The view taking focus can use this
 * to set an internal selection more appropriate using this rect.
 *
 * This Activity excercises that behavior using three adjacent {@link com.android.frameworktest.util.InternalSelectionView}
 * that report interesting rects when giving up focus, and use interesting rects
 * when taking focus to best select the internal row to show as selected.
 */
public class AdjacentVerticalRectLists extends Activity {

    private LinearLayout mLayout;

    private InternalSelectionView mLeftColumn;

    private InternalSelectionView mMiddleColumn;

    private InternalSelectionView mRightColumn;

    public LinearLayout getLayout() {
        return mLayout;
    }

    public InternalSelectionView getLeftColumn() {
        return mLeftColumn;
    }

    public InternalSelectionView getMiddleColumn() {
        return mMiddleColumn;
    }

    public InternalSelectionView getRightColumn() {
        return mRightColumn;
    }

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mLayout = new LinearLayout(this);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.FILL_PARENT, 1);
        mLeftColumn = new InternalSelectionView(this, 5, "left column");
        mLeftColumn.setLayoutParams(params);
        mLeftColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mLeftColumn);
        mMiddleColumn = new InternalSelectionView(this, 5, "middle column");
        mMiddleColumn.setLayoutParams(params);
        mMiddleColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mMiddleColumn);
        mRightColumn = new InternalSelectionView(this, 5, "right column");
        mRightColumn.setLayoutParams(params);
        mRightColumn.setPadding(10, 10, 10, 10);
        mLayout.addView(mRightColumn);
        setContentView(mLayout);
    }
}

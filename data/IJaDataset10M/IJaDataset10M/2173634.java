package org.eclipse.gef.examples.flow.parts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

/**
 * DirectEditManager for Activities
 * @author Daniel Lee
 */
public class ActivityDirectEditManager extends DirectEditManager {

    Font scaledFont;

    protected VerifyListener verifyListener;

    protected Label activityLabel;

    /**
 * Creates a new ActivityDirectEditManager with the given attributes.
 * @param source the source EditPart
 * @param editorType type of editor
 * @param locator the CellEditorLocator
 */
    public ActivityDirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator, Label label) {
        super(source, editorType, locator);
        activityLabel = label;
    }

    /**
 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
 */
    protected void bringDown() {
        Font disposeFont = scaledFont;
        scaledFont = null;
        super.bringDown();
        if (disposeFont != null) disposeFont.dispose();
    }

    /**
 * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
 */
    protected void initCellEditor() {
        Text text = (Text) getCellEditor().getControl();
        verifyListener = new VerifyListener() {

            public void verifyText(VerifyEvent event) {
                Text text = (Text) getCellEditor().getControl();
                String oldText = text.getText();
                String leftText = oldText.substring(0, event.start);
                String rightText = oldText.substring(event.end, oldText.length());
                GC gc = new GC(text);
                Point size = gc.textExtent(leftText + event.text + rightText);
                gc.dispose();
                if (size.x != 0) size = text.computeSize(size.x, SWT.DEFAULT);
                getCellEditor().getControl().setSize(size.x, size.y);
            }
        };
        text.addVerifyListener(verifyListener);
        String initialLabelText = activityLabel.getText();
        getCellEditor().setValue(initialLabelText);
        IFigure figure = getEditPart().getFigure();
        scaledFont = figure.getFont();
        FontData data = scaledFont.getFontData()[0];
        Dimension fontSize = new Dimension(0, data.getHeight());
        activityLabel.translateToAbsolute(fontSize);
        data.setHeight(fontSize.height);
        scaledFont = new Font(null, data);
        text.setFont(scaledFont);
    }

    /**
 * @see org.eclipse.gef.tools.DirectEditManager#unhookListeners()
 */
    protected void unhookListeners() {
        super.unhookListeners();
        Text text = (Text) getCellEditor().getControl();
        text.removeVerifyListener(verifyListener);
        verifyListener = null;
    }
}

package org.regilo.core.ui.internal.treelist;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A special composite to layout columns inside a table. The composite is needed
 * since we have to layout the columns "before" the actual table gets layouted.
 * Hence we can't use a normal layout manager.
 * <p>
 * XXX: Should switch to use {@link org.eclipse.jface.layout.TableColumnLayout}.
 * </p>
 */
public class TableLayoutComposite extends Composite {

    /**
	 * The number of extra pixels taken as horizontal trim by the table column.
	 * To ensure there are N pixels available for the content of the column,
	 * assign N+COLUMN_TRIM for the column width.
	 * <p>
	 * XXX: Should either switch to use
	 * {@link org.eclipse.jface.layout.TableColumnLayout} or get API from JFace
	 * or SWT, see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=218483
	 * </p>
	 * 
	 * @since 3.1
	 */
    private static int COLUMN_TRIM;

    static {
        String platform = SWT.getPlatform();
        if ("win32".equals(platform)) COLUMN_TRIM = 4; else if ("carbon".equals(platform)) COLUMN_TRIM = 24; else COLUMN_TRIM = 3;
    }

    private List columns = new ArrayList();

    /**
	 * Creates a new <code>TableLayoutComposite</code>.
	 * 
	 * @param parent
	 *            the parent composite
	 * @param style
	 *            the SWT style
	 */
    public TableLayoutComposite(Composite parent, int style) {
        super(parent, style);
        addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                Rectangle area = getClientArea();
                Table table = (Table) getChildren()[0];
                Point preferredSize = computeTableSize(table);
                int width = area.width - 2 * table.getBorderWidth();
                if (preferredSize.y > area.height) {
                    Point vBarSize = table.getVerticalBar().getSize();
                    width -= vBarSize.x;
                }
                layoutTable(table, width, area, table.getSize().x < area.width);
            }
        });
    }

    /**
	 * Adds a new column of data to this table layout.
	 * 
	 * @param data
	 *            the column layout data
	 */
    public void addColumnData(ColumnLayoutData data) {
        columns.add(data);
    }

    private Point computeTableSize(Table table) {
        Point result = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        int width = 0;
        int size = columns.size();
        for (int i = 0; i < size; ++i) {
            ColumnLayoutData layoutData = (ColumnLayoutData) columns.get(i);
            if (layoutData instanceof ColumnPixelData) {
                ColumnPixelData col = (ColumnPixelData) layoutData;
                width += col.width;
                if (col.addTrim) {
                    width += COLUMN_TRIM;
                }
            } else if (layoutData instanceof ColumnWeightData) {
                ColumnWeightData col = (ColumnWeightData) layoutData;
                width += col.minimumWidth;
            } else {
                Assert.isTrue(false, "Unknown column layout data");
            }
        }
        if (width > result.x) result.x = width;
        return result;
    }

    private void layoutTable(Table table, int width, Rectangle area, boolean increase) {
        if (width <= 1) return;
        TableColumn[] tableColumns = table.getColumns();
        int size = Math.min(columns.size(), tableColumns.length);
        int[] widths = new int[size];
        int fixedWidth = 0;
        int numberOfWeightColumns = 0;
        int totalWeight = 0;
        for (int i = 0; i < size; i++) {
            ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
            if (col instanceof ColumnPixelData) {
                ColumnPixelData cpd = (ColumnPixelData) col;
                int pixels = cpd.width;
                if (cpd.addTrim) {
                    pixels += COLUMN_TRIM;
                }
                widths[i] = pixels;
                fixedWidth += pixels;
            } else if (col instanceof ColumnWeightData) {
                ColumnWeightData cw = (ColumnWeightData) col;
                numberOfWeightColumns++;
                int weight = cw.weight;
                totalWeight += weight;
            } else {
                Assert.isTrue(false, "Unknown column layout data");
            }
        }
        if (numberOfWeightColumns > 0) {
            int rest = width - fixedWidth;
            int totalDistributed = 0;
            for (int i = 0; i < size; ++i) {
                ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
                if (col instanceof ColumnWeightData) {
                    ColumnWeightData cw = (ColumnWeightData) col;
                    int weight = cw.weight;
                    int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
                    if (pixels < cw.minimumWidth) pixels = cw.minimumWidth;
                    totalDistributed += pixels;
                    widths[i] = pixels;
                }
            }
            int diff = rest - totalDistributed;
            for (int i = 0; diff > 0; ++i) {
                if (i == size) i = 0;
                ColumnLayoutData col = (ColumnLayoutData) columns.get(i);
                if (col instanceof ColumnWeightData) {
                    ++widths[i];
                    --diff;
                }
            }
        }
        if (increase) {
            table.setSize(area.width, area.height);
        }
        for (int i = 0; i < size; i++) {
            tableColumns[i].setWidth(widths[i]);
        }
        if (!increase) {
            table.setSize(area.width, area.height);
        }
    }
}

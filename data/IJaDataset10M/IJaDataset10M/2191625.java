package org.pandcorps.pandax.menu;

import java.util.*;
import org.pandcorps.pandam.*;
import org.pandcorps.pandam.event.action.*;

public abstract class Pangrid<I> extends Panctor {

    private final List<? extends List<I>> rows;

    private final Panmage cursor;

    private final float dimX;

    private final float dimY;

    private final float curOff;

    int curRow = 0;

    int curCol = 0;

    private final GridChangeEvent<I> changeEvent = new GridChangeEvent<I>(this);

    public Pangrid(final String id, final List<? extends List<I>> rows, final Panmage cursor, final float cursorOff) {
        super(id);
        this.rows = rows;
        this.cursor = cursor;
        final List<I> firstRow = rows.get(0);
        final I firstItem = firstRow.get(0);
        final Panmage first = getImage(firstItem);
        final Panple dim = first.getSize();
        dimX = dim.getX();
        dimY = dim.getY();
        curOff = cursorOff;
        if (getCurrentItem() == null) {
            right();
        }
        enable();
    }

    private final void enable() {
        final Pangine engine = Pangine.getEngine();
        final Panteraction interaction = engine.getInteraction();
        final Panput submit = interaction.KEY_ENTER, up = interaction.KEY_UP, down = interaction.KEY_DOWN;
        final Panput left = interaction.KEY_LEFT, right = interaction.KEY_RIGHT;
        Panput.inactivate(submit, up, down, left, right);
        final ActionStartListener upListener = new ActionStartListener() {

            @Override
            public void onActionStart(final ActionStartEvent event) {
                do {
                    curRow--;
                    if (curRow < 0) {
                        curRow = rows.size() - 1;
                    }
                } while (rows.get(curRow).get(curCol) == null);
                change();
            }
        };
        final ActionStartListener downListener = new ActionStartListener() {

            @Override
            public void onActionStart(final ActionStartEvent event) {
                do {
                    curRow++;
                    if (curRow >= rows.size()) {
                        curRow = 0;
                    }
                } while (rows.get(curRow).get(curCol) == null);
                change();
            }
        };
        final ActionStartListener leftListener = new ActionStartListener() {

            @Override
            public void onActionStart(final ActionStartEvent event) {
                final List<I> row = rows.get(curRow);
                do {
                    curCol--;
                    if (curCol < 0) {
                        curCol = row.size() - 1;
                    }
                } while (row.get(curCol) == null);
                change();
            }
        };
        final ActionStartListener rightListener = new ActionStartListener() {

            @Override
            public void onActionStart(final ActionStartEvent event) {
                right();
            }
        };
        final ActionStartListener submitListener = new ActionStartListener() {

            @Override
            public void onActionStart(final ActionStartEvent event) {
                unregisterListeners();
                Panput.inactivate(submit, up, down, left, right);
                try {
                    onSubmit(new GridSubmitEvent<I>(Pangrid.this));
                } catch (final Exception e) {
                    throw Panception.get(e);
                }
            }
        };
        register(submit, submitListener);
        register(up, upListener);
        register(down, downListener);
        register(left, leftListener);
        register(right, rightListener);
    }

    public final I getCurrentItem() {
        return rows.get(curRow).get(curCol);
    }

    private final void right() {
        final List<I> row = rows.get(curRow);
        do {
            curCol++;
            if (curCol >= row.size()) {
                curCol = 0;
            }
        } while (row.get(curCol) == null);
        change();
    }

    @Override
    protected final void updateView() {
    }

    @Override
    public final Pansplay getCurrentDisplay() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected final void renderView(final Panderer renderer) {
        final Panple center = getPosition();
        final float offX = center.getX() - (0.5f * dimX * rows.get(0).size());
        float y = center.getY() + (0.5f * dimY * rows.size());
        final float z = center.getZ();
        final Panlayer layer = getLayer();
        int rowIndex = 0;
        for (final List<I> row : rows) {
            int colIndex = 0;
            float x = offX;
            for (final I item : row) {
                if (item != null) {
                    renderer.render(layer, getImage(item), x, y, z);
                    if (rowIndex == curRow && colIndex == curCol) {
                        renderer.render(layer, cursor, x, y, z + curOff);
                    }
                }
                x += dimX;
                colIndex++;
            }
            y -= dimY;
            rowIndex++;
        }
    }

    protected abstract Panmage getImage(final I item);

    protected final void change() {
        try {
            onChange(changeEvent);
        } catch (final Exception e) {
            throw Panception.get(e);
        }
    }

    protected void onChange(final GridChangeEvent<I> event) throws Exception {
    }

    protected abstract void onSubmit(final GridSubmitEvent<I> event) throws Exception;
}

package net.sf.jaer.event;

import net.sf.jaer.chip.AEChip;

/**
 *
 * @author tobi
 */
public interface EventPacketInterface<T extends BasicEvent> {

    public String getDescription();

    T getEvent(int k);

    public int getSize();

    void clear();

    public int getNumCellTypes();

    /** @return time interval for packet - from first to last event, in timestamp ticks. Returns 0 if there are fewer than two events. */
    public int getDurationUs();

    public int getFirstTimestamp();

    public int getLastTimestamp();

    public void render(AEChip chip);

    public void display(AEChip chip);
}

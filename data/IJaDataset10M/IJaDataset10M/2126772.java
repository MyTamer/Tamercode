package org.mmtk.utility.statistics;

import org.vmmagic.pragma.*;

/**
 * This class implements a simple counter of events of different sizes
 * (eg object allocations, where total number of objects and total
 * volume of objects would be counted).
 *
 * The counter is trivially composed from two event counters (one for
 * counting the number of events, the other for counting the volume).
 */
@Uninterruptible
public class SizeCounter {

    /****************************************************************************
   *
   * Instance variables
   */
    private EventCounter units;

    private EventCounter volume;

    /**
   * Constructor
   *
   * @param name The name to be associated with this counter
   */
    public SizeCounter(String name) {
        this(name, true, false);
    }

    /**
   * Constructor
   *
   * @param name The name to be associated with this counter
   * @param start True if this counter is to be implicitly started
   * when <code>startAll()</code> is called (otherwise the counter
   * must be explicitly started).
   */
    public SizeCounter(String name, boolean start) {
        this(name, start, false);
    }

    /**
   * Constructor
   *
   * @param name The name to be associated with this counter
   * @param start True if this counter is to be implicitly started
   * when <code>startAll()</code> is called (otherwise the counter
   * must be explicitly started).
   * @param mergephases True if this counter does not separately
   * report GC and Mutator phases.
   */
    public SizeCounter(String name, boolean start, boolean mergephases) {
        units = new EventCounter(name, start, mergephases);
        volume = new EventCounter(name + "Volume", start, mergephases);
    }

    /**
   * Increment the event counter by <code>value</code>
   *
   * @param value The amount by which the counter should be incremented.
   */
    public void inc(int value) {
        units.inc();
        volume.inc(value);
    }

    /**
   * Start this counter
   */
    public void start() {
        units.start();
        volume.start();
    }

    /**
   * Stop this counter
   */
    public void stop() {
        units.stop();
        volume.stop();
    }

    /**
   * Print current (mid-phase) units
   */
    public void printCurrentUnits() {
        units.printCurrent();
    }

    /**
   * Print (mid-phase) volume
   */
    public void printCurrentVolume() {
        volume.printCurrent();
    }

    /**
   * Print units
   */
    public void printUnits() {
        units.printTotal();
    }

    /**
   * Print volume
   */
    public void printVolume() {
        volume.printTotal();
    }
}

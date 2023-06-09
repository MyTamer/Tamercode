package org.dishevelled.timer;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Collections;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

/**
 * Timer class with nanosecond resolution and summary
 * statistics on recorded elapsed times.  This class provides
 * nanosecond precision but not necessarily nanosecond accuracy
 * (see the javadoc for <code>System.nanoTime()</code> for more
 * details).  The recorded times themselves are not preserved,
 * however, only the statistics.  As a consequence <code>start()</code>
 * and <code>stop()</code> can be called many number of times
 * without requiring large amounts of memory.
 *
 * <h4>Special cases</h4>
 * <p>When <code>size() == 0</code>,
 * <pre>
 * min() == Double.NaN
 * max() == Double.NaN
 * mean() == Double.NaN
 * standardDeviation() == Double.NaN
 * </pre>
 * </p>
 * <p>When <code>size() == 1</code>,
 * <pre>
 * standardDeviation() == 0.0d
 * </pre>
 * </p>
 *
 * <h4>Static methods</h4>
 * <p>Execution time can be sensitive to various factors, such
 * as order of execution, runtime optimization from the just-in-time compiler
 * (JIT), and garbage collection.  This class provides static methods
 * to help deal with these factors.</p>
 *
 * <p>Given a few benchmarks to run, wrap them in Runnable objects
 * <pre>
 * Runnable r0 = new Runnable() { public void run() { ... } };
 * Runnable r1 = new Runnable() { public void run() { ... } };
 * Runnable r2 = new Runnable() { public void run() { ... } };
 * List&lt;Runnable&gt; benchmarks = Arrays.asList(new Runnable[] { r0, r1, r2 });
 * </pre>
 * Prime the JIT by running the benchmarks several times
 * <pre>
 * Timer.prime(benchmarks, 1000);
 * </pre>
 * Then measure the execution times of the benchmarks by running
 * them several times in random execution order
 * <pre>
 * Map&lt;Runnable, Timer&gt; result = Timer.shuffle(benchmarks, 100, 100);
 * </pre>
 * Summary statistics on recorded execution times are captured by the
 * timer returned for each Runnable benchmark
 * <pre>
 * for (Map.Entry&lt;Runnable, Timer&gt; e : result.entrySet()) {
 *   Runnable r = e.getKey();
 *   Timer t = e.getValue();
 *   System.out.println("runnable=" + r + " mean execution time=" + t.mean() + "ns");
 * }
 * </pre></p>
 *
 * @see java.lang.System#nanoTime
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class Timer {

    /** Summary statistics. */
    private final SummaryStatistics summaryStatistics;

    /** Last start time, in nanoseconds. */
    private double startTime;

    /** Flag indicating this timer has been started at least once. */
    private boolean started;

    /**
     * Create a new timer.
     */
    public Timer() {
        summaryStatistics = new SummaryStatistics();
        started = false;
    }

    /**
     * Reset the record of elapsed times from this timer.  After
     * the timer has been reset, it must be started at least once
     * before <code>stop()</code> is called.
     */
    public void reset() {
        summaryStatistics.clear();
        started = false;
    }

    /**
     * Start the timer.  The timer must be started at least once
     * before <code>stop()</code> is called.
     */
    public void start() {
        started = true;
        startTime = System.nanoTime();
    }

    /**
     * Stop the timer and record the time elapsed in nanoseconds
     * since <code>start()</code> was last called.
     *
     * @throws TimerException if <code>start()</code> has not been called
     *    at least once before this method was called
     */
    public void stop() {
        if (started) {
            double currentTime = System.nanoTime();
            double elapsedTime = currentTime - startTime;
            summaryStatistics.addValue(elapsedTime);
        } else {
            throw new TimerException("timer was never started");
        }
    }

    /**
     * Return the minimum elapsed time recorded by this timer
     * in nanoseconds.
     *
     * @return the minimum elapsed time recorded by this timer
     *    in nanoseconds
     */
    public double min() {
        return summaryStatistics.getMin();
    }

    /**
     * Return the maximum elapsed time recorded by this timer
     * in nanoseconds.
     *
     * @return the maximum elapsed time recorded by this timer
     *    in nanoseconds
     */
    public double max() {
        return summaryStatistics.getMax();
    }

    /**
     * Return the number of elapsed times recorded by this timer.
     *
     * @return the number of elapsed times recorded by this timer
     */
    public long size() {
        return summaryStatistics.getN();
    }

    /**
     * Return the sum of the elapsed times recorded by this timer
     * in nanoseconds.
     *
     * @return the sum of the elapsed times recorded by this timer
     */
    public double sum() {
        return summaryStatistics.getSum();
    }

    /**
     * Return the arithmetic mean of the elapsed times recorded by this
     * timer in nanoseconds.
     *
     * @return the arithmetic mean of the elapsed times recorded by this
     *    timer in nanoseconds
     */
    public double mean() {
        return summaryStatistics.getMean();
    }

    /**
     * Return the standard deviation of the elapsed times recorded by this
     * timer in nanoseconds.
     *
     * @return the standard deviation of the elapsed times recorded by this
     *    timer in nanoseconds
     */
    public double standardDeviation() {
        return summaryStatistics.getStandardDeviation();
    }

    /**
     * Time the specified code block.
     *
     * @param codeBlock code block to execute
     * @return timer used to measure execution time
     */
    public static Timer time(final Runnable codeBlock) {
        return time(codeBlock, new Timer());
    }

    /**
     * Time the specified code block with the specified timer.
     *
     * @param codeBlock code block to execute
     * @param t timer to use to measure execution time
     * @return timer used to measure execution time
     */
    public static Timer time(final Runnable codeBlock, final Timer t) {
        t.start();
        codeBlock.run();
        t.stop();
        return t;
    }

    /**
     * Prime the just-in-time compiler (JIT) by executing the
     * specified code block <code>n</code> times.
     *
     * @param codeBlock code block to execute
     * @param n number of times to execute code block
     */
    public static void prime(final Runnable codeBlock, final int n) {
        for (int i = 0; i < n; i++) {
            codeBlock.run();
        }
    }

    /**
     * Prime the just-in-time compiler (JIT) by executing each
     * code block in the specified list of code blocks <code>n</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to execute each code block
     */
    public static void prime(final List<? extends Runnable> codeBlocks, final int n) {
        for (Runnable codeBlock : codeBlocks) {
            prime(codeBlock, n);
        }
    }

    /**
     * Loop over the specified code block <code>n</code> times.
     *
     * @param codeBlock code block to execute
     * @param n number of times to execute code block
     * @return timer used to measure execution time
     */
    public static Timer loop(final Runnable codeBlock, final int n) {
        return loop(codeBlock, n, new Timer());
    }

    /**
     * Loop over the specified code block <code>n</code> times
     * with the specified timer.
     *
     * @param codeBlock code block to execute
     * @param n number of times to execute code block
     * @param t timer to use to measure execution time
     * @return timer used to measure execution time
     */
    public static Timer loop(final Runnable codeBlock, final int n, final Timer t) {
        for (int i = 0; i < n; i++) {
            time(codeBlock, t);
        }
        return t;
    }

    /**
     * For each of the code blocks in the specified list of code blocks,
     * loop over the code block <code>n</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to execute each code block
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> loop(final List<? extends Runnable> codeBlocks, final int n) {
        Map<Runnable, Timer> map = new HashMap<Runnable, Timer>(codeBlocks.size());
        for (Runnable codeBlock : codeBlocks) {
            map.put(codeBlock, loop(codeBlock, n));
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * Loop over the code blocks in the specified list of code blocks
     * <code>n</code> times, executing each code block <code>m</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to loop over the list of code blocks
     * @param m number of times to execute each code block
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> loop(final List<? extends Runnable> codeBlocks, final int n, final int m) {
        Map<Runnable, Timer> map = new HashMap<Runnable, Timer>(codeBlocks.size());
        for (int i = 0; i < n; i++) {
            for (Runnable codeBlock : codeBlocks) {
                if (map.containsKey(codeBlock)) {
                    Timer t = map.get(codeBlock);
                    loop(codeBlock, m, t);
                    map.put(codeBlock, t);
                } else {
                    map.put(codeBlock, loop(codeBlock, m));
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * For each of the code blocks in the specified list of code blocks,
     * executed in random order, loop over the code block <code>n</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to execute each code block
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> shuffle(final List<? extends Runnable> codeBlocks, final int n) {
        return shuffle(codeBlocks, n, new Random());
    }

    /**
     * For each of the code blocks in the specified list of code blocks,
     * executed in random order using the specified source of randomness,
     * loop over the code block <code>n</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to execute each code block
     * @param random source of randomness
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> shuffle(final List<? extends Runnable> codeBlocks, final int n, final Random random) {
        List<Runnable> codeBlocksCopy = new ArrayList<Runnable>(codeBlocks);
        Collections.shuffle(codeBlocksCopy, random);
        return loop(codeBlocksCopy, n);
    }

    /**
     * Loop over the code blocks in the specified list of code blocks
     * <code>n</code> times, in random order, executing each code block
     * <code>m</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to loop over the list of code blocks
     * @param m number of times to execute each code block
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> shuffle(final List<? extends Runnable> codeBlocks, final int n, final int m) {
        return shuffle(codeBlocks, n, m, new Random());
    }

    /**
     * Loop over the code blocks in the specified list of code blocks
     * <code>n</code> times, in random order using the specified source of
     * randomness, executing each code block <code>m</code> times.
     *
     * @param codeBlocks list of code blocks to execute
     * @param n number of times to loop over the list of code blocks
     * @param m number of times to execute each code block
     * @param random source of randomness
     * @return map of code blocks to timers used to measure execution time
     */
    public static Map<Runnable, Timer> shuffle(final List<? extends Runnable> codeBlocks, final int n, final int m, final Random random) {
        List<Runnable> codeBlocksCopy = new ArrayList<Runnable>(codeBlocks);
        Map<Runnable, Timer> map = new HashMap<Runnable, Timer>(codeBlocksCopy.size());
        for (int i = 0; i < n; i++) {
            Collections.shuffle(codeBlocksCopy, random);
            for (Runnable codeBlock : codeBlocksCopy) {
                if (map.containsKey(codeBlock)) {
                    Timer t = map.get(codeBlock);
                    loop(codeBlock, m, t);
                    map.put(codeBlock, t);
                } else {
                    map.put(codeBlock, loop(codeBlock, m));
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }
}

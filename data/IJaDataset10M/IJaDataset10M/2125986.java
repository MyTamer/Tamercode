package atnf.atoms.mon;

import java.util.*;
import org.apache.log4j.Logger;
import atnf.atoms.time.*;
import atnf.atoms.mon.util.MonitorConfig;
import atnf.atoms.mon.archiver.PointArchiver;

/**
 * Maintains a buffer of the most recent data updates for each point.
 * 
 * @author David Brodrick
 * @author Le Cuong Nguyen
 */
public class PointBuffer {

    /** Stores the buffers of recently collected data for each point. */
    private static Hashtable<PointDescription, LinkedList<PointData>> theirBufferTable = new Hashtable<PointDescription, LinkedList<PointData>>(1000, 1000);

    /** The maximum number of records to be buffered for a single point. */
    private static int theirMaxBufferSize;

    /** The maximum amount of time to buffer data for a single point. */
    private static RelTime theirMaxBufferAge;

    /** Logger. */
    private static Logger theirLogger = Logger.getLogger(PointBuffer.class.getName());

    /** Static block to parse buffer size parameters. */
    static {
        try {
            theirMaxBufferSize = Integer.parseInt(MonitorConfig.getProperty("MaxBufferSize", "50"));
        } catch (Exception e) {
            theirLogger.warn("Error parsing MaxBufferSize configuration parameter: " + e);
            theirMaxBufferSize = 50;
        }
        int numsecs;
        try {
            numsecs = Integer.parseInt(MonitorConfig.getProperty("MaxBufferAge", "90"));
        } catch (Exception e) {
            theirLogger.warn("Error parsing MaxBufferAge configuration parameter: " + e);
            numsecs = 90;
        }
        theirMaxBufferAge = RelTime.factory(numsecs * 1000000);
    }

    /**
   * Add new data to the buffer for the given point.
   * 
   * @param pm
   *          The point to add the new data for.
   * @param data
   *          The new data for the given point.
   */
    public static void updateData(PointDescription pm, PointData data) {
        if (data != null) {
            LinkedList<PointData> thisbuf = theirBufferTable.get(pm);
            if (thisbuf == null) {
                synchronized (theirBufferTable) {
                    thisbuf = theirBufferTable.get(pm);
                    if (thisbuf == null) {
                        thisbuf = new LinkedList<PointData>();
                        theirBufferTable.put(pm, thisbuf);
                    }
                }
            }
            synchronized (thisbuf) {
                AbsTime agecutoff = AbsTime.factory().add(theirMaxBufferAge.negate());
                while (thisbuf.size() > 0 && (thisbuf.size() > theirMaxBufferSize || thisbuf.getFirst().getTimestamp().isBeforeOrEquals(agecutoff))) {
                    thisbuf.removeFirst();
                }
                thisbuf.add(data);
            }
        }
    }

    /**
   * Return the latest data for the specified point.
   * 
   * @param point
   *          Source/name of the monitor point to retrieve. This is expected to be in the format <tt>source.name</tt>.
   * @return Latest data for the specified point.
   */
    public static PointData getPointData(String point) {
        PointDescription pm = PointDescription.getPoint(point);
        if (pm == null) {
            return null;
        }
        return getPointData(pm);
    }

    /**
   * Return the latest data for the specified point.
   * 
   * @param pm
   *          Monitor point to get the latest data for.
   * @return Latest data for the specified point.
   */
    public static PointData getPointData(PointDescription pm) {
        PointData res = null;
        if (pm != null) {
            LinkedList<PointData> thisbuf = theirBufferTable.get(pm);
            if (thisbuf != null) {
                synchronized (thisbuf) {
                    if (!thisbuf.isEmpty()) {
                        res = thisbuf.getLast();
                    }
                }
            }
        }
        return res;
    }

    /**
   * Return all data in the specified time range for the given point. This will access the memory buffer and/or the data archive on
   * disk in order to gather all data between the specified times. A <tt>sample_rate</tt> parameter can be used to undersample the
   * available data to reduce the amount of data which is returned.
   * 
   * @param pm
   *          The point to get the data for.
   * @param start_time
   *          The earliest time in the range of interest.
   * @param end_time
   *          The most recent time in the range of interest.
   * @param maxsamples
   *          The maximum number of samples to be returned.
   * @return Vector containing all data in the specified time range. <tt>null</tt> will be returned if no data were found.
   */
    public static Vector<PointData> getPointData(PointDescription pm, AbsTime start_time, AbsTime end_time, int maxsamples) {
        if (isAfterOrEqualsFirstData(pm, start_time)) {
            return getPointDataBuffer(pm, start_time, end_time);
        }
        Vector<PointData> bufdata = getPointDataBuffer(pm, start_time, end_time);
        if (bufdata == null) {
            bufdata = new Vector<PointData>();
        }
        PointArchiver arc = PointArchiver.getPointArchiver();
        Vector<PointData> arcdata = arc.extract(pm, start_time, end_time);
        if (arcdata == null) {
            arcdata = new Vector<PointData>();
        }
        boolean mergebuffer = true;
        if (arcdata.size() > 0) {
            if (arcdata.size() >= arc.getMaxNumRecords()) {
                mergebuffer = false;
            }
        }
        if (mergebuffer) {
            int cnt = 0;
            if (bufdata.size() > 0) {
                AbsTime buffer_start = ((PointData) bufdata.firstElement()).getTimestamp();
                while (arcdata.size() > 0 && ((PointData) arcdata.lastElement()).getTimestamp().isAfterOrEquals(buffer_start)) {
                    arcdata.remove(arcdata.lastElement());
                    cnt++;
                }
                arcdata.addAll(bufdata);
            }
        }
        if (arcdata.size() > 0 && maxsamples > 1 && arcdata.size() > maxsamples) {
            AbsTime nextsamp = start_time;
            RelTime increment = RelTime.factory((end_time.getValue() - start_time.getValue()) / maxsamples);
            Vector<PointData> newres = new Vector<PointData>(maxsamples);
            int i = 0;
            while (i < arcdata.size() && nextsamp.isBeforeOrEquals(end_time)) {
                while (i < arcdata.size() && ((PointData) arcdata.get(i)).getTimestamp().isBefore(nextsamp)) {
                    i++;
                }
                if (i >= arcdata.size()) {
                    break;
                }
                newres.add(arcdata.get(i));
                nextsamp = nextsamp.add(increment);
                i++;
            }
            arcdata = newres;
        }
        if (arcdata.size() == 0) {
            arcdata = null;
        }
        return arcdata;
    }

    /**
   * Return all data in the specified time range for the given point. This will access the memory buffer and/or the data archive on
   * disk in order to gather all data between the specified times. A <tt>sample_rate</tt> parameter can be used to undersample the
   * available data to reduce the amount of data which is returned.
   * 
   * @param point
   *          Source and point name to get the data for. This must be in the format <tt>source.pointname</tt>.
   * @param start_time
   *          The earliest time in the range of interest.
   * @param end_time
   *          The most recent time in the range of interest.
   * @param sample_rate
   *          Not yet sure how it works...
   * @return Vector containing all data in the specified time range. <tt>null</tt> will be returned if no data were found or if the
   *         source/point name were invalid.
   */
    public static Vector<PointData> getPointData(String point, AbsTime start, AbsTime end, int sample_rate) {
        PointDescription pm = PointDescription.getPoint(point);
        if (pm == null) {
            return null;
        }
        return getPointData(pm, start, end, sample_rate);
    }

    /**
   * Return the last record who's timestamp is <= the timestamp argument.
   * 
   * @param point
   *          Monitor point to request data for.
   * @param timestamp
   *          Get data before or equalling this time.
   * @return The data, or null if no record was found.
   */
    public static PointData getPreceding(String point, AbsTime timestamp) {
        PointDescription pm = PointDescription.getPoint(point);
        if (pm == null) {
            return null;
        }
        PointData res = null;
        LinkedList<PointData> bufferdata = theirBufferTable.get(pm);
        if (bufferdata != null) {
            synchronized (bufferdata) {
                if (!bufferdata.isEmpty() && bufferdata.getFirst().getTimestamp().isBeforeOrEquals(timestamp)) {
                    Iterator<PointData> i = bufferdata.iterator();
                    res = i.next();
                    while (i.hasNext()) {
                        PointData pd = i.next();
                        if (pd.getTimestamp().isAfter(timestamp)) {
                            break;
                        }
                        res = pd;
                    }
                }
            }
        }
        if (res == null) {
            PointArchiver arc = PointArchiver.getPointArchiver();
            res = arc.getPreceding(pm, timestamp);
        }
        return res;
    }

    /**
   * Return the last record who's timestamp is >= the timestamp argument.
   * 
   * @param point
   *          Monitor point to request data for.
   * @param timestamp
   *          Get data after or equalling this time.
   * @return The data, or null if no record was found.
   */
    public static PointData getFollowing(String point, AbsTime timestamp) {
        PointDescription pm = PointDescription.getPoint(point);
        if (pm == null) {
            return null;
        }
        PointData res = null;
        PointData temp = null;
        LinkedList<PointData> bufferdata = theirBufferTable.get(pm);
        if (bufferdata != null) {
            synchronized (bufferdata) {
                if (!bufferdata.isEmpty()) {
                    if (bufferdata.getFirst().getTimestamp().isBeforeOrEquals(timestamp)) {
                        for (int i = bufferdata.size() - 1; i >= 0; i--) {
                            PointData pd = ((PointData) bufferdata.get(i));
                            if (pd.getTimestamp().isBefore(timestamp)) {
                                break;
                            } else {
                                res = pd;
                            }
                        }
                    } else {
                        temp = bufferdata.getFirst();
                    }
                }
            }
        }
        if (res == null) {
            PointArchiver arc = PointArchiver.getPointArchiver();
            res = arc.getFollowing(pm, timestamp);
            if (res == null) {
                res = temp;
            }
        }
        return res;
    }

    /**
   * Check if the specified timestamp is after or equal to the oldest data in the buffer for the specified point.
   * 
   * @param pm
   *          The point to check.
   * @param time
   *          The timestamp to be checked against the buffer.
   * @return True if the timestamp is equal to or more recent than the oldest buffered data, False otherwise.
   */
    private static boolean isAfterOrEqualsFirstData(PointDescription pm, AbsTime time) {
        boolean res = false;
        LinkedList<PointData> databuffer = theirBufferTable.get(pm);
        if (databuffer != null) {
            synchronized (databuffer) {
                if (!databuffer.isEmpty() && time.isAfterOrEquals(databuffer.getFirst().getTimestamp())) {
                    res = true;
                }
            }
        }
        return res;
    }

    /**
   * Return data for the given point from the memory buffer between the specified times. NOTE: This data is from the memory buffer
   * only, the disk archive is not accessed for this operation.
   * 
   * @param pm
   *          The point to get the data for.
   * @param start_time
   *          The earliest time in the range of interest.
   * @param end_time
   *          The most recent time in the range of interest.
   * @return Vector of buffer data in the given time range. <tt>null</tt> will be returned if no data were found.
   */
    private static Vector<PointData> getPointDataBuffer(PointDescription pm, AbsTime start_time, AbsTime end_time) {
        Vector<PointData> res = null;
        LinkedList<PointData> databuffer = theirBufferTable.get(pm);
        if (databuffer != null) {
            synchronized (databuffer) {
                if (!databuffer.isEmpty()) {
                    res = new Vector<PointData>(databuffer.size());
                    Iterator<PointData> i = databuffer.iterator();
                    while (i.hasNext()) {
                        PointData pd = i.next();
                        AbsTime thistime = pd.getTimestamp();
                        if (thistime.isAfter(end_time)) {
                            break;
                        }
                        if (thistime.isAfterOrEquals(start_time)) {
                            res.add(pd);
                        }
                    }
                    if (res.isEmpty()) {
                        res = null;
                    }
                }
            }
        }
        return res;
    }
}

package org.openscience.cdk.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *  A set of utilities which did not really fit into any other category
 *
 * @author     steinbeck
 * @cdk.githash
 * @cdk.created    2001-06-19
 */
public class SwissArmyKnife {

    public static final boolean debug = false;

    /**
	 *  Returns a string reporting the time passed between startTime and endTime, both given in milliseconds, in hours, minutes, seconds and milliseconds
	 *
	 * @param  startTime  The start time in milliseconds
	 * @param  endTime    The end time in milliseconds
	 * @return            A human readable representation of a timespan given in milliseconds
	 */
    public static String getDuration(long startTime, long endTime) {
        long diff = endTime - startTime;
        return getDuration(diff);
    }

    /**
	 *  Returns a String reporting the time passed during a given number of milliseconds.
	 *
	 * @param  diff  A time span in milliseconds
	 * @return       A human readable representation of a timespan given in milliseconds
	 */
    public static String getDuration(long diff) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(diff));
        StringBuffer s = new StringBuffer();
        if (calendar.get(Calendar.HOUR) > 1) {
            s.append("hours: " + (calendar.get(Calendar.HOUR) - 1) + ", ");
        }
        if (calendar.get(Calendar.MINUTE) > 0) {
            s.append("minutes: " + (calendar.get(Calendar.MINUTE)) + ", ");
        }
        if (calendar.get(Calendar.SECOND) > 0) {
            s.append("seconds: " + (calendar.get(Calendar.SECOND)) + ", ");
        }
        if (calendar.get(Calendar.MILLISECOND) > 1) {
            s.append("milliseconds: " + (calendar.get(Calendar.MILLISECOND)) + ", ");
        }
        s.append("total milliseconds: " + diff);
        return s.toString();
    }

    /**
	 *  Returns a string representation of a 2D int matrix for printing or listing to the console
	 *
	 * @param  contab  The 2D int matrix for which a string representation is to be generatred
	 */
    public static String printInt2D(int[][] contab) {
        String line = "";
        for (int f = 0; f < contab.length; f++) {
            for (int g = 0; g < contab.length; g++) {
                line += contab[f][g] + " ";
            }
            line += "\n";
        }
        return line;
    }

    /**
	 *  Calculates the faculty for a given integer
	 *
	 * @param  i  The int value for which the faculty is to be returned
	 * @return    The faculty of i
	 */
    public static int faculty(int i) {
        if (i > 1) {
            return i * faculty(i - 1);
        }
        return 1;
    }
}

package org.jempbox.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * This class is used to convert dates to strings and back using the PDF
 * date standards.  Date are described in PDFReference1.4 section 3.8.2
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @author <a href="mailto:chris@oezbek.net">Christopher Oezbek</a>
 * 
 * @version $Revision: 1.6 $
 */
public class DateConverter {

    private static final SimpleDateFormat[] POTENTIAL_FORMATS = new SimpleDateFormat[] { new SimpleDateFormat("EEEE, dd MMM yyyy hh:mm:ss a"), new SimpleDateFormat("EEEE, MMM dd, yyyy hh:mm:ss a"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz") };

    private DateConverter() {
    }

    /**
     * This will convert a string to a calendar.
     *
     * @param date The string representation of the calendar.
     *
     * @return The calendar that this string represents.
     *
     * @throws IOException If the date string is not in the correct format.
     */
    public static Calendar toCalendar(String date) throws IOException {
        Calendar retval = null;
        if (date != null && date.trim().length() > 0) {
            int year = 0;
            int month = 1;
            int day = 1;
            int hour = 0;
            int minute = 0;
            int second = 0;
            try {
                SimpleTimeZone zone = null;
                if (date.startsWith("D:")) {
                    date = date.substring(2, date.length());
                }
                date = date.replaceAll("[-:T]", "");
                if (date.length() < 4) {
                    throw new IOException("Error: Invalid date format '" + date + "'");
                }
                year = Integer.parseInt(date.substring(0, 4));
                if (date.length() >= 6) {
                    month = Integer.parseInt(date.substring(4, 6));
                }
                if (date.length() >= 8) {
                    day = Integer.parseInt(date.substring(6, 8));
                }
                if (date.length() >= 10) {
                    hour = Integer.parseInt(date.substring(8, 10));
                }
                if (date.length() >= 12) {
                    minute = Integer.parseInt(date.substring(10, 12));
                }
                if (date.length() >= 14) {
                    second = Integer.parseInt(date.substring(12, 14));
                }
                if (date.length() >= 15) {
                    char sign = date.charAt(14);
                    if (sign == 'Z') {
                        zone = new SimpleTimeZone(0, "Unknown");
                    } else {
                        int hours = 0;
                        int minutes = 0;
                        if (date.length() >= 17) {
                            if (sign == '+') {
                                hours = Integer.parseInt(date.substring(15, 17));
                            } else {
                                hours = -Integer.parseInt(date.substring(14, 16));
                            }
                        }
                        if (sign == '+') {
                            if (date.length() >= 19) {
                                minutes = Integer.parseInt(date.substring(17, 19));
                            }
                        } else {
                            if (date.length() >= 18) {
                                minutes = Integer.parseInt(date.substring(16, 18));
                            }
                        }
                        zone = new SimpleTimeZone(hours * 60 * 60 * 1000 + minutes * 60 * 1000, "Unknown");
                    }
                }
                if (zone == null) {
                    retval = new GregorianCalendar();
                } else {
                    retval = new GregorianCalendar(zone);
                }
                retval.clear();
                retval.set(year, month - 1, day, hour, minute, second);
            } catch (NumberFormatException e) {
                if (date.substring(date.length() - 3, date.length() - 2).equals(":") && (date.substring(date.length() - 6, date.length() - 5).equals("+") || date.substring(date.length() - 6, date.length() - 5).equals("-"))) {
                    date = date.substring(0, date.length() - 3) + date.substring(date.length() - 2);
                }
                for (int i = 0; retval == null && i < POTENTIAL_FORMATS.length; i++) {
                    try {
                        Date utilDate = POTENTIAL_FORMATS[i].parse(date);
                        retval = new GregorianCalendar();
                        retval.setTime(utilDate);
                    } catch (ParseException pe) {
                    }
                }
                if (retval == null) {
                    throw new IOException("Error converting date:" + date);
                }
            }
        }
        return retval;
    }

    private static final void zeroAppend(StringBuffer out, int number) {
        if (number < 10) {
            out.append("0");
        }
        out.append(number);
    }

    /**
     * Convert the date to iso 8601 string format.
     * 
     * @param cal The date to convert.
     * @return The date represented as an ISO 8601 string.
     */
    public static String toISO8601(Calendar cal) {
        StringBuffer retval = new StringBuffer();
        retval.append(cal.get(Calendar.YEAR));
        retval.append("-");
        zeroAppend(retval, cal.get(Calendar.MONTH) + 1);
        retval.append("-");
        zeroAppend(retval, cal.get(Calendar.DAY_OF_MONTH));
        retval.append("T");
        zeroAppend(retval, cal.get(Calendar.HOUR_OF_DAY));
        retval.append(":");
        zeroAppend(retval, cal.get(Calendar.MINUTE));
        retval.append(":");
        zeroAppend(retval, cal.get(Calendar.SECOND));
        int timeZone = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
        if (timeZone < 0) {
            retval.append("-");
        } else {
            retval.append("+");
        }
        timeZone = Math.abs(timeZone);
        int hours = timeZone / 1000 / 60 / 60;
        int minutes = (timeZone - (hours * 1000 * 60 * 60)) / 1000 / 1000;
        if (hours < 10) {
            retval.append("0");
        }
        retval.append(Integer.toString(hours));
        retval.append(":");
        if (minutes < 10) {
            retval.append("0");
        }
        retval.append(Integer.toString(minutes));
        return retval.toString();
    }
}

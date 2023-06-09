package com.google.gwt.i18n.client.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "sv" locale.
 */
public class DateTimeFormatInfoImpl_sv extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "FM", "EM" };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE'en' 'den' d:'e' MMMM y";
    }

    @Override
    public String dateFormatLong() {
        return "d MMMM y";
    }

    @Override
    public String dateFormatMedium() {
        return "d MMM y";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "före Kristus", "efter Kristus" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "f.Kr.", "e.Kr." };
    }

    @Override
    public String formatMonthAbbrevDay() {
        return "d MMM";
    }

    @Override
    public String formatMonthFullDay() {
        return "d:'e' MMMM";
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE d MMMM";
    }

    @Override
    public String formatMonthNumDay() {
        return "d/M";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "MMM y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "d MMM y";
    }

    @Override
    public String formatYearMonthFull() {
        return "MMMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "d MMMM y";
    }

    @Override
    public String formatYearMonthNum() {
        return "yyyy-MM";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "yyyy-MM-dd";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE d MMM y";
    }

    @Override
    public String formatYearQuarterFull() {
        return "yyyy QQQQ";
    }

    @Override
    public String formatYearQuarterShort() {
        return "yyyy Q";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "januari", "februari", "mars", "april", "maj", "juni", "juli", "augusti", "september", "oktober", "november", "december" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "jan", "feb", "mar", "apr", "maj", "jun", "jul", "aug", "sep", "okt", "nov", "dec" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "1:a kvartalet", "2:a kvartalet", "3:e kvartalet", "4:e kvartalet" };
    }

    @Override
    public String[] quartersShort() {
        return new String[] { "K1", "K2", "K3", "K4" };
    }

    @Override
    public String timeFormatFull() {
        return "'kl'. HH:mm:ss zzzz";
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "söndag", "måndag", "tisdag", "onsdag", "torsdag", "fredag", "lördag" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "S", "M", "T", "O", "T", "F", "L" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "sön", "mån", "tis", "ons", "tors", "fre", "lör" };
    }

    @Override
    public String[] weekdaysShortStandalone() {
        return new String[] { "sön", "mån", "tis", "ons", "tor", "fre", "lör" };
    }
}

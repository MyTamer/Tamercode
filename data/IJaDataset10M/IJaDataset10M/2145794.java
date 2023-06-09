package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "gez" locale.
 */
public class DateTimeFormatInfoImpl_gez extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "ጽባሕ", "ምሴት" };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE፥ dd MMMM መዓልት y G";
    }

    @Override
    public String dateFormatLong() {
        return "dd MMMM y";
    }

    @Override
    public String dateFormatMedium() {
        return "dd-MMM-y";
    }

    @Override
    public String dateFormatShort() {
        return "dd/MM/yy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "ዓ/ዓ", "ዓ/ም" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "ዓ/ዓ", "ዓ/ም" };
    }

    @Override
    public String formatMonthFullWeekdayDay() {
        return "EEEE, MMMM d";
    }

    @Override
    public String formatMonthNumDay() {
        return "M/d";
    }

    @Override
    public String formatYearMonthAbbrev() {
        return "MMM y";
    }

    @Override
    public String formatYearMonthAbbrevDay() {
        return "MMM d, y";
    }

    @Override
    public String formatYearMonthFull() {
        return "MMMM y";
    }

    @Override
    public String formatYearMonthFullDay() {
        return "MMMM d, y";
    }

    @Override
    public String formatYearMonthNum() {
        return "M/y";
    }

    @Override
    public String formatYearMonthNumDay() {
        return "M/d/y";
    }

    @Override
    public String formatYearMonthWeekdayDay() {
        return "EEE, MMM d, y";
    }

    @Override
    public String formatYearQuarterFull() {
        return "QQQQ y";
    }

    @Override
    public String formatYearQuarterShort() {
        return "Q y";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "ጠሐረ", "ከተተ", "መገበ", "አኀዘ", "ግንባት", "ሠንየ", "ሐመለ", "ነሐሰ", "ከረመ", "ጠቀመ", "ኀደረ", "ኀሠሠ" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "ጠ", "ከ", "መ", "አ", "ግ", "ሠ", "ሐ", "ነ", "ከ", "ጠ", "ኀ", "ኀ" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "ጠሐረ", "ከተተ", "መገበ", "አኀዘ", "ግንባት", "ሠንየ", "ሐመለ", "ነሐሰ", "ከረመ", "ጠቀመ", "ኀደረ", "ኀሠሠ" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String timeFormatFull() {
        return "h:mm:ss a zzzz";
    }

    @Override
    public String timeFormatLong() {
        return "h:mm:ss a z";
    }

    @Override
    public String timeFormatMedium() {
        return "h:mm:ss a";
    }

    @Override
    public String timeFormatShort() {
        return "h:mm a";
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "እኁድ", "ሰኑይ", "ሠሉስ", "ራብዕ", "ሐሙስ", "ዓርበ", "ቀዳሚት" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "እ", "ሰ", "ሠ", "ራ", "ሐ", "ዓ", "ቀ" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "እኁድ", "ሰኑይ", "ሠሉስ", "ራብዕ", "ሐሙስ", "ዓርበ", "ቀዳሚት" };
    }
}

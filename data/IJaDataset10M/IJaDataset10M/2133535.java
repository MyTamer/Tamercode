package com.google.gwt.i18n.shared.impl.cldr;

/**
 * Implementation of DateTimeFormatInfo for the "hy" locale.
 */
public class DateTimeFormatInfoImpl_hy extends DateTimeFormatInfoImpl {

    @Override
    public String[] ampms() {
        return new String[] { "Առ․", "Կե․" };
    }

    @Override
    public String dateFormatFull() {
        return "EEEE, MMMM d, y";
    }

    @Override
    public String dateFormatLong() {
        return "MMMM dd, y";
    }

    @Override
    public String dateFormatMedium() {
        return "MMM d, y";
    }

    @Override
    public String dateFormatShort() {
        return "MM/dd/yy";
    }

    @Override
    public String[] erasFull() {
        return new String[] { "Մ․Թ․Ա․", "Մ․Թ․" };
    }

    @Override
    public String[] erasShort() {
        return new String[] { "Մ․Թ․Ա․", "Մ․Թ․" };
    }

    @Override
    public String formatYearMonthFullDay() {
        return "MMMM d, y";
    }

    @Override
    public String[] monthsFull() {
        return new String[] { "Հունվար", "Փետրվար", "Մարտ", "Ապրիլ", "Մայիս", "Հունիս", "Հուլիս", "Օգոստոս", "Սեպտեմբեր", "Հոկտեմբեր", "Նոյեմբեր", "Դեկտեմբեր" };
    }

    @Override
    public String[] monthsNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
    }

    @Override
    public String[] monthsShort() {
        return new String[] { "Հնվ", "Փտվ", "Մրտ", "Ապր", "Մյս", "Հնս", "Հլս", "Օգս", "Սեպ", "Հոկ", "Նոյ", "Դեկ" };
    }

    @Override
    public String[] quartersFull() {
        return new String[] { "Q1", "Q2", "Q3", "Q4" };
    }

    @Override
    public String[] weekdaysFull() {
        return new String[] { "Կիրակի", "Երկուշաբթի", "Երեքշաբթի", "Չորեքշաբթի", "Հինգշաբթի", "Ուրբաթ", "Շաբաթ" };
    }

    @Override
    public String[] weekdaysNarrow() {
        return new String[] { "1", "2", "3", "4", "5", "6", "7" };
    }

    @Override
    public String[] weekdaysShort() {
        return new String[] { "Կիր", "Երկ", "Երք", "Չոր", "Հնգ", "Ուր", "Շաբ" };
    }
}

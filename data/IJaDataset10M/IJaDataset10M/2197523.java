package com.google.gwt.user.datepicker.client;

import java.util.Date;

/**
 * The CalendarView is a calendar grid that represents the current view of a
 * {@link DatePicker}. Note, the calendar view only deals with the currently
 * visible dates and all state is flushed when the calendar view is refreshed.
 * 
 */
public abstract class CalendarView extends DatePickerComponent {

    /**
   * Constructor.
   */
    public CalendarView() {
    }

    /**
   * Adds a style name to the cell of the supplied date. This style is only set
   * until the next time the {@link CalendarView} is refreshed.
   * 
   * @param styleName style name to add
   * @param date date that will have the supplied style added
   */
    public abstract void addStyleToDate(String styleName, Date date);

    /**
   * Returns the first date that is currently shown by the calendar.
   * 
   * @return the first date.
   */
    public abstract Date getFirstDate();

    /**
   * Returns the last date that is currently shown by the calendar.
   * 
   * @return the last date.
   */
    public abstract Date getLastDate();

    /**
   * Is the cell representing the given date enabled?
   * 
   * @param date the date
   * @return is the date enabled
   */
    public abstract boolean isDateEnabled(Date date);

    /**
   * Removes a visible style name from the cell of the supplied date.
   * 
   * @param styleName style name to remove
   * @param date date that will have the supplied style added
   */
    public abstract void removeStyleFromDate(String styleName, Date date);

    /**
   * Enables or Disables a particular date. by default all valid dates are
   * enabled after a rendering event. Disabled dates cannot be selected.
   * 
   * @param enabled true for enabled, false for disabled
   * @param date date to enable or disable
   */
    public abstract void setEnabledOnDate(boolean enabled, Date date);

    /**
   * Allows the calendar view to update the date picker's highlighted date.
   * 
   * @param date the highlighted date
   */
    protected final void setHighlightedDate(Date date) {
        getDatePicker().setHighlightedDate(date);
    }
}

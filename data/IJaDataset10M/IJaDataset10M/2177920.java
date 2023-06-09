package org.achartengine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A series for the multiple category charts like the doughnut.
 */
public class CategoryMultiSeries {

    /** The series title. */
    private String mTitle;

    /** The series local keys. */
    private List<String> mCategories = new ArrayList<String>();

    /** The series' individual data labels. */
    private List<List<String>> mTitles = new ArrayList<List<String>>();

    /** The series data values. */
    private List<List<Number>> mValues = new ArrayList<List<Number>>();

    /**
   * Builds a new category series.
   * @param title the series title
   */
    public CategoryMultiSeries(String title) {
        mTitle = title;
    }

    /**
   * Adds a new value to the series
   * @param titles the titles to be used as labels
   * @param values the new value
   */
    public void add(List<String> titles, List<Number> values) {
        add(mCategories.size() + "", titles, values);
    }

    /**
   * Adds a new value to the series.
   * @param category the category name
   * @param titles the titles to be used as labels
   * @param values the new value
   */
    public void add(String category, List<String> titles, List<Number> values) {
        mCategories.add(category);
        mTitles.add(titles);
        mValues.add(values);
    }

    /**
   * Removes an existing value from the series.
   * @param index the index in the series of the value to remove
   */
    public void remove(int index) {
        mCategories.remove(index);
        mTitles.remove(index);
        mValues.remove(index);
    }

    /**
   * Removes all the existing values from the series.
   */
    public void clear() {
        mCategories.clear();
        mTitles.clear();
        mValues.clear();
    }

    /**
   * Returns the values at the specified index.
   * @param index the index
   * @return the value at the index
   */
    public List<Number> getValues(int index) {
        return mValues.get(index);
    }

    /**
   * Returns the category name at the specified index.
   * @param index the index
   * @return the category name at the index
   */
    public String getCategory(int index) {
        return mCategories.get(index);
    }

    /**
   * Returns the categories count.
   * @return the categories count
   */
    public int getCategoriesCount() {
        return mCategories.size();
    }

    /**
   * Returns the series item count.
   * @param index the index
   * @return the series item count
   */
    public int getItemCount(int index) {
        return mValues.get(index).size();
    }

    /**
   * Returns the series titles.
   * @param index the index
   * @return the series titles
   */
    public List<String> getTitles(int index) {
        return mTitles.get(index);
    }

    /**
   * Transforms the category series to an XY series.
   * @return the XY series
   */
    public XYSeries toXYSeries() {
        XYSeries xySeries = new XYSeries(mTitle);
        return xySeries;
    }
}

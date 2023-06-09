package net.sf.mzmine.data;

import net.sf.mzmine.util.Range;

/**
 * 
 */
public interface PeakList {

    /**
     * @return Short descriptive name for the peak list
     */
    public String getName();

    /**
     * Change the name of this peak list
     */
    public void setName(String name);

    /**
     * Returns number of raw data files participating in the peak list
     */
    public int getNumberOfRawDataFiles();

    /**
     * Returns all raw data files participating in the peak list
     */
    public RawDataFile[] getRawDataFiles();

    /**
     * Returns true if this peak list contains given file
     */
    public boolean hasRawDataFile(RawDataFile file);

    /**
     * Returns a raw data file
     * 
     * @param position
     *            Position of the raw data file in the matrix (running numbering
     *            from left 0,1,2,...)
     */
    public RawDataFile getRawDataFile(int position);

    /**
     * Returns number of rows in the alignment result
     */
    public int getNumberOfRows();

    /**
     * Returns the peak of a given raw data file on a give row of the peak list
     * 
     * @param row
     *            Row of the peak list
     * @param rawDataFile
     *            Raw data file where the peak is detected/estimated
     */
    public ChromatographicPeak getPeak(int row, RawDataFile rawDataFile);

    /**
     * Returns all peaks for a raw data file
     */
    public ChromatographicPeak[] getPeaks(RawDataFile rawDataFile);

    /**
     * Returns all peaks on one row
     */
    public PeakListRow getRow(int row);

    /**
     * Returns all peak list rows
     */
    public PeakListRow[] getRows();

    /**
     * Returns all rows with average retention time within given range
     * 
     * @param startRT
     *            Start of the retention time range
     * @param endRT
     *            End of the retention time range
     */
    public PeakListRow[] getRowsInsideScanRange(Range rtRange);

    /**
     * Returns all rows with average m/z within given range
     * 
     * @param startMZ
     *            Start of the m/z range
     * @param endMZ
     *            End of the m/z range
     */
    public PeakListRow[] getRowsInsideMZRange(Range mzRange);

    /**
     * Returns all rows with average m/z and retention time within given range
     * 
     * @param startRT
     *            Start of the retention time range
     * @param endRT
     *            End of the retention time range
     * @param startMZ
     *            Start of the m/z range
     * @param endMZ
     *            End of the m/z range
     */
    public PeakListRow[] getRowsInsideScanAndMZRange(Range rtRange, Range mzRange);

    /**
     * Returns all peaks overlapping with a retention time range
     * 
     * @param startRT
     *            Start of the retention time range
     * @param endRT
     *            End of the retention time range
     */
    public ChromatographicPeak[] getPeaksInsideScanRange(RawDataFile file, Range rtRange);

    /**
     * Returns all peaks in a given m/z range
     * 
     * @param startMZ
     *            Start of the m/z range
     * @param endMZ
     *            End of the m/z range
     */
    public ChromatographicPeak[] getPeaksInsideMZRange(RawDataFile file, Range mzRange);

    /**
     * Returns all peaks in a given m/z & retention time ranges
     * 
     * @param startRT
     *            Start of the retention time range
     * @param endRT
     *            End of the retention time range
     * @param startMZ
     *            Start of the m/z range
     * @param endMZ
     *            End of the m/z range
     */
    public ChromatographicPeak[] getPeaksInsideScanAndMZRange(RawDataFile file, Range rtRange, Range mzRange);

    /**
     * Returns maximum raw data point intensity among all peaks in this peak
     * list
     * 
     * @return Maximum intensity
     */
    public double getDataPointMaxIntensity();

    /**
     * Add a new row to the peak list
     */
    public void addRow(PeakListRow row);

    /**
     * Removes a row from this peak list
     * 
     */
    public void removeRow(int row);

    /**
     * Removes a row from this peak list
     * 
     */
    public void removeRow(PeakListRow row);

    /**
     * Returns a row number of given peak
     */
    public int getPeakRowNum(ChromatographicPeak peak);

    /**
     * Returns a row containing given peak
     */
    public PeakListRow getPeakRow(ChromatographicPeak peak);

    public void addDescriptionOfAppliedTask(PeakListAppliedMethod appliedMethod);

    /**
     * Returns all tasks (descriptions) applied to this peak list
     */
    public PeakListAppliedMethod[] getAppliedMethods();

    /**
     * Returns the whole m/z range of the peak list
     */
    public Range getRowsMZRange();

    /**
     * Returns the whole retention time range of the peak list
     */
    public Range getRowsRTRange();
}

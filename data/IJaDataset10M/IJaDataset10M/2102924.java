package no.uib.jsparklines.renderers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import no.uib.jsparklines.data.XYDataPoint;
import no.uib.jsparklines.renderers.util.GradientColorCoding;
import no.uib.jsparklines.renderers.util.GradientColorCoding.ColorGradient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.IntervalBarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.DefaultIntervalCategoryDataset;

/**
 * A renderer for displaying a JSparklines interval chart inside a table cell.
 * Assumes that the cell values are of type Integer, Short, Byte, Long,
 * Double, Float, XYDataPoint or XYDataPoint[]. If data of XYDataPoint is used 
 * the X value is assumed to be the lower range of the interval and the Y values 
 * is assumed to be the upper range. For the other cell value types the width 
 * of the interval has to be set by the user.
 *
 * @author Harald Barsnes
 */
public class JSparklinesIntervalChartTableCellRenderer extends JPanel implements TableCellRenderer {

    /**
     * Turns of the gradient painting for the interval charts.
     */
    static {
        IntervalBarRenderer.setDefaultBarPainter(new StandardBarPainter());
    }

    /**
     * The horizontal alignment of the label when showing number and chart.
     */
    private int labelHorizontalAlignement = SwingConstants.RIGHT;

    /**
     * The minimum value to display as a chart. Values smaller than this lower
     * limit are shown as this minimum value when shown as a chart. This to make
     * sure that the chart is visible at all.
     */
    private double minimumChartValue = 0.05;

    /**
     * Used to decide how many decimals to include in the tooltip. If the number
     * is smaller than the lower limit, 8 decimnals are shown, otherwise only
     * 2 decimals are used.
     */
    private double tooltipLowerValue = 0.01;

    /**
     * The chart panel to be displayed.
     */
    private ChartPanel chartPanel;

    /**
     * The chart to display.
     */
    private JFreeChart chart;

    /**
     * The label used to display the number and the interval chart at the same
     * time.
     */
    private JLabel valueLabel;

    /**
     * The maximum value. Used to set the maximum range for the chart.
     */
    private double maxValue = 1;

    /**
     * The minimum value. Used to set the minmum range for the chart.
     */
    private double minValue = 0;

    /**
     * If true the underlying numbers are shown instead of the charts.
     */
    private boolean showNumbers = false;

    /**
     * If true the value of the chart is shown next to the interval chart.
     */
    private boolean showNumberAndChart = false;

    /**
     * The width of the label used to display the value and the chart in the
     * same time.
     */
    private int widthOfValueLabel = 40;

    /**
     * The background color used for the plots. For plots using light
     * colors, it's recommended to use a dark background color, and for
     * plots using darker colors it is recommended to use a light background.
     */
    private Color plotBackgroundColor = null;

    /**
     * The colors to use for the intervals with negative numbers.
     */
    private Color negativeValuesColor = new Color(51, 51, 255);

    /**
     * The colors to use for the intervals with positive numbers.
     */
    private Color positiveValuesColor = new Color(255, 51, 51);

    /**
     * If true a red/green gradient coloring is used.
     */
    private boolean gradientColoring = false;

    /**
     * The currently selected color gradient.
     */
    private ColorGradient currentColorGradient = ColorGradient.RedBlackBlue;

    /**
     * The width of the interval used to highlight the value.
     */
    private double widthOfInterval;

    /**
     * If true XYDataPoint cell value are required, due to the constructor used.
     */
    private boolean xyDataPointRequied = false;

    /**
     * If true, a black reference line is added in the center of the plot.
     */
    private boolean showReferenceLine = false;

    /**
     * The reference line width.
     */
    private double referenceLineWidth = 0.03;

    /**
     * The reference line color.
     */
    private Color referenceLineColor = Color.BLACK;

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. Use this constructor when only positive
     * values are to be plotted. This constructor uses default colors for the intervals. If you want to
     * set your own colors, use one of the other constructors.
     *
     * @param plotOrientation       the orientation of the plot
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     *                              (this is the same as setting the minimum value to 0)
     * @param widthOfInterval       the width of the interval used to highlight the value
     */
    public JSparklinesIntervalChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, Double widthOfInterval) {
        this.maxValue = maxValue;
        this.widthOfInterval = widthOfInterval;
        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. Use this constructor when only positive
     * values are to be plotted.
     *
     * @param plotOrientation       the orientation of the plot
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     *                              (this is the same as setting the minimum value to 0)
     * @param widthOfInterval       the width of the interval used to highlight the value
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     */
    public JSparklinesIntervalChartTableCellRenderer(PlotOrientation plotOrientation, Double maxValue, Double widthOfInterval, Color positiveValuesColor) {
        this(plotOrientation, 0.0, maxValue, widthOfInterval, positiveValuesColor, positiveValuesColor);
    }

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. Use this constructor when positive
     * and negative values are to be plotted. This constructor uses default colors for the bars.
     * If you want to set your own colors, use one of the other constructors.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     * @param widthOfInterval       the width of the interval used to highlight the value
     */
    public JSparklinesIntervalChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue, Double widthOfInterval) {
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.widthOfInterval = widthOfInterval;
        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. Use this constructor when positive
     * and negative values are to be plotted.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     * @param widthOfInterval       the width of the interval used to highlight the value
     * @param negativeValuesColor   the color to use for the negative values if two sided data is shown
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     */
    public JSparklinesIntervalChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue, Double widthOfInterval, Color negativeValuesColor, Color positiveValuesColor) {
        this.positiveValuesColor = positiveValuesColor;
        this.negativeValuesColor = negativeValuesColor;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.widthOfInterval = widthOfInterval;
        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Creates a new JSparklinesIntervalChartTableCellRenderer. To use this constructor 
     * the cells must contain XYDataPoint values, where X represents the lower range and 
     * Y the upper range of the interval to be shown.
     *
     * @param plotOrientation       the orientation of the plot
     * @param minValue              the minium value to be plotted, used to make sure that all plots
     *                              in the same column has the same minmum value and are thus comparable
     * @param maxValue              the maximum value to be plotted, used to make sure that all plots
     *                              in the same column has the same maxium value and are thus comparable
     * @param negativeValuesColor   the color to use for the negative values if two sided data is shown
     * @param positiveValuesColor   the color to use for the positive values if two sided data is shown,
     *                              and the color used for one sided data
     */
    public JSparklinesIntervalChartTableCellRenderer(PlotOrientation plotOrientation, Double minValue, Double maxValue, Color negativeValuesColor, Color positiveValuesColor) {
        this.positiveValuesColor = positiveValuesColor;
        this.negativeValuesColor = negativeValuesColor;
        this.maxValue = maxValue;
        this.minValue = minValue;
        xyDataPointRequied = true;
        setUpRendererAndChart(plotOrientation);
    }

    /**
     * Sets up the table cell renderer and the interval chart.
     *
     * @param plotOrientation
     */
    private void setUpRendererAndChart(PlotOrientation plotOrientation) {
        setName("Table.cellRenderer");
        setLayout(new BorderLayout());
        valueLabel = new JLabel("");
        valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
        valueLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getSize() - 2f));
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        chart = ChartFactory.createBarChart(null, null, null, dataset, plotOrientation, false, false, false);
        this.chartPanel = new ChartPanel(chart);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.add(valueLabel);
        add(chartPanel);
    }

    /**
     * If true, a black reference line is shown in the middle of the plot.
     * 
     * @param showReferenceLine if true, a black reference line is shown in the middle of the plot
     */
    public void showReferenceLine(boolean showReferenceLine) {
        this.showReferenceLine = showReferenceLine;
    }

    /**
     * If true, a black reference line is shown in the middle of the plot.
     * 
     * @param showReferenceLine if true, a black reference line is shown in the middle of the plot
     * @param lineWidth the line width
     * @param color the color
     */
    public void showReferenceLine(boolean showReferenceLine, double lineWidth, Color color) {
        this.showReferenceLine = showReferenceLine;
        this.referenceLineWidth = lineWidth;
        this.referenceLineColor = color;
    }

    /**
     * Set the color gradient to use for the intervals. To disable the color gradient
     * use null as the paramater.
     * <br><br>
     * Values below zero uses the first color in the gradient name, while values
     * above zero uses the third color in the gradient.
     * <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     *
     * @param colorGradient the color gradient to use, null disables the color gradient
     */
    public void setGradientColoring(ColorGradient colorGradient) {
        setGradientColoring(colorGradient, null);
    }

    /**
     * Set the color gradient to use for the intervals. To disable the color gradient
     * use null as the paramater.
     * <br><br>
     * Values below zero uses the first color in the gradient name, while values
     * above zero uses the third color in the gradient.
     * <br><br>
     * Note that the max value is set to the maximum absolute value of the max
     * and min values in order to make the color gradient equal on both sides.
     *
     * @param colorGradient         the color gradient to use, null disables the color gradient
     * @param plotBackgroundColor   the background color to use, for gradients using white
     *                              as the "middle" color, it's recommended to use a dark
     *                              background color
     */
    public void setGradientColoring(ColorGradient colorGradient, Color plotBackgroundColor) {
        this.gradientColoring = (colorGradient != null);
        this.plotBackgroundColor = plotBackgroundColor;
        this.currentColorGradient = colorGradient;
        if (gradientColoring) {
            if (Math.abs(minValue) > maxValue) {
                maxValue = Math.abs(minValue);
            }
        }
    }

    /**
     * Set the plot background color.
     *
     * @param plotBackgroundColor
     */
    public void setBackgroundColor(Color plotBackgroundColor) {
        this.plotBackgroundColor = plotBackgroundColor;
    }

    /**
     * If true the number will be shown together with the interval chart in the cell.
     * False only display the interval chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart    if true the number and the chart is shown in the cell
     * @param widthOfLabel          the width used to display the label containing the number
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
    }

    /**
     * If true the number will be shown together with the interval chart in the cell.
     * False only display the interval chart. This method is not to be confused with
     * the showNumbers-method that only displays the numbers.
     *
     * @param showNumberAndChart    if true the number and the chart is shown in the cell
     * @param widthOfLabel          the width used to display the label containing the number
     * @param font                  the font to use for the label
     * @param horizontalAlignement  the horizontal alignent of the text in the label:
     *                              one of the following constants defined in SwingConstants:
     *                              LEFT, CENTER, RIGHT, LEADING or TRAILING.
     */
    public void showNumberAndChart(boolean showNumberAndChart, int widthOfLabel, Font font, int horizontalAlignement) {
        this.showNumberAndChart = showNumberAndChart;
        this.widthOfValueLabel = widthOfLabel;
        labelHorizontalAlignement = horizontalAlignement;
        valueLabel.setFont(font);
    }

    /**
     * Set the maximum value.
     *
     * @param maxValue the maximum value
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Set the minimum value.
     *
     * @param minValue the minimum value
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * Set if the underlying numbers or the interval charts are to be shown.
     *
     * @param showNumbers if true the underlying numbers are shown
     */
    public void showNumbers(boolean showNumbers) {
        this.showNumbers = showNumbers;
    }

    /**
     * Sets up the cell renderer for the given component.
     *
     * @param table
     * @param value
     * @param isSelected
     * @param hasFocus
     * @param row
     * @param column
     * @return the rendered cell
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JComponent c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value == null) {
            Color bg = c.getBackground();
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }
        if (xyDataPointRequied) {
            if (!(value instanceof XYDataPoint) && !(value instanceof XYDataPoint[])) {
                throw new IllegalArgumentException("Inconsistent use of constructor! " + "The constructor used requires that the cells contain values of type XYDataPoint or XYDataPoint[]. Type found: " + value.getClass());
            }
        }
        if (showNumbers) {
            if (value instanceof Double || value instanceof Float) {
                if (value instanceof Float) {
                    value = ((Float) value).doubleValue();
                }
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, roundDouble(((Double) value).doubleValue(), 2), isSelected, hasFocus, row, column);
                if (Math.abs(new Double("" + value)) < tooltipLowerValue) {
                    c.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
                }
            } else if (value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Short) {
                if (value instanceof Short) {
                    value = ((Short) value).intValue();
                } else if (value instanceof Long) {
                    value = ((Long) value).intValue();
                } else if (value instanceof Short) {
                    value = ((Short) value).intValue();
                }
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, (Integer) value, isSelected, hasFocus, row, column);
            } else if (value instanceof XYDataPoint) {
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, "[" + roundDouble(((XYDataPoint) value).getX(), 2) + ", " + roundDouble(((XYDataPoint) value).getY(), 2) + "]", isSelected, hasFocus, row, column);
            } else if (value instanceof XYDataPoint[]) {
                String temp = "";
                XYDataPoint[] tempValues = (XYDataPoint[]) value;
                for (int i = 0; i < tempValues.length; i++) {
                    temp += "[" + roundDouble(tempValues[i].getX(), 2) + ", " + roundDouble(tempValues[i].getY(), 2) + "] ";
                }
                c = (JComponent) new DefaultTableCellRenderer().getTableCellRendererComponent(table, temp, isSelected, hasFocus, row, column);
            }
            ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
            Color bg = c.getBackground();
            c.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            return c;
        }
        if (value instanceof Double || value instanceof Float) {
            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }
            if (Math.abs(new Double("" + value)) < tooltipLowerValue) {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 8));
            } else {
                this.setToolTipText("" + roundDouble(new Double("" + value).doubleValue(), 2));
            }
        } else if (value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Short) {
            this.setToolTipText("" + value);
        } else if (value instanceof XYDataPoint) {
            double x = ((XYDataPoint) value).getX();
            double y = ((XYDataPoint) value).getY();
            if (Math.floor(x) == x && Math.floor(y) == y) {
                this.setToolTipText("[" + (int) x + ", " + (int) y + "]");
            } else {
                this.setToolTipText("[" + roundDouble(x, 2) + ", " + roundDouble(y, 2) + "]");
            }
        } else if (value instanceof XYDataPoint[]) {
            String temp = "<html>";
            XYDataPoint[] tempValues = (XYDataPoint[]) value;
            for (int i = 0; i < tempValues.length; i++) {
                double x = tempValues[i].getX();
                double y = tempValues[i].getY();
                if (Math.floor(x) == x && Math.floor(y) == y) {
                    temp += "[" + (int) x + ", " + (int) y + "]<br>";
                } else {
                    temp += "[" + roundDouble(x, 2) + ", " + roundDouble(y, 2) + "]<br>";
                }
            }
            temp += "</html>";
            this.setToolTipText(temp);
        }
        if (showNumberAndChart) {
            DecimalFormat numberFormat = new DecimalFormat("0.00");
            numberFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
            if (value instanceof Double || value instanceof Float) {
                double temp = Double.valueOf("" + value);
                if (temp == -1) {
                    valueLabel.setText("N/A");
                } else {
                    valueLabel.setText(numberFormat.format(temp));
                }
            } else if (value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Short) {
                valueLabel.setText("" + Integer.valueOf("" + value).intValue());
            } else if (value instanceof XYDataPoint) {
                valueLabel.setText("[" + roundDouble(((XYDataPoint) value).getX(), 2) + ", " + roundDouble(((XYDataPoint) value).getY(), 2) + "]");
            } else if (value instanceof XYDataPoint[]) {
                String temp = "";
                XYDataPoint[] tempValues = (XYDataPoint[]) value;
                for (int i = 0; i < tempValues.length; i++) {
                    temp += "[" + roundDouble(tempValues[i].getX(), 2) + ", " + roundDouble(tempValues[i].getY(), 2) + "] ";
                }
                valueLabel.setText(temp);
            }
            Color bg = c.getBackground();
            valueLabel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            if (labelHorizontalAlignement == SwingConstants.RIGHT) {
                valueLabel.setText(valueLabel.getText() + "  ");
            } else if (labelHorizontalAlignement == SwingConstants.LEFT) {
                valueLabel.setText("  " + valueLabel.getText());
            }
            valueLabel.setForeground(c.getForeground());
            valueLabel.setHorizontalAlignment(labelHorizontalAlignement);
            valueLabel.setMinimumSize(new Dimension(widthOfValueLabel, 0));
            valueLabel.setSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setMaximumSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setPreferredSize(new Dimension(widthOfValueLabel, valueLabel.getPreferredSize().height));
            valueLabel.setVisible(true);
        } else {
            valueLabel.setMinimumSize(new Dimension(0, 0));
            valueLabel.setSize(0, 0);
            valueLabel.setVisible(false);
        }
        setBorder(c.getBorder());
        setOpaque(c.isOpaque());
        setBackground(c.getBackground());
        DefaultIntervalCategoryDataset dataset = null;
        if (value instanceof Double || value instanceof Float) {
            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }
            if (((Double) value).doubleValue() < minimumChartValue && ((Double) value).doubleValue() > 0) {
                value = minimumChartValue;
            }
            if (((Double) value).doubleValue() == -1) {
                double[][] lows = { { 0 } };
                double[][] highs = { { 0 } };
                dataset = new DefaultIntervalCategoryDataset(lows, highs);
            } else {
                double[][] lows = { { ((Double) value) - (widthOfInterval / 2) } };
                double[][] highs = { { ((Double) value + (widthOfInterval / 2)) } };
                dataset = new DefaultIntervalCategoryDataset(lows, highs);
            }
        } else if (value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Short) {
            if (value instanceof Integer) {
                value = ((Integer) value).intValue();
            } else if (value instanceof Long) {
                value = ((Long) value).intValue();
            } else if (value instanceof Short) {
                value = ((Short) value).intValue();
            }
            if (((Integer) value).intValue() < minimumChartValue && ((Integer) value).intValue() > 0) {
                value = Double.valueOf(minimumChartValue).intValue();
            }
            double[][] lows = { { ((Integer) value) - (widthOfInterval / 2) } };
            double[][] highs = { { ((Integer) value + (widthOfInterval / 2)) } };
            dataset = new DefaultIntervalCategoryDataset(lows, highs);
        } else if (value instanceof XYDataPoint) {
            double[][] lows = { { ((XYDataPoint) value).getX() } };
            double[][] highs = { { ((XYDataPoint) value).getY() } };
            if (((XYDataPoint) value).getX() >= ((XYDataPoint) value).getY()) {
                throw new IllegalArgumentException("Lower interval range >= upper interval range! " + ((XYDataPoint) value).getX() + ">=" + ((XYDataPoint) value).getY());
            }
            dataset = new DefaultIntervalCategoryDataset(lows, highs);
        } else if (value instanceof XYDataPoint[]) {
            XYDataPoint[] values = (XYDataPoint[]) value;
            double[][] lows = new double[values.length][1];
            double[][] highs = new double[values.length][1];
            for (int i = 0; i < values.length; i++) {
                lows[i][0] = values[i].getX();
                highs[i][0] = values[i].getY();
                if (lows[i][0] >= highs[i][0]) {
                    throw new IllegalArgumentException("Lower interval range >= upper interval range! " + lows[i][0] + ">=" + highs[i][0]);
                }
            }
            dataset = new DefaultIntervalCategoryDataset(lows, highs);
        }
        CategoryPlot plot = chart.getCategoryPlot();
        plot.getRangeAxis().setRange(minValue, maxValue);
        plot.setDataset(dataset);
        plot.setOutlineVisible(false);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setRangeGridlinesVisible(false);
        IntervalBarRenderer renderer = new IntervalBarRenderer();
        renderer.setShadowVisible(false);
        if (value instanceof Double || value instanceof Float) {
            if (value instanceof Float) {
                value = ((Float) value).doubleValue();
            }
            if (gradientColoring) {
                renderer.setSeriesPaint(0, GradientColorCoding.findGradientColor((Double) value, minValue, maxValue, currentColorGradient));
            } else {
                if ((Double) value >= 0) {
                    renderer.setSeriesPaint(0, positiveValuesColor);
                } else {
                    renderer.setSeriesPaint(0, negativeValuesColor);
                }
            }
        } else if (value instanceof Integer || value instanceof Short || value instanceof Long || value instanceof Short) {
            if (value instanceof Integer) {
                value = ((Integer) value).intValue();
            } else if (value instanceof Long) {
                value = ((Long) value).intValue();
            } else if (value instanceof Short) {
                value = ((Short) value).intValue();
            }
            if (gradientColoring) {
                renderer.setSeriesPaint(0, GradientColorCoding.findGradientColor(((Integer) value).doubleValue(), minValue, maxValue, currentColorGradient));
            } else {
                if ((Integer) value >= 0) {
                    renderer.setSeriesPaint(0, positiveValuesColor);
                } else {
                    renderer.setSeriesPaint(0, negativeValuesColor);
                }
            }
        } else if (value instanceof XYDataPoint) {
            double temp = ((XYDataPoint) value).getX() + ((XYDataPoint) value).getY();
            temp /= 2;
            if (gradientColoring) {
                renderer.setSeriesPaint(0, GradientColorCoding.findGradientColor(temp, minValue, maxValue, currentColorGradient));
            } else {
                if (temp >= 0) {
                    renderer.setSeriesPaint(0, positiveValuesColor);
                } else {
                    renderer.setSeriesPaint(0, negativeValuesColor);
                }
            }
        } else if (value instanceof XYDataPoint[]) {
            XYDataPoint[] values = (XYDataPoint[]) value;
            for (int i = 0; i < values.length; i++) {
                double temp = values[i].getX() + values[i].getY();
                temp /= 2;
                if (gradientColoring) {
                    renderer.setSeriesPaint(i, GradientColorCoding.findGradientColor(temp, minValue, maxValue, currentColorGradient));
                } else {
                    if (temp >= 0) {
                        renderer.setSeriesPaint(i, positiveValuesColor);
                    } else {
                        renderer.setSeriesPaint(i, negativeValuesColor);
                    }
                }
            }
        }
        if (showReferenceLine) {
            DefaultCategoryDataset referenceLineDataset = new DefaultCategoryDataset();
            referenceLineDataset.addValue(maxValue, "A", "B");
            plot.setDataset(1, referenceLineDataset);
            LayeredBarRenderer referenceLineRenderer = new LayeredBarRenderer();
            referenceLineRenderer.setSeriesBarWidth(0, referenceLineWidth);
            referenceLineRenderer.setSeriesFillPaint(0, referenceLineColor);
            referenceLineRenderer.setSeriesPaint(0, referenceLineColor);
            plot.setRenderer(1, referenceLineRenderer);
        }
        if (plotBackgroundColor != null && !isSelected) {
            plot.setBackgroundPaint(plotBackgroundColor);
            chartPanel.setBackground(plotBackgroundColor);
            chart.setBackgroundPaint(plotBackgroundColor);
        } else {
            Color bg = c.getBackground();
            plot.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chartPanel.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            chart.setBackgroundPaint(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
            this.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue()));
        }
        plot.setRenderer(renderer);
        return this;
    }

    /**
     * Rounds of a double value to the wanted number of decimalplaces
     *
     * @param d the double to round of
     * @param places number of decimal places wanted
     * @return double - the new double
     */
    private static double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10, (double) places);
    }

    /**
     * Returns the minimum chart value to plot.
     *
     * @return the minimumChartValue
     */
    public double getMinimumChartValue() {
        return minimumChartValue;
    }

    /**
     * Set the minimum chart value to plot.
     *
     * @param minimumChartValue the minimumChartValue to set
     */
    public void setMinimumChartValue(double minimumChartValue) {
        this.minimumChartValue = minimumChartValue;
    }

    /**
     * Returns the lower value before using 8 decimals for the tooltip.
     *
     * @return the tooltipLowerValue
     */
    public double getTooltipLowerValue() {
        return tooltipLowerValue;
    }

    /**
     * Set the lower limit for the values before using 8 decimals for the tooltip.
     *
     * @param tooltipLowerValue the tooltipLowerValue to set
     */
    public void setTooltipLowerValue(double tooltipLowerValue) {
        this.tooltipLowerValue = tooltipLowerValue;
    }

    /**
     * Set the color used for the negative values.
     *
     * @param negativeValuesColor the color used for the negative values
     */
    public void setNegativeValuesColor(Color negativeValuesColor) {
        this.negativeValuesColor = negativeValuesColor;
    }

    /**
     * Set the color used for the positive values.
     *
     * @param positiveValuesColor the color used for the positive values
     */
    public void setPositiveValuesColor(Color positiveValuesColor) {
        this.positiveValuesColor = positiveValuesColor;
    }
}

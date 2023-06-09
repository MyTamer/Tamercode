package net.sourceforge.eclipsetrader.ta_lib.indicators;

import net.sourceforge.eclipsetrader.charts.IndicatorPlugin;
import net.sourceforge.eclipsetrader.charts.IndicatorPluginPreferencePage;
import net.sourceforge.eclipsetrader.charts.PlotLine;
import net.sourceforge.eclipsetrader.charts.Settings;
import net.sourceforge.eclipsetrader.core.db.BarData;
import net.sourceforge.eclipsetrader.ta_lib.Factory;
import net.sourceforge.eclipsetrader.ta_lib.TALibPlugin;
import net.sourceforge.eclipsetrader.ta_lib.internal.TALibIndicatorPlugin;
import net.sourceforge.eclipsetrader.ta_lib.internal.TALibIndicatorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import com.tictactec.ta.lib.MInteger;

public class ADX extends Factory {

    public static final String DEFAULT_LABEL = Messages.ADX_DefaultLabel;

    public static final int DEFAULT_LINETYPE = PlotLine.LINE;

    public static final RGB DEFAULT_COLOR = new RGB(0, 0, 192);

    public static final int DEFAULT_PERIOD = 14;

    public ADX() {
    }

    public IndicatorPlugin createIndicator() {
        IndicatorPlugin indicator = new TALibIndicatorPlugin() {

            private String label = DEFAULT_LABEL;

            private int lineType = DEFAULT_LINETYPE;

            private Color color = new Color(null, DEFAULT_COLOR);

            private int period = DEFAULT_PERIOD;

            public void calculate() {
                int startIdx = 0;
                int endIdx = getBarData().size() - 1;
                Object[] values = getInput(getBarData());
                double[] inHigh = (double[]) values[BarData.HIGH];
                double[] inLow = (double[]) values[BarData.LOW];
                double[] inClose = (double[]) values[BarData.CLOSE];
                MInteger outBegIdx = new MInteger();
                MInteger outNbElement = new MInteger();
                double[] outReal = getOutputArray(getBarData(), TALibPlugin.getCore().adxLookback(period));
                TALibPlugin.getCore().adx(startIdx, endIdx, inHigh, inLow, inClose, period, outBegIdx, outNbElement, outReal);
                PlotLine line = new PlotLine();
                for (int i = 0; i < outNbElement.value; i++) line.append(outReal[i]);
                line.setLabel(label);
                line.setType(lineType);
                line.setColor(color);
                getOutput().add(line);
                getOutput().setScaleFlag(true);
            }

            public void setParameters(Settings settings) {
                label = settings.getString("label", label);
                lineType = settings.getInteger("lineType", lineType).intValue();
                color = settings.getColor("color", color);
                period = settings.getInteger("period", period).intValue();
            }
        };
        return indicator;
    }

    public IndicatorPluginPreferencePage createPreferencePage() {
        IndicatorPluginPreferencePage page = new TALibIndicatorPreferencePage() {

            public void createControl(Composite parent) {
                Composite content = new Composite(parent, SWT.NONE);
                GridLayout gridLayout = new GridLayout(2, false);
                gridLayout.marginWidth = gridLayout.marginHeight = 0;
                content.setLayout(gridLayout);
                content.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
                setControl(content);
                addColorSelector(content, "color", Messages.ADX_Color, DEFAULT_COLOR);
                addLabelField(content, "label", Messages.ADX_Label, DEFAULT_LABEL);
                addLineTypeSelector(content, "lineType", Messages.ADX_LineType, DEFAULT_LINETYPE);
                addIntegerValueSelector(content, "period", Messages.ADX_Period, 1, 9999, DEFAULT_PERIOD);
            }
        };
        return page;
    }
}

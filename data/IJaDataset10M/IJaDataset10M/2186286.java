package org.eclipsetrader.ui.charts.indicators;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.swt.graphics.RGB;
import org.eclipsetrader.core.charts.IDataSeries;
import org.eclipsetrader.core.charts.NumericDataSeries;
import org.eclipsetrader.ui.charts.IChartIndicator;
import org.eclipsetrader.ui.charts.IChartParameters;
import org.eclipsetrader.ui.charts.OHLCField;
import org.eclipsetrader.ui.charts.RenderStyle;
import org.eclipsetrader.ui.internal.charts.Util;
import org.eclipsetrader.ui.internal.charts.indicators.Activator;
import org.eclipsetrader.ui.internal.charts.indicators.AdaptableWrapper;
import org.eclipsetrader.ui.internal.charts.indicators.SingleLineElement;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;

public class RSI implements IChartIndicator, IExecutableExtension {

    private String id;

    private String name;

    private OHLCField field = OHLCField.Close;

    private int period = 7;

    public RSI() {
    }

    public RSI(int period, OHLCField field) {
        this.period = period;
        this.field = field;
    }

    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        id = config.getAttribute("id");
        name = config.getAttribute("name");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OHLCField getField() {
        return field;
    }

    public void setField(OHLCField field) {
        this.field = field;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public IAdaptable computeElement(IAdaptable source, IChartParameters parameters) {
        IDataSeries dataSeries = (IDataSeries) source.getAdapter(IDataSeries.class);
        if (dataSeries != null) {
            OHLCField field = parameters.hasParameter("field") ? OHLCField.getFromName(parameters.getString("field")) : OHLCField.Close;
            int period = parameters.getInteger("period");
            RenderStyle style = parameters.hasParameter("style") ? RenderStyle.getStyleFromName(parameters.getString("style")) : RenderStyle.Line;
            RGB color = parameters.getColor("color");
            IAdaptable[] values = dataSeries.getValues();
            Core core = Activator.getDefault() != null ? Activator.getDefault().getCore() : new Core();
            int startIdx = 0;
            int endIdx = values.length - 1;
            double[] inReal = Util.getValuesForField(values, field);
            MInteger outBegIdx = new MInteger();
            MInteger outNbElement = new MInteger();
            double[] outReal = new double[values.length - core.rsiLookback(period)];
            core.rsi(startIdx, endIdx, inReal, period, outBegIdx, outNbElement, outReal);
            NumericDataSeries result = new NumericDataSeries(getName(), outReal, dataSeries);
            result.setHighest(new AdaptableWrapper(100));
            result.setLowest(new AdaptableWrapper(0));
            return new SingleLineElement(result, style, color);
        }
        return null;
    }
}

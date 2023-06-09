package org.encog.app.analyst.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.encog.app.analyst.AnalystError;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.csv.basic.BasicFile;
import org.encog.app.analyst.csv.basic.LoadedRow;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.app.quant.QuantError;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.util.csv.CSVFormat;
import org.encog.util.csv.ReadCSV;

/**
 * Used by the analyst to evaluate a CSV file.
 * 
 */
public class AnalystEvaluateRawCSV extends BasicFile {

    /**
	 * The analyst file to use.
	 */
    private EncogAnalyst analyst;

    /**
	 * The input count.
	 */
    private int inputCount;

    /**
	 * The output count.
	 */
    private int outputCount;

    /**
	 * The ideal count.
	 */
    private int idealCount;

    /**
	 * Analyze the data. This counts the records and prepares the data to be
	 * processed.
	 * @param theAnalyst The analyst to use.
	 * @param inputFile The input file.
	 * @param headers True if headers are present.
	 * @param format The format the file is in.
	 */
    public final void analyze(final EncogAnalyst theAnalyst, final File inputFile, final boolean headers, final CSVFormat format) {
        this.setInputFilename(inputFile);
        setExpectInputHeaders(headers);
        setInputFormat(format);
        this.analyst = theAnalyst;
        setAnalyzed(true);
        performBasicCounts();
        this.inputCount = this.analyst.determineInputCount();
        this.outputCount = this.analyst.determineOutputCount();
        this.idealCount = getInputHeadings().length - this.inputCount;
        if ((getInputHeadings().length != this.inputCount) && (getInputHeadings().length != (this.inputCount + this.outputCount))) {
            throw new AnalystError("Invalid number of columns(" + getInputHeadings().length + "), must match input(" + this.inputCount + ") count or input+output(" + (this.inputCount + this.outputCount) + ") count.");
        }
    }

    /**
	 * Prepare the output file, write headers if needed.
	 * 
	 * @param outputFile
	 *            The name of the output file.
	 * @param method
	 * @return The output stream for the text file.
	 */
    private PrintWriter analystPrepareOutputFile(final File outputFile) {
        try {
            final PrintWriter tw = new PrintWriter(new FileWriter(outputFile));
            if (isProduceOutputHeaders()) {
                final StringBuilder line = new StringBuilder();
                for (final AnalystField field : this.analyst.getScript().getNormalize().getNormalizedFields()) {
                    if (field.isInput()) {
                        field.addRawHeadings(line, null, getOutputFormat());
                    }
                }
                if (this.idealCount > 0) {
                    for (final AnalystField field : this.analyst.getScript().getNormalize().getNormalizedFields()) {
                        if (field.isOutput()) {
                            field.addRawHeadings(line, "ideal:", getOutputFormat());
                        }
                    }
                }
                for (final AnalystField field : this.analyst.getScript().getNormalize().getNormalizedFields()) {
                    if (field.isOutput()) {
                        field.addRawHeadings(line, "output:", getOutputFormat());
                    }
                }
                tw.println(line.toString());
            }
            return tw;
        } catch (final IOException e) {
            throw new QuantError(e);
        }
    }

    /**
	 * Process the file.
	 * @param outputFile The output file.
	 * @param method The method to use.
	 */
    public final void process(final File outputFile, final MLRegression method) {
        final ReadCSV csv = new ReadCSV(getInputFilename().toString(), isExpectInputHeaders(), getInputFormat());
        if (method.getInputCount() != this.inputCount) {
            throw new AnalystError("This machine learning method has " + method.getInputCount() + " inputs, however, the data has " + this.inputCount + " inputs.");
        }
        MLData output = null;
        final MLData input = new BasicMLData(method.getInputCount());
        final PrintWriter tw = analystPrepareOutputFile(outputFile);
        resetStatus();
        while (csv.next()) {
            updateStatus(false);
            final LoadedRow row = new LoadedRow(csv, this.idealCount);
            int dataIndex = 0;
            for (int i = 0; i < this.inputCount; i++) {
                final String str = row.getData()[i];
                final double d = getInputFormat().parse(str);
                input.setData(i, d);
                dataIndex++;
            }
            dataIndex += this.idealCount;
            output = method.compute(input);
            for (int i = 0; i < this.outputCount; i++) {
                final double d = output.getData(i);
                row.getData()[dataIndex++] = getInputFormat().format(d, getPrecision());
            }
            writeRow(tw, row);
        }
        reportDone(false);
        tw.close();
        csv.close();
    }
}

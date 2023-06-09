package repast.simphony.data2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * DataSink that writes data to a file in tabular format.
 * 
 * @author Nick Collier
 */
public class FileDataSink implements DataSink {

    private Formatter formatter;

    private File file;

    private BufferedWriter writer;

    private String name;

    public FileDataSink(String name, File file, Formatter formatter) {
        this.formatter = formatter;
        this.file = file;
        this.name = name;
    }

    /**
   * Gets the file this FileDataSink will write to.
   * 
   * @return the file this FileDataSink will write to.
   */
    public File getFile() {
        return file;
    }

    /**
   * Gets the name of this FileDataSink.
   * 
   * @return the name of this FileDataSink.
   */
    public String getName() {
        return name;
    }

    /**
   * Gets the formatter used to format the data written using this FileDataSink.
   * 
   * @return the formatter used to format the data written using this
   *         FileDataSink.
   */
    public Formatter getFormatter() {
        return formatter;
    }

    /**
   * Gets the format type for this FileDataSink.
   * 
   * @return the format type for this FileDataSink.
   */
    public FormatType getFormat() {
        return this.formatter.getFormatType();
    }

    @Override
    public void open(List<String> sourceIds) {
        try {
            writer = new BufferedWriter(new FileWriter(file));
            String header = formatter.getHeader();
            if (header.length() > 0) {
                writer.write(formatter.getHeader());
                writer.newLine();
            }
        } catch (IOException ex) {
            throw new DataException("Error opening FileDataSink.", ex);
        }
    }

    @Override
    public void flush() {
        try {
            if (writer != null) {
                writer.flush();
            }
        } catch (IOException ex) {
            throw new DataException("Error while flushing FileDataSink.", ex);
        }
    }

    @Override
    public void rowStarted() {
        formatter.clear();
    }

    @Override
    public void append(String key, Object value) {
        formatter.addData(key, value);
    }

    @Override
    public void rowEnded() {
        try {
            writer.write(formatter.formatData());
            writer.newLine();
        } catch (IOException ex) {
            throw new DataException("Error writing to FileDataSink '" + file.getName() + "'", ex);
        }
    }

    @Override
    public void recordEnded() {
    }

    @Override
    public void close() {
        try {
            writer.flush();
        } catch (IOException ex) {
            throw new DataException("Error closing FileDataSink.", ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }
}

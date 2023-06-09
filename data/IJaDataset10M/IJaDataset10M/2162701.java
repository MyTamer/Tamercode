package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Implementation of FTPFileEntryParser and FTPFileListParser for NT Systems.
 *
 * @author  <a href="Winston.Ojeda@qg.com">Winston Ojeda</a>
 * @author <a href="mailto:scohen@apache.org">Steve Cohen</a>
 * @version $Id: NTFTPEntryParser.java 658518 2008-05-21 01:04:30Z sebb $
 * @see org.apache.commons.net.ftp.FTPFileEntryParser FTPFileEntryParser (for usage instructions)
 */
public class NTFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {

    private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy hh:mma";

    /**
     * this is the regular expression used by this parser.
     */
    private static final String REGEX = "(\\S+)\\s+(\\S+)\\s+" + "(?:(<DIR>)|([0-9]+))\\s+" + "(\\S.*)";

    /**
     * The sole constructor for an NTFTPEntryParser object.
     *
     * @exception IllegalArgumentException
     * Thrown if the regular expression is unparseable.  Should not be seen
     * under normal conditions.  It it is seen, this is a sign that
     * <code>REGEX</code> is  not a valid regular expression.
     */
    public NTFTPEntryParser() {
        this(null);
    }

    /**
     * This constructor allows the creation of an NTFTPEntryParser object 
     * with something other than the default configuration.
     *
     * @param config The {@link FTPClientConfig configuration} object used to 
     * configure this parser.
     * @exception IllegalArgumentException
     * Thrown if the regular expression is unparseable.  Should not be seen
     * under normal conditions.  It it is seen, this is a sign that
     * <code>REGEX</code> is  not a valid regular expression.
     * @since 1.4
     */
    public NTFTPEntryParser(FTPClientConfig config) {
        super(REGEX);
        configure(config);
    }

    /**
     * Parses a line of an NT FTP server file listing and converts it into a
     * usable format in the form of an <code> FTPFile </code> instance.  If the
     * file listing line doesn't describe a file, <code> null </code> is
     * returned, otherwise a <code> FTPFile </code> instance representing the
     * files in the directory is returned.
     * <p>
     * @param entry A line of text from the file listing
     * @return An FTPFile instance corresponding to the supplied entry
     */
    public FTPFile parseFTPEntry(String entry) {
        FTPFile f = new FTPFile();
        f.setRawListing(entry);
        if (matches(entry)) {
            String datestr = group(1) + " " + group(2);
            String dirString = group(3);
            String size = group(4);
            String name = group(5);
            try {
                f.setTimestamp(super.parseTimestamp(datestr));
            } catch (ParseException e) {
            }
            if (null == name || name.equals(".") || name.equals("..")) {
                return (null);
            }
            f.setName(name);
            if ("<DIR>".equals(dirString)) {
                f.setType(FTPFile.DIRECTORY_TYPE);
                f.setSize(0);
            } else {
                f.setType(FTPFile.FILE_TYPE);
                if (null != size) {
                    f.setSize(Long.parseLong(size));
                }
            }
            return (f);
        }
        return null;
    }

    /**
     * Defines a default configuration to be used when this class is
     * instantiated without a {@link  FTPClientConfig  FTPClientConfig}
     * parameter being specified.
     * @return the default configuration for this parser.
     */
    @Override
    public FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_NT, DEFAULT_DATE_FORMAT, null, null, null, null);
    }
}

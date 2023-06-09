package org.springframework.richclient.settings.xml;

import org.springframework.richclient.settings.Settings;
import org.springframework.richclient.settings.SettingsException;
import org.springframework.richclient.settings.SettingsFactory;

/**
 * <code>SettingsFactory</code> for creating <code>XmlSettings</code>.
 * 
 * @author Peter De Bruycker
 */
public class XmlSettingsFactory implements SettingsFactory {

    private String location;

    private XmlSettingsReaderWriter readerWriter;

    /**
	 * Returns the <code>XmlSettingsReaderWriter</code> used for persisting
	 * the xml to the backing store. If no <code>XmlSettingsReaderWriter</code>
	 * was set, the default (<code>FileSystemXmlSettingsReaderWriter</code>)
	 * will be used.
	 * 
	 * @return the <code>XmlSettingsReaderWriter</code>
	 */
    public XmlSettingsReaderWriter getReaderWriter() {
        if (readerWriter == null) {
            readerWriter = new FileSystemXmlSettingsReaderWriter(getLocation());
        }
        return readerWriter;
    }

    /**
	 * Sets the <code>XmlSettingsReaderWriter</code> to use. If set to
	 * <code>null</code>, the default (<code>FileSystemXmlSettingsReaderWriter</code>)
	 * will be used.
	 * 
	 * @param readerWriter
	 *            the <code>XmlSettingsReaderWriter</code>
	 */
    public void setReaderWriter(XmlSettingsReaderWriter readerWriter) {
        this.readerWriter = readerWriter;
    }

    public Settings createSettings(String key) throws SettingsException {
        return getReaderWriter().read(key);
    }

    /**
	 * Returns the location for the xml files. 
	 * 
	 * @return the location
	 */
    public String getLocation() {
        if (location == null) {
            location = "settings";
        }
        return location;
    }

    /**
	 * Sets the location of the xml files.
	 * 
	 * @param location
	 *            the location
	 */
    public void setLocation(String location) {
        this.location = location;
    }
}

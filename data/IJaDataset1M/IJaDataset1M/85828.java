package de.jollyday.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.joda.time.DateTimeConstants;
import de.jollyday.HolidayType;
import de.jollyday.config.Configuration;
import de.jollyday.config.Month;
import de.jollyday.config.ObjectFactory;
import de.jollyday.config.Weekday;
import de.jollyday.holidaytype.LocalizedHolidayType;

public class XMLUtil {

    /**
	 * the package name to search for the generated java classes.
	 */
    public static final String PACKAGE = "de.jollyday.config";

    private static Logger LOG = Logger.getLogger(XMLUtil.class.getName());

    /**
	 * Unmarshalls the configuration from the stream. Uses <code>JAXB</code> for
	 * this.
	 *
	 * @param stream a {@link java.io.InputStream} object.
	 * @return The unmarshalled configuration.
	 * @throws java.io.IOException Could not close the provided stream.
	 */
    public static Configuration unmarshallConfiguration(InputStream stream) throws IOException {
        if (stream == null) {
            throw new IllegalArgumentException("Stream is NULL. Cannot read XML.");
        }
        try {
            JAXBContext ctx = null;
            try {
                ctx = createJAXBContext(Thread.currentThread().getContextClassLoader());
            } catch (Exception e) {
                LOG.warning("Could not create JAXB context using the current threads context classloader. Defaulting to ObjectFactory classloader.");
                ctx = null;
            }
            if (ctx == null) {
                ctx = createJAXBContext(ObjectFactory.class.getClassLoader());
            }
            Unmarshaller um = ctx.createUnmarshaller();
            @SuppressWarnings("unchecked") JAXBElement<Configuration> el = (JAXBElement<Configuration>) um.unmarshal(stream);
            return el.getValue();
        } catch (Exception ue) {
            throw new IllegalStateException("Cannot parse holidays XML file.", ue);
        } finally {
            stream.close();
        }
    }

    /**
	 * Loads the JAXB context using the provided classloader.
	 * @param classLoader The classloader to use
	 * @return JAXBContext
	 * @throws JAXBException
	 */
    private static JAXBContext createJAXBContext(ClassLoader classLoader) throws JAXBException {
        return JAXBContext.newInstance(XMLUtil.PACKAGE, classLoader);
    }

    /**
	 * Returns the <code>DateTimeConstants</code> value for the given weekday.
	 *
	 * @param weekday a {@link de.jollyday.config.Weekday} object.
	 * @return DateTimeConstants value.
	 */
    public static final int getWeekday(Weekday weekday) {
        switch(weekday) {
            case MONDAY:
                return DateTimeConstants.MONDAY;
            case TUESDAY:
                return DateTimeConstants.TUESDAY;
            case WEDNESDAY:
                return DateTimeConstants.WEDNESDAY;
            case THURSDAY:
                return DateTimeConstants.THURSDAY;
            case FRIDAY:
                return DateTimeConstants.FRIDAY;
            case SATURDAY:
                return DateTimeConstants.SATURDAY;
            case SUNDAY:
                return DateTimeConstants.SUNDAY;
            default:
                throw new IllegalArgumentException("Unknown weekday " + weekday);
        }
    }

    /**
	 * Returns the <code>DateTimeConstants</code> value for the given month.
	 *
	 * @param month a {@link de.jollyday.config.Month} object.
	 * @return DateTimeConstants value.
	 */
    public static int getMonth(Month month) {
        switch(month) {
            case JANUARY:
                return DateTimeConstants.JANUARY;
            case FEBRUARY:
                return DateTimeConstants.FEBRUARY;
            case MARCH:
                return DateTimeConstants.MARCH;
            case APRIL:
                return DateTimeConstants.APRIL;
            case MAY:
                return DateTimeConstants.MAY;
            case JUNE:
                return DateTimeConstants.JUNE;
            case JULY:
                return DateTimeConstants.JULY;
            case AUGUST:
                return DateTimeConstants.AUGUST;
            case SEPTEMBER:
                return DateTimeConstants.SEPTEMBER;
            case OCTOBER:
                return DateTimeConstants.OCTOBER;
            case NOVEMBER:
                return DateTimeConstants.NOVEMBER;
            case DECEMBER:
                return DateTimeConstants.DECEMBER;
            default:
                throw new IllegalArgumentException("Unknown month " + month);
        }
    }

    /**
	 * Gets the type.
	 *
	 * @param type
	 *            the type of holiday in the config
	 * @return the type of holiday
	 */
    public static HolidayType getType(de.jollyday.config.HolidayType type) {
        switch(type) {
            case OFFICIAL_HOLIDAY:
                return LocalizedHolidayType.OFFICIAL_HOLIDAY;
            case UNOFFICIAL_HOLIDAY:
                return LocalizedHolidayType.UNOFFICIAL_HOLIDAY;
            default:
                throw new IllegalArgumentException("Unknown type " + type);
        }
    }
}

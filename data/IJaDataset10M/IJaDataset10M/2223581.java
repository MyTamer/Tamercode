package org.eclipse.emf.ecore.xml.type.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.xml.type.InvalidDatatypeValueException;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;

/**
 * An internal extension of Java's XMLGregorianCalendar to represent
 * <a href="http://www.w3.org/TR/2001/REC-xmlschema-2-20010502/">W3C XML Schema 1.0</a> 
 * dateTime, time, date, gYearMonth,  gYear, gMonthDay, gDay, gMonth data types.
 * <p> 
 * NOTE: this class is for internal use only. 
 */
public final class XMLCalendar extends XMLGregorianCalendar implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final short DATETIME = 0;

    public static final short TIME = 1;

    public static final short DATE = 2;

    public static final short GYEARMONTH = 3;

    public static final short GYEAR = 4;

    public static final short GMONTHDAY = 5;

    public static final short GDAY = 6;

    public static final short GMONTH = 7;

    protected static final String[] XML_SCHEMA_TYPES = { "dateTime", "time", "date", "gYearMonth", "gYear", "gMonthDay", "gDay", "gMonth" };

    public static final int EQUALS = 0;

    public static final int LESS_THAN = -1;

    public static final int GREATER_THAN = 1;

    public static final int INDETERMINATE = 2;

    short dataType;

    private XMLGregorianCalendar xmlGregorianCalendar;

    private Date date;

    static final DatatypeFactory datatypeFactory;

    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    protected static final DateFormat[] EDATE_FORMATS = { new SafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"), new SafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), new SafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S"), new SafeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.'S'Z'"), new SafeSimpleDateFormat("yyyy-MM-dd'Z'"), new SafeSimpleDateFormat("yyyy-MM-dd") };

    static {
        EDATE_FORMATS[0].setTimeZone(TimeZone.getTimeZone("GMT"));
        EDATE_FORMATS[3].setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private static final boolean FIX_GMONTH_PARSE;

    private static final boolean FIX_GMONTH_PRINT;

    static {
        XMLGregorianCalendar test = null;
        try {
            test = datatypeFactory.newXMLGregorianCalendar("--12");
        } catch (Exception e) {
        }
        FIX_GMONTH_PARSE = test == null;
        FIX_GMONTH_PRINT = test == null || test.toString().length() > 4;
    }

    private XMLCalendar(XMLGregorianCalendar xmlGregorianCalendar, Date date, short dataType) {
        this.xmlGregorianCalendar = xmlGregorianCalendar;
        this.date = date;
        this.dataType = dataType;
    }

    public XMLCalendar(String value, short datatype) {
        value = XMLTypeUtil.normalize(value, true);
        if (value.length() == 0) {
            throw new InvalidDatatypeValueException("Incomplete value");
        }
        if (datatype < 0 || datatype > GMONTH) {
            throw new IllegalArgumentException("Illegal datatype value " + datatype);
        }
        if (datatype == GMONTH && FIX_GMONTH_PARSE) {
            if (value.length() < 6 || value.charAt(4) != '-' || value.charAt(5) != '-') {
                StringBuilder v = new StringBuilder(value);
                v.insert(4, "--");
                value = v.toString();
            }
        }
        this.date = null;
        this.dataType = datatype;
        this.xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(value);
    }

    public XMLCalendar(Date date, short dataType) {
        this.xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(XMLCalendar.EDATE_FORMATS[0].format(date));
        this.dataType = dataType;
        this.date = date;
    }

    public static int compare(XMLCalendar value1, XMLCalendar value2) {
        switch(value1.xmlGregorianCalendar.compare(value2.xmlGregorianCalendar)) {
            case DatatypeConstants.EQUAL:
                {
                    return XMLCalendar.EQUALS;
                }
            case DatatypeConstants.LESSER:
                {
                    return XMLCalendar.LESS_THAN;
                }
            case DatatypeConstants.GREATER:
                {
                    return XMLCalendar.GREATER_THAN;
                }
            default:
                {
                    return XMLCalendar.INDETERMINATE;
                }
        }
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof XMLCalendar ? xmlGregorianCalendar.equals(((XMLCalendar) object).xmlGregorianCalendar) : object instanceof XMLGregorianCalendar && xmlGregorianCalendar.equals(object);
    }

    @Override
    public int hashCode() {
        return xmlGregorianCalendar.hashCode();
    }

    @Override
    public String toString() {
        return toXMLFormat();
    }

    public Date getDate() {
        if (date == null) {
            try {
                if (dataType == XMLCalendar.DATETIME) {
                    try {
                        date = XMLCalendar.EDATE_FORMATS[0].parse(toXMLFormat());
                    } catch (Exception e) {
                        try {
                            date = XMLCalendar.EDATE_FORMATS[1].parse(toXMLFormat());
                        } catch (Exception e2) {
                            try {
                                date = XMLCalendar.EDATE_FORMATS[2].parse(toXMLFormat());
                            } catch (Exception e3) {
                                date = XMLCalendar.EDATE_FORMATS[3].parse(toXMLFormat());
                            }
                        }
                    }
                } else if (dataType == XMLCalendar.DATE) {
                    try {
                        date = XMLCalendar.EDATE_FORMATS[4].parse(toXMLFormat());
                    } catch (Exception e) {
                        date = XMLCalendar.EDATE_FORMATS[5].parse(toXMLFormat());
                    }
                }
            } catch (Exception e) {
                throw new WrappedException(e);
            }
        }
        return date;
    }

    private static class SafeSimpleDateFormat extends SimpleDateFormat {

        private static final long serialVersionUID = 1L;

        public SafeSimpleDateFormat(String pattern) {
            super(pattern, Locale.ENGLISH);
        }

        @Override
        public synchronized Date parse(String source) throws ParseException {
            return super.parse(source);
        }
    }

    @Override
    public void add(Duration duration) {
        xmlGregorianCalendar.add(duration);
        date = null;
    }

    @Override
    public void clear() {
        xmlGregorianCalendar.clear();
        date = null;
    }

    @Override
    public Object clone() {
        return new XMLCalendar(xmlGregorianCalendar, date, dataType);
    }

    @Override
    public int compare(XMLGregorianCalendar xmlGregorianCalendar) {
        return this.xmlGregorianCalendar.compare(xmlGregorianCalendar instanceof XMLCalendar ? ((XMLCalendar) xmlGregorianCalendar).xmlGregorianCalendar : xmlGregorianCalendar);
    }

    @Override
    public int getDay() {
        return xmlGregorianCalendar.getDay();
    }

    @Override
    public BigInteger getEon() {
        return xmlGregorianCalendar.getEon();
    }

    @Override
    public BigInteger getEonAndYear() {
        return xmlGregorianCalendar.getEonAndYear();
    }

    @Override
    public BigDecimal getFractionalSecond() {
        return xmlGregorianCalendar.getFractionalSecond();
    }

    @Override
    public int getHour() {
        return xmlGregorianCalendar.getHour();
    }

    @Override
    public int getMinute() {
        return xmlGregorianCalendar.getMinute();
    }

    @Override
    public int getMonth() {
        return xmlGregorianCalendar.getMonth();
    }

    @Override
    public int getSecond() {
        return xmlGregorianCalendar.getSecond();
    }

    @Override
    public TimeZone getTimeZone(int defaultTimeZone) {
        return xmlGregorianCalendar.getTimeZone(defaultTimeZone);
    }

    @Override
    public int getTimezone() {
        return xmlGregorianCalendar.getTimezone();
    }

    @Override
    public QName getXMLSchemaType() {
        return xmlGregorianCalendar.getXMLSchemaType();
    }

    @Override
    public int getYear() {
        return xmlGregorianCalendar.getYear();
    }

    @Override
    public boolean isValid() {
        return xmlGregorianCalendar.isValid();
    }

    @Override
    public XMLGregorianCalendar normalize() {
        return xmlGregorianCalendar.normalize();
    }

    @Override
    public void reset() {
        date = null;
        xmlGregorianCalendar.reset();
    }

    @Override
    public void setDay(int day) {
        xmlGregorianCalendar.setDay(day);
    }

    @Override
    public void setFractionalSecond(BigDecimal fractionalSecond) {
        xmlGregorianCalendar.setFractionalSecond(fractionalSecond);
    }

    @Override
    public void setHour(int hour) {
        xmlGregorianCalendar.setHour(hour);
    }

    @Override
    public void setMillisecond(int millisecond) {
        xmlGregorianCalendar.setMillisecond(millisecond);
    }

    @Override
    public void setMinute(int minute) {
        xmlGregorianCalendar.setMinute(minute);
    }

    @Override
    public void setMonth(int month) {
        xmlGregorianCalendar.setMonth(month);
    }

    @Override
    public void setSecond(int second) {
        xmlGregorianCalendar.setSecond(second);
    }

    @Override
    public void setTimezone(int offset) {
        xmlGregorianCalendar.setTimezone(offset);
    }

    @Override
    public void setYear(BigInteger year) {
        xmlGregorianCalendar.setYear(year);
    }

    @Override
    public void setYear(int year) {
        xmlGregorianCalendar.setYear(year);
    }

    @Override
    public GregorianCalendar toGregorianCalendar() {
        return xmlGregorianCalendar.toGregorianCalendar();
    }

    @Override
    public GregorianCalendar toGregorianCalendar(TimeZone timeZone, Locale locale, XMLGregorianCalendar defaults) {
        return xmlGregorianCalendar.toGregorianCalendar(timeZone, locale, defaults);
    }

    @Override
    public String toXMLFormat() {
        if (dataType == GMONTH && FIX_GMONTH_PRINT) {
            String value = xmlGregorianCalendar.toXMLFormat();
            if (value.length() > 5 && value.charAt(4) == '-' && value.charAt(5) == '-') {
                StringBuilder v = new StringBuilder(value);
                v.delete(4, 6);
                value = v.toString();
            }
            return value;
        }
        return xmlGregorianCalendar.toXMLFormat();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeShort(dataType);
        out.writeUTF(toString());
        out.writeObject(date);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        dataType = in.readShort();
        xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(in.readUTF());
        date = (Date) in.readObject();
    }
}

package org.xmlvm.ios;

import java.util.*;
import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public class CFGregorianDate {

    public int year;

    public byte month;

    public byte day;

    public byte hour;

    public byte minute;

    public double second;

    /** Default constructor */
    CFGregorianDate() {
    }

    /**
	 * Boolean CFGregorianDateIsValid(CFGregorianDate gdate, CFOptionFlags unitFlags);
	 */
    public byte isValid(long unitFlags) {
        throw new RuntimeException("Stub");
    }

    /**
	 * CFAbsoluteTime CFGregorianDateGetAbsoluteTime(CFGregorianDate gdate, CFTimeZoneRef tz);
	 */
    public double getAbsoluteTime(NSTimeZone tz) {
        throw new RuntimeException("Stub");
    }
}

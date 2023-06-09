package org.joda.time.chrono.hebrew;

import org.joda.time.Days;
import org.joda.time.DurationFieldType;
import org.joda.time.Hours;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadablePeriod;

public class MoladPeriod implements ReadablePeriod {

    private final Days d;

    private final Hours h;

    private final Parts p;

    public MoladPeriod(Days d, Hours h, Parts p) {
        this.d = d;
        this.h = h;
        this.p = p;
    }

    public PeriodType getPeriodType() {
        return new PartsPeriodType();
    }

    public int size() {
        return 3;
    }

    public DurationFieldType getFieldType(int index) {
        switch(index) {
            case 0:
                return DurationFieldType.days();
            case 1:
                return DurationFieldType.hours();
            case 2:
                return PartsDurationFieldType.parts();
            default:
                throw new IllegalArgumentException("Unknown fieldtype of index " + index);
        }
    }

    public int getValue(int index) {
        switch(index) {
            case 0:
                return d.getDays();
            case 1:
                return h.getHours();
            case 2:
                return p.getParts();
            default:
                throw new IllegalArgumentException("Unknown value of index " + index);
        }
    }

    public int get(DurationFieldType field) {
        if (DurationFieldType.days().equals(field)) {
            return d.getDays();
        } else if (DurationFieldType.hours().equals(field)) {
            return h.getHours();
        } else if (PartsDurationFieldType.parts().equals(field)) {
            return p.getParts();
        } else {
            throw new IllegalArgumentException("Unknown value of type " + field);
        }
    }

    public boolean isSupported(DurationFieldType field) {
        return DurationFieldType.days().equals(field) || DurationFieldType.hours().equals(field) || PartsDurationFieldType.parts().equals(field);
    }

    public Period toPeriod() {
        return null;
    }

    public MutablePeriod toMutablePeriod() {
        return null;
    }
}

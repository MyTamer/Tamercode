package sun.management;

import java.lang.management.MemoryUsage;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.io.InvalidObjectException;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.OpenDataException;
import com.sun.management.GcInfo;

/**
 * A CompositeData for GcInfo for the local management support.
 * This class avoids the performance penalty paid to the
 * construction of a CompositeData use in the local case.
 */
public class GcInfoCompositeData extends LazyCompositeData {

    private final GcInfo info;

    private final GcInfoBuilder builder;

    private final Object[] gcExtItemValues;

    public GcInfoCompositeData(GcInfo info, GcInfoBuilder builder, Object[] gcExtItemValues) {
        this.info = info;
        this.builder = builder;
        this.gcExtItemValues = gcExtItemValues;
    }

    public GcInfo getGcInfo() {
        return info;
    }

    protected CompositeData getCompositeData() {
        final Object[] baseGcInfoItemValues;
        try {
            baseGcInfoItemValues = new Object[] { new Long(info.getId()), new Long(info.getStartTime()), new Long(info.getEndTime()), new Long(info.getDuration()), memoryUsageMapType.toOpenTypeData(info.getMemoryUsageBeforeGc()), memoryUsageMapType.toOpenTypeData(info.getMemoryUsageAfterGc()) };
        } catch (OpenDataException e) {
            throw new AssertionError(e);
        }
        final int gcExtItemCount = builder.getGcExtItemCount();
        if (gcExtItemCount == 0 && gcExtItemValues != null && gcExtItemValues.length != 0) {
            throw new AssertionError("Unexpected Gc Extension Item Values");
        }
        if (gcExtItemCount > 0 && (gcExtItemValues == null || gcExtItemCount != gcExtItemValues.length)) {
            throw new AssertionError("Unmatched Gc Extension Item Values");
        }
        Object[] values = new Object[baseGcInfoItemValues.length + gcExtItemCount];
        System.arraycopy(baseGcInfoItemValues, 0, values, 0, baseGcInfoItemValues.length);
        if (gcExtItemCount > 0) {
            System.arraycopy(gcExtItemValues, 0, values, baseGcInfoItemValues.length, gcExtItemCount);
        }
        try {
            return new CompositeDataSupport(builder.getGcInfoCompositeType(), builder.getItemNames(), values);
        } catch (OpenDataException e) {
            throw new AssertionError(e);
        }
    }

    private static final String ID = "id";

    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";

    private static final String DURATION = "duration";

    private static final String MEMORY_USAGE_BEFORE_GC = "memoryUsageBeforeGc";

    private static final String MEMORY_USAGE_AFTER_GC = "memoryUsageAfterGc";

    private static final String[] baseGcInfoItemNames = { ID, START_TIME, END_TIME, DURATION, MEMORY_USAGE_BEFORE_GC, MEMORY_USAGE_AFTER_GC };

    private static MappedMXBeanType memoryUsageMapType;

    static {
        try {
            Method m = GcInfo.class.getMethod("getMemoryUsageBeforeGc");
            memoryUsageMapType = MappedMXBeanType.getMappedType(m.getGenericReturnType());
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        } catch (OpenDataException e) {
            throw new AssertionError(e);
        }
    }

    static String[] getBaseGcInfoItemNames() {
        return baseGcInfoItemNames;
    }

    private static OpenType[] baseGcInfoItemTypes = null;

    static synchronized OpenType[] getBaseGcInfoItemTypes() {
        if (baseGcInfoItemTypes == null) {
            OpenType<?> memoryUsageOpenType = memoryUsageMapType.getOpenType();
            baseGcInfoItemTypes = new OpenType[] { SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, SimpleType.LONG, memoryUsageOpenType, memoryUsageOpenType };
        }
        return baseGcInfoItemTypes;
    }

    public static long getId(CompositeData cd) {
        return getLong(cd, ID);
    }

    public static long getStartTime(CompositeData cd) {
        return getLong(cd, START_TIME);
    }

    public static long getEndTime(CompositeData cd) {
        return getLong(cd, END_TIME);
    }

    public static Map<String, MemoryUsage> getMemoryUsageBeforeGc(CompositeData cd) {
        try {
            TabularData td = (TabularData) cd.get(MEMORY_USAGE_BEFORE_GC);
            return cast(memoryUsageMapType.toJavaTypeData(td));
        } catch (InvalidObjectException e) {
            throw new AssertionError(e);
        } catch (OpenDataException e) {
            throw new AssertionError(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, MemoryUsage> cast(Object x) {
        return (Map<String, MemoryUsage>) x;
    }

    public static Map<String, MemoryUsage> getMemoryUsageAfterGc(CompositeData cd) {
        try {
            TabularData td = (TabularData) cd.get(MEMORY_USAGE_AFTER_GC);
            return cast(memoryUsageMapType.toJavaTypeData(td));
        } catch (InvalidObjectException e) {
            throw new AssertionError(e);
        } catch (OpenDataException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Returns true if the input CompositeData has the expected
     * CompositeType (i.e. contain all attributes with expected
     * names and types).  Otherwise, return false.
     */
    public static void validateCompositeData(CompositeData cd) {
        if (cd == null) {
            throw new NullPointerException("Null CompositeData");
        }
        if (!isTypeMatched(getBaseGcInfoCompositeType(), cd.getCompositeType())) {
            throw new IllegalArgumentException("Unexpected composite type for GcInfo");
        }
    }

    private static CompositeType baseGcInfoCompositeType = null;

    private static synchronized CompositeType getBaseGcInfoCompositeType() {
        if (baseGcInfoCompositeType == null) {
            try {
                baseGcInfoCompositeType = new CompositeType("sun.management.BaseGcInfoCompositeType", "CompositeType for Base GcInfo", getBaseGcInfoItemNames(), getBaseGcInfoItemNames(), getBaseGcInfoItemTypes());
            } catch (OpenDataException e) {
                throw Util.newException(e);
            }
        }
        return baseGcInfoCompositeType;
    }

    private static final long serialVersionUID = -5716428894085882742L;
}

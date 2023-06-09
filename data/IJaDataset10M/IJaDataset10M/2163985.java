package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.layoutlib.api.IDensityBasedResourceValue;
import com.android.sdklib.AndroidVersion;
import com.android.sdklib.IAndroidTarget;
import org.eclipse.swt.graphics.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Qualifier for Screen Pixel Density.
 */
public final class PixelDensityQualifier extends ResourceQualifier {

    private static final Pattern sDensityLegacyPattern = Pattern.compile("^(\\d+)dpi$");

    public static final String NAME = "Pixel Density";

    private Density mValue = Density.MEDIUM;

    /**
     * Screen Orientation enum.
     */
    public static enum Density {

        HIGH("hdpi", "High Density", IDensityBasedResourceValue.Density.HIGH), MEDIUM("mdpi", "Medium Density", IDensityBasedResourceValue.Density.MEDIUM), LOW("ldpi", "Low Density", IDensityBasedResourceValue.Density.LOW), NODPI("nodpi", "No Density", IDensityBasedResourceValue.Density.NODPI);

        private final String mValue;

        private final String mDisplayValue;

        private final IDensityBasedResourceValue.Density mDensity;

        private Density(String value, String displayValue, IDensityBasedResourceValue.Density density) {
            mValue = value;
            mDisplayValue = displayValue;
            mDensity = density;
        }

        /**
         * Returns the enum for matching the provided qualifier value.
         * @param value The qualifier value.
         * @return the enum for the qualifier value or null if no matching was found.
         */
        public static Density getEnum(String value) {
            for (Density orient : values()) {
                if (orient.mValue.equals(value)) {
                    return orient;
                }
            }
            return null;
        }

        static Density getLegacyEnum(String value) {
            Matcher m = sDensityLegacyPattern.matcher(value);
            if (m.matches()) {
                String v = m.group(1);
                try {
                    int density = Integer.parseInt(v);
                    for (Density orient : values()) {
                        if (orient.mDensity.getValue() == density) {
                            return orient;
                        }
                    }
                } catch (NumberFormatException e) {
                }
            }
            return null;
        }

        public String getValue() {
            return mValue;
        }

        public int getDpiValue() {
            return mDensity.getValue();
        }

        public String getLegacyValue() {
            if (this != NODPI) {
                return String.format("%1$ddpi", getDpiValue());
            }
            return "";
        }

        public String getDisplayValue() {
            return mDisplayValue;
        }

        /**
         * Returns the {@link com.android.layoutlib.api.IDensityBasedResourceValue.Density} value
         * associated to this {@link Density}.
         */
        public IDensityBasedResourceValue.Density getDensity() {
            return mDensity;
        }

        public static int getIndex(Density value) {
            int i = 0;
            for (Density input : values()) {
                if (value == input) {
                    return i;
                }
                i++;
            }
            return -1;
        }

        public static Density getByIndex(int index) {
            int i = 0;
            for (Density value : values()) {
                if (i == index) {
                    return value;
                }
                i++;
            }
            return null;
        }
    }

    public PixelDensityQualifier() {
    }

    public PixelDensityQualifier(Density value) {
        mValue = value;
    }

    public Density getValue() {
        return mValue;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return NAME;
    }

    @Override
    public Image getIcon() {
        return IconFactory.getInstance().getIcon("dpi");
    }

    @Override
    public boolean isValid() {
        return mValue != null;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        Density density = Density.getEnum(value);
        if (density == null) {
            density = Density.getLegacyEnum(value);
        }
        if (density != null) {
            PixelDensityQualifier qualifier = new PixelDensityQualifier();
            qualifier.mValue = density;
            config.setPixelDensityQualifier(qualifier);
            return true;
        }
        return false;
    }

    @Override
    public boolean isMatchFor(ResourceQualifier qualifier) {
        if (qualifier instanceof PixelDensityQualifier) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isBetterMatchThan(ResourceQualifier compareTo, ResourceQualifier reference) {
        if (compareTo == null) {
            return true;
        }
        PixelDensityQualifier compareQ = (PixelDensityQualifier) compareTo;
        PixelDensityQualifier referenceQ = (PixelDensityQualifier) reference;
        if (mValue == referenceQ.mValue && compareQ.mValue != referenceQ.mValue) {
            return true;
        } else {
            return mValue.mDensity.getValue() > compareQ.mValue.mDensity.getValue();
        }
    }

    @Override
    public boolean equals(Object qualifier) {
        if (qualifier instanceof PixelDensityQualifier) {
            return mValue == ((PixelDensityQualifier) qualifier).mValue;
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (mValue != null) {
            return mValue.hashCode();
        }
        return 0;
    }

    /**
     * Returns the string used to represent this qualifier in the folder name.
     */
    @Override
    public String getFolderSegment(IAndroidTarget target) {
        if (mValue != null) {
            if (target != null) {
                AndroidVersion version = target.getVersion();
                if (version.getApiLevel() <= 3 && version.getCodename() == null) {
                    return mValue.getLegacyValue();
                }
            }
            return mValue.getValue();
        }
        return "";
    }

    @Override
    public String getStringValue() {
        if (mValue != null) {
            return mValue.getDisplayValue();
        }
        return "";
    }
}

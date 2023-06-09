package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.sdklib.IAndroidTarget;
import org.eclipse.swt.graphics.Image;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resource Qualifier for Screen Dimension.
 */
public final class ScreenDimensionQualifier extends ResourceQualifier {

    /** Default screen size value. This means the property is not set */
    static final int DEFAULT_SIZE = -1;

    private static final Pattern sDimensionPattern = Pattern.compile("^(\\d+)x(\\d+)$");

    public static final String NAME = "Screen Dimension";

    /** Screen size 1 value. This is not size X or Y because the folder name always
     * contains the biggest size first. So if the qualifier is 400x200, size 1 will always be
     * 400 but that'll be X in landscape and Y in portrait.
     * Default value is <code>DEFAULT_SIZE</code> */
    private int mValue1 = DEFAULT_SIZE;

    /** Screen size 2 value. This is not size X or Y because the folder name always
     * contains the biggest size first. So if the qualifier is 400x200, size 2 will always be
     * 200 but that'll be Y in landscape and X in portrait.
     * Default value is <code>DEFAULT_SIZE</code> */
    private int mValue2 = DEFAULT_SIZE;

    public ScreenDimensionQualifier() {
    }

    public ScreenDimensionQualifier(int value1, int value2) {
        mValue1 = value1;
        mValue2 = value2;
    }

    public int getValue1() {
        return mValue1;
    }

    public int getValue2() {
        return mValue2;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getShortName() {
        return "Dimension";
    }

    @Override
    public Image getIcon() {
        return IconFactory.getInstance().getIcon("dimension");
    }

    @Override
    public boolean isValid() {
        return mValue1 != DEFAULT_SIZE && mValue2 != DEFAULT_SIZE;
    }

    @Override
    public boolean checkAndSet(String value, FolderConfiguration config) {
        Matcher m = sDimensionPattern.matcher(value);
        if (m.matches()) {
            String d1 = m.group(1);
            String d2 = m.group(2);
            ScreenDimensionQualifier qualifier = getQualifier(d1, d2);
            if (qualifier != null) {
                config.setScreenDimensionQualifier(qualifier);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object qualifier) {
        if (qualifier instanceof ScreenDimensionQualifier) {
            ScreenDimensionQualifier q = (ScreenDimensionQualifier) qualifier;
            return (mValue1 == q.mValue1 && mValue2 == q.mValue2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static ScreenDimensionQualifier getQualifier(String size1, String size2) {
        try {
            int s1 = Integer.parseInt(size1);
            int s2 = Integer.parseInt(size2);
            ScreenDimensionQualifier qualifier = new ScreenDimensionQualifier();
            if (s1 > s2) {
                qualifier.mValue1 = s1;
                qualifier.mValue2 = s2;
            } else {
                qualifier.mValue1 = s2;
                qualifier.mValue2 = s1;
            }
            return qualifier;
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * Returns the string used to represent this qualifier in the folder name.
     */
    @Override
    public String getFolderSegment(IAndroidTarget target) {
        return String.format("%1$dx%2$d", mValue1, mValue2);
    }

    @Override
    public String getStringValue() {
        if (mValue1 != -1 && mValue2 != -1) {
            return String.format("%1$dx%2$d", mValue1, mValue2);
        }
        return "";
    }
}

package uk.org.toot.audio.tool;

import java.util.List;
import uk.org.toot.audio.core.AudioControls;
import uk.org.toot.audio.core.ChannelFormat;
import uk.org.toot.control.EnumControl;

/**
 * @author st
 */
public class FormatControls extends AudioControls {

    private static final int FORMAT_ID = 1;

    private FormatIndicator indicator;

    public FormatControls() {
        super(ToolIds.FORMAT_ID, "Format");
        add(indicator = new FormatIndicator());
    }

    public void setFormat(ChannelFormat format) {
        indicator.setValue(format);
    }

    public static class FormatIndicator extends EnumControl {

        private static List<ChannelFormat> values;

        public FormatIndicator() {
            super(FORMAT_ID, "Format", ChannelFormat.MONO);
            indicator = true;
            if (values == null) {
                values = new java.util.ArrayList<ChannelFormat>();
                values.add(ChannelFormat.MONO);
                values.add(ChannelFormat.STEREO);
                values.add(ChannelFormat.QUAD);
                values.add(ChannelFormat.FIVE_1);
            }
        }

        public List getValues() {
            return values;
        }

        public String getValueString() {
            return ((ChannelFormat) getValue()).getName();
        }

        public int getWidthLimit() {
            return 150;
        }
    }
}

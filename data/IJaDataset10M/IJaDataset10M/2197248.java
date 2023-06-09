package flash.events;

import org.epistem.j2avm.annotations.runtime.*;

@FlashNativeClass
public class ActivityEvent extends flash.events.Event {

    @SlotId(0)
    public static final String ACTIVITY = "activity";

    /**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ActivityEvent(String arg1, boolean arg2, boolean arg3, boolean arg4) {
        super(null, false, false);
    }

    /**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ActivityEvent(String arg1, boolean arg2, boolean arg3) {
        super(null, false, false);
    }

    /**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ActivityEvent(String arg1, boolean arg2) {
        super(null, false, false);
    }

    /**
	 * Note: it may be the case that some objects are not supposed to be
	 *       constructed - this may have to be hand-tuned later
	 */
    public ActivityEvent(String arg1) {
        super(null, false, false);
    }

    @SlotId(-1)
    @Getter
    public native boolean getActivating();

    @SlotId(-1)
    @Setter
    public native void setActivating(boolean arg1);
}

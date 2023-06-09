package org.pjsip.pjsua;

public class pjsip_event_body_rx_msg {

    private long swigCPtr;

    protected boolean swigCMemOwn;

    protected pjsip_event_body_rx_msg(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(pjsip_event_body_rx_msg obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0 && swigCMemOwn) {
            swigCMemOwn = false;
            pjsuaJNI.delete_pjsip_event_body_rx_msg(swigCPtr);
        }
        swigCPtr = 0;
    }

    public void setRdata(SWIGTYPE_p_pjsip_rx_data value) {
        pjsuaJNI.pjsip_event_body_rx_msg_rdata_set(swigCPtr, this, SWIGTYPE_p_pjsip_rx_data.getCPtr(value));
    }

    public SWIGTYPE_p_pjsip_rx_data getRdata() {
        long cPtr = pjsuaJNI.pjsip_event_body_rx_msg_rdata_get(swigCPtr, this);
        return (cPtr == 0) ? null : new SWIGTYPE_p_pjsip_rx_data(cPtr, false);
    }

    public pjsip_event_body_rx_msg() {
        this(pjsuaJNI.new_pjsip_event_body_rx_msg(), true);
    }
}

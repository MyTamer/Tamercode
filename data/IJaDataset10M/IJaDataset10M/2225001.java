package jmri.jmrix.rps.rpsmon;

import jmri.jmrix.rps.*;

public class RpsMonFrame extends jmri.jmrix.AbstractMonFrame implements ReadingListener, MeasurementListener {

    public RpsMonFrame() {
        super();
        Distributor.instance().addReadingListener(this);
        Distributor.instance().addMeasurementListener(this);
    }

    protected String title() {
        return "RPS Monitor";
    }

    public void dispose() {
        Distributor.instance().removeReadingListener(this);
        Distributor.instance().removeMeasurementListener(this);
        super.dispose();
    }

    protected void init() {
    }

    public void notify(Reading r) {
        String raw = "";
        if (r.getRawData() != null) raw = r.getRawData().toString();
        nextLine(r.toString() + "\n", raw);
    }

    public void notify(Measurement m) {
        String raw = "";
        if (m.getReading() != null) raw = m.getReading().toString();
        nextLine(m.toString() + "\n", raw);
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RpsMonFrame.class.getName());
}

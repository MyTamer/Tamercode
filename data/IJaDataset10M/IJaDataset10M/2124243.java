package se.kth.cid.conzilla.map.graphics;

import se.kth.cid.conzilla.map.*;
import se.kth.cid.component.*;
import se.kth.cid.conceptmap.*;
import se.kth.cid.util.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class NSPaint implements EditListener {

    public static final int RECTANGLE = 0;

    public static final int RAISEDRECT = 1;

    public static final int SUNKENRECT = 2;

    public static final int ROUNDRECT = 3;

    public static final int OVAL = 4;

    public static final int DIAMOND = 5;

    public static final int UNKNOWN = 6;

    public static final int INVISIBLE = 7;

    public static final int NONE = 8;

    public NeuronStyle neuronstyle;

    Hashtable rspaintobjects;

    LineDrawer linedrawer;

    BoxDrawer boxdrawer;

    TitleDrawer titledrawer;

    DataDrawer datadrawer;

    Object appobject;

    boolean hitDirty;

    public NSPaint(NeuronStyle neuronstyle, final MapDisplayer.MapComponentDrawer parent) {
        this.neuronstyle = neuronstyle;
        linedrawer = new LineDrawer();
        boxdrawer = new BoxDrawer();
        titledrawer = new TitleDrawer(neuronstyle, parent);
        datadrawer = new DataDrawer(neuronstyle, parent);
        appobject = null;
        if (neuronstyle.getNeuron() != null) neuronstyle.getNeuron().addEditListener(this);
        if (neuronstyle.getNeuronType() != null) neuronstyle.getNeuronType().addEditListener(this);
        fixPaintingObjects();
        fix();
    }

    public void detach() {
        if (neuronstyle.getNeuron() != null) neuronstyle.getNeuron().removeEditListener(this);
        if (neuronstyle.getNeuronType() != null) neuronstyle.getNeuronType().removeEditListener(this);
        detachRS();
        linedrawer.detach();
        linedrawer = null;
        boxdrawer.detach();
        boxdrawer = null;
        titledrawer.detach();
        titledrawer = null;
        datadrawer.detach();
        datadrawer = null;
        appobject = null;
    }

    public void detachRS() {
        Enumeration en = rspaintobjects.elements();
        for (; en.hasMoreElements(); ) ((RSPaint) en.nextElement()).detach();
        rspaintobjects = null;
    }

    public void fixPaintingObjects() {
        rspaintobjects = new Hashtable();
        Enumeration enu = neuronstyle.getRoles().elements();
        for (; enu.hasMoreElements(); ) {
            RoleStyle rolestyle = (RoleStyle) enu.nextElement();
            rspaintobjects.put(rolestyle, new RSPaint(rolestyle));
        }
    }

    public void fix() {
        fixRS();
        fixNS();
    }

    public void fixRS(EditEvent e) {
        if (e.getEditType() == RoleStyle.LINE_EDITED) ((RSPaint) rspaintobjects.get(((RoleStyle.RoleStyleEditObject) e.getTarget()).roleStyle)).fix();
    }

    public void fixRS() {
        Enumeration en = rspaintobjects.elements();
        for (; en.hasMoreElements(); ) ((RSPaint) en.nextElement()).fix();
    }

    public void fixNS(EditEvent e) {
        fixNS();
    }

    public void fixNS() {
        hitDirty = true;
        linedrawer.fixFromNeuronStyle(neuronstyle, neuronstyle.getLine());
        boxdrawer.fixFromNeuronStyle(neuronstyle);
        titledrawer.fixFromNeuronStyle(boxdrawer.getInnerBoundingBox(), boxdrawer.getBoxType());
        datadrawer.fixFromNeuronStyle(titledrawer.getFreeSpace(), neuronstyle);
    }

    public Object getAppObject() {
        return appobject;
    }

    public void setAppObject(Object ob) {
        appobject = ob;
    }

    public boolean setDataValuesEditable(boolean editable, ComponentSaver csaver) {
        return datadrawer.setDataValuesEditable(editable, csaver);
    }

    public boolean setTitleEditable(boolean editable) {
        if (!neuronstyle.isEditable()) if (editable == true) return false; else titledrawer.setEditable(false); else titledrawer.setEditable(editable);
        return true;
    }

    public void paint(Graphics g) {
        Color ctemp = g.getColor();
        Color over = null;
        if (neuronstyle.getMark() != null) over = neuronstyle.getMark();
        Enumeration en = rspaintobjects.elements();
        for (; en.hasMoreElements(); ) ((RSPaint) en.nextElement()).paint(g, over);
        linedrawer.paint(g, over);
        boxdrawer.paint(g, over);
        if (boxdrawer.getBoxType() != BoxDrawer.NONE) {
            titledrawer.paint(g, over);
            datadrawer.paint(g);
        }
        g.setColor(ctemp);
    }

    public int didHit(int x, int y, MapEvent m) {
        Enumeration enu = rspaintobjects.elements();
        for (; enu.hasMoreElements(); ) {
            RSPaint rsp = (RSPaint) enu.nextElement();
            if (rsp.didHit(m) != 0) return m.hit;
        }
        if (boxdrawer.getBoxType() != BoxDrawer.NONE && boxdrawer.didHit(m)) {
            m.neuronstyle = neuronstyle;
            if (titledrawer.didHit(m)) {
                m.hit = MapEvent.HIT_TITLE;
                return m.hit;
            }
            if (datadrawer.didHit(m)) {
                m.hit = MapEvent.HIT_DATA;
                return m.hit;
            }
            m.hit = MapEvent.HIT_BOX;
            return m.hit;
        }
        if (hitDirty == true) {
            linedrawer.fixHitInfo();
            hitDirty = false;
        }
        if (linedrawer.didHit(m)) {
            m.neuronstyle = neuronstyle;
            m.hit = MapEvent.HIT_NEURONLINE;
            return m.hit;
        }
        return MapEvent.HIT_NONE;
    }

    public void componentEdited(EditEvent e) {
        if (neuronstyle.isNeuronConnected()) fix();
    }
}

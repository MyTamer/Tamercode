package org.web3d.x3d.palette.items;

import java.util.Vector;
import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DRigidJointNode;
import static org.web3d.x3d.types.X3DPrimitiveTypes.*;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * UNIVERSALJOINT.java
 * Created on January 3, 2009
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey, Don Brutzman
 * @version $Id$
 */
public class UNIVERSALJOINT extends X3DRigidJointNode {

    private Vector<String> forceOutput;

    private SFFloat anchorPointX, anchorPointXDefault;

    private SFFloat anchorPointY, anchorPointYDefault;

    private SFFloat anchorPointZ, anchorPointZDefault;

    private SFFloat axis1X, axis1XDefault;

    private SFFloat axis1Y, axis1YDefault;

    private SFFloat axis1Z, axis1ZDefault;

    private SFFloat axis2X, axis2XDefault;

    private SFFloat axis2Y, axis2YDefault;

    private SFFloat axis2Z, axis2ZDefault;

    private SFFloat stop1Bounce, stop1BounceDefault;

    private SFFloat stop2Bounce, stop2BounceDefault;

    private SFFloat stop1ErrorCorrection, stop1ErrorCorrectionDefault;

    private SFFloat stop2ErrorCorrection, stop2ErrorCorrectionDefault;

    public UNIVERSALJOINT() {
    }

    @Override
    public String getElementName() {
        return UNIVERSALJOINT_ELNAME;
    }

    @Override
    public void initialize() {
        forceOutput = new Vector<String>();
        if (UNIVERSALJOINT_ATTR_FORCEOUTPUT_DFLT.length() > 0) {
            forceOutput.add(UNIVERSALJOINT_ATTR_FORCEOUTPUT_DFLT);
        }
        String[] fa = parse3(UNIVERSALJOINT_ATTR_ANCHORPOINT_DFLT);
        anchorPointX = anchorPointXDefault = new SFFloat(fa[0], 0.0f, null);
        anchorPointY = anchorPointYDefault = new SFFloat(fa[1], 0.0f, null);
        anchorPointZ = anchorPointZDefault = new SFFloat(fa[2], 0.0f, null);
        fa = parse3(UNIVERSALJOINT_ATTR_AXIS1_DFLT);
        axis1X = axis1XDefault = new SFFloat(fa[0], null, null);
        axis1Y = axis1YDefault = new SFFloat(fa[1], null, null);
        axis1Z = axis1ZDefault = new SFFloat(fa[2], null, null);
        fa = parse3(UNIVERSALJOINT_ATTR_AXIS2_DFLT);
        axis2X = axis2XDefault = new SFFloat(fa[0], null, null);
        axis2Y = axis2YDefault = new SFFloat(fa[1], null, null);
        axis2Z = axis2ZDefault = new SFFloat(fa[2], null, null);
        stop1Bounce = stop1BounceDefault = new SFFloat(UNIVERSALJOINT_ATTR_STOP1BOUNCE_DFLT, 0.0f, 1.0f);
        stop2Bounce = stop2BounceDefault = new SFFloat(UNIVERSALJOINT_ATTR_STOP2BOUNCE_DFLT, 0.0f, 1.0f);
        stop1ErrorCorrection = stop1ErrorCorrectionDefault = new SFFloat(UNIVERSALJOINT_ATTR_STOP1ERRORCORRECTION_DFLT, 0.0f, 1.0f);
        stop2ErrorCorrection = stop2ErrorCorrectionDefault = new SFFloat(UNIVERSALJOINT_ATTR_STOP2ERRORCORRECTION_DFLT, 0.0f, 1.0f);
        setContent("\n\t\t<RigidBody containerField='body1'/>" + "\n\t\t<RigidBody containerField='body2'/>\n\t");
    }

    @Override
    public void initializeFromJdom(org.jdom.Element root, JTextComponent comp) {
        super.initializeFromJdom(root, comp);
        org.jdom.Attribute attr = root.getAttribute(UNIVERSALJOINT_ATTR_FORCEOUTPUT_NAME);
        if (attr != null) {
            forceOutput = new Vector<String>();
            forceOutput.add(attr.getValue());
        }
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_ANCHORPOINT_NAME);
        if (attr != null) {
            String[] fa = parse3(attr.getValue());
            anchorPointX = new SFFloat(fa[0], 0.0f, null, true);
            anchorPointY = new SFFloat(fa[1], 0.0f, null, true);
            anchorPointZ = new SFFloat(fa[2], 0.0f, null, true);
        }
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_AXIS1_NAME);
        if (attr != null) {
            String[] fa = parse3(attr.getValue());
            axis1X = new SFFloat(fa[0], 0.0f, null, true);
            axis1Y = new SFFloat(fa[1], 0.0f, null, true);
            axis1Z = new SFFloat(fa[2], 0.0f, null, true);
        }
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_AXIS2_NAME);
        if (attr != null) {
            String[] fa = parse3(attr.getValue());
            axis2X = new SFFloat(fa[0], 0.0f, null, true);
            axis2Y = new SFFloat(fa[1], 0.0f, null, true);
            axis2Z = new SFFloat(fa[2], 0.0f, null, true);
        }
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_STOP1BOUNCE_NAME);
        if (attr != null) stop1Bounce = new SFFloat(attr.getValue(), 0.0f, 1.0f);
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_STOP2BOUNCE_NAME);
        if (attr != null) stop2Bounce = new SFFloat(attr.getValue(), 0.0f, 1.0f);
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_STOP1ERRORCORRECTION_NAME);
        if (attr != null) stop1ErrorCorrection = new SFFloat(attr.getValue(), 0.0f, 1.0f);
        attr = root.getAttribute(UNIVERSALJOINT_ATTR_STOP2ERRORCORRECTION_NAME);
        if (attr != null) stop2ErrorCorrection = new SFFloat(attr.getValue(), 0.0f, 1.0f);
    }

    @Override
    public Class<? extends BaseCustomizer> getCustomizer() {
        return UNIVERSALJOINTCustomizer.class;
    }

    @Override
    public String createAttributes() {
        StringBuffer sb = new StringBuffer();
        if (UNIVERSALJOINT_ATTR_ANCHORPOINT_REQD || (!anchorPointX.equals(anchorPointXDefault) || !anchorPointY.equals(anchorPointYDefault) || !anchorPointZ.equals(anchorPointZDefault))) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_ANCHORPOINT_NAME);
            sb.append("='");
            sb.append(anchorPointX);
            sb.append(" ");
            sb.append(anchorPointY);
            sb.append(" ");
            sb.append(anchorPointZ);
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_AXIS1_REQD || (!axis1X.equals(axis1XDefault) || !axis1Y.equals(axis1YDefault) || !axis1Z.equals(axis1ZDefault))) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_AXIS1_NAME);
            sb.append("='");
            sb.append(axis1X);
            sb.append(" ");
            sb.append(axis1Y);
            sb.append(" ");
            sb.append(axis1Z);
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_AXIS2_REQD || (!axis1X.equals(axis2XDefault) || !axis1Y.equals(axis2YDefault) || !axis1Z.equals(axis2ZDefault))) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_AXIS2_NAME);
            sb.append("='");
            sb.append(axis2X);
            sb.append(" ");
            sb.append(axis2Y);
            sb.append(" ");
            sb.append(axis2Z);
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_FORCEOUTPUT_REQD || (forceOutput.size() > 0)) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_FORCEOUTPUT_NAME);
            sb.append("='");
            sb.append(formatForceOutput());
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_STOP1BOUNCE_REQD || !stop1Bounce.equals(stop1BounceDefault)) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_STOP1BOUNCE_NAME);
            sb.append("='");
            sb.append(getStop1Bounce());
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_STOP2BOUNCE_REQD || !stop2Bounce.equals(stop2BounceDefault)) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_STOP2BOUNCE_NAME);
            sb.append("='");
            sb.append(getStop2Bounce());
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_STOP1ERRORCORRECTION_REQD || !stop1ErrorCorrection.equals(stop1ErrorCorrectionDefault)) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_STOP1ERRORCORRECTION_NAME);
            sb.append("='");
            sb.append(getStop1ErrorCorrection());
            sb.append("'");
        }
        if (UNIVERSALJOINT_ATTR_STOP2ERRORCORRECTION_REQD || !stop2ErrorCorrection.equals(stop2ErrorCorrectionDefault)) {
            sb.append(" ");
            sb.append(UNIVERSALJOINT_ATTR_STOP2ERRORCORRECTION_NAME);
            sb.append("='");
            sb.append(getStop2ErrorCorrection());
            sb.append("'");
        }
        return sb.toString();
    }

    public String getUnformattedForceOutput() {
        StringBuffer sb = new StringBuffer();
        for (String s : forceOutput) {
            StringBuffer s2 = new StringBuffer();
            for (int i = 0; i < s.length(); i++) {
                s2.append(s.substring(i, i + 1));
            }
            if ((forceOutput.size() == 1) || ((s2.substring(0, 0).compareTo("\"") == 0) && (s2.substring(s2.length() - 1, s2.length() - 1).compareTo("\"") == 0))) sb.append(s2); else {
                sb.append("\"");
                sb.append(s2);
                sb.append("\"");
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private String formatForceOutput() {
        StringBuffer sb = new StringBuffer();
        for (String s : forceOutput) {
            StringBuffer s2 = new StringBuffer();
            for (int i = 0; i < s.length(); i++) {
                if (s.substring(i, i + 1).compareTo("'") == 0) s2.append("&apos;"); else if (s.substring(i, i + 1).compareTo("&") == 0) s2.append("&amp;"); else s2.append(s.substring(i, i + 1));
            }
            StringBuffer s3 = s2;
            if ((forceOutput.size() == 1) || ((s2.substring(0, 0).compareTo("\"") == 0) && (s2.substring(s2.length() - 1, s2.length() - 1).compareTo("\"") == 0))) sb.append(s3); else {
                sb.append("\"");
                sb.append(s3);
                sb.append("\"");
            }
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    private Vector<String> parseForceOutput(String s) {
        Vector<String> v = new Vector<String>();
        s = s.trim();
        if (s.length() > 0) {
            if (!s.startsWith("\"")) v.add(s); else {
                s = s.substring(1);
                if (s.endsWith("\"")) ;
                s = s.substring(0, s.length() - 1);
                String[] sa = s.split("\"\\s*\"");
                for (String str : sa) {
                    v.add(str);
                }
            }
        }
        return v;
    }

    public void setForceOutput(String forceOutputString) {
        forceOutput = parseForceOutput(forceOutputString);
    }

    public String getAxis1X() {
        return axis1X.toString();
    }

    public void setAxis1X(String axisX) {
        this.axis1X = new SFFloat(axisX, 0.0f, null, true);
    }

    public String getAxis1Y() {
        return axis1Y.toString();
    }

    public void setAxis1Y(String axisY) {
        this.axis1Y = new SFFloat(axisY, 0.0f, null, true);
    }

    public String getAxis1Z() {
        return axis1Z.toString();
    }

    public void setAxis1Z(String axisZ) {
        this.axis1Z = new SFFloat(axisZ, 0.0f, null, true);
    }

    public String getAxis2X() {
        return axis2X.toString();
    }

    public void setAxis2X(String axisX) {
        this.axis2X = new SFFloat(axisX, 0.0f, null, true);
    }

    public String getAxis2Y() {
        return axis2Y.toString();
    }

    public void setAxis2Y(String axisY) {
        this.axis2Y = new SFFloat(axisY, 0.0f, null, true);
    }

    public String getAxis2Z() {
        return axis2Z.toString();
    }

    public void setAxis2Z(String axisZ) {
        this.axis2Z = new SFFloat(axisZ, 0.0f, null, true);
    }

    public String getAnchorPointX() {
        return anchorPointX.toString();
    }

    public void setAnchorPointX(String anchorPointX) {
        this.anchorPointX = new SFFloat(anchorPointX, 1.0f, null, true);
    }

    public String getAnchorPointY() {
        return anchorPointY.toString();
    }

    public void setAnchorPointY(String anchorPointY) {
        this.anchorPointY = new SFFloat(anchorPointY, 1.0f, null, true);
    }

    public String getAnchorPointZ() {
        return anchorPointZ.toString();
    }

    public void setAnchorPointZ(String anchorPointZ) {
        this.anchorPointZ = new SFFloat(anchorPointZ, 0.0f, null, true);
    }

    public String getStop1Bounce() {
        return stop1Bounce.toString();
    }

    public void setStop1Bounce(String stopBounce) {
        this.stop1Bounce = new SFFloat(stopBounce, 1.0f, null, true);
    }

    public String getStop2Bounce() {
        return stop2Bounce.toString();
    }

    public void setStop2Bounce(String stopBounce) {
        this.stop2Bounce = new SFFloat(stopBounce, 1.0f, null, true);
    }

    public String getStop1ErrorCorrection() {
        return stop1ErrorCorrection.toString();
    }

    public void setStop1ErrorCorrection(String stopErrorCorrection) {
        this.stop1ErrorCorrection = new SFFloat(stopErrorCorrection, 1.0f, null, true);
    }

    public String getStop2ErrorCorrection() {
        return stop2ErrorCorrection.toString();
    }

    public void setStop2ErrorCorrection(String stopErrorCorrection) {
        this.stop2ErrorCorrection = new SFFloat(stopErrorCorrection, 1.0f, null, true);
    }
}

package jmri.jmrix.zimo;

/**
 * Returns a list of valid Zimo Connection Types
 * <P>
 * @author      Bob Jacobsen   Copyright (C) 2010
 * @author      Kevin Dickerson    Copyright (C) 2010
 * @version	$Revision: 1.1 $
 *
 */
public class Mx1ConnectionTypeList implements jmri.jmrix.ConnectionTypeList {

    public String[] getAvailableProtocolClasses() {
        return new String[] { "jmri.jmrix.zimo.mx1.ConnectionConfig" };
    }
}

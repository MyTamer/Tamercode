package uk.co.westhawk.snmp.pdu;

import uk.co.westhawk.snmp.stack.*;
import uk.co.westhawk.snmp.pdu.*;
import java.util.*;

/**
 * The class InterfaceGetNextPdu.
 *
 * This file is auto generated by the StubBrowser utility, using Mibble.
 * See the uk/co/westhawk/stub/ directory.
 *
 * Added speed parameter and methods by hand.

 * Make sure that you replace the package name and classname placeholders. 
 * Also, move this file to the correct package directory.
 * If these things are not done, this class will not compile correctly!!
 *
 * @version $Revision: 3.17 $ $Date: 2008/05/06 10:17:06 $
 */
public class InterfaceGetNextPdu extends InterfaceGetNextPduStub {

    private static final String version_id = "@(#)$Id: InterfaceGetNextPdu.java,v 3.17 2008/05/06 10:17:06 birgita Exp $ Copyright Westhawk Ltd";

    protected long _speed;

    /**
 * Constructor.
 *
 * @param con The context of the request
 */
    public InterfaceGetNextPdu(SnmpContextBasisFace con) {
        super(con);
    }

    /**
 * Returns the last calculates speed.
 *
 * @see #getSpeed(InterfaceGetNextPdu)
 */
    public long getSpeed() {
        return _speed;
    }

    /**
 * Calculates the speed of the interface. This is done by providing the
 * method with <i>the previous value of this interface</i>. An interface 
 * is marked by its index. Do <i>not</i> confuse it 
 * with <i>the previous interface ifInOctets the MIB</i>.
 * Total number of octets (received and transmitted) per second.
 *
 * @param old The previous value of this interface 
 */
    public long getSpeed(InterfaceGetNextPdu old) {
        _speed = -1;
        if (this._ifOperStatus > 0 && old._ifOperStatus > 0 && this._valid && old._valid) {
            long tdiff = (this._sysUpTime - old._sysUpTime);
            if (tdiff != 0) {
                long inO = this._ifInOctets - old._ifInOctets;
                long outO = this._ifOutOctets - old._ifOutOctets;
                _speed = 100 * (inO + outO) / tdiff;
            }
        } else {
            _speed = -1;
        }
        return _speed;
    }

    /** 
 * Returns how many interfaces are present.
 *
 * @return the number of interfaces
 */
    public static int getIfNumber(SnmpContextBasisFace con) throws PduException, java.io.IOException {
        int ifNumber = 0;
        if (con != null) {
            OneIntPdu ifNumberPdu = new OneIntPdu(con, ifNumber_OID + ".0");
            boolean answered = ifNumberPdu.waitForSelf();
            boolean timedOut = ifNumberPdu.isTimedOut();
            if (timedOut == false) {
                Integer intValue = ifNumberPdu.getValue();
                if (intValue != null) {
                    ifNumber = intValue.intValue();
                }
            }
        }
        return ifNumber;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append(", speed=").append(_speed);
        buffer.append("]");
        return buffer.toString();
    }
}
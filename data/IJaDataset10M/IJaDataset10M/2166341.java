package org.openrtk.idl.epp0705.domain;

/**
 * Class that contains the elements used to represent domain registration period.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/domain/epp_DomainPeriod.java,v 1.1 2003/03/20 22:42:30 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/20 22:42:30 $<br>
 * @see org.openrtk.idl.epp0705.domain.epp_DomainCreateReq
 * @see org.openrtk.idl.epp0705.domain.epp_DomainRenewReq
 * @see org.openrtk.idl.epp0705.domain.epp_DomainTransferReq
 */
public class epp_DomainPeriod implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The unit type of the domain registration period.
   * @see #setUnit(org.openrtk.idl.epp0705.domain.epp_DomainPeriodUnitType)
   * @see #getUnit()
   */
    public org.openrtk.idl.epp0705.domain.epp_DomainPeriodUnitType m_unit = null;

    /**
   * The value of the domain registration period.
   * @see #setValue(short)
   * @see #getValue()
   */
    public short m_value = (short) 0;

    /**
   * Empty constructor
   */
    public epp_DomainPeriod() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_unit The unit type of the domain registration period
   * @param _m_value The value of the domain registration period
   */
    public epp_DomainPeriod(org.openrtk.idl.epp0705.domain.epp_DomainPeriodUnitType _m_unit, short _m_value) {
        m_unit = _m_unit;
        m_value = _m_value;
    }

    /**
   * Accessor method for the unit type of the domain registration period
   * @param value The unit type of period
   * @see #m_unit
   */
    public void setUnit(org.openrtk.idl.epp0705.domain.epp_DomainPeriodUnitType value) {
        m_unit = value;
    }

    /**
   * Accessor method for the unit type of the domain registration period
   * @return The unit type of period
   * @see #m_unit
   */
    public org.openrtk.idl.epp0705.domain.epp_DomainPeriodUnitType getUnit() {
        return m_unit;
    }

    /**
   * Accessor method for the value of the domain registration period
   * @param value The value of period
   * @see #m_value
   */
    public void setValue(short value) {
        m_value = value;
    }

    public void setValue(int value) {
        m_value = (short) value;
    }

    /**
   * Accessor method for the value of the domain registration period
   * @return The value of period
   * @see #m_value
   */
    public short getValue() {
        return m_value;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_unit [" + m_unit + "] m_value [" + m_value + "] }";
    }
}

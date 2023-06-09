package org.didicero.base.entity;

/**
 * Autogenerated POJO EJB class for EventService containing the 
 * bulk of the entity implementation.
 *
 * This is autogenerated by AndroMDA using the EJB3
 * cartridge.
 *
 * DO NOT MODIFY this class.
 *
 * 
 *
 */
@javax.persistence.Entity
@javax.persistence.DiscriminatorValue("E")
@javax.persistence.NamedQuery(name = "EventService.findAll", query = "select eventService from EventService AS eventService")
public class EventService extends org.didicero.base.entity.Event implements java.io.Serializable {

    private static final long serialVersionUID = 6715731925330293663L;

    private java.lang.String serviceClass;

    /**
     * Default empty constructor
     */
    public EventService() {
    }

    /**
     * Implementation for the constructor with all POJO attributes except auto incremented identifiers.
     * This method sets all POJO fields defined in this class to the values provided by 
     * the parameters.
     *
     * @param eventtype Value for the eventtype property
     * @param timestamp Value for the timestamp property
     * @param serviceClass Value for the serviceClass property
     */
    public EventService(org.didicero.base.types.EventType eventtype, java.util.Date timestamp, java.lang.String serviceClass) {
        setEventtype(eventtype);
        setTimestamp(timestamp);
        setServiceClass(serviceClass);
    }

    /**
     * Cloneable implementation with all POJO attribute values and CMR relations.
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        EventService aClone = new EventService(this.getEventtype(), this.getTimestamp(), this.getServiceClass());
        aClone.setId(this.getId());
        return aClone;
    }

    /**
     * Get the serviceClass property.
     * 
     * @return java.lang.String The value of serviceClass
     */
    @javax.persistence.Column(name = "SERVICE_CLASS", insertable = true, updatable = true)
    public java.lang.String getServiceClass() {
        return serviceClass;
    }

    /**
     * Set the serviceClass property.
     * @param value the new value
     */
    public void setServiceClass(java.lang.String value) {
        firePropertyChange("serviceClass", this.serviceClass, this.serviceClass = value);
    }

    /**
     * Indicates if the argument is of the same type and all values are equal.
     *
     * @param object The target object to compare with
     * @return boolean True if both objects a 'equal'
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof EventService)) {
            return false;
        }
        final EventService that = (EventService) object;
        if (this.getId() == null || that.getId() == null || !this.getId().equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code value for the object
     *
     * @return int The hash code value
     */
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + (getId() == null ? 0 : getId().hashCode());
        return hashCode;
    }

    /**
     * Returns a String representation of the object
     *
     * @return String Textual representation of the object displaying name/value pairs for all attributes
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EventService(=");
        sb.append(super.toString());
        sb.append("serviceClass: ");
        sb.append(getServiceClass());
        sb.append(")");
        return sb.toString();
    }
}

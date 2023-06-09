package edu.unibi.agbi.biodwh.entity.reactome;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PhysicalEntity2CompartmentId generated by hbm2java
 */
@Embeddable
public class PhysicalEntity2CompartmentId implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6380103631006984864L;

    private Integer dbId;

    private Integer compartmentRank;

    private Integer compartment;

    private String compartmentClass;

    public PhysicalEntity2CompartmentId() {
    }

    public PhysicalEntity2CompartmentId(Integer dbId, Integer compartmentRank, Integer compartment, String compartmentClass) {
        this.dbId = dbId;
        this.compartmentRank = compartmentRank;
        this.compartment = compartment;
        this.compartmentClass = compartmentClass;
    }

    @Column(name = "DB_ID")
    public Integer getDbId() {
        return this.dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    @Column(name = "compartment_rank")
    public Integer getCompartmentRank() {
        return this.compartmentRank;
    }

    public void setCompartmentRank(Integer compartmentRank) {
        this.compartmentRank = compartmentRank;
    }

    @Column(name = "compartment")
    public Integer getCompartment() {
        return this.compartment;
    }

    public void setCompartment(Integer compartment) {
        this.compartment = compartment;
    }

    @Column(name = "compartment_class", length = 64)
    public String getCompartmentClass() {
        return this.compartmentClass;
    }

    public void setCompartmentClass(String compartmentClass) {
        this.compartmentClass = compartmentClass;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof PhysicalEntity2CompartmentId)) return false;
        PhysicalEntity2CompartmentId castOther = (PhysicalEntity2CompartmentId) other;
        return ((this.getDbId() == castOther.getDbId()) || (this.getDbId() != null && castOther.getDbId() != null && this.getDbId().equals(castOther.getDbId()))) && ((this.getCompartmentRank() == castOther.getCompartmentRank()) || (this.getCompartmentRank() != null && castOther.getCompartmentRank() != null && this.getCompartmentRank().equals(castOther.getCompartmentRank()))) && ((this.getCompartment() == castOther.getCompartment()) || (this.getCompartment() != null && castOther.getCompartment() != null && this.getCompartment().equals(castOther.getCompartment()))) && ((this.getCompartmentClass() == castOther.getCompartmentClass()) || (this.getCompartmentClass() != null && castOther.getCompartmentClass() != null && this.getCompartmentClass().equals(castOther.getCompartmentClass())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getDbId() == null ? 0 : this.getDbId().hashCode());
        result = 37 * result + (getCompartmentRank() == null ? 0 : this.getCompartmentRank().hashCode());
        result = 37 * result + (getCompartment() == null ? 0 : this.getCompartment().hashCode());
        result = 37 * result + (getCompartmentClass() == null ? 0 : this.getCompartmentClass().hashCode());
        return result;
    }
}

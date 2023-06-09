package edu.unibi.agbi.biodwh.entity.kegg.ligand.enzyme;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * KeggEnzymeEffectorId generated by hbm2java
 */
@Embeddable
public class KeggEnzymeEffectorId implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4694641283470347539L;

    private String entry;

    private String effector;

    public KeggEnzymeEffectorId() {
    }

    public KeggEnzymeEffectorId(String entry, String effector) {
        this.entry = entry;
        this.effector = effector;
    }

    @Column(name = "entry", nullable = false)
    public String getEntry() {
        return this.entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    @Column(name = "effector", nullable = false)
    public String getEffector() {
        return this.effector;
    }

    public void setEffector(String effector) {
        this.effector = effector;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof KeggEnzymeEffectorId)) return false;
        KeggEnzymeEffectorId castOther = (KeggEnzymeEffectorId) other;
        return ((this.getEntry() == castOther.getEntry()) || (this.getEntry() != null && castOther.getEntry() != null && this.getEntry().equals(castOther.getEntry()))) && ((this.getEffector() == castOther.getEffector()) || (this.getEffector() != null && castOther.getEffector() != null && this.getEffector().equals(castOther.getEffector())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getEntry() == null ? 0 : this.getEntry().hashCode());
        result = 37 * result + (getEffector() == null ? 0 : this.getEffector().hashCode());
        return result;
    }
}

package edu.unibi.agbi.biodwh.entity.kegg.ligand.enzyme;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * KeggEnzymeEffector generated by hbm2java
 */
@Entity(name = "kegg_enzyme_effector")
@Table(name = "kegg_enzyme_effector")
public class KeggEnzymeEffector implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6375722998434536015L;

    private KeggEnzymeEffectorId id;

    private KeggEnzyme keggEnzyme;

    public KeggEnzymeEffector() {
    }

    public KeggEnzymeEffector(KeggEnzymeEffectorId id, KeggEnzyme keggEnzyme) {
        this.id = id;
        this.keggEnzyme = keggEnzyme;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entry", column = @Column(name = "entry", nullable = false)), @AttributeOverride(name = "effector", column = @Column(name = "effector", nullable = false)) })
    public KeggEnzymeEffectorId getId() {
        return this.id;
    }

    public void setId(KeggEnzymeEffectorId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false, insertable = false, updatable = false)
    public KeggEnzyme getKeggEnzyme() {
        return this.keggEnzyme;
    }

    public void setKeggEnzyme(KeggEnzyme keggEnzyme) {
        this.keggEnzyme = keggEnzyme;
    }
}

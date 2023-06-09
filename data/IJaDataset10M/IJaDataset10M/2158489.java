package edu.unibi.agbi.dawismd.entities.biodwh.kegg.medicus.disease;

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
 * KeggDiseasePathway generated by hbm2java
 */
@Entity(name = "kegg_disease_pathway")
@Table(name = "kegg_disease_pathway")
public class KeggDiseasePathway implements java.io.Serializable {

    private static final long serialVersionUID = -2931166485954622403L;

    private KeggDiseasePathwayId id;

    private KeggDisease keggDisease;

    public KeggDiseasePathway() {
    }

    public KeggDiseasePathway(KeggDiseasePathwayId id, KeggDisease keggDisease) {
        this.id = id;
        this.keggDisease = keggDisease;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entry", column = @Column(name = "entry", nullable = false, length = 6)), @AttributeOverride(name = "org", column = @Column(name = "org", nullable = false, length = 4)), @AttributeOverride(name = "number", column = @Column(name = "number", nullable = false, length = 8)) })
    public KeggDiseasePathwayId getId() {
        return this.id;
    }

    public void setId(KeggDiseasePathwayId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false, insertable = false, updatable = false)
    public KeggDisease getKeggDisease() {
        return this.keggDisease;
    }

    public void setKeggDisease(KeggDisease keggDisease) {
        this.keggDisease = keggDisease;
    }
}

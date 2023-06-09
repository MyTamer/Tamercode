package edu.unibi.agbi.dawismd.entities.biodwh.kegg.ligand.compound;

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
 * KeggCompoundReaction generated by hbm2java
 */
@Entity(name = "kegg_compound_reaction")
@Table(name = "kegg_compound_reaction")
public class KeggCompoundReaction implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1206747447127774868L;

    private KeggCompoundReactionId id;

    private KeggCompound keggCompound;

    public KeggCompoundReaction() {
    }

    public KeggCompoundReaction(KeggCompoundReactionId id, KeggCompound keggCompound) {
        this.id = id;
        this.keggCompound = keggCompound;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entry", column = @Column(name = "entry", nullable = false)), @AttributeOverride(name = "reaction", column = @Column(name = "reaction", nullable = false)) })
    public KeggCompoundReactionId getId() {
        return this.id;
    }

    public void setId(KeggCompoundReactionId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false, insertable = false, updatable = false)
    public KeggCompound getKeggCompound() {
        return this.keggCompound;
    }

    public void setKeggCompound(KeggCompound keggCompound) {
        this.keggCompound = keggCompound;
    }
}

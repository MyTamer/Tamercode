package edu.unibi.agbi.biodwh.entity.kegg.genes.genome;

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
 * KeggGenomeStatistics generated by hbm2java
 */
@Entity(name = "kegg_genome_statistics")
@Table(name = "kegg_genome_statistics")
public class KeggGenomeStatistics implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7558012866895016751L;

    private KeggGenomeStatisticsId id;

    private KeggGenome keggGenome;

    public KeggGenomeStatistics() {
    }

    public KeggGenomeStatistics(KeggGenomeStatisticsId id, KeggGenome keggGenome) {
        this.id = id;
        this.keggGenome = keggGenome;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entry", column = @Column(name = "entry", nullable = false)), @AttributeOverride(name = "statistics", column = @Column(name = "statistics", nullable = false)), @AttributeOverride(name = "number", column = @Column(name = "number", nullable = false)) })
    public KeggGenomeStatisticsId getId() {
        return this.id;
    }

    public void setId(KeggGenomeStatisticsId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false, insertable = false, updatable = false)
    public KeggGenome getKeggGenome() {
        return this.keggGenome;
    }

    public void setKeggGenome(KeggGenome keggGenome) {
        this.keggGenome = keggGenome;
    }
}

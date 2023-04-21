package org.ostion.siplacad.model.entity;

import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.Restrict;
import org.ostion.siplacad.model.Estado;
import org.ostion.siplacad.model.TipoParaleloDirigido;
import org.ostion.util.OstionEntity;

/**
 * ParaleloCarrera generated by hbm2java
 */
@Entity
@Table(name = "paralelo_dirigido", catalog = "siplacaddb", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_paralelo", "id_carrera" }), @UniqueConstraint(columnNames = { "id_paralelo", "id_unidad" }) })
@Restrict
public class ParaleloDirigido extends OstionEntity<ParaleloDirigido, Integer> implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5909842243930568813L;

    private Integer id;

    private Paralelo paralelo;

    private Carrera carrera;

    private Unidad unidad;

    /**
	 * Enumerated
	 * @since 2010-05-02
	 */
    private TipoParaleloDirigido tipo;

    private String observaciones;

    private Estado estado;

    public ParaleloDirigido() {
    }

    public ParaleloDirigido(Paralelo paralelo, TipoParaleloDirigido tipo, Estado estado) {
        this.paralelo = paralelo;
        this.tipo = tipo;
        this.estado = estado;
    }

    public ParaleloDirigido(Paralelo paralelo, Carrera carrera, Unidad unidad, TipoParaleloDirigido tipo, String observaciones, Estado estado) {
        this.paralelo = paralelo;
        this.carrera = carrera;
        this.unidad = unidad;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.estado = estado;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paralelo", nullable = false)
    @NotNull
    public Paralelo getParalelo() {
        return this.paralelo;
    }

    public void setParalelo(Paralelo paralelo) {
        this.paralelo = paralelo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera")
    public Carrera getCarrera() {
        return this.carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad")
    public Unidad getUnidad() {
        return this.unidad;
    }

    public void setUnidad(Unidad unidad) {
        this.unidad = unidad;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    @NotNull
    public TipoParaleloDirigido getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoParaleloDirigido tipo) {
        this.tipo = tipo;
    }

    @Column(name = "observaciones")
    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    @NotNull
    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
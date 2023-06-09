package com.movilnet.clom.framework.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Equipo generated by hbm2java
 */
@Entity
@Table(name = "equipo")
@SequenceGenerator(name = "equipo_equipoid_seq", sequenceName = "equipo_equipoid_seq")
@NamedQueries({ @NamedQuery(name = "equipo.getDeviceMainBySerial", query = "from Equipo e inner join fetch e.serials left join fetch e.cliente " + "inner join fetch e.modelo inner join fetch e.modelo.marca " + "inner join fetch e.color where e.id = (select ei.equipo.id from Serial ei where ei.id= :serialid)"), @NamedQuery(name = "equipo.getDeviceById", query = "from Equipo e inner join fetch e.serials left join fetch e.cliente " + "inner join fetch e.modelo inner join fetch e.modelo.marca " + "inner join fetch e.color where e.id = :equipoId"), @NamedQuery(name = "equipo.getByContenedorId", query = "from Equipo as e where e.contenedor.id=:contenedorId") })
public class Equipo implements Serializable {

    private static final long serialVersionUID = -5086320129812121119L;

    private Long id;

    private Modelo modelo;

    private Color color;

    private Cliente cliente;

    private Date fechaactivacionlinea;

    private Date fechaventafinal;

    private Date fechacompramarca;

    private Integer numeroasignado;

    private Double preciocompra;

    private Double precioventa;

    private Contenedor contenedor;

    private List<OrdenGarantia> ordenGarantias = new ArrayList<OrdenGarantia>(0);

    private List<Serial> serials = new ArrayList<Serial>(0);

    public Equipo() {
    }

    public Equipo(Long id, Modelo modelo, Color color, Cliente cliente, Date fechaactivacionlinea, Date fechaventafinal, Date fechacompramarca, Integer numeroasignado, Double preciocompra, Double precioventa, List<OrdenGarantia> ordenGarantias, List<Serial> serials, Contenedor contenedor) {
        this.id = id;
        this.modelo = modelo;
        this.color = color;
        this.cliente = cliente;
        this.fechaactivacionlinea = fechaactivacionlinea;
        this.fechaventafinal = fechaventafinal;
        this.fechacompramarca = fechacompramarca;
        this.numeroasignado = numeroasignado;
        this.preciocompra = preciocompra;
        this.precioventa = precioventa;
        this.ordenGarantias = ordenGarantias;
        this.serials = serials;
        this.setContenedor(contenedor);
    }

    @Id
    @Column(name = "equipoid", unique = true, nullable = false)
    @GeneratedValue(generator = "equipo_equipoid_seq")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modeloid", nullable = false)
    public Modelo getModelo() {
        return this.modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colorid", nullable = false)
    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clienteid", nullable = true)
    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaactivacionlinea", length = 13)
    public Date getFechaactivacionlinea() {
        return this.fechaactivacionlinea;
    }

    public void setFechaactivacionlinea(Date fechaactivacionlinea) {
        this.fechaactivacionlinea = fechaactivacionlinea;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "fechaventafinal", length = 13)
    public Date getFechaventafinal() {
        return this.fechaventafinal;
    }

    public void setFechaventafinal(Date fechaventafinal) {
        this.fechaventafinal = fechaventafinal;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "fechacompramarca", length = 13)
    public Date getFechacompramarca() {
        return this.fechacompramarca;
    }

    public void setFechacompramarca(Date fechacompramarca) {
        this.fechacompramarca = fechacompramarca;
    }

    @Column(name = "numeroasignado")
    public Integer getNumeroasignado() {
        return this.numeroasignado;
    }

    public void setNumeroasignado(Integer numeroasignado) {
        this.numeroasignado = numeroasignado;
    }

    @Column(name = "preciocompra", scale = 0)
    public Double getPreciocompra() {
        return this.preciocompra;
    }

    public void setPreciocompra(Double preciocompra) {
        this.preciocompra = preciocompra;
    }

    @Column(name = "precioventa", scale = 0)
    public Double getPrecioventa() {
        return this.precioventa;
    }

    public void setPrecioventa(Double precioventa) {
        this.precioventa = precioventa;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipo")
    public List<OrdenGarantia> getOrdenGarantias() {
        return this.ordenGarantias;
    }

    public void setOrdenGarantias(List<OrdenGarantia> ordenGarantias) {
        this.ordenGarantias = ordenGarantias;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipo")
    public List<Serial> getSerials() {
        return this.serials;
    }

    public void setSerials(List<Serial> serials) {
        this.serials = serials;
    }

    public void setContenedor(Contenedor contenedor) {
        this.contenedor = contenedor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenedorid", nullable = true)
    public Contenedor getContenedor() {
        return contenedor;
    }
}

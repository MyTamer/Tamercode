package es.caib.zonaper.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class EntradaPreregistroBackup {

    private Long codigo;

    private String numeroPreregistro;

    private Date fecha;

    private Date fechaCaducidad;

    private String idPersistencia;

    private char tipo;

    private char nivelAutenticacion;

    private String usuario;

    private String descripcionTramite;

    private long codigoRdsAsiento;

    private String claveRdsAsiento;

    private long codigoRdsJustificante;

    private String claveRdsJustificante;

    private String numeroRegistro;

    private Date fechaConfirmacion;

    private Set documentosBackup = new HashSet(0);

    private String idioma;

    private String nifRepresentante;

    private String nombreRepresentante;

    private String nifRepresentado;

    private String nombreRepresentado;

    private String tramite;

    private int version;

    private char confirmadoAutomaticamente;

    private String habilitarAvisos;

    private String avisoSMS;

    private String avisoEmail;

    public void addDocumentoBackup(DocumentoEntradaPreregistroBackup doc) {
        doc.setEntradaPreregistroBackup(this);
        documentosBackup.add(doc);
    }

    public void removeDocumentoBackup(DocumentoEntradaPreregistroBackup doc) {
        documentosBackup.remove(doc);
    }

    public String getClaveRdsAsiento() {
        return claveRdsAsiento;
    }

    public void setClaveRdsAsiento(String claveRdsAsiento) {
        this.claveRdsAsiento = claveRdsAsiento;
    }

    public String getClaveRdsJustificante() {
        return claveRdsJustificante;
    }

    public void setClaveRdsJustificante(String claveRdsJustificante) {
        this.claveRdsJustificante = claveRdsJustificante;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public long getCodigoRdsAsiento() {
        return codigoRdsAsiento;
    }

    public void setCodigoRdsAsiento(long codigoRdsAsiento) {
        this.codigoRdsAsiento = codigoRdsAsiento;
    }

    public long getCodigoRdsJustificante() {
        return codigoRdsJustificante;
    }

    public void setCodigoRdsJustificante(long codigoRdsJustificante) {
        this.codigoRdsJustificante = codigoRdsJustificante;
    }

    public Set getDocumentosBackup() {
        return documentosBackup;
    }

    public void setDocumentosBackup(Set documentos) {
        this.documentosBackup = documentos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public char getNivelAutenticacion() {
        return nivelAutenticacion;
    }

    public void setNivelAutenticacion(char nivelAutenticacion) {
        this.nivelAutenticacion = nivelAutenticacion;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdPersistencia() {
        return idPersistencia;
    }

    public void setIdPersistencia(String idPersistencia) {
        this.idPersistencia = idPersistencia;
    }

    public String getDescripcionTramite() {
        return descripcionTramite;
    }

    public void setDescripcionTramite(String descripcionTramite) {
        this.descripcionTramite = descripcionTramite;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public Date getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(Date fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getNumeroPreregistro() {
        return numeroPreregistro;
    }

    public void setNumeroPreregistro(String numeroPreregistro) {
        this.numeroPreregistro = numeroPreregistro;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getNifRepresentado() {
        return nifRepresentado;
    }

    public void setNifRepresentado(String nifRepresentado) {
        this.nifRepresentado = nifRepresentado;
    }

    public String getNifRepresentante() {
        return nifRepresentante;
    }

    public void setNifRepresentante(String nifRepresentante) {
        this.nifRepresentante = nifRepresentante;
    }

    public String getNombreRepresentado() {
        return nombreRepresentado;
    }

    public void setNombreRepresentado(String nombreRepresentado) {
        this.nombreRepresentado = nombreRepresentado;
    }

    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    public void setNombreRepresentante(String nombreRepresentante) {
        this.nombreRepresentante = nombreRepresentante;
    }

    public String getTramite() {
        return tramite;
    }

    public void setTramite(String tramite) {
        this.tramite = tramite;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public char getConfirmadoAutomaticamente() {
        return confirmadoAutomaticamente;
    }

    public void setConfirmadoAutomaticamente(char confirmadoAutomaticamente) {
        this.confirmadoAutomaticamente = confirmadoAutomaticamente;
    }

    public String getAvisoEmail() {
        return avisoEmail;
    }

    public void setAvisoEmail(String avisoEmail) {
        this.avisoEmail = avisoEmail;
    }

    public String getAvisoSMS() {
        return avisoSMS;
    }

    public void setAvisoSMS(String avisoSMS) {
        this.avisoSMS = avisoSMS;
    }

    public String getHabilitarAvisos() {
        return habilitarAvisos;
    }

    public void setHabilitarAvisos(String habilitarAvisos) {
        this.habilitarAvisos = habilitarAvisos;
    }
}

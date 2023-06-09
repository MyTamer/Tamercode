package model;

import java.util.Date;

/**
 * TblConsultaId generated by hbm2java
 */
public class TblConsultaId implements java.io.Serializable {

    private int idConsulta;

    private int fkProntuario;

    private int fkMedico;

    private Date dtData;

    public TblConsultaId() {
    }

    public TblConsultaId(int idConsulta, int fkProntuario, int fkMedico, Date dtData) {
        this.idConsulta = idConsulta;
        this.fkProntuario = fkProntuario;
        this.fkMedico = fkMedico;
        this.dtData = dtData;
    }

    public int getIdConsulta() {
        return this.idConsulta;
    }

    public void setIdConsulta(int idConsulta) {
        this.idConsulta = idConsulta;
    }

    public int getFkProntuario() {
        return this.fkProntuario;
    }

    public void setFkProntuario(int fkProntuario) {
        this.fkProntuario = fkProntuario;
    }

    public int getFkMedico() {
        return this.fkMedico;
    }

    public void setFkMedico(int fkMedico) {
        this.fkMedico = fkMedico;
    }

    public Date getDtData() {
        return this.dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof TblConsultaId)) return false;
        TblConsultaId castOther = (TblConsultaId) other;
        return (this.getIdConsulta() == castOther.getIdConsulta()) && (this.getFkProntuario() == castOther.getFkProntuario()) && (this.getFkMedico() == castOther.getFkMedico()) && ((this.getDtData() == castOther.getDtData()) || (this.getDtData() != null && castOther.getDtData() != null && this.getDtData().equals(castOther.getDtData())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getIdConsulta();
        result = 37 * result + this.getFkProntuario();
        result = 37 * result + this.getFkMedico();
        result = 37 * result + (getDtData() == null ? 0 : this.getDtData().hashCode());
        return result;
    }
}

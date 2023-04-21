package com.icteam.fiji.model;

/**
 * TipLocazSoc generated by hbm2java
 */
public class TipLocazSoc extends Auditable implements java.io.Serializable {

    private Long CTipLocazSoc;

    private String NTipLocazSoc;

    /**
    * default constructor
    */
    public TipLocazSoc() {
    }

    public Long getCTipLocazSoc() {
        return this.CTipLocazSoc;
    }

    public void setCTipLocazSoc(Long CTipLocazSoc) {
        if (CTipLocazSoc != null && CTipLocazSoc <= 0) this.CTipLocazSoc = null; else this.CTipLocazSoc = CTipLocazSoc;
    }

    public String getNTipLocazSoc() {
        return this.NTipLocazSoc;
    }

    public void setNTipLocazSoc(String NTipLocazSoc) {
        this.NTipLocazSoc = NTipLocazSoc;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipLocazSoc)) return false;
        TipLocazSoc that = (TipLocazSoc) o;
        Long tip = that.getCTipLocazSoc();
        if (CTipLocazSoc != null ? !CTipLocazSoc.equals(tip) : tip != null) return false;
        return true;
    }

    public int hashCode() {
        return (CTipLocazSoc != null ? CTipLocazSoc.hashCode() : 0);
    }
}
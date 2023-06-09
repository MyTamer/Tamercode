package ocumed.persistenz.hibernate;

import java.util.HashSet;
import java.util.Set;

/**
 * OcInstitut generated by hbm2java
 */
public class OcInstitut implements java.io.Serializable {

    private int institutid;

    private String institutname;

    private Set<OcPatient> ocPatients = new HashSet<OcPatient>(0);

    public OcInstitut() {
    }

    public OcInstitut(int institutid, String institutname) {
        this.institutid = institutid;
        this.institutname = institutname;
    }

    public OcInstitut(int institutid, String institutname, Set<OcPatient> ocPatients) {
        this.institutid = institutid;
        this.institutname = institutname;
        this.ocPatients = ocPatients;
    }

    public int getInstitutid() {
        return this.institutid;
    }

    public void setInstitutid(int institutid) {
        this.institutid = institutid;
    }

    public String getInstitutname() {
        return this.institutname;
    }

    public void setInstitutname(String institutname) {
        this.institutname = institutname;
    }

    public Set<OcPatient> getOcPatients() {
        return this.ocPatients;
    }

    public void setOcPatients(Set<OcPatient> ocPatients) {
        this.ocPatients = ocPatients;
    }
}

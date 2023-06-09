package com.cincosoft.project;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * Service generated by hbm2java
 */
@Entity
@Table(name = "service", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Service implements java.io.Serializable {

    private long id;

    private String name;

    private String display;

    private Usecase usecase;

    private String displayBis;

    private boolean activeInProfile = false;

    public Service() {
    }

    public Service(long id, String name, String display) {
        this.id = id;
        this.name = name;
        this.display = display;
    }

    public Service(long id, Usecase usecase, String name, String display) {
        this.id = id;
        this.usecase = usecase;
        this.name = name;
        this.display = display;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    @NotNull
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usecase_id")
    @NotNull
    public Usecase getUsecase() {
        return this.usecase;
    }

    public void setUsecase(Usecase usecase) {
        this.usecase = usecase;
    }

    @Column(name = "name", unique = true, nullable = false, length = 50)
    @NotNull
    @Length(max = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "display", nullable = false, length = 30)
    @NotNull
    @Length(max = 30)
    public String getDisplay() {
        return this.display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Transient
    public boolean isActiveInProfile() {
        return activeInProfile;
    }

    public void setActiveInProfile(boolean activeInProfile) {
        this.activeInProfile = activeInProfile;
    }

    @Transient
    public String getDisplayBis() {
        return displayBis;
    }

    public void setDisplayBis(String displayBis) {
        this.displayBis = displayBis;
    }

    @Override
    public String toString() {
        return "Service(" + id + "," + name + "," + display + ")";
    }
}

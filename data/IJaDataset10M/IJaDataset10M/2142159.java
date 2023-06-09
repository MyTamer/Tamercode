package com.tetratech.edas2.model;

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
 * ResultTaxonHabit generated by hbm2java
 */
@Entity
@Table(name = "ED_RESULT_TAXON_HABIT")
public class ResultTaxonHabit extends AttributableUpdateRetireEntity implements java.io.Serializable {

    private ResultTaxonHabitId id;

    private ResultTaxonDetail resultTaxonDetail;

    private Organization organization;

    private Citation citation;

    private Habit habit;

    public ResultTaxonHabit() {
    }

    public ResultTaxonHabit(ResultTaxonHabitId id, ResultTaxonDetail resultTaxonDetail, Organization organization, Habit habit) {
        this.id = id;
        this.resultTaxonDetail = resultTaxonDetail;
        this.organization = organization;
        this.habit = habit;
    }

    public ResultTaxonHabit(ResultTaxonHabitId id, ResultTaxonDetail resultTaxonDetail, Organization organization, Citation citation, Habit habit) {
        this.id = id;
        this.resultTaxonDetail = resultTaxonDetail;
        this.organization = organization;
        this.citation = citation;
        this.habit = habit;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "resUid", column = @Column(name = "RES_UID", nullable = false, precision = 20, scale = 0)), @AttributeOverride(name = "habitUid", column = @Column(name = "HABIT_UID", nullable = false, precision = 6, scale = 0)) })
    public ResultTaxonHabitId getId() {
        return this.id;
    }

    public void setId(ResultTaxonHabitId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RES_UID", nullable = false, insertable = false, updatable = false)
    public ResultTaxonDetail getResultTaxonDetail() {
        return this.resultTaxonDetail;
    }

    public void setResultTaxonDetail(ResultTaxonDetail resultTaxonDetail) {
        this.resultTaxonDetail = resultTaxonDetail;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_UID", nullable = false)
    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITATN_UID")
    public Citation getCitation() {
        return this.citation;
    }

    public void setCitation(Citation citation) {
        this.citation = citation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HABIT_UID", nullable = false, insertable = false, updatable = false)
    public Habit getHabit() {
        return this.habit;
    }

    public void setHabit(Habit habit) {
        this.habit = habit;
    }
}

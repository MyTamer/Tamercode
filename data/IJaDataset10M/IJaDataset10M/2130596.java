package de.objectcode.time4u.migrator.server05.old.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * OldTeams generated by hbm2java
 */
@Entity
@Table(name = "TEAMS")
public class OldTeams implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private long ownerId;

    private Long revision;

    public OldTeams() {
    }

    public OldTeams(String name, long ownerId) {
        this.name = name;
        this.ownerId = ownerId;
    }

    public OldTeams(String name, long ownerId, Long revision) {
        this.name = name;
        this.ownerId = ownerId;
        this.revision = revision;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "owner_id", nullable = false)
    public long getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @Column(name = "revision")
    public Long getRevision() {
        return this.revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }
}

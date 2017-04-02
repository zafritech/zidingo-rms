package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "XREF_DOCUMENTS")
public class ArtifactHierachy implements Serializable {

    private static final long serialVersionUID = -463724910897591213L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @ManyToOne
    @JoinColumn(name = "artifactId")
    private Artifact artifact;

    @OneToOne
    @JoinColumn(name = "itemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Item parent;

    private int sortIndex;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public ArtifactHierachy() {
    }

    public ArtifactHierachy(Artifact artifact,
            Item item,
            Item parent) {

        this.uuId = UUID.randomUUID().toString();
        this.artifact = artifact;
        this.item = item;
        this.parent = parent;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public ArtifactHierachy(Artifact artifact,
            Item item,
            Item parent,
            int index) {

        this.uuId = UUID.randomUUID().toString();
        this.artifact = artifact;
        this.item = item;
        this.parent = parent;
        this.sortIndex = index;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the uuId
     */
    public String getUuId() {
        return uuId;
    }

    /**
     * @param uuId the uuId to set
     */
    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    /**
     * @return the artifact
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * @param artifact the artifact to set
     */
    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * @return the parent
     */
    public Item getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Item parent) {
        this.parent = parent;
    }

    /**
     * @return the sortIndex
     */
    public int getSortIndex() {
        return sortIndex;
    }

    /**
     * @param sortIndex the sortIndex to set
     */
    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param modifiedDate the modifiedDate to set
     */
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

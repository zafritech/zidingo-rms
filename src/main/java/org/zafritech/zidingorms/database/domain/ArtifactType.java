package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "REF_ARTIFACT_TYPES")
public class ArtifactType implements Serializable {

    private static final long serialVersionUID = 2681977956968103631L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @Column(nullable = false)
    private String artifactTypeName;

    private String artifactTypeLongName;

    @Column(columnDefinition = "TEXT")
    private String artifactTypeDescription;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Override
    public String toString() {
        return "Artifact Type {"
                + "ID: " + getId()
                + ", Artifact Type Name= '" + getArtifactTypeName() + '\''
                + ", Artifact Type Long Name= '" + getArtifactTypeLongName() + '\''
                + '}';
    }

    public ArtifactType() {
    }

    public ArtifactType(String typeName) {

        this.uuId = UUID.randomUUID().toString();
        this.artifactTypeName = typeName;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public ArtifactType(String typeName, String typeLongName, String typeDescription) {

        this.uuId = UUID.randomUUID().toString();
        this.artifactTypeName = typeName;
        this.artifactTypeLongName = typeLongName;
        this.artifactTypeDescription = typeDescription;
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
     * @return the artifactTypeName
     */
    public String getArtifactTypeName() {
        return artifactTypeName;
    }

    /**
     * @param artifactTypeName the artifactTypeName to set
     */
    public void setArtifactTypeName(String artifactTypeName) {
        this.artifactTypeName = artifactTypeName;
    }

    /**
     * @return the artifactTypeLongName
     */
    public String getArtifactTypeLongName() {
        return artifactTypeLongName;
    }

    /**
     * @param artifactTypeLongName the artifactTypeLongName to set
     */
    public void setArtifactTypeLongName(String artifactTypeLongName) {
        this.artifactTypeLongName = artifactTypeLongName;
    }

    /**
     * @return the artifactTypeDescription
     */
    public String getArtifactTypeDescription() {
        return artifactTypeDescription;
    }

    /**
     * @param artifactTypeDescription the artifactTypeDescription to set
     */
    public void setArtifactTypeDescription(String artifactTypeDescription) {
        this.artifactTypeDescription = artifactTypeDescription;
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

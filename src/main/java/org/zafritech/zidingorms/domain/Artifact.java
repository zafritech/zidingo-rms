package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.zafritech.zidingorms.commons.enums.ArtifactStatus;

@Entity(name = "TBL_ARTIFACTS")
public class Artifact implements Serializable {

    private static final long serialVersionUID = -3931394851232097879L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    private String identifier;

    private String artifactName;

    private String artifactLongName;

    @Column(columnDefinition = "TEXT")
    private String artifactDescription;

    @ManyToOne
    private ArtifactType artifactType;

    @ManyToOne
    private Project artifactProject;

    @ManyToOne
    private Folder artifactFolder;
    
    @Enumerated(EnumType.STRING)
    private ArtifactStatus artifactStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    public Artifact() {
    }

    public Artifact(String identifier,
            String artifactName,
            ArtifactType artifactType,
            Project artifactProject,
            Folder artifactFolder) {

        this.uuId = UUID.randomUUID().toString();
        this.identifier = identifier;
        this.artifactName = artifactName;
        this.artifactType = artifactType;
        this.artifactProject = artifactProject;
        this.artifactFolder = artifactFolder;
        this.artifactStatus = ArtifactStatus.DRAFT;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public Artifact(String identifier,
            String artifactName,
            String artifactLongName,
            ArtifactType artifactType,
            Project artifactProject,
            Folder artifactFolder) {

        this.uuId = UUID.randomUUID().toString();
        this.identifier = identifier;
        this.artifactName = artifactName;
        this.artifactLongName = artifactLongName;
        this.artifactType = artifactType;
        this.artifactProject = artifactProject;
        this.artifactFolder = artifactFolder;
        this.artifactStatus = ArtifactStatus.DRAFT;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Artifact {"
                + "id: " + getId()
                + ", artifactName = '" + getArtifactName() + '\''
                + '}';
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
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the artifactName
     */
    public String getArtifactName() {
        return artifactName;
    }

    /**
     * @param artifactName the artifactName to set
     */
    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    /**
     * @return the artifactLongName
     */
    public String getArtifactLongName() {
        return artifactLongName;
    }

    /**
     * @param artifactLongName the artifactLongName to set
     */
    public void setArtifactLongName(String artifactLongName) {
        this.artifactLongName = artifactLongName;
    }

    /**
     * @return the artifactDescription
     */
    public String getArtifactDescription() {
        return artifactDescription;
    }

    /**
     * @param artifactDescription the artifactDescription to set
     */
    public void setArtifactDescription(String artifactDescription) {
        this.artifactDescription = artifactDescription;
    }

    /**
     * @return the artifactType
     */
    public ArtifactType getArtifactType() {
        return artifactType;
    }

    /**
     * @param artifactType the artifactType to set
     */
    public void setArtifactType(ArtifactType artifactType) {
        this.artifactType = artifactType;
    }

    /**
     * @return the artifactProject
     */
    public Project getArtifactProject() {
        return artifactProject;
    }

    /**
     * @param artifactProject the artifactProject to set
     */
    public void setArtifactProject(Project artifactProject) {
        this.artifactProject = artifactProject;
    }

    /**
     * @return the artifactFolder
     */
    public Folder getArtifactFolder() {
        return artifactFolder;
    }

    /**
     * @param artifactFolder the artifactFolder to set
     */
    public void setArtifactFolder(Folder artifactFolder) {
        this.artifactFolder = artifactFolder;
    }

    public ArtifactStatus getArtifactStatus() {
        return artifactStatus;
    }

    public void setArtifactStatus(ArtifactStatus artifactStatus) {
        this.artifactStatus = artifactStatus;
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

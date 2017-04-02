package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_FOLDERS")
public class Folder implements Serializable {

    private static final long serialVersionUID = -7335525087566303043L;

    @Id
    @GeneratedValue
    private Long Id;

    private String uuId;

    @Column(nullable = false)
    private String folderName;

    private String folderType;

    @ManyToOne
    @JoinColumn(name = "parentId")
    private Folder parent;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Override
    public String toString() {
        return "Folder {"
                + "ID: " + getId()
                + ", Folder Name = '" + getFolderName() + '\''
                + ", Folder Type = '" + getFolderType() + '\''
                + '}';
    }

    public Folder() {
    }

    public Folder(String folderName, String folderType, Project project) {

        this.uuId = UUID.randomUUID().toString();
        this.folderName = folderName;
        this.folderType = folderType;
        this.parent = null;
        this.project = project;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public Folder(String folderName, String folderType, Folder parent, Project project) {

        this.uuId = UUID.randomUUID().toString();
        this.folderName = folderName;
        this.folderType = folderType;
        this.parent = parent;
        this.project = project;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    /**
     * @return the Id
     */
    public Long getId() {
        return Id;
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
     * @return the folderName
     */
    public String getFolderName() {
        return folderName;
    }

    /**
     * @param folderName the folderName to set
     */
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    /**
     * @return the folderType
     */
    public String getFolderType() {
        return folderType;
    }

    /**
     * @param folderType the folderType to set
     */
    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    /**
     * @return the parent
     */
    public Folder getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Folder parent) {
        this.parent = parent;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

}

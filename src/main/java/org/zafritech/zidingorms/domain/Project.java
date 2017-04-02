package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_PROJECTS")
public class Project implements Serializable {

    private static final long serialVersionUID = -1267086640569786293L;

    @Id
    @GeneratedValue
    private Long Id;

    private String uuId;

    private String projectName;

    private String projectShortName;

    @Column(columnDefinition = "TEXT")
    private String projectDescription;

    @ManyToOne
    private Company projectCompany;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Project() {
    }

    public Project(String name,
            String shortName,
            Company company) {

        this.uuId = UUID.randomUUID().toString();
        this.projectName = name;
        this.projectShortName = shortName;
        this.projectCompany = company;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());

    }

    @Override
    public String toString() {

        return "Project {"
                + "ID: " + getId()
                + ", Project Name = '" + getProjectName() + '\''
                + ", Project Type = '" + getProjectShortName() + '\''
                + '}';
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
     * @return the projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * @return the projectShortName
     */
    public String getProjectShortName() {
        return projectShortName;
    }

    /**
     * @param projectShortName the projectShortName to set
     */
    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    /**
     * @return the projectDescription
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * @param projectDescription the projectDescription to set
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * @return the projectCompany
     */
    public Company getProjectCompany() {
        return projectCompany;
    }

    /**
     * @param projectCompany the projectCompany to set
     */
    public void setProjectCompany(Company projectCompany) {
        this.projectCompany = projectCompany;
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

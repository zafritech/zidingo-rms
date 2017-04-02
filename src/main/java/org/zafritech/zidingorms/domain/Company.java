package org.zafritech.zidingorms.domain;

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

@Entity(name = "TBL_COMPANIES")
public class Company implements Serializable {

	private static final long serialVersionUID = -2569109922025524359L;

	@Id @GeneratedValue
    private Long Id;
    
    private String uuId;
    
    private String companyName;
    
    private String companyShortName;
    
    @Column(columnDefinition = "TEXT")
    private String companyRoleDescription;
    
    private byte[] companyLogo;
    
    private Contact contact;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    public Company() {
    }

    public Company(String name, String shortName) {
        
        this.uuId = UUID.randomUUID().toString();
        this.companyName = name;
        this.companyShortName = shortName;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public Company(String name, String shortName, Contact contact) {
        
        this.uuId = UUID.randomUUID().toString();
        this.companyName = name;
        this.companyShortName = shortName;
        this.contact = contact;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
        
    }

    @Override
    public String toString() {
        
        return "Project {"
                + "ID: " + getId()
                + ", Project Name = '" + getCompanyName() + '\''
                + ", Project Type = '" + getCompanyShortName() + '\''
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
     * @param UuId the uuId to set
     */
    public void setUuId(String UuId) {
        this.uuId = UuId;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param CompanyName the companyName to set
     */
    public void setCompanyName(String CompanyName) {
        this.companyName = CompanyName;
    }

    /**
     * @return the companyShortName
     */
    public String getCompanyShortName() {
        return companyShortName;
    }

    /**
     * @param CompanyShortName the companyShortName to set
     */
    public void setCompanyShortName(String CompanyShortName) {
        this.companyShortName = CompanyShortName;
    }

    /**
     * @return the companyRoleDescription
     */
    public String getCompanyRoleDescription() {
        return companyRoleDescription;
    }

    /**
     * @param CompanyRoleDescription the companyRoleDescription to set
     */
    public void setCompanyRoleDescription(String CompanyRoleDescription) {
        this.companyRoleDescription = CompanyRoleDescription;
    }

    /**
     * @return the companyLogo
     */
    public byte[] getCompanyLogo() {
        return companyLogo;
    }

    /**
     * @param CompanyLogo the companyLogo to set
     */
    public void setCompanyLogo(byte[] CompanyLogo) {
        this.companyLogo = CompanyLogo;
    }

    /**
     * @return the companyContact
     */
    public Contact geContact() {
        return contact;
    }

    /**
     * @param CompanyContact the companyContact to set
     */
    public void setContact(Contact CompanyContact) {
        this.contact = CompanyContact;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param CreationDate the creationDate to set
     */
    public void setCreationDate(Date CreationDate) {
        this.creationDate = CreationDate;
    }

    /**
     * @return the modifiedDate
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * @param ModifiedDate the modifiedDate to set
     */
    public void setModifiedDate(Date ModifiedDate) {
        this.modifiedDate = ModifiedDate;
    }
}

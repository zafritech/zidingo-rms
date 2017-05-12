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

@Entity(name = "REF_LINK_TYPES")
public class LinkType implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private String linkTypeName;
    
    @Column(columnDefinition = "TEXT")
    private String linkTypeLongName;
    
    @Column(columnDefinition = "TEXT")
    private String linkTypeDescription;
    
    private boolean enabled;
    
    private boolean defaultType;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public LinkType() {
        
    }

    public LinkType(String linkTypeName, String linkTypeLongName) {
        
        this.uuId = UUID.randomUUID().toString();
        this.linkTypeName = linkTypeName;
        this.linkTypeLongName = linkTypeLongName;
        this.linkTypeDescription = null;
        this.enabled = true;
        this.defaultType = false;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public LinkType(String linkTypeName, String linkTypeLongName, String linkTypeDescription) {
        
        this.uuId = UUID.randomUUID().toString();
        this.linkTypeName = linkTypeName;
        this.linkTypeLongName = linkTypeLongName;
        this.linkTypeDescription = linkTypeDescription;
        this.enabled = true;
        this.defaultType = false;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public LinkType(String linkTypeName, String linkTypeLongName, String linkTypeDescription, boolean defaulType) {
        
        this.uuId = UUID.randomUUID().toString();
        this.linkTypeName = linkTypeName;
        this.linkTypeLongName = linkTypeLongName;
        this.linkTypeDescription = linkTypeDescription;
        this.enabled = true;
        this.defaultType = defaulType;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        
        return "LinkType{" + "id=" + id + 
               ", linkTypeName=" + linkTypeName + ", linkTypeLongName=" + 
               linkTypeLongName + ", creationDate=" + creationDate + '}';
    }
    
    public Long getId() {
        return id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getLinkTypeName() {
        return linkTypeName;
    }

    public void setLinkTypeName(String linkTypeName) {
        this.linkTypeName = linkTypeName;
    }

    public String getLinkTypeLongName() {
        return linkTypeLongName;
    }

    public void setLinkTypeLongName(String linkTypeLongName) {
        this.linkTypeLongName = linkTypeLongName;
    }

    public String getLinkTypeDescription() {
        return linkTypeDescription;
    }

    public void setLinkTypeDescription(String linkTypeDescription) {
        this.linkTypeDescription = linkTypeDescription;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isDefaultType() {
        return defaultType;
    }

    public void setDefaultType(boolean defaultType) {
        this.defaultType = defaultType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

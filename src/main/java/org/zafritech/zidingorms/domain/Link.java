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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_LINKS")
public class Link implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @ManyToOne
    @JoinColumn(name = "srcItemId")
    private Item srcItem;
    
    @ManyToOne
    @JoinColumn(name = "srcArtifactId")
    private Artifact srcArtifact;
    
    @ManyToOne
    @JoinColumn(name = "dstItemId")
    private Item dstItem;
    
    @ManyToOne
    @JoinColumn(name = "dstArtifactId")
    private Artifact dstArtifact;
    
    @ManyToOne
    @JoinColumn(name = "linkTypeId")
    private LinkType linkType;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public Link() {
        
    }

    public Link(Item srcItem, 
                Item dstItem, 
                LinkType linkType) {
        
        this.uuId = UUID.randomUUID().toString();
        this.srcItem = srcItem;
        this.dstItem = dstItem;
        this.linkType = linkType;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public Link(Item srcItem, 
                Artifact srcArtifact, 
                Item dstItem, 
                Artifact dstArtifact, 
                LinkType linkType) {
        
        this.uuId = UUID.randomUUID().toString();
        this.srcItem = srcItem;
        this.srcArtifact = srcArtifact;
        this.dstItem = dstItem;
        this.dstArtifact = dstArtifact;
        this.linkType = linkType;
        this.creationDate = new Timestamp(System.currentTimeMillis());
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

    public Item getSrcItem() {
        return srcItem;
    }

    public void setSrcItem(Item srcItem) {
        this.srcItem = srcItem;
    }

    public Artifact getSrcArtifact() {
        return srcArtifact;
    }

    public void setSrcArtifact(Artifact srcArtifact) {
        this.srcArtifact = srcArtifact;
    }

    public Item getDstItem() {
        return dstItem;
    }

    public void setDstItem(Item dstItem) {
        this.dstItem = dstItem;
    }

    public Artifact getDstArtifact() {
        return dstArtifact;
    }

    public void setDstArtifact(Artifact dstArtifact) {
        this.dstArtifact = dstArtifact;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

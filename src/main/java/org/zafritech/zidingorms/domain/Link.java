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
    private Item sourceItem;
    
    @ManyToOne
    @JoinColumn(name = "dstItemId")
    private Item destinationItem;
    
    @ManyToOne
    @JoinColumn(name = "linkTypeId")
    private LinkType linkType;
    
    @ManyToOne
    @JoinColumn(name = "linkGroupId")
    private LinkGroup linkGroup;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public Link() {
        
    }

    public Link(Item sourceItem, Item destinationItem, LinkType linkType) {
        
        this.uuId = UUID.randomUUID().toString();
        this.sourceItem = sourceItem;
        this.destinationItem = destinationItem;
        this.linkType = linkType;
    }

    public Link(Item sourceItem, Item destinationItem, LinkType linkType, LinkGroup linkGroup) {
        
        this.uuId = UUID.randomUUID().toString();
        this.sourceItem = sourceItem;
        this.destinationItem = destinationItem;
        this.linkType = linkType;
        this.linkGroup = linkGroup;
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

    public Item getSourceItem() {
        return sourceItem;
    }

    public void setSourceItem(Item sourceItem) {
        this.sourceItem = sourceItem;
    }

    public Item getDestinationItem() {
        return destinationItem;
    }

    public void setDestinationItem(Item destinationItem) {
        this.destinationItem = destinationItem;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public LinkGroup getLinkGroup() {
        return linkGroup;
    }

    public void setLinkGroup(LinkGroup linkGroup) {
        this.linkGroup = linkGroup;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

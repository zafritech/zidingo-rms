package org.zafritech.zidingorms.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.zafritech.zidingorms.commons.enums.MediaType;
import org.zafritech.zidingorms.domain.ItemType;

public class ItemDao {

    private Long id;

    private String uuId;

    private String sysId;

    @Column(columnDefinition = "TEXT")
    private String itemClass;

    private String identifier;

    private String itemValue;

    private ItemType itemType;

    private MediaType mediaType;

    private Long artifactId;

    private int itemLevel;

    private int sortIndex;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Override
    public String toString() {
        return "ItemDao [sysId=" + sysId + ", requirementClass=" + itemClass + ", identifier=" + identifier
                + ", itemValue=" + itemValue + ", itemLevel=" + itemLevel + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }

    public String getSysId() {
        return sysId;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String reqClass) {
        this.itemClass = reqClass;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Long getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(Long artifactId) {
        this.artifactId = artifactId;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

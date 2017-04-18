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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import org.zafritech.zidingorms.commons.enums.MediaType;

@Indexed
@Entity(name = "TBL_ITEMS")
public class Item implements Serializable {

    private static final long serialVersionUID = -7317466283245209471L;

    @Id
    @GeneratedValue
    private Long id;

    private String uuId;

    @Field
    private String sysId;

    @Column(columnDefinition = "TEXT")
    private String itemClass;

    @Field
    private String identifier;

    @Field(store = Store.NO)
    @NotNull
    @Column(columnDefinition = "TEXT")
    private String itemValue;

    @ManyToOne
    @JoinColumn(name = "itemTypeId")
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @ManyToOne
    @JoinColumn(name = "artifactId")
    private Artifact artifact;

    private int linkCount;

    private boolean linkChanged;
    
    private int commentCount;

    private int itemLevel;

    private int sortIndex;

    private int itemVersion;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @Override
    public String toString() {
        return "A Item {"
                + "id:" + getId()
                + ", identifier = '" + getIdentifier()
                + ", Item Value = '" + getItemValue() + '\''
                + '}';
    }

    public Item() {
    }

    public Item(String sysId,
            String itemValue,
            ItemType itemType,
            Artifact parent) {

        this.uuId = UUID.randomUUID().toString();
        this.sysId = sysId;
        this.itemValue = itemValue;
        this.itemType = itemType;
        this.mediaType = MediaType.TEXT;
        this.artifact = parent;
        this.itemVersion = 1;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.modifiedDate = new Timestamp(System.currentTimeMillis());
    }

    public Item(String sysId,
            String identifier,
            String itemValue,
            ItemType itemType,
            MediaType mediaType,
            Artifact parent) {

        this.uuId = UUID.randomUUID().toString();
        this.sysId = sysId;
        this.identifier = identifier;
        this.itemValue = itemValue;
        this.itemType = itemType;
        this.mediaType = mediaType;
        this.artifact = parent;
        this.itemVersion = 1;
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

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String reqClass) {
        this.itemClass = reqClass;
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
     * @return the itemValue
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * @param itemValue the itemValue to set
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * @return the itemType
     */
    public ItemType getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(int itemVersion) {
        this.itemVersion = itemVersion;
    }

    /**
     * @return the mediaType
     */
    public MediaType getMediaType() {
        return mediaType;
    }

    /**
     * @param mediaType the mediaType to set
     */
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact parentArtifact) {
        this.artifact = parentArtifact;
    }

    public int getLinkCount() {
        return linkCount;
    }

    public void setLinkCount(int linkCount) {
        this.linkCount = linkCount;
    }

    public boolean isLinkChanged() {
        return linkChanged;
    }

    public void setLinkChanged(boolean linkChanged) {
        this.linkChanged = linkChanged;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
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

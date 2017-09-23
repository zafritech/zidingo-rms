package org.zafritech.zidingorms.database.domain;

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
import org.zafritech.zidingorms.core.commons.enums.ItemStatus;
import org.zafritech.zidingorms.core.commons.enums.MediaType;

@Indexed
@Entity(name = "TBL_ITEMS")
public class Item implements Serializable {

    private static long serialVersionUID = -7317466283245209471L;

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
    
    @ManyToOne
    @JoinColumn(name = "itemCategoryId")
    private ItemCategory itemCategory;

    private int linkCount;

    private boolean linkChanged;
    
    private int commentCount;

    private int itemLevel;

    private int sortIndex;

    private int itemVersion;
    
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
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

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
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

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
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

    public int getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(int itemVersion) {
        this.itemVersion = itemVersion;
    }

    public ItemStatus getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
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

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

@Entity(name = "REF_ITEM_TYPES")
public class ItemType implements Serializable {

	private static final long serialVersionUID = -5610201579805529720L;

	@Id @GeneratedValue
    private Long Id;
    
    private String uuId;
    
    private String itemTypeName;
    
    private String itemTypeLongName;
    
    @Column(columnDefinition = "TEXT")
    private String itemTypeDescription;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;
    
    public ItemType() {
    	super();
    }
    
    public ItemType(String itemTypeName) {
		super();
		this.itemTypeName = itemTypeName;
	}

	@Override
    public String toString() {
    	return "Item Type {"
    			+ "ID: " + getId()
    			+ ", Item Type Name = '" + getItemTypeName() + '\''
    			+ ", Item Type Long Name = '" + getItemTypeLongName() + '\''
    			+ ", Item Type Description = '" + getItemTypeDescription() + '\''
    			+ '}';
    }

    public ItemType(String itemTypeName, String itemTypeLongName, String itemTypeDescription) {
        
        this.uuId = UUID.randomUUID().toString();
        this.itemTypeName = itemTypeName;
        this.itemTypeLongName = itemTypeLongName;
        this.itemTypeDescription = itemTypeDescription;
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
    public String getUuid() {
        return uuId;
    }

    /**
     * @param Uuid the uuId to set
     */
    public void setUuid(String Uuid) {
        this.uuId = Uuid;
    }

    /**
     * @return the itemTypeName
     */
    public String getItemTypeName() {
        return itemTypeName;
    }

    /**
     * @param ItemTypeName the itemTypeName to set
     */
    public void setItemTypeName(String ItemTypeName) {
        this.itemTypeName = ItemTypeName;
    }

    /**
     * @return the itemTypeLongName
     */
    public String getItemTypeLongName() {
        return itemTypeLongName;
    }

    /**
     * @param ItemTypeLongName the itemTypeLongName to set
     */
    public void setItemTypeLongName(String ItemTypeLongName) {
        this.itemTypeLongName = ItemTypeLongName;
    }

    /**
     * @return the itemTypeDescription
     */
    public String getItemTypeDescription() {
        return itemTypeDescription;
    }

    /**
     * @param ItemTypeDescription the itemTypeDescription to set
     */
    public void setItemTypeDescription(String ItemTypeDescription) {
        this.itemTypeDescription = ItemTypeDescription;
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

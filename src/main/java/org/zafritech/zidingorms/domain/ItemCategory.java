package org.zafritech.zidingorms.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_ITEM_CATEGORIES")
public class ItemCategory implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private String categoryName;
    
    private String categoryCode;
    
    @ManyToOne
    @JoinColumn(name = "parentId")
    private ItemCategory parent;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "XREF_ITEM_CATEGORY_MEMBERS",
               joinColumns = {@JoinColumn(name = "item_category_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    @JsonBackReference
    private Set<User> categoryMembers = new HashSet<User>();

    @JoinColumn(name = "leadId")
    private User categoryLead;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public ItemCategory() {
        
    }

    public ItemCategory(String categoryName, String categoryCode) {
        
        this.uuId = UUID.randomUUID().toString();
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.createdDate = new Timestamp(System.currentTimeMillis());
    }

    public ItemCategory(String categoryName, String categoryCode, User categoryLead) {
        
        this.uuId = UUID.randomUUID().toString();
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.categoryLead = categoryLead;
        this.createdDate = new Timestamp(System.currentTimeMillis());
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public ItemCategory getParent() {
        return parent;
    }

    public void setParent(ItemCategory parent) {
        this.parent = parent;
    }

    public Set<User> getCategoryMembers() {
        return categoryMembers;
    }

    public void setCategoryMembers(Set<User> categoryMembers) {
        this.categoryMembers = categoryMembers;
    }

    public User getCategoryLead() {
        return categoryLead;
    }

    public void setCategoryLead(User categoryLead) {
        this.categoryLead = categoryLead;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

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
import org.zafritech.zidingorms.core.commons.enums.ItemStatus;

@Entity(name = "TBL_VERIFICATION_REFERENCES")
public class VerificationReference implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item item;
    
    @ManyToOne
    @JoinColumn(name = "verificationMethodId")
    private VerificationMethod method;
    
    @Column(columnDefinition = "TEXT")
    private String vvReferences;
    
    @Column(columnDefinition = "TEXT")
    private String vvEvidence;
    
    @Enumerated(EnumType.STRING)
    private ItemStatus vvStatus;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public VerificationReference() {
        
    }

    public VerificationReference(Item item, VerificationMethod method) {
        
        this.uuId = UUID.randomUUID().toString();
        this.item = item;
        this.method = method;
        this.creationDate = new Timestamp(System.currentTimeMillis());
    }

    public VerificationReference(Item item, VerificationMethod method, String vvReferences) {
        
        this.uuId = UUID.randomUUID().toString();
        this.item = item;
        this.method = method;
        this.vvReferences = vvReferences;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public VerificationMethod getMethod() {
        return method;
    }

    public void setMethod(VerificationMethod method) {
        this.method = method;
    }

    public String getVvReferences() {
        return vvReferences;
    }

    public void setVvReferences(String vvReferences) {
        this.vvReferences = vvReferences;
    }

    public String getVvEvidence() {
        return vvEvidence;
    }

    public void setVvEvidence(String vvEvidence) {
        this.vvEvidence = vvEvidence;
    }

    public ItemStatus getVvStatus() {
        return vvStatus;
    }

    public void setVvStatus(ItemStatus vvStatus) {
        this.vvStatus = vvStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

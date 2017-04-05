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

@Entity(name = "TBL_LINK_GROUPS")
public class LinkGroup implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @ManyToOne
    @JoinColumn(name = "srcArtifactId")
    private Artifact sourceArtificat;
    
    @ManyToOne
    @JoinColumn(name = "dstArtifactId")
    private Artifact destinationArtificat;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public LinkGroup() {
        
    }

    public LinkGroup(Artifact sourceArtificat, Artifact destinationArtificat) {
        
        this.uuId = UUID.randomUUID().toString();
        this.sourceArtificat = sourceArtificat;
        this.destinationArtificat = destinationArtificat;
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

    public Artifact getSourceArtificat() {
        return sourceArtificat;
    }

    public void setSourceArtificat(Artifact sourceArtificat) {
        this.sourceArtificat = sourceArtificat;
    }

    public Artifact getDestinationArtificat() {
        return destinationArtificat;
    }

    public void setDestinationArtificat(Artifact destinationArtificat) {
        this.destinationArtificat = destinationArtificat;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}

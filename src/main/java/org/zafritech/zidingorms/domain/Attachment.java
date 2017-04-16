package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "TBL_MESSAGE_ATTACHMENTS")
public class Attachment implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuid;
    
    private String attachmentName;
    
    private String attachmentPath;
    
    private String attachmentType;

    public Attachment() {
        
    }

    public Attachment(String attachmentName, String attachmentPath) {
        
        this.attachmentName = attachmentName;
        this.attachmentPath = attachmentPath;
    }

    public Attachment(String attachmentName, String attachmentPath, String attachmentType) {
        
        this.attachmentName = attachmentName;
        this.attachmentPath = attachmentPath;
        this.attachmentType = attachmentType;
    }

    public Long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }
}

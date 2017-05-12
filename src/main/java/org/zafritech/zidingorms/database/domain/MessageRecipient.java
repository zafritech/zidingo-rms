package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_MESSAGE_RECIPIENTS")
public class MessageRecipient implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User recipient;
    
    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;
    
    private boolean messageRead;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageReadDate;
    
    public MessageRecipient() {
        
    }

    public MessageRecipient(User recipient, Message message) {
        
        this.uuId = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.message = message;
        this.messageRead = false;
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

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(boolean read) {
        this.messageRead = read;
    }

    public Date getMessageReadDate() {
        return messageReadDate;
    }

    public void setMessageReadDate(Date messageReadDate) {
        this.messageReadDate = messageReadDate;
    }
}

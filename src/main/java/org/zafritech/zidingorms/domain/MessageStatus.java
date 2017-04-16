package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.zafritech.zidingorms.commons.enums.MessageStatusType;

@Entity(name = "TBL_MESSAGE_STATUSES")
public class MessageStatus implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
        
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "messageId")
    private Message message;
    
    @Enumerated(EnumType.STRING)
    private MessageStatusType statusType;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;

    public MessageStatus() {
        
    }

    public MessageStatus(User user, Message message, MessageStatusType statusType) {
        
        this.user = user;
        this.message = message;
        this.statusType = statusType;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(MessageStatusType statusType) {
        this.statusType = statusType;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
}

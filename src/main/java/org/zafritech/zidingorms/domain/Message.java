package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_MESSAGES")
public class Message implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @OneToOne
    @JoinColumn(name = "userId")
    private User sender;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @ManyToOne
    @JoinColumn(name = "replyToId")
    private Message replyTo;
    
    @OneToMany
    @JoinTable(name = "XREF_MESSAGE_RECIPIENTS", 
               joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> recipients = new HashSet<User>();

    @OneToMany
    @JoinTable(name = "XREF_MESSAGE_ATTACHMENTS_LIST", 
               joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "attachment_id", referencedColumnName = "id")}
    )
    private Set<Attachment> attachments = new HashSet<Attachment>();
    
    @OneToMany
    @JoinTable(name = "XREF_MESSAGE_STATUS_LIST", 
               joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "status_id", referencedColumnName = "id")}
    )
    private Set<MessageStatus> statusList = new HashSet<MessageStatus>();
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;

    public Message() {
        
    }

    public Message(User sender, String message) {
        
        this.sender = sender;
        this.message = message;
    }

    public Message(User sender, String message, Message replyTo) {
        
        this.sender = sender;
        this.message = message;
        this.replyTo = replyTo;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(Message replyTo) {
        this.replyTo = replyTo;
    }

    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<MessageStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(Set<MessageStatus> statusList) {
        this.statusList = statusList;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
    
}

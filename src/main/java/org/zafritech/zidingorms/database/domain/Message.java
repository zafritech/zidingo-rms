package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @JoinColumn(name = "senderId")
    private User sender;
    
    @Column(columnDefinition = "TEXT")
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @ManyToOne
    @JoinColumn(name = "parentId")
    private Message parentMessage;
        
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date sentDate;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    
    private boolean reminder;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextReminder;
    
    private int reminderFrequency;

    public Message() {
        
    }

    public Message(String subject, String message) {
        
        this.uuId = UUID.randomUUID().toString();
        this.subject = subject;
        this.message = message;
        this.sentDate = new Timestamp(System.currentTimeMillis());
    }

    public Message(String subject, String message, Message parentMessage) {
        
        this.uuId = UUID.randomUUID().toString();
        this.subject = subject;
        this.message = message;
        this.parentMessage = parentMessage;
        this.sentDate = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", uuId=" + uuId + ", sender=" + 
                sender + ", subject=" + subject + ", message=" + message + 
                ", parentMessage=" + parentMessage + ", sentDate=" + sentDate + 
                ", expiryDate=" + expiryDate + ", reminder=" + reminder + 
                ", nextReminder=" + nextReminder + ", reminderFrequency=" + 
                reminderFrequency + '}';
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message getParentMessage() {
        return parentMessage;
    }

    public void setParentMessage(Message parentMessage) {
        this.parentMessage = parentMessage;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public Date getNextReminder() {
        return nextReminder;
    }

    public void setNextReminder(Date nextReminder) {
        this.nextReminder = nextReminder;
    }

    public int getReminderFrequency() {
        return reminderFrequency;
    }

    public void setReminderFrequency(int reminderFrequency) {
        this.reminderFrequency = reminderFrequency;
    }
}

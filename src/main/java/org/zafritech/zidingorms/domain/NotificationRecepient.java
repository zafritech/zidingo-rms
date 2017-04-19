package org.zafritech.zidingorms.domain;

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

@Entity(name = "TBL_NOTIFICATION_RECIPIENTS")
public class NotificationRecepient implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User recipient;
    
    @ManyToOne
    @JoinColumn(name = "notificationId")
    private Notification notification;
    
    private boolean notificationRead;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationReadDate;

    public NotificationRecepient() {
        
    }

    public NotificationRecepient(User recipient, Notification notification) {
        
        this.uuId = UUID.randomUUID().toString();
        this.recipient = recipient;
        this.notification = notification;
        this.notificationRead = false;
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

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public boolean isNotificationRead() {
        return notificationRead;
    }

    public void setNotificationRead(boolean notificationRead) {
        this.notificationRead = notificationRead;
    }

    public Date getNotificationReadDate() {
        return notificationReadDate;
    }

    public void setNotificationReadDate(Date notificationReadDate) {
        this.notificationReadDate = notificationReadDate;
    }
}
package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_NOTIFICATIONS")
public class Notification implements Serializable {

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
    private String notification;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate; 

    public Notification() {
        
    }

    public Notification(String notification) {
        
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + getId() + ", sender=" + getSender() + 
               ", subject=" + getSubject() + ", notification=" + getNotification() + 
               ", notificationDate=" + getNotificationDate() + '}';
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

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}

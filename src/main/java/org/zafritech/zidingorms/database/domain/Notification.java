package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_NOTIFICATIONS")
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private String notificationPriority;
    
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String notification;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date notificationDate; 

    public Notification() {
        
    }

    public Notification(String name, String notification, String priority) {
        
        this.name = name;
        this.notification = notification;
        this.notificationPriority = priority;
    }

    @Override
    public String toString() {
        
        return "Notification{" + "id=" + id + ", uuId=" + uuId + 
               ", notification=" + notification + ", notificationDate=" + 
               notificationDate + '}';
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotificationPriority() {
        return notificationPriority;
    }

    public void setNotificationPriority(String notificationPriority) {
        this.notificationPriority = notificationPriority;
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

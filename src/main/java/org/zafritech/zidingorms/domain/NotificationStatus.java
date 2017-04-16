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
import org.zafritech.zidingorms.commons.enums.NotificationStatusType;

@Entity(name = "TBL_NOTIFICATION_STATUSES")
class NotificationStatus implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
        
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "notificationId")
    private Notification notification;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatusType statusType;
    
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;
    
    public NotificationStatus() {
        
    }

    public NotificationStatus(User user, Notification notification, NotificationStatusType statusType) {
        
        this.user = user;
        this.notification = notification;
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        
        return "NotificationStatus{" + "id=" + getId() + 
                ", user=" + getUser() + ", notification=" + getNotification() + 
                ", statusType=" + getStatusType() + ", statusDate=" + getStatusDate() + '}';
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

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public NotificationStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(NotificationStatusType statusType) {
        this.statusType = statusType;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }
}

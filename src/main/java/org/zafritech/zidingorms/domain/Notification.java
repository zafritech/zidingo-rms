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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;

@Entity(name = "TBL_NOTIFICATIONS")
public class Notification implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    @Column(columnDefinition = "TEXT")
    private String notification;
    
    @OneToMany
    @JoinTable(name = "XREF_NOTIFICATION_RECIPIENTS", 
               joinColumns = {@JoinColumn(name = "notification_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> recipients = new HashSet<User>();
    
    @OneToMany
    @JoinTable(name = "XREF_NOTIFICATION_STATUS_LIST", 
               joinColumns = {@JoinColumn(name = "notification_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "status_id", referencedColumnName = "id")}
    )
    private Set<NotificationStatus> statusList = new HashSet<NotificationStatus>();
    
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
        
        return "Notification{" + "id=" + getId() + ", notification=" + 
                getNotification() + ", notificationDate=" + getNotificationDate() + '}';
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

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Set<User> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<User> recipients) {
        this.recipients = recipients;
    }

    public Set<NotificationStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(Set<NotificationStatus> statusList) {
        this.statusList = statusList;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}

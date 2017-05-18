package org.zafritech.zidingorms.database.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "XREF_NOTIFICATION_RECIPIENTS",
               joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    @JsonBackReference
    private Set<User> notificationRecipients = new HashSet<User>();
    
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
        
        return "Notification{" + "id=" + getId() + ", uuId=" + getUuId() + 
               ", notification=" + getNotification() + ", notificationDate=" + 
               getNotificationDate() + '}';
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

    public String getNotificationPriority() {
        return notificationPriority;
    }

    public void setNotificationPriority(String notificationPriority) {
        this.notificationPriority = notificationPriority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public Set<User> getNotificationRecipients() {
        return notificationRecipients;
    }

    public void setNotificationRecipients(Set<User> notificationRecipients) {
        this.notificationRecipients = notificationRecipients;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }
}

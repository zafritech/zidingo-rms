package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_TASK_ARCHIVES")
class TaskArchive implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private Integer taskCount;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User archivedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date archiveDate;

    public TaskArchive() {
        
    }

    public TaskArchive(Integer taskCount, User archivedBy) {
        
        this.uuId = UUID.randomUUID().toString();
        this.taskCount = taskCount;
        this.archivedBy = archivedBy;
        this.archiveDate = new Timestamp(System.currentTimeMillis());
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

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public User getArchivedBy() {
        return archivedBy;
    }

    public void setArchivedBy(User archivedBy) {
        this.archivedBy = archivedBy;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }
}

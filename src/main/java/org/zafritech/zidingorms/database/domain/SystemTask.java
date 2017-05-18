package org.zafritech.zidingorms.database.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_SYSTEM_TASKS")
class SystemTask implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;

    private String taskName;
    
    private String taskDescription;
    
    @Column(columnDefinition = "TEXT")
    private boolean runOnce;
    
    private String scheduleType;
    
    private String scheduleValue;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastRunDate;

    public SystemTask() {
        
    }

    public SystemTask(String taskName, boolean runOnce, String scheduleType, String scheduleValue) {
        
        this.uuId = UUID.randomUUID().toString();
        this.taskName = taskName;
        this.runOnce = runOnce;
        this.scheduleType = scheduleType;
        this.scheduleValue = scheduleValue;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public boolean isRunOnce() {
        return runOnce;
    }

    public void setRunOnce(boolean runOnce) {
        this.runOnce = runOnce;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public String getScheduleValue() {
        return scheduleValue;
    }

    public void setScheduleValue(String scheduleValue) {
        this.scheduleValue = scheduleValue;
    }

    public Date getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(Date lastRunDate) {
        this.lastRunDate = lastRunDate;
    }
}

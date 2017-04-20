package org.zafritech.zidingorms.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "TBL_TASKS")
public class Task implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private String taskName;
    
    @Column(columnDefinition = "TEXT")
    private String taskDetails;
    
    private String taskUnits;
    
    private double initialSize;
    
    private double taskProgress;
    
    private String taskStatus;
    
    @OneToOne
    @JoinColumn(name = "assignedToId")
    private User assignedTo;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedDate;

    public Task() {
        
    }

    public Task(String taskName, 
                String taskDetails, 
                String taskUnits, 
                double initialSize,
                User assignedTo) {
        
        this.uuId = UUID.randomUUID().toString();
        this.taskName = taskName;
        this.taskDetails = taskDetails;
        this.taskUnits = taskUnits;
        this.initialSize = initialSize;
        this.taskProgress = 0.0;
        this.taskStatus = "OPEN";
        this.assignedDate = new Timestamp(System.currentTimeMillis());
        this.assignedTo = assignedTo;
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

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public String getTaskUnits() {
        return taskUnits;
    }

    public void setTaskUnits(String taskUnits) {
        this.taskUnits = taskUnits;
    }

    public double getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(double initialSize) {
        this.initialSize = initialSize;
    }

    public double getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(double taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }
}

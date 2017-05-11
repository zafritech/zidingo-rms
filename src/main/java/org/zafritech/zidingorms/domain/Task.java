package org.zafritech.zidingorms.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.zafritech.zidingorms.commons.enums.TaskAction;

@Entity(name = "TBL_TASKS")
public class Task implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    private String uuId;
    
    private String batchId;
        
    @ManyToOne
    @JoinColumn(name = "itemId")
    private Item taskItem;
    
    @Enumerated(EnumType.STRING)
    private TaskAction taskAction;
    
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "XREF_TASK_ASSIGNMENTS",
               joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
               inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    @JsonBackReference
    private Set<User> assignedTo = new HashSet<User>();
    
    private boolean completed;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    private User completedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;

    @Override
    public String toString() {
        
        return "Task{" + "id=" + getId() + ", uuId=" + getUuId() + ", taskItem=" + 
                getTaskItem() + ", taskAction=" + getTaskAction() + ", assignedTo=" + 
                getAssignedTo() + ", completed=" + isCompleted() + ", creationDate=" + 
                getCreationDate() + ", completedBy=" + getCompletedBy() + ", completionDate=" 
                + getCompletionDate() + '}';
    }

    
    public Task() {
        
    }

    public Task(Item taskItem, TaskAction taskAction, String batchId) {
        
        this.uuId = UUID.randomUUID().toString();
        this.taskItem = taskItem;
        this.taskAction = taskAction;
        this.batchId = batchId;
        this.creationDate = new Timestamp(System.currentTimeMillis());
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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Item getTaskItem() {
        return taskItem;
    }

    public void setTaskItem(Item taskItem) {
        this.taskItem = taskItem;
    }

    public TaskAction getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(TaskAction taskAction) {
        this.taskAction = taskAction;
    }

    public Set<User> getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Set<User> assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public User getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(User completedBy) {
        this.completedBy = completedBy;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

}

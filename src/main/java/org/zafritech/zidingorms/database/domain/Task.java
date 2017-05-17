package org.zafritech.zidingorms.database.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import org.zafritech.zidingorms.core.commons.enums.TaskAction;

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
    
    @Enumerated(EnumType.STRING)
    private TaskAction taskActionTaken;
    
    @Column(columnDefinition = "TEXT")
    private String actionComment;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User completedBy;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date completionDate;
    
    private boolean archived;
    
    @ManyToOne
    @JoinColumn(name = "taskArchiveId")
    private TaskArchive archive;

    @Override
    public String toString() {
        
        return "Task{" + "id=" + id + ", uuId=" + uuId + ", batchId=" + batchId + 
                ", taskItem=" + taskItem + ", taskAction=" + taskAction + 
                ", assignedTo=" + assignedTo + ", completed=" + completed + 
                ", taskActionTaken=" + taskActionTaken + ", actionComment=" + 
                actionComment + ", creationDate=" + creationDate + ", completedBy=" + 
                completedBy + ", completionDate=" + completionDate + ", archived=" + 
                archived + ", archive=" + archive + '}';
    }

    
    
    public Task() {
        
    }

    public Task(Item taskItem, TaskAction taskAction, String batchId) {
        
        this.uuId = UUID.randomUUID().toString();
        this.taskItem = taskItem;
        this.taskAction = taskAction;
        this.batchId = batchId;
        this.creationDate = new Timestamp(System.currentTimeMillis());
        this.archived = false;
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

    public TaskAction getTaskActionTaken() {
        return taskActionTaken;
    }

    public void setTaskActionTaken(TaskAction taskActionTaken) {
        this.taskActionTaken = taskActionTaken;
    }

    public String getActionComment() {
        return actionComment;
    }

    public void setActionComment(String actionComment) {
        this.actionComment = actionComment;
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

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public TaskArchive getArchive() {
        return archive;
    }

    public void setArchive(TaskArchive archive) {
        this.archive = archive;
    }
}

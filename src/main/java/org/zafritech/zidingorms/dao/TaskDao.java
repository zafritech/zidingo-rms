/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao;

/**
 *
 * @author LukeS
 */
public class TaskDao {
    
    private String assignedTo;
            
    private String taskUnits;

    private double initialSize;
    
    private String taskName;
    
    private String taskDetails;

    public TaskDao() {
        
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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
}

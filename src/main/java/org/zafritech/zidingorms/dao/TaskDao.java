/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao;

import org.zafritech.zidingorms.commons.enums.TaskAction;

/**
 *
 * @author LukeS
 */
public class TaskDao {
    
    private Long itemId;
            
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
            
    private TaskAction taskAction;

    public TaskDao() {
        
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public TaskAction getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(TaskAction taskAction) {
        this.taskAction = taskAction;
    }

}

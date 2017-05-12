/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.dao;

/**
 *
 * @author LukeS
 */
public class NoticeDao {
    
    private String name;
    
    private String priority;
    
    private String message;

    public NoticeDao() {
        
    }

    public NoticeDao(String name, String priority, String message) {
        
        this.name = name;
        this.priority = priority;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

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
public class CategDao {
    
    private Long projectId;
    
    private String categoryName;
    
    private String categoryCode;
    
    private Long categoryParentId;
    
    private String categoryLeadUuId;

    @Override
    public String toString() {
        
        return "CategDao{" + "projectId=" + projectId + 
                ", categoryName=" + categoryName + ", categoryCode=" 
                + categoryCode + ", categoryParentId=" 
                + categoryParentId + ", categoryLeadUuId=" 
                + categoryLeadUuId + '}';
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getCategoryParentId() {
        return categoryParentId;
    }

    public void setCategoryParentId(Long categoryParentId) {
        this.categoryParentId = categoryParentId;
    }

    public String getCategoryLeadUuId() {
        return categoryLeadUuId;
    }

    public void setCategoryLeadUuId(String categoryLeadUuId) {
        this.categoryLeadUuId = categoryLeadUuId;
    }
    
}

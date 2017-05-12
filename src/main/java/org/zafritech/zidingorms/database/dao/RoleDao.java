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
public class RoleDao {
    
    private String roleName;
    
    private String roleDisplayName;

    public RoleDao() {
        
    }

    public RoleDao(String roleName, String roleDisplayName) {
        
        this.roleName = roleName;
        this.roleDisplayName = roleDisplayName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDisplayName() {
        return roleDisplayName;
    }

    public void setRoleDisplayName(String roleDisplayName) {
        this.roleDisplayName = roleDisplayName;
    }
}

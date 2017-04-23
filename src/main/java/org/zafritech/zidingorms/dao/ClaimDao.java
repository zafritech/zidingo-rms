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
public class ClaimDao {
    
    private String userUuId;
    
    private String userClaimType;
    
    private String userClaimValue;
    
    private String userClaimStringValue;

    public ClaimDao() {
        
    }

    public ClaimDao(String userUuId, String userClaimType, String userClaimValue) {
        
        this.userUuId = userUuId;
        this.userClaimType = userClaimType;
        this.userClaimValue = userClaimValue;
    }

    @Override
    public String toString() {
        return "ClaimDao{" + "userUuId=" + userUuId + ", userClaimType=" + userClaimType + ", userClaimValue=" + userClaimValue + '}';
    }

    public String getUserUuId() {
        return userUuId;
    }

    public void setUserUuId(String userUuId) {
        this.userUuId = userUuId;
    }

    public String getUserClaimType() {
        return userClaimType;
    }

    public void setUserClaimType(String userClaimType) {
        this.userClaimType = userClaimType;
    }

    public String getUserClaimValue() {
        return userClaimValue;
    }

    public void setUserClaimValue(String userClaimValue) {
        this.userClaimValue = userClaimValue;
    }

    public String getUserClaimStringValue() {
        return userClaimStringValue;
    }

    public void setUserClaimStringValue(String userClaimStringValue) {
        this.userClaimStringValue = userClaimStringValue;
    }
}

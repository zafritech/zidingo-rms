/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author LukeS
 */
public class ProfileDao {

    @NotEmpty(message = "Email address cannot be empty.")
    private String email;

    @NotEmpty(message = "First name cannot be empty.")
    @Size(min = 2, max = 64, message = "First name must be between 2 and 64 characters long.")
    private String userName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(min = 2, max = 64, message = "Last name must be between 2 and 64 characters long.")
    private String lastName;

    public ProfileDao() {
    }

    public ProfileDao(String userName, String lastName) {

        this.userName = userName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

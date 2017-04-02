/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.daos;

import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author LukeS
 */
public class UserDao {

    private Long id;

    @NotEmpty(message = "Email address cannot be empty.")
    @Email(message = "Email address is not well-formed.")
    private String email;

    @NotEmpty(message = "Password cannot be empty.")
    @Size(min = 6, max = 16, message = "Password must be between 6 and 16 characters long.")
    private String password;

    @NotEmpty(message = "Password confirmation cannot be empty.")
    private String confirmPassword;

    public UserDao() {
    }

    public UserDao(String email, String password, String confirmPassword) {

        this.email = email;

        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

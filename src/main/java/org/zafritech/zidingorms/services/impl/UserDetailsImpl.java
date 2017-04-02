/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zafritech.zidingorms.domain.Role;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = -4046829509244992597L;
	
	private final User user;
    
    public UserDetailsImpl(User user) {
        
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        Collection<GrantedAuthority> authorities = new HashSet<>();
        
        Set<Role> roles = user.getUserRoles();
        
        for(Role role : roles) {
            
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        
        return authorities;
    }

    @Override
    public String getPassword() {
       
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
        
        return true;
    }
}

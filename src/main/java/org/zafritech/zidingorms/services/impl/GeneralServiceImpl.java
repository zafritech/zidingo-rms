/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.UserRepository;
import org.zafritech.zidingorms.services.GeneralService;

/**
 *
 * @author LukeS
 */
@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User loggedInUser() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String name = userDetails.getUsername();
        
        User user = userRepository.findByUserName(name);

        return user;
    }
}

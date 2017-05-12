/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class ProfilesController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/profile")
    public ModelAndView getUser() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        User user = userRepository.findByUserName(name);

        ModelAndView model = new ModelAndView("views/profiles/profile");
        model.addObject("user", user);

        return model;
    }
}

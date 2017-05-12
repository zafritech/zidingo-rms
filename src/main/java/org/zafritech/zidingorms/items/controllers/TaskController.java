/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
@Controller
public class TaskController {
    
    @Autowired
    private UserService userService;
        
    @RequestMapping("/tasks")
    public String tasksActions(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/projects/tasks";
    }
    
    @RequestMapping("/tasks/{uuid}")
    public String performTask(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/projects/tasks";
    }
}

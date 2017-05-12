/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.controllers;

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
public class MessageController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/messages")
    public String messagesActions(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/messages";
    }
    
    @RequestMapping("/messages/{uuid}")
    public String readMessage(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/messages";
    }
    
    @RequestMapping("/notifications")
    public String notificationsActions(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/notifications";
    }
    
    @RequestMapping("/notifications/{uuid}")
    public String readNotification(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/notifications";
    }
}

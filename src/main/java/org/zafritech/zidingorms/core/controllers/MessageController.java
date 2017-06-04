/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.core.messages.MessageService;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.domain.Message;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.MessageRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class MessageController {
    
    @Autowired
    private UserService userService;
 
    @Autowired
    public MessageService messageService;
  
    @Autowired
    private MessageRepository messageRepository;
           
    @RequestMapping(value = {"/messages", "/messages/inbox"})
    public String messagesInbox(Model model) {
        
        User user = userService.loggedInUser();
        
        List<Message> messages = messageService.getIncomingMessages(user);
        
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
          
        return "views/messages/inbox";
    }
       
    @RequestMapping(value = {"/messages/sent"})
    public String sentMessages(Model model) {
        
        User user = userService.loggedInUser();
        List<Message> messages = messageService.getSentMessages(user);
        
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
          
        return "views/messages/sent";
    }
    
    @RequestMapping(value = {"/notifications", "/messages/notifications"})
    public String notificationsActions(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "views/messages/notifications";
    }
    
    @RequestMapping("/messages/{uuid}")
    public String readMessage(@PathVariable(value = "uuid") String uuid, Model model) {
        
        User user = userService.loggedInUser();
        Message message = messageRepository.findByUuId(uuid);
        
        model.addAttribute("user", user);
        model.addAttribute("message", message);
        model.addAttribute("newLineChar", "\n");
        
        return "views/messages/message";
    }
    
    @RequestMapping("/notifications/{uuid}")
    public String readNotification(Model model) {
        
        User user = userService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "views/messages/notification";
    }
    
    @RequestMapping("/messages/trash")
    public String trashedItems(Model model) {
        
        User user = userService.loggedInUser();
        List<Message> messages = messageService.getDeletedMessages(user);
        
        model.addAttribute("user", user);
        model.addAttribute("messages", messages);
        
        return "views/messages/trash";
    }
}

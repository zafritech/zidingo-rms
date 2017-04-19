/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.domain.Message;
import org.zafritech.zidingorms.domain.Notification;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.services.MessageService;
import org.zafritech.zidingorms.services.UserService;

/**
 *
 * @author LukeS
 */
@RestController
public class MessageRestController {

    @Autowired
    public MessageService messageService;
    
    @Autowired
    public UserService userService;
    
    @RequestMapping(value = "/api/messages/message/new", method = POST)
    public ResponseEntity<Message> newMessage(@RequestBody MsgDao msgDao) {
        
        Message msg = messageService.sendMessage(msgDao);

        return new ResponseEntity<Message>(msg, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/messages/notification/new", method = POST)
    public ResponseEntity<Notification> newNotification(@RequestBody NoticeDao noticeDao) {
        
        Notification notice = messageService.sendNotification(noticeDao);

        return new ResponseEntity<Notification>(notice, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/messages/unread", method = GET)
    public ResponseEntity<List<Message>> getUnreadMessages() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        
        List<Message> messages = messageService.getUnreadMessages(user);
        
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    } 
    
    @RequestMapping(value = "/api/notifications/unread", method = GET)
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        
        List<Notification> notifications = messageService.getUnreadNotifications(user);
        
        return new ResponseEntity<List<Notification>>(notifications, HttpStatus.OK);
    } 
}

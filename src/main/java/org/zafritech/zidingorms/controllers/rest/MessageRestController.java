/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.domain.Message;
import org.zafritech.zidingorms.domain.Notification;
import org.zafritech.zidingorms.services.MessageService;

/**
 *
 * @author LukeS
 */
@RestController
public class MessageRestController {

    @Autowired
    public MessageService messageService;
    
    @RequestMapping(value = "/api/messages/message/new", method = POST)
    public ResponseEntity<Message> newMessage(@RequestBody MsgDao msgDao) {
        
        Message msg = messageService.saveMessage(msgDao);

        return new ResponseEntity<Message>(msg, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/messages/notification/new", method = POST)
    public ResponseEntity<Notification> newNotification(@RequestBody NoticeDao noticeDao) {
        
        Notification notice = messageService.saveNotification(noticeDao);

        return new ResponseEntity<Notification>(notice, HttpStatus.OK);
    }
}

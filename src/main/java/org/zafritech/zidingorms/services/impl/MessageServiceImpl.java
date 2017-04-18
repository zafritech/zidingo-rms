/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.dao.converter.DaoToMessageConverter;
import org.zafritech.zidingorms.domain.Message;
import org.zafritech.zidingorms.domain.MessageRecipient;
import org.zafritech.zidingorms.domain.Notification;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.MessageRecipientRepository;
import org.zafritech.zidingorms.repositories.MessageRepository;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.MessageService;

/**
 *
 * @author LukeS
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageRecipientRepository messageRecipientRepository;
    
    @Override
    public Message saveMessage(MsgDao msgDao) {
        
        DaoToMessageConverter daoToMsgConverter = new DaoToMessageConverter();
        Message message = daoToMsgConverter.convert(msgDao);
        message.setSender(generalService.loggedInUser()); 
        
        Message msg = messageRepository.save(message);
        
        List<User> users = generalService.allUser();
        
        for (User user : users) {
            
           MessageRecipient recipient = new MessageRecipient(user, msg);
           messageRecipientRepository.save(recipient);
        }
        
        return msg; 
    }
    
    @Override
    public Notification saveNotification(NoticeDao noticeDao) {
        
        return null;
    }
}

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
import org.zafritech.zidingorms.domain.NotificationRecepient;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.MessageRecipientRepository;
import org.zafritech.zidingorms.repositories.MessageRepository;
import org.zafritech.zidingorms.repositories.NotificationRecepientRepository;
import org.zafritech.zidingorms.repositories.NotificationRepository;
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
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private NotificationRecepientRepository notificationRecepientRepository;
    
    @Override
    public Message sendMessage(MsgDao msgDao) {
        
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
    public Notification sendNotification(NoticeDao noticeDao) {
        
        Notification notice = new Notification(noticeDao.getName(), noticeDao.getMessage(), noticeDao.getPriority());
        
        Notification savedNotice = notificationRepository.save(notice);
        
        List<User> users = generalService.allUser();
        
        for (User user : users) {
            
           NotificationRecepient recepient = new NotificationRecepient(user, savedNotice);
           notificationRecepientRepository.save(recepient);
        }
        
        return savedNotice;
    }
    
    @Override
    public List<Message> getUnreadMessages(User user) {
        
        List<MessageRecipient> received = messageRecipientRepository.findByRecipientAndMessageRead(user, false);
        
        List<Message> messages = new ArrayList<>();
                
        for (MessageRecipient receipt: received) {
            
            Message msg = messageRepository.findOne(receipt.getMessage().getId());
            messages.add(msg);
        }
        
        return messages;
    }
    
    @Override
    public List<Notification> getUnreadNotifications(User user) {
        
        List<NotificationRecepient> received = notificationRecepientRepository.findByRecipientAndNotificationRead(user, false);
        
        List<Notification> notices = new ArrayList<>();
        
        for (NotificationRecepient receipt: received) {
            
            Notification notice = notificationRepository.findOne(receipt.getNotification().getId());
            notices.add(notice);
        }
        
        return notices;
    }
}

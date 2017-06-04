/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.messages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.MsgDao;
import org.zafritech.zidingorms.database.dao.NoticeDao;
import org.zafritech.zidingorms.database.dao.converter.DaoToMessageConverter;
import org.zafritech.zidingorms.database.domain.Message;
import org.zafritech.zidingorms.database.domain.Notification;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.MessageRepository;
import org.zafritech.zidingorms.database.repositories.NotificationRepository;

/**
 *
 * @author LukeS
 */
@Service
public class MessageServiceImpl implements MessageService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Message sendMessage(MsgDao msgDao) {
        
        List<User> users = userService.allUser();
        Set<User> recipients = new HashSet<User>();
        
        DaoToMessageConverter daoToMsgConverter = new DaoToMessageConverter();
        
        Message message = daoToMsgConverter.convert(msgDao);
        
        users.forEach(user->recipients.add(user));  
        
        message.setSender(userService.loggedInUser()); 
        message.setSentTo(recipients); 
        
        Message msg = messageRepository.save(message);
        
        return msg; 
    }
    
    @Override
    public Notification sendNotification(NoticeDao noticeDao) {
        
        Notification notice = new Notification(noticeDao.getName(), noticeDao.getMessage(), noticeDao.getPriority());
        
        Notification savedNotice = notificationRepository.save(notice);
        
        return savedNotice;
    }
    
    @Override
    public List<Message> getUnreadMessages(User user) {
        
        List<Message> messages = new ArrayList<>();
        
        return messages;
    }
    
    @Override
    public List<Notification> getUnreadNotifications(User user) {
       
        List<Notification> notices = new ArrayList<>();
        
        return notices;
    }

    @Override
    public List<Message> getIncomingMessages(User user) {
        
        List<Message> inbox = messageRepository.findBySentTo(user);
        
        return inbox;
    }
    
    @Override
    public List<Message> getSentMessages(User user) {

        List<Message> messages = new ArrayList();
                
        return messages;
    }

    @Override
    public List<Message> getDeletedMessages(User user) {
        
        List<Message> messages = new ArrayList();
                
        return messages;
    }
}

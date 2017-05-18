/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.messages;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.MsgDao;
import org.zafritech.zidingorms.database.dao.NoticeDao;
import org.zafritech.zidingorms.database.domain.Message;
import org.zafritech.zidingorms.database.domain.Notification;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
@Service
public interface MessageService {
    
    Message sendMessage(MsgDao msgDao);
    
    Notification sendNotification(NoticeDao noticeDao);
    
    List<Message> getIncomingMessages(User user);
    
    List<Message> getUnreadMessages(User user);
    
    List<Message> getSentMessages(User user);
    
    List<Notification> getUnreadNotifications(User user);
    
    List<Message> getDeletedMessages(User user);
}

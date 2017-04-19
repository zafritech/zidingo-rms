/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.domain.Message;
import org.zafritech.zidingorms.domain.Notification;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
@Service
public interface MessageService {
    
    Message sendMessage(MsgDao msgDao);
    
    Notification sendNotification(NoticeDao noticeDao);
    
    List<Message> getUnreadMessages(User user);
    
    List<Notification> getUnreadNotifications(User user);
}

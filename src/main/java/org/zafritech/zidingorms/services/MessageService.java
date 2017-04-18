/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.MsgDao;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.domain.Message;
import org.zafritech.zidingorms.domain.Notification;

/**
 *
 * @author LukeS
 */
@Service
public interface MessageService {
    
    Message saveMessage(MsgDao msgDao);
    
    Notification saveNotification(NoticeDao noticeDao);
}

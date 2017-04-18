/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao.converter;

import org.springframework.core.convert.converter.Converter;
import org.zafritech.zidingorms.dao.NoticeDao;
import org.zafritech.zidingorms.domain.Notification;

/**
 *
 * @author LukeS
 */
public class DaoToNotificationConverter implements Converter<NoticeDao, Notification> {

    @Override
    public Notification convert(NoticeDao noticeDao) {

        Notification notice = new Notification();
        
        return null;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.dao.converter;

import org.springframework.core.convert.converter.Converter;
import org.zafritech.zidingorms.database.dao.MsgDao;
import org.zafritech.zidingorms.database.domain.Message;

/**
 *
 * @author LukeS
 */
public class DaoToMessageConverter implements Converter<MsgDao, Message> {

    @Override
    public Message convert(MsgDao msgDao) {
        
        Message msg = new Message(msgDao.getSubject(), msgDao.getMessage());
        
        return msg;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao.converter;

import org.springframework.core.convert.converter.Converter;
import org.zafritech.zidingorms.dao.UserDao;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public class UserToDaoConverter implements Converter<User, UserDao> {

    @Override
    public UserDao convert(User user) {

        UserDao userDao = new UserDao();

        userDao.setId(user.getId());
        userDao.setEmail(user.getEmail());
        userDao.setPassword(user.getPassword());

        return userDao;
    }
}

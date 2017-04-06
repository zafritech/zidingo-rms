/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.dao.UserDao;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Component
public class DaoToUserConverter implements Converter<UserDao, User> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User convert(UserDao userDao) {

        if (userDao.getId() != null) {

            User user = userRepository.findOne(userDao.getId());
            user.setEmail(userDao.getEmail());
            user.setPassword(userDao.getPassword());

            return user;

        } else {

            User user = new User(userDao.getEmail(), userDao.getPassword());

            return user;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.ArrayList;
import org.zafritech.zidingorms.services.UserService;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.UserDao;
import org.zafritech.zidingorms.dao.converter.DaoToUserConverter;
import org.zafritech.zidingorms.domain.Role;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    private DaoToUserConverter daoToUser;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();
        List<User> sanitizedUsers = new ArrayList<>();

        userRepository.findAll().forEach(users::add);
        
        users.forEach(user->{
            
            user.setUserName(null); 
            user.setPassword(null); 
            sanitizedUsers.add(user);
            
        });

        return sanitizedUsers;
    }

    @Override
    public User findByUserName(String name) {

        return userRepository.findByEmail(name);
    }

    @Override
    public User findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if (user == null) {

            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    @Override
    public User findById(Long id) {

        return userRepository.findOne(id);
    }

    @Override
    public User getByUuId(String uuid) {

        return userRepository.getByUuId(uuid);
    }

    @Override
    public User saveUser(User user) {

        if (userExists(user.getEmail())) {
            return null;
        }

        User newUser = new User(user.getEmail(), user.getPassword(), (HashSet<Role>) user.getUserRoles());

        return userRepository.save(newUser);
    }

    @Override
    public User saveDao(UserDao userDao) {

        if (userDao != null) {

            return userRepository.save(daoToUser.convert(userDao));
        }

        return null;
    }

    @Override
    public void deleteUser(Long id) {

        userRepository.delete(id);
    }

    @Override
    public boolean passwordAndConfirmationMatch(UserDao userDao) {

        if (userDao.getPassword().equals(userDao.getConfirmPassword())) {
            return true;
        }

        return false;
    }

    @Override
    public boolean userExists(String email) {

        if (userRepository.findByEmail(email) != null) {
            return true;
        }

        return false;
    }
}

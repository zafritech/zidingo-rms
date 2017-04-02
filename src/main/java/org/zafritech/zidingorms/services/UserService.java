/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.zafritech.zidingorms.daos.UserDao;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public interface UserService {
    
    User findById(Long id);
    
    User findByUserName(String name);
    
    User findByEmail(String name);

    List<User> findAll();

    User getByUuId(String uuid);
    
    User saveUser(User user);
    
    User saveDao(UserDao user);
    
    void deleteUser(Long id);
    
    boolean passwordAndConfirmationMatch(UserDao user);
    
    boolean userExists(String email);
}

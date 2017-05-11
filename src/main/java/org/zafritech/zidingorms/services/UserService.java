/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.zafritech.zidingorms.dao.UserDao;
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

    List<User> findOrderByFirstName();
    
    List<User> findOrderByFirstName(int pageSize, int pageNumber);

    User getByUuId(String uuid);

    User saveUser(User user);

    User saveDao(UserDao user);

    void deleteUser(Long id);

    boolean passwordAndConfirmationMatch(UserDao user);

    boolean userExists(String email);
    
    boolean passwordMatches(String rawPassword, String encodedPassword);
    
    User changePasswordTo(User user, String password);
    
    List<Integer> getPagesList(int currentPage, int lastPage);
}

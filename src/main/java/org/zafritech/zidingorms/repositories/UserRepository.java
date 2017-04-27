/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.Set;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    User findByUserName(String username);
    
    Set<User> findAllByOrderByFirstNameAsc();

    @Override
    Set<User> findAll();

    User getByUuId(String uuid);
}

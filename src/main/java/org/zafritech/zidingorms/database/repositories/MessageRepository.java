/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Message;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
    
    List<Message> findBySentTo(User user);
    
    Message findByUuId(String uuid);
}

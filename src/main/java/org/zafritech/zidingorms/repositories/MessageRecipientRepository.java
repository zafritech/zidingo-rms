/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.MessageRecipient;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
public interface MessageRecipientRepository extends CrudRepository<MessageRecipient, Long> {
    
    List<MessageRecipient> findByRecipientAndMessageRead(User user, boolean read);
}

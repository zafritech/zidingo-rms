/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.repositories;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.domain.MessageRecipient;

/**
 *
 * @author LukeS
 */
public interface MessageRecipientRepository extends CrudRepository<MessageRecipient, Long> {
    
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
@Service
public interface GeneralService {
    
    User loggedInUser();
    
    List<User> allUser();
}

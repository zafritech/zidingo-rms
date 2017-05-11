/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.zafritech.zidingorms.domain.Task;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.ClaimDao;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.Claim;
import org.zafritech.zidingorms.domain.User;

/**
 *
 * @author LukeS
 */
@Service
public interface GeneralService {
    
    User loggedInUser();
    
    List<User> allUser();
    
    Task createTask(TaskDao taskDao);
    
    List<Task>  getActiveTasks(User user);
    
    Claim createClaim(ClaimDao claimDao);
    
    List<Claim> findUserClaims(User user);
    
    List<Task> findUserTasks(User user);
    
    Integer findUserTasksCount(User user);
}

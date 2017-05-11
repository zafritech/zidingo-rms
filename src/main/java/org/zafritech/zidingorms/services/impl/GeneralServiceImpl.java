/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.ClaimType;
import org.zafritech.zidingorms.dao.ClaimDao;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.Claim;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Task;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ClaimRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.TaskRepository;
import org.zafritech.zidingorms.repositories.UserRepository;
import org.zafritech.zidingorms.services.GeneralService;

/**
 *
 * @author LukeS
 */
@Service
public class GeneralServiceImpl implements GeneralService {

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ClaimRepository claimRepository;
  
    @Override
    public User loggedInUser() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String name = userDetails.getUsername();
        
        User user = userRepository.findByUserName(name);

        return user;
    }
    
    @Override 
    public List<User> allUser() {
        
        return new ArrayList(userRepository.findAll());
    }

    @Override
    public Task createTask(TaskDao taskDao) {

        Item item = itemRepository.findOne(taskDao.getItemId());
        ItemCategory category = item.getItemCategory();
        Task task = new Task(item, taskDao.getTaskAction(), taskDao.getBatchId());
        Set<User> users = new HashSet<>();
        
        if (category != null) {
            
            users.add(category.getLead());
            
            List<Claim> claims = claimRepository.findByClaimTypeAndClaimValue(ClaimType.CATEGORY_MEMBER, 
                                                                          category.getId().toString());
        
            if (claims != null) {

                for (Claim claim : claims) {

                    users.add(claim.getUser());
                }
            }

            task.setAssignedTo(users);
            
        } else {
            
            System.out.println("Item " + item.getSysId() + " has no defined category.");
        }
        
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getActiveTasks(User user) {
        
        List<Task> tasks = taskRepository.findByAssignedToAndCompleted(user, false);
                
        return tasks;
    }
    
    @Override
    public Claim createClaim(ClaimDao claimDao) {
        
        User user = userRepository.getByUuId(claimDao.getUserUuId());
        
        Claim claim = new Claim(user, ClaimType.valueOf(claimDao.getUserClaimType()), claimDao.getUserClaimValue());
        
        return claimRepository.save(claim);
    }

    @Override
    public List<Claim> findUserClaims(User user) {

        List<Claim> claims = claimRepository.findByUser(user);
                
        return claims;
    }

    @Override
    public List<Task> findUserTasks(User user) {

        List<Task> tasks = taskRepository.findByAssignedToAndCompleted(user, false);
       
        return tasks;
    }

    @Override
    public Integer findUserTasksCount(User user) {

        List<Task> tasks = taskRepository.findByAssignedToAndCompleted(user, false);
        Integer taskSize = tasks.size();
                
        return taskSize;
    }
}

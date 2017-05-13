/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ClaimType;
import org.zafritech.zidingorms.database.dao.TaskDao;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Task;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ClaimRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.TaskRepository;
import org.zafritech.zidingorms.items.services.TaskService;

/**
 *
 * @author LukeS
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ClaimRepository claimRepository;
     
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

    @Override
    public Task findUserTaskByUuId(String uuid) {

        return taskRepository.findByUuId(uuid);
    }
}

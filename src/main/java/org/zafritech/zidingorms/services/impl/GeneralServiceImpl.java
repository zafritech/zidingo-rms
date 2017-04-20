/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.ArrayList;
import java.util.List;
import org.zafritech.zidingorms.domain.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.TaskDao;
import org.zafritech.zidingorms.domain.User;
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
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    
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

        Task task = new Task(taskDao.getTaskName(), 
                             taskDao.getTaskDetails(), 
                             taskDao.getTaskUnits(), 
                             taskDao.getInitialSize(),
                             userRepository.getByUuId(taskDao.getAssignedTo()));
        
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getActiveTasks(User user) {
        
        List<Task> tasks = taskRepository.findByAssignedToAndTaskStatus(user, "OPEN");
                
        return tasks;
    }
}

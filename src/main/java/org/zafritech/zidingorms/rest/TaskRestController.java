/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.TaskDao;
import org.zafritech.zidingorms.database.domain.Task;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.items.services.TaskService;

/**
 *
 * @author LukeS
 */
@RestController
public class TaskRestController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
        
    @RequestMapping(value = "/api/tasks/new", method = POST)
    public ResponseEntity<Task> newTask(@RequestBody TaskDao taskDao) {
        
        Task task = taskService.createTask(taskDao);

        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/tasks/active", method = GET)
    public ResponseEntity<List<Task>> getUnreadMessages() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUserName(auth.getName());
        
        List<Task> tasks = taskService.getActiveTasks(user);
        
        return new ResponseEntity<List<Task>>(tasks, HttpStatus.OK);
    }
}

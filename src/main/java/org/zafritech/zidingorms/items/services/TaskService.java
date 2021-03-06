/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services;

import java.util.List;
import org.zafritech.zidingorms.database.domain.Task;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.zafritech.zidingorms.database.dao.TaskDao;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
@Service
public interface TaskService {
         
    Task createTask(TaskDao taskDao);
    
    List<Task>  getActiveTasks(User user);
        
    List<Task> findUserTasks(User user);
    
    List<Task> findOpenUserTasks(User user);
    
    Task findUserTaskByUuId(String uuid);
    
    Integer findUserTasksCountOpen(User user);
    
    Integer findUserTasksCountAll(User user);
    
    Task performConfirmationTask(Long id);
    
    Task performAcknowledgementTask(Long id);
    
    Task performCommentTask(CommentDao dao, Long id);
}

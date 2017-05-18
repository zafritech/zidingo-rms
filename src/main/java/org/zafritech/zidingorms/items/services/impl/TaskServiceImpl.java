/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services.impl;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ClaimType;
import org.zafritech.zidingorms.core.commons.enums.TaskAction;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.CommentDao;
import org.zafritech.zidingorms.database.dao.TaskDao;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.Task;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ClaimRepository;
import org.zafritech.zidingorms.database.repositories.ItemCommentRepository;
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
    private UserService userService;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private ItemCommentRepository commentRepository;
            
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

        List<Task> tasks = taskRepository.findByAssignedTo(user);
        
        return tasks;
    }

    @Override
    public List<Task> findOpenUserTasks(User user) {
        
        return taskRepository.findByAssignedToAndCompleted(user, false);
    }

    @Override
    public Integer findUserTasksCountOpen(User user) {

        List<Task> tasks = taskRepository.findByAssignedToAndCompleted(user, false);
        Integer taskSize = tasks.size();
                
        return taskSize;
    }

    @Override
    public Integer findUserTasksCountAll(User user) {

        List<Task> tasks = taskRepository.findByAssignedToAndCompleted(user, false);
        Integer taskSize = tasks.size();
                
        return taskSize;
    }

    @Override
    public Task findUserTaskByUuId(String uuid) {

        return taskRepository.findByUuId(uuid);
    }
    
    @Override
    public Task performConfirmationTask(Long id) {

        User user = userService.loggedInUser();
        Task task = taskRepository.findOne(id);
        
        task.setTaskActionTaken(TaskAction.CONFIRM_REQ); 
        task.setCompleted(true);
        task.setCompletedBy(user);
        task.setCompletionDate(new Timestamp(System.currentTimeMillis())); 
        
        return taskRepository.save(task); 
    }

    @Override
    public Task performAcknowledgementTask(Long id) {
     
        User user = userService.loggedInUser();
        Task task = taskRepository.findOne(id);
        
        task.setTaskActionTaken(TaskAction.ACKNOWLEDGE_REQ); 
        task.setCompleted(true);
        task.setCompletedBy(user);
        task.setCompletionDate(new Timestamp(System.currentTimeMillis())); 
        
        return taskRepository.save(task); 
    }

    @Override
    public Task performCommentTask(CommentDao dao, Long id) {
    
        String commentText = "";
        Long itemId = dao.getItemId();
        User user = userService.findByEmail("client@astad.qa");
        Task task = taskRepository.findOne(id);
        
        ItemComment comment = new ItemComment();
        ItemComment oldComment = commentRepository.findFirstByAuthorAndItemIdOrderByCreationDateDesc(user, itemId);
        
        if (oldComment != null) {
        
            commentText = oldComment.getComment() + "\n\r" + dao.getComment();
            
            comment = oldComment;
            comment.setComment(commentText);
            
        } else {
            
            commentText = dao.getComment();
            
            comment.setItem(itemRepository.findOne(itemId)); 
            comment.setUuId(UUID.randomUUID().toString());
            comment.setCreationDate(new Timestamp(System.currentTimeMillis()));
            comment.setAuthor(userService.findByEmail("client@astad.qa")); 
            comment.setComment(commentText);
        }
        
        task.setTaskActionTaken(TaskAction.COMMENT_REQ); 
        task.setCompleted(true);
        task.setCompletedBy(userService.loggedInUser());
        task.setCompletionDate(new Timestamp(System.currentTimeMillis())); 
        task.setActionComment(commentText); 
        
        commentRepository.save(comment);
        return taskRepository.save(task); 
    }
}

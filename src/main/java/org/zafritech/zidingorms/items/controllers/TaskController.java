/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.domain.ApplicationSession;
import org.zafritech.zidingorms.database.domain.ItemComment;
import org.zafritech.zidingorms.database.domain.Task;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.domain.VerificationReference;
import org.zafritech.zidingorms.database.repositories.VerificationReferenceRepository;
import org.zafritech.zidingorms.items.services.CommentService;
import org.zafritech.zidingorms.items.services.TaskService;

/**
 *
 * @author LukeS
 */
@Controller
public class TaskController {
  
    @Autowired
    ApplicationSession applicationState;
        
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private CommentService commentService;
 
    @Autowired
    private VerificationReferenceRepository vvReferenceRepository;
    
    @RequestMapping("/tasks")
    public String tasksActions(Model model) {
        
        System.out.println("\nProject: " + applicationState.getProject());
        System.out.println("\nFolder: " + applicationState.getFolderId());
        System.out.println("\nDocument: " + applicationState.getArtifact());
        
        User user = userService.loggedInUser();
        
        List<Task> tasks = taskService.findOpenUserTasks(user);
        Integer total = taskService.findUserTasks(user).size();
        Integer open = tasks.size();
        Integer complete = total - open;
        Integer percentage = Math.round(((float) complete / (float) total) * 100.0f);
        
        model.addAttribute("user", user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("open", open);
        model.addAttribute("complete", complete);
        model.addAttribute("total", total);
        model.addAttribute("percentage", percentage);
        
        return "views/item/tasks";
    }
    
    @RequestMapping("/tasks/{uuid}")
    public String performTask(@PathVariable String uuid, Model model) {
        
        User user = userService.loggedInUser();
        Task task = taskService.findUserTaskByUuId(uuid);
        List<ItemComment> comments = commentService.findByItemIdOrderByCreationDateDesc(task.getTaskItem().getId());
        VerificationReference verification = vvReferenceRepository.findFirstByItem(task.getTaskItem());
        
        model.addAttribute("user", user);
        model.addAttribute("task", task);
        model.addAttribute("comments", comments);
        model.addAttribute("verification", verification);
        model.addAttribute("newLineChar", "\n");
        
        return "views/item/taskdetails";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.ItemService;

/**
 *
 * @author LukeS
 */
@Controller
public class ActivitiesController {
    
    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @RequestMapping("/tasks")
    public String tasksActions(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/projects/tasks";
    }
    
    @RequestMapping("/tasks/{uuid}")
    public String performTask(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/projects/tasks";
    }
    
    @RequestMapping("/messages")
    public String messagesActions(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/messages";
    }
    
    @RequestMapping("/messages/{uuid}")
    public String readMessage(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/messages";
    }
    
    @RequestMapping("/notifications")
    public String notificationsActions(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/notifications";
    }
    
    @RequestMapping("/notifications/{uuid}")
    public String readNotification(Model model) {
        
        User user = generalService.loggedInUser();
        
        model.addAttribute("user", user);
        
        return "/views/messages/notifications";
    }
    
    @RequestMapping("/items/{id}/{uuid}")
    public String itemsActions(@PathVariable String id, 
                               @PathVariable String uuid,
                               @RequestParam(defaultValue = "1", value = "page", required=false) Integer  page,
                               @RequestParam(defaultValue = "25", value = "size", required=false) Integer size,
                               Model model) {
        
        User user = generalService.loggedInUser();
        Project project = projectRepository.findByUuId(id);
        ItemCategory category = itemCategoryRepository.findByUuId(uuid);
        List<Item> items = itemService.findByItemCategoryPaged(project, category, size, page);
        
        int pageCount = (int)Math.ceil((float)(itemService.findByItemCategory(project, category).size() / size)) + 1;
        int currentPage = (page > pageCount) ? pageCount : page;
        List<Integer> pageList = itemService.getPagesList(currentPage, pageCount);
        
        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("category", category);
        model.addAttribute("items", items);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", size);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("pageList", pageList);
        model.addAttribute("lastDisplayed", Collections.max(pageList));
        
        return "/views/item/categories";
    }
}

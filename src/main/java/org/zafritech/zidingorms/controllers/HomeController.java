/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.dao.DisciplineDao;
import org.zafritech.zidingorms.domain.Claim;
import org.zafritech.zidingorms.domain.Task;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.services.GeneralService;
import org.zafritech.zidingorms.services.MessageService;
import org.zafritech.zidingorms.services.ProjectService;

/**
 *
 * @author LukeS
 */
@Controller
public class HomeController {

    @Autowired
    private GeneralService generalService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private MessageService messageService;
    
    @RequestMapping("/")
    public String home(Model model) {
        
        User user = generalService.loggedInUser();
        List<Claim> claims = generalService.findUserClaims(user);
        List<DisciplineDao> disciplines = projectService.getDisciplinesData(user);
        Integer notificationsCount = messageService.getUnreadNotifications(user).size();
        Integer messagesCount = messageService.getUnreadMessages(user).size(); 
        Integer tasksCount = generalService.findUserTasks(user).size(); 
        
        model.addAttribute("user", user);
        model.addAttribute("notificationsCount", notificationsCount);
        model.addAttribute("messagesCount", messagesCount);
        model.addAttribute("tasksCount", tasksCount);
        model.addAttribute("disciplines", disciplines);
        model.addAttribute("claims", claims);
        
        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {

        request.getSession().invalidate();

        return "redirect:/login?logout";
    }
}

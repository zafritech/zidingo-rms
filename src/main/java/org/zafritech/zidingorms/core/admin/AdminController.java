/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author LukeS
 */
@Controller
@Secured({"ROLE_ADMIN"})
public class AdminController {
    
    @Value("${zidingo.upload-dir}")
    private String upload_dir;
    
    @Autowired
    public AdminService adminService;
    
    @RequestMapping("/admin")
    public String adminPage(Model model) {
        
        model.addAttribute("attribute", "value");
        
        return "admin/index";
    }
    
    @RequestMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        
        model.addAttribute("attribute", "value");
        
        return "admin/dashboard";
    }
    
    @RequestMapping("/admin/pmsleads/load")
    public String importLeadsFromXlsx() {

        adminService.loadPMSLeads(upload_dir + "DOORS22.xlsx");

        return "redirect:/admin";
    }
    
    @RequestMapping("/admin/astadleads/load")
    public String importASTADLeadsFromXlsx() {

        adminService.updateASTADLeads(upload_dir + "ASTAD_Leads.xlsx");

        return "redirect:/admin";
    }
    
    @RequestMapping("/admin/statuses/import")
    public String importItemStatusesFromXlsx() {
        
        adminService.updateItemStatuses(upload_dir + "DOORS-v23.xlsx");
        
        return "redirect:/admin";
    }
    
    @RequestMapping("/admin/requests/confirmation")
    public String importConfirmationRequestsFromXlsx() {
        
        adminService.updateConfirmationRequests(upload_dir + "ConfirmationRequest-v23.xlsx");
        
        return "redirect:/admin";
    }
}

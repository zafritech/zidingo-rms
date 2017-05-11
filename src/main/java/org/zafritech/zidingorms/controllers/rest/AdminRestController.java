/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.services.AdminService;


/**
 *
 * @author LukeS
 */
@RestController
public class AdminRestController {
    
    @Autowired
    public AdminService adminService;
    
    @RequestMapping("/api/admin/pmsleads")
    public ResponseEntity<String> loadPMSLeads(Model model) {
        
        String filePath = "";
        
        boolean loaded = adminService.loadPMSLeads(filePath);
        
        String result = (loaded) ? "SUCCESS" : "FAIL"; 

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }
}

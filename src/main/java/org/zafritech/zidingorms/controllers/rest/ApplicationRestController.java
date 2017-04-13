/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author LukeS
 */
@Controller
public class ApplicationRestController {
    
    @RequestMapping("/api/login/check")
    public ResponseEntity<String> checkUserLogin(Model model) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();

        return new ResponseEntity<String>(userName, HttpStatus.OK);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author LukeS
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {

        return "index";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {

        request.getSession().invalidate();

        return "redirect:/login?logout";
    }
}

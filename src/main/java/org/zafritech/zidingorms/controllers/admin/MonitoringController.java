/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author LukeS
 */
@Controller
public class MonitoringController {

    @RequestMapping("/admin/monitor")
    public String home(Model model) {

        model.addAttribute("attribute", "value");
        return "/admin/monitor/index";
    }

    @RequestMapping("/admin/monitor/health")
    public String health(Model model) {

        model.addAttribute("health", "/health");
        return "/admin/monitor/health";
    }

    @RequestMapping("/admin/monitor/metrics")
    public String metrics(Model model) {

        model.addAttribute("metrics", "/metrics");
        return "/admin/monitor/metrics";
    }
}

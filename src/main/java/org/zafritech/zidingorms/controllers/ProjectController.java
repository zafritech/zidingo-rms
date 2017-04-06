/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.FolderRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.services.FolderService;

/**
 *
 * @author LukeS
 */
@Controller
public class ProjectController {
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private FolderService folderService;
    
    @RequestMapping("/projects")
    public String projects(Model model) {

        List<Project> projects = (List<Project>) projectRepository.findAll();

        model.addAttribute("projects", projects);

        return "/views/projects/index";
    }
    
    @RequestMapping("/projects/{id}")
    public String getProject(@PathVariable Long id, Model model) {

//        Project project = projectRepository.findOne(folderRepository.findOne(id).getProject().getId());
        Project project = projectRepository.findOne(id);

        model.addAttribute("project", project);
        model.addAttribute("artifacts", artifactRepository.findByArtifactProject(project));
        model.addAttribute("folder", folderRepository.findOne(folderService.getProjectFolder(id).getId())); 

        return "/views/projects/project";
    }
    
    @RequestMapping("/projects/{id}/{folderId}")
    public String getProjectFolder(@PathVariable Long id, @PathVariable Long folderId, Model model) {
        
        Project project = projectRepository.findOne(folderRepository.findOne(id).getProject().getId());

        model.addAttribute("project", project);
        model.addAttribute("artifacts", artifactRepository.findByArtifactFolder(folderRepository.findOne(folderId)));
        model.addAttribute("folder", folderRepository.findOne(folderId));
        
        return "/views/projects/project";
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.projects;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zafritech.zidingorms.database.domain.Company;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.CompanyRepository;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.database.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private FolderService folderService;
    
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @RequestMapping("/projects")
    public String projects(Model model) {

        List<Project> projects = (List<Project>) projectRepository.findAll();

        model.addAttribute("projects", projects);

        return "/views/projects/index";
    }
    
    @RequestMapping("/projects/{uuid}")
    public String getProject(@PathVariable String uuid, Model model) {

        Project project = projectRepository.findByUuId(uuid); 

        model.addAttribute("project", project);
        model.addAttribute("artifacts", artifactRepository.findByArtifactProject(project));
        model.addAttribute("folder", folderRepository.findOne(folderService.getProjectFolder(project.getId()).getId())); 

        return "/views/projects/project";
    }
    
    @RequestMapping("/projects/{uuid}/{folderId}")
    public String getProjectFolder(@PathVariable String uuid, @PathVariable Long folderId, Model model) {
        
        Project project = projectRepository.findByUuId(uuid);

        model.addAttribute("project", project);
        model.addAttribute("artifacts", artifactRepository.findByArtifactFolder(folderRepository.findOne(folderId)));
        model.addAttribute("folder", folderRepository.findOne(folderId));
        
        return "/views/projects/project";
    }
    
    @RequestMapping("/projects/details/{uuid}")
    public String getProjectDetails(@PathVariable String uuid, Model model) {
        
        Project project = projectRepository.findByUuId(uuid);
        Company company = companyRepository.findOne(project.getProjectCompany().getId());
        List<ItemCategory> categories = projectService.getProjectItemCategories(uuid);

        model.addAttribute("project", project);
        model.addAttribute("company", company);
        model.addAttribute("categories", categories);
        
        return "/views/projects/details";
    }
    
    @RequestMapping("/projects/categories/{uuid}")
    public String getArtifactCategories(@PathVariable String uuid, 
                                        @RequestParam(name = "s", defaultValue = "8") int pageSize,
                                        @RequestParam(name = "p", defaultValue = "1") int pageNumber,
                                        Model model) {
        
        Project project = projectRepository.findByUuId(uuid);
        Company company = companyRepository.findOne(project.getProjectCompany().getId());
        
        List<ItemCategory> categories = projectService.listByProjectByPage(uuid, pageSize, pageNumber);
        
        Integer categoriesCount = itemCategoryRepository.findByProjectOrderByCategoryNameAsc(project).size();
        Integer pageCount = (int)Math.ceil((float)(categoriesCount / pageSize)) + 1;
        
        List<Integer> pageList = projectService.getPagesList(pageNumber, pageCount);

        model.addAttribute("project", project);
        model.addAttribute("company", company);
        model.addAttribute("categories", categories);
        model.addAttribute("itemCount", categoriesCount);
        model.addAttribute("page", pageNumber);
        model.addAttribute("size", pageSize);
        model.addAttribute("list", pageList);
        model.addAttribute("count", pageCount);
        model.addAttribute("last", Collections.max(pageList));
        
        return "/views/projects/categories";
    }
}

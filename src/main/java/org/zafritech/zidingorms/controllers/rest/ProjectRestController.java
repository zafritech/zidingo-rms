package org.zafritech.zidingorms.controllers.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.dao.CategDao;
import org.zafritech.zidingorms.dao.FolderDao;
import org.zafritech.zidingorms.dao.ProjectTreeDao;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.services.ProjectService;
import org.zafritech.zidingorms.services.impl.ProjectServiceImpl;

@RestController
public class ProjectRestController {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    private ProjectService projectService;

    @Autowired
    public void setProjectService(ProjectServiceImpl projectService) {

        this.projectService = projectService;
    }

    @RequestMapping(value = "/api/projects", method = RequestMethod.GET)
    public List<Project> getProjects() {

        List<Project> projects = projectService.listProjects();

        return projects;
    }

    @RequestMapping(value = "/api/folders/{id}", method = RequestMethod.GET)
    public List<ProjectTreeDao> getProjectTree(@PathVariable(value = "id") Long id) {

        List<ProjectTreeDao> tree = new ArrayList<>();

        Project project = projectService.findById(id);

        List<ProjectTreeDao> folders = projectService.getProjectFolders(project);
        List<ProjectTreeDao> artifacts = projectService.getProjectArtifacts(project);

        tree.addAll(folders);
        tree.addAll(artifacts);

        return tree;
    }

    @RequestMapping(value = "/api/project/{id}", method = RequestMethod.GET)
    public ResponseEntity<Project> getProjectUuId(@PathVariable(value = "id") Long id) {

        Project project = projectRepository.findOne(id);
        
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/projects/list", method = RequestMethod.GET)
    public List<Project> getProjectsList() {

        List<Project> projects = projectService.listProjects();

        return projects;
    }
    
    
    @RequestMapping(value = "/api/project/folder/create", method = RequestMethod.POST)
    public Folder getCreateFolder(@RequestBody FolderDao folderDao) {

        return projectService.createFolder(folderDao);
    }
    
    @RequestMapping(value="/api/categories/list/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ItemCategory>> getProjectCategoriesList(@PathVariable(value = "id") Long id) {
        
        List<ItemCategory> categories = projectService.getProjectItemCategories(projectRepository.findOne(id).getUuId());
        
        return new ResponseEntity<List<ItemCategory>>(categories, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/project/category/create", method = RequestMethod.POST)
    public ItemCategory getCreateItemCategory(@RequestBody CategDao catDao) {

        return projectService.createItemCategory(catDao);
    }
}

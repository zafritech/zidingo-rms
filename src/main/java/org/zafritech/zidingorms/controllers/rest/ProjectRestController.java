package org.zafritech.zidingorms.controllers.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.dao.ProjectTreeDao;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.services.ProjectService;
import org.zafritech.zidingorms.services.impl.ProjectServiceImpl;

@RestController
public class ProjectRestController {

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
}

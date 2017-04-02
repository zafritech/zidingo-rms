/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.FolderRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ArtifactService;

/**
 *
 * @author LukeS
 */
@Controller
public class ApplicationController {

    private String UPLOAD_FOLDER = "F://Projects//Uploads//";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private SystemVariableRepository sysVarRepository;
    
    public void setArtifactRepository(ArtifactRepository artifactRepository) {

        this.artifactRepository = artifactRepository;
    }

    @RequestMapping("/projects")
    public String projects(Model model) {

        List<Project> projects = (List<Project>) projectRepository.findAll();

        model.addAttribute("projects", projects);

        return "/views/projects/index";
    }

    @RequestMapping("/artifacts")
    public String artifacts(Model model) {

        List<Artifact> artifacts = (List<Artifact>) artifactRepository.findAll();

        model.addAttribute("artifacts", artifacts);

        return "/views/artifacts/index";
    }

    @RequestMapping("/projects/{id}")
    public String getProject(@PathVariable Long id, Model model) {

        Project project = projectRepository.findOne(folderRepository.findOne(id).getProject().getId());
        
        model.addAttribute("project", project);
        model.addAttribute("artifacts", artifactRepository.findByArtifactProject(project));

        return "/views/projects/project";
    }

    @RequestMapping("/artifacts/{id}")
    public String getArtifact(@PathVariable Long id, Model model) {

        model.addAttribute("artifact", artifactRepository.findOne(id));
        model.addAttribute("items", itemRepository.findByArtifactIdOrderBySortIndexAsc(id));

        return "/views/artifacts/artifact";
    }

    @RequestMapping("/artifacts/metadata/{id}")
    public String getArtifactMetaData(@PathVariable Long id, Model model) {
        
        Artifact artifact = artifactRepository.findOne(id);
        List<SystemVariable> sysIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
        List<SystemVariable> reqIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name());
        List<SystemVariable> sysNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name());
        List<SystemVariable> reqNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name());
        
        if (sysIdents == null) {
            
            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            sysIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
        }
        
        if (reqIdents == null) {
            
            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            reqIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name());
        }
        
        if (sysNumDigits == null) {
            
            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            sysNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name());
        }
        
        if (reqNumDigits == null) {
            
            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            reqNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name());
        }
        
        model.addAttribute("artifact", artifact);
        model.addAttribute("itemident", sysIdents.get(0).getVariableValue());
        model.addAttribute("requirementIds", reqIdents);
        model.addAttribute("templateDigits", sysNumDigits.get(0).getVariableValue());
        model.addAttribute("reqIdDigits", reqNumDigits.get(0).getVariableValue());
        
        return "/views/artifacts/metadata";
    }
    
    @PostMapping("/excel/import")
    public String importFromExcel(@RequestParam("file") MultipartFile file,
            @RequestParam("artifactId") Long id,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {

            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            byte[] bytes = file.getBytes();

            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            artifactService.importExcel(id, path.toString());

            File excelFile = new File(path.toString());
            excelFile.delete();

        } catch (IOException e) {
        }

        return "redirect:/artifacts/" + id;
    }
}

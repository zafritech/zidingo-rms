/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.zafritech.zidingorms.database.domain.ApplicationSession;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.projects.FolderService;

/**
 *
 * @author LukeS
 */
@RestController
public class FolderRestController {
 
    @Autowired
    ApplicationSession appSession;
            
    @Autowired
    private FolderService folderService;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @RequestMapping(value = "/api/projectfolders/{id}", method = GET)
    public ResponseEntity<List<Folder>> getProjectFolders(@PathVariable(value = "id") Long id) {
        
        List<Folder> folders = folderService.getByProjectId(id);
        
        return new ResponseEntity<List<Folder>>(folders, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/api/folders/select/{id}", method = RequestMethod.GET)
    public ResponseEntity<Folder> selectFolder(@PathVariable(value = "id") Long id) {
        
        appSession.setFolderId(id);
        Folder folder = folderRepository.findOne(id);

        return new ResponseEntity<Folder>(folder, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/folders/selected", method = RequestMethod.GET)
    public ResponseEntity<Folder> getSelectedProject() {
        
        Folder folder = new Folder();
        
        Long folderId = appSession.getFolderId();
        
        if (folderId != null) {
        
            folder = folderRepository.findOne(folderId);
            
        } else {
            
            folder = null;
        }
        
        return new ResponseEntity<Folder>(folder, HttpStatus.OK);
    }
}

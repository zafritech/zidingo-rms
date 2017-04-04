/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.services.FolderService;

/**
 *
 * @author LukeS
 */
@RestController
public class FolderController {
    
    @Autowired
    private FolderService folderService;
    
    @RequestMapping(value = "/api/projectfolders/{id}", method = GET)
    public ResponseEntity<List<Folder>> getProjectFolders(@PathVariable(value = "id") Long id) {
        
        List<Folder> folders = folderService.getByProjectId(id);
        
        return new ResponseEntity<List<Folder>>(folders, HttpStatus.OK);
    }
}

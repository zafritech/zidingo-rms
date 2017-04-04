/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.repositories.FolderRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.services.FolderService;

/**
 *
 * @author LukeS
 */
@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;
    
    @Override
    public List<Folder> getByProjectId(Long id) {

        Project project = projectRepository.findOne(id);
        
        return folderRepository.findByProject(project);
    }
}

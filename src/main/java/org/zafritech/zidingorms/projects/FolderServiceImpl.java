/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.projects;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.FolderType;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;

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
    
    @Override
    public Folder getProjectFolder(Long id) {
        
        Project project = projectRepository.findOne(id);
        
        return folderRepository.findFirstByProjectAndFolderTypeOrderByFolderName(project, FolderType.PROJECT.name());
    }
}

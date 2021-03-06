/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.Project;

/**
 *
 * @author LukeS
 */
public interface FolderRepository extends CrudRepository<Folder, Long> {

    Folder findByFolderName(String folder);

    List<Folder> findByProject(Project project);
    
    Folder findFirstByProjectAndFolderTypeOrderByFolderName(Project project, String type);
}

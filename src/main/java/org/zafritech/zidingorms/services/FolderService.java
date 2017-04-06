/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.util.List;
import org.zafritech.zidingorms.domain.Folder;

/**
 *
 * @author LukeS
 */
public interface FolderService {
    
    List<Folder> getByProjectId(Long id);
    
    Folder getProjectFolder(Long id);
}

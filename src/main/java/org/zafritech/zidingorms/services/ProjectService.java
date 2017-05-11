package org.zafritech.zidingorms.services;

import java.util.List;
import org.zafritech.zidingorms.dao.CategDao;
import org.zafritech.zidingorms.dao.DisciplineDao;
import org.zafritech.zidingorms.dao.FolderDao;

import org.zafritech.zidingorms.dao.ProjectTreeDao;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.domain.ItemCategory;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.domain.User;

public interface ProjectService {

    Project findById(Long id);

    List<Project> listProjects();

    List<ProjectTreeDao> getProjectFolders(Project project);

    List<ProjectTreeDao> getProjectArtifacts(Project project);

    Folder createFolder(FolderDao folderDao);
    
    List<ItemCategory> getProjectItemCategories(String uuid);
    
    List<ItemCategory> listByProjectByPage(String uuid, int pageSize, int pageNumber);
    
    ItemCategory createItemCategory(CategDao catDao);
    
    List<DisciplineDao> getDisciplinesData(User user);
    
    List<Integer> getPagesList(int currentPage, int lastPage);
}

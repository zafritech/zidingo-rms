package org.zafritech.zidingorms.projects;

import java.util.List;
import org.zafritech.zidingorms.database.dao.CategDao;
import org.zafritech.zidingorms.database.dao.DisciplineDao;
import org.zafritech.zidingorms.database.dao.FolderDao;
import org.zafritech.zidingorms.database.dao.ProjectTreeDao;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.User;

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

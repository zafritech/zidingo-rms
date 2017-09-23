/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.projects;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.FolderType;
import org.zafritech.zidingorms.database.dao.CategDao;
import org.zafritech.zidingorms.database.dao.DisciplineDao;
import org.zafritech.zidingorms.database.dao.FolderDao;
import org.zafritech.zidingorms.database.dao.ProjectTreeDao;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Company;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.database.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;

/**
 *
 * @author LukeS
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    private final static int PAGESIZE = 3;
   
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public Project create(String name, String shortName, Company company) {

        Project project = projectRepository.save(new Project(name, shortName, company));

        return project;
    }

    @Override
    public List<Project> listProjects() {

        List<Project> projects = new ArrayList<>();
        Iterable<Project> set = projectRepository.findAll();

        for (Project project : set) {

            projects.add(project);
        }

        return projects;
    }

    @Override
    public List<ProjectTreeDao> getProjectFolders(Project project) {

        List<Folder> folders = folderRepository.findByProject(project);

        List<ProjectTreeDao> projectTree = new ArrayList<>();

        for (Folder folder : folders) {

            if (folder.getFolderType().equals(FolderType.PROJECT.name())) {
            
                projectTree.add(new ProjectTreeDao(
                        
                        folder.getId(),
                        (folder.getParent() == null) ? 0L : folder.getParent().getId(),
                        folder.getFolderName(),
                        (folder.getParent() == null),
                        true,
                        true,
                        project.getId()));
                
            } else {
            
                projectTree.add(new ProjectTreeDao(
                        
                        folder.getId(),
                        (folder.getParent() == null) ? 0L : folder.getParent().getId(),
                        folder.getFolderName(),
                        (folder.getParent() == null),
                        true,
                        true));
            
            }

        }

        return projectTree;
    }

    @Override
    public List<ProjectTreeDao> getProjectArtifacts(Project project) {

        List<Artifact> docs = artifactRepository.findByArtifactProject(project);

        List<ProjectTreeDao> projectDocs = new ArrayList<>();

        for (Artifact doc : docs) {

            projectDocs.add(new ProjectTreeDao(doc.getId() + 5000, // Prevent TreeNodes id classes with folders
                    doc.getArtifactFolder().getId(),
                    doc.getArtifactName(),
                    false,
                    false,
                    true,
                    doc.getId()));
        }

        return projectDocs;
    }

    @Override
    public Project findById(Long id) {

        return projectRepository.findOne(id);
    }

    @Override
    public Folder createFolder(FolderDao folderDao) {

        Folder folder = new Folder();
        
        if (folderDao.getParentId() == 0) {
            
            folder = new Folder(folderDao.getFolderName(),
                                folderDao.getFolderType(),
                                folderRepository.findOne(folderDao.getParentId()),
                                projectRepository.findOne(folderDao.getProjectId()));

        } else {
            
            folder = new Folder(folderDao.getFolderName(),
                                folderDao.getFolderType(),
                                projectRepository.findOne(folderDao.getProjectId()));
        }
        
        folderRepository.save(folder);
        
        return folder;
    }

    @Override
    public List<ItemCategory> getProjectItemCategories(String uuid) {

        Project project = projectRepository.findByUuId(uuid);
        
        List<ItemCategory> categories = itemCategoryRepository.findByProjectOrderByCategoryNameAsc(project);
        
        return categories;
    }

    @Override
    public ItemCategory createItemCategory(CategDao catDao) {

        ItemCategory category = new ItemCategory(catDao.getCategoryName(),
                                                 catDao.getCategoryCode(),
                                                 userRepository.getByUuId(catDao.getCategoryLeadUuId()),
                                                 projectRepository.findOne(catDao.getProjectId()));
        
        return itemCategoryRepository.save(category);
    } 

    @Override
    public List<ItemCategory> listByProjectByPage(String uuid, int pageSize, int pageNumber) {

        Project project = projectRepository.findByUuId(uuid);
        PageRequest request = new PageRequest(pageNumber - 1, pageSize);
        
        return itemCategoryRepository.findByProjectOrderByCategoryNameAsc(request, project);
    }
    
    @Override
    public  List<Integer> getPagesList(int currentPage, int lastPage) {
        
        List<Integer> pageList = new ArrayList<>();

        int startIndex = 1;
        int upperLimit = 1;
        
        if (lastPage < 9) {
            
            startIndex = 1;
            upperLimit = lastPage;
            
        } else {

            upperLimit = ((int) Math.ceil((double)currentPage / 9) * 9);
            upperLimit = (lastPage < upperLimit) ? lastPage : upperLimit;
            startIndex = upperLimit - 8;
        
        }
        
        for (int i = startIndex; i <= upperLimit; i++) {

            pageList.add(i);
        }
        
        return pageList;
    }

    @Override
    public List<DisciplineDao> getDisciplinesData(User user) {

        List<ItemCategory> categories = itemCategoryRepository.findByLead(user);
        List<DisciplineDao> daos = new ArrayList();
        
        for (ItemCategory cat : categories) {
            
            DisciplineDao dao = new DisciplineDao();
            
            dao.setId(cat.getId()); 
            dao.setUuId(cat.getUuId()); 
            dao.setProjectId(cat.getProject().getUuId()); 
            dao.setCategoryCode(cat.getCategoryCode());
            dao.setCategoryName(cat.getCategoryName());
            dao.setItemCount(itemRepository.findByItemCategory(cat).size()); 
            
            // Exclude empty disciplines/departments
            if (dao.getItemCount() > 0) {
            
                daos.add(dao);
            }
        }
        
        return daos;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.core.loader.DataInitializer;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ArtifactTypeRepository;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;

/**
 *
 * @author LukeS
 */
@Component
public class StarterCLR implements CommandLineRunner {
    
    @Autowired
    private DataInitializer dataInitializer;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private ArtifactTypeRepository artifactTypeRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Override
    public void run(String... strings) throws Exception {

//        InitializeData();
//        moveFolder();
//        moveDocument();
//        createDocument();
    }
    
    private void InitializeData() {
        
        dataInitializer.initializeRoles();
        dataInitializer.initializeUsers();
        dataInitializer.initializeItemTypes();
        dataInitializer.initializeLinkTypes();
        dataInitializer.initializeArtifactTypes();
        dataInitializer.initializeProjects();
        dataInitializer.initializeSystemVariable();
        dataInitializer.initializeVerificationMethods();
    }
    
    private void moveFolder() {
        
        Folder folder = folderRepository.findOne(6L);
        Folder parent = folderRepository.findOne(1L);
        
        folder.setParent(parent);
        folderRepository.save(folder);
    }
    
    private void moveDocument() {
        
        Folder folder = folderRepository.findOne(14L);
        Artifact artifact1 = artifactRepository.findOne(4L);
        Artifact artifact2 = artifactRepository.findOne(5L);
        
        artifact1.setArtifactFolder(folder);
        artifactRepository.save(artifact1);
        
        artifact2.setArtifactFolder(folder);
        artifactRepository.save(artifact2);
    }
    
//    private void createDocument() {
//        
//        Project project = projectRepository.findOne(1L);
//        Folder folder = folderRepository.findOne(12L);
//        
//        Artifact artifact = artifactRepository.save(new Artifact("XC08100100-CR18", 
//                                                                 "Change Request 18", "CR 18 - Change Request #18", 
//                                                                 artifactTypeRepository.findByArtifactTypeName("GEN"), 
//                                                                 project, 
//                                                                 folder));
//        
//        for (int i = 7436; i < 7438; i++) {
//            
//            Item item = itemRepository.findOne(Long.valueOf(i));
//            item.setArtifact(artifact);
//            itemRepository.save(item);
//        }
//    }
}

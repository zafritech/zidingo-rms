/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.daos.ProjectTreeDao;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Company;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.FolderRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.services.ProjectService;

/**
 *
 * @author LukeS
 */
@Service
public class ProjectServiceImpl implements ProjectService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

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

            projectTree.add(new ProjectTreeDao(folder.getId(),
                    (folder.getParent() == null) ? 0L : folder.getParent().getId(),
                    folder.getFolderName(),
                    (folder.getParent() == null),
                    true,
                    false));

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
}

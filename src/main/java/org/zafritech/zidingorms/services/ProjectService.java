package org.zafritech.zidingorms.services;

import java.util.List;

import org.zafritech.zidingorms.dao.ProjectTreeDao;
import org.zafritech.zidingorms.domain.Project;

public interface ProjectService {

    Project findById(Long id);

    List<Project> listProjects();

    List<ProjectTreeDao> getProjectFolders(Project project);

    List<ProjectTreeDao> getProjectArtifacts(Project project);

}

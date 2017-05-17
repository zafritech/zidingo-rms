/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author LukeS
 */
@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApplicationSession implements Serializable{
    
    private Long projectId;
    
    private String project;
    
    private Long folderId;
    
    private Long artifact;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getArtifact() {
        return artifact;
    }

    public void setArtifact(Long artifact) {
        this.artifact = artifact;
    }
}

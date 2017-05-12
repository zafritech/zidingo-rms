/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.database.dao.LinkDao;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Link;
import org.zafritech.zidingorms.database.domain.LinkType;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.LinkTypeRepository;
import org.zafritech.zidingorms.database.repositories.ProjectRepository;
import org.zafritech.zidingorms.items.services.LinkService;

/**
 *
 * @author LukeS
 */
@RestController
public class LinkRestController {
   
     @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private LinkTypeRepository linkTypeRepository;
    
    @Autowired
    private LinkService linkService;
            
    @RequestMapping(value = "/api/link/artifacts/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Artifact>> getArtifacts(@PathVariable(value = "id") Long id) {
        
        Project project = projectRepository.findOne(id);
        List<Artifact> artifacts = artifactRepository.findByArtifactProject(project);
        
        return new ResponseEntity<List<Artifact>>(artifacts, HttpStatus.OK);
    }
            
    @RequestMapping(value = "/api/link/linktypes", method = RequestMethod.GET)
    public ResponseEntity<List<LinkType>> getLinkTypes() {
        
        List<LinkType> linkType = linkTypeRepository.findAllByOrderByLinkTypeName();
        
        return new ResponseEntity<List<LinkType>>(linkType, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/link/new", method = RequestMethod.POST)
    public ResponseEntity<Link> newItem(@RequestBody LinkDao linkDao) {

        Link link = linkService.saveNewDao(linkDao);

        return new ResponseEntity<Link>(link, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/link/{id}", method = RequestMethod.GET)
    public ResponseEntity<Link> getLink(@PathVariable(value = "id") Long id) {
        
        Link link = linkService.findOne(id);
        
        return new ResponseEntity<Link>(link, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/link/delete/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> deleteLink(@PathVariable(value = "id") Long id) {
        
        Long linkId = linkService.deleteLink(id);
        
        return new ResponseEntity<Long>(linkId, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/link/links/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Link>> getLinksList(@PathVariable(value = "id") Long id) {
        
        List<Link> links = linkService.findItemLinks(id);
        
        return new ResponseEntity<List<Link>>(links, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/link/incominglinks/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> getIncomigLinksCount(@PathVariable(value = "id") Long id) {
        
        Long linkCount = linkService.getIncomingItemLinksCount(id);
        
        return new ResponseEntity<Long>(linkCount, HttpStatus.OK);
    }
}

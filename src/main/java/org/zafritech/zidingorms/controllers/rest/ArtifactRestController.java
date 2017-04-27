/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.commons.enums.ArtifactStatus;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.ArtifactType;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ArtifactTypeRepository;
import org.zafritech.zidingorms.services.ItemService;

/**
 *
 * @author LukeS
 */
@RestController
public class ArtifactRestController {
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ArtifactTypeRepository artifactTypeRepository;
    
    @Autowired
    private ItemService itemService;
    
    @RequestMapping(value = "/api/artifacts/statuslist", method = RequestMethod.GET)
    public List<ArtifactStatus> getArtifactStatuses() {

        return Arrays.asList(ArtifactStatus.values());
    }
    
    @RequestMapping(value = "/api/artifact/{id}", method = RequestMethod.GET)
    public ResponseEntity<Artifact> getArtifact(@PathVariable(value = "id") Long id) {

        Artifact artifact = artifactRepository.findOne(id);
        
       return new ResponseEntity<Artifact>(artifact, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/artifact/types", method = RequestMethod.GET)
    public ResponseEntity<List<ArtifactType>> getArtifact() {

        List<ArtifactType> artifactTypes = artifactTypeRepository.findAll();
        
        return new ResponseEntity<List<ArtifactType>>(artifactTypes, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/artifact/requirements/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> getArtifactRequirements(@PathVariable(value = "id") Long id) {

        List<Item> requirements = itemService.findRequirements(id);
        
        return new ResponseEntity<List<Item>>(requirements, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/api/artifacts/list", method = RequestMethod.GET)
    public ResponseEntity<List<Artifact>> getArtifactsList() {

        Iterable<Artifact> artifactsIter = artifactRepository.findAll();
        List<Artifact> artifacts = new ArrayList<>();
        artifactsIter.forEach(artifacts::add);
        
       return new ResponseEntity<List<Artifact>>(artifacts, HttpStatus.OK);
    }
}

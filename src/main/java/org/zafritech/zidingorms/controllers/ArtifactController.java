/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.dao.SearchDao;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.Link;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.search.ItemSearch;
import org.zafritech.zidingorms.services.ArtifactService;
import org.zafritech.zidingorms.services.ItemService;
import org.zafritech.zidingorms.services.LinkService;

/**
 *
 * @author LukeS
 */
@Controller
public class ArtifactController {
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private ArtifactService artifactService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private SystemVariableRepository sysVarRepository;
    
    @Autowired
    private LinkService linkService;
    
    public void setArtifactRepository(ArtifactRepository artifactRepository) {

        this.artifactRepository = artifactRepository;
    }
    
    // Inject the UserSearch object
    @Autowired
    private ItemSearch itemSearch;

    @RequestMapping("/artifacts")
    public String artifacts(Model model) {

        List<Artifact> artifacts = (List<Artifact>) artifactRepository.findAll();

        model.addAttribute("artifacts", artifacts);

        return "/views/artifacts/index";
    }

    @RequestMapping("/artifacts/{id}")
    public String getArtifact(@PathVariable(value = "id") Long id,
                              @RequestParam(defaultValue = "1", value = "page", required=false) Integer  page,
                              @RequestParam(defaultValue = "25", value = "size", required=false) Integer size,
                              @RequestParam(defaultValue = "", value = "q", required=false) String q,
                              Model model) {
        
        int pageCount = (int)Math.ceil((float)(itemService.getNumberOfItems(id) / size)) + 1;
        int currentPage = (page > pageCount) ? pageCount : page;

        List<Item> items = itemService.getPagedRequirements(id, size, currentPage);
        List<Integer> pageList = itemService.getPagesList(currentPage, pageCount);
                
        model.addAttribute("artifact", artifactRepository.findOne(id));
        model.addAttribute("items", items);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("size", size);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("pageList", pageList);
        model.addAttribute("lastDisplayed", Collections.max(pageList));
        model.addAttribute("newLineChar", "\n");
        if (!q.isEmpty()) { model.addAttribute("q", q); }

        return "/views/artifacts/artifact";
    }

    @RequestMapping("/artifacts/metadata/{id}")
    public String getArtifactMetaData(@PathVariable Long id, Model model) {

        Artifact artifact = artifactRepository.findOne(id);
        List<SystemVariable> sysIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
        List<SystemVariable> reqIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name());
        List<SystemVariable> sysNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name());
        List<SystemVariable> reqNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name());

        if (sysIdents == null) {

            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            sysIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
        }

        if (reqIdents == null) {

            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            reqIdents = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name());
        }

        if (sysNumDigits == null) {

            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            sysNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name());
        }

        if (reqNumDigits == null) {

            artifactService.initializeArtifact(id, SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
            reqNumDigits = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name());
        }

        model.addAttribute("artifact", artifact);
        model.addAttribute("itemident", sysIdents.get(0).getVariableValue());
        model.addAttribute("requirementIds", reqIdents);
        model.addAttribute("templateDigits", sysNumDigits.get(0).getVariableValue());
        model.addAttribute("reqIdDigits", reqNumDigits.get(0).getVariableValue());

        return "/views/artifacts/metadata";
    }
    
    @RequestMapping("/items/links/{id}")
    public String manageItemLinks(@PathVariable Long id, Model model) {

        Item item = itemRepository.findOne(id); 
        List<Link> links = linkService.findItemLinks(id);
       
        model.addAttribute("item", item);
        model.addAttribute("links", links);
        model.addAttribute("newLineChar", "\n");

        return "/views/item/links";
    }
    
    @RequestMapping("/artifact/items/search")
    public String searchItems(String q, Long size, Long page, Model model) {
        
        SearchDao searchResults = null;
        
        try {
            
            searchResults = itemSearch.search(q, size, page);
          
        } catch (Exception ex) {
            
          // here you should handle unexpected errors
          // ...
          // throw ex;
        }
        
        model.addAttribute("results", searchResults);
        model.addAttribute("string", q.replace(" ", "+"));
        
        return "/views/artifacts/search";
    }
    
    @RequestMapping("/artifact/search/result/{id}")
    public String itemSearchResult(@PathVariable(value = "id") Long id,
                                    @RequestParam(value = "item", required=false) Long item,
                                    @RequestParam(defaultValue = "25", value = "size", required=false) Integer size,
                                    @RequestParam(defaultValue = "", value = "q", required=false) String q) {
         
        Integer page = itemService.getPageWithItem(id, item, size);
        
        String queryString = (!q.isEmpty()) ? "&q=" + q : "";
        
        String url = "/artifacts/" + id + "?size=" + size + "&page=" + page + queryString;
         
          return "redirect:" + url;
     }
}

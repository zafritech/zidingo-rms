/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.LinkDao;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.Link;
import org.zafritech.zidingorms.database.domain.LinkType;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.LinkRepository;
import org.zafritech.zidingorms.database.repositories.LinkTypeRepository;
import org.zafritech.zidingorms.items.services.LinkService;

/**
 *
 * @author LukeS
 */
@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private LinkRepository linkRepository;
    
    @Autowired
    private LinkTypeRepository linkTypeRepository;
    
    @Override
    public Link saveNewDao(LinkDao linkDao) {
        
        Artifact srcArtifact = artifactRepository.findOne(linkDao.getSrcArtifactId());
        Artifact dstArtifact = artifactRepository.findOne(linkDao.getDstArtifactId());
        
        Item srcItem = itemRepository.findOne(linkDao.getSrcItemId());
        Item dstItem = itemRepository.findOne(linkDao.getDstItemId());
        
        LinkType linkType = linkTypeRepository.findOne(linkDao.getLinkTypeId());
        
        Link link = linkRepository.save(new Link(srcItem, srcArtifact, dstItem, dstArtifact, linkType));
        updateItemLinkCount(srcItem);
        updateItemLinkCount(dstItem);
        
        return link;
    }
    
    private void updateItemLinkCount(Item item) {
        
        int newLinkCount = item.getLinkCount() + 1;
        item.setLinkCount(newLinkCount);
        itemRepository.save(item);
    }

    @Override 
    public Link findOne(Long id) {
       
        return linkRepository.findOne(id); 
    }
    
    @Override
    public Long deleteLink(Long id) {
        
        Link link = linkRepository.findOne(id);
        Item srcItem = itemRepository.findOne(link.getSrcItem().getId());
        Item dstItem = itemRepository.findOne(link.getDstItem().getId());
        
        linkRepository.delete(id);
        
        srcItem.setLinkCount(srcItem.getLinkCount() - 1);
        itemRepository.save(srcItem);
        
        dstItem.setLinkCount(dstItem.getLinkCount() - 1);
        itemRepository.save(dstItem);
        
        return id;
    }
    
    @Override
    public List<Link> findItemLinks(Long id) {

        ArrayList<Link> arrayList = new ArrayList<Link>();
        List<Link> srcLinks = linkRepository.findBySrcItem(itemRepository.findOne(id));
        List<Link> dstLinks = linkRepository.findByDstItem(itemRepository.findOne(id));
        
        for (Link link : srcLinks) {
            
            arrayList.add(link);
        }
        
        for (Link link : dstLinks) {
            
            arrayList.add(link);
        }
        
        Collections.sort(arrayList);

        return arrayList; 
    }

    @Override
    public Long getIncomingItemLinksCount(Long id) {
        
        List<Link> dstLinks = linkRepository.findByDstItem(itemRepository.findOne(id));
        
        return new Long(dstLinks.size());
    }
}

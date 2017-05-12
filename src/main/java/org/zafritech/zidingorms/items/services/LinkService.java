/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.items.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.LinkDao;
import org.zafritech.zidingorms.database.domain.Link;

/**
 *
 * @author LukeS
 */
@Service
public interface LinkService {
    
    Link saveNewDao(LinkDao linkDao);
    
    Link findOne(Long id);
    
    Long deleteLink(Long id);
    
    List<Link> findItemLinks(Long id);
    
    Long getIncomingItemLinksCount(Long id);
}
